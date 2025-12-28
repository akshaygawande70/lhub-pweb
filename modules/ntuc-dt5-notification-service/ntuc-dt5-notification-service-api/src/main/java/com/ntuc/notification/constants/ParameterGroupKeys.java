package com.ntuc.notification.constants;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalService;
import svc.ntuc.nlh.parameter.service.ParameterLocalService;

@Component(immediate = true, service = ParameterGroupKeys.class)
public class ParameterGroupKeys {

    @Reference
    private ParameterGroupLocalService _parameterGroupLocalService;

    @Reference
    private ParameterLocalService _parameterLocalService;

    /**
     * Returns a map containing all parameter values from both the CLS and GLOBAL parameter groups.
     *
     * Resolution order:
     * 1) GLOBAL (includes GLOBAL_* + ALERT_* keys)
     * 2) CLS overrides
     *
     * CLS values take precedence if a key exists in both.
     */
    public Map<ParameterKeyEnum, Object> getAllParameterValues() {
        Map<ParameterKeyEnum, Object> allParams = new EnumMap<>(ParameterKeyEnum.class);

        // First, put all global parameters (including ALERT_* policy keys)
        allParams.putAll(getAllParameterValuesGlobal());

        // Then, override/add CLS parameters
        allParams.putAll(getAllParameterValuesCls());

        return allParams;
    }

    /** Returns all parameter values for the CLS group. */
    private Map<ParameterKeyEnum, Object> getAllParameterValuesCls() {
        ParameterKeyEnum[] clsParams = Arrays.stream(ParameterKeyEnum.values())
                .filter(e -> e.name().startsWith("CLS_"))
                .toArray(ParameterKeyEnum[]::new);

        return getAllParameterValuesByCode(ParameterKeyEnum.CLS_PARAMETER_GROUP_CODE.code(), clsParams);
    }

    /**
     * Returns all parameter values for the GLOBAL group.
     *
     * IMPORTANT:
     * Alert policy keys (ALERT_*) are global knobs and MUST be loaded here.
     */
    private Map<ParameterKeyEnum, Object> getAllParameterValuesGlobal() {
        ParameterKeyEnum[] globalParams = Arrays.stream(ParameterKeyEnum.values())
                .filter(e -> e.name().startsWith("GLOBAL_") || e.name().startsWith("ALERT_"))
                .toArray(ParameterKeyEnum[]::new);

        return getAllParameterValuesByCode(ParameterKeyEnum.GLOBAL_PARAMETER_GROUP_CODE.code(), globalParams);
    }

    /**
     * Internal: fetches all parameter values for the given group code.
     *
     * Storage:
     * - returns String for scalar values
     * - returns String[] when value contains commas
     */
    private Map<ParameterKeyEnum, Object> getAllParameterValuesByCode(String groupCode, ParameterKeyEnum[] params) {
        Map<ParameterKeyEnum, Object> paramValues = new EnumMap<>(ParameterKeyEnum.class);

        ParameterGroup group = _parameterGroupLocalService.getByCode(groupCode, false);
        if (group == null) {
            return paramValues;
        }

        long groupId = group.getGroupId();
        long parameterGroupId = group.getParameterGroupId();

        for (ParameterKeyEnum key : params) {
            String value = null;

            try {
                value = _parameterLocalService
                        .getByGroupCode(groupId, parameterGroupId, key.code(), false)
                        .getParamValue();

                if (value != null) {
                    value = value.trim();

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
                // Missing param -> null (caller uses defaults)
            }

            paramValues.put(key, value);
        }

        return paramValues;
    }
}
