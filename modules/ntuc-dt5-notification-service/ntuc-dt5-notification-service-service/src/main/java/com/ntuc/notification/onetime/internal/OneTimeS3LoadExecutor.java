package com.ntuc.notification.onetime.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.constants.NotificationType;
import com.ntuc.notification.constants.ParameterGroupKeys;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.onetime.internal.eligibility.EligibilityDecider;
import com.ntuc.notification.onetime.internal.eligibility.EligibilityDecision;
import com.ntuc.notification.onetime.internal.s3.S3IterationResult;
import com.ntuc.notification.onetime.internal.s3.S3JsonIterator;
import com.ntuc.notification.service.NotificationHandler;
import com.ntuc.notification.util.DDMStructureUtil;
import com.ntuc.notification.util.DDMTemplateUtil;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

/**
 * Plain Java executor for OneTime S3 load.
 *
 * Non-negotiable:
 * - AuditLog DB is the single source of truth.
 * - No secrets in audit details.
 * - No reflection constructor hacks.
 */
public class OneTimeS3LoadExecutor {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final ParameterGroupKeys parameterGroupKeys;
    private final UserLocalService userLocalService;
    private final GroupLocalService groupLocalService;
    private final DDMTemplateUtil ddmTemplateUtil;
    private final DDMStructureUtil ddmStructureUtil;
    private final DDMStructureLocalService ddmStructureLocalService;
    private final NotificationHandler notificationHandler;
    private final AuditEventWriter auditEventWriter;

    public OneTimeS3LoadExecutor(
            ParameterGroupKeys parameterGroupKeys,
            UserLocalService userLocalService,
            GroupLocalService groupLocalService,
            DDMTemplateUtil ddmTemplateUtil,
            DDMStructureUtil ddmStructureUtil,
            DDMStructureLocalService ddmStructureLocalService,
            NotificationHandler notificationHandler,
            AuditEventWriter auditEventWriter) {

        this.parameterGroupKeys = parameterGroupKeys;
        this.userLocalService = userLocalService;
        this.groupLocalService = groupLocalService;
        this.ddmTemplateUtil = ddmTemplateUtil;
        this.ddmStructureUtil = ddmStructureUtil;
        this.ddmStructureLocalService = ddmStructureLocalService;
        this.notificationHandler = notificationHandler;
        this.auditEventWriter = auditEventWriter;
    }

