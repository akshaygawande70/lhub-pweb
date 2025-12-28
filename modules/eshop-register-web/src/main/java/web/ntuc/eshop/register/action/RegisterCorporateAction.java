package web.ntuc.eshop.register.action;

import com.liferay.commerce.account.constants.CommerceAccountConstants;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.dto.UserOtpDto;
import api.ntuc.common.util.AESEncryptUtil;
import api.ntuc.common.util.HttpApiUtil;
import api.ntuc.common.util.PortletCommandUtil;
import api.ntuc.common.util.RedirectUtil;
import svc.ntuc.nlh.otp.model.Otp;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.register.constants.MVCCommandNames;
import web.ntuc.eshop.register.constants.RegisterMessagesKey;
import web.ntuc.eshop.register.constants.RegisterPortletKeys;
import web.ntuc.eshop.register.util.OtpUtil;
import web.ntuc.eshop.register.util.RegisterAccount;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.REGISTER_CORPORATE_ACTION,
		"javax.portlet.name=" + RegisterPortletKeys.REGISTER }, service = MVCActionCommand.class)

public class RegisterCorporateAction extends BaseMVCActionCommand {
	private static Log log = LogFactoryUtil.getLog(RegisterCorporateAction.class);
	
	
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Register Corporate action - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			
//			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			PortletCommandUtil.actionAndResourceCommand(actionRequest,themeDisplay);
			
