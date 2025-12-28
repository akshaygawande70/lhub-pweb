package web.ntuc.nlh.seo.bulkupload.control.panel.app.portlet.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import svc.ntuc.nlh.seo.bulkupload.service.NtucBulkUploadLocalService;
import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.MVCCommandNames;
import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.NtucSeoBulkuploadControlPanelAppPortletKeys;

/**
 * The type Delete seo bulk upload mvc action command.
 *
 * @author Sagar  The type Delete seo bulk upload mvc action command.
 */
@Component(immediate = true, property = {
		"javax.portlet.name=" + NtucSeoBulkuploadControlPanelAppPortletKeys.NTUCSEOBULKUPLOADCONTROLPANELAPP,
		"mvc.command.name=/", "mvc.command.name=" + MVCCommandNames.DELETE_SEO }, service = MVCActionCommand.class)

public class DeleteSeoBulkUploadMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		long ntucBulkUploadId = ParamUtil.getLong(actionRequest, "ntucBulkUploadId");
		try {
			_ntucBulkUploadLocalService.deleteNtucBulkUpload(ntucBulkUploadId);
			SessionMessages.add(actionRequest, "seoBulkUploadDeleted");
		} catch (PortalException pe) {
			log.error("Error while deleting seo bulk upload : "+pe);
		}
	}

	Log log = LogFactoryUtil.getLog(DeleteSeoBulkUploadMVCActionCommand.class);

	@Reference
	private NtucBulkUploadLocalService _ntucBulkUploadLocalService;
}