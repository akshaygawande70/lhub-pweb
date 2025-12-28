package com.ntuc.notification.service.impl;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.CourseArticleConfig;
import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.audit.util.MdcUtil;
import com.ntuc.notification.constants.ParameterGroupKeys;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.model.CourseProcessResult;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.rest.internal.processor.validation.CourseEventValidator;
import com.ntuc.notification.service.ClsCourseFieldsProcessor;
import com.ntuc.notification.service.NotificationHandler;
import com.ntuc.notification.service.ScheduleHandler;
import com.ntuc.notification.service.internal.audit.CourseAuditEventFactory;
import com.ntuc.notification.service.internal.builder.CourseEventContextBuilder;
import com.ntuc.notification.service.internal.policy.EventTypePolicy;
import com.ntuc.notification.util.DDMStructureUtil;
import com.ntuc.notification.util.DDMTemplateUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Service-layer orchestrator for CLS course event processing.
 *
 * <p><b>Purpose (Business)</b>:
 * Processes inbound course notifications and retrigger requests to keep NTUC course
 * content in Liferay aligned with upstream CLS data. The outcome of each attempt is
 * persisted as audit events so operations can reconstruct timelines and decisions.</p>
 *
 * <p><b>Purpose (Technical)</b>:
 * - Builds {@link CourseEventContext} and dispatches to {@link NotificationHandler}
 *   for journal article creation/update flows.
 * - Loads runtime configuration values from system parameters (company/group/user,
 *   target folder, template selection).
 * - Resolves Liferay DDM template/structure context required for article operations.
 * - Writes audit events via {@link AuditEventWriter} only; audit records are the
 *   authoritative system-of-record for workflow reconstruction.</p>
 *
 * <p><b>Audit Documentation (IMPORTANT)</b>:
 * - Persisted audit is the single source of truth; server logs are not relied on
 *   for timeline reconstruction.
 * - AuditStep values are intended to stay layer-based and small; fine-grained
 *   distinctions should be expressed via message/details rather than step proliferation.
 * - STARTED semantics: {@code endTimeMs = 0}. Ended statuses must have
 *   {@code endTimeMs >= startTimeMs} (clamped by the factory if needed).
 * - Error events must clearly describe what failed, for what, why (errorCode),
 *   and what action was taken, without including secrets or raw payloads.
 * - Email alerting is audit-driven; this class does not send emails and must not
 *   trigger email as a side-effect of catching exceptions.</p>
 *
 * <p><b>Testability</b>:
 * Pure Java collaborators (validator, policies, builders, factories) are kept as
 * concrete helpers to enable plain JUnit testing without an OSGi container.</p>
 *
 * @author @akshaygawande
 */
@Component(service = ClsCourseFieldsProcessor.class)
public class ClsCourseFieldsProcessorImpl implements ClsCourseFieldsProcessor {

    /**
     * Executes the main course notification workflows (event, cron, retrigger, OTL hooks).
     * This class orchestrates inputs and context; downstream handlers perform the actual work.
     */
    @Reference private NotificationHandler notificationHandler;

    /**
     * Schedule handling is referenced for completeness of the overall processor wiring.
     * (This file currently does not invoke scheduleHandler directly.)
     */
    @Reference private ScheduleHandler scheduleHandler;

    @Reference private UserLocalService userLocalService;
    @Reference private GroupLocalService groupLocalService;
    @Reference private DDMStructureLocalService ddmStructureLocalService;

    /**
     * Source of configured values (companyId/groupId/user email, folderId, template name, etc.).
     * Values are cached on activation to avoid repeated lookups in hot paths.
     */
    @Reference private ParameterGroupKeys parameterGroupKeys;

    @Reference private DDMTemplateUtil ddmTemplateUtil;
    @Reference private DDMStructureUtil ddmStructureUtil;

    /**
     * The only writer used to persist audit events. Audit records must be sufficient
     * to reconstruct the workflow timeline and outcome.
     */
    @Reference private AuditEventWriter auditEventWriter;

