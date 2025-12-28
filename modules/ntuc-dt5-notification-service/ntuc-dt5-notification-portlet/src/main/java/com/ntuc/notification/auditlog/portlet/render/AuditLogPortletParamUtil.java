package com.ntuc.notification.auditlog.portlet.render;

import java.util.Map;

public final class AuditLogPortletParamUtil {

    private AuditLogPortletParamUtil() {
        // util
    }

    public static String getString(Map<String, String[]> params, String ns, String key) {
        String n = (ns == null) ? "" : ns;

        String v = getFirst(params, n + key);
        if (isNotBlank(v)) {
            return v.trim();
        }
        v = getFirst(params, key);
        return (v == null) ? "" : v.trim();
    }

    public static int getInt(Map<String, String[]> params, String ns, String key, int defaultValue) {
        String n = (ns == null) ? "" : ns;

        String raw = getFirst(params, n + key);
        if (!isNotBlank(raw)) {
            raw = getFirst(params, key);
        }
        Integer parsed = parseIntSafe(raw);
        return (parsed == null) ? defaultValue : parsed;
    }

    private static String getFirst(Map<String, String[]> params, String key) {
        if (params == null || key == null) {
            return null;
        }
        String[] arr = params.get(key);
        if (arr == null || arr.length == 0) {
            return null;
        }
        return arr[0];
    }

    private static boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static Integer parseIntSafe(String s) {
        if (!isNotBlank(s)) {
            return null;
        }
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception ignore) {
            return null;
        }
    }
}
