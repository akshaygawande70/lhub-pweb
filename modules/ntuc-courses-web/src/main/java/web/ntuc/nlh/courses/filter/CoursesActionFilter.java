package web.ntuc.nlh.courses.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucActionFilter;
import web.ntuc.nlh.courses.constants.CoursesPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + CoursesPortletKeys.COURSES_PORTLET, }, service = PortletFilter.class)
public class CoursesActionFilter extends NtucActionFilter {

	public CoursesActionFilter() {
		// Do nothing because not used.
	}
}
