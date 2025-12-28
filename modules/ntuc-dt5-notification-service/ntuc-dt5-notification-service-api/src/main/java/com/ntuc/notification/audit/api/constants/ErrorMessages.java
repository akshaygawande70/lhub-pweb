package com.ntuc.notification.audit.api.constants;

import java.text.MessageFormat;

/**
 * Message templates for REST/logs. Pair with ErrorCodes.
 * Keep concise; verbose details go in logs/meta.
 */
public final class ErrorMessages {
    public static final String MISSING_EVENTS_ARRAY    = "Request body must contain an events array.";
    public static final String VALIDATION_FAILED       = "Validation failed: {0}.";
    public static final String UNSUPPORTED_EVENT_TYPE  = "Event type not supported: {0}.";
    public static final String INTERNAL_PROCESSING     = "Internal processing error.";
    public static final String SCHEDULE_NOT_FOUND      = "No schedules found for courseCode: {0}.";
    public static final String MISSING_COURSE_CODE     = "courseCode path param is required.";
    public static final String TIMESTAMP_FORMAT        = "Invalid timestamp format: {0}.";
    public static final String UNSUPPORTED_COURSE_TYPE = "courseType is not supported.";

    public static String fmt(String template, Object... args) {
        return MessageFormat.format(template, args);
    }

    private ErrorMessages() {}
}
