package web.ntuc.nlh.parameter.action;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.osgi.service.component.annotations.Component;

import svc.ntuc.nlh.parameter.exception.ParameterValidationException;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import web.ntuc.nlh.parameter.config.ParameterConfig;
import web.ntuc.nlh.parameter.constants.MVCCommandNames;
import web.ntuc.nlh.parameter.constants.ParameterMessagesKey;
import web.ntuc.nlh.parameter.constants.ParameterPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.UPDATE_GROUP_ACTION,
		"javax.portlet.name=" + ParameterPortletKeys.PARAMETER_PORTLET }, service = MVCActionCommand.class)
public class UpdateGroupAction extends BaseMVCActionCommand {

	private static Log log = LogFactoryUtil.getLog(UpdateGroupAction.class);

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Update Parameter Group action - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			User user = themeDisplay.getUser();
			long scopeGroupId = themeDisplay.getScopeGroupId();
			long companyId = themeDisplay.getCompanyId();
			long groupId = Long.parseLong(actionRequest.getPreferences().getValue(ParameterConfig.GROUP_ID, "0")); 
boolean isAuthorized = ParamUtil.getBoolean(actionRequest, "isAuthorized");
			boolean validCSRF = ParamUtil.getBoolean(actionRequest, "validCSRF");
			boolean xssPass = ParamUtil.getBoolean(actionRequest, "xssPass");

			if (!isAuthorized || !validCSRF) {
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

			long parameterGroupId = ParamUtil.getLong(actionRequest, "parameterGroupId");
			long parentId = ParamUtil.getLong(actionRequest, "parentId");
			String groupName = ParamUtil.getString(actionRequest, "groupName");
			String groupCode = ParamUtil.getString(actionRequest, "groupCode");
			String description = ParamUtil.getString(actionRequest, "description");

			ParameterGroup pg = null;
			if (parameterGroupId > 0) {
				pg = ParameterGroupLocalServiceUtil.getParameterGroup(parameterGroupId);
			} else {
				List<ParameterGroup> pgExist = null;
						try {
							pgExist = ParameterGroupLocalServiceUtil.getByGroupIdCode(groupId, groupCode,false);
						} catch (Exception e) {
							log.info("no parameter grup exist with code "+ groupCode);
						}
						
					if (pgExist != null && Validator.isNotNull(pgExist) && !pgExist.isEmpty()){
						PortletConfig portletConfig = (PortletConfig) actionRequest
								.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
						String msg = LanguageUtil.format(portletConfig.getResourceBundle(themeDisplay.getLocale()),
								ParameterMessagesKey.PARAMETER_GROUP_EXIST, groupCode);
						throw new ParameterValidationException(msg);
					}

				parameterGroupId = CounterLocalServiceUtil.increment(ParameterGroup.class.getName());
				pg = ParameterGroupLocalServiceUtil.createParameterGroup(groupId, parameterGroupId, parentId, groupCode, groupName, description, themeDisplay);

			
			}
			
			pg = ParameterGroupLocalServiceUtil.updateParameterGroup(groupId, parameterGroupId, parentId, groupCode, groupName, description, themeDisplay);

			SessionMessages.add(actionRequest, ParameterMessagesKey.SUCCESS_SAVE_GROUP);
		} catch (Exception e) {
			log.error("Error while save parameter group: " + e.getMessage(), e);
			SessionErrors.add(actionRequest, ParameterMessagesKey.FAILED_SAVE_GROUP);
		}
		log.info("Update Parameter Group action - end");
	}

}
