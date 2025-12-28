package com.ntuc.notification.rest.internal.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Acknowledgement response for CLS → PWEB /notify intake.
 *
 * status:
 * - success
 * - failed
 * - partial_success
 *
 * errors[]:
 * - notificationId (always)
 * - courseCode (when applicable; empty string otherwise)
 * - message
 */
public class CourseNotificationAckResponse {

    private String status;
    private String message;
    private List<ErrorItem> errors;

    public static CourseNotificationAckResponse success(String message) {
        CourseNotificationAckResponse r = new CourseNotificationAckResponse();
        r.status = "success";
        r.message = message == null ? "" : message;
        r.errors = Collections.emptyList();
        return r;
    }

    public static CourseNotificationAckResponse failed(String message, List<ErrorItem> errors) {
        CourseNotificationAckResponse r = new CourseNotificationAckResponse();
        r.status = "failed";
        r.message = message == null ? "" : message;
        r.errors = errors == null ? Collections.<ErrorItem>emptyList() : new ArrayList<>(errors);
        return r;
    }

    public static CourseNotificationAckResponse partialSuccess(String message, List<ErrorItem> errors) {
        CourseNotificationAckResponse r = new CourseNotificationAckResponse();
        r.status = "partial_success";
        r.message = message == null ? "" : message;
        r.errors = errors == null ? Collections.<ErrorItem>emptyList() : new ArrayList<>(errors);
        return r;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ErrorItem> getErrors() {
        return errors;
    }

    public static class ErrorItem {
        private String notificationId;
        private String courseCode;
        private String message;

        public ErrorItem() {
            // json
        }

        public ErrorItem(String notificationId, String message) {
            this(notificationId, "", message);
        }

        public ErrorItem(String notificationId, String courseCode, String message) {
            this.notificationId = notificationId == null ? "" : notificationId;
            this.courseCode = courseCode == null ? "" : courseCode;
            this.message = message == null ? "" : message;
        }

        public String getNotificationId() {
            return notificationId;
        }

        public String getCourseCode() {
            return courseCode;
        }

        public String getMessage() {
            return message;
        }
    }
}
