package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.util.DbAlertDeduplicator;
import com.ntuc.notification.service.AuditLogLocalService;

import java.util.Objects;

/**
 * Liferay/ServiceBuilder-backed lookup implementation for DbAlertDeduplicator.
 *
 * <p>
 * This is plumbing (OSGi/service layer) and intentionally thin:
 * it delegates to {@link AuditLogLocalService#existsRecentAlertOutcome(long, String, long)}.
 * </p>
 *
 * <p>
 * Unit tests are not required for this class (Liferay-bound wiring).
 * </p>
 */
public class AuditLogRecentAlertOutcomeLookup implements DbAlertDeduplicator.RecentAlertOutcomeLookup {

    private final AuditLogLocalService auditLogLocalService;

    public AuditLogRecentAlertOutcomeLookup(AuditLogLocalService auditLogLocalService) {
        this.auditLogLocalService = Objects.requireNonNull(auditLogLocalService, "auditLogLocalService");
    }

    @Override
    public boolean existsRecentOutcome(long companyId, String fingerprint, long sinceStartTimeMs) {
        return auditLogLocalService.existsRecentAlertOutcome(companyId, fingerprint, sinceStartTimeMs);
    }
}
