package web.ntuc.nlh.search.result.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucRenderFilter;
import web.ntuc.nlh.search.result.constants.SearchResultPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + SearchResultPortletKeys.SEARCH_RESULT_PORTLET }, service = PortletFilter.class)
public class SearchResultRenderFilter extends NtucRenderFilter {

	public SearchResultRenderFilter() {
		// Do nothing because not used.
	}

}
