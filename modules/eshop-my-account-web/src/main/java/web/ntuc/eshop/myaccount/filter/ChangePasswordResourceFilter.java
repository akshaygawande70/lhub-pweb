package web.ntuc.eshop.myaccount.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucResourceFilter;
import web.ntuc.eshop.myaccount.constants.MyAccountPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + MyAccountPortletKeys.CHANGE_PASSWORD_PORTLET, }, service = PortletFilter.class)
public class ChangePasswordResourceFilter extends NtucResourceFilter {

	public ChangePasswordResourceFilter() {
		// Do nothing because not used.
	}

}
