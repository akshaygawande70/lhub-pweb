package web.ntuc.eshop.invoice.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucRenderFilter;
import web.ntuc.eshop.invoice.constants.InvoicePortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + InvoicePortletKeys.INVOICE_COURSE_PORTLET, }, service = PortletFilter.class)
public class InvoiceCourseRenderFilter extends NtucRenderFilter {

	public InvoiceCourseRenderFilter() {
		// Do nothing because not used.
	}

}
