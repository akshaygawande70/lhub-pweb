package com.ntuc.notification.audit.util;

import com.ntuc.notification.audit.api.util.AsyncExecutor;

/**
 * Backward-compatible wrapper for existing call sites.
 *
 * Prefer injecting AsyncExecutor instead of using this static helper.
 */
public final class ExecutorUtil {

    private ExecutorUtil() {
        // Utility class
    }

    public static void runAsync(AsyncExecutor asyncExecutor, String executorName, Runnable task) {
        asyncExecutor.runAsync(executorName, task);
    }
}
