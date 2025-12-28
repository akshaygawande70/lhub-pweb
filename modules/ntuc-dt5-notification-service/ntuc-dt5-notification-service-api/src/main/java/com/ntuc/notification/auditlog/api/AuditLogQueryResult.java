package com.ntuc.notification.auditlog.api;

import com.ntuc.notification.model.AuditLog;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Query result wrapper for the Audit Log Portlet.
 *
 * <p>Contains:
 * - page of AuditLog rows
 * - total count
 * - distinct filter values for dropdowns (category/status/severity/step)</p>
 */
public final class AuditLogQueryResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<AuditLog> rows;
    private final int total;

    private final List<String> categories;
    private final List<String> statuses;
    private final List<String> severities;
    private final List<String> steps;

    public AuditLogQueryResult(
        List<AuditLog> rows,
        int total,
        List<String> categories,
        List<String> statuses,
        List<String> severities,
        List<String> steps
    ) {
        this.rows = rows == null ? Collections.emptyList() : rows;
        this.total = total;

        this.categories = categories == null ? Collections.emptyList() : categories;
        this.statuses = statuses == null ? Collections.emptyList() : statuses;
        this.severities = severities == null ? Collections.emptyList() : severities;
        this.steps = steps == null ? Collections.emptyList() : steps;
    }

    public List<AuditLog> getRows() { return rows; }
    public int getTotal() { return total; }

    public List<String> getCategories() { return categories; }
    public List<String> getStatuses() { return statuses; }
    public List<String> getSeverities() { return severities; }
    public List<String> getSteps() { return steps; }
}
