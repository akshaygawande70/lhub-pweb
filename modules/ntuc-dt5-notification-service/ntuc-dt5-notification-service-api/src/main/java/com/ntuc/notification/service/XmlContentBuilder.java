package com.ntuc.notification.service;

import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.model.CourseResponse;
import com.ntuc.notification.model.ScheduleResponse;

import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface XmlContentBuilder {

    /**
     * Builds XML containing only critical fields.
     */
    String processCriticalFields(CourseEventContext eventCtx, CourseResponse courseResponse);

    /**
     * Builds XML containing only non-critical fields.
     */
    String buildNonCriticalFields(CourseEventContext eventCtx, CourseResponse courseResponse);

    /**
     * Builds XML containing only batch fields.
     */
    String buildBatchFields(CourseEventContext eventCtx, CourseResponse courseResponse);

    /**
     * Builds XML for the given field list.
     */
    String buildJournalContent(
            CourseEventContext eventCtx,
            CourseResponse courseResponse,
            String[] fieldsToProcess);

    /**
     * Updates or appends fields in existing JournalArticle content (course fields).
     */
    String updateOrAppendJournalContent(
            CourseEventContext eventCtx,
            CourseResponse courseResponse,
            String[] fieldsToProcess,
            String existingContent);

    /**
     * Builds XML containing only critical schedule fields.
     */
    String processCriticalFieldsSchedule(CourseEventContext eventCtx, ScheduleResponse scheduleResponse);

    /**
     * Updates or appends schedule fields in existing JournalArticle content.
     *
     * NOTE: Implementation may still throw runtime on XML issues, but signature is kept clean
     * to avoid API mismatch during refactor.
     */
    String updateOrAppendSchedules(
            CourseEventContext ctx,
            ScheduleResponse scheduleResponse,
            String[] fieldsToProcess,
            String existingXml);

    /**
     * Parses a JournalArticle's XML using a dynamic mapping.
     * No field names are hardcoded; mapping controls everything.
     *
     * @param xmlContent   The JournalArticle XML content.
     * @param fieldMapping Map<PojoFieldName, JournalArticleFieldName>
     * @return Map of pojoFieldName -> value (all as String)
     */
    Map<String, String> extractFieldsFromXml(String xmlContent, Map<String, String> fieldMapping);

    /**
     * Builds XML for all re-trigger fields (explicit field list).
     */
    String processAllRetriggerFields(CourseEventContext eventCtx, CourseResponse courseResponse, String[] fieldsToProcess);
}
