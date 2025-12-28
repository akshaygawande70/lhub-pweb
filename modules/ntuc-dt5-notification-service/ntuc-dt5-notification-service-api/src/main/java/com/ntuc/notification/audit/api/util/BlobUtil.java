package com.ntuc.notification.audit.api.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

/**
 * Service-layer utility for converting between UTF-8 Strings and SQL Blobs.
 *
 * Rules:
 * - Must NEVER throw
 * - Must NEVER OOM
 * - Safe for UI + search usage
 */
public final class BlobUtil {

    private static final int DEFAULT_MAX_BYTES = 512 * 1024; // 512 KB cap

    private BlobUtil() {
        // Utility class
    }

    /**
     * Convert a UTF-8 String to a SQL Blob for DB storage.
     */
    public static Blob fromString(String str) throws SQLException {
        if (str == null) {
            return null;
        }
        return new SerialBlob(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Convert a SQL Blob back to a UTF-8 String (capped).
     */
    public static String toString(Blob blob) {
        return toString(blob, DEFAULT_MAX_BYTES);
    }

    /**
     * Convert a SQL Blob back to a UTF-8 String with explicit size cap.
     */
    public static String toString(Blob blob, int maxBytes) {
        if (blob == null || maxBytes <= 0) {
            return "";
        }

        try (InputStream is = blob.getBinaryStream()) {
            ByteArrayOutputStream buffer =
                    new ByteArrayOutputStream(Math.min(4096, maxBytes));

            byte[] data = new byte[4096];
            int remaining = maxBytes;
            int nRead;

            while (remaining > 0 &&
                   (nRead = is.read(data, 0, Math.min(data.length, remaining))) != -1) {

                buffer.write(data, 0, nRead);
                remaining -= nRead;
            }

            String s = new String(buffer.toByteArray(), StandardCharsets.UTF_8);

            if (blob.length() > maxBytes) {
                s += "\n\n-- truncated --";
            }

            return s;

        } catch (Exception e) {
            return "[invalid blob]";
        }
    }
}
