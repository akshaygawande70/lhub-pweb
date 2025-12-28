package web.ntuc.nlh.course.admin.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.constants.PortletDisplayCategoryConstant;
import api.ntuc.common.util.CSRFValidationUtil;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalService;
import svc.ntuc.nlh.parameter.service.ParameterLocalService;
import web.ntuc.nlh.course.admin.constants.CourseAdminWebPortletKeys;

/**
 * @author fandifadillah
 */
@Component(immediate = true, property = {
		"com.liferay.portlet.display-category=" + PortletDisplayCategoryConstant.ADMIN,
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.init-param.template-path=/", "javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.version=3.0", "javax.portlet.name=" + CourseAdminWebPortletKeys.COURSE_ADMIN_PORTLET,
		"javax.portlet.display-name=" + CourseAdminWebPortletKeys.COURSE_ADMIN_DISPLAY,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class CourseAdminWebPortlet extends MVCPortlet {

	private static Log log = LogFactoryUtil.getLog(CourseAdminWebPortlet.class);

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		log.info("Course admin portlet render - start");
		try {
			String authToken = CSRFValidationUtil.authToken(renderRequest);
			renderRequest.setAttribute("authToken", authToken);
			
		} catch (Exception e) {
			log.error("Failed when render Course admin, error:" + e.getMessage());
		}

		log.info("Course admin portlet render - start");
		super.render(renderRequest, renderResponse);
	}

	@Reference
	protected ParameterLocalService parameterLocalService;

	@Reference
	protected ParameterGroupLocalService parameterGroupLocalService;
}