package com.ntuc.notification.auditlog.portlet.render;

import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.audit.api.dto.AuditLogSearchRequest;

import java.util.Map;

/**
 * Converts DataTables serverSide request params into {@link AuditLogSearchRequest}.
 *
 * Rules:
 * - Accepts both non-namespaced and namespaced keys.
 * - If both exist, namespaced takes precedence.
 * - If a key has multiple values (e.g. query string + POST body), last-non-blank wins deterministically.
 * - q is populated from:
 *   1) q (custom field) OR
 *   2) search[value] (DataTables standard)
 */
public final class DataTablesAuditLogParamParser {

    private DataTablesAuditLogParamParser() {
        // util
    }

    public static AuditLogSearchRequest parse(
            Map<String, String[]> params, long companyId, long groupId) {

        String ns = detectNamespace(params);

        AuditLogSearchRequest r = new AuditLogSearchRequest();
        r.setCompanyId(companyId);
        r.setGroupId(groupId);

        r.setDraw(intParam(params, ns, "draw", 0));
        r.setStart(intParam(params, ns, "start", 0));
        r.setLength(intParam(params, ns, "length", 25));

        r.setSeverity(enumParam(params, ns, "severity", AuditSeverity.class));
        r.setStatus(enumParam(params, ns, "status", AuditStatus.class));
        r.setCategory(enumParam(params, ns, "category", AuditCategory.class));
        r.setStep(enumParam(params, ns, "step", AuditStep.class));

        r.setFromTimeMs(longParamNullable(params, ns, "fromTimeMs"));
        r.setToTimeMs(longParamNullable(params, ns, "toTimeMs"));

        r.setNtucDTId(longParamNullable(params, ns, "ntucDTId"));

        String q = str(params, ns, "q");
        if (isBlank(q)) {
            q = str(params, ns, "search[value]");
        }
        r.setQ(trimToNull(q));

        int orderCol = intParam(params, ns, "order[0][column]", -1);
        String orderDir = str(params, ns, "order[0][dir]");

        r.setSortColumn(mapSortColumn(orderCol));
        r.setSortDir("asc".equalsIgnoreCase(orderDir)
                ? AuditLogSearchRequest.SortDir.ASC
                : AuditLogSearchRequest.SortDir.DESC);

        return r;
    }

    private static AuditLogSearchRequest.SortColumn mapSortColumn(int orderCol) {
        switch (orderCol) {
            case 0:  return AuditLogSearchRequest.SortColumn.TIME;
            case 1:  return AuditLogSearchRequest.SortColumn.SEVERITY;
            case 2:  return AuditLogSearchRequest.SortColumn.STATUS;
            case 3:  return AuditLogSearchRequest.SortColumn.CATEGORY;
            case 4:  return AuditLogSearchRequest.SortColumn.STEP;
            case 5:  return AuditLogSearchRequest.SortColumn.MESSAGE;
            case 6:  return AuditLogSearchRequest.SortColumn.COURSE_CODE;
            case 7:  return AuditLogSearchRequest.SortColumn.NTUC_DT_ID;
            case 8:  return AuditLogSearchRequest.SortColumn.CORRELATION_ID;
            case 10: return AuditLogSearchRequest.SortColumn.AUDIT_ID;
            default: return AuditLogSearchRequest.SortColumn.TIME;
        }
    }

    private static String detectNamespace(Map<String, String[]> params) {
        for (String k : params.keySet()) {
            if (k != null && k.endsWith("draw") && !"draw".equals(k)) {
                return k.substring(0, k.length() - "draw".length());
            }
        }
        return "";
    }

    /**
     * Deterministic read:
     * - Prefer namespaced key if present and has any non-blank value.
     * - If multiple values exist, last non-blank wins.
     */
    private static String str(Map<String, String[]> params, String ns, String key) {
        String nsVal = lastNonBlank(params.get(ns + key));
        if (!isBlank(nsVal)) {
            return nsVal;
        }
        return lastNonBlank(params.get(key));
    }

    private static int intParam(Map<String, String[]> params, String ns, String key, int def) {
        String v = str(params, ns, key);
        try {
            return isBlank(v) ? def : Integer.parseInt(v.trim());
        }
        catch (Exception e) {
            return def;
        }
    }

    private static Long longParamNullable(Map<String, String[]> params, String ns, String key) {
        String v = str(params, ns, key);
        try {
            if (isBlank(v)) return null;
            long x = Long.parseLong(v.trim());
            return (x > 0) ? Long.valueOf(x) : null;
        }
        catch (Exception e) {
            return null;
        }
    }

    private static <E extends Enum<E>> E enumParam(
            Map<String, String[]> params, String ns, String key, Class<E> type) {

        String v = str(params, ns, key);
        if (isBlank(v)) return null;

        try {
            return Enum.valueOf(type, v.trim().toUpperCase());
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the last non-blank value in the array.
     * This makes behavior deterministic when the same parameter is supplied
     * by both query string and POST body (common in DataTables + portlet URLs).
     */
    private static String lastNonBlank(String[] arr) {
        if (arr == null || arr.length == 0) return null;

        for (int i = arr.length - 1; i >= 0; i--) {
            String v = arr[i];
            if (!isBlank(v)) {
                return v;
            }
        }
        return null;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
