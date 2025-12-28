package web.ntuc.eshop.otp.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucActionFilter;
import web.ntuc.eshop.otp.constants.EshopOtpWebPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + EshopOtpWebPortletKeys.OTP, }, service = PortletFilter.class)
public class OtpActionFilter extends NtucActionFilter {
	public OtpActionFilter() {
		// Do nothing because not used.
	}
}
