package com.ntuc.notification.rest.internal.processor.context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Pure executor factory – unit-testable.
 */
public class NotificationExecutorFactory {

    public Executor create() {
        return Executors.newSingleThreadExecutor();
    }
}
