package web.ntuc.nlh.course.admin.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucResourceFilter;
import web.ntuc.nlh.course.admin.constants.CourseAdminWebPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + CourseAdminWebPortletKeys.COURSE_ADMIN_PORTLET }, service = PortletFilter.class)
public class CourseAdminResourceFilter extends NtucResourceFilter {

	public CourseAdminResourceFilter() {
		// Do nothing because not used.
	}

}
