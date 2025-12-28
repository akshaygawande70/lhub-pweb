package com.ntuc.notification.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.constants.FieldMappingConstants;
import com.ntuc.notification.constants.ParameterGroupKeys;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.model.CourseResponse;
import com.ntuc.notification.model.ScheduleResponse;
import com.ntuc.notification.service.XmlContentBuilder;

import org.jsoup.Jsoup;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Generates, updates, and extracts Liferay JournalArticle XML content using CLS payloads.
 *
 * <p><b>Purpose (Business):</b> Converts course and schedule data fetched from CLS into the XML format
 * expected by Liferay DDM-backed JournalArticles, enabling consistent content publishing.</p>
 *
 * <p><b>Purpose (Technical):</b> Builds a <code>&lt;root&gt;</code> XML document with nested
 * <code>&lt;dynamic-element&gt;</code> nodes, applying field mappings, DDM type resolution, localization,
 * and HTML/JSON sanitization.</p>
 *
 * <p><b>Notes:</b> This is an OSGi DS component and is not designed for direct plain JUnit testing
 * outside an OSGi container. Extracted pure helpers can be unit-tested.</p>
 *
 * @author @akshaygawande
 */
@Component(service = XmlContentBuilder.class)
public class XmlContentBuilderImpl implements XmlContentBuilder {

    private static final Log _log = LogFactoryUtil.getLog(XmlContentBuilderImpl.class);

    @Reference
    private ParameterGroupKeys parameterGroupKeys;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Cached parameter values loaded during DS activation.
     * Values include field lists for critical/non-critical/batch/schedule processing.
     */
    private Map<ParameterKeyEnum, Object> paramValues;

    /**
     * Initializes cached parameter values used by this builder.
     *
     * <p><b>Purpose (Business):</b> Ensures field selection rules configured by administrators are
     * available before processing begins.</p>
     *
     * <p><b>Purpose (Technical):</b> Loads all parameters into memory once to avoid repeated calls to the
     * parameter service during XML builds.</p>
     *
     * <p><b>Inputs/Invariants:</b> Parameter service may be unavailable during startup; failures are handled
     * by falling back to an empty map.</p>
     *
     * <p><b>Side effects:</b> Populates {@link #paramValues}; logs warnings on failure.</p>
     *
     * <p><b>Return semantics:</b> None.</p>
     */
    @Activate
    protected void activate() {
        try {
            paramValues = parameterGroupKeys.getAllParameterValues();
        }
        catch (Throwable t) {
            _log.warn("Failed to preload parameter values in XmlContentBuilderImpl", t);
            paramValues = new HashMap<ParameterKeyEnum, Object>();
        }
    }

    // ---------------------------------------------------------------------
    // Public APIs — Course content
    // ---------------------------------------------------------------------

    /**
     * Builds JournalArticle XML for the configured set of critical course fields.
     *
     * <p><b>Purpose (Business):</b> Produces the authoritative subset of course content considered essential
     * for publishing/updates.</p>
     *
     * <p><b>Purpose (Technical):</b> Resolves the field list from {@link ParameterKeyEnum#CLS_FIELD_CRITICAL}
     * and delegates to {@link #buildJournalContent(CourseEventContext, CourseResponse, String[])}.</p>
     *
     * <p><b>Inputs/Invariants:</b> Uses cached parameters loaded at activation; if unavailable, processes an
     * empty field set.</p>
     *
     * <p><b>Side effects:</b> Logs informational messages.</p>
     *
     * <p><b>Return semantics:</b> Returns generated XML; returns an empty root XML if payload is missing or
     * an error occurs.</p>
     */
    @Override
    public String processCriticalFields(CourseEventContext eventCtx, CourseResponse courseResponse) {
        _log.info("Generating critical fields XML");
        String[] fields = getParamArray(paramValues, ParameterKeyEnum.CLS_FIELD_CRITICAL);
        return buildJournalContent(eventCtx, courseResponse, fields);
    }

    /**
     * Builds JournalArticle XML for the configured set of non-critical course fields.
     *
     * <p><b>Purpose (Business):</b> Updates supplementary content that is not required for core publishing
     * but enhances the course page.</p>
     *
     * <p><b>Purpose (Technical):</b> Resolves the field list from {@link ParameterKeyEnum#CLS_FIELD_NONCRITICAL}
     * and delegates to {@link #buildJournalContent(CourseEventContext, CourseResponse, String[])}.</p>
     *
     * <p><b>Inputs/Invariants:</b> Uses cached parameters loaded at activation; if unavailable, processes an
     * empty field set.</p>
     *
     * <p><b>Side effects:</b> Logs informational messages.</p>
     *
     * <p><b>Return semantics:</b> Returns generated XML; returns an empty root XML if payload is missing or
     * an error occurs.</p>
     */
    @Override
    public String buildNonCriticalFields(CourseEventContext eventCtx, CourseResponse courseResponse) {
        _log.info("Generating non-critical fields XML");
        String[] fields = getParamArray(paramValues, ParameterKeyEnum.CLS_FIELD_NONCRITICAL);
        return buildJournalContent(eventCtx, courseResponse, fields);
    }

    /**
     * Builds JournalArticle XML for the configured set of batch-related course fields.
     *
     * <p><b>Purpose (Business):</b> Enables publishing/updating of course attributes that are batch-oriented
     * (operational scheduling and related metadata).</p>
     *
     * <p><b>Purpose (Technical):</b> Resolves the field list from {@link ParameterKeyEnum#CLS_FIELD_BATCH}
     * and delegates to {@link #buildJournalContent(CourseEventContext, CourseResponse, String[])}.</p>
     *
     * <p><b>Inputs/Invariants:</b> Uses cached parameters loaded at activation; if unavailable, processes an
     * empty field set.</p>
     *
     * <p><b>Side effects:</b> Logs informational messages.</p>
     *
     * <p><b>Return semantics:</b> Returns generated XML; returns an empty root XML if payload is missing or
     * an error occurs.</p>
     */
    @Override
    public String buildBatchFields(CourseEventContext eventCtx, CourseResponse courseResponse) {
        _log.info("Generating batch fields XML");
        String[] fields = getParamArray(paramValues, ParameterKeyEnum.CLS_FIELD_BATCH);
        return buildJournalContent(eventCtx, courseResponse, fields);
    }

