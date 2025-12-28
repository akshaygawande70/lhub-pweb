package com.ntuc.notification.service.impl;

import com.liferay.journal.model.JournalArticle;
import com.liferay.petra.executor.PortalExecutorManager;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
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
import com.ntuc.notification.constants.CourseStatusConstants;
import com.ntuc.notification.constants.NotificationType;
import com.ntuc.notification.constants.ProcessingStatusConstants;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.service.CourseFetcher;
import com.ntuc.notification.service.JournalArticleService;
import com.ntuc.notification.service.NotificationHandler;
import com.ntuc.notification.service.NotificationUpdateHelper;
import com.ntuc.notification.service.NotificationUpdateHelper.Phase;
import com.ntuc.notification.service.NtucSBLocalService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * OSGi implementation.
 *
 * Important:
 * - Do NOT unit-test this class directly outside OSGi.
 * - Unit-test extracted pure helpers instead.
 *
 * Audit rule:
 * - NO legacy AuditLogger usage.
 * - Persist all observability via AuditEventWriter (DB audit).
 *
 * Email rule:
 * - NO EmailSender usage here.
 * - Email decisions are centralized in AuditEmailTriggerImpl (post-persist hook).
 */
@Component(service = NotificationHandler.class)
public class NotificationHandlerImpl implements NotificationHandler {

    private static final Log _log = LogFactoryUtil.getLog(NotificationHandlerImpl.class);
    private static final String EXECUTOR_NAME = "notification-handler";

    private static final int MAX_TASK_BACKLOG = 400;
    private static final int WARN_TASK_BACKLOG = 250;

    @Reference
    private JournalArticleService _journalArticleService;

    @Reference
    private CourseFetcher _courseFetcher;

    @Reference
    private PortalExecutorManager _portalExecutorManager;

    @Reference
    private NtucSBLocalService _ntucSBLocalService;

    @Reference
    private NotificationUpdateHelper _updateHelper;

    @Reference
    private AuditEventWriter _auditEventWriter;

    @Activate
    protected void activate() {
        // Intentionally empty:
        // - NotificationHandler must not preload or own alerting/email parameters.
    }

    // ------------------------------------------------------------
    // Entry points
    // ------------------------------------------------------------

