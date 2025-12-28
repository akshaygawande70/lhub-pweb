package web.ntuc.nlh.courses.resource;

import com.liferay.dynamic.data.mapping.util.DDMIndexer;
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
import web.ntuc.nlh.courses.constants.CoursesPortletKeys;
import web.ntuc.nlh.courses.constants.MVCCommandNames;
import web.ntuc.nlh.courses.util.CommonSearchResult;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.COURSES_DATA_COUNT_RESOURCE,
		"javax.portlet.name=" + CoursesPortletKeys.COURSES_PORTLET }, service = MVCResourceCommand.class)
public class CoursesDataCountResource implements MVCResourceCommand {
	private static Log log = LogFactoryUtil.getLog(CoursesDataCountResource.class);
	private CommonSearchResult common = new CommonSearchResult();

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Courses data count resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

			long topic = ParamUtil.getLong(resourceRequest, "topic");
			long theme = ParamUtil.getLong(resourceRequest, "theme");

			String filter = ParamUtil.getString(resourceRequest, "filter");
			JSONObject filterJson = JSONFactoryUtil.createJSONObject(filter);

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

//		Adding course
			Map<String, Object> mapResult = common.getHitsCount(categoryIds, searchContext, themeDisplay, request,
					resourceRequest, resourceResponse, false, ddmIndexer, filterJson, sorts, queries,
					searchRequestBuilderFactory, categoryFacetFactory, aggregations, searchResponseBuilderFactory);
			int hitsCount = (int) mapResult.get("total");
			Double maxPrice = (Double) mapResult.get(CoursesPortletKeys.MAX_PRICE);
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
			log.error("Error while getting data count : " + e.getMessage());
			return true;
		}
		log.info("course data count resources - end");
		return false;
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
	CompanyLocalService companyLocalService;

}
