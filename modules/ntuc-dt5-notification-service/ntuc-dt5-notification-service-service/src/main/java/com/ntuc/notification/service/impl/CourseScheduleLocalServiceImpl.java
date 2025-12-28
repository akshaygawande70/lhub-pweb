package com.ntuc.notification.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.petra.executor.PortalExecutorManager;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.Validator;
import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.audit.util.MdcUtil;
import com.ntuc.notification.model.CourseSchedule;
import com.ntuc.notification.model.ScheduleResponse;
import com.ntuc.notification.schedule.api.util.ScheduleDateTimeValidator;
import com.ntuc.notification.service.ClsConnectionHelper;
import com.ntuc.notification.service.base.CourseScheduleLocalServiceBaseImpl;

import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Local service implementation for CourseSchedule snapshots.
 *
 * Behaviour:
 *   - getScheduleForCourse(courseCode, serviceContext)
 *   - Try CLS live:
 *       - If success -> async upsert snapshot, return live JSON (fromCache=false)
 *       - If CLS 404 -> return empty schedule JSON (fromCache=false), async upsert empty snapshot
 *   - Else -> return last snapshot from DB (fromCache=true)
 *
 * New-world rules:
 * - NO legacy AuditLogger.
 * - Persist observability via AuditEventWriter (DB audit).
 * - Audit must never break runtime flow.
 *
 * Timing rule:
 * - durationMs must be reconstructable from AuditEvent start/end.
 * - STARTED events must have endTimeMs=0 (unknown yet).
 * - END events must reuse the same measured startTimeMs for their duration calculation.
 */
@Component(
        property = "model.class.name=com.ntuc.notification.model.CourseSchedule",
        service = AopService.class
)
public class CourseScheduleLocalServiceImpl extends CourseScheduleLocalServiceBaseImpl {

