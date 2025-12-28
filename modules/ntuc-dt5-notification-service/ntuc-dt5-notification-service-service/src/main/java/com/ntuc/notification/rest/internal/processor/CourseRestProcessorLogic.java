package com.ntuc.notification.rest.internal.processor;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.audit.util.AuditEventFactory;
import com.ntuc.notification.audit.util.MdcUtil;
import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.model.CourseEventList;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.onetime.api.OneTimeLoadFacade;
import com.ntuc.notification.rest.internal.dto.CourseNotificationAckResponse;
import com.ntuc.notification.rest.internal.dto.CourseNotificationAckResponse.ErrorItem;
import com.ntuc.notification.rest.internal.dto.CourseRestDtos;
import com.ntuc.notification.rest.internal.processor.context.NotificationExecutorProvider;
import com.ntuc.notification.rest.internal.processor.context.RequestContext;
import com.ntuc.notification.rest.internal.processor.context.RequestContextProvider;
import com.ntuc.notification.rest.internal.processor.validation.CourseNotificationRequestValidator;
import com.ntuc.notification.service.ClsCourseFieldsProcessor;
import com.ntuc.notification.service.NtucSBLocalService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.ws.rs.core.Response;

/**
 * Plain Java logic for Course REST processing.
 *
 * MUST:
 * - Validate request synchronously and respond immediately (ISD).
 * - Persist every accepted notification into NtucSB BEFORE async dispatch.
 * - canRetry MUST always remain true (enforced at LocalService layer too).
 * - Persist audit as the single source of truth.
 *
 * Partial success rule:
 * - Invalid events must NOT block valid events.
 * - status:
 *   - failed: 0 valid events
 *   - partial_success: some valid, some invalid
 *   - success: all valid
 */
public class CourseRestProcessorLogic {

    private final NtucSBLocalService ntucSBLocalService;
    private final CounterLocalService counterLocalService;
    private final ClsCourseFieldsProcessor clsCourseFieldsProcessor;
    private final AuditEventWriter auditEventWriter;
    private final OneTimeLoadFacade oneTimeLoadFacade;
    private final RequestContextProvider requestContextProvider;
    private final NotificationExecutorProvider executorProvider;

    private final CourseNotificationRequestValidator requestValidator = new CourseNotificationRequestValidator();
    private final NotificationIntakePersister intakePersister;

    public CourseRestProcessorLogic(
            NtucSBLocalService ntucSBLocalService,
            CounterLocalService counterLocalService,
            ClsCourseFieldsProcessor clsCourseFieldsProcessor,
            AuditEventWriter auditEventWriter,
            OneTimeLoadFacade oneTimeLoadFacade,
            RequestContextProvider requestContextProvider,
            NotificationExecutorProvider executorProvider) {

        this.ntucSBLocalService = Objects.requireNonNull(ntucSBLocalService, "ntucSBLocalService");
        this.counterLocalService = Objects.requireNonNull(counterLocalService, "counterLocalService");
        this.clsCourseFieldsProcessor = Objects.requireNonNull(clsCourseFieldsProcessor, "clsCourseFieldsProcessor");
        this.auditEventWriter = Objects.requireNonNull(auditEventWriter, "auditEventWriter");
        this.oneTimeLoadFacade = Objects.requireNonNull(oneTimeLoadFacade, "oneTimeLoadFacade");
        this.requestContextProvider = Objects.requireNonNull(requestContextProvider, "requestContextProvider");
        this.executorProvider = Objects.requireNonNull(executorProvider, "executorProvider");

        this.intakePersister = new NotificationIntakePersister(this.ntucSBLocalService, this.counterLocalService);
    }

