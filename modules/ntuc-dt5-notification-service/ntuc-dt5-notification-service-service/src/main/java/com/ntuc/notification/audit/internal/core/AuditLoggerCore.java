package com.ntuc.notification.audit.internal.core;

import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.audit.internal.AuditActionFilter;

import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

/**
 * Core audit logic.
 *
 * PURE JAVA:
 * - No OSGi
 * - No Liferay services
 * - No ServiceBuilder entities
 *
 * Fully JUnit-testable.
 */
public class AuditLoggerCore {

    private static final int MAX_MSG_LEN = 75;
    private static final int MAX_JSON_LEN = 800_000;
    private static final byte[] EMPTY_JSON_BYTES =
            "{}".getBytes(StandardCharsets.UTF_8);

    private final AuditLogRepository repository;
    private final PkGenerator pkGenerator;
    private final AuditClock clock;
    private final JsonSerializer json;
    private final AuditActionFilter actionFilter;

    public AuditLoggerCore(
            AuditLogRepository repository,
            PkGenerator pkGenerator,
            AuditClock clock,
            JsonSerializer json,
            AuditActionFilter actionFilter) {

        if (repository == null ||
                pkGenerator == null ||
                clock == null ||
                json == null ||
                actionFilter == null) {

            throw new IllegalArgumentException(
                    "All AuditLoggerCore dependencies are mandatory");
        }

        this.repository = repository;
        this.pkGenerator = pkGenerator;
        this.clock = clock;
        this.json = json;
        this.actionFilter = actionFilter;
    }

    // ---------------------------------------------------------------------
    // Public API
    // ---------------------------------------------------------------------

    public void log(
            String action,
            String description,
            Object changedFields,
            Object additionalInfo) {

        log(null, 0L, action, description, changedFields, additionalInfo, null);
    }

    public void log(
            CourseEventContext ctx,
            String action,
            String description,
            Object changedFields,
            Object additionalInfo) {

        log(ctx, 0L, action, description, changedFields, additionalInfo, null);
    }

    public void log(
            CourseEventContext ctx,
            long ntucDTId,
            String action,
            String description,
            Object changedFields,
            Object additionalInfo,
            String correlationId) {

        AuditRecord record = newAuditRecord();

        if (ctx != null) {
            record.setGroupId(ctx.getGroupId());
            record.setCompanyId(ctx.getCompanyId());
            record.setUserId(ctx.getUserId());

            // API-safe: CourseEventContext exposes courseCode directly (no getEvent()).
            record.setCourseCode(
                    safeString(ctx.getCourseCode(), MAX_MSG_LEN));

            // Prefer explicit ntucDTId param if provided; otherwise, take from ctx.
            if (ntucDTId <= 0 && ctx.getNtucDTId() > 0) {
                ntucDTId = ctx.getNtucDTId();
            }
        }

        if (ntucDTId > 0) {
            record.setNtucDTId(ntucDTId);
        }

        fillAndPersist(
                record,
                action,
                description,
                changedFields,
                additionalInfo,
                correlationId);
    }

    public void logExplicit(
            long groupId,
            long companyId,
            long userId,
            String courseCode,
            long ntucDTId,
            String action,
            String description,
            Object changedFields,
            Object additionalInfo,
            String correlationId) {

        AuditRecord record = newAuditRecord();

        if (groupId > 0) record.setGroupId(groupId);
        if (companyId > 0) record.setCompanyId(companyId);
        if (userId > 0) record.setUserId(userId);
        if (courseCode != null) {
            record.setCourseCode(
                    safeString(courseCode, MAX_MSG_LEN));
        }
        if (ntucDTId > 0) record.setNtucDTId(ntucDTId);

        fillAndPersist(
                record,
                action,
                description,
                changedFields,
                additionalInfo,
                correlationId);
    }

    // ---------------------------------------------------------------------
    // Core logic
    // ---------------------------------------------------------------------

    private void fillAndPersist(
            AuditRecord record,
            String action,
            String description,
            Object changedFields,
            Object additionalInfo,
            String correlationId) {

        record.setAction(safeString(action, MAX_MSG_LEN));
        record.setDescription(safeString(description, MAX_MSG_LEN));
        record.setTimestamp(clock.now());

        String changedJson = trimJson(json.toJson(changedFields));
        String detailJson = trimJson(json.toJson(additionalInfo));
        detailJson = appendCorrelationId(detailJson, correlationId);

        try {
            record.setChangedFieldsJson(toBlobOrEmpty(changedJson));
            record.setAdditionalInfo(toBlobOrEmpty(detailJson));
        } catch (SQLException e) {
            // Absolute safety: never leave blobs null
            try {
                record.setChangedFieldsJson(
                        new SerialBlob(EMPTY_JSON_BYTES));
                record.setAdditionalInfo(
                        new SerialBlob(EMPTY_JSON_BYTES));
            } catch (SQLException ignore) {
                // nothing more we can do
            }
        }

        if (actionFilter.isLoggable(action)) {
            repository.persist(record);
        }
    }

    private AuditRecord newAuditRecord() {
        AuditRecord record = new AuditRecord();
        record.setAuditLogId(
                pkGenerator.nextPk("AuditLog"));
        return record;
    }

    // ---------------------------------------------------------------------
    // Utilities
    // ---------------------------------------------------------------------

    private static String trimJson(String s) {
        if (s == null) return null;
        return s.length() > MAX_JSON_LEN
                ? s.substring(0, MAX_JSON_LEN)
                : s;
    }

    private static Blob toBlobOrEmpty(String s) throws SQLException {
        if (s == null) {
            return new SerialBlob(EMPTY_JSON_BYTES);
        }
        return new SerialBlob(
                s.getBytes(StandardCharsets.UTF_8));
    }

    private static String appendCorrelationId(
            String detailJson,
            String correlationId) {

        if (correlationId == null) {
            return detailJson;
        }

        String trimmed =
                detailJson == null ? "" : detailJson.trim();

        if (trimmed.isEmpty() || "{}".equals(trimmed)) {
            return "{\"corrId\":\"" + safe(correlationId) + "\"}";
        }

        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            return trimmed.substring(0, trimmed.length() - 1)
                    + ",\"corrId\":\"" + safe(correlationId) + "\"}";
        }

        return "{\"detail\":\"" + safe(trimmed)
                + "\",\"corrId\":\"" + safe(correlationId) + "\"}";
    }

    private static String safeString(String s, int max) {
        if (s == null) return null;
        return s.length() > max ? s.substring(0, max) : s;
    }

    private static String safe(String s) {
        return s == null
                ? ""
                : s.replace("\"", "'").replace("\\", "\\\\");
    }
}
