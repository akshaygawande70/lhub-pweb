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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import web.ntuc.eshop.registeredcourse.dto.ExamResultDto;
import web.ntuc.eshop.registeredcourse.dto.RegisteredCourseDto;
import web.ntuc.eshop.registeredcourse.util.RegisterCourseUtil;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.COURSE_EXAM_RESULT_RESOURCE,
		"javax.portlet.name="
				+ RegisteredCoursePortletKeys.REGISTERED_COURSE_PORTLET }, service = MVCResourceCommand.class)
public class ExamResultResource implements MVCResourceCommand {

	private Log log = LogFactoryUtil.getLog(ExamResultResource.class);

	private static final String SOURCE_URL = "/individual/examresults";

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Exam Result Data Resource - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long groupId = themeDisplay.getScopeGroupId();
			String portletName = (String) resourceRequest.getAttribute(WebKeys.PORTLET_ID);

			User user = themeDisplay.getUser();

			String nric = ParamUtil.getString(resourceRequest, "nric");
			String birthDate = ParamUtil.getString(resourceRequest, "birthDate");
			String batchId = ParamUtil.getString(resourceRequest, "batchId");

			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

			HttpServletRequest httpRequest = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
			Integer iDisplayStart = ParamUtil.getInteger(httpRequest, "iDisplayStart");
			Integer iDisplayLength = ParamUtil.getInteger(httpRequest, "iDisplayLength");
			if (iDisplayLength == 0) {
				iDisplayLength = 10;
			}

			String sEcho = ParamUtil.getString(httpRequest, "sEcho");

			int start = iDisplayStart;
			int end = start + iDisplayLength;

			String authToken = CSRFValidationUtil.authToken(resourceRequest);

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

			PortletSession session = resourceRequest.getPortletSession();
			RegisteredCourseDto registeredCourse = (RegisteredCourseDto) session
					.getAttribute("registeredCourse_" + user.getUuid(), PortletSession.PORTLET_SCOPE);

//			@SuppressWarnings("unchecked")
//			List<ExamResultDto> examResultDtoList = (List<ExamResultDto>) session
//					.getAttribute("examResultList_" + batchId, PortletSession.PORTLET_SCOPE);

			/*if (!Validator.isNull(examResultDtoList)) {

				JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();
				for (ExamResultDto examResultDto : examResultDtoList) {

					jsonBranch.put("exam_name", examResultDto.getExamName());
					jsonBranch.put("exam_date", examResultDto.getExamDate());
					jsonBranch.put("exam_result", examResultDto.getExamResult());

					PortletURL viewUrl = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(resourceRequest),
							portletName, themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
					viewUrl.getRenderParameters().setValue("mvcRenderCommandName",
							MVCCommandNames.VIEW_EXAM_RESULT_DETAIL_RENDER);
					viewUrl.getRenderParameters().setValue("batchId", examResultDto.getBatchId());
					viewUrl.getRenderParameters().setValue("authToken", authToken);
					jsonBranch.put("viewUrl", viewUrl);

					parameterJsonArray.put(jsonBranch);

				}

				int allCount = examResultDtoList.size();
				resourceResponse.getWriter()
						.println(RegisterCourseUtil.getTableData(allCount, allCount, sEcho, parameterJsonArray));
			} else {*/

				List<ExamResultDto> examResultList = new ArrayList<>();

				JSONObject jsonRequest = JSONFactoryUtil.createJSONObject();
				if (!Validator.isNull(nric) && !Validator.isNull(birthDate)) {

					jsonRequest.put("nric_number", nric);
					jsonRequest.put("dob", birthDate);
					jsonRequest.put("course_code", registeredCourse.getCourseCode());
					jsonRequest.put("batch_id", registeredCourse.getBatchId());
				} else {
					jsonRequest.put("company_code", "0");
					jsonRequest.put("uen_number", "0");
					jsonRequest.put("nric_number", "0");
					jsonRequest.put("dob", "1960-01-01");
				}

				String encryptedJsonRequest = AESEncryptUtil.encrypt(jsonRequest.toString(), encodedKey);

				Object tmsResponse = HttpApiUtil.request(parameterApiCorporateValidate.getParamValue() + SOURCE_URL,
						Http.Method.GET.name(), encryptedJsonRequest, parameterClientId.getParamValue(),
						parameterClientSecret.getParamValue(), encodedKey);
				
//				 log.info("===== JSON RESPONSE =====");
//				 log.info(tmsResponse);
//				 log.info("===== JSON RESPONSE =====");
				
				JSONObject courseObj = JSONFactoryUtil.createJSONObject(tmsResponse.toString());

				String tempExam = courseObj.getString("exam");

				JSONArray examArr = JSONFactoryUtil.createJSONArray(tempExam);
				if (!Validator.isNull(tmsResponse) && !Validator.isNull(examArr)) {
					for (Object object : examArr) {
						JSONObject obj = JSONFactoryUtil.createJSONObject(object.toString());
						JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();

						ExamResultDto dto = new ExamResultDto();

						dto.setCourseTitle(registeredCourse.getCourseTitle());
						dto.setCourseCode(registeredCourse.getCourseCode());
						dto.setBatchId(registeredCourse.getBatchId());
						dto.setCourseStartDate(registeredCourse.getCourseStartDate());
						dto.setCourseEndDate(registeredCourse.getCourseEndDate());
						dto.setExamName(obj.getString("exam_name"));

						String tempDateStr = obj.getString("exam_date");
						Date tempDate = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(tempDateStr);
						dto.setExamDate(tempDate);

						dto.setExamResult(obj.getString("exam_result"));

						jsonBranch.put("exam_name", obj.getString("exam_name"));
						jsonBranch.put("exam_date", obj.getString("exam_date"));
						jsonBranch.put("exam_result", obj.getString("exam_result"));

						PortletURL viewUrl = PortletURLFactoryUtil.create(
								PortalUtil.getHttpServletRequest(resourceRequest), portletName,
								themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
						viewUrl.getRenderParameters().setValue("mvcRenderCommandName",
								MVCCommandNames.VIEW_EXAM_RESULT_DETAIL_RENDER);
						viewUrl.getRenderParameters().setValue("batchId", registeredCourse.getBatchId());
						viewUrl.getRenderParameters().setValue("examName", obj.getString("exam_name"));
						viewUrl.getRenderParameters().setValue("authToken", authToken);
						jsonBranch.put("viewUrl", viewUrl);
						
//						log.info(dto);
//						session.setAttribute("examResult_" + dto.getExamName(), dto,PortletSession.PORTLET_SCOPE);
						
						examResultList.add(dto);
						parameterJsonArray.put(jsonBranch);

					}
//					log.info("batch id = "+registeredCourse.getBatchId());
					session.setAttribute("examResultList_" + registeredCourse.getBatchId(), examResultList,
							PortletSession.PORTLET_SCOPE);
//					List<ExamResultDto> examResultDtoList = (List<ExamResultDto>) session
//							.getAttribute("examResultList_" + batchId, PortletSession.PORTLET_SCOPE);
//					log.info(examResultDtoList);
					int allCount = examResultList.size();
					resourceResponse.getWriter()
							.println(RegisterCourseUtil.getTableData(allCount, allCount, sEcho, parameterJsonArray));
				} else {

					JSONArray emptyArr = JSONFactoryUtil.createJSONArray();
					resourceResponse.getWriter()
							.println(RegisterCourseUtil.getTableData(0, 10, "1", emptyArr).toString());
				}
//			}
		} catch (Exception e) {
			log.error("Found error at " + e.getMessage());
		}
		log.info("Exam Result Data Resource - End");
		return false;
	}

}
