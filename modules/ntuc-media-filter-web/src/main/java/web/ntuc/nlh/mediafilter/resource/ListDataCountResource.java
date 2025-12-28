package web.ntuc.nlh.mediafilter.resource;

import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.aggregation.Aggregations;
import com.liferay.portal.search.facet.category.CategoryFacetFactory;
import com.liferay.portal.search.legacy.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.legacy.searcher.SearchResponseBuilderFactory;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.sort.Sorts;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.PortletCommandUtil;
//import api.ntuc.nlh.content.util.CommonDateResult;
import web.ntuc.nlh.mediafilter.constants.MVCCommandNames;
import web.ntuc.nlh.mediafilter.constants.MediaFilterPortletKeys;
import web.ntuc.nlh.mediafilter.util.CommonDateResult;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.LIST_TOPICS_COUNT_RESOURCE,
		"javax.portlet.name=" + MediaFilterPortletKeys.MEDIAFILTER_PORTLET }, service = MVCResourceCommand.class)
public class ListDataCountResource implements MVCResourceCommand {

	private static Log log = LogFactoryUtil.getLog(ListDataCountResource.class);

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Media Topic Count Resources - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long groupId = themeDisplay.getScopeGroupId();
			log.info("groupID = " + groupId);

//			Authorization and validation check
			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

			long topics = ParamUtil.getLong(resourceRequest, "topicId");

			String filter = ParamUtil.getString(resourceRequest, "filter");
			JSONObject filterJson = JSONFactoryUtil.createJSONObject(filter);

			log.info("topics : " + topics);


//			Getting Article Data
			HttpServletRequest request = PortalUtil.getHttpServletRequest(resourceRequest);

			List<Long> categoryIds = new ArrayList<>();

			if (Validator.isNotNull(topics) && !Validator.isBlank(String.valueOf(topics)) && topics > 0) {
				categoryIds.add(topics);
			}

//			Preparation Elastic Search
			SearchContext searchContext = SearchContextFactory.getInstance(request);

			searchContext.setAttribute("paginationType", "more");

			searchContext.getQueryConfig().setCollatedSpellCheckResultEnabled(true);
			searchContext.getQueryConfig().setCollatedSpellCheckResultScoresThreshold(200);
			searchContext.getQueryConfig().setQueryIndexingEnabled(false);
			searchContext.getQueryConfig().setQueryIndexingThreshold(50);
			searchContext.getQueryConfig().setQuerySuggestionEnabled(true);
			searchContext.getQueryConfig().setQuerySuggestionScoresThreshold(0);

			// Getting detail data from courses id
			Map<String, Object> mapResult = CommonDateResult.getHitsCount(categoryIds, searchContext, themeDisplay,
					request, resourceRequest, resourceResponse, false, ddmIndexer, filterJson, sorts, queries,
					searchRequestBuilderFactory, categoryFacetFactory, aggregations, searchResponseBuilderFactory,
					MediaFilterPortletKeys.SEARCH_RESULTS, MediaFilterPortletKeys.PARAMETER_ARTICLE_ADMIN_GROUP_CODE,
					MediaFilterPortletKeys.HITS, MediaFilterPortletKeys.DISTINCT_CATEGORY_IDS, "media", null, null,
					null, null);

			int hitsCount = (int) mapResult.get("total");
			resourceResponse.setContentType("application/json");
			PrintWriter out = null;
			out = resourceResponse.getWriter();
			JSONObject rowData = JSONFactoryUtil.createJSONObject();
			rowData.put("total", hitsCount);
			rowData.put("status", HttpServletResponse.SC_OK);
			out.print(rowData.toString());
			out.flush();
		} catch (Exception e) {
			log.error("Error while searching result data : " + e.getMessage());
			return true;
		}
		log.info("Media Topic Count resources - end");
		return false;
	}

	@Reference
	AssetVocabularyLocalService assetVocabularyLocalService;

	@Reference
	AssetCategoryLocalService assetCategoryLocalService;

	@Reference
	CompanyLocalService companyLocalService;

	@Reference
	DDMIndexer ddmIndexer;

	@Reference
	SearchRequestBuilderFactory searchRequestBuilderFactory;

	@Reference
	CategoryFacetFactory categoryFacetFactory;

	@Reference
	Queries queries;

	@Reference
	Sorts sorts;

	@Reference
	Aggregations aggregations;

	@Reference
	SearchResponseBuilderFactory searchResponseBuilderFactory;
}
