package com.ntuc.notification.auditlog.portlet.render;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AuditLogPortletParamUtilTest {

    @Test
    public void getString_prefersNamespaced_whenPresent() {
        String ns = "_ns_";

        Map<String, String[]> params = new HashMap<>();
        params.put(ns + "category", new String[] {"DT5_FAILURE"});
        params.put("category", new String[] {"CLS_FAILURE"});

        String v = AuditLogPortletParamUtil.getString(params, ns, "category");
        assertEquals("DT5_FAILURE", v);
    }

    @Test
    public void getString_fallsBackToNonNamespaced_whenNamespacedMissing() {
        String ns = "_ns_";

        Map<String, String[]> params = new HashMap<>();
        params.put("category", new String[] {"CLS_FAILURE"});

        String v = AuditLogPortletParamUtil.getString(params, ns, "category");
        assertEquals("CLS_FAILURE", v);
    }

    @Test
    public void getString_returnsEmpty_whenMissing() {
        String ns = "_ns_";

        Map<String, String[]> params = new HashMap<>();

        String v = AuditLogPortletParamUtil.getString(params, ns, "category");
        assertEquals("", v);
    }

    @Test
    public void getString_trimsAndFallsBack() {
        String ns = "_ns_";

        Map<String, String[]> params = new HashMap<>();
        params.put(ns + "category", new String[] {"   "});
        params.put("category", new String[] {"  CLS_FAILURE "});

        String v = AuditLogPortletParamUtil.getString(params, ns, "category");
        assertEquals("CLS_FAILURE", v);
    }

    @Test
    public void getString_whenNsNull_fallsBackToNonNamespaced() {
        String ns = null;

        Map<String, String[]> params = new HashMap<>();
        params.put("category", new String[] {"CLS_FAILURE"});

        String v = AuditLogPortletParamUtil.getString(params, ns, "category");
        assertEquals("CLS_FAILURE", v);
    }

    @Test
    public void getInt_prefersNamespaced_whenPresent() {
        String ns = "_ns_";

        Map<String, String[]> params = new HashMap<>();
        params.put(ns + "delta", new String[] {"50"});
        params.put("delta", new String[] {"25"});

        int v = AuditLogPortletParamUtil.getInt(params, ns, "delta", 20);
        assertEquals(50, v);
    }

    @Test
    public void getInt_fallsBackToNonNamespaced_whenNamespacedMissing() {
        String ns = "_ns_";

        Map<String, String[]> params = new HashMap<>();
        params.put("delta", new String[] {"25"});

        int v = AuditLogPortletParamUtil.getInt(params, ns, "delta", 20);
        assertEquals(25, v);
    }

    @Test
    public void getInt_returnsDefault_whenMissing() {
        String ns = "_ns_";

        Map<String, String[]> params = new HashMap<>();

        int v = AuditLogPortletParamUtil.getInt(params, ns, "cur", 1);
        assertEquals(1, v);
    }

    @Test
    public void getInt_returnsDefault_whenNonNumeric() {
        String ns = "_ns_";

        Map<String, String[]> params = new HashMap<>();
        params.put(ns + "delta", new String[] {"nope"});

        int v = AuditLogPortletParamUtil.getInt(params, ns, "delta", 20);
        assertEquals(20, v);
    }

    @Test
    public void getInt_whenNsNull_fallsBackToNonNamespaced() {
        String ns = null;

        Map<String, String[]> params = new HashMap<>();
        params.put("delta", new String[] {"33"});

        int v = AuditLogPortletParamUtil.getInt(params, ns, "delta", 20);
        assertEquals(33, v);
    }
}
