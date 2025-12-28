package web.ntuc.eshop.invoice.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucResourceFilter;
import web.ntuc.eshop.invoice.constants.InvoicePortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + InvoicePortletKeys.INVOICE_EXAM_MERCHANDIZE_PORTLET, }, service = PortletFilter.class)
public class InvoiceExamMerchandizeResourceFilter extends NtucResourceFilter {

	public InvoiceExamMerchandizeResourceFilter() {
		// Do nothing because not used.
	}

}