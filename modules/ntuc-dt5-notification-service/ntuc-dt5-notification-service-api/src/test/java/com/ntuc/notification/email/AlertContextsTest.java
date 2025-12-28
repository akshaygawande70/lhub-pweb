package com.ntuc.notification.email;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AlertContextsTest {

    @Test
    public void clsApiUnreachable_populatesExpectedFieldsAndDefaults() {
        Map<String, Object> counts = new HashMap<>();
        counts.put("total", 5);

        Map<String, Object> ctx = AlertContexts.clsApiUnreachable(
                503,
                "/api/path",
                120_000L,
                3,
                counts,
                "fixed interval",
                null,
                null
        );

        assertEquals("CRITICAL", ctx.get("severity"));
        assertEquals("CLS API Unreachable", ctx.get("alertType"));
        assertEquals("DT5", ctx.get("system")); // defaulted
        assertEquals("prod", ctx.get("env"));   // defaulted
        assertEquals(503, ctx.get("httpStatus"));
        assertEquals(counts, ctx.get("counts"));
        assertEquals("DevOps, Integration Team", ctx.get("notifyGroup"));
        assertFalse(ctx.containsKey("unknown"));
    }

    @Test
    public void criticalFieldMissing_skipsNullAndEmptyValues() {
        Map<String, Object> ctx = AlertContexts.criticalFieldMissing(
                Arrays.asList("courseCode"),
                "   ", // should be skipped
                "SYS",
                "UAT"
        );

        assertEquals("HIGH", ctx.get("severity"));
        assertEquals("SYS", ctx.get("system"));
        assertEquals("UAT", ctx.get("env"));
        assertEquals(Collections.singletonList("courseCode"), ctx.get("criticalFields"));
        assertFalse("Blank payload should be omitted", ctx.containsKey("payloadSnippet"));
    }

    @Test
    public void courseSyncAborted_buildsCountsMapAndOmitNulls() {
        Map<String, Object> ctx = AlertContexts.courseSyncAborted(
                2L,
                4L,
                null,
                "boom",
                null,
                "SYS",
                "UAT"
        );

        assertEquals("Course Sync Aborted Mid-Run", ctx.get("alertType"));
        assertEquals("boom", ctx.get("error"));
        assertEquals("SYS", ctx.get("system"));
        assertEquals("UAT", ctx.get("env"));

        @SuppressWarnings("unchecked")
        Map<String, Object> counts = (Map<String, Object>) ctx.get("counts");
        assertEquals(2L, counts.get("processed"));
        assertEquals(4L, counts.get("total"));
        assertFalse("Null failed count must be omitted", counts.containsKey("failed"));
    }

    @Test
    public void journalArticleUpdateFailed_acceptsLinksList() {
        List<Map<String, String>> links = Collections.singletonList(Collections.singletonMap("type", "retry"));

        Map<String, Object> ctx = AlertContexts.journalArticleUpdateFailed(
                "C101",
                "validation",
                "stacktrace",
                links,
                "SYS",
                "DEV"
        );

        assertEquals("C101", ctx.get("courseCode"));
        assertEquals("validation", ctx.get("reason"));
        assertEquals(links, ctx.get("links"));
        assertEquals("DEV", ctx.get("env"));
    }
}
