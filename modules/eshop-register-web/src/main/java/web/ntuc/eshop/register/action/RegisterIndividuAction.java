package web.ntuc.eshop.register.action;

import com.liferay.commerce.account.constants.CommerceAccountConstants;
import com.liferay.portal.kernel.exception.PortalException;
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
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.dto.UserOtpDto;
import api.ntuc.common.util.PortletCommandUtil;
import api.ntuc.common.util.RedirectUtil;
import svc.ntuc.nlh.otp.model.Otp;
import web.ntuc.eshop.register.constants.MVCCommandNames;
import web.ntuc.eshop.register.constants.RegisterMessagesKey;
import web.ntuc.eshop.register.constants.RegisterPortletKeys;
import web.ntuc.eshop.register.dto.PersonDto;
import web.ntuc.eshop.register.util.OtpUtil;

@Component(immediate = true, property = { 
		"mvc.command.name=" + MVCCommandNames.REGISTER_INDIVIDUAL_ACTION,
		"javax.portlet.name=" + RegisterPortletKeys.REGISTER}, service = MVCActionCommand.class)
public class RegisterIndividuAction extends BaseMVCActionCommand {
	private static Log log = LogFactoryUtil.getLog(RegisterIndividuAction.class);

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Register Individual action - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			PortletCommandUtil.actionAndResourceCommand(actionRequest,themeDisplay);
			
			String idNumber = ParamUtil.getString(actionRequest, "id-number");
			String email = ParamUtil.getString(actionRequest, "email");
			String fullName = ParamUtil.getString(actionRequest, "full-name");
			String dob = ParamUtil.getString(actionRequest, "dobSingpas", ParamUtil.getString(actionRequest, "dob"));
			String password1 = ParamUtil.getString(actionRequest, "password1");
			String password2 = ParamUtil.getString(actionRequest, "password2");
			log.info("idNumber = "+idNumber);
			log.info("email = "+email);
			log.info("fullName = "+fullName);
			log.info("dob = "+dob);
//			log.info("password1 = "+password1);
//			log.info("password2 = "+password2);
			boolean isValidationPassed = true;
			PersonDto person = new PersonDto(idNumber, fullName, dob, email);
			String strRegexnric = "^\\d{3}[a-zA-Z]$"; // The regex pattern
	        if (idNumber != null && idNumber != "" && idNumber.length() >0 && idNumber.length() == 4  && idNumber.matches(strRegexnric)) {
	        	log.info("Register Individual action-  valid id number length 4 regex pattern matched");
	        }else {
	        	isValidationPassed = false;
	        	log.error("Register Individual action-  idNumber length more than 4 character not allowed or regex pattern not matched");
	        	//throw new RuntimeException("Register Individual action-  idNumber length more than 4 character not allowed or regex pattern not matched");
	        	SessionErrors.add(actionRequest, "nric-fail");
	        }
	        if (fullName != null && fullName != "" && fullName.length() >0) {
	        	
	        }else {
	        	isValidationPassed = false;
	        	SessionErrors.add(actionRequest, "fullname-fail");
	        }
	        String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	        if (email !=null && email !="" && email.matches(EMAIL_REGEX)) {
	        	log.info("Register Individual action-  valid email pattern");
	        }else {
	        	isValidationPassed = false;
	        	log.error("Register Individual action-  email regex pattern not matched");
	        	SessionErrors.add(actionRequest, "email-fail");
	        }
	        String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{12,}$";
	        if (password1 !=null && password1 !="" && password1.matches(PASSWORD_REGEX)
	        		&& password2 !=null && password2 !="" && password2.matches(PASSWORD_REGEX)) {
	        	log.info("Register Individual action-  valid password pattern");
	        	if(!password1.equals(password2)) {
	        		isValidationPassed = false;
		        	log.error("Register Individual action-  confirm password not matched");
		        	SessionErrors.add(actionRequest, "password-confirmation-fail");
	        	}
	        }else
	        {
	        	isValidationPassed = false;
	        	log.error("Register Individual action-  password regex pattern not matched");
	        	SessionErrors.add(actionRequest, "password-fail");
	        }
	       
	        
	        if(dob !=null && dob != "") {
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		        LocalDate date = LocalDate.parse(dob, formatter);
		        LocalDate todayDate = LocalDate.now();
	            if (date.isAfter(todayDate)) {
	            	isValidationPassed = false;
	            	log.error("Register Individual action:::::::: -  Date of birth cannot be in the future");
	            	SessionErrors.add(actionRequest, "dob-fail");
	            }
	        }else {
	        	SessionErrors.add(actionRequest, "dob-null");
	        	isValidationPassed = false;
	        	log.error("Register Individual action:::::::: -  Date of birth cannot be null");
	        	
	        }

