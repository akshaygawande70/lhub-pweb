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
 * OSGi orchestrator for course event processing.
 *
 * Non-negotiables enforced:
 * - Audit is the single source of truth for Ops.
 * - Audit is written only via AuditEventWriter.
 * - No email is sent from here. Email alerting is audit-driven only.
 * - All logic is extracted into JUnit-testable helpers.
 */
@Component(service = ClsCourseFieldsProcessor.class)
public class ClsCourseFieldsProcessorImpl implements ClsCourseFieldsProcessor {

    @Reference private NotificationHandler notificationHandler;
    @Reference private ScheduleHandler scheduleHandler;

    @Reference private UserLocalService userLocalService;
    @Reference private GroupLocalService groupLocalService;
    @Reference private DDMStructureLocalService ddmStructureLocalService;

    @Reference private ParameterGroupKeys parameterGroupKeys;
    @Reference private DDMTemplateUtil ddmTemplateUtil;
    @Reference private DDMStructureUtil ddmStructureUtil;

    @Reference private AuditEventWriter auditEventWriter;

    // Pure Java helpers (unit-testable)
    private final CourseEventValidator eventValidator = new CourseEventValidator("dd/MM/yyyy HH:mm:ss");
    private final EventTypePolicy eventTypePolicy = new EventTypePolicy();
    private final CourseEventContextBuilder eventContextBuilder = new CourseEventContextBuilder();
    private final CourseAuditEventFactory auditEventFactory = new CourseAuditEventFactory();

    private Map<ParameterKeyEnum, Object> paramValues;

    @Activate
    protected void activate() {
        paramValues = parameterGroupKeys.getAllParameterValues();
    }

    @Override
    public void handleCourseNotification(CourseEvent event, boolean isCron) {
        final long startedAt = System.currentTimeMillis();
        final String correlationId = currentCorrId();

        eventValidator.validate(event);

        final String courseCode = safe(event.getCourseCodeSingle());
        final String eventType = safe(event.getEventType());
        final long ntucDTId = (event.getNtucSB() != null) ? event.getNtucSB().getNtucDTId() : 0L;

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

            final CourseArticleConfig articleConfig = new CourseArticleConfig(
                folderId,
                ddmStructure.getStructureKey(),
                ddmTemplate.getTemplateKey(),
                defaultLanguageId,
                eventType
            );

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
                    AuditStep.CRON_PROCESS
                );
                auditEventWriter.write(cronOk);
                return;
            }

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
                    AuditStep.PROCESS_JOURNAL_ARTICLE,
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
                AuditStep.PROCESS_JOURNAL_ARTICLE
            );
            auditEventWriter.write(ok);

        } catch (Exception e) {
            AuditEvent failed = auditEventFactory.processingFailed(
                startedAt,
                System.currentTimeMillis(),
                correlationId,
                groupId,
                companyId,
                userId,
                courseCode,
                ntucDTId,
                AuditStep.PROCESS_JOURNAL_ARTICLE,
                classifyCategory(e),
                classifyErrorCode(e),
                e
            );
            auditEventWriter.write(failed);

            throw new IllegalStateException("Internal portal error :: " + safeMsg(e), e);
        }
    }

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

    private static List<String> parseChangeFrom(String dbValue) {
        if (dbValue == null || dbValue.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(dbValue.split(","));
    }

    private static String safeMsg(Throwable e) {
        String msg = (e == null) ? null : e.getMessage();
        return (msg == null) ? (e == null ? "Unknown error" : e.getClass().getSimpleName()) : msg;
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }

    private static long asLong(Object o) {
        if (o == null) return 0L;
        return Long.parseLong(String.valueOf(o));
    }

    private String currentCorrId() {
        String c = MdcUtil.getCorrId();
        return (c != null) ? c : java.util.UUID.randomUUID().toString();
    }

    private static AuditCategory classifyCategory(Exception e) {
        if (e instanceof PortalException) {
            return AuditCategory.JOURNAL_ARTICLE;
        }
        return AuditCategory.DT5_FLOW;
    }

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
