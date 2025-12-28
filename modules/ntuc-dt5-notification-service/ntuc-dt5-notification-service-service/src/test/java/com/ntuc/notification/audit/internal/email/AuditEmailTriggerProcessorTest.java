package com.ntuc.notification.audit.internal.email;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.constants.AlertScope;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.audit.util.DbAlertDeduplicator;
import com.ntuc.notification.email.EmailSender;
import com.ntuc.notification.email.api.AlertEmailCategory;
import com.ntuc.notification.email.api.AuditEmailTriggerDeps;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuditEmailTriggerProcessorTest {

    @Mock private EmailSender emailSender;
    @Mock private AuditEventWriter auditWriter;
    @Mock private DbAlertDeduplicator deduplicator;

    @Mock private AuditEmailTriggerDeps.PolicyResolver policyResolver;
    @Mock private AuditEmailTriggerDeps.FingerprintBuilder fingerprintBuilder;
    @Mock private AuditEmailTriggerDeps.EmailCategoryResolver categoryResolver;
    @Mock private AuditEmailTriggerDeps.TemplateModelBuilder modelBuilder;
    @Mock private AuditEmailTriggerDeps.TemplateResolver templateResolver;

    private AuditEmailTriggerProcessor processor;

    @Before
    public void setUp() {
        processor = new AuditEmailTriggerProcessor(
            policyResolver,
            fingerprintBuilder,
            categoryResolver,
            modelBuilder,
            templateResolver,
            deduplicator,
            emailSender,
            auditWriter,
            5
        );
    }

    @Test
    public void emailSent_success_isAudited() {
        AlertPolicy policy =
            new AlertPolicy(true, AuditSeverity.ERROR, 5, AlertScope.FLOW, "ops@test.com");

        AuditEvent src = baseEvent();

        wireHappyPath(policy, src);

        processor.handlePersistedEvent("1", src, Collections.emptyMap());

        verify(emailSender)
            .send(eq(1L), eq("ops@test.com"), eq("sub"), eq("body"), eq(true));

        verify(auditWriter)
            .write(argThat(e -> e.getStep() == AuditStep.EMAIL_SENT));
    }

    @Test
    public void emailSendFailure_isAudited_andNotThrown() {
        AlertPolicy policy =
            new AlertPolicy(true, AuditSeverity.ERROR, 5, AlertScope.FLOW, "ops@test.com");

        AuditEvent src = baseEvent();

        wireHappyPath(policy, src);

        doThrow(new RuntimeException("smtp down"))
            .when(emailSender)
            .send(anyLong(), anyString(), anyString(), anyString(), anyBoolean());

        // JUnit 4: just execute — test fails if exception propagates
        processor.handlePersistedEvent("1", src, Collections.emptyMap());

        verify(auditWriter)
            .write(argThat(e -> e.getStep() == AuditStep.EMAIL_SEND_FAILED));
    }


    @Test
    public void deduped_email_isSuppressed() {
        AlertPolicy policy =
            new AlertPolicy(true, AuditSeverity.ERROR, 5, AlertScope.FLOW, "ops@test.com");

        AuditEvent src = baseEvent();

        wireHappyPath(policy, src);
        when(deduplicator.isAllowed(anyLong(), anyString(), anyLong(), anyInt()))
            .thenReturn(false);

        processor.handlePersistedEvent("1", src, Collections.emptyMap());

        verify(emailSender, never()).send(anyLong(), any(), any(), any(), anyBoolean());

        verify(auditWriter)
            .write(argThat(e -> e.getStep() == AuditStep.EMAIL_SUPPRESSED));
    }

    @Test
    public void dedupeCheckFailure_failsOpen_andAuditsPartial() {
        AlertPolicy policy =
            new AlertPolicy(true, AuditSeverity.ERROR, 5, AlertScope.FLOW, "ops@test.com");

        AuditEvent src = baseEvent();

        wireHappyPath(policy, src);
        when(deduplicator.isAllowed(anyLong(), anyString(), anyLong(), anyInt()))
            .thenThrow(new RuntimeException("db down"));

        processor.handlePersistedEvent("1", src, Collections.emptyMap());

        verify(auditWriter)
            .write(argThat(e -> e.getStep() == AuditStep.EMAIL_DEDUPE_CHECK_FAILED));

        verify(emailSender)
            .send(anyLong(), anyString(), anyString(), anyString(), anyBoolean());
    }

    // ---------- helpers ----------

    private void wireHappyPath(AlertPolicy policy, AuditEvent src) {
        when(policyResolver.resolve(any(), anyInt())).thenReturn(policy);
        when(policyResolver.isEligible(any(), any())).thenReturn(true);
        when(categoryResolver.resolve(src)).thenReturn(AlertEmailCategory.DT5_FAILURE);
        when(fingerprintBuilder.build(any(), eq(src), any())).thenReturn("fp");
        when(deduplicator.isAllowed(anyLong(), anyString(), anyLong(), anyInt()))
            .thenReturn(true);
        when(templateResolver.resolve(any()))
            .thenReturn(new AuditEmailTriggerDeps.TemplatePair("sub", "body"));
        when(modelBuilder.build(any(), any(), any()))
            .thenReturn(Collections.emptyMap());
    }

    private AuditEvent baseEvent() {
        return AuditEvent.builder()
            .companyId(1)
            .groupId(1)
            .userId(1)
            .category(AuditCategory.DT5_FLOW)
            .step(AuditStep.CRON_RECORD_FAILED)
            .severity(AuditSeverity.ERROR)
            .status(AuditStatus.FAILED)
            .correlationId("corr")
            .jobRunId("job")
            .eventId("evt")
            .requestId("req")
            .courseCode("C1")
            .ntucDTId(100L)
            .message("fail")
            .build();
    }
}
