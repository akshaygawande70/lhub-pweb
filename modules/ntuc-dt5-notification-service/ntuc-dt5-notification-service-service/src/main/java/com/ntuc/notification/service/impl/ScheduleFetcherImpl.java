package com.ntuc.notification.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.audit.util.MdcUtil;
import com.ntuc.notification.constants.FieldMappingConstants;
import com.ntuc.notification.constants.ParameterGroupKeys;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.model.CourseResponse;
import com.ntuc.notification.model.ScheduleResponse;
import com.ntuc.notification.service.ClsConnectionHelper;
import com.ntuc.notification.service.JournalArticleService;
import com.ntuc.notification.service.ScheduleFetcher;
import com.ntuc.notification.service.XmlContentBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * OSGi implementation.
 *
 * New-world rules:
 * - NO legacy AuditLogger.
 * - NO EmailSender here.
 * - Persist observability via AuditEventWriter (DB audit).
 *
 * Note:
 * - Per your decision, checked exceptions (e.g., DocumentException) should be caught inside fetcher
 *   and rethrown as runtime after audit.
 */
@Component(service = ScheduleFetcher.class)
public class ScheduleFetcherImpl implements ScheduleFetcher {

    private static final Log _log = LogFactoryUtil.getLog(ScheduleFetcherImpl.class);

    private static final ObjectMapper _objectMapper = new ObjectMapper();

    @Reference private ClsConnectionHelper _clsConnectionHelper;
    @Reference private XmlContentBuilder _xmlContentBuilder;
    @Reference private JournalArticleService _journalArticleService;
    @Reference private JournalArticleLocalService _journalArticleLocalService;
    @Reference private ParameterGroupKeys _parameterGroupKeys;

    @Reference private AuditEventWriter _auditEventWriter;

    private volatile Map<ParameterKeyEnum, Object> _paramValues;

    @Activate
    protected void activate() {
        _paramValues = _parameterGroupKeys.getAllParameterValues();
    }