    @Override
    public JournalArticle process(CourseEventContext eventCtx) {
        if (eventCtx == null) {
            throw new IllegalArgumentException("CourseEventContext must not be null");
        }

        final CourseArticleConfig articleConfig = requireArticleConfig(eventCtx);

        final long startMs = now();
        final String corrId = corrId();
        final String requestId = UUID.randomUUID().toString();
        final String stableEventId = UUID.randomUUID().toString();

        final Ids ids = Ids.from(eventCtx);

        final String eventType = safe(eventCtx.getEventType());
        final String courseCode = safe(ids.courseCode);

        writeAudit(
                startMs,
                0L,
                corrId,
                requestId,
                stableEventId,
                ids,
                AuditSeverity.INFO,
                AuditStatus.STARTED,
                AuditStep.ASYNC_PROCESS_START,
                AuditCategory.DT5_FLOW,
                "NotificationHandler.process entry",
                AuditErrorCode.NONE,
                null,
                null,
                details(
                        "phase", "ENTRY",
                        "eventType", eventType
                )
        );

        try {
            NtucSB record = fetchNtucSBOrNull(ids.ntucDTId);
            record = _updateHelper.fetchOr(record);

            if (record != null) {
                record.setProcessingStatus(ProcessingStatusConstants.PROCESSING);
                record.setCourseStatus(CourseStatusConstants.UNKNOWN);
                _ntucSBLocalService.updateNtucSB(record);
            }

            JournalArticle retArticle;

            if (NotificationType.PUBLISHED.equalsIgnoreCase(eventType)) {
                retArticle = triggerPublishedEventWorkflow(eventCtx, articleConfig, false);
            } else if (NotificationType.CHANGED.equalsIgnoreCase(eventType)) {
                retArticle = triggerChangedEventWorkflow(eventCtx, articleConfig, false);
            } else if (NotificationType.INACTIVE.equalsIgnoreCase(eventType)) {
                retArticle = triggerInactiveEventWorkflow(eventCtx, articleConfig);
            } else if (NotificationType.UNPUBLISHED.equalsIgnoreCase(eventType)) {
                retArticle = triggerUnpublishedEventWorkflow(eventCtx, articleConfig);
            } else {
                writeAudit(
                        startMs,
                        now(),
                        corrId,
                        requestId,
                        stableEventId,
                        ids,
                        AuditSeverity.WARNING,
                        AuditStatus.SKIPPED,
                        AuditStep.ASYNC_PROCESS_END,
                        AuditCategory.DT5_FLOW,
                        "Unsupported event type; skipped",
                        AuditErrorCode.UNSUPPORTED_EVENT_TYPE,
                        "Unsupported event type: " + eventType,
                        IllegalArgumentException.class.getName(),
                        details(
                                "phase", "ENTRY",
                                "eventType", eventType
                        )
                );

                throw new IllegalArgumentException("Unsupported event type: " + eventType);
            }

            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    stableEventId,
                    ids,
                    AuditSeverity.INFO,
                    AuditStatus.SUCCESS,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "NotificationHandler.process exit",
                    AuditErrorCode.NONE,
                    null,
                    null,
                    details(
                            "phase", "EXIT",
                            "eventType", eventType,
                            "resultId", String.valueOf(idOf(retArticle)),
                            "courseCode", courseCode
                    )
            );

            return retArticle;

        } catch (Exception e) {
            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    stableEventId,
                    ids,
                    AuditSeverity.ERROR,
                    AuditStatus.FAILED,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "NotificationHandler.process failed",
                    AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e),
                    (e != null ? e.getClass().getName() : null),
                    details(
                            "phase", "ENTRY",
                            "eventType", eventType,
                            "courseCode", courseCode
                    )
            );

            _log.error("Error processing eventType=" + eventType + " courseCode=" + courseCode, e);
            throw e;
        }
    }

    @Override
    public JournalArticle processRetrigger(CourseEventContext eventCtx) {
        if (eventCtx == null) {
            throw new IllegalArgumentException("eventCtx must not be null");
        }

        final CourseArticleConfig articleConfig = requireArticleConfig(eventCtx);

        final long startMs = now();
        final String corrId = corrId();
        final String requestId = UUID.randomUUID().toString();
        final String stableEventId = UUID.randomUUID().toString();

        final Ids ids = Ids.from(eventCtx);

        final String eventType = safe(eventCtx.getEventType());
        final String courseCode = safe(ids.courseCode);

        writeAudit(
                startMs,
                0L,
                corrId,
                requestId,
                stableEventId,
                ids,
                AuditSeverity.INFO,
                AuditStatus.STARTED,
                AuditStep.ASYNC_PROCESS_START,
                AuditCategory.DT5_FLOW,
                "NotificationHandler.processRetrigger entry",
                AuditErrorCode.NONE,
                null,
                null,
                details(
                        "phase", "ENTRY",
                        "eventType", eventType,
                        "retrigger", "true",
                        "courseCode", courseCode
                )
        );

        try {
            NtucSB record = _updateHelper.fetchOr(fetchNtucSBOrNull(ids.ntucDTId));
            JournalArticle retArticle;

            if (NotificationType.PUBLISHED.equalsIgnoreCase(eventType) || NotificationType.CHANGED.equalsIgnoreCase(eventType)) {
                retArticle = processPublishedOrChangedEventRetrigger(eventCtx);

                if (record != null) {
                    record.setIsCriticalProcessed(true);
                    record.setIsNonCriticalProcessed(true);
                    record.setIsCronProcessed(true);
                    record.setCanRetry(!(idOf(retArticle) > 0));
                    _ntucSBLocalService.updateNtucSB(record);
                }

            } else if (NotificationType.INACTIVE.equalsIgnoreCase(eventType)) {
                retArticle = triggerInactiveEventWorkflow(eventCtx, articleConfig);

                if (record != null) {
                    record.setIsCriticalProcessed(false);
                    record.setIsNonCriticalProcessed(false);
                    record.setIsCronProcessed(false);
                    record.setCanRetry(!(idOf(retArticle) > 0));
                    _ntucSBLocalService.updateNtucSB(record);
                }

            } else if (NotificationType.UNPUBLISHED.equalsIgnoreCase(eventType)) {
                retArticle = triggerUnpublishedEventWorkflow(eventCtx, articleConfig);

                if (record != null) {
                    record.setIsCriticalProcessed(false);
                    record.setIsNonCriticalProcessed(false);
                    record.setIsCronProcessed(false);
                    record.setCanRetry(!(idOf(retArticle) > 0));
                    _ntucSBLocalService.updateNtucSB(record);
                }

            } else {
                writeAudit(
                        startMs,
                        now(),
                        corrId,
                        requestId,
                        stableEventId,
                        ids,
                        AuditSeverity.WARNING,
                        AuditStatus.SKIPPED,
                        AuditStep.ASYNC_PROCESS_END,
                        AuditCategory.DT5_FLOW,
                        "Unsupported event type (retrigger); skipped",
                        AuditErrorCode.UNSUPPORTED_EVENT_TYPE,
                        "Unsupported event type: " + eventType,
                        IllegalArgumentException.class.getName(),
                        details(
                                "phase", "ENTRY",
                                "eventType", eventType,
                                "retrigger", "true",
                                "courseCode", courseCode
                        )
                );

                throw new IllegalArgumentException("Unsupported event type: " + eventType);
            }

            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    stableEventId,
                    ids,
                    AuditSeverity.INFO,
                    AuditStatus.SUCCESS,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "NotificationHandler.processRetrigger exit",
                    AuditErrorCode.NONE,
                    null,
                    null,
                    details(
                            "phase", "EXIT",
                            "eventType", eventType,
                            "resultId", String.valueOf(idOf(retArticle)),
                            "retrigger", "true",
                            "courseCode", courseCode
                    )
            );

            return retArticle;

        } catch (Exception e) {
            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    stableEventId,
                    ids,
                    AuditSeverity.ERROR,
                    AuditStatus.FAILED,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "NotificationHandler.processRetrigger failed",
                    AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e),
                    (e != null ? e.getClass().getName() : null),
                    details(
                            "phase", "ENTRY",
                            "eventType", eventType,
                            "retrigger", "true",
                            "courseCode", courseCode
                    )
            );

            _log.error("Error reprocessing eventType=" + eventType + " courseCode=" + courseCode, e);
            throw e;
        }
    }

    // ------------------------------------------------------------
    // OTL entry points
    // ------------------------------------------------------------

    @Override
    public JournalArticle triggerOTLPublishedEvent(String json, CourseEventContext eventCtx) {
        if (eventCtx == null) {
            throw new IllegalArgumentException("eventCtx must not be null");
        }

        final CourseArticleConfig articleConfig = requireArticleConfig(eventCtx);
        final String courseCode = safe(eventCtx.getCourseCode());

        try {
            JournalArticle retArticle = _courseFetcher.processPublishedOTLPublishedEvent(json, eventCtx);

            if (retArticle != null) {
                retArticle = _journalArticleService.fetchJournalArticleByUuidAndGroupId(retArticle.getUuid(), eventCtx.getGroupId());
                retArticle = changeStatus(eventCtx, articleConfig, retArticle, WorkflowConstants.STATUS_APPROVED);
            }

            return retArticle;

        } catch (Exception e) {
            _log.error("OTL published processing failed for courseCode=" + courseCode, e);
            return null;
        }
    }

    @Override
    public JournalArticle triggerOTLInactiveEvent(CourseEventContext eventCtx, int status) {
        if (eventCtx == null) {
            throw new IllegalArgumentException("eventCtx must not be null");
        }

        final CourseArticleConfig articleConfig = requireArticleConfig(eventCtx);
        final String courseCode = safe(eventCtx.getCourseCode());

        try {
            return _journalArticleService.updateStatus(eventCtx, articleConfig, /*article*/ null, status);
        } catch (Exception e) {
            _log.error("OTL inactive processing failed for courseCode=" + courseCode, e);
            return null;
        }
    }

    // ------------------------------------------------------------
    // Workflow orchestration
    // ------------------------------------------------------------

    private JournalArticle triggerPublishedEventWorkflow(CourseEventContext eventCtx, CourseArticleConfig articleConfig, boolean isRetrigger) {
        JournalArticle ret = processCritical(true, eventCtx);
        if (ret != null) {
            ret = _journalArticleService.fetchJournalArticleByUuidAndGroupId(ret.getUuid(), eventCtx.getGroupId());
            ret = asyncNonCritical(ret, eventCtx, isRetrigger);
        }
        return ret;
    }

    private JournalArticle triggerChangedEventWorkflow(CourseEventContext eventCtx, CourseArticleConfig articleConfig, boolean isRetrigger) {
        JournalArticle ret = processCritical(false, eventCtx);
        if (ret != null) {
            ret = asyncNonCritical(ret, eventCtx, isRetrigger);
        }
        return ret;
    }

    private JournalArticle triggerInactiveEventWorkflow(CourseEventContext eventCtx, CourseArticleConfig articleConfig) {
        JournalArticle ret = changeStatus(eventCtx, articleConfig, null, WorkflowConstants.STATUS_EXPIRED);
        updateNtucSBForStatusChange(eventCtx, /*success*/ ret != null, WorkflowConstants.STATUS_EXPIRED);
        return ret;
    }

    private JournalArticle triggerUnpublishedEventWorkflow(CourseEventContext eventCtx, CourseArticleConfig articleConfig) {
        JournalArticle ret = changeStatus(eventCtx, articleConfig, null, WorkflowConstants.STATUS_EXPIRED);
        updateNtucSBForStatusChange(eventCtx, /*success*/ ret != null, WorkflowConstants.STATUS_EXPIRED);
        return ret;
    }

    private JournalArticle changeStatus(CourseEventContext eventCtx, CourseArticleConfig articleConfig, JournalArticle article, int status) {
        JournalArticle ret = _journalArticleService.updateStatus(eventCtx, articleConfig, article, status);
        if (ret == null) {
            throw new IllegalStateException("updateStatus returned null");
        }
        return ret;
    }

    // ------------------------------------------------------------
    // Critical / Non-critical phases
    // ------------------------------------------------------------

    @Override
    public JournalArticle processCritical(boolean isNew, CourseEventContext ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("eventCtx must not be null");
        }

        final CourseArticleConfig articleConfig = requireArticleConfig(ctx);

        final long startMs = now();
        final String corrId = corrId();
        final String requestId = UUID.randomUUID().toString();
        final String stableEventId = UUID.randomUUID().toString();

        final Ids ids = Ids.from(ctx);
        final String code = safe(ids.courseCode);

        NtucSB record = _updateHelper.fetchOr(fetchNtucSBOrNull(ids.ntucDTId));

        // Decide status policy BEFORE critical runs (needed to catch CHANGED-but-missing -> create new)
        final boolean keepApprovedAfterCritical = shouldKeepApprovedAfterCritical(isNew, ctx, ids, code);

        writeAudit(
                startMs,
                0L,
                corrId,
                requestId,
                stableEventId,
                ids,
                AuditSeverity.INFO,
                AuditStatus.STARTED,
                AuditStep.ASYNC_PROCESS_START,
                AuditCategory.DT5_FLOW,
                "Critical phase start",
                AuditErrorCode.NONE,
                null,
                null,
                details(
                        "phase", "CRITICAL",
                        "isNew", String.valueOf(isNew),
                        "keepApprovedAfterCritical", String.valueOf(keepApprovedAfterCritical)
                )
        );

        try {
            JournalArticle retArticle = _courseFetcher.fetchAndProcessCritical(isNew, ctx);

            if (retArticle != null) {
                // Always re-fetch to ensure we work on the latest persisted version
                retArticle = _journalArticleService.fetchJournalArticleByUuidAndGroupId(retArticle.getUuid(), ctx.getGroupId());

                // - new course OR "changed-but-missing-created-new" => PENDING
                // - existing course update => APPROVED (no downgrade)
                final int postCriticalStatus = keepApprovedAfterCritical
                        ? WorkflowConstants.STATUS_APPROVED
                        : WorkflowConstants.STATUS_PENDING;

                retArticle = changeStatus(ctx, articleConfig, retArticle, postCriticalStatus);

                _updateHelper.update(
                        record,
                        Phase.CRITICAL,
                        true,
                        false,
                        ProcessingStatusConstants.SUCCESS,
                        _updateHelper.courseStatusForWorkflowStatus(postCriticalStatus)
                );
            } else {
                _updateHelper.update(
                        record,
                        Phase.CRITICAL,
                        false,
                        true,
                        ProcessingStatusConstants.FAILED,
                        resolveOrPreserveCourseStatus(ctx.getGroupId(), ctx.getCompanyId(), code, record, ctx)
                );
            }

            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    stableEventId,
                    ids,
                    AuditSeverity.INFO,
                    AuditStatus.SUCCESS,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "Critical phase end",
                    AuditErrorCode.NONE,
                    null,
                    null,
                    details(
                            "phase", "CRITICAL",
                            "isNew", String.valueOf(isNew),
                            "keepApprovedAfterCritical", String.valueOf(keepApprovedAfterCritical),
                            "postCriticalStatus", String.valueOf(keepApprovedAfterCritical
                                    ? WorkflowConstants.STATUS_APPROVED
                                    : WorkflowConstants.STATUS_PENDING),
                            "resultId", String.valueOf(idOf(retArticle))
                    )
            );

            return retArticle;

        } catch (Exception e) {
            _log.error("Critical processing failed for courseCode=" + code, e);

            _updateHelper.update(
                    record,
                    Phase.CRITICAL,
                    false,
                    true,
                    ProcessingStatusConstants.FAILED,
                    resolveOrPreserveCourseStatus(ctx.getGroupId(), ctx.getCompanyId(), code, record, ctx)
            );

            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    stableEventId,
                    ids,
                    AuditSeverity.ERROR,
                    AuditStatus.FAILED,
                    AuditStep.ASYNC_PROCESS_END,
                    AuditCategory.DT5_FLOW,
                    "Critical phase failed",
                    AuditErrorCode.DT5_UNEXPECTED,
                    safeMsg(e),
                    (e != null ? e.getClass().getName() : null),
                    details(
                            "phase", "CRITICAL",
                            "isNew", String.valueOf(isNew),
                            "keepApprovedAfterCritical", String.valueOf(keepApprovedAfterCritical)
                    )
            );

            return null;
        }
    }

    /**
     * Status policy:
     * - If "new" => do NOT keep approved after critical (go PENDING until non-critical completes)
     * - If "existing" => keep approved after critical (do not downgrade to pending)
     * - If CHANGED but article missing (CourseFetcher will treat as new) => behave like new (PENDING)
     */
    private boolean shouldKeepApprovedAfterCritical(boolean isNew, CourseEventContext ctx, Ids ids, String courseCode) {
        if (isNew) {
            return false;
        }

        // For updates, only keep APPROVED if the course already exists.
        // If it does NOT exist, CourseFetcher may create it (CHANGED but missing) => treat like new => PENDING.
        try {
            JournalArticle existing = _courseFetcher.findExistingArticle(ids.groupId, ids.companyId, courseCode, ctx);
            return existing != null;
        } catch (Exception e) {
            _log.warn("Failed to check existing article for courseCode=" + courseCode + "; defaulting to PENDING", e);
            return false;
        }
    }

    @Override
    public JournalArticle asyncNonCritical(JournalArticle article, CourseEventContext ctx, boolean isRetrigger) {
        if (ctx == null) {
            throw new IllegalArgumentException("eventCtx must not be null");
        }

        final CourseArticleConfig articleConfig = requireArticleConfig(ctx);

        final Ids ids = Ids.from(ctx);
        final String corrId = corrId();

        Runnable task = () -> {
            final long startMs = now();
            final String requestId = UUID.randomUUID().toString();
            final String stableEventId = UUID.randomUUID().toString();

            NtucSB record = _updateHelper.fetchOr(fetchNtucSBOrNull(ids.ntucDTId));
            boolean success = false;
            JournalArticle ret = null;

            long oldCompanyId = CompanyThreadLocal.getCompanyId();
            String oldPrincipal = PrincipalThreadLocal.getName();

            writeAudit(
                    startMs,
                    0L,
                    corrId,
                    requestId,
                    stableEventId,
                    ids,
                    AuditSeverity.INFO,
                    AuditStatus.STARTED,
                    AuditStep.ASYNC_ENQUEUED,
                    AuditCategory.DT5_FLOW,
                    "Non-critical phase start",
                    AuditErrorCode.NONE,
                    null,
                    null,
                    details(
                            "phase", "NON_CRITICAL",
                            "articleId", String.valueOf(idOf(article)),
                            "retrigger", String.valueOf(isRetrigger)
                    )
            );

            try {
                CompanyThreadLocal.setCompanyId(ids.companyId);
                PrincipalThreadLocal.setName(String.valueOf(ids.userId));

                ret = _courseFetcher.fetchAndProcessNonCritical(article, ctx);
                success = (ret != null);

                if (success) {
                    ret = _journalArticleService.fetchJournalArticleByUuidAndGroupId(ret.getUuid(), ctx.getGroupId());
                    changeStatus(ctx, articleConfig, ret, WorkflowConstants.STATUS_APPROVED);
                }

                _updateHelper.update(
                        record,
                        Phase.NON_CRITICAL,
                        success,
                        !success,
                        success ? ProcessingStatusConstants.SUCCESS : ProcessingStatusConstants.FAILED,
                        success
                                ? _updateHelper.courseStatusForWorkflowStatus(WorkflowConstants.STATUS_APPROVED)
                                : _updateHelper.courseStatusForWorkflowStatus(WorkflowConstants.STATUS_PENDING)
                );

                if (!success) {
                    writeAudit(
                            startMs,
                            now(),
                            corrId,
                            requestId,
                            stableEventId,
                            ids,
                            AuditSeverity.WARNING,
                            AuditStatus.PARTIAL,
                            AuditStep.ASYNC_PROCESS_END,
                            AuditCategory.DT5_FLOW,
                            "Non-critical returned null result",
                            AuditErrorCode.DT5_UNEXPECTED,
                            "fetchAndProcessNonCritical returned null",
                            null,
                            details(
                                    "phase", "NON_CRITICAL",
                                    "articleId", String.valueOf(idOf(article)),
                                    "retrigger", String.valueOf(isRetrigger)
                            )
                    );
                } else {
                    writeAudit(
                            startMs,
                            now(),
                            corrId,
                            requestId,
                            stableEventId,
                            ids,
                            AuditSeverity.INFO,
                            AuditStatus.SUCCESS,
                            AuditStep.ASYNC_PROCESS_END,
                            AuditCategory.DT5_FLOW,
                            "Non-critical phase success",
                            AuditErrorCode.NONE,
                            null,
                            null,
                            details(
                                    "phase", "NON_CRITICAL",
                                    "articleId", String.valueOf(idOf(article)),
                                    "retrigger", String.valueOf(isRetrigger),
                                    "resultId", String.valueOf(idOf(ret))
                            )
                    );
                }

            } catch (Throwable t) {
                _log.error((isRetrigger ? "Sync" : "Async") + " non-critical failed for articleId=" + idOf(article), t);

                _updateHelper.update(
                        record,
                        Phase.NON_CRITICAL,
                        false,
                        true,
                        ProcessingStatusConstants.FAILED,
                        resolveOrPreserveCourseStatus(ctx.getGroupId(), ctx.getCompanyId(), safe(ids.courseCode), record, ctx)
                );

                writeAudit(
                        startMs,
                        now(),
                        corrId,
                        requestId,
                        stableEventId,
                        ids,
                        AuditSeverity.ERROR,
                        AuditStatus.FAILED,
                        AuditStep.ASYNC_PROCESS_END,
                        AuditCategory.DT5_FLOW,
                        "Non-critical phase failed",
                        AuditErrorCode.DT5_UNEXPECTED,
                        safeMsg(t),
                        (t != null ? t.getClass().getName() : null),
                        details(
                                "phase", "NON_CRITICAL",
                                "articleId", String.valueOf(idOf(article)),
                                "retrigger", String.valueOf(isRetrigger)
                        )
                );

            } finally {
                CompanyThreadLocal.setCompanyId(oldCompanyId);
                PrincipalThreadLocal.setName(oldPrincipal);
            }
        };

        if (isRetrigger) {
            task.run();
            return article;
        }

        ExecutorService executor = _portalExecutorManager.getPortalExecutor(EXECUTOR_NAME);

        if (executor instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) executor;
            int backlog = tpe.getQueue().size();

            if (backlog > WARN_TASK_BACKLOG && _log.isWarnEnabled()) {
                _log.warn("Backlog for " + EXECUTOR_NAME + " = " + backlog + " (warn threshold " + WARN_TASK_BACKLOG + ")");
            }
            if (backlog > MAX_TASK_BACKLOG) {
                _log.error("Backlog " + backlog + " > " + MAX_TASK_BACKLOG + ", running task in caller to apply backpressure");
                task.run();
                return article;
            }
        }

        CompletableFuture
                .runAsync(task, executor)
                .exceptionally(t -> {
                    _log.error("Async task wrapper failed for articleId=" + idOf(article), t);

                    writeAudit(
                            now(),
                            now(),
                            corrId,
                            UUID.randomUUID().toString(),
                            UUID.randomUUID().toString(),
                            ids,
                            AuditSeverity.ERROR,
                            AuditStatus.FAILED,
                            AuditStep.ASYNC_PROCESS_END,
                            AuditCategory.DT5_FLOW,
                            "Async wrapper failed",
                            AuditErrorCode.DT5_UNEXPECTED,
                            safeMsg(t),
                            (t != null ? t.getClass().getName() : null),
                            details(
                                    "phase", "ASYNC_WRAPPER",
                                    "articleId", String.valueOf(idOf(article)),
                                    "retrigger", String.valueOf(isRetrigger)
                            )
                    );

                    return null;
                });

        return article;
    }

    @Override
    public JournalArticle processCron(CourseEventContext eventCtx) throws Exception {
        if (eventCtx == null) {
            throw new IllegalArgumentException("CourseEventContext must not be null");
        }
        requireArticleConfig(eventCtx);
        return _courseFetcher.fetchAndProcessCron(eventCtx);
    }

    private JournalArticle processPublishedOrChangedEventRetrigger(CourseEventContext eventCtx) {
        requireArticleConfig(eventCtx);
        return _courseFetcher.fetchAndProcessPublishedEventRetrigger(eventCtx);
    }

    // ------------------------------------------------------------
    // Status resolution helpers
    // ------------------------------------------------------------

    private void updateNtucSBForStatusChange(CourseEventContext ctx, boolean success, int requestedStatus) {
        final Ids ids = Ids.from(ctx);

        NtucSB record = _updateHelper.fetchOr(fetchNtucSBOrNull(ids.ntucDTId));

        String courseStatus = success
                ? _updateHelper.courseStatusForWorkflowStatus(requestedStatus)
                : resolveOrPreserveCourseStatus(ctx.getGroupId(), ctx.getCompanyId(), safe(ctx.getCourseCode()), record, ctx);

        _updateHelper.update(
                record,
                Phase.CRITICAL,
                success,
                !success,
                success ? ProcessingStatusConstants.SUCCESS : ProcessingStatusConstants.FAILED,
                courseStatus
        );
    }

    private String resolveOrPreserveCourseStatus(long groupId, long companyId, String courseCode, NtucSB existingRecord, CourseEventContext eventCtx) {
        try {
            JournalArticle ja = _courseFetcher.findExistingArticle(groupId, companyId, courseCode, eventCtx);
            if (ja != null) {
                return _updateHelper.courseStatusForWorkflowStatus(ja.getStatus());
            }
        } catch (Exception ex) {
            _log.warn("Could not resolve current status for courseCode=" + courseCode, ex);
        }

        String prev = (existingRecord != null) ? existingRecord.getCourseStatus() : null;
        return (prev != null && !prev.isEmpty()) ? prev : CourseStatusConstants.UNKNOWN;
    }

    // ------------------------------------------------------------
    // NtucSB lookup helper
    // ------------------------------------------------------------

    private NtucSB fetchNtucSBOrNull(long ntucDTId) {
        if (ntucDTId <= 0) {
            return null;
        }
        try {
            return _ntucSBLocalService.fetchNtucSB(ntucDTId);
        } catch (Exception e) {
            _log.warn("Failed to fetch NtucSB for ntucDTId=" + ntucDTId, e);
            return null;
        }
    }

    // ------------------------------------------------------------
    // Audit helpers (new world)
    // ------------------------------------------------------------

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
                    .jobRunId("") // fill when jobRunId lands in ctx
                    .eventId(eventId)
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
            // Audit must never break runtime flow
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
        if (t == null) {
            return "Unknown error";
        }
        String m = t.getMessage();
        return (m == null || m.trim().isEmpty()) ? t.getClass().getSimpleName() : m;
    }

    // ------------------------------------------------------------
    // Common small helpers
    // ------------------------------------------------------------

    private static String safe(String s) {
        return s == null ? "" : s;
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

    private static CourseArticleConfig requireArticleConfig(CourseEventContext ctx) {
        CourseArticleConfig cfg = (ctx == null) ? null : ctx.getArticleConfig();
        if (cfg == null) {
            throw new IllegalStateException("CourseEventContext.articleConfig is required but was null");
        }
        return cfg;
    }

    // Compact identity holder
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

            String cc = (ctx != null) ? safe(ctx.getCourseCode()) : "";
            long n = (ctx != null) ? ctx.getNtucDTId() : 0L;

            return new Ids(g, c, u, cc, n);
        }
    }
}
