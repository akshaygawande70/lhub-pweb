package web.ntuc.nlh.eventfilter.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucRenderFilter;
import web.ntuc.nlh.eventfilter.constants.EventFilterPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + EventFilterPortletKeys.EVENTFILTER_PORTLET }, service = PortletFilter.class)
public class EventFilterRenderFilter extends NtucRenderFilter {

	public EventFilterRenderFilter() {
		// Do nothing because not used.
	}
}