    /**
     * Builds JournalArticle XML for a caller-provided list of fields (retrigger use case).
     *
     * <p><b>Purpose (Business):</b> Supports selective re-processing when an operator triggers updates for
     * an explicit set of fields.</p>
     *
     * <p><b>Purpose (Technical):</b> Normalizes the provided list and delegates to
     * {@link #buildJournalContent(CourseEventContext, CourseResponse, String[])}.</p>
     *
     * <p><b>Inputs/Invariants:</b> <code>fieldsToProcess</code> may be null; null is treated as an empty list.</p>
     *
     * <p><b>Side effects:</b> Logs informational messages.</p>
     *
     * <p><b>Return semantics:</b> Returns generated XML; returns an empty root XML if payload is missing or
     * an error occurs.</p>
     */
    @Override
    public String processAllRetriggerFields(
            CourseEventContext eventCtx,
            CourseResponse courseResponse,
            String[] fieldsToProcess) {

        _log.info("Generating ALL fields XML");
        String[] safeFields = (fieldsToProcess == null) ? new String[0] : fieldsToProcess;
        return buildJournalContent(eventCtx, courseResponse, safeFields);
    }

    /**
     * Generates JournalArticle XML for course payloads using a mapped field list.
     *
     * <p><b>Purpose (Business):</b> Produces the XML payload used by Liferay to persist course details in a
     * JournalArticle, enabling the course page to render accurate content.</p>
     *
     * <p><b>Purpose (Technical):</b> Converts the CLS body into a <code>Map</code>, resolves localization and
     * DDM field typing from {@link CourseEventContext}, maps each source path to one or more Liferay field paths,
     * and writes <code>dynamic-element</code> nodes with sanitized content.</p>
     *
     * <p><b>Inputs/Invariants:</b>
     * <ul>
     *   <li><code>courseResponse</code> and <code>courseResponse.getBody()</code> must be non-null to build content.</li>
     *   <li><code>fieldsToProcess</code> may be null/blank entries and is normalized.</li>
     *   <li>Field mappings are resolved via {@link FieldMappingConstants}.</li>
     * </ul>
     * </p>
     *
     * <p><b>Side effects:</b> Logs errors and debug output; performs HTML/JSON sanitization.</p>
     *
     * <p><b>Audit behavior:</b> None (audit is handled outside this component).</p>
     *
     * <p><b>Return semantics:</b> Never returns null. Returns a valid XML string; on any failure returns an
     * empty root XML with default locale attributes.</p>
     */
    @Override
    public String buildJournalContent(
            CourseEventContext eventCtx,
            CourseResponse courseResponse,
            String[] fieldsToProcess) {

        try {
            if (courseResponse == null || courseResponse.getBody() == null) {
                _log.warn("buildJournalContent called with null courseResponse/body");
                return buildEmptyRootXml(eventCtx);
            }

            Map<String, Object> courseMap = objectMapper.convertValue(
                    courseResponse.getBody(),
                    new TypeReference<Map<String, Object>>() { }
            );

            final String defaultLang = resolveDefaultLanguageId(eventCtx);
            final String[] availableLangs = resolveAvailableLanguageIds(eventCtx, defaultLang);
            final Map<String, String> fieldsTypeInfo = resolveFieldsTypeInfo(eventCtx);

            Document document = SAXReaderUtil.createDocument();
            Element root = document.addElement("root")
                    .addAttribute("available-locales", String.join(",", availableLangs))
                    .addAttribute("default-locale", defaultLang);

            String[] safeFields = normalizeFields(fieldsToProcess);

            for (String sourceField : safeFields) {
                Object value = FieldMappingConstants.getValueFromPath(courseMap, sourceField);

                List<String> liferayFields = FieldMappingConstants.getLiferayFields(sourceField);
                if (liferayFields == null || liferayFields.isEmpty()) {
                    if (_log.isDebugEnabled()) {
                        _log.debug("No Liferay field mapped to source field: " + sourceField);
                    }
                    continue;
                }

                for (String liferayField : liferayFields) {
                    if (isDdmFieldsetStyleField(liferayField)) {
                        addFieldElementDDM(root, liferayField, value, defaultLang, availableLangs, fieldsTypeInfo);
                    }
                    else {
                        addFieldElement(root, liferayField, value, defaultLang, availableLangs, fieldsTypeInfo);
                    }
                }
            }

            if (_log.isDebugEnabled()) {
                _log.debug("Final generated XML:\n" + document.formattedString());
            }

            return document.formattedString();
        }
        catch (Exception e) {
            _log.error("Error while generating journal content", e);
            return buildEmptyRootXml(eventCtx);
        }
    }

    /**
     * Updates existing JournalArticle XML content with newly computed values, adding missing fields as needed.
     *
     * <p><b>Purpose (Business):</b> Supports incremental updates to an existing article without discarding
     * previously stored fields not included in the current processing set.</p>
     *
     * <p><b>Purpose (Technical):</b> Parses the existing XML, resolves the mapped Liferay fields for each source
     * field, then either updates existing <code>dynamic-element</code> nodes or appends new ones, using DDM typing
     * and sanitization rules.</p>
     *
     * <p><b>Inputs/Invariants:</b>
     * <ul>
     *   <li>If <code>existingContent</code> is blank, falls back to a fresh build.</li>
     *   <li>If <code>courseResponse</code> or its body is null, returns existingContent unchanged.</li>
     *   <li><code>fieldsToProcess</code> is normalized; blank entries are ignored.</li>
     * </ul>
     * </p>
     *
     * <p><b>Side effects:</b> Mutates the in-memory XML document; logs errors/debug output; sanitizes values.</p>
     *
     * <p><b>Audit behavior:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Returns the updated XML string; on failure returns the original
     * <code>existingContent</code> (best-effort update).</p>
     */
    @Override
    public String updateOrAppendJournalContent(
            CourseEventContext eventCtx,
            CourseResponse courseResponse,
            String[] fieldsToProcess,
            String existingContent) {

        try {
            if (existingContent == null || existingContent.trim().isEmpty()) {
                return buildJournalContent(eventCtx, courseResponse, fieldsToProcess);
            }

            if (courseResponse == null || courseResponse.getBody() == null) {
                _log.warn("updateOrAppendJournalContent called with null courseResponse/body; returning existingContent");
                return existingContent;
            }

            Map<String, Object> courseMap = objectMapper.convertValue(
                    courseResponse.getBody(),
                    new TypeReference<Map<String, Object>>() { }
            );

            final String defaultLang = resolveDefaultLanguageId(eventCtx);
            final String[] availableLangs = resolveAvailableLanguageIds(eventCtx, defaultLang);
            final Map<String, String> fieldsTypeInfo = resolveFieldsTypeInfo(eventCtx);

            Document document = SAXReaderUtil.read(existingContent);
            Element root = document.getRootElement();

            String[] safeFields = normalizeFields(fieldsToProcess);

            for (String field : safeFields) {
                Object value = FieldMappingConstants.getValueFromPath(courseMap, field);

                List<String> liferayFields = FieldMappingConstants.getLiferayFields(field);
                if (liferayFields == null || liferayFields.isEmpty()) {
                    _log.info("No Liferay field mapped to source field: " + field);
                    continue;
                }

                for (String liferayField : liferayFields) {
                    Element existing = findDynamicElement(root, liferayField);

                    if (existing != null) {
                        if (_log.isDebugEnabled()) {
                            _log.debug("Updating existing field: " + liferayField);
                        }

                        if (isDdmFieldsetStyleField(liferayField)) {
                            updateDynamicElementDDM(root, value, liferayField, defaultLang, availableLangs, fieldsTypeInfo);
                        }
                        else {
                            updateDynamicElement(existing, value, liferayField, defaultLang, availableLangs, fieldsTypeInfo);
                        }
                    }
                    else {
                        if (_log.isDebugEnabled()) {
                            _log.debug("Adding new field: " + liferayField);
                        }

                        if (isDdmFieldsetStyleField(liferayField)) {
                            addFieldElementDDM(root, liferayField, value, defaultLang, availableLangs, fieldsTypeInfo);
                        }
                        else {
                            addFieldElement(root, liferayField, value, defaultLang, availableLangs, fieldsTypeInfo);
                        }
                    }
                }
            }

            if (_log.isDebugEnabled()) {
                _log.debug("Updated XML content:\n" + document.formattedString());
            }

            return document.formattedString();
        }
        catch (Exception e) {
            _log.error("Error updating journal content", e);
            return existingContent;
        }
    }

