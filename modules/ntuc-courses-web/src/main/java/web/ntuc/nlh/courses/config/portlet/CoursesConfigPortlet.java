package web.ntuc.nlh.courses.config.portlet;

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
import web.ntuc.nlh.courses.config.CoursesConfig;
import web.ntuc.nlh.courses.constants.CoursesPortletKeys;

@Component(configurationPid = "web.nlh.ntuc.courses.config.CoursesConfig", configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true, property = {
		"javax.portlet.name=" + CoursesPortletKeys.COURSES_PORTLET }, service = ConfigurationAction.class)
public class CoursesConfigPortlet extends DefaultConfigurationAction {

	Log log = LogFactoryUtil.getLog(CoursesConfigPortlet.class);

	private CoursesConfig coursesConfig;

	@Override
	public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {
		String themes = ParamUtil.getString(actionRequest, CoursesConfig.THEMES, "0");
		String topic = ParamUtil.getString(actionRequest,CoursesConfig.TOPICS, "0");

		setPreference(actionRequest, CoursesConfig.THEMES, themes);
		setPreference(actionRequest, CoursesConfig.TOPICS, topic);

		super.processAction(portletConfig, actionRequest, actionResponse);
	}

	@Override
	public void include(PortletConfig portletConfig, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("Course Config - Start");

		// Overall Session di Liferay (Session, user, group, dll)
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

		long groupId = themeDisplay.getScopeGroupId();
		log.info("groupID config : " + groupId);

		// Get vocabulary from Group
		AssetVocabulary themeVocabulary = assetVocabularyLocalService.getGroupVocabulary(groupId,
				CoursesPortletKeys.ASSET_VOCAB_THEME);

		AssetVocabulary topicVocabulary = assetVocabularyLocalService.getGroupVocabulary(groupId,
				CoursesPortletKeys.ASSET_VOCAB_TOPIC);

		// Get all categories from vocabulary
		List<AssetCategory> themeCategories = assetCategoryLocalService
				.getVocabularyCategories(themeVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		List<AssetCategory> topicCategories = assetCategoryLocalService
				.getVocabularyCategories(topicVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		request.setAttribute(CoursesConfig.class.getName(), coursesConfig);
		request.setAttribute("themes", themeCategories);
		request.setAttribute("topics", topicCategories);

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
		coursesConfig = Configurable.createConfigurable(CoursesConfig.class, properties);
	}

	@Reference
	protected AssetVocabularyLocalService assetVocabularyLocalService;

	@Reference
	protected AssetCategoryLocalService assetCategoryLocalService;

}
