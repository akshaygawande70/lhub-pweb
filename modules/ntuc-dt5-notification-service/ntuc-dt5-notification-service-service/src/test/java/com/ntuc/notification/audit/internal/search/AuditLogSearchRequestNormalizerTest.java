package com.ntuc.notification.audit.internal.search;

import com.ntuc.notification.audit.api.dto.AuditLogSearchRequest;

import org.junit.Test;

import static org.junit.Assert.*;

public class AuditLogSearchRequestNormalizerTest {

    @Test
    public void normalize_numericQ_setsAuditLogId_andClearsQ() {
        AuditLogSearchRequest r = new AuditLogSearchRequest();
        r.setQ("354644");

        AuditLogSearchRequest out = AuditLogSearchRequestNormalizer.normalize(r);

        assertEquals(Long.valueOf(354644L), out.getAuditLogId());
        assertNull(out.getQ());
    }

    @Test
    public void normalize_textQ_keepsQ() {
        AuditLogSearchRequest r = new AuditLogSearchRequest();
        r.setQ("CRON_RECORD_FAILED");

        AuditLogSearchRequest out = AuditLogSearchRequestNormalizer.normalize(r);

        assertNull(out.getAuditLogId());
        assertEquals("CRON_RECORD_FAILED", out.getQ());
    }
}
