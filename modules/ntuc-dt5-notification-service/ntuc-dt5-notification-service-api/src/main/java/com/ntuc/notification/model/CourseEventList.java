package com.ntuc.notification.model;

import java.util.List;

/**
 * DTO representing a list of CourseEvent objects.
 *
 * Pure API contract:
 * - No business logic
 * - No runtime dependencies
 */
public class CourseEventList {

    private List<CourseEvent> events;

    public List<CourseEvent> getEvents() {
        return events;
    }

    public void setEvents(List<CourseEvent> events) {
        this.events = events;
    }
}
