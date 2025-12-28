package com.ntuc.notification.rest.internal.processor.validation;

import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.model.CourseEventList;
import com.ntuc.notification.rest.internal.processor.validation.CourseNotificationRequestValidator.EventError;
import com.ntuc.notification.rest.internal.processor.validation.CourseNotificationRequestValidator.ValidationResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link CourseNotificationRequestValidator}.
 *
 * Coverage focus:
 * - Global wrapper validation
 * - Per-event mandatory fields
 * - Conditional changeFrom validation
 * - Course-level validation (courseCode, courseType)
 * - Timestamp parsing and GUID validation
 *
 * Pure JUnit + Mockito, no OSGi/Liferay dependencies.
 *
 * @author @akshaygawande
 */
@RunWith(MockitoJUnitRunner.class)
public class CourseNotificationRequestValidatorTest {

    private CourseNotificationRequestValidator validator;

    @Before
    public void setUp() {
        validator = new CourseNotificationRequestValidator();
    }

    // ---------------------------------------------------------------------
    // Global validation
    // ---------------------------------------------------------------------

    @Test
    public void validate_nullWrapper_fails() {
        ValidationResult result = validator.validate(null);

        assertFalse(result.isValid());
        assertEquals("Request body is required", result.getMessage());
        assertEquals(1, result.getEventErrors().size());
    }

    @Test
    public void validate_emptyEvents_fails() {
        CourseEventList wrapper = Mockito.mock(CourseEventList.class);
        Mockito.when(wrapper.getEvents()).thenReturn(Collections.emptyList());

        ValidationResult result = validator.validate(wrapper);

        assertFalse(result.isValid());
        assertEquals("No events found in request", result.getMessage());
    }

    // ---------------------------------------------------------------------
    // Happy path
    // ---------------------------------------------------------------------

    @Test
    public void validate_validEvent_ok() {
        CourseEventList wrapper = Mockito.mock(CourseEventList.class);
        CourseEvent event = buildValidEvent();

        Mockito.when(wrapper.getEvents()).thenReturn(Collections.singletonList(event));

        ValidationResult result = validator.validate(wrapper);

        assertTrue(result.isValid());
        assertTrue(result.getEventErrors().isEmpty());
    }

    // ---------------------------------------------------------------------
    // notificationId
    // ---------------------------------------------------------------------

    @Test
    public void validate_missingNotificationId_fails() {
        CourseEvent event = buildValidEvent();
        Mockito.when(event.getNotificationId()).thenReturn(" ");

        List<EventError> errors = validator.validateEvent(event);

        assertHasError(errors, "notificationId is required");
    }

    @Test
    public void validate_invalidGuid_fails() {
        CourseEvent event = buildValidEvent();
        Mockito.when(event.getNotificationId()).thenReturn("123");

        List<EventError> errors = validator.validateEvent(event);

        assertHasError(errors, "notificationId must be a valid GUID");
    }

    // ---------------------------------------------------------------------
    // eventType
    // ---------------------------------------------------------------------

    @Test
    public void validate_invalidEventType_fails() {
        CourseEvent event = buildValidEvent();
        Mockito.when(event.getEventType()).thenReturn("deleted");

        List<EventError> errors = validator.validateEvent(event);

        assertHasError(errors, "eventType must be one of");
    }

    // ---------------------------------------------------------------------
    // timestamp
    // ---------------------------------------------------------------------

    @Test
    public void validate_invalidTimestamp_fails() {
        CourseEvent event = buildValidEvent();
        Mockito.when(event.getTimestamp()).thenReturn("2024-01-01");

        List<EventError> errors = validator.validateEvent(event);

        assertHasError(errors, "timestamp must match format");
    }

    // ---------------------------------------------------------------------
    // courses
    // ---------------------------------------------------------------------

    @Test
    public void validate_missingCourses_fails() {
        CourseEvent event = buildValidEvent();
        Mockito.when(event.getCourses()).thenReturn(null);

        List<EventError> errors = validator.validateEvent(event);

        assertHasError(errors, "courses is required");
    }

    @Test
    public void validate_courseCodeTooLong_fails() {
        CourseEvent.Course course = buildCourse("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "TMS");

        CourseEvent event = buildValidEvent();
        Mockito.when(event.getCourses()).thenReturn(Collections.singletonList(course));

        List<EventError> errors = validator.validateEvent(event);

        assertHasError(errors, "courseCode must be <=");
    }

    @Test
    public void validate_invalidCourseType_fails() {
        CourseEvent.Course course = buildCourse("COURSE1", "ABC");

        CourseEvent event = buildValidEvent();
        Mockito.when(event.getCourses()).thenReturn(Collections.singletonList(course));

        List<EventError> errors = validator.validateEvent(event);

        assertHasError(errors, "courseType must be TMS");
    }

    // ---------------------------------------------------------------------
    // changeFrom (conditional)
    // ---------------------------------------------------------------------

    @Test
    public void validate_changedWithoutChangeFrom_fails() {
        CourseEvent event = buildValidEvent();
        Mockito.when(event.getEventType()).thenReturn("changed");
        Mockito.when(event.getChangeFrom()).thenReturn(Collections.emptyList());

        List<EventError> errors = validator.validateEvent(event);

        assertHasError(errors, "changeFrom is required");
    }

    @Test
    public void validate_invalidChangeFromValue_fails() {
        CourseEvent event = buildValidEvent();
        Mockito.when(event.getEventType()).thenReturn("changed");
        Mockito.when(event.getChangeFrom()).thenReturn(Arrays.asList("invalidField"));

        List<EventError> errors = validator.validateEvent(event);

        assertHasError(errors, "changeFrom value not allowed");
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private CourseEvent buildValidEvent() {
        CourseEvent event = Mockito.mock(CourseEvent.class);

        Mockito.when(event.getNotificationId())
                .thenReturn("123e4567-e89b-12d3-a456-426614174000");
        Mockito.when(event.getEventType()).thenReturn("published");
        Mockito.when(event.getTimestamp()).thenReturn("01/01/2024 10:00:00");

        CourseEvent.Course course = buildCourse("COURSE1", "TMS");
        Mockito.when(event.getCourses()).thenReturn(Collections.singletonList(course));

        Mockito.when(event.getChangeFrom()).thenReturn(null);

        return event;
    }

    private CourseEvent.Course buildCourse(String code, String type) {
        CourseEvent.Course course = Mockito.mock(CourseEvent.Course.class);
        Mockito.when(course.getCourseCode()).thenReturn(code);
        Mockito.when(course.getCourseType()).thenReturn(type);
        return course;
    }

    private void assertHasError(List<EventError> errors, String contains) {
        assertFalse(errors.isEmpty());
        boolean found = errors.stream()
                .anyMatch(e -> e.getMessage().contains(contains));
        assertTrue("Expected error containing: " + contains, found);
    }
}
