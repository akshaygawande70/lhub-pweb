package com.ntuc.notification.audit.internal.search;

import com.ntuc.notification.audit.api.dto.AuditLogSearchRequest;

/**
 * Pure Java normalizer:
 * - clamps paging (start/length)
 * - normalizes q
 * - supports: numeric q => auditLogId exact match
 *
 * Unit-test target.
 *
 * IMPORTANT:
 * - MUST NOT mutate the input request instance (prevents cross-request state bleed).
 */
public final class AuditLogSearchRequestNormalizer {

    static final int DEFAULT_PAGE_SIZE = 25;
    static final int MAX_PAGE_SIZE = 100;
    static final int MAX_Q_CHARS = 100;

    private AuditLogSearchRequestNormalizer() {
        // util
    }

    public static AuditLogSearchRequest normalize(AuditLogSearchRequest in) {
        AuditLogSearchRequest src = (in == null) ? new AuditLogSearchRequest() : in;

        // IMPORTANT: return a NEW instance (do not mutate src)
        AuditLogSearchRequest r = new AuditLogSearchRequest();

        // Preserve scope + DataTables envelope
        r.setCompanyId(src.getCompanyId());
        r.setGroupId(src.getGroupId());
        r.setDraw(src.getDraw());

        // Clamp paging (start/length)
        int start = src.getStart();
        if (start < 0) {
            start = 0;
        }
        r.setStart(start);

        int len = src.getLength();
        if (len <= 0) {
            len = DEFAULT_PAGE_SIZE;
        }
        if (len > MAX_PAGE_SIZE) {
            len = MAX_PAGE_SIZE;
        }
        r.setLength(len);

        // Preserve explicit filters (do NOT override)
        r.setSeverity(src.getSeverity());
        r.setStatus(src.getStatus());
        r.setCategory(src.getCategory());
        r.setStep(src.getStep());

        // Preserve time window
        r.setFromTimeMs(src.getFromTimeMs());
        r.setToTimeMs(src.getToTimeMs());

        // Normalize ids
        r.setAuditLogId(normalizePositiveLong(src.getAuditLogId()));
        r.setNtucDTId(normalizePositiveLong(src.getNtucDTId()));

        // Preserve sorting (whitelisted later)
        r.setSortColumn(src.getSortColumn());
        r.setSortDir(src.getSortDir());

        // Normalize q
        String q = safeTrim(src.getQ());
        if (q.length() > MAX_Q_CHARS) {
            q = q.substring(0, MAX_Q_CHARS);
        }
        q = q.isEmpty() ? null : q;

        // Numeric q => treat as auditLogId exact match
        if (q != null && isAllDigits(q)) {
            try {
                long id = Long.parseLong(q);
                if (id > 0L) {
                    r.setAuditLogId(Long.valueOf(id));
                    q = null;
                }
            }
            catch (Exception ignore) {
                // keep q as-is
            }
        }

        r.setQ(q);

        return r;
    }

    static Long normalizePositiveLong(Long v) {
        if (v == null) return null;
        return (v.longValue() > 0L) ? v : null;
    }

    private static String safeTrim(String s) {
        return (s == null) ? "" : s.trim();
    }

    private static boolean isAllDigits(String s) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch < '0' || ch > '9') return false;
        }
        return !s.isEmpty();
    }
}
