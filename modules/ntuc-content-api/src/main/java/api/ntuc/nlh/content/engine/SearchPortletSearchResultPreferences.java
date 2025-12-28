package api.ntuc.nlh.content.engine;

import com.liferay.portal.kernel.util.GetterUtil;

import javax.portlet.PortletPreferences;

public class SearchPortletSearchResultPreferences implements SearchResultPreferences {

	private Boolean displayResultsInDocumentForm;
	private final DocumentFormPermissionChecker documentFormPermissionChecker;
	private Boolean highlightEnabled;
	private final PortletPreferences portletPreferences;
	private Boolean viewInContext;

	public SearchPortletSearchResultPreferences(PortletPreferences portletPreferences,
			ThemeDisplaySupplier themeDisplaySupplier) {
		this.portletPreferences = portletPreferences;

		this.documentFormPermissionChecker = new DocumentFormPermissionCheckerImpl(
				themeDisplaySupplier.getThemeDisplay());
	}

	public boolean isDisplayResultsInDocumentForm() {
		if (this.displayResultsInDocumentForm != null) {
			return this.displayResultsInDocumentForm.booleanValue();
		}
		if (this.documentFormPermissionChecker.hasPermission()) {
			this.displayResultsInDocumentForm = Boolean.valueOf(
					GetterUtil.getBoolean(this.portletPreferences.getValue("displayResultsInDocumentForm", null)));
		} else {
			this.displayResultsInDocumentForm = Boolean.valueOf(false);
		}
		return this.displayResultsInDocumentForm.booleanValue();
	}

	public boolean isHighlightEnabled() {
		if (this.highlightEnabled != null) {
			return this.highlightEnabled.booleanValue();
		}
		this.highlightEnabled = Boolean
				.valueOf(GetterUtil.getBoolean(this.portletPreferences.getValue("highlightEnabled", null), true));

		return this.highlightEnabled.booleanValue();
	}

	public boolean isViewInContext() {
		if (this.viewInContext != null) {
			return this.viewInContext.booleanValue();
		}
		this.viewInContext = Boolean
				.valueOf(GetterUtil.getBoolean(this.portletPreferences.getValue("viewInContext", null), true));

		return this.viewInContext.booleanValue();
	}
}
