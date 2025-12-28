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
import web.ntuc.nlh.course.admin.util.CourseConvertFromApi;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.CONVERT_COURSE_ACTION,
		"javax.portlet.name=" + CourseAdminWebPortletKeys.COURSE_ADMIN_PORTLET }, service = MVCActionCommand.class)
public class ConvertCourseAction extends BaseMVCActionCommand {
	private static Log log = LogFactoryUtil.getLog(ConvertCourseAction.class);

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Convert course action - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

			PortletCommandUtil.actionAndResourceCommand(actionRequest, themeDisplay);
			
			Executor executor = Executors.newFixedThreadPool(10);
			CourseConvertFromApi courseConvertFromApi = new CourseConvertFromApi();
			CompletableFuture.supplyAsync(() -> {
				courseConvertFromApi.converter();
				return null;
			}, executor);

		} catch (Exception e) {
			log.error("Error while convert course: " + e.getMessage(), e);
		}
		log.info("Convert course action - end");

	}

}
