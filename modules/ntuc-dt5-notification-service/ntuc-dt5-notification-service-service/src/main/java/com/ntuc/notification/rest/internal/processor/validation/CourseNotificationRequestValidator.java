package com.ntuc.notification.rest.internal.processor.validation;

import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.model.CourseEventList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Validates the inbound CLS → PWEB notification request.
 *
 * Mandatory rules (strict):
 * - wrapper required
 * - events[] required and not empty
 * - notificationId required (GUID)
 * - eventType required: published | unpublished | changed | inactive
 * - timestamp required: dd/MM/yyyy HH:mm:ss
 * - courses[] required and not empty
 * - each courseCode required (trimmed, length <= 20)
 * - courseType required and must be TMS
 *
 * Conditional rules:
 * - changeFrom[] required when eventType=changed
 * - changeFrom values must be within the allowed enum set
 *
 * Pure Java - unit testable (no OSGi, no Liferay).
 */
public final class CourseNotificationRequestValidator {

    public static final String TS_PATTERN = "dd/MM/yyyy HH:mm:ss";

    private static final int COURSE_CODE_MAX_LEN = 20;
    private static final String COURSE_TYPE_TMS = "TMS";

    private static final Pattern GUID =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private static final Set<String> EVENT_TYPES = new HashSet<>(
            Arrays.asList("published", "unpublished", "changed", "inactive"));

    /**
     * IMPORTANT: Use spelling as agreed:
     * - fundingEligibilityCriteria
     */
    private static final Set<String> CHANGE_FROM_ALLOWED = new HashSet<>(
            Arrays.asList("course", "pricingTable", "fundingEligibilityCriteria", "subsidy"));

    private final SimpleDateFormat tsFormat;

    public CourseNotificationRequestValidator() {
        this(TS_PATTERN);
    }

    public CourseNotificationRequestValidator(String timestampPattern) {
        tsFormat = new SimpleDateFormat(timestampPattern, Locale.ENGLISH);
        tsFormat.setLenient(false);
    }

    public ValidationResult validate(CourseEventList wrapper) {
        if (wrapper == null) {
            return ValidationResult.failedGlobal("Request body is required");
        }

        List<CourseEvent> events = wrapper.getEvents();
        if (events == null || events.isEmpty()) {
            return ValidationResult.failedGlobal("No events found in request");
        }

        List<EventError> errors = new ArrayList<>();

        for (CourseEvent e : events) {
            errors.addAll(validateEvent(e));
        }

        if (!errors.isEmpty()) {
            return ValidationResult.failed(errors, "Invalid request parameter(s)");
        }

        return ValidationResult.ok();
    }

    /**
     * Public per-event validation to enable partial_success processing.
     * Returns an empty list if the event is valid.
     */
    public List<EventError> validateEvent(CourseEvent e) {
        return validateEventInternal(e);
    }

