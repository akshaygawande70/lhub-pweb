package web.ntuc.nlh.eventfilter.config.portlet;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import aQute.bnd.annotation.metatype.Configurable;
import web.ntuc.nlh.eventfilter.config.EventFilterConfig;
import web.ntuc.nlh.eventfilter.config.dto.EventTypeDto;
import web.ntuc.nlh.eventfilter.constants.EventFilterPortletKeys;
import api.ntuc.nlh.content.dto.ConfigListDto;

@Component(configurationPid = "web.nlh.ntuc.eventfilter.config.EventFilterConfig", configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true, property = {
		"javax.portlet.name=" + EventFilterPortletKeys.EVENTFILTER_PORTLET }, service = ConfigurationAction.class)
public class EventFilterConfigPortlet extends DefaultConfigurationAction {

	Log log = LogFactoryUtil.getLog(EventFilterConfigPortlet.class);

	private EventFilterConfig eventFilterConfig;

	@Override
	public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		String eventType = ParamUtil.getString(actionRequest, EventFilterConfig.EVENTTYPE, "0");
		String topics = ParamUtil.getString(actionRequest, EventFilterConfig.TOPICS, "0");

		setPreference(actionRequest, EventFilterConfig.EVENTTYPE, eventType);
		setPreference(actionRequest, EventFilterConfig.TOPICS, topics);

		super.processAction(portletConfig, actionRequest, actionResponse);
	}

	@Override
	public void include(PortletConfig portletConfig, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("Event Filter Config - Start");

		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		long globalGroupId = themeDisplay.getCompanyGroupId();
		log.info("global ID config : " + globalGroupId);

		// Setup Event Type List
		String[] eventType = { "All Event", "Upcoming Events", "Past Events" };
		List<EventTypeDto> eventTypeList = new ArrayList<>();
		for (int i = 0; i < eventType.length; i++) {
			EventTypeDto dto = new EventTypeDto();
			dto.setTypeId(Long.valueOf(i) + 1);
			dto.setTypeName(eventType[i]);
			eventTypeList.add(dto);
		}

		// Get vocabulary from Group
		AssetVocabulary topicVocabulary = assetVocabularyLocalService.getGroupVocabulary(globalGroupId,
				EventFilterPortletKeys.ASSET_VOCAB_TOPICS);

		// Get all categories from vocabulary
		List<AssetCategory> allCategories = assetCategoryLocalService
				.getVocabularyCategories(topicVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		List<ConfigListDto> topicCategories = new ArrayList<>();

		for (AssetCategory categories : allCategories) {
			if (categories.getParentCategoryId() == 0) {
				ConfigListDto dto = new ConfigListDto();
				dto.setTopicId(categories.getCategoryId());
				dto.setTopicName(categories.getName());
				dto.setParentTopicId(categories.getParentCategoryId());
				dto.setGroupId(categories.getGroupId());
				dto.setCompanyId(categories.getCompanyId());
				dto.setVocabId(categories.getVocabularyId());
				topicCategories.add(dto);
			}
		}

		log.info("Topic : " + topicCategories.toString());

		request.setAttribute(EventFilterConfig.class.getName(), eventFilterConfig);
		request.setAttribute("eventType", eventTypeList);
		request.setAttribute("topics", topicCategories);

		super.include(portletConfig, request, response);

		log.info("Event Filter Config - End");
	}

	@Override
	public String getJspPath(HttpServletRequest request) {
		return "/config/config.jsp";
	}

	@Activate
	@Modified
	protected void activate(Map<Object, Object> properties) {
		eventFilterConfig = Configurable.createConfigurable(EventFilterConfig.class, properties);
	}

	@Reference
	protected AssetVocabularyLocalService assetVocabularyLocalService;

	@Reference
	protected AssetCategoryLocalService assetCategoryLocalService;

}
