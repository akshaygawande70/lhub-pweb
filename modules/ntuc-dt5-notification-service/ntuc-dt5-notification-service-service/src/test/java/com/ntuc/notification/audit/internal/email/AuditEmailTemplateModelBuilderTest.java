package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AuditEmailTemplateModelBuilderTest {

    private final AuditEmailTemplateModelBuilder builder =
        new AuditEmailTemplateModelBuilder();

    @Test
    public void build_populatesCoreTokens() {
        AuditEvent e = baseEvent();
        Map<String, String> m = builder.build(e, "123", "fp1");

        assertNotNull(m);
        assertEquals("123", m.get(AuditEmailTemplateModelBuilder.T_AUDIT_ID));
        assertEquals("corr-1", m.get(AuditEmailTemplateModelBuilder.T_CORRELATION_ID));
        assertEquals("COURSE1", m.get(AuditEmailTemplateModelBuilder.T_COURSE_CODE));
        assertEquals("100", m.get(AuditEmailTemplateModelBuilder.T_NTUC_DT_ID));
    }

    @Test
    public void build_usesEndTimeElseStartTime() {
        AuditEvent e = baseEvent();
        Map<String, String> m = builder.build(e, "1", "fp");

        assertNotNull(m);
        String ts = m.get(AuditEmailTemplateModelBuilder.T_TIMESTAMP);
        assertNotNull(ts);
        assertTrue(ts.contains("UTC"));
    }

    @Test
    public void build_handlesMissingTimes() {
        // IMPORTANT:
        // Do NOT use AuditEvent.builder().build() because AuditEvent's builder may not allow fully empty events.
        // We only need "missing times" (0 / unset), not a totally empty object.
        AuditEvent e = eventWithoutTimes();

        Map<String, String> m = builder.build(e, "1", "fp");

        assertNotNull(m);

        // Hard assertion: token key must exist and never be null.
        assertTrue(m.containsKey(AuditEmailTemplateModelBuilder.T_TIMESTAMP));
        assertNotNull(m.get(AuditEmailTemplateModelBuilder.T_TIMESTAMP));

        assertEquals("", m.get(AuditEmailTemplateModelBuilder.T_TIMESTAMP));
    }

    @Test
    public void build_populatesDetailTokens() {
        AuditEvent e = baseEvent();
        Map<String, String> m = builder.build(e, "1", "fp");

        assertNotNull(m);
        assertEquals("/endpoint", m.get(AuditEmailTemplateModelBuilder.T_ENDPOINT));
        assertEquals("500", m.get(AuditEmailTemplateModelBuilder.T_RESPONSE_CODE));
        assertEquals("STRUCT1", m.get(AuditEmailTemplateModelBuilder.T_DDM_STRUCTURE_KEY));
        assertEquals("TPL1", m.get(AuditEmailTemplateModelBuilder.T_DDM_TEMPLATE_KEY));
    }

    @Test
    public void build_handlesNullEventSafely() {
        Map<String, String> m = builder.build(null, "1", null);

        assertNotNull(m);
        assertEquals("1", m.get(AuditEmailTemplateModelBuilder.T_AUDIT_ID));
        assertEquals("", m.get(AuditEmailTemplateModelBuilder.T_CORRELATION_ID));
        assertEquals("", m.get(AuditEmailTemplateModelBuilder.T_TIMESTAMP));
    }

    private AuditEvent baseEvent() {
        Map<String, String> details = new HashMap<String, String>();
        details.put("endpoint", "/endpoint");
        details.put("responseCode", "500");
        details.put("ddmStructureKey", "STRUCT1");
        details.put("ddmTemplateKey", "TPL1");

        return AuditEvent.builder()
            .companyId(1)
            .groupId(1)
            .userId(1)
            .category(AuditCategory.DT5_FLOW)
            .step(AuditStep.CRON_RECORD_FAILED)
            .severity(AuditSeverity.ERROR)
            .status(AuditStatus.FAILED)
            .correlationId("corr-1")
            .courseCode("COURSE1")
            .ntucDTId(100L)
            .startTimeMs(System.currentTimeMillis() - 1000)
            .endTimeMs(System.currentTimeMillis())
            .details(details)
            .build();
    }

    private AuditEvent eventWithoutTimes() {
        // Provide mandatory-ish fields (based on your baseEvent usage),
        // but omit timestamps logically by setting them to 0.
        return AuditEvent.builder()
            .companyId(1)
            .groupId(1)
            .userId(1)
            .category(AuditCategory.DT5_FLOW)
            .step(AuditStep.CRON_RECORD_FAILED)
            .severity(AuditSeverity.ERROR)
            .status(AuditStatus.FAILED)
            .correlationId("corr-1")
            .courseCode("COURSE1")
            .ntucDTId(100L)
            .startTimeMs(0L)
            .endTimeMs(0L)
            .details(new HashMap<String, String>())
            .build();
    }
}
