package com.ntuc.notification.audit.internal.email;

import java.util.Map;

/**
 * Very small token replacement engine for DT5 email templates.
 *
 * <p>Templates are stored as HTML in system configuration and contain tokens like:
 * {@code [$CORRELATION_ID$]}.</p>
 *
 * <p>Security rules:
 * - Inputs must already be sanitized.
 * - Engine does NOT evaluate scripts or expressions.
 * - Output is size-capped.</p>
 */
public final class EmailTemplateEngine {

    public static final int DEFAULT_MAX_CHARS = 50_000;

    private EmailTemplateEngine() {
        // util
    }

    public static String render(String template, Map<String, String> tokens) {
        return render(template, tokens, DEFAULT_MAX_CHARS);
    }

    public static String render(String template, Map<String, String> tokens, int maxChars) {
        if (template == null) {
            return "";
        }

        String out = template;

        if (tokens != null && !tokens.isEmpty()) {
            for (Map.Entry<String, String> e : tokens.entrySet()) {
                String key = e.getKey();
                if (key == null || key.isEmpty()) {
                    continue;
                }
                String val = (e.getValue() == null) ? "" : e.getValue();
                out = out.replace(key, val);
            }
        }

        if (maxChars > 0 && out.length() > maxChars) {
            return out.substring(0, maxChars) + "…";
        }

        return out;
    }
}
