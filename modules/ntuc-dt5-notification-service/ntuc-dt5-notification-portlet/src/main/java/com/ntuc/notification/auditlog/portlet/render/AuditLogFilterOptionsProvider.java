package com.ntuc.notification.auditlog.portlet.render;

import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides stable dropdown options for Audit Log browsing.
 *
 * UX rule (strict):
 * - Steps MUST be the full enum superset (not "only values present in DB"),
 *   so filters are predictable and shareable links keep working.
 *
 * This class is pure Java (safe for unit tests).
 */
public final class AuditLogFilterOptionsProvider {

    public AuditLogFilterOptionsProvider() {
        // stateless
    }

    public List<String> categories() {
        return enumNames(AuditCategory.values());
    }

    public List<String> statuses() {
        return enumNames(AuditStatus.values());
    }

    public List<String> severities() {
        return enumNames(AuditSeverity.values());
    }

    public List<String> steps() {
        // CRITICAL: Always return the full enum list
        return enumNames(AuditStep.values());
    }

    private static <E extends Enum<E>> List<String> enumNames(E[] values) {
        if (values == null || values.length == 0) {
            return Collections.emptyList();
        }

        List<String> out = new ArrayList<>(values.length);
        for (E e : values) {
            if (e != null) {
                out.add(e.name());
            }
        }

        // Optional: keep stable ordering as declared
        return Collections.unmodifiableList(out);
    }
}
