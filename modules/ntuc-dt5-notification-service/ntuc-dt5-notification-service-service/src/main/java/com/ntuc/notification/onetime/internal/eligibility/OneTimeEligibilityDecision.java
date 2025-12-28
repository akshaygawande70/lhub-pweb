package com.ntuc.notification.onetime.internal.eligibility;

/**
 * Result of eligibility decision for one-time S3 load.
 *
 * Pure DTO:
 * - No Liferay
 * - No AWS
 * - Safe for unit testing
 */
public final class OneTimeEligibilityDecision {

    private final boolean eligible;
    private final String status;
    private final String eventType;
    private final String courseCode;

    public OneTimeEligibilityDecision(boolean eligible, String status, String eventType, String courseCode) {
        this.eligible = eligible;
        this.status = status == null ? "" : status;
        this.eventType = eventType == null ? "" : eventType;
        this.courseCode = courseCode == null ? "" : courseCode;
    }

    public boolean isEligible() {
        return eligible;
    }

    public String getStatus() {
        return status;
    }

    public String getEventType() {
        return eventType;
    }

    public String getCourseCode() {
        return courseCode;
    }
}
