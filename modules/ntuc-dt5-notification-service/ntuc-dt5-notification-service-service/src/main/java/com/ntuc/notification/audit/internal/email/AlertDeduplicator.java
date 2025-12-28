package com.ntuc.notification.audit.internal.email;

import java.time.Clock;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * In-memory deduplicator for alert emails.
 *
 * Public API (aligned to production call sites):
 * - new AlertDeduplicator()
 * - tryAcquire(String fingerprint, int windowMinutes)
 *
 * Tests may use the package-private Clock constructor to control time.
 */
public class AlertDeduplicator {

    private final Clock clock;
    private final ConcurrentHashMap<String, Long> lastSeenByFingerprint = new ConcurrentHashMap<>();

    /**
     * Production constructor: uses system clock.
     */
    public AlertDeduplicator() {
        this(Clock.systemUTC());
    }

    /**
     * Package-private constructor for unit tests (deterministic time).
     */
    AlertDeduplicator(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    /**
     * @param fingerprint dedupe key (required)
     * @param windowMinutes dedupe window in minutes (<=0 disables dedupe)
     * @return true if allowed to proceed; false if suppressed within the window
     */
    public boolean tryAcquire(String fingerprint, int windowMinutes) {
        if (fingerprint == null || fingerprint.trim().isEmpty()) {
            throw new IllegalArgumentException("fingerprint must not be blank");
        }

        if (windowMinutes <= 0) {
            return true;
        }

        long windowMs = windowMinutes * 60_000L;
        long now = clock.millis();

        evictExpired(now, windowMs);

        AtomicBoolean allowed = new AtomicBoolean(false);

        lastSeenByFingerprint.compute(fingerprint, (k, prev) -> {
            if (prev == null) {
                allowed.set(true);
                return now;
            }

            long age = now - prev;
            if (age >= windowMs) {
                allowed.set(true);
                return now;
            }

            allowed.set(false);
            return prev;
        });

        return allowed.get();
    }

    private void evictExpired(long now, long windowMs) {
        Iterator<Map.Entry<String, Long>> it = lastSeenByFingerprint.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Long> e = it.next();
            Long ts = e.getValue();
            if (ts == null || (now - ts) >= windowMs) {
                it.remove();
            }
        }
    }
}
