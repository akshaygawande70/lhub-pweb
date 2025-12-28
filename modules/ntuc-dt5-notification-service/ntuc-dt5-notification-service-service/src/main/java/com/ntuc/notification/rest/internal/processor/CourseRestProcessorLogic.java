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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.ws.rs.core.Response;

/**
 * Plain Java logic for Course REST processing.
 *
 * Business purpose:
 * Accept course notifications and one-time S3 load triggers, ensuring every accepted item is persisted
 * and processed asynchronously while returning an immediate acknowledgement to the caller.
 *
 * Technical purpose:
 * Validates request payloads, persists valid notifications into NtucSB, dispatches async processing,
 * and emits DB-backed audit events as the single source of truth.
 *
 * Intake rules:
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
 *
 * @author @akshaygawande
 */
public class CourseRestProcessorLogic {

    private final NtucSBLocalService ntucSBLocalService;
    private final CounterLocalService counterLocalService;
    private final ClsCourseFieldsProcessor clsCourseFieldsProcessor;
    private final AuditEventWriter auditEventWriter;
    private final OneTimeLoadFacade oneTimeLoadFacade;
    private final RequestContextProvider requestContextProvider;
    private final NotificationExecutorProvider executorProvider;

    /**
     * Stateless validator for wrapper and per-event validation rules.
     */
    private final CourseNotificationRequestValidator requestValidator = new CourseNotificationRequestValidator();

    /**
     * Persists the intake record for every valid event before async dispatch.
     */
    private final NotificationIntakePersister intakePersister;

    /**
     * Creates the REST processor with all required collaborators.
     *
     * Business purpose:
     * Wires the dependencies needed to reliably accept notifications, persist them, and trigger processing.
     *
     * Technical purpose:
     * Enforces non-null collaborators and builds the intake persister that writes NtucSB records.
     *
     * Inputs/Invariants:
     * - All parameters must be non-null.
     *
     * Side effects:
     * - None at construction time; persistence happens when processing requests.
     *
     * Audit behavior:
     * - None directly; audit is emitted during request handling.
     *
     * Return semantics:
     * - Constructs an instance or throws NullPointerException via Objects.requireNonNull.
     *
     * @param ntucSBLocalService ServiceBuilder local service used by intake persistence.
     * @param counterLocalService Counter service used to generate jobRunId values.
     * @param clsCourseFieldsProcessor Course processor invoked during async dispatch.
     * @param auditEventWriter Writer that persists audit events to the AuditLog table.
     * @param oneTimeLoadFacade Facade that triggers one-time S3 load processing.
     * @param requestContextProvider Provider for RequestContext/MDC-aware correlation handling.
     * @param executorProvider Executor used for async dispatch of valid events.
     */
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

    /**
     * Handles the REST batch intake of course notifications.
     *
     * Business purpose:
     * Accepts a batch of course notifications, acknowledges immediately, and ensures valid notifications
     * are persisted and processed asynchronously without invalid items blocking the batch.
     *
     * Technical purpose:
     * Validates wrapper and events, persists valid items into NtucSB, dispatches async processing, and
     * records audit events for acceptance/rejection and downstream dispatch outcomes.
     *
     * Inputs/Invariants:
     * - Wrapper may be null; wrapper events may be null/empty.
     * - Only validated events are persisted and dispatched.
     *
     * Side effects:
     * - DB writes: NtucSB persistence for valid events; audit persistence for all outcomes.
     * - Async dispatch: schedules processing on {@link NotificationExecutorProvider}.
     * - MDC/ThreadLocal: correlation/MDC is wrapped via {@link MdcUtil#wrap(Runnable)}.
     *
     * Audit behavior:
     * - VALIDATION: recorded when wrapper/event validation fails.
     * - ENTRY: recorded when intake is accepted (including partial acceptance).
     * - EXECUTION: recorded per-event when async dispatch fails; and when dispatch completes.
     *
     * Return semantics:
     * - 400 for invalid requests (no valid events).
     * - 200 with success/partial_success acknowledgement otherwise.
     *
     * @param wrapper Request wrapper containing the events list.
     * @return JAX-RS response with acknowledgement body and correlation headers.
     */
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

