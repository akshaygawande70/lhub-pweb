package api.ntuc.nlh.content.engine;

import java.util.Date;
import java.util.List;

import javax.portlet.PortletURL;

public class SearchResultSummaryDisplayContext {

	public long getAssetEntryUserId() {
		return assetEntryUserId;
	}

	public String getAssetRendererURLDownload() {
		return assetRendererURLDownload;
	}

	public String getClassName() {
		return className;
	}

	public long getClassPK() {
		return classPK;
	}

	public String getContent() {
		return content;
	}

	public String getCreationDateString() {
		return creationDateString;
	}

	public String getCreatorUserName() {
		return creatorUserName;
	}

	public List<SearchResultFieldDisplayContext> getDocumentFormFieldDisplayContexts() {

		return documentFormFieldDisplayContexts;
	}

	public String getFieldAssetCategoryIds() {
		return fieldAssetCategoryIds;
	}

	public String getFieldAssetTagNames() {
		return fieldAssetTagNames;
	}

	public String getHighlightedTitle() {
		return highlightedTitle;
	}

	public String getIconId() {
		return iconId;
	}

	public String getLocaleLanguageId() {
		return localeLanguageId;
	}

	public String getLocaleReminder() {
		return localeReminder;
	}

	public String getModelResource() {
		return modelResource;
	}

	public String getPathThemeImages() {
		return pathThemeImages;
	}

	public PortletURL getPortletURL() {
		return portletURL;
	}

	public String getThumbnailURLString() {
		return thumbnailURLString;
	}

	public String getTitle() {
		return title;
	}

	public String getViewURL() {
		return viewURL;
	}

	public boolean isAssetCategoriesOrTagsVisible() {
		return assetCategoriesOrTagsVisible;
	}

	public boolean isAssetRendererURLDownloadVisible() {
		return assetRendererURLDownloadVisible;
	}

	public boolean isContentVisible() {
		return contentVisible;
	}

	public boolean isCreationDateVisible() {
		return creationDateVisible;
	}

	public boolean isCreatorVisible() {
		return creatorVisible;
	}

	public boolean isDocumentFormVisible() {
		return documentFormVisible;
	}

	public boolean isIconVisible() {
		return iconVisible;
	}

	public boolean isLocaleReminderVisible() {
		return localeReminderVisible;
	}

	public boolean isThumbnailVisible() {
		return thumbnailVisible;
	}

	public boolean isUserPortraitVisible() {
		return userPortraitVisible;
	}

	public void setAssetCategoriesOrTagsVisible(boolean assetCategoriesOrTagsVisible) {

		this.assetCategoriesOrTagsVisible = assetCategoriesOrTagsVisible;
	}

	public void setAssetEntryUserId(long assetEntryUserId) {
		this.assetEntryUserId = assetEntryUserId;
	}

	public void setAssetRendererURLDownload(String assetRendererURLDownload) {
		this.assetRendererURLDownload = assetRendererURLDownload;
	}

	public void setAssetRendererURLDownloadVisible(boolean assetRendererURLDownloadVisible) {

		this.assetRendererURLDownloadVisible = assetRendererURLDownloadVisible;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setClassPK(long classPK) {
		this.classPK = classPK;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setContentVisible(boolean contentVisible) {
		this.contentVisible = contentVisible;
	}

	public void setCreationDateString(String creationDateString) {
		this.creationDateString = creationDateString;
	}

	public void setCreationDateVisible(boolean creationDateVisible) {
		this.creationDateVisible = creationDateVisible;
	}

	public void setCreatorUserName(String creatorUserName) {
		this.creatorUserName = creatorUserName;
	}

	public void setCreatorVisible(boolean writtenByVisible) {
		creatorVisible = writtenByVisible;
	}

	public void setDocumentFormFieldDisplayContexts(
			List<SearchResultFieldDisplayContext> documentFormFieldDisplayContexts) {

		this.documentFormFieldDisplayContexts = documentFormFieldDisplayContexts;
	}

	public void setDocumentFormVisible(boolean documentFormVisible) {
		this.documentFormVisible = documentFormVisible;
	}

	public void setFieldAssetCategoryIds(String fieldAssetCategoryIds) {
		this.fieldAssetCategoryIds = fieldAssetCategoryIds;
	}

	public void setFieldAssetTagNames(String fieldAssetTagNames) {
		this.fieldAssetTagNames = fieldAssetTagNames;
	}

	public void setHighlightedTitle(String highlightedTitle) {
		this.highlightedTitle = highlightedTitle;
	}

	public void setIconId(String iconId) {
		this.iconId = iconId;
	}

	public void setIconVisible(boolean iconVisible) {
		this.iconVisible = iconVisible;
	}

	public void setLocaleLanguageId(String localeLanguageId) {
		this.localeLanguageId = localeLanguageId;
	}

	public void setLocaleReminder(String localeReminder) {
		this.localeReminder = localeReminder;
	}

	public void setLocaleReminderVisible(boolean localeReminderVisible) {
		this.localeReminderVisible = localeReminderVisible;
	}

	public void setModelResource(String modelResource) {
		this.modelResource = modelResource;
	}

	public void setPathThemeImages(String pathThemeImages) {
		this.pathThemeImages = pathThemeImages;
	}

	public void setPortletURL(PortletURL portletURL) {
		this.portletURL = portletURL;
	}

	public void setThumbnailURLString(String thumbnailURLString) {
		this.thumbnailURLString = thumbnailURLString;
	}

	public void setThumbnailVisible(boolean thumbnailVisible) {
		this.thumbnailVisible = thumbnailVisible;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUserPortraitVisible(boolean userPortraitVisible) {
		this.userPortraitVisible = userPortraitVisible;
	}

	public void setViewURL(String viewURL) {
		this.viewURL = viewURL;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Object getAssetObject() {
		return assetObject;
	}

	public void setAssetObject(Object assetObject) {
		this.assetObject = assetObject;
	}

	public long[] getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(long[] categoryIds) {
		this.categoryIds = categoryIds;
	}

	private boolean assetCategoriesOrTagsVisible;
	private long assetEntryUserId;
	private String assetRendererURLDownload;
	private boolean assetRendererURLDownloadVisible;
	private String className;
	private long classPK;
	private String content;
	private boolean contentVisible;
	private String creationDateString;
	private Date creationDate;
	private boolean creationDateVisible;
	private String creatorUserName;
	private boolean creatorVisible;
	private List<SearchResultFieldDisplayContext> documentFormFieldDisplayContexts;
	private boolean documentFormVisible;
	private String fieldAssetCategoryIds;
	private String fieldAssetTagNames;
	private String highlightedTitle;
	private String iconId;
	private boolean iconVisible;
	private String localeLanguageId;
	private String localeReminder;
	private boolean localeReminderVisible;
	private String modelResource;
	private String pathThemeImages;
	private PortletURL portletURL;
	private String thumbnailURLString;
	private boolean thumbnailVisible;
	private String title;
	private boolean userPortraitVisible;
	private String viewURL;
	private Object assetObject;
	private long[] categoryIds;
}