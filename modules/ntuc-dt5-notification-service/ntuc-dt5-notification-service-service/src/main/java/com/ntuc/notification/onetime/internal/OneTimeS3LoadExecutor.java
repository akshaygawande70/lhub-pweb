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
 * Plain Java executor for a one-time S3 load workflow.
 *
 * <p>
 * Business Purpose:
 * Loads course-related JSON payloads from S3 for controlled migration/recovery runs and
 * invokes the existing notification handler to create/update content consistently.
 *
 * <p>
 * Technical Purpose:
 * Performs parameter validation, resolves DDM configuration, iterates S3 JSON objects, applies
 * eligibility rules, and triggers the appropriate notification flows while writing AuditLog events.
 *
 * <p>
 * Non-negotiable Rules:
 * <ul>
 *   <li>AuditLog DB is the single source of truth.</li>
 *   <li>No secrets in audit details.</li>
 *   <li>No reflection constructor hacks.</li>
 * </ul>
 *
 * @author @akshaygawande
 */
public class OneTimeS3LoadExecutor {

    /**
     * Shared JSON mapper used by S3 iterator parsing.
     * This is intentionally static to avoid repeated initialization overhead per workflow run.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /** Parameter group accessor for runtime configuration and operational toggles. */
    private final ParameterGroupKeys parameterGroupKeys;

    /** Liferay user resolver used to map GLOBAL_USER_EMAIL to a concrete userId. */
    private final UserLocalService userLocalService;

    /** Liferay group resolver used for obtaining language defaults and scoping configuration. */
    private final GroupLocalService groupLocalService;

    /** DDM template resolver used to find the course template by name. */
    private final DDMTemplateUtil ddmTemplateUtil;

    /** DDM structure helper used to extract field type metadata for downstream processing. */
    private final DDMStructureUtil ddmStructureUtil;

    /** Liferay local service used to resolve DDMStructure from a template classPK. */
    private final DDMStructureLocalService ddmStructureLocalService;

    /** Orchestrator that triggers course publish/inactive flows and JA updates. */
    private final NotificationHandler notificationHandler;

    /** Central audit writer that persists events and triggers any post-persist actions. */
    private final AuditEventWriter auditEventWriter;

    /**
     * Creates an executor with all required collaborators injected from the OSGi wrapper.
     *
     * <p>
     * Business Purpose:
     * Ensures the one-time load run uses the same platform services as production flows.
     *
     * <p>
     * Technical Purpose:
     * Captures service references for later execution; this constructor performs no work.
     *
     * @param parameterGroupKeys parameter group accessor
     * @param userLocalService user resolver service
     * @param groupLocalService group resolver service
     * @param ddmTemplateUtil template resolver helper
     * @param ddmStructureUtil structure helper
     * @param ddmStructureLocalService DDMStructure local service
     * @param notificationHandler downstream handler
     * @param auditEventWriter audit writer
     */
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

