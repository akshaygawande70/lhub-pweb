package com.ntuc.webcontent.structure.panel.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.ntuc.webcontent.structure.panel.constants.WebcontentStructureModifyPortletKeys;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Dhivakar Sengottaiyan
 * The type Configuration renderer.
 */
@Component(immediate = true, property = { "mvc.command.name=/configure/email", "javax.portlet.name="
		+ WebcontentStructureModifyPortletKeys.WEBCONTENTSTRUCTUREMODIFY }, service = MVCRenderCommand.class)

public class ConfigurationRenderer implements MVCRenderCommand {
	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String redirect = renderRequest.getParameter("redirect");
		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();
		portletDisplay.setShowBackIcon(true);
		portletDisplay.setURLBack(redirect);
		return "/configuration.jsp";
	}
}
