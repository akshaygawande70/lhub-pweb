package web.ntuc.nlh.mediafilter.util;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.security.permission.ResourceActions;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import api.ntuc.nlh.content.engine.AssetRendererFactoryLookup;
import api.ntuc.nlh.content.engine.PortletURLFactory;
import api.ntuc.nlh.content.engine.SearchResultFieldDisplayContext;
import api.ntuc.nlh.content.engine.SearchResultPreferences;
import api.ntuc.nlh.content.engine.SearchResultSummaryDisplayContext;
import api.ntuc.nlh.content.engine.SearchResultViewURLSupplier;
import api.ntuc.nlh.content.engine.SearchUtil;

public class SearchResultSummaryDisplayBuilder {

	public SearchResultSummaryDisplayContext build() throws PortalException, PortletException {
		String className = document.get(Field.ENTRY_CLASS_NAME);
		long classPK = getEntryClassPK();

		AssetRendererFactory<?> assetRendererFactory = getAssetRendererFactoryByClassName(className);

		AssetRenderer<?> assetRenderer = null;

		if (assetRendererFactory != null) {
			long resourcePrimKey = GetterUtil.getLong(document.get(Field.ROOT_ENTRY_CLASS_PK));

			if (resourcePrimKey > 0) {
				classPK = resourcePrimKey;
			}

			assetRenderer = assetRendererFactory.getAssetRenderer(classPK);
		}

		Summary summary = getSummary(className, assetRenderer);

		if (summary == null) {
			return null;
		}

		return build(summary, className, classPK, assetRenderer);
	}

	public SearchResultSummaryDisplayContext buildURL() throws PortalException, PortletException {
		String className = document.get(Field.ENTRY_CLASS_NAME);
		long classPK = getEntryClassPK();

		AssetRendererFactory<?> assetRendererFactory = getAssetRendererFactoryByClassName(className);

		AssetRenderer<?> assetRenderer = null;

		if (assetRendererFactory != null) {
			long resourcePrimKey = GetterUtil.getLong(document.get(Field.ROOT_ENTRY_CLASS_PK));

			if (resourcePrimKey > 0) {
				classPK = resourcePrimKey;
			}

			assetRenderer = assetRendererFactory.getAssetRenderer(classPK);
		}

		Summary summary = getSummary(className, assetRenderer);

		if (summary == null) {
			return null;
		}

		return buildURL(summary, className, classPK, assetRenderer);
	}

	public void setAbridged(boolean abridged) {
		this.abridged = abridged;
	}

	public void setAssetEntryLocalService(AssetEntryLocalService assetEntryLocalService) {

		this.assetEntryLocalService = assetEntryLocalService;
	}

	public void setAssetRendererFactoryLookup(AssetRendererFactoryLookup assetRendererFactoryLookup) {

		this.assetRendererFactoryLookup = assetRendererFactoryLookup;
	}

