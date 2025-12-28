package api.ntuc.nlh.content.util;

import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.data.engine.rest.dto.v2_0.DataDefinitionField.IndexType;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.journal.model.JournalArticle;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
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
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.aggregation.Aggregations;
import com.liferay.portal.search.facet.category.CategoryFacetFactory;
import com.liferay.portal.search.legacy.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.legacy.searcher.SearchResponseBuilderFactory;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.sort.Sort;
import com.liferay.portal.search.sort.SortOrder;
import com.liferay.portal.search.sort.Sorts;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import api.ntuc.nlh.content.engine.SearchResultSummaryDisplayBuilder;
import api.ntuc.nlh.content.engine.SearchResultSummaryDisplayContext;
import api.ntuc.nlh.content.engine.ThemeDisplaySupplier;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;

public class CommonDateResult {
	private static Log log = LogFactoryUtil.getLog(CommonDateResult.class);
	private static String publishDateField = "publishDate_sortable";

	public static Map<String, Object> buildResultSummary(List<Long> categoryIds, SearchContext searchContext,
			ThemeDisplay themeDisplay, HttpServletRequest request, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse, boolean isFilter, DDMIndexer ddmIndexer, JSONObject filterJson,
			Sorts sorts, Queries queries, SearchRequestBuilderFactory searchRequestBuilderFactory,
			CategoryFacetFactory categoryFacetFactory, Aggregations aggregations,
			SearchResponseBuilderFactory searchResponseBuilderFactory, String searchResultKey, String adminGroupCodeKey,
			String hitsKey, String distinctCatIdKey, String type, String eventStructureKey, String fieldArrayKey,
			String fieldNameKey, String fieldPrefixKey) throws Exception {
		List<SearchResultSummaryDisplayContext> results = new ArrayList<>();
		Map<String, Object> processMap = processHits(categoryIds, searchContext, themeDisplay, request, resourceRequest,
				resourceResponse, isFilter, ddmIndexer, filterJson, sorts, queries, searchRequestBuilderFactory,
				categoryFacetFactory, aggregations, searchResponseBuilderFactory, searchResultKey, adminGroupCodeKey,
				hitsKey, distinctCatIdKey, type, eventStructureKey, fieldArrayKey, fieldNameKey, fieldPrefixKey);
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

		Map<String, Object> returnedResult = new HashMap<>();
		returnedResult.put(searchResultKey, results);
		returnedResult.put(distinctCatIdKey, processMap.get(distinctCatIdKey));
		return returnedResult;
	}