    /**
     * Executes the one-time S3 load workflow for the provided S3 path.
     *
     * <p>
     * Business Purpose:
     * Runs a controlled import of course JSON objects from S3 and applies the platform’s
     * established publish/inactive behavior for each eligible course record.
     *
     * <p>
     * Technical Purpose:
     * Validates parameters, resolves user/group/DDM configuration, iterates S3 JSON objects,
     * and triggers {@link NotificationHandler} for each eligible payload while producing
     * audit records for traceability.
     *
     * <p>
     * Inputs / Invariants:
     * <ul>
     *   <li>{@code bucket} must be non-blank.</li>
     *   <li>Required parameters must be present: companyId, groupId, folderId, templateName, region.</li>
     *   <li>No secrets are emitted into audit details (AWS keys are never logged).</li>
     * </ul>
     *
     * <p>
     * Side Effects:
     * <ul>
     *   <li>Reads parameter group values.</li>
     *   <li>Reads DDM template/structure metadata.</li>
     *   <li>Iterates S3 objects and triggers publish/inactive flows.</li>
     *   <li>Writes audit events (DB persistence) for validation, execution, and per-item processing.</li>
     * </ul>
     *
     * <p>
     * Audit Behavior:
     * <ul>
     *   <li>ENTRY: workflow start/end summary for the one-time run.</li>
     *   <li>VALIDATION: configuration and input checks (STARTED/SUCCESS/FAILED).</li>
     *   <li>EXECUTION: S3 iteration and orchestration (STARTED/SUCCESS/PARTIAL/FAILED).</li>
     *   <li>JA_PROCESS: used when JournalArticle processing fails or succeeds for an item.</li>
     * </ul>
     *
     * <p>
     * Return Semantics:
     * This method returns void. It may throw {@link PortalException} for fatal platform issues
     * during mandatory resolution (e.g., user resolution failure).
     *
     * @param bucket S3 bucket name
     * @param prefix S3 key prefix scope
     * @throws PortalException when mandatory user resolution fails or platform services throw fatally
     */
    public void execute(String bucket, String prefix) throws PortalException {
        final long flowStart = System.currentTimeMillis();
        final String corrId = UUID.randomUUID().toString();

        boolean validationSucceeded = false;
        AuditStatus finalExecutionStatus = AuditStatus.SUCCESS;
        AuditSeverity finalExecutionSeverity = AuditSeverity.INFO;

        // Using ENTRY as this is the workflow entry point for the one-time run.
        // Specifics: "ONE_TIME_S3_LOAD" kept in message/details (not in AuditStep).
        auditEventWriter.write(AuditEvent.builder()
                .startTimeMs(flowStart)
                .endTimeMs(0) // STARTED => endTimeMs must be 0
                .correlationId(corrId)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.STARTED)
                .step(AuditStep.ENTRY)
                .category(AuditCategory.DT5_FLOW)
                .message("One-time S3 load started")
                .detail("bucket", safe(bucket))
                .detail("prefix", safe(prefix))
                .build());