    public void execute(String bucket, String prefix) throws PortalException {
        final long flowStart = System.currentTimeMillis();
        final String corrId = UUID.randomUUID().toString();

        if (isBlank(bucket)) {
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(flowStart).endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.ONE_TIME_S3_VALIDATE)
                    .category(AuditCategory.DT5_FLOW)
                    .errorCode(AuditErrorCode.REQ_INVALID_S3_PATH)
                    .message("One-time S3 load aborted: bucket is blank")
                    .detail("prefix", safe(prefix))
                    .build());
            return;
        }

        Map<ParameterKeyEnum, Object> params = parameterGroupKeys.getAllParameterValues();

        long companyId = asLong(params.get(ParameterKeyEnum.CLS_COMPANY_ID), 0L);
        long groupId = asLong(params.get(ParameterKeyEnum.CLS_GROUP_ID), 0L);
        long folderId = asLong(params.get(ParameterKeyEnum.CLS_FOLDERID), 0L);
        String templateName = asString(params.get(ParameterKeyEnum.CLS_TEMPLATE_NAME), "");

        String regionStr = asString(params.get(ParameterKeyEnum.CLS_AWS_REGION), "");

        // credentials may be blank -> default provider chain
        String accessKey = asString(params.get(ParameterKeyEnum.CLS_AWS_ACCESS_KEY_ID), "");
        String secretKey = asString(params.get(ParameterKeyEnum.CLS_AWS_SECRET_ACCESS_KEY), "");

        long validateStart = System.currentTimeMillis();
        auditEventWriter.write(AuditEvent.builder()
                .startTimeMs(validateStart).endTimeMs(validateStart)
                .correlationId(corrId)
                .companyId(companyId).groupId(groupId)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.STARTED)
                .step(AuditStep.ONE_TIME_S3_VALIDATE)
                .category(AuditCategory.DT5_FLOW)
                .message("One-time S3 load validation started")
                .detail("bucket", bucket.trim())
                .detail("prefix", safe(prefix))
                .detail("region", safe(regionStr))
                .build());

        if (companyId <= 0 || groupId <= 0 || folderId <= 0 || isBlank(templateName) || isBlank(regionStr)) {
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(validateStart).endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId).groupId(groupId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.ONE_TIME_S3_VALIDATE)
                    .category(AuditCategory.DT5_FLOW)
                    .errorCode(AuditErrorCode.VALIDATION_FAILED)
                    .message("One-time S3 load aborted: missing required parameters")
                    .detail("companyId", String.valueOf(companyId))
                    .detail("groupId", String.valueOf(groupId))
                    .detail("folderId", String.valueOf(folderId))
                    .detail("templateName", safe(templateName))
                    .detail("region", safe(regionStr))
                    .build());
            return;
        }

        long userId = resolveUserId(companyId, params, corrId);

        DDMTemplate ddmTemplate = ddmTemplateUtil.getTemplateByNameAndGroupId(groupId, templateName);
        if (ddmTemplate == null) {
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(validateStart).endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId).groupId(groupId).userId(userId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.ONE_TIME_S3_VALIDATE)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.DDM_TEMPLATE_NOT_FOUND)
                    .message("One-time S3 load aborted: DDM template not found")
                    .detail("templateName", templateName)
                    .build());
            return;
        }

        DDMStructure ddmStructure;
        try {
            ddmStructure = ddmStructureLocalService.getDDMStructure(ddmTemplate.getClassPK());
        }
        catch (Exception e) {
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(validateStart).endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId).groupId(groupId).userId(userId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.ONE_TIME_S3_VALIDATE)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.DDM_STRUCTURE_NOT_FOUND)
                    .message("One-time S3 load aborted: failed to resolve DDM structure")
                    .errorMessage(safeMsg(e))
                    .exceptionClass(e.getClass().getName())
                    .build());
            return;
        }

        Group group = groupLocalService.fetchGroup(groupId);
        String defaultLanguageId = (group != null && group.getDefaultLanguageId() != null)
                ? group.getDefaultLanguageId()
                : "en_US";

        CourseArticleConfig articleConfig = new CourseArticleConfig(
                folderId,
                ddmStructure.getStructureKey(),
                ddmTemplate.getTemplateKey(),
                defaultLanguageId,
                "ONE_TIME_S3_LOAD");

        auditEventWriter.write(AuditEvent.builder()
                .startTimeMs(validateStart).endTimeMs(System.currentTimeMillis())
                .correlationId(corrId)
                .companyId(companyId).groupId(groupId).userId(userId)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.SUCCESS)
                .step(AuditStep.ONE_TIME_S3_VALIDATE)
                .category(AuditCategory.DT5_FLOW)
                .message("One-time S3 load validation succeeded")
                .detail("structureKey", articleConfig.getDdmStructureKey())
                .detail("templateKey", articleConfig.getDdmTemplateKey())
                .build());

        Region region = Region.of(regionStr);
        AwsCredentialsProvider creds = resolveAwsCredentials(accessKey, secretKey);

        OneTimeS3Counters counters = new OneTimeS3Counters();

        long execStart = System.currentTimeMillis();
        auditEventWriter.write(AuditEvent.builder()
                .startTimeMs(execStart).endTimeMs(execStart)
                .correlationId(corrId)
                .companyId(companyId).groupId(groupId).userId(userId)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.STARTED)
                .step(AuditStep.ONE_TIME_S3_EXECUTE)
                .category(AuditCategory.DT5_FLOW)
                .message("One-time S3 load execution started")
                .detail("bucket", bucket.trim())
                .detail("prefix", safe(prefix))
                .build());

        try {
            Map<String, String> fieldsTypeInfo = ddmStructureUtil.extractFieldDataTypes(ddmStructure);

            S3IterationResult iter = processAll(
                    bucket.trim(),
                    prefix,
                    region,
                    creds,
                    companyId,
                    groupId,
                    userId,
                    fieldsTypeInfo,
                    articleConfig,
                    corrId,
                    counters);

            AuditStatus status = (counters.failed > 0 || iter.getFailed() > 0) ? AuditStatus.PARTIAL : AuditStatus.SUCCESS;
            AuditSeverity severity = (counters.failed > 0 || iter.getFailed() > 0) ? AuditSeverity.WARNING : AuditSeverity.INFO;

            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(execStart).endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId).groupId(groupId).userId(userId)
                    .severity(severity)
                    .status(status)
                    .step(AuditStep.ONE_TIME_S3_EXECUTE)
                    .category(AuditCategory.DT5_FLOW)
                    .message("One-time S3 load execution finished")
                    .detail("filesSeen", String.valueOf(iter.getFilesSeen()))
                    .detail("jsonProcessed", String.valueOf(iter.getJsonProcessed()))
                    .detail("iteratorFailed", String.valueOf(iter.getFailed()))
                    .detail("eligible", String.valueOf(counters.eligible))
                    .detail("processed", String.valueOf(counters.processed))
                    .detail("skipped", String.valueOf(counters.skipped))
                    .detail("failed", String.valueOf(counters.failed))
                    .build());
        }
        catch (Exception e) {
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(execStart).endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId).groupId(groupId).userId(userId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.ONE_TIME_S3_EXECUTE)
                    .category(AuditCategory.CLS)
                    .errorCode(AuditErrorCode.ONE_TIME_S3_FAILED)
                    .message("One-time S3 load execution failed")
                    .errorMessage(safeMsg(e))
                    .exceptionClass(e.getClass().getName())
                    .build());
        }
        finally {
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(flowStart).endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId).groupId(groupId).userId(userId)
                    .severity(AuditSeverity.INFO)
                    .status(AuditStatus.SUCCESS)
                    .step(AuditStep.ONE_TIME_S3_EXECUTE)
                    .category(AuditCategory.DT5_FLOW)
                    .message("One-time S3 load total duration")
                    .detail("durationMs", String.valueOf(System.currentTimeMillis() - flowStart))
                    .build());
        }
    }

    // Visible for unit tests
    S3IterationResult processAll(
            String bucket,
            String prefix,
            Region region,
            AwsCredentialsProvider creds,
            long companyId,
            long groupId,
            long userId,
            Map<String, String> fieldsTypeInfo,
            CourseArticleConfig articleConfig,
            String corrId,
            OneTimeS3Counters counters) {

        try (S3JsonIterator it = new S3JsonIterator(region, creds, MAPPER)) {

            return it.forEachJson(bucket, prefix, (key, node) -> {
                try {
                    EligibilityDecision d = EligibilityDecider.decide(node);

                    if (!d.isEligible()) {
                        counters.skipped++;
                        auditEventWriter.write(AuditEvent.builder()
                                .startTimeMs(System.currentTimeMillis()).endTimeMs(System.currentTimeMillis())
                                .correlationId(corrId)
                                .companyId(companyId).groupId(groupId).userId(userId)
                                .severity(AuditSeverity.INFO)
                                .status(AuditStatus.SKIPPED)
                                .step(AuditStep.ONE_TIME_S3_EXECUTE)
                                .category(AuditCategory.DT5_FLOW)
                                .message("One-time S3 file skipped (ineligible status)")
                                .detail("key", key)
                                .detail("status", safe(d.getStatus()))
                                .build());
                        // Return from lambda -> skip this item, continue next
                        return;
                    }

                    counters.eligible++;

                    CourseEventContext eventCtx = new CourseEventContext(
                            groupId,
                            companyId,
                            userId,
                            d.getCourseCode(),
                            d.getEventType(),
                            Collections.<String>emptyList(),
                            0L,
                            articleConfig
                    );

                    JournalArticle ret;
                    if (NotificationType.PUBLISHED.equals(d.getEventType())) {
                        ret = notificationHandler.triggerOTLPublishedEvent(
                                node.toString(),
                                eventCtx
                        );
                    }
                    else {
                        ret = notificationHandler.triggerOTLInactiveEvent(eventCtx, WorkflowConstants.STATUS_EXPIRED);
                    }

                    if (ret == null) {
                        counters.failed++;
                        auditEventWriter.write(AuditEvent.builder()
                                .startTimeMs(System.currentTimeMillis()).endTimeMs(System.currentTimeMillis())
                                .correlationId(corrId)
                                .companyId(companyId).groupId(groupId).userId(userId)
                                .courseCode(d.getCourseCode())
                                .severity(AuditSeverity.ERROR)
                                .status(AuditStatus.FAILED)
                                .step(AuditStep.ONE_TIME_S3_EXECUTE)
                                .category(AuditCategory.JOURNAL_ARTICLE)
                                .errorCode(AuditErrorCode.JA_PROCESSING_FAILED)
                                .message("One-time S3 file processed but handler returned null")
                                .detail("key", key)
                                .build());
                    }
                    else {
                        counters.processed++;
                        auditEventWriter.write(AuditEvent.builder()
                                .startTimeMs(System.currentTimeMillis()).endTimeMs(System.currentTimeMillis())
                                .correlationId(corrId)
                                .companyId(companyId).groupId(groupId).userId(userId)
                                .courseCode(d.getCourseCode())
                                .severity(AuditSeverity.INFO)
                                .status(AuditStatus.SUCCESS)
                                .step(AuditStep.ONE_TIME_S3_EXECUTE)
                                .category(AuditCategory.DT5_FLOW)
                                .message("One-time S3 file processed successfully")
                                .detail("key", key)
                                .detail("eventType", d.getEventType())
                                .build());
                    }
                }
                catch (Exception ex) {
                    counters.failed++;
                    auditEventWriter.write(AuditEvent.builder()
                            .startTimeMs(System.currentTimeMillis()).endTimeMs(System.currentTimeMillis())
                            .correlationId(corrId)
                            .companyId(companyId).groupId(groupId).userId(userId)
                            .severity(AuditSeverity.ERROR)
                            .status(AuditStatus.FAILED)
                            .step(AuditStep.ONE_TIME_S3_EXECUTE)
                            .category(AuditCategory.DT5_FLOW)
                            .errorCode(AuditErrorCode.ONE_TIME_S3_FAILED)
                            .message("One-time S3 file processing failed")
                            .errorMessage(safeMsg(ex))
                            .exceptionClass(ex.getClass().getName())
                            .detail("key", key)
                            .build());
                }
            });
        }
        catch (Exception ex) {
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(System.currentTimeMillis()).endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId).groupId(groupId).userId(userId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.ONE_TIME_S3_EXECUTE)
                    .category(AuditCategory.CLS)
                    .errorCode(AuditErrorCode.ONE_TIME_S3_FAILED)
                    .message("One-time S3 iteration failed")
                    .errorMessage(safeMsg(ex))
                    .exceptionClass(ex.getClass().getName())
                    .build());

            return new S3IterationResult(0, 0, 1);
        }
    }

    // ------------------------- helpers & testables -------------------------

    static final class OneTimeS3Counters {
        int eligible;
        int processed;
        int skipped;
        int failed;
    }

    private long resolveUserId(long companyId, Map<ParameterKeyEnum, Object> params, String corrId) throws PortalException {
        String userEmail = asString(params.get(ParameterKeyEnum.GLOBAL_USER_EMAIL), "");
        if (isBlank(userEmail)) {
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(System.currentTimeMillis()).endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.ONE_TIME_S3_VALIDATE)
                    .category(AuditCategory.DT5_FLOW)
                    .errorCode(AuditErrorCode.VALIDATION_FAILED)
                    .message("One-time S3 load aborted: GLOBAL_USER_EMAIL is blank")
                    .build());
            throw new PortalException("GLOBAL_USER_EMAIL is blank");
        }

        try {
            return userLocalService.getUserIdByEmailAddress(companyId, userEmail);
        }
        catch (Exception e) {
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(System.currentTimeMillis()).endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.ONE_TIME_S3_VALIDATE)
                    .category(AuditCategory.DT5_FLOW)
                    .errorCode(AuditErrorCode.VALIDATION_FAILED)
                    .message("One-time S3 load aborted: failed to resolve user by GLOBAL_USER_EMAIL")
                    .errorMessage(safeMsg(e))
                    .exceptionClass(e.getClass().getName())
                    .build());
            throw new PortalException(e);
        }
    }

    private static AwsCredentialsProvider resolveAwsCredentials(String accessKey, String secretKey) {
        if (!isBlank(accessKey) && !isBlank(secretKey)) {
            return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
        }
        return DefaultCredentialsProvider.create();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String safe(String s) {
        return (s == null) ? "" : s.trim();
    }

    private static long asLong(Object o, long def) {
        try {
            if (o == null) return def;
            String s = String.valueOf(o).trim();
            if (s.isEmpty()) return def;
            return Long.parseLong(s);
        }
        catch (Exception ignore) {
            return def;
        }
    }

    private static String asString(Object o, String def) {
        if (o == null) return def;
        String s = String.valueOf(o);
        return (s == null) ? def : s.trim();
    }

    private static String safeMsg(Exception e) {
        String m = e.getMessage();
        return (m == null || m.trim().isEmpty()) ? e.getClass().getSimpleName() : m;
    }

    @SuppressWarnings("unused")
    private static String nowSg() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }
}