    // -------------------------------------------------------------------------
    // Pure Java helpers (unit-testable)
    // -------------------------------------------------------------------------

    /**
     * Validates incoming event payloads (format and required fields) prior to processing.
     * Uses a strict timestamp pattern expected from upstream payloads.
     */
    private final CourseEventValidator eventValidator = new CourseEventValidator("dd/MM/yyyy HH:mm:ss");

    /**
     * Encodes the supported event types and cron vs non-cron processing decisions.
     */
    private final EventTypePolicy eventTypePolicy = new EventTypePolicy();

    /**
     * Constructs the immutable context object passed across downstream layers.
     */
    private final CourseEventContextBuilder eventContextBuilder = new CourseEventContextBuilder();

    /**
     * Creates strongly-typed audit events for each key workflow outcome.
     * The factory is responsible for enforcing audit timing semantics and message clarity.
     */
    private final CourseAuditEventFactory auditEventFactory = new CourseAuditEventFactory();

    /**
     * Cached parameter values loaded at component activation.
     * This map is treated as read-only during runtime processing.
     */
    private Map<ParameterKeyEnum, Object> paramValues;

    /**
     * Loads configured parameter values once the component is activated.
     */
    @Activate
    protected void activate() {
        paramValues = parameterGroupKeys.getAllParameterValues();
    }

