package com.ntuc.notification.service;

import com.liferay.journal.model.JournalArticle;
import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.model.ScheduleResponse;

import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ScheduleFetcher {

    /**
     * Fetch schedules for a course.
     *
     * - Prefer LIVE data from CLS
     * - Fallback to cached schedules when CLS is unreachable
     */
    // ScheduleResponse fetchCourseSchedules(String courseCode);

    /**
     * Critical schedule processing.
     *
     * NOTE: XML parsing/building errors are handled inside the service implementation
     * and surfaced as runtime exceptions after auditing/alerting.
     */
    Map<Long, ScheduleResponse> fetchAndProcessCriticalSchedule(CourseEventContext ctx);

    /**
     * Non-critical schedule enrichment and persistence.
     */
    JournalArticle fetchAndProcessNonCriticalSchedule(
            long articleId,
            ScheduleResponse scheduleResponse,
            CourseEventContext ctx);
}
