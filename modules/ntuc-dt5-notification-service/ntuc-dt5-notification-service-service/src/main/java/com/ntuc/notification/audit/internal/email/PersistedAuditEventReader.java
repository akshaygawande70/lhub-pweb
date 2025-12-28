package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.model.AuditLog;
import com.ntuc.notification.service.AuditLogLocalService;

import java.sql.Blob;
import java.util.Collections;
import java.util.Map;

/**
 * Loads a persisted AuditLog row and converts it into an AuditEvent view for alert policy evaluation.
 *
 * Rules:
 * - DB is the source of truth. Trigger must evaluate based on persisted values.
 * - Must never throw.
 */
public class PersistedAuditEventReader {

    private final AuditLogLocalService auditLogLocalService;
    private final AuditLogBlobReader blobReader;
    private final AuditDetailsJsonParser detailsJsonParser;

    public PersistedAuditEventReader(
            AuditLogLocalService auditLogLocalService,
            AuditLogBlobReader blobReader,
            AuditDetailsJsonParser detailsJsonParser) {

        this.auditLogLocalService = auditLogLocalService;
        this.blobReader = blobReader;
        this.detailsJsonParser = detailsJsonParser;
    }

    public PersistedAuditEventReader(AuditLogLocalService auditLogLocalService) {
        this(auditLogLocalService, new AuditLogBlobReader(), new AuditDetailsJsonParser());
    }

    public AuditEvent read(long auditLogId) {
        if (auditLogId <= 0) {
            return null;
        }

        try {
            AuditLog row = auditLogLocalService.fetchAuditLog(auditLogId);
            if (row == null) {
                return null;
            }

            String severityStr = row.getSeverity();
            String statusStr = row.getStatus();
            String stepStr = row.getStep();
            String categoryStr = row.getCategory();
            String errorCodeStr = row.getErrorCode();

            Blob detailsBlob = row.getDetailsJson();
            Blob stackBlob = row.getStackTraceTruncated();

            String detailsJson = blobReader.readUtf8(detailsBlob);
            Map<String, String> details = detailsJsonParser.parse(detailsJson);

            String stackTruncated = blobReader.readUtf8(stackBlob, 64 * 1024);

            AuditEvent.Builder b = AuditEvent.builder()
                    .startTimeMs(row.getStartTimeMs())
                    .endTimeMs(row.getEndTimeMs())
                    .correlationId(row.getCorrelationId())
                    .jobRunId(row.getJobRunId())
                    .eventId(row.getEventId())
                    .requestId(row.getRequestId())
                    .companyId(row.getCompanyId())
                    .groupId(row.getGroupId())
                    .userId(row.getUserId())
                    .courseCode(row.getCourseCode())
                    .ntucDTId(row.getNtucDTId())
                    .severity(enumOr(severityStr, AuditSeverity.class, AuditSeverity.INFO))
                    .status(enumOr(statusStr, AuditStatus.class, AuditStatus.SUCCESS))
                    .step(enumOr(stepStr, AuditStep.class, AuditStep.VALIDATION))
                    .category(enumOr(categoryStr, AuditCategory.class, AuditCategory.DT5_FLOW))
                    .message(row.getMessage())
                    .errorCode(enumOr(errorCodeStr, AuditErrorCode.class, AuditErrorCode.NONE))
                    .errorMessage(row.getErrorMessage())
                    .exceptionClass(row.getExceptionClass())
                    .stackTraceHash(blankToNull(row.getStackTraceHash()))
                    .stackTraceTruncated(blankToNull(stackTruncated));

            b.details(details == null ? Collections.<String, String>emptyMap() : details);

            return b.build();
        }
        catch (Throwable ignore) {
            return null;
        }
    }

    private static String blankToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static <E extends Enum<E>> E enumOr(String s, Class<E> type, E fallback) {
        if (s == null) {
            return fallback;
        }
        String t = s.trim();
        if (t.isEmpty()) {
            return fallback;
        }
        try {
            return Enum.valueOf(type, t);
        }
        catch (Throwable ignore) {
            return fallback;
        }
    }
}
