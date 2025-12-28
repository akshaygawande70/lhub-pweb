package com.ntuc.notification.audit.api.dto;

import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import java.io.Serializable;

/**
 * One row in the audit log DataTable.
 *
 * IMPORTANT:
 * - Do NOT include secrets, raw payloads, or full stack traces here.
 * - Drill-down must call a dedicated endpoint (by auditId/correlationId).
 */
public class AuditLogRowDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long auditId;

    private long startTimeMs;
    private long endTimeMs;
    private long durationMs;

    private AuditSeverity severity;
    private AuditStatus status;
    private AuditCategory category;
    private AuditStep step;

    private String message;

    private String correlationId;
    private String jobRunId;
    private String courseCode;

    // NEW: DT business key visible in grid + filterable
    private long ntucDTId;

    private String errorCode;
    private String errorMessage;
    private String exceptionClass;

    public long getAuditId() { return auditId; }
    public void setAuditId(long auditId) { this.auditId = auditId; }

    public long getStartTimeMs() { return startTimeMs; }
    public void setStartTimeMs(long startTimeMs) { this.startTimeMs = startTimeMs; }

    public long getEndTimeMs() { return endTimeMs; }
    public void setEndTimeMs(long endTimeMs) { this.endTimeMs = endTimeMs; }

    public long getDurationMs() { return durationMs; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }

    public AuditSeverity getSeverity() { return severity; }
    public void setSeverity(AuditSeverity severity) { this.severity = severity; }

    public AuditStatus getStatus() { return status; }
    public void setStatus(AuditStatus status) { this.status = status; }

    public AuditCategory getCategory() { return category; }
    public void setCategory(AuditCategory category) { this.category = category; }

    public AuditStep getStep() { return step; }
    public void setStep(AuditStep step) { this.step = step; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public String getJobRunId() { return jobRunId; }
    public void setJobRunId(String jobRunId) { this.jobRunId = jobRunId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public long getNtucDTId() { return ntucDTId; }
    public void setNtucDTId(long ntucDTId) { this.ntucDTId = ntucDTId; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getExceptionClass() { return exceptionClass; }
    public void setExceptionClass(String exceptionClass) { this.exceptionClass = exceptionClass; }
}
