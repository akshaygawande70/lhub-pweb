package com.ntuc.notification.audit.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DbAlertDeduplicatorTest {

    @Mock
    private DbAlertDeduplicator.RecentAlertOutcomeLookup lookup;

    @Test
    public void isAllowed_companyIdNonPositive_returnsTrue_noLookup() {
        DbAlertDeduplicator d = new DbAlertDeduplicator(lookup);

        assertTrue(d.isAllowed(0L, "fp", 1000L, 10));
        assertTrue(d.isAllowed(-1L, "fp", 1000L, 10));

        verifyNoInteractions(lookup);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isAllowed_fingerprintNull_throws() {
        DbAlertDeduplicator d = new DbAlertDeduplicator(lookup);
        d.isAllowed(20097L, null, 1000L, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isAllowed_fingerprintBlank_throws() {
        DbAlertDeduplicator d = new DbAlertDeduplicator(lookup);
        d.isAllowed(20097L, "   ", 1000L, 10);
    }

    @Test
    public void isAllowed_windowMinutesNonPositive_returnsTrue_noLookup() {
        DbAlertDeduplicator d = new DbAlertDeduplicator(lookup);

        assertTrue(d.isAllowed(20097L, "fp", 1000L, 0));
        assertTrue(d.isAllowed(20097L, "fp", 1000L, -5));

        verifyNoInteractions(lookup);
    }

    @Test
    public void isAllowed_nowMsNonPositive_returnsTrue_noLookup() {
        DbAlertDeduplicator d = new DbAlertDeduplicator(lookup);

        assertTrue(d.isAllowed(20097L, "fp", 0L, 10));
        assertTrue(d.isAllowed(20097L, "fp", -10L, 10));

        verifyNoInteractions(lookup);
    }

    @Test
    public void isAllowed_sinceNonPositive_returnsTrue_noLookup() {
        DbAlertDeduplicator d = new DbAlertDeduplicator(lookup);

        // nowMs is smaller than window -> since <= 0 -> allowed
        assertTrue(d.isAllowed(20097L, "fp", 1L, 10));

        verifyNoInteractions(lookup);
    }

    @Test
    public void isAllowed_lookupSaysExists_returnsFalse_andTrimsFingerprint_andComputesSince() {
        DbAlertDeduplicator d = new DbAlertDeduplicator(lookup);

        long nowMs = 1_000_000L;
        int windowMinutes = 15;
        long expectedSince = nowMs - (15 * 60_000L);

        when(lookup.existsRecentOutcome(eq(20097L), eq("abc"), eq(expectedSince))).thenReturn(true);

        assertFalse(d.isAllowed(20097L, "  abc  ", nowMs, windowMinutes));

        verify(lookup, times(1)).existsRecentOutcome(20097L, "abc", expectedSince);
        verifyNoMoreInteractions(lookup);
    }

    @Test
    public void isAllowed_lookupSaysNotExists_returnsTrue_andUsesTrimmedFingerprint() {
        DbAlertDeduplicator d = new DbAlertDeduplicator(lookup);

        long nowMs = 2_000_000L;
        int windowMinutes = 1;
        long expectedSince = nowMs - (1 * 60_000L);

        when(lookup.existsRecentOutcome(eq(20097L), eq("fp"), eq(expectedSince))).thenReturn(false);

        assertTrue(d.isAllowed(20097L, " fp ", nowMs, windowMinutes));

        verify(lookup, times(1)).existsRecentOutcome(20097L, "fp", expectedSince);
        verifyNoMoreInteractions(lookup);
    }

    @Test
    public void ctor_nullLookup_throwsNpe() {
        try {
            new DbAlertDeduplicator(null);
        } catch (NullPointerException e) {
            assertTrue(e.getMessage().contains("lookup"));
            return;
        }
        throw new AssertionError("Expected NullPointerException");
    }
}
