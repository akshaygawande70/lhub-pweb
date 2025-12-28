package com.ntuc.notification.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.CourseArticleConfig;
import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.audit.util.MdcUtil;

import com.ntuc.notification.constants.NotificationType;
import com.ntuc.notification.constants.ParameterGroupKeys;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.model.CourseResponse;
import com.ntuc.notification.service.ClsConnectionHelper;
import com.ntuc.notification.service.CourseFetcher;
import com.ntuc.notification.service.JournalArticleService;
import com.ntuc.notification.service.XmlContentBuilder;
import com.ntuc.notification.service.api.dto.CriticalProcessingResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Fetches and processes course data from CLS and updates Liferay articles accordingly.
 *
 * Rules:
 * - No direct email sending from here.
 * - Observability is DB-driven via AuditEventWriter only.
 * - Email (if any) is triggered centrally AFTER audit persistence (AuditEmailTriggerImpl).
 *
 * Note:
 * - OSGi component; do not unit-test directly outside OSGi.
 */
@Component(service = CourseFetcher.class)
public class CourseFetcherImpl implements CourseFetcher {

    private static final Log _log = LogFactoryUtil.getLog(CourseFetcherImpl.class);
    private static final ObjectMapper _objectMapper = new ObjectMapper();

    @Reference
    private ClsConnectionHelper _clsConnectionHelper;

    @Reference
    private XmlContentBuilder _xmlContentBuilder;

    @Reference
    private JournalArticleService _journalArticleService;

    @Reference
    private JournalArticleLocalService _journalArticleLocalService;

    @Reference
    private ParameterGroupKeys _parameterGroupKeys;

    @Reference
    private AuditEventWriter _auditEventWriter;

    private Map<ParameterKeyEnum, Object> _paramValues;

    @Activate
    protected void activate() {
        try {
            _paramValues = _parameterGroupKeys.getAllParameterValues();
        }
        catch (Throwable t) {
            _log.warn("Failed to pre-load parameter values in CourseFetcherImpl", t);
            _paramValues = new HashMap<ParameterKeyEnum, Object>();
        }
    }

    // ========================================================================
    // Option B APIs (new)
    // ========================================================================

    @Override
    public CriticalProcessingResult fetchAndProcessCriticalWithPayload(boolean isNew, CourseEventContext eventCtx) {
        InternalCriticalResult r = doCritical(isNew, eventCtx, /*capturePayload*/ true);

        if (r == null || r.updated == null) {
            return new CriticalProcessingResult("", 0L, "");
        }

        String payloadJson = "";

        // Only reuse payload for PUBLISHED (per your rule: returnedFields empty => CLS returns full fields)
        if (NotificationType.PUBLISHED.equalsIgnoreCase(safe(eventType(eventCtx)))) {
            payloadJson = safeJson(r.payloadJson);
        }

        return new CriticalProcessingResult(
                safe(r.updated.getUuid()),
                r.updated.getId(),
                payloadJson
        );
    }

    @Override
    public JournalArticle fetchAndProcessNonCriticalFromPayload(JournalArticle article, CourseEventContext eventCtx, String payloadJson) {
        final long startMs = System.currentTimeMillis();
        final String corrId = corrId();
        final String requestId = UUID.randomUUID().toString();

        final Ids ids = Ids.from(eventCtx);
        final CourseArticleConfig articleConfig = requireArticleConfig(eventCtx);
        final String courseCode = safeCourseCode(eventCtx);
        final String eventType = safe(eventType(eventCtx));

        if (article == null) {
            String msg = "Non-critical called with null article for courseCode=" + courseCode;

            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.WARNING, AuditStatus.SKIPPED, AuditStep.PROCESS_JOURNAL_ARTICLE,
                    AuditCategory.DT5_FLOW, msg, AuditErrorCode.VALIDATION_FAILED,
                    msg, IllegalArgumentException.class.getName(),
                    details("phase", "NON_CRITICAL", "eventType", eventType, "payloadReuse", "true")
            );

            throw new IllegalArgumentException("article must not be null");
        }

        // Only PUBLISHED uses payload reuse; others keep current behavior
        if (!NotificationType.PUBLISHED.equalsIgnoreCase(eventType)) {
            return fetchAndProcessNonCritical(article, eventCtx);
        }

        _log.info("Processing NON_CRITICAL (payload reuse) for courseCode=" + courseCode + ", articleId=" + article.getId());

