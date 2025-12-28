package web.ntuc.nlh.search.result.portlet;

import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.constants.PortletDisplayCategoryConstant;
import api.ntuc.common.util.CSRFValidationUtil;
import web.ntuc.nlh.search.result.constants.SearchResultPortletKeys;

/**
 * @author fandifadillah
 */
@Component(immediate = true, property = {
		"com.liferay.portlet.display-category=" + PortletDisplayCategoryConstant.ADMIN,
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.init-param.template-path=/", "javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + SearchResultPortletKeys.SEARCH_RESULT_PORTLET,
		"javax.portlet.display-name=" + SearchResultPortletKeys.SEARCH_RESULT_DISPLAY,
		"javax.portlet.resource-bundle=content.Language", "javax.portlet.version=3.0",
		"javax.portlet.init-param.add-process-action-success-action=false",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class SearchResultPortlet extends MVCPortlet {
	private static Log log = LogFactoryUtil.getLog(SearchResultPortlet.class);

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		log.info("Search result portlet render - start");
		try {
			String authToken = CSRFValidationUtil.authToken(renderRequest);
			renderRequest.setAttribute("authToken", authToken);

			HttpServletRequest httpReq = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(renderRequest));
			String keyword = HtmlUtil.escapeXPath(httpReq.getParameter(SearchResultPortletKeys.PARAMETER_KEYWORD));
			String course = HtmlUtil.escapeXPath(httpReq.getParameter(SearchResultPortletKeys.PARAMETER_COURSE));

			renderRequest.setAttribute("keyword", keyword.replace("_", " "));
			renderRequest.setAttribute("dataCourse", course.replace("_", " "));

		} catch (Exception e) {
			log.error("Failed when render Search, error:" + e.getMessage());
		}
		log.info("Search result portlet render - end");
		super.render(renderRequest, renderResponse);
	}

	@Reference
	AssetCategoryLocalService _assetCategoryLocalService;

}