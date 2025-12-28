package com.ntuc.notification.service.internal.cls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.constants.NotificationType;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.dto.cls.ClsAccessTokenResponse;
import com.ntuc.notification.model.CourseListResponse;
import com.ntuc.notification.model.CourseResponse;
import com.ntuc.notification.model.CourseResponseMapper;
import com.ntuc.notification.model.CourseResponseWrapper;
import com.ntuc.notification.model.ScheduleResponse;
import com.ntuc.notification.service.internal.http.HttpExecutor;
import com.ntuc.notification.service.internal.http.HttpResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * CLS integration client that performs HTTP calls and emits audit events for every meaningful outcome.
 *
 * <p><b>Business purpose:</b> Fetch course details and schedules from CLS so NTUC course content can remain accurate and
 * timely.</p>
 *
 * <p><b>Technical purpose:</b> Execute CLS OAuth and CLS fetch requests using {@link HttpExecutor}, map responses using
 * Jackson / project mappers, and persist outcomes through {@link AuditEventWriter} without direct email sending.</p>
 *
 * <p><b>Key rules:</b>
 * <ul>
 *   <li>No direct emails are sent from this class; it writes audit events only. Centralized email triggering is handled
 *       elsewhere based on persisted audit outcomes.</li>
 *   <li>Integrity: CLS response courseCode must match the event courseCode (normalized compare). If mismatch, audit
 *       FAILED and return {@code null}.</li>
 *   <li>Audit timing: for {@link AuditStatus#STARTED}, endTimeMs is forced to {@code 0}. For ended events, endTimeMs is
 *       clamped to be {@code >= startTimeMs}.</li>
 *   <li>Audit steps are intentionally coarse-grained for CLS: {@link AuditStep#CLS_AUTH} and {@link AuditStep#CLS_FETCH}.
 *       Fine-grained phases are captured under detailsJson (e.g., {@code phase=DUMMY_COURSES}).</li>
 * </ul>
 * </p>
 *
 * @author @akshaygawande
 */
public class ClsConnectionClient {

    private static final String PHASE_COURSE_DETAILS = "COURSE_DETAILS";
    private static final String PHASE_SCHEDULES = "SCHEDULES";
    private static final String PHASE_DUMMY_COURSES = "DUMMY_COURSES";
    private static final String PHASE_DUMMY_SUBSCRIPTIONS = "DUMMY_SUBSCRIPTIONS";

    private final Map<ParameterKeyEnum, Object> _params;
    private final HttpExecutor _http;
    private final ObjectMapper _mapper;
    private final AuditEventWriter _auditEventWriter;

    /**
     * Cached OAuth token for CLS.
     *
     * <p>Volatile ensures cross-thread visibility when this client is used concurrently.</p>
     */
    private volatile String _cachedAccessToken;

    /**
     * Epoch millis when the cached token should be considered expired (includes safety skew).
     */
    private volatile long _cachedAccessTokenExpiresAtMs;

    /**
     * Creates a CLS connection client.
     *
     * <p><b>Business purpose:</b> Provide a reusable CLS client instance configured by environment parameters.</p>
     * <p><b>Technical purpose:</b> Store injected dependencies (params, HTTP executor, Jackson mapper, audit writer) and
     * initialize safe defaults for optional params.</p>
     *
     * @param params Environment/runtime parameters (timeouts, endpoints, retry counts, company/group IDs).
     * @param http HTTP executor abstraction used to perform CLS calls.
     * @param mapper Jackson mapper used to deserialize CLS responses.
     * @param auditEventWriter Writer that persists audit events; failures here must not break runtime flow.
     */
    public ClsConnectionClient(
        Map<ParameterKeyEnum, Object> params,
        HttpExecutor http,
        ObjectMapper mapper,
        AuditEventWriter auditEventWriter) {

        _params = (params != null) ? params : new HashMap<ParameterKeyEnum, Object>();
        _http = http;
        _mapper = mapper;
        _auditEventWriter = auditEventWriter;
    }

    /**
     * Fetches course details from CLS for the given workflow context.
     *
     * <p><b>Business purpose:</b> Retrieve authoritative course attributes from CLS so downstream processing can update
     * NTUC course content accurately.</p>
     *
     * <p><b>Technical purpose:</b> Performs OAuth (cached) and a CLS fetch request with retries, validates the CLS payload,
     * maps the first course entry, and enforces a strict courseCode integrity check against the workflow context.</p>
     *
     * <p><b>Inputs/Invariants:</b>
     * <ul>
     *   <li>{@code eventCtx} provides companyId/groupId/userId/courseCode/ntucDTId and event type metadata.</li>
     *   <li>Returned properties are normalized based on event type: PUBLISHED returns empty, CHANGED returns changeFrom.</li>
     * </ul>
     * </p>
     *
     * <p><b>Side effects:</b>
     * <ul>
     *   <li>External call: CLS OAuth token acquisition (when cache miss/expired).</li>
     *   <li>External call: CLS course details fetch with request payload.</li>
     *   <li>Audit writes: emits STARTED/SUCCESS/FAILED events for CLS_AUTH and CLS_FETCH.</li>
     * </ul>
     * </p>
     *
     * <p><b>Audit behavior:</b>
     * <ul>
     *   <li>CLS_FETCH STARTED recorded once at the beginning of the operation.</li>
     *   <li>Each attempt emits CLS_FETCH FAILED on HTTP error or exception.</li>
     *   <li>CLS_FETCH SUCCESS recorded on a valid mapped course with courseCode integrity match.</li>
     *   <li>CLS_AUTH audit events are emitted by token acquisition logic and are correlated to the same corrId/requestId.</li>
     * </ul>
     * </p>
     *
     * <p><b>Return semantics:</b>
     * <ul>
     *   <li>Returns mapped {@link CourseResponse} on success.</li>
     *   <li>Returns {@code null} when CLS returns no course, invalid payload, integrity mismatch, or after retries fail.</li>
     *   <li>Throws {@link IOException} for certain token/URL encoding failures that are surfaced by the auth path.</li>
     * </ul>
     * </p>
     *
     * @param eventCtx Workflow event context (identity + courseCode + event metadata).
     * @param returnedProperties Optional list of CLS fields to request; will be normalized based on event type.
     * @return Mapped {@link CourseResponse} or {@code null} on failure/empty payload.
     * @throws IOException If OAuth/token acquisition or encoding triggers an IO-level failure.
     */
    public CourseResponse getCourseDetails(CourseEventContext eventCtx, String[] returnedProperties) throws IOException {
        final long startMs = System.currentTimeMillis();
        final String corrId = corrId();
        final String requestId = UUID.randomUUID().toString();

        final Ids ids = Ids.from(eventCtx);
        final String courseCode = safe(ids.courseCode);

        returnedProperties = normalizeReturnedProps(eventCtx, returnedProperties);

        int connectTimeout = parseIntSafe((String) _params.get(ParameterKeyEnum.CLS_HTTP_CONNECT_TIMEOUT_MS), 0);
        int readTimeout = parseIntSafe((String) _params.get(ParameterKeyEnum.CLS_HTTP_READ_TIMEOUT_MS), 0);

        String baseUrl = (String) _params.get(ParameterKeyEnum.CLS_AUTH_BASE_URL);
        String endpoint = (String) _params.get(ParameterKeyEnum.CLS_COURSE_DETAILS_ENDPOINT);
        String method = (String) _params.get(ParameterKeyEnum.CLS_COURSE_DETAILS_METHOD);
        int retryCount = parseIntSafe((String) _params.get(ParameterKeyEnum.CLS_COURSE_RETRY_COUNT), 0);

        // Using CLS_FETCH as this is the CLS data retrieval boundary.
        // Specifics about the call type are captured in detailsJson phase=COURSE_DETAILS.
        audit(
            startMs, System.currentTimeMillis(), corrId, requestId, ids,
            AuditSeverity.INFO, AuditStatus.STARTED, AuditStep.CLS_FETCH,
            AuditCategory.CLS, "CLS course fetch start",
            AuditErrorCode.NONE, null, null,
            details(
                "phase", PHASE_COURSE_DETAILS,
                "endpoint", safe(endpoint),
                "method", safe(method),
                "retries", String.valueOf(Math.max(retryCount, 1)),
                "returnedPropsCount", String.valueOf(returnedProperties == null ? 0 : returnedProperties.length),
                "returnedProps", returnedProperties == null ? "" : String.join(",", returnedProperties)
            )
        );

        int lastStatus = -1;
        String lastBody = null;

        for (int attempt = 1; attempt <= Math.max(retryCount, 1); attempt++) {
            long startNs = System.nanoTime();

            try {
                String token = fetchAccessTokenInternal(ids, corrId, requestId);
                if (token == null) {
                    // Using CLS_AUTH as this failure is about acquiring a valid OAuth token.
                    // Attempt number is captured in detailsJson.
                    audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CLS_AUTH,
                        AuditCategory.CLS, "CLS course fetch: token null",
                        AuditErrorCode.CLS_OAUTH_FAILED, "token null",
                        null,
                        details("phase", PHASE_COURSE_DETAILS, "attempt", String.valueOf(attempt))
                    );
                    continue;
                }

                String payload = buildCoursePayload(courseCode, safeProps(returnedProperties));
                byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8);

                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");

                HttpResponse resp = _http.execute(
                    method,
                    safe(baseUrl) + safe(endpoint),
                    headers,
                    payloadBytes,
                    connectTimeout,
                    readTimeout
                );

                long elapsedMs = elapsedMs(startNs);

                lastStatus = resp.getStatusCode();
                lastBody = resp.getBody();

                if (resp.getStatusCode() == 200 && resp.getBody() != null) {
                    CourseListResponse list = _mapper.readValue(resp.getBody(), CourseListResponse.class);

                    if (list != null && list.getError() != null) {
                        // Using CLS_FETCH: CLS responded successfully at HTTP level, but the payload contains an error object.
                        audit(
                            startMs, System.currentTimeMillis(), corrId, requestId, ids,
                            AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CLS_FETCH,
                            AuditCategory.CLS, "CLS returned error object",
                            AuditErrorCode.CLS_INVALID_RESPONSE,
                            safe("CLS error=" + list.getError().getCode()),
                            null,
                            details(
                                "phase", PHASE_COURSE_DETAILS,
                                "attempt", String.valueOf(attempt),
                                "statusCode", String.valueOf(resp.getStatusCode()),
                                "elapsedMs", String.valueOf(elapsedMs)
                            )
                        );
                        continue;
                    }

                    CourseResponse mapped = mapFirstCourse(list);

                    // Strategy A alias: "NOT FOUND" -> treat as empty payload / no course returned
                    if (mapped == null || mapped.getBody() == null) {
                        audit(
                            startMs, System.currentTimeMillis(), corrId, requestId, ids,
                            AuditSeverity.WARNING, AuditStatus.FAILED, AuditStep.CLS_FETCH,
                            AuditCategory.CLS, "CLS returned no course for courseCode",
                            AuditErrorCode.CLS_EMPTY_BODY,
                            "No course returned from CLS for courseCode=" + courseCode,
                            null,
                            details(
                                "phase", PHASE_COURSE_DETAILS,
                                "attempt", String.valueOf(attempt),
                                "statusCode", String.valueOf(lastStatus)
                            )
                        );
                        return null;
                    }

                    // ----------------- CRITICAL INTEGRITY CHECK -----------------
                    String actualCourseCode = extractCourseCodeFromCourseResponse(mapped);

                    if (!CourseCodeMatcher.matches(courseCode, actualCourseCode)) {
                        audit(
                            startMs, System.currentTimeMillis(), corrId, requestId, ids,
                            AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CLS_FETCH,
                            AuditCategory.CLS, "CLS returned courseCode mismatch",
                            AuditErrorCode.CLS_INVALID_RESPONSE,
                            safe("Expected courseCode=" + courseCode + " but got courseCode=" + safe(actualCourseCode)),
                            null,
                            details(
                                "phase", PHASE_COURSE_DETAILS,
                                "attempt", String.valueOf(attempt),
                                "statusCode", String.valueOf(resp.getStatusCode()),
                                "elapsedMs", String.valueOf(elapsedMs),
                                "expectedCourseCode", courseCode,
                                "actualCourseCode", safe(actualCourseCode)
                            )
                        );
                        return null;
                    }
                    // ------------------------------------------------------------

                    audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.INFO, AuditStatus.SUCCESS, AuditStep.CLS_FETCH,
                        AuditCategory.CLS, "CLS course fetch success",
                        AuditErrorCode.NONE, null, null,
                        details(
                            "phase", PHASE_COURSE_DETAILS,
                            "attempts", String.valueOf(attempt),
                            "statusCode", String.valueOf(resp.getStatusCode()),
                            "elapsedMs", String.valueOf(elapsedMs)
                        )
                    );

                    return mapped;
                }

                audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CLS_FETCH,
                    AuditCategory.CLS, "CLS course fetch attempt failed",
                    classifyHttpError(resp.getStatusCode()),
                    safe("HTTP " + resp.getStatusCode()),
                    null,
                    details(
                        "phase", PHASE_COURSE_DETAILS,
                        "attempt", String.valueOf(attempt),
                        "statusCode", String.valueOf(resp.getStatusCode()),
                        "elapsedMs", String.valueOf(elapsedMs)
                    )
                );
            }
            catch (Exception e) {
                audit(
                    startMs, System.currentTimeMillis(), corrId, requestId, ids,
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CLS_FETCH,
                    AuditCategory.CLS, "CLS course fetch attempt exception",
                    AuditErrorCode.CLS_CONNECTION_FAILED,
                    safeMsg(e),
                    (e != null ? e.getClass().getName() : null),
                    details("phase", PHASE_COURSE_DETAILS, "attempt", String.valueOf(attempt))
                );
            }
        }

        // Final failure after retries (no email here; trigger decides based on persisted audit).
        audit(
            startMs, System.currentTimeMillis(), corrId, requestId, ids,
            AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CLS_FETCH,
            AuditCategory.CLS, "CLS course fetch failed after retries",
            AuditErrorCode.CLS_CONNECTION_FAILED,
            safe("all retries failed; lastStatus=" + lastStatus),
            null,
            details(
                "phase", PHASE_COURSE_DETAILS,
                "lastStatus", String.valueOf(lastStatus),
                "lastBodyPresent", String.valueOf(lastBody != null)
            )
        );

        return null;
    }

    /**
     * Fetches the latest schedules for a course from CLS.
     *
     * <p><b>Business purpose:</b> Keep schedule listings current so learners and admins see accurate session availability.</p>
     *
     * <p><b>Technical purpose:</b> Executes a CLS schedules GET call (with OAuth token) and maps the JSON response to
     * {@link ScheduleResponse}.</p>
     *
     * <p><b>Inputs/Invariants:</b> {@code courseCode} must be non-blank to perform a CLS call.</p>
     *
     * <p><b>Side effects:</b>
     * <ul>
     *   <li>External call: CLS OAuth token acquisition (when cache miss/expired).</li>
     *   <li>External call: CLS schedules fetch.</li>
     *   <li>Audit writes: emits SUCCESS/FAILED events for CLS_FETCH.</li>
     * </ul>
     * </p>
     *
     * <p><b>Audit behavior:</b> Emits CLS_FETCH SUCCESS on 200 with a body. Emits CLS_FETCH FAILED for 404 and for
     * retry exhaustion (connection failures and other non-200 statuses are captured per attempt).</p>
     *
     * <p><b>Return semantics:</b>
     * <ul>
     *   <li>Returns mapped {@link ScheduleResponse} on HTTP 200 with body.</li>
     *   <li>Returns {@code null} on blank input, 404, or retry exhaustion.</li>
     * </ul>
     * </p>
     *
     * @param courseCode CLS course code.
     * @return {@link ScheduleResponse} or {@code null}.
     * @throws IOException If encoding/token acquisition causes an IO-level failure.
     */
    public ScheduleResponse getLatestCourseSchedules(String courseCode) throws IOException {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            return null;
        }

        String corrId = corrId();
        String requestId = UUID.randomUUID().toString();
        long startMs = System.currentTimeMillis();

        Ids ids = Ids.fromParamsOnly(_params).withCourse(courseCode);

        int connectTimeout = parseIntSafe((String) _params.get(ParameterKeyEnum.CLS_HTTP_CONNECT_TIMEOUT_MS), 0);
        int readTimeout = parseIntSafe((String) _params.get(ParameterKeyEnum.CLS_HTTP_READ_TIMEOUT_MS), 0);

        String baseUrl = (String) _params.get(ParameterKeyEnum.CLS_AUTH_BASE_URL);
        String endpointTpl = (String) _params.get(ParameterKeyEnum.CLS_COURSE_SCHEDULES_ENDPOINT);
        String method = (String) _params.get(ParameterKeyEnum.CLS_COURSE_SCHEDULES_METHOD);
        int retryCount = parseIntSafe((String) _params.get(ParameterKeyEnum.CLS_COURSE_RETRY_COUNT), 0);

        for (int attempt = 1; attempt <= Math.max(retryCount, 1); attempt++) {
            try {
                String token = fetchAccessTokenInternal(ids, corrId, requestId);
                if (token == null) {
                    continue;
                }

                String endpoint = safe(endpointTpl).replace(
                    "{courseCode}", URLEncoder.encode(courseCode, StandardCharsets.UTF_8.toString()));

                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");

                HttpResponse resp = _http.execute(method, safe(baseUrl) + endpoint, headers, null, connectTimeout, readTimeout);

                if (resp.getStatusCode() == 200 && resp.getBody() != null) {
                    audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.INFO, AuditStatus.SUCCESS, AuditStep.CLS_FETCH,
                        AuditCategory.CLS, "CLS schedule fetch success",
                        AuditErrorCode.NONE, null, null,
                        details("phase", PHASE_SCHEDULES, "attempt", String.valueOf(attempt), "statusCode", "200")
                    );
                    return _mapper.readValue(resp.getBody(), ScheduleResponse.class);
                }

                if (resp.getStatusCode() == 404) {
                    audit(
                        startMs, System.currentTimeMillis(), corrId, requestId, ids,
                        AuditSeverity.WARNING, AuditStatus.FAILED, AuditStep.CLS_FETCH,
                        AuditCategory.CLS, "CLS schedule fetch returned 404",
                        AuditErrorCode.CLS_HTTP_4XX, "HTTP 404", null,
                        details("phase", PHASE_SCHEDULES, "attempt", String.valueOf(attempt), "statusCode", "404")
                    );
                    return null;
                }
            }
            catch (Exception ignore) {
                // Per-attempt failure is tolerated; final failure is audited after retries.
            }
        }

        audit(
            startMs, System.currentTimeMillis(), corrId, requestId, ids,
            AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CLS_FETCH,
            AuditCategory.CLS, "CLS schedule fetch failed after retries",
            AuditErrorCode.CLS_CONNECTION_FAILED, "all retries failed", null,
            details("phase", PHASE_SCHEDULES, "courseCode", courseCode)
        );

        return null;
    }

    // =====================================================================
    // Dummy endpoints (raw JSON pass-through)
    // =====================================================================

    /**
     * Fetches raw JSON from the CLS dummy courses endpoint.
     *
     * <p><b>Business purpose:</b> Support diagnostics and controlled validation of CLS connectivity and behavior.</p>
     * <p><b>Technical purpose:</b> Pass-through CLS dummy endpoint response without DTO mapping.</p>
     *
     * @param courseCode CLS course code.
     * @return Raw JSON response body or {@code null}.
     * @throws IOException If URL encoding/token acquisition causes an IO-level failure.
     */
    public String getCoursesDummyRawJson(String courseCode) throws IOException {
        return getDummyRawJson(
            courseCode,
            (String) _params.get(ParameterKeyEnum.CLS_DUMMY_COURSES_ENDPOINT),
            PHASE_DUMMY_COURSES
        );
    }

    /**
     * Fetches raw JSON from the CLS dummy subscriptions endpoint.
     *
     * <p><b>Business purpose:</b> Support diagnostics and controlled validation of CLS subscription-related payloads.</p>
     * <p><b>Technical purpose:</b> Pass-through CLS dummy endpoint response without DTO mapping.</p>
     *
     * @param courseCode CLS course code.
     * @return Raw JSON response body or {@code null}.
     * @throws IOException If URL encoding/token acquisition causes an IO-level failure.
     */
    public String getSubscriptionsDummyRawJson(String courseCode) throws IOException {
        return getDummyRawJson(
            courseCode,
            (String) _params.get(ParameterKeyEnum.CLS_DUMMY_SUBSCRIPTIONS_ENDPOINT),
            PHASE_DUMMY_SUBSCRIPTIONS
        );
    }

    /**
     * Raw JSON pass-through for CLS dummy endpoints.
     *
     * <p><b>Business purpose:</b> Enable operational troubleshooting without altering payload semantics.</p>
     * <p><b>Technical purpose:</b> Executes a CLS call and returns the response body as-is, emitting audit events under
     * {@link AuditStep#CLS_FETCH} while the endpoint kind is recorded as {@code details.phase}.</p>
     *
     * <p><b>Inputs/Invariants:</b> courseCode must be non-blank and endpoint template must be provided.</p>
     *
     * <p><b>Side effects:</b> External CLS OAuth (if needed), external CLS dummy HTTP call, audit writes.</p>
     *
     * <p><b>Return semantics:</b> Returns raw response body on HTTP 200; otherwise {@code null}.</p>
     */
    private String getDummyRawJson(String courseCode, String endpointTpl, String phase) throws IOException {
        if (courseCode == null || courseCode.trim().isEmpty() || endpointTpl == null) {
            return null;
        }

        Ids ids = Ids.fromParamsOnly(_params).withCourse(courseCode);
        String corrId = corrId();
        String requestId = UUID.randomUUID().toString();
        long startMs = System.currentTimeMillis();

        String baseUrl = (String) _params.get(ParameterKeyEnum.CLS_AUTH_BASE_URL);
        String method = (String) _params.get(ParameterKeyEnum.CLS_DUMMY_METHOD);

        int connectTimeout = parseIntSafe((String) _params.get(ParameterKeyEnum.CLS_HTTP_CONNECT_TIMEOUT_MS), 0);
        int readTimeout = parseIntSafe((String) _params.get(ParameterKeyEnum.CLS_HTTP_READ_TIMEOUT_MS), 0);

        String token = fetchAccessTokenInternal(ids, corrId, requestId);
        if (token == null) {
            // Using CLS_AUTH as the failure is strictly about OAuth token acquisition, not the dummy endpoint call itself.
            audit(
                startMs, System.currentTimeMillis(), corrId, requestId, ids,
                AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CLS_AUTH,
                AuditCategory.CLS, "CLS dummy fetch: token null",
                AuditErrorCode.CLS_OAUTH_FAILED, "token null", null,
                details("phase", phase)
            );
            return null;
        }

        String endpoint = endpointTpl.replace(
            "{courseCode}",
            URLEncoder.encode(courseCode, StandardCharsets.UTF_8.toString())
        );

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        HttpResponse resp = _http.execute(
            method,
            safe(baseUrl) + endpoint,
            headers,
            null,
            connectTimeout,
            readTimeout
        );

        if (resp.getStatusCode() == 200 && resp.getBody() != null) {
            audit(
                startMs, System.currentTimeMillis(), corrId, requestId, ids,
                AuditSeverity.INFO, AuditStatus.SUCCESS, AuditStep.CLS_FETCH,
                AuditCategory.CLS, "CLS dummy fetch success",
                AuditErrorCode.NONE, null, null,
                details("phase", phase, "endpoint", endpointTpl, "statusCode", String.valueOf(resp.getStatusCode()))
            );
            return resp.getBody();
        }

        audit(
            startMs, System.currentTimeMillis(), corrId, requestId, ids,
            AuditSeverity.WARNING, AuditStatus.FAILED, AuditStep.CLS_FETCH,
            AuditCategory.CLS, "CLS dummy fetch failed",
            classifyHttpError(resp.getStatusCode()),
            safe("HTTP " + resp.getStatusCode()),
            null,
            details("phase", phase, "endpoint", endpointTpl, "statusCode", String.valueOf(resp.getStatusCode()))
        );

        return null;
    }

    // ---------------- Token / auth ----------------

    /**
     * Obtains a CLS OAuth access token, using an in-memory cache with safety skew.
     *
     * <p><b>Business purpose:</b> Ensure CLS calls are authenticated reliably while minimizing unnecessary token requests.</p>
     *
     * <p><b>Technical purpose:</b> Checks cache first; on miss/expiry, calls CLS OAuth endpoint and caches the token until
     * (expiresIn - safetySkew).</p>
     *
     * <p><b>Inputs/Invariants:</b> Requires CLS auth params (base URL, endpoint, clientId, clientSecret).</p>
     *
     * <p><b>Side effects:</b>
     * <ul>
     *   <li>External call: CLS OAuth endpoint when cache miss/expired.</li>
     *   <li>State mutation: updates {@code _cachedAccessToken} and {@code _cachedAccessTokenExpiresAtMs}.</li>
     *   <li>Audit writes: CLS_AUTH STARTED/PARTIAL/SUCCESS/FAILED with detailed phases.</li>
     * </ul>
     * </p>
     *
     * <p><b>Return semantics:</b> Returns token string or {@code null} when configuration is missing or auth fails.</p>
     *
     * @param ids Correlation identity for audit fields.
     * @param corrId Correlation ID (shared across CLS_AUTH and CLS_FETCH events within a request).
     * @param requestId Request ID (shared across CLS_AUTH and CLS_FETCH events within a request).
     * @return OAuth token or {@code null}.
     * @throws IOException If the underlying HTTP execute or JSON parsing fails with an IO-level error.
     */
    private String fetchAccessTokenInternal(Ids ids, String corrId, String requestId) throws IOException {
        final long startMs = System.currentTimeMillis();
        final long startNs = System.nanoTime();

        long now = startMs;
        if (_cachedAccessToken != null && now < _cachedAccessTokenExpiresAtMs) {
            long ttlMs = _cachedAccessTokenExpiresAtMs - now;

            audit(
                startMs, System.currentTimeMillis(), corrId, requestId, ids,
                AuditSeverity.INFO, AuditStatus.SUCCESS, AuditStep.CLS_AUTH,
                AuditCategory.CLS, "CLS auth token cache hit",
                AuditErrorCode.NONE, null, null,
                details(
                    "phase", "CACHE_CHECK",
                    "cacheHit", "true",
                    "nowMs", String.valueOf(now),
                    "expiresAtMs", String.valueOf(_cachedAccessTokenExpiresAtMs),
                    "ttlMs", String.valueOf(ttlMs),
                    "tokenPresent", "true"
                )
            );

            return _cachedAccessToken;
        }

        audit(
            startMs, System.currentTimeMillis(), corrId, requestId, ids,
            AuditSeverity.INFO, AuditStatus.STARTED, AuditStep.CLS_AUTH,
            AuditCategory.CLS, "CLS auth token cache expired. CLS auth token refresh start",
            AuditErrorCode.NONE, null, null,
            details(
                "phase", "CACHE_CHECK",
                "cacheHit", "false",
                "nowMs", String.valueOf(now),
                "expiresAtMs", String.valueOf(_cachedAccessTokenExpiresAtMs),
                "ttlMs", String.valueOf(_cachedAccessTokenExpiresAtMs - now),
                "reason", (_cachedAccessToken == null ? "CACHE_MISS" : "CACHE_EXPIRED")
            )
        );

        String baseUrl = (String) _params.get(ParameterKeyEnum.CLS_AUTH_BASE_URL);
        String endpoint = (String) _params.get(ParameterKeyEnum.CLS_AUTH_ENDPOINT);
        String method = (String) _params.get(ParameterKeyEnum.CLS_AUTH_METHOD);

        String rawClientId = (String) _params.get(ParameterKeyEnum.CLS_AUTH_CLIENT_ID);
        String rawClientSecret = (String) _params.get(ParameterKeyEnum.CLS_AUTH_CLIENT_SECRET);

        String clientId = decodeBase64OrPlain(rawClientId);
        String clientSecret = decodeBase64OrPlain(rawClientSecret);

        boolean clientIdWasDecoded = (rawClientId != null) && !rawClientId.equals(clientId);
        boolean clientSecretWasDecoded = (rawClientSecret != null) && !rawClientSecret.equals(clientSecret);

        String urlScheme = "";
        String urlHost = "";
        String urlPath = "";
        String urlHasQuery = "false";
        try {
            URI u = new URI(safe(baseUrl) + safe(endpoint));
            urlScheme = safe(u.getScheme());
            urlHost = safe(u.getHost());
            urlPath = safe(u.getPath());
            urlHasQuery = String.valueOf(u.getQuery() != null && !u.getQuery().isEmpty());
        }
        catch (Exception ignore) {
            // URL inspection is best-effort for audit diagnostics; failures do not block auth.
        }

        audit(
            startMs, System.currentTimeMillis(), corrId, requestId, ids,
            AuditSeverity.INFO, AuditStatus.STARTED, AuditStep.CLS_AUTH,
            AuditCategory.CLS, "CLS auth config evaluated",
            AuditErrorCode.NONE, null, null,
            details(
                "phase", "CONFIG_VALIDATE",
                "method", safe(method),
                "baseUrlHost", urlHost,
                "endpointPath", safe(endpoint),
                "urlScheme", urlScheme,
                "urlPath", urlPath,
                "urlHasQuery", urlHasQuery,
                "hasBaseUrl", String.valueOf(baseUrl != null),
                "hasEndpoint", String.valueOf(endpoint != null),
                "hasClientId", String.valueOf(clientId != null),
                "hasSecret", String.valueOf(clientSecret != null),
                "clientIdLen", String.valueOf(clientId == null ? 0 : clientId.length()),
                "clientSecretLen", String.valueOf(clientSecret == null ? 0 : clientSecret.length()),
                "clientIdWasBase64Decoded", String.valueOf(clientIdWasDecoded),
                "clientSecretWasBase64Decoded", String.valueOf(clientSecretWasDecoded)
            )
        );

        if (baseUrl == null || endpoint == null || clientId == null || clientSecret == null) {
            audit(
                System.currentTimeMillis(), System.currentTimeMillis(), corrId, requestId, ids,
                AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CLS_AUTH,
                AuditCategory.CLS, "CLS auth config missing",
                AuditErrorCode.CLS_OAUTH_FAILED, "config missing", null,
                details(
                    "phase", "CONFIG_VALIDATE",
                    "method", safe(method),
                    "baseUrlHost", urlHost,
                    "endpointPath", safe(endpoint),
                    "hasBaseUrl", String.valueOf(baseUrl != null),
                    "hasEndpoint", String.valueOf(endpoint != null),
                    "hasClientId", String.valueOf(clientId != null),
                    "hasSecret", String.valueOf(clientSecret != null),
                    "clientIdLen", String.valueOf(clientId == null ? 0 : clientId.length()),
                    "clientSecretLen", String.valueOf(clientSecret == null ? 0 : clientSecret.length())
                )
            );
            return null;
        }

        String params = "client_id=" + urlEncode(clientId)
            + "&client_secret=" + urlEncode(clientSecret)
            + "&grant_type=client_credentials&scope=pweb";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept", "application/json");

        audit(
            startMs, System.currentTimeMillis(), corrId, requestId, ids,
            AuditSeverity.INFO, AuditStatus.STARTED, AuditStep.CLS_AUTH,
            AuditCategory.CLS, "CLS auth request prepared",
            AuditErrorCode.NONE, null, null,
            details(
                "phase", "HTTP_EXECUTE",
                "method", safe(method),
                "urlHost", urlHost,
                "urlPath", urlPath,
                "contentType", "application/x-www-form-urlencoded",
                "accept", "application/json",
                "grantType", "client_credentials",
                "scope", "pweb",
                "bodyKeys", "client_id,client_secret,grant_type,scope",
                "bodyLenBytes", String.valueOf(params.getBytes(StandardCharsets.UTF_8).length)
            )
        );

        HttpResponse resp;
        try {
            resp = _http.execute(
                method,
                safe(baseUrl) + safe(endpoint),
                headers,
                params.getBytes(StandardCharsets.UTF_8),
                0,
                0
            );
        }
        catch (Exception e) {
            long endMs = System.currentTimeMillis();
            audit(
                startMs, endMs, corrId, requestId, ids,
                AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CLS_AUTH,
                AuditCategory.CLS, "CLS auth HTTP execute exception",
                AuditErrorCode.CLS_CONNECTION_FAILED,
                safeMsg(e),
                (e != null ? e.getClass().getName() : null),
                details(
                    "phase", "HTTP_EXECUTE",
                    "durationMs", String.valueOf(elapsedMs(startNs)),
                    "method", safe(method),
                    "urlHost", urlHost,
                    "urlPath", urlPath
                )
            );

            if (e instanceof IOException) {
                throw (IOException)e;
            }
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            }
            throw new IOException(e);
        }

        long durationMs = elapsedMs(startNs);
        int status = resp.getStatusCode();
        String body = resp.getBody();
        int bodyLenBytes = (body == null) ? 0 : body.getBytes(StandardCharsets.UTF_8).length;

        audit(
            startMs, System.currentTimeMillis(), corrId, requestId, ids,
            AuditSeverity.INFO, AuditStatus.PARTIAL, AuditStep.CLS_AUTH,
            AuditCategory.CLS, "CLS auth HTTP response received (not yet validated)",
            AuditErrorCode.NONE, null, null,
            details(
                "phase", "HTTP_EXECUTE",
                "statusCode", String.valueOf(status),
                "durationMs", String.valueOf(durationMs),
                "bodyPresent", String.valueOf(body != null),
                "bodyLenBytes", String.valueOf(bodyLenBytes)
            )
        );

        if (resp.getStatusCode() >= 200 && resp.getStatusCode() < 300 && resp.getBody() != null) {
            ObjectMapper tokenMapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

            ClsAccessTokenResponse tokenResp;
            try {
                tokenResp = tokenMapper.readValue(resp.getBody(), ClsAccessTokenResponse.class);
            }
            catch (Exception e) {
                long endMs = System.currentTimeMillis();
                audit(
                    startMs, endMs, corrId, requestId, ids,
                    AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CLS_AUTH,
                    AuditCategory.CLS, "CLS auth JSON parse failed",
                    AuditErrorCode.CLS_CONNECTION_FAILED,
                    safeMsg(e),
                    (e != null ? e.getClass().getName() : null),
                    details(
                        "phase", "JSON_PARSE",
                        "durationMs", String.valueOf(elapsedMs(startNs)),
                        "method", safe(method),
                        "urlHost", urlHost,
                        "urlPath", urlPath
                    )
                );

                if (e instanceof IOException) {
                    throw (IOException)e;
                }
                if (e instanceof RuntimeException) {
                    throw (RuntimeException)e;
                }
                throw new IOException(e);
            }

            String newToken = (tokenResp != null) ? tokenResp.getAccessToken() : null;
            long expiresInSec = 0L;

            try {
                expiresInSec = (tokenResp != null) ? tokenResp.getExpiresIn() : 0L;
            }
            catch (Exception ignore) {
                // expiresIn is optional; fallback expiry applies.
            }

            long nowAfter = System.currentTimeMillis();
            long safetySkewMs = 60_000L;

            long effectiveExpiryMs = (expiresInSec > 0)
                ? Math.max(nowAfter, (nowAfter + (expiresInSec * 1000L)) - safetySkewMs)
                : nowAfter + (50L * 60_000L);

            _cachedAccessToken = newToken;
            _cachedAccessTokenExpiresAtMs = effectiveExpiryMs;

            audit(
                startMs, System.currentTimeMillis(), corrId, requestId, ids,
                AuditSeverity.INFO, AuditStatus.SUCCESS, AuditStep.CLS_AUTH,
                AuditCategory.CLS, "CLS auth token cached",
                AuditErrorCode.NONE, null, null,
                details(
                    "phase", "JSON_PARSE",
                    "statusCode", String.valueOf(status),
                    "durationMs", String.valueOf(durationMs),
                    "accessTokenPresent", String.valueOf(newToken != null && !safe(newToken).isEmpty()),
                    "expiresInSec", String.valueOf(expiresInSec),
                    "safetySkewMs", String.valueOf(safetySkewMs),
                    "effectiveExpiryMs", String.valueOf(effectiveExpiryMs),
                    "computedTtlMs", String.valueOf(effectiveExpiryMs - nowAfter),
                    "fallbackExpiryUsed", String.valueOf(!(expiresInSec > 0))
                )
            );

            return newToken;
        }

        audit(
            System.currentTimeMillis(), System.currentTimeMillis(), corrId, requestId, ids,
            AuditSeverity.ERROR, AuditStatus.FAILED, AuditStep.CLS_AUTH,
            AuditCategory.CLS, "CLS auth failed",
            AuditErrorCode.CLS_OAUTH_FAILED, safe("HTTP " + resp.getStatusCode()),
            null,
            details(
                "phase", "HTTP_EXECUTE",
                "statusCode", String.valueOf(resp.getStatusCode()),
                "durationMs", String.valueOf(durationMs),
                "bodyPresent", String.valueOf(body != null),
                "bodyLenBytes", String.valueOf(bodyLenBytes),
                "errorClass", classifyHttpError(resp.getStatusCode()).name()
            )
        );

        return null;
    }

    // ---------------- Helpers ----------------

    /**
     * Normalizes returnedProperties for CLS course fetch based on the event type semantics.
     *
     * <p><b>Business purpose:</b> Optimize CLS fetch payload to retrieve only what is needed for a given workflow.</p>
     * <p><b>Technical purpose:</b> PUBLISHED requests no returnedProperties; CHANGED uses changeFrom types when present.</p>
     *
     * @param eventCtx Workflow context containing event type and changeFrom information.
     * @param returnedProperties Default returnedProperties from the caller.
     * @return Normalized returnedProperties array (never null).
     */
    private String[] normalizeReturnedProps(CourseEventContext eventCtx, String[] returnedProperties) {
        if (eventCtx != null) {
            String eventType = safe(eventCtx.getEventType());

            if (NotificationType.PUBLISHED.equalsIgnoreCase(eventType)) {
                return new String[0];
            }

            if (NotificationType.CHANGED.equalsIgnoreCase(eventType)) {
                List<String> changeFrom = eventCtx.getChangeFromTypes();
                if (changeFrom != null && !changeFrom.isEmpty()) {
                    return changeFrom.toArray(new String[0]);
                }
            }
        }

        return (returnedProperties == null) ? new String[0] : returnedProperties;
    }

    /**
     * Maps the first course wrapper element into a {@link CourseResponse}.
     *
     * <p><b>Business purpose:</b> Convert CLS course list responses into the project's canonical course response shape.</p>
     * <p><b>Technical purpose:</b> Select first entry and delegate to {@link CourseResponseMapper}.</p>
     *
     * @param list CLS list response.
     * @return Mapped {@link CourseResponse}, or {@code null} when list is empty or invalid.
     */
    protected CourseResponse mapFirstCourse(CourseListResponse list) {
        if (list == null) return null;

        List<CourseResponseWrapper> courses = list.getCourses();
        if (courses == null || courses.isEmpty()) return null;

        CourseResponseWrapper first = courses.get(0);
        if (first == null) return null;

        return CourseResponseMapper.mapToCourseResponse(first);
    }

    private static String extractCourseCodeFromCourseResponse(CourseResponse mapped) {
        if (mapped == null) {
            return "";
        }

        CourseResponse.Body body = mapped.getBody();
        if (body == null) {
            return "";
        }

        String cc = body.getCourseCode();
        return (cc == null) ? "" : cc;
    }

    private static String buildCoursePayload(String courseCode, String[] props) {
        String propsJson = (props != null && props.length > 0)
            ? Arrays.stream(props).map(p -> "\"" + escapeJson(p) + "\"").collect(Collectors.joining(","))
            : "";

        return "{\"courseCodes\":[\"" + escapeJson(courseCode) + "\"],\"returnedProperties\":[" + propsJson + "]}";
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String[] safeProps(String[] props) {
        return (props == null) ? new String[0] : props;
    }

    private static long elapsedMs(long startNs) {
        return (System.nanoTime() - startNs) / 1_000_000L;
    }

    private static AuditErrorCode classifyHttpError(int status) {
        if (status >= 400 && status < 500) return AuditErrorCode.CLS_HTTP_4XX;
        if (status >= 500) return AuditErrorCode.CLS_HTTP_5XX;
        return AuditErrorCode.CLS_CONNECTION_FAILED;
    }

    /**
     * Writes an immutable {@link AuditEvent} using {@link AuditEventWriter} and guarantees no exception escapes.
     *
     * <p><b>Business purpose:</b> Persist an authoritative audit trail for CLS calls and outcomes.</p>
     * <p><b>Technical purpose:</b> Build {@link AuditEvent} with normalized endTimeMs semantics and push it to the writer.</p>
     *
     * <p><b>Side effects:</b> Audit persistence (DB write) via {@link AuditEventWriter} implementation.</p>
     *
     * <p><b>Return semantics:</b> No return; errors are swallowed to protect runtime flow.</p>
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
            long normalizedEndMs = normalizeEndMs(startMs, endMs, status);

            AuditEvent.Builder b = new AuditEvent.Builder()
                .startTimeMs(startMs)
                .endTimeMs(normalizedEndMs)
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
            // Audit persistence must never block CLS processing.
        }
    }

    /**
     * Normalizes endTimeMs per audit invariants:
     * <ul>
     *   <li>STARTED events store endTimeMs=0</li>
     *   <li>Ended events clamp endTimeMs to be >= startTimeMs</li>
     * </ul>
     */
    private static long normalizeEndMs(long startMs, long endMs, AuditStatus status) {
        if (status == AuditStatus.STARTED) {
            return 0L;
        }
        if (endMs <= 0L) {
            return startMs;
        }
        return (endMs < startMs) ? startMs : endMs;
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

    private static int parseIntSafe(String s, int def) {
        try {
            return (s == null) ? def : Integer.parseInt(s.trim());
        }
        catch (Exception e) {
            return def;
        }
    }

    private static String decodeBase64OrPlain(String v) {
        if (v == null) return null;
        try {
            return new String(java.util.Base64.getDecoder().decode(v), StandardCharsets.UTF_8);
        }
        catch (Exception ignore) {
            return v;
        }
    }

    private static String urlEncode(String s) throws IOException {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (Exception e) {
            throw new IOException(e);
        }
    }

    private static String corrId() {
        String c = com.ntuc.notification.audit.util.MdcUtil.getCorrId();
        return (c != null) ? c : UUID.randomUUID().toString();
    }

    /**
     * Lightweight identity bundle used to populate audit fields consistently.
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

        static Ids from(CourseEventContext ctx) {
            long g = (ctx != null) ? ctx.getGroupId() : 0L;
            long c = (ctx != null) ? ctx.getCompanyId() : 0L;
            long u = (ctx != null) ? ctx.getUserId() : 0L;
            String cc = (ctx != null) ? safe(ctx.getCourseCode()) : "";
            long n = (ctx != null) ? ctx.getNtucDTId() : 0L;
            return new Ids(g, c, u, cc, n);
        }

        static Ids fromParamsOnly(Map<ParameterKeyEnum, Object> params) {
            long c = parseLongObj(params.get(ParameterKeyEnum.CLS_COMPANY_ID));
            long g = parseLongObj(params.get(ParameterKeyEnum.CLS_GROUP_ID));
            return new Ids(g, c, 0L, "", 0L);
        }

        Ids withCourse(String cc) {
            return new Ids(groupId, companyId, userId, safe(cc), ntucDTId);
        }

        private static long parseLongObj(Object o) {
            if (o == null) return 0L;
            try {
                return Long.parseLong(String.valueOf(o));
            }
            catch (Exception e) {
                return 0L;
            }
        }
    }
}
