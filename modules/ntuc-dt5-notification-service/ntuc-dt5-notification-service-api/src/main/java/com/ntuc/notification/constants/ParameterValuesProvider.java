package com.ntuc.notification.constants;

import java.util.Map;

/**
 * API interface for resolving ParameterKeyEnum values for runtime behavior.
 *
 * Contract:
 * - Implementations may read from ServiceBuilder/local services/config, etc.
 * - Callers must treat returned values as best-effort and apply defaults.
 *
 * Value conventions:
 * - Scalar values may be returned as String
 * - Multi-values may be returned as String[]
 */
public interface ParameterValuesProvider {

    /**
     * Returns a map containing parameter values used by the notification service.
     *
     * Implementations define resolution/override rules.
     */
    Map<ParameterKeyEnum, Object> getAllParameterValues();
}
