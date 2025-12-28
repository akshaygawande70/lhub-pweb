package com.ntuc.notification.schedule.api.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Objects;

/**
 * Strict validation for UI-submitted schedule date/time values.
 *
 * <p>Accepted format: dd/MM/uuuu HH:mm</p>
 *
 * Rules:
 * <ul>
 *   <li>Strict calendar validation (no rollover)</li>
 *   <li>Timezone-aware conversion</li>
 *   <li>Start must be <= End</li>
 * </ul>
 */
public final class ScheduleDateTimeValidator {

    private static final DateTimeFormatter STRICT_FMT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm")
                    .withResolverStyle(ResolverStyle.STRICT);

    private ScheduleDateTimeValidator() {
        // utility
    }

    public static ZonedDateTime parseStrict(
            String raw,
            ZoneId zoneId) {

        Objects.requireNonNull(zoneId, "zoneId");

        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Date/time is required in format dd/MM/yyyy HH:mm");
        }

        try {
            LocalDateTime ldt =
                    LocalDateTime.parse(raw.trim(), STRICT_FMT);

            return ldt.atZone(zoneId);

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date/time. Expected format dd/MM/yyyy HH:mm (e.g. 28/12/2025 09:30)", e);
        }
    }

    public static void validateRange(
            String startRaw,
            String endRaw,
            ZoneId zoneId) {

        ZonedDateTime start = parseStrict(startRaw, zoneId);
        ZonedDateTime end = parseStrict(endRaw, zoneId);

        if (start.isAfter(end)) {
            throw new IllegalArgumentException(
                    "Start Date must be earlier than or equal to End Date");
        }
    }
}
