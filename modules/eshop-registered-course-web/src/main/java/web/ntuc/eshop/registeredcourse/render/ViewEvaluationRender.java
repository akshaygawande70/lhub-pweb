package web.ntuc.eshop.registeredcourse.render;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.DateUtil;
import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.registeredcourse.constants.RegisteredCoursePortletKeys;
import web.ntuc.eshop.registeredcourse.dto.RegisteredCourseDto;
import web.ntuc.eshop.registeredcourse.util.RegisterCourseUtil;

@Component(immediate = true, property = {
		"mvc.command.name=" + web.ntuc.eshop.registeredcourse.constants.MVCCommandNames.VIEW_EVALUATION_RENDER,
		"javax.portlet.name="
				+ RegisteredCoursePortletKeys.REGISTERED_COURSE_PORTLET }, service = MVCRenderCommand.class)
public class ViewEvaluationRender implements MVCRenderCommand {

	private static Log log = LogFactoryUtil.getLog(ViewEvaluationRender.class);

	private static String evaluationPage = "/evaluation/view-evaluation.jsp";

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		log.info("View Evaluation Render - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy HH:mm");

			String authToken = CSRFValidationUtil.authToken(renderRequest);
			PortletCommandUtil.renderCommand(renderRequest);
			String nric = "";
			String companyName = "";
			String companyCode = "";
			String uenNumber = "";
			int accType = 0;
			String birthDate = "";

			User user = themeDisplay.getUser();

			String batchId = ParamUtil.getString(renderRequest, "batchId");
			String subBookingId = ParamUtil.getString(renderRequest, "subBookingId");

			CommerceAccount account = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());

			uenNumber = (String) account.getExpandoBridge().getAttribute("UEN Number");
			companyCode = (String) account.getExpandoBridge().getAttribute("Company Code");
			companyName = (String) account.getExpandoBridge().getAttribute("Company Name");

			PortletSession session = renderRequest.getPortletSession();
			Object courseListSession = (Object) session.getAttribute("courseListResponse_" + user.getUuid(),
					PortletSession.PORTLET_SCOPE);

			RegisteredCourseDto selectedCourse = RegisterCourseUtil.getSelectedCourse(2, batchId, courseListSession,
					log);

			Date courseStartDate = selectedCourse.getCourseStartDate();
			Date courseEndDate = selectedCourse.getCourseEndDate();

			int totalTrainee = selectedCourse.getTraineeList().size();

			String startDate = DateUtil.toString(courseStartDate, format) + " SGT";
			String endDate = DateUtil.toString(courseEndDate, format) + " SGT";

			renderRequest.setAttribute("totalTrainee", totalTrainee);
			renderRequest.setAttribute("titlePage", "Course Evaluation");
			renderRequest.setAttribute("batchId", selectedCourse.getBatchId());
			renderRequest.setAttribute("courseCode", selectedCourse.getCourseCode());
			renderRequest.setAttribute("courseTitle", selectedCourse.getCourseTitle());
			renderRequest.setAttribute("startDate", startDate);
			renderRequest.setAttribute("endDate", endDate);

			renderRequest.setAttribute("accType", 2);
			renderRequest.setAttribute("nric", nric);
			renderRequest.setAttribute("birthDate", birthDate);
			renderRequest.setAttribute("uenNumber", uenNumber);
			renderRequest.setAttribute("companyCode", companyCode);
			renderRequest.setAttribute("companyName", companyName);

			renderRequest.setAttribute("batchId", batchId);
			renderRequest.setAttribute("subBookingId", subBookingId);
			renderRequest.setAttribute("authToken", authToken);

		} catch (Exception e) {
			log.error("Failed when render data from " + e.getMessage());
		}
		log.info("View Evaluation Render - Stop");
		return evaluationPage;
	}

}
