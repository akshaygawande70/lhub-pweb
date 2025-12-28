package com.ntuc.notification.audit.internal.email;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class AuditDetailsJsonParserTest {

    @Test
    public void parse_null_returnsEmpty() {
        AuditDetailsJsonParser p = new AuditDetailsJsonParser();
        assertTrue(p.parse(null).isEmpty());
    }

    @Test
    public void parse_validObject_returnsMap() {
        AuditDetailsJsonParser p = new AuditDetailsJsonParser();

        Map<String, String> m = p.parse("{\"a\":\"b\",\"x\":123}");

        assertEquals("b", m.get("a"));
        assertEquals("123", m.get("x"));
    }

    @Test
    public void parse_invalid_returnsEmpty() {
        AuditDetailsJsonParser p = new AuditDetailsJsonParser();
        assertTrue(p.parse("{not-json").isEmpty());
    }
}
