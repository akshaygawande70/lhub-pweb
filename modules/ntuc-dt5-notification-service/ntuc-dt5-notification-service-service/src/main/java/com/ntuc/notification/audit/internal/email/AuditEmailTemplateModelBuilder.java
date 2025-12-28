package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.AuditEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Builds template tokens for audit-driven alert emails.
 *
 * Contract:
 * - Never throws (even if AuditEvent getters unbox null internally)
 * - Never returns null
 * - Always includes all known tokens as keys (values may be empty string)
 * - Uses only persisted AuditEvent data
 */
public class AuditEmailTemplateModelBuilder {

    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    // ================= TEMPLATE TOKENS =================

    static final String T_AUDIT_ID           = "[$AUDIT_ID$]";
    static final String T_CORRELATION_ID     = "[$CORRELATION_ID$]";
    static final String T_COURSE_CODE        = "[$COURSE_CODE$]";
    static final String T_NTUC_DT_ID         = "[$NTUC_DT_ID$]";

    static final String T_STEP               = "[$STEP$]";
    static final String T_ERROR_CODE         = "[$ERROR_CODE$]";
    static final String T_ERROR_MESSAGE      = "[$ERROR_MESSAGE$]";
    static final String T_TIMESTAMP          = "[$TIMESTAMP$]";

    static final String T_ENDPOINT           = "[$ENDPOINT$]";
    static final String T_RESPONSE_CODE      = "[$RESPONSE_CODE$]";
    static final String T_DDM_STRUCTURE_KEY  = "[$DDM_STRUCTURE_KEY$]";
    static final String T_DDM_TEMPLATE_KEY   = "[$DDM_TEMPLATE_KEY$]";
    static final String T_DEDUPE_FINGERPRINT = "[$DEDUPE_FINGERPRINT$]";

    // ================= DETAILS KEYS =================

    static final String D_ENDPOINT          = "endpoint";
    static final String D_RESPONSE_CODE     = "responseCode";
    static final String D_DDM_STRUCTURE_KEY = "ddmStructureKey";
    static final String D_DDM_TEMPLATE_KEY  = "ddmTemplateKey";

    /**
     * Build template token map.
     *
     * IMPORTANT:
     * - Always returns a non-null map
     * - Always contains all known tokens as keys (at minimum empty string values)
     */
    public Map<String, String> build(AuditEvent e, String auditId, String fingerprint) {
        Map<String, String> m = new HashMap<String, String>();
        prefill(m);

        // Hard no-throw: swallow absolutely everything and return best-effort map.
        try {
            // Core identifiers
            m.put(T_AUDIT_ID, safeToString(auditId));
            m.put(T_CORRELATION_ID, safeGetString(e, new StringGetter() {
                @Override public String get(AuditEvent x) { return x.getCorrelationId(); }
            }));
            m.put(T_COURSE_CODE, safeGetString(e, new StringGetter() {
                @Override public String get(AuditEvent x) { return x.getCourseCode(); }
            }));
            m.put(T_NTUC_DT_ID, safeGetObjectAsString(e, new ObjectGetter() {
                @Override public Object get(AuditEvent x) { return safeGetNtucDtId(x); }
            }));

            // Audit basics
            m.put(T_STEP, safeGetEnumName(e, new EnumGetter() {
                @Override public Enum<?> get(AuditEvent x) { return (Enum<?>) x.getStep(); }
            }));
            m.put(T_ERROR_CODE, safeGetEnumName(e, new EnumGetter() {
                @Override public Enum<?> get(AuditEvent x) { return (Enum<?>) x.getErrorCode(); }
            }));
            m.put(T_ERROR_MESSAGE, safeGetString(e, new StringGetter() {
                @Override public String get(AuditEvent x) { return x.getErrorMessage(); }
            }));

            // Timestamp (endTimeMs > startTimeMs > empty)
            long ts = resolveTimestampSafe(e);
            m.put(T_TIMESTAMP, formatUtc(ts));

            // Optional detail tokens
            m.put(T_ENDPOINT, safeDetail(e, D_ENDPOINT));
            m.put(T_RESPONSE_CODE, safeDetail(e, D_RESPONSE_CODE));
            m.put(T_DDM_STRUCTURE_KEY, safeDetail(e, D_DDM_STRUCTURE_KEY));
            m.put(T_DDM_TEMPLATE_KEY, safeDetail(e, D_DDM_TEMPLATE_KEY));

            // Ops-safe helper
            m.put(T_DEDUPE_FINGERPRINT, safeToString(fingerprint));
        }
        catch (Throwable ignore) {
            // return prefilled map
        }

        // Absolute guarantee: never return null
        return m;
    }

