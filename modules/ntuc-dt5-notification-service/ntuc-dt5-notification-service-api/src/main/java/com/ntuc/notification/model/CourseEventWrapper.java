package com.ntuc.notification.model;

import java.io.Serializable;
import java.util.List;

/**
 * Pure DTO wrapper for a list of CourseEvent.
 *
 * API-safe: no OSGi annotations, no Liferay services.
 */
public class CourseEventWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<CourseEvent> events;

    public CourseEventWrapper() {
        // default constructor
    }

    public CourseEventWrapper(List<CourseEvent> events) {
        this.events = events;
    }

    public List<CourseEvent> getEvents() {
        return events;
    }

    public void setEvents(List<CourseEvent> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "CourseEventWrapper{events=" + (events == null ? 0 : events.size()) + "}";
    }
}
