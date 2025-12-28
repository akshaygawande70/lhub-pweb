package com.ntuc.notification.util.cache;

import static org.junit.Assert.*;

import com.ntuc.notification.model.ScheduleResponse;
import org.junit.Test;

public class ScheduleCacheTest {

    private static final String COURSE = "C101";

    @Test
    public void getIfFresh_returnsCachedValue_whenNotExpired() {
        ScheduleCache cache = new ScheduleCache(1_000L);
        ScheduleResponse response = sampleResponse();

        cache.put(COURSE, response);

        ScheduleResponse cached = cache.getIfFresh(COURSE);
        assertSame(response, cached);
    }

    @Test
    public void getIfFresh_afterExpiry_returnsNullAndEvicts() throws Exception {
        ScheduleCache cache = new ScheduleCache(1L);
        cache.put(COURSE, sampleResponse());

        Thread.sleep(5L);

        assertNull(cache.getIfFresh(COURSE));
        // Second lookup should also be null since entry was evicted
        assertNull(cache.getIfFresh(COURSE));
    }

    @Test
    public void put_orGetWithNullOrBlankCourse_areNoOps() {
        ScheduleCache cache = new ScheduleCache(1_000L);
        ScheduleResponse response = sampleResponse();

        cache.put(null, response);
        cache.put("", response);
        cache.put("  ", response);

        assertNull(cache.getIfFresh(null));
        assertNull(cache.getIfFresh(""));
        assertNull(cache.getIfFresh("  "));
    }

    @Test
    public void invalidate_removesCachedEntry() {
        ScheduleCache cache = new ScheduleCache(1_000L);
        cache.put(COURSE, sampleResponse());

        cache.invalidate(COURSE);

        assertNull(cache.getIfFresh(COURSE));
    }

    private static ScheduleResponse sampleResponse() {
        return new ScheduleResponse(
                "intake",
                "2024-01-01",
                "2024-01-02",
                null,
                10,
                "venue",
                "desc",
                "pax",
                "wait",
                "buy",
                "roi",
                "note",
                "download",
                null,
                false
        );
    }
}
