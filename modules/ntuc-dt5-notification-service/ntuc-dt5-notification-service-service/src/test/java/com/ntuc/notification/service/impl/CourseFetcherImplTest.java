package com.ntuc.notification.service.impl;

import com.liferay.journal.model.JournalArticle;

import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.CourseArticleConfig;
import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.constants.NotificationType;
import com.ntuc.notification.constants.ParameterGroupKeys;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.model.CourseResponse;
import com.ntuc.notification.service.ClsConnectionHelper;
import com.ntuc.notification.service.JournalArticleService;
import com.ntuc.notification.service.XmlContentBuilder;
import com.ntuc.notification.service.api.dto.CriticalProcessingResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Command: ./gradlew test
 */
@RunWith(MockitoJUnitRunner.class)
public class CourseFetcherImplTest {

    @Mock
    private ClsConnectionHelper clsConnectionHelper;

    @Mock
    private XmlContentBuilder xmlContentBuilder;

    @Mock
    private JournalArticleService journalArticleService;

    @Mock
    private ParameterGroupKeys parameterGroupKeys;

    @Mock
    private AuditEventWriter auditEventWriter;

    @Mock
    private CourseEventContext eventCtx;

    @Mock
    private CourseArticleConfig articleConfig;

    private CourseFetcherImpl impl;

    @Before
    public void setUp() throws Exception {
        impl = new CourseFetcherImpl();

        setField(impl, "_clsConnectionHelper", clsConnectionHelper);
        setField(impl, "_xmlContentBuilder", xmlContentBuilder);
        setField(impl, "_journalArticleService", journalArticleService);
        setField(impl, "_parameterGroupKeys", parameterGroupKeys);
        setField(impl, "_auditEventWriter", auditEventWriter);

        // Keep context stable across tests unless overridden.
        when(eventCtx.getArticleConfig()).thenReturn(articleConfig);
        when(eventCtx.getCourseCode()).thenReturn("C-001");
        when(eventCtx.getCompanyId()).thenReturn(2000L);
        when(eventCtx.getGroupId()).thenReturn(3000L);
        when(eventCtx.getUserId()).thenReturn(4000L);
        when(eventCtx.getNtucDTId()).thenReturn(5000L);
        when(eventCtx.getChangeFromTypes()).thenReturn(Collections.emptyList());

        // Avoid DS activation dependence; set param cache directly for deterministic tests.
        Map<ParameterKeyEnum, Object> params = new HashMap<>();
        params.put(ParameterKeyEnum.CLS_FIELD_CRITICAL, new String[]{"courseCode", "courseName"});
        params.put(ParameterKeyEnum.CLS_FIELD_NONCRITICAL, new String[]{"venue"});
        params.put(ParameterKeyEnum.CLS_FIELD_BATCH, new String[]{"batchField1"});
        setField(impl, "_paramValues", params);
    }

    @Test
    public void fetchAndProcessCriticalWithPayload_published_capturesPayload() throws Exception {
        when(eventCtx.getEventType()).thenReturn(NotificationType.PUBLISHED);

        CourseResponse cr = buildCourseResponse("C-001", "Course One");
        when(clsConnectionHelper.getCourseDetails(any(CourseEventContext.class), any(String[].class))).thenReturn(cr);

        // For create-new path: existing is null.
        when(journalArticleService.findExistingArticle(anyLong(), anyLong(), anyString(), any(CourseEventContext.class)))
                .thenReturn(null);

        when(xmlContentBuilder.processCriticalFields(any(CourseEventContext.class), any(CourseResponse.class)))
                .thenReturn("<xml/>");

        JournalArticle updated = mock(JournalArticle.class);
        when(updated.getId()).thenReturn(123L);
        when(updated.getUuid()).thenReturn("uuid-123");

        when(journalArticleService.processFields(
                any(CourseEventContext.class),
                any(CourseArticleConfig.class),
                any(CourseResponse.class),
                anyString(),
                eq(true),
                eq(0L),
                isNull()
        )).thenReturn(updated);

        CriticalProcessingResult r = impl.fetchAndProcessCriticalWithPayload(true, eventCtx);

        assertNotNull(r);
        assertEquals("uuid-123", r.getJournalArticleUuid());
        assertEquals(123L, r.getJournalArticleId());
        assertTrue("Expected payloadJson to be present for PUBLISHED capture", r.hasPayload());

        verify(clsConnectionHelper, times(1)).getCourseDetails(any(CourseEventContext.class), any(String[].class));
        verify(journalArticleService, times(1)).processFields(any(), any(), any(), anyString(), eq(true), eq(0L), isNull());
        verify(auditEventWriter, atLeastOnce()).write(any());
    }

