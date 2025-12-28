package web.ntuc.eshop.registeredcourse.resource;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.AESEncryptUtil;
import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.HttpApiUtil;
import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.registeredcourse.constants.MVCCommandNames;
import web.ntuc.eshop.registeredcourse.constants.RegisteredCoursePortletKeys;
import web.ntuc.eshop.registeredcourse.dto.AttendanceDto;
import web.ntuc.eshop.registeredcourse.dto.RegisteredCourseDto;
import web.ntuc.eshop.registeredcourse.util.RegisterCourseUtil;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.COURSE_ATTENDANCES_RESOURCE,
		"javax.portlet.name="
				+ RegisteredCoursePortletKeys.REGISTERED_COURSE_PORTLET }, service = MVCResourceCommand.class)
public class AttendancesResource implements MVCResourceCommand {

	private Log log = LogFactoryUtil.getLog(AttendancesResource.class);

	private static final String INDIVIDUAL_ATTENDANCE_URL = "/individual/attendance";

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Attendance Data Resource - Start");
		try {
			String authToken = CSRFValidationUtil.authToken(resourceRequest);
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long groupId = themeDisplay.getScopeGroupId();
			String portletName = (String) resourceRequest.getAttribute(WebKeys.PORTLET_ID);

			User user = themeDisplay.getUser();

			String nric = ParamUtil.getString(resourceRequest, "nric");
			String birthDate = ParamUtil.getString(resourceRequest, "birthDate");
			String batchId = ParamUtil.getString(resourceRequest, "batchId");
//			String percentageHtml = "";

			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

			HttpServletRequest httpRequest = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
			Integer iDisplayStart = ParamUtil.getInteger(httpRequest, "iDisplayStart");
			Integer iDisplayLength = ParamUtil.getInteger(httpRequest, "iDisplayLength");
			if (iDisplayLength == 0) {
				iDisplayLength = 10;
			}

			String sEcho = ParamUtil.getString(httpRequest, "sEcho", "0");

			int start = iDisplayStart;
			int end = start + iDisplayLength;

//			double totalHours = 0;
//			double percentageAttendance = 0;
//			int tempPercentage = 0;

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

			JSONArray parameterJsonArray = JSONFactoryUtil.createJSONArray();

			Object tmsAttendanceResponse = null;

			PortletSession session = resourceRequest.getPortletSession();
			Object courseListSession = (Object) session.getAttribute("courseListResponse_" + user.getUuid(),
					PortletSession.PORTLET_SCOPE);
			
			RegisteredCourseDto registeredCourse = RegisterCourseUtil.getSelectedCourse(1, batchId, courseListSession,
					log);
			

//			@SuppressWarnings("unchecked")
//			List<AttendanceDto> attendanceDtoList = (List<AttendanceDto>) session
//					.getAttribute("attendanceList_" + batchId, PortletSession.PORTLET_SCOPE);

			/*if (!Validator.isNull(attendanceDtoList)) {

				JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();
				for (AttendanceDto attendanceDto : attendanceDtoList) {
					String strHour = attendanceDto.getAttendanceHour();
					int strPercentage = Integer.parseInt(attendanceDto.getAttendancePercentage());

					LocalTime time = LocalTime.parse(strHour, DateTimeFormatter.ofPattern("HHmm"));
					int hour = time.get(ChronoField.CLOCK_HOUR_OF_DAY);

//					totalHours = totalHours + hour;
//					tempPercentage = tempPercentage + strPercentage;
//					percentageAttendance = tempPercentage / attendanceDtoList.size();

					percentageHtml = "<div class='circle_percent' data-percent='" + percentageAttendance
							+ "'><div class='circle_inner'><div class='round_per'></div></div></div>";
				}
				jsonBranch.put("course_title", registeredCourse.getCourseTitle());
				jsonBranch.put("batch_id", registeredCourse.getBatchId());
				jsonBranch.put("attendance_hr", totalHours);
				jsonBranch.put("attendance_percentage", percentageHtml);

				PortletURL viewUrl = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(resourceRequest),
						portletName, themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
				viewUrl.getRenderParameters().setValue("mvcRenderCommandName",
						MVCCommandNames.VIEW_ATTENDANCES_DETAIL_RENDER);
				viewUrl.getRenderParameters().setValue("batchId", registeredCourse.getBatchId());
				viewUrl.getRenderParameters().setValue("authToken", authToken);
				jsonBranch.put("viewUrl", viewUrl);

				parameterJsonArray.put(jsonBranch);

				int allCount = attendanceDtoList.size();
				resourceResponse.getWriter()
						.println(RegisterCourseUtil.getTableData(allCount, allCount, sEcho, parameterJsonArray));

			} else {*/

				List<AttendanceDto> attendanceList = new ArrayList<AttendanceDto>();

				JSONObject jsonAttendanceRequest = JSONFactoryUtil.createJSONObject();
				if (!Validator.isNull(nric) && !Validator.isNull(birthDate)) {
					jsonAttendanceRequest.put("nric_number", nric);
					jsonAttendanceRequest.put("dob", birthDate);
					jsonAttendanceRequest.put("batch_id", registeredCourse.getBatchId());
					jsonAttendanceRequest.put("course_code", registeredCourse.getCourseCode());
				} else {
					jsonAttendanceRequest.put("company_code", "0");
					jsonAttendanceRequest.put("nric_number", "0");
					jsonAttendanceRequest.put("dob", "1960-01-01");
				}

				String encryptedJsonAttendanceRequest = AESEncryptUtil.encrypt(jsonAttendanceRequest.toString(),
						encodedKey);

				tmsAttendanceResponse = HttpApiUtil.request(
						parameterApiCorporateValidate.getParamValue() + INDIVIDUAL_ATTENDANCE_URL,
						Http.Method.GET.name(), encryptedJsonAttendanceRequest, parameterClientId.getParamValue(),
						parameterClientSecret.getParamValue(), encodedKey);
				
				JSONObject attendTmsObj = JSONFactoryUtil.createJSONObject(tmsAttendanceResponse.toString());

				JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();

				JSONArray attendArr = null;

				if (!Validator.isNull(tmsAttendanceResponse)) {
					attendArr = JSONFactoryUtil.createJSONArray(attendTmsObj.getString("attendance"));
					int sessionNo = 1;

					for (Object object : attendArr) {
						JSONObject tempObj = JSONFactoryUtil.createJSONObject(object.toString());

						String strHour = tempObj.getString("attendance_hr");
						String strPercentage =tempObj.getString("attendance_percentage");

//						LocalTime time = LocalTime.parse(strHour, DateTimeFormatter.ofPattern("HHmm"));
//						int hour = time.get(ChronoField.CLOCK_HOUR_OF_DAY);

//						totalHours = totalHours + hour;
//						tempPercentage = tempPercentage + strPercentage;
//						percentageAttendance = tempPercentage / attendArr.length();

						

						AttendanceDto attendance = new AttendanceDto();

						attendance.setBatchId(registeredCourse.getBatchId());
						attendance.setCourseCode(registeredCourse.getCourseCode());
						attendance.setCourseTitle(registeredCourse.getCourseTitle());
						attendance.setCourseStartDate(registeredCourse.getCourseStartDate());
						attendance.setCourseEndDate(registeredCourse.getCourseEndDate());
						attendance.setSession(sessionNo);
						attendance.setAttendanceHour(strHour);
						attendance.setAttendancePercentage(strPercentage);
						attendance.setIsAttended(tempObj.getString("isattended"));
						attendance.setMakeBatch(tempObj.getString("makeup_batch"));
						attendance.setAttendanceDateTime(tempObj.getString("attendance_date_time"));

						attendanceList.add(attendance);
						sessionNo++;
					}
					String totalAttendedPer = attendTmsObj.getString("total_attended_per");
					String totalAttendedHr = attendTmsObj.getString("total_attended_hr");
//					percentageHtml = "<div class='circle_percent' data-percent='" + totalAttendedPer
//							+ "'><div class='circle_inner'><div class='round_per'></div></div></div>";

					jsonBranch.put("course_title", registeredCourse.getCourseTitle());
					jsonBranch.put("batch_id", registeredCourse.getBatchId());
					jsonBranch.put("attendance_hr", totalAttendedHr);
					jsonBranch.put("attendance_percentage", totalAttendedPer);

					PortletURL viewUrl = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(resourceRequest),
							portletName, themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
					viewUrl.getRenderParameters().setValue("mvcRenderCommandName",
							MVCCommandNames.VIEW_ATTENDANCES_DETAIL_RENDER);
					viewUrl.getRenderParameters().setValue("batchId", registeredCourse.getBatchId());
					viewUrl.getRenderParameters().setValue("authToken", authToken);
					jsonBranch.put("viewUrl", viewUrl);

					parameterJsonArray.put(jsonBranch);

					session.setAttribute("attendanceList_" + registeredCourse.getBatchId(), attendanceList,
							PortletSession.PORTLET_SCOPE);

					int allCount = 1;
					resourceResponse.getWriter()
							.println(RegisterCourseUtil.getTableData(allCount, allCount, sEcho, parameterJsonArray));

				} else {

					JSONArray emptyArr = JSONFactoryUtil.createJSONArray();
					resourceResponse.getWriter()
							.println(RegisterCourseUtil.getTableData(0, 10, "1", emptyArr).toString());
				}

//			}
		} catch (

		Exception e) {
			log.error("Found error at " + e.getMessage());
		}
		log.info("Attendance Data Resource - End");
		return false;
	}

}
