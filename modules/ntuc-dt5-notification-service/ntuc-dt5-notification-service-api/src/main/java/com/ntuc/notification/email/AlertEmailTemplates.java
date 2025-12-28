package com.ntuc.notification.email;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AlertEmailTemplates {

    private AlertEmailTemplates() {
        // Utility class
    }

    /** Short, human-friendly subject */
    public static final String SUBJECT =
        "[${severity:CRITICAL}] ${alertType:System Alert} - ${system:DT5} (${env:prod})";

    /** Minimal body with only business-useful fields (others live in audit logs) */
    public static final String BODY =
        "Dear ${notifyGroup:Team},\n\n"
      + "This is to inform you that the system has detected an event.\n\n"
      + "${summary?Message   : ${summary}\n}"
      + "${error?Details:\n${error}\n}"
      + "${ntucDTId?Notification Id   : ${ntucDTId}\n}"
      + "\n"
      + "Time: ${occurredAt}\n"
      + "\n"
      + "Thank you,\n"
      + "NTUC LearningHub Pte Ltd\n\n"
      + "This is a system generated email. Please do not reply directly to this email.";

    /**
     * Minimal renderer (Java 8+).
     *
     * Supported:
     * 1) Placeholder: ${key[:default]}
     * 2) Optional block: ${key? ... } (renders content only if key has a non-blank value)
     */
    public static String render(String template, Map<String, Object> ctx) {
        if (template == null) {
            return "";
        }

        String out = template;

        // 1) Optional sections FIRST: ${key? ... }
        Matcher opt = Regexes.OPTIONAL.matcher(out);
        StringBuffer sbOpt = new StringBuffer();
        while (opt.find()) {
            String key = opt.group(1);
            String content = opt.group(2);
            String v = val(ctx, key);
            opt.appendReplacement(sbOpt, isBlank(v) ? "" : Matcher.quoteReplacement(content));
        }
        opt.appendTail(sbOpt);
        out = sbOpt.toString();

        // 2) Placeholders AFTER: ${key[:default]}
        Matcher ph = Regexes.PLACEHOLDER.matcher(out);
        StringBuffer sbPh = new StringBuffer();
        while (ph.find()) {
            String key = ph.group(1);
            String def = ph.group(2);
            String v = val(ctx, key);
            if (isBlank(v)) {
                v = def; // may be null
            }
            ph.appendReplacement(sbPh, Matcher.quoteReplacement(v == null ? "" : v));
        }
        ph.appendTail(sbPh);
        out = sbPh.toString();

        // 3) Tidy up
        out = out.replaceAll("(?m)^[ \\t]+$", ""); // strip whitespace-only lines
        out = out.replaceAll("\\n{3,}", "\n\n");   // collapse 3+ newlines to 2
        return out.trim();
    }

    /** Normalize commonly-used ctx entries and keep things tidy */
    public static void normalizeCommon(Map<String, Object> ctx) {
        if (ctx == null) {
            return;
        }

        String now = DateFormats.nowString();
        ctx.putIfAbsent("now", now);

        // Ensure occurredAt exists so "Time: ${occurredAt}" always renders
        ctx.putIfAbsent("occurredAt", now);

        // Keep email readable
        truncate(ctx, "error", 2000);
    }

    // ===== internals =====

    private static void truncate(Map<String, Object> ctx, String key, int max) {
        Object v = ctx.get(key);
        if (v == null) {
            return;
        }
        String s = String.valueOf(v);
        if (s.length() > max) {
            ctx.put(key, s.substring(0, Math.max(0, max - 3)) + "...");
        }
    }

    private static String val(Map<String, Object> ctx, String key) {
        if ("now".equals(key)) {
            return DateFormats.nowString();
        }
        if (ctx == null) {
            return null;
        }
        Object v = ctx.get(key);
        return v == null ? null : String.valueOf(v);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static final class Regexes {

        private static final Pattern OPTIONAL =
            Pattern.compile("\\$\\{([a-zA-Z0-9_]+)\\?((?:[^}]|\\$\\{[^}]*\\})*)\\}");

        private static final Pattern PLACEHOLDER =
            Pattern.compile("\\$\\{([a-zA-Z0-9_]+)(?::([^}]*))?\\}");
    }

    private static final class DateFormats {

        private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        private static String nowString() {
            return LocalDateTime.now().format(FORMATTER);
        }
    }

    // Optional: tiny helper for callers
    public static Map<String, Object> newContext() {
        return new HashMap<String, Object>();
    }
}
