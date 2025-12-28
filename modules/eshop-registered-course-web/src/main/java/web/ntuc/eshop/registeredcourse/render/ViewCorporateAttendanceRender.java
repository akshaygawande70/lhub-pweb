package web.ntuc.eshop.registeredcourse.render;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.RoleServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.AESEncryptUtil;
import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.DateUtil;
import api.ntuc.common.util.HttpApiUtil;
import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.registeredcourse.constants.RegisteredCoursePortletKeys;
import web.ntuc.eshop.registeredcourse.dto.AttendanceDto;
import web.ntuc.eshop.registeredcourse.dto.CorporateAttendanceDto;
import web.ntuc.eshop.registeredcourse.dto.RegisteredCourseDto;
import web.ntuc.eshop.registeredcourse.dto.TraineeDto;
import web.ntuc.eshop.registeredcourse.util.RegisterCourseUtil;

@Component(immediate = true, property = {
		"mvc.command.name="
				+ web.ntuc.eshop.registeredcourse.constants.MVCCommandNames.VIEW_CORPORATE_ATTENDANCES_RENDER,
		"javax.portlet.name="
				+ RegisteredCoursePortletKeys.REGISTERED_COURSE_PORTLET }, service = MVCRenderCommand.class)
public class ViewCorporateAttendanceRender implements MVCRenderCommand {

	private static Log log = LogFactoryUtil.getLog(ViewCorporateAttendanceRender.class);

	private static String corpAttendancePage = "/attendance/view-corporate-attendance.jsp";

	private static final String CORPORATE_ATTENDANCE_URL = "/corporate/attendance";

	private RegisterCourseUtil courseUtil = new RegisterCourseUtil();

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		log.info("View Corporate Attendance Render - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

			SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy HH:mm");

			String authToken = CSRFValidationUtil.authToken(renderRequest);
			PortletCommandUtil.renderCommand(renderRequest);
			String companyName = "";
			String companyCode = "";
			String uenNumber = "";

			String batchId = ParamUtil.getString(renderRequest, "batchId");
			String subBookingId = ParamUtil.getString(renderRequest, "subBookingId");
			// log.info("batch ID Render : " + batchId + " | Sub Booking ID : " +
			// subBookingId);

			User user = themeDisplay.getUser();

			CommerceAccount account = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());
			// log.info("This Account : " + account.toString());

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

			String startDate = DateUtil.toString(courseStartDate, format) + " SGT";
			String endDate = DateUtil.toString(courseEndDate, format) + " SGT";

