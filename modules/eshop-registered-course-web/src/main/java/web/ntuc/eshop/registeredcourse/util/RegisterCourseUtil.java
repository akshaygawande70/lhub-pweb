package web.ntuc.eshop.registeredcourse.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.registeredcourse.constants.RegisteredCoursePortletKeys;
import web.ntuc.eshop.registeredcourse.dto.RegisteredCourseDto;
import web.ntuc.eshop.registeredcourse.dto.TraineeDto;

public class RegisterCourseUtil {

	private Log log = LogFactoryUtil.getLog(RegisterCourseUtil.class);

	public static JSONObject getTableData(int allCount, int countAfterFilter, String sEcho, JSONArray paramArray) {
		JSONObject tableData = JSONFactoryUtil.createJSONObject();
		tableData.put("iTotalRecords", allCount);
		tableData.put("iTotalDisplayRecords", countAfterFilter);
		tableData.put("sEcho", Integer.parseInt(sEcho));
		tableData.put("aaData", paramArray);

		return tableData;
	}

	public static String getSecretKey() {
		ParameterGroup parameterAuthGroup = ParameterGroupLocalServiceUtil
				.getByCode(RegisteredCoursePortletKeys.PARAMETER_AUTH_GROUP_CODE, false);
		long siteGroupId = parameterAuthGroup.getGroupId();
		Parameter parameterSecretKey = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
				parameterAuthGroup.getParameterGroupId(), RegisteredCoursePortletKeys.PARAMETER_ESHOP_TMS_SECRET_KEY,
				false);
		return parameterSecretKey.getParamValue();
	}

	public static RegisteredCourseDto getSelectedCourse(int accType, String batchId, Object tmsResponse, Log log) {
		log.info("Get Selected Course - Start");
		RegisteredCourseDto selectedCourse = null;
		try {
			JSONArray objArr = JSONFactoryUtil.createJSONArray(tmsResponse.toString());
			for (Object object : objArr) {
				JSONObject responseObj = JSONFactoryUtil.createJSONObject(object.toString());
				if (responseObj.getString("batch_id").equals(batchId)) {
					RegisteredCourseDto dto = new RegisteredCourseDto();

					dto.setCourseLevel(responseObj.getString("course_level"));
					dto.setBatchId(responseObj.getString("batch_id"));
					dto.setCourseTitle(responseObj.getString("course_title"));
					dto.setNricNumber(responseObj.getString("nric_number"));
					dto.setSessionHrs(responseObj.getString("session_hrs"));
					String tempStartStr = responseObj.getString("course_start_date");
					Date tempStartDate = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(tempStartStr);
					dto.setCourseStartDate(tempStartDate);
					dto.setCourseType(responseObj.getString("course_type"));
					dto.setCourseCode(responseObj.getString("course_code"));
					dto.setFunded(responseObj.getString("funded"));
					dto.setCourseExamFlag(responseObj.getString("course_exam_flag"));
					String tempEndStr = responseObj.getString("course_end_date");
					Date tempEndDate = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(tempEndStr);
					dto.setCourseEndDate(tempEndDate);
					dto.setPatternCode(responseObj.getString("pattern_code"));
					dto.setCourseCategory(responseObj.getString("course_category"));

					if (accType == 2) {
						JSONArray traineeArr = JSONFactoryUtil.createJSONArray(responseObj.getString("trainee_data"));

						List<TraineeDto> traineeList = new ArrayList<TraineeDto>();

						for (Object traineeObj : traineeArr) {
							JSONObject obj = JSONFactoryUtil.createJSONObject(traineeObj.toString());
							TraineeDto traineeDto = new TraineeDto();
							traineeDto.setBatchId(dto.getBatchId());
							traineeDto.setTraineeName(obj.getString("trainee_name"));
							traineeDto.setTraineeEmail(obj.getString("trainee_email"));

							traineeList.add(traineeDto);
						}

						dto.setSubBookingId(responseObj.getString("subbooking_id"));
						dto.setTraineeList(traineeList);
					}

					selectedCourse = dto;
				}
			}
		} catch (Exception e) {
			log.error("Found Error When Mapping Data at : " + e.getMessage());
		}
		log.info("Get Selected Course - End");
		return selectedCourse;
	}
}
