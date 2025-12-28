package com.ntuc.notification.audit.internal.email;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.Test;

public class AlertDeduplicatorTest {

    @Test
    public void tryAcquire_firstTimeTrue_secondTimeFalseWithinWindow() {
        Clock clock = Clock.fixed(Instant.parse("2025-12-21T00:00:00Z"), ZoneOffset.UTC);
        AlertDeduplicator d = new AlertDeduplicator(clock);

        assertTrue(d.tryAcquire("fp-1", 1));
        assertFalse(d.tryAcquire("fp-1", 1));
    }

    @Test
    public void tryAcquire_afterWindow_allowsAgain() {
        MutableClock clock = new MutableClock(Instant.parse("2025-12-21T00:00:00Z"));
        AlertDeduplicator d = new AlertDeduplicator(clock);

        assertTrue(d.tryAcquire("fp-1", 1));
        clock.plusMillis(60_000);
        assertTrue(d.tryAcquire("fp-1", 1));
    }

    private static final class MutableClock extends Clock {
        private Instant instant;

        private MutableClock(Instant instant) {
            this.instant = instant;
        }

        void plusMillis(long ms) {
            instant = instant.plusMillis(ms);
        }

        @Override
        public ZoneOffset getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(java.time.ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
