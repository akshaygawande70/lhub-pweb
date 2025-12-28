package web.ntuc.eshop.reports.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucActionFilter;
import web.ntuc.eshop.reports.constants.ReportPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + ReportPortletKeys.SALES_AND_SUMMARY_REPORT_PORTLET, }, service = PortletFilter.class)
public class ReportActionFilter extends NtucActionFilter {

	public ReportActionFilter() {
		// Do nothing because not used.
	}
}
