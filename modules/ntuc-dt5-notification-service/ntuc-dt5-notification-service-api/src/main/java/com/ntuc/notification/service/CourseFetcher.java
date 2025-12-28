package com.ntuc.notification.service;

import com.liferay.journal.model.JournalArticle;
import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.service.api.dto.CriticalProcessingResult;

/**
 * Fetches and processes course data from CLS and updates Liferay articles accordingly.
 *
 * <p>Rules:
 * <ul>
 *   <li>No direct email sending from implementations.</li>
 *   <li>Observability is via AuditEventWriter only.</li>
 *   <li>For PUBLISHED: critical phase may fetch "all fields" and return a payload snapshot
 *       to avoid a second CLS call for NON-CRITICAL.</li>
 * </ul>
 */
public interface CourseFetcher {

    JournalArticle fetchAndProcessCritical(boolean isNew, CourseEventContext eventCtx);

    JournalArticle fetchAndProcessNonCritical(JournalArticle article, CourseEventContext eventCtx);

    JournalArticle fetchAndProcessCron(CourseEventContext eventCtx);

    JournalArticle findExistingArticle(long groupId, long companyId, String courseCode, CourseEventContext eventCtx);

    JournalArticle fetchAndProcessPublishedEventRetrigger(CourseEventContext eventCtx);

    JournalArticle processPublishedOTLPublishedEvent(String json, CourseEventContext eventCtx);

    /**
     * Critical phase that returns a payload snapshot which can be reused by NON-CRITICAL,
     * avoiding a second CLS call for PUBLISHED flows.
     */
    CriticalProcessingResult fetchAndProcessCriticalWithPayload(boolean isNew, CourseEventContext eventCtx);

    /**
     * NON-CRITICAL that uses the payload snapshot (typically serialized CourseResponse) instead of calling CLS again.
     */
    JournalArticle fetchAndProcessNonCriticalFromPayload(
            JournalArticle article,
            CourseEventContext eventCtx,
            String payloadJson);
}
