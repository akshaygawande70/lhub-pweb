package web.ntuc.nlh.search.resource;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import web.ntuc.nlh.search.constants.MVCCommandNames;
import web.ntuc.nlh.search.constants.SearchMessagesKey;
import web.ntuc.nlh.search.constants.SearchPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.TOPIC_DATA_RESOURCES,
		"javax.portlet.name=" + SearchPortletKeys.SEARCH_PORTLET}, service = MVCResourceCommand.class)
public class TopicDataResource implements MVCResourceCommand{
	private static Log log = LogFactoryUtil.getLog(TopicDataResource.class);

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("topic data resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long scopeGroupId = themeDisplay.getScopeGroupId();
			log.info("scopeGroupId = "+scopeGroupId);
			boolean isAuthorized = ParamUtil.getBoolean(resourceRequest, "isAuthorized");
			boolean validCSRF = ParamUtil.getBoolean(resourceRequest, "validCSRF");
			boolean xssPass = ParamUtil.getBoolean(resourceRequest, "xssPass");

			if (!isAuthorized || !validCSRF) {
				String msg = "isAuthorized : " + isAuthorized + " | validCSRF : " + validCSRF;
				throw new Exception(msg);
			}

			if (!xssPass) {
				PortletConfig portletConfig = (PortletConfig) resourceRequest
						.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
				String msg = LanguageUtil.format(portletConfig.getResourceBundle(themeDisplay.getLocale()),
						SearchMessagesKey.XSS_VALIDATION_NOT_PASS, xssPass);
				throw new Exception(msg);
			}
			
			AssetVocabulary topicVocabulary = assetVocabularyLocalService.fetchGroupVocabulary(scopeGroupId, SearchPortletKeys.ASSET_VOCAB_TOPIC);
			OrderByComparator<AssetCategory> assetComparator = null;
			List<AssetCategory> topicCategories = assetCategoryLocalService.getVocabularyCategories(topicVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, assetComparator);
			JSONArray listTopics = JSONFactoryUtil.createJSONArray();
			for (AssetCategory asset : topicCategories) {
				JSONObject jsonTopic = JSONFactoryUtil.createJSONObject();
				jsonTopic.put("categoryId", asset.getCategoryId());
				jsonTopic.put("title", asset.getTitle());
				jsonTopic.put("icon", asset.getDescription());
				listTopics.put(jsonTopic);
			}
			resourceResponse.getWriter().println(listTopics.toString());
			
		} catch (Exception e) {
			log.error("Error while searching topic data : "+e.getMessage());
			return true;
		}
		log.info("topic data resources - end");
		return false;
	}
	
	@Reference
	protected AssetVocabularyLocalService assetVocabularyLocalService;
	
	@Reference
	protected AssetCategoryLocalService assetCategoryLocalService;
}
