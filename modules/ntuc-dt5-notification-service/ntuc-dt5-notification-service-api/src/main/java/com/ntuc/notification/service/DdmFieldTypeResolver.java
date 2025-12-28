package com.ntuc.notification.service;

import java.util.Map;

/**
 * Resolves DDM field type information for a given structure.
 *
 * API-safe:
 * - No OSGi annotations
 * - No Liferay services/models in signatures
 *
 * Implemented in -service with caching.
 */
public interface DdmFieldTypeResolver {

    /**
     * Returns a map of:
     *   key   = field path (e.g. "courseTitle", "schedule.duration.hours")
     *   value = ddm type (e.g. "text", "text_area", "fieldset")
     *
     * Never returns null.
     */
    Map<String, String> resolve(long groupId, String ddmStructureKey);
}
