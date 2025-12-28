package com.ntuc.notification.portlet.portlet;

import java.sql.Blob;

/**
 * Abstraction for converting SQL Blob columns to safe UTF-8 text.
 *
 * Production implementation delegates to BlobUtil.
 * Unit tests can provide a stub.
 */
public interface AuditLogBlobExtractor {

    String toText(Blob blob);
}
