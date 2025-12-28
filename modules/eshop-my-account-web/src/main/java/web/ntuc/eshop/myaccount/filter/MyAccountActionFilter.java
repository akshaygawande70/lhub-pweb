package web.ntuc.eshop.myaccount.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucActionFilter;
import web.ntuc.eshop.myaccount.constants.MyAccountPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + MyAccountPortletKeys.MY_ACCOUNT_PORTLET, }, service = PortletFilter.class)
public class MyAccountActionFilter extends NtucActionFilter {

	public MyAccountActionFilter() {
		// Do nothing because not used.
	}
}