    /**
     * Handles a single inbound course notification event.
     *
     * <p>Flow summary:</p>
     * <ol>
     *   <li>Validate event payload</li>
     *   <li>Apply event-type policy (cron vs non-cron supported types)</li>
     *   <li>Resolve company/group/user and DDM template/structure context</li>
     *   <li>Build {@link CourseArticleConfig} and {@link CourseEventContext}</li>
     *   <li>Write audit "started" event</li>
     *   <li>Dispatch to {@link NotificationHandler} for actual processing</li>
     *   <li>Write audit success/failure events</li>
     * </ol>
     *
     * <p>This method does not send emails; any alerting must be derived from
     * persisted audit records only.</p>
     */
    @Override
    public void handleCourseNotification(CourseEvent event, boolean isCron) {
        final long startedAt = System.currentTimeMillis();
        final String correlationId = currentCorrId();

        // Validates required fields and timestamp format before any downstream work occurs.
        eventValidator.validate(event);

        final String courseCode = safe(event.getCourseCodeSingle());
        final String eventType = safe(event.getEventType());
        final long ntucDTId = (event.getNtucSB() != null) ? event.getNtucSB().getNtucDTId() : 0L;

        // Initialized to preserve context in failure auditing even when resolution fails early.
        long companyId = 0L;
        long groupId = 0L;
        long userId = 0L;

        try {
            if (!eventTypePolicy.shouldProcess(eventType, isCron)) {
                AuditEvent rejected = auditEventFactory.rejectedUnsupportedEvent(
                    startedAt,
                    System.currentTimeMillis(),
                    correlationId,
                    courseCode,
                    ntucDTId,
                    isCron,
                    eventType
                );
                auditEventWriter.write(rejected);

                // Surface as caller-visible validation-style error after recording audit evidence.
                throw new IllegalArgumentException("Event type not supported: " + eventType);
            }

            companyId = asLong(paramValues.get(ParameterKeyEnum.CLS_COMPANY_ID));
            groupId = asLong(paramValues.get(ParameterKeyEnum.CLS_GROUP_ID));

            userId = userLocalService.getUserIdByEmailAddress(
                companyId,
                String.valueOf(paramValues.get(ParameterKeyEnum.GLOBAL_USER_EMAIL))
            );

            final long folderId = asLong(paramValues.get(ParameterKeyEnum.CLS_FOLDERID));
            final String templateName = String.valueOf(paramValues.get(ParameterKeyEnum.CLS_TEMPLATE_NAME));

            // Template lookup is mandatory; downstream article processing requires it.
            final DDMTemplate ddmTemplate = ddmTemplateUtil.getTemplateByNameAndGroupId(groupId, templateName);
            if (ddmTemplate == null) {
                AuditEvent missingTemplate = auditEventFactory.ddmTemplateMissing(
                    startedAt,
                    System.currentTimeMillis(),
                    correlationId,
                    groupId,
                    companyId,
                    userId,
                    courseCode,
                    ntucDTId,
                    templateName
                );
                auditEventWriter.write(missingTemplate);

                throw new IllegalStateException("DDM template not found: " + templateName);
            }

            final DDMStructure ddmStructure = ddmStructureLocalService.getDDMStructure(ddmTemplate.getClassPK());
            final Map<String, String> fieldsTypeInfo = ddmStructureUtil.extractFieldDataTypes(ddmStructure);

            final Group group = groupLocalService.getGroup(groupId);
            final String defaultLanguageId = group.getDefaultLanguageId();
            final String[] availableLanguageIds = group.getAvailableLanguageIds();

            // Captures the article-processing configuration bound to this event.
            final CourseArticleConfig articleConfig = new CourseArticleConfig(
                folderId,
                ddmStructure.getStructureKey(),
                ddmTemplate.getTemplateKey(),
                defaultLanguageId,
                eventType
            );

            // Context passed through downstream layers; avoids leaking raw payloads into helpers.
            final CourseEventContext eventCtx = eventContextBuilder.build(
                groupId,
                companyId,
                userId,
                courseCode,
                eventType,
                (event.getChangeFrom() != null) ? event.getChangeFrom() : Collections.<String>emptyList(),
                ntucDTId,
                articleConfig
            );

            AuditEvent started = auditEventFactory.processingStarted(
                startedAt,
                System.currentTimeMillis(),
                correlationId,
                groupId,
                companyId,
                userId,
                courseCode,
                ntucDTId,
                isCron,
                eventType,
                folderId,
                templateName,
                ddmTemplate.getTemplateKey(),
                ddmStructure.getStructureKey(),
                defaultLanguageId,
                Arrays.asList(availableLanguageIds),
                (fieldsTypeInfo == null) ? 0 : fieldsTypeInfo.size()
            );
            auditEventWriter.write(started);

            if (isCron) {
                // Cron path delegates to the notification handler and records a bounded audit outcome.
                notificationHandler.processCron(eventCtx);

                AuditEvent cronOk = auditEventFactory.processingSuccess(
                    startedAt,
                    System.currentTimeMillis(),
                    correlationId,
                    groupId,
                    companyId,
                    userId,
                    courseCode,
                    ntucDTId,
                    AuditStep.EXECUTION
                );
                auditEventWriter.write(cronOk);
                return;
            }

            // Standard (non-cron) path: process and validate returned JournalArticle.
            JournalArticle retArticle = notificationHandler.process(eventCtx);

            if (retArticle == null) {
                PortalException pe = new PortalException(
                    "Processing returned null JournalArticle for courseCode=" + courseCode + ", ntucDTId=" + ntucDTId
                );

                AuditEvent failed = auditEventFactory.processingFailed(
                    startedAt,
                    System.currentTimeMillis(),
                    correlationId,
                    groupId,
                    companyId,
                    userId,
                    courseCode,
                    ntucDTId,
                    AuditStep.JA_PROCESS,
                    AuditCategory.JOURNAL_ARTICLE,
                    AuditErrorCode.JA_RETURNED_NULL,
                    pe
                );
                auditEventWriter.write(failed);

                throw pe;
            }

            AuditEvent ok = auditEventFactory.processingSuccess(
                startedAt,
                System.currentTimeMillis(),
                correlationId,
                groupId,
                companyId,
                userId,
                courseCode,
                ntucDTId,
                AuditStep.JA_PROCESS
            );
            auditEventWriter.write(ok);

        } catch (Exception e) {
            // Final safety net: records a failure audit event using classification helpers,
            // then raises a caller-visible runtime exception without leaking sensitive details.
            AuditEvent failed = auditEventFactory.processingFailed(
                startedAt,
                System.currentTimeMillis(),
                correlationId,
                groupId,
                companyId,
                userId,
                courseCode,
                ntucDTId,
                AuditStep.JA_PROCESS,
                classifyCategory(e),
                classifyErrorCode(e),
                e
            );
            auditEventWriter.write(failed);

            throw new IllegalStateException("Internal portal error :: " + safeMsg(e), e);
        }
    }

