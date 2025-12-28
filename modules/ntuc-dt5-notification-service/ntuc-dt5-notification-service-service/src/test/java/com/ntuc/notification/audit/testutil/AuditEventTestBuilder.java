package com.ntuc.notification.audit.testutil;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Test-only builder for creating real immutable AuditEvent instances.
 *
 * Rules:
 * - No Mockito for AuditEvent
 * - No reflection hacks
 * - Uses realistic defaults
 * - Produces deterministic events (avoid random UUIDs in default values)
 */
public final class AuditEventTestBuilder {

    private AuditCategory category = AuditCategory.DT5_FLOW;
    private AuditSeverity severity = AuditSeverity.ERROR;
    private AuditStatus status = AuditStatus.FAILED;
    private AuditStep step = AuditStep.VALIDATION;

    private String message = "Test failure";
    private AuditErrorCode errorCode = AuditErrorCode.NONE;
    private String errorMessage = "";
    private String exceptionClass = "java.lang.RuntimeException";

    /**
     * IMPORTANT:
     * AuditEvent enforces correlationId is non-null/non-blank by "safe()" defaulting to "".
     * But empty correlationId is not useful for test fingerprints. Provide a stable default.
     */
    private String correlationId = "corr-test";
    private String jobRunId = "";
    private String requestId = "";
    private String eventId = "";

    private long companyId = 0L;
    private long groupId = 0L;
    private long userId = 0L;

    private String courseCode = "";
    private long ntucDTId = 0L;

    private long startTimeMs = 1000L;
    private long endTimeMs = 1010L;

    private String stackTraceHash = null;
    private String stackTraceTruncated = null;

    private final Map<String, String> details = new LinkedHashMap<>();

    public static AuditEventTestBuilder create() {
        return new AuditEventTestBuilder();
    }

    public AuditEventTestBuilder category(AuditCategory v) {
        this.category = v;
        return this;
    }

    public AuditEventTestBuilder severity(AuditSeverity v) {
        this.severity = v;
        return this;
    }

    public AuditEventTestBuilder status(AuditStatus v) {
        this.status = v;
        return this;
    }

    public AuditEventTestBuilder step(AuditStep v) {
        this.step = v;
        return this;
    }

    public AuditEventTestBuilder message(String v) {
        this.message = v;
        return this;
    }

    public AuditEventTestBuilder errorCode(AuditErrorCode v) {
        this.errorCode = (v == null) ? AuditErrorCode.NONE : v;
        return this;
    }

    public AuditEventTestBuilder errorMessage(String v) {
        this.errorMessage = v;
        return this;
    }

    public AuditEventTestBuilder exceptionClass(String v) {
        this.exceptionClass = v;
        return this;
    }

    public AuditEventTestBuilder correlationId(String v) {
        this.correlationId = v;
        return this;
    }

    public AuditEventTestBuilder jobRunId(String v) {
        this.jobRunId = v;
        return this;
    }

    public AuditEventTestBuilder requestId(String v) {
        this.requestId = v;
        return this;
    }

    public AuditEventTestBuilder eventId(String v) {
        this.eventId = v;
        return this;
    }

    public AuditEventTestBuilder companyId(long v) {
        this.companyId = v;
        return this;
    }

    public AuditEventTestBuilder groupId(long v) {
        this.groupId = v;
        return this;
    }

    public AuditEventTestBuilder userId(long v) {
        this.userId = v;
        return this;
    }

    public AuditEventTestBuilder courseCode(String v) {
        this.courseCode = v;
        return this;
    }

    public AuditEventTestBuilder ntucDTId(long v) {
        this.ntucDTId = v;
        return this;
    }

    public AuditEventTestBuilder startTimeMs(long v) {
        this.startTimeMs = v;
        return this;
    }

    public AuditEventTestBuilder endTimeMs(long v) {
        this.endTimeMs = v;
        return this;
    }

    public AuditEventTestBuilder stackTraceHash(String v) {
        this.stackTraceHash = v;
        return this;
    }

    public AuditEventTestBuilder stackTraceTruncated(String v) {
        this.stackTraceTruncated = v;
        return this;
    }

    public AuditEventTestBuilder detail(String key, String value) {
        if (key == null || key.trim().isEmpty()) {
            return this;
        }
        details.put(key, value == null ? "" : value);
        return this;
    }

    public AuditEvent build() {
        AuditEvent.Builder b = AuditEvent.builder()
            .startTimeMs(startTimeMs)
            .endTimeMs(endTimeMs)
            .correlationId(correlationId)
            .jobRunId(jobRunId)
            .eventId(eventId)
            .requestId(requestId)
            .companyId(companyId)
            .groupId(groupId)
            .userId(userId)
            .courseCode(courseCode)
            .ntucDTId(ntucDTId)
            .severity(severity)
            .status(status)
            .step(step)
            .category(category)
            .message(message)
            .errorCode(errorCode)
            .errorMessage(errorMessage)
            .exceptionClass(exceptionClass)
            .stackTraceHash(stackTraceHash)
            .stackTraceTruncated(stackTraceTruncated);

        if (!details.isEmpty()) {
            b.details(details);
        }

        return b.build();
    }
}
