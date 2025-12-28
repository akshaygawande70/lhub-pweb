package com.ntuc.notification.rest.internal.processor;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.model.CourseEventList;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.onetime.api.OneTimeLoadFacade;
import com.ntuc.notification.rest.internal.processor.context.NotificationExecutorProvider;
import com.ntuc.notification.rest.internal.processor.context.RequestContext;
import com.ntuc.notification.rest.internal.processor.context.RequestContextProvider;
import com.ntuc.notification.service.ClsCourseFieldsProcessor;
import com.ntuc.notification.service.NtucSBLocalService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CourseRestProcessorLogicTest {

    @Mock private NtucSBLocalService ntucSBLocalService;
    @Mock private CounterLocalService counterLocalService;
    @Mock private ClsCourseFieldsProcessor clsCourseFieldsProcessor;
    @Mock private AuditEventWriter auditEventWriter;
    @Mock private OneTimeLoadFacade oneTimeLoadFacade;
    @Mock private RequestContextProvider requestContextProvider;
    @Mock private NotificationExecutorProvider executorProvider;

    @Captor private ArgumentCaptor<AuditEvent> auditCaptor;

    @Test
    public void wrapperEmpty_writesAuditWithCompanyGroupUserFromContext() {
        when(counterLocalService.increment(anyString())).thenReturn(999L);

        RequestContext baseCtx = new RequestContext("corr-1", 20097L, 39367L, 315604L, "", 0L);
        when(requestContextProvider.currentWithCorrelation(anyString())).thenReturn(baseCtx);

        CourseRestProcessorLogic logic = new CourseRestProcessorLogic(
                ntucSBLocalService,
                counterLocalService,
                clsCourseFieldsProcessor,
                auditEventWriter,
                oneTimeLoadFacade,
                requestContextProvider,
                executorProvider
        );

        CourseEventList wrapper = new CourseEventList();
        wrapper.setEvents(Collections.<CourseEvent>emptyList());

        logic.postCourse(wrapper);

        verify(auditEventWriter, atLeastOnce()).write(auditCaptor.capture());
        AuditEvent e = auditCaptor.getAllValues().get(0);

        assertEquals(20097L, e.getCompanyId());
        assertEquals(39367L, e.getGroupId());
        assertEquals(315604L, e.getUserId());
    }

    @Test
    public void dispatchFailure_writesAuditWithCourseCodeAndNtucDtId() throws Exception {
        when(counterLocalService.increment(anyString())).thenReturn(1000L);

        RequestContext baseCtx = new RequestContext("corr-2", 20097L, 39367L, 315604L, "", 0L);
        when(requestContextProvider.currentWithCorrelation(anyString())).thenReturn(baseCtx);

        // Execute async runnable immediately
        doAnswer(invocation -> {
            Runnable r = invocation.getArgument(0);
            r.run();
            return null;
        }).when(executorProvider).execute(any(Runnable.class));

        // Build a validator-compliant event (based on your validator rules).
        CourseEvent ev = buildValidEvent("NSS test-000012");

        CourseEventList wrapper = new CourseEventList();
        wrapper.setEvents(Collections.singletonList(ev));

        // Persisted NtucSB must return dt id so CourseRestProcessorLogic assigns it to event.
        NtucSB sb = mock(NtucSB.class);
        when(sb.getNtucDTId()).thenReturn(360460L);

        // Cover common persister patterns (create + update OR add).
        lenient().when(ntucSBLocalService.createNtucSB(anyLong())).thenReturn(sb);
        lenient().when(ntucSBLocalService.updateNtucSB(any())).thenReturn(sb);
        lenient().when(ntucSBLocalService.addNtucSB(any())).thenReturn(sb);

        doThrow(new RuntimeException("boom"))
                .when(clsCourseFieldsProcessor)
                .handleCourseNotification(any(CourseEvent.class), eq(false));

        CourseRestProcessorLogic logic = new CourseRestProcessorLogic(
                ntucSBLocalService,
                counterLocalService,
                clsCourseFieldsProcessor,
                auditEventWriter,
                oneTimeLoadFacade,
                requestContextProvider,
                executorProvider
        );

        logic.postCourse(wrapper);

        verify(auditEventWriter, atLeastOnce()).write(auditCaptor.capture());

        boolean foundFailureWithKeys = false;
        for (AuditEvent a : auditCaptor.getAllValues()) {
            if ("NSS test-000012".equals(a.getCourseCode()) && a.getNtucDTId() == 360460L) {
                foundFailureWithKeys = true;
                assertEquals(20097L, a.getCompanyId());
                assertEquals(39367L, a.getGroupId());
                assertEquals(315604L, a.getUserId());
            }
        }

        assertTrue("Expected at least one audit event with courseCode and ntucDTId", foundFailureWithKeys);
    }

    /**
     * Creates a CourseEvent that should pass CourseNotificationRequestValidator rules:
     * - notificationId required (GUID)
     * - eventType required
     * - timestamp required: dd/MM/yyyy HH:mm:ss
     * - courses[] required and not empty
     * - each courseCode required
     * - courseType required and must be TMS
     */
    private static CourseEvent buildValidEvent(String courseCode) {
        CourseEvent ev = new CourseEvent();

        // If your generated model uses different names, change only here.
        ev.setNotificationId("1c46f759-8db1-421e-accf-e090138001cc");
        ev.setEventType("published");
        ev.setTimestamp("24/12/2025 18:40:25");

        CourseEvent.Course c = new CourseEvent.Course();
        c.setCourseCode(courseCode);
        c.setCourseType("TMS");

        ev.setCourses(Collections.singletonList(c));

        // normalizeEvent() will also set courseCodeSingle based on first course if needed.
        ev.setCourseCodeSingle(courseCode);
        ev.setCourseTypeSingle("TMS");

        return ev;
    }
}
