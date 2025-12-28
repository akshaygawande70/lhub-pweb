package com.ntuc.notification.journal.api;

/**
 * Extracts a value from Liferay JournalArticle DDM XML content.
 *
 * Contract:
 * - Must be deterministic for the same input XML.
 * - Must be safe for unit testing (no Liferay runtime dependencies).
 * - Implementations decide how to parse and handle missing values.
 */
public interface DynamicElementValueExtractor {

    /**
     * Extracts the value of a dynamic-element by field name.
     *
     * @param xmlContent DDM XML content
     * @param fieldName the dynamic-element field name (e.g. "courseCode")
     * @return extracted value, or empty string if missing/unreadable
     */
    String extract(String xmlContent, String fieldName);
}