    private static void prefill(Map<String, String> m) {
        m.put(T_AUDIT_ID, "");
        m.put(T_CORRELATION_ID, "");
        m.put(T_COURSE_CODE, "");
        m.put(T_NTUC_DT_ID, "");

        m.put(T_STEP, "");
        m.put(T_ERROR_CODE, "");
        m.put(T_ERROR_MESSAGE, "");
        m.put(T_TIMESTAMP, "");

        m.put(T_ENDPOINT, "");
        m.put(T_RESPONSE_CODE, "");
        m.put(T_DDM_STRUCTURE_KEY, "");
        m.put(T_DDM_TEMPLATE_KEY, "");
        m.put(T_DEDUPE_FINGERPRINT, "");
    }

    // ================= SAFE ACCESS =================

    /**
     * If AuditEvent#getNtucDTId() unboxes null internally, it can throw NPE.
     * We must guard it.
     */
    private static Object safeGetNtucDtId(AuditEvent e) {
        if (e == null) {
            return null;
        }
        try {
            return e.getNtucDTId();
        }
        catch (Throwable ignore) {
            return null;
        }
    }

    private static String safeGetString(AuditEvent e, StringGetter g) {
        if (e == null || g == null) {
            return "";
        }
        try {
            return safeToString(g.get(e));
        }
        catch (Throwable ignore) {
            return "";
        }
    }

    private static String safeGetEnumName(AuditEvent e, EnumGetter g) {
        if (e == null || g == null) {
            return "";
        }
        try {
            Enum<?> v = g.get(e);
            return (v == null) ? "" : v.name();
        }
        catch (Throwable ignore) {
            return "";
        }
    }

    private static String safeGetObjectAsString(AuditEvent e, ObjectGetter g) {
        if (e == null || g == null) {
            return "";
        }
        try {
            Object v = g.get(e);
            return (v == null) ? "" : String.valueOf(v);
        }
        catch (Throwable ignore) {
            return "";
        }
    }

    private static long resolveTimestampSafe(AuditEvent e) {
        if (e == null) {
            return 0L;
        }

        // IMPORTANT: getters may throw NPE due to internal unboxing.
        try {
            Object endObj = e.getEndTimeMs();
            Long end = (endObj instanceof Long) ? (Long) endObj : null;
            if (end != null && end.longValue() > 0) {
                return end.longValue();
            }
        }
        catch (Throwable ignore) {
        }

        try {
            Object startObj = e.getStartTimeMs();
            Long start = (startObj instanceof Long) ? (Long) startObj : null;
            if (start != null && start.longValue() > 0) {
                return start.longValue();
            }
        }
        catch (Throwable ignore) {
        }

        return 0L;
    }

    private static String safeDetail(AuditEvent e, String key) {
        if (e == null || key == null) {
            return "";
        }
        try {
            Map<String, String> details = e.getDetails();
            if (details == null) {
                return "";
            }
            String v = details.get(key);
            return (v == null) ? "" : v;
        }
        catch (Throwable ignore) {
            return "";
        }
    }

    private static String safeToString(Object v) {
        return (v == null) ? "" : String.valueOf(v);
    }

    private static String formatUtc(long ms) {
        if (ms <= 0) {
            return "";
        }
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS 'UTC'", Locale.ROOT);
        sdf.setTimeZone(UTC);
        return sdf.format(new Date(ms));
    }

    // ================= MINI FUNCTIONAL INTERFACES (JDK8-safe) =================

    private interface StringGetter {
        String get(AuditEvent e);
    }

    private interface EnumGetter {
        Enum<?> get(AuditEvent e);
    }

    private interface ObjectGetter {
        Object get(AuditEvent e);
    }
}
