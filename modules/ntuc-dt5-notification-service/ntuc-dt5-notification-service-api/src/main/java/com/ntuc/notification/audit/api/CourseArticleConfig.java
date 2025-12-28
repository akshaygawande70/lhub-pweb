package com.ntuc.notification.audit.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * Pure DTO carrying JournalArticle-related configuration needed by services.
 *
 * API-safe:
 * - No OSGi annotations
 * - No Liferay services/models
 * - Immutable
 */
public class CourseArticleConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long folderId;
    private final String ddmStructureKey;
    private final String ddmTemplateKey;
    private final String defaultLanguageId;
    private final String eventType;

    /**
     * Optional DDM field type information.
     *
     * Key   : Liferay field path (e.g. "courseTitle", "schedule.duration.hours")
     * Value : DDM type (e.g. "text", "text_area", "fieldset")
     *
     * API-safe: plain Map<String, String>
     */
    private final Map<String, String> fieldsTypeInfo;

    // ---------------------------------------------------------------------
    // Backward-compatible constructor (no field type info)
    // ---------------------------------------------------------------------

    public CourseArticleConfig(
            long folderId,
            String ddmStructureKey,
            String ddmTemplateKey,
            String defaultLanguageId,
            String eventType) {

        this(
            folderId,
            ddmStructureKey,
            ddmTemplateKey,
            defaultLanguageId,
            eventType,
            Collections.<String, String>emptyMap()
        );
    }

    // ---------------------------------------------------------------------
    // Primary constructor
    // ---------------------------------------------------------------------

    public CourseArticleConfig(
            long folderId,
            String ddmStructureKey,
            String ddmTemplateKey,
            String defaultLanguageId,
            String eventType,
            Map<String, String> fieldsTypeInfo) {

        this.folderId = folderId;
        this.ddmStructureKey = ddmStructureKey;
        this.ddmTemplateKey = ddmTemplateKey;
        this.defaultLanguageId = defaultLanguageId;
        this.eventType = eventType;
        this.fieldsTypeInfo = (fieldsTypeInfo == null)
                ? Collections.<String, String>emptyMap()
                : Collections.unmodifiableMap(fieldsTypeInfo);
    }

    public long getFolderId() {
        return folderId;
    }

    public String getDdmStructureKey() {
        return ddmStructureKey;
    }

    public String getDdmTemplateKey() {
        return ddmTemplateKey;
    }

    public String getDefaultLanguageId() {
        return defaultLanguageId;
    }

    public String getEventType() {
        return eventType;
    }

    /**
     * Returns immutable map of DDM field type info.
     * Never null.
     */
    public Map<String, String> getFieldsTypeInfo() {
        return fieldsTypeInfo;
    }
}
