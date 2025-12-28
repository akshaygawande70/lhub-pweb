package web.ntuc.nlh.search.portlet;

import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.constants.PortletDisplayCategoryConstant;
import api.ntuc.common.util.CSRFValidationUtil;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalService;
import svc.ntuc.nlh.parameter.service.ParameterLocalService;
import web.ntuc.nlh.search.constants.SearchPortletKeys;

/**
 * @author fandifadillah
 */
@Component(immediate = true, property = {
		"com.liferay.portlet.display-category=" + PortletDisplayCategoryConstant.ADMIN,
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.init-param.template-path=/", "javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + SearchPortletKeys.SEARCH_PORTLET,
		"javax.portlet.display-name=" + SearchPortletKeys.SEARCH_DISPLAY,
		"javax.portlet.resource-bundle=content.Language", "javax.portlet.version=3.0",
		"javax.portlet.init-param.add-process-action-success-action=false",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class SearchPortlet extends MVCPortlet {
	private static Log log = LogFactoryUtil.getLog(SearchPortlet.class);

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
//		log.info("Search portlet render - start");
		try {
			String authToken = CSRFValidationUtil.authToken(renderRequest);
			renderRequest.setAttribute("authToken", authToken);

		} catch (Exception e) {
			log.error("Failed when render Search, error:" + e.getMessage());
		}
//		log.info("Search portlet render - end");
		super.render(renderRequest, renderResponse);
	}

	@Reference
	protected ParameterLocalService parameterLocalService;

	@Reference
	protected ParameterGroupLocalService parameterGroupLocalService;

	@Reference
	protected AssetVocabularyLocalService assetVocabularyLocalService;

	@Reference
	protected AssetCategoryLocalService assetCategoryLocalService;
}