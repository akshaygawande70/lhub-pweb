package web.ntuc.nlh.courses.portlet;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.constants.PortletDisplayCategoryConstant;
import api.ntuc.common.util.CSRFValidationUtil;
import web.ntuc.nlh.courses.config.CoursesConfig;
import web.ntuc.nlh.courses.constants.CoursesPortletKeys;

/**
 * @author muhamadpangestu
 */
@Component(immediate = true, property = { "com.liferay.portlet.display-category="+ PortletDisplayCategoryConstant.CP_NTUC,
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Courses", "javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp", "javax.portlet.name=" + CoursesPortletKeys.COURSES_PORTLET,
		"javax.portlet.resource-bundle=content.Language", "javax.portlet.version=3.0",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class CoursesPortlet extends MVCPortlet {

	private static Log log = LogFactoryUtil.getLog(CoursesPortlet.class);
	List<Long> categoryId = new ArrayList<>();

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		log.info("Courses Portlet render - start");
		try {

			String authToken = CSRFValidationUtil.authToken(renderRequest);
			renderRequest.setAttribute("authToken", authToken);

			Long themeId = Long.valueOf(renderRequest.getPreferences().getValue(CoursesConfig.THEMES, "0"));
			Long topicId = Long.valueOf(renderRequest.getPreferences().getValue(CoursesConfig.TOPICS, "0"));

			String theme = String.valueOf(themeId);
			String topic = String.valueOf(topicId);

			log.info("theme = " + theme + " topic = " + topic);

			String dataThemeName = "";
			String dataTopicName = "";
			if (!Validator.isBlank(theme) && themeId > 0) {
				AssetCategory themeAsset = assetCategoryLocalService.getAssetCategory(Long.valueOf(theme));
				dataThemeName = themeAsset.getName();
			}
			if (!Validator.isBlank(topic) && topicId > 0) {
				AssetCategory topicAsset = assetCategoryLocalService.getAssetCategory(Long.valueOf(topic));
				dataTopicName = topicAsset.getName();
			}

			log.info("dataThemeName = " + dataThemeName + " dataTopicName = " + dataTopicName);
			
			renderRequest.setAttribute("dataTheme", theme);
			renderRequest.setAttribute("dataTopic", topic);
			renderRequest.setAttribute("dataThemeName", dataThemeName);
			renderRequest.setAttribute("dataTopicName", dataTopicName);

		} catch (

		Exception e) {
			log.error("Failed when render Courses, error:" + e.getMessage());
		}
		log.info("Courses portlet render - end");
		super.render(renderRequest, renderResponse);
	}

	@Reference
	AssetCategoryLocalService assetCategoryLocalService;
}