    public Response postCourse(CourseEventList wrapper) {
        final long startMs = System.currentTimeMillis();

        final String correlationId = newCorrelationId();
        final RequestContext baseCtx = requestContextProvider.currentWithCorrelation(correlationId);

        final String requestId = UUID.randomUUID().toString();
        final String jobRunId = String.valueOf(nextJobRunId());

        // -------------------------------------------------------------
        // Wrapper-level validation
        // -------------------------------------------------------------
        CourseNotificationRequestValidator.ValidationResult wrapperValidation = requestValidator.validate(wrapper);
        List<CourseEvent> allEvents = (wrapper == null) ? Collections.<CourseEvent>emptyList() : safeList(wrapper.getEvents());

        if (allEvents.isEmpty()) {
            List<ErrorItem> errors = toAckErrors(wrapperValidation.getEventErrors());

            auditEventWriter.write(
                    AuditEventFactory.end(
                            AuditCategory.DT5_FLOW,
                            AuditStep.REST_NOTIFY_BATCH,
                            AuditSeverity.WARNING,
                            baseCtx,
                            AuditStatus.FAILED,
                            "REST intake rejected: wrapper validation failed",
                            AuditErrorCode.VALIDATION_FAILED,
                            wrapperValidation.getMessage()
                    ).withDetail("errorCount", String.valueOf(errors.size()))
                     .withDetail("reason", "requestWrapperValidationFailed")
                     .withDetail("requestId", requestId)
                     .withDetail("jobRunId", jobRunId)
            );

            CourseNotificationAckResponse body =
                    CourseNotificationAckResponse.failed(wrapperValidation.getMessage(), errors);

            return Response.status(Response.Status.BAD_REQUEST)
                    .header("X-Correlation-Id", correlationId)
                    .header("X-Request-Id", requestId)
                    .header("X-Job-Run-Id", jobRunId)
                    .entity(body)
                    .build();
        }

        // -------------------------------------------------------------
        // Per-event validation (collect errors; continue for valid ones)
        // -------------------------------------------------------------
        List<CourseEvent> validEvents = new ArrayList<>();
        List<CourseNotificationRequestValidator.EventError> validationErrors = new ArrayList<>();

        for (CourseEvent e : allEvents) {
            List<CourseNotificationRequestValidator.EventError> errs = requestValidator.validateEvent(e);
            if (errs == null || errs.isEmpty()) {
                validEvents.add(e);
            } else {
                validationErrors.addAll(errs);
            }
        }

        if (validEvents.isEmpty()) {
            List<ErrorItem> errors = toAckErrors(validationErrors);

            auditEventWriter.write(
                    AuditEventFactory.end(
                            AuditCategory.DT5_FLOW,
                            AuditStep.REST_NOTIFY_BATCH,
                            AuditSeverity.WARNING,
                            baseCtx,
                            AuditStatus.FAILED,
                            "REST intake rejected: all events invalid",
                            AuditErrorCode.VALIDATION_FAILED,
                            "Invalid request parameter(s)"
                    ).withDetail("eventCount", String.valueOf(allEvents.size()))
                     .withDetail("validCount", "0")
                     .withDetail("invalidCount", String.valueOf(allEvents.size()))
                     .withDetail("errorCount", String.valueOf(errors.size()))
                     .withDetail("reason", "requestEventValidationFailed")
                     .withDetail("requestId", requestId)
                     .withDetail("jobRunId", jobRunId)
            );

            CourseNotificationAckResponse body =
                    CourseNotificationAckResponse.failed("Invalid request parameter(s)", errors);

            return Response.status(Response.Status.BAD_REQUEST)
                    .header("X-Correlation-Id", correlationId)
                    .header("X-Request-Id", requestId)
                    .header("X-Job-Run-Id", jobRunId)
                    .entity(body)
                    .build();
        }

        // -------------------------------------------------------------
        // Persist ONLY valid events
        // -------------------------------------------------------------
        List<Long> persistedIds = new ArrayList<>();
        for (CourseEvent raw : validEvents) {
            CourseEvent normalized = normalizeEvent(raw);

            NtucSB saved = intakePersister.persist(normalized, baseCtx.getCompanyId());

            normalized.setNtucSBId(saved.getNtucDTId());
            normalized.setNtucSB(saved);

            persistedIds.add(saved.getNtucDTId());
        }

        // -------------------------------------------------------------
        // Audit intake accepted (even for partial)
        // -------------------------------------------------------------
        auditEventWriter.write(
                AuditEventFactory.start(
                        AuditCategory.DT5_FLOW,
                        AuditStep.REST_NOTIFY_BATCH,
                        AuditSeverity.INFO,
                        baseCtx,
                        AuditStatus.STARTED,
                        (validationErrors.isEmpty() ? "REST intake accepted" : "REST intake accepted (partial)"),
                        AuditErrorCode.NONE
                ).withDetail("eventCount", String.valueOf(allEvents.size()))
                 .withDetail("validCount", String.valueOf(validEvents.size()))
                 .withDetail("invalidCount", String.valueOf(allEvents.size() - validEvents.size()))
                 .withDetail("persistedCount", String.valueOf(persistedIds.size()))
                 .withDetail("hasValidationErrors", String.valueOf(!validationErrors.isEmpty()))
                 .withDetail("requestId", requestId)
                 .withDetail("jobRunId", jobRunId)
        );

        // -------------------------------------------------------------
        // Async dispatch ONLY valid events
        // -------------------------------------------------------------
        executorProvider.execute(MdcUtil.wrap(() -> {
            requestContextProvider.currentWithCorrelation(correlationId);

            int successCount = 0;

            for (CourseEvent event : validEvents) {
                RequestContext eventCtx = baseCtx.withCourse(safeCourseCode(event), event.getNtucSBId());

                try {
                    clsCourseFieldsProcessor.handleCourseNotification(event, false);
                    successCount++;
                } catch (Exception ex) {
                    auditEventWriter.write(
                            AuditEventFactory.fail(
                                    AuditCategory.DT5_FLOW,
                                    AuditStep.REST_NOTIFY_BATCH,
                                    AuditSeverity.ERROR,
                                    eventCtx,
                                    "REST dispatch failed for event",
                                    AuditErrorCode.DT5_UNEXPECTED,
                                    ex
                            ).withDetail("dispatch", "failed")
                             .withDetail("requestId", requestId)
                             .withDetail("jobRunId", jobRunId)
                    );
                }
            }

            auditEventWriter.write(
                    AuditEventFactory.end(
                            AuditCategory.DT5_FLOW,
                            AuditStep.ASYNC_PROCESS_END,
                            AuditSeverity.INFO,
                            baseCtx,
                            AuditStatus.SUCCESS,
                            "REST intake dispatched",
                            AuditErrorCode.NONE
                    ).withDetail("eventCount", String.valueOf(validEvents.size()))
                     .withDetail("successCount", String.valueOf(successCount))
                     .withDetail("requestId", requestId)
                     .withDetail("jobRunId", jobRunId)
            );
        }));

        // -------------------------------------------------------------
        // Response mapping: success vs partial_success
        // -------------------------------------------------------------
        if (!validationErrors.isEmpty()) {
            CourseNotificationAckResponse partial =
                    CourseNotificationAckResponse.partialSuccess(
                            "Some events failed to be handled.",
                            toAckErrors(validationErrors));

            return Response.ok()
                    .header("X-Correlation-Id", correlationId)
                    .header("X-Request-Id", requestId)
                    .header("X-Job-Run-Id", jobRunId)
                    .entity(partial)
                    .build();
        }

        CourseNotificationAckResponse ok =
                CourseNotificationAckResponse.success("Events received and processed successfully.");

        return Response.ok()
                .header("X-Correlation-Id", correlationId)
                .header("X-Request-Id", requestId)
                .header("X-Job-Run-Id", jobRunId)
                .entity(ok)
                .build();
    }

