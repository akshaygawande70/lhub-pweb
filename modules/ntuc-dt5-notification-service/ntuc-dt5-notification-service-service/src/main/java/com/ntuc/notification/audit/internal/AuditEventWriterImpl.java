package com.ntuc.notification.audit.internal;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.constants.AuditEventTopics;
import com.ntuc.notification.model.AuditLog;
import com.ntuc.notification.service.AuditLogLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * Persists immutable AuditEvent into the AuditLog table.
 *
 * Rules:
 * - Persist is the primary responsibility.
 * - Post-persist notifications are emitted via OSGi EventAdmin (decoupled).
 * - Must never throw to callers.
 *
 * Note:
 * - ServiceBuilder LocalServices are NOT unit-tested.
 * - Mapping logic is extracted into {@link AuditEventDbMapper} which IS unit-tested.
 */
@Component(service = AuditEventWriter.class)
public class AuditEventWriterImpl implements AuditEventWriter {

    private static final Log _log = LogFactoryUtil.getLog(AuditEventWriterImpl.class);

    @Reference
    private CounterLocalService _counterLocalService;

    @Reference
    private AuditLogLocalService _auditLogLocalService;

    /**
     * Optional: audit persistence must work even if EventAdmin is unavailable.
     */
    @Reference(
        cardinality = ReferenceCardinality.OPTIONAL,
        policy = ReferencePolicy.DYNAMIC,
        unbind = "unsetEventAdmin"
    )
    private volatile EventAdmin _eventAdmin;

    private final AuditEventDbMapper _dbMapper = new AuditEventDbMapper();

    @Override
    public void write(AuditEvent event) {
        if (event == null) {
            return;
        }

        try {
            long auditLogId = _counterLocalService.increment(AuditLog.class.getName());

            AuditLog row = _auditLogLocalService.createAuditLog(auditLogId);

            _dbMapper.mapInto(row, event);

            // Persist first (source of truth)
            _auditLogLocalService.addAuditLog(row);

            // Notify observers (best-effort). Observers MUST re-load by auditLogId.
            publishAuditPersistedEventBestEffort(auditLogId, event.getCompanyId());
        }
        catch (Throwable ignore) {
            if (_log.isDebugEnabled()) {
                _log.debug(
                    "AuditEvent persistence failed (suppressed). correlationId=" + event.getCorrelationId(),
                    ignore
                );
            }
        }
    }

    private void publishAuditPersistedEventBestEffort(long auditLogId, long companyId) {
        EventAdmin ea = _eventAdmin;
        if (ea == null) {
            return;
        }

        try {
            Map<String, Object> props = new HashMap<String, Object>();
            props.put(AuditEventTopics.PROP_AUDIT_LOG_ID, Long.valueOf(auditLogId));
            props.put(AuditEventTopics.PROP_COMPANY_ID, Long.valueOf(companyId));

            ea.postEvent(new Event(AuditEventTopics.AUDIT_PERSISTED, props));
        }
        catch (Throwable ignore) {
            // Never throw. Audit persistence already succeeded.
        }
    }

    protected void unsetEventAdmin(EventAdmin eventAdmin) {
        if (_eventAdmin == eventAdmin) {
            _eventAdmin = null;
        }
    }
}
