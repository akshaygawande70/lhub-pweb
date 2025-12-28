package web.ntuc.nlh.search.result.util;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalServiceUtil;
import com.liferay.asset.kernel.service.persistence.AssetCategoryPersistence;
import com.liferay.asset.kernel.service.persistence.AssetCategoryUtil;
import com.liferay.asset.kernel.service.persistence.AssetVocabularyPersistence;
import com.liferay.asset.kernel.service.persistence.AssetVocabularyUtil;
import com.liferay.data.engine.rest.dto.v2_0.DataDefinitionField.IndexType;
import com.liferay.dynamic.data.mapping.kernel.DDMStructureManagerUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.journal.model.JournalArticle;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
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
import com.liferay.portal.kernel.search.facet.faceted.searcher.FacetedSearcher;
import com.liferay.portal.kernel.search.facet.faceted.searcher.FacetedSearcherManagerUtil;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.NestedQuery;
import com.liferay.portal.kernel.search.generic.QueryTermImpl;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import api.ntuc.nlh.content.engine.SearchResultSummaryDisplayContext;
import api.ntuc.nlh.content.engine.ThemeDisplaySupplier;
import api.ntuc.nlh.content.util.ContentUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.nlh.search.result.constants.SearchResultPortletKeys;
import web.ntuc.nlh.search.result.dto.SearchResultDto;

public class CommonSearchResult {
	private static Log log = LogFactoryUtil.getLog(CommonSearchResult.class);
	
	@Reference
	DDMIndexer _ddmIndexer;

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

	public List<AssetCategory> getGlobalCategory(long globalGroupId) {
		ParameterGroup groupGlobalParam = ParameterGroupLocalServiceUtil
				.getByCode(SearchResultPortletKeys.PARAMETER_GLOBAL_GROUP_CODE, false);
		long paramGroupId = groupGlobalParam.getGroupId();
		Parameter globalTopicParam = ParameterLocalServiceUtil.getByGroupCode(paramGroupId,
				groupGlobalParam.getParameterGroupId(), SearchResultPortletKeys.PARAMETER_GLOBAL_SEARCH_TOPIC_CODE,
				false);
		AssetVocabulary topicVocabulary = AssetVocabularyLocalServiceUtil.fetchGroupVocabulary(globalGroupId,
				globalTopicParam.getParamValue());

		OrderByComparator<AssetCategory> order = null;
		return AssetCategoryLocalServiceUtil
				.getVocabularyCategories(topicVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, order)
				.stream().filter(category -> category.getName().equals(SearchResultPortletKeys.COURSE_CATEGORY_NAME))
				.collect(Collectors.toList());

	}
	
	
	public int getDtoResultCount(long globalGroupId, long localGroupId, String categoryName,
			SearchContext searchContext, ThemeDisplay themeDisplay, HttpServletRequest request,
			ResourceRequest resourceRequest, ResourceResponse resourceResponse, int course) {
		int hitCount = 0;
		try {
			List<Long> assetCategoryIds = new ArrayList<>();

			 hitCount = this.buildResultSummaryCount(assetCategoryIds, searchContext, themeDisplay,
					request, resourceRequest, resourceResponse, course, false, _ddmIndexer, null, _sorts, _queries,
					_searchRequestBuilderFactory, _categoryFacetFactory, _aggregations, _searchResponseBuilderFactory);

		} catch (Exception e) {
			log.error("Error while searching result data : " + e.getMessage());
		}
		return hitCount;
	}
	
