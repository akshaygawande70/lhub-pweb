package web.ntuc.eshop.registeredcourse.resource;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import api.ntuc.common.util.DateUtil;
import api.ntuc.common.util.HttpApiUtil;
import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.registeredcourse.constants.MVCCommandNames;
import web.ntuc.eshop.registeredcourse.constants.RegisteredCoursePortletKeys;
import web.ntuc.eshop.registeredcourse.util.RegisterCourseUtil;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.REGISTERED_COURSE_RESOURCE,
		"javax.portlet.name="
				+ RegisteredCoursePortletKeys.REGISTERED_COURSE_PORTLET }, service = MVCResourceCommand.class)
public class RegisteredCourseResource implements MVCResourceCommand {

	private Log log = LogFactoryUtil.getLog(RegisteredCourseResource.class);
//	private static final String SECRET_KEY = "1FB4BC02FB22816F8FE874E58ED331AE";
	private static final String INDIVIDUAL_URL = "/individual/courses";
	private static final String CORPORATE_URL = "/corporate/courses";
//	private RegisterCourseUtil courseUtil = new RegisterCourseUtil();

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		// log.info("Registered Course Data Resource - Start");
		try {
			String authToken = CSRFValidationUtil.authToken(resourceRequest);
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			String portletName = (String) resourceRequest.getAttribute(WebKeys.PORTLET_ID);
			
//			Get Address from User 
			User user = themeDisplay.getUser();

			int accType = ParamUtil.getInteger(resourceRequest, "accType");
			String nric = ParamUtil.getString(resourceRequest, "nric");
			String birthDate = ParamUtil.getString(resourceRequest, "birthDate");
			String companyCode = ParamUtil.getString(resourceRequest, "companyCode");
			String uenNumber = ParamUtil.getString(resourceRequest, "uenNumber");

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

			ParameterGroup parameterAuthGroup = ParameterGroupLocalServiceUtil
					.getByCode(RegisteredCoursePortletKeys.PARAMETER_AUTH_GROUP_CODE, false);
			long siteGroupId = parameterAuthGroup.getGroupId();
			Parameter parameterClientId = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(),
					RegisteredCoursePortletKeys.PARAMETER_ESHOP_CLIENT_ID_CODE, false);
			Parameter parameterClientSecret = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(),
					RegisteredCoursePortletKeys.PARAMETER_ESHOP_CLIENT_SECRET_CODE, false);

