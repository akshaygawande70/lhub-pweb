package com.ntuc.notification.audit.api.dto;

import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import java.io.Serializable;

/**
 * Typed search request for server-side DataTables-backed audit log browsing.
 *
 * Contract rules:
 * - PORTLET must translate request params into this DTO (no leaking raw param maps).
 * - SERVICE must treat this DTO as the contract and apply validation/clamping defensively.
 *
 * Search semantics:
 * - auditLogId: exact match (highest priority; if provided, other filters may be ignored).
 * - q: free-text search applied across selected fields (message/errorCode/errorMessage/correlationId/jobRunId/courseCode).
 */
public class AuditLogSearchRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum SortDir { ASC, DESC }

    public enum SortColumn {
        TIME,
        SEVERITY,
        STATUS,
        CATEGORY,
        STEP,
        MESSAGE,
        CORRELATION_ID,
        JOB_RUN_ID,
        COURSE_CODE,
        NTUC_DT_ID,
        AUDIT_ID
    }

    private long companyId;
    private long groupId;

    private int draw;
    private int start;
    private int length;

    private Long auditLogId; // exact PK match
    private Long ntucDTId;   // exact match business key

    private AuditSeverity severity;
    private AuditStatus status;
    private AuditCategory category;
    private AuditStep step;

    private String q;      // free text search
    private Long fromTimeMs;
    private Long toTimeMs;

    private SortColumn sortColumn = SortColumn.TIME;
    private SortDir sortDir = SortDir.DESC;

    public long getCompanyId() { return companyId; }
    public void setCompanyId(long companyId) { this.companyId = companyId; }

    public long getGroupId() { return groupId; }
    public void setGroupId(long groupId) { this.groupId = groupId; }

    public int getDraw() { return draw; }
    public void setDraw(int draw) { this.draw = draw; }

    public int getStart() { return start; }
    public void setStart(int start) { this.start = start; }

    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }

    public Long getAuditLogId() { return auditLogId; }
    public void setAuditLogId(Long auditLogId) { this.auditLogId = auditLogId; }

    public Long getNtucDTId() { return ntucDTId; }
    public void setNtucDTId(Long ntucDTId) { this.ntucDTId = ntucDTId; }

    public AuditSeverity getSeverity() { return severity; }
    public void setSeverity(AuditSeverity severity) { this.severity = severity; }

    public AuditStatus getStatus() { return status; }
    public void setStatus(AuditStatus status) { this.status = status; }

    public AuditCategory getCategory() { return category; }
    public void setCategory(AuditCategory category) { this.category = category; }

    public AuditStep getStep() { return step; }
    public void setStep(AuditStep step) { this.step = step; }

    public String getQ() { return q; }
    public void setQ(String q) { this.q = q; }

    public Long getFromTimeMs() { return fromTimeMs; }
    public void setFromTimeMs(Long fromTimeMs) { this.fromTimeMs = fromTimeMs; }

    public Long getToTimeMs() { return toTimeMs; }
    public void setToTimeMs(Long toTimeMs) { this.toTimeMs = toTimeMs; }

    public SortColumn getSortColumn() { return sortColumn; }
    public void setSortColumn(SortColumn sortColumn) {
        this.sortColumn = (sortColumn == null) ? SortColumn.TIME : sortColumn;
    }

    public SortDir getSortDir() { return sortDir; }
    public void setSortDir(SortDir sortDir) {
        this.sortDir = (sortDir == null) ? SortDir.DESC : sortDir;
    }
}
