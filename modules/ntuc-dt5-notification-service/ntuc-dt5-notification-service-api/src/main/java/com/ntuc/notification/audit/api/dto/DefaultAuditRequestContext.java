package com.ntuc.notification.audit.api.dto;

import com.ntuc.notification.audit.api.AuditRequestContext;

import java.io.Serializable;
import java.util.UUID;

/**
 * Default immutable implementation of {@link AuditRequestContext}.
 *
 * Use this everywhere (REST, scheduler, async) instead of placing implementations in rest/internal.
 */
public final class DefaultAuditRequestContext implements AuditRequestContext, Serializable {

    private static final long serialVersionUID = 1L;

    private final String correlationId;
    private final long companyId;
    private final long groupId;
    private final long userId;
    private final String courseCode;
    private final long ntucDTId;

    private DefaultAuditRequestContext(Builder b) {
        this.correlationId = b.correlationId;
        this.companyId = b.companyId;
        this.groupId = b.groupId;
        this.userId = b.userId;
        this.courseCode = b.courseCode;
        this.ntucDTId = b.ntucDTId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public DefaultAuditRequestContext withCourse(String courseCode, long ntucDTId) {
        return DefaultAuditRequestContext.builder()
                .correlationId(this.correlationId)
                .companyId(this.companyId)
                .groupId(this.groupId)
                .userId(this.userId)
                .courseCode(courseCode)
                .ntucDTId(ntucDTId)
                .build();
    }

    @Override
    public String getCorrelationId() {
        return correlationId;
    }

    @Override
    public long getCompanyId() {
        return companyId;
    }

    @Override
    public long getGroupId() {
        return groupId;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public String getCourseCode() {
        return courseCode;
    }

    @Override
    public long getNtucDTId() {
        return ntucDTId;
    }

    public static final class Builder {

        private String correlationId;
        private long companyId;
        private long groupId;
        private long userId;
        private String courseCode = "";
        private long ntucDTId;

        public Builder correlationId(String correlationId) {
            this.correlationId = safeTrim(correlationId);
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
            this.courseCode = safeTrim(courseCode);
            return this;
        }

        public Builder ntucDTId(long ntucDTId) {
            this.ntucDTId = ntucDTId;
            return this;
        }

        public DefaultAuditRequestContext build() {
            if (isBlank(correlationId)) {
                // Caller should normally provide correlation, but we guarantee non-blank.
                correlationId = UUID.randomUUID().toString();
            }
            if (courseCode == null) {
                courseCode = "";
            }
            return new DefaultAuditRequestContext(this);
        }

        private static String safeTrim(String s) {
            return (s == null) ? "" : s.trim();
        }

        private static boolean isBlank(String s) {
            return s == null || s.trim().isEmpty();
        }
    }
}
