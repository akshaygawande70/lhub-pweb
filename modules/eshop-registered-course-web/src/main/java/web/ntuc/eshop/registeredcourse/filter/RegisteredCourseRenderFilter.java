package web.ntuc.eshop.registeredcourse.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucRenderFilter;
import web.ntuc.eshop.registeredcourse.constants.RegisteredCoursePortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + RegisteredCoursePortletKeys.REGISTERED_COURSE_PORTLET, }, service = PortletFilter.class)
public class RegisteredCourseRenderFilter extends NtucRenderFilter {

	public RegisteredCourseRenderFilter() {
		// Do nothing because not used.
	}

}