    @Override
    public Map<Long, ScheduleResponse> fetchAndProcessCriticalSchedule(CourseEventContext eventCtx) {
        final String corrId = corrId();
        final String requestId = UUID.randomUUID().toString();
        final String eventId = UUID.randomUUID().toString();

        final Ids ids = Ids.from(eventCtx);
        final String courseCode = safe(ids.courseCode);

        final long startMs = now();

        writeAudit(
                startMs, 0L, corrId, requestId, eventId, ids,
                AuditSeverity.INFO, AuditStatus.STARTED, AuditStep.ASYNC_PROCESS_START,
                AuditCategory.DT5_FLOW,
                "ScheduleFetcher.critical start",
                AuditErrorCode.NONE, null, null,
                details("phase", "CRITICAL")
        );

        try {
            JournalArticle article =
                    _journalArticleService.findExistingArticle(ids.groupId, ids.companyId, courseCode, eventCtx);

            if (article == null) {
                writeAudit(
                        startMs, now(), corrId, requestId, eventId, ids,
                        AuditSeverity.WARNING, AuditStatus.SKIPPED, AuditStep.ASYNC_PROCESS_END,
                        AuditCategory.JOURNAL_ARTICLE,
                        "ScheduleFetcher.critical skipped: article not found",
                        AuditErrorCode.JA_NOT_FOUND,
                        "Article not found for courseCode=" + courseCode,
                        null,
                        details("phase", "CRITICAL", "courseCode", courseCode)
                );
                return Collections.emptyMap();
            }

            ScheduleResponse scheduleResponse;
            try {
                scheduleResponse = _clsConnectionHelper.getLatestCourseSchedules(courseCode);
                if (scheduleResponse == null) {
                    throw new PortalException("No schedule data returned from CLS, courseCode=" + courseCode);
                }
            } catch (Exception ex) {
                _log.warn("CLS schedules unavailable, attempting DB XML reconstruction. courseCode=" + courseCode, ex);

                writeAudit(
                        startMs, now(), corrId, requestId, eventId, ids,
                        AuditSeverity.WARNING, AuditStatus.PARTIAL, AuditStep.ASYNC_PROCESS_END,
                        AuditCategory.CLS,
                        "ScheduleFetcher.critical CLS failed; attempting XML reconstruction",
                        AuditErrorCode.CLS_CONNECTION_FAILED,
                        safeMsg(ex),
                        ex.getClass().getName(),
                        details("phase", "CRITICAL", "fallback", "XML_RECONSTRUCT")
                );

                return reconstructSchedulesFromArticleXml(article, eventCtx, ids, corrId);
            }

            Map<ParameterKeyEnum, Object> paramValues = getParamValues();
            String[] criticalFields = (String[]) paramValues.get(ParameterKeyEnum.CLS_FIELD_CRITICAL_SCHEDULE);

            String xml = _xmlContentBuilder.updateOrAppendSchedules(
                    eventCtx,
                    scheduleResponse,
                    criticalFields,
                    article.getContent()
            );

            JournalArticle retArticle = _journalArticleService.processFields(
                    eventCtx,
                    eventCtx.getArticleConfig(),
                    /* courseResponse */ null,
                    xml,
                    /* isNew */ false,
                    article.getId(),
                    article
            );

            writeAudit(
                    startMs, now(), corrId, requestId, eventId, ids,
                    AuditSeverity.INFO,
                    (retArticle != null) ? AuditStatus.SUCCESS : AuditStatus.FAILED,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "ScheduleFetcher.critical persisted",
                    (retArticle != null) ? AuditErrorCode.NONE : AuditErrorCode.JA_PROCESSING_FAILED,
                    (retArticle != null) ? null : "processFields returned null",
                    null,
                    details(
                            "phase", "CRITICAL",
                            "articleId", String.valueOf(article.getId()),
                            "resultId", String.valueOf(idOf(retArticle))
                    )
            );

            Map<Long, ScheduleResponse> out = new HashMap<Long, ScheduleResponse>();
            out.put(article.getId(), scheduleResponse);
            return out;

        } catch (RuntimeException re) {
            writeAudit(
                    startMs, now(), corrId, requestId, eventId, ids,
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "ScheduleFetcher.critical failed",
                    AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(re),
                    re.getClass().getName(),
                    details("phase", "CRITICAL")
            );
            throw re;
        } catch (Exception e) {
            writeAudit(
                    startMs, now(), corrId, requestId, eventId, ids,
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "ScheduleFetcher.critical failed",
                    AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e),
                    e.getClass().getName(),
                    details("phase", "CRITICAL")
            );
            throw new IllegalStateException("Critical schedule processing failed courseCode=" + courseCode, e);
        }
    }

    private Map<Long, ScheduleResponse> reconstructSchedulesFromArticleXml(
            JournalArticle article,
            CourseEventContext eventCtx,
            Ids ids,
            String corrId) {

        final long startMs = now();
        final String requestId = UUID.randomUUID().toString();
        final String eventId = UUID.randomUUID().toString();

        try {
            String xmlContent = article.getContent();

            Map<ParameterKeyEnum, Object> paramValues = getParamValues();
            String[] critical = (String[]) paramValues.get(ParameterKeyEnum.CLS_FIELD_CRITICAL_SCHEDULE);
            String[] nonCritical = (String[]) paramValues.get(ParameterKeyEnum.CLS_FIELD_NONCRITICAL_SCHEDULE);

            Set<String> requiredFields = Arrays
                    .stream(new String[][]{critical, nonCritical})
                    .flatMap(Arrays::stream)
                    .collect(Collectors.toSet());

            Map<String, String> reversedMap = new HashMap<String, String>();
            for (Map.Entry<String, String> entry : FieldMappingConstants.LIFERAY_TO_SOURCE_FIELD_MAP.entrySet()) {
                reversedMap.put(entry.getValue(), entry.getKey());
            }

            Map<String, String> scheduleFieldsMap = reversedMap.entrySet()
                    .stream()
                    .filter(e -> requiredFields.contains(e.getKey()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new
                    ));

            Map<String, String> flatPojo = _xmlContentBuilder.extractFieldsFromXml(xmlContent, scheduleFieldsMap);

            if (allValuesEmptyOrNull(flatPojo)) {
                writeAudit(
                        startMs, now(), corrId, requestId, eventId, ids,
                        AuditSeverity.WARNING, AuditStatus.SKIPPED, AuditStep.ASYNC_PROCESS_END,
                        AuditCategory.DT5_FLOW,
                        "Schedule reconstruction skipped: extracted fields empty",
                        AuditErrorCode.NONE,
                        null,
                        null,
                        details(
                                "phase", "CRITICAL",
                                "fallback", "XML_RECONSTRUCT",
                                "articleId", String.valueOf(article.getId())
                        )
                );

                return Collections.emptyMap();
            }

            Map<String, Object> nestedMap = toNestedMap(flatPojo);
            ScheduleResponse scheduleResponseRet =
                    _objectMapper.convertValue(nestedMap, ScheduleResponse.class);

            Map<Long, ScheduleResponse> out = new HashMap<Long, ScheduleResponse>();
            out.put(article.getId(), scheduleResponseRet);

            writeAudit(
                    startMs, now(), corrId, requestId, eventId, ids,
                    AuditSeverity.INFO, AuditStatus.SUCCESS, AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "Schedule reconstruction success",
                    AuditErrorCode.NONE,
                    null,
                    null,
                    details(
                            "phase", "CRITICAL",
                            "fallback", "XML_RECONSTRUCT",
                            "articleId", String.valueOf(article.getId())
                    )
            );

            return out;

        } catch (Exception e) {
            writeAudit(
                    startMs, now(), corrId, requestId, eventId, ids,
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "Schedule reconstruction failed",
                    AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e),
                    e.getClass().getName(),
                    details(
                            "phase", "CRITICAL",
                            "fallback", "XML_RECONSTRUCT",
                            "articleId", String.valueOf(article.getId())
                    )
            );

            throw new IllegalStateException("Schedule XML reconstruction failed for articleId=" + article.getId(), e);
        }
    }

    @Override
    public JournalArticle fetchAndProcessNonCriticalSchedule(
            long articleId, ScheduleResponse scheduleResponse, CourseEventContext eventCtx) {

        final String corrId = corrId();
        final String requestId = UUID.randomUUID().toString();
        final String eventId = UUID.randomUUID().toString();

        final Ids ids = Ids.from(eventCtx);
        final long startMs = now();

        writeAudit(
                startMs, 0L, corrId, requestId, eventId, ids,
                AuditSeverity.INFO, AuditStatus.STARTED, AuditStep.ASYNC_PROCESS_START,
                AuditCategory.DT5_FLOW,
                "ScheduleFetcher.nonCritical start",
                AuditErrorCode.NONE, null, null,
                details("phase", "NON_CRITICAL", "articleId", String.valueOf(articleId))
        );

        try {
            Map<ParameterKeyEnum, Object> paramValues = getParamValues();
            String[] nonCriticalFields = (String[]) paramValues.get(ParameterKeyEnum.CLS_FIELD_NONCRITICAL_SCHEDULE);

            CourseResponse courseResponse =
                    _clsConnectionHelper.getCourseDetails(eventCtx, nonCriticalFields);

            if (courseResponse == null) {
                writeAudit(
                        startMs, now(), corrId, requestId, eventId, ids,
                        AuditSeverity.WARNING, AuditStatus.SKIPPED, AuditStep.ASYNC_PROCESS_END,
                        AuditCategory.CLS,
                        "ScheduleFetcher.nonCritical skipped: no course details from CLS",
                        AuditErrorCode.CLS_EMPTY_BODY,
                        "CLS returned null course details",
                        null,
                        details("phase", "NON_CRITICAL", "articleId", String.valueOf(articleId))
                );
                return null;
            }

            JournalArticle article = _journalArticleLocalService.getJournalArticle(articleId);

            String updatedXml = _xmlContentBuilder.updateOrAppendSchedules(
                    eventCtx,
                    scheduleResponse,
                    nonCriticalFields,
                    article.getContent()
            );

            JournalArticle retArticle = _journalArticleService.processFields(
                    eventCtx,
                    eventCtx.getArticleConfig(),
                    courseResponse,
                    updatedXml,
                    /* isNew */ false,
                    articleId,
                    article
            );

            writeAudit(
                    startMs, now(), corrId, requestId, eventId, ids,
                    AuditSeverity.INFO,
                    (retArticle != null) ? AuditStatus.SUCCESS : AuditStatus.FAILED,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "ScheduleFetcher.nonCritical persisted",
                    (retArticle != null) ? AuditErrorCode.NONE : AuditErrorCode.JA_PROCESSING_FAILED,
                    (retArticle != null) ? null : "processFields returned null",
                    null,
                    details(
                            "phase", "NON_CRITICAL",
                            "articleId", String.valueOf(articleId),
                            "resultId", String.valueOf(idOf(retArticle))
                    )
            );

            return retArticle;

        } catch (Exception e) {
            writeAudit(
                    startMs, now(), corrId, requestId, eventId, ids,
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "ScheduleFetcher.nonCritical failed",
                    AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e),
                    e.getClass().getName(),
                    details("phase", "NON_CRITICAL", "articleId", String.valueOf(articleId))
            );
            throw new IllegalStateException("Failed non-critical schedule processing for courseCode=" + safe(ids.courseCode), e);
        }
    }

    // ===== pure helpers (unit-test friendly) =====

    static boolean allValuesEmptyOrNull(Map<String, ?> map) {
        return map.values().stream()
                .allMatch(v -> v == null || (v instanceof String && ((String) v).trim().isEmpty()));
    }

    @SuppressWarnings("unchecked")
    static Map<String, Object> toNestedMap(Map<String, String> flatMap) {
        Map<String, Object> nested = new HashMap<String, Object>();
        for (Map.Entry<String, String> entry : flatMap.entrySet()) {
            String[] parts = entry.getKey().split("\\.");
            Map<String, Object> current = nested;

            for (int i = 0; i < parts.length - 1; i++) {
                Object child = current.get(parts[i]);
                if (!(child instanceof Map)) {
                    child = new HashMap<String, Object>();
                    current.put(parts[i], child);
                }
                current = (Map<String, Object>) child;
            }

            String value = entry.getValue();
            Object finalValue = null;

            if (value != null && !value.isEmpty()) {
                try {
                    finalValue = Integer.valueOf(value);
                } catch (NumberFormatException ignore) {
                    finalValue = value;
                }
            }

            current.put(parts[parts.length - 1], finalValue);
        }
        return nested;
    }

    // ===== small helpers =====

    private Map<ParameterKeyEnum, Object> getParamValues() {
        Map<ParameterKeyEnum, Object> local = _paramValues;
        if (local == null) {
            local = _parameterGroupKeys.getAllParameterValues();
            _paramValues = local;
        }
        return local;
    }

    private void writeAudit(
            long startMs,
            long endMs,
            String corrId,
            String requestId,
            String eventId,
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
                    .eventId(eventId)
                    .jobRunId("")
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

            if (details != null && !details.isEmpty()) {
                for (Map.Entry<String, String> e : details.entrySet()) {
                    b.detail(e.getKey(), e.getValue());
                }
            }

            _auditEventWriter.write(b.build());
        } catch (Exception ignore) {
            // audit must never break runtime
        }
    }

    private static Map<String, String> details(String... kv) {
        Map<String, String> m = new HashMap<String, String>();
        for (int i = 0; i + 1 < kv.length; i += 2) {
            m.put(kv[i], kv[i + 1] == null ? "" : kv[i + 1]);
        }
        return m;
    }

    private static long idOf(JournalArticle ja) {
        return (ja == null) ? 0L : ja.getId();
    }

    private static String corrId() {
        String c = MdcUtil.getCorrId();
        return (c != null) ? c : UUID.randomUUID().toString();
    }

    private static long now() {
        return System.currentTimeMillis();
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }

    private static String safeMsg(Throwable t) {
        if (t == null) return "Unknown error";
        String m = t.getMessage();
        return (m == null || m.trim().isEmpty()) ? t.getClass().getSimpleName() : m;
    }

    private static final class Ids {
        final long groupId;
        final long companyId;
        final long userId;
        final String courseCode;
        final long ntucDTId;

        private Ids(long g, long c, long u, String cc, long n) {
            this.groupId = g;
            this.companyId = c;
            this.userId = u;
            this.courseCode = cc;
            this.ntucDTId = n;
        }

        static Ids from(CourseEventContext ctx) {
            long g = (ctx != null) ? ctx.getGroupId() : 0L;
            long c = (ctx != null) ? ctx.getCompanyId() : 0L;
            long u = (ctx != null) ? ctx.getUserId() : 0L;
            String cc = (ctx != null && ctx.getCourseCode() != null) ? ctx.getCourseCode() : "";
            long n = (ctx != null) ? ctx.getNtucDTId() : 0L;
            return new Ids(g, c, u, cc, n);
        }
    }
}
