package com.ntuc.notification.audit.internal.core;

import java.sql.Blob;
import java.util.Date;

/**
 * Plain audit record used by AuditLoggerCore.
 * No ServiceBuilder dependency.
 */
public class AuditRecord {

    private long auditLogId;
    private long groupId;
    private long companyId;
    private long userId;
    private long ntucDTId;

    private String courseCode;
    private String action;
    private String description;

    private Date timestamp;
    private Blob changedFieldsJson;
    private Blob additionalInfo;

    // ---------- getters / setters ----------

    public long getAuditLogId() {
        return auditLogId;
    }

    public void setAuditLogId(long auditLogId) {
        this.auditLogId = auditLogId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getNtucDTId() {
        return ntucDTId;
    }

    public void setNtucDTId(long ntucDTId) {
        this.ntucDTId = ntucDTId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Blob getChangedFieldsJson() {
        return changedFieldsJson;
    }

    public void setChangedFieldsJson(Blob changedFieldsJson) {
        this.changedFieldsJson = changedFieldsJson;
    }

    public Blob getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Blob additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
