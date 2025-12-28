package web.ntuc.nlh.assetfilter.portlet;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.CSRFValidationUtil;
import web.ntuc.nlh.assetfilter.config.AssetFilterConfig;
import web.ntuc.nlh.assetfilter.constants.AssetFilterPortletKeys;

/**
 * @author muhamadpangestu
 */
@Component(immediate = true, property = { "com.liferay.portlet.display-category=ntuc-module",
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=AssetFilter", "javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + AssetFilterPortletKeys.ASSETFILTER_PORTLET,
		"javax.portlet.resource-bundle=content.Language", "javax.portlet.version=3.0",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class AssetFilterPortlet extends MVCPortlet {

	private static Log log = LogFactoryUtil.getLog(AssetFilterPortlet.class);

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		log.info("Topics Asset Filter Portlet render - start");
		try {

			String authToken = CSRFValidationUtil.authToken(renderRequest);
			renderRequest.setAttribute("authToken", authToken);

			// Get id topic from configuration
			Long topic = Long.valueOf(renderRequest.getPreferences().getValue(AssetFilterConfig.TOPICS, "0"));
			Long subTopic = Long.valueOf(renderRequest.getPreferences().getValue(AssetFilterConfig.SUBTOPICS, "0"));
			String topicTitle = String.valueOf(renderRequest.getPreferences().getValue(AssetFilterConfig.TOPICTITLE, ""));
			
			String topicId = String.valueOf(topic);
			String subTopicId = String.valueOf(subTopic);

			List<Integer> filterYears = new ArrayList<>();

//			Generate Years
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int lastFiveYear = year - 4;
			for (int i = year; i >= lastFiveYear; i--) {
				filterYears.add(i);
			}

//			Generate Topic Name
			String dataTopicName = "";
			String dataSubTopicName = "";
			if (!Validator.isBlank(topicId)) {
				AssetCategory topicAsset = assetCategoryLocalService.getAssetCategory(Long.valueOf(topicId));
				dataTopicName = topicAsset.getName();
			}

			if (!Validator.isBlank(subTopicId)) {
				AssetCategory subTopicAsset = assetCategoryLocalService.getAssetCategory(Long.valueOf(subTopicId));
				dataSubTopicName = subTopicAsset.getName();
			}
			
			String subTopicName = topicTitle;
			
			log.info("Fix Title : " + subTopicName);

			renderRequest.setAttribute("topicTitle", subTopicName);
			renderRequest.setAttribute("topicId", topicId);
			renderRequest.setAttribute("subTopicId", subTopicId);
			renderRequest.setAttribute("years", filterYears);
		} catch (Exception e) {
			log.error("Failed when render topics, error:" + e.getMessage());
		}
		log.info("Topics Asset Filter Portlet render - end");
		super.render(renderRequest, renderResponse);
	}

	private String capitalize(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

	@Reference
	AssetCategoryLocalService assetCategoryLocalService;
}