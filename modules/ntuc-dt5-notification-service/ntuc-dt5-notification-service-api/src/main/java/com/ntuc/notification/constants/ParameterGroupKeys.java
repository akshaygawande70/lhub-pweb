package com.ntuc.notification.constants;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalService;
import svc.ntuc.nlh.parameter.service.ParameterLocalService;

/**
 * Central access point for runtime configuration sourced from Parameter Groups.
 *
 * <p><b>Business purpose:</b> Provides consistent, tenant-specific configuration for CLS integration
 * and global alerting policies, ensuring operational behavior can be tuned without redeployments.</p>
 *
 * <p><b>Technical purpose:</b> Loads parameters from two groups (GLOBAL + CLS) and merges them into an
 * {@link EnumMap} keyed by {@link ParameterKeyEnum}, applying a deterministic override order.</p>
 *
 * <p>Resolution order:
 * <ol>
 *   <li>GLOBAL (includes GLOBAL_* and ALERT_* keys)</li>
 *   <li>CLS overrides (CLS_* keys win on collision)</li>
 * </ol>
 * </p>
 *
 * @author @akshaygawande
 */
@Component(immediate = true, service = ParameterGroupKeys.class)
public class ParameterGroupKeys {

    /**
     * ServiceBuilder local service for resolving ParameterGroup metadata by code.
     *
     * <p>Used to locate the target groupId and parameterGroupId required for subsequent
     * parameter lookups.</p>
     */
    @Reference
    private ParameterGroupLocalService _parameterGroupLocalService;

    /**
     * ServiceBuilder local service for fetching individual parameter values within a group.
     *
     * <p>Lookups are intentionally tolerant to missing keys: absent parameters resolve
     * to {@code null}, allowing callers to apply defaults.</p>
     */
    @Reference
    private ParameterLocalService _parameterLocalService;

    /**
     * Returns a merged map containing all parameter values from both the GLOBAL and CLS
     * parameter groups.
     *
     * <p><b>Business purpose:</b> Ensures workflows evaluate configuration using a single,
     * authoritative view with well-defined precedence.</p>
     *
     * <p><b>Technical purpose:</b> Aggregates values from the GLOBAL group first (including
     * ALERT_* policy keys), then overlays CLS-specific values.</p>
     *
     * @return map of parameter keys to resolved values; values may be {@code String},
     *         {@code String[]}, or {@code null} when not configured
     */
    public Map<ParameterKeyEnum, Object> getAllParameterValues() {
        Map<ParameterKeyEnum, Object> allParams = new EnumMap<>(ParameterKeyEnum.class);

        // Load global configuration first (GLOBAL_* + ALERT_* act as system defaults)
        allParams.putAll(getAllParameterValuesGlobal());

        // Overlay CLS-specific configuration, which takes precedence on conflicts
        allParams.putAll(getAllParameterValuesCls());

        return allParams;
    }

    /**
     * Returns all parameter values for the CLS parameter group.
     *
     * <p><b>Business purpose:</b> Supports course lifecycle workflows by allowing
     * CLS-specific behavior to override global defaults.</p>
     *
     * <p><b>Technical purpose:</b> Filters {@link ParameterKeyEnum} entries by the
     * {@code CLS_} prefix and resolves their values from the CLS group.</p>
     *
     * @return map of CLS parameter keys to resolved values
     */
    private Map<ParameterKeyEnum, Object> getAllParameterValuesCls() {
        ParameterKeyEnum[] clsParams = Arrays.stream(ParameterKeyEnum.values())
                .filter(e -> e.name().startsWith("CLS_"))
                .toArray(ParameterKeyEnum[]::new);

        return getAllParameterValuesByCode(
                ParameterKeyEnum.CLS_PARAMETER_GROUP_CODE.code(),
                clsParams
        );
    }

    /**
     * Returns all parameter values for the GLOBAL parameter group.
     *
     * <p><b>Business purpose:</b> Centralizes system-wide configuration and alerting
     * policy knobs that apply across all CLS executions.</p>
     *
     * <p><b>Technical purpose:</b> Filters {@link ParameterKeyEnum} entries by the
     * {@code GLOBAL_} and {@code ALERT_} prefixes and resolves their values from
     * the GLOBAL group.</p>
     *
     * @return map of global and alert policy parameter keys to resolved values
     */
    private Map<ParameterKeyEnum, Object> getAllParameterValuesGlobal() {
        ParameterKeyEnum[] globalParams = Arrays.stream(ParameterKeyEnum.values())
                .filter(e ->
                        e.name().startsWith("GLOBAL_")
                        || e.name().startsWith("ALERT_"))
                .toArray(ParameterKeyEnum[]::new);

        return getAllParameterValuesByCode(
                ParameterKeyEnum.GLOBAL_PARAMETER_GROUP_CODE.code(),
                globalParams
        );
    }

    /**
     * Fetches parameter values for a specific parameter group code.
     *
     * <p><b>Business purpose:</b> Provides a consistent, fault-tolerant read path for
     * configuration values without failing workflows due to missing parameters.</p>
     *
     * <p><b>Technical purpose:</b> Resolves parameters by group code and key, normalizing
     * values into either scalar strings or string arrays.</p>
     *
     * <p>Storage semantics:
     * <ul>
     *   <li>Single values are returned as {@link String}</li>
     *   <li>Comma-delimited values are split and returned as {@code String[]}</li>
     *   <li>Missing parameters resolve to {@code null}</li>
     * </ul>
     * </p>
     *
     * @param groupCode parameter group code to resolve
     * @param params    parameter keys to fetch from the group
     * @return map of parameter keys to resolved values
     */
    private Map<ParameterKeyEnum, Object> getAllParameterValuesByCode(
            String groupCode,
            ParameterKeyEnum[] params) {

        Map<ParameterKeyEnum, Object> paramValues =
                new EnumMap<>(ParameterKeyEnum.class);

        ParameterGroup group =
                _parameterGroupLocalService.getByCode(groupCode, false);

        // Group not configured -> return empty map, callers apply defaults
        if (group == null) {
            return paramValues;
        }

        long groupId = group.getGroupId();
        long parameterGroupId = group.getParameterGroupId();

        for (ParameterKeyEnum key : params) {
            String value = null;

            try {
                value = _parameterLocalService
                        .getByGroupCode(
                                groupId,
                                parameterGroupId,
                                key.code(),
                                false)
                        .getParamValue();

                if (value != null) {
                    value = value.trim();

                    // Normalize comma-delimited values into String[]
                    if (value.contains(",")) {
                        paramValues.put(
                                key,
                                Arrays.stream(value.split(","))
                                        .map(String::trim)
                                        .filter(s -> !s.isEmpty())
                                        .toArray(String[]::new)
                        );
                        continue;
                    }
                }
            } catch (Exception ignore) {
                // Missing or inaccessible parameter -> treated as null
            }

            paramValues.put(key, value);
        }

        return paramValues;
    }
}
