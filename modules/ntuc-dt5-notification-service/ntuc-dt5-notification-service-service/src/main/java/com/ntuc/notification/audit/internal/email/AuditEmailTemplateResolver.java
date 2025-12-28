package com.ntuc.notification.audit.internal.email;

import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.ntuc.notification.configuration.Dt5EmailTemplateConfiguration;
import com.ntuc.notification.email.api.AlertEmailCategory;

import java.util.Objects;

/**
 * Resolves subject/body HTML templates from system configuration.
 *
 * <p>Rules:</p>
 * <ul>
 *   <li>Templates are selected by {@link AlertEmailCategory} only.</li>
 *   <li>No email sending occurs here.</li>
 *   <li>No auditing occurs here.</li>
 *   <li>On configuration read failure this class MUST throw so caller can audit.</li>
 *   <li>Template content is defensively size-capped.</li>
 * </ul>
 */
public class AuditEmailTemplateResolver {

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

    // Keep aligned with UI maxLength=180
    static final int MAX_SUBJECT_LEN = 180;

    // Hard cap for safety (CKEditor sometimes stores very large HTML)
    static final int MAX_BODY_LEN = 20000;

    private final ConfigurationProvider configurationProvider;

    public AuditEmailTemplateResolver(ConfigurationProvider configurationProvider) {
        this.configurationProvider = Objects.requireNonNull(configurationProvider, "configurationProvider");
    }

    public TemplatePair resolve(AlertEmailCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("category is required");
        }

        Dt5EmailTemplateConfiguration cfg = readSystemConfigOrThrow();

        switch (category) {
            case CLS_FAILURE:
                return new TemplatePair(
                    clamp(cfg.clsFailureSubject(), MAX_SUBJECT_LEN),
                    clamp(cfg.clsFailureBody(), MAX_BODY_LEN)
                );
            case JA_FAILURE:
                return new TemplatePair(
                    clamp(cfg.jaFailureSubject(), MAX_SUBJECT_LEN),
                    clamp(cfg.jaFailureBody(), MAX_BODY_LEN)
                );
            case DT5_FAILURE:
            default:
                return new TemplatePair(
                    clamp(cfg.dt5FailureSubject(), MAX_SUBJECT_LEN),
                    clamp(cfg.dt5FailureBody(), MAX_BODY_LEN)
                );
        }
    }

    private Dt5EmailTemplateConfiguration readSystemConfigOrThrow() {
        try {
            Dt5EmailTemplateConfiguration cfg =
                configurationProvider.getSystemConfiguration(Dt5EmailTemplateConfiguration.class);

            if (cfg == null) {
                throw new IllegalStateException("Dt5EmailTemplateConfiguration returned null");
            }

            return cfg;
        }
        catch (Exception e) {
            throw new IllegalStateException("Unable to load Dt5EmailTemplateConfiguration", e);
        }
    }

    private static String clamp(String v, int maxLen) {
        if (v == null) {
            return "";
        }
        if (v.length() <= maxLen) {
            return v;
        }
        return v.substring(0, maxLen);
    }
}
