package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.email.api.AlertEmailCategory;

/**
 * Maps a persisted AuditEvent to a STRICT AlertEmailCategory used for:
 * - template selection
 * - dedupe fingerprinting
 *
 * <p>IMPORTANT:
 * - AuditCategory is domain-only (CLS / JOURNAL_ARTICLE / DT5_FLOW / ALERT_EMAIL)
 * - AlertEmailCategory is email-only (CLS_FAILURE / JA_FAILURE / DT5_FAILURE)
 *
 * <p>Recursion guard:
 * - Events in AuditCategory.ALERT_EMAIL must NOT trigger further emails.</p>
 */
public final class AlertEmailCategoryResolver {

    /**
     * Resolve alert category for email decisions.
     *
     * @return null only when recursion guard blocks (AuditCategory.ALERT_EMAIL)
     */
    public AlertEmailCategory resolve(AuditEvent event) {
        if (event == null || event.getCategory() == null) {
            return AlertEmailCategory.DT5_FAILURE;
        }

        AuditCategory auditCategory = event.getCategory();

        // Prevent email recursion storms.
        if (auditCategory == AuditCategory.ALERT_EMAIL) {
            return null;
        }

        if (auditCategory == AuditCategory.CLS) {
            return AlertEmailCategory.CLS_FAILURE;
        }

        if (auditCategory == AuditCategory.JOURNAL_ARTICLE) {
            return AlertEmailCategory.JA_FAILURE;
        }

        // DT5_FLOW (and any future domains) map to DT5_FAILURE for alerting.
        return AlertEmailCategory.DT5_FAILURE;
    }

    /**
     * Variant that never returns null.
     *
     * Caller use-case:
     * - when you want a safe default even if recursion guard blocks.
     *
     * NOTE: Use this carefully; defaulting ALERT_EMAIL -> DT5_FAILURE can cause recursion.
     * So this method explicitly returns DT5_FAILURE only when event/category is null.
     * If recursion guard triggers, it still returns null to stay safe.
     */
    public AlertEmailCategory resolveOrDefault(AuditEvent event, AlertEmailCategory defaultCategory) {
        AlertEmailCategory resolved = resolve(event);
        if (resolved != null) {
            return resolved;
        }
        return defaultCategory;
    }
}
