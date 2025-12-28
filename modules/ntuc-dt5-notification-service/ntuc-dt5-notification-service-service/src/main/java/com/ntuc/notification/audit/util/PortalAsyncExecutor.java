package com.ntuc.notification.audit.util;

import java.util.concurrent.ExecutorService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.petra.executor.PortalExecutorManager;
import com.ntuc.notification.audit.api.util.AsyncExecutor;

/**
 * Service implementation that uses Liferay's PortalExecutorManager.
 * MDC wrapping stays internal to service to avoid leaking logging concerns.
 */
@Component(service = AsyncExecutor.class)
public class PortalAsyncExecutor implements AsyncExecutor {

    @Reference
    private PortalExecutorManager portalExecutorManager;

    @Override
    public void runAsync(String executorName, Runnable task) {
        ExecutorService exec = portalExecutorManager.getPortalExecutor(executorName);
        exec.submit(MdcUtil.wrap(task));
    }
}
