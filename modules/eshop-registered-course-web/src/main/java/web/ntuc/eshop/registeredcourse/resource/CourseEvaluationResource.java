package web.ntuc.eshop.registeredcourse.resource;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
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
import web.ntuc.eshop.registeredcourse.dto.EvaluationDto;
import web.ntuc.eshop.registeredcourse.dto.RegisteredCourseDto;
import web.ntuc.eshop.registeredcourse.util.RegisterCourseUtil;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.COURSE_EVALUATION_RESOURCE,
		"javax.portlet.name="
				+ RegisteredCoursePortletKeys.REGISTERED_COURSE_PORTLET }, service = MVCResourceCommand.class)
public class CourseEvaluationResource implements MVCResourceCommand {

	private Log log = LogFactoryUtil.getLog(CourseEvaluationResource.class);

	private static final String COURSE_EVALUATION_URL = "/corporate/courseevaluation";

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		try {
			String authToken = CSRFValidationUtil.authToken(resourceRequest);
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

			User user = themeDisplay.getUser();
			String companyCode = ParamUtil.getString(resourceRequest, "companyCode");
			String batchId = ParamUtil.getString(resourceRequest, "batchId");
			String courseCode = ParamUtil.getString(resourceRequest, "courseCode");

			double customerConsent = 0;
			double facilities = 0;
			double overallProgramme = 0;
			double programmeMaterial = 0;
			double trainerEffectiveness = 0;
			double trainingProgramme = 0;
			double average = 0;

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

			ParameterGroup parameterApiGroup = ParameterGroupLocalServiceUtil
					.getByCode(RegisteredCoursePortletKeys.PARAMETER_URL_GROUP_CODE, false);
			Parameter parameterApiCorporateValidate = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterApiGroup.getParameterGroupId(), RegisteredCoursePortletKeys.PARAMETER_ESHOP_API_CODE,
					false);

			String encodedKey = RegisterCourseUtil.getSecretKey();

			JSONArray parameterJsonArray = JSONFactoryUtil.createJSONArray();

			Object tmsResponse = null;
			PortletSession session = resourceRequest.getPortletSession();
			Object courseListSession = (Object) session.getAttribute("courseListResponse_" + user.getUuid(),
					PortletSession.PORTLET_SCOPE);
			RegisteredCourseDto selectedCourse = RegisterCourseUtil.getSelectedCourse(2, batchId, courseListSession,
					log);

			JSONObject jsonRequest = JSONFactoryUtil.createJSONObject();

			jsonRequest.put("company_code", companyCode);
			jsonRequest.put("course_code", courseCode);
			jsonRequest.put("batch_id", batchId);

			String encryptedJsonRequest = AESEncryptUtil.encrypt(jsonRequest.toString(), encodedKey);

			tmsResponse = HttpApiUtil.request(parameterApiCorporateValidate.getParamValue() + COURSE_EVALUATION_URL,
					Http.Method.GET.name(), encryptedJsonRequest, parameterClientId.getParamValue(),
					parameterClientSecret.getParamValue(), encodedKey);
			
			 log.info("===== JSON RESPONSE =====");
			 log.info(tmsResponse);
			 log.info("===== JSON RESPONSE =====");
			
			JSONObject attendObj = JSONFactoryUtil.createJSONObject(tmsResponse.toString());

			JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();

			JSONArray attendArr = JSONFactoryUtil.createJSONArray(attendObj.getString("evaluation"));
			String status = attendObj.getString("statuscode");
			if (!Validator.isNull(tmsResponse) && status.equals("200")) {
				List<EvaluationDto> evaluationListDto = new ArrayList<>();

				for (Object object : attendArr) {
					JSONObject tempObj = JSONFactoryUtil.createJSONObject(object.toString());

					EvaluationDto dto = new EvaluationDto();

					dto.setBatchId(selectedCourse.getBatchId());
					dto.setSectionSum(tempObj.getString("sections_sum"));
					dto.setAvg(tempObj.getDouble("avg"));

					evaluationListDto.add(dto);

					if (dto.getSectionSum().equals("Customer's Consent")) {
						customerConsent = dto.getAvg();
					} else if (dto.getSectionSum().equals("Facilities")) {
						facilities = dto.getAvg();
					} else if (dto.getSectionSum().equals("Overall Programme")) {
						overallProgramme = dto.getAvg();
					} else if (dto.getSectionSum().equals("Programme Materials")) {
						programmeMaterial = dto.getAvg();
					} else if (dto.getSectionSum().equals("Trainer's Effectiveness")) {
						trainerEffectiveness = dto.getAvg();
					} else if (dto.getSectionSum().equals("Training Programme")) {
						trainingProgramme = dto.getAvg();
					}

				}

				double sum = customerConsent + facilities + overallProgramme + programmeMaterial + trainerEffectiveness
						+ trainingProgramme;
				average = sum / evaluationListDto.size();

				DecimalFormat df = new DecimalFormat("###.###");

				jsonBranch.put("customer_consent", customerConsent);
				jsonBranch.put("facilities", facilities);
				jsonBranch.put("overall_programme", overallProgramme);
				jsonBranch.put("programme_material", programmeMaterial);
				jsonBranch.put("trainer_effectiveness", trainerEffectiveness);
				jsonBranch.put("training_programme", trainingProgramme);
				jsonBranch.put("average", df.format(average));

				parameterJsonArray.put(jsonBranch);

				int allCount = 1;
				int countAfterFilter = 10;
				resourceResponse.getWriter().println(
						RegisterCourseUtil.getTableData(allCount, countAfterFilter, sEcho, parameterJsonArray));
			} else {

				JSONArray emptyArr = JSONFactoryUtil.createJSONArray();
				resourceResponse.getWriter().println(RegisterCourseUtil.getTableData(0, 10, "1", emptyArr).toString());
			}

		} catch (Exception e) {
			log.error("Found error at " + e.getMessage());
		}
		return false;
	}

}
