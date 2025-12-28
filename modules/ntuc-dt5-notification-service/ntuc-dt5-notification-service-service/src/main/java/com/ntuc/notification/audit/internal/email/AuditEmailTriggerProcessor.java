package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.audit.util.DbAlertDeduplicator;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.email.EmailSender;
import com.ntuc.notification.email.api.AlertEmailCategory;
import com.ntuc.notification.email.api.AuditEmailTriggerDeps;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Pure Java processor for audit-driven alert emails.
 *
 * IMPORTANT:
 * - MUST NOT throw to callers.
 * - MUST audit ALL outcomes (sent / suppressed / failed).
 * - MUST rely only on persisted AuditEvent.
 */
public class AuditEmailTriggerProcessor {

    private final AuditEmailTriggerDeps.PolicyResolver policyResolver;
    private final AuditEmailTriggerDeps.FingerprintBuilder fingerprintBuilder;
    private final AuditEmailTriggerDeps.EmailCategoryResolver emailCategoryResolver;
    private final AuditEmailTriggerDeps.TemplateModelBuilder templateModelBuilder;
    private final AuditEmailTriggerDeps.TemplateResolver templateResolver;

    private final DbAlertDeduplicator dbDeduplicator;
    private final EmailSender emailSender;
    private final AuditEventWriter auditEventWriter;
    private final int defaultDedupeWindowMinutes;

    public AuditEmailTriggerProcessor(
        AuditEmailTriggerDeps.PolicyResolver policyResolver,
        AuditEmailTriggerDeps.FingerprintBuilder fingerprintBuilder,
        AuditEmailTriggerDeps.EmailCategoryResolver emailCategoryResolver,
        AuditEmailTriggerDeps.TemplateModelBuilder templateModelBuilder,
        AuditEmailTriggerDeps.TemplateResolver templateResolver,
        DbAlertDeduplicator dbDeduplicator,
        EmailSender emailSender,
        AuditEventWriter auditEventWriter,
        int defaultDedupeWindowMinutes
    ) {
        this.policyResolver = Objects.requireNonNull(policyResolver);
        this.fingerprintBuilder = Objects.requireNonNull(fingerprintBuilder);
        this.emailCategoryResolver = Objects.requireNonNull(emailCategoryResolver);
        this.templateModelBuilder = Objects.requireNonNull(templateModelBuilder);
        this.templateResolver = Objects.requireNonNull(templateResolver);
        this.dbDeduplicator = Objects.requireNonNull(dbDeduplicator);
        this.emailSender = Objects.requireNonNull(emailSender);
        this.auditEventWriter = Objects.requireNonNull(auditEventWriter);
        this.defaultDedupeWindowMinutes = defaultDedupeWindowMinutes;
    }

    public void handlePersistedEvent(
        String auditLogId,
        AuditEvent event,
        Map<ParameterKeyEnum, Object> paramValues
    ) {
        if (event == null || event.getCategory() == AuditCategory.ALERT_EMAIL) {
            return;
        }

        Map<ParameterKeyEnum, Object> pv =
            (paramValues == null) ? Collections.emptyMap() : paramValues;

        Object policyObj = policyResolver.resolve(pv, defaultDedupeWindowMinutes);
        if (policyObj == null || !policyResolver.isEligible(policyObj, event)) {
            return;
        }

        AlertPolicy policy = castPolicy(policyObj);

        AlertEmailCategory category = emailCategoryResolver.resolve(event);
        if (category == null) {
            return;
        }

        String fingerprint = fingerprintBuilder.build(policy, event, category);

        boolean allowed;
        try {
            allowed = dbDeduplicator.isAllowed(
                event.getCompanyId(),
                fingerprint,
                System.currentTimeMillis(),
                policy.getDedupeWindowMinutes()
            );
        }
        catch (RuntimeException ex) {
            auditDedupeCheckFailed(auditLogId, event, category, fingerprint, ex);
            allowed = true; // fail-open
        }

        if (!allowed) {
            auditSuppressed(auditLogId, event, policy, category, fingerprint);
            return;
        }

        send(auditLogId, policy, event, category, fingerprint);
    }

