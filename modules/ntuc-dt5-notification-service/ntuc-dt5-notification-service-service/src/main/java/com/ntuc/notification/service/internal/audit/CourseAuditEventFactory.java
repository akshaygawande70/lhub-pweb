package com.ntuc.notification.service.internal.audit;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import java.util.List;

/**
 * Factory for AuditEvent for course processing flow.
 *
 * Note:
 * - We store structured details via AuditEvent.detail(k,v).
 * - JSON serialization is service-layer responsibility (AuditEventDbMapper).
 */
public class CourseAuditEventFactory {

    public AuditEvent rejectedUnsupportedEvent(
        long startMs,
        long endMs,
        String correlationId,
        String courseCode,
        long ntucDTId,
        boolean isCron,
        String eventType) {

        return AuditEvent.builder()
            .startTimeMs(startMs)
            .endTimeMs(endMs)
            .correlationId(correlationId)
            .courseCode(courseCode)
            .ntucDTId(ntucDTId)
            .severity(AuditSeverity.WARNING)
            .status(AuditStatus.SKIPPED)
            .step(AuditStep.VALIDATE_EVENT)
            .category(AuditCategory.DT5_FLOW)
            .message("Unsupported event type; event skipped")
            .detail("isCron", String.valueOf(isCron))
            .detail("eventType", safe(eventType))
            .errorCode(AuditErrorCode.UNSUPPORTED_EVENT_TYPE)
            .errorMessage("Event type not supported: " + safe(eventType))
            .exceptionClass(IllegalArgumentException.class.getName())
            .build();
    }

    public AuditEvent ddmTemplateMissing(
        long startMs,
        long endMs,
        String correlationId,
        long groupId,
        long companyId,
        long userId,
        String courseCode,
        long ntucDTId,
        String templateName) {

        return AuditEvent.builder()
            .startTimeMs(startMs)
            .endTimeMs(endMs)
            .correlationId(correlationId)
            .companyId(companyId)
            .groupId(groupId)
            .userId(userId)
            .courseCode(courseCode)
            .ntucDTId(ntucDTId)
            .severity(AuditSeverity.ERROR)
            .status(AuditStatus.FAILED)
            .step(AuditStep.RESOLVE_TEMPLATE)
            .category(AuditCategory.JOURNAL_ARTICLE)
            .message("DDM template not found")
            .detail("templateName", safe(templateName))
            .errorCode(AuditErrorCode.DDM_TEMPLATE_NOT_FOUND)
            .errorMessage("DDM template not found: " + safe(templateName))
            .exceptionClass(IllegalStateException.class.getName())
            .build();
    }

    public AuditEvent processingStarted(
        long startMs,
        long endMs,
        String correlationId,
        long groupId,
        long companyId,
        long userId,
        String courseCode,
        long ntucDTId,
        boolean isCron,
        String eventType,
        long folderId,
        String templateName,
        String templateKey,
        String structureKey,
        String defaultLanguageId,
        List<String> availableLanguageIds,
        int fieldsTypeInfoSize) {

        return AuditEvent.builder()
            .startTimeMs(startMs)
            .endTimeMs(endMs)
            .correlationId(correlationId)
            .companyId(companyId)
            .groupId(groupId)
            .userId(userId)
            .courseCode(courseCode)
            .ntucDTId(ntucDTId)
            .severity(AuditSeverity.INFO)
            .status(AuditStatus.STARTED)
            .step(AuditStep.BUILD_CONTEXT)
            .category(AuditCategory.DT5_FLOW) // informational; alerting keys off FAILED events
            .message("Course processing started")
            .detail("isCron", String.valueOf(isCron))
            .detail("eventType", safe(eventType))
            .detail("folderId", String.valueOf(folderId))
            .detail("templateName", safe(templateName))
            .detail("templateKey", safe(templateKey))
            .detail("structureKey", safe(structureKey))
            .detail("defaultLanguageId", safe(defaultLanguageId))
            .detail("availableLanguageIdsCount", String.valueOf((availableLanguageIds == null) ? 0 : availableLanguageIds.size()))
            .detail("fieldsTypeInfoSize", String.valueOf(fieldsTypeInfoSize))
            .build();
    }

    public AuditEvent processingSuccess(
        long startMs,
        long endMs,
        String correlationId,
        long groupId,
        long companyId,
        long userId,
        String courseCode,
        long ntucDTId,
        AuditStep step) {

        return AuditEvent.builder()
            .startTimeMs(startMs)
            .endTimeMs(endMs)
            .correlationId(correlationId)
            .companyId(companyId)
            .groupId(groupId)
            .userId(userId)
            .courseCode(courseCode)
            .ntucDTId(ntucDTId)
            .severity(AuditSeverity.INFO)
            .status(AuditStatus.SUCCESS)
            .step(step)
            .category(AuditCategory.DT5_FLOW)
            .message("Course processing succeeded")
            .build();
    }

    public AuditEvent processingFailed(
        long startMs,
        long endMs,
        String correlationId,
        long groupId,
        long companyId,
        long userId,
        String courseCode,
        long ntucDTId,
        AuditStep step,
        AuditCategory category,
        AuditErrorCode errorCode,
        Throwable t) {

        String msg = (t == null || t.getMessage() == null) ? "" : t.getMessage();

        return AuditEvent.builder()
            .startTimeMs(startMs)
            .endTimeMs(endMs)
            .correlationId(correlationId)
            .companyId(companyId)
            .groupId(groupId)
            .userId(userId)
            .courseCode(courseCode)
            .ntucDTId(ntucDTId)
            .severity(AuditSeverity.ERROR)
            .status(AuditStatus.FAILED)
            .step(step)
            .category((category != null) ? category : AuditCategory.DT5_FLOW)
            .message("Course processing failed")
            .detail("exceptionClass", (t == null) ? "" : safe(t.getClass().getName()))
            .detail("message", safe(msg))
            .errorCode((errorCode != null) ? errorCode : AuditErrorCode.DT5_UNEXPECTED)
            .errorMessage(safe(msg))
            .exceptionClass((t == null) ? "" : t.getClass().getName())
            .build();
    }

    private static String safe(String s) {
        return (s == null) ? "" : s.replace("\"", "'");
    }
}
