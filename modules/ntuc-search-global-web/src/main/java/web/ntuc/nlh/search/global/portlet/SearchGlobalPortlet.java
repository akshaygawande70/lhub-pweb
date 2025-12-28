package web.ntuc.nlh.search.global.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import java.io.IOException;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.osgi.service.component.annotations.Component;

import api.ntuc.common.constants.PortletDisplayCategoryConstant;
import api.ntuc.common.util.CSRFValidationUtil;
import web.ntuc.nlh.search.global.constants.SearchGlobalPortletKeys;

/**
 * @author skitukale
 */
@Component(immediate = true, property = {
		"com.liferay.portlet.display-category=" + PortletDisplayCategoryConstant.ADMIN,
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=false",
		"javax.portlet.init-param.template-path=/", "javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + SearchGlobalPortletKeys.SEARCH_GLOBAL_PORTLET,
		"javax.portlet.display-name=" + SearchGlobalPortletKeys.SEARCH_GLOBAL_DISPLAY, "javax.portlet.version=3.0",
		"javax.portlet.init-param.add-process-action-success-action=false",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class SearchGlobalPortlet extends MVCPortlet {
	private static Log log = LogFactoryUtil.getLog(SearchGlobalPortlet.class);

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		log.info("Search Global portlet render - start");
		try {
			String authToken = CSRFValidationUtil.authToken(renderRequest);
			renderRequest.setAttribute("authToken", authToken);
			renderRequest.setAttribute("keyword", "");
		} catch (Exception e) {
			log.error("Failed when render Search, error: " + e.getMessage());
		}
		log.info("Search Global portlet render - end");
		super.render(renderRequest, renderResponse);
	}

}