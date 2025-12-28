package api.ntuc.common.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
//import javax.servlet.http.HttpServletRequest;

public class RedirectUtil {
	private RedirectUtil() {}
	
	private static Log log = LogFactoryUtil.getLog(RedirectUtil.class);
	
	public static void redirectByUrl(ActionRequest request, ActionResponse response, String urlName)
			throws PortletModeException, WindowStateException, IOException, PortalException {
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		String finalUrl = themeDisplay.getPortalURL()+"/"+urlName;
		response.sendRedirect(finalUrl);
	}
	
	public static void redirectByPortletName(ActionRequest request, ActionResponse response, String portletName, WindowState state) throws PortalException, PortletModeException, WindowStateException, IOException {
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		long plid = LayoutLocalServiceUtil.getFriendlyURLLayout(themeDisplay.getScopeGroupId(), false, "/"+portletName).getPlid();
		PortletURL redirect = PortletURLFactoryUtil.create(request,
				portletName, plid, PortletRequest.RENDER_PHASE);
		redirect.setPortletMode(PortletMode.VIEW);
		redirect.setWindowState(state);
		response.sendRedirect(redirect.toString());
	}

}
