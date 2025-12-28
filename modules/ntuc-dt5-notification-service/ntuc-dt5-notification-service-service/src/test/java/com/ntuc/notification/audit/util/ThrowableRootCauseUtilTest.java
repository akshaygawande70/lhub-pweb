package com.ntuc.notification.audit.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ThrowableRootCauseUtilTest {

    @Test
    public void testRootCause_deepChain() {
        Exception root = new IllegalArgumentException("bad input");
        Exception mid = new RuntimeException("wrapper", root);
        Exception top = new Exception("top", mid);

        Throwable out = ThrowableRootCauseUtil.rootCause(top);

        Assert.assertSame(root, out);
    }

    @Test
    public void testSafeMessage_fallbackToClassName() {
        Exception e = new Exception();
        String msg = ThrowableRootCauseUtil.safeMessage(e);
        Assert.assertEquals("Exception", msg);
    }
}

