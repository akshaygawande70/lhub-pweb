package com.ntuc.notification.rest.internal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * REST-only DTOs.
 * Keep them in -service (not in -api) unless they are shared across modules.
 */
public class CourseRestDtos {

    public static class ErrorInfo {
        public String notificationId;
        public String courseCode;
        public String code;
        public String message;

        public ErrorInfo(String notificationId, String courseCode, String code, String message) {
            this.notificationId = notificationId;
            this.courseCode = courseCode;
            this.code = code;
            this.message = message;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class FinalResponse {
        public String status;
        public String message;
        public java.util.List<ErrorInfo> errors;

        public FinalResponse(String status, String message, java.util.List<ErrorInfo> errors) {
            this.status = status;
            this.message = message;
            this.errors = (errors == null) ? new java.util.ArrayList<ErrorInfo>() : errors;
        }
    }

    public static class OneTimeLoadRequest {
        public String s3Path; // e.g., s3://bucket/key or s3://bucket/prefix/
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class OneTimeLoadResponse {
        public String status; // SUCCESS / FAILED
        public String message;
        public String correlationId;

        public OneTimeLoadResponse(String status, String message, String correlationId) {
            this.status = status;
            this.message = message;
            this.correlationId = correlationId;
        }
    }

    /**
     * JSON body: { "productCode": "..." }
     */
    public static class ProductCodeRequest {
        public String productCode;
    }

    /**
     * JSON body: { "courseCode": "..." }
     */
    public static class CourseCodeRequest {
        public String courseCode;
    }
}
