package com.ntuc.notification.emailtemplate.api;

/**
 * Admin service for saving system-scoped DT5 email templates.
 *
 * Audit rules:
 * - Must write STARTED + SUCCESS/FAILED audit events.
 * - Must allow ops to reconstruct changes from audit DB alone.
 */
public interface Dt5EmailTemplateAdminService {

    void saveSystemTemplates(Dt5EmailTemplateUpdateRequest request);
}
