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
 * Utility to extract field name -> field type mapping from a DDMStructure definition.
 *
 * Notes:
 * - Uses DDMStructure.getDefinition() JSON (fields + nestedFields).
 * - Returns a flat map of all field names (including nested ones) to their "type".
 */
@Component(service = DDMStructureUtil.class)
public class DDMStructureUtil {

    private static final Log _log = LogFactoryUtil.getLog(DDMStructureUtil.class);

    /**
     * Extracts a map of field names and their corresponding DDM field types from a DDMStructure.
     *
     * @param ddmStructure the Liferay DDMStructure
     * @return map like: "courseTitle" -> "text", "fundingEligibilityCriteria" -> "fieldset", etc.
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
                _log.debug("Parsing DDMStructure definition for field types...");
            }

            extractFieldsRecursively(fieldsArray, fieldDataTypesMap);

            if (_log.isInfoEnabled()) {
                _log.info("DDM field extraction completed. Total fields extracted: " + fieldDataTypesMap.size());
            }

        } catch (Exception e) {
            _log.error("Error while extracting fields from DDMStructure: " + e.getMessage(), e);
        }

        return fieldDataTypesMap;
    }

    /**
     * Recursively extracts field names and types from the "fields"/"nestedFields" arrays.
     */
    private void extractFieldsRecursively(JSONArray fieldsArray, Map<String, String> fieldMap) {
        if (fieldsArray == null || fieldsArray.length() == 0) {
            return;
        }

        for (int i = 0; i < fieldsArray.length(); i++) {
            JSONObject fieldObj = fieldsArray.getJSONObject(i);
            if (fieldObj == null) {
                continue;
            }

            String fieldName = fieldObj.has("name") ? fieldObj.getString("name") : null;
            String fieldType = fieldObj.has("type") ? fieldObj.getString("type") : null;

            if (Validator.isNotNull(fieldName) && Validator.isNotNull(fieldType)) {
                fieldMap.put(fieldName, fieldType);

                if (_log.isDebugEnabled()) {
                    _log.debug("DDM field: " + fieldName + " -> " + fieldType);
                }
            } else {
                if (_log.isDebugEnabled()) {
                    _log.debug("Skipping field missing name/type: " + fieldObj.toString());
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
