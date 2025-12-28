package com.ntuc.notification.rest.internal.processor.context;

public interface RequestContextProvider {

    RequestContext current();

    /**
     * Allows async to preserve the same correlation id.
     */
    RequestContext currentWithCorrelation(String correlationId);

	String getOrCreateCorrelationId();
}
