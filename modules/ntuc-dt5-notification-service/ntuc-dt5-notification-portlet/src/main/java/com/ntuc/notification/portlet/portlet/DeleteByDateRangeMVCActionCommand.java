package com.ntuc.notification.portlet.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.ntuc.notification.portlet.constants.NtucDt5NotificationPortletKeys;
import com.ntuc.notification.service.NtucSBLocalService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.liferay.portal.kernel.util.ParamUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = { "javax.portlet.name=" + NtucDt5NotificationPortletKeys.NTUCDT5NOTIFICATION,
		"mvc.command.name=deleteByDateRange" }, service = MVCActionCommand.class)
public class DeleteByDateRangeMVCActionCommand implements MVCActionCommand {
	private static final Log _log = LogFactoryUtil.getLog(DeleteByDateRangeMVCActionCommand.class);
	@Reference
	private NtucSBLocalService _ntucSBLocalService;

	private static final SimpleDateFormat _DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public boolean processAction(ActionRequest actionRequest, ActionResponse actionResponse) {

		try {
			Date fromDate = ParamUtil.getDate(actionRequest, "fromDate", _DATE_FORMAT);
			Date toDate = ParamUtil.getDate(actionRequest, "toDate", _DATE_FORMAT);

			if (fromDate == null || toDate == null) {
				SessionMessages.add(actionRequest, "errorDeletingNotifications");
				_log.warn("Missing or invalid date parameters");
				return true;
			}
			// Normalize fromDate to start of the day
			Calendar fromCal = Calendar.getInstance();
			fromCal.setTime(fromDate);
			fromCal.set(Calendar.HOUR_OF_DAY, 0);
			fromCal.set(Calendar.MINUTE, 0);
			fromCal.set(Calendar.SECOND, 0);
			fromCal.set(Calendar.MILLISECOND, 0);
			fromDate = fromCal.getTime();

			// Normalize toDate to end of the day
			Calendar toCal = Calendar.getInstance();
			toCal.setTime(toDate);
			toCal.set(Calendar.HOUR_OF_DAY, 23);
			toCal.set(Calendar.MINUTE, 59);
			toCal.set(Calendar.SECOND, 59);
			toCal.set(Calendar.MILLISECOND, 999);
			toDate = toCal.getTime();

			_ntucSBLocalService.deleteByDateRange(fromDate, toDate);

			SessionMessages.add(actionRequest, "notificationsDeleted");
			System.out.println("\n\n\nDeletion SuccessFUll\n\n\n\n\n");
		} catch (Exception e) {
			SessionMessages.add(actionRequest, "errorDeletingNotifications");
			_log.error("Error deleting notifications by date range", e);
		}
		return true;
	}

}
