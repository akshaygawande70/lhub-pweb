package web.ntuc.nlh.eventfilter.portlet;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

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
import web.ntuc.nlh.eventfilter.config.EventFilterConfig;
import web.ntuc.nlh.eventfilter.constants.EventFilterPortletKeys;

/**
 * @author muhamadpangestu
 */
@Component(immediate = true, property = { "com.liferay.portlet.display-category=ntuc-module",
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=EventFilter", "javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + EventFilterPortletKeys.EVENTFILTER_PORTLET,
		"javax.portlet.resource-bundle=content.Language", "javax.portlet.version=3.0",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class EventFilterPortlet extends MVCPortlet {

	private static Log log = LogFactoryUtil.getLog(EventFilterPortlet.class);

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		log.info("Event Filter Portlet render - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

			long groupId = themeDisplay.getScopeGroupId();

			String authToken = CSRFValidationUtil.authToken(renderRequest);
			renderRequest.setAttribute("authToken", authToken);

			// Get id type from configuration
			Long eventType = Long.valueOf(renderRequest.getPreferences().getValue(EventFilterConfig.EVENTTYPE, "0"));

			// Get id topic from configuration
			Long topic = Long.valueOf(renderRequest.getPreferences().getValue(EventFilterConfig.TOPICS, "0"));

			String eventTypeId = String.valueOf(eventType);
			String topicId = String.valueOf(topic);
			log.info("eventtype ID : " + eventTypeId + " | topicId : " + topicId);

			List<Integer> filterYears = new ArrayList<>();

//			Generate Years
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int lastFiveYear = year - 4;
			for (int i = year; i >= lastFiveYear; i--) {
				filterYears.add(i);
			}

			// Get Event category from vocabulary
			AssetVocabulary eventVocabulary = assetVocabularyLocalService.getGroupVocabulary(groupId,
					EventFilterPortletKeys.ASSET_VOCAB_EVENT);

			// Get all categories from vocabulary
			List<AssetCategory> eventList = assetCategoryLocalService.getVocabularyCategories(
					eventVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

			renderRequest.setAttribute("events", eventList);
			renderRequest.setAttribute("topicId", topicId);
			renderRequest.setAttribute("eventTypeId", eventTypeId);
			renderRequest.setAttribute("years", filterYears);
		} catch (Exception e) {
			log.error("Failed when render topics, error:" + e.getMessage());
		}
		log.info("Event Filter Portlet render - end");
		super.render(renderRequest, renderResponse);
	}


	@Reference
	protected AssetVocabularyLocalService assetVocabularyLocalService;

	@Reference
	AssetCategoryLocalService assetCategoryLocalService;
}