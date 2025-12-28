package com.ntuc.notification.audit.api.constants;

/**
 * Audit category = domain/subsystem classification.
 *
 * <p>IMPORTANT:
 * - Category is NOT the outcome (success/failure).
 * - Outcome is expressed via:
 *   - severity (INFO/WARNING/ERROR)
 *   - status (STARTED/SUCCESS/FAILED/PARTIAL/etc.)
 *   - errorCode / errorMessage / exceptionClass (for failures)
 *
 * <p>Email alert categories (CLS_FAILURE / JA_FAILURE / DT5_FAILURE)
 * must NOT live here; those belong to AlertEmailCategory.</p>
 */
public enum AuditCategory {

    /**
     * CLS integration domain: OAuth, HTTP, timeouts, payload, mapping (domain-level).
     */
    CLS,

    /**
     * JournalArticle / DDM / XML domain: structure/template/DDM/DocumentException issues.
     */
    JOURNAL_ARTICLE,

    /**
     * DT5 orchestration flow domain (REST intake, cron orchestration, coordination).
     */
    DT5_FLOW,

    /**
     * Email alerting domain: audit-driven email triggering/suppression/sending.
     */
    ALERT_EMAIL
}
