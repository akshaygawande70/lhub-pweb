package web.ntuc.nlh.course.admin.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucActionFilter;
import web.ntuc.nlh.course.admin.constants.CourseAdminWebPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + CourseAdminWebPortletKeys.COURSE_ADMIN_PORTLET }, service = PortletFilter.class)
public class CourseAdminActionFilter extends NtucActionFilter {

	public CourseAdminActionFilter() {
		// Do nothing because not used.
	}
}
