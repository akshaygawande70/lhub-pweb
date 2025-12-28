package web.ntuc.nlh.course.admin.util;

import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
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
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.search.legacy.searcher.SearchRequestBuilderFactory;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.MimeResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import api.ntuc.nlh.content.engine.PortletRequestThemeDisplaySupplier;
import api.ntuc.nlh.content.engine.PortletURLFactory;
import api.ntuc.nlh.content.engine.PortletURLFactoryImpl;
import api.ntuc.nlh.content.engine.SearchPortletSearchResultPreferences;
import api.ntuc.nlh.content.engine.SearchResultPreferences;
//import api.ntuc.nlh.content.engine.SearchResultSummaryDisplayBuilder;
import api.ntuc.nlh.content.engine.SearchResultSummaryDisplayContext;
import api.ntuc.nlh.content.engine.ThemeDisplaySupplier;
import api.ntuc.nlh.content.util.ContentUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.nlh.course.admin.constants.CourseAdminWebPortletKeys;
import web.ntuc.nlh.course.admin.dto.ResultDto;
import web.ntuc.nlh.course.admin.dto.ResultSummaryDisplayContextDto;
import web.ntuc.nlh.course.admin.dto.SearchResultDto;

public class CourseSearch {
	private static final Log log = LogFactoryUtil.getLog(CourseSearch.class);
	private static String globalGroupId = "groupId";
	
	public static ResultDto searchCourse(ResourceRequest resourceRequest, ResourceResponse resourceResponse, ThemeDisplay themeDisplay,int start, int end, String keywords, Boolean paginate,SearchRequestBuilderFactory _searchRequestBuilderFactory) throws Exception {
		HttpServletRequest request = PortalUtil.getHttpServletRequest(resourceRequest);
		List<SearchResultDto> courses = new ArrayList<>();
		SearchContext searchContext = SearchContextFactory.getInstance(request);
		searchContext.setAttribute("paginationType", "more");
		searchContext.getQueryConfig().setCollatedSpellCheckResultEnabled(true);
		searchContext.getQueryConfig().setCollatedSpellCheckResultScoresThreshold(200);
		searchContext.getQueryConfig().setQueryIndexingEnabled(false);
		searchContext.getQueryConfig().setQueryIndexingThreshold(50);
		searchContext.getQueryConfig().setQuerySuggestionEnabled(true);
		searchContext.getQueryConfig().setQuerySuggestionScoresThreshold(0);
		searchContext.setKeywords(keywords);
		if(paginate) {
			searchContext.setStart(start);
			searchContext.setEnd(end);
		}
		
		ResultSummaryDisplayContextDto resultSummaryDisplayContextDto = buildResultSummary(searchContext, themeDisplay, request, resourceRequest, resourceResponse, _searchRequestBuilderFactory);
		List<SearchResultSummaryDisplayContext> resultCourse = resultSummaryDisplayContextDto.getCourses();
		if (Validator.isNotNull(resultCourse)) {
			for (SearchResultSummaryDisplayContext res : resultCourse) {
				SearchResultDto v = new SearchResultDto();
				v.setDesc(res.getContent());
				if (res.getClassName().equals(JournalArticle.class.getName())) {
					JournalArticle journalArticle = (JournalArticle) res.getAssetObject();

					String xmlContent = journalArticle.getContent();
					com.liferay.portal.kernel.xml.Document document = SAXReaderUtil.read(xmlContent);
					String status = journalArticle.getStatus() == 0 ? "APPROVE" : journalArticle.getStatus() == 2 ? "DRAFT" : "PENDING";
					String courseCode = ContentUtil.getWebContentVal(document, 1, "courseCode", themeDisplay);
					String title = ContentUtil.getWebContentVal(document, 1, "courseTitle", themeDisplay);
					String price = ContentUtil.getWebContentVal(document, 1, "price", themeDisplay);
					String funded = ContentUtil.getWebContentVal(document, 1, "funded", themeDisplay);
					String popular = ContentUtil.getWebContentVal(document, 1, "popular", themeDisplay);
					
					if (price.equals("")) {
						v.setPrice(0.0);
					}
					v.setPrice(Double.valueOf(price));
					
					if (popular.equals("true")) {
						v.setPopular("TRUE");
					} else {
						v.setPopular("FALSE");
					}
					
					if (funded.equals("true")) {
						v.setFunded("TRUE");
					} else {
						v.setFunded("FALSE");
					}
					
					v.setTitle(title.toUpperCase());
					v.setStatus(status);
					v.setCourseCode(courseCode.toUpperCase());
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
				courses.add(v);
			}
		}
		ResultDto resultDto = new ResultDto(courses, resultSummaryDisplayContextDto.getCount());
		return resultDto;
	}

	private static ResultSummaryDisplayContextDto buildResultSummary(SearchContext searchContext,
			ThemeDisplay themeDisplay, HttpServletRequest request, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse,SearchRequestBuilderFactory _searchRequestBuilderFactory) throws Exception {

		List<SearchResultSummaryDisplayContext> res = new ArrayList<>();
		Hits hits = processHits(searchContext, themeDisplay, request, resourceRequest, resourceResponse,_searchRequestBuilderFactory);
//		log.info("hits length = "+hits.getLength());
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

			res.add(searchResultSummaryDisplayContext);
		}
		
		ResultSummaryDisplayContextDto result = new ResultSummaryDisplayContextDto(res, hits.getLength());

		return result;
	}

