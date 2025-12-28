package web.ntuc.nlh.seo.bulkupload.control.panel.app.portlet.render;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload;
import svc.ntuc.nlh.seo.bulkupload.service.NtucBulkUploadLocalService;
import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.MVCCommandNames;
import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.NtucSeoBulkuploadControlPanelAppPortletKeys;
import web.ntuc.nlh.seo.bulkupload.control.panel.app.display.context.PanelAppManagementToolbarDisplayContext;

/**
 * @author Sagar
 * The type Ntuc seo bulkupload control panel mvc render command.
 */
@Component(immediate = true, property = {
		"javax.portlet.name=" + NtucSeoBulkuploadControlPanelAppPortletKeys.NTUCSEOBULKUPLOADCONTROLPANELAPP,
		"mvc.command.name=/", "mvc.command.name=" + MVCCommandNames.VIEW_SEO }, service = MVCRenderCommand.class)

public class NtucSeoBulkuploadControlPanelMVCRenderCommand implements MVCRenderCommand {
	/**
	 * The Log.
	 */
	Log log = LogFactoryUtil.getLog(NtucSeoBulkuploadControlPanelMVCRenderCommand.class.getName());

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {

		addSeoBulkUploadListAttributes(renderRequest);

		addManagementToolbarAttributes(renderRequest, renderResponse);

		return "/view.jsp";
	}

	private void addSeoBulkUploadListAttributes(RenderRequest renderRequest) {

		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long groupId = themeDisplay.getScopeGroupId();
		long companyId = themeDisplay.getCompanyId();

		int currentPage = ParamUtil.getInteger(renderRequest, SearchContainer.DEFAULT_CUR_PARAM,
				SearchContainer.DEFAULT_CUR);
		int delta = ParamUtil.getInteger(renderRequest, SearchContainer.DEFAULT_DELTA_PARAM,
				SearchContainer.DEFAULT_DELTA);

		int start = ((currentPage > 0) ? (currentPage - 1) : 0) * delta;
		int end = start + delta;
		String orderByCol = ParamUtil.getString(renderRequest, "orderByCol", "createDate");
		String orderByType = ParamUtil.getString(renderRequest, "orderByType", "asc");
		OrderByComparator<NtucBulkUpload> orderByComparator = OrderByComparatorFactoryUtil.create("NtucBulkUpload",
				orderByCol, !("asc").equals(orderByType));
		String keywords = ParamUtil.getString(renderRequest, "keywords");
		List<NtucBulkUpload> seoBulkUploads = _ntucBulkUploadLocalService.getNtucBulkUploadByKeywords(groupId,
				companyId, keywords, start, end, orderByComparator);
		long seoBulkUploadCount = _ntucBulkUploadLocalService.getNtucBulkUploadCountsByKeywords(groupId, companyId,
				keywords);
		renderRequest.setAttribute("seoBulkUploads", seoBulkUploads);
		renderRequest.setAttribute("seoBulkUploadCount", seoBulkUploadCount);
	}

	private void addManagementToolbarAttributes(RenderRequest renderRequest, RenderResponse renderResponse) {

		LiferayPortletRequest liferayPortletRequest = portal.getLiferayPortletRequest(renderRequest);
		LiferayPortletResponse liferayPortletResponse = portal.getLiferayPortletResponse(renderResponse);
		HttpServletRequest httpServletRequest = portal.getHttpServletRequest(renderRequest);

		PanelAppManagementToolbarDisplayContext panelAppManagementToolbarDisplayContext = new PanelAppManagementToolbarDisplayContext(
				liferayPortletRequest, liferayPortletResponse, httpServletRequest);

		renderRequest.setAttribute("panelAppManagementToolbarDisplayContext", panelAppManagementToolbarDisplayContext);
	}

	@Reference
	private Portal portal;
	@Reference
	private NtucBulkUploadLocalService _ntucBulkUploadLocalService;
}
