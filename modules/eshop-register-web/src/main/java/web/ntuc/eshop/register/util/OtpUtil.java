package web.ntuc.eshop.register.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;

import javax.portlet.ActionRequest;
import javax.portlet.PortletPreferences;

import api.ntuc.common.dto.UserOtpDto;
import svc.ntuc.nlh.otp.model.Otp;
import svc.ntuc.nlh.otp.service.OtpLocalServiceUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.register.constants.RegisterPortletKeys;

public class OtpUtil {
	
	public static Otp addOtp(User user, UserOtpDto userOtpDto, ServiceContext serviceContext,PortletPreferences portletPreferences, String languageId) throws NumberFormatException, PortalException {
		ParameterGroup parameterOtpGroup = ParameterGroupLocalServiceUtil
				.getByCode(RegisterPortletKeys.PARAMETER_OTP_GROUP_CODE, false);
		long siteGroupId = parameterOtpGroup.getGroupId();
		Parameter parameterOtpKeyLength = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
				parameterOtpGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_OTP_KEY_LENGTH_CODE, false);
		Parameter parameterOtpLength = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
				parameterOtpGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_OTP_LENGTH_CODE, false);
		Otp otp = OtpLocalServiceUtil.addOtp(siteGroupId, user.getUserId(), Integer.parseInt(parameterOtpKeyLength.getParamValue()), Integer.parseInt(parameterOtpLength.getParamValue()), userOtpDto.getFullName(), userOtpDto.getEmailAddress(),serviceContext,portletPreferences,languageId);
		return otp;
	}
	
}
