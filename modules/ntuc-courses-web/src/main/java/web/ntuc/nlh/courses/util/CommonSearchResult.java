package web.ntuc.nlh.courses.util;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetVocabularyLocalServiceUtil;
import com.liferay.data.engine.rest.dto.v2_0.DataDefinitionField.IndexType;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.journal.model.JournalArticle;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.NestedQuery;
import com.liferay.portal.kernel.search.generic.QueryTermImpl;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.aggregation.Aggregations;
import com.liferay.portal.search.aggregation.bucket.Bucket;
import com.liferay.portal.search.aggregation.bucket.FilterAggregation;
import com.liferay.portal.search.aggregation.bucket.FilterAggregationResult;
import com.liferay.portal.search.aggregation.bucket.NestedAggregation;
import com.liferay.portal.search.aggregation.bucket.NestedAggregationResult;
import com.liferay.portal.search.aggregation.bucket.TermsAggregation;
import com.liferay.portal.search.aggregation.bucket.TermsAggregationResult;
import com.liferay.portal.search.aggregation.metrics.MaxAggregation;
import com.liferay.portal.search.aggregation.metrics.MaxAggregationResult;
import com.liferay.portal.search.facet.category.CategoryFacetFactory;
import com.liferay.portal.search.legacy.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.legacy.searcher.SearchResponseBuilderFactory;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.searcher.SearchResponse;
import com.liferay.portal.search.sort.FieldSort;
import com.liferay.portal.search.sort.NestedSort;
import com.liferay.portal.search.sort.Sort;
import com.liferay.portal.search.sort.SortOrder;
import com.liferay.portal.search.sort.Sorts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.portlet.MimeResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Reference;

import api.ntuc.nlh.content.engine.PortletRequestThemeDisplaySupplier;
import api.ntuc.nlh.content.engine.PortletURLFactory;
import api.ntuc.nlh.content.engine.PortletURLFactoryImpl;
import api.ntuc.nlh.content.engine.SearchPortletSearchResultPreferences;
import api.ntuc.nlh.content.engine.SearchResultPreferences;
//import api.ntuc.nlh.content.engine.SearchResultSummaryDisplayBuilder;
import api.ntuc.nlh.content.engine.SearchResultSummaryDisplayContext;
import api.ntuc.nlh.content.engine.ThemeDisplaySupplier;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.nlh.courses.constants.CoursesPortletKeys;

public class CommonSearchResult {
	private static Log log = LogFactoryUtil.getLog(CommonSearchResult.class);

	public List<AssetCategory> getGlobalCategory(long globalGroupId) {
		ParameterGroup groupGlobalParam = ParameterGroupLocalServiceUtil
				.getByCode(CoursesPortletKeys.PARAMETER_GLOBAL_GROUP_CODE, false);
		long paramGroupId = groupGlobalParam.getGroupId();
		Parameter globalTopicParam = ParameterLocalServiceUtil.getByGroupCode(paramGroupId,
				groupGlobalParam.getParameterGroupId(), CoursesPortletKeys.PARAMETER_GLOBAL_SEARCH_TOPIC_CODE, false);
		AssetVocabulary topicVocabulary = AssetVocabularyLocalServiceUtil.fetchGroupVocabulary(globalGroupId,
				globalTopicParam.getParamValue());

		OrderByComparator<AssetCategory> order = null;
		return AssetCategoryLocalServiceUtil
				.getVocabularyCategories(topicVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, order)
				.stream().filter(category -> category.getName().equals(CoursesPortletKeys.COURSE_CATEGORY_NAME))
				.collect(Collectors.toList());

	}

