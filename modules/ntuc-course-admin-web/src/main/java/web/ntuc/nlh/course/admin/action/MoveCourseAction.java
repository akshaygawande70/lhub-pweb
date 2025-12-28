package web.ntuc.nlh.course.admin.action;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.course.admin.exception.CourseValidationException;
import svc.ntuc.nlh.parameter.exception.ParameterValidationException;
import web.ntuc.nlh.course.admin.constants.CourseAdminMessagesKey;
import web.ntuc.nlh.course.admin.constants.CourseAdminWebPortletKeys;
import web.ntuc.nlh.course.admin.constants.MVCCommandNames;
import web.ntuc.nlh.course.admin.util.GenerateCourseCMS;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.MOVE_COURSE_ACTION,
		"javax.portlet.name=" + CourseAdminWebPortletKeys.COURSE_ADMIN_PORTLET }, service = MVCActionCommand.class)
public class MoveCourseAction extends BaseMVCActionCommand {
	private static Log log = LogFactoryUtil.getLog(MoveCourseAction.class);

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Move course action - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

			/*boolean isAuthorized = ParamUtil.getBoolean(actionRequest, "isAuthorized");
			boolean validCSRF = ParamUtil.getBoolean(actionRequest, "validCSRF");
			boolean xssPass = ParamUtil.getBoolean(actionRequest, "xssPass");

			if (!isAuthorized || !validCSRF) {
				String msg = "isAuthorized : " + isAuthorized + " | validCSRF : " + validCSRF;
				SessionErrors.add(actionRequest, "you-dont-have-permission-or-your-session-is-end");
				throw new CourseValidationException(msg);
			}

			if (!xssPass) {
				PortletConfig portletConfig = (PortletConfig) actionRequest
						.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
				String msg = LanguageUtil.format(portletConfig.getResourceBundle(themeDisplay.getLocale()),
						CourseAdminMessagesKey.XSS_VALIDATION_NOT_PASS, xssPass);
				SessionErrors.add(actionRequest, "your-input-not-pass-xss");
				throw new ParameterValidationException(msg);
			}*/
			
			PortletCommandUtil.actionAndResourceCommand(actionRequest,themeDisplay);
			Executor executor = Executors.newFixedThreadPool(10);
			GenerateCourseCMS generateCourseCMS = new GenerateCourseCMS();
			CompletableFuture.supplyAsync(() -> {
				generateCourseCMS.moveApprovedCourse();
				return null;
			}, executor);

		} catch (Exception e) {
			log.error("Error while move course: " + e.getMessage(), e);
		}
		log.info("Move course action - end");

	}
}
