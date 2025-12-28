package web.ntuc.nlh.assetfilter.config.portlet;

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
import api.ntuc.nlh.content.dto.ConfigListDto;
import web.ntuc.nlh.assetfilter.config.AssetFilterConfig;
import web.ntuc.nlh.assetfilter.constants.AssetFilterPortletKeys;

@Component(configurationPid = "web.nlh.ntuc.assetfilter.config.AssetFilterConfig", configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true, property = {
		"javax.portlet.name=" + AssetFilterPortletKeys.ASSETFILTER_PORTLET }, service = ConfigurationAction.class)
public class AssetFilterConfigPortlet extends DefaultConfigurationAction {
	
	Log log = LogFactoryUtil.getLog(AssetFilterConfigPortlet.class);

	private AssetFilterConfig assetFilterConfig;

	@Override
	public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {
		
		String topics = ParamUtil.getString(actionRequest, AssetFilterConfig.TOPICS, "0");
		String subTopics = ParamUtil.getString(actionRequest, AssetFilterConfig.SUBTOPICS, "0");
		String topicTitle = ParamUtil.getString(actionRequest, AssetFilterConfig.TOPICTITLE, "");

		setPreference(actionRequest, AssetFilterConfig.TOPICS, topics);
		setPreference(actionRequest, AssetFilterConfig.SUBTOPICS, subTopics);
		setPreference(actionRequest, AssetFilterConfig.TOPICTITLE, topicTitle);

		super.processAction(portletConfig, actionRequest, actionResponse);
	}

	@Override
	public void include(PortletConfig portletConfig, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("Course Config - Start");
		
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		long globalGroupId = themeDisplay.getCompanyGroupId();
		log.info("global ID config : " + globalGroupId);

		// Get vocabulary from Group
		AssetVocabulary topicVocabulary = assetVocabularyLocalService.getGroupVocabulary(globalGroupId,
				AssetFilterPortletKeys.ASSET_VOCAB_TOPICS);
		
		// Get all categories from vocabulary
		List<AssetCategory> allCategories = assetCategoryLocalService
				.getVocabularyCategories(topicVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		List<ConfigListDto> topicCategories = new ArrayList<>();
		List<ConfigListDto> subTopicCategories = new ArrayList<>();
		
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
			}else if (categories.getParentCategoryId() > 0) {
				ConfigListDto dto = new ConfigListDto();
				dto.setTopicId(categories.getCategoryId());
				dto.setTopicName(categories.getName());
				dto.setParentTopicId(categories.getParentCategoryId());
				dto.setGroupId(categories.getGroupId());
				dto.setCompanyId(categories.getCompanyId());
				dto.setVocabId(categories.getVocabularyId());
				subTopicCategories.add(dto);
			}
		}
		
		log.info("Topic : " + topicCategories.toString());
		log.info("Sub Topic : " + subTopicCategories.toString());
		
		request.setAttribute(AssetFilterConfig.class.getName(), assetFilterConfig);
		request.setAttribute("topics", topicCategories);
		request.setAttribute("subTopics", subTopicCategories);

		super.include(portletConfig, request, response);

		log.info("Courses Config - End");
	}

	@Override
	public String getJspPath(HttpServletRequest request) {
		return "/config/config.jsp";
	}

	@Activate
	@Modified
	protected void activate(Map<Object, Object> properties) {
		assetFilterConfig = Configurable.createConfigurable(AssetFilterConfig.class, properties);
	}

	@Reference
	protected AssetVocabularyLocalService assetVocabularyLocalService;

	@Reference
	protected AssetCategoryLocalService assetCategoryLocalService;

}
