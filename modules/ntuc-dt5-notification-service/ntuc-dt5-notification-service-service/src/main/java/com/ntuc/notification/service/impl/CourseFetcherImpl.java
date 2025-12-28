package com.ntuc.notification.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.journal.model.JournalArticle;
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
 * Orchestrates course synchronization between CLS (external source) and Liferay JournalArticles.
 *
 * <p><strong>Business purpose:</strong> keeps course pages accurate by applying authoritative updates from CLS for
 * critical and non-critical fields, including cron/batch flows and one-time-load payloads.</p>
 *
 * <p><strong>Technical purpose:</strong> reads field-selection configuration from ParameterGroupKeys, fetches CLS payloads
 * via ClsConnectionHelper, transforms payload into JournalArticle XML via XmlContentBuilder, and persists updates through
 * JournalArticleService. Observability is captured as DB-backed audit events via AuditEventWriter.</p>
 *
 * <p><strong>Audit model:</strong> audit logs are the single source of truth for reconstructing timelines. This component
 * records STARTED (endTimeMs=0 semantics are respected by the caller that chooses values), SUCCESS/FAILED/SKIPPED/PARTIAL
 * outcomes with canonical {@link AuditStep} layer boundaries; context-specific detail is carried in message/detailsJson.</p>
 *
 * <p><strong>Email policy:</strong> no code path sends email directly. Any alerting is audit-driven and triggered
 * centrally after audit persistence (outside this component).</p>
 *
 * <p><strong>Testability note:</strong> this is an OSGi DS component; plain JUnit tests can cover orchestration behavior
 * by mocking referenced services, and can cover static helpers deterministically.</p>
 *
 * @author @akshaygawande
 */
@Component(service = CourseFetcher.class)
public class CourseFetcherImpl implements CourseFetcher {

    private static final Log _log = LogFactoryUtil.getLog(CourseFetcherImpl.class);

    /**
     * Shared mapper for payload reuse and OTL parsing.
     * Kept static to avoid DS lifecycle overhead and to ensure consistent parsing rules across calls.
     */
    private static final ObjectMapper _objectMapper = new ObjectMapper();

    @Reference
    private ClsConnectionHelper _clsConnectionHelper;

    @Reference
    private XmlContentBuilder _xmlContentBuilder;

    @Reference
    private JournalArticleService _journalArticleService;

    @Reference
    private ParameterGroupKeys _parameterGroupKeys;

    @Reference
    private AuditEventWriter _auditEventWriter;

    /**
     * Cached parameter values resolved at activation time.
     *
     * <p>Expected to hold arrays/collections/strings for keys such as CLS_FIELD_CRITICAL, CLS_FIELD_NONCRITICAL,
     * CLS_FIELD_BATCH, and CLS_FIELD_CHANGE_FROM_*.</p>
     */
    private Map<ParameterKeyEnum, Object> _paramValues;

    /**
     * Preloads parameter values during DS activation to minimize per-request calls to ParameterGroupKeys.
     *
     * <p><strong>Business purpose:</strong> ensures course sync flows have immediate access to field-selection configuration.</p>
     * <p><strong>Technical purpose:</strong> caches the full parameter map; on failure, falls back to an empty map to keep
     * runtime flows operational.</p>
     *
     * <p><strong>Inputs/Invariants:</strong> called by OSGi DS; must not throw.</p>
     * <p><strong>Side effects:</strong> initializes {@link #_paramValues}.</p>
     * <p><strong>Audit behavior:</strong> no audit events are written here; activation failures are logged only.</p>
     * <p><strong>Return semantics:</strong> void; never throws (any error results in empty map).</p>
     */
    @Activate
    protected void activate() {
        try {
            _paramValues = _parameterGroupKeys.getAllParameterValues();
        }
        catch (Throwable t) {
            // Activation should never block the bundle start; runtime will continue with default behavior.
            _log.warn("Failed to pre-load parameter values in CourseFetcherImpl", t);
            _paramValues = new HashMap<ParameterKeyEnum, Object>();
        }
    }

    // ========================================================================
    // Option B APIs (new)
    // ========================================================================

