package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AlertScope;
import com.ntuc.notification.audit.util.MessageNormalizer;
import com.ntuc.notification.email.api.AlertEmailCategory;

/**
 * Builds deterministic alert fingerprints for DB-backed deduplication.
 *
 * Contract:
 * - Must never throw
 * - Null event produces deterministic, minimal fingerprint
 */
public class AlertFingerprintBuilder {

    public String build(AlertPolicy policy, AuditEvent event, AlertEmailCategory emailCategory) {

        if (event == null) {
            // category | errorCode | normalizedMessage | scopeKey
            return safe(emailCategory == null ? "" : emailCategory.name())
                + "||"
                + MessageNormalizer.normalize(null)
                + "|";
        }

        String errorCode =
            event.getErrorCode() == null ? "" : event.getErrorCode().name();

        String exClass = safe(event.getExceptionClass());

        String normalizedMsg =
            MessageNormalizer.normalize(event.getErrorMessage());

        String scopeKey = resolveScopeKey(policy, event);

        return safe(emailCategory == null ? "" : emailCategory.name())
            + "|" + errorCode
            + "|" + exClass
            + "|" + normalizedMsg
            + "|" + scopeKey;
    }

    private String resolveScopeKey(AlertPolicy policy, AuditEvent event) {

        AlertScope scope =
            (policy == null || policy.getScope() == null)
                ? AlertScope.FLOW
                : policy.getScope();

        switch (scope) {
            case COURSE:
                return safe(event.getCourseCode());
            case JOB:
                return safe(event.getJobRunId());
            case FLOW:
            default:
                return safe(event.getCorrelationId());
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
