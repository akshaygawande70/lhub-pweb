package com.ntuc.notification.audit.internal;

import static org.junit.Assert.*;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.model.AuditLog;
import com.ntuc.notification.model.impl.AuditLogImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class AuditEventDbMapperTest {

    private final AuditEventDbMapper mapper = new AuditEventDbMapper();

    @Test
    public void mapInto_nonEmailCategory_doesNotSetAlertFingerprint() {
        AuditLog row = new AuditLogImpl();

        AuditEvent e = AuditEvent.builder()
            .companyId(1)
            .groupId(2)
            .userId(3)
            .category(AuditCategory.DT5_FLOW)
            .step(AuditStep.CRON_RECORD_FAILED)
            .severity(AuditSeverity.ERROR)
            .status(AuditStatus.FAILED)
            .message("m")
            .build();

        mapper.mapInto(row, e);

        assertBlank(row.getAlertFingerprint());
    }

    @Test
    public void mapInto_emailOutcome_setsHashedAlertFingerprint64() {
        AuditLog row = new AuditLogImpl();

        Map<String, String> details = new HashMap<String, String>();
        details.put("fingerprint", "DT5_FAILURE|X|Y|very long message very long message|corr-1");

        AuditEvent e = AuditEvent.builder()
            .companyId(1)
            .groupId(2)
            .userId(3)
            .category(AuditCategory.ALERT_EMAIL)
            .step(AuditStep.EMAIL_SENT)
            .severity(AuditSeverity.INFO)
            .status(AuditStatus.SUCCESS)
            .message("sent")
            .details(details)
            .build();

        mapper.mapInto(row, e);

        assertNotNull(row.getAlertFingerprint());
        assertEquals(64, row.getAlertFingerprint().length());
        assertTrue(row.getAlertFingerprint().matches("^[0-9a-f]{64}$"));
    }

    @Test
    public void mapInto_emailOutcome_usesDedupeFingerprintFallbackKey() {
        AuditLog row = new AuditLogImpl();

        Map<String, String> details = new HashMap<String, String>();
        details.put("dedupeFingerprint", "CLS_FAILURE|A|B|msg|corr-1");

        AuditEvent e = AuditEvent.builder()
            .companyId(1)
            .groupId(2)
            .userId(3)
            .category(AuditCategory.ALERT_EMAIL)
            .step(AuditStep.EMAIL_SUPPRESSED)
            .severity(AuditSeverity.INFO)
            .status(AuditStatus.SKIPPED)
            .message("suppressed")
            .details(details)
            .build();

        mapper.mapInto(row, e);

        assertNotNull(row.getAlertFingerprint());
        assertEquals(64, row.getAlertFingerprint().length());
        assertTrue(row.getAlertFingerprint().matches("^[0-9a-f]{64}$"));
    }

    @Test
    public void mapInto_alertEmail_nonOutcomeStep_doesNotSetAlertFingerprint() {
        AuditLog row = new AuditLogImpl();

        Map<String, String> details = new HashMap<String, String>();
        details.put("fingerprint", "x");

        AuditEvent e = AuditEvent.builder()
            .companyId(1)
            .groupId(2)
            .userId(3)
            .category(AuditCategory.ALERT_EMAIL)
            .step(AuditStep.CRON_RECORD_FAILED) // not an EMAIL_* outcome
            .severity(AuditSeverity.ERROR)
            .status(AuditStatus.FAILED)
            .message("m")
            .details(details)
            .build();

        mapper.mapInto(row, e);

        assertBlank(row.getAlertFingerprint());
    }

    private static void assertBlank(String s) {
        assertTrue("Expected blank but was: [" + s + "]", s == null || s.trim().isEmpty());
    }
}
