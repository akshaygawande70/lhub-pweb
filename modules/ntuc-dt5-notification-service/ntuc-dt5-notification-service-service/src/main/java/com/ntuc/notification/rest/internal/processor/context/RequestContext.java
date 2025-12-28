package com.ntuc.notification.rest.internal.processor.context;

import com.ntuc.notification.audit.api.AuditRequestContext;

import java.io.Serializable;

/**
 * Request-scoped context captured from Liferay thread-locals + correlation id.
 *
 * Immutable by design.
 *
 * Contract:
 * - courseCode is never null ("" when unknown).
 */
public class RequestContext implements AuditRequestContext, Serializable {

    private static final long serialVersionUID = 1L;

    private final String correlationId;
    private final long companyId;
    private final long groupId;
    private final long userId;
    private final String courseCode;
    private final long ntucDTId;

    public RequestContext(
            String correlationId,
            long companyId,
            long groupId,
            long userId,
            String courseCode,
            long ntucDTId) {

        this.correlationId = correlationId;
        this.companyId = companyId;
        this.groupId = groupId;
        this.userId = userId;
        this.courseCode = (courseCode == null) ? "" : courseCode;
        this.ntucDTId = ntucDTId;
    }

    /**
     * Returns a NEW RequestContext with course-specific business keys.
     * Original context remains untouched.
     */
    public RequestContext withCourse(String courseCode, long ntucDTId) {
        return new RequestContext(
            this.correlationId,
            this.companyId,
            this.groupId,
            this.userId,
            courseCode,
            ntucDTId
        );
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
}
