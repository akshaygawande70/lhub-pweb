package com.ntuc.notification.service.impl;

import com.liferay.portal.aop.AopService;
import com.ntuc.notification.model.AuditLog;
import com.ntuc.notification.service.base.AuditLogLocalServiceBaseImpl;

import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * The implementation of the audit log local service.
 *
 * This is a local service. Methods of this service will not have security checks.
 */
@Component(
    property = "model.class.name=com.ntuc.notification.model.AuditLog",
    service = AopService.class
)
public class AuditLogLocalServiceImpl extends AuditLogLocalServiceBaseImpl {

    @Override
    public List<AuditLog> getAuditLogs(int start, int end) {
        return auditLogPersistence.findAll(start, end);
    }

    @Override
    public int getAuditLogsCount() {
        return auditLogPersistence.countAll();
    }

    /**
     * DB-backed dedupe check for alert emails (cluster-safe).
     *
     * We consider both EMAIL_SENT and EMAIL_SUPPRESSED as "recent outcomes" so we do not spam ops.
     * NOTE: we intentionally ignore EMAIL_SEND_FAILED so that transient mail outages can retry.
     *
     * @param companyId company scope
     * @param fingerprint dedupe fingerprint (required)
     * @param sinceStartTimeMs window start (inclusive)
     * @return true if a recent outcome exists within the window
     */
    @Override
    public boolean existsRecentAlertOutcome(long companyId, String fingerprint, long sinceStartTimeMs) {
        if (companyId <= 0 || fingerprint == null || fingerprint.trim().isEmpty() || sinceStartTimeMs <= 0) {
            return false;
        }

        String fp = fingerprint.trim();

        // category is constant here because finder includes it
        List<AuditLog> rows =
            auditLogPersistence.findByCompanyCategoryAlertFingerprintStartTimeMs(
                companyId, "ALERT_EMAIL", fp, sinceStartTimeMs, 0, 10);

        if (rows == null || rows.isEmpty()) {
            return false;
        }

        for (AuditLog r : rows) {
            if (r == null) {
                continue;
            }

            String step = r.getStep();
            if ("EMAIL_SENT".equals(step) || "EMAIL_SUPPRESSED".equals(step)) {
                return true;
            }
        }

        return false;
    }
}
