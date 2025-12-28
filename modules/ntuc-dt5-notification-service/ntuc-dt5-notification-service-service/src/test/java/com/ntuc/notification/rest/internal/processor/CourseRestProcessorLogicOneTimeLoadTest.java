package com.ntuc.notification.rest.internal.processor;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.onetime.api.OneTimeLoadFacade;
import com.ntuc.notification.rest.internal.dto.CourseRestDtos;
import com.ntuc.notification.rest.internal.processor.context.NotificationExecutorProvider;
import com.ntuc.notification.rest.internal.processor.context.RequestContext;
import com.ntuc.notification.rest.internal.processor.context.RequestContextProvider;
import com.ntuc.notification.service.ClsCourseFieldsProcessor;
import com.ntuc.notification.service.NtucSBLocalService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CourseRestProcessorLogicOneTimeLoadTest {

    @Mock private NtucSBLocalService ntucSBLocalService;
    @Mock private CounterLocalService counterLocalService;
    @Mock private ClsCourseFieldsProcessor clsCourseFieldsProcessor;
    @Mock private AuditEventWriter auditEventWriter;
    @Mock private OneTimeLoadFacade oneTimeLoadFacade;
    @Mock private RequestContextProvider requestContextProvider;
    @Mock private NotificationExecutorProvider executorProvider;

    private RequestContext baseCtx;

    private CourseRestProcessorLogic logic;

    @Before
    public void setUp() {
        baseCtx = mock(RequestContext.class);

        when(requestContextProvider.currentWithCorrelation(any(String.class)))
                .thenReturn(baseCtx);

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
    public void postOneTimeLoad_nullReq_returns400_andAudits() {
        Response r = logic.postOneTimeLoad(null);

        assertEquals(400, r.getStatus());
        assertNotNull(r.getHeaderString("X-Correlation-Id"));

        Object entity = r.getEntity();
        assertTrue(entity instanceof CourseRestDtos.OneTimeLoadResponse);

        CourseRestDtos.OneTimeLoadResponse body = (CourseRestDtos.OneTimeLoadResponse) entity;
        assertEquals("FAILED", body.status);
        assertTrue(body.message.contains("s3Path"));

        verify(auditEventWriter, atLeastOnce()).write(any(AuditEvent.class));
        verifyNoInteractions(oneTimeLoadFacade);
    }

    @Test
    public void postOneTimeLoad_blankPath_returns400_andAudits() {
        CourseRestDtos.OneTimeLoadRequest req = new CourseRestDtos.OneTimeLoadRequest();
        req.s3Path = "   ";

        Response r = logic.postOneTimeLoad(req);

        assertEquals(400, r.getStatus());
        assertNotNull(r.getHeaderString("X-Correlation-Id"));

        CourseRestDtos.OneTimeLoadResponse body = (CourseRestDtos.OneTimeLoadResponse) r.getEntity();
        assertEquals("FAILED", body.status);

        verify(auditEventWriter, atLeastOnce()).write(any(AuditEvent.class));
        verifyNoInteractions(oneTimeLoadFacade);
    }

    @Test
    public void postOneTimeLoad_valid_callsFacade_returns200_andAudits() {
        CourseRestDtos.OneTimeLoadRequest req = new CourseRestDtos.OneTimeLoadRequest();
        req.s3Path = "s3://bucket/prefix";

        Response r = logic.postOneTimeLoad(req);

        assertEquals(200, r.getStatus());
        assertNotNull(r.getHeaderString("X-Correlation-Id"));

        CourseRestDtos.OneTimeLoadResponse body = (CourseRestDtos.OneTimeLoadResponse) r.getEntity();
        assertEquals("SUCCESS", body.status);

        verify(oneTimeLoadFacade, times(1)).executeS3Path("s3://bucket/prefix");
        verify(auditEventWriter, atLeast(2)).write(any(AuditEvent.class)); // STARTED + SUCCESS
    }

    @Test
    public void postOneTimeLoad_facadeIllegalArgument_returns400_andAudits() {
        doThrow(new IllegalArgumentException("bad path"))
                .when(oneTimeLoadFacade).executeS3Path("s3://bad");

        CourseRestDtos.OneTimeLoadRequest req = new CourseRestDtos.OneTimeLoadRequest();
        req.s3Path = "s3://bad";

        Response r = logic.postOneTimeLoad(req);

        assertEquals(400, r.getStatus());

        CourseRestDtos.OneTimeLoadResponse body = (CourseRestDtos.OneTimeLoadResponse) r.getEntity();
        assertEquals("FAILED", body.status);
        assertTrue(body.message.contains("bad"));

        verify(auditEventWriter, atLeast(2)).write(any(AuditEvent.class)); // STARTED + FAILED
    }

    @Test
    public void postOneTimeLoad_facadeRuntimeException_returns500_andAudits() {
        doThrow(new RuntimeException("boom"))
                .when(oneTimeLoadFacade).executeS3Path("s3://bucket/x");

        CourseRestDtos.OneTimeLoadRequest req = new CourseRestDtos.OneTimeLoadRequest();
        req.s3Path = "s3://bucket/x";

        Response r = logic.postOneTimeLoad(req);

        assertEquals(500, r.getStatus());

        CourseRestDtos.OneTimeLoadResponse body = (CourseRestDtos.OneTimeLoadResponse) r.getEntity();
        assertEquals("FAILED", body.status);

        verify(auditEventWriter, atLeast(2)).write(any(AuditEvent.class)); // STARTED + FAIL
    }
}
