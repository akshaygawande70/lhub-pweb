package web.ntuc.eshop.otp.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucRenderFilter;
import web.ntuc.eshop.otp.constants.EshopOtpWebPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + EshopOtpWebPortletKeys.OTP, }, service = PortletFilter.class)
public class OtpRenderFilter extends NtucRenderFilter{
	public OtpRenderFilter() {
		// Do nothing because not used.
	}
}
