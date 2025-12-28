package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import java.util.Objects;

/**
 * Factory for audit events produced by the email trigger itself.
 *
 * Rules:
 * - Must not include secrets.
 * - Must include correlation identifiers so ops can reconstruct timeline.
 */
public class AuditEmailFailureEventFactory {

    private static final int DEFAULT_DEDUPE_WINDOW_MINUTES = 5;

    public AuditEvent emailSendFailed(
            AuditEvent triggeringEvent,
            String toEmail,
            String subject,
            String fingerprint,
            AlertPolicy policy,
            Exception ex) {

        Objects.requireNonNull(triggeringEvent, "triggeringEvent");
        Objects.requireNonNull(ex, "ex");

        long now = System.currentTimeMillis();

        String scope = (policy == null || policy.getScope() == null) ? "FLOW" : policy.getScope().name();
        int window = (policy == null) ? DEFAULT_DEDUPE_WINDOW_MINUTES : policy.getDedupeWindowMinutes();

        return AuditEvent.builder()
                .companyId(triggeringEvent.getCompanyId())
                .groupId(triggeringEvent.getGroupId())
                .userId(triggeringEvent.getUserId())
                .courseCode(triggeringEvent.getCourseCode())
                .ntucDTId(triggeringEvent.getNtucDTId())
                .correlationId(triggeringEvent.getCorrelationId())
                .requestId(triggeringEvent.getRequestId())
                .eventId(triggeringEvent.getEventId())
                .jobRunId(triggeringEvent.getJobRunId())
                .startTimeMs(now)
                .endTimeMs(now)
                .severity(AuditSeverity.ERROR)
                .status(AuditStatus.FAILED)
                .step(AuditStep.EMAIL_SEND_FAILED)
                .category(triggeringEvent.getCategory())
                .errorCode(AuditErrorCode.EMAIL_SEND_FAILED)
                .message("Audit email send failed")
                .errorMessage(ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage())
                .exceptionClass(ex.getClass().getName())
                .detail("toEmail", safe(toEmail))
                .detail("subject", safe(subject))
                .detail("dedupeFingerprint", safe(fingerprint))
                .detail("alertScope", scope)
                .detail("dedupeWindowMinutes", String.valueOf(window))
                .build();
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
