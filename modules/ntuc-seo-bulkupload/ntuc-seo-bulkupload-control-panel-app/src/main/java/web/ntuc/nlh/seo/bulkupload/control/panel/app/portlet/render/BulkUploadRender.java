package web.ntuc.nlh.seo.bulkupload.control.panel.app.portlet.render;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.MVCCommandNames;
import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.NtucSeoBulkuploadControlPanelAppPortletKeys;

/**
 * @author Sagar
 * The type Bulk upload render.
 */
@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.ADD_SEO, "javax.portlet.name="
		+ NtucSeoBulkuploadControlPanelAppPortletKeys.NTUCSEOBULKUPLOADCONTROLPANELAPP }, service = MVCRenderCommand.class)

public class BulkUploadRender implements MVCRenderCommand {
	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String redirect = renderRequest.getParameter("redirect");
		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();
		portletDisplay.setShowBackIcon(true);
		portletDisplay.setURLBack(redirect);
		return "/seoBulkUpload/upload.jsp";
	}
}
