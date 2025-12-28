package com.ntuc.notification.audit.api.util;

/**
 * Abstraction for async execution.
 *
 * API module must not expose Liferay executor types. This interface keeps callers
 * JUnit-testable and prevents OSGi/runtime leakage.
 */
public interface AsyncExecutor {

    /**
     * Execute the given task asynchronously.
     *
     * @param executorName portal executor name (e.g. "ntuc-notification-executor")
     * @param task runnable to execute
     */
    void runAsync(String executorName, Runnable task);
}
