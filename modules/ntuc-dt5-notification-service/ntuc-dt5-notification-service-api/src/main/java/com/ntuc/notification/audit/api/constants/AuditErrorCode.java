package com.ntuc.notification.audit.api.constants;

/**
 * Stable audit error codes.
 *
 * Rules:
 * - Keep codes stable; do not encode dynamic details here.
 * - Put "what exactly happened" into errorMessage + detailsJson.
 * - This enum is intentionally a superset to keep existing code compiling while refactors proceed.
 *
 * Migration notes:
 * - Do NOT delete legacy codes while historical audit rows may still reference them.
 * - Prefer the more specific codes when adding new audit events.
 */
public enum AuditErrorCode {

    // ---------------------------------------------------------------------
    // No error
    // ---------------------------------------------------------------------
    NONE,

    // ---------------------------------------------------------------------
    // Request / routing / validation
    // ---------------------------------------------------------------------
    REQ_BODY_REQUIRED,
    REQ_NULL_EVENT,
    REQ_NULL_COURSE,
    REQ_NO_COURSES,
    REQ_INVALID_EVENT_HEADER,
    REQ_INVALID_COURSE,
    REQ_INVALID_S3_PATH,

    VALIDATION_FAILED,
    UNSUPPORTED_EVENT_TYPE,

    // ---------------------------------------------------------------------
    // One-time utilities
    // ---------------------------------------------------------------------
    ONE_TIME_S3_FAILED,

    // ---------------------------------------------------------------------
    // CLS connectivity / auth / transport
    // ---------------------------------------------------------------------
    CLS_OAUTH_FAILED,
    CLS_CONNECTION_FAILED,
    CLS_TIMEOUT,
    CLS_HTTP_4XX,
    CLS_HTTP_5XX,

    // ---------------------------------------------------------------------
    // CLS payload / contract issues
    // ---------------------------------------------------------------------
    CLS_EMPTY_BODY,
    CLS_INVALID_RESPONSE,
    CLS_PARSE_FAILED,
    CLS_MISSING_REQUIRED_FIELDS,

    // ---------------------------------------------------------------------
    // DDM / template / structure resolution
    // ---------------------------------------------------------------------
    DDM_TEMPLATE_NOT_FOUND,
    DDM_STRUCTURE_NOT_FOUND,

    // ---------------------------------------------------------------------
    // Template / rendering
    // ---------------------------------------------------------------------
    TEMPLATE_RESOLVE_FAILED,

    // ---------------------------------------------------------------------
    // Email / alerting
    // ---------------------------------------------------------------------
    EMAIL_SEND_FAILED,

    // ---------------------------------------------------------------------
    // XML / content build
    // ---------------------------------------------------------------------
    XML_BUILD_FAILED,
    XML_INVALID,

    // ---------------------------------------------------------------------
    // Journal Article processing boundary
    // ---------------------------------------------------------------------
    JA_EMPTY_XML,
    JA_NOT_FOUND,

    JA_PERSIST_FAILED,
    JA_LOOKUP_EXCEPTION,

    JA_LAYOUT_NOT_FOUND,
    JA_LAYOUT_LOOKUP_EXCEPTION,

    JA_CREATE_EXCEPTION,
    JA_UPDATE_EXCEPTION,

    // Prefer *_EXCEPTION codes when an exception was caught.
    JA_PROCESS_FIELDS_EXCEPTION,
    JA_STATUS_UPDATE_EXCEPTION,

    // Legacy / overlapping: keep for compile + historical audit rows.
    // Prefer:
    // - JA_NOT_FOUND over JA_RETURNED_NULL (when lookup did not find anything)
    // - JA_PERSIST_FAILED / JA_*_EXCEPTION over JA_PROCESSING_FAILED
    JA_RETURNED_NULL,
    JA_PROCESSING_FAILED,
    JA_STATUS_UPDATE_FAILED,

    // ---------------------------------------------------------------------
    // Cron (new precise error codes)
    // ---------------------------------------------------------------------
    CRON_JOB_FAILED,
    CRON_RECORD_FAILED,

    // ---------------------------------------------------------------------
    // Generic / legacy catch-alls (should become rare over time)
    // ---------------------------------------------------------------------
    INTERNAL_ERROR,
    PROCESSING_FAILED,
    BATCH_FAILED,

    // Legacy: long-term becomes AuditCategory.DT5_FAILURE (but keep enum value stable)
    DT5_FAILURE,

    // Catch-all (should become rare over time)
    DT5_UNEXPECTED,

    // ---------------------------------------------------------------------
    // Legacy aliases (compile-compatibility during migration)
    // ---------------------------------------------------------------------
    // Legacy: ambiguous; keep compiling, map later by usage (you chose aliasing to CLS_EMPTY_BODY in places).
    CLS_NOT_FOUND,

    // Legacy: overlaps with JA_RETURNED_NULL; keep but don't introduce new usage.
    JA_PROCESSFIELDS_NULL,

    // Legacy: overlaps with CLS_MISSING_REQUIRED_FIELDS; keep but don't introduce new usage.
    MISSING_REQUIRED_FIELDS, DT5_TEMPLATE_UPDATE_FAILED, DT5_INVALID_INPUT
}
