package web.ntuc.nlh.parameter.action;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.osgi.service.component.annotations.Component;

import svc.ntuc.nlh.parameter.exception.ParameterValidationException;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.nlh.parameter.constants.MVCCommandNames;
import web.ntuc.nlh.parameter.constants.ParameterMessagesKey;
import web.ntuc.nlh.parameter.constants.ParameterPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.DELETE_PARAMETER_ACTION,
		"javax.portlet.name=" + ParameterPortletKeys.PARAMETER_PORTLET }, service = MVCActionCommand.class)
public class DeleteParameterAction extends BaseMVCActionCommand {

	private static Log log = LogFactoryUtil.getLog(DeleteParameterAction.class);

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Delete Parameter action - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			User user = themeDisplay.getUser();
			boolean isAuthorized = ParamUtil.getBoolean(actionRequest, "isAuthorized");
			boolean validCSRF = ParamUtil.getBoolean(actionRequest, "validCSRF");
			boolean xssPass = ParamUtil.getBoolean(actionRequest, "xssPass");

			if (!(isAuthorized && validCSRF)) {
				log.error("parameter group delete action - isAuthorized : " + isAuthorized + " | validCSRF : "
						+ validCSRF);
				String msg = "isAuthorized : " + isAuthorized + " | validCSRF : " + validCSRF;
				SessionErrors.add(actionRequest, "you-dont-have-permission-or-your-session-is-end");
				throw new ParameterValidationException(msg);
			}

			if (!xssPass) {
				PortletConfig portletConfig = (PortletConfig) actionRequest
						.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
				String msg = LanguageUtil.format(portletConfig.getResourceBundle(themeDisplay.getLocale()),
						ParameterMessagesKey.XSS_VALIDATION_NOT_PASS, xssPass);
				SessionErrors.add(actionRequest, "your-input-not-pass-xss");
				throw new ParameterValidationException(msg);
			}

			long parameterId = ParamUtil.getLong(actionRequest, "parameterId");
			
			Parameter p = ParameterLocalServiceUtil.getParameter(parameterId);
			
			p.setModifiedBy(user.getUserId());
			p.setModifiedDate(new Date());

			p.setDeleted(true);

			p = ParameterLocalServiceUtil.updateParameter(p);
			
			SessionMessages.add(actionRequest, ParameterMessagesKey.SUCCESS_DELETE_GROUP);
		} catch (Exception e) {
			log.error("Error deleting parameter : " + e.getMessage());
			SessionErrors.add(actionRequest, ParameterMessagesKey.FAILED_DELETE_PARAMETER);
		}
		log.info("Delete Parameter action - end");
	}

}
