package com.ntuc.notification.rest.internal.processor.validation;

import com.ntuc.notification.model.CourseEvent;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class CourseEventValidatorTest {

    private final CourseEventValidator v = new CourseEventValidator();

    @Test
    public void validateEventHeader_nullEvent_throws() {
        assertThrows("CourseEvent must not be null", new Runnable() {
            @Override public void run() { v.validateEventHeader(null); }
        });
    }

    @Test
    public void validateEventHeader_missingNotificationId_throws() {
        CourseEvent e = baseEvent();
        e.setNotificationId(" ");

        assertThrows("notificationId is required", new Runnable() {
            @Override public void run() { v.validateEventHeader(e); }
        });
    }

    @Test
    public void validateEventHeader_invalidGuid_throws() {
        CourseEvent e = baseEvent();
        e.setNotificationId("not-a-guid");

        assertThrows("notificationId must be a valid GUID", new Runnable() {
            @Override public void run() { v.validateEventHeader(e); }
        });
    }

    @Test
    public void validateEventHeader_invalidEventType_throws() {
        CourseEvent e = baseEvent();
        e.setEventType("publish");

        assertThrows("eventType must be one of: published, unpublished, changed, inactive", new Runnable() {
            @Override public void run() { v.validateEventHeader(e); }
        });
    }

    @Test
    public void validateEventHeader_invalidTimestamp_throws() {
        CourseEvent e = baseEvent();
        e.setTimestamp("2025-01-01T10:00:00Z");

        assertThrows("timestamp must match format dd/MM/yyyy HH:mm:ss", new Runnable() {
            @Override public void run() { v.validateEventHeader(e); }
        });
    }

    @Test
    public void validate_coursesEmpty_throws() {
        CourseEvent e = baseEvent();
        e.setCourses(Collections.<CourseEvent.Course>emptyList());

        assertThrows("courses is required and must not be empty", new Runnable() {
            @Override public void run() { v.validate(e); }
        });
    }

    @Test
    public void validate_courseCodeTooLong_throws() {
        CourseEvent e = baseEvent();
        CourseEvent.Course c = new CourseEvent.Course();
        c.setCourseCode("123456789012345678901"); // 21 chars
        c.setCourseType("TMS");
        e.setCourses(Arrays.asList(c));

        assertThrows("course.courseCode must be <= 20 characters", new Runnable() {
            @Override public void run() { v.validate(e); }
        });
    }

    @Test
    public void validate_courseTypeNotTms_throws() {
        CourseEvent e = baseEvent();
        CourseEvent.Course c = new CourseEvent.Course();
        c.setCourseCode("SFDW021");
        c.setCourseType("LMS");
        e.setCourses(Arrays.asList(c));

        assertThrows("Invalid courseType: LMS", new Runnable() {
            @Override public void run() { v.validate(e); }
        });
    }

    @Test
    public void validate_changed_missingChangeFrom_throws() {
        CourseEvent e = baseEvent();
        e.setEventType("changed");
        e.setChangeFrom(null);

        assertThrows("changeFrom is required when eventType=changed", new Runnable() {
            @Override public void run() { v.validate(e); }
        });
    }

    @Test
    public void validate_changed_invalidChangeFromValue_throws() {
        CourseEvent e = baseEvent();
        e.setEventType("changed");
        e.setChangeFrom(Arrays.asList("badValue"));

        assertThrows("changeFrom value not allowed: badValue. Allowed:", new Runnable() {
            @Override public void run() { v.validate(e); }
        });
    }

    @Test
    public void validate_changed_allowsfundingEligibilityCriteria_ok() {
        CourseEvent e = baseEvent();
        e.setEventType("changed");
        e.setChangeFrom(Arrays.asList("fundingEligibilityCriteria"));

        v.validate(e); // no throw
    }

    @Test
    public void validate_courseCodeSingle_present_mustMatchCourses() {
        CourseEvent e = baseEvent();
        e.setCourseCodeSingle("DIFFERENT");

        assertThrows("courseCodeSingle must match one of courses[].courseCode", new Runnable() {
            @Override public void run() { v.validate(e); }
        });
    }

    @Test
    public void validate_validPublished_ok() {
        v.validate(baseEvent()); // no throw
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private static CourseEvent baseEvent() {
        CourseEvent e = new CourseEvent();
        e.setNotificationId("ff1feb90-63bb-49b8-bf57-3703bfced789");
        e.setEventType("published");
        e.setTimestamp("13/04/2025 10:30:15");

        CourseEvent.Course c = new CourseEvent.Course();
        c.setCourseCode("SFDW021");
        c.setCourseType("TMS");
        e.setCourses(Arrays.asList(c));

        // optional by service flows; keep empty here
        e.setCourseCodeSingle("");

        return e;
    }

    private static void assertThrows(final String expectedMessageOrPrefix, Runnable r) {
        try {
            r.run();
            fail("Expected IllegalArgumentException containing: " + expectedMessageOrPrefix);
        } catch (IllegalArgumentException ex) {
            String msg = String.valueOf(ex.getMessage());
            if (!msg.contains(expectedMessageOrPrefix)) {
                fail("Expected message containing: [" + expectedMessageOrPrefix + "] but was: [" + msg + "]");
            }
        }
    }
}
