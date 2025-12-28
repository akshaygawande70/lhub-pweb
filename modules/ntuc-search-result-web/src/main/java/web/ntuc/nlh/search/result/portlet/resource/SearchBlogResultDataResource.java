package web.ntuc.nlh.search.result.portlet.resource;

import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.service.CPDefinitionLocalServiceUtil;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.facet.AssetEntriesFacet;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.ScopeFacet;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.persistence.GroupPersistence;
import com.liferay.portal.kernel.service.persistence.GroupUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.PortletCommandUtil;
import api.ntuc.nlh.content.engine.SearchResultSummaryDisplayContext;
import api.ntuc.nlh.content.util.ContentUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.nlh.search.result.constants.MVCCommandNames;
import web.ntuc.nlh.search.result.constants.SearchResultPortletKeys;
import web.ntuc.nlh.search.result.dto.SearchResultDto;
import web.ntuc.nlh.search.result.util.CommonSearchResult;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.SEARCH_BLOG_DATA_RESOURCES,
		"javax.portlet.name=" + SearchResultPortletKeys.SEARCH_RESULT_PORTLET }, service = MVCResourceCommand.class)
public class SearchBlogResultDataResource implements MVCResourceCommand {
	private static Log log = LogFactoryUtil.getLog(SearchBlogResultDataResource.class);
	CommonSearchResult common = new CommonSearchResult();

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("search other data resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long localGroupId = themeDisplay.getScopeGroupId();
			Company company = _companyLocalService.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
			long globalGroupId = company.getGroup().getGroupId();
			String keyword = ParamUtil.getString(resourceRequest, "keyword", " ");
			int course = ParamUtil.getInteger(resourceRequest, "course", 0);
			
			int pageNumber = ParamUtil.getInteger(resourceRequest, "pageNum");
			int limit = ParamUtil.getInteger(resourceRequest, "limit");
			String filter = ParamUtil.getString(resourceRequest, "filter");
			JSONObject filterJson = JSONFactoryUtil.createJSONObject(filter);
			int showFrom = limit * (pageNumber - 1);
			int showTo = showFrom + limit;
			
			log.info(">>>>>>>>>>>>>>>>>>keyword : " + keyword);
			
			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);
			
			HttpServletRequest request = PortalUtil.getHttpServletRequest(resourceRequest);

			SearchContext searchContext = SearchContextFactory.getInstance(request);
			searchContext.setKeywords("%"+keyword+"%");
//			searchContext.setAttribute("paginationType", "more");
			searchContext.getQueryConfig().setCollatedSpellCheckResultEnabled(true);
			searchContext.getQueryConfig().setCollatedSpellCheckResultScoresThreshold(200);
			searchContext.getQueryConfig().setQueryIndexingEnabled(false);
			searchContext.getQueryConfig().setQueryIndexingThreshold(50);
			searchContext.getQueryConfig().setQuerySuggestionEnabled(true);
			searchContext.getQueryConfig().setQuerySuggestionScoresThreshold(0);

			List<SearchResultDto> blogs = new ArrayList<>();
			
			String[] entryClassNames = null;
			BooleanQuery booleanQuery = new BooleanQueryImpl();
			//BLOGS_CATEGORY_NAME
			entryClassNames = new String[1];
			entryClassNames[0] = "com.liferay.journal.model.JournalArticle";
			List<String> ddmStructureKeys = CommonSearchResult.getDDMStrunctureKey(SearchResultPortletKeys.BLOGS_CATEGORY_NAME);
			booleanQuery = new BooleanQueryImpl();
			for (String ddm: ddmStructureKeys) {
				booleanQuery.addExactTerm("ddmStructureKey", ddm);
			}
			BooleanClause<Query> booleanClause = BooleanClauseFactoryUtil.create(booleanQuery,
					BooleanClauseOccur.MUST.getName());
			searchContext.setBooleanClauses(new BooleanClause[] { booleanClause });
			if (entryClassNames != null) {
				searchContext.setEntryClassNames(entryClassNames);
			}
			
			int resCount = common.getDtoResultCount(globalGroupId, localGroupId,
					SearchResultPortletKeys.BLOGS_CATEGORY_NAME, searchContext, themeDisplay, request,
					resourceRequest, resourceResponse, course);
			searchContext.setStart(showFrom);
			searchContext.setEnd(showTo);
			
			List<SearchResultDto> blogsResult = common.getDtoResult(globalGroupId, localGroupId,
					SearchResultPortletKeys.BLOGS_CATEGORY_NAME, searchContext, themeDisplay, request,
					resourceRequest, resourceResponse, course);
			
			if (Validator.isNotNull(blogsResult)) {
				blogs.addAll(blogsResult);
			}

			resourceResponse.setContentType("application/json");
			PrintWriter out = null;
			out = resourceResponse.getWriter();
			JSONObject rowData = JSONFactoryUtil.createJSONObject();
			rowData.put("blogs", blogs);
			rowData.put("status", HttpServletResponse.SC_OK);
			rowData.put("resCount", resCount);
			out.print(rowData.toString());
			out.flush();

		} catch (Exception e) {
			log.error("Error while searching other data : " + e.getMessage(), e);
			return true;
		}
		log.info("search result other data resources - end");
		return false;
	}

	

	@Reference
	CompanyLocalService _companyLocalService;
}
