package com.ntuc.notification.rest.internal.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.model.CourseEventList;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.onetime.api.OneTimeLoadFacade;
import com.ntuc.notification.rest.internal.dto.CourseRestDtos;
import com.ntuc.notification.rest.internal.processor.context.NotificationExecutorProvider;
import com.ntuc.notification.rest.internal.processor.context.RequestContext;
import com.ntuc.notification.rest.internal.processor.context.RequestContextProvider;
import com.ntuc.notification.service.ClsCourseFieldsProcessor;
import com.ntuc.notification.service.NtucSBLocalService;

import java.util.Arrays;
import java.util.Collections;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit tests for {@link CourseRestProcessorLogic}.
 *
 * Notes:
 * - Executes async dispatch synchronously by stubbing {@link NotificationExecutorProvider}.
 * - Replaces internal persister using the built-in test hook to avoid ServiceBuilder persistence wiring.
 */
@RunWith(MockitoJUnitRunner.class)
public class CourseRestProcessorLogicTest {

    @Mock
    private NtucSBLocalService ntucSBLocalService;

    @Mock
    private CounterLocalService counterLocalService;

    @Mock
    private ClsCourseFieldsProcessor clsCourseFieldsProcessor;

    @Mock
    private AuditEventWriter auditEventWriter;

    @Mock
    private OneTimeLoadFacade oneTimeLoadFacade;

    @Mock
    private RequestContextProvider requestContextProvider;

    @Mock
    private NotificationExecutorProvider executorProvider;

    @Mock
    private RequestContext requestContext;

    @Captor
    private ArgumentCaptor<AuditEvent> auditEventCaptor;

    private CourseRestProcessorLogic logic;

    @Before
    public void setUp() {
        when(counterLocalService.increment(anyString())).thenReturn(1001L);
        when(requestContextProvider.currentWithCorrelation(anyString())).thenReturn(requestContext);
        when(requestContext.getCompanyId()).thenReturn(2001L);
        when(requestContext.withCourse(anyString(), anyLong())).thenReturn(requestContext);

        // Run "async" tasks inline for deterministic unit tests.
        doAnswer(inv -> {
            Runnable r = inv.getArgument(0);
            r.run();
            return null;
        }).when(executorProvider).execute(any(Runnable.class));

        logic = new CourseRestProcessorLogic(
                ntucSBLocalService,
                counterLocalService,
                clsCourseFieldsProcessor,
                auditEventWriter,
                oneTimeLoadFacade,
                requestContextProvider,
                executorProvider
        );
    }

    @Test
    public void postCourse_whenWrapperNull_returnsBadRequest_andAuditsValidationFailure() {
        Response resp = logic.postCourse(null);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        assertNotNull(resp.getHeaders().getFirst("X-Correlation-Id"));
        assertNotNull(resp.getHeaders().getFirst("X-Request-Id"));
        assertNotNull(resp.getHeaders().getFirst("X-Job-Run-Id"));

        verify(auditEventWriter, times(1)).write(any(AuditEvent.class));
    }

    @Test
    public void postCourse_whenWrapperHasEmptyEvents_returnsBadRequest_andAuditsValidationFailure() {
        CourseEventList wrapper = mock(CourseEventList.class);
        when(wrapper.getEvents()).thenReturn(Collections.<CourseEvent>emptyList());

        Response resp = logic.postCourse(wrapper);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        verify(auditEventWriter, times(1)).write(any(AuditEvent.class));
    }

    @Test
    public void postOneTimeLoad_whenMissingS3Path_returnsBadRequest_andAuditsValidationFailure() {
        CourseRestDtos.OneTimeLoadRequest req = new CourseRestDtos.OneTimeLoadRequest();
        req.s3Path = "   ";

        Response resp = logic.postOneTimeLoad(req);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        assertNotNull(resp.getHeaders().getFirst("X-Correlation-Id"));
        verify(auditEventWriter, times(1)).write(any(AuditEvent.class));
    }

    @Test
    public void postOneTimeLoad_whenFacadeThrowsIllegalArgumentException_returnsBadRequest_andAuditsValidationFailure() {
        CourseRestDtos.OneTimeLoadRequest req = new CourseRestDtos.OneTimeLoadRequest();
        req.s3Path = "s3://bucket/key";

        doThrow(new IllegalArgumentException("bad path")).when(oneTimeLoadFacade).executeS3Path(anyString());

        Response resp = logic.postOneTimeLoad(req);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());

        // accepted (ENTRY) + failed validation
        verify(auditEventWriter, times(2)).write(any(AuditEvent.class));
    }

    @Test
    public void postOneTimeLoad_whenFacadeSucceeds_returnsOk_andAuditsEntryAndExecution() throws Exception {
        CourseRestDtos.OneTimeLoadRequest req = new CourseRestDtos.OneTimeLoadRequest();
        req.s3Path = "s3://bucket/key";

        Response resp = logic.postOneTimeLoad(req);

        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertNotNull(resp.getHeaders().getFirst("X-Correlation-Id"));

        // accepted (ENTRY) + triggered (EXECUTION)
        verify(auditEventWriter, times(2)).write(any(AuditEvent.class));
        verify(oneTimeLoadFacade, times(1)).executeS3Path("s3://bucket/key");
    }

    @Test
    public void postCourse_whenValidEvent_persists_dispatches_andReturnsOk() {
        // Arrange: replace persister so we can unit-test success path without ServiceBuilder wiring.
        NotificationIntakePersister persister = mock(NotificationIntakePersister.class);
        NtucSB saved = mock(NtucSB.class);
        when(saved.getNtucDTId()).thenReturn(9001L);
        when(persister.persist(any(CourseEvent.class), anyLong())).thenReturn(saved);

        CourseRestProcessorLogic._testOnlyReplaceIntakePersister(logic, persister);

        // Create a minimal event that should pass validator expectations.
        // If the validator enforces additional fields, this test should be updated to populate them.
        CourseEvent event = mock(CourseEvent.class);
        when(event.getCourseCodeSingle()).thenReturn("C-001");
        when(event.getNtucSBId()).thenReturn(9001L);

        CourseEventList wrapper = mock(CourseEventList.class);
        when(wrapper.getEvents()).thenReturn(Arrays.asList(event));

        // Act
        Response resp = logic.postCourse(wrapper);

        // Assert: request accepted
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertNotNull(resp.getHeaders().getFirst("X-Correlation-Id"));
        assertNotNull(resp.getHeaders().getFirst("X-Request-Id"));
        assertNotNull(resp.getHeaders().getFirst("X-Job-Run-Id"));

        // Persisted before dispatch
        verify(persister, times(1)).persist(any(CourseEvent.class), anyLong());

        // Dispatched (runs inline)
        verify(clsCourseFieldsProcessor, times(1)).handleCourseNotification(any(CourseEvent.class), any(Boolean.class));

        // Audit: intake accepted + dispatch completion at minimum
        verify(auditEventWriter, times(2)).write(any(AuditEvent.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void normalizeEvent_whenNull_throwsIllegalArgumentException() {
        CourseRestProcessorLogic.normalizeEvent(null);
    }
}
