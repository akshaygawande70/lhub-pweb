package web.ntuc.eshop.otp.portlet;

import web.ntuc.eshop.otp.constants.EshopOtpWebPortletKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.constants.PortletDisplayCategoryConstant;
import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.RoleUtil;

/**
 * @author fandifadillah
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category="+PortletDisplayCategoryConstant.CP_NTUC,
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=OTP",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + EshopOtpWebPortletKeys.OTP,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.version=3.0",
		"com.liferay.portlet.private-session-attributes=false",
		"javax.portlet.security-role-ref=power-user,user",
		"com.liferay.portlet.action-url-redirect=true"
	},
	service = Portlet.class
)
public class EshopOtpWebPortlet extends MVCPortlet {
	private static Log log = LogFactoryUtil.getLog(EshopOtpWebPortlet.class);
	
	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		try {			
			String authToken = CSRFValidationUtil.authToken(renderRequest);
			renderRequest.setAttribute("authToken", authToken);
			User user = UserLocalServiceUtil.getUser(themeDisplay.getUserId());
			List<Role> roles = user.getRoles();
			if (!RoleUtil.matchByPartialRoleName(roles, EshopOtpWebPortletKeys.ADMIN_ROLE)) {
				PortletSession portletSession = renderRequest.getPortletSession();
				long otpId = (long) portletSession.getAttribute("otpId", PortletSession.APPLICATION_SCOPE);
				renderRequest.setAttribute("otpId", otpId);
			}
			
		}catch (Exception e) {
			log.error("Failed when render OTP, error:" + e.getMessage());
			String finalUrl = themeDisplay.getPortalURL()+"/home";
			HttpServletResponse httpServletResponse = PortalUtil.getHttpServletResponse(renderResponse);
			httpServletResponse.sendRedirect(finalUrl);
		}
		super.render(renderRequest, renderResponse);
	}
}