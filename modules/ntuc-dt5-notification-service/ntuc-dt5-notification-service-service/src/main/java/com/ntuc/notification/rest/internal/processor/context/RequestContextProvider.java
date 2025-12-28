package com.ntuc.notification.rest.internal.processor.context;

/**
 * Provides a consistent {@link RequestContext} for request/async processing.
 *
 * <p><b>Business purpose:</b>
 * Ensures notification workflows run with the correct tenant (company), site (group),
 * user identity, and correlation information for traceability and support.</p>
 *
 * <p><b>Technical purpose:</b>
 * Supplies a per-execution context snapshot, typically derived from Liferay thread-locals,
 * and supports correlation propagation across async boundaries.</p>
 *
 * @author @akshaygawande
 */
public interface RequestContextProvider {

    /**
     * Returns the current execution {@link RequestContext}.
     *
     * <p><b>Business purpose:</b>
     * Establishes the baseline context for processing a REST-triggered workflow.</p>
     *
     * <p><b>Technical purpose:</b>
     * Builds a context snapshot for the current thread, including correlationId,
     * companyId, groupId and userId.</p>
     *
     * <p><b>Inputs/Invariants:</b>
     * Uses platform thread-locals and may create correlationId when absent.</p>
     *
     * <p><b>Side effects:</b>
     * Implementations may write correlationId into MDC when it is created.</p>
     *
     * <p><b>Return semantics:</b>
     * Never returns {@code null} in normal operation.</p>
     *
     * @return populated {@link RequestContext}
     */
    RequestContext current();

    /**
     * Returns the current {@link RequestContext} while forcing a provided correlationId.
     *
     * <p><b>Business purpose:</b>
     * Preserves trace continuity when work is executed asynchronously or via
     * chained processing steps.</p>
     *
     * <p><b>Technical purpose:</b>
     * Builds a context snapshot and propagates the provided correlationId into MDC so
     * downstream logging/audit uses the same identifier.</p>
     *
     * <p><b>Inputs/Invariants:</b>
     * correlationId must be non-blank.</p>
     *
     * <p><b>Side effects:</b>
     * Implementations write the provided correlationId into MDC/ThreadLocal context.</p>
     *
     * <p><b>Return semantics:</b>
     * Never returns {@code null}. Implementations may throw {@link IllegalArgumentException}
     * for blank correlationId.</p>
     *
     * @param correlationId correlation identifier to enforce for the context
     * @return populated {@link RequestContext}
     * @throws IllegalArgumentException when correlationId is blank
     */
    RequestContext currentWithCorrelation(String correlationId);

    /**
     * Returns an existing correlationId for the current execution or creates one.
     *
     * <p><b>Business purpose:</b>
     * Guarantees that every workflow has a trace identifier even when upstream did not supply one.</p>
     *
     * <p><b>Technical purpose:</b>
     * Reads correlationId from MDC (if available). If absent, generates a new value and
     * stores it into MDC so subsequent components share the same correlationId.</p>
     *
     * <p><b>Inputs/Invariants:</b>
     * No inputs; relies on MDC state.</p>
     *
     * <p><b>Side effects:</b>
     * Writes a newly generated correlationId into MDC.</p>
     *
     * <p><b>Return semantics:</b>
     * Always returns a non-blank correlationId.</p>
     *
     * @return non-blank correlationId
     */
    String getOrCreateCorrelationId();
}