			String uenNumber = ParamUtil.getString(actionRequest, "uen-number");
			String companyCode = ParamUtil.getString(actionRequest, "company-code");
			String companyName = ParamUtil.getString(actionRequest, "company-name");
			String contactPerson = ParamUtil.getString(actionRequest, "contact-person");
			String contactPersonEmailAddress = ParamUtil.getString(actionRequest, "contact-person-email-address");
			String password1 = ParamUtil.getString(actionRequest, "password1");
			String password2 = ParamUtil.getString(actionRequest, "password2");
			boolean isValidationPassed = true;
	        String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	        if (contactPersonEmailAddress !=null && contactPersonEmailAddress !="" && contactPersonEmailAddress.matches(EMAIL_REGEX)) {
	        	log.info("Register Corporate action-  valid email pattern");
	        }else {
	        	isValidationPassed = false;
	        	log.error("Register Corporate action-  email regex pattern not matched");
	        	SessionErrors.add(actionRequest, "email-fail");
	        }
	        String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{12,}$";
	        if (password1 !=null && password1 !="" && password1.matches(PASSWORD_REGEX)
	        		&& password2 !=null && password2 !="" && password2.matches(PASSWORD_REGEX)) {
	        	log.info("Register Corporate action-  valid password pattern");
	        	if(!password1.equals(password2)) {
	        		isValidationPassed = false;
		        	log.error("Register Individual action-  confirm password not matched");
		        	SessionErrors.add(actionRequest, "password-confirmation-fail");
	        	}
	        }else
	        {
	        	isValidationPassed = false;
	        	log.error("Register Corporate action-  password regex pattern not matched");
	        	SessionErrors.add(actionRequest, "password-fail");
	        }
			
//			if(!RegisterAccount.corporateValidate(uenNumber, companyCode)) {
//				SessionErrors.add(actionRequest, "error-validate");
//			} else {
	        if(isValidationPassed) {
				this.registerUser(actionRequest, themeDisplay, companyName, contactPerson, contactPersonEmailAddress, companyCode, uenNumber, password1, password2);
				SessionMessages.add(actionRequest, "success-sign-up");
				RedirectUtil.redirectByUrl(actionRequest, actionResponse, RegisterPortletKeys.OTP);
	        }

//			}
			
			
			
		}catch (Exception e) {
			log.error("Error while add Register Corporate : " + e.getMessage(), e);
		}
		log.info("Register Corporate action - end");
	}
	
	private void registerUser(ActionRequest actionRequest, ThemeDisplay themeDisplay, String companyName ,String contactPerson, String contactPersonEmailAddress,String companyCode, String uenNumber, String pass1, String pass2) {
		try {
			PortletSession portletSession = actionRequest.getPortletSession();
			String contactPersons[] = contactPerson.split(" ");
			String firstName = contactPersons[0];
			String lastName = "";
			if(contactPersons.length > 1) {
				for(int i = 1; i < contactPersons.length; i++) {
					if(i == 1) {
						lastName += contactPersons[i];
					} else {
						lastName += " "+contactPersons[i];
					}
				}
			} else {
				lastName = firstName;
			}
			Role role = RoleLocalServiceUtil.fetchRole(themeDisplay.getCompanyId(), RegisterPortletKeys.ESHOP_ROLE);
			Role corporateRole = RoleLocalServiceUtil.fetchRole(themeDisplay.getCompanyId(), RegisterPortletKeys.ESHOP_CORPORATE_ROLE);
			
			long creatorUserId = 0L;
			boolean autoPassword = false;
			String password1 = pass1;
			String password2 = pass2;
			boolean autoScreenName = false;
			String screenName = contactPersonEmailAddress.replace("@", "-");
			String emailAddress = contactPersonEmailAddress;
			String languageId = themeDisplay.getLanguageId();
			String middleName = null;
			long prefixId = 0L;
			long suffixId = 0L;
			boolean male = true;
			int birthdayMonth = 1;
			int birthdayDay = 1;
			int birthdayYear = 1970;
			String jobTitle = null;
			long[] groupIds = null;
			long[] organizationIds = null;
			long[] roleIds = new long[2];
			roleIds[0] = role.getRoleId();
			roleIds[1] = corporateRole.getRoleId();
			long[] userGroupIds = null;
			boolean sendEmail = true;
			int type = CommerceAccountConstants.ACCOUNT_TYPE_BUSINESS;
			String idNumber = StringPool.BLANK;
			
//			ServiceContext serviceContext = ServiceContextFactory.getInstance(
//					User.class.getName(), actionRequest);
//			User user = UserLocalServiceUtil.addUserWithWorkflow(creatorUserId, themeDisplay.getCompanyId(), autoPassword, password1, password2, autoScreenName, screenName, emailAddress, LocaleUtil.fromLanguageId(languageId), firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);
//			RegisterAccount.add(actionRequest, user, contactPerson, contactPersonEmailAddress,type,idNumber, companyCode, companyName, uenNumber);
//			boolean deleteLogo = Boolean.FALSE;
//			byte[] logoBytes = null;
//			
//			
//			long increment = CounterLocalServiceUtil.increment(CommerceAccount.class.getName());
//			CommerceAccount account = CommerceAccountLocalServiceUtil.createCommerceAccount(increment);
			UserOtpDto userOtpDto = new UserOtpDto(creatorUserId, autoPassword, password1, password2, autoScreenName, screenName, emailAddress, languageId, contactPerson, firstName, lastName, middleName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds, roleIds, userGroupIds, sendEmail, type, idNumber, companyName, companyCode, uenNumber);
			User user = UserLocalServiceUtil.getDefaultUser(themeDisplay.getCompanyId());
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
					User.class.getName(), actionRequest);
			PortletPreferences portletPreferences = actionRequest.getPreferences();
//			String languageId = LanguageUtil.getLanguageId(actionRequest);
			Otp otp = OtpUtil.addOtp(user, userOtpDto, serviceContext, portletPreferences,languageId);
			
			portletSession.setAttribute("userOtpDto", userOtpDto, PortletSession.APPLICATION_SCOPE);
			portletSession.setAttribute("otpId", otp.getOtpId(), PortletSession.APPLICATION_SCOPE);
			
		}catch (PortalException e) {
			log.info("error while adding user : "+e.getMessage());
		}
	}
	
	
}
