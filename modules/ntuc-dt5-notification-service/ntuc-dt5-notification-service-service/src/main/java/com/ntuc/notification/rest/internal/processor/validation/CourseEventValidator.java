package com.ntuc.notification.rest.internal.processor.validation;

import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.model.CourseEvent.Course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Pure Java validator for CourseEvent payloads (FAIL-FAST).
 *
 * IMPORTANT:
 * - No OSGi annotations.
 * - No Liferay services.
 * - Safe for plain JUnit tests.
 *
 * COMPATIBILITY:
 * - Supports both new CourseEventValidator() and new CourseEventValidator("pattern").
 * - Supports validateEventHeader(...) / validateCourse(...) (legacy tests)
 * - Supports validate(CourseEvent) (service orchestrator usage)
 *
 * WARNING:
 * - This validator THROWS on the first violation (fail-fast).
 * - For REST batch intake where partial_success is required, use
 *   CourseNotificationRequestValidator which COLLECTS errors instead.
 *
 * Contract rules aligned with CourseNotificationRequestValidator:
 * - notificationId required (GUID)
 * - eventType required: published | unpublished | changed | inactive
 * - timestamp required: dd/MM/yyyy HH:mm:ss (strict)
 * - courses[] required and not empty
 * - each courseCode required (trimmed, <= 20 chars)
 * - courseType required and must be TMS
 * - changeFrom[] required when eventType=changed
 * - changeFrom values allowed: course, pricingTable, fundingEligibilityCriteria, subsidy
 */
public class CourseEventValidator {

    private static final String DEFAULT_TIMESTAMP_PATTERN = "dd/MM/yyyy HH:mm:ss";
    private static final int COURSE_CODE_MAX_LEN = 20;
    private static final String COURSE_TYPE_TMS = "TMS";

    private static final Pattern GUID =
        Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private static final Set<String> EVENT_TYPES =
        new HashSet<String>(Arrays.asList("published", "unpublished", "changed", "inactive"));

    /**
     * IMPORTANT: agreed spelling is "fundingEligibilityCriteria".
     */
    private static final Set<String> CHANGE_FROM_ALLOWED =
        new HashSet<String>(Arrays.asList("course", "pricingTable", "fundingEligibilityCriteria", "subsidy"));

    private final String timestampPattern;
    private final SimpleDateFormat timestampFormat;

    public CourseEventValidator() {
        this(DEFAULT_TIMESTAMP_PATTERN);
    }

    public CourseEventValidator(String timestampPattern) {
        this.timestampPattern = isBlank(timestampPattern) ? DEFAULT_TIMESTAMP_PATTERN : timestampPattern;

        this.timestampFormat = new SimpleDateFormat(this.timestampPattern, Locale.ENGLISH);
        this.timestampFormat.setLenient(false);
    }

    /**
     * Validates the event as required by service processing.
     *
     * This method enforces the full contract:
     * - header validation
     * - courses validation
     * - changeFrom conditional validation
     *
     * Additionally, if courseCodeSingle is present, it must match one of the courses[] courseCode.
     */
    public void validate(CourseEvent event) {
        validateEventHeader(event);

        // Mandatory: courses[] and per-course rules
        validateCourses(event);

        // Conditional: changeFrom when changed
        validateChangeFrom(event);

        // Optional service convenience field: if present, must match a courseCode in courses[]
        String single = safe(event.getCourseCodeSingle());
        if (!isBlank(single)) {
            if (!containsCourseCode(event.getCourses(), single)) {
                throw new IllegalArgumentException(
                    "courseCodeSingle must match one of courses[].courseCode: " + single);
            }
        }
    }

    /**
     * Validates header-level fields that REST handler expects.
     * This is the API contract validation.
     */
    public void validateEventHeader(CourseEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("CourseEvent must not be null");
        }

        String notificationId = safe(event.getNotificationId());
        if (isBlank(notificationId)) {
            throw new IllegalArgumentException("notificationId is required");
        }
        if (!GUID.matcher(notificationId).matches()) {
            throw new IllegalArgumentException("notificationId must be a valid GUID");
        }

        String eventType = safeLower(event.getEventType());
        if (isBlank(eventType)) {
            throw new IllegalArgumentException("eventType is required");
        }
        if (!EVENT_TYPES.contains(eventType)) {
            throw new IllegalArgumentException(
                "eventType must be one of: published, unpublished, changed, inactive");
        }

        String ts = safe(event.getTimestamp());
        if (isBlank(ts)) {
            throw new IllegalArgumentException("timestamp is required");
        }
        parseTimestamp(ts);
    }

    /**
     * Validates a single Course element (used by REST tests).
     *
     * NOTE: This is strict and aligned with the API contract.
     */
    public void validateCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("course must not be null");
        }

        String courseCode = safe(course.getCourseCode());
        if (isBlank(courseCode)) {
            throw new IllegalArgumentException("course.courseCode is required");
        }
        if (courseCode.length() > COURSE_CODE_MAX_LEN) {
            throw new IllegalArgumentException("course.courseCode must be <= " + COURSE_CODE_MAX_LEN + " characters");
        }

        String courseType = safe(course.getCourseType());
        if (isBlank(courseType)) {
            throw new IllegalArgumentException("course.courseType is required");
        }
        if (!COURSE_TYPE_TMS.equals(courseType)) {
            throw new IllegalArgumentException("Invalid courseType: " + courseType);
        }
    }

    // ---------------- Internal validations ----------------

    private void validateCourses(CourseEvent event) {
        List<Course> courses = event.getCourses();
        if (courses == null || courses.isEmpty()) {
            throw new IllegalArgumentException("courses is required and must not be empty");
        }
        for (Course c : courses) {
            validateCourse(c);
        }
    }

    private void validateChangeFrom(CourseEvent event) {
        String eventType = safeLower(event.getEventType());
        if (!"changed".equals(eventType)) {
            return;
        }

        List<String> changeFrom = event.getChangeFrom();
        if (changeFrom == null || changeFrom.isEmpty()) {
            throw new IllegalArgumentException("changeFrom is required when eventType=changed");
        }

        for (String cf : changeFrom) {
            String v = safe(cf);
            if (isBlank(v)) {
                throw new IllegalArgumentException("changeFrom contains an empty value");
            }
            if (!CHANGE_FROM_ALLOWED.contains(v)) {
                throw new IllegalArgumentException(
                    "changeFrom value not allowed: " + v + ". Allowed: " + CHANGE_FROM_ALLOWED);
            }
        }
    }

    private void parseTimestamp(String ts) {
        try {
            timestampFormat.parse(ts);
        } catch (ParseException e) {
            throw new IllegalArgumentException("timestamp must match format " + timestampPattern);
        }
    }

    private static boolean containsCourseCode(List<Course> courses, String courseCode) {
        if (courses == null || courses.isEmpty() || isBlank(courseCode)) {
            return false;
        }
        String needle = courseCode.trim();
        for (Course c : courses) {
            if (c == null) {
                continue;
            }
            if (needle.equals(safe(c.getCourseCode()))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String safe(String s) {
        return (s == null) ? "" : s.trim();
    }

    private static String safeLower(String s) {
        return (s == null) ? "" : s.trim().toLowerCase(Locale.ENGLISH);
    }
}
