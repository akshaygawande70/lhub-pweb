package com.ntuc.notification.rest.internal.processor.context;

import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class NotificationExecutorFactoryTest {

    @Test
    public void executor_executesRunnable() {
        NotificationExecutorFactory factory = new NotificationExecutorFactory();
        Executor executor = factory.create();

        AtomicBoolean ran = new AtomicBoolean(false);

        executor.execute(() -> ran.set(true));

        // allow async execution
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        assertTrue(ran.get());
    }
}
