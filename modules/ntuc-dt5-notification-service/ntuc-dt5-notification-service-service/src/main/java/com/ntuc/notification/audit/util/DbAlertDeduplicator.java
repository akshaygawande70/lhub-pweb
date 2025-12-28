package com.ntuc.notification.audit.util;

import java.util.Objects;

/**
 * DB-backed deduplicator for alert emails (cluster-safe).
 *
 * <p>
 * This class is intentionally pure Java and unit-testable.
 * It delegates the actual DB existence check to {@link RecentAlertOutcomeLookup}.
 * </p>
 *
 * <h3>Semantics</h3>
 * <ul>
 *   <li>If {@code windowMinutes <= 0}, dedupe is disabled and emails are allowed.</li>
 *   <li>If {@code companyId <= 0} or {@code nowMs <= 0}, the call is treated as non-dedupeable and allowed.</li>
 *   <li>{@code fingerprint} must be non-blank; it is trimmed and the trimmed value is used.</li>
 *   <li>Lookup is performed using {@code sinceStartTimeMs = nowMs - windowMinutes * 60_000}.</li>
 * </ul>
 */
public class DbAlertDeduplicator {

    /**
     * Abstraction for querying whether an alert outcome exists within a time window.
     *
     * <p>
     * Implementations must treat "recent outcome" as any persisted audit record outcome
     * that qualifies for dedupe (e.g., EMAIL_SENT or EMAIL_SUPPRESSED; excluding EMAIL_SEND_FAILED)
     * per the service-layer policy.
     * </p>
     */
    public interface RecentAlertOutcomeLookup {
        boolean existsRecentOutcome(long companyId, String fingerprint, long sinceStartTimeMs);
    }

    private final RecentAlertOutcomeLookup lookup;

    public DbAlertDeduplicator(RecentAlertOutcomeLookup lookup) {
        this.lookup = Objects.requireNonNull(lookup, "lookup");
    }

    /**
     * @return {@code true} if allowed to proceed (no recent outcome exists for the fingerprint)
     */
    public boolean isAllowed(long companyId, String fingerprint, long nowMs, int windowMinutes) {
        if (companyId <= 0) {
            return true;
        }

        String fp = (fingerprint == null) ? null : fingerprint.trim();
        if (fp == null || fp.isEmpty()) {
            throw new IllegalArgumentException("fingerprint must not be blank");
        }

        if (windowMinutes <= 0) {
            return true;
        }
        if (nowMs <= 0) {
            return true;
        }

        long windowMs = minutesToMs(windowMinutes);
        if (windowMs <= 0) {
            return true;
        }

        long since = nowMs - windowMs;
        if (since <= 0) {
            return true;
        }

        return !lookup.existsRecentOutcome(companyId, fp, since);
    }

    private static long minutesToMs(int minutes) {
        // Defensive overflow-safe conversion (though minutes is int, we keep the math in long).
        return minutes * 60_000L;
    }
}
