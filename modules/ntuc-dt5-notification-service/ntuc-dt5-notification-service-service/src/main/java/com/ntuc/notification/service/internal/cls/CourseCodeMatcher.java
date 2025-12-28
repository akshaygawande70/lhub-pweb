package com.ntuc.notification.service.internal.cls;

/**
 * Pure utility for comparing course codes from different sources.
 *
 * Rules:
 * - Null-safe
 * - Trim
 * - Case-insensitive
 * - Empty/blank does NOT match a non-empty value
 */
public final class CourseCodeMatcher {

    private CourseCodeMatcher() {
        // util
    }

    public static boolean matches(String expectedCourseCode, String actualCourseCode) {
        String e = normalize(expectedCourseCode);
        String a = normalize(actualCourseCode);

        if (e.isEmpty() || a.isEmpty()) {
            return false;
        }

        return e.equalsIgnoreCase(a);
    }

    public static String normalize(String courseCode) {
        return (courseCode == null) ? "" : courseCode.trim();
    }
}