    private static final Log _log = LogFactoryUtil.getLog(CourseScheduleLocalServiceImpl.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Parse CLS date string.
     * Supports:
     *   - "dd/MM/yyyy"
     *   - "dd/MM/yyyy HH:mm"
     */
    private static final DateTimeFormatter CLS_FLEX =
            new DateTimeFormatterBuilder()
                    .appendPattern("dd/MM/yyyy")
                    .optionalStart()
                    .appendPattern(" HH:mm")
                    .optionalEnd()
                    .toFormatter();

    /**
     * CLS "datetime" format (dd/MM/yyyy HH:mm).
     */
    private static final DateTimeFormatter CLS_DATE_TIME =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * CLS zone is ONLY for CLS payload semantics, not for admin UI edits.
     */
    private static final ZoneId CLS_ZONE = ZoneId.of("Asia/Singapore");

    // Transaction config for async upsert
    private static final TransactionConfig _UPSERT_TX_CONFIG =
            TransactionConfig.Factory.create(
                    Propagation.REQUIRES_NEW,
                    new Class<?>[]{Exception.class}
            );

    @Reference
    private ClsConnectionHelper _clsConnectionHelper;

    @Reference
    private PortalExecutorManager _portalExecutorManager;

    @Reference
    private AuditEventWriter _auditEventWriter;

    // ========================================================================
    //  MAIN ENTRY POINT (called from FTL)
    // ========================================================================

    @Override
    public String getScheduleForCourse(String courseCode, ServiceContext serviceContext) throws PortalException {
        if (Validator.isNull(courseCode)) {
            _log.warn("getScheduleForCourse called with null/empty courseCode");
            return null;
        }

        // FTL often passes null; fall back to thread-local context.
        if (serviceContext == null) {
            serviceContext = ServiceContextThreadLocal.getServiceContext();
        }

        final long startMs = now();
        final String corrId = corrId();
        final String requestId = UUID.randomUUID().toString();
        final String eventId = UUID.randomUUID().toString();

        final long companyId = (serviceContext != null) ? serviceContext.getCompanyId() : CompanyThreadLocal.getCompanyId();
        final long groupId = (serviceContext != null) ? serviceContext.getScopeGroupId() : 0L;
        final long userId = (serviceContext != null) ? serviceContext.getUserId() : 0L;

        writeAudit(
                startMs,
                0L,
                corrId,
                requestId,
                eventId,
                companyId,
                groupId,
                userId,
                courseCode,
                0L,
                AuditSeverity.INFO,
                AuditStatus.STARTED,
                AuditStep.ASYNC_PROCESS_START,
                AuditCategory.DT5_FLOW,
                "CourseSchedule.getScheduleForCourse start",
                AuditErrorCode.NONE,
                null,
                null,
                details("op", "GET_SCHEDULE_FOR_COURSE", "source", "FTL", "courseCode", courseCode)
        );

        ScheduleResponse live = null;

        try {
            live = _clsConnectionHelper.getLatestCourseSchedules(courseCode);

            if (live != null && live.getError() == null) {
                final String rawJson = toJsonSafe(live);

                scheduleAsyncUpsert(courseCode, live, serviceContext, rawJson, companyId, groupId, userId, corrId);

                live.setFromCache(false);

                writeAudit(
                        startMs,
                        now(),
                        corrId,
                        requestId,
                        eventId,
                        companyId,
                        groupId,
                        userId,
                        courseCode,
                        0L,
                        AuditSeverity.INFO,
                        AuditStatus.SUCCESS,
                        AuditStep.ASYNC_PROCESS_END,
                        AuditCategory.DT5_FLOW,
                        "CourseSchedule.getScheduleForCourse success (live CLS)",
                        AuditErrorCode.NONE,
                        null,
                        null,
                        details("op", "GET_SCHEDULE_FOR_COURSE", "fromCache", "false", "path", "CLS_OK")
                );

                return rawJson;
            }

            if (live != null && live.getError() != null) {
                String errorCode = live.getError().getCode();

                if (String.valueOf(HttpURLConnection.HTTP_NOT_FOUND).equals(errorCode)) {
                    _log.info(
                            "CLS returned 404 (course not found) for courseCode=" + courseCode +
                                    ". Returning empty schedule response and updating snapshot."
                    );

                    ScheduleResponse empty = new ScheduleResponse(
                            null,
                            null,
                            null,
                            new ScheduleResponse.Duration(0, 0),
                            0,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            live.getError()
                    );
                    empty.setFromCache(false);

                    final String rawJsonEmpty = toJsonSafe(empty);

                    scheduleAsyncUpsert(courseCode, empty, serviceContext, rawJsonEmpty, companyId, groupId, userId, corrId);

                    writeAudit(
                            startMs,
                            now(),
                            corrId,
                            requestId,
                            eventId,
                            companyId,
                            groupId,
                            userId,
                            courseCode,
                            0L,
                            AuditSeverity.INFO,
                            AuditStatus.SUCCESS,
                            AuditStep.ASYNC_PROCESS_END,
                            AuditCategory.CLS,
                            "CourseSchedule.getScheduleForCourse success (CLS 404 -> empty schedule)",
                            AuditErrorCode.CLS_NOT_FOUND,
                            "CLS returned 404 for course",
                            null,
                            details("op", "GET_SCHEDULE_FOR_COURSE", "fromCache", "false", "path", "CLS_404", "clsStatus", "404")
                    );

                    return rawJsonEmpty;
                }

                _log.warn(
                        "CLS returned schedule with error for courseCode=" + courseCode +
                                " code=" + errorCode +
                                " message=" + live.getError().getMessage()
                );
            } else {
                _log.warn("CLS returned null schedule for courseCode=" + courseCode);
            }

        } catch (Exception e) {
            _log.error("Error fetching schedule from CLS for courseCode=" + courseCode, e);

            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    eventId,
                    companyId,
                    groupId,
                    userId,
                    courseCode,
                    0L,
                    AuditSeverity.WARNING,
                    AuditStatus.PARTIAL,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.CLS,
                    "CourseSchedule.getScheduleForCourse CLS error; will serve snapshot if available",
                    AuditErrorCode.CLS_CONNECTION_FAILED,
                    safeMsg(e),
                    e.getClass().getName(),
                    details("op", "GET_SCHEDULE_FOR_COURSE", "path", "CLS_EXCEPTION")
            );
        }

        ScheduleResponse snapshot = getScheduleSnapshot(courseCode);
        if (snapshot != null) {
            snapshot.setFromCache(true);
            _log.info("Serving schedule snapshot from CSchedule for courseCode=" + courseCode);
        } else {
            _log.warn("No schedule snapshot available in CSchedule for courseCode=" + courseCode);
        }

        writeAudit(
                startMs,
                now(),
                corrId,
                requestId,
                eventId,
                companyId,
                groupId,
                userId,
                courseCode,
                0L,
                AuditSeverity.INFO,
                (snapshot != null) ? AuditStatus.SUCCESS : AuditStatus.SKIPPED,
                AuditStep.ASYNC_PROCESS_END,
                AuditCategory.DT5_FLOW,
                "CourseSchedule.getScheduleForCourse served snapshot",
                AuditErrorCode.NONE,
                null,
                null,
                details("op", "GET_SCHEDULE_FOR_COURSE", "fromCache", "true", "path", "SNAPSHOT", "hasSnapshot", String.valueOf(snapshot != null))
        );

        return toJsonSafe(snapshot);
    }

