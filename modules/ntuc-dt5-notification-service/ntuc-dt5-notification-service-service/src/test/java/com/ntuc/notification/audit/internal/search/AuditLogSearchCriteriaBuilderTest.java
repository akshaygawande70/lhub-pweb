package com.ntuc.notification.audit.internal.search;

import com.ntuc.notification.audit.api.dto.AuditLogSearchRequest;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AuditLogSearchCriteriaBuilderTest {

    @Test
    public void build_setsAuditLogIdEq() {
        AuditLogSearchRequest r = new AuditLogSearchRequest();
        r.setAuditLogId(Long.valueOf(10L));

        AuditLogSearchCriteria c = AuditLogSearchCriteriaBuilder.build(r);

        assertEquals(Long.valueOf(10L), c.getAuditLogIdEq());
    }

    @Test
    public void build_globalSearch_includesErrorCodeAndErrorMessage() {
        AuditLogSearchRequest r = new AuditLogSearchRequest();
        r.setQ("CRON_RECORD_FAILED");

        AuditLogSearchCriteria c = AuditLogSearchCriteriaBuilder.build(r);

        assertNotNull(c.getGlobalSearch());
        List<String> fields = c.getGlobalSearch().getFields();

        assertTrue(fields.contains("message"));
        assertTrue(fields.contains("errorCode"));
        assertTrue(fields.contains("errorMessage"));
        assertTrue(fields.contains("correlationId"));
        assertTrue(fields.contains("jobRunId"));
        assertTrue(fields.contains("courseCode"));
    }
}
