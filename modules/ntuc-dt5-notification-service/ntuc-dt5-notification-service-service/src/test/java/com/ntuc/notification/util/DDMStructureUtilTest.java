package com.ntuc.notification.util;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.portal.kernel.json.JSONFactoryUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DDMStructureUtil}.
 *
 * Focus:
 * - Pure JSON parsing logic
 * - Recursive extraction of nestedFields
 * - Defensive handling of null / empty definitions
 *
 * Liferay runtime is NOT required.
 */
@RunWith(MockitoJUnitRunner.class)
public class DDMStructureUtilTest {

    @Mock
    private DDMStructure ddmStructure;

    private DDMStructureUtil util;

    @Before
    public void setUp() {
        util = new DDMStructureUtil();
    }

    @Test
    public void extractFieldDataTypes_nullStructure_returnsEmptyMap() {
        Map<String, String> result = util.extractFieldDataTypes(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void extractFieldDataTypes_emptyDefinition_returnsEmptyMap() {
        when(ddmStructure.getDefinition()).thenReturn(null);

        Map<String, String> result = util.extractFieldDataTypes(ddmStructure);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void extractFieldDataTypes_noFieldsArray_returnsEmptyMap() {
        when(ddmStructure.getDefinition())
            .thenReturn(JSONFactoryUtil.createJSONObject().toString());

        Map<String, String> result = util.extractFieldDataTypes(ddmStructure);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void extractFieldDataTypes_simpleFields_extractedCorrectly() {
        String json =
            "{ \"fields\": [" +
                "{ \"name\": \"courseTitle\", \"type\": \"text\" }," +
                "{ \"name\": \"courseCode\", \"type\": \"text\" }" +
            "] }";

        when(ddmStructure.getDefinition()).thenReturn(json);

        Map<String, String> result = util.extractFieldDataTypes(ddmStructure);

        assertEquals(2, result.size());
        assertEquals("text", result.get("courseTitle"));
        assertEquals("text", result.get("courseCode"));
    }

    @Test
    public void extractFieldDataTypes_nestedFields_extractedRecursively() {
        String json =
            "{ \"fields\": [" +
                "{" +
                    "\"name\": \"fundingEligibilityCriteria\"," +
                    "\"type\": \"fieldset\"," +
                    "\"nestedFields\": [" +
                        "{ \"name\": \"title\", \"type\": \"text\" }," +
                        "{ \"name\": \"description\", \"type\": \"text_area\" }" +
                    "]" +
                "}" +
            "] }";

        when(ddmStructure.getDefinition()).thenReturn(json);

        Map<String, String> result = util.extractFieldDataTypes(ddmStructure);

        assertEquals(3, result.size());
        assertEquals("fieldset", result.get("fundingEligibilityCriteria"));
        assertEquals("text", result.get("title"));
        assertEquals("text_area", result.get("description"));
    }

    @Test
    public void extractFieldDataTypes_fieldMissingNameOrType_isSkipped() {
        String json =
            "{ \"fields\": [" +
                "{ \"name\": \"validField\", \"type\": \"text\" }," +
                "{ \"name\": \"invalidField\" }," +
                "{ \"type\": \"text\" }" +
            "] }";

        when(ddmStructure.getDefinition()).thenReturn(json);

        Map<String, String> result = util.extractFieldDataTypes(ddmStructure);

        assertEquals(1, result.size());
        assertTrue(result.containsKey("validField"));
        assertFalse(result.containsKey("invalidField"));
    }

    @Test
    public void extractFieldDataTypes_malformedJson_returnsEmptyMapGracefully() {
        when(ddmStructure.getDefinition()).thenReturn("{ invalid json");

        Map<String, String> result = util.extractFieldDataTypes(ddmStructure);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