    // ========================================================================
    //  ASYNC UPSERT (SEND-AND-FORGET)
    // ========================================================================

    private void scheduleAsyncUpsert(
            String courseCode,
            ScheduleResponse dto,
            ServiceContext serviceContext,
            String rawJson,
            long companyId,
            long groupId,
            long userId,
            String corrId) {

        ExecutorService executor = null;

        try {
            executor = _portalExecutorManager.getPortalExecutor(CourseScheduleLocalServiceImpl.class.getName());
        } catch (Exception e) {
            _log.error(
                    "Unable to obtain portal executor for CourseScheduleLocalServiceImpl. " +
                            "Falling back to synchronous upsert for courseCode=" + courseCode,
                    e
            );
        }

        if (executor == null) {
            final long upsertStartMs = now();
            try {
                upsertFromCls(courseCode, dto, serviceContext, rawJson);

                writeAudit(
                        upsertStartMs,
                        now(),
                        corrId,
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        companyId,
                        groupId,
                        userId,
                        courseCode,
                        0L,
                        AuditSeverity.INFO,
                        AuditStatus.SUCCESS,
                        AuditStep.ASYNC_PROCESS_END,
                        AuditCategory.DT5_FLOW,
                        "CourseSchedule snapshot upsert success (sync fallback)",
                        AuditErrorCode.NONE,
                        null,
                        null,
                        details("op", "UPSERT_SNAPSHOT", "path", "UPSERT_SYNC_FALLBACK")
                );
            } catch (Exception e) {
                _log.error("Sync fallback upsertFromCls failed for courseCode=" + courseCode, e);
                writeAudit(
                        upsertStartMs,
                        now(),
                        corrId,
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        companyId,
                        groupId,
                        userId,
                        courseCode,
                        0L,
                        AuditSeverity.ERROR,
                        AuditStatus.FAILED,
                        AuditStep.ASYNC_PROCESS_END,
                        AuditCategory.DT5_FLOW,
                        "CourseSchedule snapshot upsert failed (sync fallback)",
                        AuditErrorCode.DT5_UNEXPECTED,
                        safeMsg(e),
                        e.getClass().getName(),
                        details("op", "UPSERT_SNAPSHOT", "path", "UPSERT_SYNC_FALLBACK")
                );
            }
            return;
        }

        final long effectiveCompanyId = companyId > 0 ? companyId : CompanyThreadLocal.getCompanyId();
        final long effectiveGroupId = groupId;

        try {
            executor.submit(() -> {
                final long upsertStartMs = now();
                long originalCompanyId = CompanyThreadLocal.getCompanyId();

                try {
                    if (effectiveCompanyId > 0) {
                        CompanyThreadLocal.setCompanyId(effectiveCompanyId);
                    }

                    ServiceContext asyncSC = new ServiceContext();
                    asyncSC.setCompanyId(effectiveCompanyId);
                    asyncSC.setScopeGroupId(effectiveGroupId);

                    TransactionInvokerUtil.invoke(_UPSERT_TX_CONFIG, () -> {
                        upsertFromCls(courseCode, dto, asyncSC, rawJson);
                        return null;
                    });

                    writeAudit(
                            upsertStartMs,
                            now(),
                            corrId,
                            UUID.randomUUID().toString(),
                            UUID.randomUUID().toString(),
                            effectiveCompanyId,
                            effectiveGroupId,
                            userId,
                            courseCode,
                            0L,
                            AuditSeverity.INFO,
                            AuditStatus.SUCCESS,
                            AuditStep.ASYNC_PROCESS_END,
                            AuditCategory.DT5_FLOW,
                            "CourseSchedule snapshot upsert success (async)",
                            AuditErrorCode.NONE,
                            null,
                            null,
                            details("op", "UPSERT_SNAPSHOT", "path", "UPSERT_ASYNC")
                    );

                } catch (Throwable t) {
                    _log.error("Async upsertFromCls failed for courseCode=" + courseCode, t);

                    writeAudit(
                            upsertStartMs,
                            now(),
                            corrId,
                            UUID.randomUUID().toString(),
                            UUID.randomUUID().toString(),
                            effectiveCompanyId,
                            effectiveGroupId,
                            userId,
                            courseCode,
                            0L,
                            AuditSeverity.ERROR,
                            AuditStatus.FAILED,
                            AuditStep.ASYNC_PROCESS_END,
                            AuditCategory.DT5_FLOW,
                            "CourseSchedule snapshot upsert failed (async)",
                            AuditErrorCode.DT5_UNEXPECTED,
                            safeMsg(t),
                            t.getClass().getName(),
                            details("op", "UPSERT_SNAPSHOT", "path", "UPSERT_ASYNC")
                    );

                } finally {
                    CompanyThreadLocal.setCompanyId(originalCompanyId);
                }
            });

        } catch (RejectedExecutionException rex) {
            _log.warn(
                    "Executor rejected async upsert for courseCode=" + courseCode +
                            ". Falling back to synchronous upsert.",
                    rex
            );

            final long upsertStartMs = now();
            try {
                upsertFromCls(courseCode, dto, serviceContext, rawJson);

                writeAudit(
                        upsertStartMs,
                        now(),
                        corrId,
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        companyId,
                        groupId,
                        userId,
                        courseCode,
                        0L,
                        AuditSeverity.INFO,
                        AuditStatus.SUCCESS,
                        AuditStep.ASYNC_PROCESS_END,
                        AuditCategory.DT5_FLOW,
                        "CourseSchedule snapshot upsert success (executor rejected + sync fallback)",
                        AuditErrorCode.NONE,
                        null,
                        null,
                        details("op", "UPSERT_SNAPSHOT", "path", "UPSERT_REJECTED_FALLBACK")
                );

            } catch (Exception e) {
                _log.error("Sync fallback after executor rejection failed for courseCode=" + courseCode, e);
                writeAudit(
                        upsertStartMs,
                        now(),
                        corrId,
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        companyId,
                        groupId,
                        userId,
                        courseCode,
                        0L,
                        AuditSeverity.ERROR,
                        AuditStatus.FAILED,
                        AuditStep.ASYNC_PROCESS_END,
                        AuditCategory.DT5_FLOW,
                        "CourseSchedule snapshot upsert failed (executor rejected + sync fallback)",
                        AuditErrorCode.DT5_UNEXPECTED,
                        safeMsg(e),
                        e.getClass().getName(),
                        details("op", "UPSERT_SNAPSHOT", "path", "UPSERT_REJECTED_FALLBACK")
                );
            }
        } catch (Exception ex) {
            _log.error("Unexpected error scheduling async upsert for courseCode=" + courseCode, ex);
        }
    }