    /**
     * Updates or appends schedule fields within an existing JournalArticle XML string.
     *
     * <p><b>Purpose (Business):</b> Refreshes course schedule-related content for display and downstream processing,
     * while preserving unrelated article fields.</p>
     *
     * <p><b>Purpose (Technical):</b> Converts the schedule response to a map, parses existing XML, then updates or
     * adds the mapped schedule fields using DDM typing and sanitization.</p>
     *
     * <p><b>Inputs/Invariants:</b>
     * <ul>
     *   <li>If <code>existingContent</code> is blank, a new schedule XML is generated.</li>
     *   <li><code>fieldsToProcess</code> is normalized before processing.</li>
     * </ul>
     * </p>
     *
     * <p><b>Side effects:</b> Mutates an in-memory XML document; logs errors/debug output; sanitizes values.</p>
     *
     * <p><b>Audit behavior:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Returns updated XML; on failure returns original <code>existingContent</code>.</p>
     */
    @Override
    public String updateOrAppendSchedules(
            CourseEventContext eventCtx,
            ScheduleResponse scheduleResponse,
            String[] fieldsToProcess,
            String existingContent) {

        try {
            if (existingContent == null || existingContent.trim().isEmpty()) {
                return buildJournalContentSchedule(eventCtx, scheduleResponse, fieldsToProcess);
            }

            Map<String, Object> courseMap = objectMapper.convertValue(
                    scheduleResponse,
                    new TypeReference<Map<String, Object>>() { }
            );

            final String defaultLang = resolveDefaultLanguageId(eventCtx);
            final String[] availableLangs = resolveAvailableLanguageIds(eventCtx, defaultLang);
            final Map<String, String> fieldsTypeInfo = resolveFieldsTypeInfo(eventCtx);

            Document document = SAXReaderUtil.read(existingContent);
            Element root = document.getRootElement();

            String[] safeFields = normalizeFields(fieldsToProcess);

            for (String field : safeFields) {
                Object value = FieldMappingConstants.getValueFromPath(courseMap, field);

                List<String> liferayFields = FieldMappingConstants.getLiferayFields(field);
                if (liferayFields == null || liferayFields.isEmpty()) {
                    _log.info("No Liferay field mapped to source field: " + field);
                    continue;
                }

                for (String liferayField : liferayFields) {
                    Element existing = findDynamicElement(root, liferayField);
                    if (existing != null) {
                        if (_log.isDebugEnabled()) {
                            _log.debug("Updating existing field: " + liferayField);
                        }
                        updateDynamicElement(existing, value, liferayField, defaultLang, availableLangs, fieldsTypeInfo);
                    }
                    else {
                        if (_log.isDebugEnabled()) {
                            _log.debug("Adding new field: " + liferayField);
                        }
                        addFieldElement(root, liferayField, value, defaultLang, availableLangs, fieldsTypeInfo);
                    }
                }
            }

            if (_log.isDebugEnabled()) {
                _log.debug("Updated schedules XML content:\n" + document.formattedString());
            }
            return document.formattedString();
        }
        catch (Exception e) {
            _log.error("Error updating schedules XML content", e);
            return existingContent;
        }
    }

    /**
     * Builds JournalArticle XML for the configured set of critical schedule fields.
     *
     * <p><b>Purpose (Business):</b> Ensures key schedule attributes required for displaying and validating
     * course runs are always refreshed as a set.</p>
     *
     * <p><b>Purpose (Technical):</b> Resolves the schedule field list from
     * {@link ParameterKeyEnum#CLS_FIELD_CRITICAL_SCHEDULE} and delegates to the schedule build routine.</p>
     *
     * <p><b>Inputs/Invariants:</b> Uses cached parameters loaded at activation; missing parameters result in
     * an empty field list.</p>
     *
     * <p><b>Side effects:</b> Logs informational messages.</p>
     *
     * <p><b>Return semantics:</b> Returns generated schedule XML; returns an empty root XML on failure.</p>
     */
    @Override
    public String processCriticalFieldsSchedule(CourseEventContext eventCtx, ScheduleResponse scheduleResponse) {
        _log.info("Generating critical schedule fields XML");
        String[] fields = getParamArray(paramValues, ParameterKeyEnum.CLS_FIELD_CRITICAL_SCHEDULE);
        return buildJournalContentSchedule(eventCtx, scheduleResponse, fields);
    }

