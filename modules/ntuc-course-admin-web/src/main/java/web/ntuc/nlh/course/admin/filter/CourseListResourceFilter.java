package web.ntuc.nlh.course.admin.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucResourceFilter;
import web.ntuc.nlh.course.admin.constants.CourseAdminWebPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + CourseAdminWebPortletKeys.COURSE_LIST_PORTLET }, service = PortletFilter.class)
public class CourseListResourceFilter extends NtucResourceFilter {

	public CourseListResourceFilter() {
		// Do nothing because not used.
	}

}
