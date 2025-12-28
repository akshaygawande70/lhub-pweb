package com.ntuc.notification.schedule.api.util;

import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link ScheduleDateTimeValidator}.
 *
 * Contract (based on current implementation):
 * - Accepted format: dd/MM/uuuu HH:mm (UI message uses dd/MM/yyyy HH:mm)
 * - Strict calendar validation (ResolverStyle.STRICT)
 * - zoneId required
 * - validateRange: start must be <= end
 */
public class ScheduleDateTimeValidatorTest {

    private static final ZoneId SG = ZoneId.of("Asia/Singapore");
    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    // ---------------------------------------------------------------------
    // parseStrict()
    // ---------------------------------------------------------------------

    @Test
    public void parseStrict_valid_parsesFieldsAndZone_sg() {
        ZonedDateTime zdt = ScheduleDateTimeValidator.parseStrict("28/12/2025 09:30", SG);

        assertNotNull(zdt);
        assertEquals(2025, zdt.getYear());
        assertEquals(12, zdt.getMonthValue());
        assertEquals(28, zdt.getDayOfMonth());
        assertEquals(9, zdt.getHour());
        assertEquals(30, zdt.getMinute());
        assertEquals(SG, zdt.getZone());
    }

    @Test
    public void parseStrict_valid_parsesZone_ist() {
        ZonedDateTime zdt = ScheduleDateTimeValidator.parseStrict("28/12/2025 09:30", IST);

        assertNotNull(zdt);
        assertEquals(IST, zdt.getZone());
        assertEquals(9, zdt.getHour());
        assertEquals(30, zdt.getMinute());
    }

    @Test
    public void parseStrict_zoneNull_throwsNpeWithMessage() {
        try {
            ScheduleDateTimeValidator.parseStrict("28/12/2025 09:30", null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().toLowerCase().contains("zoneid"));
        }
    }

    @Test
    public void parseStrict_null_throwsIae_requiredMessage() {
        assertIae(
                () -> ScheduleDateTimeValidator.parseStrict(null, SG),
                "required",
                "dd/mm/yyyy hh:mm"
        );
    }

    @Test
    public void parseStrict_blank_throwsIae_requiredMessage() {
        assertIae(
                () -> ScheduleDateTimeValidator.parseStrict("   ", SG),
                "required",
                "dd/mm/yyyy hh:mm"
        );
    }

    @Test
    public void parseStrict_invalidFormat_wrongSeparator_throwsIae_expectedMessage() {
        assertIae(
                () -> ScheduleDateTimeValidator.parseStrict("28-12-2025 09:30", SG),
                "invalid date/time",
                "expected format"
        );
    }

    @Test
    public void parseStrict_invalidFormat_missingTime_throwsIae() {
        assertIae(
                () -> ScheduleDateTimeValidator.parseStrict("28/12/2025", SG),
                "invalid date/time"
        );
    }

    @Test
    public void parseStrict_invalidCalendar_dayOutOfRange_throwsIae() {
        assertIae(
                () -> ScheduleDateTimeValidator.parseStrict("32/12/2025 09:30", SG),
                "invalid date/time"
        );
    }

    @Test
    public void parseStrict_invalidCalendar_nonLeapFeb29_throwsIae() {
        assertIae(
                () -> ScheduleDateTimeValidator.parseStrict("29/02/2025 10:00", SG),
                "invalid date/time"
        );
    }

    @Test
    public void parseStrict_leapFeb29_ok() {
        ZonedDateTime zdt = ScheduleDateTimeValidator.parseStrict("29/02/2024 10:00", SG);

        assertNotNull(zdt);
        assertEquals(2024, zdt.getYear());
        assertEquals(2, zdt.getMonthValue());
        assertEquals(29, zdt.getDayOfMonth());
        assertEquals(10, zdt.getHour());
        assertEquals(0, zdt.getMinute());
    }

    @Test
    public void parseStrict_timeOutOfRange_hour25_throwsIae() {
        assertIae(
                () -> ScheduleDateTimeValidator.parseStrict("28/12/2025 25:00", SG),
                "invalid date/time"
        );
    }

    @Test
    public void parseStrict_timeOutOfRange_minute60_throwsIae() {
        assertIae(
                () -> ScheduleDateTimeValidator.parseStrict("28/12/2025 23:60", SG),
                "invalid date/time"
        );
    }

    // ---------------------------------------------------------------------
    // validateRange()
    // ---------------------------------------------------------------------

    @Test
    public void validateRange_equal_ok() {
        ScheduleDateTimeValidator.validateRange(
                "28/12/2025 09:30",
                "28/12/2025 09:30",
                SG
        );
    }

    @Test
    public void validateRange_endAfterStart_ok() {
        ScheduleDateTimeValidator.validateRange(
                "28/12/2025 09:30",
                "28/12/2025 10:30",
                SG
        );
    }

    @Test
    public void validateRange_endBeforeStart_throwsIae_withSpecificMessage() {
        try {
            ScheduleDateTimeValidator.validateRange(
                    "28/12/2025 10:30",
                    "28/12/2025 09:30",
                    SG
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Start Date must be earlier than or equal to End Date", e.getMessage());
        }
    }

    @Test
    public void validateRange_startInvalid_throwsIae() {
        assertIae(
                () -> ScheduleDateTimeValidator.validateRange(
                        "bad",
                        "28/12/2025 09:30",
                        SG
                ),
                "invalid date/time"
        );
    }

    @Test
    public void validateRange_endInvalid_throwsIae() {
        assertIae(
                () -> ScheduleDateTimeValidator.validateRange(
                        "28/12/2025 09:30",
                        "bad",
                        SG
                ),
                "invalid date/time"
        );
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private static void assertIae(Runnable r, String... msgContainsLowercase) {
        try {
            r.run();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
            String m = e.getMessage().toLowerCase();
            for (String s : msgContainsLowercase) {
                assertTrue("Message should contain '" + s + "' but was: " + e.getMessage(),
                        m.contains(s.toLowerCase()));
            }
        }
    }
}
