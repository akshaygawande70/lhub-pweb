package web.ntuc.nlh.assetfilter.resource;

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
import web.ntuc.nlh.content.util.CommonAssetFilterResult;
import web.ntuc.nlh.assetfilter.constants.AssetFilterPortletKeys;
import web.ntuc.nlh.assetfilter.constants.MVCCommandNames;


@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.LIST_TOPICS_COUNT_RESOURCE,
		"javax.portlet.name=" + AssetFilterPortletKeys.ASSETFILTER_PORTLET }, service = MVCResourceCommand.class)
public class ListDataCountResource implements MVCResourceCommand {

	private static Log log = LogFactoryUtil.getLog(ListDataCountResource.class);

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Blogs Topic Count Resources - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

//			Authorization and validation check
			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);
			
			long topics = ParamUtil.getLong(resourceRequest, "topicId");
			long subTopics = ParamUtil.getLong(resourceRequest, "subTopicId");

			String filter = ParamUtil.getString(resourceRequest, "filter");
			JSONObject filterJson = JSONFactoryUtil.createJSONObject(filter);


//			Getting Article Data
			HttpServletRequest request = PortalUtil.getHttpServletRequest(resourceRequest);

			List<Long> categoryIds = new ArrayList<>();

			if (Validator.isNotNull(subTopics) && !Validator.isBlank(String.valueOf(subTopics)) && subTopics > 0) {
				categoryIds.add(subTopics);
			} else if(Validator.isNotNull(topics) && !Validator.isBlank(String.valueOf(topics)) && topics > 0) {
				categoryIds.add(topics);
			}else {
				categoryIds.add(-1L);
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
			Map<String, Object> mapResult = null;
			if(categoryIds !=null && categoryIds.size()>0 && categoryIds.get(0)>0) {
//				Getting detail data from courses id
				 mapResult = CommonAssetFilterResult.getHitsCount(categoryIds, searchContext, themeDisplay,
						request, resourceRequest, resourceResponse, false, ddmIndexer, filterJson, sorts, queries,
						searchRequestBuilderFactory, categoryFacetFactory, aggregations, searchResponseBuilderFactory,
						AssetFilterPortletKeys.SEARCH_RESULTS, AssetFilterPortletKeys.PARAMETER_ARTICLE_ADMIN_GROUP_CODE,
						AssetFilterPortletKeys.HITS, AssetFilterPortletKeys.DISTINCT_CATEGORY_IDS, "blog", null, null, null,
						null);

				
			}else {
//				Getting detail data from courses id
				mapResult = CommonAssetFilterResult.getHitsCountAll(categoryIds, searchContext, themeDisplay,
						request, resourceRequest, resourceResponse, false, ddmIndexer, filterJson, sorts, queries,
						searchRequestBuilderFactory, categoryFacetFactory, aggregations, searchResponseBuilderFactory,
						AssetFilterPortletKeys.SEARCH_RESULTS, AssetFilterPortletKeys.PARAMETER_ARTICLE_ADMIN_GROUP_CODE,
						AssetFilterPortletKeys.HITS, AssetFilterPortletKeys.DISTINCT_CATEGORY_IDS, "blog", null, null, null,
						null);

				
			}

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
		log.info("Blogs Topic Count resources - end");
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