	public List<SearchResultDto> getDtoResult(long globalGroupId, long localGroupId, String categoryName,
			SearchContext searchContext, ThemeDisplay themeDisplay, HttpServletRequest request,
			ResourceRequest resourceRequest, ResourceResponse resourceResponse, int course) {
		List<SearchResultDto> result = null;

		try {
			List<Long> assetCategoryIds = new ArrayList<>();

			Map<String, Object> mapResult = this.buildResultSummary(assetCategoryIds, searchContext, themeDisplay,
					request, resourceRequest, resourceResponse, course, false, _ddmIndexer, null, _sorts, _queries,
					_searchRequestBuilderFactory, _categoryFacetFactory, _aggregations, _searchResponseBuilderFactory);

			@SuppressWarnings("unchecked")
			List<SearchResultSummaryDisplayContext> resultOther = (List<SearchResultSummaryDisplayContext>) mapResult
					.get(SearchResultPortletKeys.SEARCH_RESULTS);

			List<SearchResultDto> dtos = new ArrayList<>();
			int tempCount2 =0;
			for (SearchResultSummaryDisplayContext res : resultOther) {
				tempCount2 = tempCount2+1;
				log.info("23102023======================2================================== START tempcount2:::::::::::"+tempCount2 );
				SearchResultDto v = new SearchResultDto();
				v.setTitle(res.getHighlightedTitle());
				v.setDesc(res.getContent());
				if (res.getClassName().equals(JournalArticle.class.getName())) {
					JournalArticle journalArticle = (JournalArticle) res.getAssetObject();
					
					String xmlContent = journalArticle.getContent();
					com.liferay.portal.kernel.xml.Document document = SAXReaderUtil.read(xmlContent);
					String url = "";
					if (categoryName.equals(SearchResultPortletKeys.PRESS_CATEGORY_NAME)) {
						url = ContentUtil.getWebContentVal(document, 1, "Link", themeDisplay);
					} else if (categoryName.equals(SearchResultPortletKeys.PAGES_CATEGORY_NAME)) {
						url = ContentUtil.getWebContentVal(document, 1, "linkToPage", themeDisplay);
					} else if (categoryName.equals(SearchResultPortletKeys.NEWS_CATEGORY_NAME)) {
						url = "/lhub-in-the-news";
					}
					
					String finalUrl = url.split("\\?")[0];
					v.setUrlMore(finalUrl);
					v.setDate("Publish Date : " + new SimpleDateFormat("dd MMM yyyy").format(journalArticle.getDisplayDate()));
					document = null;
				}

				// add data when viewURL not null
				if (Validator.isNull(v.getUrlMore())) {
					if (res.isAssetRendererURLDownloadVisible()) {
						v.setUrlMore(res.getAssetRendererURLDownload());
					} else {
						if (Validator.isNotNull(res.getViewURL())) {
							v.setUrlMore(res.getViewURL());
						}
					}
				}

				dtos.add(v);
			}
			result = dtos;
			
			Collections.sort(result, new Comparator<SearchResultDto>() {
				@Override
				public int compare(SearchResultDto u1, SearchResultDto u2) {
					try {
						Date u1Date = new SimpleDateFormat("dd MMM yyyy").parse(u1.getDate().replace("Publish Date : ", ""));
						Date u2Date = new SimpleDateFormat("dd MMM yyyy").parse(u2.getDate().replace("Publish Date : ", ""));
						return u2Date.compareTo(u1Date);
					} catch (java.text.ParseException e) {
						return 0;						
					}
				}
			});
		} catch (Exception e) {
			log.error("Error while searching result data : " + e.getMessage());
		}
		return result;
	}
	
