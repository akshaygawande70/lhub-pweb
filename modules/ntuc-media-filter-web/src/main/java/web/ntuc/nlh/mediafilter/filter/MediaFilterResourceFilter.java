package web.ntuc.nlh.mediafilter.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucResourceFilter;
import web.ntuc.nlh.mediafilter.constants.MediaFilterPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + MediaFilterPortletKeys.MEDIAFILTER_PORTLET }, service = PortletFilter.class)
public class MediaFilterResourceFilter extends NtucResourceFilter {

	public MediaFilterResourceFilter() {
		// Do nothing because not used.
	}

}