	public static Map<String, Object> getHitsCount(List<Long> categoryIds, SearchContext searchContext,
			ThemeDisplay themeDisplay, HttpServletRequest request, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse, boolean isFilter, DDMIndexer ddmIndexer, JSONObject filterJson,
			Sorts sorts, Queries queries, SearchRequestBuilderFactory searchRequestBuilderFactory,
			CategoryFacetFactory categoryFacetFactory, Aggregations aggregation,
			SearchResponseBuilderFactory searchResponseBuilderFactory, String searchResultKey, String adminGroupCodeKey,
			String hitsKey, String distinctCatIdKey, String type, String eventStructureKey, String fieldArrayKey,
			String fieldNameKey, String fieldPrefixKey) throws Exception {
		Map<String, Object> mapResult = processHits(categoryIds, searchContext, themeDisplay, request,
				resourceRequest, resourceResponse, isFilter, ddmIndexer, filterJson, sorts, queries,
				searchRequestBuilderFactory, categoryFacetFactory, aggregation, searchResponseBuilderFactory,
				searchResultKey, adminGroupCodeKey, hitsKey, distinctCatIdKey, type, eventStructureKey, fieldArrayKey,
				fieldNameKey, fieldPrefixKey);
		Hits hits = (Hits) mapResult.get(hitsKey);
		Map<String, Object> returnedMap = new HashMap<>();
		returnedMap.put("total", hits.getLength());
		return returnedMap;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> processHits(List<Long> categoryIds, SearchContext searchContext,
			ThemeDisplay themeDisplay, HttpServletRequest request, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse, boolean isFilter, DDMIndexer ddmIndexer, JSONObject filterJson,
			Sorts sorts, Queries queries, SearchRequestBuilderFactory searchRequestBuilderFactory,
			CategoryFacetFactory categoryFacetFactory, Aggregations aggregations,
			SearchResponseBuilderFactory searchResponseBuilderFactory, String searchResultKey, String adminGroupCodeKey,
			String hitsKey, String distinctCatIdKey, String type, String eventStructureKey, String fieldArrayKey,
			String fieldNameKey, String fieldPrefixKey) throws Exception {
		String localeUsed = themeDisplay.getLocale().toString();

		Indexer<JournalArticle> indexer = IndexerRegistryUtil.getIndexer(JournalArticle.class);
//		Buat Main Query
		BooleanQuery mainQuery = new BooleanQueryImpl();

		List<ParameterGroup> groupCourseAdmins = ParameterGroupLocalServiceUtil
				.getByGroupIdCode(themeDisplay.getScopeGroupId(), adminGroupCodeKey, false);
		ParameterGroup groupCourseAdmin = groupCourseAdmins.get(0);
		long siteGroupId = groupCourseAdmin.getGroupId();
		log.info(siteGroupId);

		BooleanQuery dateQuery = new BooleanQueryImpl();
		BooleanQuery categoryQuery = new BooleanQueryImpl();

		if (type.equals("event")) {
			Parameter structureParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					groupCourseAdmin.getParameterGroupId(), eventStructureKey, false);
			DynamicQuery structureQuery = DDMStructureLocalServiceUtil.dynamicQuery();
			structureQuery.add(PropertyFactoryUtil.forName("name").like("%>" + structureParam.getParamValue() + "<%"));
			structureQuery.add(PropertyFactoryUtil.forName("groupId").eq(siteGroupId));
			List<DDMStructure> ddmStructures = DDMStructureLocalServiceUtil.dynamicQuery(structureQuery, 0, 1);
			mainQuery.addRequiredTerm("ddmStructureKey", ddmStructures.get(0).getStructureKey());
			searchRequestBuilderFactory.builder(searchContext).fetchSource(true).build();

			if (Validator.isNotNull(filterJson)) {
				dateQuery = eventDateFilterElastic(filterJson.getLong("eventTypeId"), filterJson.getInt("years"),
						filterJson.getInt("month"), filterJson.getString("specifiedDate"), ddmStructures.get(0),
						ddmIndexer, localeUsed, fieldArrayKey, fieldNameKey, fieldPrefixKey);
				if (filterJson.getLong("categoryId") != 0) {
					categoryQuery = categoryFilterElastic(filterJson.getLong("categoryId"));
				}
				searchRequestBuilderFactory.builder(searchContext).sorts(dateSortElastic(sorts));
			}

			NestedQuery nestedEndDateQuery = new NestedQuery(fieldArrayKey, dateQuery);

			mainQuery.add(nestedEndDateQuery, BooleanClauseOccur.MUST);
			mainQuery.add(categoryQuery, BooleanClauseOccur.MUST);
		} else {
			if (Validator.isNotNull(filterJson)) {
				dateQuery = dateFilterElastic(filterJson.getLong("years"), filterJson.getLong("month"),
						filterJson.getString("specifiedDate"));
				searchRequestBuilderFactory.builder(searchContext).sorts(dateSortElastic(sorts));

				mainQuery.add(dateQuery, BooleanClauseOccur.MUST);
			}

		}

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
		returnedMap.put(hitsKey, hits);

		log.info("hits.getLength() = " + hits.getLength());

		return returnedMap;
	}

	public static BooleanQuery dateFilterElastic(Long years, Long month, String specifiedDate)
			throws java.text.ParseException {
		log.info("Year : " + years + " | Month : " + month + " | Date : " + specifiedDate);

		BooleanQuery yearQuery = new BooleanQueryImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat specifiedFormat = new SimpleDateFormat("dd MMM yyyy");

		long currentYear = (long) Calendar.getInstance().get(Calendar.YEAR);
		long rangeYear = currentYear - 4;
		String defaultStartDate = "0101000000";
		String defaultEndDate = "1231235959";
		String startDateStr = "";
		String endDateStr = "";
		long startDate;
		long endDate;
		long setYear = 0;

		if (years == 0 && month == 0) {
			setYear = currentYear;
		} else if (years != 0 && month == 0) {
			setYear = years;
			rangeYear = years;
		} else if (years != 0 && month != 0) {
			setYear = years;
			rangeYear = years;
			String startDateTemp = defaultStartDate.substring(2, 9);
			String endDateTemp = defaultEndDate.substring(2, 9);
			if (month < 10) {
				defaultStartDate = "0" + month + startDateTemp;
				defaultEndDate = "0" + month + endDateTemp;
			} else {
				defaultStartDate = month + startDateTemp;
				defaultEndDate = month + endDateTemp;
			}
		}

		if (!specifiedDate.isEmpty()) {
			Date specifiedTemp = specifiedFormat.parse(specifiedDate);
			startDateStr = sdf.format(specifiedTemp);
			endDateStr = startDateStr.substring(0, 8) + "235959";
		} else {
			startDateStr = rangeYear + defaultStartDate;
			endDateStr = setYear + defaultEndDate;
		}

		Date startDateTemp = sdf.parse(startDateStr);
		Date endDateTemp = sdf.parse(endDateStr);
		startDate = startDateTemp.getTime();
		endDate = endDateTemp.getTime();

		try {
			BooleanQuery rangeQuery = new BooleanQueryImpl();
			rangeQuery.addRangeTerm(publishDateField, startDate, endDate);
			yearQuery.add(rangeQuery, BooleanClauseOccur.MUST);
		} catch (ParseException e) {
			log.info(e.getMessage());
		}
		return yearQuery;
	}

