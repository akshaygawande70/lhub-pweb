package com.ntuc.notification.portlet.util;

import java.util.Map;

/**
 * Pure parameter parsing utilities for Update Schedule resource handling.
 *
 * <p>This class contains NO Liferay/Portlet dependencies and is intended to be unit-tested
 * with plain JUnit.</p>
 */
public class UpdateScheduleParamUtil {

    private UpdateScheduleParamUtil() {
        // static utility
    }

    /**
     * Normalizes a YES/NO/blank value.
     *
     * <ul>
     *   <li>null/blank -> ""</li>
     *   <li>"YES"/"NO" (any case, trimmed) -> canonical "YES"/"NO"</li>
     *   <li>Anything else -> IllegalArgumentException</li>
     * </ul>
     */
    public static String normalizeYesNoBlank(String raw) {
        if (raw == null) {
            return "";
        }

        String s = raw.trim();

        if (s.isEmpty()) {
            return "";
        }

        if ("YES".equalsIgnoreCase(s)) {
            return "YES";
        }

        if ("NO".equalsIgnoreCase(s)) {
            return "NO";
        }

        throw new IllegalArgumentException("Invalid value: must be YES, NO, or blank");
    }

    /**
     * Resolves the namespace/prefix by finding any parameter key that ends with the provided suffix.
     *
     * <p>Example: key "_ns_courseScheduleId", suffix "courseScheduleId" -> returns "_ns_".</p>
     *
     * @return the namespace/prefix (possibly ""), never null
     */
    public static String resolveNamespace(Map<String, String[]> parameterMap, String fieldSuffix) {
        if (parameterMap == null || parameterMap.isEmpty()) {
            return "";
        }

        for (String key : parameterMap.keySet()) {
            if (key != null && key.endsWith(fieldSuffix)) {
                return key.substring(0, key.length() - fieldSuffix.length());
            }
        }

        return "";
    }
}