	public static List<String> getDDMStrunctureKey(String vocabularyName)throws Exception{
		List<String> ddmStructuresKeys = new ArrayList<String>();
		try{
			AssetVocabularyPersistence avp=AssetVocabularyUtil.getPersistence();
			Class<?> avClazz = avp.getClass();
			ClassLoader avClassLoader = avClazz.getClassLoader();
			
			AssetCategoryPersistence acp=AssetCategoryUtil.getPersistence();
			Class<?> acClazz = acp.getClass();
			ClassLoader acClassLoader = acClazz.getClassLoader();
			
			DynamicQuery dqVoc=DynamicQueryFactoryUtil
	    		.forClass(AssetVocabulary.class,avClassLoader);
			dqVoc.add(RestrictionsFactoryUtil.eq("name", vocabularyName));
			List<AssetVocabulary> vocabularies=AssetVocabularyLocalServiceUtil.dynamicQuery(dqVoc);

			for(AssetVocabulary s : vocabularies) {
//				log.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx ");
//				log.info("vocabularies name : " + s.getName());
//				log.info("vocabularies getDescription : " + s.getDescription());
//				log.info("vocabularies getTitle : " + s.getTitle());
				for(Long l : s.getSelectedClassTypePKs()) {
//					log.info("LONG : " + l);
					com.liferay.dynamic.data.mapping.kernel.DDMStructure ddm = DDMStructureManagerUtil.fetchStructure(l);
					ddmStructuresKeys.add(ddm.getStructureKey());
				}
//				log.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx ");
			}
			
			
			
		}catch(Exception e){
			log.error("Error while get categories, "+e.getMessage());
		}
		return ddmStructuresKeys;
	}