    @Test
    public void fetchAndProcessCriticalWithPayload_nonPublished_payloadNotReturned() throws Exception {
        when(eventCtx.getEventType()).thenReturn(NotificationType.CHANGED);

        CourseResponse cr = buildCourseResponse("C-001", "Course One");
        when(clsConnectionHelper.getCourseDetails(any(CourseEventContext.class), any(String[].class))).thenReturn(cr);

        when(journalArticleService.findExistingArticle(anyLong(), anyLong(), anyString(), any(CourseEventContext.class)))
                .thenReturn(null);

        when(xmlContentBuilder.processCriticalFields(any(CourseEventContext.class), any(CourseResponse.class)))
                .thenReturn("<xml/>");

        JournalArticle updated = mock(JournalArticle.class);
        when(updated.getId()).thenReturn(99L);
        when(updated.getUuid()).thenReturn("u99");

        when(journalArticleService.processFields(any(), any(), any(), anyString(), eq(true), eq(0L), isNull()))
                .thenReturn(updated);

        CriticalProcessingResult r = impl.fetchAndProcessCriticalWithPayload(true, eventCtx);

        assertNotNull(r);
        assertEquals("u99", r.getJournalArticleUuid());
        assertEquals(99L, r.getJournalArticleId());
        assertFalse("Payload should not be returned for non-PUBLISHED even if captured internally", r.hasPayload());

        verify(auditEventWriter, atLeastOnce()).write(any());
    }

    @Test
    public void fetchAndProcessNonCriticalFromPayload_nullArticle_throwsIllegalArgumentException() {
        when(eventCtx.getEventType()).thenReturn(NotificationType.PUBLISHED);

        try {
            impl.fetchAndProcessNonCriticalFromPayload(null, eventCtx, "{\"x\":1}");
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException expected) {
            // expected
        }

        verify(auditEventWriter, atLeastOnce()).write(any());
        verifyNoInteractions(xmlContentBuilder);
        verifyNoInteractions(clsConnectionHelper);
    }

    @Test
    public void fetchAndProcessNonCriticalFromPayload_published_invalidJson_fallsBackToCls() throws Exception {
        when(eventCtx.getEventType()).thenReturn(NotificationType.PUBLISHED);

        JournalArticle article = mock(JournalArticle.class);
        when(article.getId()).thenReturn(20L);
        when(article.getContent()).thenReturn("<old/>");

        CourseResponse cr = buildCourseResponse("C-001", "Course One");
        when(clsConnectionHelper.getCourseDetails(any(CourseEventContext.class), any(String[].class))).thenReturn(cr);

        when(xmlContentBuilder.updateOrAppendJournalContent(any(), any(), any(String[].class), anyString()))
                .thenReturn("<new/>");

        JournalArticle updated = mock(JournalArticle.class);
        when(updated.getId()).thenReturn(21L);

        when(journalArticleService.processFields(any(), any(), any(), anyString(), eq(false), eq(20L), eq(article)))
                .thenReturn(updated);

        // invalid JSON -> fromJson() returns null -> should fallback to CLS non-critical path
        JournalArticle out = impl.fetchAndProcessNonCriticalFromPayload(article, eventCtx, "{not-json");

        assertNotNull(out);
        assertEquals(21L, out.getId());

        verify(clsConnectionHelper, times(1)).getCourseDetails(any(CourseEventContext.class), any(String[].class));
        verify(xmlContentBuilder, times(1)).updateOrAppendJournalContent(any(), any(), any(String[].class), anyString());
        verify(journalArticleService, times(1)).processFields(any(), any(), any(), anyString(), eq(false), eq(20L), eq(article));
        verify(auditEventWriter, atLeastOnce()).write(any());
    }

