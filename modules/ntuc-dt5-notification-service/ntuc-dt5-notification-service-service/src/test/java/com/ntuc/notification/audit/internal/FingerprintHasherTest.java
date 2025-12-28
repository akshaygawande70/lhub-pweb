package com.ntuc.notification.audit.internal;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FingerprintHasherTest {

    private final FingerprintHasher hasher = new FingerprintHasher();

    @Test
    public void sha256Hex_blank_returnsEmpty() {
        assertEquals("", hasher.sha256Hex(null));
        assertEquals("", hasher.sha256Hex(""));
        assertEquals("", hasher.sha256Hex("   "));
    }

    @Test
    public void sha256Hex_isDeterministic_andLowercase64() {
        String h1 = hasher.sha256Hex("abc");
        String h2 = hasher.sha256Hex("abc");

        assertEquals(h1, h2);
        assertNotNull(h1);
        assertEquals(64, h1.length());
        assertEquals(h1, h1.toLowerCase());
    }

    @Test
    public void sha256Hex_trimsInput() {
        assertEquals(hasher.sha256Hex("abc"), hasher.sha256Hex(" abc "));
    }
}