	public Map<String, AssetVocabulary> getLocalThemeAndTopicVocabulary(long localGroupId) {
		try {
			List<ParameterGroup> courseCategoryParameterGroups = ParameterGroupLocalServiceUtil
					.getByGroupIdCode(localGroupId, SearchResultPortletKeys.COURSE_CATEGORY_GROUP_CODE, false);
			ParameterGroup courseCategoryParameterGroup = courseCategoryParameterGroups.get(0);
			Parameter courseTopicParameter = ParameterLocalServiceUtil.getByGroupCode(
					courseCategoryParameterGroup.getGroupId(), courseCategoryParameterGroup.getParameterGroupId(),
					SearchResultPortletKeys.COURSE_TOPIC_NAME, false);
			Parameter courseThemeParameter = ParameterLocalServiceUtil.getByGroupCode(
					courseCategoryParameterGroup.getGroupId(), courseCategoryParameterGroup.getParameterGroupId(),
					SearchResultPortletKeys.COURSE_THEME_NAME, false);

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
	
	public int buildResultSummaryCount(List<Long> categoryIds, SearchContext searchContext,
			ThemeDisplay themeDisplay, HttpServletRequest request, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse, int course, boolean isFilter, DDMIndexer ddmIndexer,
			JSONObject filterJson, Sorts sorts, Queries queries,
			SearchRequestBuilderFactory searchRequestBuilderFactory, CategoryFacetFactory categoryFacetFactory,
			Aggregations aggregations, SearchResponseBuilderFactory searchResponseBuilderFactory) throws Exception {

		List<SearchResultSummaryDisplayContext> results = new ArrayList<>();
		Map<String, Object> processMap = this.processHits(categoryIds, searchContext, themeDisplay, request,
				resourceRequest, resourceResponse, course, isFilter, ddmIndexer, filterJson, sorts, queries,
				searchRequestBuilderFactory, categoryFacetFactory, aggregations, searchResponseBuilderFactory);
		Hits hits = (Hits) processMap.get("hits");
		List<Document> documents = hits.toList();
		log.info("documents size :: "+documents.size());
		log.info("hits size :: "+hits.getLength());
		return hits.getLength();
	}

	public Map<String, Object> buildResultSummary(List<Long> categoryIds, SearchContext searchContext,
			ThemeDisplay themeDisplay, HttpServletRequest request, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse, int course, boolean isFilter, DDMIndexer ddmIndexer,
			JSONObject filterJson, Sorts sorts, Queries queries,
			SearchRequestBuilderFactory searchRequestBuilderFactory, CategoryFacetFactory categoryFacetFactory,
			Aggregations aggregations, SearchResponseBuilderFactory searchResponseBuilderFactory) throws Exception {

		List<SearchResultSummaryDisplayContext> results = new ArrayList<>();
		Map<String, Object> processMap = this.processHits(categoryIds, searchContext, themeDisplay, request,
				resourceRequest, resourceResponse, course, isFilter, ddmIndexer, filterJson, sorts, queries,
				searchRequestBuilderFactory, categoryFacetFactory, aggregations, searchResponseBuilderFactory);
		Hits hits = (Hits) processMap.get("hits");
		List<Document> documents = hits.toList();
		log.info("documents size :: "+documents.size());
		log.info("hits size :: "+hits.getLength());
		int tempCount1 =0;
		for (Document document : documents) {
			tempCount1 = tempCount1+1;
			log.info("23102023======================================================== START tempcount1:::::::::::::"+tempCount1 );
//			for (Map.Entry<String, Field> entry : document.getFields().entrySet()) {
//				log.info("Key : " + entry.getKey() + " Value : " + entry.getValue().getValue());
//			}
//			DocumentMediaUtil.generateThumbnailURL(fileEntryId, themeDisplay);
//			log.info("======================================================== END" );
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
			//searchResultSummaryDisplayBuilder.setResourceActions(ResourceActionsUtil.getResourceActions());
			ThemeDisplaySupplier themeDisplaySupplier = new PortletRequestThemeDisplaySupplier(resourceRequest);
			SearchResultPreferences searchResultPreferences = new SearchPortletSearchResultPreferences(
					resourceRequest.getPreferences(), themeDisplaySupplier);
			searchResultSummaryDisplayBuilder.setSearchResultPreferences(searchResultPreferences);
			searchResultSummaryDisplayBuilder.setThemeDisplay(themeDisplay);

			SearchResultSummaryDisplayContext searchResultSummaryDisplayContext = searchResultSummaryDisplayBuilder
					.build();

			results.add(searchResultSummaryDisplayContext);
		}

		Map<String, Object> returnedResult = new HashMap<>();
		returnedResult.put(SearchResultPortletKeys.SEARCH_RESULTS, results);
		returnedResult.put(SearchResultPortletKeys.DISTINCT_CATEGORY_IDS,
				processMap.get(SearchResultPortletKeys.DISTINCT_CATEGORY_IDS));
		returnedResult.put(SearchResultPortletKeys.MAX_PRICE, processMap.get(SearchResultPortletKeys.MAX_PRICE));
		return returnedResult;
	}

	public Map<String, Object> getHitsCount(List<Long> categoryIds, SearchContext searchContext,
			ThemeDisplay themeDisplay, HttpServletRequest request, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse, int course, boolean isFilter, DDMIndexer ddmIndexer,
			JSONObject filterJson, Sorts sorts, Queries queries,
			SearchRequestBuilderFactory searchRequestBuilderFactory, CategoryFacetFactory categoryFacetFactory,
			Aggregations aggregation, SearchResponseBuilderFactory searchResponseBuilderFactory) throws Exception {
		Map<String, Object> mapResult = this.processHits(categoryIds, searchContext, themeDisplay, request,
				resourceRequest, resourceResponse, course, isFilter, ddmIndexer, filterJson, sorts, queries,
				searchRequestBuilderFactory, categoryFacetFactory, aggregation, searchResponseBuilderFactory);
		Hits hits = (Hits) mapResult.get(SearchResultPortletKeys.HITS);
		Double maxPrice = (Double) mapResult.get(SearchResultPortletKeys.MAX_PRICE);
		Map<String, Object> returnedMap = new HashMap<>();
		returnedMap.put("total", hits.getLength());
		returnedMap.put("maxPrice", maxPrice);
		return returnedMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> processHits(List<Long> categoryIds, SearchContext searchContext,
			ThemeDisplay themeDisplay, HttpServletRequest request, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse, int course, boolean isFilter, DDMIndexer ddmIndexer,
			JSONObject filterJson, Sorts sorts, Queries queries,
			SearchRequestBuilderFactory searchRequestBuilderFactory, CategoryFacetFactory categoryFacetFactory,
			Aggregations aggregations, SearchResponseBuilderFactory searchResponseBuilderFactory) throws Exception {
		Indexer<JournalArticle> indexer = IndexerRegistryUtil.getIndexer(JournalArticle.class);

		BooleanQuery mainQuery = new BooleanQueryImpl();
		List<ParameterGroup> groupCourseAdmins = ParameterGroupLocalServiceUtil
				.getByGroupIdCode(themeDisplay.getScopeGroupId(), SearchResultPortletKeys.PARAMETER_COURSE_ADMIN_GROUP_CODE, false);
		ParameterGroup groupCourseAdmin = groupCourseAdmins.get(0);
		long siteGroupId = groupCourseAdmin.getGroupId();

		if (course == 1) {
			BooleanQuery themeQuery = new BooleanQueryImpl();
			BooleanQuery topicQuery = new BooleanQueryImpl();
			BooleanQuery priceQuery = new BooleanQueryImpl();
			BooleanQuery fundedQuery = new BooleanQueryImpl();
			Parameter structureParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					groupCourseAdmin.getParameterGroupId(), SearchResultPortletKeys.PARAMETER_COURSE_STRUCTURE_CODE,
					false);
			DynamicQuery structureQuery = DDMStructureLocalServiceUtil.dynamicQuery();
			structureQuery.add(PropertyFactoryUtil.forName("name").like("%>" + structureParam.getParamValue() + "<%"));
			structureQuery.add(PropertyFactoryUtil.forName("groupId").eq(siteGroupId));
			List<DDMStructure> ddmStructures = DDMStructureLocalServiceUtil.dynamicQuery(structureQuery, 0, 1);
			mainQuery.addRequiredTerm("ddmStructureKey", ddmStructures.get(0).getStructureKey());
			searchRequestBuilderFactory.builder(searchContext).fetchSource(true).build();

			TermsAggregation assetCategoryAggregation = aggregations.terms(SearchResultPortletKeys.ASSET_CATEGORY_AGGS,
					Field.ASSET_CATEGORY_IDS);
			assetCategoryAggregation.setSize(500);
			String priceFieldKey = this.getDDMSearchField(ddmStructures.get(0), "price", false);
			String priceValueKey = StringBundler.concat(SearchResultPortletKeys.DDM_FIELD_ARRAY, StringPool.PERIOD,
					ddmIndexer.getValueFieldName(IndexType.KEYWORD.getValue() + "_Number_sortable"));
			MaxAggregation maxAggregation = aggregations.max(SearchResultPortletKeys.MAX_PRICE_AGGS, priceValueKey);
			FilterAggregation filterAggregation = aggregations.filter(SearchResultPortletKeys.FILTER_AGGS,
					queries.term(SearchResultPortletKeys.DDM_FIELD_ARRAY + "." + SearchResultPortletKeys.DDM_FIELD_NAME,
							priceFieldKey));
			filterAggregation.addChildAggregation(maxAggregation);
			NestedAggregation nestedAggregation = aggregations.nested(SearchResultPortletKeys.NESTED_AGGS,
					SearchResultPortletKeys.DDM_FIELD_ARRAY);
			nestedAggregation.addChildAggregation(filterAggregation);
			searchRequestBuilderFactory.builder(searchContext).addAggregation(nestedAggregation).build();
			searchRequestBuilderFactory.builder(searchContext).addAggregation(assetCategoryAggregation).build();

			if (Validator.isNotNull(filterJson)) {
				priceQuery = this.priceFilterElastic(filterJson.getJSONObject("price"), ddmStructures.get(0),
						ddmIndexer);
				fundedQuery = this.fundedFilterElastic(filterJson.getJSONArray("funded"), ddmStructures.get(0),
						ddmIndexer);
				themeQuery = this.categoryFilterElastic(filterJson.getJSONArray("themeIds"));
				topicQuery = this.categoryFilterElastic(filterJson.getJSONArray("topicIds"));
				searchRequestBuilderFactory.builder(searchContext).sorts(this.priceSortElastic(
						filterJson.getString("sort"), ddmStructures.get(0), ddmIndexer, sorts, queries));
			}

			NestedQuery nestedPriceQuery = new NestedQuery(SearchResultPortletKeys.DDM_FIELD_ARRAY, priceQuery);
			NestedQuery nestedFundedQuery = new NestedQuery(SearchResultPortletKeys.DDM_FIELD_ARRAY, fundedQuery);

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
		}

		FacetedSearcher facetedSearcher = FacetedSearcherManagerUtil.createFacetedSearcher();
		Hits hits = facetedSearcher.search(searchContext);
		Map<String, Object> returnedMap = new HashMap<>();
		returnedMap.put(SearchResultPortletKeys.HITS, hits);
		if (course == 1) {
			SearchResponse res = searchResponseBuilderFactory.builder(searchContext).build();
			TermsAggregationResult termsAggregationResult = (TermsAggregationResult) res
					.getAggregationResult(SearchResultPortletKeys.ASSET_CATEGORY_AGGS);
			int bucketSize = termsAggregationResult.getBuckets().size();
			long[] distinctCategoryIds = new long[bucketSize];
			int i = 0;
			for (Bucket b : termsAggregationResult.getBuckets()) {
				distinctCategoryIds[i] = Long.valueOf(b.getKey());
				i++;
			}
			returnedMap.put(SearchResultPortletKeys.DISTINCT_CATEGORY_IDS, distinctCategoryIds);

			NestedAggregationResult nestedAggregationResult = (NestedAggregationResult) res
					.getAggregationResult(SearchResultPortletKeys.NESTED_AGGS);
			FilterAggregationResult filterAggregationResult = (FilterAggregationResult) nestedAggregationResult
					.getChildAggregationResult(SearchResultPortletKeys.FILTER_AGGS);
			MaxAggregationResult maxAggregationResult = (MaxAggregationResult) filterAggregationResult
					.getChildAggregationResult(SearchResultPortletKeys.MAX_PRICE_AGGS);
			double maxPrice = maxAggregationResult.getValue();
			returnedMap.put(SearchResultPortletKeys.MAX_PRICE, maxPrice);
		}
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
			priceQuery.addRequiredTerm(StringBundler.concat(SearchResultPortletKeys.DDM_FIELD_ARRAY, StringPool.PERIOD,
					SearchResultPortletKeys.DDM_FIELD_NAME), priceField);
			BooleanQuery rangeQuery = new BooleanQueryImpl();
			rangeQuery.addRangeTerm(
					StringBundler.concat(SearchResultPortletKeys.DDM_FIELD_ARRAY, StringPool.PERIOD,
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
		String sortableFieldName = StringBundler.concat(SearchResultPortletKeys.DDM_FIELD_ARRAY, StringPool.PERIOD,
				ddmIndexer.getValueFieldName(IndexType.KEYWORD.getValue() + "_Number_sortable"));
		String priceField = this.getDDMSearchField(ddmStructure, "price", false);
		FieldSort fieldSort = sorts.field(sortableFieldName, sortOrder);
		NestedSort nestedSort = sorts.nested(SearchResultPortletKeys.DDM_FIELD_ARRAY);

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
				fundedQuery.addRequiredTerm(StringBundler.concat(SearchResultPortletKeys.DDM_FIELD_ARRAY,
						StringPool.PERIOD, SearchResultPortletKeys.DDM_FIELD_NAME), fundedField);
				BooleanQuery eachFundedQuery = new BooleanQueryImpl();
				for (int i = 0; i < fundedJson.length(); i++) {
					Query query = new TermQueryImpl(
							StringBundler.concat(SearchResultPortletKeys.DDM_FIELD_ARRAY, StringPool.PERIOD,
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
		String fieldName = SearchResultPortletKeys.FIELD_PREFIX + structure.getStructureId() + "__" + searchfield;
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
