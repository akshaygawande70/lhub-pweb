package com.ntuc.notification.service;

import com.liferay.journal.model.JournalArticle;
import com.ntuc.notification.audit.api.CourseEventContext;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface NotificationHandler {

    /**
     * Main entry point for a course event workflow.
     *
     * IMPORTANT:
     * - CourseArticleConfig must be obtained from eventCtx.getArticleConfig().
     */
    JournalArticle process(CourseEventContext eventCtx);

    /**
     * Retrigger entry (reprocess event).
     *
     * IMPORTANT:
     * - CourseArticleConfig must be obtained from eventCtx.getArticleConfig().
     */
    JournalArticle processRetrigger(CourseEventContext eventCtx);

    /**
     * Critical phase (sync).
     */
    JournalArticle processCritical(boolean isNew, CourseEventContext eventCtx) throws Exception;

    /**
     * Non-critical phase (async by default; sync if isRetrigger=true).
     */
    JournalArticle asyncNonCritical(
            JournalArticle article,
            CourseEventContext eventCtx,
            boolean isRetrigger);

    /**
     * Cron processing entry.
     */
    JournalArticle processCron(CourseEventContext eventCtx) throws Exception;

    /**
     * OTL published event (JSON-triggered).
     *
     * IMPORTANT:
     * - CourseArticleConfig must be obtained from eventCtx.getArticleConfig().
     * - courseCode must come from eventCtx.getCourseCode() (or derived from JSON in CourseFetcher).
     */
    JournalArticle triggerOTLPublishedEvent(String json, CourseEventContext eventCtx);

    /**
     * OTL inactive/unpublished event (status-only).
     *
     * IMPORTANT:
     * - CourseArticleConfig must be obtained from eventCtx.getArticleConfig().
     */
    JournalArticle triggerOTLInactiveEvent(CourseEventContext eventCtx, int status);
}
