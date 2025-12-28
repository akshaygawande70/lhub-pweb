package com.ntuc.notification.portlet.util;

import java.util.Map;

/**
 * Reads request parameters by suffix to tolerate Liferay parameter namespacing.
 *
 * Example:
 * - dt5FailureSubject
 * - _com_liferay_configuration_admin_web_portlet_SystemSettingsPortlet_dt5FailureSubject
 */
public final class RequestParamSuffixUtil {

    private RequestParamSuffixUtil() {
        // util
    }

    public static String getStringBySuffix(javax.servlet.http.HttpServletRequest request, String suffix, String deflt) {
        if (request == null || suffix == null || suffix.isEmpty()) {
            return deflt;
        }

        String direct = request.getParameter(suffix);
        if (direct != null) {
            return direct;
        }

        Map<String, String[]> map = request.getParameterMap();
        if (map == null || map.isEmpty()) {
            return deflt;
        }

        for (Map.Entry<String, String[]> e : map.entrySet()) {
            String key = e.getKey();
            if (key != null && key.endsWith(suffix)) {
                String[] v = e.getValue();
                if (v != null && v.length > 0 && v[0] != null) {
                    return v[0];
                }
                return "";
            }
        }

        return deflt;
    }
}
