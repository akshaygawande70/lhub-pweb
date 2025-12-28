package web.ntuc.nlh.courses.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucResourceFilter;
import web.ntuc.nlh.courses.constants.CoursesPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + CoursesPortletKeys.COURSES_PORTLET, }, service = PortletFilter.class)
public class CoursesResourceFilter extends NtucResourceFilter {

	public CoursesResourceFilter() {
		// Do nothing because not used.
	}
}
