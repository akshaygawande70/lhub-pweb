package api.ntuc.common.util;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.ClientDataRequest;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;

import api.ntuc.common.constants.PortletCommandConstant;

public class PortletCommandUtil {
	private static Log log = LogFactoryUtil.getLog(PortletCommandUtil.class);
	
	public static void renderCommand(RenderRequest renderRequest) throws Exception {
		boolean isAuthorized = ParamUtil.getBoolean(renderRequest, "isAuthorized");
		boolean xssPass = ParamUtil.getBoolean(renderRequest, "xssPass");
		if (!(isAuthorized && xssPass)) {
			log.error("view render - isAuthorized : " + isAuthorized + " | xssPass : " + xssPass);
		}
	}
	
	public static void actionAndResourceCommand(ClientDataRequest request, ThemeDisplay themeDisplay) throws Exception {
		boolean isAuthorized = ParamUtil.getBoolean(request, "isAuthorized");
		boolean validCSRF = ParamUtil.getBoolean(request, "validCSRF");
		boolean xssPass = ParamUtil.getBoolean(request, "xssPass");

		if (!isAuthorized || !validCSRF) {
			String msg = "isAuthorized : " + isAuthorized + " | validCSRF : " + validCSRF;
			SessionErrors.add(request, "you-dont-have-permission-or-your-session-is-end");
			throw new Exception(msg);
		}

		if (!xssPass) {
			PortletConfig portletConfig = (PortletConfig) request
					.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			String msg = LanguageUtil.format(portletConfig.getResourceBundle(themeDisplay.getLocale()),
					PortletCommandConstant.XSS_VALIDATION_NOT_PASS, xssPass);
			SessionErrors.add(request, "your-input-not-pass-xss");
			throw new Exception(msg);
		}
	}
}