    @Test
    public void fetchAndProcessNonCritical_clsEmptyBody_returnsNull() throws Exception {
        when(eventCtx.getEventType()).thenReturn(NotificationType.PUBLISHED);

        JournalArticle article = mock(JournalArticle.class);
        when(article.getId()).thenReturn(30L);
        when(article.getContent()).thenReturn("<old/>");

        CourseResponse cr = new CourseResponse();
        cr.setBody(null);

        when(clsConnectionHelper.getCourseDetails(any(CourseEventContext.class), any(String[].class))).thenReturn(cr);

        JournalArticle out = impl.fetchAndProcessNonCritical(article, eventCtx);

        assertNull(out);
        verify(auditEventWriter, atLeastOnce()).write(any());
        verifyNoInteractions(xmlContentBuilder);
        verifyNoMoreInteractions(journalArticleService);
    }

    @Test
    public void fetchAndProcessCron_noExistingArticle_returnsNull() throws Exception {
        when(eventCtx.getEventType()).thenReturn(NotificationType.PUBLISHED);

        CourseResponse cr = buildCourseResponse("C-001", "Course One");
        when(clsConnectionHelper.getCourseDetails(any(CourseEventContext.class), any(String[].class))).thenReturn(cr);

        when(journalArticleService.findExistingArticle(anyLong(), anyLong(), anyString(), any(CourseEventContext.class)))
                .thenReturn(null);

        JournalArticle out = impl.fetchAndProcessCron(eventCtx);

        assertNull(out);
        verify(auditEventWriter, atLeastOnce()).write(any());
        verifyNoInteractions(xmlContentBuilder);
    }

    @Test
    public void fetchAndProcessCron_success_updatesAndReturnsArticle() throws Exception {
        when(eventCtx.getEventType()).thenReturn(NotificationType.PUBLISHED);

        CourseResponse cr = buildCourseResponse("C-001", "Course One");
        when(clsConnectionHelper.getCourseDetails(any(CourseEventContext.class), any(String[].class))).thenReturn(cr);

        JournalArticle existing = mock(JournalArticle.class);
        when(existing.getId()).thenReturn(41L);
        when(existing.getContent()).thenReturn("<old/>");

        when(journalArticleService.findExistingArticle(anyLong(), anyLong(), anyString(), any(CourseEventContext.class)))
                .thenReturn(existing);

        when(xmlContentBuilder.updateOrAppendJournalContent(any(), any(), any(String[].class), anyString()))
                .thenReturn("<new/>");

        JournalArticle updated = mock(JournalArticle.class);
        when(updated.getId()).thenReturn(42L);

        when(journalArticleService.processFields(any(), any(), any(), anyString(), eq(false), eq(41L), eq(existing)))
                .thenReturn(updated);

        JournalArticle out = impl.fetchAndProcessCron(eventCtx);

        assertNotNull(out);
        assertEquals(42L, out.getId());

        // Verify batch field list used to call CLS (at least one field).
        ArgumentCaptor<String[]> fieldsCaptor = ArgumentCaptor.forClass(String[].class);
        verify(clsConnectionHelper).getCourseDetails(any(CourseEventContext.class), fieldsCaptor.capture());
        assertTrue(fieldsCaptor.getValue().length >= 1);
        assertTrue(Arrays.asList(fieldsCaptor.getValue()).contains("batchField1"));

        verify(auditEventWriter, atLeastOnce()).write(any());
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private static CourseResponse buildCourseResponse(String code, String name) {
        CourseResponse cr = new CourseResponse();
        CourseResponse.Body body = new CourseResponse.Body();
        body.setCourseCode(code);
        body.setCourseName(name);
        cr.setBody(body);
        return cr;
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field f = findField(target.getClass(), fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    private static Field findField(Class<?> type, String fieldName) throws Exception {
        Class<?> c = type;
        while (c != null) {
            try {
                return c.getDeclaredField(fieldName);
            }
            catch (NoSuchFieldException ignore) {
                c = c.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}
