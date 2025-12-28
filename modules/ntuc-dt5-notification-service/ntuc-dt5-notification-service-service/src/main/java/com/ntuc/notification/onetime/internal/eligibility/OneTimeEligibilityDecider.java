package com.ntuc.notification.onetime.internal.eligibility;

import com.fasterxml.jackson.databind.JsonNode;
import com.ntuc.notification.constants.NotificationType;

/**
 * Pure eligibility decision logic for one-time S3 load.
 *
 * IMPORTANT:
 * - No AWS SDK types
 * - No Liferay types
 * - Safe for unit tests (plain JUnit)
 */
public final class OneTimeEligibilityDecider {

    private OneTimeEligibilityDecider() {
        // util
    }

    public static OneTimeEligibilityDecision decide(JsonNode node) {
        JsonNode body = (node == null) ? null : node.get("body");
        if (body == null || body.isNull()) {
            return new OneTimeEligibilityDecision(false, "", "", "");
        }

        String status = lc(text(body.get("courseCatalogueStatus")));
        String courseCode = text(body.get("courseCode"));

        boolean published = eq(status, NotificationType.PUBLISHED);
        boolean inactive = eq(status, NotificationType.INACTIVE);

        // If status is neither, it's ineligible BUT we still return courseCode for auditing/tests.
        if (!published && !inactive) {
            return new OneTimeEligibilityDecision(false, status, "", courseCode);
        }

        String eventType = published ? NotificationType.PUBLISHED : NotificationType.INACTIVE;
        return new OneTimeEligibilityDecision(true, status, eventType, courseCode);
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
