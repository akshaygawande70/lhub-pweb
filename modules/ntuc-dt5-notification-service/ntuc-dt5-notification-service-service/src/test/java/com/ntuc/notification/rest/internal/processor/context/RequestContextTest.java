package com.ntuc.notification.rest.internal.processor.context;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequestContextTest {

    @Test
    public void withCourse_createsNewContextWithBusinessKeys() {
        RequestContext base =
            new RequestContext("corr-1", 1L, 2L, 3L, null, 0L);

        RequestContext courseCtx = base.withCourse("COURSE-1", 99L);

        assertEquals("corr-1", courseCtx.getCorrelationId());
        assertEquals(1L, courseCtx.getCompanyId());
        assertEquals(2L, courseCtx.getGroupId());
        assertEquals(3L, courseCtx.getUserId());
        assertEquals("COURSE-1", courseCtx.getCourseCode());
        assertEquals(99L, courseCtx.getNtucDTId());
    }

    @Test
    public void originalContext_isNotMutated() {
        RequestContext base =
            new RequestContext("corr-2", 10L, 20L, 30L, null, 0L);

        base.withCourse("C1", 1L);

        // After refactor: courseCode is normalized to "" (never null).
        assertEquals("", base.getCourseCode());
        assertEquals(0L, base.getNtucDTId());
    }
}
