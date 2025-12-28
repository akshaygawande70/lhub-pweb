package web.ntuc.nlh.parameter.resources;

import java.util.List;
import java.util.Locale;

import javax.portlet.ActionURL;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.dao.orm.Order;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.DateUtil;
import svc.ntuc.nlh.parameter.exception.ParameterValidationException;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.nlh.parameter.config.ParameterConfig;
import web.ntuc.nlh.parameter.constants.MVCCommandNames;
import web.ntuc.nlh.parameter.constants.ParameterMessagesKey;
import web.ntuc.nlh.parameter.constants.ParameterPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.PARAMETER_DATA_RESOURCES,
		"javax.portlet.name=" + ParameterPortletKeys.PARAMETER_PORTLET }, service = MVCResourceCommand.class)
public class ParameterDataResource implements MVCResourceCommand {

	private static Log log = LogFactoryUtil.getLog(ParameterDataResource.class);

	static String parameterGroupIdParam = "parameterGroupId";
	static String parameterIdParam = "parameterId";
	static String parameterValueParam = "paramValue";

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("parameter data resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			Locale locale = themeDisplay.getLocale();
			String portletName = (String) resourceRequest.getAttribute(WebKeys.PORTLET_ID);

			boolean isAuthorized = ParamUtil.getBoolean(resourceRequest, "isAuthorized");
			boolean validCSRF = ParamUtil.getBoolean(resourceRequest, "validCSRF");
			boolean xssPass = ParamUtil.getBoolean(resourceRequest, "xssPass");

			if (!isAuthorized || !validCSRF) {
				String msg = "isAuthorized : " + isAuthorized + " | validCSRF : " + validCSRF;
				throw new ParameterValidationException(msg);
			}

			if (!xssPass) {
				PortletConfig portletConfig = (PortletConfig) resourceRequest
						.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
				String msg = LanguageUtil.format(portletConfig.getResourceBundle(locale),
						ParameterMessagesKey.XSS_VALIDATION_NOT_PASS, xssPass);
				throw new ParameterValidationException(msg);
			}

			HttpServletRequest httpRequest = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
			Integer iDisplayStart = ParamUtil.getInteger(httpRequest, "iDisplayStart");
			Integer iDisplayLength = ParamUtil.getInteger(httpRequest, "iDisplayLength");
			if (iDisplayLength == 0) {
				iDisplayLength = 10;
			}
			Integer iSortColumnIndex = ParamUtil.getInteger(httpRequest, "iSortCol_0");
			String sSortDirection = ParamUtil.getString(httpRequest, "sSortDir_0");
			String sEcho = ParamUtil.getString(httpRequest, "sEcho");

			String paramName = ParamUtil.getString(httpRequest, "sSearch");

			Order order = null;
			boolean ascending = sSortDirection.equals("asc") ? true : false;
			String orderByColumn = this.getOrderByColumn(iSortColumnIndex);
			if (ascending) {
				order = OrderFactoryUtil.asc(orderByColumn);
			} else {
				order = OrderFactoryUtil.desc(orderByColumn);
			}

			int start = iDisplayStart;
			int end = start + iDisplayLength;

			String groupId = resourceRequest.getPreferences().getValue(ParameterConfig.GROUP_ID, "0");
			log.info("groupId From Portlet Preferences : " + groupId);

			List<Parameter> parameters = ParameterLocalServiceUtil.getParameters(start, end, order, paramName,
					Long.valueOf(groupId));
			log.info("Data size : " + parameters.size());
			int allCount = ParameterLocalServiceUtil.countData(Long.valueOf(groupId), false);
			int countAfterFilter = ParameterLocalServiceUtil
					.getParameters(QueryUtil.ALL_POS, QueryUtil.ALL_POS, order, paramName, Long.valueOf(groupId))
					.size();

			String authToken = CSRFValidationUtil.authToken(resourceRequest);
			JSONArray parameterJsonArray = JSONFactoryUtil.createJSONArray();
			for (Parameter param : parameters) {
				JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();
				long parameterGroupId = param.getParameterGroupId();
				ParameterGroup pg = ParameterGroupLocalServiceUtil.getParameterGroup(parameterGroupId);

				jsonBranch.put("groupName", pg.getGroupName());
				jsonBranch.put(parameterIdParam, param.getParameterId());
				jsonBranch.put("paramCode", param.getParamCode());
				jsonBranch.put("paramName", param.getParamName());
				if (param.getParamName().toLowerCase().contains("password")) {
					jsonBranch.put(parameterValueParam, "********");
				} else {
					jsonBranch.put(parameterValueParam, param.getParamValue());
				}
				DateUtil dateUtil = new DateUtil();
				jsonBranch.put("createDate", dateUtil.toString(param.getCreatedDate()));
				jsonBranch.put("modifiedDate", dateUtil.toString(param.getModifiedDate()));

				PortletURL editUrl = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(resourceRequest),
						portletName, themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
				editUrl.getRenderParameters().setValue("mvcRenderCommandName", MVCCommandNames.UPDATE_PARAMETER_RENDER);
				editUrl.getRenderParameters().setValue(parameterGroupIdParam, String.valueOf(pg.getParameterGroupId()));
				editUrl.getRenderParameters().setValue(parameterIdParam, String.valueOf(param.getParameterId()));
				editUrl.getRenderParameters().setValue("authToken", authToken);
				jsonBranch.put("editUrl", editUrl);

				ActionURL deleteUrl = resourceResponse.createActionURL();
				deleteUrl.getActionParameters().setValue("javax.portlet.action",
						MVCCommandNames.DELETE_PARAMETER_ACTION);
				deleteUrl.getActionParameters().setValue(parameterGroupIdParam, String.valueOf(pg.getParameterGroupId()));
				deleteUrl.getActionParameters().setValue(parameterIdParam, String.valueOf(param.getParameterId()));
				deleteUrl.getActionParameters().setValue("authToken", authToken);
				deleteUrl.getActionParameters().setValue("tabParamGroup", "false");
				deleteUrl.getActionParameters().setValue("tabParam", "true");
				jsonBranch.put("deleteUrl", deleteUrl);

				parameterJsonArray.put(jsonBranch);
			}

			JSONObject tableData = JSONFactoryUtil.createJSONObject();
			tableData.put("iTotalRecords", allCount);
			tableData.put("iTotalDisplayRecords", countAfterFilter);
			tableData.put("sEcho", Integer.parseInt(sEcho));
			tableData.put("aaData", parameterJsonArray);
			resourceResponse.getWriter().println(tableData.toString());
		} catch (Exception e) {
			log.error("Error while searching parameter data : " + e.getMessage());
			return true;
		}
		log.info("parameter data resources - end");
		return false;
	}

	private String getOrderByColumn(Integer iSortColumnIndex) {
		String orderBy = "";
		if (iSortColumnIndex == 0) {
			orderBy = "parameterGroupId";
		} else if (iSortColumnIndex == 1) {
			orderBy = "paramCode";
		} else if (iSortColumnIndex == 2) {
			orderBy = "paramName";
		} else if (iSortColumnIndex == 3) {
			orderBy = "paramValue";
		} else if (iSortColumnIndex == 4) {
			orderBy = "createDate";
		} else if (iSortColumnIndex == 5) {
			orderBy = "modifiedDate";
		}
		return orderBy;
	}

}
