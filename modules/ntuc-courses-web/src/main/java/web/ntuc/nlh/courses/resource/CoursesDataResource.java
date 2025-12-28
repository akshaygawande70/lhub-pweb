package web.ntuc.nlh.courses.resource;

import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
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
import web.ntuc.nlh.courses.constants.CoursesPortletKeys;
import web.ntuc.nlh.courses.constants.MVCCommandNames;
import web.ntuc.nlh.courses.dto.CoursesDto;
import web.ntuc.nlh.courses.util.CommonSearchResult;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.COURSES_DATA_RESOURCE,
		"javax.portlet.name=" + CoursesPortletKeys.COURSES_PORTLET }, service = MVCResourceCommand.class)
public class CoursesDataResource implements MVCResourceCommand {

	private static Log log = LogFactoryUtil.getLog(CoursesDataResource.class);
	private CommonSearchResult common = new CommonSearchResult();

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("List Courses Resources - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

//			Filter Implementation and prepare resources
			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

			int pageNumber = ParamUtil.getInteger(resourceRequest, "pageNum");
			int limit = ParamUtil.getInteger(resourceRequest, "limit");
			String filter = ParamUtil.getString(resourceRequest, "filter");
			
			JSONObject filterJson = JSONFactoryUtil.createJSONObject(filter);
			int showFrom = limit * (pageNumber - 1);
			int showTo = showFrom + limit;

			long topic = ParamUtil.getLong(resourceRequest, "topic");
			long theme = ParamUtil.getLong(resourceRequest, "theme");

			HttpServletRequest request = PortalUtil.getHttpServletRequest(resourceRequest);

//			Adding Courses Id
			List<CoursesDto> courses = new ArrayList<>();
			List<Long> categoryIds = new ArrayList<>();

			if (Validator.isNotNull(topic) && !Validator.isBlank(String.valueOf(topic)) && topic > 0) {
				categoryIds.add(topic);
			} else {
				categoryIds.add(theme);
			}

//			Preparation Elastic Search
			SearchContext searchContext = SearchContextFactory.getInstance(request);

			searchContext.setAttribute("paginationType", "more");

			searchContext.getQueryConfig().setCollatedSpellCheckResultEnabled(true);
			searchContext.getQueryConfig().setCollatedSpellCheckResultScoresThreshold(200);
			searchContext.getQueryConfig().setQueryIndexingEnabled(false);
			searchContext.getQueryConfig().setQueryIndexingThreshold(50);
			searchContext.getQueryConfig().setQuerySuggestionEnabled(true);
			searchContext.getQueryConfig().setQuerySuggestionScoresThreshold(0);
			searchContext.setStart(showFrom);
			searchContext.setEnd(showTo);

//			Getting detail data from courses id
			Map<String, Object> mapResult  = common.buildResultSummary(categoryIds, searchContext,
					themeDisplay, request, resourceRequest, resourceResponse, false, ddmIndexer, filterJson, sorts, queries,
					searchRequestBuilderFactory, categoryFacetFactory, aggregations, searchResponseBuilderFactory,true);

			@SuppressWarnings("unchecked")
			List<SearchResultSummaryDisplayContext> resultCourse = (List<SearchResultSummaryDisplayContext>) mapResult
					.get(CoursesPortletKeys.SEARCH_RESULTS);
			
			if (Validator.isNotNull(resultCourse)) {
				for (SearchResultSummaryDisplayContext res : resultCourse) {
					CoursesDto v = new CoursesDto();
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
						String whatInIt = ContentUtil.getWebContentVal(document, 1, "detailWhatsInItforMe", themeDisplay);
						
						v.setTitle(title);
						v.setStatus(status);
						v.setUrlImage(imageUrl);
						v.setWhatInIt(whatInIt);
						
						if (price.equals("")) {
							v.setPrice(0.0);
						} else {
							v.setPrice(Double.valueOf(price));
						}

						if (funded.equals("true")) {
							v.setFunded("FUNDED");
							v.setStatus("Before Funding");
						} else {
							v.setFunded("NOT FUNDED");
						}

						if (popular.equals("true")) {
							v.setPopular("POPULAR");
						}
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

			resourceResponse.setContentType("application/json");
			PrintWriter out = null;
			out = resourceResponse.getWriter();
			if (showTo > courses.size()) {
				showTo = courses.size();
			}
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
	AssetVocabularyLocalService assetVocabularyLocalService;

	@Reference
	AssetCategoryLocalService assetCategoryLocalService;

	@Reference
	CompanyLocalService companyLocalService;
	
	@Reference
	DDMIndexer ddmIndexer;

	@Reference
	SearchRequestBuilderFactory searchRequestBuilderFactory;

	@Reference
	CategoryFacetFactory categoryFacetFactory;

	@Reference
	Queries queries;

	@Reference
	Sorts sorts;

	@Reference
	Aggregations aggregations;

	@Reference
	SearchResponseBuilderFactory searchResponseBuilderFactory;
}
