package web.ntuc.nlh.course.admin.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucRenderFilter;
import web.ntuc.nlh.course.admin.constants.CourseAdminWebPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + CourseAdminWebPortletKeys.COURSE_ADMIN_PORTLET }, service = PortletFilter.class)
public class CourseAdminRenderFilter extends NtucRenderFilter {

	public CourseAdminRenderFilter() {
		// Do nothing because not used.
	}

}
