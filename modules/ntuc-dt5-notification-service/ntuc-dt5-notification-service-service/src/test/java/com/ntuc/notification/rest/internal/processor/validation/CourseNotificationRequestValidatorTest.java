package com.ntuc.notification.rest.internal.processor.validation;

import com.ntuc.notification.model.CourseEvent;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class CourseNotificationRequestValidatorTest {

    private final CourseNotificationRequestValidator validator = new CourseNotificationRequestValidator();

    @Test
    public void validateEvent_invalidEventType_stillReturnsCourseCode() {
        CourseEvent e = event(
                "ff1feb90-63bb-49b8-bf57-3703bfced804",
                "publishedd",
                "22/12/2025 00:29:00",
                Arrays.asList(course("NSS test-000012", "TMS"))
        );

        List<CourseNotificationRequestValidator.EventError> errors = validator.validateEvent(e);

        assertFalse(errors.isEmpty());

        CourseNotificationRequestValidator.EventError eventTypeErr = findByMessageContains(errors, "eventType must be one of");
        assertNotNull(eventTypeErr);
        assertEquals("NSS test-000012", eventTypeErr.getCourseCode());
    }

    @Test
    public void validateEvent_invalidTimestamp_stillReturnsCourseCode() {
        CourseEvent e = event(
                "ff1feb90-63bb-49b8-bf57-3703bfced804",
                "published",
                "22-12-2025 00:29:00", // invalid format
                Arrays.asList(course("NSS test-000012", "TMS"))
        );

        List<CourseNotificationRequestValidator.EventError> errors = validator.validateEvent(e);

        CourseNotificationRequestValidator.EventError tsErr = findByMessageContains(errors, "timestamp must match format");
        assertNotNull(tsErr);
        assertEquals("NSS test-000012", tsErr.getCourseCode());
    }

    @Test
    public void validateEvent_coursesMissing_headerErrorsReturnEmptyCourseCode() {
        CourseEvent e = event(
                "ff1feb90-63bb-49b8-bf57-3703bfced804",
                "publishedd",
                "22/12/2025 00:29:00",
                null
        );

        List<CourseNotificationRequestValidator.EventError> errors = validator.validateEvent(e);

        CourseNotificationRequestValidator.EventError eventTypeErr = findByMessageContains(errors, "eventType must be one of");
        assertNotNull(eventTypeErr);
        assertEquals("", eventTypeErr.getCourseCode());
    }

    @Test
    public void validateEvent_multipleCourses_usesFirstNonBlankCourseCode() {
        CourseEvent e = event(
                "ff1feb90-63bb-49b8-bf57-3703bfced804",
                "publishedd",
                "22/12/2025 00:29:00",
                Arrays.asList(
                        course("   ", "TMS"),
                        course("NSS test-000099", "TMS"),
                        course("NSS test-000100", "TMS")
                )
        );

        List<CourseNotificationRequestValidator.EventError> errors = validator.validateEvent(e);

        CourseNotificationRequestValidator.EventError eventTypeErr = findByMessageContains(errors, "eventType must be one of");
        assertNotNull(eventTypeErr);
        assertEquals("NSS test-000099", eventTypeErr.getCourseCode());
    }

    private static CourseNotificationRequestValidator.EventError findByMessageContains(
            List<CourseNotificationRequestValidator.EventError> errors, String contains) {

        for (CourseNotificationRequestValidator.EventError e : errors) {
            if (e != null && e.getMessage() != null && e.getMessage().contains(contains)) {
                return e;
            }
        }
        return null;
    }

    private static CourseEvent event(String notificationId, String eventType, String timestamp, List<CourseEvent.Course> courses) {
        CourseEvent e = new CourseEvent();
        e.setNotificationId(notificationId);
        e.setEventType(eventType);
        e.setTimestamp(timestamp);
        e.setCourses(courses == null ? Collections.<CourseEvent.Course>emptyList() : courses);
        e.setChangeFrom(Collections.<String>emptyList());
        return e;
    }

    private static CourseEvent.Course course(String courseCode, String courseType) {
        CourseEvent.Course c = new CourseEvent.Course();
        c.setCourseCode(courseCode);
        c.setCourseType(courseType);
        return c;
    }
}
