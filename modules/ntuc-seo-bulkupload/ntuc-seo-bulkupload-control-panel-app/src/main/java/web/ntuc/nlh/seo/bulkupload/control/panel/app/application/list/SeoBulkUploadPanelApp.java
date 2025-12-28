package web.ntuc.nlh.seo.bulkupload.control.panel.app.application.list;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.portal.kernel.model.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.NtucSeoBulkuploadControlPanelAppCategoryKeys;
import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.NtucSeoBulkuploadControlPanelAppPortletKeys;

/**
 * @author sagar
 * The type Seo bulk upload panel app.
 */
@Component(immediate = true, property = { "panel.app.order:Integer=100", "panel.category.key="
		+ NtucSeoBulkuploadControlPanelAppCategoryKeys.CONTROL_PANEL_CATEGORY }, service = PanelApp.class)

public class SeoBulkUploadPanelApp extends BasePanelApp {

	@Override
	public String getPortletId() {
		return NtucSeoBulkuploadControlPanelAppPortletKeys.NTUCSEOBULKUPLOADCONTROLPANELAPP;
	}

	@Override
	@Reference(target = "(javax.portlet.name="
			+ NtucSeoBulkuploadControlPanelAppPortletKeys.NTUCSEOBULKUPLOADCONTROLPANELAPP + ")", unbind = "-")
	public void setPortlet(Portlet portlet) {
		super.setPortlet(portlet);
	}
}
