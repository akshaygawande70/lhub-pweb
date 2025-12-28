package com.ntuc.notification.email;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AlertContexts {

    private AlertContexts() {
        // Utility class
    }

    // ===== 2. Integration Failures =====

    /** CLS API unreachable or 5xx for X minutes */
    public static Map<String, Object> clsApiUnreachable(
        Integer httpStatus,
        String endpoint,
        Long durationMs,
        Integer attempts,
        Map<String, Object> counts,
        String retryPolicy,
        String system,
        String env
    ) {
        Map<String, Object> m = base("CRITICAL", "CLS API Unreachable", system, env);
        put(m, "summary", "CLS returned 5xx/unreachable; sync paused.");
        put(m, "reason", "Upstream outage or network issue.");
        put(m, "httpStatus", httpStatus);
        put(m, "endpoint", endpoint);
        put(m, "durationMs", durationMs);
        put(m, "attempts", attempts); // FIXED: was "attempt"
        put(m, "retryPolicy", retryPolicy);
        put(m, "counts", counts);
        put(m, "notifyGroup", "DevOps, Integration Team");
        return m;
    }

    /** Invalid / unexpected CLS API response structure */
    public static Map<String, Object> invalidClsResponse(
        String endpoint,
        String reason,
        String payloadSnippet,
        String error,
        String system,
        String env
    ) {
        Map<String, Object> m = base("CRITICAL", "Invalid CLS Response", system, env);
        put(m, "summary", "Unexpected JSON structure from CLS; mapping blocked.");
        put(m, "reason", reason);
        put(m, "endpoint", endpoint);
        put(m, "payloadSnippet", payloadSnippet);
        put(m, "error", error);
        put(m, "notifyGroup", "DevOps, Integration Team");
        return m;
    }

    /** Critical field missing in course data (e.g., courseCode, startDate) */
    public static Map<String, Object> criticalFieldMissing(
        Collection<String> criticalFields,
        String payloadSnippet,
        String system,
        String env
    ) {
        Map<String, Object> m = base("HIGH", "Critical Field Missing in Course Data", system, env);
        put(m, "summary", "Incoming record missing critical fields.");
        put(m, "criticalFields", criticalFields);
        put(m, "payloadSnippet", payloadSnippet);
        put(m, "notifyGroup", "Content & Ops");
        return m;
    }

    // ===== 3. Processing Failures =====

    /** Critical field change rejected by Liferay */
    public static Map<String, Object> criticalFieldChangeRejected(
        String courseCode,
        String fieldDiff,
        String error,
        String system,
        String env
    ) {
        Map<String, Object> m = base("CRITICAL", "Critical Field Change Rejected", system, env);
        put(m, "courseCode", courseCode);
        put(m, "summary", "Attempt to change critical field was rejected.");
        put(m, "fieldDiff", fieldDiff);
        put(m, "error", error);
        put(m, "notifyGroup", "Content & Ops");
        return m;
    }

    /** JournalArticle creation/update failed for critical course */
    public static Map<String, Object> journalArticleUpdateFailed(
        String courseCode,
        String reason,
        String error,
        List<Map<String, String>> links,
        String system,
        String env
    ) {
        Map<String, Object> m = base("CRITICAL", "JournalArticle Update Failed", system, env);
        put(m, "courseCode", courseCode);
        put(m, "summary", "Failed to create/update article for critical course.");
        put(m, "reason", reason);
        put(m, "error", error);
        put(m, "links", links);
        put(m, "notifyGroup", "Content & Ops");
        return m;
    }

    /** Course sync job aborted mid-run */
    public static Map<String, Object> courseSyncAborted(
        Long processed,
        Long total,
        Long failed,
        String error,
        String retryPolicy,
        String system,
        String env
    ) {
        Map<String, Object> m = base("CRITICAL", "Course Sync Aborted Mid-Run", system, env);
        put(m, "summary", "Sync stopped mid-run; data may be inconsistent.");

        Map<String, Object> counts = new HashMap<String, Object>();
        if (total != null) {
            counts.put("total", total);
        }
        if (processed != null) {
            counts.put("processed", processed);
        }
        if (failed != null) {
            counts.put("failed", failed);
        }

        put(m, "counts", counts.isEmpty() ? null : counts);
        put(m, "error", error);
        put(m, "retryPolicy", retryPolicy);
        put(m, "notifyGroup", "DevOps, Content & Ops");
        return m;
    }

    // ===== Common helpers =====

    private static Map<String, Object> base(String severity, String alertType, String system, String env) {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("severity", or(severity, "CRITICAL"));
        m.put("alertType", or(alertType, "System Alert"));
        m.put("system", or(system, "DT5"));
        m.put("env", or(env, "prod"));
        return m;
    }

    private static <T> T or(T v, T def) {
        return v != null ? v : def;
    }

    private static void put(Map<String, Object> m, String k, Object v) {
        if (v == null) {
            return;
        }
        if (v instanceof CharSequence) {
            String s = v.toString();
            if (s.trim().isEmpty()) {
                return;
            }
        }
        m.put(k, v);
    }
}
