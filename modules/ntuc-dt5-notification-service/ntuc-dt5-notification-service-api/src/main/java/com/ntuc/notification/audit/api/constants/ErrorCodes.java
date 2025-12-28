package com.ntuc.notification.audit.api.constants;

/**
 * Stable error codes that can be returned via REST and used in audit/meta.
 * Keep codes stable even if messages change.
 */
public final class ErrorCodes {
    public static final String E_MISSING_EVENTS_ARRAY    = "NTUC-400-001";
    public static final String E_VALIDATION_FAILED       = "NTUC-400-002";
    public static final String E_UNSUPPORTED_EVENT_TYPE  = "NTUC-400-003";
    public static final String E_INTERNAL_PROCESSING     = "NTUC-500-001";
    public static final String E_SCHEDULE_NOT_FOUND      = "NTUC-404-001";
    public static final String E_MISSING_COURSE_CODE     = "NTUC-400-004";
    public static final String E_TIMESTAMP_FORMAT        = "NTUC-400-005";
    public static final String E_UNSUPPORTED_COURSE_TYPE = "NTUC-400-006";

    private ErrorCodes() {}
}
