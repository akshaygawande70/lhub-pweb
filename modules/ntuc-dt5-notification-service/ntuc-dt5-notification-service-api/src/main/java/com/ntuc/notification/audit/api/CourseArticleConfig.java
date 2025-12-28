package com.ntuc.notification.audit.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * Immutable API DTO carrying JournalArticle-related configuration required by
 * notification and content processing services.
 *
 * <p><strong>Business purpose:</strong>
 * Encapsulates all article creation and update parameters so that downstream
 * workflows can render, persist, and audit course-related content consistently.</p>
 *
 * <p><strong>Technical purpose:</strong>
 * Provides an API-safe, serialization-friendly configuration object with no
 * dependency on OSGi, Liferay services, or platform-specific models.</p>
 *
 * <p>Design guarantees:</p>
 * <ul>
 *   <li>Immutable once constructed</li>
 *   <li>Thread-safe</li>
 *   <li>Serializable for async and audit propagation</li>
 * </ul>
 *
 * @author @akshaygawande
 */
public class CourseArticleConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Target JournalArticle folder ID */
    private final long folderId;

    /** DDM structure key used for article creation */
    private final String ddmStructureKey;

    /** DDM template key used for article rendering */
    private final String ddmTemplateKey;

    /** Default language ID for article content */
    private final String defaultLanguageId;

    /** Logical event type associated with this article configuration */
    private final String eventType;

    /**
     * Optional DDM field type metadata.
     *
     * <p>Key   : Liferay field path (e.g. {@code courseTitle},
     * {@code schedule.duration.hours})</p>
     * <p>Value : DDM field type (e.g. {@code text}, {@code text_area},
     * {@code fieldset})</p>
     *
     * <p>This map is immutable and never {@code null}.</p>
     */
    private final Map<String, String> fieldsTypeInfo;

    // ---------------------------------------------------------------------
    // Backward-compatible constructor (no field type info)
    // ---------------------------------------------------------------------

    /**
     * Constructs an article configuration without explicit field type metadata.
     *
     * <p><strong>Business purpose:</strong>
     * Supports legacy workflows where field typing is derived implicitly.</p>
     *
     * <p><strong>Technical purpose:</strong>
     * Delegates to the primary constructor with an empty, immutable map.</p>
     *
     * @param folderId target folder ID
     * @param ddmStructureKey structure key
     * @param ddmTemplateKey template key
     * @param defaultLanguageId default language ID
     * @param eventType logical event type
     *
     * @return fully-initialized immutable configuration
     */
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

    /**
     * Constructs an article configuration with optional DDM field type metadata.
     *
     * <p><strong>Business purpose:</strong>
     * Enables precise rendering and processing of structured article content.</p>
     *
     * <p><strong>Technical purpose:</strong>
     * Normalizes and defensively copies field metadata into an immutable map.</p>
     *
     * @param folderId target folder ID
     * @param ddmStructureKey structure key
     * @param ddmTemplateKey template key
     * @param defaultLanguageId default language ID
     * @param eventType logical event type
     * @param fieldsTypeInfo optional field type metadata (may be {@code null})
     *
     * @return fully-initialized immutable configuration
     */
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

    /**
     * Returns the target folder ID.
     *
     * @return folder ID
     */
    public long getFolderId() {
        return folderId;
    }

    /**
     * Returns the DDM structure key.
     *
     * @return structure key
     */
    public String getDdmStructureKey() {
        return ddmStructureKey;
    }

    /**
     * Returns the DDM template key.
     *
     * @return template key
     */
    public String getDdmTemplateKey() {
        return ddmTemplateKey;
    }

    /**
     * Returns the default language ID.
     *
     * @return language ID
     */
    public String getDefaultLanguageId() {
        return defaultLanguageId;
    }

    /**
     * Returns the logical event type.
     *
     * @return event type identifier
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Returns immutable DDM field type metadata.
     *
     * <p>Never returns {@code null}.</p>
     *
     * @return immutable field type map
     */
    public Map<String, String> getFieldsTypeInfo() {
        return fieldsTypeInfo;
    }
}
