package com.ntuc.notification.auditlog.api;

/**
 * Service facade used by the Audit Log Portlet to query AuditLog records.
 *
 * <p>Rules:
 * - Results come from DB only (AuditLog is the single source of truth)
 * - Free-text and date-range validations are enforced in service implementation</p>
 */
public interface AuditLogQueryService {

    AuditLogQueryResult search(AuditLogQueryRequest request);

}
