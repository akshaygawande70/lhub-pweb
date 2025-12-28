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
 * Service for generating and updating Journal Article XML content based on CLS data.
 *
 * NOTE:
 * - This is OSGi, do not unit-test directly outside OSGi.
 * - Extract and unit-test pure helper methods if needed.
 */
@Component(service = XmlContentBuilder.class)
public class XmlContentBuilderImpl implements XmlContentBuilder {

    private static final Log _log = LogFactoryUtil.getLog(XmlContentBuilderImpl.class);

    @Reference
    private ParameterGroupKeys parameterGroupKeys;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<ParameterKeyEnum, Object> paramValues;

    @Activate
    protected void activate() {
        try {
            paramValues = parameterGroupKeys.getAllParameterValues();
        } catch (Throwable t) {
            _log.warn("Failed to preload parameter values in XmlContentBuilderImpl", t);
            paramValues = new HashMap<ParameterKeyEnum, Object>();
        }
    }

    // ---------------------------------------------------------------------
    // Public APIs
    // ---------------------------------------------------------------------

    @Override
    public String processCriticalFields(CourseEventContext eventCtx, CourseResponse courseResponse) {
        _log.info("Generating critical fields XML");
        String[] fields = getParamArray(paramValues, ParameterKeyEnum.CLS_FIELD_CRITICAL);
        return buildJournalContent(eventCtx, courseResponse, fields);
    }

    @Override
    public String buildNonCriticalFields(CourseEventContext eventCtx, CourseResponse courseResponse) {
        _log.info("Generating non-critical fields XML");
        String[] fields = getParamArray(paramValues, ParameterKeyEnum.CLS_FIELD_NONCRITICAL);
        return buildJournalContent(eventCtx, courseResponse, fields);
    }

    @Override
    public String buildBatchFields(CourseEventContext eventCtx, CourseResponse courseResponse) {
        _log.info("Generating batch fields XML");
        String[] fields = getParamArray(paramValues, ParameterKeyEnum.CLS_FIELD_BATCH);
        return buildJournalContent(eventCtx, courseResponse, fields);
    }

    @Override
    public String processAllRetriggerFields(CourseEventContext eventCtx, CourseResponse courseResponse, String[] fieldsToProcess) {
        _log.info("Generating ALL fields XML");
        String[] safeFields = (fieldsToProcess == null) ? new String[0] : fieldsToProcess;
        return buildJournalContent(eventCtx, courseResponse, safeFields);
    }

    @Override
    public String buildJournalContent(CourseEventContext eventCtx, CourseResponse courseResponse, String[] fieldsToProcess) {
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
                    } else {
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

    @Override
    public String updateOrAppendJournalContent(
            CourseEventContext eventCtx,
            CourseResponse courseResponse,
            String[] fieldsToProcess,
            String existingContent) {

        try {
            if (existingContent == null || existingContent.trim().isEmpty()) {
                // No existing XML -> treat it like a fresh build.
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
                        } else {
                            updateDynamicElement(existing, value, liferayField, defaultLang, availableLangs, fieldsTypeInfo);
                        }
                    }
                    else {
                        if (_log.isDebugEnabled()) {
                            _log.debug("Adding new field: " + liferayField);
                        }

                        if (isDdmFieldsetStyleField(liferayField)) {
                            addFieldElementDDM(root, liferayField, value, defaultLang, availableLangs, fieldsTypeInfo);
                        } else {
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

    @Override
    public String processCriticalFieldsSchedule(CourseEventContext eventCtx, ScheduleResponse scheduleResponse) {
        _log.info("Generating critical schedule fields XML");
        String[] fields = getParamArray(paramValues, ParameterKeyEnum.CLS_FIELD_CRITICAL_SCHEDULE);
        return buildJournalContentSchedule(eventCtx, scheduleResponse, fields);
    }

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
    // ctx migration-safe (audit.api.CourseEventContext)
    // ---------------------------------------------------------------------

    private static String resolveDefaultLanguageId(CourseEventContext eventCtx) {
        if (eventCtx != null && eventCtx.getArticleConfig() != null) {
            String lang = eventCtx.getArticleConfig().getDefaultLanguageId();
            if (lang != null && !lang.trim().isEmpty()) {
                return lang.trim();
            }
        }
        return "en_GB";
    }

    private static String[] resolveAvailableLanguageIds(CourseEventContext eventCtx, String defaultLang) {
        return new String[] { defaultLang };
    }

    private static Map<String, String> resolveFieldsTypeInfo(CourseEventContext eventCtx) {
        if (eventCtx != null && eventCtx.getArticleConfig() != null) {
            return eventCtx.getArticleConfig().getFieldsTypeInfo();
        }
        return Collections.emptyMap();
    }


    // ---------------------------------------------------------------------
    // XML helpers
    // ---------------------------------------------------------------------

    private static boolean isDdmFieldsetStyleField(String liferayField) {
        if (liferayField == null) {
            return false;
        }
        return "areaOfInterest".equalsIgnoreCase(liferayField)
                || "prerequisites.detailPrerequisites.files".equalsIgnoreCase(liferayField);
    }

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
            List<Element> toRemove = new ArrayList<Element>();
            for (Element el : (List<Element>) parent.elements("dynamic-element")) {
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

    private String serialize(Object value) throws Exception {
        if (value == null) {
            return "";
        }
        if (value instanceof Map || value instanceof List) {
            return objectMapper.writeValueAsString(sanitizeJson(value));
        }
        return removeInlineStylesAndClasses(value.toString());
    }

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

    private static String buildEmptyRootXml(CourseEventContext eventCtx) {
        try {
            final String defaultLang = resolveDefaultLanguageId(eventCtx);
            final String[] availableLangs = resolveAvailableLanguageIds(eventCtx, defaultLang);

            Document document = SAXReaderUtil.createDocument();
            document.addElement("root")
                    .addAttribute("available-locales", String.join(",", availableLangs))
                    .addAttribute("default-locale", defaultLang);

            return document.formattedString();
        } catch (Exception ignore) {
            return "<root available-locales=\"en_GB\" default-locale=\"en_GB\" />";
        }
    }

    // ---------------------------------------------------------------------
    // XML extraction helpers
    // ---------------------------------------------------------------------

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