    private List<EventError> validateEventInternal(CourseEvent e) {
        if (e == null) {
            return Collections.singletonList(new EventError("", "", "Event item must not be null"));
        }

        String notificationId = safe(e.getNotificationId());
        String eventType = safeLower(e.getEventType());
        String timestamp = safe(e.getTimestamp());

        // IMPORTANT:
        // 1) Even if the error is header-level, try to surface courseCode for ops/debugging.
        // 2) This must be derived from courses[] because event doesn't have a native courseCode.
        final String derivedCourseCode = deriveFirstCourseCode(e);

        List<EventError> errs = new ArrayList<>();

        // notificationId
        if (isBlank(notificationId)) {
            errs.add(new EventError("", derivedCourseCode, "notificationId is required"));
        } else if (!GUID.matcher(notificationId).matches()) {
            errs.add(new EventError(notificationId, derivedCourseCode, "notificationId must be a valid GUID"));
        }

        // eventType
        if (isBlank(eventType)) {
            errs.add(new EventError(notificationIdOrEmpty(notificationId), derivedCourseCode, "eventType is required"));
        } else if (!EVENT_TYPES.contains(eventType)) {
            errs.add(new EventError(notificationIdOrEmpty(notificationId), derivedCourseCode,
                    "eventType must be one of: published, unpublished, changed, inactive"));
        }

        // timestamp
        if (isBlank(timestamp)) {
            errs.add(new EventError(notificationIdOrEmpty(notificationId), derivedCourseCode, "timestamp is required"));
        } else if (!isValidTimestamp(timestamp)) {
            errs.add(new EventError(notificationIdOrEmpty(notificationId), derivedCourseCode,
                    "timestamp must match format " + TS_PATTERN));
        }

        // courses[]
        List<CourseEvent.Course> courses = e.getCourses();
        if (courses == null || courses.isEmpty()) {
            errs.add(new EventError(notificationIdOrEmpty(notificationId), "",
                    "courses is required and must not be empty"));
        } else {
            for (CourseEvent.Course c : courses) {
                if (c == null) {
                    errs.add(new EventError(notificationIdOrEmpty(notificationId), derivedCourseCode,
                            "courses item must not be null"));
                    continue;
                }

                String courseCode = safe(c.getCourseCode());
                String courseType = safe(c.getCourseType());

                if (isBlank(courseCode)) {
                    errs.add(new EventError(notificationIdOrEmpty(notificationId), derivedCourseCode, "courseCode is required"));
                } else if (courseCode.length() > COURSE_CODE_MAX_LEN) {
                    errs.add(new EventError(notificationIdOrEmpty(notificationId), courseCode,
                            "courseCode must be <= " + COURSE_CODE_MAX_LEN + " characters"));
                }

                if (isBlank(courseType)) {
                    errs.add(new EventError(notificationIdOrEmpty(notificationId), courseCode,
                            "courseType is required"));
                } else if (!COURSE_TYPE_TMS.equals(courseType)) {
                    errs.add(new EventError(notificationIdOrEmpty(notificationId), courseCode,
                            "courseType must be " + COURSE_TYPE_TMS));
                }
            }
        }

        // changeFrom[] (only for changed)
        if ("changed".equals(eventType)) {
            List<String> changeFrom = e.getChangeFrom();
            if (changeFrom == null || changeFrom.isEmpty()) {
                errs.add(new EventError(notificationIdOrEmpty(notificationId), derivedCourseCode,
                        "changeFrom is required when eventType=changed"));
            } else {
                for (String cf : changeFrom) {
                    String v = safe(cf);
                    if (isBlank(v)) {
                        errs.add(new EventError(notificationIdOrEmpty(notificationId), derivedCourseCode,
                                "changeFrom contains an empty value"));
                        continue;
                    }
                    if (!CHANGE_FROM_ALLOWED.contains(v)) {
                        errs.add(new EventError(notificationIdOrEmpty(notificationId), derivedCourseCode,
                                "changeFrom value not allowed: " + v + ". Allowed: " + CHANGE_FROM_ALLOWED));
                    }
                }
            }
        }

        return errs;
    }

    /**
     * Derives a courseCode for error reporting even when validation fails before "normalization".
     * Deterministic rule:
     * - first non-blank courses[].courseCode (trimmed)
     * - else empty string
     */
    private static String deriveFirstCourseCode(CourseEvent e) {
        if (e == null) {
            return "";
        }

        List<CourseEvent.Course> courses = e.getCourses();
        if (courses == null || courses.isEmpty()) {
            return "";
        }

        for (CourseEvent.Course c : courses) {
            if (c == null) {
                continue;
            }
            String cc = safe(c.getCourseCode());
            if (!isBlank(cc)) {
                return cc;
            }
        }

        return "";
    }

    private boolean isValidTimestamp(String ts) {
        try {
            tsFormat.parse(ts);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    private static String notificationIdOrEmpty(String notificationId) {
        return (notificationId == null) ? "" : notificationId;
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

    // ========================================================================
    // Result DTOs (pure Java)
    // ========================================================================

    public static final class ValidationResult {
        private final boolean valid;
        private final String message;
        private final List<EventError> eventErrors;

        private ValidationResult(boolean valid, String message, List<EventError> eventErrors) {
            this.valid = valid;
            this.message = message;
            this.eventErrors = (eventErrors == null) ? Collections.<EventError>emptyList() : eventErrors;
        }

        public static ValidationResult ok() {
            return new ValidationResult(true, "", Collections.<EventError>emptyList());
        }

        public static ValidationResult failedGlobal(String message) {
            return new ValidationResult(false, message,
                    Collections.singletonList(new EventError("", "", message)));
        }

        public static ValidationResult failed(List<EventError> errors, String message) {
            return new ValidationResult(false, message,
                    (errors == null) ? Collections.<EventError>emptyList() : errors);
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }

        public List<EventError> getEventErrors() {
            return eventErrors;
        }
    }

    /**
     * Error item returned by validator.
     *
     * NOTE: courseCode is included:
     * - Always filled when derivable from courses[]
     * - Empty only when genuinely unavailable
     */
    public static final class EventError {
        private final String notificationId;
        private final String courseCode;
        private final String message;

        public EventError(String notificationId, String courseCode, String message) {
            this.notificationId = (notificationId == null) ? "" : notificationId;
            this.courseCode = (courseCode == null) ? "" : courseCode;
            this.message = (message == null) ? "" : message;
        }

        public String getNotificationId() {
            return notificationId;
        }

        public String getCourseCode() {
            return courseCode;
        }

        public String getMessage() {
            return message;
        }
    }
}
