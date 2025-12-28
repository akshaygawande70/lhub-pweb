package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.model.AuditLog;
import com.ntuc.notification.service.AuditLogLocalService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PersistedAuditEventReaderTest {

    @Mock
    private AuditLogLocalService auditLogLocalService;

    @Test
    public void read_missing_returnsNull() {
        when(auditLogLocalService.fetchAuditLog(10L)).thenReturn(null);

        PersistedAuditEventReader r = new PersistedAuditEventReader(auditLogLocalService);
        assertNull(r.read(10L));
    }

    @Test
    public void read_mapsStringsAndDetailsJsonBlob() throws Exception {
        AuditLog row = mock(AuditLog.class);

        when(row.getStartTimeMs()).thenReturn(100L);
        when(row.getEndTimeMs()).thenReturn(150L);

        when(row.getCompanyId()).thenReturn(1L);
        when(row.getGroupId()).thenReturn(2L);
        when(row.getUserId()).thenReturn(3L);
        when(row.getCourseCode()).thenReturn("C1");
        when(row.getNtucDTId()).thenReturn(9L);

        when(row.getCorrelationId()).thenReturn("corr");
        when(row.getJobRunId()).thenReturn("jobrun");
        when(row.getRequestId()).thenReturn("req");
        when(row.getEventId()).thenReturn("event");

        when(row.getSeverity()).thenReturn("ERROR");
        when(row.getStatus()).thenReturn("FAILED");
        when(row.getStep()).thenReturn("CRON_RECORD_FAILED");
        when(row.getCategory()).thenReturn("DT5_FAILURE");
        when(row.getMessage()).thenReturn("msg");

        when(row.getErrorCode()).thenReturn("CRON_RECORD_FAILED");
        when(row.getErrorMessage()).thenReturn("err");
        when(row.getExceptionClass()).thenReturn("java.lang.RuntimeException");

        Blob details = new SerialBlob("{\"k\":\"v\"}".getBytes("UTF-8"));
        when(row.getDetailsJson()).thenReturn(details);

        when(row.getStackTraceTruncated()).thenReturn(null);
        when(row.getStackTraceHash()).thenReturn("h");

        when(auditLogLocalService.fetchAuditLog(10L)).thenReturn(row);

        PersistedAuditEventReader reader = new PersistedAuditEventReader(auditLogLocalService);
        AuditEvent e = reader.read(10L);

        assertNotNull(e);
        assertEquals("corr", e.getCorrelationId());
        assertEquals(AuditSeverity.ERROR, e.getSeverity());
        assertEquals(AuditStatus.FAILED, e.getStatus());
        assertEquals(AuditStep.CRON_RECORD_FAILED, e.getStep());
        assertEquals("v", e.getDetails().get("k"));
    }
}
