package com.ntuc.notification.service.internal.cls;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.service.ClsConnectionHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Plain Java core for CLS dummy APIs.
 *
 * Rules:
 * - No DB writes.
 * - Return raw JSON from CLS (no DTO parsing/serialization).
 * - Audit STARTED + outcome.
 */
public class ClsDummyApiFacade {

    public enum DummyKind {
        COURSES(AuditStep.CLS_DUMMY_COURSES_FETCH, "CLS dummy courses fetch"),
        SUBSCRIPTIONS(AuditStep.CLS_DUMMY_SUBSCRIPTIONS_FETCH, "CLS dummy subscriptions fetch");

        private final AuditStep _step;
        private final String _msg;

        DummyKind(AuditStep step, String msg) {
            _step = step;
            _msg = msg;
        }

        public AuditStep step() { return _step; }
        public String msg() { return _msg; }
    }

    private final ClsConnectionHelper _clsConnectionHelper;
    private final AuditEventWriter _auditEventWriter;

    public ClsDummyApiFacade(
            ClsConnectionHelper clsConnectionHelper,
            AuditEventWriter auditEventWriter) {

        _clsConnectionHelper = clsConnectionHelper;
        _auditEventWriter = auditEventWriter;
    }

    public String fetchDummyJson(
            String kindRaw,
            String courseCode,
            long companyId,
            long groupId,
            long userId,
            String correlationId) {

        DummyKind kind = parseKind(kindRaw);

        if (kind == null) {
            long t = now();
            audit(
                t, t, correlationId,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                companyId, groupId, userId, safe(courseCode),
                AuditSeverity.WARNING, AuditStatus.FAILED,
                AuditStep.ASYNC_PROCESS_END,
                AuditCategory.DT5_FLOW,
                "CLS dummy fetch rejected: invalid kind",
                AuditErrorCode.DT5_INVALID_INPUT,
                safe("Invalid kind=" + safe(kindRaw) + " (expected COURSES|SUBSCRIPTIONS)"),
                null,
                details("kindRaw", safe(kindRaw), "expected", "COURSES|SUBSCRIPTIONS")
            );
            return null;
        }

        return fetch(kind, courseCode, companyId, groupId, userId, correlationId);
    }

    static DummyKind parseKind(String kindRaw) {
        if (kindRaw == null) return null;
        String k = kindRaw.trim();
        if (k.isEmpty()) return null;

        if ("COURSES".equalsIgnoreCase(k)) return DummyKind.COURSES;
        if ("SUBSCRIPTIONS".equalsIgnoreCase(k)) return DummyKind.SUBSCRIPTIONS;

        return null;
    }

    private String fetch(
            DummyKind kind,
            String courseCode,
            long companyId,
            long groupId,
            long userId,
            String correlationId) {

        if (courseCode == null || courseCode.trim().isEmpty()) {
            return null;
        }

        final long startMs = now();
        final String requestId = UUID.randomUUID().toString();
        final String eventId = UUID.randomUUID().toString();

        audit(
            startMs, 0L, correlationId, requestId, eventId,
            companyId, groupId, userId, courseCode,
            AuditSeverity.INFO, AuditStatus.STARTED,
            kind.step(), AuditCategory.CLS,
            kind.msg() + " start",
            AuditErrorCode.NONE, null, null,
            details("kind", kind.name())
        );

        try {
            String json;

            if (kind == DummyKind.COURSES) {
                json = _clsConnectionHelper.getCoursesDummyRawJson(courseCode);
            }
            else {
                json = _clsConnectionHelper.getSubscriptionsDummyRawJson(courseCode);
            }

            if (json == null) {
                audit(
                    startMs, now(), correlationId, requestId, eventId,
                    companyId, groupId, userId, courseCode,
                    AuditSeverity.WARNING, AuditStatus.FAILED,
                    kind.step(), AuditCategory.CLS,
                    kind.msg() + " returned null body",
                    AuditErrorCode.CLS_INVALID_RESPONSE,
                    "CLS dummy returned null body",
                    null,
                    details("kind", kind.name(), "reason", "NULL_BODY")
                );
                return null;
            }

            audit(
                startMs, now(), correlationId, requestId, eventId,
                companyId, groupId, userId, courseCode,
                AuditSeverity.INFO, AuditStatus.SUCCESS,
                kind.step(), AuditCategory.CLS,
                kind.msg() + " success",
                AuditErrorCode.NONE, null, null,
                details("kind", kind.name(), "bytes", String.valueOf(json.length()))
            );

            return json;
        }
        catch (Exception e) {
            audit(
                startMs, now(), correlationId, requestId, eventId,
                companyId, groupId, userId, courseCode,
                AuditSeverity.ERROR, AuditStatus.FAILED,
                kind.step(), AuditCategory.CLS,
                kind.msg() + " failed",
                AuditErrorCode.CLS_CONNECTION_FAILED,
                safeMsg(e),
                (e != null ? e.getClass().getName() : null),
                details("kind", kind.name())
            );
            return null;
        }
    }

    private void audit(
            long startMs,
            long endMs,
            String corrId,
            String requestId,
            String eventId,
            long companyId,
            long groupId,
            long userId,
            String courseCode,
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
            long normalizedEndMs = (status == AuditStatus.STARTED) ? 0L : normalizeEndMs(startMs, endMs);

            AuditEvent.Builder b = new AuditEvent.Builder()
                .startTimeMs(startMs)
                .endTimeMs(normalizedEndMs)
                .correlationId(safe(corrId))
                .requestId(safe(requestId))
                .eventId(safe(eventId))
                .jobRunId("")
                .companyId(companyId)
                .groupId(groupId)
                .userId(userId)
                .courseCode(safe(courseCode))
                .ntucDTId(0L)
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
            // Audit must never break runtime flow.
        }
    }

    private static long normalizeEndMs(long startMs, long endMs) {
        if (endMs <= 0L) return startMs;
        return (endMs < startMs) ? startMs : endMs;
    }

    private static Map<String, String> details(String... kv) {
        Map<String, String> m = new HashMap<String, String>();
        for (int i = 0; i + 1 < kv.length; i += 2) {
            m.put(kv[i], kv[i + 1] == null ? "" : kv[i + 1]);
        }
        return m;
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }

    private static String safeMsg(Throwable t) {
        if (t == null) return "Unknown error";
        String m = t.getMessage();
        return (m == null || m.trim().isEmpty()) ? t.getClass().getSimpleName() : m;
    }

    private static long now() {
        return System.currentTimeMillis();
    }
}