        if (isBlank(bucket)) {
            // Replaced non-canonical step ONE_TIME_S3_VALIDATE -> VALIDATION (input validation boundary).
            // Specifics: "one-time S3 load" retained in message/details.
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(flowStart)
                    .endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.VALIDATION)
                    .category(AuditCategory.DT5_FLOW)
                    .errorCode(AuditErrorCode.REQ_INVALID_S3_PATH)
                    .message("One-time S3 load aborted: bucket is blank")
                    .detail("prefix", safe(prefix))
                    .build());

            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(flowStart)
                    .endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .severity(AuditSeverity.INFO)
                    .status(AuditStatus.SUCCESS)
                    .step(AuditStep.ENTRY)
                    .category(AuditCategory.DT5_FLOW)
                    .message("One-time S3 load finished")
                    .detail("durationMs", String.valueOf(System.currentTimeMillis() - flowStart))
                    .detail("result", "ABORTED_VALIDATION")
                    .build());
            return;
        }

        Map<ParameterKeyEnum, Object> params = parameterGroupKeys.getAllParameterValues();

        long companyId = asLong(params.get(ParameterKeyEnum.CLS_COMPANY_ID), 0L);
        long groupId = asLong(params.get(ParameterKeyEnum.CLS_GROUP_ID), 0L);
        long folderId = asLong(params.get(ParameterKeyEnum.CLS_FOLDERID), 0L);
        String templateName = asString(params.get(ParameterKeyEnum.CLS_TEMPLATE_NAME), "");
        String regionStr = asString(params.get(ParameterKeyEnum.CLS_AWS_REGION), "");

        // Credentials may be blank -> default provider chain.
        // Secrets are intentionally never written to audit details.
        String accessKey = asString(params.get(ParameterKeyEnum.CLS_AWS_ACCESS_KEY_ID), "");
        String secretKey = asString(params.get(ParameterKeyEnum.CLS_AWS_SECRET_ACCESS_KEY), "");

        final long validateStart = System.currentTimeMillis();

        // Replaced non-canonical step ONE_TIME_S3_VALIDATE -> VALIDATION (validation phase boundary).
        // Specifics: bucket/prefix/region kept as details.
        auditEventWriter.write(AuditEvent.builder()
                .startTimeMs(validateStart)
                .endTimeMs(0) // STARTED => endTimeMs must be 0
                .correlationId(corrId)
                .companyId(companyId)
                .groupId(groupId)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.STARTED)
                .step(AuditStep.VALIDATION)
                .category(AuditCategory.DT5_FLOW)
                .message("One-time S3 load validation started")
                .detail("bucket", bucket.trim())
                .detail("prefix", safe(prefix))
                .detail("region", safe(regionStr))
                .build());

        if (companyId <= 0 || groupId <= 0 || folderId <= 0 || isBlank(templateName) || isBlank(regionStr)) {
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(validateStart)
                    .endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId)
                    .groupId(groupId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.VALIDATION)
                    .category(AuditCategory.DT5_FLOW)
                    .errorCode(AuditErrorCode.VALIDATION_FAILED)
                    .message("One-time S3 load aborted: missing required parameters")
                    .detail("companyId", String.valueOf(companyId))
                    .detail("groupId", String.valueOf(groupId))
                    .detail("folderId", String.valueOf(folderId))
                    .detail("templateName", safe(templateName))
                    .detail("region", safe(regionStr))
                    .build());

            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(flowStart)
                    .endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .severity(AuditSeverity.INFO)
                    .status(AuditStatus.SUCCESS)
                    .step(AuditStep.ENTRY)
                    .category(AuditCategory.DT5_FLOW)
                    .message("One-time S3 load finished")
                    .detail("durationMs", String.valueOf(System.currentTimeMillis() - flowStart))
                    .detail("result", "ABORTED_VALIDATION")
                    .build());
            return;
        }

        long userId = resolveUserId(companyId, params, corrId);

        DDMTemplate ddmTemplate = ddmTemplateUtil.getTemplateByNameAndGroupId(groupId, templateName);

        if (ddmTemplate == null) {
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(validateStart)
                    .endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId)
                    .groupId(groupId)
                    .userId(userId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.VALIDATION)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.DDM_TEMPLATE_NOT_FOUND)
                    .message("One-time S3 load aborted: DDM template not found")
                    .detail("templateName", templateName)
                    .build());

            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(flowStart)
                    .endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .severity(AuditSeverity.INFO)
                    .status(AuditStatus.SUCCESS)
                    .step(AuditStep.ENTRY)
                    .category(AuditCategory.DT5_FLOW)
                    .message("One-time S3 load finished")
                    .detail("durationMs", String.valueOf(System.currentTimeMillis() - flowStart))
                    .detail("result", "ABORTED_VALIDATION")
                    .build());
            return;
        }

        DDMStructure ddmStructure;

        try {
            ddmStructure = ddmStructureLocalService.getDDMStructure(ddmTemplate.getClassPK());
        }
        catch (Exception e) {
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(validateStart)
                    .endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId)
                    .groupId(groupId)
                    .userId(userId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.VALIDATION)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.DDM_STRUCTURE_NOT_FOUND)
                    .message("One-time S3 load aborted: failed to resolve DDM structure")
                    .errorMessage(safeMsg(e))
                    .exceptionClass(e.getClass().getName())
                    .build());

            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(flowStart)
                    .endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .severity(AuditSeverity.INFO)
                    .status(AuditStatus.SUCCESS)
                    .step(AuditStep.ENTRY)
                    .category(AuditCategory.DT5_FLOW)
                    .message("One-time S3 load finished")
                    .detail("durationMs", String.valueOf(System.currentTimeMillis() - flowStart))
                    .detail("result", "ABORTED_VALIDATION")
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
                .startTimeMs(validateStart)
                .endTimeMs(System.currentTimeMillis())
                .correlationId(corrId)
                .companyId(companyId)
                .groupId(groupId)
                .userId(userId)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.SUCCESS)
                .step(AuditStep.VALIDATION)
                .category(AuditCategory.DT5_FLOW)
                .message("One-time S3 load validation succeeded")
                .detail("structureKey", articleConfig.getDdmStructureKey())
                .detail("templateKey", articleConfig.getDdmTemplateKey())
                .build());

        validationSucceeded = true;

        Region region = Region.of(regionStr);
        AwsCredentialsProvider creds = resolveAwsCredentials(accessKey, secretKey);

        OneTimeS3Counters counters = new OneTimeS3Counters();

        final long execStart = System.currentTimeMillis();

        // Replaced non-canonical step ONE_TIME_S3_EXECUTE -> EXECUTION (main orchestration boundary).
        // Specifics: one-time nature and S3 scope retained in message/details.
        auditEventWriter.write(AuditEvent.builder()
                .startTimeMs(execStart)
                .endTimeMs(0) // STARTED => endTimeMs must be 0
                .correlationId(corrId)
                .companyId(companyId)
                .groupId(groupId)
                .userId(userId)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.STARTED)
                .step(AuditStep.EXECUTION)
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

            final boolean hasFailures = (counters.failed > 0 || iter.getFailed() > 0);

            finalExecutionStatus = hasFailures ? AuditStatus.PARTIAL : AuditStatus.SUCCESS;
            finalExecutionSeverity = hasFailures ? AuditSeverity.WARNING : AuditSeverity.INFO;

            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(execStart)
                    .endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId)
                    .groupId(groupId)
                    .userId(userId)
                    .severity(finalExecutionSeverity)
                    .status(finalExecutionStatus)
                    .step(AuditStep.EXECUTION)
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
            finalExecutionStatus = AuditStatus.FAILED;
            finalExecutionSeverity = AuditSeverity.ERROR;

            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(execStart)
                    .endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId)
                    .groupId(groupId)
                    .userId(userId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.EXECUTION)
                    .category(AuditCategory.CLS)
                    .errorCode(AuditErrorCode.ONE_TIME_S3_FAILED)
                    .message("One-time S3 load execution failed")
                    .errorMessage(safeMsg(e))
                    .exceptionClass(e.getClass().getName())
                    .build());
        }
        finally {
            // Using ENTRY as this is the workflow completion summary.
            // Specifics: duration and overall result moved to details.
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(flowStart)
                    .endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId)
                    .groupId(groupId)
                    .userId(userId)
                    .severity(validationSucceeded ? finalExecutionSeverity : AuditSeverity.ERROR)
                    .status(validationSucceeded ? finalExecutionStatus : AuditStatus.FAILED)
                    .step(AuditStep.ENTRY)
                    .category(AuditCategory.DT5_FLOW)
                    .message("One-time S3 load finished")
                    .detail("durationMs", String.valueOf(System.currentTimeMillis() - flowStart))
                    .detail("result", validationSucceeded ? String.valueOf(finalExecutionStatus) : "FAILED_VALIDATION")
                    .build());
        }
    }

    /**
     * Iterates all JSON objects under the S3 path and triggers handler operations per eligible item.
     *
     * <p>
     * Business Purpose:
     * Applies eligibility rules and processes only valid courses for publish/inactive actions.
     *
     * <p>
     * Technical Purpose:
     * Wraps {@link S3JsonIterator} usage and emits per-item audit events for skip/success/failure outcomes.
     *
     * <p>
     * Inputs / Invariants:
     * <ul>
     *   <li>Must not log AWS secrets.</li>
     *   <li>Per-item failures do not stop the entire run; they are captured as audit failures and counters.</li>
     * </ul>
     *
     * <p>
     * Side Effects:
     * <ul>
     *   <li>Reads S3 objects via AWS SDK.</li>
     *   <li>Calls {@link NotificationHandler} to update content.</li>
     *   <li>Writes audit events for item-level outcomes.</li>
     * </ul>
     *
     * <p>
     * Audit Behavior:
     * <ul>
     *   <li>EXECUTION: item skipped / item failed (non-JA failures).</li>
     *   <li>JA_PROCESS: handler returned null / handler success for an item.</li>
     * </ul>
     *
     * <p>
     * Return Semantics:
     * Returns {@link S3IterationResult}. If iteration fails to initialize, returns a result with failed=1.
     *
     * @param bucket S3 bucket
     * @param prefix S3 prefix
     * @param region AWS region
     * @param creds AWS credential provider (may be default chain)
     * @param companyId companyId for audit scoping
     * @param groupId groupId for audit scoping
     * @param userId userId for audit scoping
     * @param fieldsTypeInfo DDM field type map
     * @param articleConfig resolved article configuration
     * @param corrId correlationId for the run
     * @param counters mutable counters to track outcomes
     * @return iteration result summary
     */
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

                        // Replaced non-canonical step ONE_TIME_S3_EXECUTE -> EXECUTION (per-item outcome within execution boundary).
                        // Specifics: ineligible status moved to details.
                        auditEventWriter.write(AuditEvent.builder()
                                .startTimeMs(System.currentTimeMillis())
                                .endTimeMs(System.currentTimeMillis())
                                .correlationId(corrId)
                                .companyId(companyId)
                                .groupId(groupId)
                                .userId(userId)
                                .severity(AuditSeverity.INFO)
                                .status(AuditStatus.SKIPPED)
                                .step(AuditStep.EXECUTION)
                                .category(AuditCategory.DT5_FLOW)
                                .message("One-time S3 item skipped (ineligible status)")
                                .detail("key", key)
                                .detail("status", safe(d.getStatus()))
                                .build());

                        // Return from lambda -> skip this item, continue next.
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

                        // Replaced non-canonical step ONE_TIME_S3_EXECUTE -> JA_PROCESS (JournalArticle processing boundary).
                        // Specifics: S3 object key moved to details.
                        auditEventWriter.write(AuditEvent.builder()
                                .startTimeMs(System.currentTimeMillis())
                                .endTimeMs(System.currentTimeMillis())
                                .correlationId(corrId)
                                .companyId(companyId)
                                .groupId(groupId)
                                .userId(userId)
                                .courseCode(d.getCourseCode())
                                .severity(AuditSeverity.ERROR)
                                .status(AuditStatus.FAILED)
                                .step(AuditStep.JA_PROCESS)
                                .category(AuditCategory.JOURNAL_ARTICLE)
                                .errorCode(AuditErrorCode.JA_PROCESSING_FAILED)
                                .message("One-time S3 item processed but handler returned null")
                                .detail("key", key)
                                .detail("eventType", safe(d.getEventType()))
                                .build());
                    }
                    else {
                        counters.processed++;

                        // Replaced non-canonical step ONE_TIME_S3_EXECUTE -> JA_PROCESS (successful JA processing outcome).
                        // Specifics: key and eventType moved to details.
                        auditEventWriter.write(AuditEvent.builder()
                                .startTimeMs(System.currentTimeMillis())
                                .endTimeMs(System.currentTimeMillis())
                                .correlationId(corrId)
                                .companyId(companyId)
                                .groupId(groupId)
                                .userId(userId)
                                .courseCode(d.getCourseCode())
                                .severity(AuditSeverity.INFO)
                                .status(AuditStatus.SUCCESS)
                                .step(AuditStep.JA_PROCESS)
                                .category(AuditCategory.DT5_FLOW)
                                .message("One-time S3 item processed successfully")
                                .detail("key", key)
                                .detail("eventType", safe(d.getEventType()))
                                .build());
                    }
                }
                catch (Exception ex) {
                    counters.failed++;

                    auditEventWriter.write(AuditEvent.builder()
                            .startTimeMs(System.currentTimeMillis())
                            .endTimeMs(System.currentTimeMillis())
                            .correlationId(corrId)
                            .companyId(companyId)
                            .groupId(groupId)
                            .userId(userId)
                            .severity(AuditSeverity.ERROR)
                            .status(AuditStatus.FAILED)
                            .step(AuditStep.EXECUTION)
                            .category(AuditCategory.DT5_FLOW)
                            .errorCode(AuditErrorCode.ONE_TIME_S3_FAILED)
                            .message("One-time S3 item processing failed")
                            .errorMessage(safeMsg(ex))
                            .exceptionClass(ex.getClass().getName())
                            .detail("key", key)
                            .build());
                }
            });
        }
        catch (Exception ex) {
            // Iterator initialization / traversal failure: treat as execution boundary failure.
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(System.currentTimeMillis())
                    .endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId)
                    .groupId(groupId)
                    .userId(userId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.EXECUTION)
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

    /**
     * Simple counter holder used to summarize eligibility and processing outcomes.
     *
     * <p>
     * Business Purpose:
     * Provides operational counts used for audit summaries and reporting.
     *
     * <p>
     * Technical Purpose:
     * Mutable struct updated during iteration; scoped to a single workflow execution.
     */
    static final class OneTimeS3Counters {
        int eligible;
        int processed;
        int skipped;
        int failed;
    }

    /**
     * Resolves the userId to execute the workflow under, using GLOBAL_USER_EMAIL.
     *
     * <p>
     * Business Purpose:
     * Ensures all content updates are attributable to a consistent platform identity.
     *
     * <p>
     * Technical Purpose:
     * Reads GLOBAL_USER_EMAIL from parameters and resolves it via {@link UserLocalService}.
     *
     * <p>
     * Inputs / Invariants:
     * <ul>
     *   <li>GLOBAL_USER_EMAIL must be present and non-blank.</li>
     * </ul>
     *
     * <p>
     * Side Effects:
     * <ul>
     *   <li>Calls Liferay user resolution service.</li>
     *   <li>Writes validation audit events on failure.</li>
     * </ul>
     *
     * <p>
     * Audit Behavior:
     * Uses VALIDATION as this is configuration validation and identity resolution for the run.
     *
     * @param companyId companyId for user resolution
     * @param params parameter map
     * @param corrId correlationId for audit linking
     * @return resolved userId
     * @throws PortalException when GLOBAL_USER_EMAIL is missing or user resolution fails
     */
    private long resolveUserId(long companyId, Map<ParameterKeyEnum, Object> params, String corrId) throws PortalException {
        String userEmail = asString(params.get(ParameterKeyEnum.GLOBAL_USER_EMAIL), "");

        if (isBlank(userEmail)) {
            auditEventWriter.write(AuditEvent.builder()
                    .startTimeMs(System.currentTimeMillis())
                    .endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.VALIDATION)
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
                    .startTimeMs(System.currentTimeMillis())
                    .endTimeMs(System.currentTimeMillis())
                    .correlationId(corrId)
                    .companyId(companyId)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.VALIDATION)
                    .category(AuditCategory.DT5_FLOW)
                    .errorCode(AuditErrorCode.VALIDATION_FAILED)
                    .message("One-time S3 load aborted: failed to resolve user by GLOBAL_USER_EMAIL")
                    .errorMessage(safeMsg(e))
                    .exceptionClass(e.getClass().getName())
                    .build());

            throw new PortalException(e);
        }
    }

    /**
     * Resolves AWS credentials provider without persisting or logging secrets.
     *
     * <p>
     * Business Purpose:
     * Allows secure access to S3 using either explicit keys (for controlled runs) or the
     * AWS default credential chain (for production-like environments).
     *
     * <p>
     * Technical Purpose:
     * Returns a static provider when both keys are provided, otherwise the default chain.
     *
     * @param accessKey access key id (never logged)
     * @param secretKey secret access key (never logged)
     * @return AWS credentials provider
     */
    private static AwsCredentialsProvider resolveAwsCredentials(String accessKey, String secretKey) {
        if (!isBlank(accessKey) && !isBlank(secretKey)) {
            return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
        }

        return DefaultCredentialsProvider.create();
    }

    /** Null/blank guard used for user-provided and parameter-provided strings. */
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Produces a safe, trimmed string for audit details.
     * This must not be used for secrets; callers must ensure the content is non-sensitive.
     */
    private static String safe(String s) {
        return (s == null) ? "" : s.trim();
    }

    /**
     * Converts an object to long with defensive parsing.
     *
     * <p>
     * Return Semantics:
     * Returns {@code def} when value is null, blank, or not a valid long.
     */
    private static long asLong(Object o, long def) {
        try {
            if (o == null) {
                return def;
            }

            String s = String.valueOf(o).trim();

            if (s.isEmpty()) {
                return def;
            }

            return Long.parseLong(s);
        }
        catch (Exception ignore) {
            return def;
        }
    }

    /**
     * Converts an object to string with trimming; returns default when null.
     *
     * <p>
     * Return Semantics:
     * Returns {@code def} when value is null, otherwise trimmed {@link String#valueOf(Object)}.
     */
    private static String asString(Object o, String def) {
        if (o == null) {
            return def;
        }

        String s = String.valueOf(o);

        return (s == null) ? def : s.trim();
    }

    /**
     * Produces a non-empty, non-sensitive error message for audit usage.
     *
     * <p>
     * Return Semantics:
     * Returns exception simple class name when message is null/blank.
     */
    private static String safeMsg(Exception e) {
        String m = e.getMessage();

        return (m == null || m.trim().isEmpty()) ? e.getClass().getSimpleName() : m;
    }

    /**
     * Legacy helper retained for operational debugging only (currently unused).
     *
     * <p>
     * Note:
     * Kept to preserve behavior parity with earlier operational scripts without exposing it publicly.
     */
    @SuppressWarnings("unused")
    private static String nowSg() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }
}
