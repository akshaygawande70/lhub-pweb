package com.ntuc.notification.onetime.internal.eligibility;

import com.fasterxml.jackson.databind.JsonNode;
import com.ntuc.notification.constants.NotificationType;

/**
 * Pure eligibility decision logic for one-time S3 load.
 *
 * Non-negotiable:
 * - No AWS SDK types
 * - No Liferay types
 * - Safe for plain JUnit tests
 */
public final class EligibilityDecider {

    private EligibilityDecider() {
        // util
    }

    public static EligibilityDecision decide(JsonNode node) {
        JsonNode body = (node == null) ? null : node.get("body");
        if (body == null || body.isNull()) {
            return new EligibilityDecision(false, "", "", "");
        }

        String status = lc(text(body.get("courseCatalogueStatus")));
        String courseCode = text(body.get("courseCode"));

        boolean published = eq(status, NotificationType.PUBLISHED);
        boolean inactive = eq(status, NotificationType.INACTIVE);

        // Ineligible but still return courseCode + status for audit visibility.
        if (!published && !inactive) {
            return new EligibilityDecision(false, status, "", courseCode);
        }

        String eventType = published ? NotificationType.PUBLISHED : NotificationType.INACTIVE;
        return new EligibilityDecision(true, status, eventType, courseCode);
    }

    private static String text(JsonNode n) {
        return (n == null || n.isNull()) ? "" : String.valueOf(n.asText(""));
    }

    private static boolean eq(String a, String b) {
        return !isBlank(a) && !isBlank(b) && lc(a).equals(lc(b));
    }

    private static String lc(String s) {
        return (s == null) ? "" : s.trim().toLowerCase();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
