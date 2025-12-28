package com.ntuc.notification.service;

import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.model.CourseProcessResult;
import com.ntuc.notification.model.NtucSB;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ClsCourseFieldsProcessor {

    /**
     * Handles course notification event from CLS.
     *
     * @param event CourseEvent
     * @param isCron whether triggered by cron
     */
    void handleCourseNotification(CourseEvent event, boolean isCron);

    /**
     * Retriggers a course processing flow based on existing NtucSB entry.
     *
     * API MUST NOT leak Liferay JournalArticle, so we return an API-safe result.
     *
     * @param entry NtucSB entry
     * @return processing result
     */
    CourseProcessResult handleCourseRetrigger(NtucSB entry);

    /**
     * Processes full payload JSON for OTL published event.
     *
     * API MUST NOT leak Liferay JournalArticle, so we return an API-safe result.
     *
     * @param json payload JSON
     * @param eventCtx event context
     * @param courseCode course code
     * @return processing result
     */
    CourseProcessResult processAllFieldsFromJson(String json, CourseEventContext eventCtx, String courseCode);

    
}
