package com.ntuc.notification.portlet.portlet;

import java.sql.Blob;

/**
 * Minimal read-only projection of an audit log row for the portlet layer.
 *
 * IMPORTANT:
 * - Portlet must not depend on ServiceBuilder model classes in unit tests.
 * - Production code can adapt AuditLog -> this interface.
 */
public interface AuditLogDetailsSource {

    long getAuditLogId();
    
    long getNtucDTId();
    String getCourseCode();
    String getSeverity();
    String getStatus();
    String getStep();
    String getCategory();
    String getMessage();

    String getErrorCode();
    String getErrorMessage();
    String getExceptionClass();
    String getStackTraceHash();

    Blob getDetailsJson();
    Blob getStackTraceTruncated();
}
