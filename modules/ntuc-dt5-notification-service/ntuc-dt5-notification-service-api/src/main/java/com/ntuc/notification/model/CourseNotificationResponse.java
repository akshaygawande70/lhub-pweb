package com.ntuc.notification.model;

public class CourseNotificationResponse {

	private String eventType; 
	private String courseCode;
	private String timestamp;
	private String courseType;
	
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	
	
}