    /**
     * Handles a manual retrigger request based on an existing NtucSB entry.
     *
     * <p>This method returns a {@link CourseProcessResult} rather than throwing,
     * allowing UI/admin flows to surface a concise outcome without exposing stack traces.</p>
     *
     * <p>Note: Retrigger flow currently does not emit audit events here; audit responsibilities
     * are expected to be handled in the downstream handler/service layers (or can be added via
     * factory-backed audit events without step proliferation).</p>
     */
    @Override
    public CourseProcessResult handleCourseRetrigger(NtucSB entry) {
        CourseProcessResult result = new CourseProcessResult();

        try {
            long companyId = asLong(paramValues.get(ParameterKeyEnum.CLS_COMPANY_ID));
            long groupId = asLong(paramValues.get(ParameterKeyEnum.CLS_GROUP_ID));

            long userId = userLocalService.getUserIdByEmailAddress(
                companyId,
                String.valueOf(paramValues.get(ParameterKeyEnum.GLOBAL_USER_EMAIL))
            );

            CourseEvent event = fromEntry(entry);
            if (entry != null) {
                event.setChangeFrom(parseChangeFrom(entry.getChangeFrom()));
            }

            long folderId = asLong(paramValues.get(ParameterKeyEnum.CLS_FOLDERID));
            String templateName = String.valueOf(paramValues.get(ParameterKeyEnum.CLS_TEMPLATE_NAME));

            DDMTemplate ddmTemplate = ddmTemplateUtil.getTemplateByNameAndGroupId(groupId, templateName);
            if (ddmTemplate == null) {
                result.setSuccess(false);
                result.setError("DDM template not found: " + templateName);
                return result;
            }

            DDMStructure ddmStructure = ddmStructureLocalService.getDDMStructure(ddmTemplate.getClassPK());

            Group group = groupLocalService.getGroup(groupId);
            String defaultLanguageId = group.getDefaultLanguageId();

            long ntucDTId = (entry != null) ? entry.getNtucDTId() : 0L;

            CourseArticleConfig articleConfig = new CourseArticleConfig(
                folderId,
                ddmStructure.getStructureKey(),
                ddmTemplate.getTemplateKey(),
                defaultLanguageId,
                (event != null) ? safe(event.getEventType()) : ""
            );

            CourseEventContext eventCtx = eventContextBuilder.build(
                groupId,
                companyId,
                userId,
                (event != null) ? safe(event.getCourseCodeSingle()) : "",
                (event != null) ? safe(event.getEventType()) : "",
                (event != null && event.getChangeFrom() != null) ? event.getChangeFrom() : Collections.<String>emptyList(),
                ntucDTId,
                articleConfig
            );

            JournalArticle retArticle = notificationHandler.processRetrigger(eventCtx);
            if (retArticle == null) {
                result.setSuccess(false);
                result.setError("Retrigger returned null JournalArticle");
                return result;
            }

            result.setSuccess(true);
            result.setJournalArticleId(retArticle.getId());
            result.setArticleId(retArticle.getArticleId());
            result.setArticleUuid(retArticle.getUuid());
            result.setVersion(retArticle.getVersion());
            result.setArticleStatus(WorkflowConstants.getStatusLabel(retArticle.getStatus()));
            return result;

        } catch (Exception e) {
            result.setSuccess(false);
            result.setError(safeMsg(e));
            return result;
        }
    }

