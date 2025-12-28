package web.ntuc.nlh.eventfilter.resource;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
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
//import api.ntuc.nlh.content.util.CommonDateResult;
import api.ntuc.nlh.content.util.ContentUtil;
import web.ntuc.nlh.eventfilter.constants.EventFilterPortletKeys;
import web.ntuc.nlh.eventfilter.constants.MVCCommandNames;
import web.ntuc.nlh.eventfilter.dto.EventDto;
import web.ntuc.nlh.eventfilter.util.CommonDateResult;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.LIST_TOPICS_RESOURCE,
		"javax.portlet.name=" + EventFilterPortletKeys.EVENTFILTER_PORTLET }, service = MVCResourceCommand.class)
public class ListDataResource implements MVCResourceCommand {

	private static Log log = LogFactoryUtil.getLog(ListDataResource.class);
	static String topicListParam = "topicList";

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Event Topic Resources - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long groupId = themeDisplay.getScopeGroupId();

//			Authorization and validation check
			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);
			
			int pageNumber = ParamUtil.getInteger(resourceRequest, "pageNum");
			int limit = ParamUtil.getInteger(resourceRequest, "limit");
			String filter = ParamUtil.getString(resourceRequest, "filter");

			JSONObject filterJson = JSONFactoryUtil.createJSONObject(filter);
			int showFrom = limit * (pageNumber - 1);
			int showTo = showFrom + limit;

			long topics = ParamUtil.getLong(resourceRequest, "topicId");
			long eventType = ParamUtil.getLong(resourceRequest, "eventTypeId");

			// Get vocabulary from Group
			AssetVocabulary topicVocabulary = assetVocabularyLocalService.getGroupVocabulary(groupId,
					EventFilterPortletKeys.ASSET_VOCAB_EVENT);

			// Get all categories from vocabulary
			List<AssetCategory> eventCategoriesList = assetCategoryLocalService.getVocabularyCategories(
					topicVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

			List<Long> eventIdList = new ArrayList<>();
			for (AssetCategory assetCategory : eventCategoriesList) {
				eventIdList.add(assetCategory.getCategoryId());
			}

			List<EventDto> allEventList = new ArrayList<>();
			List<EventDto> upComingEventList = new ArrayList<>();
			List<EventDto> pastEventList = new ArrayList<>();

			log.info("topics : " + topics + " | eventTypeId : " + eventType);


//			Getting Article Data
			HttpServletRequest request = PortalUtil.getHttpServletRequest(resourceRequest);

			List<Long> categoryIds = new ArrayList<>();

			if (Validator.isNotNull(topics) && !Validator.isBlank(String.valueOf(eventType))) {
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

			Map<String, Object> mapResult = CommonDateResult.buildResultSummary(categoryIds, searchContext,
					themeDisplay, request, resourceRequest, resourceResponse, false, ddmIndexer, filterJson, sorts,
					queries, searchRequestBuilderFactory, categoryFacetFactory, aggregations,
					searchResponseBuilderFactory, EventFilterPortletKeys.SEARCH_RESULTS,
					EventFilterPortletKeys.PARAMETER_EVENT_ADMIN_GROUP_CODE, EventFilterPortletKeys.HITS,
					EventFilterPortletKeys.DISTINCT_CATEGORY_IDS, "event",
					EventFilterPortletKeys.PARAMETER_EVENT_STRUCTURE_CODE, EventFilterPortletKeys.DDM_FIELD_ARRAY,
					EventFilterPortletKeys.DDM_FIELD_NAME, EventFilterPortletKeys.FIELD_PREFIX);
			
			@SuppressWarnings("unchecked")
			List<SearchResultSummaryDisplayContext> resultContent = (List<SearchResultSummaryDisplayContext>) mapResult
					.get(EventFilterPortletKeys.SEARCH_RESULTS);

			for (SearchResultSummaryDisplayContext res : resultContent) {
				EventDto event = new EventDto();

				event.setTitle(res.getHighlightedTitle());
				event.setDesc(res.getContent());

				if (res.getClassName().equals(JournalArticle.class.getName())) {
					try {
						JournalArticle article = (JournalArticle) res.getAssetObject();

//						Getting category id from journal Article
						Long resourcePrimKey = Long.valueOf(article.getResourcePrimKey());
						List<AssetCategory> categories = AssetCategoryLocalServiceUtil
								.getCategories(JournalArticle.class.getName(), resourcePrimKey);

						for (AssetCategory ac : categories) {
							Long eventCategoryId = Long.valueOf(ac.getCategoryId());
							if (eventIdList.contains(eventCategoryId)) {
								event.setCategoryId(eventCategoryId);
							}
						}

						String xmlContent = article.getContent();
						com.liferay.portal.kernel.xml.Document document = SAXReaderUtil.read(xmlContent);
						String imageUrl = ContentUtil.getImageUrl(document, 1, "image", themeDisplay);
						String status = ContentUtil.getWebContentVal(document, 1, "category", themeDisplay);
						String startDateTemp = ContentUtil.getWebContentVal(document, 1, "startDate", themeDisplay);
						String endDateTemp = ContentUtil.getWebContentVal(document, 1, "endDate", themeDisplay);

						if (!startDateTemp.equals("") && !endDateTemp.equals("")) {
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
							Date startDate = formatter.parse(startDateTemp);
							String startTemp = formatter.format(startDate);
							Date endDate = formatter.parse(endDateTemp);
							String endTemp = formatter.format(endDate);
							event.setStartDate(startTemp);
							event.setEndDate(endTemp);
						}

						if (!status.equals("")) {
							event.setStatus(status);
						} else {
							event.setStatus("");
						}

						event.setUrlImage(imageUrl);
						document = null;
					} catch (Exception e) {
						event.setUrlImage("");
					}
				}

				// add data when viewURL not null
				if (Validator.isNull(event.getUrlMore())) {
					if (res.isAssetRendererURLDownloadVisible()) {
						event.setUrlMore(res.getAssetRendererURLDownload());
					} else {
						if (Validator.isNotNull(res.getViewURL())) {
							event.setUrlMore(res.getViewURL());
						}
					}
				}

				allEventList.add(event);
			}

			if (eventType != 1) {
				for (EventDto dto : allEventList) {
					// get current date
					Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String currentDate = sdf.format(date);
					Date compareDate = sdf.parse(currentDate);
					Date eventDate = sdf.parse(dto.getEndDate());
					if (!dto.getEndDate().isEmpty() && !dto.getStartDate().isEmpty()) {
						int result = eventDate.compareTo(compareDate);
						if (result > 0) {
							upComingEventList.add(dto);
						} else if (result < 0) {
							pastEventList.add(dto);
						}
					}
				}
			}

			resourceResponse.setContentType("application/json");
			PrintWriter out = null;
			out = resourceResponse.getWriter();

			JSONObject rowData = JSONFactoryUtil.createJSONObject();

			if (eventType == 1) {
				rowData.put("topicListParam", allEventList);
				if (showTo > allEventList.size()) {
					showTo = allEventList.size();
				}
			} else if (eventType == 2) {
				rowData.put("topicListParam", upComingEventList);
				if (showTo > upComingEventList.size()) {
					showTo = upComingEventList.size();
				}
			} else if (eventType == 3) {
				rowData.put("topicListParam", pastEventList);
				if (showTo > pastEventList.size()) {
					showTo = pastEventList.size();
				}
			}

//			1 => All | 2 => Upcoming | 3 => Past
			rowData.put("eventType", eventType);
			rowData.put("status", HttpServletResponse.SC_OK);
			out.print(rowData.toString());
			out.flush();
		} catch (Exception e) {
			log.error("Error while searching result data : " + e.getMessage());
			return true;
		}
		log.info("Event Topic resources - end");
		return false;
	}

	@Reference
	protected AssetVocabularyLocalService assetVocabularyLocalService;

	@Reference
	protected AssetCategoryLocalService assetCategoryLocalService;

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
