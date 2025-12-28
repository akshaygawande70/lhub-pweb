package web.ntuc.nlh.course.admin.render;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.course.admin.model.Course;
import svc.ntuc.nlh.course.admin.service.CourseLocalServiceUtil;
import web.ntuc.nlh.course.admin.constants.CourseAdminWebPortletKeys;
import web.ntuc.nlh.course.admin.constants.MVCCommandNames;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.COURSE_VIEW_RENDER,
		"javax.portlet.name=" + CourseAdminWebPortletKeys.COURSE_ADMIN_PORTLET }, service = MVCRenderCommand.class)
public class CourseViewRender implements MVCRenderCommand {

	private static Log log = LogFactoryUtil.getLog(CourseViewRender.class);
	private static String viewCourse = "/view-action.jsp";

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		try {

			PortletCommandUtil.renderCommand(renderRequest);

			long courseId = ParamUtil.getLong(renderRequest, "id", 0);
			Course course = CourseLocalServiceUtil.getCourse(courseId);

			renderRequest.setAttribute("p", course);

		} catch (Exception e) {
			log.error("Course View Render Error : " + e.getMessage());
			SessionErrors.add(renderRequest, "no-course-available");
		}
		return viewCourse;
	}

}
