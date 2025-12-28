package com.ntuc.notification.audit.internal.email;

import org.junit.Test;

import javax.sql.rowset.serial.SerialBlob;

import static org.junit.Assert.*;

public class AuditLogBlobReaderTest {

    @Test
    public void readUtf8_null_returnsNull() {
        AuditLogBlobReader r = new AuditLogBlobReader();
        assertNull(r.readUtf8(null));
    }

    @Test
    public void readUtf8_readsBlob() throws Exception {
        AuditLogBlobReader r = new AuditLogBlobReader();

        SerialBlob b = new SerialBlob("{\"a\":\"b\"}".getBytes("UTF-8"));

        assertEquals("{\"a\":\"b\"}", r.readUtf8(b));
    }

    @Test
    public void readUtf8_capsBytes() throws Exception {
        AuditLogBlobReader r = new AuditLogBlobReader();

        String s = "0123456789";
        SerialBlob b = new SerialBlob(s.getBytes("UTF-8"));

        assertEquals("01234", r.readUtf8(b, 5));
    }
}