	        if(isValidationPassed) {
	        	this.registerUser(actionRequest, themeDisplay, person, password1, password2);
				SessionMessages.add(actionRequest, "success-sign-up");
				RedirectUtil.redirectByUrl(actionRequest, actionResponse, RegisterPortletKeys.OTP);
	        }
			
		}catch (Exception e) {
			log.error("Error while add Register Individual : " + e.getMessage(), e);
		}
		log.info("Register Individual action - end");
	}
	
	private void registerUser(ActionRequest actionRequest, ThemeDisplay themeDisplay, PersonDto person, String pass1, String pass2) {
		try {
			PortletSession portletSession = actionRequest.getPortletSession();
			String fullNames[] = person.getFullName().split(" ");
			String firstName = fullNames[0];
			String lastName = "";
			if(fullNames.length > 1) {
				for(int i = 1; i < fullNames.length; i++) {
					if(i == 1) {
						lastName += fullNames[i];
					} else {
						lastName += " "+fullNames[i];
					}
				}
			} else {
				lastName = firstName;
			}
			Role eshopRole = RoleLocalServiceUtil.fetchRole(themeDisplay.getCompanyId(), RegisterPortletKeys.ESHOP_ROLE);
			Role individualRole = RoleLocalServiceUtil.fetchRole(themeDisplay.getCompanyId(), RegisterPortletKeys.ESHOP_INDIVIDUAL_ROLE);
			
			String dobs[] = person.getDob().split("-");
			
			long creatorUserId = 0L;
			boolean autoPassword = false;
			String password1 = pass1;
			String password2 = pass2;
			boolean autoScreenName = false;
			String screenName = person.getEmail().replace("@", "-");
			String emailAddress = person.getEmail();
			String languageId = themeDisplay.getLanguageId();
			String middleName = null;
			long prefixId = 0L;
			long suffixId = 0L;
			boolean male = true;
			int birthdayMonth = Integer.parseInt(dobs[1])-1;
			int birthdayDay = Integer.parseInt(dobs[2]);
			int birthdayYear = Integer.parseInt(dobs[0]);
			String jobTitle = null;
			long[] groupIds = null;
			long[] organizationIds = null;
			long[] roleIds = new long[2];
			roleIds[0] = eshopRole.getRoleId();
			roleIds[1] = individualRole.getRoleId();
			long[] userGroupIds = null;
			boolean sendEmail = true;
			int type = CommerceAccountConstants.ACCOUNT_TYPE_PERSONAL;
			
//			ServiceContext serviceContext = ServiceContextFactory.getInstance(
//					User.class.getName(), actionRequest);
//			User user = UserLocalServiceUtil.addUserWithWorkflow(creatorUserId, themeDisplay.getCompanyId(), autoPassword, password1, password2, autoScreenName, screenName, emailAddress, LocaleUtil.fromLanguageId(languageId), firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);
			
//			RegisterAccount.add(actionRequest, user,person.getFullName(), emailAddress,type,person.getNric(),null, null, null);
			
			UserOtpDto userOtpDto = new UserOtpDto(creatorUserId, autoPassword, password1, password2, autoScreenName, screenName, emailAddress, languageId, person.getFullName(), firstName, lastName, middleName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds, roleIds, userGroupIds, sendEmail, type, person.getNric(), "", "","" );
//			User user = UserLocalServiceUtil.addUserWithWorkflow(userOtpDto.getCreatorUserId(), themeDisplay.getCompanyId(), userOtpDto.isAutoPassword(), userOtpDto.getPassword1(), userOtpDto.getPassword2(), userOtpDto.isAutoScreenName(), userOtpDto.getScreenName(), userOtpDto.getEmailAddress(), LocaleUtil.fromLanguageId(userOtpDto.getLanguageId()), userOtpDto.getFirstName(), userOtpDto.getLastName(), userOtpDto.getMiddleName(), userOtpDto.getPrefixId(), userOtpDto.getSuffixId(), userOtpDto.isMale(), userOtpDto.getBirthdayMonth(), userOtpDto.getBirthdayDay(), userOtpDto.getBirthdayYear(), userOtpDto.getJobTitle(), userOtpDto.getGroupIds(), userOtpDto.getOrganizationIds(), userOtpDto.getRoleIds(), userOtpDto.getUserGroupIds(), userOtpDto.isSendEmail(), serviceContext);
//			log.info("after create user");
			
			User user = UserLocalServiceUtil.getDefaultUser(themeDisplay.getCompanyId());
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
					User.class.getName(), actionRequest);
			PortletPreferences portletPreferences = actionRequest.getPreferences();
//			String languageId = LanguageUtil.getLanguageId(actionRequest);
			Otp otp = OtpUtil.addOtp(user, userOtpDto, serviceContext,portletPreferences,languageId);
//			String obj = JSONFactoryUtil.serialize(userOtpDto);
//			log.info("obj = "+obj);
			portletSession.setAttribute("userOtpDto", userOtpDto, PortletSession.APPLICATION_SCOPE);
			portletSession.setAttribute("otpId", otp.getOtpId(), PortletSession.APPLICATION_SCOPE);
			
		}catch (PortalException e) {
			log.info("error while adding user : "+e.getMessage());
		}
	}
}
