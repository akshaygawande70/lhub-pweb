package web.ntuc.eshop.otp.resource;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PrefsPropsUtil;

import java.io.PrintWriter;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.dto.UserOtpDto;
import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.otp.model.Otp;
import svc.ntuc.nlh.otp.service.OtpLocalServiceUtil;
import web.ntuc.eshop.otp.constants.EshopOtpWebPortletKeys;
import web.ntuc.eshop.otp.constants.MVCCommandNames;
import web.ntuc.eshop.otp.util.RegisterAccount;
import web.ntuc.eshop.otp.util.SessionUtil;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.OTP_CREATE_USER_RESOURCE,
		"javax.portlet.name=" + EshopOtpWebPortletKeys.OTP}, service = MVCResourceCommand.class)
public class CreateUserResource implements MVCResourceCommand{
	private static Log log = LogFactoryUtil.getLog(CreateUserResource.class);
	
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("create user resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			PortletCommandUtil.actionAndResourceCommand(resourceRequest,themeDisplay);
			PortletSession portletSession = resourceRequest.getPortletSession();
			UserOtpDto userOtpDto = (UserOtpDto) portletSession.getAttribute("userOtpDto", PortletSession.APPLICATION_SCOPE);
			PrintWriter printWriter = resourceResponse.getWriter();
			log.info("create user resources - check otp validated flag outer loop");
			long otpId = (long) portletSession.getAttribute("otpId", PortletSession.APPLICATION_SCOPE);
			Otp otp = OtpLocalServiceUtil.getOtp(otpId);
			if(otp.getOTPValidatedFlag()) {
				log.info("create user resources - if part otp  validated successfully ");
//				String obj = JSONFactoryUtil.serialize(userOtpDto);
				
				/*Company company = themeDisplay.getCompany();
				String emailToAddress = userOtpDto.getEmailAddress();
				String languageId = LanguageUtil.getLanguageId(resourceRequest);

				
				User checkedUser = UserLocalServiceUtil.getUserByEmailAddress(company.getCompanyId(), emailToAddress);*/
//				if(Validator.isNull(checkedUser)) {
//					create user
					ServiceContext serviceContext = ServiceContextFactory.getInstance(
							User.class.getName(), resourceRequest);
					User user = UserLocalServiceUtil.addUserWithWorkflow(userOtpDto.getCreatorUserId(),
							themeDisplay.getCompanyId(), userOtpDto.isAutoPassword(), userOtpDto.getPassword1(),
							userOtpDto.getPassword2(), userOtpDto.isAutoScreenName(), userOtpDto.getScreenName(),
							userOtpDto.getEmailAddress(), LocaleUtil.fromLanguageId(userOtpDto.getLanguageId()),
							userOtpDto.getFirstName(), userOtpDto.getMiddleName(), userOtpDto.getLastName(),
							userOtpDto.getPrefixId(), userOtpDto.getSuffixId(), userOtpDto.isMale(),
							userOtpDto.getBirthdayMonth(), userOtpDto.getBirthdayDay(), userOtpDto.getBirthdayYear(),
							userOtpDto.getJobTitle(), userOtpDto.getGroupIds(), userOtpDto.getOrganizationIds(),
							userOtpDto.getRoleIds(), userOtpDto.getUserGroupIds(), userOtpDto.isSendEmail(), serviceContext);
					UserLocalServiceUtil.updateLastLogin(user.getUserId(), user.getLoginIP());
					UserLocalServiceUtil.updatePasswordReset(user.getUserId(), Boolean.FALSE);
					UserLocalServiceUtil.updateEmailAddressVerified(user.getUserId(), Boolean.TRUE);
					UserLocalServiceUtil.updateAgreedToTermsOfUse(user.getUserId(), Boolean.TRUE);
					UserLocalServiceUtil.updateReminderQuery(user.getUserId(), "what-is-your-father's-middle-name", userOtpDto.getPassword1());
					log.info("after created user id = "+user.getUserId());
//					User user =  UserLocalServiceUtil.addUserWithWorkflow(creatorUserId, themeDisplay.getCompanyId(), autoPassword, password1, password2, autoScreenName, screenName, emailAddress, LocaleUtil.fromLanguageId(languageId), firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);
//					create account
//					RegisterAccount.add(resourceRequest, user,userOtpDto.getFullName(), user.getEmailAddress(),userOtpDto.getType(),userOtpDto.getNric(),null, null, null);
					RegisterAccount.add(resourceRequest,user, userOtpDto);
//					delete session
					SessionUtil.removeLocalSession(portletSession);
					
					
					if(!Validator.isNull(user)) {
						printWriter.write(String.valueOf(Boolean.TRUE));
					} else {
						printWriter.write(String.valueOf(Boolean.FALSE));
					}
				/*} 
				else {
					String emailFromName = PrefsPropsUtil.getStringFromNames(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_NAME);
					String emailFromAddress = PrefsPropsUtil.getStringFromNames(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);
					
					
					String emailParam = "emailPasswordSent";
					if(company.isSendPasswordResetLink()) {
						emailParam = "emailPasswordReset";
					}
					
					String subject = PrefsPropsUtil.getContent(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_PASSWORD_RESET_SUBJECT);
					String body = PrefsPropsUtil.getContent(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_PASSWORD_RESET_BODY);
//					PortletPreferences portletPreferences = resourceRequest.getPreferences();
//					String subject1 = portletPreferences.getValue(
//							emailParam + "Subject_" + languageId, null);
//					String body1 = portletPreferences.getValue(
//							emailParam + "Body_" + languageId, null);
//					log.info("subject1 = "+subject1);
//					log.info("body1 = "+body1);
					ServiceContext serviceContext = ServiceContextFactory.getInstance(
							User.class.getName(), resourceRequest);
					boolean isPasswordSend = UserLocalServiceUtil.sendPassword(
							company.getCompanyId(), emailToAddress, emailFromName, emailFromAddress, subject,
							body, serviceContext);
					if(isPasswordSend) {
						printWriter.write(String.valueOf(Boolean.TRUE));
					} else {
						printWriter.write(String.valueOf(Boolean.FALSE));
					}
					
					
				}*/
				
			}else {
				log.info("create user resources otp  validation failed - else part User creation failed");
			}

			
			

			
		}catch (Exception e) {
			log.info("Error while creating user : "+e.getMessage());
			return true;
		}
		log.info("create user resources - end");
		return false;
	}
	
}
