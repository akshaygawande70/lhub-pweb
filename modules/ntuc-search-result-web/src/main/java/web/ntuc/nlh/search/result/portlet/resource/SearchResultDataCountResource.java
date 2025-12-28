package web.ntuc.nlh.search.result.portlet.resource;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
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
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
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
import web.ntuc.nlh.search.result.constants.MVCCommandNames;
import web.ntuc.nlh.search.result.constants.SearchResultMessageKey;
import web.ntuc.nlh.search.result.constants.SearchResultPortletKeys;
import web.ntuc.nlh.search.result.util.CommonSearchResult;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.SEARCH_DATA_COUNT_RESOURCES,
		"javax.portlet.name=" + SearchResultPortletKeys.SEARCH_RESULT_PORTLET }, service = MVCResourceCommand.class)
public class SearchResultDataCountResource implements MVCResourceCommand {
	private static Log log = LogFactoryUtil.getLog(SearchResultDataCountResource.class);
	private CommonSearchResult common = new CommonSearchResult();

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("search result data count resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			
			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

			//			get default company id for topic vocabulary
			Company company = _companyLocalService.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
			long globalGroupId = company.getGroup().getGroupId(); // themeDisplay.getScopeGroupId()
			String keyword = ParamUtil.getString(resourceRequest, "keyword", " ");

			String filter = ParamUtil.getString(resourceRequest, "filter");
			JSONObject filterJson = JSONFactoryUtil.createJSONObject(filter);
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
			Map<String, Object> mapResult = common.getHitsCount(categoryIds, searchContext, themeDisplay, request,
					resourceRequest, resourceResponse, course, false, _ddmIndexer, filterJson, _sorts, _queries,
					_searchRequestBuilderFactory, _categoryFacetFactory, _aggregations, _searchResponseBuilderFactory);
			int hitsCount = (int) mapResult.get("total");
			Double maxPrice = (Double) mapResult.get(SearchResultPortletKeys.MAX_PRICE);
			resourceResponse.setContentType("application/json");
			PrintWriter out = null;
			out = resourceResponse.getWriter();
			JSONObject rowData = JSONFactoryUtil.createJSONObject();
			rowData.put("total", hitsCount);
			rowData.put("maxPrice", maxPrice);
			rowData.put("status", HttpServletResponse.SC_OK);
			out.print(rowData.toString());
			out.flush();
		} catch (Exception e) {
			log.error("Error while searching result data count : " + e.getMessage());
			return true;
		}
		log.info("search result data count resources - end");
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
	CompanyLocalService _companyLocalService;

}
