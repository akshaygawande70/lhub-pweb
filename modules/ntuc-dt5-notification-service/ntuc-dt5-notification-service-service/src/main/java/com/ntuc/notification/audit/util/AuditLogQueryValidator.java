package com.ntuc.notification.audit.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Validates and normalizes AuditLog query inputs.
 *
 * <p>Rules (aligned with your JSP):
 * 1) From ≤ To
 * 2) Range ≤ 90 days
 * 3) To ≤ today
 * 4) Search query ≤ 100 chars
 *
 * <p>Returns a normalized result with messages that can be surfaced as UI toasts.</p>
 */
public final class AuditLogQueryValidator {

    private static final DateTimeFormatter YMD = DateTimeFormatter.ISO_LOCAL_DATE;

    public static final int MAX_QUERY_LEN = 100;
    public static final int MAX_RANGE_DAYS = 90;

    private AuditLogQueryValidator() {
        // util
    }

    public static Result normalize(String fromYmd, String toYmd, String query) {
        LocalDate today = LocalDate.now();

        boolean changed = false;
        List<String> msgs = new ArrayList<>();

        LocalDate from = parseOrDefault(fromYmd, today, "Invalid From date; defaulted to today.", msgs);
        LocalDate to = parseOrDefault(toYmd, today, "Invalid To date; defaulted to today.", msgs);

        if (to.isAfter(today)) {
            to = today;
            changed = true;
            msgs.add("To date was in the future; clamped to today.");
        }

        if (from.isAfter(to)) {
            from = to;
            changed = true;
            msgs.add("From date was after To date; aligned to To date.");
        }

        long days = ChronoUnit.DAYS.between(from, to);
        if (days > MAX_RANGE_DAYS) {
            from = to.minusDays(MAX_RANGE_DAYS);
            changed = true;
            msgs.add("Date range exceeded 90 days; shortened to the last 90 days.");
        }

        String q = (query == null) ? "" : query.trim();
        if (q.length() > MAX_QUERY_LEN) {
            q = q.substring(0, MAX_QUERY_LEN);
            changed = true;
            msgs.add("Search query truncated to 100 characters.");
        }

        // If parse failures occurred, msgs will contain those; mark changed.
        if (!msgs.isEmpty()) {
            // If parse messages were added, it implies normalization.
            // But only mark changed if we actually altered fields; safe to mark true here.
            changed = true;
        }

        return new Result(from, to, q, changed, Collections.unmodifiableList(msgs));
    }

    private static LocalDate parseOrDefault(String ymd, LocalDate dflt, String msg, List<String> msgs) {
        if (ymd == null || ymd.trim().isEmpty()) {
            return dflt;
        }
        try {
            return LocalDate.parse(ymd.trim(), YMD);
        } catch (DateTimeParseException ex) {
            msgs.add(msg);
            return dflt;
        }
    }

    public static final class Result {
        private final LocalDate from;
        private final LocalDate to;
        private final String query;
        private final boolean changed;
        private final List<String> messages;

        private Result(LocalDate from, LocalDate to, String query, boolean changed, List<String> messages) {
            this.from = from;
            this.to = to;
            this.query = query;
            this.changed = changed;
            this.messages = messages;
        }

        public LocalDate getFrom() { return from; }
        public LocalDate getTo() { return to; }
        public String getQuery() { return query; }
        public boolean isChanged() { return changed; }
        public List<String> getMessages() { return messages; }

        public String getFromYmd() { return from.format(YMD); }
        public String getToYmd() { return to.format(YMD); }
    }
}
