package com.ntuc.notification.audit.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RootCauseExtractorTest {

    private final RootCauseExtractor extractor = new RootCauseExtractor();

    @Test
    public void extract_nullThrowable_returnsNull() {
        assertNull(extractor.extract((Throwable) null));
    }

    @Test
    public void extract_singleThrowable_returnsSame() {
        RuntimeException ex = new RuntimeException("boom");
        assertSame(ex, extractor.extract(ex));
    }

    @Test
    public void extract_nestedThrowable_returnsDeepestCause() {
        IllegalArgumentException root = new IllegalArgumentException("root");
        RuntimeException mid = new RuntimeException("mid", root);
        RuntimeException top = new RuntimeException("top", mid);

        Throwable out = extractor.extract(top);

        assertSame(root, out);
        assertEquals("root", out.getMessage());
    }

    @Test
    public void extract_cycle_doesNotLoop() {
        RuntimeException a = new RuntimeException("a");
        RuntimeException b = new RuntimeException("b", a);
        a.initCause(b);

        Throwable out = extractor.extract(a);

        boolean ok = (out == a) || (out == b);
        assertEquals(true, ok);
    }
}
