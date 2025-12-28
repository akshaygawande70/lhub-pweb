package web.ntuc.nlh.courses.resource;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetCategoryTable;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.petra.sql.dsl.DSLQueryFactoryUtil;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
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
import java.util.stream.Collectors;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.PortletCommandUtil;
import api.ntuc.nlh.content.dto.FilterCoursesDto;
import web.ntuc.nlh.courses.constants.CoursesPortletKeys;
import web.ntuc.nlh.courses.constants.MVCCommandNames;
import web.ntuc.nlh.courses.util.CommonSearchResult;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.COURSES_FILTER_RESOURCE,
		"javax.portlet.name=" + CoursesPortletKeys.COURSES_PORTLET }, service = MVCResourceCommand.class)
public class CoursesFilterResource implements MVCResourceCommand {

	private static Log log = LogFactoryUtil.getLog(CoursesFilterResource.class);
	CommonSearchResult common = new CommonSearchResult();

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("courses filter resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long localGroupId = themeDisplay.getScopeGroupId();

//			Filter Implementation and prepare resources
			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

			long topic = ParamUtil.getLong(resourceRequest, "topic");
			long theme = ParamUtil.getLong(resourceRequest, "theme");

			log.info("Filter topic : " + topic + " | Filter theme : " + theme);

			HttpServletRequest request = PortalUtil.getHttpServletRequest(resourceRequest);

			List<Long> categoryIds = new ArrayList<>();

			if (Validator.isNotNull(topic) && !Validator.isBlank(String.valueOf(topic)) && topic > 0) {
				categoryIds.add(topic);
			} else {
				categoryIds.add(theme);
			}

			SearchContext searchContext = SearchContextFactory.getInstance(request);
			searchContext.setAttribute("paginationType", "more");
			searchContext.getQueryConfig().setCollatedSpellCheckResultEnabled(true);
			searchContext.getQueryConfig().setCollatedSpellCheckResultScoresThreshold(200);
			searchContext.getQueryConfig().setQueryIndexingEnabled(false);
			searchContext.getQueryConfig().setQueryIndexingThreshold(50);
			searchContext.getQueryConfig().setQuerySuggestionEnabled(true);
			searchContext.getQueryConfig().setQuerySuggestionScoresThreshold(0);

			Map<String, Object> mapResult = common.buildResultSummary(categoryIds, searchContext, themeDisplay, request,
					resourceRequest, resourceResponse, true, ddmIndexer, null, sorts, queries,
					searchRequestBuilderFactory, categoryFacetFactory, aggregations, searchResponseBuilderFactory,false);

			long[] distinctCategoryIds = (long[]) mapResult.get(CoursesPortletKeys.DISTINCT_CATEGORY_IDS);

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
			List<AssetCategory> allTopicVocabCategories = assetCategoryLocalService.getVocabularyCategories(
					courseTopicVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, courseOrder);
			List<AssetCategory> allThemeVocabCategories = assetCategoryLocalService.getVocabularyCategories(
					themeVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, courseOrder);

			if (Validator.isNotNull(distinctCategoryIds)) {
				List<Long> allTopicIds = new ArrayList<>();
				List<Long> allThemeIds = new ArrayList<>();

				long[] resCategoryIds = distinctCategoryIds;
				if (resCategoryIds != null  && resCategoryIds.length>0) {
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
					
					Long[] results= allTopicIds.toArray(new Long[allTopicIds.size()]);
					DSLQuery query=DSLQueryFactoryUtil.select().from(AssetCategoryTable.INSTANCE).where(AssetCategoryTable.INSTANCE.categoryId.in(results));
					List<AssetCategory> tempdata= assetCategoryLocalService.dslQuery(query);
					for (AssetCategory temp : tempdata) {

				//	for (Long topicCategoryId : allTopicIds) {
						FilterCoursesDto dto = new FilterCoursesDto();
				//		AssetCategory temp = assetCategoryLocalService.getCategory(topicCategoryId);
						dto.setFilterId(temp.getCategoryId());
						dto.setFilterName(temp.getName());
						dto.setCategory("topicIds");
						filterTopic.add(dto);
					}
					tempdata = null;
				}
				if(allThemeIds!=null && allThemeIds.size()>0) {
					Long[] results=  results= allThemeIds.toArray(new Long[allThemeIds.size()]);
					DSLQuery query=DSLQueryFactoryUtil.select().from(AssetCategoryTable.INSTANCE).where(AssetCategoryTable.INSTANCE.categoryId.in(results));
					List<AssetCategory> tempdata=assetCategoryLocalService.dslQuery(query);
					for (AssetCategory temp : tempdata) {
				//	for (Long themeCategoryId : allThemeIds) {
						FilterCoursesDto dto = new FilterCoursesDto();
				//		AssetCategory temp = assetCategoryLocalService.getCategory(themeCategoryId);
						dto.setFilterId(temp.getCategoryId());
						dto.setFilterName(temp.getName());
						dto.setCategory("themeIds");
						filterTheme.add(dto);
					}
					tempdata = null;
					
				}


				Double price = (Double) mapResult.get(CoursesPortletKeys.MAX_PRICE);

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
			return true;
		}
		log.info("courses result filter resources - end");
		return false;
	}

	public Long getCoursesVocabularyId(Long categoryId) {
		AssetCategory tempCategory = null;
		Long vocabularyId = null;
		try {
			tempCategory = assetCategoryLocalService.getCategory(categoryId);
			vocabularyId = tempCategory.getVocabularyId();
//			log.info(vocabularyId);
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vocabularyId;
	}

	@Reference
	DDMIndexer ddmIndexer;

	@Reference
	SearchRequestBuilderFactory searchRequestBuilderFactory;

	@Reference
	Queries queries;

	@Reference
	Sorts sorts;

	@Reference
	Aggregations aggregations;

	@Reference
	SearchResponseBuilderFactory searchResponseBuilderFactory;

	@Reference
	CategoryFacetFactory categoryFacetFactory;

	@Reference
	AssetVocabularyLocalService assetVocabularyLocalService;

	@Reference
	AssetCategoryLocalService assetCategoryLocalService;

	@Reference
	CompanyLocalService companyLocalService;
}
