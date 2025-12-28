package com.ntuc.notification.audit.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageNormalizerTest {

    @Test
    public void normalize_null_returnsUnknown() {
        assertEquals("unknown", MessageNormalizer.normalize(null));
    }

    @Test
    public void normalize_blank_returnsUnknown() {
        assertEquals("unknown", MessageNormalizer.normalize(""));
        assertEquals("unknown", MessageNormalizer.normalize("   "));
        assertEquals("unknown", MessageNormalizer.normalize("\n\t"));
    }

    @Test
    public void normalize_collapsesWhitespace_andLowercases() {
        String in = "  Foo   BAR \n Baz\tQux  ";
        assertEquals("foo bar baz qux", MessageNormalizer.normalize(in));
    }

    @Test
    public void normalize_replacesUuid() {
        String in = "correlation 7224f34b-26ce-42e8-85de-809ee21874df failed";
        assertEquals("correlation <uuid> failed", MessageNormalizer.normalize(in));
    }

    @Test
    public void normalize_replacesHex() {
        String in = "failed with 0xDEADBEEF and 0x01";
        assertEquals("failed with <hex> and <hex>", MessageNormalizer.normalize(in));
    }

    @Test
    public void normalize_replacesNumbers_twoDigitsOrMore() {
        String in = "auditLogId=354644 companyId=20097 x=9 y=10 z=123";
        // 9 stays as-is, 10 and 123 become <num>
        assertEquals("auditlogid=<num> companyid=<num> x=9 y=<num> z=<num>", MessageNormalizer.normalize(in));
    }

    @Test
    public void normalize_replacesUrl() {
        String in = "call https://example.com/api/v1?id=123&c=abc failed";
        assertEquals("call <url> failed", MessageNormalizer.normalize(in));
    }

    @Test
    public void normalize_replacesTimestamp_isoWithZ() {
        String in = "time 2025-12-23T09:03:00.075Z error";
        assertEquals("time <ts> error", MessageNormalizer.normalize(in));
    }

    @Test
    public void normalize_replacesTimestamp_isoWithSpaceAndUtc() {
        String in = "time 2025-12-23 09:03:00 UTC error";
        assertEquals("time <ts> error", MessageNormalizer.normalize(in));
    }

    @Test
    public void normalize_lengthIsCappedTo512() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2000; i++) {
            sb.append('A');
        }

        String out = MessageNormalizer.normalize(sb.toString());

        assertNotNull(out);
        assertEquals(512, out.length());
        // it should also be lowercase
        assertEquals(out, out.toLowerCase());
    }

    @Test
    public void normalize_orderOfReplacements_isStable() {
        String in = "uuid 7224f34b-26ce-42e8-85de-809ee21874df url https://x.y/z?id=99 ts 2025-12-23T09:03:00Z num 1234 hex 0xABCD";
        String expected = "uuid <uuid> url <url> ts <ts> num <num> hex <hex>";
        assertEquals(expected, MessageNormalizer.normalize(in));
    }
}
