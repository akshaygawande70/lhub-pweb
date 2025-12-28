package web.ntuc.nlh.parameter.portlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.WebKeys;

import api.ntuc.common.constants.PortletDisplayCategoryConstant;
import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.PermissionUtil;
import web.ntuc.nlh.parameter.constants.ParameterPortletKeys;
import web.ntuc.nlh.parameter.constants.PermissionConstant;

/**
 * @author fazarnugroho
 */
@Component(immediate = true, property = {
		"com.liferay.portlet.display-category=" + PortletDisplayCategoryConstant.ADMIN,
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.init-param.template-path=/", "javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + ParameterPortletKeys.PARAMETER_PORTLET,
		"javax.portlet.display-name=" + ParameterPortletKeys.PARAMETER_DISPLAY,
		"javax.portlet.resource-bundle=content.Language", "javax.portlet.version=3.0",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class ParameterPortlet extends MVCPortlet {

	private static Log log = LogFactoryUtil.getLog(ParameterPortlet.class);

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		log.info("Parameter portlet render - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

			renderPermission(themeDisplay, renderRequest, renderResponse);

			String authToken = CSRFValidationUtil.authToken(renderRequest);
			renderRequest.setAttribute("authToken", authToken);
			
//			String awsKey = PropsUtil.get("ntuc.aws.key");
//			log.info("aws key = "+awsKey);
			
//			LIFERAY_NTUC_PERIOD_AWS_PERIOD_KEY
//			System.getenv().forEach((k, v) -> {
//				log.info(k + ":" + v);
//			});
			
//			String prop = System.getProperty("LIFERAY_NTUC_PERIOD_AWS_PERIOD_KEY");
//			log.info("prod = "+prop);
			
			renderRequest.setAttribute("tabParamGroup", ParamUtil.getBoolean(renderRequest, "tabParamGroup", true));
			renderRequest.setAttribute("tabParam", ParamUtil.getBoolean(renderRequest, "tabParam", false));
		} catch (Exception e) {
			log.error("Failed when render Parameter, error:" + e.getMessage());
		}
		log.info("Parameter portlet render - end");
		super.render(renderRequest, renderResponse);
	}

	private void renderPermission(ThemeDisplay themeDisplay, RenderRequest renderRequest,
			RenderResponse renderResponse) {
		PermissionChecker permissionChecker = themeDisplay.getPermissionChecker();
		long scopeGroupId = themeDisplay.getScopeGroupId();

		Map<String, String> permissionMap = new HashMap<>();
		permissionMap.put(PermissionConstant.ROLE_ADD_GROUP, PermissionConstant.ADD_GROUP);
		permissionMap.put(PermissionConstant.ROLE_EDIT_GROUP, PermissionConstant.EDIT_GROUP);
		permissionMap.put(PermissionConstant.ROLE_DELETE_GROUP, PermissionConstant.DELETE_GROUP);
		permissionMap.put(PermissionConstant.ROLE_ADD_PARAMETER, PermissionConstant.ADD_PARAMETER);
		permissionMap.put(PermissionConstant.ROLE_EDIT_PARAMETER, PermissionConstant.EDIT_PARAMETER);
		permissionMap.put(PermissionConstant.ROLE_DELETE_PARAMETER, PermissionConstant.DELETE_PARAMETER);
		permissionMap.put(PermissionConstant.ROLE_VIEW_PARAMETER, PermissionConstant.VIEW_PARAMETER);

		boolean isAdministrator = renderRequest.isUserInRole(PermissionConstant.ROLE_ADMINISTRATOR);
		renderRequest.setAttribute(PermissionConstant.IS_ADMINISTRATOR, isAdministrator);

		boolean permitted = isAdministrator;
		for (Map.Entry<String, String> entry : permissionMap.entrySet()) {
			boolean permit = PermissionUtil.contains(permissionChecker, scopeGroupId, entry.getKey(),
					PermissionConstant.PERMISSION_RESOURCES_NAME);
			renderRequest.setAttribute(entry.getValue(), permit);
			permitted = permitted || permit;
		}

		if (!permitted) {
			renderRequest.setAttribute(getMVCPathAttributeName(renderResponse.getNamespace()), "/error.jsp");
		}
	}

}