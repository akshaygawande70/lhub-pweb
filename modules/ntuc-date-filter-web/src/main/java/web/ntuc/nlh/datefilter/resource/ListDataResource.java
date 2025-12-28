package web.ntuc.nlh.datefilter.resource;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import web.ntuc.nlh.datefilter.constants.DateFilterPortletKeys;
import web.ntuc.nlh.datefilter.constants.MVCCommandNames;
import web.ntuc.nlh.datefilter.dto.TopicsDto;
import web.ntuc.nlh.datefilter.util.CommonDateResult;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.LIST_TOPICS_RESOURCE,
		"javax.portlet.name=" + DateFilterPortletKeys.DATEFILTER_PORTLET }, service = MVCResourceCommand.class)
public class ListDataResource implements MVCResourceCommand {

	private static Log log = LogFactoryUtil.getLog(ListDataResource.class);

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Blog Topic Resources - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long groupId = themeDisplay.getScopeGroupId();
			log.info("groupID = " + groupId);

//			Authorization and validation check
			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);
			
			int pageNumber = ParamUtil.getInteger(resourceRequest, "pageNum");
			int limit = ParamUtil.getInteger(resourceRequest, "limit");
			String filter = ParamUtil.getString(resourceRequest, "filter");

			JSONObject filterJson = JSONFactoryUtil.createJSONObject(filter);
			int showFrom = limit * (pageNumber - 1);
			int showTo = showFrom + limit;

			long topics = ParamUtil.getLong(resourceRequest, "topicId");
			long subTopics = ParamUtil.getLong(resourceRequest, "subTopicId");
			List<TopicsDto> topicList = new ArrayList<>();


//			Getting Article Data
			HttpServletRequest request = PortalUtil.getHttpServletRequest(resourceRequest);

			List<Long> categoryIds = new ArrayList<>();

			if (Validator.isNotNull(subTopics) && !Validator.isBlank(String.valueOf(subTopics)) && subTopics > 0) {
				categoryIds.add(subTopics);
			} else {
				categoryIds.add(topics);
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
			Map<String, Object> mapResult = CommonDateResult.buildResultSummary(categoryIds, searchContext,
					themeDisplay, request, resourceRequest, resourceResponse, false, ddmIndexer, filterJson, sorts,
					queries, searchRequestBuilderFactory, categoryFacetFactory, aggregations,
					searchResponseBuilderFactory, DateFilterPortletKeys.SEARCH_RESULTS,
					DateFilterPortletKeys.PARAMETER_ARTICLE_ADMIN_GROUP_CODE, DateFilterPortletKeys.HITS,
					DateFilterPortletKeys.DISTINCT_CATEGORY_IDS, "blog", null, null, null, null);

			@SuppressWarnings("unchecked")
			List<SearchResultSummaryDisplayContext> resultContent = (List<SearchResultSummaryDisplayContext>) mapResult
					.get(DateFilterPortletKeys.SEARCH_RESULTS);

			if (Validator.isNotNull(resultContent)) {
				for (SearchResultSummaryDisplayContext res : resultContent) {
					TopicsDto topic = new TopicsDto();

					topic.setTitle(res.getHighlightedTitle());

					if (res.getClassName().equals(JournalArticle.class.getName())) {
						JournalArticle article = (JournalArticle) res.getAssetObject();

						String xmlContent = article.getContent();
						com.liferay.portal.kernel.xml.Document document = SAXReaderUtil.read(xmlContent);
						String imageUrl = ContentUtil.getImageUrl(document, 1, "image", themeDisplay);
						String status = ContentUtil.getWebContentVal(document, 1, "category", themeDisplay);
						String description = ContentUtil.getWebContentVal(document, 1, "content", themeDisplay);

						String descReplace = description.replaceAll("\\<.*?\\>", "");
						String finalDesc = descReplace.replaceAll("&.*?;", "");

						Date date = article.getDisplayDate();
						String dateFormat = "dd MMM yyyy hh:mm:ss aaa";
						SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
						String displayDate = sdf.format(date);

						topic.setDesc(finalDesc);
						topic.setDate(displayDate);
						topic.setStatus(status);
						topic.setUrlImage(imageUrl);
						document = null;
					}

					// add data when viewURL not null
					if (Validator.isNull(topic.getUrlMore())) {
						if (res.isAssetRendererURLDownloadVisible()) {
							topic.setUrlMore(res.getAssetRendererURLDownload());
						} else {
							if (Validator.isNotNull(res.getViewURL())) {
								topic.setUrlMore(res.getViewURL());
							}
						}
					}

					topicList.add(topic);
				}
			}

			resourceResponse.setContentType("application/json");
			PrintWriter out = null;
			out = resourceResponse.getWriter();
			if (showTo > topicList.size()) {
				showTo = topicList.size();
			}

			JSONObject rowData = JSONFactoryUtil.createJSONObject();
			rowData.put("topicList", topicList);
			rowData.put("status", HttpServletResponse.SC_OK);
			out.print(rowData.toString());
			out.flush();
		} catch (Exception e) {
			log.error("Error while searching result data : " + e.getMessage());
			return true;
		}
		log.info("Blog Topic resources - end");
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
