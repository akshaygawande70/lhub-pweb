package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Builds derived audit events for email suppression.
 *
 * SECURITY:
 * - No secrets
 * - No raw payloads
 * - Structured details only
 */
public class AuditSuppressionEventFactory {

    private AuditSuppressionEventFactory() {
        // util
    }

    public static AuditEvent emailSuppressed(AuditEvent source, String fingerprint, AlertPolicy policy) {
        Map<String, String> details = new HashMap<>();
        details.put("dedupeFingerprint", safe(fingerprint));
        details.put("dedupeWindowMinutes", String.valueOf(policy.getDedupeWindowMinutes()));
        details.put("alertScope", policy.getScope().name());

        // IMPORTANT:
        // Replace this builder usage with your canonical AuditEventFactory if your AuditEvent doesn't expose a builder.
        return AuditEvent.builder()
                .companyId(source.getCompanyId())
                .groupId(source.getGroupId())
                .userId(source.getUserId())
                .courseCode(source.getCourseCode())
                .ntucDTId(source.getNtucDTId())
                .correlationId(source.getCorrelationId())
                .requestId(source.getRequestId())
                .eventId(source.getEventId())
                .jobRunId(source.getJobRunId())
                .category(source.getCategory())
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.SKIPPED)
                .step(AuditStep.EMAIL_SUPPRESSED)
                .message("Email suppressed by dedupe window")
                .details(detailsOrEmpty(details))
                .build();
    }

    private static Map<String, String> detailsOrEmpty(Map<String, String> d) {
        return d == null ? Collections.emptyMap() : d;
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
