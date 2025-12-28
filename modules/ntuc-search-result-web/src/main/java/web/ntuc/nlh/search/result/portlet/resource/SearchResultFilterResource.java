package web.ntuc.nlh.search.result.portlet.resource;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetCategoryTable;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.petra.sql.dsl.DSLQueryFactoryUtil;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
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
import java.util.stream.Collectors;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.exception.NtucException;
import api.ntuc.common.util.PortletCommandUtil;
import api.ntuc.nlh.content.dto.FilterCoursesDto;
import web.ntuc.nlh.search.result.constants.MVCCommandNames;
import web.ntuc.nlh.search.result.constants.SearchResultMessageKey;
import web.ntuc.nlh.search.result.constants.SearchResultPortletKeys;
import web.ntuc.nlh.search.result.util.CommonSearchResult;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.SEARCH_FILTER_RESOURCES,
		"javax.portlet.name=" + SearchResultPortletKeys.SEARCH_RESULT_PORTLET }, service = MVCResourceCommand.class)
public class SearchResultFilterResource implements MVCResourceCommand {
	private static Log log = LogFactoryUtil.getLog(SearchResultFilterResource.class);
	CommonSearchResult common = new CommonSearchResult();

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("search filter resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long localGroupId = themeDisplay.getScopeGroupId();

			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

			//			get default company id for topic vocabulary
			Company company = _companyLocalService.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
			long globalGroupId = company.getGroup().getGroupId();
			String keyword = ParamUtil.getString(resourceRequest, "keyword", " ");

			int course = 1;

			HttpServletRequest request = PortalUtil.getHttpServletRequest(resourceRequest);

			List<Long> categoryIds = new ArrayList<>();
			List<AssetCategory> assetCategories = common.getGlobalCategory(globalGroupId);
			for (AssetCategory asset : assetCategories) {
				categoryIds.add(asset.getCategoryId());
			}

			SearchContext searchContext = SearchContextFactory.getInstance(request);
			searchContext.setKeywords("\"" + keyword + "\"");
			searchContext.setAttribute("paginationType", "more");
			searchContext.getQueryConfig().setCollatedSpellCheckResultEnabled(true);
			searchContext.getQueryConfig().setCollatedSpellCheckResultScoresThreshold(200);
			searchContext.getQueryConfig().setQueryIndexingEnabled(false);
			searchContext.getQueryConfig().setQueryIndexingThreshold(50);
			searchContext.getQueryConfig().setQuerySuggestionEnabled(true);
			searchContext.getQueryConfig().setQuerySuggestionScoresThreshold(0);

			//			Adding course

			Map<String, Object> mapResult = common.processHits(categoryIds, searchContext, themeDisplay, request,
					resourceRequest, resourceResponse, course, true, _ddmIndexer, null, _sorts, _queries,
					_searchRequestBuilderFactory, _categoryFacetFactory, _aggregations, _searchResponseBuilderFactory);

			long[] distinctCategoryIds = (long[]) mapResult.get(SearchResultPortletKeys.DISTINCT_CATEGORY_IDS);

			List<FilterCoursesDto> fundedOptionList = new ArrayList<>();
			String[] fundedList = { "Funded", "Not Funded" };
			for (int i = 0; i < fundedList.length; i++) {
				FilterCoursesDto dto = new FilterCoursesDto();
				dto.setFilterId(Long.valueOf(i));
				dto.setFilterName(fundedList[i]);
				dto.setCategory("funded");
				fundedOptionList.add(dto);
			}

			List<FilterCoursesDto> filterTheme = new ArrayList<>();
			List<FilterCoursesDto> filterTopic = new ArrayList<>();

			Map<String, AssetVocabulary> assetMap = common.getLocalThemeAndTopicVocabulary(localGroupId);
			AssetVocabulary themeVocabulary = assetMap.get("themeVocabulary");
			AssetVocabulary courseTopicVocabulary = assetMap.get("courseTopicVocabulary");

			OrderByComparator<AssetCategory> courseOrder = null;
			List<AssetCategory> allTopicVocabCategories = _assetCategoryLocalService.getVocabularyCategories(
					courseTopicVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, courseOrder);
			List<AssetCategory> allThemeVocabCategories = _assetCategoryLocalService.getVocabularyCategories(
					themeVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, courseOrder);

			if (Validator.isNotNull(distinctCategoryIds)) {
				List<Long> allTopicIds = new ArrayList<>();
				List<Long> allThemeIds = new ArrayList<>();

				long[] resCategoryIds = distinctCategoryIds;
				if (resCategoryIds != null && resCategoryIds.length>0) {
					for (long resCategoryId : resCategoryIds) {
						List<Long> topicIds = allTopicVocabCategories.stream()
								.filter(category -> category.getCategoryId() == resCategoryId)
								.map(assetCategory -> assetCategory.getCategoryId()).collect(Collectors.toList());
						allTopicIds.addAll(topicIds);
						List<Long> themeIds = allThemeVocabCategories.stream()
								.filter(category -> category.getCategoryId() == resCategoryId)
								.map(assetCategory -> assetCategory.getCategoryId()).collect(Collectors.toList());
						allThemeIds.addAll(themeIds);
					}
				}

				if(allTopicIds!=null && allTopicIds.size()>0) {
					Long[] results = allTopicIds.toArray(new Long[allTopicIds.size()]);
					DSLQuery query=DSLQueryFactoryUtil.select().from(AssetCategoryTable.INSTANCE).where(AssetCategoryTable.INSTANCE.categoryId.in(results));
					List<AssetCategory> tempdata=_assetCategoryLocalService.dslQuery(query);
					for (AssetCategory temp : tempdata) {
						FilterCoursesDto dto = new FilterCoursesDto();
						dto.setFilterId(temp.getCategoryId());
						dto.setFilterName(temp.getName());
						dto.setCategory("topicIds");
						filterTopic.add(dto);
					}
					tempdata =null;
				}


				if(allThemeIds!=null && allThemeIds.size()>0) {
					Long[] results= allThemeIds.toArray(new Long[allThemeIds.size()]);
					DSLQuery query=DSLQueryFactoryUtil.select().from(AssetCategoryTable.INSTANCE).where(AssetCategoryTable.INSTANCE.categoryId.in(results));
					List<AssetCategory> tempdata=_assetCategoryLocalService.dslQuery(query);
					for (AssetCategory temp : tempdata) {
						FilterCoursesDto dto = new FilterCoursesDto();
						dto.setFilterId(temp.getCategoryId());
						dto.setFilterName(temp.getName());
						dto.setCategory("themeIds");
						filterTheme.add(dto);
					}
					tempdata = null;
				}
				
				Double price = (Double) mapResult.get(SearchResultPortletKeys.MAX_PRICE);

				resourceResponse.setContentType("application/json");
				PrintWriter out = null;
				out = resourceResponse.getWriter();

				JSONObject rowData = JSONFactoryUtil.createJSONObject();
				rowData.put("filterTheme", filterTheme);
				rowData.put("filterTopic", filterTopic);
				rowData.put("maxPrice", price);
				rowData.put("fundedList", fundedOptionList);
				rowData.put("status", HttpServletResponse.SC_OK);
				out.print(rowData.toString());
				out.flush();
			}
		} catch (Exception e) {
			log.error("Error while searching filter data : " + e.getMessage());
			e.printStackTrace();
			return true;
		}
		log.info("search result filter resources - end");
		return false;
	}

	@Reference
	DDMIndexer _ddmIndexer;

	@Reference
	SearchRequestBuilderFactory _searchRequestBuilderFactory;

	@Reference
	Queries _queries;

	@Reference
	Sorts _sorts;

	@Reference
	Aggregations _aggregations;

	@Reference
	SearchResponseBuilderFactory _searchResponseBuilderFactory;

	@Reference
	CategoryFacetFactory _categoryFacetFactory;

	@Reference
	AssetVocabularyLocalService _assetVocabularyLocalService;

	@Reference
	AssetCategoryLocalService _assetCategoryLocalService;

	@Reference
	CompanyLocalService _companyLocalService;

}
