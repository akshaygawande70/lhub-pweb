package com.ntuc.notification.audit.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Immutable, API-safe workflow context shared across modules.
 *
 * <p><b>Business purpose:</b> Carries the minimum identifiers and metadata needed to correlate
 * a course-related workflow (create/update/schedule) to audit records and downstream processing.</p>
 *
 * <p><b>Technical purpose:</b> Provides a serializable DTO that can be passed across module boundaries
 * without introducing OSGi annotations, Liferay services/models, or internal service-layer types.</p>
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>No OSGi annotations</li>
 *   <li>No Liferay services/models</li>
 *   <li>No com.ntuc.notification.model.CourseEvent exposure</li>
 *   <li>Null-safe defaults for String/List fields to keep call-sites simple</li>
 * </ul>
 * </p>
 *
 * @author @akshaygawande
 */
public class CourseEventContext implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Scope identifiers for multi-tenant execution and content resolution.
     */
    private final long groupId;
    private final long companyId;

    /**
     * User initiating or representing the workflow execution.
     */
    private final long userId;

    /**
     * Course identifier used by CLS and NTUC workflows.
     * Never null; normalized to an empty string.
     */
    private final String courseCode;

    /**
     * Logical event type (e.g. CREATED / CHANGED / DELETED) used to drive processing paths.
     * Never null; normalized to an empty string.
     */
    private final String eventType;

    /**
     * Optional “changeFrom” types used when eventType = CHANGED to compute field intersections.
     * API-safe: plain List<String>, never null.
     */
    private final List<String> changeFromTypes;

    /**
     * Correlation identifier originating from NtucSB.ntucDTId.
     * Used exclusively for audit traceability.
     */
    private final long ntucDTId;

    /**
     * Article configuration required by the field processing pipeline.
     * May be null if the workflow does not involve JournalArticle mutations.
     */
    private final CourseArticleConfig articleConfig;

    // ---------------------------------------------------------------------
    // Backward-compatible constructors
    // ---------------------------------------------------------------------

    /**
     * Legacy minimal constructor used by older call-sites.
     *
     * <p><b>Defaults:</b>
     * <ul>
     *   <li>eventType = ""</li>
     *   <li>changeFromTypes = []</li>
     *   <li>ntucDTId = 0</li>
     *   <li>articleConfig = null</li>
     * </ul>
     * </p>
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
     * Backward-compatible constructor including event type.
     *
     * <p><b>Defaults:</b>
     * <ul>
     *   <li>changeFromTypes = []</li>
     *   <li>ntucDTId = 0</li>
     *   <li>articleConfig = null</li>
     * </ul>
     * </p>
     */
    public CourseEventContext(
            long groupId,
            long companyId,
            long userId,
            String courseCode,
            String eventType) {

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
     * Convenience constructor when article configuration is available but ntucDTId is not.
     *
     * <p><b>Defaults:</b>
     * <ul>
     *   <li>ntucDTId = 0</li>
     * </ul>
     * </p>
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
    // Primary constructor
    // ---------------------------------------------------------------------

    /**
     * Primary constructor used by new call-sites.
     *
     * <p><b>Invariants:</b>
     * <ul>
     *   <li>courseCode and eventType are never null</li>
     *   <li>changeFromTypes is never null</li>
     * </ul>
     * </p>
     */
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
        this.changeFromTypes =
                (changeFromTypes == null) ? Collections.<String>emptyList() : changeFromTypes;
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