			ParameterGroup parameterAuthGroup = ParameterGroupLocalServiceUtil
					.getByCode(RegisteredCoursePortletKeys.PARAMETER_AUTH_GROUP_CODE, false);
			long siteGroupId = parameterAuthGroup.getGroupId();
			Parameter parameterClientId = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(),
					RegisteredCoursePortletKeys.PARAMETER_ESHOP_CLIENT_ID_CODE, false);

			Parameter parameterClientSecret = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(),
					RegisteredCoursePortletKeys.PARAMETER_ESHOP_CLIENT_SECRET_CODE, false);

			ParameterGroup parameterApiGroup = ParameterGroupLocalServiceUtil
					.getByCode(RegisteredCoursePortletKeys.PARAMETER_URL_GROUP_CODE, false);
			Parameter parameterApiCorporateValidate = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterApiGroup.getParameterGroupId(), RegisteredCoursePortletKeys.PARAMETER_ESHOP_API_CODE,
					false);

			String encodedKey = RegisterCourseUtil.getSecretKey();

			Object tmsAttendanceResponse = null;

			List<TraineeDto> traineeList = new ArrayList<>();

			// log.info("Inccorect");

			JSONObject jsonAttendanceRequest = JSONFactoryUtil.createJSONObject();
			if (!Validator.isNull(companyCode) && !Validator.isNull(batchId)) {
				jsonAttendanceRequest.put("company_code", companyCode);
				jsonAttendanceRequest.put("course_code", selectedCourse.getCourseCode());
				jsonAttendanceRequest.put("batch_id", selectedCourse.getBatchId());
			} else {
				jsonAttendanceRequest.put("company_code", "0");
				jsonAttendanceRequest.put("uen_number", "0");
				jsonAttendanceRequest.put("batch_id", "0");
			}

			String encryptedJsonAttendanceRequest = AESEncryptUtil.encrypt(jsonAttendanceRequest.toString(),
					encodedKey);

			tmsAttendanceResponse = HttpApiUtil.request(
					parameterApiCorporateValidate.getParamValue() + CORPORATE_ATTENDANCE_URL, Http.Method.GET.name(),
					encryptedJsonAttendanceRequest, parameterClientId.getParamValue(),
					parameterClientSecret.getParamValue(), encodedKey);

			JSONArray tmsArr = null;
			JSONObject tmsObj = null;

			if (!Validator.isNull(tmsAttendanceResponse)) {

				if (tmsAttendanceResponse.toString().startsWith("[")) {
					tmsArr = JSONFactoryUtil.createJSONArray(tmsAttendanceResponse.toString());

				} else {
					tmsObj = JSONFactoryUtil.createJSONObject(tmsAttendanceResponse.toString());
					tmsArr = JSONFactoryUtil.createJSONArray(tmsObj.getString("corptrainee"));
				}
				for (Object object : tmsArr) {
					JSONObject traineeObj = JSONFactoryUtil.createJSONObject(object.toString());

					TraineeDto trainee = new TraineeDto();

					trainee.setBatchId(batchId);
					trainee.setTraineeName(traineeObj.getString("trainee_name"));
					trainee.setTraineeEmail(traineeObj.getString("trainee_email"));

					List<AttendanceDto> attendances = new ArrayList<AttendanceDto>();

					JSONArray attendanceArr = JSONFactoryUtil.createJSONArray(traineeObj.getString("attendance"));
					int sessionNo = 1;

					for (Object Obj : attendanceArr) {

						JSONObject attendanceObj = JSONFactoryUtil.createJSONObject(Obj.toString());
						AttendanceDto dto = new AttendanceDto();
						dto.setBatchId(selectedCourse.getBatchId());
						dto.setCourseCode(selectedCourse.getCourseCode());
						dto.setCourseTitle(selectedCourse.getCourseTitle());
						dto.setCourseStartDate(selectedCourse.getCourseStartDate());
						dto.setCourseEndDate(selectedCourse.getCourseEndDate());
						dto.setSession(sessionNo);
						dto.setAttendanceHour(attendanceObj.getString("attendance_hr"));
						dto.setAttendancePercentage(attendanceObj.getString("attendance_percentage"));
						dto.setIsAttended(attendanceObj.getString("isattended"));
						dto.setMakeBatch(attendanceObj.getString("makeup_batch"));
						dto.setAttendanceDateTime(attendanceObj.getString("attendance_date_time"));
						sessionNo++;

						attendances.add(dto);
					}
					trainee.setAttendanceList(attendances);

					traineeList.add(trainee);
				}
			}

			renderRequest.setAttribute("traineeList", traineeList);
			renderRequest.setAttribute("titlePage", "Attendance");
			renderRequest.setAttribute("uenNumber", uenNumber);
			renderRequest.setAttribute("companyCode", companyCode);
			renderRequest.setAttribute("companyName", companyName);

			renderRequest.setAttribute("batchId", batchId);
			renderRequest.setAttribute("subBookingId", selectedCourse.getSubBookingId());
			renderRequest.setAttribute("courseCode", selectedCourse.getCourseCode());
			renderRequest.setAttribute("courseTitle", selectedCourse.getCourseTitle());
			renderRequest.setAttribute("startDate", startDate);
			renderRequest.setAttribute("endDate", endDate);
			renderRequest.setAttribute("authToken", authToken);

		} catch (Exception e) {
			log.error("Failed when render data from " + e.getMessage());
		}
		log.info("View Corporate Attendance Render - Stop");
		return corpAttendancePage;
	}
}
