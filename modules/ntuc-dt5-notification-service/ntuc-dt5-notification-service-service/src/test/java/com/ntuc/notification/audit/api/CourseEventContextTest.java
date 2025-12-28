package com.ntuc.notification.audit.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link CourseEventContext}.
 *
 * Focus:
 * - Constructor defaulting and null-normalization
 * - Getter correctness
 * - Backward-compatible constructors
 */
@RunWith(MockitoJUnitRunner.class)
public class CourseEventContextTest {

    @Test
    public void legacyCtor_minimal_defaultsApplied() {
        CourseEventContext ctx =
                new CourseEventContext(1L, 2L, 3L, "COURSE-1");

        assertEquals(1L, ctx.getGroupId());
        assertEquals(2L, ctx.getCompanyId());
        assertEquals(3L, ctx.getUserId());
        assertEquals("COURSE-1", ctx.getCourseCode());

        assertEquals("", ctx.getEventType());
        assertNotNull(ctx.getChangeFromTypes());
        assertTrue(ctx.getChangeFromTypes().isEmpty());
        assertEquals(0L, ctx.getNtucDTId());
        assertNull(ctx.getArticleConfig());
    }

    @Test
    public void legacyCtor_withEventType_defaultsApplied() {
        CourseEventContext ctx =
                new CourseEventContext(10L, 20L, 30L, "COURSE-2", "CHANGED");

        assertEquals("COURSE-2", ctx.getCourseCode());
        assertEquals("CHANGED", ctx.getEventType());
        assertNotNull(ctx.getChangeFromTypes());
        assertTrue(ctx.getChangeFromTypes().isEmpty());
        assertEquals(0L, ctx.getNtucDTId());
        assertNull(ctx.getArticleConfig());
    }

    @Test
    public void convenienceCtor_withArticleConfig_setsConfig() {
        CourseArticleConfig config =
                new CourseArticleConfig(
                        123L,
                        "STRUCT_KEY",
                        "TPL_KEY",
                        "en_US",
                        "CREATED"
                );

        CourseEventContext ctx =
                new CourseEventContext(
                        5L,
                        6L,
                        7L,
                        "COURSE-3",
                        "CREATED",
                        Collections.singletonList("BASIC"),
                        config
                );

        assertEquals("COURSE-3", ctx.getCourseCode());
        assertEquals("CREATED", ctx.getEventType());
        assertEquals(Collections.singletonList("BASIC"), ctx.getChangeFromTypes());
        assertEquals(0L, ctx.getNtucDTId());
        assertSame(config, ctx.getArticleConfig());
    }

    @Test
    public void primaryCtor_allFieldsSet() {
        List<String> changeFrom = Arrays.asList("TITLE", "SCHEDULE");

        CourseArticleConfig config =
                new CourseArticleConfig(
                        456L,
                        "STRUCT_KEY_2",
                        "TPL_KEY_2",
                        "en_US",
                        "CHANGED"
                );

        CourseEventContext ctx =
                new CourseEventContext(
                        100L,
                        200L,
                        300L,
                        "COURSE-4",
                        "CHANGED",
                        changeFrom,
                        9999L,
                        config
                );

        assertEquals(100L, ctx.getGroupId());
        assertEquals(200L, ctx.getCompanyId());
        assertEquals(300L, ctx.getUserId());
        assertEquals("COURSE-4", ctx.getCourseCode());
        assertEquals("CHANGED", ctx.getEventType());
        assertEquals(changeFrom, ctx.getChangeFromTypes());
        assertEquals(9999L, ctx.getNtucDTId());
        assertSame(config, ctx.getArticleConfig());
    }

    @Test
    public void primaryCtor_nullStrings_areNormalized_andNullListBecomesEmpty() {
        CourseEventContext ctx =
                new CourseEventContext(
                        1L,
                        1L,
                        1L,
                        null,
                        null,
                        null,
                        0L,
                        null
                );

        assertEquals("", ctx.getCourseCode());
        assertEquals("", ctx.getEventType());
        assertNotNull(ctx.getChangeFromTypes());
        assertTrue(ctx.getChangeFromTypes().isEmpty());
        assertEquals(0L, ctx.getNtucDTId());
        assertNull(ctx.getArticleConfig());
    }
}
