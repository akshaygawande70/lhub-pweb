package web.ntuc.nlh.parameter.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.osgi.service.component.annotations.Component;

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
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import svc.ntuc.nlh.parameter.exception.ParameterValidationException;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.nlh.parameter.config.ParameterConfig;
import web.ntuc.nlh.parameter.constants.MVCCommandNames;
import web.ntuc.nlh.parameter.constants.ParameterMessagesKey;
import web.ntuc.nlh.parameter.constants.ParameterPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.UPDATE_PARAMETER_ACTION,
		"javax.portlet.name=" + ParameterPortletKeys.PARAMETER_PORTLET }, service = MVCActionCommand.class)
public class UpdateParameterAction extends BaseMVCActionCommand {

	private static Log log = LogFactoryUtil.getLog(UpdateParameterAction.class);

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Update Parameter action - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			User user = themeDisplay.getUser();
			long groupId = Long.parseLong(actionRequest.getPreferences().getValue(ParameterConfig.GROUP_ID, "0"));

			long companyId = themeDisplay.getCompanyId();

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

			long paramId = ParamUtil.getLong(actionRequest, "parameterId");
			long paramGroupId = ParamUtil.getLong(actionRequest, "parameterGroupId");
			String paramCode = ParamUtil.getString(actionRequest, "paramCode");
//			Map<Locale, String> paramNameTemp = LocalizationUtil.getLocalizationMap(actionRequest, "paramName");
//			Map<Locale, String> paramName = new HashMap<Locale, String>();
//			paramNameTemp.forEach((a, b) -> paramName.put(a, b));
			String paramName =  ParamUtil.getString(actionRequest, "paramName");
			String paramValue = ParamUtil.getString(actionRequest, "paramValue");
			String descrp = ParamUtil.getString(actionRequest, "description");

			Parameter p = null;
			if (paramId > 0) {
				p = ParameterLocalServiceUtil.getParameter(paramId);
			} else {
				List<Parameter> exist = null;

				try {
					ParameterLocalServiceUtil.getByGroupIdCode(groupId, paramCode, false);
				} catch (Exception e) {
					log.error("no code existi with param code " + paramCode);
				}
				if (Validator.isNotNull(exist)) {
					PortletConfig portletConfig = (PortletConfig) actionRequest
							.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
					String message = LanguageUtil.format(portletConfig.getResourceBundle(themeDisplay.getLocale()),
							ParameterMessagesKey.PARAMETER_EXIST, paramCode);
					throw new ParameterValidationException(message);
				}

				paramId = CounterLocalServiceUtil.increment(Parameter.class.getName());
				p = ParameterLocalServiceUtil.createParameter(paramId);

				p.setGroupId(groupId);
				p.setCompanyId(companyId);
				p.setCreatedBy(user.getUserId());
				p.setCreatedDate(new Date());
			}

			p.setModifiedBy(user.getUserId());
			p.setModifiedDate(new Date());

			p.setParameterGroupId(paramGroupId);
			p.setParamCode(paramCode);
			p.setParamName(paramName);
			p.setParamValue(paramValue);
			p.setDescription(descrp);
			p.setDeleted(false);

			p = ParameterLocalServiceUtil.updateParameter(p);

			SessionMessages.add(actionRequest, ParameterMessagesKey.SUCCESS_SAVE_PARAMETER);
		} catch (Exception e) {
			log.error("Error while save parameter group: " + e.getMessage(), e);
			SessionErrors.add(actionRequest, ParameterMessagesKey.FAILED_SAVE_PARAMETER);
		}
		log.info("Update Parameter action - end");
	}

}
