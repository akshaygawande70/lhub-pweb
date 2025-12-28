package web.ntuc.eshop.registeredcourse.render;

import com.liferay.commerce.account.constants.CommerceAccountConstants;
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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.DateUtil;
import api.ntuc.common.util.PortletCommandUtil;
import api.ntuc.common.util.RoleUtil;
import web.ntuc.eshop.registeredcourse.constants.RegisteredCoursePortletKeys;
import web.ntuc.eshop.registeredcourse.dto.RegisteredCourseDto;
import web.ntuc.eshop.registeredcourse.util.RegisterCourseUtil;

@Component(immediate = true, property = {
		"mvc.command.name=" + web.ntuc.eshop.registeredcourse.constants.MVCCommandNames.VIEW_ATTENDANCES_RENDER,
		"javax.portlet.name="
				+ RegisteredCoursePortletKeys.REGISTERED_COURSE_PORTLET }, service = MVCRenderCommand.class)
public class ViewAttendanceRender implements MVCRenderCommand {

	private static Log log = LogFactoryUtil.getLog(ViewAttendanceRender.class);
	private static String attendancePage = "/attendance/view-attendance.jsp";

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		log.info("View Attendance Render - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			String authToken = CSRFValidationUtil.authToken(renderRequest);
			PortletCommandUtil.renderCommand(renderRequest);
			String nric = "";
			Date tempDate = null;
			String birthDate = "";

			String batchId = ParamUtil.getString(renderRequest, "batchId");

			User user = themeDisplay.getUser();
			long userId = user.getUserId();

			CommerceAccount account = null;

			List<Role> roleList = new ArrayList<>();
			for (Long roleId : user.getRoleIds()) {
				Role newRole = RoleServiceUtil.getRole(roleId);
				roleList.add(newRole);
			}

			account = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());

			nric = (String) account.getExpandoBridge().getAttribute("NRIC");

			birthDate = DateUtil.toString(user.getBirthday(), format);

			renderRequest.setAttribute("titlePage", "Attendance");
			renderRequest.setAttribute("nric", nric);
			renderRequest.setAttribute("birthDate", birthDate);
			renderRequest.setAttribute("accType", 1);

			renderRequest.setAttribute("batchId", batchId);
			renderRequest.setAttribute("authToken", authToken);

		} catch (Exception e) {
			log.error("Failed when render data from " + e.getMessage());
		}
		log.info("View Attendance Render - Stop");
		return attendancePage;
	}

}
