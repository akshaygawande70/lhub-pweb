/*
 * NotificationUpdateHelperImpl.java
 *
 * OSGi DS component providing a small, focused wrapper around NtucSB persistence updates.
 *
 * Business purpose:
 * - Keeps notification/course processing flags and statuses consistent on the NtucSB tracking record.
 *
 * Technical purpose:
 * - Applies phase-specific processed flags, updates retry/status fields, and persists via NtucSBLocalService.
 *
 * @author @akshaygawande
 */
package com.ntuc.notification.service.impl;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import com.ntuc.notification.constants.CourseStatusConstants;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.service.NotificationUpdateHelper;
import com.ntuc.notification.service.NtucSBLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * OSGi implementation of {@link NotificationUpdateHelper}.
 *
 * Business purpose:
 * - Updates processing state for notification workflows so operations teams can track progress and retries.
 *
 * Technical purpose:
 * - Reads the latest NtucSB record (when available), updates phase flags and status fields, and persists changes.
 *
 * Notes:
 * - This class is a thin persistence wrapper; most logic is deterministic and can be unit-tested by injecting a mock
 *   {@link NtucSBLocalService} (no OSGi runtime required for the core branching behavior).
 */
@Component(service = NotificationUpdateHelper.class)
public class NotificationUpdateHelperImpl implements NotificationUpdateHelper {

    private static final Log _log = LogFactoryUtil.getLog(NotificationUpdateHelperImpl.class);

    @Reference
    private NtucSBLocalService _ntucSBLocalService;

    /**
     * Updates the NtucSB tracking record for a given processing phase and persists the change.
     *
     * Business purpose:
     * - Records whether a workflow phase (critical/non-critical/cron/all) has been processed and whether it can be retried.
     *
     * Technical purpose:
     * - Fetches the persisted NtucSB (if present), applies phase-specific processed flags, sets retry/status fields,
     *   then calls {@link NtucSBLocalService#updateNtucSB(NtucSB)}.
     *
     * Inputs/Invariants:
     * - {@code base} must contain a valid {@code ntucDTId}; if {@code base} is null the method returns without updating.
     * - {@code phase} determines which processed flag(s) are updated.
     *
     * Side effects:
     * - Writes to the database via {@link NtucSBLocalService#updateNtucSB(NtucSB)}.
     *
     * Audit behavior:
     * - None in this helper; audit is expected to be handled by the orchestrating workflow layer.
     *
     * Return semantics:
     * - Void; errors are caught and logged to avoid failing upstream workflows.
     *
     * @param base the NtucSB tracking record (or a partially populated instance with the same primary key)
     * @param phase which processing phase is being updated
     * @param processed whether the phase has completed processing
     * @param canRetry whether the workflow can retry after this update
     * @param processingStatus textual processing status to store on the record
     * @param courseStatus business-facing course status to store on the record
     */
    @Override
    public void update(
            NtucSB base,
            Phase phase,
            boolean processed,
            boolean canRetry,
            String processingStatus,
            String courseStatus) {

        if (base == null) {
            _log.error("NtucSB base record is null; cannot update.");
            return;
        }

        try {
            // Prefer the persisted row (if it exists) to avoid overwriting fields with a stale in-memory instance.
            NtucSB toUpdate = fetchOr(base);

            // Phase controls which processed flag(s) are toggled.
            // Details (phase) are modeled in the enum; the database stores the resulting boolean flags.
            switch (phase) {
                case CRITICAL:
                    toUpdate.setIsCriticalProcessed(processed);
                    break;
                case NON_CRITICAL:
                    toUpdate.setIsNonCriticalProcessed(processed);
                    break;
                case CRON:
                    toUpdate.setIsCronProcessed(processed);
                    break;
                case ALL:
                    toUpdate.setIsCriticalProcessed(processed);
                    toUpdate.setIsNonCriticalProcessed(processed);
                    toUpdate.setIsCronProcessed(processed);
                    break;
                default:
                    // Unknown phase: no processed flags are changed, but status fields (below) are still applied.
                    break;
            }

            // Status fields are stored regardless of phase to keep operational state consistent.
            toUpdate.setCanRetry(canRetry);
            toUpdate.setProcessingStatus(processingStatus);
            toUpdate.setCourseStatus(courseStatus);

            _ntucSBLocalService.updateNtucSB(toUpdate);
        }
        catch (Exception e) {
            _log.error("Failed to update NtucSB id=" + base.getNtucDTId(), e);
        }
    }

    /**
     * Returns the latest persisted NtucSB for the given record's primary key, falling back to the input record.
     *
     * Business purpose:
     * - Ensures updates are applied against the canonical database row when available.
     *
     * Technical purpose:
     * - Calls {@link NtucSBLocalService#fetchNtucSB(long)} using {@code record.getNtucDTId()} and returns the persisted
     *   model if found; otherwise returns the original input reference.
     *
     * Inputs/Invariants:
     * - If {@code record} is null, the method returns null.
     *
     * Side effects:
     * - Reads from the database via {@link NtucSBLocalService#fetchNtucSB(long)}.
     *
     * Audit behavior:
     * - None.
     *
     * Return semantics:
     * - Returns null only when {@code record} is null.
     *
     * @param record NtucSB instance containing the primary key
     * @return persisted NtucSB when present; otherwise the provided {@code record}; null when {@code record} is null
     */
    @Override
    public NtucSB fetchOr(NtucSB record) {
        if (record == null) {
            return null;
        }

        NtucSB persisted = _ntucSBLocalService.fetchNtucSB(record.getNtucDTId());

        // Returning the original input keeps the caller's object identity intact when no DB row exists yet.
        return (persisted != null) ? persisted : record;
    }

    /**
     * Maps Liferay workflow status to a business-facing course status string.
     *
     * Business purpose:
     * - Normalizes technical workflow states into stable course status values used by downstream UI/reporting.
     *
     * Technical purpose:
     * - Converts {@link WorkflowConstants} status values into {@link CourseStatusConstants} strings.
     *
     * Inputs/Invariants:
     * - {@code wfStatus} is expected to be a Liferay workflow status code.
     *
     * Side effects:
     * - None.
     *
     * Audit behavior:
     * - None.
     *
     * Return semantics:
     * - Returns {@link CourseStatusConstants#UNKNOWN} for unsupported/unknown workflow states.
     *
     * @param wfStatus Liferay workflow status code (e.g., {@link WorkflowConstants#STATUS_APPROVED})
     * @return mapped course status string, never null
     */
    @Override
    public String courseStatusForWorkflowStatus(int wfStatus) {
        switch (wfStatus) {
            case WorkflowConstants.STATUS_APPROVED:
                return CourseStatusConstants.PUBLISHED;
            case WorkflowConstants.STATUS_PENDING:
                return CourseStatusConstants.PENDING;
            case WorkflowConstants.STATUS_EXPIRED:
                return CourseStatusConstants.EXPIRED;
            default:
                return CourseStatusConstants.UNKNOWN;
        }
    }
}
