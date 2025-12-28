package com.ntuc.notification.audit.api.spi;

import com.ntuc.notification.audit.api.dto.AuditLogSearchRequest;
import com.ntuc.notification.audit.api.dto.AuditLogSearchResult;

/**
 * Service boundary for audit log searching.
 *
 * Dependency direction:
 * PORTLET -> API (this interface) <- SERVICE (implementation)
 */
public interface AuditLogSearchGateway {

    AuditLogSearchResult search(AuditLogSearchRequest request);
}
