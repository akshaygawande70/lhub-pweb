package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.AuditEvent;

/**
 * Centralized audit-driven email trigger.
 *
 * Rules:
 * - MUST make email decisions based on persisted audit records (AuditLog table).
 * - MUST dedupe/suppress according to ParameterKeys.
 * - MUST write EMAIL_SUPPRESSED / EMAIL_SEND_FAILED audit entries as needed.
 *
 * This is the ONLY entrypoint that may decide to send alert emails (via EmailSender transport).
 */
public interface AuditEmailTrigger {

    /**
     * Preferred entrypoint: called AFTER the AuditLog row is persisted.
     * Implementations must load the persisted record and drive policy from it.
     *
     * @param auditLogId persisted auditLog primary key
     */
    void onAuditPersisted(long auditLogId);

    /**
     * Legacy transitional entrypoint (avoid new usage).
     *
     * IMPORTANT:
     * Implementations MUST NOT treat the in-memory event as source of truth.
     * If an auditLogId is available (e.g. in details), they may re-route to {@link #onAuditPersisted(long)}.
     * Otherwise they must safely no-op.
     */
    void onAuditPersisted(AuditEvent event);
}
