package com.ntuc.notification.audit.api.constants;

/**
 * Canonical, layer-based audit steps.
 *
 * <h2>Non-negotiable semantics</h2>
 * <ul>
 *   <li><b>Step = subsystem boundary</b>, not a lifecycle phase.</li>
 *   <li>Lifecycle phase is expressed by {@link AuditStatus} (STARTED/SUCCESS/FAILED/SKIPPED/RETRIED/PARTIAL).</li>
 *   <li>Impact is expressed by {@link AuditSeverity} (INFO/WARNING/ERROR).</li>
 *   <li>Any nuance (e.g., CRON vs REST trigger, SCHEDULE vs DUMMY vs COURSE, critical vs non-critical)
 *       must go into message + detailsJson.</li>
 * </ul>
 *
 * <h2>Required details convention (service-side responsibility)</h2>
 * <ul>
 *   <li><b>trigger</b>: REST | CRON | RETRIGGER | OTL | ONE_TIME_S3</li>
 *   <li><b>api</b> (when applicable): COURSE_DETAILS | SCHEDULE | DUMMY</li>
 *   <li><b>phase</b> (optional): short discriminator for internal phases, never used for filtering as a substitute for step</li>
 * </ul>
 *
 * <p>This enum is intentionally small and stable. Only add a new value when introducing a genuinely new
 * subsystem boundary that operations would want to filter on independently.</p>
 */
public enum AuditStep {

    /**
     * Entry boundary for inbound triggers (REST endpoint, cron trigger, retrigger call, OTL events).
     *
     * <p>Use this for request receipt, routing, and high-level orchestration setup.
     * Do not use for deep processing.</p>
     */
    ENTRY,

    /**
     * Validation boundary for inputs and derived invariants.
     *
     * <p>Examples: request body validation, event header checks, schedule date validation,
     * courseCode normalization checks, parameter/config presence validation.</p>
     */
    VALIDATION,

    /**
     * Context building boundary.
     *
     * <p>Examples: building {@code CourseEventContext}, resolving {@code CourseArticleConfig},
     * computing returnedProperties from changeFrom, deriving business keys.</p>
     */
    CONTEXT,

    /**
     * Execution boundary for job scheduling and asynchronous processing.
     *
     * <p>Examples: enqueueing async work, beginning async execution, per-record execution loops for CRON/OTL/S3.</p>
     */
    EXECUTION,

    /**
     * CLS OAuth boundary (token acquisition/refresh/cache decisions).
     */
    CLS_AUTH,

    /**
     * CLS API boundary (all external CLS calls).
     *
     * <p>Includes: course details fetch, schedule fetch, dummy APIs, CLS cron-related fetches.
     * Differentiate via detailsJson keys such as {@code api=SCHEDULE} / {@code api=DUMMY}.</p>
     */
    CLS_FETCH,

    /**
     * Content persistence boundary for Liferay JournalArticle and DDM.
     *
     * <p>Includes: lookup, create, update, XML build/validation, status updates, layout lookup.</p>
     */
    JA_PROCESS,

    /**
     * Template boundary for resolving and rendering templates.
     *
     * <p>Includes: selecting template keys, building template model, FreeMarker rendering,
     * template persistence (if applicable).</p>
     */
    TEMPLATE,

    /**
     * Alerting boundary driven solely by persisted audit records.
     *
     * <p>Includes: policy resolution, fingerprint building, dedupe checks, send/suppress outcomes.
     * Outcomes must be represented by status + errorCode (not additional steps).</p>
     */
    EMAIL_ALERT, EMAIL_SENT, EMAIL_SUPPRESSED, EMAIL_SEND_FAILED
}