    /**
     * Processes all fields from an upstream JSON payload using an existing {@link CourseEventContext}.
     *
     * <p>Used by one-time load / recovery style flows where the event context is already known and
     * the payload is processed directly into a JournalArticle via downstream handlers.</p>
     */
    @Override
    public CourseProcessResult processAllFieldsFromJson(String json, CourseEventContext eventCtx, String courseCode) {
        CourseProcessResult result = new CourseProcessResult();

        try {
            CourseArticleConfig articleConfig = (eventCtx != null) ? eventCtx.getArticleConfig() : null;
            if (articleConfig == null) {
                result.setSuccess(false);
                result.setError("CourseArticleConfig is required but missing in CourseEventContext");
                return result;
            }

            JournalArticle article = notificationHandler.triggerOTLPublishedEvent(json, eventCtx);
            if (article == null) {
                result.setSuccess(false);
                result.setError("Returned null JournalArticle");
                return result;
            }

            result.setSuccess(true);
            result.setJournalArticleId(article.getId());
            result.setArticleId(article.getArticleId());
            result.setArticleUuid(article.getUuid());
            result.setVersion(article.getVersion());
            return result;

        } catch (Exception e) {
            result.setSuccess(false);
            result.setError(safeMsg(e));
            return result;
        }
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    /**
     * Builds a {@link CourseEvent} from an existing ServiceBuilder entry.
     * This supports retrigger flows that reconstruct event context from persisted data.
     */
    private static CourseEvent fromEntry(NtucSB entry) {
        if (entry == null) return null;

        CourseEvent e = new CourseEvent();
        e.setCourseCodeSingle(entry.getCourseCode());
        e.setCourseTypeSingle(entry.getCourseType());
        e.setEventType(entry.getEvent());

        if (entry.getNotificationDate() != null) {
            e.setTimestamp(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(entry.getNotificationDate()));
        }

        e.setNtucSB(entry);
        e.setNtucSBId(entry.getNtucDTId());
        return e;
    }

    /**
     * Parses a comma-separated changeFrom value as stored in the database.
     */
    private static List<String> parseChangeFrom(String dbValue) {
        if (dbValue == null || dbValue.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(dbValue.split(","));
    }

    /**
     * Produces a safe error message suitable for UI/admin responses without leaking raw payloads.
     */
    private static String safeMsg(Throwable e) {
        String msg = (e == null) ? null : e.getMessage();
        return (msg == null) ? (e == null ? "Unknown error" : e.getClass().getSimpleName()) : msg;
    }

    /**
     * Null-safe string normalization for downstream context construction.
     */
    private static String safe(String s) {
        return (s == null) ? "" : s;
    }

    /**
     * Converts configured parameter values into a long. Returns 0 when absent.
     */
    private static long asLong(Object o) {
        if (o == null) return 0L;
        return Long.parseLong(String.valueOf(o));
    }

    /**
     * Returns the current correlationId if present in MDC, otherwise generates a new one.
     * CorrelationId is used for audit traceability across layers.
     */
    private String currentCorrId() {
        String c = MdcUtil.getCorrId();
        return (c != null) ? c : java.util.UUID.randomUUID().toString();
    }

    /**
     * Coarse audit category classifier for failure auditing.
     * Fine-grained distinctions should be expressed via audit message/details rather than new steps.
     */
    private static AuditCategory classifyCategory(Exception e) {
        if (e instanceof PortalException) {
            return AuditCategory.JOURNAL_ARTICLE;
        }
        return AuditCategory.DT5_FLOW;
    }

    /**
     * Coarse audit errorCode classifier for failure auditing.
     * This supports consistent reporting and alert policy mapping.
     */
    private static AuditErrorCode classifyErrorCode(Exception e) {
        if (e instanceof IllegalArgumentException) {
            return AuditErrorCode.VALIDATION_FAILED;
        }
        if (e instanceof PortalException) {
            return AuditErrorCode.JA_PROCESSING_FAILED;
        }
        return AuditErrorCode.DT5_UNEXPECTED;
    }
}
