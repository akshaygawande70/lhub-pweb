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
 * Writes immutable {@link AuditEvent} records to the persistent AuditLog store and emits
 * a decoupled post-persist notification for downstream observers.
 *
 * <p>Business purpose: Ensure every workflow action produces a durable, queryable audit trail record
 * that downstream alerting/UI components can react to without tight coupling.</p>
 *
 * <p>Technical purpose: Map {@link AuditEvent} into a ServiceBuilder {@link AuditLog} row, persist it,
 * then (best-effort) post an OSGi EventAdmin event containing only identifiers for observers to reload.</p>
 *
 * <p>Reliability rules:</p>
 * <ul>
 *   <li>Persistence is the single source of truth.</li>
 *   <li>EventAdmin publishing is optional and best-effort.</li>
 *   <li>No exception is ever propagated to callers.</li>
 * </ul>
 *
 * <p>Testing note: ServiceBuilder LocalServices are typically excluded from plain unit tests.
 * Mapping logic lives in {@link AuditEventDbMapper} and is intended to be unit-tested independently.</p>
 *
 * @author @akshaygawande
 */
@Component(service = AuditEventWriter.class)
public class AuditEventWriterImpl implements AuditEventWriter {

    private static final Log _log = LogFactoryUtil.getLog(AuditEventWriterImpl.class);

    @Reference
    private CounterLocalService _counterLocalService;

    @Reference
    private AuditLogLocalService _auditLogLocalService;

    /**
     * Optional dependency. Audit persistence must succeed even when EventAdmin is absent.
     *
     * <p>Volatile + dynamic binding ensures safe reads across threads while allowing OSGi to bind/unbind
     * the service at runtime without restarting this component.</p>
     */
    @Reference(
        cardinality = ReferenceCardinality.OPTIONAL,
        policy = ReferencePolicy.DYNAMIC,
        unbind = "unsetEventAdmin"
    )
    private volatile EventAdmin _eventAdmin;

    /**
     * Pure mapping helper that translates API events into SB row fields.
     *
     * <p>Kept as a dedicated collaborator so mapping rules remain deterministic and testable
     * without involving persistence or OSGi wiring.</p>
     */
    private final AuditEventDbMapper _dbMapper = new AuditEventDbMapper();

    /**
     * Persists an {@link AuditEvent} to the AuditLog table and emits a post-persist notification (best-effort).
     *
     * <p><b>Purpose (Business):</b> Records a durable audit trail entry so operations and UI can trace
     * workflow outcomes, failures, and alert dedupe decisions.</p>
     *
     * <p><b>Purpose (Technical):</b> Allocates a new AuditLog ID, maps the event into a SB model,
     * persists via {@link AuditLogLocalService}, and posts an OSGi event with identifiers only.</p>
     *
     * <p><b>Inputs / Invariants:</b>
     * <ul>
     *   <li>If {@code event} is {@code null}, no action is performed.</li>
     *   <li>Callers must not rely on exceptions for control flow; failures are suppressed by design.</li>
     * </ul>
     * </p>
     *
     * <p><b>Side effects:</b>
     * <ul>
     *   <li>DB write: inserts an {@link AuditLog} row.</li>
     *   <li>Async: posts an OSGi EventAdmin event (best-effort) after persistence succeeds.</li>
     * </ul>
     * </p>
     *
     * <p><b>Audit behavior:</b> This writer is the persistence layer for already-formed audit events.
     * It does not generate additional audit steps; it stores what it is given.</p>
     *
     * <p><b>Return semantics:</b> Returns {@code void}. Never throws; all failures are suppressed.</p>
     *
     * @param event immutable audit event to persist; ignored when {@code null}
     */
    @Override
    public void write(AuditEvent event) {
        if (event == null) {
            return;
        }

        try {
            // Allocate an ID up-front to ensure observers can re-load the persisted row by primary key.
            long auditLogId = _counterLocalService.increment(AuditLog.class.getName());

            // Create the SB row, then map all event fields into it deterministically.
            AuditLog row = _auditLogLocalService.createAuditLog(auditLogId);

            _dbMapper.mapInto(row, event);

            // Persist first: persistence is the source of truth for downstream observers.
            _auditLogLocalService.addAuditLog(row);

            // Notify observers (best-effort). Observers must re-load using auditLogId.
            publishAuditPersistedEventBestEffort(auditLogId, event.getCompanyId());
        }
        catch (Throwable ignore) {
            // Never throw to callers. Auditing must not block primary workflows.
            if (_log.isDebugEnabled()) {
                _log.debug(
                    "AuditEvent persistence failed (suppressed). correlationId=" + event.getCorrelationId(),
                    ignore
                );
            }
        }
    }

    /**
     * Posts a decoupled "audit persisted" notification using EventAdmin, without impacting persistence outcomes.
     *
     * <p><b>Purpose (Business):</b> Allows alerting/UI/reactive pipelines to respond to newly persisted
     * audit entries without coupling to persistence or workflow orchestration.</p>
     *
     * <p><b>Purpose (Technical):</b> Posts {@link AuditEventTopics#AUDIT_PERSISTED} containing only
     * the {@code auditLogId} and {@code companyId} so observers can safely re-fetch from the DB.</p>
     *
     * <p><b>Inputs / Invariants:</b>
     * <ul>
     *   <li>EventAdmin may be {@code null} (optional dependency). In that case, this method is a no-op.</li>
     *   <li>Posting failures must not surface to the caller.</li>
     * </ul>
     * </p>
     *
     * <p><b>Side effects:</b> Async event post via OSGi EventAdmin.</p>
     *
     * <p><b>Audit behavior:</b> No audit rows are generated here; it only emits a notification.</p>
     *
     * <p><b>Return semantics:</b> Returns {@code void}. Never throws; failures are suppressed.</p>
     *
     * @param auditLogId persisted AuditLog primary key for observers to reload
     * @param companyId  company scope for routing/partitioning by observers
     */
    private void publishAuditPersistedEventBestEffort(long auditLogId, long companyId) {
        // Read volatile once to avoid races where it changes mid-flight.
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
            // Never throw. Audit persistence already succeeded and is the source of truth.
        }
    }

    /**
     * OSGi dynamic unbind callback for {@link EventAdmin}.
     *
     * <p><b>Purpose (Business):</b> Keeps audit persistence available even when optional infrastructure
     * services are restarted or temporarily unavailable.</p>
     *
     * <p><b>Purpose (Technical):</b> Clears the volatile reference only when the unbound instance matches
     * the currently held one, preventing accidental nulling when rebinds occur quickly.</p>
     *
     * <p><b>Inputs / Invariants:</b> {@code eventAdmin} is the instance being unbound by OSGi.</p>
     *
     * <p><b>Side effects:</b> Updates a volatile reference used by async notification publishing.</p>
     *
     * <p><b>Audit behavior:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Returns {@code void}. Never throws.</p>
     *
     * @param eventAdmin the EventAdmin instance being unbound
     */
    protected void unsetEventAdmin(EventAdmin eventAdmin) {
        if (_eventAdmin == eventAdmin) {
            _eventAdmin = null;
        }
    }
}
