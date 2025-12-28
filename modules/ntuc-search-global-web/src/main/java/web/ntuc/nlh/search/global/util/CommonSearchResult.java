package web.ntuc.nlh.search.global.util;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetVocabularyLocalServiceUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
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
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.search.legacy.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.query.Queries;
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
import web.ntuc.nlh.search.global.constants.SearchGlobalPortletKeys;

public class CommonSearchResult {
	private static Log log = LogFactoryUtil.getLog(CommonSearchResult.class);

	public List<AssetCategory> getGlobalCategory(long globalGroupId) {
		ParameterGroup groupGlobalParam = ParameterGroupLocalServiceUtil
				.getByCode(SearchGlobalPortletKeys.PARAMETER_GLOBAL_GROUP_CODE, false);
		long paramGroupId = groupGlobalParam.getGroupId();
		Parameter globalTopicParam = ParameterLocalServiceUtil.getByGroupCode(paramGroupId,
				groupGlobalParam.getParameterGroupId(), SearchGlobalPortletKeys.PARAMETER_GLOBAL_SEARCH_TOPIC_CODE,
				false);
		AssetVocabulary topicVocabulary = AssetVocabularyLocalServiceUtil.fetchGroupVocabulary(globalGroupId,
				globalTopicParam.getParamValue());

		OrderByComparator<AssetCategory> order = null;
		return AssetCategoryLocalServiceUtil
				.getVocabularyCategories(topicVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, order)
				.stream().filter(category -> category.getName().equals(SearchGlobalPortletKeys.COURSE_CATEGORY_NAME))
				.collect(Collectors.toList());
	}

	public Map<String, Object> buildResultSummary(List<Long> categoryIds, SearchContext searchContext,
			ThemeDisplay themeDisplay, HttpServletRequest request, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws Exception {

		List<SearchResultSummaryDisplayContext> results = new ArrayList<>();
		Map<String, Object> processMap = this.processHits(categoryIds, searchContext, themeDisplay,
				resourceRequest, resourceResponse);
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
		returnedResult.put(SearchGlobalPortletKeys.SEARCH_GLOBAL_RESULTS, results);
		return returnedResult;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> processHits(List<Long> categoryIds, SearchContext searchContext,
			ThemeDisplay themeDisplay, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws Exception {
		Indexer<JournalArticle> indexer = IndexerRegistryUtil.getIndexer(JournalArticle.class);

		BooleanQuery mainQuery = new BooleanQueryImpl();
		List<ParameterGroup> groupCourseAdmins = ParameterGroupLocalServiceUtil
				.getByGroupIdCode(themeDisplay.getScopeGroupId(), SearchGlobalPortletKeys.PARAMETER_COURSE_ADMIN_GROUP_CODE, false);
		ParameterGroup groupCourseAdmin = groupCourseAdmins.get(0);
		long siteGroupId = groupCourseAdmin.getGroupId();

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
		returnedMap.put(SearchGlobalPortletKeys.HITS, hits);
		log.info("hits.getLength() = " + hits.getLength());

		return returnedMap;
	}

	@Reference
	SearchRequestBuilderFactory _searchRequestBuilderFactory;

	@Reference
	Queries _queries;

	@Reference
	Sorts _sorts;
}
