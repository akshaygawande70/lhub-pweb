package com.ntuc.notification.audit.internal.email;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditEventTopics;
import com.ntuc.notification.audit.util.DbAlertDeduplicator;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.constants.ParameterValuesProvider;
import com.ntuc.notification.email.EmailSender;
import com.ntuc.notification.email.api.AuditEmailTriggerDeps;
import com.ntuc.notification.email.api.AlertEmailCategory;
import com.ntuc.notification.service.AuditLogLocalService;

import java.util.Collections;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

@Component(
    service = {AuditEmailTrigger.class, EventHandler.class},
    property = "event.topics=" + AuditEventTopics.AUDIT_PERSISTED
)
public class AuditEmailTriggerImpl implements AuditEmailTrigger, EventHandler {

    private static final Log _log = LogFactoryUtil.getLog(AuditEmailTriggerImpl.class);

    private static final int DEFAULT_DEDUPE_WINDOW_MINUTES = 5;

    @Reference
    private ParameterValuesProvider _parameterValuesProvider;

    @Reference
    private EmailSender _emailSender;

    @Reference
    private AuditLogLocalService _auditLogLocalService;

    @Reference
    private ConfigurationProvider _configurationProvider;

    @Reference
    private AuditEventWriter _auditEventWriter;

    private volatile Map<ParameterKeyEnum, Object> _paramValues;

    private volatile PersistedAuditEventReader _persistedReader;

    // Pure Java processor (unit-test this; do NOT unit test OSGi component)
    private volatile AuditEmailTriggerProcessor _processor;

    @Activate
    protected void activate() {
        _persistedReader = new PersistedAuditEventReader(_auditLogLocalService);

        DbAlertDeduplicator.RecentAlertOutcomeLookup lookup =
            new AuditLogRecentAlertOutcomeLookup(_auditLogLocalService);

        DbAlertDeduplicator dbDeduplicator = new DbAlertDeduplicator(lookup);

        final AlertPolicyResolver policyDelegate = new AlertPolicyResolver();
        final AlertFingerprintBuilder fingerprintDelegate = new AlertFingerprintBuilder();
        final AlertEmailCategoryResolver categoryDelegate = new AlertEmailCategoryResolver();
        final AuditEmailTemplateModelBuilder modelDelegate = new AuditEmailTemplateModelBuilder();
        final AuditEmailTemplateResolver templateDelegate = new AuditEmailTemplateResolver(_configurationProvider);

        _processor = new AuditEmailTriggerProcessor(
            new AuditEmailTriggerDeps.PolicyResolver() {
                @Override
                public Object resolve(Map<ParameterKeyEnum, Object> paramValues, int defaultDedupeWindowMinutes) {
                    return policyDelegate.resolve(paramValues, defaultDedupeWindowMinutes);
                }

                @Override
                public boolean isEligible(Object policy, AuditEvent event) {
                    return policyDelegate.isEligible((AlertPolicy) policy, event);
                }
            },
            new AuditEmailTriggerDeps.FingerprintBuilder() {
                @Override
                public String build(Object policy, AuditEvent event, AlertEmailCategory category) {
                    return fingerprintDelegate.build((AlertPolicy) policy, event, category);
                }
            },
            new AuditEmailTriggerDeps.EmailCategoryResolver() {
                @Override
                public AlertEmailCategory resolve(AuditEvent event) {
                    return categoryDelegate.resolve(event);
                }
            },
            new AuditEmailTriggerDeps.TemplateModelBuilder() {
                @Override
                public Map<String, String> build(AuditEvent event, String auditLogId, String fingerprint) {
                    return modelDelegate.build(event, auditLogId, fingerprint);
                }
            },
            new AuditEmailTriggerDeps.TemplateResolver() {
                @Override
                public AuditEmailTriggerDeps.TemplatePair resolve(AlertEmailCategory category) {
                    AuditEmailTemplateResolver.TemplatePair p = templateDelegate.resolve(category);
                    return new AuditEmailTriggerDeps.TemplatePair(p.getSubject(), p.getBody());
                }
            },
            dbDeduplicator,
            _emailSender,
            _auditEventWriter,
            DEFAULT_DEDUPE_WINDOW_MINUTES
        );

        refreshParamsBestEffort();
    }

    /**
     * EventAdmin entrypoint (preferred): called after AuditLog persistence.
     */
    @Override
    public void handleEvent(Event event) {
        if (event == null) {
            return;
        }

        // Always drive from persisted auditLogId
        Object id = event.getProperty(AuditEventTopics.PROP_AUDIT_LOG_ID);
        long auditLogId = toLong(id);

        if (auditLogId > 0) {
            onAuditPersisted(auditLogId);
        }
    }

    @Override
    public void onAuditPersisted(long auditLogId) {
        try {
            AuditEvent persisted = reader().read(auditLogId);
            if (persisted == null) {
                return;
            }

            // Recursion guard (redundant safety): never alert on alert-email events.
            if (persisted.getCategory() == AuditCategory.ALERT_EMAIL) {
                return;
            }

            processor().handlePersistedEvent(String.valueOf(auditLogId), persisted, paramValues());
        }
        catch (Throwable t) {
            if (_log.isDebugEnabled()) {
                _log.debug("AuditEmailTriggerImpl suppressed exception (auditLogId=" + auditLogId + ")", t);
            }
        }
    }

    @Override
    public void onAuditPersisted(AuditEvent event) {
        // Transitional, strict no-op unless auditLogId exists.
        if (event == null) {
            return;
        }

        Object auditLogId = (event.getDetails() == null) ? null : event.getDetails().get("auditLogId");
        long id = toLong(auditLogId);

        if (id > 0) {
            onAuditPersisted(id);
        }
    }

    private long toLong(Object v) {
        if (v == null) {
            return 0L;
        }
        if (v instanceof Long) {
            return ((Long) v).longValue();
        }
        if (v instanceof Integer) {
            return ((Integer) v).longValue();
        }
        try {
            return Long.parseLong(String.valueOf(v).trim());
        }
        catch (Exception ignore) {
            return 0L;
        }
    }

    private Map<ParameterKeyEnum, Object> paramValues() {
        Map<ParameterKeyEnum, Object> pv = _paramValues;
        if (pv == null || pv.isEmpty()) {
            refreshParamsBestEffort();
            pv = _paramValues;
        }
        return (pv == null) ? Collections.<ParameterKeyEnum, Object>emptyMap() : pv;
    }

    private void refreshParamsBestEffort() {
        try {
            _paramValues = _parameterValuesProvider.getAllParameterValues();
        }
        catch (Throwable t) {
            _paramValues = Collections.<ParameterKeyEnum, Object>emptyMap();
        }
    }

    private PersistedAuditEventReader reader() {
        PersistedAuditEventReader r = _persistedReader;
        if (r == null) {
            r = new PersistedAuditEventReader(_auditLogLocalService);
            _persistedReader = r;
        }
        return r;
    }

    private AuditEmailTriggerProcessor processor() {
        AuditEmailTriggerProcessor p = _processor;
        if (p == null) {
            throw new IllegalStateException("AuditEmailTriggerProcessor is not initialized");
        }
        return p;
    }
}
