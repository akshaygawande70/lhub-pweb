package com.ntuc.notification.service;

import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.model.ScheduleResponse;

import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ScheduleHandler {

    /**
     * High-level handler for schedule resolution.
     * Called by processors / REST layers.
     */
    // ScheduleResponse handleCourseSchedules(String courseCode);

    /**
     * Main workflow entry point (critical + async non-critical).
     *
     * Implementation will audit/alert and throw RuntimeException on failure.
     */
    ScheduleResponse process(CourseEventContext eventCtx);

    /**
     * Processes critical fields synchronously.
     *
     * @return Map<journalArticleId, ScheduleResponse>
     */
    Map<Long, ScheduleResponse> processCritical(CourseEventContext eventCtx);

    /**
     * Processes non-critical fields asynchronously.
     */
    void processAsyncNonCritical(long id, ScheduleResponse scheduleResponse, CourseEventContext eventCtx);
}
