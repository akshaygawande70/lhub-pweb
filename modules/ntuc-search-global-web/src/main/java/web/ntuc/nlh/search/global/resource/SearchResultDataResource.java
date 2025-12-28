package web.ntuc.nlh.search.global.resource;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
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
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.nlh.search.global.constants.MVCCommandNames;
import web.ntuc.nlh.search.global.constants.SearchGlobalMessageKeys;
import web.ntuc.nlh.search.global.constants.SearchGlobalPortletKeys;
import web.ntuc.nlh.search.global.result.dto.SearchResultDto;
import web.ntuc.nlh.search.global.util.CommonSearchResult;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.SEARCH_DATA_RESOURCES,
		"javax.portlet.name=" + SearchGlobalPortletKeys.SEARCH_GLOBAL_PORTLET}, service = MVCResourceCommand.class)
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

//			get default company id for topic vocabulary
			Company company = _companyLocalService.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
			long globalGroupId = company.getGroup().getGroupId(); 
			long localGroupId = themeDisplay.getScopeGroupId();
			String keyword = ParamUtil.getString(resourceRequest, "keyword", " ");

			int showFrom = 0;
			int showTo = 5;

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
			
			List<SearchResultDto> blogs = new ArrayList<>();
			List<SearchResultDto> pages = new ArrayList<>();
			List<SearchResultDto> pressReleases = new ArrayList<>();
			List<SearchResultDto> news = new ArrayList<>();
			
			List<SearchResultDto> blogsResult = this.getDtoResult(globalGroupId, localGroupId,
					SearchGlobalPortletKeys.BLOGS_CATEGORY_NAME, searchContext, themeDisplay, request,
					resourceRequest, resourceResponse);
			List<SearchResultDto> pagesResult = this.getDtoResult(globalGroupId, localGroupId,
					SearchGlobalPortletKeys.PAGES_CATEGORY_NAME, searchContext, themeDisplay, request,
					resourceRequest, resourceResponse);
			List<SearchResultDto> pressReleasesResult = this.getDtoResult(globalGroupId, localGroupId,
					SearchGlobalPortletKeys.PRESS_CATEGORY_NAME, searchContext, themeDisplay, request,
					resourceRequest, resourceResponse);
			List<SearchResultDto> newsResult = this.getDtoResult(globalGroupId, localGroupId,
					SearchGlobalPortletKeys.NEWS_CATEGORY_NAME, searchContext, themeDisplay, request,
					resourceRequest, resourceResponse);
			if (Validator.isNotNull(blogsResult)) {
				blogs.addAll(blogsResult);
			}
			if (Validator.isNotNull(pagesResult)) {
				pages.addAll(pagesResult);
			}
			if (Validator.isNotNull(pressReleasesResult)) {
				pressReleases.addAll(pressReleasesResult);
			}
			if (Validator.isNotNull(newsResult)) {
				news.addAll(newsResult);
			}
			List<SearchResultDto> others = new ArrayList<>();
			others.addAll(pages);
			others.addAll(pressReleases);
			others.addAll(news);
//			Adding course
			Map<String, Object> mapResult = common.buildResultSummary(categoryIds, searchContext, themeDisplay, request,
					resourceRequest, resourceResponse);
			
			@SuppressWarnings("unchecked")
			List<SearchResultSummaryDisplayContext> resultCourse = (List<SearchResultSummaryDisplayContext>) mapResult
					.get(SearchGlobalPortletKeys.SEARCH_GLOBAL_RESULTS);
			
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
						
						document = null;

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
			List<SearchResultDto> othersSubList = new ArrayList<>();
			if(others.size() > showTo) {
				othersSubList = others.subList(showFrom, showTo);
			} else {
				othersSubList = others;
			}
			rowData.put("others", othersSubList);
			rowData.put("blogs", blogs);
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
	
	private List<SearchResultDto> getDtoResult(long globalGroupId, long localGroupId, String categoryName,
			SearchContext searchContext, ThemeDisplay themeDisplay, HttpServletRequest request,
			ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
		List<SearchResultDto> result = null;

		try {
			ParameterGroup groupGlobalParam = ParameterGroupLocalServiceUtil
					.getByCode(SearchGlobalPortletKeys.PARAMETER_GLOBAL_GROUP_CODE, false);
			Parameter globalTopicParam = ParameterLocalServiceUtil.getByGroupCode(groupGlobalParam.getGroupId(),
					groupGlobalParam.getParameterGroupId(), SearchGlobalPortletKeys.PARAMETER_GLOBAL_SEARCH_TOPIC_CODE,
					false);
			AssetVocabulary topicVocabulary = _assetVocabularyLocalService.fetchGroupVocabulary(globalGroupId,
					globalTopicParam.getParamValue());
			OrderByComparator<AssetCategory> order = null;
			List<AssetCategory> assetCategories = _assetCategoryLocalService
					.getVocabularyCategories(topicVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS,
							order)
					.stream().filter(category -> category.getName().equals(categoryName)).collect(Collectors.toList());
			List<Long> assetCategoryIds = new ArrayList<>();
			for (AssetCategory asset : assetCategories) {
				assetCategoryIds.add(asset.getCategoryId());

			}
			
			Map<String, Object> mapResult = common.buildResultSummary(assetCategoryIds,
					searchContext, themeDisplay, request, resourceRequest, resourceResponse);
			
			@SuppressWarnings("unchecked")
			List<SearchResultSummaryDisplayContext> resultOther = (List<SearchResultSummaryDisplayContext>) mapResult.get(SearchGlobalPortletKeys.SEARCH_GLOBAL_RESULTS);
			
			List<SearchResultDto> dtos = new ArrayList<>();
			for (SearchResultSummaryDisplayContext res : resultOther) {
				SearchResultDto v = new SearchResultDto();
				v.setTitle(res.getHighlightedTitle());
				v.setDesc(res.getContent());
				if (res.getClassName().equals(JournalArticle.class.getName())) {
					JournalArticle journalArticle = (JournalArticle) res.getAssetObject();

					String xmlContent = journalArticle.getContent();
					com.liferay.portal.kernel.xml.Document document = SAXReaderUtil.read(xmlContent);
					String url = "";
					String category = "";
					String imageUrl = "";
					if (categoryName.equals(SearchGlobalPortletKeys.PRESS_CATEGORY_NAME)) {
						url = ContentUtil.getWebContentVal(document, 1, "Link", themeDisplay);
					}else if (categoryName.equals(SearchGlobalPortletKeys.PAGES_CATEGORY_NAME)) {
						url = ContentUtil.getWebContentVal(document, 1, "linkToPage", themeDisplay);
					}else if (categoryName.equals(SearchGlobalPortletKeys.NEWS_CATEGORY_NAME)) {
						url = "/lhub-in-the-news";
					} else if (categoryName.equals(SearchGlobalPortletKeys.BLOGS_CATEGORY_NAME)) {
						category = ContentUtil.getWebContentVal(document, 1, "category", themeDisplay);
						imageUrl = ContentUtil.getImageUrl(document, 1, "image", themeDisplay);
					}
					document = null;
					String finalUrl = url.split("\\?")[0];
					v.setUrlMore(finalUrl);
					v.setCategory(category);
					v.setUrlImage(imageUrl);

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

				dtos.add(v);
			}
			result = dtos;
		} catch (Exception e) {
			log.error("Error while searching result data : " + e.getMessage());
		}
		return result;
	}
	
	@Reference
	AssetVocabularyLocalService _assetVocabularyLocalService;

	@Reference
	AssetCategoryLocalService _assetCategoryLocalService;

	@Reference
	CompanyLocalService _companyLocalService;


}
