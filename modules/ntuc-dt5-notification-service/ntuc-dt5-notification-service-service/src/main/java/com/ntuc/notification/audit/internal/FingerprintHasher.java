package com.ntuc.notification.audit.internal;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Deterministic hasher for alert fingerprint persistence.
 *
 * IMPORTANT:
 * - DB column must store a fixed-size identifier.
 * - Use SHA-256 hex (64 chars).
 * - Caller is responsible for providing the raw fingerprint string.
 */
public class FingerprintHasher {

    public String sha256Hex(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return "";
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.trim().getBytes(StandardCharsets.UTF_8));
            return toHex(digest);
        }
        catch (Exception e) {
            // Fail-open: no hash (dedupe may not work, but persistence must not fail).
            return "";
        }
    }

    private static String toHex(byte[] bytes) {
        char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }
}
