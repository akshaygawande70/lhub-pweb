package com.ntuc.notification.service.impl;

import com.liferay.petra.executor.PortalExecutorManager;
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
import com.ntuc.notification.model.ScheduleResponse;
import com.ntuc.notification.service.ScheduleFetcher;
import com.ntuc.notification.service.ScheduleHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Handles schedule and related workflows.
 *
 * New-world rules:
 * - NO legacy AuditLogger.
 * - NO EmailSender here.
 * - Persist all observability via AuditEventWriter (DB audit).
 * - Email decisions are centralized post-persist (AuditEmailTrigger).
 */
@Component(service = ScheduleHandler.class)
public class ScheduleHandlerImpl implements ScheduleHandler {

    private static final Log _log = LogFactoryUtil.getLog(ScheduleHandlerImpl.class);

    private static final String EXECUTOR_NAME = "notification-handler";

    private static final int MAX_TASK_BACKLOG = 400;
    private static final int WARN_TASK_BACKLOG = 250;

    @Reference
    private ScheduleFetcher _scheduleFetcher;

    @Reference
    private PortalExecutorManager _portalExecutorManager;

    @Reference
    private AuditEventWriter _auditEventWriter;

    @Override
    public ScheduleResponse process(CourseEventContext eventCtx) {
        if (eventCtx == null) {
            throw new IllegalArgumentException("CourseEventContext must not be null");
        }

        final long startMs = now();
        final String corrId = corrId();
        final String requestId = UUID.randomUUID().toString();
        final String eventId = UUID.randomUUID().toString();

        final Ids ids = Ids.from(eventCtx);

        writeAudit(
                startMs,
                0L,
                corrId,
                requestId,
                eventId,
                ids,
                AuditSeverity.INFO,
                AuditStatus.STARTED,
                AuditStep.ASYNC_PROCESS_START,
                AuditCategory.DT5_FLOW,
                "ScheduleHandler.process entry",
                AuditErrorCode.NONE,
                null,
                null,
                details("phase", "PROCESS")
        );

        try {
            ScheduleResponse out = triggerScheduleWorkflow(eventCtx, ids, corrId);

            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    eventId,
                    ids,
                    AuditSeverity.INFO,
                    (out != null) ? AuditStatus.SUCCESS : AuditStatus.SKIPPED,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "ScheduleHandler.process exit",
                    AuditErrorCode.NONE,
                    null,
                    null,
                    details(
                            "phase", "PROCESS",
                            "hasResponse", String.valueOf(out != null)
                    )
            );

            return out;

        } catch (RuntimeException e) {
            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    eventId,
                    ids,
                    AuditSeverity.ERROR,
                    AuditStatus.FAILED,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "ScheduleHandler.process failed",
                    AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e),
                    e.getClass().getName(),
                    details("phase", "PROCESS")
            );
            _log.error("ScheduleHandler.process failed courseCode=" + safe(ids.courseCode), e);
            throw e;
        } catch (Exception e) {
            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    eventId,
                    ids,
                    AuditSeverity.ERROR,
                    AuditStatus.FAILED,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "ScheduleHandler.process failed",
                    AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e),
                    e.getClass().getName(),
                    details("phase", "PROCESS")
            );
            _log.error("ScheduleHandler.process failed courseCode=" + safe(ids.courseCode), e);
            throw new IllegalStateException("Schedule processing failed for courseCode=" + safe(ids.courseCode), e);
        }
    }

    private ScheduleResponse triggerScheduleWorkflow(CourseEventContext eventCtx, Ids ids, String corrId) {
        final long startMs = now();
        final String requestId = UUID.randomUUID().toString();
        final String eventId = UUID.randomUUID().toString();

        writeAudit(
                startMs,
                0L,
                corrId,
                requestId,
                eventId,
                ids,
                AuditSeverity.INFO,
                AuditStatus.STARTED,
                AuditStep.ASYNC_PROCESS_START,
                AuditCategory.DT5_FLOW,
                "Schedule workflow start (critical)",
                AuditErrorCode.NONE,
                null,
                null,
                details("phase", "CRITICAL")
        );

        Map<Long, ScheduleResponse> scheduleResponseMap = _scheduleFetcher.fetchAndProcessCriticalSchedule(eventCtx);

        writeAudit(
                startMs,
                now(),
                corrId,
                requestId,
                eventId,
                ids,
                AuditSeverity.INFO,
                (scheduleResponseMap != null && !scheduleResponseMap.isEmpty()) ? AuditStatus.SUCCESS : AuditStatus.SKIPPED,
                AuditStep.ASYNC_PROCESS_END,
                AuditCategory.DT5_FLOW,
                "Schedule workflow end (critical)",
                AuditErrorCode.NONE,
                null,
                null,
                details(
                        "phase", "CRITICAL",
                        "articleCount", String.valueOf(scheduleResponseMap == null ? 0 : scheduleResponseMap.size())
                )
        );

        if (scheduleResponseMap == null || scheduleResponseMap.isEmpty()) {
            return null;
        }

        for (Map.Entry<Long, ScheduleResponse> e : scheduleResponseMap.entrySet()) {
            processAsyncNonCritical(e.getKey(), e.getValue(), eventCtx, ids, corrId);
            return e.getValue();
        }

        return null;
    }

    @Override
    public Map<Long, ScheduleResponse> processCritical(CourseEventContext eventCtx) {
        return _scheduleFetcher.fetchAndProcessCriticalSchedule(eventCtx);
    }

    @Override
    public void processAsyncNonCritical(long articleId, ScheduleResponse scheduleResponse, CourseEventContext eventCtx) {
        // kept for interface compatibility; internal call uses richer overload below
        processAsyncNonCritical(articleId, scheduleResponse, eventCtx, Ids.from(eventCtx), corrId());
    }

    private void processAsyncNonCritical(
            long articleId,
            ScheduleResponse scheduleResponse,
            CourseEventContext eventCtx,
            Ids ids,
            String corrId) {

        ExecutorService executor = _portalExecutorManager.getPortalExecutor(EXECUTOR_NAME);

        Runnable task = () -> {
            final long startMs = now();
            final String requestId = UUID.randomUUID().toString();
            final String eventId = UUID.randomUUID().toString();

            writeAudit(
                    startMs,
                    0L,
                    corrId,
                    requestId,
                    eventId,
                    ids,
                    AuditSeverity.INFO,
                    AuditStatus.STARTED,
                    AuditStep.ASYNC_ENQUEUED,
                    AuditCategory.DT5_FLOW,
                    "Schedule non-critical start",
                    AuditErrorCode.NONE,
                    null,
                    null,
                    details(
                            "phase", "NON_CRITICAL",
                            "articleId", String.valueOf(articleId)
                    )
            );

            try {
                _scheduleFetcher.fetchAndProcessNonCriticalSchedule(articleId, scheduleResponse, eventCtx);

                writeAudit(
                        startMs,
                        now(),
                        corrId,
                        requestId,
                        eventId,
                        ids,
                        AuditSeverity.INFO,
                        AuditStatus.SUCCESS,
                        AuditStep.ASYNC_PROCESS_END,
                        AuditCategory.DT5_FLOW,
                        "Schedule non-critical success",
                        AuditErrorCode.NONE,
                        null,
                        null,
                        details(
                                "phase", "NON_CRITICAL",
                                "articleId", String.valueOf(articleId)
                        )
                );
            } catch (Throwable t) {
                _log.error("Schedule non-critical failed articleId=" + articleId + " courseCode=" + safe(ids.courseCode), t);

                writeAudit(
                        startMs,
                        now(),
                        corrId,
                        requestId,
                        eventId,
                        ids,
                        AuditSeverity.ERROR,
                        AuditStatus.FAILED,
                        AuditStep.ASYNC_PROCESS_END,
                        AuditCategory.DT5_FLOW,
                        "Schedule non-critical failed",
                        AuditErrorCode.DT5_UNEXPECTED,
                        safeMsg(t),
                        t.getClass().getName(),
                        details(
                                "phase", "NON_CRITICAL",
                                "articleId", String.valueOf(articleId)
                        )
                );
            }
        };

        if (executor instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) executor;
            int backlog = tpe.getQueue().size();

            if (backlog > WARN_TASK_BACKLOG && _log.isWarnEnabled()) {
                _log.warn("Backlog for " + EXECUTOR_NAME + " = " + backlog + " (warn threshold " + WARN_TASK_BACKLOG + ")");
            }
            if (backlog > MAX_TASK_BACKLOG) {
                _log.error("Backlog " + backlog + " > " + MAX_TASK_BACKLOG + ", running non-critical in caller to apply backpressure");
                task.run();
                return;
            }
        }

        CompletableFuture.runAsync(task, executor).exceptionally(t -> {
            _log.error("Async wrapper failed (schedule) articleId=" + articleId, t);
            return null;
        });
    }

    // ---------------- audit helpers ----------------

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
