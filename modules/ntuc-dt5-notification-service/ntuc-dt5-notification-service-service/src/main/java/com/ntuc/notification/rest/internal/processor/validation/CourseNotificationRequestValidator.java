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
 * Validates inbound CLS → PWEB notification requests before any downstream processing occurs.
 *
 * <p><b>Business purpose:</b> Protects course content and publishing workflows by rejecting malformed or
 * semantically invalid notifications that could create incorrect updates or noisy audit trails.</p>
 *
 * <p><b>Technical purpose:</b> Applies strict structural and field-level validation to {@link CourseEventList}
 * and each contained {@link CourseEvent}, returning a deterministic list of errors suitable for
 * partial-success event processing.</p>
 *
 * <p>Design notes:
 * <ul>
 *   <li>Pure Java, deterministic, and unit-testable (no OSGi, no Liferay dependencies).</li>
 *   <li>Validation is non-throwing by default; parsing errors are translated into validation errors.</li>
 *   <li>Timestamp parsing is strict (non-lenient) to prevent ambiguous dates.</li>
 * </ul>
 * </p>
 *
 * Mandatory rules (strict):
 * <ul>
 *   <li>wrapper required</li>
 *   <li>events[] required and not empty</li>
 *   <li>notificationId required (GUID)</li>
 *   <li>eventType required: published | unpublished | changed | inactive</li>
 *   <li>timestamp required: dd/MM/yyyy HH:mm:ss</li>
 *   <li>courses[] required and not empty</li>
 *   <li>each courseCode required (trimmed, length &lt;= 20)</li>
 *   <li>courseType required and must be TMS</li>
 * </ul>
 *
 * Conditional rules:
 * <ul>
 *   <li>changeFrom[] required when eventType=changed</li>
 *   <li>changeFrom values must be within the allowed enum set</li>
 * </ul>
 *
 * @author @akshaygawande
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

    /**
     * Strict timestamp parser. Not thread-safe; validator instances are expected to be request-scoped.
     */
    private final SimpleDateFormat tsFormat;

    public CourseNotificationRequestValidator() {
        this(TS_PATTERN);
    }

    public CourseNotificationRequestValidator(String timestampPattern) {
        tsFormat = new SimpleDateFormat(timestampPattern, Locale.ENGLISH);
        tsFormat.setLenient(false);
    }

    /**
     * Validates the complete wrapper request.
     *
     * <p><b>Business purpose:</b> Ensures the notification payload is structurally complete before
     * any course-level processing begins.</p>
     *
     * <p><b>Technical purpose:</b> Validates wrapper and delegates to per-event validation.</p>
     *
     * <p><b>Inputs/Invariants:</b> {@code wrapper} may be null.</p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Audit behavior:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Returns {@link ValidationResult#ok()} when valid; otherwise a failed
     * result containing one or more {@link EventError} entries.</p>
     */
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
     *
     * <p><b>Business purpose:</b> Allows individual events to be accepted or rejected independently.</p>
     *
     * <p><b>Technical purpose:</b> Exposes deterministic event-level validation.</p>
     *
     * <p><b>Inputs/Invariants:</b> {@code e} may be null.</p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Audit behavior:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Returns an empty list when valid; otherwise a list of errors.</p>
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

        // Even if the error is header-level, try to surface courseCode for ops/debugging.
        final String derivedCourseCode = deriveFirstCourseCode(e);

        List<EventError> errs = new ArrayList<>();

        if (isBlank(notificationId)) {
            errs.add(new EventError("", derivedCourseCode, "notificationId is required"));
        } else if (!GUID.matcher(notificationId).matches()) {
            errs.add(new EventError(notificationId, derivedCourseCode, "notificationId must be a valid GUID"));
        }

        if (isBlank(eventType)) {
            errs.add(new EventError(notificationIdOrEmpty(notificationId), derivedCourseCode, "eventType is required"));
        } else if (!EVENT_TYPES.contains(eventType)) {
            errs.add(new EventError(notificationIdOrEmpty(notificationId), derivedCourseCode,
                    "eventType must be one of: published, unpublished, changed, inactive"));
        }

        if (isBlank(timestamp)) {
            errs.add(new EventError(notificationIdOrEmpty(notificationId), derivedCourseCode, "timestamp is required"));
        } else if (!isValidTimestamp(timestamp)) {
            errs.add(new EventError(notificationIdOrEmpty(notificationId), derivedCourseCode,
                    "timestamp must match format " + TS_PATTERN));
        }

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
            this.eventErrors = (eventErrors == null)
                    ? Collections.<EventError>emptyList()
                    : eventErrors;
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