    public Response postOneTimeLoad(CourseRestDtos.OneTimeLoadRequest req) {
        final String correlationId = newCorrelationId();
        final RequestContext baseCtx = requestContextProvider.currentWithCorrelation(correlationId);

        if (req == null || isBlank(req.s3Path)) {
            auditEventWriter.write(
                    AuditEventFactory.end(
                            AuditCategory.DT5_FLOW,
                            AuditStep.ONE_TIME_S3_VALIDATE,
                            AuditSeverity.WARNING,
                            baseCtx,
                            AuditStatus.FAILED,
                            "One-time S3 load rejected: s3Path missing",
                            AuditErrorCode.VALIDATION_FAILED,
                            "s3Path is required"
                    )
            );

            return Response.status(Response.Status.BAD_REQUEST)
                    .header("X-Correlation-Id", correlationId)
                    .entity(new CourseRestDtos.OneTimeLoadResponse("FAILED", "s3Path is required", correlationId))
                    .build();
        }

        final String s3Path = req.s3Path.trim();

        auditEventWriter.write(
                AuditEventFactory.start(
                        AuditCategory.DT5_FLOW,
                        AuditStep.ONE_TIME_S3_VALIDATE,
                        AuditSeverity.INFO,
                        baseCtx,
                        AuditStatus.STARTED,
                        "One-time S3 load accepted",
                        AuditErrorCode.NONE
                ).withDetail("s3Path", s3Path)
        );

        try {
            oneTimeLoadFacade.executeS3Path(s3Path);

            auditEventWriter.write(
                    AuditEventFactory.end(
                            AuditCategory.DT5_FLOW,
                            AuditStep.ONE_TIME_S3_VALIDATE,
                            AuditSeverity.INFO,
                            baseCtx,
                            AuditStatus.SUCCESS,
                            "One-time S3 load triggered",
                            AuditErrorCode.NONE,
                            ""
                    ).withDetail("s3Path", s3Path)
            );

            return Response.ok()
                    .header("X-Correlation-Id", correlationId)
                    .entity(new CourseRestDtos.OneTimeLoadResponse("SUCCESS", "Triggered", correlationId))
                    .build();
        }
        catch (IllegalArgumentException ex) {
            auditEventWriter.write(
                    AuditEventFactory.end(
                            AuditCategory.DT5_FLOW,
                            AuditStep.ONE_TIME_S3_VALIDATE,
                            AuditSeverity.WARNING,
                            baseCtx,
                            AuditStatus.FAILED,
                            "One-time S3 load rejected: invalid s3Path",
                            AuditErrorCode.VALIDATION_FAILED,
                            ex.getMessage()
                    ).withDetail("s3Path", safe(s3Path))
            );

            return Response.status(Response.Status.BAD_REQUEST)
                    .header("X-Correlation-Id", correlationId)
                    .entity(new CourseRestDtos.OneTimeLoadResponse("FAILED", ex.getMessage(), correlationId))
                    .build();
        }
        catch (Exception ex) {
            auditEventWriter.write(
                    AuditEventFactory.fail(
                            AuditCategory.DT5_FLOW,
                            AuditStep.ONE_TIME_S3_VALIDATE,
                            AuditSeverity.ERROR,
                            baseCtx,
                            "One-time S3 load trigger failed",
                            AuditErrorCode.DT5_UNEXPECTED,
                            ex
                    ).withDetail("s3Path", safe(s3Path))
            );

            return Response.serverError()
                    .header("X-Correlation-Id", correlationId)
                    .entity(new CourseRestDtos.OneTimeLoadResponse("FAILED", "Failed to trigger", correlationId))
                    .build();
        }
    }

