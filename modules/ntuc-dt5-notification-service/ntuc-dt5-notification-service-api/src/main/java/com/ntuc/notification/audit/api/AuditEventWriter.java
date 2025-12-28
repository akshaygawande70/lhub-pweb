package com.ntuc.notification.audit.api;

/**
 * Single write path for audit persistence.
 *
 * Core rules:
 * - Callers write immutable {@link AuditEvent} only through this interface.
 * - The AuditLog table is the single source of truth (ops must not rely on server logs).
 *
 * Email alerting:
 * - Emails MUST be triggered only based on persisted audit records.
 * - The writer implementation may invoke a post-persist trigger, but only AFTER the audit row is persisted,
 *   and the trigger must drive logic from the persisted audit record (e.g., auditLogId), not from caller context.
 */
public interface AuditEventWriter {
    void write(AuditEvent event);
}
