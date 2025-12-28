package web.ntuc.eshop.register.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucActionFilter;
import web.ntuc.eshop.register.constants.RegisterPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + RegisterPortletKeys.REGISTER, }, service = PortletFilter.class)
public class RegisterActionFilter extends NtucActionFilter{
	public RegisterActionFilter() {
		// Do nothing because not used.
	}
}
