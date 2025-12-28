package com.ntuc.notification.audit.api.constants;

/**
 * AuditCategory groups events by subsystem ownership (who should action it).
 *
 * <p>Selection rule:
 * - Use CLS for OAuth/HTTP/payload/contract issues related to CLS integration.
 * - Use JOURNAL_ARTICLE for DDM/template/XML/JournalArticle persistence issues.
 * - Use DT5_FLOW for orchestration/routing/validation/execution coordination not belonging to CLS/JA.
 * - Use ALERT_EMAIL only for the audit-driven email pipeline outcomes (sent/suppressed/failed/dedupe).
 *
 * <p>Category is orthogonal to step/status/severity.
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
