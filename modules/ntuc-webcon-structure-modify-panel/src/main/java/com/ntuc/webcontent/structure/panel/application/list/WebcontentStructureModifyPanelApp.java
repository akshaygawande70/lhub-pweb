package com.ntuc.webcontent.structure.panel.application.list;

import com.ntuc.webcontent.structure.panel.constants.WebcontentStructureModifyPanelCategoryKeys;
import com.ntuc.webcontent.structure.panel.constants.WebcontentStructureModifyPortletKeys;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.portal.kernel.model.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Dhivakar Sengottaiyan
 * The type Webcontent structure modify panel app.
 */
@Component(
	property = {
		"panel.app.order:Integer=100",
		"panel.category.key=" + WebcontentStructureModifyPanelCategoryKeys.CONTROL_PANEL_CATEGORY
	},
	service = PanelApp.class
)
public class WebcontentStructureModifyPanelApp extends BasePanelApp {

	@Override
	public String getPortletId() {
		return WebcontentStructureModifyPortletKeys.WEBCONTENTSTRUCTUREMODIFY;
	}

	@Override
	@Reference(
		target = "(javax.portlet.name=" + WebcontentStructureModifyPortletKeys.WEBCONTENTSTRUCTUREMODIFY + ")",
		unbind = "-"
	)
	public void setPortlet(Portlet portlet) {
		super.setPortlet(portlet);
	}

}