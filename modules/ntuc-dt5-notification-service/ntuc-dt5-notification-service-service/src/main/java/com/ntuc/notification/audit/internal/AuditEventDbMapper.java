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
 * Maps immutable {@link AuditEvent} into a ServiceBuilder {@link AuditLog} entity.
 *
 * <p><strong>Business purpose:</strong>
 * Provides a single, deterministic translation layer that persists audit trail data
 * for compliance, troubleshooting, alert deduplication, and operational visibility.</p>
 *
 * <p><strong>Technical purpose:</strong>
 * Converts API-layer audit events into DB-safe ServiceBuilder fields while enforcing
 * column constraints, UTF-8 Blob encoding, and failure isolation.</p>
 *
 * <p>Design guarantees:</p>
 * <ul>
 *   <li>No legacy action/description fields are used.</li>
 *   <li>All JSON/text payloads are stored as UTF-8 {@link Blob}.</li>
 *   <li>Mapping never throws; failures degrade safely to null/empty.</li>
 * </ul>
 *
 * <p>This class is plain Java and fully unit-testable.</p>
 *
 * @author @akshaygawande
 */
public class AuditEventDbMapper {

    /**
     * Maximum allowed JSON size for details payload before truncation.
     */
    static final int MAX_DETAILS_JSON_CHARS = 8000;

    /**
     * Default Liferay varchar length used for summary-style columns.
     */
    private static final int DB_VARCHAR_75 = 75;

    /**
     * SHA-256 hex length. Stored fingerprints must be fixed-length.
     */
    private static final int SHA256_HEX_LEN = 64;

    /**
     * Maps values from an {@link AuditEvent} into an {@link AuditLog} row.
     *
     * <p><strong>Business purpose:</strong>
     * Persists a single audit record representing a workflow step or alert outcome.</p>
     *
     * <p><strong>Technical purpose:</strong>
     * Copies, normalizes, truncates, and encodes event fields into ServiceBuilder-safe
     * representations without throwing exceptions.</p>
     *
     * <p><strong>Inputs / invariants:</strong></p>
     * <ul>
     *   <li>Both parameters may be null; method is no-op in that case.</li>
     *   <li>AuditEvent is treated as immutable.</li>
     * </ul>
     *
     * <p><strong>Side effects:</strong></p>
     * <ul>
     *   <li>Mutates the provided {@link AuditLog} entity.</li>
     *   <li>Creates {@link Blob} instances for large text fields.</li>
     * </ul>
     *
     * <p><strong>Audit behavior:</strong>
     * This method does not emit audit events; it is invoked as part of audit persistence.</p>
     *
     * <p><strong>Return semantics:</strong>
     * Void; never throws.</p>
     */
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

        // --- Email alert dedupe fingerprint ---
        // Stored as SHA-256 hash only; raw fingerprint remains in detailsJson
        row.setAlertFingerprint(nullable(resolveAlertFingerprintHash(event)));

        // --- Stack trace ---
        row.setStackTraceHash(nullable(event.getStackTraceHash()));
        row.setStackTraceTruncated(toSqlBlob(nullable(event.getStackTraceTruncated())));

        // --- Details JSON ---
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
     * Resolves and hashes the alert fingerprint for DB storage.
     *
     * <p><strong>Business purpose:</strong>
     * Enables deterministic deduplication of email alerts.</p>
     *
     * <p><strong>Technical purpose:</strong>
     * Extracts the raw fingerprint from details and stores only its SHA-256 hash.</p>
     *
     * <p><strong>Return semantics:</strong>
     * Returns null when not applicable or invalid.</p>
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

    /**
     * Extracts the raw alert fingerprint from the event details.
     *
     * <p>Applicable only for {@link AuditCategory#ALERT_EMAIL} outcomes.</p>
     */
    private static String resolveAlertFingerprintRaw(AuditEvent e) {
        if (e == null || e.getCategory() != AuditCategory.ALERT_EMAIL) {
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

        return nullable(fp);
    }

    /**
     * Serializes a key-value map into a compact JSON object string.
     */
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

    /**
     * Converts UTF-8 text into a SQL {@link Blob}.
     */
    private static Blob toSqlBlob(String text) {
        if (text == null) {
            return null;
        }
        try {
            return new SerialBlob(text.getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception ignore) {
            return null;
        }
    }

    /**
     * Escapes a string for safe JSON embedding.
     */
    private static String escapeJson(String s) {
        if (s == null) {
            return "";
        }

        StringBuilder out = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': out.append("\\\""); break;
                case '\\': out.append("\\\\"); break;
                case '\n': out.append("\\n"); break;
                case '\r': out.append("\\r"); break;
                case '\t': out.append("\\t"); break;
                default: out.append(c);
            }
        }
        return out.toString();
    }

    /**
     * Truncates text to DB varchar limits.
     */
    private static String fitDbVarchar(String s) {
        if (s == null) {
            return "";
        }
        s = s.trim();
        return (s.length() <= DB_VARCHAR_75) ? s : s.substring(0, DB_VARCHAR_75);
    }

    private static String safeEnum(Enum<?> e) {
        return (e == null) ? "" : e.name();
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }

    private static String nullable(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    /**
     * Computes SHA-256 hex digest.
     */
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
            return toHex(md.digest(s.getBytes(StandardCharsets.UTF_8)));
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
