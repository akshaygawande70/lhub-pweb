package web.ntuc.eshop.myaccount.action;

import com.liferay.portal.kernel.exception.UserPasswordException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.session.AuthenticatedSessionManager;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.myaccount.constants.MVCCommandNames;
import web.ntuc.eshop.myaccount.constants.MyAccountMessagesKeys;
import web.ntuc.eshop.myaccount.constants.MyAccountPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.UPDATE_PASSWORD_ACTION,
		"javax.portlet.name=" + MyAccountPortletKeys.CHANGE_PASSWORD_PORTLET}, service = MVCActionCommand.class)
public class UpdatePasswordAction extends BaseMVCActionCommand {

	Log log = LogFactoryUtil.getLog(UpdatePasswordAction.class);

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Update password - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			PortletCommandUtil.actionAndResourceCommand(actionRequest, themeDisplay);
			String currentPassword = ParamUtil.getString(actionRequest, "current-password", "");
			String password1 = ParamUtil.getString(actionRequest, "password1", "");
			String password2 = ParamUtil.getString(actionRequest, "password2", "");
//			String login = themeDisplay.getUser().getEmailAddress();
			
			User user = themeDisplay.getUser();
			long userId = UserLocalServiceUtil.authenticateForBasic(themeDisplay.getCompanyId(),
					CompanyConstants.AUTH_TYPE_EA, user.getLogin(), currentPassword);
			
			if (themeDisplay.getUserId() != userId) {
				SessionErrors.add(actionRequest, "error-key");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
				SessionErrors.add(actionRequest, MyAccountMessagesKeys.INCORRECT_PASSWORD);
				throw new Exception("Your current password is incorrect the password was not changed");
			} else {
				boolean passwordModified = false;
				boolean passwordReset = false;
				if (Validator.isNotNull(password1) || Validator.isNotNull(password2)) {

					UserLocalServiceUtil.updatePassword(user.getUserId(), password1, password2, passwordReset);

					passwordModified = true;
				}

				if ((user.getUserId() == themeDisplay.getUserId()) && passwordModified) {

					String login = null;

					Company company = themeDisplay.getCompany();

					String authType = company.getAuthType();

					if (authType.equals(CompanyConstants.AUTH_TYPE_EA)) {
						login = user.getEmailAddress();
					} else if (authType.equals(CompanyConstants.AUTH_TYPE_SN)) {
						login = user.getScreenName();
					} else if (authType.equals(CompanyConstants.AUTH_TYPE_ID)) {
						login = String.valueOf(user.getUserId());
					}
					
					_authenticatedSessionManager.login(
							_portal.getOriginalServletRequest(_portal.getHttpServletRequest(actionRequest)),
							_portal.getHttpServletResponse(actionResponse), login, password1, false, null);
				}
				SessionMessages.add(actionRequest, MyAccountMessagesKeys.PASSWORD_UPDATE_SUCCESS);
			}

		} catch (Throwable throwable) {
			if (throwable instanceof UserPasswordException) {
//				SessionErrors.add(
//						actionRequest, throwable.getClass(), throwable);
				SessionErrors.add(actionRequest, MyAccountMessagesKeys.PASSWORD_TOO_OFTEN);
			}
			log.error("Error while update password " + throwable.getMessage());
		}
		log.info("Update password - end");
	}

	@Reference
	private AuthenticatedSessionManager _authenticatedSessionManager;

	@Reference
	private Portal _portal;

}
