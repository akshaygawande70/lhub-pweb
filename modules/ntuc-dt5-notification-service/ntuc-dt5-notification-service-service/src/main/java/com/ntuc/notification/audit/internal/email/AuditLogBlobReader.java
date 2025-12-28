package com.ntuc.notification.audit.internal.email;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;

/**
 * Best-effort Blob -> UTF-8 String converter.
 *
 * Rules:
 * - Must never throw (audit pipeline must not break flow).
 * - Must NOT cause OOM (cap reads).
 * - Must not transform content (no lowercasing, no trimming, no rewriting).
 */
public class AuditLogBlobReader {

    private static final int DEFAULT_MAX_BYTES = 64 * 1024; // 64KB cap

    public String readUtf8(Blob blob) {
        return readUtf8(blob, DEFAULT_MAX_BYTES);
    }

    public String readUtf8(Blob blob, int maxBytes) {
        if (blob == null || maxBytes <= 0) {
            return null;
        }

        InputStream is = null;

        try {
            is = blob.getBinaryStream();
            if (is == null) {
                return null;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream(Math.min(4096, maxBytes));
            byte[] buf = new byte[4096];

            int remaining = maxBytes;
            int read;

            while (remaining > 0 && (read = is.read(buf, 0, Math.min(buf.length, remaining))) != -1) {
                baos.write(buf, 0, read);
                remaining -= read;
            }

            return new String(baos.toByteArray(), "UTF-8");
        }
        catch (Throwable ignore) {
            return null;
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (Throwable ignore) {
                    // never throw
                }
            }
        }
    }
}
