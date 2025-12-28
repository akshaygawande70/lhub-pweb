package web.ntuc.nlh.datefilter.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucActionFilter;
import web.ntuc.nlh.datefilter.constants.DateFilterPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + DateFilterPortletKeys.DATEFILTER_PORTLET, }, service = PortletFilter.class)
public class DateFilterActionFilter extends NtucActionFilter {

	public DateFilterActionFilter() {
		// Do nothing because not used.
	}
}
