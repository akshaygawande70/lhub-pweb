package com.ntuc.notification.audit.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;

/**
 * Service-layer utility for converting between UTF-8 Strings and SQL Blobs.
 *
 * <p>Security/operational rules:
 * - Returning empty string on failure avoids breaking the UI.
 * - Do NOT use this for secrets or raw external payloads.</p>
 */
public final class BlobUtil {

    private BlobUtil() {
        // util
    }

    public static String toString(Blob blob) {
        if (blob == null) {
            return "";
        }

        try (InputStream is = blob.getBinaryStream()) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int nRead;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            return "";
        }
    }
}
