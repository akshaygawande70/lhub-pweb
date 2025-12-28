package com.ntuc.notification.audit.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CourseArticleConfigTest {

    @Test
    public void constructor_withoutFieldTypeInfo_initializesEmptyMap() {
        CourseArticleConfig config =
                new CourseArticleConfig(10L, "STRUCT", "TPL", "en_US", "COURSE");

        assertNotNull(config.getFieldsTypeInfo());
        assertTrue(config.getFieldsTypeInfo().isEmpty());
    }

    @Test
    public void constructor_withNullFieldTypeInfo_normalizesToEmptyMap() {
        CourseArticleConfig config =
                new CourseArticleConfig(
                        10L, "STRUCT", "TPL", "en_US", "COURSE", null);

        assertNotNull(config.getFieldsTypeInfo());
        assertTrue(config.getFieldsTypeInfo().isEmpty());
    }

    @Test
    public void constructor_withFieldTypeInfo_createsImmutableCopy() {
        Map<String, String> map = new HashMap<>();
        map.put("courseTitle", "text");

        CourseArticleConfig config =
                new CourseArticleConfig(
                        10L, "STRUCT", "TPL", "en_US", "COURSE", map);

        assertEquals("text", config.getFieldsTypeInfo().get("courseTitle"));

        try {
            config.getFieldsTypeInfo().put("x", "y");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            // expected
        }
    }

    @Test
    public void getters_returnProvidedValues() {
        CourseArticleConfig config =
                new CourseArticleConfig(
                        5L, "S1", "T1", "en_GB", "UPDATE");

        assertEquals(5L, config.getFolderId());
        assertEquals("S1", config.getDdmStructureKey());
        assertEquals("T1", config.getDdmTemplateKey());
        assertEquals("en_GB", config.getDefaultLanguageId());
        assertEquals("UPDATE", config.getEventType());
    }
}
