package web.ntuc.nlh.mediafilter.config.portlet;

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
import web.ntuc.nlh.mediafilter.config.MediaFilterConfig;
import web.ntuc.nlh.mediafilter.constants.MediaFilterPortletKeys;

@Component(configurationPid = "web.nlh.ntuc.mediafilter.config.MediaFilterConfig", configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true, property = {
		"javax.portlet.name=" + MediaFilterPortletKeys.MEDIAFILTER_PORTLET }, service = ConfigurationAction.class)
public class MediaFilterConfigPortlet extends DefaultConfigurationAction {
	Log log = LogFactoryUtil.getLog(MediaFilterConfigPortlet.class);

	private MediaFilterConfig mediaFilterConfig;

	@Override
	public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		String topics = ParamUtil.getString(actionRequest, MediaFilterConfig.TOPICS, "0");

		setPreference(actionRequest, MediaFilterConfig.TOPICS, topics);

		super.processAction(portletConfig, actionRequest, actionResponse);
	}

	@Override
	public void include(PortletConfig portletConfig, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("Media Filter Config - Start");

		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		long globalGroupId = themeDisplay.getCompanyGroupId();
		log.info("global ID config : " + globalGroupId);

		// Get vocabulary from Group
		AssetVocabulary topicVocabulary = assetVocabularyLocalService.getGroupVocabulary(globalGroupId,
				MediaFilterPortletKeys.ASSET_VOCAB_TOPICS);

		// Get all categories from vocabulary
		List<AssetCategory> allCategories = assetCategoryLocalService
				.getVocabularyCategories(topicVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		List<ConfigListDto> topicCategories = new ArrayList<>();
		List<ConfigListDto> subTopicCategories = new ArrayList<>();

		for (AssetCategory categories : allCategories) {
			ConfigListDto dto = new ConfigListDto();
			dto.setTopicId(categories.getCategoryId());
			dto.setTopicName(categories.getName());
			dto.setParentTopicId(categories.getParentCategoryId());
			dto.setGroupId(categories.getGroupId());
			dto.setCompanyId(categories.getCompanyId());
			dto.setVocabId(categories.getVocabularyId());
			topicCategories.add(dto);

			subTopicCategories.add(dto);
		}

		request.setAttribute(MediaFilterConfig.class.getName(), mediaFilterConfig);
		request.setAttribute("topics", topicCategories);

		super.include(portletConfig, request, response);

		log.info("Media Filter Config - End");

	}

	@Override
	public String getJspPath(HttpServletRequest request) {
		return "/config/config.jsp";
	}

	@Activate
	@Modified
	protected void activate(Map<Object, Object> properties) {
		mediaFilterConfig = Configurable.createConfigurable(MediaFilterConfig.class, properties);
	}

	@Reference
	protected AssetVocabularyLocalService assetVocabularyLocalService;

	@Reference
	protected AssetCategoryLocalService assetCategoryLocalService;

}
