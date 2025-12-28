package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import org.junit.Test;

import static org.junit.Assert.*;

public class AuditEmailFailureEventFactoryTest {

    @Test
    public void emailSendFailed_buildsStableEvent() {
        AuditEvent triggering = AuditEvent.builder()
                .companyId(1L)
                .groupId(2L)
                .userId(3L)
                .courseCode("C1")
                .ntucDTId(9L)
                .correlationId("corr")
                .jobRunId("jobrun")
                .eventId("event")
                .requestId("req")
                .severity(AuditSeverity.ERROR)
                .status(AuditStatus.FAILED)
                .step(AuditStep.CRON_RECORD_FAILED)
                .category(AuditCategory.DT5_FLOW)
                .message("x")
                .build();

        AuditEmailFailureEventFactory f = new AuditEmailFailureEventFactory();

        AuditEvent out = f.emailSendFailed(
                triggering,
                "ops@x.com",
                "subj",
                "fp",
                null,
                new RuntimeException("boom")
        );

        assertEquals(AuditStep.EMAIL_SEND_FAILED, out.getStep());
        assertEquals(AuditErrorCode.EMAIL_SEND_FAILED, out.getErrorCode());
        assertEquals(AuditStatus.FAILED, out.getStatus());
        assertEquals(AuditSeverity.ERROR, out.getSeverity());
        assertEquals("ops@x.com", out.getDetails().get("toEmail"));
        assertEquals("fp", out.getDetails().get("dedupeFingerprint"));
    }
}
