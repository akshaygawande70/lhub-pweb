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
 * NotificationHandler service implementation for DT5 course processing.
 *
 * <p><b>Purpose (Business)</b>:
 * Executes the end-to-end course processing workflows (PUBLISHED/CHANGED/INACTIVE/UNPUBLISHED)
 * and keeps the NTUC course content in Liferay aligned with upstream CLS events. Progress and
 * outcomes are captured in persisted audit so operations can reconstruct timelines and decisions.</p>
 *
 * <p><b>Purpose (Technical)</b>:
 * - Orchestrates critical vs non-critical processing using {@link CourseFetcher} and {@link JournalArticleService}.
 * - Updates {@link NtucSB} processing flags/status to reflect workflow outcomes for admin/UI visibility.
 * - Schedules non-critical work asynchronously using Liferay {@link PortalExecutorManager} with backpressure
 *   safeguards to prevent unbounded queue growth.
 * - Emits audit events via {@link AuditEventWriter} only (DB audit is the system-of-record).</p>
 *
 * <p><b>Audit rules</b>:
 * - Persisted audit is the single source of truth for timelines and outcomes.
 * - AuditStep values are layer-based; lifecycle is expressed via status/severity and message/details.
 *   <ul>
 *     <li>{@link AuditStep#ENTRY}: handler entry/exit for a request/retrigger invocation</li>
 *     <li>{@link AuditStep#VALIDATION}: unsupported event type / invalid input decisions</li>
 *     <li>{@link AuditStep#JA_PROCESS}: critical/non-critical processing that results in JournalArticle changes</li>
 *     <li>{@link AuditStep#EXECUTION}: async enqueue/run infrastructure and wrapper failures</li>
 *   </ul>
 * - STARTED semantics: {@code endTimeMs = 0}. Ended statuses must have {@code endTimeMs >= startTimeMs}.
 * - Errors must be safe: no secrets, no raw payloads; use errorCode + concise errorMessage.</p>
 *
 * <p><b>Email rules</b>:
 * - No EmailSender usage here.
 * - Email alerting decisions are centralized in the audit-driven post-persist trigger layer.</p>
 *
 * <p><b>Testability note</b>:
 * This is an OSGi DS component and relies on Liferay executors and ThreadLocals; unit tests should target extracted
 * pure helpers/policies rather than attempting plain JUnit coverage for this component outside an OSGi container.</p>
 *
 * @author @akshaygawande
 */
@Component(service = NotificationHandler.class)
public class NotificationHandlerImpl implements NotificationHandler {

    private static final Log _log = LogFactoryUtil.getLog(NotificationHandlerImpl.class);
    private static final String EXECUTOR_NAME = "notification-handler";

    /**
     * Backpressure thresholds for the non-critical async queue.
     * When the queue exceeds MAX, work is executed in the caller thread to avoid runaway backlog.
     */
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

    /**
     * DB-backed audit writer. Audit must never break runtime flow; failures are swallowed by writeAudit().
     */
    @Reference
    private AuditEventWriter _auditEventWriter;

    @Activate
    protected void activate() {
        // Intentionally empty:
        // - NotificationHandler must not preload or own alerting/email parameters.
        // - All observability is persisted via audit events at runtime.
    }

    // ------------------------------------------------------------
    // Entry points
    // ------------------------------------------------------------

    /**
     * <p><b>Purpose (Business)</b>: Processes a single CLS event to keep the corresponding Liferay course content in sync.</p>
     * <p><b>Purpose (Technical)</b>: Validates context, initializes correlation identity, updates {@link NtucSB} status flags,
     * routes by eventType to the correct workflow, and records persisted audit for entry/outcome.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code eventCtx} must be non-null.
     * - {@code eventCtx.getArticleConfig()} must be non-null.</p>
     *
     * <p><b>Side effects</b>:
     * - Writes persisted audit events (STARTED and terminal SUCCESS/FAILED/SKIPPED).
     * - Updates {@link NtucSB} processingStatus/courseStatus at entry and during workflow execution.
     * - May enqueue async non-critical work via {@link PortalExecutorManager} depending on event type.</p>
     *
     * <p><b>Audit behavior</b>:
     * - {@link AuditStep#ENTRY} STARTED at handler entry and SUCCESS/FAILED on completion.
     * - Unsupported event types are recorded as {@link AuditStep#VALIDATION} with SKIPPED before throwing.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns the resulting {@link JournalArticle} when workflow succeeds.
     * - Throws runtime exceptions for unsupported types and unexpected failures (after auditing).</p>
     *
     * @param eventCtx request context containing IDs, eventType, courseCode and article configuration
     * @return processed/updated JournalArticle, or never returns if an exception is thrown
     */
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

        // Handler entry audit. Canonical step: ENTRY.
        writeAudit(
                startMs,
                0L,
                corrId,
                requestId,
                stableEventId,
                ids,
                AuditSeverity.INFO,
                AuditStatus.STARTED,
                AuditStep.ENTRY,
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
                // Unsupported types are a validation-style decision; record evidence and fail fast.
                writeAudit(
                        startMs,
                        now(),
                        corrId,
                        requestId,
                        stableEventId,
                        ids,
                        AuditSeverity.WARNING,
                        AuditStatus.SKIPPED,
                        AuditStep.VALIDATION,
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

            // Handler exit audit. Canonical step: ENTRY (layer outcome for this invocation).
            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    stableEventId,
                    ids,
                    AuditSeverity.INFO,
                    AuditStatus.SUCCESS,
                    AuditStep.ENTRY,
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
            // Failure audit remains associated with the request-level invocation.
            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    stableEventId,
                    ids,
                    AuditSeverity.ERROR,
                    AuditStatus.FAILED,
                    AuditStep.ENTRY,
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

    /**
     * <p><b>Purpose (Business)</b>: Reprocesses a prior event on-demand to recover from partial failures and close the loop.</p>
     * <p><b>Purpose (Technical)</b>: Executes the same routing as {@link #process(CourseEventContext)} but applies
     * synchronous behavior for non-critical work and updates retry flags in {@link NtucSB}.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code eventCtx} must be non-null.
     * - {@code eventCtx.getArticleConfig()} must be non-null.</p>
     *
     * <p><b>Side effects</b>:
     * - Writes persisted audit events for entry/outcome.
     * - Updates {@link NtucSB} flags (isCriticalProcessed/isNonCriticalProcessed/isCronProcessed/canRetry).</p>
     *
     * <p><b>Audit behavior</b>:
     * - {@link AuditStep#ENTRY} STARTED and terminal outcome for retrigger invocation.
     * - Unsupported event type recorded under {@link AuditStep#VALIDATION} with SKIPPED before throwing.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns the resulting {@link JournalArticle} when successful.
     * - Throws runtime exceptions for unsupported types and unexpected failures (after auditing).</p>
     *
     * @param eventCtx request context used for rerun
     * @return processed/updated JournalArticle
     */
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

        // Retrigger invocation entry audit. Canonical step: ENTRY.
        writeAudit(
                startMs,
                0L,
                corrId,
                requestId,
                stableEventId,
                ids,
                AuditSeverity.INFO,
                AuditStatus.STARTED,
                AuditStep.ENTRY,
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
                // Unsupported types are a validation-style decision; record evidence and fail fast.
                writeAudit(
                        startMs,
                        now(),
                        corrId,
                        requestId,
                        stableEventId,
                        ids,
                        AuditSeverity.WARNING,
                        AuditStatus.SKIPPED,
                        AuditStep.VALIDATION,
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

            // Retrigger invocation exit audit. Canonical step: ENTRY.
            writeAudit(
                    startMs,
                    now(),
                    corrId,
                    requestId,
                    stableEventId,
                    ids,
                    AuditSeverity.INFO,
                    AuditStatus.SUCCESS,
                    AuditStep.ENTRY,
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
                    AuditStep.ENTRY,
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

    /**
     * <p><b>Purpose (Business)</b>: Applies an OTL "published" payload so the course page reflects the latest upstream state.</p>
     * <p><b>Purpose (Technical)</b>: Delegates to {@link CourseFetcher} OTL handler, re-fetches the article, and enforces
     * an APPROVED status transition when a result exists.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code eventCtx} must be non-null.
     * - {@code eventCtx.getArticleConfig()} must be non-null.
     * - {@code json} is treated as opaque input and must never be logged or placed into audit details.</p>
     *
     * <p><b>Side effects</b>:
     * - Updates workflow status via {@link JournalArticleService#updateStatus(CourseEventContext, CourseArticleConfig, JournalArticle, int)}.</p>
     *
     * <p><b>Audit behavior</b>:
     * - This path currently logs only server logs on failure; audit coverage for OTL can be added later without changing contract.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns updated {@link JournalArticle} on success.
     * - Returns null when an exception occurs.</p>
     *
     * @param json OTL payload from upstream
     * @param eventCtx request context
     * @return updated JournalArticle, or null on failure
     */
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

    /**
     * <p><b>Purpose (Business)</b>: Applies an OTL "inactive" update so the course is no longer active/visible as required.</p>
     * <p><b>Purpose (Technical)</b>: Delegates status update to {@link JournalArticleService} using provided status code.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code eventCtx} must be non-null.
     * - {@code eventCtx.getArticleConfig()} must be non-null.</p>
     *
     * <p><b>Side effects</b>:
     * - Updates workflow status in Liferay.</p>
     *
     * <p><b>Audit behavior</b>:
     * - This path currently logs only server logs on failure; audit coverage for OTL can be added later without changing contract.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns updated {@link JournalArticle} on success.
     * - Returns null when an exception occurs.</p>
     *
     * @param eventCtx request context
     * @param status liferay workflow status to apply
     * @return updated JournalArticle, or null on failure
     */
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

    /**
     * <p><b>Purpose (Business)</b>: Executes the PUBLISHED workflow (critical now, non-critical later) to publish a course page.</p>
     * <p><b>Purpose (Technical)</b>: Runs {@link #processCritical(boolean, CourseEventContext)} with {@code isNew=true},
     * re-fetches the persisted article, and schedules non-critical processing.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code eventCtx} and {@code articleConfig} must be non-null.</p>
     *
     * <p><b>Side effects</b>:
     * - May enqueue async non-critical work depending on {@code isRetrigger}.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns the current article reference when critical succeeded; otherwise null.</p>
     */
    private JournalArticle triggerPublishedEventWorkflow(CourseEventContext eventCtx, CourseArticleConfig articleConfig, boolean isRetrigger) {
        JournalArticle ret = processCritical(true, eventCtx);
        if (ret != null) {
            ret = _journalArticleService.fetchJournalArticleByUuidAndGroupId(ret.getUuid(), eventCtx.getGroupId());
            ret = asyncNonCritical(ret, eventCtx, isRetrigger);
        }
        return ret;
    }

    /**
     * <p><b>Purpose (Business)</b>: Executes the CHANGED workflow to apply upstream updates without breaking availability.</p>
     * <p><b>Purpose (Technical)</b>: Runs critical update with {@code isNew=false}, then schedules non-critical processing.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code eventCtx} and {@code articleConfig} must be non-null.</p>
     *
     * <p><b>Side effects</b>:
     * - May enqueue async non-critical work depending on {@code isRetrigger}.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns the current article reference when critical succeeded; otherwise null.</p>
     */
    private JournalArticle triggerChangedEventWorkflow(CourseEventContext eventCtx, CourseArticleConfig articleConfig, boolean isRetrigger) {
        JournalArticle ret = processCritical(false, eventCtx);
        if (ret != null) {
            ret = asyncNonCritical(ret, eventCtx, isRetrigger);
        }
        return ret;
    }

    /**
     * <p><b>Purpose (Business)</b>: Marks a course as inactive so it no longer appears as active content.</p>
     * <p><b>Purpose (Technical)</b>: Applies an EXPIRED status change and updates {@link NtucSB} to reflect the outcome.</p>
     *
     * <p><b>Side effects</b>:
     * - Writes to Liferay workflow status and updates {@link NtucSB} flags.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns updated JournalArticle on success; may return null if status update failed upstream.</p>
     */
    private JournalArticle triggerInactiveEventWorkflow(CourseEventContext eventCtx, CourseArticleConfig articleConfig) {
        JournalArticle ret = changeStatus(eventCtx, articleConfig, null, WorkflowConstants.STATUS_EXPIRED);
        updateNtucSBForStatusChange(eventCtx, /*success*/ ret != null, WorkflowConstants.STATUS_EXPIRED);
        return ret;
    }

    /**
     * <p><b>Purpose (Business)</b>: Unpublishes a course by expiring its content.</p>
     * <p><b>Purpose (Technical)</b>: Applies an EXPIRED status change and updates {@link NtucSB} to reflect the outcome.</p>
     *
     * <p><b>Side effects</b>:
     * - Writes to Liferay workflow status and updates {@link NtucSB} flags.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns updated JournalArticle on success; may return null if status update failed upstream.</p>
     */
    private JournalArticle triggerUnpublishedEventWorkflow(CourseEventContext eventCtx, CourseArticleConfig articleConfig) {
        JournalArticle ret = changeStatus(eventCtx, articleConfig, null, WorkflowConstants.STATUS_EXPIRED);
        updateNtucSBForStatusChange(eventCtx, /*success*/ ret != null, WorkflowConstants.STATUS_EXPIRED);
        return ret;
    }

    /**
     * <p><b>Purpose (Business)</b>: Ensures the course content workflow status is transitioned to the requested state.</p>
     * <p><b>Purpose (Technical)</b>: Delegates to {@link JournalArticleService#updateStatus(CourseEventContext, CourseArticleConfig, JournalArticle, int)}
     * and enforces a non-null result to avoid silent partial updates.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code eventCtx} and {@code articleConfig} must be non-null.
     * - {@code status} must be a valid Liferay workflow constant.</p>
     *
     * <p><b>Side effects</b>:
     * - Updates persisted workflow status for the target JournalArticle.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns updated JournalArticle.
     * - Throws {@link IllegalStateException} if updateStatus returns null.</p>
     */
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

    /**
     * <p><b>Purpose (Business)</b>: Performs the "critical" part of course processing required for correctness and availability.</p>
     * <p><b>Purpose (Technical)</b>: Fetches and applies critical fields via {@link CourseFetcher}, enforces workflow status policy
     * (PENDING vs APPROVED), updates {@link NtucSB} flags, and records persisted audit for the critical phase.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code ctx} must be non-null.
     * - {@code ctx.getArticleConfig()} must be non-null.</p>
     *
     * <p><b>Side effects</b>:
     * - Writes audit events for STARTED/SUCCESS/FAILED under {@link AuditStep#JA_PROCESS}.
     * - Updates {@link NtucSB} phase flags and course/processing status.</p>
     *
     * <p><b>Audit behavior</b>:
     * - {@link AuditStep#JA_PROCESS} STARTED at phase start ({@code endTimeMs=0}) and terminal outcome on completion.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns the updated {@link JournalArticle} when critical succeeds.
     * - Returns null when critical fails (after updating NtucSB + audit).</p>
     *
     * @param isNew whether the event is treated as a new course creation path
     * @param ctx course event context
     * @return updated JournalArticle or null on failure
     */
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

        // Critical processing is JournalArticle-centric; canonical step: JA_PROCESS.
        writeAudit(
                startMs,
                0L,
                corrId,
                requestId,
                stableEventId,
                ids,
                AuditSeverity.INFO,
                AuditStatus.STARTED,
                AuditStep.JA_PROCESS,
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
                    AuditStep.JA_PROCESS,
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
                    AuditStep.JA_PROCESS,
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
     *
     * <p><b>Purpose (Business)</b>: Preserves availability for updates while allowing safe publishing for new content.</p>
     * <p><b>Purpose (Technical)</b>: Determines if post-critical status should remain APPROVED based on existence check.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code ctx} and identifiers must represent the same event being processed.
     * - Exceptions during existence check are treated as "do not keep approved" to avoid false positives.</p>
     *
     * <p><b>Side effects</b>:
     * - May call CLS/Liferay-backed lookup via {@link CourseFetcher#findExistingArticle(long, long, String, CourseEventContext)}.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns true only when an existing article is found for update paths; false otherwise.</p>
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

    /**
     * <p><b>Purpose (Business)</b>: Completes non-critical enhancements (secondary fields) to finalize course publishing.</p>
     * <p><b>Purpose (Technical)</b>: Runs non-critical processing either inline (retrigger) or asynchronously (normal path),
     * ensures thread context is set for Liferay operations, updates {@link NtucSB}, and writes persisted audit for execution
     * and JournalArticle outcomes.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code ctx} must be non-null and must contain non-null articleConfig.
     * - {@code article} may be null; audit will record articleId=0 and processing may fail accordingly.</p>
     *
     * <p><b>Side effects</b>:
     * - Updates Liferay ThreadLocals (CompanyThreadLocal / PrincipalThreadLocal) for the duration of execution.
     * - Updates workflow status to APPROVED when non-critical succeeds.
     * - Updates {@link NtucSB} phase flags and statuses.
     * - Writes audit events for:
     *   - {@link AuditStep#EXECUTION} STARTED for task entry (async infrastructure boundary)
     *   - {@link AuditStep#JA_PROCESS} terminal outcome for non-critical work.</p>
     *
     * <p><b>Audit behavior</b>:
     * - STARTED uses {@code endTimeMs=0}.
     * - Wrapper failures in {@link CompletableFuture} are audited as {@link AuditStep#EXECUTION} FAILED.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns the original {@code article} reference (enqueue semantics) for non-retrigger calls.
     * - For retrigger calls, executes inline and still returns the original {@code article} reference.</p>
     *
     * @param article current article reference (may be null)
     * @param ctx event context
     * @param isRetrigger true to run inline and produce synchronous admin outcome
     * @return original article reference (enqueue-style)
     */
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

            // Non-critical task start is executed within async infrastructure; canonical step: EXECUTION.
            writeAudit(
                    startMs,
                    0L,
                    corrId,
                    requestId,
                    stableEventId,
                    ids,
                    AuditSeverity.INFO,
                    AuditStatus.STARTED,
                    AuditStep.EXECUTION,
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

                // Non-critical outcomes are JournalArticle-related; canonical step: JA_PROCESS.
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
                            AuditStep.JA_PROCESS,
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
                            AuditStep.JA_PROCESS,
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
                        AuditStep.JA_PROCESS,
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
            // Retrigger runs non-critical inline to provide a synchronous admin outcome.
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

                    // Wrapper failures are infrastructure/execution concerns; canonical step: EXECUTION.
                    writeAudit(
                            now(),
                            now(),
                            corrId,
                            UUID.randomUUID().toString(),
                            UUID.randomUUID().toString(),
                            ids,
                            AuditSeverity.ERROR,
                            AuditStatus.FAILED,
                            AuditStep.EXECUTION,
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

    /**
     * <p><b>Purpose (Business)</b>: Runs the scheduled (cron) processing path to reconcile course state periodically.</p>
     * <p><b>Purpose (Technical)</b>: Validates required config and delegates cron processing to {@link CourseFetcher}.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code eventCtx} must be non-null.
     * - {@code eventCtx.getArticleConfig()} must be non-null.</p>
     *
     * <p><b>Side effects</b>:
     * - Delegated cron path may perform updates and auditing within lower layers.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns the resulting {@link JournalArticle}.
     * - Propagates exceptions thrown by {@link CourseFetcher#fetchAndProcessCron(CourseEventContext)}.</p>
     *
     * @param eventCtx cron execution context
     * @return processed/updated JournalArticle
     * @throws Exception when cron processing fails unexpectedly
     */
    @Override
    public JournalArticle processCron(CourseEventContext eventCtx) throws Exception {
        if (eventCtx == null) {
            throw new IllegalArgumentException("CourseEventContext must not be null");
        }
        requireArticleConfig(eventCtx);
        return _courseFetcher.fetchAndProcessCron(eventCtx);
    }

    /**
     * <p><b>Purpose (Business)</b>: Supports manual retrigger of PUBLISHED/CHANGED flows for operator recovery actions.</p>
     * <p><b>Purpose (Technical)</b>: Validates config and delegates to {@link CourseFetcher} retrigger implementation.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code eventCtx} must be non-null and contain non-null articleConfig.</p>
     *
     * <p><b>Side effects</b>:
     * - Delegated retrigger path may write JournalArticle updates and audit events in downstream layers.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns processed/updated JournalArticle, or null depending on downstream behavior.</p>
     */
    private JournalArticle processPublishedOrChangedEventRetrigger(CourseEventContext eventCtx) {
        requireArticleConfig(eventCtx);
        return _courseFetcher.fetchAndProcessPublishedEventRetrigger(eventCtx);
    }

    // ------------------------------------------------------------
    // Status resolution helpers
    // ------------------------------------------------------------

    /**
     * <p><b>Purpose (Business)</b>: Keeps admin/UI status flags consistent after INACTIVE/UNPUBLISHED operations.</p>
     * <p><b>Purpose (Technical)</b>: Updates {@link NtucSB} with success/failure flags and resolved course status, preserving
     * prior status when current status cannot be determined.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code ctx} must be non-null.
     * - {@code requestedStatus} should be a valid Liferay workflow status.</p>
     *
     * <p><b>Side effects</b>:
     * - Writes to {@link NtucSB} (via {@link NotificationUpdateHelper#update}).</p>
     */
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

    /**
     * <p><b>Purpose (Business)</b>: Ensures displayed course status remains meaningful when failures occur.</p>
     * <p><b>Purpose (Technical)</b>: Attempts to resolve current status from an existing JournalArticle; if unavailable,
     * preserves previous stored status from {@link NtucSB} and falls back to UNKNOWN.</p>
     *
     * <p><b>Side effects</b>:
     * - May call {@link CourseFetcher#findExistingArticle(long, long, String, CourseEventContext)}.</p>
     *
     * <p><b>Return semantics</b>:
     * - Never returns null; returns a non-empty status string (or UNKNOWN).</p>
     */
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

    /**
     * <p><b>Purpose (Business)</b>: Loads the processing record used for operator visibility and retry decisions.</p>
     * <p><b>Purpose (Technical)</b>: Fetches {@link NtucSB} defensively; failures are downgraded to warnings and return null
     * so event processing can continue with persisted audit capturing outcomes.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code ntucDTId} may be 0/negative; those return null.</p>
     *
     * <p><b>Side effects</b>:
     * - Reads from the ServiceBuilder local service.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns NtucSB when available.
     * - Returns null when id is invalid or lookup fails.</p>
     */
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

    /**
     * <p><b>Purpose (Business)</b>: Persists an audit record so operations can reconstruct timelines and decisions.</p>
     * <p><b>Purpose (Technical)</b>: Builds an {@link AuditEvent} and delegates to {@link AuditEventWriter}; failures are swallowed
     * to ensure auditing never breaks runtime flow.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - STARTED events must use {@code endMs=0}.
     * - Terminal events must have {@code endMs >= startMs} at the call site.</p>
     *
     * <p><b>Side effects</b>:
     * - Writes to the persisted audit store through {@link AuditEventWriter}.</p>
     *
     * <p><b>Audit behavior</b>:
     * - This method is the only write path used by this component (no server-log-only timelines).</p>
     *
     * <p><b>Return semantics</b>:
     * - No return value; exceptions are swallowed by design.</p>
     */
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

    /**
     * <p><b>Purpose (Business)</b>: Attaches stable key/value evidence to audit records for troubleshooting and reporting.</p>
     * <p><b>Purpose (Technical)</b>: Builds a small map from alternating key/value parameters; null values become empty strings.</p>
     *
     * <p><b>Return semantics</b>:
     * - Always returns a non-null map (may be empty).</p>
     */
    private static Map<String, String> details(String... kv) {
        Map<String, String> m = new HashMap<String, String>();
        for (int i = 0; i + 1 < kv.length; i += 2) {
            m.put(kv[i], kv[i + 1] == null ? "" : kv[i + 1]);
        }
        return m;
    }

    /**
     * <p><b>Purpose (Business)</b>: Produces a safe, concise error message suitable for persisted audit.</p>
     * <p><b>Purpose (Technical)</b>: Uses exception message when present; otherwise falls back to the simple class name.</p>
     *
     * <p><b>Return semantics</b>:
     * - Never returns null.</p>
     */
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

    /**
     * <p><b>Purpose (Business)</b>: Guarantees article configuration is present so downstream processing is deterministic.</p>
     * <p><b>Purpose (Technical)</b>: Extracts {@link CourseArticleConfig} from context and fails fast when missing.</p>
     *
     * <p><b>Inputs/Invariants</b>:
     * - {@code ctx} may be null; treated as missing config.</p>
     *
     * <p><b>Side effects</b>: None.</p>
     *
     * <p><b>Return semantics</b>:
     * - Returns non-null config.
     * - Throws {@link IllegalStateException} when config is absent.</p>
     */
    private static CourseArticleConfig requireArticleConfig(CourseEventContext ctx) {
        CourseArticleConfig cfg = (ctx == null) ? null : ctx.getArticleConfig();
        if (cfg == null) {
            throw new IllegalStateException("CourseEventContext.articleConfig is required but was null");
        }
        return cfg;
    }

    /**
     * Compact identity holder for stable auditing and downstream service calls.
     *
     * <p><b>Purpose (Business)</b>: Ensures course and tenant identifiers are consistently attached to audit and updates.</p>
     * <p><b>Purpose (Technical)</b>: Captures group/company/user/courseCode/ntucDTId once and passes as a single value.</p>
     */
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

        /**
         * <p><b>Purpose (Business)</b>: Normalizes event identity so audit and DB updates use consistent identifiers.</p>
         * <p><b>Purpose (Technical)</b>: Extracts identifiers defensively (null-safe) and normalizes courseCode to non-null.</p>
         *
         * <p><b>Return semantics</b>:
         * - Always returns a non-null Ids instance with non-null courseCode.</p>
         */
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
