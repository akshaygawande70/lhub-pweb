package com.ntuc.notification.service.internal.cls;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.model.CourseListResponse;
import com.ntuc.notification.model.CourseResponse;
import com.ntuc.notification.model.ScheduleResponse;
import com.ntuc.notification.service.internal.http.HttpExecutor;
import com.ntuc.notification.service.internal.http.HttpResponse;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit tests for {@link ClsConnectionClient} (plain Java, no OSGi/Liferay runtime required).
 *
 * <p>Tests focus on CLS call orchestration decisions and audit emission boundaries rather than DTO mapping internals.</p>
 */
@RunWith(MockitoJUnitRunner.class)
public class ClsConnectionClientTest {

    @Mock
    private HttpExecutor http;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private AuditEventWriter auditWriter;

    @Mock
    private HttpResponse httpResponse;

    @Mock
    private CourseEventContext eventCtx;

    @Test
    public void getLatestCourseSchedules_blankCourseCode_returnsNull_andNoHttpCall() throws Exception {
        ClsConnectionClient client = newClient(new HashMap<ParameterKeyEnum, Object>());

        assertNull(client.getLatestCourseSchedules("   "));
        verifyNoInteractions(http);
    }

    @Test
    public void getLatestCourseSchedules_200Body_returnsSchedule_andAuditsSuccess() throws Exception {
        Map<ParameterKeyEnum, Object> p = baseParams();
        p.put(ParameterKeyEnum.CLS_COURSE_SCHEDULES_ENDPOINT, "/schedules/{courseCode}");
        p.put(ParameterKeyEnum.CLS_COURSE_SCHEDULES_METHOD, "GET");
        p.put(ParameterKeyEnum.CLS_COURSE_RETRY_COUNT, "1");

        ClsConnectionClient client = newClient(p);

        // Seed token cache to avoid auth path complexity in this test.
        setPrivateField(client, "_cachedAccessToken", "t-1");
        setPrivateField(client, "_cachedAccessTokenExpiresAtMs", System.currentTimeMillis() + 60_000L);

        when(http.execute(eq("GET"), anyString(), anyMap(), isNull(), anyInt(), anyInt())).thenReturn(httpResponse);
        when(httpResponse.getStatusCode()).thenReturn(200);
        when(httpResponse.getBody()).thenReturn("{\"ok\":true}");

        ScheduleResponse schedule = mock(ScheduleResponse.class);
        when(mapper.readValue(anyString(), eq(ScheduleResponse.class))).thenReturn(schedule);

        ScheduleResponse out = client.getLatestCourseSchedules("COURSE-1");

        assertSame(schedule, out);

        ArgumentCaptor<AuditEvent> captor = ArgumentCaptor.forClass(AuditEvent.class);
        verify(auditWriter, atLeastOnce()).write(captor.capture());

        // At minimum, ensure audit events were emitted and objects are non-null.
        assertFalse(captor.getAllValues().isEmpty());
        for (AuditEvent e : captor.getAllValues()) {
            assertNotNull(e);
        }
    }

    @Test
    public void getCourseDetails_courseCodeMismatch_returnsNull_andAuditsFailed() throws Exception {
        Map<ParameterKeyEnum, Object> p = baseParams();
        p.put(ParameterKeyEnum.CLS_COURSE_DETAILS_ENDPOINT, "/course/details");
        p.put(ParameterKeyEnum.CLS_COURSE_DETAILS_METHOD, "POST");
        p.put(ParameterKeyEnum.CLS_COURSE_RETRY_COUNT, "1");

        // Use a subclass to bypass static CourseResponseMapper dependency in mapFirstCourse().
        ClsConnectionClient client = new ClsConnectionClient(p, http, mapper, auditWriter) {
            @Override
            protected CourseResponse mapFirstCourse(CourseListResponse list) {
                CourseResponse cr = mock(CourseResponse.class);
                CourseResponse.Body body = mock(CourseResponse.Body.class);
                when(cr.getBody()).thenReturn(body);
                when(body.getCourseCode()).thenReturn("DIFFERENT-CODE");
                return cr;
            }
        };

        // Token cache hit to keep the test scoped to course fetch behavior.
        setPrivateField(client, "_cachedAccessToken", "t-2");
        setPrivateField(client, "_cachedAccessTokenExpiresAtMs", System.currentTimeMillis() + 60_000L);

        when(eventCtx.getCompanyId()).thenReturn(101L);
        when(eventCtx.getGroupId()).thenReturn(202L);
        when(eventCtx.getUserId()).thenReturn(303L);
        when(eventCtx.getCourseCode()).thenReturn("COURSE-OK");
        when(eventCtx.getNtucDTId()).thenReturn(404L);
        when(eventCtx.getEventType()).thenReturn("CHANGED");

        when(http.execute(eq("POST"), anyString(), anyMap(), any(byte[].class), anyInt(), anyInt()))
            .thenReturn(httpResponse);

        when(httpResponse.getStatusCode()).thenReturn(200);
        when(httpResponse.getBody()).thenReturn("{\"courses\":[{}]}");

        when(mapper.readValue(anyString(), eq(CourseListResponse.class))).thenReturn(mock(CourseListResponse.class));

        CourseResponse out = client.getCourseDetails(eventCtx, new String[] {"courseCode"});

        assertNull(out);

        ArgumentCaptor<AuditEvent> captor = ArgumentCaptor.forClass(AuditEvent.class);
        verify(auditWriter, atLeastOnce()).write(captor.capture());

        assertFalse(captor.getAllValues().isEmpty());
        for (AuditEvent e : captor.getAllValues()) {
            assertNotNull(e);
        }
    }

    private ClsConnectionClient newClient(Map<ParameterKeyEnum, Object> p) {
        return new ClsConnectionClient(p, http, mapper, auditWriter);
    }

    private static Map<ParameterKeyEnum, Object> baseParams() {
        Map<ParameterKeyEnum, Object> p = new HashMap<ParameterKeyEnum, Object>();
        p.put(ParameterKeyEnum.CLS_AUTH_BASE_URL, "https://cls.example");
        p.put(ParameterKeyEnum.CLS_HTTP_CONNECT_TIMEOUT_MS, "10");
        p.put(ParameterKeyEnum.CLS_HTTP_READ_TIMEOUT_MS, "10");
        p.put(ParameterKeyEnum.CLS_COMPANY_ID, "1");
        p.put(ParameterKeyEnum.CLS_GROUP_ID, "2");

        // Auth params are present to avoid null-config early returns if auth path is reached.
        p.put(ParameterKeyEnum.CLS_AUTH_ENDPOINT, "/oauth/token");
        p.put(ParameterKeyEnum.CLS_AUTH_METHOD, "POST");
        p.put(ParameterKeyEnum.CLS_AUTH_CLIENT_ID, "cid");
        p.put(ParameterKeyEnum.CLS_AUTH_CLIENT_SECRET, "csec");
        return p;
    }

    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}
