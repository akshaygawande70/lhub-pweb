package com.ntuc.notification.audit.internal.email;

import static org.junit.Assert.assertEquals;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AlertScope;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.email.api.AlertEmailCategory;

import org.junit.Test;

public class AlertFingerprintBuilderTest {

    private final AlertFingerprintBuilder builder = new AlertFingerprintBuilder();

    @Test
    public void build_flowScope_usesCorrelationId_andNormalizesMessage() {
        AlertPolicy policy = new AlertPolicy(true, AuditSeverity.ERROR, 10, AlertScope.FLOW, "fallback@example.com");

        AuditEvent e = baseEvent()
            .correlationId("corr-1")
            .errorCode(AuditErrorCode.CLS_CONNECTION_FAILED)
            .exceptionClass("com.foo.TimeoutException")
            .errorMessage("Read timed out for https://api.host/x?id=123")
            .build();

        String fp = builder.build(policy, e, AlertEmailCategory.CLS_FAILURE);

        String expected =
            "CLS_FAILURE|CLS_CONNECTION_FAILED|com.foo.TimeoutException|read timed out for <url>|corr-1";

        assertEquals(expected, fp);
    }

    @Test
    public void build_courseScope_usesCourseCode() {
        AlertPolicy policy = new AlertPolicy(true, AuditSeverity.ERROR, 10, AlertScope.COURSE, "fallback@example.com");

        AuditEvent e = baseEvent()
            .courseCode("ABC123")
            .errorCode(AuditErrorCode.CLS_CONNECTION_FAILED)
            .exceptionClass("com.foo.TimeoutException")
            .errorMessage("Timeout for course=ABC123 correlation=7224f34b-26ce-42e8-85de-809ee21874df")
            .build();

        String fp = builder.build(policy, e, AlertEmailCategory.CLS_FAILURE);

        String expected =
            "CLS_FAILURE|CLS_CONNECTION_FAILED|com.foo.TimeoutException|" +
            "timeout for course=abc<num> correlation=<uuid>|ABC123";

        assertEquals(expected, fp);
    }

    @Test
    public void build_jobScope_usesJobRunId() {
        AlertPolicy policy = new AlertPolicy(true, AuditSeverity.ERROR, 10, AlertScope.JOB, "fallback@example.com");

        AuditEvent e = baseEvent()
            .jobRunId("job-777")
            .errorCode(AuditErrorCode.CRON_RECORD_FAILED)
            .exceptionClass("java.lang.IllegalArgumentException")
            .errorMessage("notificationId is required (job=5555)")
            .build();

        String fp = builder.build(policy, e, AlertEmailCategory.DT5_FAILURE);

        String expected =
            "DT5_FAILURE|CRON_RECORD_FAILED|java.lang.IllegalArgumentException|" +
            "notificationid is required (job=<num>)|job-777";

        assertEquals(expected, fp);
    }

    @Test
    public void build_nullEvent_doesNotThrow_andIsDeterministic() {
        AlertPolicy policy = new AlertPolicy(true, AuditSeverity.ERROR, 10, AlertScope.FLOW, "fallback@example.com");

        String fp = builder.build(policy, null, AlertEmailCategory.DT5_FAILURE);

        // category set, all other components empty, msg normalizes to 'unknown'
        assertEquals("DT5_FAILURE||unknown|", fp);
    }

    private static AuditEvent.Builder baseEvent() {
        return AuditEvent.builder()
            .companyId(20097)
            .groupId(39367)
            .userId(0)
            .severity(AuditSeverity.ERROR)
            .status(AuditStatus.FAILED)
            .category(AuditCategory.DT5_FLOW)
            .step(AuditStep.CRON_RECORD_FAILED)
            .message("x")
            .correlationId("corr-default");
    }
}
