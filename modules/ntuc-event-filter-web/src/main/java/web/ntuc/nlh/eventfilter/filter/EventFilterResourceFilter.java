package web.ntuc.nlh.eventfilter.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucResourceFilter;
import web.ntuc.nlh.eventfilter.constants.EventFilterPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + EventFilterPortletKeys.EVENTFILTER_PORTLET }, service = PortletFilter.class)
public class EventFilterResourceFilter extends NtucResourceFilter {
	
	public EventFilterResourceFilter() {
		// Do nothing because not used.
	}
}
