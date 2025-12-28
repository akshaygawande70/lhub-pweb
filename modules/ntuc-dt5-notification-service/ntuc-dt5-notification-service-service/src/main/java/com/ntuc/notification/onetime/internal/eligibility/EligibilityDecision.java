package com.ntuc.notification.onetime.internal.eligibility;

/**
 * Result of eligibility decision for one-time S3 load.
 *
 * Pure DTO:
 * - No Liferay
 * - No AWS
 * - Safe for unit tests
 */
public final class EligibilityDecision {

    private final boolean eligible;
    private final String status;
    private final String eventType;
    private final String courseCode;

    public EligibilityDecision(boolean eligible, String status, String eventType, String courseCode) {
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