	public static BooleanQuery eventDateFilterElastic(Long eventTypeId, int years, int months, String specifiedDate,
			DDMStructure ddmStructure, DDMIndexer ddmIndexer, String localeUsed, String fieldArrayKey,
			String fieldNameKey, String fieldPrefixKey) {
		BooleanQuery dateFilterQuery = new BooleanQueryImpl();
		try {
			String startDateStr = "";
			String endDateStr = "";

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat specifiedFormat = new SimpleDateFormat("dd MMM yyyy");
			LocalDate now = LocalDate.now();
			if (eventTypeId == 1) {
				if (years == 0 && months == 0) {
					startDateStr = dtf.format(now.minusYears(4));
					endDateStr = dtf.format(now.plusYears(4));
				} else if (years != 0 && months == 0) {
					startDateStr = dtf.format(LocalDate.of(years, Month.JANUARY, 1));
					endDateStr = dtf.format(LocalDate.of(years, Month.DECEMBER, 31));
				} else if (years != 0 && months != 0) {
					LocalDate startDate = LocalDate.of(years, Month.of(months), 1);
					LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
					startDateStr = dtf.format(startDate);
					endDateStr = dtf.format(endDate);
				}
			} else if (eventTypeId == 2) {
				startDateStr = dtf.format(now);
				endDateStr = dtf.format(now.plusYears(4));
			} else {
				if (years == 0 && months == 0) {
					startDateStr = dtf.format(now.minusYears(4));
					endDateStr = dtf.format(now);
				} else if (years != 0 && months == 0) {
					startDateStr = dtf.format(LocalDate.of(years, Month.JANUARY, 1));
					endDateStr = dtf.format(LocalDate.of(years, Month.DECEMBER, 31));
				} else if (years != 0 && months != 0) {
					LocalDate startDate = LocalDate.of(years, Month.of(months), 1);
					LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
					startDateStr = dtf.format(startDate);
					endDateStr = dtf.format(endDate);
				}

			}

			if (!specifiedDate.isEmpty()) {
				Date specifiedTemp = specifiedFormat.parse(specifiedDate);
				startDateStr = sdf.format(specifiedTemp);
				endDateStr = sdf.format(specifiedTemp);

			}

			String endDateField = getDDMSearchField(ddmStructure, "endDate", localeUsed, fieldPrefixKey);
			dateFilterQuery.addRequiredTerm(StringBundler.concat(fieldArrayKey, StringPool.PERIOD, fieldNameKey),
					endDateField);
			BooleanQuery rangeQuery = new BooleanQueryImpl();
			rangeQuery.addRangeTerm(
					StringBundler.concat(fieldArrayKey, StringPool.PERIOD,
							ddmIndexer.getValueFieldName(
									IndexType.KEYWORD.getValue() + "_" + localeUsed + "_String_sortable")),
					startDateStr, endDateStr);

			dateFilterQuery.add(rangeQuery, BooleanClauseOccur.MUST);
		} catch (ParseException | java.text.ParseException e) {
			log.info(e.getMessage());
		}
		return dateFilterQuery;
	}

	public static BooleanQuery categoryFilterElastic(Long categoryId) {
		BooleanQuery categoryQuery = new BooleanQueryImpl();
		try {
			Query query = new TermQueryImpl(Field.ASSET_CATEGORY_IDS, String.valueOf(categoryId));
			categoryQuery.add(query, BooleanClauseOccur.SHOULD);
		} catch (ParseException e) {
			log.info(e.getMessage());
		}

		return categoryQuery;
	}

	public static String getDDMSearchField(DDMStructure structure, String searchfield, String LocaleUsed,
			String fieldPrefixKey) {
		// ddm__keyword__12345__MenuDay_en_US
		String fieldName = fieldPrefixKey + structure.getStructureId() + "__" + searchfield;
		if (!LocaleUsed.isEmpty()) {
			fieldName = fieldName + "_" + LocaleUsed;
		}
		return fieldName;
	}

	public static Sort dateSortElastic(Sorts sorts) {
		SortOrder sortOrder = SortOrder.DESC;

		String sortableFieldName = publishDateField;
		return sorts.field(sortableFieldName, sortOrder);

	}

	@Reference
	SearchRequestBuilderFactory _searchRequestBuilderFactory;

	@Reference
	Queries _queries;

	@Reference
	Sorts _sorts;
}