    // ========================================================================
    //  SNAPSHOT HELPERS
    // ========================================================================

    @Override
    public ScheduleResponse getScheduleSnapshot(String courseCode) {
        if (Validator.isNull(courseCode)) {
            return null;
        }

        CourseSchedule entity = courseSchedulePersistence.fetchByCourseCode(courseCode);
        if (entity == null) {
            return null;
        }

        return mapEntityToScheduleResponse(entity);
    }

    @Override
    public CourseSchedule fetchByCourseCode(String courseCode) {
        return courseSchedulePersistence.fetchByCourseCode(courseCode);
    }

    @Override
    public java.util.List<CourseSchedule> getAllCourseSchedules() {
        return courseSchedulePersistence.findAll();
    }

    // ========================================================================
    //  ADMIN UPDATE (STRICT VALIDATION ENFORCED HERE)
    // ========================================================================

    @Override
    public CourseSchedule updateScheduleFromAdmin(
            long courseScheduleId,
            String intakeNumber,
            String startDateString,
            String endDateString,
            Integer availability,
            String venue,
            Integer durationHours,
            Integer durationMinutes,
            String availablePax,
            String availableWaitlist,
            String lxpBuyUrl,
            String scheduleDownloadUrl,
            String errorCode,
            String errorMessage,
            ServiceContext serviceContext)
            throws PortalException {

        if (serviceContext == null) {
            serviceContext = ServiceContextThreadLocal.getServiceContext();
        }

        final long startMs = now();
        final String corrId = corrId();
        final String requestId = UUID.randomUUID().toString();
        final String eventId = UUID.randomUUID().toString();

        CourseSchedule cs = courseSchedulePersistence.findByPrimaryKey(courseScheduleId);

        long companyId = (serviceContext != null) ? serviceContext.getCompanyId() : cs.getCompanyId();
        long groupId = (serviceContext != null) ? serviceContext.getScopeGroupId() : cs.getGroupId();
        long userId = (serviceContext != null) ? serviceContext.getUserId() : 0L;

        writeAudit(
                startMs,
                0L,
                corrId,
                requestId,
                eventId,
                companyId,
                groupId,
                userId,
                safe(cs.getCourseCode()),
                0L,
                AuditSeverity.INFO,
                AuditStatus.STARTED,
                AuditStep.ASYNC_PROCESS_START,
                AuditCategory.DT5_FLOW,
                "CourseSchedule.updateScheduleFromAdmin start",
                AuditErrorCode.NONE,
                null,
                null,
                details("op", "ADMIN_UPDATE_SCHEDULE", "courseScheduleId", String.valueOf(courseScheduleId), "source", "ADMIN")
        );

        try {
            ZoneId adminZone = (serviceContext != null && serviceContext.getTimeZone() != null)
                    ? serviceContext.getTimeZone().toZoneId()
                    : ZoneId.systemDefault();

            // Enforce strict format + range rule
            ScheduleDateTimeValidator.validateRange(startDateString, endDateString, adminZone);

            // Parse strictly in the same zone (avoid CLS_ZONE mismatch)
            ZonedDateTime zStart = ScheduleDateTimeValidator.parseStrict(startDateString, adminZone);
            ZonedDateTime zEnd = ScheduleDateTimeValidator.parseStrict(endDateString, adminZone);

            if (!Validator.isNull(intakeNumber)) {
                cs.setIntakeNumber(intakeNumber);
            }

            cs.setStartDate(Date.from(zStart.toInstant()));
            cs.setEndDate(Date.from(zEnd.toInstant()));

            if (availability != null) {
                cs.setAvailability(availability.intValue());
            }

            if (venue != null) {
                cs.setVenue(venue);
            }

            if (durationHours != null) {
                cs.setDurationHours(durationHours.intValue());
            }
            if (durationMinutes != null) {
                cs.setDurationMinutes(durationMinutes.intValue());
            }

            if (availablePax != null) {
                cs.setAvailablePax(availablePax);
            }
            if (availableWaitlist != null) {
                cs.setAvailableWaitlist(availableWaitlist);
            }

            if (lxpBuyUrl != null) {
                cs.setLxpBuyUrl(lxpBuyUrl);
            }

            if (scheduleDownloadUrl != null) {
                cs.setScheduleDownloadUrl(scheduleDownloadUrl);
            }

            cs.setErrorCode(errorCode);
            cs.setErrorMessage(errorMessage);

            Date now = (serviceContext != null) ? serviceContext.getModifiedDate(new Date()) : new Date();
            cs.setModifiedDate(now);

            try {
                cs.setModifiedBy((serviceContext != null) ? serviceContext.getUserId() : 0L);
            } catch (Throwable ignore) {
                // ignore if field doesn't exist in this SB version
            }

            CourseSchedule updated = courseSchedulePersistence.update(cs);

            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    eventId,
                    companyId,
                    groupId,
                    userId,
                    safe(updated.getCourseCode()),
                    0L,
                    AuditSeverity.INFO,
                    AuditStatus.SUCCESS,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "CourseSchedule.updateScheduleFromAdmin success",
                    AuditErrorCode.NONE,
                    null,
                    null,
                    details("op", "ADMIN_UPDATE_SCHEDULE", "courseScheduleId", String.valueOf(courseScheduleId), "source", "ADMIN")
            );

            return updated;

        } catch (IllegalArgumentException e) {
            // strict validation failure -> controlled PortalException
            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    eventId,
                    companyId,
                    groupId,
                    userId,
                    safe(cs.getCourseCode()),
                    0L,
                    AuditSeverity.WARNING,
                    AuditStatus.FAILED,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "CourseSchedule.updateScheduleFromAdmin validation failed",
                    AuditErrorCode.VALIDATION_FAILED,
                    safeMsg(e),
                    e.getClass().getName(),
                    details("op", "ADMIN_UPDATE_SCHEDULE", "courseScheduleId", String.valueOf(courseScheduleId), "source", "ADMIN")
            );

