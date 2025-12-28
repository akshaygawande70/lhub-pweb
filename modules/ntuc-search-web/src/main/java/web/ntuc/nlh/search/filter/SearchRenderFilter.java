package web.ntuc.nlh.search.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucRenderFilter;
import web.ntuc.nlh.search.constants.SearchPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + SearchPortletKeys.SEARCH_PORTLET, }, service = PortletFilter.class)
public class SearchRenderFilter extends NtucRenderFilter {

	public SearchRenderFilter() {
		// Do nothing because not used.
	}

}
