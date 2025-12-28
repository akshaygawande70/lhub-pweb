package com.ntuc.notification.util.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.ntuc.notification.model.ScheduleResponse;

/**
 * Simple in-memory cache for course schedules.
 * Per-JVM only, TTL based.
 */
public class ScheduleCache {

    private static final class Entry {
        final ScheduleResponse response;
        final long cachedAtMs;

        Entry(ScheduleResponse response, long cachedAtMs) {
            this.response = response;
            this.cachedAtMs = cachedAtMs;
        }
    }

    private final ConcurrentMap<String, Entry> cache = new ConcurrentHashMap<>();
    private final long maxAgeMs;

    public ScheduleCache(long maxAgeMs) {
        this.maxAgeMs = maxAgeMs;
    }

    public void put(String courseCode, ScheduleResponse response) {
        if (courseCode == null || courseCode.isEmpty() || response == null) return;
        cache.put(courseCode, new Entry(response, System.currentTimeMillis()));
    }

    public ScheduleResponse getIfFresh(String courseCode) {
        if (courseCode == null || courseCode.isEmpty()) return null;
        Entry entry = cache.get(courseCode);
        if (entry == null) return null;

        long age = System.currentTimeMillis() - entry.cachedAtMs;
        if (age > maxAgeMs) {
            cache.remove(courseCode, entry);
            return null;
        }
        return entry.response;
    }

    public void invalidate(String courseCode) {
        if (courseCode == null || courseCode.isEmpty()) return;
        cache.remove(courseCode);
    }

    public void clear() {
        cache.clear();
    }
}
