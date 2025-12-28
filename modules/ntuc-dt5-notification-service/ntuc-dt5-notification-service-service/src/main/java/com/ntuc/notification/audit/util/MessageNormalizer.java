package com.ntuc.notification.audit.util;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Normalizes error messages for fingerprinting/deduplication.
 *
 * Rules:
 * - lowercases
 * - collapses whitespace
 * - replaces uuids/urls/timestamps/hex/numbers (2+ digits) with tokens
 * - caps output to 512 chars
 */
public class MessageNormalizer {

    private static final int MAX_LEN = 512;

    private static final Pattern UUID =
        Pattern.compile("\\b[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\\b");

    private static final Pattern HEX =
        Pattern.compile("\\b0x[0-9a-fA-F]+\\b");

    private static final Pattern URL =
        Pattern.compile("\\bhttps?://\\S+\\b");

    // NOTE: we lowercase early, so match 't' and 'z' (lowercase).
    private static final Pattern ISO_TS_Z =
        Pattern.compile("\\b\\d{4}-\\d{2}-\\d{2}t\\d{2}:\\d{2}:\\d{2}(?:\\.\\d{1,9})?z\\b");

    // NOTE: we lowercase early, so match 'utc' (lowercase).
    private static final Pattern ISO_TS_SPACE_UTC =
        Pattern.compile("\\b\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}(?:\\.\\d{1,9})?\\s+utc\\b");

    /**
     * Replace 2+ digits with <num> even when embedded in tokens (abc123 -> abc<num>),
     * but do NOT touch hex literals (0xDEADBEEF).
     *
     * After lowercasing, hex prefix is always '0x'.
     */
    private static final Pattern NUM_2PLUS_ANYWHERE_EXCEPT_HEX =
        Pattern.compile("(?<!0x)\\d{2,}");

    private static final Pattern WS =
        Pattern.compile("\\s+");

    private MessageNormalizer() {
        // util
    }

    public static String normalize(String in) {
        if (in == null) {
            return "unknown";
        }

        String s = in.trim();
        if (s.isEmpty()) {
            return "unknown";
        }

        s = s.toLowerCase(Locale.ROOT);
        s = WS.matcher(s).replaceAll(" ");

        // Stable replacement order matters for deterministic fingerprints
        s = UUID.matcher(s).replaceAll("<uuid>");
        s = URL.matcher(s).replaceAll("<url>");
        s = ISO_TS_Z.matcher(s).replaceAll("<ts>");
        s = ISO_TS_SPACE_UTC.matcher(s).replaceAll("<ts>");

        // IMPORTANT: hex before numbers so numbers don't break hex matching
        s = HEX.matcher(s).replaceAll("<hex>");
        s = NUM_2PLUS_ANYWHERE_EXCEPT_HEX.matcher(s).replaceAll("<num>");

        if (s.length() > MAX_LEN) {
            s = s.substring(0, MAX_LEN);
        }

        return s;
    }
}
