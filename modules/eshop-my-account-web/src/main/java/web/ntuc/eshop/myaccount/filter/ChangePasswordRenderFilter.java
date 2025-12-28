package web.ntuc.eshop.myaccount.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucRenderFilter;
import web.ntuc.eshop.myaccount.constants.MyAccountPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + MyAccountPortletKeys.CHANGE_PASSWORD_PORTLET, }, service = PortletFilter.class)
public class ChangePasswordRenderFilter extends NtucRenderFilter {

	public ChangePasswordRenderFilter() {
		// Do nothing because not used.
	}

}
