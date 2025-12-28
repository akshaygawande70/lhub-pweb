package com.ntuc.notification.audit.internal;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.model.AuditLog;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Blob;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;

/**
 * Maps immutable {@link AuditEvent} into ServiceBuilder {@link AuditLog} row.
 *
 * Non-negotiables:
 * - No legacy "action/description" fields.
 * - Blob columns are stored as UTF-8 JSON/text inside a java.sql.Blob.
 * - Mapping must be deterministic and safe (no throwing).
 *
 * JUnit-testable: JSON serialization is pure Java.
 */
public class AuditEventDbMapper {

    static final int MAX_DETAILS_JSON_CHARS = 8000;

    // Keep this aligned with DB column size (Liferay default String is often 75 unless customized)
    private static final int DB_VARCHAR_75 = 75;

    /**
     * Persisted DB fingerprint MUST be fixed-length.
     * SHA-256 hex is always 64 chars and safe for default varchar sizes.
     */
    private static final int SHA256_HEX_LEN = 64;

    public void mapInto(AuditLog row, AuditEvent event) {
        if (row == null || event == null) {
            return;
        }

        // --- Liferay standard timestamps ---
        if (event.getStartTimeMs() > 0) {
            java.util.Date d = new java.util.Date(event.getStartTimeMs());
            row.setCreateDate(d);
            row.setModifiedDate(d);
        }

        // --- Tenant / actor ---
        row.setCompanyId(event.getCompanyId());
        row.setGroupId(event.getGroupId());
        row.setUserId(event.getUserId());

        // --- Business keys ---
        row.setCourseCode(safe(event.getCourseCode()));
        row.setNtucDTId(event.getNtucDTId());

        // --- Timeline ---
        row.setStartTimeMs(event.getStartTimeMs());
        row.setEndTimeMs(event.getEndTimeMs());
        row.setDurationMs(event.getDurationMs());

        // --- Correlation ---
        row.setCorrelationId(safe(event.getCorrelationId()));
        row.setJobRunId(safe(event.getJobRunId()));
        row.setRequestId(safe(event.getRequestId()));
        row.setEventId(safe(event.getEventId()));

        // --- Classification ---
        row.setSeverity(safeEnum(event.getSeverity()));
        row.setStatus(safeEnum(event.getStatus()));
        row.setStep(safeEnum(event.getStep()));
        row.setCategory(safeEnum(event.getCategory()));

        // --- Summary ---
        row.setMessage(fitDbVarchar(event.getMessage()));

        // --- Error ---
        row.setErrorCode(safeEnum(event.getErrorCode()));
        row.setErrorMessage(nullable(fitDbVarchar(event.getErrorMessage())));
        row.setExceptionClass(nullable(event.getExceptionClass()));

        // --- Email dedupe fingerprint (ONLY for email outcomes) ---
        // IMPORTANT: DB column stores HASH ONLY (fixed length). Raw stays in detailsJson.
        row.setAlertFingerprint(nullable(resolveAlertFingerprintHash(event)));

        // --- Stack ---
        row.setStackTraceHash(nullable(event.getStackTraceHash()));
        row.setStackTraceTruncated(toSqlBlob(nullable(event.getStackTraceTruncated())));

        // --- Details ---
        Map<String, String> merged = new LinkedHashMap<String, String>();
        if (event.getDetails() != null && !event.getDetails().isEmpty()) {
            merged.putAll(event.getDetails());
        }
        merged.put("fullMessage", safe(event.getMessage()));
        merged.put("fullErrorMessage", safe(event.getErrorMessage()));

        String detailsJson = toDetailsJson(merged);
        row.setDetailsJson(toSqlBlob(detailsJson));
    }

    /**
     * Returns SHA-256 hex of the raw alert fingerprint for DB storage.
     *
     * Raw fingerprint is expected in details under:
     * - "fingerprint" (preferred)
     * - "dedupeFingerprint" (backward compatible)
     *
     * Returns null when not an EMAIL_* outcome record.
     */
    private static String resolveAlertFingerprintHash(AuditEvent e) {
        String raw = resolveAlertFingerprintRaw(e);
        if (raw == null) {
            return null;
        }

        String hex = sha256Hex(raw);
        if (hex == null || hex.length() != SHA256_HEX_LEN) {
            return null;
        }

        return hex;
    }

    private static String resolveAlertFingerprintRaw(AuditEvent e) {
        if (e == null) {
            return null;
        }

        if (e.getCategory() != AuditCategory.ALERT_EMAIL) {
            return null;
        }

        AuditStep step = e.getStep();
        if (step != AuditStep.EMAIL_SENT
                && step != AuditStep.EMAIL_SUPPRESSED
                && step != AuditStep.EMAIL_SEND_FAILED) {
            return null;
        }

        if (e.getDetails() == null) {
            return null;
        }

        String fp = e.getDetails().get("fingerprint");
        if (fp == null) {
            fp = e.getDetails().get("dedupeFingerprint");
        }

        if (fp == null) {
            return null;
        }

        fp = fp.trim();
        return fp.isEmpty() ? null : fp;
    }

    static String toDetailsJson(Map<String, String> details) {
        if (details == null || details.isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder(256);
        sb.append("{");

        Iterator<Map.Entry<String, String>> it = details.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> e = it.next();
            sb.append("\"").append(escapeJson(e.getKey())).append("\":");
            sb.append("\"").append(escapeJson(e.getValue())).append("\"");
            if (it.hasNext()) {
                sb.append(",");
            }
            if (sb.length() > MAX_DETAILS_JSON_CHARS) {
                break;
            }
        }

        sb.append("}");

        if (sb.length() > MAX_DETAILS_JSON_CHARS) {
            return "{\"truncated\":\"true\"}";
        }

        return sb.toString();
    }

    private static Blob toSqlBlob(String text) {
        if (text == null) {
            return null;
        }

        try {
            byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
            return new SerialBlob(bytes);
        }
        catch (Exception ignore) {
            return null;
        }
    }

    private static String escapeJson(String s) {
        if (s == null) {
            return "";
        }

        StringBuilder out = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':
                    out.append("\\\"");
                    break;
                case '\\':
                    out.append("\\\\");
                    break;
                case '\n':
                    out.append("\\n");
                    break;
                case '\r':
                    out.append("\\r");
                    break;
                case '\t':
                    out.append("\\t");
                    break;
                default:
                    out.append(c);
            }
        }
        return out.toString();
    }

    private static String fitDbVarchar(String s) {
        if (s == null) {
            return "";
        }
        s = s.trim();
        if (s.length() <= DB_VARCHAR_75) {
            return s;
        }
        return s.substring(0, DB_VARCHAR_75);
    }

    private static String safeEnum(Enum<?> e) {
        return (e == null) ? "" : e.name();
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }

    private static String nullable(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static String sha256Hex(String raw) {
        if (raw == null) {
            return null;
        }
        String s = raw.trim();
        if (s.isEmpty()) {
            return null;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(s.getBytes(StandardCharsets.UTF_8));
            return toHex(digest);
        }
        catch (Exception ignore) {
            return null;
        }
    }

    private static String toHex(byte[] bytes) {
        char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }
}
