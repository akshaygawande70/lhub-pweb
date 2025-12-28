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
import web.ntuc.eshop.registeredcourse.dto.RegisteredCourseDto;
import web.ntuc.eshop.registeredcourse.util.RegisterCourseUtil;

@Component(immediate = true, property = {
		"mvc.command.name=" + web.ntuc.eshop.registeredcourse.constants.MVCCommandNames.VIEW_ATTENDANCES_DETAIL_RENDER,
		"javax.portlet.name="
				+ RegisteredCoursePortletKeys.REGISTERED_COURSE_PORTLET }, service = MVCRenderCommand.class)
public class ViewAttendanceDetailRender implements MVCRenderCommand {

	private static Log log = LogFactoryUtil.getLog(ViewAttendanceDetailRender.class);

	private static String attendanceDetailPage = "/attendance/view-attendance-detail.jsp";

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		log.info("View Attendance Detail Render - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format2 = new SimpleDateFormat("dd MMMM yyyy HH:mm");

			String authToken = CSRFValidationUtil.authToken(renderRequest);
			PortletCommandUtil.renderCommand(renderRequest);
			String nric = "";
			String birthDate = "";
			String fullName = "";
			String fullNameCap = "";

			String batchId = ParamUtil.getString(renderRequest, "batchId");

			User user = themeDisplay.getUser();

			if (!user.getMiddleName().isEmpty()) {
				fullName = user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
			} else {
				fullName = user.getFirstName() + " " + user.getLastName();
			}

			fullNameCap = fullName.toUpperCase();

			CommerceAccount account = null;

			List<Role> roleList = new ArrayList<>();
			for (Long roleId : user.getRoleIds()) {
				Role newRole = RoleServiceUtil.getRole(roleId);
				roleList.add(newRole);
			}

			account = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());

			nric = (String) account.getExpandoBridge().getAttribute("NRIC");
			birthDate = DateUtil.toString(user.getBirthday(), format);

			PortletSession session = renderRequest.getPortletSession();
			Object courseListSession = (Object) session.getAttribute("courseListResponse_" + user.getUuid(),
					PortletSession.PORTLET_SCOPE);

			RegisteredCourseDto registeredCourseDto = RegisterCourseUtil.getSelectedCourse(1, batchId,
					courseListSession, log);

			@SuppressWarnings("unchecked")
			List<RegisteredCourseDto> attendanceList = (List<RegisteredCourseDto>) session
					.getAttribute("attendanceList_" + batchId, PortletSession.PORTLET_SCOPE);

			Date courseStartDate = registeredCourseDto.getCourseStartDate();
			Date courseEndDate = registeredCourseDto.getCourseEndDate();

			String startDate = DateUtil.toString(courseStartDate, format2) + " SGT";
			String endDate = DateUtil.toString(courseEndDate, format2) + " SGT";

			renderRequest.setAttribute("titlePage", "Attendance");
			renderRequest.setAttribute("registeredCourse", registeredCourseDto);
			renderRequest.setAttribute("attendanceList", attendanceList);
			renderRequest.setAttribute("nric", nric);
			renderRequest.setAttribute("birthDate", birthDate);

			renderRequest.setAttribute("batchId", registeredCourseDto.getBatchId());
			renderRequest.setAttribute("courseCode", registeredCourseDto.getCourseCode());
			renderRequest.setAttribute("courseTitle", registeredCourseDto.getCourseTitle());
			renderRequest.setAttribute("startDate", startDate);
			renderRequest.setAttribute("endDate", endDate);
			renderRequest.setAttribute("fullName", fullNameCap);
			renderRequest.setAttribute("authToken", authToken);

		} catch (Exception e) {
			log.error("Failed when render data from " + e.getMessage());
		}
		log.info("View Attendance Detail Render - Stop");
		return attendanceDetailPage;
	}
}
