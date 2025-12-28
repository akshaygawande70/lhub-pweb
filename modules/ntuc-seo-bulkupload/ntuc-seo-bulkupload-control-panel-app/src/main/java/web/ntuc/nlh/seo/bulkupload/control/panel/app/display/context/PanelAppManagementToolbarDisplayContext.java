package web.ntuc.nlh.seo.bulkupload.control.panel.app.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.BaseManagementToolbarDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Reference;

import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.MVCCommandNames;
import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.NtucSeoBulkuploadControlPanelAppPortletKeys;

public class PanelAppManagementToolbarDisplayContext extends BaseManagementToolbarDisplayContext {
	Log log = LogFactoryUtil.getLog(PanelAppManagementToolbarDisplayContext.class.getName());

	public PanelAppManagementToolbarDisplayContext(LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse, HttpServletRequest httpServletRequest) {
		super(httpServletRequest, liferayPortletRequest, liferayPortletResponse);
		portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(liferayPortletRequest);
		themeDisplay = (ThemeDisplay) httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);
	}

	@Override
	public CreationMenu getCreationMenu() {
		return new CreationMenu() {
			{
				addDropdownItem(dropdownItem -> {
					dropdownItem.setHref(liferayPortletResponse.createRenderURL(), "mvcRenderCommandName",
							MVCCommandNames.ADD_SEO, "redirect", currentURLObj.toString());
					dropdownItem.setLabel(LanguageUtil.get(request, "Upload"));
				});
			}
		};
	}

	@Override
	public String getClearResultsURL() {
		return getSearchActionURL();
	}

	public String getDisplayStyle() {
		String displayStyle = ParamUtil.getString(request, "displayStyle");
		if (Validator.isNull(displayStyle)) {
			displayStyle = portalPreferences.getValue(
					NtucSeoBulkuploadControlPanelAppPortletKeys.NTUCSEOBULKUPLOADCONTROLPANELAPP,
					"bulkupload-display-style", "list");
		} else {
			portalPreferences.setValue(NtucSeoBulkuploadControlPanelAppPortletKeys.NTUCSEOBULKUPLOADCONTROLPANELAPP,
					"bulkupload-display-style", displayStyle);
		}
		return displayStyle;
	}

	@Override
	protected String getOrderByCol() {
		return ParamUtil.getString(request, "orderByCol", "createDate");
	}

	@Override
	protected String getOrderByType() {
		return ParamUtil.getString(request, "orderByType", "asc");
	}

	@Override
	public String getSearchActionURL() {
		PortletURL searchURL = liferayPortletResponse.createRenderURL();
		String navigation = ParamUtil.getString(request, "seoBulkUploadEntries");
		searchURL.setParameter("navigation", navigation);
		searchURL.setParameter("orderByCol", getOrderByCol());
		searchURL.setParameter("orderByType", getOrderByType());
		return searchURL.toString();
	}

	@Override
	protected List<DropdownItem> getOrderByDropdownItems() {
		return new DropdownItemList() {
			{
				add(dropdownItem -> {
					dropdownItem.setActive("fileName".equals(getOrderByCol()));
					dropdownItem.setHref(_getCurrentSortingURL(), "orderByCol", "fileName");
					dropdownItem.setLabel(LanguageUtil.get(request, "File Name"));
				});
				add(dropdownItem -> {
					dropdownItem.setActive("userName".equals(getOrderByCol()));
					dropdownItem.setHref(_getCurrentSortingURL(), "orderByCol", "userName");
					dropdownItem.setLabel(LanguageUtil.get(request, "Uploaded by User"));
				});
				add(dropdownItem -> {
					dropdownItem.setActive("createDate".equals(getOrderByCol()));
					dropdownItem.setHref(_getCurrentSortingURL(), "orderByCol", "createDate");
					dropdownItem.setLabel(LanguageUtil.get(request, "Uploaded Date"));
				});
			}
		};
	}

	private PortletURL _getCurrentSortingURL() throws PortletException {
		PortletURL sortingURL = PortletURLUtil.clone(currentURLObj, liferayPortletResponse);
		sortingURL.setParameter("mvcRenderCommandName", MVCCommandNames.VIEW_SEO);
		sortingURL.setParameter(SearchContainer.DEFAULT_CUR_PARAM, "0");
		String fileName = ParamUtil.getString(request, "fileName");
		String userName = ParamUtil.getString(request, "userName");
		if (Validator.isNotNull(userName)) {
			sortingURL.setParameter("userName", userName);
		}
		if (Validator.isNotNull(fileName)) {
			sortingURL.setParameter("fileName", fileName);
		}
		return sortingURL;
	}

	@Reference
	private final PortalPreferences portalPreferences;
	@Reference
	private final ThemeDisplay themeDisplay;
}
