package com.ntuc.notification.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Event DTO used across modules (service/portlet/rest).
 *
 * Pure API object:
 * - No JSON parsing
 * - No DB bridge logic
 * - No references to ServiceBuilder entities
 */
public class CourseEvent {

    private String notificationId;
    private String eventType;
    private List<String> changeFrom;
    private List<Course> courses;
    private String courseCodeSingle;
    private String courseTypeSingle;
    private String timestamp;

    // Keep only lightweight references if needed (ids), not entities.
    private long ntucSBId;
    
    @JsonIgnore // not part of CLS payload; internal runtime only
    private NtucSB ntucSB;

    @Override
    public String toString() {
        return "CourseEvent [notificationId=" + notificationId
                + ", eventType=" + eventType
                + ", changeFrom=" + changeFrom
                + ", courses=" + courses
                + ", courseCodeSingle=" + courseCodeSingle
                + ", courseTypeSingle=" + courseTypeSingle
                + ", timestamp=" + timestamp
                + ", ntucSBId=" + ntucSBId 
                + ", ntucSB=" + ntucSB + "]";
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public List<String> getChangeFrom() {
        return changeFrom;
    }

    public void setChangeFrom(List<String> changeFrom) {
        this.changeFrom = changeFrom;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public String getCourseCodeSingle() {
        return courseCodeSingle;
    }

    public void setCourseCodeSingle(String courseCodeSingle) {
        this.courseCodeSingle = courseCodeSingle;
    }

    public String getCourseTypeSingle() {
        return courseTypeSingle;
    }

    public void setCourseTypeSingle(String courseTypeSingle) {
        this.courseTypeSingle = courseTypeSingle;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getNtucSBId() {
        return ntucSBId;
    }

    public void setNtucSBId(long ntucSBId) {
        this.ntucSBId = ntucSBId;
    }
    
    public NtucSB getNtucSB() {
        return ntucSB;
    }

    public void setNtucSB(NtucSB ntucSB) {
        this.ntucSB = ntucSB;
    }

    public static class Course {
        private String courseCode;
        private String courseType;

        @Override
        public String toString() {
            return "Course [courseCode=" + courseCode + ", courseType=" + courseType + "]";
        }

        public String getCourseCode() {
            return courseCode;
        }

        public void setCourseCode(String courseCode) {
            this.courseCode = courseCode;
        }

        public String getCourseType() {
            return courseType;
        }

        public void setCourseType(String courseType) {
            this.courseType = courseType;
        }
    }
}
