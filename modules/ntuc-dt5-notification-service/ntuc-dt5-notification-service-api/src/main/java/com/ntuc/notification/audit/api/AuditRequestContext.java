package com.ntuc.notification.audit.api;

/**
 * API-safe request context contract for audit utilities.
 *
 * Rules:
 * - Implementations MUST be immutable.
 * - getCorrelationId() MUST be non-blank. Generating correlationId is the caller's responsibility.
 * - getCourseCode() MUST never return null (use "" when unknown).
 * - companyId/groupId/userId SHOULD be non-zero for real Liferay flows.
 *
 * NOTE:
 * - Must live in -api module
 * - Must NOT depend on rest/internal packages
 */
public interface AuditRequestContext {

    String getCorrelationId();

    long getCompanyId();

    long getGroupId();

    long getUserId();

    String getCourseCode();

    long getNtucDTId();
}