        try {
            String[] fieldNonCritical = getParamArray(_paramValues, ParameterKeyEnum.CLS_FIELD_NONCRITICAL);
            String[] fieldBatch = getParamArray(_paramValues, ParameterKeyEnum.CLS_FIELD_BATCH);

            String[] fieldsToProcess = Stream.of(fieldNonCritical, fieldBatch)
                    .flatMap(Arrays::stream)
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);

            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.INFO, AuditStatus.STARTED, AuditStep.PROCESS_JOURNAL_ARTICLE,
                    AuditCategory.DT5_FLOW, "CourseFetcher non-critical start (payload reuse)", AuditErrorCode.NONE,
                    null, null,
                    details("phase", "NON_CRITICAL", "eventType", eventType,
                            "articleId", String.valueOf(article.getId()),
                            "fieldsCount", String.valueOf(fieldsToProcess.length),
                            "payloadReuse", "true")
            );

            CourseResponse courseResponse = fromJson(payloadJson);

            // If payload missing/unparseable, fallback to existing behavior (safe). Ideally should not happen.
            if (courseResponse == null || courseResponse.getBody() == null) {
                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.WARNING, AuditStatus.PARTIAL, AuditStep.PROCESS_JOURNAL_ARTICLE,
                        AuditCategory.DT5_FLOW,
                        "Payload missing/unparseable; fallback to CLS call (non-critical)",
                        AuditErrorCode.NONE,
                        "payloadJson missing/unparseable",
                        null,
                        details("phase", "NON_CRITICAL", "eventType", eventType,
                                "articleId", String.valueOf(article.getId()),
                                "payloadReuse", "true",
                                "fallbackToCls", "true")
                );

                return fetchAndProcessNonCritical(article, eventCtx);
            }

            String updatedXml = _xmlContentBuilder.updateOrAppendJournalContent(
                    eventCtx, courseResponse, fieldsToProcess, article.getContent());

            JournalArticle updated = _journalArticleService.processFields(
                    eventCtx, articleConfig, courseResponse, updatedXml, false, article.getId(), article);

            if (updated == null) {
                String msg = "JournalArticle returned null (non-critical payload reuse) for courseCode=" + courseCode;

                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.PROCESS_JOURNAL_ARTICLE,
                        AuditCategory.JOURNAL_ARTICLE, msg, AuditErrorCode.JA_RETURNED_NULL,
                        msg, null,
                        details("phase", "NON_CRITICAL", "eventType", eventType,
                                "articleId", String.valueOf(article.getId()),
                                "payloadReuse", "true")
                );

                return null;
            }

            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.INFO, AuditStatus.SUCCESS, AuditStep.PROCESS_JOURNAL_ARTICLE,
                    AuditCategory.DT5_FLOW, "CourseFetcher non-critical success (payload reuse)", AuditErrorCode.NONE,
                    null, null,
                    details("phase", "NON_CRITICAL", "eventType", eventType,
                            "articleId", String.valueOf(article.getId()),
                            "resultId", String.valueOf(updated.getId()),
                            "payloadReuse", "true")
            );

            return updated;

        } catch (Exception e) {
            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.PROCESS_JOURNAL_ARTICLE,
                    AuditCategory.DT5_FLOW, "CourseFetcher non-critical failed (payload reuse)", AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e), (e != null ? e.getClass().getName() : null),
                    details("phase", "NON_CRITICAL", "eventType", eventType,
                            "articleId", String.valueOf(article.getId()),
                            "payloadReuse", "true")
            );

            _log.error("Non-critical (payload reuse) failed for courseCode=" + courseCode, e);
            throw new IllegalStateException("Failed non-critical (payload reuse) for courseCode=" + courseCode, e);
        }
    }

    // ========================================================================
    // Public APIs (existing)
    // ========================================================================

    @Override
    public JournalArticle fetchAndProcessCritical(boolean isNew, CourseEventContext eventCtx) {
        InternalCriticalResult r = doCritical(isNew, eventCtx, /*capturePayload*/ false);
        return (r == null) ? null : r.updated;
    }

    private InternalCriticalResult doCritical(boolean isNew, CourseEventContext eventCtx, boolean capturePayload) {
        final long startMs = System.currentTimeMillis();
        final String corrId = corrId();
        final String requestId = UUID.randomUUID().toString();

        final Ids ids = Ids.from(eventCtx);
        final CourseArticleConfig articleConfig = requireArticleConfig(eventCtx);
        final String courseCode = safeCourseCode(eventCtx);
        final String eventType = safe(eventType(eventCtx));

        _log.info("Processing CRITICAL fields for courseCode=" + courseCode + ", eventType=" + eventType);

        try {
            String[] fieldCritical = getParamArray(_paramValues, ParameterKeyEnum.CLS_FIELD_CRITICAL);

            if (NotificationType.CHANGED.equalsIgnoreCase(eventType) && fieldCritical.length > 0) {
                fieldCritical = applyChangeFromIntersection(eventCtx, fieldCritical, _paramValues);
            }

            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.INFO, AuditStatus.STARTED, AuditStep.PROCESS_JOURNAL_ARTICLE,
                    AuditCategory.DT5_FLOW, "CourseFetcher critical start", AuditErrorCode.NONE,
                    null, null,
                    details("phase", "CRITICAL", "isNew", String.valueOf(isNew), "eventType", eventType,
                            "fieldsCount", String.valueOf(fieldCritical.length))
            );

            JournalArticle existing = findExistingArticle(eventCtx.getGroupId(), eventCtx.getCompanyId(), courseCode, eventCtx);

            boolean treatAsNew = isNew;

            if (!isNew && existing == null && NotificationType.CHANGED.equalsIgnoreCase(eventType)) {

                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.WARNING,
                        AuditStatus.PARTIAL,
                        AuditStep.PROCESS_JOURNAL_ARTICLE,
                        AuditCategory.DT5_FLOW,
                        "Course does not exist for CHANGED event; creating new course",
                        AuditErrorCode.JA_NOT_FOUND,
                        null,
                        null,
                        details(
                                "phase", "CRITICAL",
                                "eventType", eventType,
                                "action", "CREATE_NEW"
                        )
                );

                treatAsNew = true;
            }
            else if (!isNew && existing == null) {

                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.INFO,
                        AuditStatus.SKIPPED,
                        AuditStep.PROCESS_JOURNAL_ARTICLE,
                        AuditCategory.DT5_FLOW,
                        "No existing article found; skipping critical",
                        AuditErrorCode.NONE,
                        null,
                        null,
                        details("phase", "CRITICAL", "isNew", String.valueOf(isNew), "eventType", eventType)
                );

                return new InternalCriticalResult(null, "");
            }

            CourseResponse courseResponse = _clsConnectionHelper.getCourseDetails(eventCtx, fieldCritical);

            if (courseResponse == null || courseResponse.getBody() == null) {
                String msg = "Invalid CLS response: body missing for courseCode=" + courseCode;

                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.PROCESS_JOURNAL_ARTICLE,
                        AuditCategory.CLS, "CLS empty body during critical processing", AuditErrorCode.CLS_EMPTY_BODY,
                        msg, IllegalStateException.class.getName(),
                        details("phase", "CRITICAL", "isNew", String.valueOf(isNew), "eventType", eventType)
                );

                throw new IllegalStateException(msg);
            }

            List<String> missingFields = validateRequiredFields(courseResponse.getBody());
            if (!missingFields.isEmpty()) {
                String msg = "CLS payload missing required fields for courseCode=" + courseCode +
                        ": " + String.join(", ", missingFields);

                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.PROCESS_JOURNAL_ARTICLE,
                        AuditCategory.CLS, "CLS missing required fields (critical)", AuditErrorCode.CLS_MISSING_REQUIRED_FIELDS,
                        msg, IllegalArgumentException.class.getName(),
                        details("phase", "CRITICAL", "missingFields", String.join(",", missingFields), "eventType", eventType)
                );

                throw new IllegalArgumentException(msg);
            }

            JournalArticle updated;
            if (!treatAsNew && existing != null) {
                String updatedXml = _xmlContentBuilder.updateOrAppendJournalContent(
                        eventCtx, courseResponse, fieldCritical, existing.getContent());

                updated = _journalArticleService.processFields(
                        eventCtx, articleConfig, courseResponse, updatedXml, false, existing.getId(), existing);
            }
            else {
                String xml = _xmlContentBuilder.processCriticalFields(eventCtx, courseResponse);

                updated = _journalArticleService.processFields(
                        eventCtx, articleConfig, courseResponse, xml, true, 0L, null);
            }

            if (updated == null) {
                String msg = "JournalArticle processing returned null (critical) for courseCode=" + courseCode;

                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.PROCESS_JOURNAL_ARTICLE,
                        AuditCategory.JOURNAL_ARTICLE, msg, AuditErrorCode.JA_RETURNED_NULL,
                        msg, null,
                        details("phase", "CRITICAL", "eventType", eventType, "hadExisting", String.valueOf(existing != null))
                );

                throw new IllegalStateException(msg);
            }

            String payloadJson = "";
            if (capturePayload && NotificationType.PUBLISHED.equalsIgnoreCase(eventType)) {
                payloadJson = toJson(courseResponse);
            }

            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.INFO, AuditStatus.SUCCESS, AuditStep.PROCESS_JOURNAL_ARTICLE,
                    AuditCategory.DT5_FLOW, "CourseFetcher critical success", AuditErrorCode.NONE,
                    null, null,
                    details("phase", "CRITICAL", "isNew", String.valueOf(isNew), "eventType", eventType,
                            "resultId", String.valueOf(updated.getId()),
                            "hadExisting", String.valueOf(existing != null),
                            "payloadCaptured", String.valueOf(!payloadJson.isEmpty()))
            );

            return new InternalCriticalResult(updated, payloadJson);

        }
        catch (Exception e) {
            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.PROCESS_JOURNAL_ARTICLE,
                    AuditCategory.DT5_FLOW, "CourseFetcher critical failed", AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e), (e != null ? e.getClass().getName() : null),
                    details("phase", "CRITICAL", "isNew", String.valueOf(isNew), "eventType", safe(eventType(eventCtx)))
            );

            _log.error("Critical processing failed for courseCode=" + courseCode, e);
            throw new IllegalStateException("Failed critical processing for courseCode=" + courseCode, e);
        }
    }

    @Override
    public JournalArticle fetchAndProcessNonCritical(JournalArticle article, CourseEventContext eventCtx) {
        final long startMs = System.currentTimeMillis();
        final String corrId = corrId();
        final String requestId = UUID.randomUUID().toString();

        final Ids ids = Ids.from(eventCtx);
        final CourseArticleConfig articleConfig = requireArticleConfig(eventCtx);
        final String courseCode = safeCourseCode(eventCtx);
        final String eventType = safe(eventType(eventCtx));

        if (article == null) {
            String msg = "Non-critical called with null article for courseCode=" + courseCode;

            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.WARNING, AuditStatus.SKIPPED, AuditStep.PROCESS_JOURNAL_ARTICLE,
                    AuditCategory.DT5_FLOW, msg, AuditErrorCode.VALIDATION_FAILED,
                    msg, IllegalArgumentException.class.getName(),
                    details("phase", "NON_CRITICAL", "eventType", eventType)
            );

            throw new IllegalArgumentException("article must not be null");
        }

        _log.info("Processing NON_CRITICAL fields for courseCode=" + courseCode + ", articleId=" + article.getId());

        try {
            String[] fieldNonCritical = getParamArray(_paramValues, ParameterKeyEnum.CLS_FIELD_NONCRITICAL);
            String[] fieldBatch = getParamArray(_paramValues, ParameterKeyEnum.CLS_FIELD_BATCH);

            String[] fieldsToProcess;
            if (!NotificationType.CHANGED.equalsIgnoreCase(eventType)) {
                fieldsToProcess = Stream.of(fieldNonCritical, fieldBatch)
                        .flatMap(Arrays::stream)
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toArray(String[]::new);
            }
            else {
                fieldsToProcess = (fieldNonCritical != null) ? fieldNonCritical : new String[0];
            }

            if (NotificationType.CHANGED.equalsIgnoreCase(eventType)) {
                fieldsToProcess = applyChangeFromIntersection(eventCtx, fieldsToProcess, _paramValues);
            }

            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.INFO, AuditStatus.STARTED, AuditStep.PROCESS_JOURNAL_ARTICLE,
                    AuditCategory.DT5_FLOW, "CourseFetcher non-critical start", AuditErrorCode.NONE,
                    null, null,
                    details("phase", "NON_CRITICAL", "eventType", eventType,
                            "articleId", String.valueOf(article.getId()),
                            "fieldsCount", String.valueOf(fieldsToProcess.length))
            );

            CourseResponse courseResponse = _clsConnectionHelper.getCourseDetails(eventCtx, fieldsToProcess);
            if (courseResponse == null || courseResponse.getBody() == null) {
                String msg = "CLS empty body (non-critical) for courseCode=" + courseCode;

                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.WARNING, AuditStatus.FAILED, AuditStep.PROCESS_JOURNAL_ARTICLE,
                        AuditCategory.CLS, msg, AuditErrorCode.CLS_EMPTY_BODY,
                        msg, null,
                        details("phase", "NON_CRITICAL", "eventType", eventType, "articleId", String.valueOf(article.getId()))
                );

                return null;
            }

            String updatedXml = _xmlContentBuilder.updateOrAppendJournalContent(
                    eventCtx, courseResponse, fieldsToProcess, article.getContent());

            JournalArticle updated = _journalArticleService.processFields(
                    eventCtx, articleConfig, courseResponse, updatedXml, false, article.getId(), article);

            if (updated == null) {
                String msg = "JournalArticle returned null (non-critical) for courseCode=" + courseCode;

                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.PROCESS_JOURNAL_ARTICLE,
                        AuditCategory.JOURNAL_ARTICLE, msg, AuditErrorCode.JA_RETURNED_NULL,
                        msg, null,
                        details("phase", "NON_CRITICAL", "eventType", eventType, "articleId", String.valueOf(article.getId()))
                );

                return null;
            }

            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.INFO, AuditStatus.SUCCESS, AuditStep.PROCESS_JOURNAL_ARTICLE,
                    AuditCategory.DT5_FLOW, "CourseFetcher non-critical success", AuditErrorCode.NONE,
                    null, null,
                    details("phase", "NON_CRITICAL", "eventType", eventType,
                            "articleId", String.valueOf(article.getId()),
                            "resultId", String.valueOf(updated.getId()))
            );

            return updated;
        }
        catch (Exception e) {
            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.PROCESS_JOURNAL_ARTICLE,
                    AuditCategory.DT5_FLOW, "CourseFetcher non-critical failed", AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e), (e != null ? e.getClass().getName() : null),
                    details("phase", "NON_CRITICAL", "eventType", eventType, "articleId", String.valueOf(article.getId()))
            );

            _log.error("Non-critical processing failed for courseCode=" + courseCode, e);
            throw new IllegalStateException("Failed non-critical processing for courseCode=" + courseCode, e);
        }
    }

    @Override
    public JournalArticle fetchAndProcessCron(CourseEventContext eventCtx) {
        final long startMs = System.currentTimeMillis();
        final String corrId = corrId();
        final String requestId = UUID.randomUUID().toString();

        final Ids ids = Ids.from(eventCtx);
        final CourseArticleConfig articleConfig = requireArticleConfig(eventCtx);
        final String courseCode = safeCourseCode(eventCtx);
        final String eventType = safe(eventType(eventCtx));

        _log.info("Processing CRON fields for courseCode=" + courseCode + ", eventType=" + eventType);

        try {
            String[] fieldsToProcess = getParamArray(_paramValues, ParameterKeyEnum.CLS_FIELD_BATCH);

            if (NotificationType.CHANGED.equalsIgnoreCase(eventType) && fieldsToProcess.length > 0) {
                fieldsToProcess = applyChangeFromIntersection(eventCtx, fieldsToProcess, _paramValues);
            }

            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.INFO, AuditStatus.STARTED, AuditStep.CRON_PROCESS,
                    AuditCategory.DT5_FLOW, "CourseFetcher cron start", AuditErrorCode.NONE,
                    null, null,
                    details("phase", "CRON", "eventType", eventType, "fieldsCount", String.valueOf(fieldsToProcess.length))
            );

            CourseResponse courseResponse = _clsConnectionHelper.getCourseDetails(eventCtx, fieldsToProcess);
            if (courseResponse == null || courseResponse.getBody() == null) {
                String msg = "CLS empty body (cron) for courseCode=" + courseCode;

                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.WARNING, AuditStatus.FAILED, AuditStep.CRON_PROCESS,
                        AuditCategory.CLS, msg, AuditErrorCode.CLS_EMPTY_BODY,
                        msg, null,
                        details("phase", "CRON", "eventType", eventType)
                );

                return null;
            }

            JournalArticle article = findExistingArticle(eventCtx.getGroupId(), eventCtx.getCompanyId(), courseCode, eventCtx);
            if (article == null) {
                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.WARNING, AuditStatus.SKIPPED, AuditStep.CRON_PROCESS,
                        AuditCategory.DT5_FLOW, "No existing article for cron update; skipping", AuditErrorCode.NONE,
                        null, null,
                        details("phase", "CRON", "eventType", eventType)
                );
                return null;
            }

            String updatedXml = _xmlContentBuilder.updateOrAppendJournalContent(
                    eventCtx, courseResponse, fieldsToProcess, article.getContent());

            JournalArticle updated = _journalArticleService.processFields(
                    eventCtx, articleConfig, courseResponse, updatedXml, false, article.getId(), article);

            if (updated == null) {
                String msg = "JournalArticle returned null (cron) for courseCode=" + courseCode;

                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CRON_PROCESS,
                        AuditCategory.JOURNAL_ARTICLE, msg, AuditErrorCode.JA_RETURNED_NULL,
                        msg, null,
                        details("phase", "CRON", "eventType", eventType, "articleId", String.valueOf(article.getId()))
                );

                return null;
            }

            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.INFO, AuditStatus.SUCCESS, AuditStep.CRON_PROCESS,
                    AuditCategory.DT5_FLOW, "CourseFetcher cron success", AuditErrorCode.NONE,
                    null, null,
                    details("phase", "CRON", "eventType", eventType,
                            "articleId", String.valueOf(article.getId()),
                            "resultId", String.valueOf(updated.getId()))
            );

            return updated;
        }
        catch (Exception e) {
            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CRON_PROCESS,
                    AuditCategory.DT5_FLOW, "CourseFetcher cron failed", AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e), (e != null ? e.getClass().getName() : null),
                    details("phase", "CRON", "eventType", eventType)
            );

            _log.error("Cron processing failed for courseCode=" + courseCode, e);
            throw new IllegalStateException("Failed cron processing for courseCode=" + courseCode, e);
        }
    }

    @Override
    public JournalArticle findExistingArticle(long groupId, long companyId, String courseCode, CourseEventContext eventCtx) {
        return _journalArticleService.findExistingArticle(groupId, companyId, courseCode, eventCtx);
    }

    @Override
    public JournalArticle fetchAndProcessPublishedEventRetrigger(CourseEventContext eventCtx) {
        final CourseArticleConfig articleConfig = requireArticleConfig(eventCtx);
        final String courseCode = safeCourseCode(eventCtx);

        try {
            String[] fieldCritical = getParamArray(_paramValues, ParameterKeyEnum.CLS_FIELD_CRITICAL);
            String[] fieldNonCritical = getParamArray(_paramValues, ParameterKeyEnum.CLS_FIELD_NONCRITICAL);
            String[] fieldBatch = getParamArray(_paramValues, ParameterKeyEnum.CLS_FIELD_BATCH);

            String[] fieldsToProcess = Stream
                    .concat(Arrays.stream(fieldCritical), Arrays.stream(fieldNonCritical))
                    .toArray(String[]::new);

            fieldsToProcess = Stream
                    .concat(Arrays.stream(fieldsToProcess), Arrays.stream(fieldBatch))
                    .toArray(String[]::new);

            JournalArticle article = findExistingArticle(eventCtx.getGroupId(), eventCtx.getCompanyId(), courseCode, eventCtx);

            CourseResponse courseResponse = _clsConnectionHelper.getCourseDetails(eventCtx, fieldsToProcess);
            if (courseResponse == null || courseResponse.getBody() == null) {
                throw new IllegalStateException("Invalid CLS response: body missing for courseCode=" + courseCode);
            }

            if (article != null) {
                String updatedXml = _xmlContentBuilder.updateOrAppendJournalContent(
                        eventCtx, courseResponse, fieldsToProcess, article.getContent());

                return _journalArticleService.processFields(
                        eventCtx, articleConfig, courseResponse, updatedXml, false, article.getId(), article);
            }

            String xml = _xmlContentBuilder.processAllRetriggerFields(eventCtx, courseResponse, fieldsToProcess);

            return _journalArticleService.processFields(eventCtx, articleConfig, courseResponse, xml, true, 0L, null);
        }
        catch (Exception e) {
            _log.error("Error while processing retrigger for courseCode=" + courseCode, e);
            throw new IllegalStateException("Failed retrigger for courseCode=" + courseCode, e);
        }
    }

    @Override
    public JournalArticle processPublishedOTLPublishedEvent(String json, CourseEventContext eventCtx) {
        final CourseArticleConfig articleConfig = requireArticleConfig(eventCtx);

        // Prefer context courseCode; fallback to payload.
        final String resolvedCourseCode = resolveCourseCodeFromContextOrJson(eventCtx, json);

        _log.info("Processing ALL fields (OTL published) for courseCode=" + resolvedCourseCode);

        try {
            String[] fieldCritical = getParamArray(_paramValues, ParameterKeyEnum.CLS_FIELD_CRITICAL);
            String[] fieldNonCritical = getParamArray(_paramValues, ParameterKeyEnum.CLS_FIELD_NONCRITICAL);
            String[] fieldBatch = getParamArray(_paramValues, ParameterKeyEnum.CLS_FIELD_BATCH);

            String[] fieldsToProcess = Stream
                    .concat(Arrays.stream(fieldCritical), Arrays.stream(fieldNonCritical))
                    .toArray(String[]::new);

            fieldsToProcess = Stream
                    .concat(Arrays.stream(fieldsToProcess), Arrays.stream(fieldBatch))
                    .toArray(String[]::new);

            JournalArticle article = findExistingArticle(
                    eventCtx.getGroupId(),
                    eventCtx.getCompanyId(),
                    resolvedCourseCode,
                    eventCtx);

            CourseResponse courseResponse = _objectMapper.readValue(json, CourseResponse.class);
            if (courseResponse == null || courseResponse.getBody() == null) {
                throw new IllegalStateException("Invalid OTL payload: body missing for courseCode=" + resolvedCourseCode);
            }

            if (article != null) {
                String updatedXml = _xmlContentBuilder.updateOrAppendJournalContent(
                        eventCtx, courseResponse, fieldsToProcess, article.getContent());

                return _journalArticleService.processFields(
                        eventCtx, articleConfig, courseResponse, updatedXml, false, article.getId(), article);
            }

            String xml = _xmlContentBuilder.processAllRetriggerFields(eventCtx, courseResponse, fieldsToProcess);

            return _journalArticleService.processFields(eventCtx, articleConfig, courseResponse, xml, true, 0L, null);
        }
        catch (Exception e) {
            _log.error("Error while processing one time load for courseCode=" + resolvedCourseCode, e);
            throw new IllegalStateException("Failed one time load for courseCode=" + resolvedCourseCode, e);
        }
    }

    // ========================================================================
    // Internal helpers
    // ========================================================================

    private static final class InternalCriticalResult {
        final JournalArticle updated;
        final String payloadJson;

        private InternalCriticalResult(JournalArticle updated, String payloadJson) {
            this.updated = updated;
            this.payloadJson = (payloadJson == null) ? "" : payloadJson;
        }
    }

    private static String safeJson(String json) {
        return (json == null) ? "" : json;
    }

    private static String toJson(CourseResponse r) {
        try {
            if (r == null) {
                return "";
            }
            return _objectMapper.writeValueAsString(r);
        } catch (Exception e) {
            return "";
        }
    }

    private static CourseResponse fromJson(String json) {
        try {
            if (json == null || json.trim().isEmpty()) {
                return null;
            }
            return _objectMapper.readValue(json, CourseResponse.class);
        } catch (Exception e) {
            return null;
        }
    }

    private static CourseArticleConfig requireArticleConfig(CourseEventContext ctx) {
        if (ctx == null || ctx.getArticleConfig() == null) {
            throw new IllegalStateException("CourseEventContext.articleConfig is required but missing");
        }
        return ctx.getArticleConfig();
    }

    private static String resolveCourseCodeFromContextOrJson(CourseEventContext ctx, String json) {
        String cc = safeCourseCode(ctx);
        if (cc != null && !cc.trim().isEmpty()) {
            return cc.trim();
        }

        // Best-effort JSON fallback (no new DTOs, no extra deps)
        try {
            if (json == null || json.trim().isEmpty()) {
                return "";
            }
            CourseResponse cr = _objectMapper.readValue(json, CourseResponse.class);
            if (cr != null && cr.getBody() != null) {
                String fromBody = cr.getBody().getCourseCode();
                return (fromBody == null) ? "" : fromBody.trim();
            }
        } catch (Exception ignore) {
            // ignore
        }

        return "";
    }

    private static String safeCourseCode(CourseEventContext ctx) {
        if (ctx == null) {
            return "";
        }
        String cc = ctx.getCourseCode();
        return (cc == null) ? "" : cc;
    }

    private static String eventType(CourseEventContext ctx) {
        return (ctx == null) ? null : ctx.getEventType();
    }

    private static List<String> validateRequiredFields(CourseResponse.Body body) {
        if (body == null) {
            return Collections.singletonList("body");
        }

        List<String> missing = new java.util.ArrayList<String>();
        if (isBlank(body.getCourseCode())) missing.add("courseCode");
        if (isBlank(body.getCourseName())) missing.add("courseName");

        return missing;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Centralized, identical intersection logic.
     *
     * - Reads changeFromTypes from eventCtx
     * - Builds union of allowed fields from parameter keys:
     *   CLS_FIELD_CHANGE_FROM_{TYPE}
     * - Returns baseFields ∩ union (preserving baseFields order)
     */
    private static String[] applyChangeFromIntersection(
            CourseEventContext eventCtx,
            String[] baseFields,
            Map<ParameterKeyEnum, Object> paramValues) {

        if (eventCtx == null || baseFields == null || baseFields.length == 0 || paramValues == null || paramValues.isEmpty()) {
            return (baseFields != null) ? baseFields : new String[0];
        }

        List<String> changeFromTypes = eventCtx.getChangeFromTypes();
        if (changeFromTypes == null || changeFromTypes.isEmpty()) {
            return baseFields;
        }

        Set<String> unionUpper = new HashSet<String>();

        for (String changeFromType : changeFromTypes) {
            if (changeFromType == null || changeFromType.trim().isEmpty()) {
                continue;
            }

            String keyName = "CLS_FIELD_CHANGE_FROM_" + changeFromType.trim().toUpperCase(Locale.ROOT);

            try {
                ParameterKeyEnum key = ParameterKeyEnum.valueOf(keyName);
                String[] fields = getParamArray(paramValues, key);

                for (String f : fields) {
                    if (f == null) {
                        continue;
                    }
                    String ft = f.trim();
                    if (!ft.isEmpty()) {
                        unionUpper.add(ft.toUpperCase(Locale.ROOT));
                    }
                }
            }
            catch (IllegalArgumentException ignore) {
                // Missing enum key is non-fatal.
            }
        }

        if (unionUpper.isEmpty()) {
            return new String[0];
        }

        Set<String> finalFields = new LinkedHashSet<String>();
        for (String base : baseFields) {
            if (base == null) {
                continue;
            }
            String trimmed = base.trim();
            if (!trimmed.isEmpty() && unionUpper.contains(trimmed.toUpperCase(Locale.ROOT))) {
                finalFields.add(trimmed);
            }
        }

        return finalFields.toArray(new String[0]);
    }

    private static String[] getParamArray(Map<ParameterKeyEnum, Object> paramValues, ParameterKeyEnum key) {
        if (paramValues == null || key == null) return new String[0];

        Object value = paramValues.get(key);
        if (value == null) return new String[0];

        if (value instanceof String[]) {
            return Arrays.stream((String[]) value)
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
        }
        else if (value instanceof Collection) {
            return ((Collection<?>) value).stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
        }
        else if (value instanceof String) {
            return Arrays.stream(((String) value).split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
        }

        return new String[0];
    }

    private void audit(
            long startMs,
            long endMs,
            String corrId,
            String requestId,
            Ids ids,
            AuditSeverity severity,
            AuditStatus status,
            AuditStep step,
            AuditCategory category,
            String message,
            AuditErrorCode errorCode,
            String errorMessage,
            String exceptionClass,
            Map<String, String> details) {

        try {
            AuditEvent.Builder b = new AuditEvent.Builder()
                    .startTimeMs(startMs)
                    .endTimeMs(endMs)
                    .correlationId(corrId)
                    .requestId(requestId)
                    .jobRunId("")
                    .eventId(UUID.randomUUID().toString())
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(safe(ids.courseCode))
                    .ntucDTId(ids.ntucDTId)
                    .severity(severity)
                    .status(status)
                    .step(step)
                    .category(category)
                    .message(message)
                    .errorCode(errorCode)
                    .errorMessage(errorMessage)
                    .exceptionClass(exceptionClass);

            if (details != null) {
                for (Map.Entry<String, String> e : details.entrySet()) {
                    b.detail(e.getKey(), e.getValue());
                }
            }

            _auditEventWriter.write(b.build());
        }
        catch (Exception ignore) {
            // Must never break runtime flow
        }
    }

    private static Map<String, String> details(String... kv) {
        Map<String, String> m = new HashMap<String, String>();
        for (int i = 0; i + 1 < kv.length; i += 2) {
            m.put(kv[i], kv[i + 1] == null ? "" : kv[i + 1]);
        }
        return m;
    }

    private static String safeMsg(Throwable t) {
        if (t == null) return "Unknown error";
        String m = t.getMessage();
        return (m == null || m.trim().isEmpty()) ? t.getClass().getSimpleName() : m;
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }

    private static String corrId() {
        String c = MdcUtil.getCorrId();
        return (c != null) ? c : UUID.randomUUID().toString();
    }

    private static final class Ids {
        final long groupId;
        final long companyId;
        final long userId;
        final String courseCode;
        final long ntucDTId;

        private Ids(long g, long c, long u, String cc, long n) {
            groupId = g;
            companyId = c;
            userId = u;
            courseCode = cc;
            ntucDTId = n;
        }

        static Ids from(CourseEventContext ctx) {
            long g = (ctx != null) ? ctx.getGroupId() : 0L;
            long c = (ctx != null) ? ctx.getCompanyId() : 0L;
            long u = (ctx != null) ? ctx.getUserId() : 0L;
            String cc = (ctx != null) ? ctx.getCourseCode() : null;
            long n = (ctx != null) ? ctx.getNtucDTId() : 0L;
            return new Ids(g, c, u, cc, n);
        }
    }
}
