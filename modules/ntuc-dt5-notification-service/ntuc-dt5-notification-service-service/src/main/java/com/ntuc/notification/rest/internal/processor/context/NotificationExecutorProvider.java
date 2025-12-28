package com.ntuc.notification.rest.internal.processor.context;

import com.liferay.petra.executor.PortalExecutorManager;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * Provides an execution mechanism for async processing.
 *
 * Plain Java friendly: logic calls {@link #execute(Runnable)} without knowing
 * Liferay's executor wiring.
 */
public class NotificationExecutorProvider {

    private final Executor executor;

    public NotificationExecutorProvider(PortalExecutorManager portalExecutorManager) {
        Objects.requireNonNull(portalExecutorManager, "portalExecutorManager");

        // Use a named portal executor if you already have one in your codebase.
        // If not, this can be changed to a specific executor later.
        this.executor = portalExecutorManager.getPortalExecutor("ntuc-dt5-notification-executor");
    }

    /**
     * Executes the given runnable asynchronously.
     */
    public void execute(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable");
        executor.execute(runnable);
    }
}
