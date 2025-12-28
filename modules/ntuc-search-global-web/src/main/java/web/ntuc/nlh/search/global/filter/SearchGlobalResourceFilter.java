package web.ntuc.nlh.search.global.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucResourceFilter;
import web.ntuc.nlh.search.global.constants.SearchGlobalPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + SearchGlobalPortletKeys.SEARCH_GLOBAL_PORTLET, }, service = PortletFilter.class)
public class SearchGlobalResourceFilter extends NtucResourceFilter {
	
	public SearchGlobalResourceFilter() {
		// Do nothing
	}
}
