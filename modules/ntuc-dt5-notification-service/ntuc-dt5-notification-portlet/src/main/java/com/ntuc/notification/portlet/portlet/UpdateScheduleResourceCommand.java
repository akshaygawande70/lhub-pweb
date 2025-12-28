package com.ntuc.notification.portlet.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.ntuc.notification.model.CourseSchedule;
import com.ntuc.notification.portlet.util.UpdateScheduleParamUtil;
import com.ntuc.notification.service.CourseScheduleLocalServiceUtil;
import com.ntuc.notification.schedule.api.util.ScheduleDateTimeValidator;

import java.text.Format;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=ntuc_dt5_notification_portlet_portlet_CourseSchedulePortlet",
                "mvc.command.name=/dt5/update_schedule"
        },
        service = MVCResourceCommand.class
)
public class UpdateScheduleResourceCommand implements MVCResourceCommand {

    private static final Log _log = LogFactoryUtil.getLog(UpdateScheduleResourceCommand.class);

    private static final String DATE_PATTERN = "dd/MM/yyyy HH:mm";
    private static final String MODIFIED_PATTERN = "dd/MM/yyyy HH:mm:ss";

    @Override
    public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

        JSONObject json = JSONFactoryUtil.createJSONObject();

        try {
            ThemeDisplay themeDisplay =
                    (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

            Locale locale = (themeDisplay != null) ? themeDisplay.getLocale() : resourceRequest.getLocale();

            // Build ServiceContext early so validation uses the canonical timezone used by services.
            ServiceContext serviceContext = ServiceContextFactory.getInstance(
                    CourseSchedule.class.getName(), resourceRequest);

            TimeZone tz = (serviceContext.getTimeZone() != null)
                    ? serviceContext.getTimeZone()
                    : ((themeDisplay != null) ? themeDisplay.getTimeZone() : TimeZone.getDefault());

            Map<String, String[]> pm = resourceRequest.getParameterMap();

            // Namespace-tolerant reading: form submits <portlet:namespace />fieldName
            String ns = UpdateScheduleParamUtil.resolveNamespace(pm, "courseScheduleId");

            long courseScheduleId = _getLong(resourceRequest, pm, ns, "courseScheduleId");
            if (courseScheduleId <= 0) {
                throw new IllegalArgumentException("Missing or invalid courseScheduleId");
            }

            // Fetch existing first (we must preserve read-only error fields regardless of what UI sends)
            CourseSchedule existing = CourseScheduleLocalServiceUtil.getCourseSchedule(courseScheduleId);

            // Read editable fields
            String intakeNumber = _getString(resourceRequest, pm, ns, "intakeNumber");
            String startDateStr = _getString(resourceRequest, pm, ns, "startDate");
            String endDateStr = _getString(resourceRequest, pm, ns, "endDate");

            // STRICT server-side validation (fail-fast UX guard)
            // NOTE: service layer MUST validate again (single source of truth).
            ScheduleDateTimeValidator.validateRange(
                    startDateStr,
                    endDateStr,
                    tz.toZoneId());

            int availability = _getInt(resourceRequest, pm, ns, "availability");
            String venue = _getString(resourceRequest, pm, ns, "venue");

            int durationHours = _getInt(resourceRequest, pm, ns, "durationHours");
            int durationMinutes = _getInt(resourceRequest, pm, ns, "durationMinutes");

            // Strict YES/NO/blank for pax/waitlist
            String availablePax = UpdateScheduleParamUtil.normalizeYesNoBlank(
                    _getString(resourceRequest, pm, ns, "availablePax"));

            String availableWaitlist = UpdateScheduleParamUtil.normalizeYesNoBlank(
                    _getString(resourceRequest, pm, ns, "availableWaitlist"));

            String lxpBuyUrl = _getString(resourceRequest, pm, ns, "lxpBuyUrl");
            String scheduleDownloadUrl = _getString(resourceRequest, pm, ns, "scheduleDownloadUrl");

            // Read-only fields: always preserve existing values
            String errorCode = existing.getErrorCode();
            String errorMessage = existing.getErrorMessage();

            CourseScheduleLocalServiceUtil.updateScheduleFromAdmin(
                    courseScheduleId,
                    intakeNumber,
                    startDateStr,
                    endDateStr,
                    availability,
                    venue,
                    durationHours,
                    durationMinutes,
                    availablePax,
                    availableWaitlist,
                    lxpBuyUrl,
                    scheduleDownloadUrl,
                    errorCode,
                    errorMessage,
                    serviceContext
            );

            // Re-fetch to return authoritative latest persisted values
            CourseSchedule updated = CourseScheduleLocalServiceUtil.getCourseSchedule(courseScheduleId);

            Format dateFmt = FastDateFormatFactoryUtil.getSimpleDateFormat(DATE_PATTERN, locale, tz);
            Format modifiedFmt = FastDateFormatFactoryUtil.getSimpleDateFormat(MODIFIED_PATTERN, locale, tz);

            json.put("success", true);

            // Required for row lookup
            json.put("courseScheduleId", updated.getCourseScheduleId());

            // Values needed to update table cells + modal data (DataTables live update)
            json.put("courseCode", _nullSafe(updated.getCourseCode()));
            json.put("intakeNumber", _nullSafe(updated.getIntakeNumber()));
            json.put("availability", updated.getAvailability());
            json.put("venue", _nullSafe(updated.getVenue()));

            json.put("startDate", (updated.getStartDate() != null) ? dateFmt.format(updated.getStartDate()) : "");
            json.put("endDate", (updated.getEndDate() != null) ? dateFmt.format(updated.getEndDate()) : "");

            json.put("durationHours", updated.getDurationHours());
            json.put("durationMinutes", updated.getDurationMinutes());

            json.put("availablePax", UpdateScheduleParamUtil.normalizeYesNoBlank(updated.getAvailablePax()));
            json.put("availableWaitlist", UpdateScheduleParamUtil.normalizeYesNoBlank(updated.getAvailableWaitlist()));

            json.put("lxpBuyUrl", _nullSafe(updated.getLxpBuyUrl()));
            json.put("scheduleDownloadUrl", _nullSafe(updated.getScheduleDownloadUrl()));

            // Read-only audit-ish fields still included for modal view
            json.put("errorCode", _nullSafe(updated.getErrorCode()));
            json.put("errorMessage", _nullSafe(updated.getErrorMessage()));

            // Modified date: both display and ordering key for DataTables
            json.put("modifiedDateDisplay", (updated.getModifiedDate() != null) ? modifiedFmt.format(updated.getModifiedDate()) : "");
            json.put("modifiedDateOrder", (updated.getModifiedDate() != null) ? updated.getModifiedDate().getTime() : 0L);

        }
        catch (PortalException e) {
            _log.error("Error updating CourseSchedule via resource command", e);
            json.put("success", false);
            json.put("message", _nullSafe(e.getMessage()));
        }
        catch (IllegalArgumentException e) {
            // Controlled validation failure (date format/range, YES/NO enforcement etc.)
            json.put("success", false);
            json.put("message", _nullSafe(e.getMessage()));
        }
        catch (Exception e) {
            _log.error("Unexpected error in UpdateScheduleResourceCommand", e);
            json.put("success", false);
            json.put("message", "Unexpected error: " + _nullSafe(e.getMessage()));
        }

        try {
            resourceResponse.setContentType("application/json");
            resourceResponse.getWriter().write(json.toString());
        }
        catch (Exception e) {
            _log.error("Unable to write JSON response", e);
        }

        return true;
    }

    private static boolean _hasParam(Map<String, String[]> pm, String name) {
        return pm != null && name != null && pm.containsKey(name);
    }

    static String _getString(ResourceRequest req, Map<String, String[]> pm, String ns, String name) {
        String namespacedKey = ns + name;

        // Presence-based selection (do not treat "0" as missing)
        if (_hasParam(pm, namespacedKey)) {
            return ParamUtil.getString(req, namespacedKey);
        }

        return ParamUtil.getString(req, name);
    }

    static long _getLong(ResourceRequest req, Map<String, String[]> pm, String ns, String name) {
        String namespacedKey = ns + name;

        if (_hasParam(pm, namespacedKey)) {
            return ParamUtil.getLong(req, namespacedKey);
        }

        return ParamUtil.getLong(req, name);
    }

    static int _getInt(ResourceRequest req, Map<String, String[]> pm, String ns, String name) {
        String namespacedKey = ns + name;

        if (_hasParam(pm, namespacedKey)) {
            return ParamUtil.getInteger(req, namespacedKey);
        }

        return ParamUtil.getInteger(req, name);
    }

    static String _nullSafe(String s) {
        return (s == null) ? "" : s;
    }
}