            throw new PortalException("Invalid start/end date. " + safeMsg(e), e);

        } catch (Exception e) {
            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    eventId,
                    companyId,
                    groupId,
                    userId,
                    safe(cs.getCourseCode()),
                    0L,
                    AuditSeverity.ERROR,
                    AuditStatus.FAILED,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "CourseSchedule.updateScheduleFromAdmin failed",
                    AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e),
                    e.getClass().getName(),
                    details("op", "ADMIN_UPDATE_SCHEDULE", "courseScheduleId", String.valueOf(courseScheduleId), "source", "ADMIN")
            );

            if (e instanceof PortalException) {
                throw (PortalException) e;
            }

            throw new PortalException("Admin schedule update failed for id=" + courseScheduleId, e);
        }
    }

    // ========================================================================
    //  UPSERT SNAPSHOT FROM CLS (unchanged)
    // ========================================================================

    @Override
    public CourseSchedule upsertFromCls(
            String courseCode,
            ScheduleResponse dto,
            ServiceContext serviceContext,
            String rawJson) throws PortalException {

        if (serviceContext == null) {
            serviceContext = ServiceContextThreadLocal.getServiceContext();
        }

        CourseSchedule entity = courseSchedulePersistence.fetchByCourseCode(courseCode);
        boolean isNew = (entity == null);

        if (!isNew) {
            boolean mustUpdate = false;

            Date lastMod = entity.getModifiedDate();
            LocalDate today = LocalDate.now(CLS_ZONE);

            if (lastMod == null) {
                mustUpdate = true;
            } else {
                LocalDate lastModLocal = lastMod.toInstant().atZone(CLS_ZONE).toLocalDate();
                if (!lastModLocal.isEqual(today)) {
                    mustUpdate = true;
                }
            }

            String existingIntake = entity.getIntakeNumber();
            String newIntake = (dto != null) ? dto.getIntakeNumber() : null;

            String existingNorm = normalize(existingIntake);
            String newNorm = normalize(newIntake);

            boolean intakeDiff =
                    (existingNorm == null && newNorm != null) ||
                            (existingNorm != null && !existingNorm.equals(newNorm));

            if (intakeDiff) {
                mustUpdate = true;
            }

            if (!mustUpdate) {
                if (_log.isDebugEnabled()) {
                    _log.debug("Skipping CSchedule upsert for courseCode=" + courseCode +
                            " (modified today and intakeNumber unchanged).");
                }
                return entity;
            }
        }

        Date now = new Date();

        if (isNew) {
            long id = counterLocalService.increment(CourseSchedule.class.getName());
            entity = courseSchedulePersistence.create(id);

            long groupId = (serviceContext != null) ? serviceContext.getScopeGroupId() : 0L;
            long companyId = (serviceContext != null) ? serviceContext.getCompanyId() : CompanyThreadLocal.getCompanyId();

            entity.setGroupId(groupId);
            entity.setCompanyId(companyId);
            entity.setCreateDate(now);
        }

        entity.setModifiedDate(now);
        entity.setCourseCode(courseCode);

        if (dto != null) {
            entity.setIntakeNumber(dto.getIntakeNumber());
            entity.setStartDate(parseDate(dto.getStartDate()));
            entity.setEndDate(parseDate(dto.getEndDate()));

            if (dto.getDuration() != null) {
                Integer h = dto.getDuration().getHours();
                Integer m = dto.getDuration().getMinutes();
                entity.setDurationHours(h != null ? h : 0);
                entity.setDurationMinutes(m != null ? m : 0);
            } else {
                entity.setDurationHours(0);
                entity.setDurationMinutes(0);
            }

            entity.setAvailability(dto.getAvailability() != null ? dto.getAvailability() : 0);
            entity.setVenue(dto.getVenue());
            entity.setDescription(dto.getDescription());
            entity.setAvailablePax(dto.getAvailablePax());
            entity.setAvailableWaitlist(dto.getAvailableWaitlist());
            entity.setLxpBuyUrl(dto.getLxpBuyUrl());
            entity.setLxpRoiUrl(dto.getLxpRoiUrl());
            entity.setImportantNote(dto.getImportantNote());
            entity.setScheduleDownloadUrl(dto.getScheduleDownloadUrl());

            if (dto.getError() != null) {
                entity.setErrorCode(dto.getError().getCode());
                entity.setErrorMessage(dto.getError().getMessage());
            } else {
                entity.setErrorCode(null);
                entity.setErrorMessage(null);
            }
        }

        entity.setRawJson(rawJson);

        CourseSchedule updated = courseSchedulePersistence.update(entity);

        if (_log.isDebugEnabled()) {
            _log.debug("Upserted CSchedule snapshot for courseCode=" + courseCode +
                    " id=" + updated.getCourseScheduleId());
        }

        return updated;
    }

    // ========================================================================
    //  INTERNAL MAPPERS
    // ========================================================================

    private ScheduleResponse mapEntityToScheduleResponse(CourseSchedule cs) {
        ScheduleResponse.Duration dur = new ScheduleResponse.Duration(
                cs.getDurationHours(),
                cs.getDurationMinutes()
        );

        ScheduleResponse.Error err = null;
        if (Validator.isNotNull(cs.getErrorCode()) || Validator.isNotNull(cs.getErrorMessage())) {
            err = new ScheduleResponse.Error(cs.getErrorCode(), cs.getErrorMessage());
        }

        return new ScheduleResponse(
                cs.getIntakeNumber(),
                formatDateForApi(cs.getStartDate()),
                formatDateForApi(cs.getEndDate()),
                dur,
                cs.getAvailability(),
                cs.getVenue(),
                cs.getDescription(),
                cs.getAvailablePax(),
                cs.getAvailableWaitlist(),
                cs.getLxpBuyUrl(),
                cs.getLxpRoiUrl(),
                cs.getImportantNote(),
                cs.getScheduleDownloadUrl(),
                err
        );
    }

    // ========================================================================
    //  DATE HELPERS (CLS only)
    // ========================================================================

    private Date parseDate(String value) {
        if (Validator.isNull(value)) return null;

        try {
            TemporalAccessor ta = CLS_FLEX.parse(value.trim());

            if (ta.isSupported(ChronoField.HOUR_OF_DAY)) {
                LocalDateTime ldt = LocalDateTime.from(ta);
                return Date.from(ldt.atZone(CLS_ZONE).toInstant());
            }

            LocalDate ld = LocalDate.from(ta);
            return Date.from(ld.atStartOfDay(CLS_ZONE).toInstant());

        } catch (Exception e) {
            _log.warn("Failed to parse CLS schedule date: " + value, e);
            return null;
        }
    }

    private String formatDateForApi(Date date) {
        if (date == null) return null;

        try {
            LocalDateTime ldt = date.toInstant().atZone(CLS_ZONE).toLocalDateTime();
            return ldt.format(CLS_DATE_TIME);
        } catch (Exception e) {
            _log.warn("Failed to format CLS datetime: " + date, e);
            return null;
        }
    }

    private String toJsonSafe(Object obj) {
        if (obj == null) return null;
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            _log.warn("Failed to serialize schedule object to JSON", e);
            return null;
        }
    }

    // ========================================================================
    //  AUDIT (new world)
    // ========================================================================

    private void writeAudit(
            long startMs,
            long endMs,
            String corrId,
            String requestId,
            String eventId,
            long companyId,
            long groupId,
            long userId,
            String courseCode,
            long ntucDTId,
            AuditSeverity severity,
            AuditStatus status,
            AuditStep step,
            AuditCategory category,
            String message,
            AuditErrorCode errorCode,
            String errorMessage,
            String exceptionClass,
            Map<String, String> details) {

        try {
            AuditEvent.Builder b = new AuditEvent.Builder()
                    .startTimeMs(startMs)
                    .endTimeMs(endMs)
                    .correlationId(corrId)
                    .requestId(requestId)
                    .eventId(eventId)
                    .jobRunId("")
                    .companyId(companyId)
                    .groupId(groupId)
                    .userId(userId)
                    .courseCode(safe(courseCode))
                    .ntucDTId(ntucDTId)
                    .severity(severity)
                    .status(status)
                    .step(step)
                    .category(category)
                    .message(message)
                    .errorCode(errorCode)
                    .errorMessage(errorMessage)
                    .exceptionClass(exceptionClass);

            if (details != null && !details.isEmpty()) {
                for (Map.Entry<String, String> e : details.entrySet()) {
                    b.detail(e.getKey(), e.getValue());
                }
            }

            _auditEventWriter.write(b.build());
        } catch (Exception ignore) {
            // Audit must never break runtime flow
        }
    }

    private static Map<String, String> details(String... kv) {
        Map<String, String> m = new HashMap<String, String>();
        for (int i = 0; i + 1 < kv.length; i += 2) {
            m.put(kv[i], kv[i + 1] == null ? "" : kv[i + 1]);
        }
        return m;
    }

    // ========================================================================
    //  SMALL HELPERS
    // ========================================================================

    private static String corrId() {
        String c = MdcUtil.getCorrId();
        return (c != null) ? c : UUID.randomUUID().toString();
    }

    private static long now() {
        return System.currentTimeMillis();
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }

    private static String safeMsg(Throwable t) {
        if (t == null) return "Unknown error";
        String m = t.getMessage();
        return (m == null || m.trim().isEmpty()) ? t.getClass().getSimpleName() : m;
    }

    private static String normalize(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
