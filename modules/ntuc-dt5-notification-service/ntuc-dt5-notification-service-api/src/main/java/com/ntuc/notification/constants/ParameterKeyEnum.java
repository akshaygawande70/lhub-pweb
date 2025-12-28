package com.ntuc.notification.constants;

/**
 * Central registry of parameter keys.
 *
 * Ordering rules:
 * 1. GLOBAL_*        → cross-module / environment-wide
 * 2. ALERT_*         → mandated audit-driven alerting policy
 * 3. CLS_*           → CLS integration + audit routing
 * 4. Legacy keys     → backward compatibility ONLY
 * 5. Parameter group codes
 *
 *  New code MUST use ALERT_* keys for email behavior.
 */
public enum ParameterKeyEnum {

    // ---------------------------------------------------------------------
    // GLOBAL (cross-module / environment-wide)
    // ---------------------------------------------------------------------

    GLOBAL_USER_EMAIL("userEmail"),

    // ---------------------------------------------------------------------
    // ALERTING POLICY (MANDATED — new code MUST use these)
    // ---------------------------------------------------------------------

    /**
     * Master switch for sending alert emails.
     * Values: true/false, 1/0, yes/no
     */
    CLS_ALERT_EMAIL_ENABLED("CLS_ALERT_EMAIL_ENABLED"),

    /**
     * Minimum severity required to trigger an email.
     * Values: INFO | WARNING | ERROR
     */
    ALERT_SEVERITY_THRESHOLD_EMAIL("ALERT_SEVERITY_THRESHOLD_EMAIL"),

    /**
     * Dedupe / suppression window in minutes.
     * Values: integer minutes (e.g. 5, 10, 30)
     */
    ALERT_DEDUPE_WINDOW_MINUTES("ALERT_DEDUPE_WINDOW_MINUTES"),

    /**
     * Dedupe scope.
     * Values: COURSE | FLOW | JOB
     */
    ALERT_SCOPE("ALERT_SCOPE"),

    /**
     * Alert email recipients.
     * Values: comma-separated string or string[] depending on implementation.
     */
    CLS_EMAIL_ALERTS_TO("CLS_EMAIL_ALERTS_TO"),

    // ---------------------------------------------------------------------
    // CLS INTEGRATION / RUNTIME / AUDIT ROUTING
    // ---------------------------------------------------------------------

    CLS_GROUP_ID("CLS_GROUP_ID"),
    CLS_COMPANY_ID("CLS_COMPANY_ID"),

    CLS_TEMPLATE_NAME("CLS_TEMPLATE_NAME"),

    CLS_AUTH_CLIENT_ID("CLS_AUTH_CLIENT_ID"),
    CLS_AUTH_CLIENT_SECRET("CLS_AUTH_CLIENT_SECRET"),
    CLS_AUTH_BASE_URL("CLS_AUTH_BASE_URL"),
    CLS_AUTH_ENDPOINT("CLS_AUTH_ENDPOINT"),
    CLS_AUTH_METHOD("CLS_AUTH_METHOD"),

    CLS_COURSE_DETAILS_ENDPOINT("CLS_COURSE_DETAILS_ENDPOINT"),
    CLS_COURSE_DETAILS_METHOD("CLS_COURSE_DETAILS_METHOD"),
    CLS_COURSE_SCHEDULES_ENDPOINT("CLS_COURSE_SCHEDULES_ENDPOINT"),
    CLS_COURSE_SCHEDULES_METHOD("CLS_COURSE_SCHEDULES_METHOD"),

    CLS_DUMMY_COURSES_ENDPOINT("CLS_DUMMY_COURSES_ENDPOINT"),
    CLS_DUMMY_SUBSCRIPTIONS_ENDPOINT("CLS_DUMMY_SUBSCRIPTIONS_ENDPOINT"),
    CLS_DUMMY_METHOD("CLS_DUMMY_METHOD"),

    CLS_HTTP_CONNECT_TIMEOUT_MS("CLS_HTTP_CONNECT_TIMEOUT_MS"),
    CLS_HTTP_READ_TIMEOUT_MS("CLS_HTTP_READ_TIMEOUT_MS"),

    CLS_AUTH_RETRY_COUNT("CLS_AUTH_RETRY_COUNT"),
    CLS_AUTH_RETRY_INTERVAL_SEC("CLS_AUTH_RETRY_INTERVAL_SEC"),
    CLS_COURSE_RETRY_COUNT("CLS_COURSE_RETRY_COUNT"),
    CLS_COURSE_RETRY_INTERVAL_SEC("CLS_COURSE_RETRY_INTERVAL_SEC"),

    CLS_FOLDERID("CLS_FOLDERID"),

    // ---------------------------------------------------------------------
    // CLS FIELD CLASSIFICATION / CHANGE DETECTION
    // ---------------------------------------------------------------------

    CLS_FIELD_CRITICAL("CLS_FIELD_CRITICAL"),
    CLS_FIELD_NONCRITICAL("CLS_FIELD_NONCRITICAL"),
    CLS_FIELD_BATCH("CLS_FIELD_BATCH"),

    CLS_FIELD_CHANGE_FROM_COURSE("CLS_FIELD_CHANGE_FROM_COURSE"),
    CLS_FIELD_CHANGE_FROM_PRICINGTABLE("CLS_FIELD_CHANGE_FROM_PRICINGTABLE"),
    CLS_FIELD_CHANGE_FROM_FUNDINGELIGIBILITYCRITERIA("CLS_FIELD_CHANGE_FROM_FUNDINGELIGIBILITYCRITERIA"),
    CLS_FIELD_CHANGE_FROM_SUBSIDY("CLS_FIELD_CHANGE_FROM_SUBSIDY"),

    CLS_FIELD_CRITICAL_SCHEDULE("CLS_FIELD_CRITICAL_SCHEDULE"),
    CLS_FIELD_NONCRITICAL_SCHEDULE("CLS_FIELD_NONCRITICAL_SCHEDULE"),

    CLS_FIELD_BATCH_CRON_EXPRESSION("CLS_FIELD_BATCH_CRON_EXPRESSION"),
    CLS_FAILED_JOBS_CRON_EXPRESSION("CLS_FAILED_JOBS_CRON_EXPRESSION"),

    // ---------------------------------------------------------------------
    // AWS (CLS auxiliary)
    // ---------------------------------------------------------------------

    CLS_AWS_ACCESS_KEY_ID("CLS_AWS_ACCESS_KEY_ID"),
    CLS_AWS_SECRET_ACCESS_KEY("CLS_AWS_SECRET_ACCESS_KEY"),
    CLS_AWS_REGION("CLS_AWS_REGION"),

    // ---------------------------------------------------------------------
    // AUDIT ROUTING FLAGS (CLS-specific)
    // ---------------------------------------------------------------------

    CLS_AL_GENERAL("CLS_AL_GENERAL"),
    CLS_AUDIT_CLS("CLS_AUDIT_CLS"),
    CLS_AUDIT_SCHEDULE("CLS_AUDIT_SCHEDULE"),
    CLS_AUDIT_CRON("CLS_AUDIT_CRON"),
    CLS_AUDIT_EMAIL("CLS_AUDIT_EMAIL"),
    CLS_AUDIT_JA("CLS_AUDIT_JA"),

    // ---------------------------------------------------------------------
    // PARAMETER GROUP CODES (ServiceBuilder lookup)
    // ---------------------------------------------------------------------

    CLS_PARAMETER_GROUP_CODE("clsParam"),
    GLOBAL_PARAMETER_GROUP_CODE("globalParam");

    private final String code;

    ParameterKeyEnum(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
