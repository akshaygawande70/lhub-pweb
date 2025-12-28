package com.ntuc.notification.audit.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * API-safe context used across modules.
 *
 * Rules:
 * - No OSGi annotations
 * - No Liferay services/models
 * - No com.ntuc.notification.model.CourseEvent exposure
 */
public class CourseEventContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long groupId;
    private final long companyId;
    private final long userId;

    private final String courseCode;
    private final String eventType;

    /**
     * Optional “changeFrom” types used when eventType=CHANGED to compute field intersections.
     * API-safe: plain List<String>.
     */
    private final List<String> changeFromTypes;

    /**
     * For audit correlation (from NtucSB.ntucDTId).
     */
    private final long ntucDTId;

    /**
     * Article configuration needed by field processing pipeline.
     */
    private final CourseArticleConfig articleConfig;

    // ---------------------------------------------------------------------
    // Backward-compatible constructors (to fix your compile errors)
    // ---------------------------------------------------------------------

    /**
     * Legacy minimal ctor used by older call-sites.
     * Defaults:
     * - eventType=""
     * - changeFromTypes=[]
     * - ntucDTId=0
     * - articleConfig=null
     */
    public CourseEventContext(long groupId, long companyId, long userId, String courseCode) {
        this(
            groupId,
            companyId,
            userId,
            courseCode,
            "",
            Collections.<String>emptyList(),
            0L,
            null
        );
    }

    /**
     * Slightly richer ctor: includes eventType.
     * Defaults:
     * - changeFromTypes=[]
     * - ntucDTId=0
     * - articleConfig=null
     */
    public CourseEventContext(long groupId, long companyId, long userId, String courseCode, String eventType) {
        this(
            groupId,
            companyId,
            userId,
            courseCode,
            eventType,
            Collections.<String>emptyList(),
            0L,
            null
        );
    }

    /**
     * Convenience ctor when you have config but not ntucDTId.
     * Defaults:
     * - ntucDTId=0
     */
    public CourseEventContext(
            long groupId,
            long companyId,
            long userId,
            String courseCode,
            String eventType,
            List<String> changeFromTypes,
            CourseArticleConfig articleConfig) {

        this(
            groupId,
            companyId,
            userId,
            courseCode,
            eventType,
            changeFromTypes,
            0L,
            articleConfig
        );
    }

    // ---------------------------------------------------------------------
    // Primary ctor
    // ---------------------------------------------------------------------

    public CourseEventContext(
            long groupId,
            long companyId,
            long userId,
            String courseCode,
            String eventType,
            List<String> changeFromTypes,
            long ntucDTId,
            CourseArticleConfig articleConfig) {

        this.groupId = groupId;
        this.companyId = companyId;
        this.userId = userId;
        this.courseCode = (courseCode == null) ? "" : courseCode;
        this.eventType = (eventType == null) ? "" : eventType;
        this.changeFromTypes = (changeFromTypes == null) ? Collections.<String>emptyList() : changeFromTypes;
        this.ntucDTId = ntucDTId;
        this.articleConfig = articleConfig;
    }

    public long getGroupId() {
        return groupId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public long getUserId() {
        return userId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getEventType() {
        return eventType;
    }

    public List<String> getChangeFromTypes() {
        return changeFromTypes;
    }

    public long getNtucDTId() {
        return ntucDTId;
    }

    public CourseArticleConfig getArticleConfig() {
        return articleConfig;
    }
}
