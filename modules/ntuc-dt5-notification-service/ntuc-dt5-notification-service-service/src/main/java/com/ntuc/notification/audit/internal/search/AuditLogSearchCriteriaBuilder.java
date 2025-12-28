package com.ntuc.notification.audit.internal.search;

import com.ntuc.notification.audit.api.dto.AuditLogSearchRequest;

import java.util.Arrays;

/**
 * Pure Java builder that converts a normalized {@link AuditLogSearchRequest}
 * into {@link AuditLogSearchCriteria}.
 *
 * Unit-tested. No Liferay dependencies.
 */
public final class AuditLogSearchCriteriaBuilder {

    private AuditLogSearchCriteriaBuilder() {
        // util
    }

    public static AuditLogSearchCriteria build(AuditLogSearchRequest req) {
        AuditLogSearchCriteria c = new AuditLogSearchCriteria();
        if (req == null) {
            return c;
        }

        if (req.getSeverity() != null) c.setSeverity(req.getSeverity().name());
        if (req.getStatus() != null) c.setStatus(req.getStatus().name());
        if (req.getCategory() != null) c.setCategory(req.getCategory().name());
        if (req.getStep() != null) c.setStep(req.getStep().name());

        // Exact matches
        c.setAuditLogIdEq(normalizePositiveLong(req.getAuditLogId()));
        c.setNtucDTIdEq(normalizePositiveLong(req.getNtucDTId()));

        c.setFromTimeMsGe(req.getFromTimeMs());
        c.setToTimeMsLe(req.getToTimeMs());

        // IMPORTANT: current DTO uses q, not searchText
        String q = trimToNull(req.getQ());
        if (q != null) {
            c.setGlobalSearch(new AuditLogSearchCriteria.LikeAny(
                Arrays.asList(
                    "message",
                    "errorCode",
                    "errorMessage",
                    "correlationId",
                    "jobRunId",
                    "courseCode"
                ),
                "%" + q + "%"
            ));
        }

        c.setSortColumn(req.getSortColumn());
        c.setSortDir(req.getSortDir());

        return c;
    }

    static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    static Long normalizePositiveLong(Long v) {
        if (v == null) return null;
        return (v.longValue() > 0L) ? v : null;
    }
}