	public void setCurrentURL(String currentURL) {
		this.currentURL = currentURL;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public void setHighlightEnabled(boolean highlightEnabled) {
		this.highlightEnabled = highlightEnabled;
	}

	public void setIndexerRegistry(IndexerRegistry indexerRegistry) {
		this.indexerRegistry = indexerRegistry;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setPortletURLFactory(PortletURLFactory portletURLFactory) {
		this.portletURLFactory = portletURLFactory;
	}

	public void setQueryTerms(String[] queryTerms) {
		this.queryTerms = queryTerms;
	}

	public void setResourceRequest(ResourceRequest resourceRequest) {
		this.resourceRequest = resourceRequest;
	}

	public void setResourceResponse(ResourceResponse resourceResponse) {
		this.resourceResponse = resourceResponse;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResourceActions(ResourceActions resourceActions) {
		this.resourceActions = resourceActions;
	}

	public void setSearchResultPreferences(SearchResultPreferences searchResultPreferences) {

		this.searchResultPreferences = searchResultPreferences;
	}

	public void setSearchResultViewURLSupplier(SearchResultViewURLSupplier searchResultViewURLSupplier) {

		this.searchResultViewURLSupplier = searchResultViewURLSupplier;
	}

	public void setThemeDisplay(ThemeDisplay themeDisplay) {
		this.themeDisplay = themeDisplay;
	}

	public SearchResultSummaryDisplayContext build(Summary summary, String className, long classPK,
			AssetRenderer<?> assetRenderer) throws PortletException {

		SearchResultSummaryDisplayContext searchResultSummaryDisplayContext = new SearchResultSummaryDisplayContext();

		searchResultSummaryDisplayContext.setClassName(className);
		searchResultSummaryDisplayContext.setClassPK(classPK);

		if (Validator.isNotNull(summary.getContent())) {
			searchResultSummaryDisplayContext.setContent(summary.getHighlightedContent());
			searchResultSummaryDisplayContext.setContentVisible(true);
		}

		searchResultSummaryDisplayContext.setHighlightedTitle(summary.getHighlightedTitle());
		searchResultSummaryDisplayContext.setPortletURL(portletURLFactory.getPortletURL());

		if (abridged) {
			return searchResultSummaryDisplayContext;
		}

//		AssetEntry assetEntry = assetEntryLocalService.fetchEntry(className, classPK);

//		buildAssetCategoriesOrTags(searchResultSummaryDisplayContext, assetEntry, className, classPK);

		buildAssetRendererURLDownload(searchResultSummaryDisplayContext, assetRenderer, summary);
//		buildCreationDateString(searchResultSummaryDisplayContext);
//		buildCreatorUserName(searchResultSummaryDisplayContext);
		buildDocumentForm(searchResultSummaryDisplayContext);
//		buildLocaleReminder(searchResultSummaryDisplayContext, summary);
//		buildModelResource(searchResultSummaryDisplayContext, className);
//		buildUserPortrait(searchResultSummaryDisplayContext, assetEntry, className);
		buildViewURL(className, classPK, searchResultSummaryDisplayContext);
		buildAssetObject(searchResultSummaryDisplayContext, assetRenderer);

		return searchResultSummaryDisplayContext;
	}

	public SearchResultSummaryDisplayContext buildURL(Summary summary, String className, long classPK,
			AssetRenderer<?> assetRenderer) throws PortletException {

		SearchResultSummaryDisplayContext searchResultSummaryDisplayContext = new SearchResultSummaryDisplayContext();

		searchResultSummaryDisplayContext.setClassName(className);
		searchResultSummaryDisplayContext.setClassPK(classPK);

		if (Validator.isNotNull(summary.getContent())) {
			searchResultSummaryDisplayContext.setContent(summary.getHighlightedContent());
			searchResultSummaryDisplayContext.setContentVisible(true);
		}

		searchResultSummaryDisplayContext.setHighlightedTitle(summary.getHighlightedTitle());
		searchResultSummaryDisplayContext.setPortletURL(portletURLFactory.getPortletURL());

		if (abridged) {
			return searchResultSummaryDisplayContext;
		}

		buildViewURL(className, classPK, searchResultSummaryDisplayContext);

		return searchResultSummaryDisplayContext;
	}

	protected void buildAssetObject(SearchResultSummaryDisplayContext searchResultSummaryDisplayContext,
			AssetRenderer<?> assetRenderer) {
		searchResultSummaryDisplayContext.setAssetObject(assetRenderer.getAssetObject());
	}

	protected void buildAssetCategoriesOrTags(SearchResultSummaryDisplayContext searchResultSummaryDisplayContext,
			AssetEntry assetEntry, String className, long classPK) {

		if (hasAssetCategoriesOrTags(assetEntry)) {
			searchResultSummaryDisplayContext.setCategoryIds(assetEntry.getCategoryIds());
			searchResultSummaryDisplayContext.setAssetCategoriesOrTagsVisible(true);
			searchResultSummaryDisplayContext.setFieldAssetCategoryIds(Field.ASSET_CATEGORY_IDS);
			searchResultSummaryDisplayContext.setFieldAssetTagNames(Field.ASSET_TAG_NAMES);
		}
	}

	protected void buildAssetRendererURLDownload(SearchResultSummaryDisplayContext searchResultSummaryDisplayContext,
			AssetRenderer<?> assetRenderer, Summary summary) {

		if (hasAssetRendererURLDownload(assetRenderer)) {
			searchResultSummaryDisplayContext.setAssetRendererURLDownload(assetRenderer.getURLDownload(themeDisplay));
			searchResultSummaryDisplayContext.setAssetRendererURLDownloadVisible(true);
			searchResultSummaryDisplayContext.setTitle(summary.getTitle());
		}
	}

	protected void buildCreationDateString(SearchResultSummaryDisplayContext searchResultSummaryDisplayContext) {

		String creation = StringUtil.trim(document.get(Field.CREATE_DATE));

		if (!Validator.isBlank(creation)) {
			searchResultSummaryDisplayContext.setCreationDateString(formatDate(creation));
			searchResultSummaryDisplayContext.setCreationDate(parseDate(creation));
			searchResultSummaryDisplayContext.setCreationDateVisible(true);
		}
	}

	protected void buildCreatorUserName(SearchResultSummaryDisplayContext searchResultSummaryDisplayContext) {

		String creatorUserName = document.get(Field.USER_NAME);

		if (creatorUserName != null) {
			searchResultSummaryDisplayContext.setCreatorUserName(creatorUserName);
			searchResultSummaryDisplayContext.setCreatorVisible(true);
		}
	}

	protected void buildDocumentForm(SearchResultSummaryDisplayContext searchResultSummaryDisplayContext) {

		if (searchResultPreferences.isDisplayResultsInDocumentForm()) {
			searchResultSummaryDisplayContext.setDocumentFormFieldDisplayContexts(buildFields());
			searchResultSummaryDisplayContext.setDocumentFormVisible(true);
		}
	}

	protected SearchResultFieldDisplayContext buildField(Field field) {
		SearchResultFieldDisplayContext searchResultFieldDisplayContext = new SearchResultFieldDisplayContext();

		searchResultFieldDisplayContext.setArray(isArray(field));
		searchResultFieldDisplayContext.setName(field.getName());
		searchResultFieldDisplayContext.setNumeric(field.isNumeric());
		searchResultFieldDisplayContext.setTokenized(field.isTokenized());
		searchResultFieldDisplayContext.setValuesToString(getValuesToString(field));

		return searchResultFieldDisplayContext;
	}

	protected List<SearchResultFieldDisplayContext> buildFields() {
		Map<String, Field> map = document.getFields();

		List<Map.Entry<String, Field>> entries = new LinkedList<>(map.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<String, Field>>() {

			@Override
			public int compare(Map.Entry<String, Field> entry1, Map.Entry<String, Field> entry2) {

				String key = entry1.getKey();

				return key.compareTo(entry2.getKey());
			}

		});

		List<SearchResultFieldDisplayContext> searchResultFieldDisplayContexts = new ArrayList<>(entries.size());

		for (Map.Entry<String, Field> entry : entries) {
			Field field = entry.getValue();

			String fieldName = field.getName();

			if (fieldName.equals(Field.UID)) {
				continue;
			}

			searchResultFieldDisplayContexts.add(buildField(field));
		}

		return searchResultFieldDisplayContexts;
	}

	protected void buildLocaleReminder(SearchResultSummaryDisplayContext searchResultSummaryDisplayContext,
			Summary summary) {

		if (locale != summary.getLocale()) {
			Locale summaryLocale = summary.getLocale();

			searchResultSummaryDisplayContext.setLocaleLanguageId(LocaleUtil.toLanguageId(summaryLocale));
			searchResultSummaryDisplayContext.setLocaleReminder(
					language.format(request, "this-result-comes-from-the-x-version-of-this-content",
							summaryLocale.getDisplayLanguage(locale), false));

			searchResultSummaryDisplayContext.setLocaleReminderVisible(true);
		}
	}

	protected void buildModelResource(SearchResultSummaryDisplayContext searchResultSummaryDisplayContext,
			String className) {

		searchResultSummaryDisplayContext
				.setModelResource(resourceActions.getModelResource(themeDisplay.getLocale(), className));
	}

	protected void buildUserPortrait(SearchResultSummaryDisplayContext searchResultSummaryDisplayContext,
			AssetEntry assetEntry, String className) {

		long entryClassPK = getEntryClassPK();

		AssetEntry childAssetEntry = assetEntryLocalService.fetchEntry(className, entryClassPK);

		if (childAssetEntry != null) {
			assetEntry = childAssetEntry;
		}

		if (assetEntry != null) {
			searchResultSummaryDisplayContext.setAssetEntryUserId(getAssetEntryUserId(assetEntry));
			searchResultSummaryDisplayContext.setUserPortraitVisible(true);
		}
	}

	protected void buildViewURL(String className, long classPK,
			SearchResultSummaryDisplayContext searchResultSummaryDisplayContext) {

		String viewURL = getSearchResultViewURL(className, classPK);
		if (Validator.isNotNull(viewURL)) {
			searchResultSummaryDisplayContext.setViewURL(viewURL);
		}
	}

	protected String formatDate(String dateString) {
		SimpleDateFormat simpleDateFormatInput = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat simpleDateFormatOutput = new SimpleDateFormat("MMM dd yyyy, h:mm a");

		try {
			return simpleDateFormatOutput.format(simpleDateFormatInput.parse(dateString));
		} catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}

	protected Date parseDate(String dateString) {
		SimpleDateFormat simpleDateFormatInput = new SimpleDateFormat("yyyyMMddHHmmss");

		try {
			return simpleDateFormatInput.parse(dateString);
		} catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}

	protected long getAssetEntryUserId(AssetEntry assetEntry) {
		if (Objects.equals(assetEntry.getClassName(), User.class.getName())) {
			return assetEntry.getClassPK();
		}

		return assetEntry.getUserId();
	}

	protected AssetRendererFactory<?> getAssetRendererFactoryByClassName(String className) {

		if (assetRendererFactoryLookup != null) {
			return assetRendererFactoryLookup.getAssetRendererFactoryByClassName(className);
		}

		return AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(className);
	}

	protected long getEntryClassPK() {
		return GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK));
	}

	protected Indexer<Object> getIndexer(String className) {
		if (indexerRegistry != null) {
			return indexerRegistry.getIndexer(className);
		}

		return IndexerRegistryUtil.getIndexer(className);
	}

	protected String getSearchResultViewURL(String className, long classPK) {
		if (searchResultViewURLSupplier != null) {
			return searchResultViewURLSupplier.getSearchResultViewURL();
		}

		return SearchUtil.getSearchResultViewURL(resourceRequest, resourceResponse, className, classPK,
				searchResultPreferences.isViewInContext(), currentURL);
	}

	@SuppressWarnings("rawtypes")
	protected Summary getSummary(String className, AssetRenderer<?> assetRenderer) throws SearchException {

		Summary summary = null;

		Indexer indexer = getIndexer(className);

		if (indexer != null) {
			String snippet = document.get(Field.SNIPPET);

			summary = indexer.getSummary(document, snippet, resourceRequest, resourceResponse);
		} else if (assetRenderer != null) {
			summary = new Summary(locale, assetRenderer.getTitle(locale), assetRenderer.getSearchSummary(locale));
		}

		if (summary != null) {
			summary.setHighlight(highlightEnabled);
			summary.setQueryTerms(queryTerms);
		}

		return summary;
	}

	protected String getValuesToString(Field field) {
		String[] values = field.getValues();

		StringBundler sb = new StringBundler(4 * values.length);

		for (int i = 0; i < values.length; i++) {
			if (field.isNumeric()) {
				sb.append(HtmlUtil.escape(values[i]));
			} else {
				sb.append(StringPool.QUOTE);
				sb.append(HtmlUtil.escape(values[i]));
				sb.append(StringPool.QUOTE);
			}

			sb.append(StringPool.COMMA_AND_SPACE);
		}

		sb.setIndex(sb.index() - 1);

		if (values.length > 1) {
			sb.setStringAt(StringPool.OPEN_BRACKET, 0);

			sb.append(StringPool.CLOSE_BRACKET);
		}

		return sb.toString();
	}

	protected boolean hasAssetCategoriesOrTags(AssetEntry assetEntry) {
		if (assetEntry == null) {
			return false;
		}

		if (ArrayUtil.isNotEmpty(assetEntry.getCategoryIds())) {
			return true;
		}

		if (ArrayUtil.isNotEmpty(assetEntry.getTagNames())) {
			return true;
		}

		return false;
	}

	protected boolean hasAssetRendererURLDownload(AssetRenderer<?> assetRenderer) {

		if (assetRenderer == null) {
			return false;
		}

		if (Validator.isNull(assetRenderer.getURLDownload(themeDisplay))) {
			return false;
		}

		return true;
	}

	protected boolean isArray(Field field) {
		String[] values = field.getValues();

		if (values.length > 1) {
			return true;
		}

		return false;
	}

	private boolean abridged;
	private AssetEntryLocalService assetEntryLocalService;
	private AssetRendererFactoryLookup assetRendererFactoryLookup;
	private String currentURL;
	private Document document;
	private boolean highlightEnabled;
	private IndexerRegistry indexerRegistry;
	private Language language;
	private Locale locale;
	private PortletURLFactory portletURLFactory;
	private String[] queryTerms;
	private ResourceRequest resourceRequest;
	private ResourceResponse resourceResponse;
	private HttpServletRequest request;
	private ResourceActions resourceActions;
	private SearchResultPreferences searchResultPreferences;
	private SearchResultViewURLSupplier searchResultViewURLSupplier;
	private ThemeDisplay themeDisplay;

}
