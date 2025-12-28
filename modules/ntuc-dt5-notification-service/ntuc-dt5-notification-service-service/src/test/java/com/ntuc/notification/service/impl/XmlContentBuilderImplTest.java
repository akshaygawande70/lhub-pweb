package com.ntuc.notification.service.impl;

import com.ntuc.notification.audit.api.CourseArticleConfig;
import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.constants.ParameterGroupKeys;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.model.CourseResponse;
import com.ntuc.notification.model.ScheduleResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link XmlContentBuilderImpl}.
 *
 * <p>
 * Key compilation fix: {@link CourseResponse#getBody()} returns {@link CourseResponse.Body},
 * not a Map. Tests therefore supply a real {@link CourseResponse} instance with a real
 * {@link CourseResponse.Body} instance (instead of mocking getBody() to return a Map).
 * </p>
 */
@RunWith(MockitoJUnitRunner.class)
public class XmlContentBuilderImplTest {

    @Mock
    private ParameterGroupKeys parameterGroupKeys;

    @Mock
    private CourseEventContext eventCtx;

    @Mock
    private CourseArticleConfig articleConfig;

    @Mock
    private ScheduleResponse scheduleResponse;

    private XmlContentBuilderImpl builder;

    @Before
    public void setUp() throws Exception {
        builder = new XmlContentBuilderImpl();

        // Wire @Reference via reflection (plain JUnit, no OSGi)
        Field f = XmlContentBuilderImpl.class.getDeclaredField("parameterGroupKeys");
        f.setAccessible(true);
        f.set(builder, parameterGroupKeys);

        when(eventCtx.getArticleConfig()).thenReturn(articleConfig);
        when(articleConfig.getDefaultLanguageId()).thenReturn("en_GB");
        when(articleConfig.getFieldsTypeInfo()).thenReturn(Collections.<String, String>emptyMap());
    }

    @Test
    public void activate_loadsParameterValues_whenServiceReturnsMap() throws Exception {
        Map<ParameterKeyEnum, Object> params = new HashMap<ParameterKeyEnum, Object>();
        params.put(ParameterKeyEnum.CLS_FIELD_CRITICAL, new String[] { " " }); // normalized to empty
        when(parameterGroupKeys.getAllParameterValues()).thenReturn(params);

        builder.activate();

        verify(parameterGroupKeys, times(1)).getAllParameterValues();

        CourseResponse cr = new CourseResponse();
        cr.setBody(new CourseResponse.Body()); // real Body instance

        String xml = builder.processCriticalFields(eventCtx, cr);

        assertNotNull(xml);
        assertTrue(xml.contains("<root"));
        assertTrue(xml.contains("default-locale=\"en_GB\""));
        assertTrue(xml.contains("available-locales=\"en_GB\""));
    }

    @Test
    public void activate_fallsBackToEmptyMap_whenParameterServiceThrows() throws Exception {
        when(parameterGroupKeys.getAllParameterValues()).thenThrow(new RuntimeException("boom"));

        builder.activate();

        CourseResponse cr = new CourseResponse();
        cr.setBody(new CourseResponse.Body());

        String xml = builder.buildNonCriticalFields(eventCtx, cr);

        assertNotNull(xml);
        assertTrue(xml.contains("<root"));
    }

    @Test
    public void buildJournalContent_returnsEmptyRoot_whenCourseResponseOrBodyNull() {
        String xml1 = builder.buildJournalContent(eventCtx, null, new String[] { "any" });
        assertNotNull(xml1);
        assertTrue(xml1.contains("<root"));

        CourseResponse cr = new CourseResponse();
        cr.setBody(null);

        String xml2 = builder.buildJournalContent(eventCtx, cr, new String[] { "any" });
        assertNotNull(xml2);
        assertTrue(xml2.contains("<root"));
    }

    @Test
    public void buildJournalContent_buildsRoot_whenFieldsEmpty_andBodyNonNull() {
        CourseResponse cr = new CourseResponse();
        cr.setBody(new CourseResponse.Body());

        String xml = builder.buildJournalContent(eventCtx, cr, new String[] { " ", null, "" });

        assertNotNull(xml);
        assertTrue(xml.contains("<root"));
        assertTrue(xml.contains("default-locale=\"en_GB\""));
    }

    @Test
    public void updateOrAppendJournalContent_whenExistingBlank_delegatesToBuild() {
        CourseResponse cr = new CourseResponse();
        cr.setBody(new CourseResponse.Body());

        String xml = builder.updateOrAppendJournalContent(eventCtx, cr, new String[0], "   ");

        assertNotNull(xml);
        assertTrue(xml.contains("<root"));
    }

    @Test
    public void updateOrAppendJournalContent_whenBodyNull_returnsExistingContent() {
        CourseResponse cr = new CourseResponse();
        cr.setBody(null);

        String existing = "<root available-locales=\"en_GB\" default-locale=\"en_GB\" />";

        String result = builder.updateOrAppendJournalContent(eventCtx, cr, new String[] { "x" }, existing);

        assertEquals(existing, result);
    }

    @Test
    public void updateOrAppendSchedules_whenExistingBlank_buildsScheduleRoot() {
        // If scheduleResponse is null, schedule builder returns empty root (still valid XML).
        String xml = builder.updateOrAppendSchedules(eventCtx, null, new String[0], "");

        assertNotNull(xml);
        assertTrue(xml.contains("<root"));
        assertTrue(xml.contains("default-locale=\"en_GB\""));
    }

    @Test
    public void updateOrAppendSchedules_whenExistingBlank_withNonNullSchedule_buildsRoot() {
        // Uses mocked scheduleResponse; objectMapper can convert a Mockito proxy into a Map (empty) safely.
        String xml = builder.updateOrAppendSchedules(eventCtx, scheduleResponse, new String[0], "   ");

        assertNotNull(xml);
        assertTrue(xml.contains("<root"));
        assertTrue(xml.contains("default-locale=\"en_GB\""));
    }

    @Test
    public void extractFieldsFromXml_readsDirectAndNestedValues() {
        String xml =
                "<root available-locales=\"en_GB\" default-locale=\"en_GB\">" +
                    "<dynamic-element name=\"courseTitle\" type=\"text\">" +
                        "<dynamic-content language-id=\"en_GB\"><![CDATA[Title GB]]></dynamic-content>" +
                    "</dynamic-element>" +
                    "<dynamic-element name=\"prerequisites\" type=\"fieldset\">" +
                        "<dynamic-element name=\"detailPrerequisites\" type=\"fieldset\">" +
                            "<dynamic-content language-id=\"en_GB\"><![CDATA[Nested GB]]></dynamic-content>" +
                        "</dynamic-element>" +
                    "</dynamic-element>" +
                "</root>";

        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("title", "courseTitle");
        mapping.put("nested", "prerequisites.detailPrerequisites");

        Map<String, String> out = builder.extractFieldsFromXml(xml, mapping);

        assertEquals("Title GB", out.get("title"));
        assertEquals("Nested GB", out.get("nested"));
    }

    @Test
    public void safeParseInt_returnsNullForBlank_andParsesValid() {
        assertNull(XmlContentBuilderImpl.safeParseInt(null));
        assertNull(XmlContentBuilderImpl.safeParseInt("   "));
        assertEquals(Integer.valueOf(123), XmlContentBuilderImpl.safeParseInt(" 123 "));
        assertNull(XmlContentBuilderImpl.safeParseInt("x"));
    }

    @Test
    public void private_getParamArray_supportsStringArrayCollectionAndCommaString() throws Exception {
        Method m = XmlContentBuilderImpl.class.getDeclaredMethod(
                "getParamArray", Map.class, ParameterKeyEnum.class);
        m.setAccessible(true);

        Map<ParameterKeyEnum, Object> params = new HashMap<ParameterKeyEnum, Object>();

        params.put(ParameterKeyEnum.CLS_FIELD_CRITICAL, new String[] { " a ", " ", null, "b" });
        String[] r1 = (String[]) m.invoke(null, params, ParameterKeyEnum.CLS_FIELD_CRITICAL);
        assertArrayEquals(new String[] { "a", "b" }, r1);

        params.put(ParameterKeyEnum.CLS_FIELD_CRITICAL, Arrays.asList(" x ", "", "y"));
        String[] r2 = (String[]) m.invoke(null, params, ParameterKeyEnum.CLS_FIELD_CRITICAL);
        assertArrayEquals(new String[] { "x", "y" }, r2);

        params.put(ParameterKeyEnum.CLS_FIELD_CRITICAL, " m, ,n , o ");
        String[] r3 = (String[]) m.invoke(null, params, ParameterKeyEnum.CLS_FIELD_CRITICAL);
        assertArrayEquals(new String[] { "m", "n", "o" }, r3);

        // Unknown type -> empty
        params.put(ParameterKeyEnum.CLS_FIELD_CRITICAL, Integer.valueOf(10));
        String[] r4 = (String[]) m.invoke(null, params, ParameterKeyEnum.CLS_FIELD_CRITICAL);
        assertArrayEquals(new String[0], r4);
    }

    @Test
    public void private_serialize_sanitizesHtmlStrings_andJsonSanitizesNestedStrings() throws Exception {
        Method serialize = XmlContentBuilderImpl.class.getDeclaredMethod("serialize", Object.class);
        serialize.setAccessible(true);

        // HTML sanitization: removes style/class, script/style tags, event handlers, and normalizes whitespace
        String dirtyHtml = "<p class=\"x\" style=\"color:red\" onclick=\"alert(1)\">Hello</p><script>alert(1)</script>";
        String sanitized = (String) serialize.invoke(builder, dirtyHtml);

        assertNotNull(sanitized);
        assertFalse("Should remove inline class", sanitized.contains("class="));
        assertFalse("Should remove inline style", sanitized.contains("style="));
        assertFalse("Should remove event handlers", sanitized.contains("onclick="));
        assertFalse("Should remove script tag content", sanitized.toLowerCase().contains("script"));

        // JSON-like map should be serialized as JSON string, with nested strings sanitized
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("k", "<span style=\"x\" class=\"y\">v</span>");
        String jsonSerialized = (String) serialize.invoke(builder, json);

        assertNotNull(jsonSerialized);
        assertTrue("Expected JSON string", jsonSerialized.contains("\"k\""));
        assertFalse("Nested string should be sanitized", jsonSerialized.contains("style="));
        assertFalse("Nested string should be sanitized", jsonSerialized.contains("class="));
    }

    @Test
    public void private_buildEmptyRootXml_generatesRootWithDefaultLocale() throws Exception {
        Method m = XmlContentBuilderImpl.class.getDeclaredMethod("buildEmptyRootXml", CourseEventContext.class);
        m.setAccessible(true);

        String xml = (String) m.invoke(null, eventCtx);

        assertNotNull(xml);
        assertTrue(xml.contains("<root"));
        assertTrue(xml.contains("default-locale=\"en_GB\""));
        assertTrue(xml.contains("available-locales=\"en_GB\""));
    }
}
