package com.ntuc.notification.email.api;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.constants.ParameterKeyEnum;

import java.util.Map;

/**
 * Pure interface contracts used by AuditEmailTriggerProcessor.
 *
 * IMPORTANT:
 * - No Liferay / OSGi types here.
 * - Enables unit testing without mocking concrete/final classes.
 */
public final class AuditEmailTriggerDeps {

    private AuditEmailTriggerDeps() {
        // holder
    }

    public interface PolicyResolver {
        Object resolve(Map<ParameterKeyEnum, Object> paramValues, int defaultDedupeWindowMinutes);
        boolean isEligible(Object policy, AuditEvent event);
    }

    public interface FingerprintBuilder {
        String build(Object policy, AuditEvent event, AlertEmailCategory category);
    }

    public interface EmailCategoryResolver {
        AlertEmailCategory resolve(AuditEvent event);
    }

    public interface TemplateModelBuilder {
        Map<String, String> build(AuditEvent event, String auditLogId, String fingerprint);
    }

    public interface TemplateResolver {
        TemplatePair resolve(AlertEmailCategory category);
    }

    /**
     * Immutable email template pair: subject + body.
     */
    public static final class TemplatePair {
        private final String subject;
        private final String body;

        public TemplatePair(String subject, String body) {
            this.subject = (subject == null) ? "" : subject;
            this.body = (body == null) ? "" : body;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody() {
            return body;
        }
    }
}
