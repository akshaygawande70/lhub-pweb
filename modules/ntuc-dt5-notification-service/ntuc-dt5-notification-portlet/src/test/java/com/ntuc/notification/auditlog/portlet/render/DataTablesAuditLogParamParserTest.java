package com.ntuc.notification.auditlog.portlet.render;

import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.dto.AuditLogSearchRequest;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DataTablesAuditLogParamParserTest {

    @Test
    public void parse_namespacedSeverityMultiValue_lastWins() {
        Map<String, String[]> p = new HashMap<>();

        String ns = "_ntuc_dt5_audit_log_browser_";
        p.put(ns + "draw", new String[] {"6"}); // also used for ns detection

        // Simulates: query string has ERROR, POST body has INFO
        p.put(ns + "severity", new String[] {"ERROR", "INFO"});

        AuditLogSearchRequest req = DataTablesAuditLogParamParser.parse(p, 20097L, 12345L);

        assertNotNull(req);
        assertEquals(20097L, req.getCompanyId());
        assertEquals(12345L, req.getGroupId());
        assertEquals(6, req.getDraw());
        assertEquals(AuditSeverity.INFO, req.getSeverity());
    }

    @Test
    public void parse_namespacedSeverityMultiValue_blankThenValue_lastNonBlankWins() {
        Map<String, String[]> p = new HashMap<>();

        String ns = "_ntuc_dt5_audit_log_browser_";
        p.put(ns + "draw", new String[] {"1"});

        // Must skip blanks and still pick the last non-blank deterministically
        p.put(ns + "severity", new String[] {"ERROR", "   ", "", "INFO"});

        AuditLogSearchRequest req = DataTablesAuditLogParamParser.parse(p, 1L, 2L);

        assertNotNull(req);
        assertEquals(AuditSeverity.INFO, req.getSeverity());
    }

    @Test
    public void parse_prefersNamespacedOverNonNamespaced() {
        Map<String, String[]> p = new HashMap<>();

        String ns = "_ntuc_dt5_audit_log_browser_";
        p.put(ns + "draw", new String[] {"1"});

        p.put("severity", new String[] {"ERROR"});
        p.put(ns + "severity", new String[] {"INFO"});

        AuditLogSearchRequest req = DataTablesAuditLogParamParser.parse(p, 1L, 2L);

        assertNotNull(req);
        assertEquals(AuditSeverity.INFO, req.getSeverity());
    }

    @Test
    public void parse_namespacedBlank_fallsBackToNonNamespaced() {
        Map<String, String[]> p = new HashMap<>();

        String ns = "_ntuc_dt5_audit_log_browser_";
        p.put(ns + "draw", new String[] {"1"});

        // Namespaced exists but blank => must fall back to non-namespaced
        p.put(ns + "severity", new String[] {"   "});
        p.put("severity", new String[] {"ERROR"});

        AuditLogSearchRequest req = DataTablesAuditLogParamParser.parse(p, 1L, 2L);

        assertNotNull(req);
        assertEquals(AuditSeverity.ERROR, req.getSeverity());
    }

    @Test
    public void parse_invalidEnum_returnsNull_notException() {
        Map<String, String[]> p = new HashMap<>();

        String ns = "_ntuc_dt5_audit_log_browser_";
        p.put(ns + "draw", new String[] {"1"});

        p.put(ns + "severity", new String[] {"NOT_A_REAL_SEVERITY"});

        AuditLogSearchRequest req = DataTablesAuditLogParamParser.parse(p, 1L, 2L);

        assertNotNull(req);
        assertNull(req.getSeverity());
    }

    @Test
    public void parse_qFallsBackToSearchValue_namespaced() {
        Map<String, String[]> p = new HashMap<>();

        String ns = "_ntuc_dt5_audit_log_browser_";
        p.put(ns + "draw", new String[] {"2"});

        p.put(ns + "search[value]", new String[] {"  correlationId-123  "});

        AuditLogSearchRequest req = DataTablesAuditLogParamParser.parse(p, 1L, 2L);

        assertNotNull(req);
        assertEquals("correlationId-123", req.getQ());
    }

    @Test
    public void parse_qFallsBackToSearchValue_nonNamespaced() {
        Map<String, String[]> p = new HashMap<>();

        String ns = "_ntuc_dt5_audit_log_browser_";
        p.put(ns + "draw", new String[] {"2"});

        // Your str() method falls back to non-namespaced automatically
        p.put("search[value]", new String[] {"  jobRunId-777  "});

        AuditLogSearchRequest req = DataTablesAuditLogParamParser.parse(p, 1L, 2L);

        assertNotNull(req);
        assertEquals("jobRunId-777", req.getQ());
    }

    @Test
    public void parse_qPrefersExplicitQOverSearchValue() {
        Map<String, String[]> p = new HashMap<>();

        String ns = "_ntuc_dt5_audit_log_browser_";
        p.put(ns + "draw", new String[] {"3"});

        p.put(ns + "q", new String[] {"  ERROR  "});
        p.put(ns + "search[value]", new String[] {"INFO"});

        AuditLogSearchRequest req = DataTablesAuditLogParamParser.parse(p, 1L, 2L);

        assertNotNull(req);
        assertEquals("ERROR", req.getQ());
    }

    @Test
    public void parse_defaults_startAndLength_whenMissing() {
        Map<String, String[]> p = new HashMap<>();

        String ns = "_ntuc_dt5_audit_log_browser_";
        p.put(ns + "draw", new String[] {"9"});

        AuditLogSearchRequest req = DataTablesAuditLogParamParser.parse(p, 1L, 2L);

        assertNotNull(req);
        assertEquals(9, req.getDraw());
        assertEquals(0, req.getStart());
        assertEquals(25, req.getLength());
    }

    @Test
    public void parse_readsStartAndLength_whenPresent() {
        Map<String, String[]> p = new HashMap<>();

        String ns = "_ntuc_dt5_audit_log_browser_";
        p.put(ns + "draw", new String[] {"9"});
        p.put(ns + "start", new String[] {"50"});
        p.put(ns + "length", new String[] {"10"});

        AuditLogSearchRequest req = DataTablesAuditLogParamParser.parse(p, 1L, 2L);

        assertNotNull(req);
        assertEquals(50, req.getStart());
        assertEquals(10, req.getLength());
    }
}
