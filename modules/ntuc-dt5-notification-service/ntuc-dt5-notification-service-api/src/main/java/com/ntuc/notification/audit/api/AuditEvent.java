package com.ntuc.notification.audit.api;

import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable audit DTO (single source of truth).
 *
 * Rules:
 * - Must be sufficient to reconstruct execution timelines without server logs.
 * - Must not contain secrets or raw payloads (sanitized, size-capped elsewhere).
 * - Use enums for category/step/status/errorCode (no random strings).
 *
 * Field semantics:
 * - correlationId is always non-null/non-blank.
 * - Business keys (e.g. courseCode) may be null when unknown.
 */
public final class AuditEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long startTimeMs;
    private final long endTimeMs;
    private final long durationMs;

    private final String correlationId;
    private final String jobRunId;
    private final String eventId;
    private final String requestId;

    private final long companyId;
    private final long groupId;
    private final long userId;

    private final String courseCode;
    private final long ntucDTId;

    private final AuditSeverity severity;
    private final AuditStatus status;
    private final AuditStep step;
    private final AuditCategory category;

    private final String message;

    private final AuditErrorCode errorCode;
    private final String errorMessage;
    private final String exceptionClass;

    private final String stackTraceHash;
    private final String stackTraceTruncated;

    private final Map<String, String> details;

    /**
     * Preferred factory method so callers don't need to know the nested Builder type.
     */
    public static Builder builder() {
        return new Builder();
    }

    private AuditEvent(Builder b) {
        this.startTimeMs = b.startTimeMs;
        this.endTimeMs = b.endTimeMs;
        this.durationMs = computeDurationMs(b.startTimeMs, b.endTimeMs);

        this.correlationId = safe(b.correlationId);
        this.jobRunId = safe(b.jobRunId);
        this.eventId = safe(b.eventId);
        this.requestId = safe(b.requestId);

        this.companyId = b.companyId;
        this.groupId = b.groupId;
        this.userId = b.userId;

        // Business keys MUST remain nullable when unknown.
        this.courseCode = safeNullable(b.courseCode);
        this.ntucDTId = b.ntucDTId;

        this.severity = Objects.requireNonNull(b.severity, "severity");
        this.status = Objects.requireNonNull(b.status, "status");
        this.step = Objects.requireNonNull(b.step, "step");
        this.category = Objects.requireNonNull(b.category, "category");

        this.message = safe(b.message);

        this.errorCode = (b.errorCode == null) ? AuditErrorCode.NONE : b.errorCode;
        this.errorMessage = safeNullable(b.errorMessage);
        this.exceptionClass = safeNullable(b.exceptionClass);

        this.stackTraceHash = safeNullable(b.stackTraceHash);
        this.stackTraceTruncated = safeNullable(b.stackTraceTruncated);

        this.details = immutableCopy(b.details);
    }

    public long getStartTimeMs() {
        return startTimeMs;
    }

    public long getEndTimeMs() {
        return endTimeMs;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getJobRunId() {
        return jobRunId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getRequestId() {
        return requestId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public long getGroupId() {
        return groupId;
    }

    public long getUserId() {
        return userId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public long getNtucDTId() {
        return ntucDTId;
    }

    public AuditSeverity getSeverity() {
        return severity;
    }

    public AuditStatus getStatus() {
        return status;
    }

    public AuditStep getStep() {
        return step;
    }

    public AuditCategory getCategory() {
        return category;
    }

    public String getMessage() {
        return message;
    }

    public AuditErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public String getStackTraceHash() {
        return stackTraceHash;
    }

    public String getStackTraceTruncated() {
        return stackTraceTruncated;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public String getDetailsJson() {
        // Keep DTO simple: JSON serialization belongs in service-layer mapper.
        return null;
    }

    /**
     * Immutable "copy-with" adding/overwriting a single detail key.
     */
    public AuditEvent withDetail(String key, String value) {
        Builder b = toBuilder();
        b.detail(key, value);
        return b.build();
    }

    /**
     * Immutable "copy-with" replacing details map.
     *
     * NOTE:
     * - This replaces the entire details map.
     * - Sanitization/size capping must be done outside the DTO.
     */
    public AuditEvent withDetails(Map<String, String> details) {
        Builder b = toBuilder();
        b.details(details);
        return b.build();
    }

    /**
     * Immutable "copy-with" courseCode override.
     */
    public AuditEvent withCourseCode(String courseCode) {
        Builder b = toBuilder();
        b.courseCode(courseCode);
        return b.build();
    }

    public Builder toBuilder() {
        Builder b = new Builder();
        b.startTimeMs(startTimeMs);
        b.endTimeMs(endTimeMs);

        b.correlationId(correlationId);
        b.jobRunId(jobRunId);
        b.eventId(eventId);
        b.requestId(requestId);

        b.companyId(companyId);
        b.groupId(groupId);
        b.userId(userId);

        b.courseCode(courseCode);
        b.ntucDTId(ntucDTId);

        b.severity(severity);
        b.status(status);
        b.step(step);
        b.category(category);

        b.message(message);

        b.errorCode(errorCode);
        b.errorMessage(errorMessage);
        b.exceptionClass(exceptionClass);

        b.stackTraceHash(stackTraceHash);
        b.stackTraceTruncated(stackTraceTruncated);

        if (details != null) {
            for (Map.Entry<String, String> e : details.entrySet()) {
                b.detail(e.getKey(), e.getValue());
            }
        }
        return b;
    }

    private static long computeDurationMs(long start, long end) {
        if (start <= 0 || end <= 0) {
            return 0L;
        }
        long d = end - start;
        return (d < 0) ? 0L : d;
    }

    private static Map<String, String> immutableCopy(Map<String, String> in) {
        if (in == null || in.isEmpty()) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(new LinkedHashMap<String, String>(in));
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }

    private static String safeNullable(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s;
    }

    /**
     * Public builder (must be public for callers across modules).
     */
    public static final class Builder {

        private long startTimeMs;
        private long endTimeMs;

        private String correlationId;
        private String jobRunId;
        private String eventId;
        private String requestId;

        private long companyId;
        private long groupId;
        private long userId;

        private String courseCode;
        private long ntucDTId;

        private AuditSeverity severity = AuditSeverity.INFO;
        private AuditStatus status = AuditStatus.STARTED;
        private AuditStep step;
        private AuditCategory category;

        private String message;

        private AuditErrorCode errorCode = AuditErrorCode.NONE;
        private String errorMessage;
        private String exceptionClass;

        private String stackTraceHash;
        private String stackTraceTruncated;

        private Map<String, String> details;

        public Builder startTimeMs(long startTimeMs) {
            this.startTimeMs = startTimeMs;
            return this;
        }

        public Builder endTimeMs(long endTimeMs) {
            this.endTimeMs = endTimeMs;
            return this;
        }

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder jobRunId(String jobRunId) {
            this.jobRunId = jobRunId;
            return this;
        }

        public Builder eventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder companyId(long companyId) {
            this.companyId = companyId;
            return this;
        }

        public Builder groupId(long groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder userId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder courseCode(String courseCode) {
            this.courseCode = courseCode;
            return this;
        }

        public Builder ntucDTId(long ntucDTId) {
            this.ntucDTId = ntucDTId;
            return this;
        }

        public Builder severity(AuditSeverity severity) {
            if (severity != null) {
                this.severity = severity;
            }
            return this;
        }

        public Builder status(AuditStatus status) {
            if (status != null) {
                this.status = status;
            }
            return this;
        }

        public Builder step(AuditStep step) {
            this.step = step;
            return this;
        }

        public Builder category(AuditCategory category) {
            this.category = category;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder errorCode(AuditErrorCode errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder exceptionClass(String exceptionClass) {
            this.exceptionClass = exceptionClass;
            return this;
        }

        public Builder stackTraceHash(String stackTraceHash) {
            this.stackTraceHash = stackTraceHash;
            return this;
        }

        public Builder stackTraceTruncated(String stackTraceTruncated) {
            this.stackTraceTruncated = stackTraceTruncated;
            return this;
        }

        public Builder detail(String key, String value) {
            if (key == null || key.trim().isEmpty()) {
                return this;
            }
            if (details == null) {
                details = new LinkedHashMap<String, String>();
            }
            details.put(key, value == null ? "" : value);
            return this;
        }

        /**
         * Bulk replace details map.
         *
         * NOTE:
         * - This REPLACES any existing details accumulated on the builder.
         * - Callers who want to "merge" should call detail(k,v) repeatedly.
         */
        public Builder details(Map<String, String> details) {
            if (details == null || details.isEmpty()) {
                this.details = null;
                return this;
            }
            this.details = new LinkedHashMap<String, String>(details);
            return this;
        }

        public AuditEvent build() {
            Objects.requireNonNull(step, "step");
            Objects.requireNonNull(category, "category");
            return new AuditEvent(this);
        }
    }
}
