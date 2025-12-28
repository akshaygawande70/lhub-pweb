package web.ntuc.eshop.register.resource;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.PrintWriter;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.register.constants.MVCCommandNames;
import web.ntuc.eshop.register.constants.RegisterPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.GET_ENV_RESOURCE,
		"javax.portlet.name=" + RegisterPortletKeys.REGISTER }, service = MVCResourceCommand.class)
public class MyInfoEnvirontmentSetup implements MVCResourceCommand{
	private static Log log = LogFactoryUtil.getLog(MyInfoEnvirontmentSetup.class);
	
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("get env resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);
			
			ParameterGroup parameterAuthGroup = ParameterGroupLocalServiceUtil
					.getByCode(RegisterPortletKeys.PARAMETER_MY_INFO_GROUP_CODE, false);
			long siteGroupId = parameterAuthGroup.getGroupId();
			Parameter parameterRedirectUrl= ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_REDIRECT_URL_CODE, false);
			Parameter parameterAttributes= ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_API_ATTRIBUTES_CODE, false);
			Parameter parameterAuthLevel= ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_AUTH_LEVEL_CODE, false);
			Parameter parameterApiUrl= ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_API_AUTHORISE_CODE, false);
			Parameter parameterClientId= ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_MY_INFO_CLIENT_ID_CODE, false);
			Parameter parameterState= ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_STATE_CODE, false);
			Parameter parameterPurpose= ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_PURPOSE_CODE, false);
			
			PrintWriter printWriter = resourceResponse.getWriter();
			JSONObject env = JSONFactoryUtil.createJSONObject();
			env.put("attributes", parameterAttributes.getParamValue());
			env.put("authApiUrl", parameterApiUrl.getParamValue());
			env.put("authLevel", parameterAuthLevel.getParamValue());
			env.put("clientId", parameterClientId.getParamValue());
			env.put("redirectUrl", parameterRedirectUrl.getParamValue());
			env.put("state", parameterState.getParamValue());
			env.put("purpose", parameterPurpose.getParamValue());
			env.put("status", "OK");
			printWriter.print(env.toString());
			printWriter.flush();
		}catch (Exception e) {
			log.error("Error while getting environment : " + e.getMessage());
			return true;
		}
		log.info("get env resources - end");
		return false;
	}
	
}
