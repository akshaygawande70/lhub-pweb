package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AlertScope;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.constants.ParameterKeyEnum;

import java.util.Map;

/**
 * Resolves alert email policy from ParameterKey values.
 *
 * Plain Java helper; unit test this. Do NOT unit test OSGi components.
 *
 * Non-negotiable safety rule:
 * - Email-related audit steps MUST NEVER trigger emails (prevents recursion).
 */
public class AlertPolicyResolver {

    // Safe defaults (ops must explicitly enable)
    private static final boolean DEFAULT_EMAIL_ENABLED = false;
    private static final AuditSeverity DEFAULT_THRESHOLD = AuditSeverity.ERROR;
    private static final AlertScope DEFAULT_SCOPE = AlertScope.FLOW;

    private static final int MIN_DEDUPE_MINUTES = 1;
    private static final int MAX_DEDUPE_MINUTES = 1440; // 24 hours safety clamp

    public AlertPolicy resolve(Map<ParameterKeyEnum, Object> params, int defaultDedupeWindowMinutes) {
        int fallbackWindow = (defaultDedupeWindowMinutes <= 0) ? 5 : defaultDedupeWindowMinutes;

        // IMPORTANT:
        // In this codebase the existing key is CLS_ALERT_EMAIL_ENABLED.
        // If you later introduce ALERT_EMAIL_ENABLED in ParameterKeyEnum, switch to alias logic.
        boolean enabled = getBoolean(params, ParameterKeyEnum.CLS_ALERT_EMAIL_ENABLED, DEFAULT_EMAIL_ENABLED);

        AuditSeverity threshold =
            getSeverity(params, ParameterKeyEnum.ALERT_SEVERITY_THRESHOLD_EMAIL, DEFAULT_THRESHOLD);

        int dedupeMin = getInt(params, ParameterKeyEnum.ALERT_DEDUPE_WINDOW_MINUTES, fallbackWindow);

        // Clamp to safe bounds
        if (dedupeMin < MIN_DEDUPE_MINUTES) {
            dedupeMin = fallbackWindow;
        }
        if (dedupeMin < MIN_DEDUPE_MINUTES) {
            dedupeMin = MIN_DEDUPE_MINUTES;
        }
        if (dedupeMin > MAX_DEDUPE_MINUTES) {
            dedupeMin = MAX_DEDUPE_MINUTES;
        }

        AlertScope scope = getScope(params, ParameterKeyEnum.ALERT_SCOPE, DEFAULT_SCOPE);

        // Use configured recipient list (comma/semicolon separated).
        // If blank, downstream should treat as "cannot send" and audit EMAIL_SEND_FAILED.
        String toEmails = getString(params, ParameterKeyEnum.CLS_EMAIL_ALERTS_TO, "");

        return new AlertPolicy(enabled, threshold, dedupeMin, scope, toEmails);
    }

    public boolean isEligible(AlertPolicy policy, AuditEvent e) {
        if (e == null) {
            return false;
        }
        return isEligible(policy, e.getStep(), e.getStatus(), e.getSeverity());
    }

    boolean isEligible(AlertPolicy policy, AuditStep step, AuditStatus status, AuditSeverity severity) {
        if (policy == null) {
            return false;
        }

        if (!policy.isEmailEnabled()) {
            return false;
        }

        // Hard-stop: never alert on email-audit steps (prevents recursion / alert storms).
        if (step == AuditStep.EMAIL_SENT
                || step == AuditStep.EMAIL_SUPPRESSED
                || step == AuditStep.EMAIL_SEND_FAILED) {
            return false;
        }

        if (status != AuditStatus.FAILED && status != AuditStatus.PARTIAL) {
            return false;
        }

        if (severity == null) {
            return false;
        }

        return severityRank(severity) >= severityRank(policy.getSeverityThreshold());
    }

    int severityRank(AuditSeverity s) {
        if (s == null) {
            return 0;
        }
        switch (s) {
            case ERROR:
                return 3;
            case WARNING:
                return 2;
            case INFO:
            default:
                return 1;
        }
    }

    private boolean getBoolean(Map<ParameterKeyEnum, Object> params, ParameterKeyEnum key, boolean def) {
        try {
            Object v = params == null ? null : params.get(key);
            if (v == null) {
                return def;
            }

            String s = String.valueOf(v).trim();
            if (s.isEmpty()) {
                return def;
            }

            return "true".equalsIgnoreCase(s) || "1".equals(s) || "yes".equalsIgnoreCase(s);
        }
        catch (Exception ignore) {
            return def;
        }
    }

    private String getString(Map<ParameterKeyEnum, Object> params, ParameterKeyEnum key, String def) {
        try {
            Object v = params == null ? null : params.get(key);
            if (v == null) {
                return def;
            }
            String s = String.valueOf(v).trim();
            return s.isEmpty() ? def : s;
        }
        catch (Exception ignore) {
            return def;
        }
    }

    private int getInt(Map<ParameterKeyEnum, Object> params, ParameterKeyEnum key, int def) {
        try {
            Object v = params == null ? null : params.get(key);
            if (v == null) {
                return def;
            }
            return Integer.parseInt(String.valueOf(v).trim());
        }
        catch (Exception ignore) {
            return def;
        }
    }

    private AuditSeverity getSeverity(Map<ParameterKeyEnum, Object> params, ParameterKeyEnum key, AuditSeverity def) {
        try {
            Object v = params == null ? null : params.get(key);
            if (v == null) {
                return def;
            }

            String s = String.valueOf(v).trim();
            if (s.isEmpty()) {
                return def;
            }

            return AuditSeverity.valueOf(s.toUpperCase());
        }
        catch (Exception ignore) {
            return def;
        }
    }

    private AlertScope getScope(Map<ParameterKeyEnum, Object> params, ParameterKeyEnum key, AlertScope def) {
        try {
            Object v = params == null ? null : params.get(key);
            if (v == null) {
                return def;
            }

            String s = String.valueOf(v).trim();
            if (s.isEmpty()) {
                return def;
            }

            return AlertScope.valueOf(s.toUpperCase());
        }
        catch (Exception ignore) {
            return def;
        }
    }
}