	private static Hits processHits(SearchContext searchContext, ThemeDisplay themeDisplay, HttpServletRequest request,
			ResourceRequest resourceRequest, ResourceResponse resourceResponse,SearchRequestBuilderFactory _searchRequestBuilderFactory) throws Exception {
		
		Indexer<JournalArticle> indexer = IndexerRegistryUtil.getIndexer(JournalArticle.class);
		
		BooleanQuery mainQuery = new BooleanQueryImpl();
		List<ParameterGroup> groupCourseAdmins = ParameterGroupLocalServiceUtil.getByGroupIdCode(
				themeDisplay.getScopeGroupId(), CourseAdminWebPortletKeys.PARAMETER_COURSE_ADMIN_GROUP_CODE, false);
		ParameterGroup groupCourseAdmin = groupCourseAdmins.get(0);
		long siteGroupId = groupCourseAdmin.getGroupId();

		Parameter structureParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
				groupCourseAdmin.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_COURSE_STRUCTURE_CODE,
				false);
		DynamicQuery structureQuery = DDMStructureLocalServiceUtil.dynamicQuery();
		structureQuery.add(PropertyFactoryUtil.forName("name").like("%>" + structureParam.getParamValue() + "<%"));
		structureQuery.add(PropertyFactoryUtil.forName("groupId").eq(siteGroupId));
		List<DDMStructure> ddmStructures = DDMStructureLocalServiceUtil.dynamicQuery(structureQuery, 0, 1);
		log.info("ddmStructureKey = "+ddmStructures.get(0).getStructureKey());
		mainQuery.addRequiredTerm("ddmStructureKey", ddmStructures.get(0).getStructureKey());
		
		Parameter templateParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
				groupCourseAdmin.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_COURSE_TEMPLATE_CODE,
				false);
		DynamicQuery templateQuery = DDMTemplateLocalServiceUtil.dynamicQuery();
		templateQuery.add(PropertyFactoryUtil.forName("name").like("%>" + templateParam.getParamValue() + "<%"));
		templateQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(siteGroupId));
		List<DDMTemplate> ddmTemplates = DDMTemplateLocalServiceUtil.dynamicQuery(templateQuery);
		mainQuery.addRequiredTerm("ddmTemplateKey", ddmTemplates.get(0).getTemplateKey());
		
		_searchRequestBuilderFactory.builder(searchContext).fetchSource(true).build();
		BooleanQuery statusQuery = new BooleanQueryImpl();
		Query queryPending = new TermQueryImpl(Field.STATUS, String.valueOf(WorkflowConstants.STATUS_PENDING));
		Query queryApprove = new TermQueryImpl(Field.STATUS, String.valueOf(WorkflowConstants.STATUS_APPROVED));
		Query queryDraft = new TermQueryImpl(Field.STATUS, String.valueOf(WorkflowConstants.STATUS_DRAFT));
		statusQuery.add(queryApprove, BooleanClauseOccur.SHOULD);
		statusQuery.add(queryPending, BooleanClauseOccur.SHOULD);
		statusQuery.add(queryDraft, BooleanClauseOccur.SHOULD);
		mainQuery.add(statusQuery, BooleanClauseOccur.MUST);
		mainQuery.addRequiredTerm(Field.GROUP_ID, siteGroupId);
		BooleanClause<Query> booleanClause = BooleanClauseFactoryUtil.create(mainQuery,
				BooleanClauseOccur.MUST.getName());
		searchContext.setBooleanClauses(new BooleanClause[] { booleanClause });
		searchContext.setAttribute("head", Boolean.FALSE);
		searchContext.setAttribute("latest", Boolean.TRUE);
		searchContext.setAttribute("status", WorkflowConstants.STATUS_ANY);
		Hits hits = indexer.search(searchContext);
		log.info("hits.getLength() = " + hits.getLength());
		return hits;
	}
}
