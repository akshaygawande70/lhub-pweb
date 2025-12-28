package web.ntuc.eshop.otp.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.dto.UserOtpDto;
import api.ntuc.common.util.PortletCommandUtil;
import api.ntuc.common.util.RedirectUtil;
import svc.ntuc.nlh.otp.model.Otp;
import svc.ntuc.nlh.otp.service.OtpLocalServiceUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.otp.constants.EshopOtpWebPortletKeys;
import web.ntuc.eshop.otp.constants.MVCCommandNames;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.OTP_RESEND_ACTION,
		"javax.portlet.name=" + EshopOtpWebPortletKeys.OTP }, service = MVCActionCommand.class)
public class ResendOtpAction extends BaseMVCActionCommand {
	private static Log log = LogFactoryUtil.getLog(ResendOtpAction.class);

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Resend OTP action - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			PortletCommandUtil.actionAndResourceCommand(actionRequest,themeDisplay);
			PortletSession portletSession = actionRequest.getPortletSession();
			UserOtpDto userOtpDto = (UserOtpDto) portletSession.getAttribute("userOtpDto", PortletSession.APPLICATION_SCOPE);
			User user = UserLocalServiceUtil.getDefaultUser(themeDisplay.getCompanyId());
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
					User.class.getName(), actionRequest);
			PortletPreferences portletPreferences = null;
			String languageId = null;
			Otp otp = this.addOtp(user, userOtpDto, serviceContext, portletPreferences, languageId);
			portletSession.setAttribute("otpId", otp.getOtpId(), PortletSession.APPLICATION_SCOPE);
			RedirectUtil.redirectByUrl(actionRequest, actionResponse, EshopOtpWebPortletKeys.OTP_URL);
		}catch (Exception e) {
			log.error("Error while resending OTP : " + e.getMessage(), e);
		}
		log.info("Resend OTP action - end");
	}
	
	private Otp addOtp(User user, UserOtpDto userOtpDto, ServiceContext serviceContext, PortletPreferences portletPreferences, String languageId) throws NumberFormatException, PortalException {
		ParameterGroup parameterOtpGroup = ParameterGroupLocalServiceUtil
				.getByCode(EshopOtpWebPortletKeys.PARAMETER_OTP_GROUP_CODE, false);
		long siteGroupId = parameterOtpGroup.getGroupId();
		Parameter parameterOtpKeyLength = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
				parameterOtpGroup.getParameterGroupId(), EshopOtpWebPortletKeys.PARAMETER_OTP_KEY_LENGTH_CODE, false);
		Parameter parameterOtpLength = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
				parameterOtpGroup.getParameterGroupId(), EshopOtpWebPortletKeys.PARAMETER_OTP_LENGTH_CODE, false);
		Otp otp = OtpLocalServiceUtil.addOtp(siteGroupId, user.getUserId(), 
				Integer.parseInt(parameterOtpKeyLength.getParamValue()), 
				Integer.parseInt(parameterOtpLength.getParamValue()), 
				userOtpDto.getFullName(), 
				userOtpDto.getEmailAddress(), 
				serviceContext,
				portletPreferences, languageId);
		return otp;
	}
	
	// masih coba coba
	
}
