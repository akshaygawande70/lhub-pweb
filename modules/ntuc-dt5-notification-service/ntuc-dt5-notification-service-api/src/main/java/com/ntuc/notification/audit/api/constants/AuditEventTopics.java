package com.ntuc.notification.audit.api.constants;

/**
 * OSGi EventAdmin topics for audit lifecycle notifications.
 *
 * IMPORTANT:
 * - These events are notifications only; the AuditLog DB remains the source of truth.
 * - Consumers MUST re-load persisted audit data using auditLogId.
 */
public final class AuditEventTopics {

    private AuditEventTopics() {
        // constants
    }

    /** Fired after an AuditLog row is persisted. */
    public static final String AUDIT_PERSISTED = "com/ntuc/notification/audit/PERSISTED";

    /** Event property key: persisted AuditLog primary key. */
    public static final String PROP_AUDIT_LOG_ID = "auditLogId";

    /** Optional event property key: companyId (for routing/metrics). */
    public static final String PROP_COMPANY_ID = "companyId";
}
