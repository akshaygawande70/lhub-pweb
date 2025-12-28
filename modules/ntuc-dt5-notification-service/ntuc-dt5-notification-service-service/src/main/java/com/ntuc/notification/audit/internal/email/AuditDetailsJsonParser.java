package com.ntuc.notification.audit.internal.email;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Parses persisted detailsJson into a Map<String,String> for downstream use (templates, fingerprinting).
 *
 * Rules:
 * - Must never throw.
 * - Must not include secrets (sanitization should already have happened before persistence).
 * - Must size-cap to avoid memory blowups.
 */
public class AuditDetailsJsonParser {

    private static final int MAX_ENTRIES = 50;
    private static final int MAX_VALUE_CHARS = 1000;

    private final ObjectMapper objectMapper;

    public AuditDetailsJsonParser() {
        this(new ObjectMapper());
    }

    public AuditDetailsJsonParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Map<String, String> parse(String detailsJson) {
        if (detailsJson == null) {
            return Collections.emptyMap();
        }

        String trimmed = detailsJson.trim();
        if (trimmed.isEmpty()) {
            return Collections.emptyMap();
        }

        try {
            Map<String, Object> raw = objectMapper.readValue(
                    trimmed, new TypeReference<Map<String, Object>>() {});

            if (raw == null || raw.isEmpty()) {
                return Collections.emptyMap();
            }

            Map<String, String> out = new LinkedHashMap<String, String>();
            for (Map.Entry<String, Object> e : raw.entrySet()) {
                if (out.size() >= MAX_ENTRIES) {
                    break;
                }
                String key = safeKey(e.getKey());
                if (key.isEmpty()) {
                    continue;
                }

                String value = coerceValue(e.getValue());
                if (value.length() > MAX_VALUE_CHARS) {
                    value = value.substring(0, MAX_VALUE_CHARS);
                }

                out.put(key, value);
            }

            return out.isEmpty() ? Collections.<String, String>emptyMap() : Collections.unmodifiableMap(out);
        }
        catch (Throwable ignore) {
            return Collections.emptyMap();
        }
    }

    private static String safeKey(String k) {
        if (k == null) {
            return "";
        }
        String t = k.trim();
        return t.isEmpty() ? "" : t;
    }

    private static String coerceValue(Object v) {
        if (v == null) {
            return "";
        }
        // Keep it simple: convert scalars to String; nested objects/arrays become JSON-ish string
        return String.valueOf(v);
    }
}