    /**
     * Fetches and processes critical fields and (optionally) returns a reusable CLS payload for the PUBLISHED flow.
     *
     * <p><strong>Business purpose:</strong> supports a two-phase update where critical changes are applied first and
     * the PUBLISHED event can reuse the fetched payload to avoid refetching for non-critical updates.</p>
     * <p><strong>Technical purpose:</strong> delegates to the internal critical pipeline with payload capture enabled and
     * returns (uuid, articleId, payloadJson). Payload is returned only for PUBLISHED to align with CLS "returnedFields"
     * behavior and to limit accidental reuse in other flows.</p>
     *
     * <p><strong>Inputs/Invariants:</strong> {@code eventCtx} must include {@link CourseEventContext#getArticleConfig()}.</p>
     * <p><strong>Side effects:</strong> may write/update JournalArticles; writes audit events via {@link AuditEventWriter};
     * calls CLS via {@link ClsConnectionHelper}.</p>
     * <p><strong>Audit behavior:</strong> audit events are emitted inside the critical pipeline (STARTED/SUCCESS/FAILED etc.).</p>
     * <p><strong>Return semantics:</strong> never returns null; returns empty identifiers when critical processing yields no
     * updated article (e.g., skip paths).</p>
     *
     * @param isNew whether the caller intends to create a new course article
     * @param eventCtx workflow context containing tenant/group/user identifiers and article config
     * @return critical processing result including updated article identifiers and optional payload JSON for reuse
     * @throws IllegalStateException if critical processing fails or required context is missing
     */
    @Override
    public CriticalProcessingResult fetchAndProcessCriticalWithPayload(boolean isNew, CourseEventContext eventCtx) {
        InternalCriticalResult r = doCritical(isNew, eventCtx, /*capturePayload*/ true);

        if (r == null || r.updated == null) {
            return new CriticalProcessingResult("", 0L, "");
        }

        String payloadJson = "";

        // Only reuse payload for PUBLISHED (per rule: returnedFields empty => CLS returns full fields).
        if (NotificationType.PUBLISHED.equalsIgnoreCase(safe(eventType(eventCtx)))) {
            payloadJson = safeJson(r.payloadJson);
        }

        return new CriticalProcessingResult(
                safe(r.updated.getUuid()),
                r.updated.getId(),
                payloadJson
        );
    }

    /**
     * Applies non-critical updates using a previously captured CLS payload (PUBLISHED flow only).
     *
     * <p><strong>Business purpose:</strong> prevents double-fetching CLS for the PUBLISHED event by reusing the payload
     * captured during critical processing, while still updating non-critical and batch fields.</p>
     * <p><strong>Technical purpose:</strong> parses {@code payloadJson} into {@link CourseResponse}; if missing/unparseable,
     * safely falls back to the standard CLS call path to preserve correctness. Updates JournalArticle XML through
     * {@link XmlContentBuilder} and persists through {@link JournalArticleService}.</p>
     *
     * <p><strong>Inputs/Invariants:</strong> {@code article} must be non-null. Payload reuse is only applied when
     * {@code eventType=PUBLISHED}; other event types delegate to {@link #fetchAndProcessNonCritical(JournalArticle, CourseEventContext)}.</p>
     * <p><strong>Side effects:</strong> may update a JournalArticle; may call CLS on fallback; writes audit events.</p>
     * <p><strong>Audit behavior:</strong> writes canonical EXECUTION events:
     * STARTED at entry, SUCCESS on update, FAILED on exception, PARTIAL when payload is unusable and flow falls back to CLS.
     * The audit record is the authoritative trace; no email is sent directly.</p>
     * <p><strong>Return semantics:</strong> returns updated JournalArticle or null if JournalArticleService returns null;
     * throws {@link IllegalArgumentException} when {@code article} is null; throws {@link IllegalStateException} for unexpected failures.</p>
     *
     * @param article existing JournalArticle to update (required)
     * @param eventCtx workflow context containing tenant/group/user identifiers and article config
     * @param payloadJson CLS payload captured during critical processing
     * @return updated JournalArticle, or null when downstream update returns null
     * @throws IllegalArgumentException when {@code article} is null
     * @throws IllegalStateException when processing fails unexpectedly
     */
    @Override
    public JournalArticle fetchAndProcessNonCriticalFromPayload(
            JournalArticle article, CourseEventContext eventCtx, String payloadJson) {

        final long startMs = System.currentTimeMillis();
        final String corrId = corrId();
        final String requestId = UUID.randomUUID().toString();

        final Ids ids = Ids.from(eventCtx);
        final CourseArticleConfig articleConfig = requireArticleConfig(eventCtx);
        final String courseCode = safeCourseCode(eventCtx);
        final String eventType = safe(eventType(eventCtx));

        if (article == null) {
            String msg = "Non-critical called with null article for courseCode=" + courseCode;

            // Using VALIDATION as this is input validation at the method boundary.
            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.WARNING, AuditStatus.SKIPPED, AuditStep.VALIDATION,
                    AuditCategory.DT5_FLOW, msg, AuditErrorCode.VALIDATION_FAILED,
                    msg, IllegalArgumentException.class.getName(),
                    details("phase", "NON_CRITICAL", "eventType", eventType, "payloadReuse", "true")
            );

            throw new IllegalArgumentException("article must not be null");
        }

        // Payload reuse is intentionally restricted to PUBLISHED to avoid applying stale or partial payloads.
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

