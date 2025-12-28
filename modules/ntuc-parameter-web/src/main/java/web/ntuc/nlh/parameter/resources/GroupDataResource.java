package web.ntuc.nlh.parameter.resources;

import java.util.List;

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
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import web.ntuc.nlh.parameter.config.ParameterConfig;
import web.ntuc.nlh.parameter.constants.MVCCommandNames;
import web.ntuc.nlh.parameter.constants.ParameterMessagesKey;
import web.ntuc.nlh.parameter.constants.ParameterPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.GROUP_DATA_RESOURCES,
		"javax.portlet.name=" + ParameterPortletKeys.PARAMETER_PORTLET }, service = MVCResourceCommand.class)
public class GroupDataResource implements MVCResourceCommand {

	private static Log log = LogFactoryUtil.getLog(GroupDataResource.class);

	static String parameterGroupIdParam = "parameterGroupId";
	
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("parameter group data resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
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
				String msg = LanguageUtil.format(portletConfig.getResourceBundle(themeDisplay.getLocale()),
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

			String groupNameSearch = ParamUtil.getString(httpRequest, "sSearch");

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

			List<ParameterGroup> parameterGroups = ParameterGroupLocalServiceUtil.getParameterGroups(start, end, order,
					groupNameSearch, Long.valueOf(groupId));
			log.info("Data size : " + parameterGroups.size());
			int allCount = ParameterGroupLocalServiceUtil.countData(Long.valueOf(groupId), false);
			int countAfterFilter = ParameterGroupLocalServiceUtil.getParameterGroups(QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, order, groupNameSearch, Long.valueOf(groupId)).size();

			String authToken = CSRFValidationUtil.authToken(resourceRequest);
			JSONArray groupJsonArray = JSONFactoryUtil.createJSONArray();
			for (ParameterGroup pg : parameterGroups) {
				JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();
				jsonBranch.put(parameterGroupIdParam, pg.getParameterGroupId());
				jsonBranch.put("groupName", pg.getGroupName());
				jsonBranch.put("groupCode", pg.getGroupCode());
				DateUtil dateUtil = new DateUtil();
				jsonBranch.put("createDate", dateUtil.toString(pg.getCreatedDate()));
				jsonBranch.put("modifiedDate", dateUtil.toString(pg.getModifiedDate()));

				PortletURL editUrl = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(resourceRequest),
						portletName, themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
				editUrl.getRenderParameters().setValue("mvcRenderCommandName", MVCCommandNames.UPDATE_GROUP_RENDER);
				editUrl.getRenderParameters().setValue(parameterGroupIdParam, String.valueOf(pg.getParameterGroupId()));
				editUrl.getRenderParameters().setValue("authToken", authToken);
				jsonBranch.put("editUrl", editUrl);

				ActionURL deleteUrl = resourceResponse.createActionURL();
				deleteUrl.getActionParameters().setValue("javax.portlet.action", MVCCommandNames.DELETE_GROUP_ACTION);
				deleteUrl.getActionParameters().setValue(parameterGroupIdParam, String.valueOf(pg.getParameterGroupId()));
				deleteUrl.getActionParameters().setValue("authToken", authToken);
				jsonBranch.put("deleteUrl", deleteUrl);

				groupJsonArray.put(jsonBranch);
			}

			JSONObject tableData = JSONFactoryUtil.createJSONObject();
			tableData.put("iTotalRecords", allCount);
			tableData.put("iTotalDisplayRecords", countAfterFilter);
			tableData.put("sEcho", Integer.parseInt(sEcho));
			tableData.put("aaData", groupJsonArray);
			resourceResponse.getWriter().println(tableData.toString());
		} catch (Exception e) {
			log.error("Error while searching parameter group data : " + e.getMessage());
			return true;
		}
		log.info("parameter group data resources - end");
		return false;
	}

	private String getOrderByColumn(Integer iSortColumnIndex) {
		String orderBy = "";
		if (iSortColumnIndex == 0) {
			orderBy = "groupName";
		} else if (iSortColumnIndex == 1) {
			orderBy = "groupCode";
		} else if (iSortColumnIndex == 2) {
			orderBy = "createDate";
		} else if (iSortColumnIndex == 3) {
			orderBy = "modifiedDate";
		}
		return orderBy;
	}
}
