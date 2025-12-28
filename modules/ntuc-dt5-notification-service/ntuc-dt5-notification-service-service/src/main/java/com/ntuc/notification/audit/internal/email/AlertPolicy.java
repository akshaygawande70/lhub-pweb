package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.constants.AlertScope;
import com.ntuc.notification.audit.api.constants.AuditSeverity;

/**
 * Resolved email alert policy derived from ParameterKeys.
 *
 * <p>
 * Immutable by design.
 * This is the single contract between parameter resolution and
 * audit-driven email triggering.
 * </p>
 *
 * <p>
 * IMPORTANT:
 * - This class does NOT decide eligibility.
 * - This class does NOT send email.
 * - It only encapsulates resolved policy values safely.
 * </p>
 */
public final class AlertPolicy {

    private static final int MIN_DEDUPE_MINUTES = 1;
    private static final int MAX_DEDUPE_MINUTES = 1440; // 24 hours safety clamp

    private final boolean emailEnabled;
    private final AuditSeverity severityThreshold;
    private final int dedupeWindowMinutes;
    private final AlertScope scope;
    private final String fallbackToEmail;

    public AlertPolicy(
            boolean emailEnabled,
            AuditSeverity severityThreshold,
            int dedupeWindowMinutes,
            AlertScope scope,
            String fallbackToEmail) {

        this.emailEnabled = emailEnabled;

        this.severityThreshold =
            (severityThreshold == null) ? AuditSeverity.ERROR : severityThreshold;

        this.dedupeWindowMinutes = clampDedupeMinutes(dedupeWindowMinutes);

        this.scope = (scope == null) ? AlertScope.FLOW : scope;

        this.fallbackToEmail =
            (fallbackToEmail == null) ? "" : fallbackToEmail.trim();
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public AuditSeverity getSeverityThreshold() {
        return severityThreshold;
    }

    /**
     * Dedupe window in minutes.
     *
     * <p>
     * Always clamped between {@value #MIN_DEDUPE_MINUTES}
     * and {@value #MAX_DEDUPE_MINUTES}.
     * </p>
     */
    public int getDedupeWindowMinutes() {
        return dedupeWindowMinutes;
    }

    public AlertScope getScope() {
        return scope;
    }

    /**
     * Recipient list for alert emails.
     *
     * <p>
     * May be empty if misconfigured.
     * Caller MUST treat empty value as a fatal configuration error
     * and audit EMAIL_SEND_FAILED.
     * </p>
     */
    public String getFallbackToEmail() {
        return fallbackToEmail;
    }

    private static int clampDedupeMinutes(int v) {
        if (v < MIN_DEDUPE_MINUTES) {
            return MIN_DEDUPE_MINUTES;
        }
        if (v > MAX_DEDUPE_MINUTES) {
            return MAX_DEDUPE_MINUTES;
        }
        return v;
    }
}
