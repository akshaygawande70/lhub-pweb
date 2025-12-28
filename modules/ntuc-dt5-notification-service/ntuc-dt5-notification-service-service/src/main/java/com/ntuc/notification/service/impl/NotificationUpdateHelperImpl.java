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
 * OSGi implementation.
 *
 * Important:
 * - Do NOT unit-test this class directly outside OSGi.
 * - Unit-test extracted pure mapping logic if needed.
 */
@Component(service = NotificationUpdateHelper.class)
public class NotificationUpdateHelperImpl implements NotificationUpdateHelper {

    private static final Log _log = LogFactoryUtil.getLog(NotificationUpdateHelperImpl.class);

    @Reference
    private NtucSBLocalService _ntucSBLocalService;

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
            NtucSB toUpdate = fetchOr(base);

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
                    // no-op
                    break;
            }

            toUpdate.setCanRetry(canRetry);
            toUpdate.setProcessingStatus(processingStatus);
            toUpdate.setCourseStatus(courseStatus);

            _ntucSBLocalService.updateNtucSB(toUpdate);

        } catch (Exception e) {
            _log.error("Failed to update NtucSB id=" + base.getNtucDTId(), e);
        }
    }

    @Override
    public NtucSB fetchOr(NtucSB record) {
        if (record == null) {
            return null;
        }
        NtucSB persisted = _ntucSBLocalService.fetchNtucSB(record.getNtucDTId());
        return (persisted != null) ? persisted : record;
    }

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
