package com.ntuc.notification.portlet.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UpdateScheduleParamUtilTest {

    // -----------------------
    // normalizeYesNoBlank()
    // -----------------------

    @Test
    public void normalizeYesNoBlank_null_returnsBlank() {
        assertEquals("", UpdateScheduleParamUtil.normalizeYesNoBlank(null));
    }

    @Test
    public void normalizeYesNoBlank_empty_returnsBlank() {
        assertEquals("", UpdateScheduleParamUtil.normalizeYesNoBlank(""));
        assertEquals("", UpdateScheduleParamUtil.normalizeYesNoBlank("   "));
    }

    @Test
    public void normalizeYesNoBlank_yes_variants_returnYES() {
        assertEquals("YES", UpdateScheduleParamUtil.normalizeYesNoBlank("YES"));
        assertEquals("YES", UpdateScheduleParamUtil.normalizeYesNoBlank("yes"));
        assertEquals("YES", UpdateScheduleParamUtil.normalizeYesNoBlank("  YeS  "));
    }

    @Test
    public void normalizeYesNoBlank_no_variants_returnNO() {
        assertEquals("NO", UpdateScheduleParamUtil.normalizeYesNoBlank("NO"));
        assertEquals("NO", UpdateScheduleParamUtil.normalizeYesNoBlank("no"));
        assertEquals("NO", UpdateScheduleParamUtil.normalizeYesNoBlank("  nO  "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void normalizeYesNoBlank_invalid_throws() {
        UpdateScheduleParamUtil.normalizeYesNoBlank("MAYBE");
    }

    @Test(expected = IllegalArgumentException.class)
    public void normalizeYesNoBlank_numeric_throws() {
        UpdateScheduleParamUtil.normalizeYesNoBlank("1");
    }

    // -----------------------
    // resolveNamespace()
    // -----------------------

    @Test
    public void resolveNamespace_emptyParamMap_returnsEmpty() {
        assertEquals("", UpdateScheduleParamUtil.resolveNamespace(new HashMap<String, String[]>(), "courseScheduleId"));
    }

    @Test
    public void resolveNamespace_nullParamMap_returnsEmpty() {
        assertEquals("", UpdateScheduleParamUtil.resolveNamespace(null, "courseScheduleId"));
    }

    @Test
    public void resolveNamespace_findsPrefixFromNamespacedKey() {
        Map<String, String[]> pm = new HashMap<>();
        pm.put("_ns_courseScheduleId", new String[] { "123" });

        assertEquals("_ns_", UpdateScheduleParamUtil.resolveNamespace(pm, "courseScheduleId"));
    }

    @Test
    public void resolveNamespace_choosesAnyMatchingSuffix() {
        Map<String, String[]> pm = new HashMap<>();
        pm.put("somethingElse", new String[] { "x" });
        pm.put("_ntuc_portlet_courseScheduleId", new String[] { "999" });

        assertEquals("_ntuc_portlet_", UpdateScheduleParamUtil.resolveNamespace(pm, "courseScheduleId"));
    }

    @Test
    public void resolveNamespace_whenNoSuffixMatch_returnsEmpty() {
        Map<String, String[]> pm = new HashMap<>();
        pm.put("_ns_otherField", new String[] { "123" });

        assertEquals("", UpdateScheduleParamUtil.resolveNamespace(pm, "courseScheduleId"));
    }
}