            // Replaced non-canonical step PROCESS_JOURNAL_ARTICLE -> EXECUTION.
            // Using EXECUTION as this audit captures the primary orchestration of non-critical updates.
            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.INFO, AuditStatus.STARTED, AuditStep.EXECUTION,
                    AuditCategory.DT5_FLOW, "CourseFetcher non-critical start (payload reuse)", AuditErrorCode.NONE,
                    null, null,
                    details("phase", "NON_CRITICAL", "eventType", eventType,
                            "articleId", String.valueOf(article.getId()),
                            "fieldsCount", String.valueOf(fieldsToProcess.length),
                            "payloadReuse", "true")
            );

            CourseResponse courseResponse = fromJson(payloadJson);

            // Defensive fallback: if payload is missing/unparseable, revert to canonical CLS call to avoid partial updates.
            if (courseResponse == null || courseResponse.getBody() == null) {
                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.WARNING, AuditStatus.PARTIAL, AuditStep.EXECUTION,
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

                // Null from JournalArticleService is treated as a FAILED outcome because persistence did not complete.
                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.EXECUTION,
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
                    AuditSeverity.INFO, AuditStatus.SUCCESS, AuditStep.EXECUTION,
                    AuditCategory.DT5_FLOW, "CourseFetcher non-critical success (payload reuse)", AuditErrorCode.NONE,
                    null, null,
                    details("phase", "NON_CRITICAL", "eventType", eventType,
                            "articleId", String.valueOf(article.getId()),
                            "resultId", String.valueOf(updated.getId()),
                            "payloadReuse", "true")
            );

            return updated;

        }
        catch (Exception e) {
            // FAILED captures the end state; message/details explain operation and that payload reuse was attempted.
            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.EXECUTION,
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

    /**
     * Fetches and processes critical course fields by calling CLS and writing to the target JournalArticle.
     *
     * <p><strong>Business purpose:</strong> ensures that core course identity and mandatory fields are always consistent
     * with CLS (e.g., code/name), forming the authoritative base for a course page.</p>
     * <p><strong>Technical purpose:</strong> computes critical field list (including CHANGED intersection rules), reads
     * CLS payload, validates required payload fields, then creates or updates the article content via builders/services.</p>
     *
     * <p><strong>Inputs/Invariants:</strong> {@code eventCtx} must contain companyId/groupId/userId/courseCode and
     * a non-null {@link CourseArticleConfig}. If {@code isNew=false}, existing article lookup determines update behavior.</p>
     * <p><strong>Side effects:</strong> CLS call; JournalArticle create/update; audit writes.</p>
     * <p><strong>Audit behavior:</strong> EXECUTION STARTED, SUCCESS on persistence, FAILED on validation/CLS/persistence failures,
     * PARTIAL/SKIPPED for specific control-flow outcomes (e.g., CHANGED without existing -> treat as new; non-CHANGED without existing -> skip).</p>
     * <p><strong>Return semantics:</strong> returns updated/created JournalArticle; may return null when control-flow results in skip
     * (e.g., no existing and not CHANGED); throws {@link IllegalStateException} / {@link IllegalArgumentException} for failures.</p>
     *
     * @param isNew whether to create a new JournalArticle (true) or update an existing one (false)
     * @param eventCtx workflow context containing identifiers and article configuration
     * @return updated JournalArticle, or null when critical processing is intentionally skipped
     * @throws IllegalStateException on CLS/body errors or unexpected failures
     * @throws IllegalArgumentException when required fields are missing in CLS payload
     */
    @Override
    public JournalArticle fetchAndProcessCritical(boolean isNew, CourseEventContext eventCtx) {
        InternalCriticalResult r = doCritical(isNew, eventCtx, /*capturePayload*/ false);
        return (r == null) ? null : r.updated;
    }

    /**
     * Internal critical processing pipeline with optional payload capture for later reuse.
     *
     * <p><strong>Business purpose:</strong> provides a single, consistent implementation for critical updates across
     * standard and payload-capture entry points.</p>
     * <p><strong>Technical purpose:</strong> centralizes field selection, CHANGED intersection logic, existing article
     * resolution, CLS fetch, required-field validation, XML build, and JournalArticle persistence. Optionally serializes
     * CLS payload for reuse when requested.</p>
     *
     * <p><strong>Inputs/Invariants:</strong> requires {@code eventCtx.articleConfig}; respects CHANGED semantics:
     * when event is CHANGED and no article exists, flow converts to "create new" rather than skipping.</p>
     * <p><strong>Side effects:</strong> CLS fetch; JournalArticle create/update; audit writes; log writes.</p>
     * <p><strong>Audit behavior:</strong> all major outcomes recorded at EXECUTION with phase=CRITICAL,
     * including errorCode and exceptionClass where applicable. Messages and details provide operational specificity
     * without proliferating steps.</p>
     * <p><strong>Return semantics:</strong> returns {@link InternalCriticalResult} where updated may be null on skip paths.
     * Throws {@link IllegalStateException} or {@link IllegalArgumentException} for failure paths.</p>
     *
     * @param isNew whether the caller intends to create a new record
     * @param eventCtx workflow context
     * @param capturePayload when true, serializes CLS payload for PUBLISHED events
     * @return internal result containing updated article and optional payload JSON
     */
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

            // CHANGED uses intersection of base critical fields with changeFromTypes-selected allowlist.
            if (NotificationType.CHANGED.equalsIgnoreCase(eventType) && fieldCritical.length > 0) {
                fieldCritical = applyChangeFromIntersection(eventCtx, fieldCritical, _paramValues);
            }

            // Replaced non-canonical step PROCESS_JOURNAL_ARTICLE -> EXECUTION.
            // Using EXECUTION as this audit captures the primary orchestration of critical processing.
            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.INFO, AuditStatus.STARTED, AuditStep.EXECUTION,
                    AuditCategory.DT5_FLOW, "CourseFetcher critical start", AuditErrorCode.NONE,
                    null, null,
                    details("phase", "CRITICAL", "isNew", String.valueOf(isNew), "eventType", eventType,
                            "fieldsCount", String.valueOf(fieldCritical.length))
            );

            JournalArticle existing = findExistingArticle(
                    eventCtx.getGroupId(), eventCtx.getCompanyId(), courseCode, eventCtx);

            boolean treatAsNew = isNew;

            if (!isNew && existing == null && NotificationType.CHANGED.equalsIgnoreCase(eventType)) {

                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.WARNING,
                        AuditStatus.PARTIAL,
                        AuditStep.EXECUTION,
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

                // Non-CHANGED update without an existing article is intentionally skipped.
                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.INFO,
                        AuditStatus.SKIPPED,
                        AuditStep.EXECUTION,
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

            // CLS response body is the contract boundary; empty body means the workflow cannot proceed reliably.
            if (courseResponse == null || courseResponse.getBody() == null) {
                String msg = "Invalid CLS response: body missing for courseCode=" + courseCode;

                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CLS_FETCH,
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

                // Using VALIDATION as this failure is about required business fields, not transport or persistence.
                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.VALIDATION,
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

                // Using JA_PROCESS as this failure is in the JournalArticle persistence/processing layer.
                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.JA_PROCESS,
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
                    AuditSeverity.INFO, AuditStatus.SUCCESS, AuditStep.EXECUTION,
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
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.EXECUTION,
                    AuditCategory.DT5_FLOW, "CourseFetcher critical failed", AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e), (e != null ? e.getClass().getName() : null),
                    details("phase", "CRITICAL", "isNew", String.valueOf(isNew), "eventType", safe(eventType(eventCtx)))
            );

            _log.error("Critical processing failed for courseCode=" + courseCode, e);
            throw new IllegalStateException("Failed critical processing for courseCode=" + courseCode, e);
        }
    }

    /**
     * Fetches non-critical and batch fields from CLS and updates an existing JournalArticle.
     *
     * <p><strong>Business purpose:</strong> keeps supplementary course fields (non-critical content and batch-managed fields)
     * synchronized without requiring a full "critical" reprocessing.</p>
     * <p><strong>Technical purpose:</strong> computes the field list based on event type (CHANGED vs others), calls CLS for
     * those fields, merges values into the existing article XML, and persists the update via JournalArticleService.</p>
     *
     * <p><strong>Inputs/Invariants:</strong> {@code article} must be non-null; {@code eventCtx.articleConfig} is required.</p>
     * <p><strong>Side effects:</strong> CLS call; JournalArticle update; audit writes.</p>
     * <p><strong>Audit behavior:</strong> EXECUTION STARTED/SUCCESS/FAILED, with errorCode describing CLS empty body
     * or JA null-return conditions. No email is sent directly.</p>
     * <p><strong>Return semantics:</strong> returns updated JournalArticle; returns null if CLS body is empty or downstream returns null;
     * throws {@link IllegalArgumentException} when {@code article} is null; throws {@link IllegalStateException} for unexpected failures.</p>
     *
     * @param article existing JournalArticle to update (required)
     * @param eventCtx workflow context
     * @return updated JournalArticle or null on non-exceptional failure outcomes
     */
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

            // Using VALIDATION as this is input validation at the method boundary.
            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.WARNING, AuditStatus.SKIPPED, AuditStep.VALIDATION,
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
                // For CHANGED, non-critical processing starts from non-critical base list only.
                fieldsToProcess = (fieldNonCritical != null) ? fieldNonCritical : new String[0];
            }

            if (NotificationType.CHANGED.equalsIgnoreCase(eventType)) {
                fieldsToProcess = applyChangeFromIntersection(eventCtx, fieldsToProcess, _paramValues);
            }

            // Replaced non-canonical step PROCESS_JOURNAL_ARTICLE -> EXECUTION.
            // Using EXECUTION as this audit captures the primary orchestration of non-critical updates.
            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.INFO, AuditStatus.STARTED, AuditStep.EXECUTION,
                    AuditCategory.DT5_FLOW, "CourseFetcher non-critical start", AuditErrorCode.NONE,
                    null, null,
                    details("phase", "NON_CRITICAL", "eventType", eventType,
                            "articleId", String.valueOf(article.getId()),
                            "fieldsCount", String.valueOf(fieldsToProcess.length))
            );

            CourseResponse courseResponse = _clsConnectionHelper.getCourseDetails(eventCtx, fieldsToProcess);
            if (courseResponse == null || courseResponse.getBody() == null) {
                String msg = "CLS empty body (non-critical) for courseCode=" + courseCode;

                // FAILED indicates the operation ended without a usable CLS response (caller can decide retrigger/alert via audit).
                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.WARNING, AuditStatus.FAILED, AuditStep.CLS_FETCH,
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
                        AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.JA_PROCESS,
                        AuditCategory.JOURNAL_ARTICLE, msg, AuditErrorCode.JA_RETURNED_NULL,
                        msg, null,
                        details("phase", "NON_CRITICAL", "eventType", eventType, "articleId", String.valueOf(article.getId()))
                );

                return null;
            }

            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.INFO, AuditStatus.SUCCESS, AuditStep.EXECUTION,
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
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.EXECUTION,
                    AuditCategory.DT5_FLOW, "CourseFetcher non-critical failed", AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e), (e != null ? e.getClass().getName() : null),
                    details("phase", "NON_CRITICAL", "eventType", eventType, "articleId", String.valueOf(article.getId()))
            );

            _log.error("Non-critical processing failed for courseCode=" + courseCode, e);
            throw new IllegalStateException("Failed non-critical processing for courseCode=" + courseCode, e);
        }
    }

    /**
     * Executes the cron/batch update path using batch-selected CLS fields against an existing course article.
     *
     * <p><strong>Business purpose:</strong> keeps course pages updated on a schedule, even without real-time events.</p>
     * <p><strong>Technical purpose:</strong> fetches CLS batch fields, optionally applies CHANGED intersection rules,
     * finds existing article, applies XML updates, and persists via JournalArticleService.</p>
     *
     * <p><strong>Inputs/Invariants:</strong> requires {@code eventCtx.articleConfig}; expects existing article for updates.</p>
     * <p><strong>Side effects:</strong> CLS call; JournalArticle update; audit writes.</p>
     * <p><strong>Audit behavior:</strong> replaced non-canonical step CRON_PROCESS -> ENTRY.
     * ENTRY represents the cron-trigger boundary; specifics (trigger=CRON, phase=CRON) are recorded in message/details.</p>
     * <p><strong>Return semantics:</strong> returns updated article; returns null when CLS body missing or when no article exists;
     * throws {@link IllegalStateException} on unexpected failures.</p>
     *
     * @param eventCtx workflow context
     * @return updated JournalArticle or null when cron cannot proceed
     */
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

            // Replaced non-canonical step CRON_PROCESS -> ENTRY.
            // Using ENTRY as this audit captures the cron-trigger boundary for this flow.
            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.INFO, AuditStatus.STARTED, AuditStep.ENTRY,
                    AuditCategory.DT5_FLOW, "CourseFetcher cron start", AuditErrorCode.NONE,
                    null, null,
                    details("phase", "CRON", "eventType", eventType, "fieldsCount", String.valueOf(fieldsToProcess.length),
                            "trigger", "CRON")
            );

            CourseResponse courseResponse = _clsConnectionHelper.getCourseDetails(eventCtx, fieldsToProcess);
            if (courseResponse == null || courseResponse.getBody() == null) {
                String msg = "CLS empty body (cron) for courseCode=" + courseCode;

                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.WARNING, AuditStatus.FAILED, AuditStep.CLS_FETCH,
                        AuditCategory.CLS, msg, AuditErrorCode.CLS_EMPTY_BODY,
                        msg, null,
                        details("phase", "CRON", "eventType", eventType, "trigger", "CRON")
                );

                return null;
            }

            JournalArticle article = findExistingArticle(eventCtx.getGroupId(), eventCtx.getCompanyId(), courseCode, eventCtx);
            if (article == null) {
                audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.WARNING, AuditStatus.SKIPPED, AuditStep.ENTRY,
                        AuditCategory.DT5_FLOW, "No existing article for cron update; skipping", AuditErrorCode.NONE,
                        null, null,
                        details("phase", "CRON", "eventType", eventType, "trigger", "CRON")
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
                        AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.JA_PROCESS,
                        AuditCategory.JOURNAL_ARTICLE, msg, AuditErrorCode.JA_RETURNED_NULL,
                        msg, null,
                        details("phase", "CRON", "eventType", eventType, "articleId", String.valueOf(article.getId()),
                                "trigger", "CRON")
                );

                return null;
            }

            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.INFO, AuditStatus.SUCCESS, AuditStep.ENTRY,
                    AuditCategory.DT5_FLOW, "CourseFetcher cron success", AuditErrorCode.NONE,
                    null, null,
                    details("phase", "CRON", "eventType", eventType,
                            "articleId", String.valueOf(article.getId()),
                            "resultId", String.valueOf(updated.getId()),
                            "trigger", "CRON")
            );

            return updated;
        }
        catch (Exception e) {
            audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.ENTRY,
                    AuditCategory.DT5_FLOW, "CourseFetcher cron failed", AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e), (e != null ? e.getClass().getName() : null),
                    details("phase", "CRON", "eventType", eventType, "trigger", "CRON")
            );

            _log.error("Cron processing failed for courseCode=" + courseCode, e);
            throw new IllegalStateException("Failed cron processing for courseCode=" + courseCode, e);
        }
    }

    /**
     * Delegates to {@link JournalArticleService} to find an existing course article by course code.
     *
     * <p><strong>Business purpose:</strong> determines whether the course already exists in the portal.</p>
     * <p><strong>Technical purpose:</strong> uses the service layer to perform lookup with Liferay-aware constraints.</p>
     *
     * <p><strong>Inputs/Invariants:</strong> expects valid tenant/group IDs and courseCode.</p>
     * <p><strong>Side effects:</strong> none in this method; downstream may read DB/index.</p>
     * <p><strong>Audit behavior:</strong> none (callers record outcomes as part of their audit lifecycle).</p>
     * <p><strong>Return semantics:</strong> returns found JournalArticle or null when none exists.</p>
     *
     * @param groupId site/group id
     * @param companyId tenant/company id
     * @param courseCode course identifier
     * @param eventCtx workflow context
     * @return existing JournalArticle or null
     */
    @Override
    public JournalArticle findExistingArticle(long groupId, long companyId, String courseCode, CourseEventContext eventCtx) {
        return _journalArticleService.findExistingArticle(groupId, companyId, courseCode, eventCtx);
    }

    /**
     * Rebuilds a course article by re-fetching all retrigger fields from CLS (PUBLISHED retrigger scenario).
     *
     * <p><strong>Business purpose:</strong> repairs or replays a PUBLISHED update when previous processing was incomplete.</p>
     * <p><strong>Technical purpose:</strong> fetches combined (critical + non-critical + batch) field set and then updates
     * existing content or creates a new article if not present.</p>
     *
     * <p><strong>Inputs/Invariants:</strong> requires {@code eventCtx.articleConfig}; expects a resolvable courseCode in context.</p>
     * <p><strong>Side effects:</strong> CLS call; JournalArticle create/update; logs errors.</p>
     * <p><strong>Audit behavior:</strong> this method logs to server log on failures and throws; workflow-level auditing is expected
     * to be handled by the entry-point component that invoked the retrigger.</p>
     * <p><strong>Return semantics:</strong> returns updated/created JournalArticle; throws {@link IllegalStateException} on failures.</p>
     *
     * @param eventCtx workflow context
     * @return updated or created JournalArticle
     */
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

    /**
     * Processes an "OTL Published" event using a provided JSON payload rather than calling CLS.
     *
     * <p><strong>Business purpose:</strong> supports one-time-load ingestion where the authoritative payload is delivered
     * directly to the platform.</p>
     * <p><strong>Technical purpose:</strong> resolves courseCode from context or payload, parses payload into
     * {@link CourseResponse}, then applies full-field processing against an existing article or creates a new one.</p>
     *
     * <p><strong>Inputs/Invariants:</strong> {@code json} should represent {@link CourseResponse}; {@code eventCtx.articleConfig} is required.</p>
     * <p><strong>Side effects:</strong> JournalArticle create/update; logs errors.</p>
     * <p><strong>Audit behavior:</strong> this method logs to server log on failures and throws; workflow-level auditing is expected
     * to be handled by the entry-point component that invoked the OTL operation.</p>
     * <p><strong>Return semantics:</strong> returns updated/created JournalArticle; throws {@link IllegalStateException} on failures.</p>
     *
     * @param json serialized CourseResponse payload
     * @param eventCtx workflow context
     * @return updated or created JournalArticle
     */
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

    /**
     * Internal carrier for critical processing outcome.
     *
     * <p>Updated may be null on skip paths. payloadJson is only populated when capturePayload is enabled and the
     * event type permits safe reuse (PUBLISHED).</p>
     */
    private static final class InternalCriticalResult {
        final JournalArticle updated;
        final String payloadJson;

        private InternalCriticalResult(JournalArticle updated, String payloadJson) {
            this.updated = updated;
            this.payloadJson = (payloadJson == null) ? "" : payloadJson;
        }
    }

    /**
     * Normalizes JSON string to non-null.
     *
     * @param json raw JSON string
     * @return non-null string (empty when null)
     */
    private static String safeJson(String json) {
        return (json == null) ? "" : json;
    }

    /**
     * Serializes a CourseResponse to JSON for payload reuse.
     *
     * <p>Serialization failures intentionally return empty string to keep workflow resilient.</p>
     *
     * @param r CourseResponse
     * @return JSON string or empty when serialization fails
     */
    private static String toJson(CourseResponse r) {
        try {
            if (r == null) {
                return "";
            }
            return _objectMapper.writeValueAsString(r);
        }
        catch (Exception e) {
            return "";
        }
    }

    /**
     * Deserializes JSON into CourseResponse.
     *
     * <p>Parsing failures return null to allow caller to choose fallback behavior.</p>
     *
     * @param json serialized payload
     * @return CourseResponse or null when blank/unparseable
     */
    private static CourseResponse fromJson(String json) {
        try {
            if (json == null || json.trim().isEmpty()) {
                return null;
            }
            return _objectMapper.readValue(json, CourseResponse.class);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Ensures the workflow context contains a CourseArticleConfig.
     *
     * <p><strong>Business purpose:</strong> avoids writing a course page without the agreed structural configuration.</p>
     * <p><strong>Technical purpose:</strong> enforces a precondition used by JournalArticleService processing.</p>
     *
     * <p><strong>Inputs/Invariants:</strong> ctx must be non-null and ctx.getArticleConfig() must be non-null.</p>
     * <p><strong>Side effects:</strong> none.</p>
     * <p><strong>Audit behavior:</strong> none; callers record failures as part of their audit lifecycle.</p>
     * <p><strong>Return semantics:</strong> returns non-null config; throws {@link IllegalStateException} when missing.</p>
     *
     * @param ctx event context
     * @return article config
     * @throws IllegalStateException when articleConfig is missing
     */
    private static CourseArticleConfig requireArticleConfig(CourseEventContext ctx) {
        if (ctx == null || ctx.getArticleConfig() == null) {
            throw new IllegalStateException("CourseEventContext.articleConfig is required but missing");
        }
        return ctx.getArticleConfig();
    }

    /**
     * Resolves courseCode from context first, falling back to payload parsing when missing.
     *
     * <p>Used in OTL flows where the event context may not have courseCode populated.</p>
     *
     * @param ctx event context
     * @param json raw JSON payload
     * @return resolved course code or empty string when unresolved
     */
    private static String resolveCourseCodeFromContextOrJson(CourseEventContext ctx, String json) {
        String cc = safeCourseCode(ctx);
        if (cc != null && !cc.trim().isEmpty()) {
            return cc.trim();
        }

        // Best-effort JSON fallback (no new DTOs, no extra deps).
        try {
            if (json == null || json.trim().isEmpty()) {
                return "";
            }
            CourseResponse cr = _objectMapper.readValue(json, CourseResponse.class);
            if (cr != null && cr.getBody() != null) {
                String fromBody = cr.getBody().getCourseCode();
                return (fromBody == null) ? "" : fromBody.trim();
            }
        }
        catch (Exception ignore) {
            // Non-fatal; unresolved courseCode will be represented as empty and logged by callers.
        }

        return "";
    }

    /**
     * Extracts courseCode from context as a non-null string.
     *
     * @param ctx event context
     * @return courseCode or empty string when missing
     */
    private static String safeCourseCode(CourseEventContext ctx) {
        if (ctx == null) {
            return "";
        }
        String cc = ctx.getCourseCode();
        return (cc == null) ? "" : cc;
    }

    /**
     * Extracts eventType from context (nullable).
     *
     * @param ctx event context
     * @return eventType or null
     */
    private static String eventType(CourseEventContext ctx) {
        return (ctx == null) ? null : ctx.getEventType();
    }

    /**
     * Validates required CLS payload fields for critical processing.
     *
     * <p><strong>Business purpose:</strong> prevents creating/updating course pages with missing identity fields.</p>
     * <p><strong>Technical purpose:</strong> checks minimal required fields on {@link CourseResponse.Body}.</p>
     *
     * @param body CLS payload body
     * @return list of missing field names (empty when valid)
     */
    private static List<String> validateRequiredFields(CourseResponse.Body body) {
        if (body == null) {
            return Collections.singletonList("body");
        }

        List<String> missing = new java.util.ArrayList<String>();
        if (isBlank(body.getCourseCode())) {
            missing.add("courseCode");
        }
        if (isBlank(body.getCourseName())) {
            missing.add("courseName");
        }

        return missing;
    }

    /**
     * @param s input string
     * @return true when null/blank
     */
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Computes field intersection for CHANGED events based on changeFromTypes allowlists.
     *
     * <p><strong>Business purpose:</strong> limits updates to only the fields that are relevant to the type(s) of change,
     * reducing unintended overwrites.</p>
     * <p><strong>Technical purpose:</strong> reads {@code eventCtx.changeFromTypes} and builds a union allowlist from
     * {@code CLS_FIELD_CHANGE_FROM_*} keys, then returns {@code baseFields ∩ union} preserving base order.</p>
     *
     * <p><strong>Inputs/Invariants:</strong> if changeFromTypes is empty, baseFields are returned unchanged.
     * If no allowlist keys resolve, returns empty array to represent "no fields eligible".</p>
     * <p><strong>Side effects:</strong> none.</p>
     * <p><strong>Audit behavior:</strong> none; callers include the resulting field counts in their audit details.</p>
     * <p><strong>Return semantics:</strong> never returns null; may return empty array when intersection yields no eligible fields.</p>
     *
     * @param eventCtx event context containing changeFromTypes
     * @param baseFields starting field list
     * @param paramValues parameter map containing change-from allowlists
     * @return intersected field list preserving base order
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
                // Missing enum key is non-fatal and simply means that changeFromType has no configured allowlist.
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

    /**
     * Normalizes parameter map values into a trimmed, non-empty String[].
     *
     * <p>Supports values stored as String[], Collection, or comma-separated String.</p>
     *
     * @param paramValues cached parameter map
     * @param key parameter key
     * @return normalized array (never null)
     */
    private static String[] getParamArray(Map<ParameterKeyEnum, Object> paramValues, ParameterKeyEnum key) {
        if (paramValues == null || key == null) {
            return new String[0];
        }

        Object value = paramValues.get(key);
        if (value == null) {
            return new String[0];
        }

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

    /**
     * Persists an audit event for this component.
     *
     * <p><strong>Business purpose:</strong> provides an authoritative timeline of what happened (and why) for each
     * course operation, enabling support and alerting to be driven from stored audit data.</p>
     * <p><strong>Technical purpose:</strong> builds {@link AuditEvent} and writes via {@link AuditEventWriter}. Any failure
     * to write audit data is swallowed to ensure audit logging never breaks the runtime flow.</p>
     *
     * <p><strong>Inputs/Invariants:</strong> {@code ids} must be present (safe defaults exist for null context);
     * correlationId/requestId should be stable for the current operation. STARTED events should use endTimeMs=0 by contract;
     * callers control these values when invoking this helper.</p>
     * <p><strong>Side effects:</strong> writes to the audit persistence layer (DB-backed); does not throw on failures.</p>
     * <p><strong>Audit behavior:</strong> this method is the only place this class writes audit events; it does not send email.
     * Errors are captured using errorCode/errorMessage/exceptionClass, without including secrets or PII.</p>
     * <p><strong>Return semantics:</strong> void; never throws (exceptions are suppressed).</p>
     *
     * @param startMs event start time in ms
     * @param endMs event end time in ms (0 for STARTED by audit timing semantics)
     * @param corrId correlation id used to stitch workflow events together
     * @param requestId per-request id for higher cardinality tracking
     * @param ids tenant/user/course identifiers
     * @param severity audit severity
     * @param status audit status
     * @param step audit step (layer boundary)
     * @param category audit category
     * @param message human-readable message (no secrets/PII)
     * @param errorCode error code enum
     * @param errorMessage error message (sanitized)
     * @param exceptionClass exception class name (nullable)
     * @param details structured key/value context (sanitized)
     */
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
            // Audit logging must never break runtime flows. Any alerting is driven from successful audit persistence elsewhere.
        }
    }

    /**
     * Builds a details map from key/value varargs.
     *
     * @param kv alternating key/value pairs
     * @return details map (values are normalized to non-null)
     */
    private static Map<String, String> details(String... kv) {
        Map<String, String> m = new HashMap<String, String>();
        for (int i = 0; i + 1 < kv.length; i += 2) {
            m.put(kv[i], kv[i + 1] == null ? "" : kv[i + 1]);
        }
        return m;
    }

    /**
     * Returns a safe message string for audit/logging without leaking sensitive content.
     *
     * @param t throwable
     * @return message or throwable simple class name when message is blank
     */
    private static String safeMsg(Throwable t) {
        if (t == null) {
            return "Unknown error";
        }
        String m = t.getMessage();
        return (m == null || m.trim().isEmpty()) ? t.getClass().getSimpleName() : m;
    }

    /**
     * Normalizes potentially-null strings for audit/event fields.
     *
     * @param s input string
     * @return empty string when null
     */
    private static String safe(String s) {
        return (s == null) ? "" : s;
    }

    /**
     * Resolves correlation id from MDC where available, otherwise generates a new id.
     *
     * <p>Correlation id is used to stitch together audit events across multiple components.</p>
     *
     * @return correlation id (non-null)
     */
    private static String corrId() {
        String c = MdcUtil.getCorrId();
        return (c != null) ? c : UUID.randomUUID().toString();
    }

    /**
     * Immutable identifier bundle derived from {@link CourseEventContext}.
     *
     * <p>Used to ensure audit events always have consistent tenant/user/course identifiers even when context is partially missing.</p>
     */
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

        /**
         * Derives identifiers from the provided context, using safe defaults when ctx is null.
         *
         * @param ctx workflow context
         * @return Ids container with safe values
         */
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