            // AuditStep replacement: REST_NOTIFY_BATCH -> VALIDATION
            // Reason: this log represents request validation failure at the REST boundary.
            // Specifics (REST batch intake, wrapper failure) moved into message/details.
            auditEventWriter.write(
                    AuditEventFactory.end(
                            AuditCategory.DT5_FLOW,
                            AuditStep.VALIDATION,
                            AuditSeverity.WARNING,
                            baseCtx,
                            AuditStatus.FAILED,
                            "REST intake rejected: wrapper validation failed",
                            AuditErrorCode.VALIDATION_FAILED,
                            wrapperValidation.getMessage()
                    ).withDetail("errorCount", String.valueOf(errors.size()))
                     .withDetail("reason", "requestWrapperValidationFailed")
                     .withDetail("requestType", "restCourseNotifyBatch")
                     .withDetail("requestId", requestId)
                     .withDetail("jobRunId", jobRunId)
                     .withDetail("durationMs", String.valueOf(System.currentTimeMillis() - startMs))
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

            // AuditStep replacement: REST_NOTIFY_BATCH -> VALIDATION
            // Reason: all events invalid is still a validation outcome at the REST boundary.
            // Specifics (all events invalid, batch stats) moved into message/details.
            auditEventWriter.write(
                    AuditEventFactory.end(
                            AuditCategory.DT5_FLOW,
                            AuditStep.VALIDATION,
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
                     .withDetail("requestType", "restCourseNotifyBatch")
                     .withDetail("requestId", requestId)
                     .withDetail("jobRunId", jobRunId)
                     .withDetail("durationMs", String.valueOf(System.currentTimeMillis() - startMs))
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

        // AuditStep replacement: REST_NOTIFY_BATCH -> ENTRY
        // Reason: this log is the successful REST entry acknowledgement (accepted intake) boundary.
        // Specifics (partial acceptance, counts, REST batch type) moved into message/details.
        auditEventWriter.write(
                AuditEventFactory.start(
                        AuditCategory.DT5_FLOW,
                        AuditStep.ENTRY,
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
                 .withDetail("requestType", "restCourseNotifyBatch")
                 .withDetail("requestId", requestId)
                 .withDetail("jobRunId", jobRunId)
        );

        // -------------------------------------------------------------
        // Async dispatch ONLY valid events
        // -------------------------------------------------------------
        executorProvider.execute(MdcUtil.wrap(() -> {
            // Ensures correlation is available to downstream logs/audit details.
            requestContextProvider.currentWithCorrelation(correlationId);

            int successCount = 0;

            for (CourseEvent event : validEvents) {
                RequestContext eventCtx = baseCtx.withCourse(safeCourseCode(event), event.getNtucSBId());

                try {
                    clsCourseFieldsProcessor.handleCourseNotification(event, false);
                    successCount++;
                } catch (Exception ex) {
                    // AuditStep replacement: REST_NOTIFY_BATCH -> EXECUTION
                    // Reason: this is an execution failure while dispatching an accepted event.
                    // Specifics (REST dispatch, event scope) moved into message/details.
                    auditEventWriter.write(
                            AuditEventFactory.fail(
                                    AuditCategory.DT5_FLOW,
                                    AuditStep.EXECUTION,
                                    AuditSeverity.ERROR,
                                    eventCtx,
                                    "REST dispatch failed for event",
                                    AuditErrorCode.DT5_UNEXPECTED,
                                    ex
                            ).withDetail("dispatch", "failed")
                             .withDetail("requestType", "restCourseNotifyBatch")
                             .withDetail("requestId", requestId)
                             .withDetail("jobRunId", jobRunId)
                    );
                }
            }

            // AuditStep replacement: ASYNC_PROCESS_END -> EXECUTION
            // Reason: completion marker for async execution of the accepted batch.
            // Specifics (async completion, counts, REST intake) moved into message/details.
            auditEventWriter.write(
                    AuditEventFactory.end(
                            AuditCategory.DT5_FLOW,
                            AuditStep.EXECUTION,
                            AuditSeverity.INFO,
                            baseCtx,
                            AuditStatus.SUCCESS,
                            "REST intake dispatched",
                            AuditErrorCode.NONE
                    ).withDetail("eventCount", String.valueOf(validEvents.size()))
                     .withDetail("successCount", String.valueOf(successCount))
                     .withDetail("requestType", "restCourseNotifyBatch")
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

    /**
     * Handles the REST trigger for a one-time S3 load.
     *
     * Business purpose:
     * Allows controlled backfill/one-time ingestion from an S3 path while providing immediate feedback
     * to the caller and capturing an audit trail of validation and trigger outcomes.
     *
     * Technical purpose:
     * Validates required input (s3Path), triggers {@link OneTimeLoadFacade}, and records audit events
     * for acceptance, rejection, and unexpected failures.
     *
     * Inputs/Invariants:
     * - Request may be null.
     * - s3Path is required and trimmed before execution.
     *
     * Side effects:
     * - External call: triggers S3 load workflow through {@link OneTimeLoadFacade}.
     * - DB writes: audit persistence for validation/trigger outcomes.
     *
     * Audit behavior:
     * - VALIDATION: recorded when s3Path is missing or invalid.
     * - ENTRY: recorded when request is accepted.
     * - EXECUTION: recorded when trigger fails unexpectedly.
     *
     * Return semantics:
     * - 400 when s3Path is missing/invalid.
     * - 200 when trigger is accepted.
     * - 500 when trigger fails unexpectedly.
     *
     * @param req Request containing s3Path.
     * @return JAX-RS response with outcome body and correlation header.
     */
    public Response postOneTimeLoad(CourseRestDtos.OneTimeLoadRequest req) {
        final String correlationId = newCorrelationId();
        final RequestContext baseCtx = requestContextProvider.currentWithCorrelation(correlationId);

        if (req == null || isBlank(req.s3Path)) {
            // AuditStep replacement: ONE_TIME_S3_VALIDATE -> VALIDATION
            // Reason: missing required parameter is a validation failure at REST boundary.
            // Specifics (one-time S3 load, missing s3Path) moved into message/details.
            auditEventWriter.write(
                    AuditEventFactory.end(
                            AuditCategory.DT5_FLOW,
                            AuditStep.VALIDATION,
                            AuditSeverity.WARNING,
                            baseCtx,
                            AuditStatus.FAILED,
                            "One-time S3 load rejected: s3Path missing",
                            AuditErrorCode.VALIDATION_FAILED,
                            "s3Path is required"
                    ).withDetail("requestType", "restOneTimeS3Load")
            );

            return Response.status(Response.Status.BAD_REQUEST)
                    .header("X-Correlation-Id", correlationId)
                    .entity(new CourseRestDtos.OneTimeLoadResponse("FAILED", "s3Path is required", correlationId))
                    .build();
        }

        final String s3Path = req.s3Path.trim();

        // AuditStep replacement: ONE_TIME_S3_VALIDATE -> ENTRY
        // Reason: accepted REST entry point for the one-time trigger.
        // Specifics (one-time load, s3Path) moved into details.
        auditEventWriter.write(
                AuditEventFactory.start(
                        AuditCategory.DT5_FLOW,
                        AuditStep.ENTRY,
                        AuditSeverity.INFO,
                        baseCtx,
                        AuditStatus.STARTED,
                        "One-time S3 load accepted",
                        AuditErrorCode.NONE
                ).withDetail("s3Path", s3Path)
                 .withDetail("requestType", "restOneTimeS3Load")
        );

        try {
            oneTimeLoadFacade.executeS3Path(s3Path);

            // AuditStep replacement: ONE_TIME_S3_VALIDATE -> EXECUTION
            // Reason: represents successful execution/trigger of the requested action.
            // Specifics (triggered, s3Path) moved into message/details.
            auditEventWriter.write(
                    AuditEventFactory.end(
                            AuditCategory.DT5_FLOW,
                            AuditStep.EXECUTION,
                            AuditSeverity.INFO,
                            baseCtx,
                            AuditStatus.SUCCESS,
                            "One-time S3 load triggered",
                            AuditErrorCode.NONE,
                            ""
                    ).withDetail("s3Path", s3Path)
                     .withDetail("requestType", "restOneTimeS3Load")
            );

            return Response.ok()
                    .header("X-Correlation-Id", correlationId)
                    .entity(new CourseRestDtos.OneTimeLoadResponse("SUCCESS", "Triggered", correlationId))
                    .build();
        }
        catch (IllegalArgumentException ex) {
            // AuditStep replacement: ONE_TIME_S3_VALIDATE -> VALIDATION
            // Reason: invalid argument indicates validation/business-rule rejection.
            // Specifics (invalid s3Path) moved into details.
            auditEventWriter.write(
                    AuditEventFactory.end(
                            AuditCategory.DT5_FLOW,
                            AuditStep.VALIDATION,
                            AuditSeverity.WARNING,
                            baseCtx,
                            AuditStatus.FAILED,
                            "One-time S3 load rejected: invalid s3Path",
                            AuditErrorCode.VALIDATION_FAILED,
                            ex.getMessage()
                    ).withDetail("s3Path", safe(s3Path))
                     .withDetail("requestType", "restOneTimeS3Load")
            );

            return Response.status(Response.Status.BAD_REQUEST)
                    .header("X-Correlation-Id", correlationId)
                    .entity(new CourseRestDtos.OneTimeLoadResponse("FAILED", ex.getMessage(), correlationId))
                    .build();
        }
        catch (Exception ex) {
            // AuditStep replacement: ONE_TIME_S3_VALIDATE -> EXECUTION
            // Reason: unexpected runtime failure while triggering the workflow.
            // Specifics (trigger failure, s3Path) moved into details.
            auditEventWriter.write(
                    AuditEventFactory.fail(
                            AuditCategory.DT5_FLOW,
                            AuditStep.EXECUTION,
                            AuditSeverity.ERROR,
                            baseCtx,
                            "One-time S3 load trigger failed",
                            AuditErrorCode.DT5_UNEXPECTED,
                            ex
                    ).withDetail("s3Path", safe(s3Path))
                     .withDetail("requestType", "restOneTimeS3Load")
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

    /**
     * Maps validator event errors into the REST acknowledgement error response model.
     *
     * Business purpose:
     * Provides clients with actionable error entries without blocking valid events in the same batch.
     *
     * Technical purpose:
     * Converts {@link CourseNotificationRequestValidator.EventError} objects into REST DTOs.
     *
     * Inputs/Invariants:
     * - errs may be null/empty.
     *
     * Side effects:
     * - None.
     *
     * Audit behavior:
     * - None (audit is written by callers based on outcomes).
     *
     * Return semantics:
     * - Returns an immutable empty list when input is null/empty.
     *
     * @param errs validation errors from wrapper/event validation.
     * @return list of REST acknowledgement error items.
     */
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

    /**
     * Generates a unique correlation identifier for request tracing.
     *
     * @return correlation id string.
     */
    private static String newCorrelationId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Allocates the next jobRunId used for REST intake correlation and audit grouping.
     *
     * Business purpose:
     * Ensures each intake request can be uniquely identified across audit records and operational support workflows.
     *
     * Technical purpose:
     * Uses {@link CounterLocalService} to increment a named counter stored in the database.
     *
     * Inputs/Invariants:
     * - Counter name is stable and must not change to preserve jobRunId continuity.
     *
     * Side effects:
     * - DB write: increments the counter value.
     *
     * Audit behavior:
     * - None directly; jobRunId is attached as a detail to audit events by callers.
     *
     * Return semantics:
     * - Returns the incremented counter value.
     *
     * @return next job run id.
     */
    private long nextJobRunId() {
        return counterLocalService.increment("ntuc.dt5.course.rest.jobRunId");
    }

    /**
     * Normalizes a CourseEvent in-place to ensure a usable single courseCode/courseType view.
     *
     * Business purpose:
     * Allows downstream processing and audit correlation even when the request supplies course identifiers
     * only in the first element of the "courses" list.
     *
     * Technical purpose:
     * If courseCodeSingle is blank, derive courseCodeSingle/courseTypeSingle from the first course entry.
     *
     * Inputs/Invariants:
     * - event must not be null.
     *
     * Side effects:
     * - Mutates the input {@link CourseEvent} by setting courseCodeSingle/courseTypeSingle when derived.
     *
     * Audit behavior:
     * - None (audit is handled by callers).
     *
     * Return semantics:
     * - Returns the same event instance after normalization.
     * - Throws {@link IllegalArgumentException} when event is null.
     *
     * @param event course event to normalize.
     * @return normalized event (same instance).
     */
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

    /**
     * Null-safe list accessor for wrapper events.
     *
     * @param events list that may be null.
     * @return non-null list.
     */
    private static List<CourseEvent> safeList(List<CourseEvent> events) {
        return (events == null) ? Collections.<CourseEvent>emptyList() : events;
    }

    /**
     * Extracts a non-null course code used for context/audit correlation.
     *
     * @param event course event.
     * @return non-null string (may be empty).
     */
    private static String safeCourseCode(CourseEvent event) {
        if (event == null) {
            return "";
        }
        return safe(event.getCourseCodeSingle());
    }

    /**
     * Returns true when the string is null or contains only whitespace.
     *
     * @param s input string.
     * @return true when blank.
     */
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Null-safe string conversion.
     *
     * @param s input string.
     * @return empty string when input is null, otherwise original.
     */
    private static String safe(String s) {
        return (s == null) ? "" : s;
    }

    // ---------------------------------------------------------------------
    // Test hook (non-production usage):
    // Allows unit tests to replace the final intakePersister without requiring
    // ServiceBuilder wiring. This avoids brittle tests while keeping production
    // code behavior unchanged.
    // ---------------------------------------------------------------------

    /**
     * Replaces the internal persister using reflection.
     *
     * Business purpose:
     * Supports deterministic unit tests for intake flows without requiring DB-backed ServiceBuilder wiring.
     *
     * Technical purpose:
     * Replaces the final {@link #intakePersister} field for test execution only.
     *
     * Inputs/Invariants:
     * - replacement must be non-null.
     *
     * Side effects:
     * - Mutates internal state (test-only).
     *
     * Audit behavior:
     * - None.
     *
     * Return semantics:
     * - Throws RuntimeException if reflection fails.
     *
     * @param replacement persister to use for tests.
     */
    static void _testOnlyReplaceIntakePersister(CourseRestProcessorLogic target, NotificationIntakePersister replacement) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(replacement, "replacement");

        try {
            Field f = CourseRestProcessorLogic.class.getDeclaredField("intakePersister");
            f.setAccessible(true);

            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);

            f.set(target, replacement);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to replace intakePersister for tests", e);
        }
    }
}
