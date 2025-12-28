package com.ntuc.notification.portlet.portlet;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.ntuc.notification.constants.ProcessingStatusConstants;
import com.ntuc.notification.model.CourseProcessResult;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.portlet.constants.NtucDt5NotificationPortletKeys;
import com.ntuc.notification.service.ClsCourseFieldsProcessor;
import com.ntuc.notification.service.NtucSBLocalService;

import java.io.PrintWriter;
import java.util.Date;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
    property = {
        "javax.portlet.name=" + NtucDt5NotificationPortletKeys.NTUCDT5NOTIFICATION,
        "mvc.command.name=/retriggerEntry"
    },
    service = MVCResourceCommand.class
)
public class RetriggerEntryMVCResourceCommand implements MVCResourceCommand {

    @Reference
    private NtucSBLocalService _ntucSBLocalService;

    @Reference
    private ClsCourseFieldsProcessor _clsCourseFieldsProcessor;

    @Override
    public boolean serveResource(ResourceRequest req, ResourceResponse res) throws PortletException {
        res.setContentType(ContentTypes.APPLICATION_JSON);
        res.setCharacterEncoding("UTF-8");

        final JSONObject out = JSONFactoryUtil.createJSONObject();

        try (PrintWriter writer = res.getWriter()) {
            long entryId = ParamUtil.getLong(req, "entryId");

            if (entryId <= 0) {
                out.put("success", false);
                out.put("message", "Invalid entryId.");
                writer.write(out.toString());
                return true;
            }

            NtucSB entry = _ntucSBLocalService.fetchNtucSB(entryId);

            if (entry == null) {
                out.put("success", false);
                out.put("message", "Entry not found.");
                writer.write(out.toString());
                return true;
            }

            if (!entry.getCanRetry()) {
                out.put("success", false);
                out.put("message", "Entry cannot be retried right now.");
                writer.write(out.toString());
                return true;
            }

            // Mark in-progress (best-effort lock)
            entry.setCanRetry(false);
            entry.setTotalRetries(entry.getTotalRetries() + 1);
            entry.setLastRetried(new Date());
            _ntucSBLocalService.updateNtucSB(entry);

            boolean success = false;
            String message;
            String courseStatus = null;
            try {
                CourseProcessResult result = _clsCourseFieldsProcessor.handleCourseRetrigger(entry);

                // Treat "result != null && result.isSuccess()" as success.
                // If your CourseProcessResult uses a different API, adjust below accordingly.
                success = (result != null && result.isSuccess());
                courseStatus = result.getArticleStatus();
                message = success
                        ? ("Retriggered entry #" + entryId + " successfully.")
                        : ("Failed to retrigger entry #" + entryId + ".");

            } catch (Exception e) {
                success = false;
                message = "Error: " + (e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage());
            }

            // Unlock on failure only (keep locked on success to avoid duplicate retries)
            entry = _ntucSBLocalService.fetchNtucSB(entryId);
            if (entry != null) {
                if (!success) {
                    entry.setCanRetry(true);
                    entry.setProcessingStatus(ProcessingStatusConstants.FAILED);
                } else {
                	entry.setProcessingStatus(ProcessingStatusConstants.SUCCESS);
                }
                
                entry.setCourseStatus(courseStatus);
                entry.setLastRetried(new Date());
                _ntucSBLocalService.updateNtucSB(entry);
            }

            out.put("success", success);
            out.put("message", message);
            writer.write(out.toString());
            return true;

        } catch (Exception e) {
            // Last-resort fallback (writer may already be committed)
            throw new PortletException("Unexpected error in /retriggerEntry", e);
        }
    }
}
