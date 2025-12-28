package com.ntuc.notification.audit.internal.search;

import com.ntuc.notification.audit.api.dto.AuditLogSearchRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pure criteria representation derived from {@link AuditLogSearchRequest}.
 *
 * This class contains only simple values suitable for unit testing.
 * The OSGi gateway adapts it to Liferay DynamicQuery restrictions.
 */
public final class AuditLogSearchCriteria {

    public static final class LikeAny {
        private final List<String> fields;
        private final String likeValue;

        public LikeAny(List<String> fields, String likeValue) {
            this.fields = (fields == null) ? Collections.emptyList() : new ArrayList<>(fields);
            this.likeValue = likeValue;
        }

        public List<String> getFields() { return Collections.unmodifiableList(fields); }
        public String getLikeValue() { return likeValue; }
    }

    private String severity;
    private String status;
    private String category;
    private String step;

    private String correlationIdEq;
    private String jobRunIdEq;
    private String courseCodeEq;

    // NEW: exact match by primary key
    private Long auditLogIdEq;

    private Long ntucDTIdEq;

    private Long fromTimeMsGe;
    private Long toTimeMsLe;

    private LikeAny globalSearch;

    private AuditLogSearchRequest.SortColumn sortColumn = AuditLogSearchRequest.SortColumn.TIME;
    private AuditLogSearchRequest.SortDir sortDir = AuditLogSearchRequest.SortDir.DESC;

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStep() { return step; }
    public void setStep(String step) { this.step = step; }

    public String getCorrelationIdEq() { return correlationIdEq; }
    public void setCorrelationIdEq(String correlationIdEq) { this.correlationIdEq = correlationIdEq; }

    public String getJobRunIdEq() { return jobRunIdEq; }
    public void setJobRunIdEq(String jobRunIdEq) { this.jobRunIdEq = jobRunIdEq; }

    public String getCourseCodeEq() { return courseCodeEq; }
    public void setCourseCodeEq(String courseCodeEq) { this.courseCodeEq = courseCodeEq; }

    public Long getAuditLogIdEq() { return auditLogIdEq; }
    public void setAuditLogIdEq(Long auditLogIdEq) { this.auditLogIdEq = auditLogIdEq; }

    public Long getNtucDTIdEq() { return ntucDTIdEq; }
    public void setNtucDTIdEq(Long ntucDTIdEq) { this.ntucDTIdEq = ntucDTIdEq; }

    public Long getFromTimeMsGe() { return fromTimeMsGe; }
    public void setFromTimeMsGe(Long fromTimeMsGe) { this.fromTimeMsGe = fromTimeMsGe; }

    public Long getToTimeMsLe() { return toTimeMsLe; }
    public void setToTimeMsLe(Long toTimeMsLe) { this.toTimeMsLe = toTimeMsLe; }

    public LikeAny getGlobalSearch() { return globalSearch; }
    public void setGlobalSearch(LikeAny globalSearch) { this.globalSearch = globalSearch; }

    public AuditLogSearchRequest.SortColumn getSortColumn() { return sortColumn; }
    public void setSortColumn(AuditLogSearchRequest.SortColumn sortColumn) {
        this.sortColumn = (sortColumn == null) ? AuditLogSearchRequest.SortColumn.TIME : sortColumn;
    }

    public AuditLogSearchRequest.SortDir getSortDir() { return sortDir; }
    public void setSortDir(AuditLogSearchRequest.SortDir sortDir) {
        this.sortDir = (sortDir == null) ? AuditLogSearchRequest.SortDir.DESC : sortDir;
    }
}
