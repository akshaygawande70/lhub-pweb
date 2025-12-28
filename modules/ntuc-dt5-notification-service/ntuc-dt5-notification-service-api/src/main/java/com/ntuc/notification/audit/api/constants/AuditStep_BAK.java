package com.ntuc.notification.audit.api.constants;

/**
 * Strict audit steps.
 *
 * This enum is intentionally a superset:
 * - Existing values are preserved for backward compatibility
 * - New values enable precise, queryable observability
 */
public enum AuditStep_BAK {

    // ---------------------------------------------------------------------
    // REST notify flow (legacy + still valid)
    // ---------------------------------------------------------------------
    REST_NOTIFY_BATCH,
    REST_NOTIFY_EVENT_VALIDATE,
    REST_NOTIFY_COURSE_VALIDATE,
    REST_NOTIFY_PERSIST,
    REST_NOTIFY_INTERNAL,

    // ---------------------------------------------------------------------
    // Async pipeline (generic)
    // ---------------------------------------------------------------------
    ASYNC_ENQUEUED,
    ASYNC_PROCESS_START,
    ASYNC_PROCESS_END,

    // ---------------------------------------------------------------------
    // One-time utilities
    // ---------------------------------------------------------------------
    ONE_TIME_S3_VALIDATE,
    ONE_TIME_S3_EXECUTE,

    // ---------------------------------------------------------------------
    // Validation / context (shared)
    // ---------------------------------------------------------------------
    VALIDATE_EVENT,
    BUILD_CONTEXT,
    VALIDATION,

    // ---------------------------------------------------------------------
    // Template resolution (shared)
    // ---------------------------------------------------------------------
    RESOLVE_TEMPLATE,

    // ---------------------------------------------------------------------
    // CLS interaction
    // ---------------------------------------------------------------------
    CLS_AUTH,
    CLS_FETCH_CRITICAL_START,
    CLS_FETCH_CRITICAL_END,
    CLS_FETCH_NONCRITICAL_START,
    CLS_FETCH_NONCRITICAL_END,
    CLS_FETCH_CRON_START,
    CLS_FETCH_CRON_END,

    // ---------------------------------------------------------------------
    // Journal Article processing (existing + expanded)
    // ---------------------------------------------------------------------
    JA_CREATE,
    JA_UPDATE,
    PROCESS_JOURNAL_ARTICLE,

    // New precise JA steps (do NOT remove legacy values)
    JA_PROCESS_FIELDS,
    JA_LOOKUP,
    JA_STATUS,
    JA_STATUS_ALL_VERSIONS,
    JA_FIND_LAYOUT,

    // ---------------------------------------------------------------------
    // Retrigger flow
    // ---------------------------------------------------------------------
    RETRIGGER_START,
    RETRIGGER_END,

    // ---------------------------------------------------------------------
    // Cron (legacy)
    // ---------------------------------------------------------------------
    CRON_PROCESS,

    // ---------------------------------------------------------------------
    // Cron (precise / queryable) - DO NOT remove legacy CRON_PROCESS
    // ---------------------------------------------------------------------
    CRON_ACTIVATED,
    CRON_JOB_STARTED,
    CRON_RECORDS_DISCOVERED,
    CRON_RECORD_STARTED,
    CRON_RECORD_SUCCESS,
    CRON_RECORD_FAILED,
    CRON_JOB_FINISHED,
    CRON_JOB_FAILED,

    // ---------------------------------------------------------------------
    // One-Time Load (OTL)
    // ---------------------------------------------------------------------
    OTL_START,
    OTL_END,

    // ---------------------------------------------------------------------
    // Email alerting steps
    // ---------------------------------------------------------------------
    EMAIL_SENT,
    EMAIL_SUPPRESSED,
    EMAIL_SEND_FAILED, 
    EMAIL_DEDUPE_CHECK_FAILED, 
    EMAIL_TEMPLATES_SAVE,
    
    
    // ---------------------------------------------------------------------
    // Dummy APIs
    // ---------------------------------------------------------------------
    CLS_DUMMY_COURSES_FETCH,
    CLS_DUMMY_SUBSCRIPTIONS_FETCH
}