    /**
     * Builds JournalArticle XML for schedule payloads using a mapped field list.
     *
     * <p><b>Purpose (Business):</b> Converts schedule responses into the XML format required for storing schedule
     * sections of a course article.</p>
     *
     * <p><b>Purpose (Technical):</b> Converts schedule response to a map, creates a new XML document root, then
     * writes mapped fields as <code>dynamic-element</code> nodes with localized content.</p>
     *
     * <p><b>Inputs/Invariants:</b> <code>scheduleResponse</code> may be null; null results in an empty root XML.</p>
     *
     * <p><b>Side effects:</b> Performs HTML/JSON sanitization; logs errors/debug output.</p>
     *
     * <p><b>Audit behavior:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Never returns null. Returns schedule XML; on error returns an empty root XML.</p>
     */
    private String buildJournalContentSchedule(
            CourseEventContext eventCtx,
            ScheduleResponse scheduleResponse,
            String[] fieldsToProcess) {

        try {
            if (scheduleResponse == null) {
                _log.warn("buildJournalContentSchedule called with null scheduleResponse");
                return buildEmptyRootXml(eventCtx);
            }

            Map<String, Object> courseMap = objectMapper.convertValue(
                    scheduleResponse,
                    new TypeReference<Map<String, Object>>() { }
            );

            final String defaultLang = resolveDefaultLanguageId(eventCtx);
            final String[] availableLangs = resolveAvailableLanguageIds(eventCtx, defaultLang);
            final Map<String, String> fieldsTypeInfo = resolveFieldsTypeInfo(eventCtx);

            Document document = SAXReaderUtil.createDocument();
            Element root = document.addElement("root")
                    .addAttribute("available-locales", String.join(",", availableLangs))
                    .addAttribute("default-locale", defaultLang);

            String[] safeFields = normalizeFields(fieldsToProcess);

            for (String sourceField : safeFields) {
                Object value = FieldMappingConstants.getValueFromPath(courseMap, sourceField);

                List<String> liferayFields = FieldMappingConstants.getLiferayFields(sourceField);
                if (liferayFields == null || liferayFields.isEmpty()) {
                    if (_log.isDebugEnabled()) {
                        _log.debug("No Liferay field mapped to source field: " + sourceField);
                    }
                    continue;
                }

                for (String liferayField : liferayFields) {
                    addFieldElement(root, liferayField, value, defaultLang, availableLangs, fieldsTypeInfo);
                }
            }

            if (_log.isDebugEnabled()) {
                _log.debug("Final generated schedule XML:\n" + document.formattedString());
            }

            return document.formattedString();
        }
        catch (Exception e) {
            _log.error("Error while generating schedule journal content", e);
            return buildEmptyRootXml(eventCtx);
        }
    }

