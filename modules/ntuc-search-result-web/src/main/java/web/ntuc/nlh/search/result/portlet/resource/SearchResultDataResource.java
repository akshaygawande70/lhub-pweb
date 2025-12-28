package web.ntuc.nlh.search.result.portlet.resource;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.journal.model.JournalArticle;
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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.search.aggregation.Aggregations;
import com.liferay.portal.search.document.Document;
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
import api.ntuc.nlh.content.engine.SearchResultSummaryDisplayContext;
import api.ntuc.nlh.content.util.ContentUtil;
import web.ntuc.nlh.search.result.constants.MVCCommandNames;
import web.ntuc.nlh.search.result.constants.SearchResultMessageKey;
import web.ntuc.nlh.search.result.constants.SearchResultPortletKeys;
import web.ntuc.nlh.search.result.dto.SearchResultDto;
import web.ntuc.nlh.search.result.util.CommonSearchResult;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.SEARCH_DATA_RESOURCES,
		"javax.portlet.name=" + SearchResultPortletKeys.SEARCH_RESULT_PORTLET }, service = MVCResourceCommand.class)
public class SearchResultDataResource implements MVCResourceCommand {

	private static Log log = LogFactoryUtil.getLog(SearchResultDataResource.class);
	private CommonSearchResult common = new CommonSearchResult();

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("search result data resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

			// get default company id for topic vocabulary
			Company company = _companyLocalService.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
			long globalGroupId = company.getGroup().getGroupId(); // themeDisplay.getScopeGroupId()
			String keyword = ParamUtil.getString(resourceRequest, "keyword", " ");

			int pageNumber = ParamUtil.getInteger(resourceRequest, "pageNum");
			int limit = ParamUtil.getInteger(resourceRequest, "limit");
			String filter = ParamUtil.getString(resourceRequest, "filter");
			JSONObject filterJson = JSONFactoryUtil.createJSONObject(filter);
			int showFrom = limit * (pageNumber - 1);
			int showTo = showFrom + limit;

			int course = 1;

			HttpServletRequest request = PortalUtil.getHttpServletRequest(resourceRequest);

			List<SearchResultDto> courses = new ArrayList<>();
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
			searchContext.setStart(showFrom);
			searchContext.setEnd(showTo);
//			Adding course
			Map<String, Object> mapResult = common.buildResultSummary(categoryIds, searchContext, themeDisplay, request,
					resourceRequest, resourceResponse, course, false, _ddmIndexer, filterJson, _sorts, _queries,
					_searchRequestBuilderFactory, _categoryFacetFactory, _aggregations, _searchResponseBuilderFactory);

			@SuppressWarnings("unchecked")
			List<SearchResultSummaryDisplayContext> resultCourse = (List<SearchResultSummaryDisplayContext>) mapResult
					.get(SearchResultPortletKeys.SEARCH_RESULTS);

			if (Validator.isNotNull(resultCourse)) {
				for (SearchResultSummaryDisplayContext res : resultCourse) {
					SearchResultDto v = new SearchResultDto();
					v.setDesc(res.getContent());
					if (res.getClassName().equals(JournalArticle.class.getName())) {
						JournalArticle journalArticle = (JournalArticle) res.getAssetObject();

						String xmlContent = journalArticle.getContent();
						com.liferay.portal.kernel.xml.Document document = SAXReaderUtil.read(xmlContent);
						
						String imageUrl = ContentUtil.getImageUrl(document, 1, "image", themeDisplay);

						v.setUrlImage(imageUrl);
						String status = ContentUtil.getWebContentVal(document, 1, "status", themeDisplay);
						String price = ContentUtil.getWebContentVal(document, 1, "price", themeDisplay);
						String funded = ContentUtil.getWebContentVal(document, 1, "funded", themeDisplay);
						String popular = ContentUtil.getWebContentVal(document, 1, "popular", themeDisplay);
						String title = ContentUtil.getWebContentVal(document, 1, "courseTitle", themeDisplay);
						v.setTitle(title);
						v.setStatus(status);
						v.setUrlImage(imageUrl);
						document=null;
						if (price.equals("")) {
							v.setPrice(0.0);
						}
						v.setPrice(Double.valueOf(price));
						if (funded.equals("true")) {
							v.setFunded("FUNDED");
							v.setStatus("Before Funding");
						} else {
							v.setFunded("NOT FUNDED");
						}

						if (popular.equals("true")) {
							v.setPopular("POPULAR");
						}
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
//			if search is not blank then show the "all result view" on the bottom of the page

			resourceResponse.setContentType("application/json");
			PrintWriter out = null;
			out = resourceResponse.getWriter();

			JSONObject rowData = JSONFactoryUtil.createJSONObject();
			rowData.put("courses", courses);
			rowData.put("status", HttpServletResponse.SC_OK);
			out.print(rowData.toString());
			out.flush();
		} catch (Exception e) {
			log.error("Error while searching result data : " + e.getMessage());
			return true;
		}
		log.info("search result data resources - end");
		return false;
	}

	@Reference
	AssetVocabularyLocalService _assetVocabularyLocalService;

	@Reference
	AssetCategoryLocalService _assetCategoryLocalService;

	@Reference
	CompanyLocalService _companyLocalService;

	@Reference
	DDMIndexer _ddmIndexer;

	@Reference
	SearchRequestBuilderFactory _searchRequestBuilderFactory;

	@Reference
	CategoryFacetFactory _categoryFacetFactory;

	@Reference
	Queries _queries;

	@Reference
	Sorts _sorts;

	@Reference
	Aggregations _aggregations;

	@Reference
	SearchResponseBuilderFactory _searchResponseBuilderFactory;

}
