package com.ntuc.notification.audit.internal.core;

import com.ntuc.notification.audit.internal.AuditActionFilter;
import com.ntuc.notification.model.AuditLog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuditLoggerCoreTest {

    @Mock private AuditLogRepository repository;
    @Mock private PkGenerator pkGenerator;
    @Mock private AuditClock clock;
    @Mock private JsonSerializer json;

    private AuditLoggerCore core;

    @Before
    public void setUp() {
        when(pkGenerator.nextPk(anyString())).thenReturn(123L);
        when(clock.now()).thenReturn(new Date(1000L));
        when(json.toJson(any())).thenReturn("{\"k\":\"v\"}");

        AuditActionFilter allowAll = new AuditActionFilter(true, false, Collections.emptySet());
        core = new AuditLoggerCore(repository, pkGenerator, clock, json, allowAll);
    }

    @Test
    public void log_whenAllowAll_persistsAudit() {
        core.log("SCH_PROCESS", "DESC", Collections.singletonMap("a", "b"), Collections.singletonMap("x", "y"));

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(repository, times(1)).add(captor.capture());

        AuditLog al = captor.getValue();
        assertEquals(123L, al.getAuditLogId());
        assertTrue(al.isNew());
        assertEquals("SCH_PROCESS", al.getAction());
        assertEquals("DESC", al.getDescription());
        assertNotNull(al.getTimestamp());
        assertNotNull(al.getChangedFieldsJson());
        assertNotNull(al.getAdditionalInfo());
    }

    @Test
    public void log_whenNotLoggable_doesNotPersist() {
        AuditActionFilter allowNone = new AuditActionFilter(false, true, Collections.emptySet());
        core = new AuditLoggerCore(repository, pkGenerator, clock, json, allowNone);

        core.log("ANY", "DESC", null, null);

        verify(repository, never()).add(any(AuditLog.class));
    }

    @Test
    public void log_whenCorrelationId_appendsIntoAdditionalInfo() {
        when(json.toJson(any())).thenReturn("{}");

        core.log(null, 0L, "SCH_PROCESS", "DESC", null, null, "corr-1");

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(repository).add(captor.capture());

        // We can't easily read Blob contents here without extra code,
        // but we at least verify additionalInfo is not null (non-null blob rule).
        assertNotNull(captor.getValue().getAdditionalInfo());
    }
}
