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
import java.util.concurrent.ExecutorService;
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
import web.ntuc.nlh.course.admin.util.GenerateCourseCMS;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.ADD_COURSE_API_ACTION,
		"javax.portlet.name=" + CourseAdminWebPortletKeys.COURSE_ADMIN_PORTLET }, service = MVCActionCommand.class)
public class AddCourseFromApiAction extends BaseMVCActionCommand {
	private static Log log = LogFactoryUtil.getLog(AddCourseFromApiAction.class);

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Add course from api action - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			
			PortletCommandUtil.actionAndResourceCommand(actionRequest, themeDisplay);
			
			CourseConvertFromApi courseConvertFromApi = new CourseConvertFromApi();
			GenerateCourseCMS generateCourseCMS = new GenerateCourseCMS();
			courseConvertFromApi.converter();
			generateCourseCMS.moveApprovedCourse();
			ExecutorService executorService1 = Executors.newFixedThreadPool(20);
			CompletableFuture.supplyAsync(() -> {
				generateCourseCMS.generate();
				return null;
			}, executorService1);
			ExecutorService executorService2 = Executors.newFixedThreadPool(20);
			CompletableFuture.supplyAsync(() -> {
				generateCourseCMS.deleteNonTMSCourse();
				return null;
			}, executorService2);
		} catch (Exception e) {
			log.error("Error while add course from api : " + e.getMessage(), e);
		}
		log.info("Add course from api action - end");
	}
}
