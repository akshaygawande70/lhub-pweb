package com.ntuc.notification.auditlog.portlet.render;

import com.ntuc.notification.audit.api.dto.AuditLogRowDto;
import com.ntuc.notification.audit.api.dto.AuditLogSearchResult;

import java.util.List;

/**
 * Writes DataTables-compliant JSON.
 *
 * CRITICAL:
 * - MUST echo draw from request (DataTables protocol).
 * - MUST include recordsTotal, recordsFiltered, data (and optional error).
 *
 * Notes:
 * - Plain Java for deterministic output and unit testing.
 * - JSON escaping MUST be safe for all control characters.
 */
public final class AuditLogDataTablesJsonWriter {

    private AuditLogDataTablesJsonWriter() {
    }

    /**
     * Convenience method. Prefer {@link #toJson(AuditLogSearchResult, int)} at the endpoint
     * so draw can be echoed even when the result is null.
     */
    public static String toJson(AuditLogSearchResult r) {
        return toJson(r, 0);
    }

    /**
     * Writes the DataTables JSON payload.
     *
     * @param r result (may be null)
     * @param drawFallback draw from request when result is null or has draw=0
     */
    public static String toJson(AuditLogSearchResult r, int drawFallback) {
        if (r == null) {
            return emptyJson(drawFallback);
        }

        int draw = (r.getDraw() > 0) ? r.getDraw() : drawFallback;

        StringBuilder sb = new StringBuilder(4096);
        sb.append('{');

        sb.append("\"draw\":").append(draw).append(',');
        sb.append("\"recordsTotal\":").append(r.getRecordsTotal()).append(',');
        sb.append("\"recordsFiltered\":").append(r.getRecordsFiltered()).append(',');
        sb.append("\"data\":");

        writeRows(sb, r.getData());

        sb.append('}');
        return sb.toString();
    }

    public static String emptyJson(int draw) {
        return "{\"draw\":" + draw +
            ",\"recordsTotal\":0" +
            ",\"recordsFiltered\":0" +
            ",\"data\":[]}";
    }

    public static String toErrorJson(int draw, String message) {
        return "{\"draw\":" + draw +
            ",\"recordsTotal\":0" +
            ",\"recordsFiltered\":0" +
            ",\"error\":\"" + escapeJson(message) + "\"" +
            ",\"data\":[]}";
    }

    private static void writeRows(StringBuilder sb, List<AuditLogRowDto> rows) {
        if (rows == null || rows.isEmpty()) {
            sb.append("[]");
            return;
        }

        sb.append('[');

        boolean first = true;

        for (AuditLogRowDto r : rows) {
            if (!first) {
                sb.append(',');
            }
            first = false;

            sb.append('{');
            kv(sb, "auditId", r.getAuditId()); sb.append(',');
            kv(sb, "startTimeMs", r.getStartTimeMs()); sb.append(',');
            kv(sb, "endTimeMs", r.getEndTimeMs()); sb.append(',');
            kv(sb, "durationMs", r.getDurationMs()); sb.append(',');
            kv(sb, "severity", r.getSeverity()); sb.append(',');
            kv(sb, "status", r.getStatus()); sb.append(',');
            kv(sb, "category", r.getCategory()); sb.append(',');
            kv(sb, "step", r.getStep()); sb.append(',');
            kv(sb, "message", r.getMessage()); sb.append(',');
            kv(sb, "courseCode", r.getCourseCode()); sb.append(',');
            kv(sb, "ntucDTId", r.getNtucDTId()); sb.append(',');
            kv(sb, "correlationId", r.getCorrelationId()); sb.append(',');
            kv(sb, "jobRunId", r.getJobRunId()); sb.append(',');
            kv(sb, "errorCode", r.getErrorCode()); sb.append(',');
            kv(sb, "errorMessage", r.getErrorMessage()); sb.append(',');
            kv(sb, "exceptionClass", r.getExceptionClass());
            sb.append('}');
        }

        sb.append(']');
    }

    private static void kv(StringBuilder sb, String k, Object v) {
        sb.append('"').append(k).append("\":");

        if (v == null) {
            sb.append("null");
            return;
        }

        if (v instanceof Number || v instanceof Boolean) {
            sb.append(v);
            return;
        }

        sb.append('"').append(escapeJson(String.valueOf(v))).append('"');
    }

    /**
     * JSON-safe escaping:
     * - Escapes backslash and quote
     * - Escapes common controls (\n \r \t \b \f)
     * - Escapes any remaining control char &lt; 0x20 as \\u00XX
     */
    private static String escapeJson(String s) {
        if (s == null) {
            return "";
        }

        StringBuilder out = new StringBuilder(s.length() + 16);

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            switch (c) {
                case '\\': out.append("\\\\"); break;
                case '"':  out.append("\\\""); break;
                case '\n': out.append("\\n"); break;
                case '\r': out.append("\\r"); break;
                case '\t': out.append("\\t"); break;
                case '\b': out.append("\\b"); break;
                case '\f': out.append("\\f"); break;

                default:
                    if (c < 0x20) {
                        out.append("\\u00");
                        out.append(toHex((c >> 4) & 0xF));
                        out.append(toHex(c & 0xF));
                    }
                    else {
                        out.append(c);
                    }
            }
        }

        return out.toString();
    }

    private static char toHex(int nibble) {
        return (char)((nibble < 10) ? ('0' + nibble) : ('A' + (nibble - 10)));
    }
}
