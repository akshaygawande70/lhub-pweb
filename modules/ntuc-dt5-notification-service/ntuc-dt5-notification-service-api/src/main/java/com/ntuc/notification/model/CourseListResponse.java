package com.ntuc.notification.model;

import java.util.List;

/**
 * DTO representing a course list response with optional error details.
 *
 * Pure API contract:
 * - No JSON annotations
 * - No framework dependencies
 */
public class CourseListResponse {

    private List<CourseResponseWrapper> courses;
    private Error error;

    public List<CourseResponseWrapper> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseResponseWrapper> courses) {
        this.courses = courses;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "CourseListResponse{" +
                "courses=" + courses +
                ", error=" + error +
                '}';
    }

    public static class Error {

        private String code;
        private String message;
        private List<ErrorDetail> errors;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<ErrorDetail> getErrors() {
            return errors;
        }

        public void setErrors(List<ErrorDetail> errors) {
            this.errors = errors;
        }

        @Override
        public String toString() {
            return "Error{" +
                    "code='" + code + '\'' +
                    ", message='" + message + '\'' +
                    ", errors=" + errors +
                    '}';
        }

        public static class ErrorDetail {

            private String courseCode;
            private String message;

            public String getCourseCode() {
                return courseCode;
            }

            public void setCourseCode(String courseCode) {
                this.courseCode = courseCode;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            @Override
            public String toString() {
                return "ErrorDetail{" +
                        "courseCode='" + courseCode + '\'' +
                        ", message='" + message + '\'' +
                        '}';
            }
        }
    }
}
