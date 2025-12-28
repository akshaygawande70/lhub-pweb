package api.ntuc.nlh.content.engine;

import com.liferay.portal.kernel.theme.ThemeDisplay;

import javax.portlet.PortletRequest;

public class PortletRequestThemeDisplaySupplier implements ThemeDisplaySupplier {
	
	private final PortletRequest portletRequest;

	public PortletRequestThemeDisplaySupplier(PortletRequest portletRequest) {
		this.portletRequest = portletRequest;
	}

	public ThemeDisplay getThemeDisplay() {
		return (ThemeDisplay) this.portletRequest.getAttribute("LIFERAY_SHARED_THEME_DISPLAY");
	}
}
