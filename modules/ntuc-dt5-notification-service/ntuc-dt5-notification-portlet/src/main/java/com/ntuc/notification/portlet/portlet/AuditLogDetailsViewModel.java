package com.ntuc.notification.portlet.portlet;

/**
 * Immutable view model for AuditLog details resource response.
 *
 * IMPORTANT:
 * - Strings are sanitized to non-null.
 * - Blob fields are already converted to UTF-8 text.
 */
public final class AuditLogDetailsViewModel {

    private final long auditLogId;

    private final long ntucDTId;
    private final String courseCode;
    private final String severity;
    private final String status;
    private final String step;
    private final String category;
    private final String message;

    private final String errorCode;
    private final String errorMessage;
    private final String exceptionClass;
    private final String stackTraceHash;

    private final String detailsJson;
    private final String stackTraceTruncated;

    public AuditLogDetailsViewModel(
        long auditLogId,
        long ntucDTId,
        String courseCode,
        String severity,
        String status,
        String step,
        String category,
        String message,
        String errorCode,
        String errorMessage,
        String exceptionClass,
        String stackTraceHash,
        String detailsJson,
        String stackTraceTruncated
    ) {
        this.auditLogId = auditLogId;

        this.ntucDTId = ntucDTId;
        this.courseCode = nn(courseCode);
        this.severity = nn(severity);
        this.status = nn(status);
        this.step = nn(step);
        this.category = nn(category);
        this.message = nn(message);

        this.errorCode = nn(errorCode);
        this.errorMessage = nn(errorMessage);
        this.exceptionClass = nn(exceptionClass);
        this.stackTraceHash = nn(stackTraceHash);

        this.detailsJson = nn(detailsJson);
        this.stackTraceTruncated = nn(stackTraceTruncated);
    }

    public long getAuditLogId() {
        return auditLogId;
    }

    public long getNtucDTId() {
		return ntucDTId;
	}

	public String getCourseCode() {
        return courseCode;
    }

    public String getSeverity() {
        return severity;
    }

    public String getStatus() {
        return status;
    }

    public String getStep() {
        return step;
    }

    public String getCategory() {
        return category;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public String getStackTraceHash() {
        return stackTraceHash;
    }

    public String getDetailsJson() {
        return detailsJson;
    }

    public String getStackTraceTruncated() {
        return stackTraceTruncated;
    }

    private static String nn(String s) {
        return (s == null) ? "" : s;
    }
}
