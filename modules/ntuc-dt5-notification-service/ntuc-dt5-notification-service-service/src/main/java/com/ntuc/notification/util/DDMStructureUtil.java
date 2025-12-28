package com.ntuc.notification.util;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * Utility responsible for extracting DDM field metadata from a {@link DDMStructure}.
 *
 * <p>
 * Business Purpose:
 * Provides a reliable way to understand how course/content fields are defined
 * in Liferay, enabling downstream services to correctly map, render, and persist
 * structured content without hardcoded assumptions.
 * </p>
 *
 * <p>
 * Technical Purpose:
 * Parses the JSON definition of a DDMStructure and produces a flattened mapping
 * of field name to DDM field type, including nested and fieldset-based fields.
 * </p>
 *
 * Characteristics:
 * - Read-only utility (no DB or service writes)
 * - Defensive against malformed or partial structure definitions
 * - Safe to use in orchestration and processing flows
 *
 * @author @akshaygawande
 */
@Component(service = DDMStructureUtil.class)
public class DDMStructureUtil {

    private static final Log _log = LogFactoryUtil.getLog(DDMStructureUtil.class);

    /**
     * Extracts a flat map of DDM field names to their declared field types.
     *
     * <p>
     * Business Purpose:
     * Allows dynamic handling of course or content fields by exposing the actual
     * structure configuration defined by content administrators.
     * </p>
     *
     * <p>
     * Technical Purpose:
     * Parses {@link DDMStructure#getDefinition()} JSON and recursively traverses
     * both top-level fields and nestedFields arrays.
     * </p>
     *
     * Inputs / Invariants:
     * - {@code ddmStructure} may be null
     * - Structure definition JSON may be empty or incomplete
     *
     * Side Effects:
     * - None (read-only)
     *
     * Audit Behavior:
     * - No audit logging (pure utility)
     *
     * Return Semantics:
     * - Never returns null
     * - Returns empty map if structure or definition is missing or invalid
     *
     * @param ddmStructure the Liferay DDMStructure instance
     * @return map of fieldName → fieldType (e.g. "courseTitle" → "text")
     */
    public Map<String, String> extractFieldDataTypes(DDMStructure ddmStructure) {
        Map<String, String> fieldDataTypesMap = new HashMap<>();

        if (ddmStructure == null) {
            _log.warn("DDMStructure is null. Returning empty field map.");
            return fieldDataTypesMap;
        }

        try {
            String definition = ddmStructure.getDefinition();

            if (Validator.isNull(definition)) {
                _log.warn("DDMStructure definition is empty. Returning empty field map.");
                return fieldDataTypesMap;
            }

            JSONObject definitionJSON = JSONFactoryUtil.createJSONObject(definition);
            JSONArray fieldsArray = definitionJSON.getJSONArray("fields");

            if (fieldsArray == null) {
                _log.warn("No 'fields' array found in DDMStructure definition. Returning empty field map.");
                return fieldDataTypesMap;
            }

            if (_log.isDebugEnabled()) {
                _log.debug("Parsing DDMStructure definition for field data types.");
            }

            extractFieldsRecursively(fieldsArray, fieldDataTypesMap);

            if (_log.isInfoEnabled()) {
                _log.info(
                    "DDMStructure field extraction completed. Total fields extracted: " +
                    fieldDataTypesMap.size());
            }

        } catch (Exception e) {
            _log.error(
                "Failed to extract field definitions from DDMStructure. Reason: " +
                e.getMessage(), e);
        }

        return fieldDataTypesMap;
    }

    /**
     * Recursively traverses DDM field definitions and extracts name → type mappings.
     *
     * <p>
     * Business Purpose:
     * Ensures nested fieldsets and complex structures are fully understood and
     * available for downstream processing.
     * </p>
     *
     * <p>
     * Technical Purpose:
     * Walks through both "fields" and "nestedFields" JSON arrays and populates
     * a shared accumulator map.
     * </p>
     *
     * Inputs / Invariants:
     * - {@code fieldsArray} may be null or empty
     * - {@code fieldMap} must be non-null
     *
     * Side Effects:
     * - Mutates {@code fieldMap} by adding discovered fields
     *
     * Audit Behavior:
     * - None
     *
     * Return Semantics:
     * - Void; accumulation via provided map
     *
     * @param fieldsArray array of field definition JSON objects
     * @param fieldMap accumulator for field name → type mappings
     */
    private void extractFieldsRecursively(
            JSONArray fieldsArray, Map<String, String> fieldMap) {

        if (fieldsArray == null || fieldsArray.length() == 0) {
            return;
        }

        for (int i = 0; i < fieldsArray.length(); i++) {
            JSONObject fieldObj = fieldsArray.getJSONObject(i);

            if (fieldObj == null) {
                continue;
            }

            String fieldName =
                fieldObj.has("name") ? fieldObj.getString("name") : null;
            String fieldType =
                fieldObj.has("type") ? fieldObj.getString("type") : null;

            if (Validator.isNotNull(fieldName) && Validator.isNotNull(fieldType)) {
                fieldMap.put(fieldName, fieldType);

                if (_log.isDebugEnabled()) {
                    _log.debug("DDM field extracted: " + fieldName + " -> " + fieldType);
                }
            } else {
                if (_log.isDebugEnabled()) {
                    _log.debug("Skipping field with missing name/type: " + fieldObj);
                }
            }

            if (fieldObj.has("nestedFields")) {
                JSONArray nestedFields = fieldObj.getJSONArray("nestedFields");

                if (nestedFields != null && nestedFields.length() > 0) {
                    extractFieldsRecursively(nestedFields, fieldMap);
                }
            }
        }
    }
}
