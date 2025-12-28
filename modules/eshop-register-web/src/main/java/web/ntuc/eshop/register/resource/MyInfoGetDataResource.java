package web.ntuc.eshop.register.resource;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.HttpApiUtil;
import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.register.constants.MVCCommandNames;
import web.ntuc.eshop.register.constants.RegisterPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.GET_DATA_RESOURCE,
		"javax.portlet.name=" + RegisterPortletKeys.REGISTER }, service = MVCResourceCommand.class)
public class MyInfoGetDataResource implements MVCResourceCommand {
	private static Log log = LogFactoryUtil.getLog(MyInfoGetDataResource.class);

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("get data resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);
			String authorizationCode = ParamUtil.getString(resourceRequest, "authorizationCode");
			String clientId = ParamUtil.getString(resourceRequest, "clientId");
			String redirectUri = ParamUtil.getString(resourceRequest, "redirectUri");
			String state = ParamUtil.getString(resourceRequest, "state");
			
			
			
			ParameterGroup parameterAuthGroup = ParameterGroupLocalServiceUtil
					.getByCode(RegisterPortletKeys.PARAMETER_MY_INFO_GROUP_CODE, false);
			long siteGroupId = parameterAuthGroup.getGroupId();
			Parameter parameterApiPerson= ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_API_PERSON_CODE, false);
			Parameter parameterApiClientId= ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_MY_INFO_API_CLIENT_ID_CODE, false);
			Parameter parameterApiClientSecret= ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_MY_INFO_API_CLIENT_SECRET_CODE, false);
			Parameter parameterChannel= ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_CHANNEL_CODE, false);
			Parameter parameterTrxId= ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_TRANSACTION_ID_CODE, false);
			
			Map<String, String> headers = new HashMap<>();
			headers.put("client_id", parameterApiClientId.getParamValue());
			headers.put("client_secret", parameterApiClientSecret.getParamValue());
			headers.put("ch", parameterChannel.getParamValue());
			headers.put("trxId", parameterTrxId.getParamValue());
			
			String finalUrl = parameterApiPerson.getParamValue();
			finalUrl += "?authorizationCode="+authorizationCode;
			finalUrl += "&clientId="+clientId;
			finalUrl += "&redirectUri="+redirectUri;
			finalUrl += "&state="+state;
			log.info("final url = "+finalUrl);
			
			Object myInfoResponse = HttpApiUtil.httpGet(finalUrl, headers);
			log.info(myInfoResponse);
			PrintWriter printWriter = resourceResponse.getWriter();
			JSONObject res = JSONFactoryUtil.createJSONObject(myInfoResponse.toString());
			printWriter.print(res.toString());
			printWriter.flush();
			
		}catch (Exception e) {
			log.info("Error while getting data : "+ e.getMessage());
			return true;
		}
		log.info("get data resources - end");
		return false;
	}
	
	
}