    /**
     * Extracts POJO field values from JournalArticle XML using a provided mapping.
     *
     * <p><b>Purpose (Business):</b> Enables reverse-mapping of stored JournalArticle XML into key/value pairs
     * for display, validation, or comparison flows.</p>
     *
     * <p><b>Purpose (Technical):</b> Parses XML via JAXP DOM and resolves values from <code>dynamic-element</code>
     * nodes; supports direct fields and one-level nested fields (<code>parent.child</code>).</p>
     *
     * <p><b>Inputs/Invariants:</b>
     * <ul>
     *   <li><code>xmlContent</code> must be non-blank to parse; blank returns an empty result.</li>
     *   <li><code>fieldMapping</code> must contain POJO field -> Journal field mappings.</li>
     * </ul>
     * </p>
     *
     * <p><b>Side effects:</b> Logs parse errors; no persistence.</p>
     *
     * <p><b>Audit behavior:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Never returns null. Returns a map of extracted values; missing XML fields yield
     * null values in the map for those keys.</p>
     */
    @Override
    public Map<String, String> extractFieldsFromXml(String xmlContent, Map<String, String> fieldMapping) {
        Map<String, String> result = new HashMap<String, String>();

        if (xmlContent == null || xmlContent.trim().isEmpty()) {
            _log.error("XML content is null or empty.");
            return result;
        }

        if (fieldMapping == null || fieldMapping.isEmpty()) {
            return result;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlContent)));
            org.w3c.dom.Element root = doc.getDocumentElement();

            for (Map.Entry<String, String> entry : fieldMapping.entrySet()) {
                String pojoField = entry.getKey();
                String journalField = entry.getValue();

                String value;
                if (journalField != null && journalField.contains(".")) {
                    String[] split = journalField.split("\\.");
                    value = getNestedDynamicElementValue(root, split[0], split[1]);
                }
                else {
                    value = getDynamicElementValue(root, journalField);
                }

                result.put(pojoField, value);
                if (_log.isDebugEnabled()) {
                    _log.debug("Mapped field: " + pojoField + " <- " + journalField + " = " + value);
                }
            }
        }
        catch (Exception ex) {
            _log.error("Exception while parsing JournalArticle XML: " + ex.getMessage(), ex);
        }

        return result;
    }

    // ---------------------------------------------------------------------
    // Context helpers (ctx migration-safe: audit.api.CourseEventContext)
    // ---------------------------------------------------------------------

    /**
     * Resolves the default language ID used for the generated JournalArticle XML.
     *
     * <p><b>Purpose (Business):</b> Ensures content is stored using the expected language, supporting consistent
     * rendering and editorial workflows.</p>
     *
     * <p><b>Purpose (Technical):</b> Reads default language from <code>eventCtx.articleConfig</code> when present,
     * otherwise falls back to <code>en_GB</code>.</p>
     *
     * <p><b>Inputs/Invariants:</b> <code>eventCtx</code> and <code>articleConfig</code> may be null.</p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Never returns null or blank; defaults to <code>en_GB</code>.</p>
     */
    private static String resolveDefaultLanguageId(CourseEventContext eventCtx) {
        if (eventCtx != null && eventCtx.getArticleConfig() != null) {
            String lang = eventCtx.getArticleConfig().getDefaultLanguageId();
            if (lang != null && !lang.trim().isEmpty()) {
                return lang.trim();
            }
        }
        return "en_GB";
    }

    /**
     * Resolves the list of language IDs written to the generated XML.
     *
     * <p><b>Purpose (Business):</b> Controls which locales are stored for the article content.</p>
     *
     * <p><b>Purpose (Technical):</b> Currently returns a single-language array aligned to the resolved default language.</p>
     *
     * <p><b>Inputs/Invariants:</b> <code>defaultLang</code> must be non-blank.</p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Returns a non-empty array containing <code>defaultLang</code>.</p>
     */
    private static String[] resolveAvailableLanguageIds(CourseEventContext eventCtx, String defaultLang) {
        return new String[] { defaultLang };
    }

    /**
     * Resolves DDM field type hints used when writing <code>dynamic-element</code> nodes.
     *
     * <p><b>Purpose (Business):</b> Ensures each field is stored with the correct type so it renders and indexes
     * properly in Liferay.</p>
     *
     * <p><b>Purpose (Technical):</b> Reads <code>fieldsTypeInfo</code> from <code>eventCtx.articleConfig</code>;
     * falls back to an empty map when not present.</p>
     *
     * <p><b>Inputs/Invariants:</b> <code>eventCtx</code> and <code>articleConfig</code> may be null.</p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Never returns null; returns an empty map when unavailable.</p>
     */
    private static Map<String, String> resolveFieldsTypeInfo(CourseEventContext eventCtx) {
        if (eventCtx != null && eventCtx.getArticleConfig() != null) {
            return eventCtx.getArticleConfig().getFieldsTypeInfo();
        }
        return Collections.emptyMap();
    }

    // ---------------------------------------------------------------------
    // XML helpers
    // ---------------------------------------------------------------------

    /**
     * Determines whether a field should be treated as "DDM fieldset style" content.
     *
     * <p><b>Purpose (Business):</b> Supports complex repeatable structures where a field behaves like a fieldset
     * rather than a single scalar value.</p>
     *
     * <p><b>Purpose (Technical):</b> Flags specific fields that require fieldset-style serialization handled by
     * {@link #addFieldElementDDM(Element, String, Object, String, String[], Map)}.</p>
     *
     * <p><b>Inputs/Invariants:</b> <code>liferayField</code> may be null.</p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Return semantics:</b> True for configured fieldset-style fields; otherwise false.</p>
     */
    private static boolean isDdmFieldsetStyleField(String liferayField) {
        if (liferayField == null) {
            return false;
        }
        return "areaOfInterest".equalsIgnoreCase(liferayField)
                || "prerequisites.detailPrerequisites.files".equalsIgnoreCase(liferayField);
    }

    /**
     * Normalizes a field list by removing null/blank entries and trimming whitespace.
     *
     * <p><b>Purpose (Business):</b> Prevents invalid configured field entries from breaking processing.</p>
     *
     * <p><b>Purpose (Technical):</b> Uses a stream pipeline to filter and trim the field array.</p>
     *
     * <p><b>Inputs/Invariants:</b> Accepts null arrays.</p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Returns a non-null array (possibly empty).</p>
     */
    private static String[] normalizeFields(String[] fieldsToProcess) {
        if (fieldsToProcess == null || fieldsToProcess.length == 0) {
            return new String[0];
        }
        return Stream.of(fieldsToProcess)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }

    /**
     * Finds a nested <code>dynamic-element</code> node by a dot-separated field path.
     *
     * <p><b>Purpose (Business):</b> Allows updates to target the correct existing XML field when incrementally
     * updating an article.</p>
     *
     * <p><b>Purpose (Technical):</b> Traverses <code>dynamic-element</code> children by matching the "name"
     * attribute for each path segment.</p>
     *
     * <p><b>Inputs/Invariants:</b> Root and fieldPath must be present; otherwise returns null.</p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Returns the terminal element for the path or null if not found.</p>
     */
    private Element findDynamicElement(Element root, String fieldPath) {
        if (root == null || fieldPath == null || fieldPath.trim().isEmpty()) {
            return null;
        }

        String[] parts = fieldPath.split("\\.");
        Element current = root;

        for (String part : parts) {
            if (part == null || part.trim().isEmpty()) {
                return null;
            }

            Element next = null;
            for (Element child : current.elements("dynamic-element")) {
                if (part.equals(child.attributeValue("name"))) {
                    next = child;
                    break;
                }
            }

            if (next == null) {
                return null;
            }

            current = next;
        }

        return current;
    }

    /**
     * Updates a scalar (non-fieldset-style) <code>dynamic-element</code> node with a new value.
     *
     * <p><b>Purpose (Business):</b> Keeps published content consistent with the latest CLS values.</p>
     *
     * <p><b>Purpose (Technical):</b> Updates DDM attributes (type/index-type), clears existing dynamic-content
     * nodes to avoid duplicates, and writes new localized CDATA content.</p>
     *
     * <p><b>Inputs/Invariants:</b> <code>element</code> must be non-null; caller is expected to have located it
     * via {@link #findDynamicElement(Element, String)} or created it via {@link #addFieldElement(Element, String, Object, String, String[], Map)}.</p>
     *
     * <p><b>Side effects:</b> Mutates the provided XML element by detaching children and updating attributes.</p>
     *
     * <p><b>Audit behavior:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Void. If element is null, the method logs and returns without changes.</p>
     *
     * @throws Exception If serialization fails or XML manipulation errors occur.
     */
    private void updateDynamicElement(
            Element element,
            Object value,
            String fieldPath,
            String defaultLanguageId,
            String[] availableLanguageIds,
            Map<String, String> fieldsTypeInfo) throws Exception {

        if (element == null) {
            _log.warn("Nested field path not found while updating: " + fieldPath);
            return;
        }

        String ddmType = fieldsTypeInfo.getOrDefault(fieldPath, "text_area");
        String serializedValue = serialize(value);

        element.addAttribute("type", ddmType);
        element.addAttribute("index-type", "text".equals(ddmType) ? "keyword" : "text");

        // Replace dynamic-content per language (avoid duplicates)
        @SuppressWarnings("unchecked")
        List<Element> contents = (List<Element>) element.elements("dynamic-content");
        if (contents != null) {
            for (Element dc : contents) {
                dc.detach();
            }
        }

        for (String langId : availableLanguageIds) {
            element.addElement("dynamic-content")
                    .addAttribute("language-id", langId)
                    .addCDATA(serializedValue);
        }
    }

    /**
     * Adds a scalar (non-fieldset-style) field path into the XML document, creating intermediate containers as needed.
     *
     * <p><b>Purpose (Business):</b> Ensures newly introduced fields are persisted to the article when a mapping
     * becomes available or a field is newly configured.</p>
     *
     * <p><b>Purpose (Technical):</b> Creates missing intermediate <code>dynamic-element</code> nodes as "fieldset"
     * containers and writes the leaf node as the mapped DDM type with localized content.</p>
     *
     * <p><b>Inputs/Invariants:</b> <code>fieldPath</code> is dot-separated; intermediate nodes are created if absent.</p>
     *
     * <p><b>Side effects:</b> Mutates the provided XML root by adding elements/attributes and writing CDATA.</p>
     *
     * <p><b>Audit behavior:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Void. No changes occur if root/path is missing.</p>
     *
     * @throws Exception If serialization fails or XML manipulation errors occur.
     */
    private void addFieldElement(
            Element root,
            String fieldPath,
            Object value,
            String defaultLanguageId,
            String[] availableLanguageIds,
            Map<String, String> fieldsTypeInfo) throws Exception {

        if (root == null || fieldPath == null || fieldPath.trim().isEmpty()) {
            return;
        }

        String[] fieldParts = fieldPath.split("\\.");
        Element current = root;

        for (int i = 0; i < fieldParts.length; i++) {
            String field = fieldParts[i];
            Element child = findDirectChildByName(current, field);

            if (child == null) {
                child = current.addElement("dynamic-element")
                        .addAttribute("name", field)
                        .addAttribute("instance-id", UUID.randomUUID().toString().substring(0, 8));

                if (i == fieldParts.length - 1) {
                    String ddmType = fieldsTypeInfo.getOrDefault(fieldPath, "text_area");
                    child.addAttribute("type", ddmType);
                    child.addAttribute("index-type", "text".equals(ddmType) ? "keyword" : "text");
                }
                else {
                    // intermediate container
                    child.addAttribute("type", "fieldset");
                    child.addAttribute("index-type", "keyword");
                }
            }

            current = child;
        }

        // Ensure we don't keep appending duplicate content if element was reused
        @SuppressWarnings("unchecked")
        List<Element> existingContents = (List<Element>) current.elements("dynamic-content");
        if (existingContents != null) {
            for (Element dc : existingContents) {
                dc.detach();
            }
        }

        String serializedValue = serialize(value);
        for (String langId : availableLanguageIds) {
            current.addElement("dynamic-content")
                    .addAttribute("language-id", langId)
                    .addCDATA(serializedValue);
        }
    }

    /**
     * Finds a direct <code>dynamic-element</code> child by matching its "name" attribute.
     *
     * <p><b>Purpose (Business):</b> Supports deterministic placement of content within structured article fields.</p>
     *
     * <p><b>Purpose (Technical):</b> Performs a linear scan of direct children named "dynamic-element".</p>
     *
     * <p><b>Inputs/Invariants:</b> Parent and name must be non-null.</p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Returns the matching child or null.</p>
     */
    private Element findDirectChildByName(Element parent, String name) {
        if (parent == null || name == null) {
            return null;
        }
        for (Element child : parent.elements("dynamic-element")) {
            if (name.equals(child.attributeValue("name"))) {
                return child;
            }
        }
        return null;
    }

    /**
     * Updates a fieldset-style field by removing existing nodes for the field and re-adding from the provided value.
     *
     * <p><b>Purpose (Business):</b> Ensures repeatable/fieldset-backed data is stored as proper DDM structures,
     * keeping the article consistent with the incoming CLS structure.</p>
     *
     * <p><b>Purpose (Technical):</b> Removes existing <code>dynamic-element</code> nodes matching the leaf field name
     * under the resolved parent, then delegates to {@link #addFieldElementDDM(Element, String, Object, String, String[], Map)}.</p>
     *
     * <p><b>Inputs/Invariants:</b> Field path is dot-separated; leaf name is removed under the parent path.</p>
     *
     * <p><b>Side effects:</b> Mutates the XML document by detaching elements and adding newly generated fieldset nodes.</p>
     *
     * <p><b>Audit behavior:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Void.</p>
     *
     * @throws Exception If serialization fails or XML manipulation errors occur.
     */
    public void updateDynamicElementDDM(
            Element documentRoot,
            Object value,
            String fieldPath,
            String defaultLanguageId,
            String[] availableLanguageIds,
            Map<String, String> fieldsTypeInfo) throws Exception {

        String[] pathParts = fieldPath.split("\\.");
        String fieldName = pathParts[pathParts.length - 1];

        Element parent = documentRoot;
        for (int i = 0; i < pathParts.length - 1; i++) {
            String part = pathParts[i];
            Element child = findDirectChildByName(parent, part);
            if (child == null) {
                parent = null;
                break;
            }
            parent = child;
        }

        if (parent != null) {
            @SuppressWarnings("unchecked")
            List<Element> siblings = (List<Element>) parent.elements("dynamic-element");

            List<Element> toRemove = new ArrayList<Element>();
            for (Element el : siblings) {
                if (fieldName.equals(el.attributeValue("name"))) {
                    toRemove.add(el);
                }
            }
            for (Element el : toRemove) {
                el.detach();
            }
        }

        addFieldElementDDM(documentRoot, fieldPath, value, defaultLanguageId, availableLanguageIds, fieldsTypeInfo);
    }

    /**
     * Adds a fieldset-style field (repeatable/nested) into the XML document based on the runtime value type.
     *
     * <p><b>Purpose (Business):</b> Stores structured course data (arrays/objects) in a DDM-friendly format so that
     * the front-end template can render repeatable blocks correctly.</p>
     *
     * <p><b>Purpose (Technical):</b> Builds a nested structure where list values create repeatable fieldsets,
     * map values create nested fieldsets, and scalar values create leaf nodes with the configured DDM type.</p>
     *
     * <p><b>Inputs/Invariants:</b>
     * <ul>
     *   <li>For list values: each map item becomes one repeatable fieldset instance.</li>
     *   <li>For map values: keys become nested fields under the fieldset instance.</li>
     *   <li>For scalar values: writes localized dynamic-content with CDATA.</li>
     * </ul>
     * </p>
     *
     * <p><b>Side effects:</b> Mutates XML by adding new elements and writing CDATA; sanitizes content.</p>
     *
     * <p><b>Audit behavior:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Void.</p>
     *
     * @throws Exception If serialization fails or XML manipulation errors occur.
     */
    @SuppressWarnings("unchecked")
    private void addFieldElementDDM(
            Element root,
            String liferayField,
            Object value,
            String defaultLanguageId,
            String[] availableLanguageIds,
            Map<String, String> fieldsTypeInfo) throws Exception {

        String[] pathParts = liferayField.split("\\.");
        String fieldName = pathParts[pathParts.length - 1];

        Element parent = root;
        for (int i = 0; i < pathParts.length - 1; i++) {
            String part = pathParts[i];

            Element child = findDirectChildByName(parent, part);
            if (child == null) {
                child = parent.addElement("dynamic-element")
                        .addAttribute("name", part)
                        .addAttribute("type", "fieldset")
                        .addAttribute("instance-id", UUID.randomUUID().toString().substring(0, 8));
            }
            parent = child;
        }

        String ddmType = fieldsTypeInfo.getOrDefault(liferayField, "text_area");

        if (value instanceof List<?>) {
            List<?> list = (List<?>) value;

            List<Element> toRemove = new ArrayList<Element>();
            for (Element child : (List<Element>) parent.elements("dynamic-element")) {
                if (fieldName.equals(child.attributeValue("name"))) {
                    toRemove.add(child);
                }
            }
            for (Element e : toRemove) {
                e.detach();
            }

            for (Object item : list) {
                if (item instanceof Map) {
                    Element fieldsetElement = parent.addElement("dynamic-element")
                            .addAttribute("name", fieldName)
                            .addAttribute("type", "fieldset")
                            .addAttribute("instance-id", UUID.randomUUID().toString().substring(0, 8));

                    Map<String, Object> subfields = (Map<String, Object>) item;
                    for (Map.Entry<String, Object> subfield : subfields.entrySet()) {
                        addFieldElementDDM(
                                fieldsetElement,
                                subfield.getKey(),
                                subfield.getValue(),
                                defaultLanguageId,
                                availableLanguageIds,
                                fieldsTypeInfo
                        );
                    }
                }
                else {
                    Element el = parent.addElement("dynamic-element")
                            .addAttribute("name", fieldName)
                            .addAttribute("type", ddmType)
                            .addAttribute("instance-id", UUID.randomUUID().toString().substring(0, 8))
                            .addAttribute("index-type", "text".equals(ddmType) ? "keyword" : "text");

                    String serialized = serialize(item);
                    for (String langId : availableLanguageIds) {
                        el.addElement("dynamic-content")
                                .addAttribute("language-id", langId)
                                .addCDATA(serialized);
                    }
                }
            }
            return;
        }

        if (value instanceof Map) {
            Element fieldsetElement = parent.addElement("dynamic-element")
                    .addAttribute("name", fieldName)
                    .addAttribute("type", "fieldset")
                    .addAttribute("instance-id", UUID.randomUUID().toString().substring(0, 8));

            Map<String, Object> map = (Map<String, Object>) value;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                addFieldElementDDM(
                        fieldsetElement,
                        entry.getKey(),
                        entry.getValue(),
                        defaultLanguageId,
                        availableLanguageIds,
                        fieldsTypeInfo
                );
            }
            return;
        }

        Element el = null;
        for (Element child : (List<Element>) parent.elements("dynamic-element")) {
            if (fieldName.equals(child.attributeValue("name"))) {
                el = child;
                break;
            }
        }

        if (el == null) {
            el = parent.addElement("dynamic-element")
                    .addAttribute("name", fieldName)
                    .addAttribute("type", ddmType)
                    .addAttribute("instance-id", UUID.randomUUID().toString().substring(0, 8))
                    .addAttribute("index-type", "text".equals(ddmType) ? "keyword" : "text");
        }
        else {
            List<Element> dynContents = (List<Element>) el.elements("dynamic-content");
            if (dynContents != null) {
                for (Element dc : dynContents) {
                    dc.detach();
                }
            }
            el.addAttribute("type", ddmType);
            el.addAttribute("index-type", "text".equals(ddmType) ? "keyword" : "text");
        }

        String serializedValue = serialize(value);
        for (String langId : availableLanguageIds) {
            el.addElement("dynamic-content")
                    .addAttribute("language-id", langId)
                    .addCDATA(serializedValue);
        }
    }

    /**
     * Serializes a value into the string representation stored in JournalArticle XML.
     *
     * <p><b>Purpose (Business):</b> Ensures stored content is safe to render and consistent for indexing/display.</p>
     *
     * <p><b>Purpose (Technical):</b>
     * <ul>
     *   <li>Null values become empty strings.</li>
     *   <li>Maps/lists are JSON-serialized after sanitizing nested strings.</li>
     *   <li>Scalar values are HTML-sanitized by removing inline styles/classes and scripts.</li>
     * </ul>
     * </p>
     *
     * <p><b>Inputs/Invariants:</b> Accepts any object; string conversion for scalars uses <code>toString()</code>.</p>
     *
     * <p><b>Side effects:</b> Sanitizes HTML and JSON recursively; logs only through callers.</p>
     *
     * <p><b>Return semantics:</b> Never returns null.</p>
     *
     * @throws Exception If JSON serialization fails.
     */
    private String serialize(Object value) throws Exception {
        if (value == null) {
            return "";
        }
        if (value instanceof Map || value instanceof List) {
            return objectMapper.writeValueAsString(sanitizeJson(value));
        }
        return removeInlineStylesAndClasses(value.toString());
    }

    /**
     * Recursively sanitizes JSON-like objects by sanitizing nested string values.
     *
     * <p><b>Purpose (Business):</b> Prevents unsafe markup from being stored inside JSON content that may later
     * be rendered by templates.</p>
     *
     * <p><b>Purpose (Technical):</b> Walks maps/lists recursively; string values are passed through HTML sanitizer.</p>
     *
     * <p><b>Inputs/Invariants:</b> Accepts maps, lists, strings, or scalars.</p>
     *
     * <p><b>Side effects:</b> None (creates new map/list instances).</p>
     *
     * <p><b>Return semantics:</b> Returns a sanitized structure; may return the original scalar for non-collection,
     * non-string values.</p>
     *
     * @throws Exception If sanitization fails.
     */
    private Object sanitizeJson(Object json) throws Exception {
        if (json instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) json;
            Map<String, Object> sanitizedMap = new HashMap<String, Object>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = String.valueOf(entry.getKey());
                Object value = entry.getValue();
                sanitizedMap.put(key, sanitizeJson(value));
            }
            return sanitizedMap;
        }
        else if (json instanceof List) {
            List<?> list = (List<?>) json;
            List<Object> sanitizedList = new ArrayList<Object>();
            for (Object element : list) {
                sanitizedList.add(sanitizeJson(element));
            }
            return sanitizedList;
        }
        else if (json instanceof String) {
            return removeInlineStylesAndClasses((String) json);
        }
        return json;
    }

    /**
     * Removes inline styles/classes and unsafe tags/attributes from an HTML snippet.
     *
     * <p><b>Purpose (Business):</b> Prevents unsafe or presentation-coupled markup from leaking into published content.</p>
     *
     * <p><b>Purpose (Technical):</b> Uses Jsoup to strip style/class attributes and remove script/style tags,
     * then normalizes whitespace.</p>
     *
     * <p><b>Inputs/Invariants:</b> Accepts potentially non-HTML strings; Jsoup parsing tolerates malformed HTML.</p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Returns a non-null sanitized string.</p>
     */
    private static String removeInlineStylesAndClasses(String html) {
        org.jsoup.nodes.Document dirty = Jsoup.parse(html);
        org.jsoup.nodes.Element body = dirty.body();

        body.select("*[style], *[class]").forEach(e -> {
            e.removeAttr("style");
            e.removeAttr("class");
        });

        body.select("script, style").remove();

        dirty.select("*").forEach(e -> e.html(e.html().replaceAll("<!--.*?-->", "")));

        body.select("*").forEach(e -> {
            e.removeAttr("onclick");
            e.removeAttr("onerror");
            e.removeAttr("onload");
            e.removeAttr("onmouseover");
            e.removeAttr("onfocus");
        });

        String sanitizedHtml = body.html().replaceAll("\\s+", " ").trim();
        sanitizedHtml = sanitizedHtml.replaceAll(">\\s+<", "><");

        org.jsoup.nodes.Document.OutputSettings settings =
                new org.jsoup.nodes.Document.OutputSettings().prettyPrint(false);
        dirty.outputSettings(settings);

        return sanitizedHtml;
    }

    /**
     * Builds a minimal valid JournalArticle XML document root.
     *
     * <p><b>Purpose (Business):</b> Provides a safe fallback XML structure when upstream payloads are missing or
     * processing fails, preventing downstream failures.</p>
     *
     * <p><b>Purpose (Technical):</b> Creates <code>&lt;root&gt;</code> with locale attributes; defaults to <code>en_GB</code>.</p>
     *
     * <p><b>Inputs/Invariants:</b> Event context may be null.</p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Never returns null. Returns a valid XML string; uses a hard-coded fallback if XML
     * creation fails unexpectedly.</p>
     */
    private static String buildEmptyRootXml(CourseEventContext eventCtx) {
        try {
            final String defaultLang = resolveDefaultLanguageId(eventCtx);
            final String[] availableLangs = resolveAvailableLanguageIds(eventCtx, defaultLang);

            Document document = SAXReaderUtil.createDocument();
            document.addElement("root")
                    .addAttribute("available-locales", String.join(",", availableLangs))
                    .addAttribute("default-locale", defaultLang);

            return document.formattedString();
        }
        catch (Exception ignore) {
            return "<root available-locales=\"en_GB\" default-locale=\"en_GB\" />";
        }
    }

    // ---------------------------------------------------------------------
    // XML extraction helpers
    // ---------------------------------------------------------------------

    /**
     * Reads the best-effort value of a direct <code>dynamic-element</code> field.
     *
     * <p><b>Purpose (Business):</b> Allows retrieving stored values for UI display or comparisons.</p>
     *
     * <p><b>Purpose (Technical):</b> Scans dynamic-elements by name and prefers <code>en_GB</code> content when present,
     * falling back to other available locales.</p>
     *
     * <p><b>Inputs/Invariants:</b> Root and fieldName must be present.</p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Returns extracted string or null if not found.</p>
     */
    private static String getDynamicElementValue(org.w3c.dom.Element root, String fieldName) {
        if (root == null || fieldName == null) {
            return null;
        }

        NodeList elements = root.getElementsByTagName("dynamic-element");
        for (int i = 0; i < elements.getLength(); i++) {
            org.w3c.dom.Element el = (org.w3c.dom.Element) elements.item(i);
            if (fieldName.equals(el.getAttribute("name"))) {
                NodeList contents = el.getElementsByTagName("dynamic-content");
                String value = null;
                for (int j = 0; j < contents.getLength(); j++) {
                    org.w3c.dom.Element content = (org.w3c.dom.Element) contents.item(j);
                    String lang = content.getAttribute("language-id");
                    if ("en_GB".equals(lang)) {
                        return content.getTextContent();
                    }
                    if ("en_US".equals(lang)) {
                        value = content.getTextContent();
                    }
                    if (value == null) {
                        value = content.getTextContent();
                    }
                }
                return value;
            }
        }
        return null;
    }

    /**
     * Reads the best-effort value of a one-level nested <code>dynamic-element</code> field (<code>parent.child</code>).
     *
     * <p><b>Purpose (Business):</b> Supports extraction of structured content stored under a parent fieldset.</p>
     *
     * <p><b>Purpose (Technical):</b> Locates the parent element by name and then scans its nested dynamic-elements for
     * the child element, returning the first available localized content.</p>
     *
     * <p><b>Inputs/Invariants:</b> Root, parentName, and childName must be present.</p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Returns extracted string or null if not found.</p>
     */
    private static String getNestedDynamicElementValue(org.w3c.dom.Element root, String parentName, String childName) {
        if (root == null || parentName == null || childName == null) {
            return null;
        }

        NodeList elements = root.getElementsByTagName("dynamic-element");
        for (int i = 0; i < elements.getLength(); i++) {
            org.w3c.dom.Element parentEl = (org.w3c.dom.Element) elements.item(i);
            if (parentName.equals(parentEl.getAttribute("name"))) {
                NodeList childEls = parentEl.getElementsByTagName("dynamic-element");
                for (int j = 0; j < childEls.getLength(); j++) {
                    org.w3c.dom.Element childEl = (org.w3c.dom.Element) childEls.item(j);
                    if (childName.equals(childEl.getAttribute("name"))) {
                        NodeList contents = childEl.getElementsByTagName("dynamic-content");
                        for (int k = 0; k < contents.getLength(); k++) {
                            org.w3c.dom.Element content = (org.w3c.dom.Element) contents.item(k);
                            String lang = content.getAttribute("language-id");
                            if ("en_GB".equals(lang)) return content.getTextContent();
                            if ("en_US".equals(lang)) return content.getTextContent();
                            return content.getTextContent();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Parses an integer safely, returning null for blank or invalid values.
     *
     * <p><b>Purpose (Business):</b> Prevents malformed numeric data from breaking article processing flows.</p>
     *
     * <p><b>Purpose (Technical):</b> Applies trim/blank checks and catches parsing exceptions.</p>
     *
     * <p><b>Inputs/Invariants:</b> Accepts null/blank strings.</p>
     *
     * <p><b>Side effects:</b> Logs a warning when parsing fails.</p>
     *
     * <p><b>Return semantics:</b> Returns parsed integer or null when not parseable.</p>
     */
    public static Integer safeParseInt(String value) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            return Integer.parseInt(value.trim());
        }
        catch (Exception e) {
            _log.warn("Could not parse integer: " + value);
            return null;
        }
    }

    // ---------------------------------------------------------------------
    // Param parsing (robust like CourseFetcherImpl)
    // ---------------------------------------------------------------------

    /**
     * Resolves a parameter value into a normalized string array.
     *
     * <p><b>Purpose (Business):</b> Allows configuration-driven field selection to be maintained in parameter groups.</p>
     *
     * <p><b>Purpose (Technical):</b> Supports multiple backing types (String[], Collection, comma-delimited String)
     * and normalizes by trimming and removing blanks.</p>
     *
     * <p><b>Inputs/Invariants:</b> Accepts null map/key and unknown value types.</p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Never returns null; returns an empty array when not configured.</p>
     */
    private static String[] getParamArray(Map<ParameterKeyEnum, Object> paramValues, ParameterKeyEnum key) {
        if (paramValues == null || key == null) {
            return new String[0];
        }

        Object value = paramValues.get(key);
        if (value == null) {
            return new String[0];
        }

        if (value instanceof String[]) {
            return Stream.of((String[]) value)
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
        }

        if (value instanceof Collection) {
            return ((Collection<?>) value).stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
        }

        if (value instanceof String) {
            return Stream.of(((String) value).split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
        }

        return new String[0];
    }
}
