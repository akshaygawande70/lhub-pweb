package com.ntuc.notification.service.internal.builder;

import com.ntuc.notification.audit.api.CourseArticleConfig;
import com.ntuc.notification.audit.api.CourseEventContext;

import java.util.Collections;
import java.util.List;

/**
 * Builds CourseEventContext deterministically.
 * Pure Java for JUnit.
 */
public class CourseEventContextBuilder {

    public CourseEventContext build(
        long groupId,
        long companyId,
        long userId,
        String courseCode,
        String eventType,
        List<String> changeFrom,
        long ntucDTId,
        CourseArticleConfig articleConfig) {

        List<String> safeChangeFrom = (changeFrom != null) ? changeFrom : Collections.<String>emptyList();

        return new CourseEventContext(
            groupId,
            companyId,
            userId,
            (courseCode != null) ? courseCode : "",
            (eventType != null) ? eventType : "",
            safeChangeFrom,
            ntucDTId,
            articleConfig
        );
    }
}
