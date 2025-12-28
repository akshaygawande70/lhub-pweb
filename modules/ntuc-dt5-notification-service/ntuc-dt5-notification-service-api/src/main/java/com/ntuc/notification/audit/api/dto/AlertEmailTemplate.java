package com.ntuc.notification.audit.api.dto;

import com.ntuc.notification.audit.api.constants.AlertEmailCategory;

import java.io.Serializable;
import java.util.Objects;

/**
 * Immutable alert email template selected by {@link AlertEmailCategory}.
 *
 * <p>Security rules:</p>
 * <ul>
 *   <li>Template content must never include secrets or raw payloads.</li>
 *   <li>Only whitelisted tokens (AlertEmailToken) will be rendered by service.</li>
 * </ul>
 */
public final class AlertEmailTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    private final AlertEmailCategory category;
    private final String subjectTemplate;
    private final String bodyTemplate;

    public AlertEmailTemplate(
            AlertEmailCategory category,
            String subjectTemplate,
            String bodyTemplate) {

        this.category = Objects.requireNonNull(category, "category");
        this.subjectTemplate = subjectTemplate == null ? "" : subjectTemplate;
        this.bodyTemplate = bodyTemplate == null ? "" : bodyTemplate;
    }

    public AlertEmailCategory getCategory() {
        return category;
    }

    public String getSubjectTemplate() {
        return subjectTemplate;
    }

    public String getBodyTemplate() {
        return bodyTemplate;
    }
}
