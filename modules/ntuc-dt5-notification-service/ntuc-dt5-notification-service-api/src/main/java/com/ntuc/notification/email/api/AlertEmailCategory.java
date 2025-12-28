package com.ntuc.notification.email.api;

/**
 * STRICT email alert classification.
 *
 * <p>Rule:
 * - This enum is used ONLY for email policy/template selection and dedupe fingerprinting.
 * - It must NOT be used as AuditCategory.
 *
 * <p>Audit records related to emailing must use:
 * - AuditCategory.ALERT_EMAIL
 * and include AlertEmailCategory in detailsJson.</p>
 */
public enum AlertEmailCategory {

    /**
     * CLS connectivity/OAuth/HTTP/timeouts.
     */
    CLS_FAILURE,

    /**
     * JournalArticle/DDM/XML/structure/template issues.
     */
    JA_FAILURE,

    /**
     * All other failures.
     */
    DT5_FAILURE
}
