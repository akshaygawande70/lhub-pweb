package com.ntuc.notification.service.internal.cls;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.service.ClsConnectionHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClsDummyApiFacadeTest {

    @Mock
    private ClsConnectionHelper clsConnectionHelper;

    @Mock
    private AuditEventWriter auditEventWriter;

    @Test
    public void parseKind_acceptsCaseInsensitiveOnly() {
        assertEquals(ClsDummyApiFacade.DummyKind.COURSES, ClsDummyApiFacade.parseKind("courses"));
        assertEquals(ClsDummyApiFacade.DummyKind.SUBSCRIPTIONS, ClsDummyApiFacade.parseKind("SUBSCRIPTIONS"));
        assertNull(ClsDummyApiFacade.parseKind("course"));
        assertNull(ClsDummyApiFacade.parseKind(" "));
        assertNull(ClsDummyApiFacade.parseKind(null));
    }

    @Test
    public void fetchDummyJson_invalidKind_auditsFailed_returnsNull() {
        ClsDummyApiFacade facade = new ClsDummyApiFacade(clsConnectionHelper, auditEventWriter);

        String json = facade.fetchDummyJson("BAD_KIND", "C1", 1L, 2L, 3L, "corr-1");

        assertNull(json);

        ArgumentCaptor<AuditEvent> cap = ArgumentCaptor.forClass(AuditEvent.class);
        verify(auditEventWriter, atLeastOnce()).write(cap.capture());

        AuditEvent e = cap.getValue();
        assertEquals(AuditStatus.FAILED, e.getStatus());
        assertEquals(AuditErrorCode.DT5_INVALID_INPUT, e.getErrorCode());
    }

    @Test
    public void fetchDummyJson_coursesSuccess_writesStartedAndSuccess() throws Exception {
        when(clsConnectionHelper.getCoursesDummyRawJson("C1")).thenReturn("{\"ok\":true}");

        ClsDummyApiFacade facade = new ClsDummyApiFacade(clsConnectionHelper, auditEventWriter);

        String json = facade.fetchDummyJson("COURSES", "C1", 1L, 2L, 3L, "corr-2");

        assertEquals("{\"ok\":true}", json);

        ArgumentCaptor<AuditEvent> cap = ArgumentCaptor.forClass(AuditEvent.class);
        verify(auditEventWriter, atLeast(2)).write(cap.capture());

        AuditEvent started = cap.getAllValues().get(0);
        assertEquals(AuditStatus.STARTED, started.getStatus());
        assertEquals(0L, started.getEndTimeMs());

        boolean hasSuccess = cap.getAllValues().stream().anyMatch(e -> e.getStatus() == AuditStatus.SUCCESS);
        assertTrue(hasSuccess);
    }

    @Test
    public void fetchDummyJson_subscriptionsNullBody_writesFailed_returnsNull() throws Exception {
        when(clsConnectionHelper.getSubscriptionsDummyRawJson("C2")).thenReturn(null);

        ClsDummyApiFacade facade = new ClsDummyApiFacade(clsConnectionHelper, auditEventWriter);

        String json = facade.fetchDummyJson("SUBSCRIPTIONS", "C2", 1L, 2L, 3L, "corr-3");

        assertNull(json);

        ArgumentCaptor<AuditEvent> cap = ArgumentCaptor.forClass(AuditEvent.class);
        verify(auditEventWriter, atLeast(2)).write(cap.capture());

        boolean hasFailed = cap.getAllValues().stream().anyMatch(e -> e.getStatus() == AuditStatus.FAILED);
        assertTrue(hasFailed);
    }

    @Test
    public void fetchDummyJson_coursesException_writesFailed_returnsNull() throws Exception {
        when(clsConnectionHelper.getCoursesDummyRawJson("C3")).thenThrow(new RuntimeException("boom"));

        ClsDummyApiFacade facade = new ClsDummyApiFacade(clsConnectionHelper, auditEventWriter);

        String json = facade.fetchDummyJson("COURSES", "C3", 1L, 2L, 3L, "corr-4");

        assertNull(json);

        ArgumentCaptor<AuditEvent> cap = ArgumentCaptor.forClass(AuditEvent.class);
        verify(auditEventWriter, atLeast(2)).write(cap.capture());

        boolean hasFailed = cap.getAllValues().stream().anyMatch(e -> e.getStatus() == AuditStatus.FAILED);
        assertTrue(hasFailed);
    }
}