//			GET API URL FROM PARAMETER
			ParameterGroup parameterApiGroup = ParameterGroupLocalServiceUtil
					.getByCode(RegisteredCoursePortletKeys.PARAMETER_URL_GROUP_CODE, false);
			Parameter parameterApiCorporateValidate = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterApiGroup.getParameterGroupId(), RegisteredCoursePortletKeys.PARAMETER_ESHOP_API_CODE,
					false);

			String encodedKey = RegisterCourseUtil.getSecretKey();

			JSONArray parameterJsonArray = JSONFactoryUtil.createJSONArray();	

			PortletSession session = resourceRequest.getPortletSession();

			Object tmsResponseSession = (Object) session.getAttribute("courseListResponse_" + user.getUuid(),
					PortletSession.PORTLET_SCOPE);

			if (!Validator.isNull(tmsResponseSession)) {
				// log.info("From Session");

				if (!Validator.isNull(tmsResponseSession) && tmsResponseSession.toString().contains("[")) {
					JSONArray courseObj = JSONFactoryUtil.createJSONArray(tmsResponseSession.toString());
					
					if (courseObj.length() < end) {
						end = courseObj.length();
					}
					
					for (int i = start; i < end; i++) {
						JSONObject obj = JSONFactoryUtil.createJSONObject(courseObj.getString(i));
						mappedParam(accType, obj, themeDisplay, resourceRequest, portletName, authToken,
								parameterJsonArray);
					}

					int allCount = courseObj.length();

					resourceResponse.getWriter()
							.println(RegisterCourseUtil.getTableData(allCount, allCount, sEcho, parameterJsonArray).toString());

				} else if (!Validator.isNull(tmsResponseSession) && !tmsResponseSession.toString().contains("[")) {
					JSONObject obj = JSONFactoryUtil.createJSONObject(tmsResponseSession.toString());
					String batchId = obj.getString("batch_id");
					if(Validator.isNull(batchId)) {
						JSONArray emptyArr = JSONFactoryUtil.createJSONArray();
						resourceResponse.getWriter().println(RegisterCourseUtil.getTableData(0, 10, "1", emptyArr).toString());
					} else {
						mappedParam(accType, obj, themeDisplay, resourceRequest, portletName, authToken, parameterJsonArray);

						int allCount = parameterJsonArray.length();

						resourceResponse.getWriter()
								.println(RegisterCourseUtil.getTableData(allCount, allCount, sEcho, parameterJsonArray).toString());
					}
				} else {
					JSONArray emptyArr = JSONFactoryUtil.createJSONArray();
					resourceResponse.getWriter().println(RegisterCourseUtil.getTableData(0, 10, "1", emptyArr).toString());
				}
			} else {
				// log.info("From TMS");
				// log.info("acctype = "+accType);
				JSONObject jsonRequest = JSONFactoryUtil.createJSONObject();
				if (accType == 2) {
					jsonRequest.put("company_code", companyCode);
					jsonRequest.put("uen_no", uenNumber);
				} else if (accType == 1) {
					jsonRequest.put("nric_number", nric);
					jsonRequest.put("dob", birthDate);
				} else {
					jsonRequest.put("company_code", "0");
					jsonRequest.put("uen_number", "0");
					jsonRequest.put("nric_number", "0");
					jsonRequest.put("dob", "1960-01-01");
				}

				String encryptedJsonRequest = AESEncryptUtil.encrypt(jsonRequest.toString(), encodedKey);

				// log.info("===== JSON REQUEST =====");
				// log.info(encryptedJsonRequest);
				// log.info("===== JSON REQUEST =====");

				Object tmsResponse = null;
				if (accType == 1) {
					tmsResponse = HttpApiUtil.request(parameterApiCorporateValidate.getParamValue() + INDIVIDUAL_URL,
							Http.Method.GET.name(), encryptedJsonRequest, parameterClientId.getParamValue(),
							parameterClientSecret.getParamValue(),encodedKey);
				} else {
					tmsResponse = HttpApiUtil.request(parameterApiCorporateValidate.getParamValue() + CORPORATE_URL,
							Http.Method.GET.name(), encryptedJsonRequest, parameterClientId.getParamValue(),
							parameterClientSecret.getParamValue(),encodedKey);
				}

//				 log.info("===== JSON RESPONSE =====");
//				 log.info(tmsResponse);
//				 log.info("===== JSON RESPONSE =====");

				session.setAttribute("courseListResponse_" + user.getUuid(), tmsResponse, PortletSession.PORTLET_SCOPE);

				if (!Validator.isNull(tmsResponse) && tmsResponse.toString().contains("[")) {
					JSONArray courseObj = JSONFactoryUtil.createJSONArray(tmsResponse.toString());
					
					if (courseObj.length() < end) {
						end = courseObj.length();
					}
					
					for (int i = start; i < end; i++) {
						// log.info(courseObj.get(i).toString());
						JSONObject obj = JSONFactoryUtil.createJSONObject(courseObj.get(i).toString());
						mappedParam(accType, obj, themeDisplay, resourceRequest, portletName, authToken,
								parameterJsonArray);
					}

					int allCount = courseObj.length();

					resourceResponse.getWriter()
							.println(RegisterCourseUtil.getTableData(allCount, allCount, sEcho, parameterJsonArray).toString());

				} else if (!Validator.isNull(tmsResponse) && !tmsResponse.toString().contains("[")) {
					JSONObject obj = JSONFactoryUtil.createJSONObject(tmsResponse.toString());
					String batchId = obj.getString("batch_id");
					if(Validator.isNull(batchId)) {
						JSONArray emptyArr = JSONFactoryUtil.createJSONArray();
						resourceResponse.getWriter().println(RegisterCourseUtil.getTableData(0, 10, "1", emptyArr).toString());
					} else {
						mappedParam(accType, obj, themeDisplay, resourceRequest, portletName, authToken, parameterJsonArray);

						int allCount = parameterJsonArray.length();

						resourceResponse.getWriter()
								.println(RegisterCourseUtil.getTableData(allCount, allCount, sEcho, parameterJsonArray).toString());
					}

				} else {
					JSONArray emptyArr = JSONFactoryUtil.createJSONArray();
					resourceResponse.getWriter().println(RegisterCourseUtil.getTableData(0, 10, "1", emptyArr).toString());
				}
			}
		} catch (Exception e) {
			log.error("Found error at " + e.getMessage());
			return true;
		}
		// log.info("Registered Course Data Resource - End");
		return false;
	}

	public void mappedParam(int accType, JSONObject obj, ThemeDisplay themeDisplay, ResourceRequest resourceRequest,
			String portletName, String authToken, JSONArray paramJsonArray)
			throws JSONException, ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
		JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();

		String tempStartStr = obj.getString("course_start_date");
		// log.info(tempStartStr);
		Date tempStartDate = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(tempStartStr);

		jsonBranch.put("course_title", obj.getString("course_title"));
		jsonBranch.put("course_code", obj.getString("course_code"));
		jsonBranch.put("batch_id", obj.getString("batch_id"));

		if (accType == 1) {
//			// log.info("Personal URL");
			String startDate = DateUtil.toString(tempStartDate, format);
			jsonBranch.put("course_start_date", startDate);
			PortletURL attendanceUrl = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(resourceRequest),
					portletName, themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
			attendanceUrl.getRenderParameters().setValue("mvcRenderCommandName",
					MVCCommandNames.VIEW_ATTENDANCES_RENDER);
			attendanceUrl.getRenderParameters().setValue("batchId", obj.getString("batch_id"));
			attendanceUrl.getRenderParameters().setValue("authToken", authToken);
			jsonBranch.put("attendanceUrl", attendanceUrl);
			// log.info("attendanceUrl = "+attendanceUrl);
			PortletURL examUrl = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(resourceRequest),
					portletName, themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
			examUrl.getRenderParameters().setValue("mvcRenderCommandName", MVCCommandNames.VIEW_EXAM_RESULT_RENDER);
			examUrl.getRenderParameters().setValue("batchId", obj.getString("batch_id"));
			examUrl.getRenderParameters().setValue("authToken", authToken);
			jsonBranch.put("examUrl", examUrl);
			// log.info("examUrl = "+examUrl);
		} else {
//			// log.info("Corporate URL");
			jsonBranch.put("subbooking_id", obj.getString("subbooking_id"));

			PortletURL attendanceUrl = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(resourceRequest),
					portletName, themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
			attendanceUrl.getRenderParameters().setValue("mvcRenderCommandName",
					MVCCommandNames.VIEW_CORPORATE_ATTENDANCES_RENDER);
			attendanceUrl.getRenderParameters().setValue("batchId", obj.getString("batch_id"));
			attendanceUrl.getRenderParameters().setValue("subBookingId", obj.getString("subbooking_id"));
			attendanceUrl.getRenderParameters().setValue("authToken", authToken);
			jsonBranch.put("attendanceUrl", attendanceUrl);
			// log.info("attendanceUrl = "+attendanceUrl);
			PortletURL examUrl = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(resourceRequest),
					portletName, themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
			examUrl.getRenderParameters().setValue("mvcRenderCommandName",
					MVCCommandNames.VIEW_CORPORATE_EXAM_RESULT_RENDER);
			examUrl.getRenderParameters().setValue("batchId", obj.getString("batch_id"));
			examUrl.getRenderParameters().setValue("subBookingId", obj.getString("subbooking_id"));
			examUrl.getRenderParameters().setValue("authToken", authToken);
			jsonBranch.put("examUrl", examUrl);
			// log.info("examUrl = "+examUrl);
			PortletURL evaluationUrl = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(resourceRequest),
					portletName, themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
			evaluationUrl.getRenderParameters().setValue("mvcRenderCommandName",
					MVCCommandNames.VIEW_EVALUATION_RENDER);
			evaluationUrl.getRenderParameters().setValue("batchId", obj.getString("batch_id"));
			evaluationUrl.getRenderParameters().setValue("subBookingId", obj.getString("subbooking_id"));
			evaluationUrl.getRenderParameters().setValue("authToken", authToken);
			jsonBranch.put("evaluationUrl", evaluationUrl);
		}

//		courseList.add(dto);

		paramJsonArray.put(jsonBranch);
	}

}