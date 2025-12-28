package com.ntuc.notification.cron.logic;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.service.ClsCourseFieldsProcessor;
import com.ntuc.notification.service.NtucSBLocalService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AbstractClsCronJobRunnerTest {

    @Mock
    private NtucSBLocalService ntucSBLocalService;

    @Mock
    private ClsCourseFieldsProcessor clsCourseFieldsProcessor;

    @Mock
    private AuditEventWriter auditEventWriter;

    private Clock fixedClock;

    @Before
    public void setUp() {
        fixedClock = Clock.fixed(Instant.parse("2025-12-21T00:00:00Z"), ZoneOffset.UTC);
    }

    @Test
    public void run_success_allRows() {
        NtucSB r1 = mockRecord(101L, "C1");
        NtucSB r2 = mockRecord(102L, "C2");

        when(ntucSBLocalService.getUnprocessedCronRecords(any()))
                .thenReturn(Arrays.asList(r1, r2));

        AbstractClsCronJobRunner runner = newTestRunner_successPersists();

        runner.run("job", "group");

        verify(clsCourseFieldsProcessor, times(2)).handleCourseNotification(any(), eq(true));
        verify(ntucSBLocalService, times(2)).updateNtucSB(any(NtucSB.class));

        ArgumentCaptor<AuditEvent> captor = ArgumentCaptor.forClass(AuditEvent.class);
        verify(auditEventWriter, atLeast(1)).write(captor.capture());

        assertTrue(captor.getAllValues().stream().anyMatch(e -> e.getStep() == AuditStep.CRON_JOB_STARTED));
        assertTrue(captor.getAllValues().stream().anyMatch(e -> e.getStep() == AuditStep.CRON_RECORDS_DISCOVERED));
        assertTrue(captor.getAllValues().stream().anyMatch(e -> e.getStep() == AuditStep.CRON_JOB_FINISHED));
        assertTrue(captor.getAllValues().stream().anyMatch(e -> e.getStep() == AuditStep.CRON_RECORD_SUCCESS));
    }

    @Test
    public void run_rowFailure_continuesNextRow() {
        NtucSB r1 = mockRecord(201L, "C1");
        NtucSB r2 = mockRecord(202L, "C2");

        when(ntucSBLocalService.getUnprocessedCronRecords(any()))
                .thenReturn(Arrays.asList(r1, r2));

        doThrow(new RuntimeException("boom"))
                .when(clsCourseFieldsProcessor).handleCourseNotification(argThat(e -> "C1".equals(e.getCourseCodeSingle())), eq(true));

        AbstractClsCronJobRunner runner = newTestRunner_successPersists();

        runner.run("job", "group");

        // still called for both records
        verify(clsCourseFieldsProcessor, times(2)).handleCourseNotification(any(), eq(true));

        // only second record persisted as success
        verify(ntucSBLocalService, times(1)).updateNtucSB(any(NtucSB.class));

        ArgumentCaptor<AuditEvent> captor = ArgumentCaptor.forClass(AuditEvent.class);
        verify(auditEventWriter, atLeast(1)).write(captor.capture());

        assertTrue(captor.getAllValues().stream().anyMatch(e ->
                e.getStep() == AuditStep.CRON_RECORD_FAILED && e.getStatus() == AuditStatus.FAILED));
    }

    @Test
    public void run_fatalFailure_jobFailedEventWritten() {
        when(ntucSBLocalService.getUnprocessedCronRecords(any()))
                .thenThrow(new RuntimeException("fatal"));

        AbstractClsCronJobRunner runner = newTestRunner_successPersists();

        runner.run("job", "group");

        ArgumentCaptor<AuditEvent> captor = ArgumentCaptor.forClass(AuditEvent.class);
        verify(auditEventWriter, atLeast(1)).write(captor.capture());

        assertTrue(captor.getAllValues().stream().anyMatch(e -> e.getStep() == AuditStep.CRON_JOB_FAILED));
    }

    private AbstractClsCronJobRunner newTestRunner_successPersists() {
        return new AbstractClsCronJobRunner(
                ntucSBLocalService,
                clsCourseFieldsProcessor,
                auditEventWriter,
                () -> "corr-1",
                () -> "jobrun-1",
                fixedClock
        ) {
            @Override
            protected List<NtucSB> fetchRecordsToProcess(NtucSBLocalService svc) {
                return svc.getUnprocessedCronRecords(null);
            }

            @Override
            protected String getSelectorDescription() {
                return "test-selector";
            }

            @Override
            protected void markRecordSuccess(NtucSBLocalService svc, NtucSB record) {
                svc.updateNtucSB(record);
            }
        };
    }

    private NtucSB mockRecord(long ntucDtId, String courseCode) {
        NtucSB r = mock(NtucSB.class);
        when(r.getNtucDTId()).thenReturn(ntucDtId);
        when(r.getCompanyId()).thenReturn(1L);
        when(r.getCourseCode()).thenReturn(courseCode);
        when(r.getCourseType()).thenReturn("T");
        when(r.getEvent()).thenReturn("CHANGED");
        when(r.getChangeFrom()).thenReturn("TITLE,VENUE");
        when(r.getNotificationDate()).thenReturn(null);
        return r;
    }
}
