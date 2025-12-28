package com.ntuc.notification.rest.internal.processor;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import java.util.Objects;

/**
 * Default REST audit factory.
 *
 * IMPORTANT:
 * - This class must only build audit events (no emails).
 * - details must be sanitized and size-capped by upstream sanitizers if present.
 */
public class DefaultCourseRestAuditEventFactory implements CourseRestAuditEventFactory {

    private final AuditStep step;
    private final AuditCategory category;

    public DefaultCourseRestAuditEventFactory(AuditStep step, AuditCategory category) {
        this.step = Objects.requireNonNull(step, "step");
        this.category = Objects.requireNonNull(category, "category");
    }

    @Override
    public AuditEvent started(
            long startMs,
            long endMs,
            String correlationId,
            String jobRunId,
            String requestId,
            int eventCount) {

        return base(startMs, endMs, correlationId, jobRunId, requestId)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.STARTED)
                .message("REST intake accepted")
                .errorCode(AuditErrorCode.NONE)
                .detail("eventCount", String.valueOf(eventCount))
                .build();
    }

    @Override
    public AuditEvent success(
            long startMs,
            long endMs,
            String correlationId,
            String jobRunId,
            String requestId,
            int eventCount,
            int successCount) {

        return base(startMs, endMs, correlationId, jobRunId, requestId)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.SUCCESS)
                .message("REST intake dispatched")
                .errorCode(AuditErrorCode.NONE)
                .detail("eventCount", String.valueOf(eventCount))
                .detail("successCount", String.valueOf(successCount))
                .build();
    }

    @Override
    public AuditEvent skipped(
            long startMs,
            long endMs,
            String correlationId,
            String jobRunId,
            String requestId,
            String reason) {

        return base(startMs, endMs, correlationId, jobRunId, requestId)
                .severity(AuditSeverity.WARNING)
                .status(AuditStatus.SKIPPED)
                .message("REST intake skipped")
                .errorCode(AuditErrorCode.NONE)
                .detail("reason", safe(reason))
                .build();
    }

    @Override
    public AuditEvent dispatchFailed(
            long startMs,
            long endMs,
            String correlationId,
            String jobRunId,
            String requestId,
            String courseCode,
            Throwable error) {

        String msg = (error == null || error.getMessage() == null) ? "Dispatch failed" : error.getMessage();

        return base(startMs, endMs, correlationId, jobRunId, requestId)
                .severity(AuditSeverity.ERROR)
                .status(AuditStatus.FAILED)
                .message("REST dispatch failed")
                .errorCode(AuditErrorCode.DT5_FAILURE)
                .courseCode(safe(courseCode))
                .errorMessage(msg)
                .exceptionClass(error != null ? error.getClass().getName() : null)
                .detail("dispatch", "failed")
                .build();
    }

    private AuditEvent.Builder base(long startMs, long endMs, String correlationId, String jobRunId, String requestId) {
        return new AuditEvent.Builder()
                .startTimeMs(startMs)
                .endTimeMs(endMs)
                .correlationId(correlationId)
                .jobRunId(jobRunId)
                .requestId(requestId)
                .eventId(java.util.UUID.randomUUID().toString())
                .companyId(0L)
                .groupId(0L)
                .userId(0L)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.STARTED)
                .step(step)
                .category(category)
                .message("REST audit");
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }
}
