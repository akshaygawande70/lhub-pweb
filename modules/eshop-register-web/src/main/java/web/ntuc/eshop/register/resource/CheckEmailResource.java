package web.ntuc.eshop.register.resource;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.PrintWriter;
import java.util.Locale;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.exception.NtucException;
import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.register.constants.MVCCommandNames;
import web.ntuc.eshop.register.constants.RegisterMessagesKey;
import web.ntuc.eshop.register.constants.RegisterPortletKeys;


@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.CHECK_EMAIL_RESOURCE,
		"javax.portlet.name=" + RegisterPortletKeys.REGISTER }, service = MVCResourceCommand.class)
public class CheckEmailResource implements MVCResourceCommand {
	
	private static Log log = LogFactoryUtil.getLog(CheckEmailResource.class);
	
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("check email resources - start");
		try {
			/*ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			Locale locale = themeDisplay.getLocale();

			boolean isAuthorized = ParamUtil.getBoolean(resourceRequest, "isAuthorized");
			boolean validCSRF = ParamUtil.getBoolean(resourceRequest, "validCSRF");
			boolean xssPass = ParamUtil.getBoolean(resourceRequest, "xssPass");

			if (!isAuthorized || !validCSRF) {
				String msg = "isAuthorized : " + isAuthorized + " | validCSRF : " + validCSRF;
				throw new NtucException(msg);
			}

			if (!xssPass) {
				PortletConfig portletConfig = (PortletConfig) resourceRequest
						.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
				String msg = LanguageUtil.format(portletConfig.getResourceBundle(locale),
						RegisterMessagesKey.XSS_VALIDATION_NOT_PASS, xssPass);
				throw new NtucException(msg);
			}*/
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			PortletCommandUtil.actionAndResourceCommand(resourceRequest,themeDisplay);
			
			PrintWriter printWriter = resourceResponse.getWriter();
			
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
			if(isUserNull(resourceRequest)) {
				jsonObject.put("data", "Valid email address");
			} else {
				jsonObject.put("data", "Invalid email address");
			}
			printWriter.write(jsonObject.toString());
			printWriter.close();
			
		}catch (Exception e) {
			log.error("Error while checking email : " + e.getMessage());
			return true;
		}
		log.info("check email resources - end");
		return false;
	}
	
	private boolean isUserNull(PortletRequest request) {
		String emailAddress = ParamUtil.getString(request, "emailAddress");
		long companyId = PortalUtil.getCompanyId(request);
		User user = UserLocalServiceUtil.fetchUserByEmailAddress(companyId, emailAddress);
		return Validator.isNull(user);
	}
}
