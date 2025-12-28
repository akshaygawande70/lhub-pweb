package com.ntuc.notification.rest.internal.processor.validation;

import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.constants.ChangeFromConstants;
import com.ntuc.notification.constants.NotificationType;
import com.ntuc.notification.model.CourseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates CourseResponse against business rules for notification processing.
 *
 * Service-side validation logic (plain Java) -> unit-testable without OSGi.
 */
public class CourseResponseValidator {

    public List<String> validateRequiredFields(CourseResponse.Body body, CourseEventContext eventCtx) {
        List<String> missing = new ArrayList<>();

        if (body == null) {
            missing.add("body");
            return missing;
        }

        if (eventCtx == null) {
            missing.add("eventCtx");
            return missing;
        }

        String eventType = safe(eventCtx.getEventType());
        List<String> changeFrom = eventCtx.getChangeFromTypes();

        boolean requireCore =
                NotificationType.PUBLISHED.equalsIgnoreCase(eventType) ||
                (changeFrom != null && changeFrom.contains(ChangeFromConstants.COURSE));

        if (requireCore) {
            if (isBlank(body.getCourseCode())) missing.add("courseCode");
            if (body.getCourseImgUrls() == null || body.getCourseImgUrls().isEmpty()) missing.add("courseImgUrls");
            if (isBlank(body.getCourseName())) missing.add("courseName");
            if (body.getPopular() == null) missing.add("popular");
            if (body.getFunded() == null) missing.add("funded");
            if (isBlank(body.getDuration())) missing.add("duration");
        }

        return missing;
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
