package web.ntuc.nlh.search.result.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucActionFilter;
import web.ntuc.nlh.search.result.constants.SearchResultPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + SearchResultPortletKeys.SEARCH_RESULT_PORTLET }, service = PortletFilter.class)
public class SearchResultActionFilter extends NtucActionFilter{
	
	public SearchResultActionFilter() {
		// Do nothing because not used.
	}
}