    // =========================================================================
    // Internals
    // =========================================================================

    private static List<ErrorItem> toAckErrors(List<CourseNotificationRequestValidator.EventError> errs) {
        if (errs == null || errs.isEmpty()) {
            return Collections.emptyList();
        }
        List<ErrorItem> out = new ArrayList<>();
        for (CourseNotificationRequestValidator.EventError e : errs) {
            out.add(new ErrorItem(e.getNotificationId(), e.getCourseCode(), e.getMessage()));
        }
        return out;
    }

    private static String newCorrelationId() {
        return UUID.randomUUID().toString();
    }

    private long nextJobRunId() {
        return counterLocalService.increment("ntuc.dt5.course.rest.jobRunId");
    }

    static CourseEvent normalizeEvent(CourseEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("CourseEvent must not be null");
        }

        if (isBlank(event.getCourseCodeSingle())) {
            List<CourseEvent.Course> courses = event.getCourses();
            if (courses != null && !courses.isEmpty() && courses.get(0) != null) {
                CourseEvent.Course c0 = courses.get(0);

                if (!isBlank(c0.getCourseCode())) {
                    event.setCourseCodeSingle(c0.getCourseCode());
                }
                if (isBlank(event.getCourseTypeSingle()) && !isBlank(c0.getCourseType())) {
                    event.setCourseTypeSingle(c0.getCourseType());
                }
            }
        }

        return event;
    }

    private static List<CourseEvent> safeList(List<CourseEvent> events) {
        return (events == null) ? Collections.<CourseEvent>emptyList() : events;
    }

    private static String safeCourseCode(CourseEvent event) {
        if (event == null) {
            return "";
        }
        return safe(event.getCourseCodeSingle());
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }
}