	public Map<String, AssetVocabulary> getLocalThemeAndTopicVocabulary(long localGroupId) {
		try {
			List<ParameterGroup> courseCategoryParameterGroups = ParameterGroupLocalServiceUtil
					.getByGroupIdCode(localGroupId,CoursesPortletKeys.COURSE_CATEGORY_GROUP_CODE, false);
			ParameterGroup courseCategoryParameterGroup = courseCategoryParameterGroups.get(0);
			Parameter courseTopicParameter = ParameterLocalServiceUtil.getByGroupCode(
					courseCategoryParameterGroup.getGroupId(), courseCategoryParameterGroup.getParameterGroupId(),
					CoursesPortletKeys.COURSE_TOPIC_NAME, false);
			Parameter courseThemeParameter = ParameterLocalServiceUtil.getByGroupCode(
					courseCategoryParameterGroup.getGroupId(), courseCategoryParameterGroup.getParameterGroupId(),
					CoursesPortletKeys.COURSE_THEME_NAME, false);

			AssetVocabulary themeVocabulary = AssetVocabularyLocalServiceUtil.getGroupVocabulary(localGroupId,
					courseThemeParameter.getParamValue());
			AssetVocabulary courseTopicVocabulary = AssetVocabularyLocalServiceUtil.getGroupVocabulary(localGroupId,
					courseTopicParameter.getParamValue());
			Map<String, AssetVocabulary> assetMap = new HashMap<>();
			assetMap.put("themeVocabulary", themeVocabulary);
			assetMap.put("courseTopicVocabulary", courseTopicVocabulary);
			return assetMap;
		} catch (PortalException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	public Map<String, Object> buildResultSummary(List<Long> categoryIds, SearchContext searchContext,
			ThemeDisplay themeDisplay, HttpServletRequest request, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse, boolean isFilter, DDMIndexer ddmIndexer, JSONObject filterJson,
			Sorts sorts, Queries queries, SearchRequestBuilderFactory searchRequestBuilderFactory,
			CategoryFacetFactory categoryFacetFactory, Aggregations aggregations,
			SearchResponseBuilderFactory searchResponseBuilderFactory,boolean filterResourceCall) throws Exception {
		List<SearchResultSummaryDisplayContext> results = new ArrayList<>();
		Map<String, Object> processMap = this.processHits(categoryIds, searchContext, themeDisplay, request,
				resourceRequest, resourceResponse, isFilter, ddmIndexer, filterJson, sorts, queries,
				searchRequestBuilderFactory, categoryFacetFactory, aggregations, searchResponseBuilderFactory);
		
		if(filterResourceCall) {
			Hits hits = (Hits) processMap.get("hits");
			List<Document> documents = hits.toList();
			for (Document document : documents) {
				SearchResultSummaryDisplayBuilder searchResultSummaryDisplayBuilder = new SearchResultSummaryDisplayBuilder();
				searchResultSummaryDisplayBuilder.setAssetEntryLocalService(AssetEntryLocalServiceUtil.getService());
				searchResultSummaryDisplayBuilder.setCurrentURL(request.getRequestURI());
				searchResultSummaryDisplayBuilder.setDocument(document);
				searchResultSummaryDisplayBuilder.setHighlightEnabled(true);
				searchResultSummaryDisplayBuilder.setLanguage(LanguageUtil.getLanguage());
				searchResultSummaryDisplayBuilder.setLocale(request.getLocale());
				MimeResponse mimeResponse = resourceResponse;
				PortletURLFactory portletURLFactory = new PortletURLFactoryImpl(resourceRequest, mimeResponse);
				searchResultSummaryDisplayBuilder.setPortletURLFactory(portletURLFactory);
				searchResultSummaryDisplayBuilder.setQueryTerms(hits.getQueryTerms());
				searchResultSummaryDisplayBuilder.setResourceRequest(resourceRequest);
				searchResultSummaryDisplayBuilder.setResourceResponse(resourceResponse);
				searchResultSummaryDisplayBuilder.setRequest(request);
				searchResultSummaryDisplayBuilder.setResourceActions(ResourceActionsUtil.getResourceActions());
				ThemeDisplaySupplier themeDisplaySupplier = new PortletRequestThemeDisplaySupplier(resourceRequest);
				SearchResultPreferences searchResultPreferences = new SearchPortletSearchResultPreferences(
						resourceRequest.getPreferences(), themeDisplaySupplier);
				searchResultSummaryDisplayBuilder.setSearchResultPreferences(searchResultPreferences);
				searchResultSummaryDisplayBuilder.setThemeDisplay(themeDisplay);

				SearchResultSummaryDisplayContext searchResultSummaryDisplayContext = searchResultSummaryDisplayBuilder
						.build();

				results.add(searchResultSummaryDisplayContext);
			}
		}
		

		Map<String, Object> returnedResult = new HashMap<>();
		returnedResult.put(CoursesPortletKeys.SEARCH_RESULTS, results);
		returnedResult.put(CoursesPortletKeys.DISTINCT_CATEGORY_IDS,
				processMap.get(CoursesPortletKeys.DISTINCT_CATEGORY_IDS));
		returnedResult.put(CoursesPortletKeys.MAX_PRICE, processMap.get(CoursesPortletKeys.MAX_PRICE));
		return returnedResult;
	}

	public Map<String, Object> getHitsCount(List<Long> categoryIds, SearchContext searchContext,
			ThemeDisplay themeDisplay, HttpServletRequest request, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse, boolean isFilter, DDMIndexer ddmIndexer,
			JSONObject filterJson, Sorts sorts, Queries queries,
			SearchRequestBuilderFactory searchRequestBuilderFactory, CategoryFacetFactory categoryFacetFactory,
			Aggregations aggregation, SearchResponseBuilderFactory searchResponseBuilderFactory) throws Exception {
		Map<String, Object> mapResult = this.processHits(categoryIds, searchContext, themeDisplay, request,
				resourceRequest, resourceResponse, isFilter, ddmIndexer, filterJson, sorts, queries,
				searchRequestBuilderFactory, categoryFacetFactory, aggregation, searchResponseBuilderFactory);
		Hits hits = (Hits) mapResult.get(CoursesPortletKeys.HITS);
		Double maxPrice = (Double) mapResult.get(CoursesPortletKeys.MAX_PRICE);
		Map<String, Object> returnedMap = new HashMap<>();
		returnedMap.put("total", hits.getLength());
		returnedMap.put("maxPrice", maxPrice);
		return returnedMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> processHits(List<Long> categoryIds, SearchContext searchContext,
			ThemeDisplay themeDisplay, HttpServletRequest request, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse, boolean isFilter, DDMIndexer ddmIndexer, JSONObject filterJson,
			Sorts sorts, Queries queries, SearchRequestBuilderFactory searchRequestBuilderFactory,
			CategoryFacetFactory categoryFacetFactory, Aggregations aggregations,
			SearchResponseBuilderFactory searchResponseBuilderFactory) throws Exception {
		Indexer<JournalArticle> indexer = IndexerRegistryUtil.getIndexer(JournalArticle.class);

		BooleanQuery mainQuery = new BooleanQueryImpl();
		List<ParameterGroup> groupCourseAdmins = ParameterGroupLocalServiceUtil
				.getByGroupIdCode(themeDisplay.getScopeGroupId(), CoursesPortletKeys.PARAMETER_COURSE_ADMIN_GROUP_CODE, false);
		ParameterGroup groupCourseAdmin = groupCourseAdmins.get(0);
		long siteGroupId = groupCourseAdmin.getGroupId();

		BooleanQuery themeQuery = new BooleanQueryImpl();
		BooleanQuery topicQuery = new BooleanQueryImpl();
		BooleanQuery priceQuery = new BooleanQueryImpl();
		BooleanQuery fundedQuery = new BooleanQueryImpl();
		Parameter structureParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
				groupCourseAdmin.getParameterGroupId(), CoursesPortletKeys.PARAMETER_COURSE_STRUCTURE_CODE, false);
		DynamicQuery structureQuery = DDMStructureLocalServiceUtil.dynamicQuery();
		structureQuery.add(PropertyFactoryUtil.forName("name").like("%>" + structureParam.getParamValue() + "<%"));
		structureQuery.add(PropertyFactoryUtil.forName("groupId").eq(siteGroupId));
		List<DDMStructure> ddmStructures = DDMStructureLocalServiceUtil.dynamicQuery(structureQuery, 0, 1);
		mainQuery.addRequiredTerm("ddmStructureKey", ddmStructures.get(0).getStructureKey());
		searchRequestBuilderFactory.builder(searchContext).fetchSource(true).build();

		TermsAggregation assetCategoryAggregation = aggregations.terms(CoursesPortletKeys.ASSET_CATEGORY_AGGS,
				Field.ASSET_CATEGORY_IDS);
		assetCategoryAggregation.setSize(500);
		String priceFieldKey = this.getDDMSearchField(ddmStructures.get(0), "price", false);
		String priceValueKey = StringBundler.concat(CoursesPortletKeys.DDM_FIELD_ARRAY, StringPool.PERIOD,
				ddmIndexer.getValueFieldName(IndexType.KEYWORD.getValue() + "_Number_sortable"));
		MaxAggregation maxAggregation = aggregations.max(CoursesPortletKeys.MAX_PRICE_AGGS, priceValueKey);
		FilterAggregation filterAggregation = aggregations.filter(CoursesPortletKeys.FILTER_AGGS, queries
				.term(CoursesPortletKeys.DDM_FIELD_ARRAY + "." + CoursesPortletKeys.DDM_FIELD_NAME, priceFieldKey));
		filterAggregation.addChildAggregation(maxAggregation);
		NestedAggregation nestedAggregation = aggregations.nested(CoursesPortletKeys.NESTED_AGGS,
				CoursesPortletKeys.DDM_FIELD_ARRAY);
		nestedAggregation.addChildAggregation(filterAggregation);
		searchRequestBuilderFactory.builder(searchContext).addAggregation(nestedAggregation).build();
		searchRequestBuilderFactory.builder(searchContext).addAggregation(assetCategoryAggregation).build();

		if (Validator.isNotNull(filterJson)) {
			priceQuery = this.priceFilterElastic(filterJson.getJSONObject("price"), ddmStructures.get(0), ddmIndexer);
			fundedQuery = this.fundedFilterElastic(filterJson.getJSONArray("funded"), ddmStructures.get(0), ddmIndexer);
			themeQuery = this.categoryFilterElastic(filterJson.getJSONArray("themeIds"));
			topicQuery = this.categoryFilterElastic(filterJson.getJSONArray("topicIds"));
			searchRequestBuilderFactory.builder(searchContext).sorts(this.priceSortElastic(filterJson.getString("sort"),
					ddmStructures.get(0), ddmIndexer, sorts, queries));
		}

		NestedQuery nestedPriceQuery = new NestedQuery(CoursesPortletKeys.DDM_FIELD_ARRAY, priceQuery);
		NestedQuery nestedFundedQuery = new NestedQuery(CoursesPortletKeys.DDM_FIELD_ARRAY, fundedQuery);

		mainQuery.add(nestedPriceQuery, BooleanClauseOccur.MUST);
		mainQuery.add(nestedFundedQuery, BooleanClauseOccur.MUST);
		mainQuery.add(themeQuery, BooleanClauseOccur.MUST);
		mainQuery.add(topicQuery, BooleanClauseOccur.MUST);

		mainQuery.addRequiredTerm("groupId", siteGroupId);

		if (!categoryIds.isEmpty()) {
			Long[] assetCategoryIds = new Long[categoryIds.size()];
			categoryIds.toArray(assetCategoryIds);
			long[] assetCategoryIdsLong = Arrays.stream(assetCategoryIds).filter(Objects::nonNull)
					.mapToLong(Long::longValue).toArray();
			mainQuery.addRequiredTerm(Field.ASSET_CATEGORY_IDS, assetCategoryIdsLong[0]);
		}

		BooleanClause<Query> booleanClause = BooleanClauseFactoryUtil.create(mainQuery,
				BooleanClauseOccur.MUST.getName());
		searchContext.setBooleanClauses(new BooleanClause[] { booleanClause });

		Hits hits = indexer.search(searchContext);
		Map<String, Object> returnedMap = new HashMap<>();
		returnedMap.put(CoursesPortletKeys.HITS, hits);

		SearchResponse res = searchResponseBuilderFactory.builder(searchContext).build();
		TermsAggregationResult termsAggregationResult = (TermsAggregationResult) res
				.getAggregationResult(CoursesPortletKeys.ASSET_CATEGORY_AGGS);
		int bucketSize = termsAggregationResult.getBuckets().size();
		long[] distinctCategoryIds = new long[bucketSize];
		int i = 0;
		for (Bucket b : termsAggregationResult.getBuckets()) {
			distinctCategoryIds[i] = Long.valueOf(b.getKey());
			i++;
		}
		returnedMap.put(CoursesPortletKeys.DISTINCT_CATEGORY_IDS, distinctCategoryIds);

		NestedAggregationResult nestedAggregationResult = (NestedAggregationResult) res
				.getAggregationResult(CoursesPortletKeys.NESTED_AGGS);
		FilterAggregationResult filterAggregationResult = (FilterAggregationResult) nestedAggregationResult
				.getChildAggregationResult(CoursesPortletKeys.FILTER_AGGS);
		MaxAggregationResult maxAggregationResult = (MaxAggregationResult) filterAggregationResult
				.getChildAggregationResult(CoursesPortletKeys.MAX_PRICE_AGGS);
		double maxPrice = maxAggregationResult.getValue();
		returnedMap.put(CoursesPortletKeys.MAX_PRICE, maxPrice);
		log.info("hits.getLength() = " + hits.getLength());

		return returnedMap;
	}

	private BooleanQuery categoryFilterElastic(JSONArray categoryJson) {
		BooleanQuery categoryQuery = new BooleanQueryImpl();
		try {
			if (categoryJson.length() > 0) {
				for (int i = 0; i < categoryJson.length(); i++) {
					long temp = categoryJson.getLong(i);
					Query query = new TermQueryImpl(Field.ASSET_CATEGORY_IDS, String.valueOf(temp));
					categoryQuery.add(query, BooleanClauseOccur.SHOULD);
				}
			}
		} catch (ParseException e) {
			log.info(e.getMessage());
		}

		return categoryQuery;
	}

	private BooleanQuery priceFilterElastic(JSONObject priceJson, DDMStructure ddmStructure, DDMIndexer ddmIndexer) {
		BooleanQuery priceQuery = new BooleanQueryImpl();
		try {
			String priceField = this.getDDMSearchField(ddmStructure, "price", false);
			priceQuery.addRequiredTerm(StringBundler.concat(CoursesPortletKeys.DDM_FIELD_ARRAY, StringPool.PERIOD,
					CoursesPortletKeys.DDM_FIELD_NAME), priceField);
			BooleanQuery rangeQuery = new BooleanQueryImpl();
			rangeQuery.addRangeTerm(
					StringBundler.concat(CoursesPortletKeys.DDM_FIELD_ARRAY, StringPool.PERIOD,
							ddmIndexer.getValueFieldName(IndexType.KEYWORD.getValue() + "_Number_sortable")),
					(long) priceJson.getDouble("min"), (long) priceJson.getDouble("max"));

			priceQuery.add(rangeQuery, BooleanClauseOccur.MUST);
		} catch (ParseException e) {
			log.info(e.getMessage());
		}
		return priceQuery;
	}

	private Sort priceSortElastic(String jsonSort, DDMStructure ddmStructure, DDMIndexer ddmIndexer, Sorts sorts,
			Queries queries) {
		SortOrder sortOrder;
		if (jsonSort.equals("asc")) {
			sortOrder = SortOrder.ASC;
		} else {
			sortOrder = SortOrder.DESC;
		}
		String sortableFieldName = StringBundler.concat(CoursesPortletKeys.DDM_FIELD_ARRAY, StringPool.PERIOD,
				ddmIndexer.getValueFieldName(IndexType.KEYWORD.getValue() + "_Number_sortable"));
		String priceField = this.getDDMSearchField(ddmStructure, "price", false);
		FieldSort fieldSort = sorts.field(sortableFieldName, sortOrder);
		NestedSort nestedSort = sorts.nested(CoursesPortletKeys.DDM_FIELD_ARRAY);

		nestedSort.setFilterQuery(queries.term(
				StringBundler.concat(DDMIndexer.DDM_FIELD_ARRAY, StringPool.PERIOD, DDMIndexer.DDM_FIELD_NAME),
				priceField));
		fieldSort.setNestedSort(nestedSort);

		return fieldSort;
	}

	private BooleanQuery fundedFilterElastic(JSONArray fundedJson, DDMStructure ddmStructure, DDMIndexer ddmIndexer) {
		BooleanQuery fundedQuery = new BooleanQueryImpl();
		try {
			if (fundedJson.length() > 0) {
				String fundedField = this.getDDMSearchField(ddmStructure, "funded", false);
				fundedQuery.addRequiredTerm(StringBundler.concat(CoursesPortletKeys.DDM_FIELD_ARRAY, StringPool.PERIOD,
						CoursesPortletKeys.DDM_FIELD_NAME), fundedField);
				BooleanQuery eachFundedQuery = new BooleanQueryImpl();
				for (int i = 0; i < fundedJson.length(); i++) {
					Query query = new TermQueryImpl(
							StringBundler.concat(CoursesPortletKeys.DDM_FIELD_ARRAY, StringPool.PERIOD,
									ddmIndexer.getValueFieldName(IndexType.KEYWORD.getValue() + "_String_sortable")),
							fundedJson.getString(i).equals("NOT FUNDED") ? String.valueOf(false)
									: String.valueOf(true));
					eachFundedQuery.add(query, BooleanClauseOccur.SHOULD);
				}
				fundedQuery.add(eachFundedQuery, BooleanClauseOccur.MUST);
			}
		} catch (ParseException e) {
			log.info(e.getMessage());
		}

		return fundedQuery;
	}

	public String getDDMSearchField(DDMStructure structure, String searchfield, boolean withLocale) {
		// ddm__keyword__12345__MenuDay_en_US
		String fieldName = CoursesPortletKeys.FIELD_PREFIX + structure.getStructureId() + "__" + searchfield;
		if (withLocale) {
			fieldName = fieldName + "_" + structure.getDefaultLanguageId();
		}
		return fieldName;
	}

	public void addExactRequiredTerm(BooleanQuery query, String field, String value) throws ParseException {
		TermQueryImpl termQuery = new TermQueryImpl(new QueryTermImpl(field, value));
		query.add(termQuery, BooleanClauseOccur.MUST);
	}

	@Reference
	SearchRequestBuilderFactory _searchRequestBuilderFactory;

	@Reference
	Queries _queries;

	@Reference
	Sorts _sorts;
}