    private void send(
        String auditLogId,
        AlertPolicy policy,
        AuditEvent e,
        AlertEmailCategory category,
        String fingerprint
    ) {
        AuditEmailTriggerDeps.TemplatePair tpl = templateResolver.resolve(category);
        if (tpl == null) {
            auditSendFailed(
                auditLogId,
                e,
                category,
                fingerprint,
                resolveToEmails(policy),
                new IllegalStateException("No template for " + category)
            );
            return;
        }

        Map<String, String> tokens =
            templateModelBuilder.build(e, auditLogId, fingerprint);

        String subject = EmailTemplateEngine.render(tpl.getSubject(), tokens);
        String body = EmailTemplateEngine.render(tpl.getBody(), tokens);
        String to = resolveToEmails(policy);

        try {
            emailSender.send(e.getCompanyId(), to, subject, body, true);
            auditSent(auditLogId, e, category, fingerprint, to);
        }
        catch (Exception ex) {
            auditSendFailed(auditLogId, e, category, fingerprint, to, ex);
        }
    }

    private AlertPolicy castPolicy(Object o) {
        if (!(o instanceof AlertPolicy)) {
            throw new IllegalStateException("Unsupported policy type: " +
                (o == null ? "null" : o.getClass().getName()));
        }
        return (AlertPolicy) o;
    }

    private String resolveToEmails(AlertPolicy policy) {
        String to = (policy == null) ? "" : safe(policy.getFallbackToEmail());
        return to.isEmpty() ? "ops@example.com" : to;
    }

    // ================= AUDIT HELPERS =================

    private void auditSuppressed(
        String auditLogId,
        AuditEvent src,
        AlertPolicy policy,
        AlertEmailCategory cat,
        String fingerprint
    ) {
        auditEventWriter.write(
            base(src)
                .step(AuditStep.EMAIL_SUPPRESSED)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.SKIPPED)
                .message("Alert email suppressed by dedupe policy")
                .detail("sourceAuditLogId", safe(auditLogId))
                .detail("alertCategory", cat.name())
                .detail("alertFingerprint", fingerprint)
                .detail("dedupeFingerprint", fingerprint)
                .detail("windowMinutes", String.valueOf(policy.getDedupeWindowMinutes()))
                .build()
        );
    }

    private void auditSent(
        String auditLogId,
        AuditEvent src,
        AlertEmailCategory cat,
        String fingerprint,
        String to
    ) {
        auditEventWriter.write(
            base(src)
                .step(AuditStep.EMAIL_SENT)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.SUCCESS)
                .message("Alert email sent")
                .detail("sourceAuditLogId", safe(auditLogId))
                .detail("alertCategory", cat.name())
                .detail("alertFingerprint", fingerprint)
                .detail("dedupeFingerprint", fingerprint)
                .detail("to", safe(to))
                .build()
        );
    }

    private void auditSendFailed(
        String auditLogId,
        AuditEvent src,
        AlertEmailCategory cat,
        String fingerprint,
        String to,
        Exception ex
    ) {
        auditEventWriter.write(
            base(src)
                .step(AuditStep.EMAIL_SEND_FAILED)
                .severity(AuditSeverity.ERROR)
                .status(AuditStatus.FAILED)
                .message("Alert email send failed")
                .errorMessage(safe(ex.getMessage()))
                .exceptionClass(ex.getClass().getName())
                .detail("sourceAuditLogId", safe(auditLogId))
                .detail("alertCategory", cat.name())
                .detail("alertFingerprint", fingerprint)
                .detail("dedupeFingerprint", fingerprint)
                .detail("to", safe(to))
                .build()
        );
    }

    private void auditDedupeCheckFailed(
        String auditLogId,
        AuditEvent src,
        AlertEmailCategory cat,
        String fingerprint,
        RuntimeException ex
    ) {
        auditEventWriter.write(
            base(src)
                .step(AuditStep.EMAIL_DEDUPE_CHECK_FAILED)
                .severity(AuditSeverity.WARNING)
                .status(AuditStatus.PARTIAL)
                .message("Alert email dedupe check failed; proceeding without suppression")
                .errorMessage(safe(ex.getMessage()))
                .exceptionClass(ex.getClass().getName())
                .detail("sourceAuditLogId", safe(auditLogId))
                .detail("alertCategory", cat.name())
                .detail("alertFingerprint", fingerprint)
                .detail("dedupeFingerprint", fingerprint)
                .build()
        );
    }

    private static AuditEvent.Builder base(AuditEvent src) {
        return AuditEvent.builder()
            .companyId(src.getCompanyId())
            .groupId(src.getGroupId())
            .userId(src.getUserId())
            .category(AuditCategory.ALERT_EMAIL)
            .correlationId(src.getCorrelationId())
            .jobRunId(src.getJobRunId())
            .requestId(src.getRequestId())
            .eventId(src.getEventId())
            .courseCode(src.getCourseCode())
            .ntucDTId(src.getNtucDTId());
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }
}
