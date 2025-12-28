package com.ntuc.notification.rest.internal.processor;

import com.ntuc.notification.audit.api.AuditEvent;

/**
 * Builds REST intake audit events.
 *
 * This factory is service-side and must remain deterministic.
 * Email alerting MUST NOT happen here; only persistence.
 */
public interface CourseRestAuditEventFactory {

    AuditEvent started(
            long startMs,
            long endMs,
            String correlationId,
            String jobRunId,
            String requestId,
            int eventCount
    );

    AuditEvent success(
            long startMs,
            long endMs,
            String correlationId,
            String jobRunId,
            String requestId,
            int eventCount,
            int successCount
    );

    AuditEvent skipped(
            long startMs,
            long endMs,
            String correlationId,
            String jobRunId,
            String requestId,
            String reason
    );

    AuditEvent dispatchFailed(
            long startMs,
            long endMs,
            String correlationId,
            String jobRunId,
            String requestId,
            String courseCode,
            Throwable error
    );
}
