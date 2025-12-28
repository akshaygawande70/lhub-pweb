package web.ntuc.eshop.invoice.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucActionFilter;
import web.ntuc.eshop.invoice.constants.InvoicePortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + InvoicePortletKeys.INVOICE_MERCHANDIZE_PORTLET, }, service = PortletFilter.class)
public class InvoiceMerchandizeActionFilter extends NtucActionFilter {

	public InvoiceMerchandizeActionFilter() {
		// Do nothing because not used.
	}
}
