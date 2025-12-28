package web.ntuc.eshop.registeredcourse.render;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.RoleServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import web.ntuc.eshop.registeredcourse.dto.ExamResultDto;
import web.ntuc.eshop.registeredcourse.dto.RegisteredCourseDto;
import web.ntuc.eshop.registeredcourse.util.RegisterCourseUtil;

@Component(immediate = true, property = {
		"mvc.command.name=" + web.ntuc.eshop.registeredcourse.constants.MVCCommandNames.VIEW_EXAM_RESULT_DETAIL_RENDER,
		"javax.portlet.name="
				+ RegisteredCoursePortletKeys.REGISTERED_COURSE_PORTLET }, service = MVCRenderCommand.class)
public class ViewExamResultDetailRender implements MVCRenderCommand {

	private static Log log = LogFactoryUtil.getLog(ViewExamResultDetailRender.class);
	private static String examResultDetailPage = "/exam-result/view-exam-result-detail.jsp";

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		log.info("View Exam Result Detail Render - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy HH:mm");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

			String authToken = CSRFValidationUtil.authToken(renderRequest);
			PortletCommandUtil.renderCommand(renderRequest);
			String nric = "";
			String birthDate = "";
			String fullName = "";
			String fullNameCap = "";

			User user = themeDisplay.getUser();

			String batchId = ParamUtil.getString(renderRequest, "batchId");
			String examName = ParamUtil.getString(renderRequest, "examName");
			
//			log.info("exam name = "+examName);
//			log.info("batch Id  = "+batchId);
			CommerceAccount account = null;

			List<Role> roleList = new ArrayList<>();
			for (Long roleId : user.getRoleIds()) {
				Role newRole = RoleServiceUtil.getRole(roleId);
				roleList.add(newRole);
			}

			account = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());

			nric = (String) account.getExpandoBridge().getAttribute("NRIC");
			birthDate = DateUtil.toString(user.getBirthday(), format2);

			PortletSession session = renderRequest.getPortletSession();

			RegisteredCourseDto registeredCourseDto = (RegisteredCourseDto) session
					.getAttribute("registeredCourse_" + user.getUuid(), PortletSession.PORTLET_SCOPE);

			@SuppressWarnings("unchecked")
			List<ExamResultDto> examResultList = (List<ExamResultDto>) session.getAttribute("examResultList_" + batchId,
					PortletSession.PORTLET_SCOPE);
//			log.info(examResultList);
			if (!user.getMiddleName().isEmpty()) {
				fullName = user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
			} else {
				fullName = user.getFirstName() + " " + user.getLastName();
			}

			fullNameCap = fullName.toUpperCase();

			for (ExamResultDto examResult : examResultList) {
				String examTemp = examResult.getExamName();
				if (examTemp.trim().equals(examName)) {
					session.setAttribute("examResult_" + examName, examResult, PortletSession.PORTLET_SCOPE);

				}
			}

			Date courseStartDate = registeredCourseDto.getCourseStartDate();
			Date courseEndDate = registeredCourseDto.getCourseEndDate();

			String startDate = DateUtil.toString(courseStartDate, format) + " SGT";
			String endDate = DateUtil.toString(courseEndDate, format) + " SGT";
			log.info("Start Date : " + startDate + " | End Date : " + endDate);

			renderRequest.setAttribute("titlePage", "Exam Results");
			renderRequest.setAttribute("registeredCourse", registeredCourseDto);
			renderRequest.setAttribute("examResultList", examResultList);
			renderRequest.setAttribute("nric", nric);
			renderRequest.setAttribute("birthDate", birthDate);

			renderRequest.setAttribute("batchId", registeredCourseDto.getBatchId());
			renderRequest.setAttribute("examName", examName);
			renderRequest.setAttribute("courseCode", registeredCourseDto.getCourseCode());
			renderRequest.setAttribute("courseTitle", registeredCourseDto.getCourseTitle());
			renderRequest.setAttribute("startDate", startDate);
			renderRequest.setAttribute("endDate", endDate);
			renderRequest.setAttribute("fullName", fullNameCap);
			renderRequest.setAttribute("authToken", authToken);
		} catch (Exception e) {
			log.error("Failed when render data from " + e.getMessage());
		}
		log.info("View Exam Result Detail Render - Stop");
		return examResultDetailPage;
	}

}
