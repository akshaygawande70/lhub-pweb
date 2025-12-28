package com.ntuc.notification.emailtemplate.internal;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import com.ntuc.notification.configuration.Dt5EmailTemplateConfiguration;
import com.ntuc.notification.emailtemplate.api.Dt5EmailTemplateAdminService;
import com.ntuc.notification.emailtemplate.api.Dt5EmailTemplateUpdateRequest;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.UUID;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * System-scope persistence for DT5 alert email templates using OSGi ConfigurationAdmin.
 *
 * Audit:
 * - Writes STARTED + SUCCESS/FAILED audit events.
 * - Ops can reconstruct the save attempt from audit DB (no server logs).
 */
@Component(service = Dt5EmailTemplateAdminService.class)
public class Dt5EmailTemplateAdminServiceImpl implements Dt5EmailTemplateAdminService {

    private static final int MAX_SUBJECT_LEN = 180;
    private static final int MAX_BODY_LEN = 20000;

    private static final String PID = Dt5EmailTemplateConfiguration.class.getName();

    @Override
    public void saveSystemTemplates(Dt5EmailTemplateUpdateRequest req) {

        String corrId = UUID.randomUUID().toString();

        AuditEvent started = AuditEvent.builder()
            .correlationId(corrId)
            .category(AuditCategory.DT5_FLOW)
            .step(AuditStep.EMAIL_TEMPLATES_SAVE)
            .status(AuditStatus.STARTED)
            .severity(AuditSeverity.INFO)
            .message("Saving DT5 email templates")
            .detail("scope", "system")
            .detail("pid", PID)
            .build();

        _auditEventWriter.write(started);

        try {
            Dictionary<String, Object> props = new Hashtable<String, Object>();

            props.put("dt5FailureSubject", clamp(req.getDt5FailureSubject(), MAX_SUBJECT_LEN));
            props.put("dt5FailureBody", clamp(req.getDt5FailureBody(), MAX_BODY_LEN));

            props.put("clsFailureSubject", clamp(req.getClsFailureSubject(), MAX_SUBJECT_LEN));
            props.put("clsFailureBody", clamp(req.getClsFailureBody(), MAX_BODY_LEN));

            props.put("jaFailureSubject", clamp(req.getJaFailureSubject(), MAX_SUBJECT_LEN));
            props.put("jaFailureBody", clamp(req.getJaFailureBody(), MAX_BODY_LEN));

            Configuration cfg = _configurationAdmin.getConfiguration(PID, null);

            Dictionary<String, Object> existing = cfg.getProperties();
            if (existing != null) {
                java.util.Enumeration<String> keys = existing.keys();
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();
                    if (props.get(key) == null) {
                        props.put(key, existing.get(key));
                    }
                }
            }

            cfg.update(props);

            AuditEvent success = started.toBuilder()
                .status(AuditStatus.SUCCESS)
                .severity(AuditSeverity.INFO)
                .message("DT5 email templates saved successfully")
                .build();

            _auditEventWriter.write(success);
        }
        catch (Exception e) {
            AuditEvent failed = started.toBuilder()
                .status(AuditStatus.FAILED)
                .severity(AuditSeverity.ERROR)
                .errorCode(AuditErrorCode.DT5_TEMPLATE_UPDATE_FAILED)
                .errorMessage("Failed to save DT5 email templates")
                .exceptionClass(e.getClass().getName())
                .build();

            _auditEventWriter.write(failed);

            throw new RuntimeException("Unable to save " + PID, e);
        }
    }

    private static String clamp(String value, int maxLen) {
        if (value == null) {
            return "";
        }
        if (value.length() <= maxLen) {
            return value;
        }
        return value.substring(0, maxLen);
    }

    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Reference
    private AuditEventWriter _auditEventWriter;
}
