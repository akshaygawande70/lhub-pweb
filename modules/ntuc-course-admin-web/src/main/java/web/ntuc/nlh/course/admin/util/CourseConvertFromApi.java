package web.ntuc.nlh.course.admin.util;

import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Http;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import api.ntuc.common.util.ComparationUtil;
import api.ntuc.common.util.DateUtil;
import api.ntuc.common.util.HttpApiUtil;
import svc.ntuc.nlh.course.admin.exception.NoSuchCourseException;
import svc.ntuc.nlh.course.admin.model.Course;
import svc.ntuc.nlh.course.admin.service.CourseLocalServiceUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.nlh.course.admin.constants.CourseAdminWebPortletKeys;
import web.ntuc.nlh.course.admin.dto.CourseKeyDto;

public class CourseConvertFromApi {

	public CourseConvertFromApi() {
		// Do nothing
	}

	private static Log log = LogFactoryUtil.getLog(CourseConvertFromApi.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public void converter() {
		log.info("=========convert start=========");
		long startTime = System.currentTimeMillis();
		try {
			ParameterGroup parameterAuthGroup = ParameterGroupLocalServiceUtil
					.getByCode(CourseAdminWebPortletKeys.PARAMETER_AUTH_GROUP_CODE, false);
			long siteGroupId = parameterAuthGroup.getGroupId();
			Parameter parameterClientId = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_CLIENT_ID_CODE,
					false);
			Parameter parameterClientSecret = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_CLIENT_SECRET_CODE,
					false);

//			GET API URL FROM PARAMETER
			ParameterGroup parameterApiGroup = ParameterGroupLocalServiceUtil
					.getByCode(CourseAdminWebPortletKeys.PARAMETER_URL_GROUP_CODE, false);
			Parameter parameterApiGetCourse = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterApiGroup.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_GET_COURSE_CODE,
					false);
			Parameter parameterApiGetPopularCourse = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterApiGroup.getParameterGroupId(),
					CourseAdminWebPortletKeys.PARAMETER_GET_POPULAR_COURSE_CODE, false);

//			GET RESPONSE FROM API
			Object courseResponse = HttpApiUtil.request(parameterApiGetCourse.getParamValue(), Http.Method.GET.name(),
					"", parameterClientId.getParamValue(), parameterClientSecret.getParamValue(),"");
			Object popularCourseResponse = HttpApiUtil.request(parameterApiGetPopularCourse.getParamValue(),
					Http.Method.GET.name(), "", parameterClientId.getParamValue(),
					parameterClientSecret.getParamValue(),"");
			JSONArray courseJsonArray = JSONFactoryUtil.createJSONArray(courseResponse.toString());
			JSONArray popularCourseJsonArray = JSONFactoryUtil.createJSONArray(popularCourseResponse.toString());
			log.info("cron scheduler TMS courseJsonArray = "+courseJsonArray );
			log.info("cron scheduler TMS popularCourseJsonArray = "+popularCourseJsonArray );
			Set<String> popularCourses = new HashSet<>();

			for (Object obj : popularCourseJsonArray) {
				JSONObject json = JSONFactoryUtil.createJSONObject(obj.toString());
				popularCourses.add(json.getString("coursecode"));
			}

			Set<String> setCourseCodesFromJson = new HashSet<>();
			Set<CourseKeyDto> courseFromJsons = new HashSet<>();

			for (Object object : courseJsonArray) {
				JSONObject obj = JSONFactoryUtil.createJSONObject(object.toString());
				setCourseCodesFromJson.add(obj.getString("course_code"));
				courseFromJsons.add(new CourseKeyDto(obj.getString("course_code"), obj.getString("batchid")));
				insertApiIntoDb(siteGroupId, obj, popularCourses);
			}

			List<Course> coursesFromDb = CourseLocalServiceUtil.getAllActiveCourse(siteGroupId);
			Set<CourseKeyDto> courseFromDbs = new HashSet<>();
			for (Course c : coursesFromDb) {
				courseFromDbs.add(new CourseKeyDto(c.getCourseCode(), c.getBatchId()));
			}

			courseFromDbs.removeAll(courseFromJsons);

			List<Course> deletedCourses = new ArrayList<>();
			for (CourseKeyDto c : courseFromDbs) {
				deletedCourses
						.add(CourseLocalServiceUtil.getCourseByCourseCodeAndBatchId(siteGroupId, c.getCourseCode(), c.getBatchId()));
			}
			for (Course c : deletedCourses) {
				CourseLocalServiceUtil.updateCourse(c.getCourseId(), c.getEndDate(), c.getVenue(),
						c.getAllowOnlinePayment(), c.getCourseTitle(), c.getAllowWebRegistration(), c.getDescription(),
						c.getAvailability(), c.getBatchId(), c.getWebExpiry(), c.getFundedCourseFlag(),
						c.getCourseCode(), c.getCourseDuration(), c.getStartDate(), c.getCourseFee(), c.getCourseType(),
						true, false, false);
			}

			DynamicQuery dynamicQuery = CourseLocalServiceUtil.dynamicQuery();
			Conjunction conjunctionQuery = RestrictionsFactoryUtil.conjunction();
			conjunctionQuery.add(RestrictionsFactoryUtil.eq("deleted", false));
			conjunctionQuery.add(RestrictionsFactoryUtil.le("startDate", new Date()));
			dynamicQuery.add(conjunctionQuery);
			List<Course> expiredCourses = CourseLocalServiceUtil.dynamicQuery(dynamicQuery);
			for (Course c : expiredCourses) {
				if (!c.getDeleted()) {
//					log.info("deleted course code = "+c.getCourseCode() +" batch id = "+c.getBatchId()+" start date = "+c.getStartDate());
//					
//					List<Course> updateAnotherCourses = CourseLocalServiceUtil.getCourseByCourseCode(siteGroupId, c.getCourseCode(), false);
//					for(Course uc : updateAnotherCourses) {
//						log.info("another course code = "+uc.getCourseCode() +" batch id = "+uc.getBatchId()+" start date = "+uc.getStartDate());
//					}
										
					CourseLocalServiceUtil.updateCourse(c.getCourseId(), c.getEndDate(), c.getVenue(),
							c.getAllowOnlinePayment(), c.getCourseTitle(), c.getAllowWebRegistration(),
							c.getDescription(), c.getAvailability(), c.getBatchId(), c.getWebExpiry(),
							c.getFundedCourseFlag(), c.getCourseCode(), c.getCourseDuration(), c.getStartDate(),
							c.getCourseFee(), c.getCourseType(), true, false, false);
					
				}
			}

		} catch (PortalException e) {
			e.printStackTrace();
		}
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		String elapsedTimeString = String.format("%02d hours, %02d min, %02d sec", 
				TimeUnit.MILLISECONDS.toHours(elapsedTime),
				TimeUnit.MILLISECONDS.toMinutes(elapsedTime) -
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedTime)),
			    TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime))
			);
		log.info("Elapsed Time = "+elapsedTimeString);
		log.info("=========convert end=========");

	}

	private void insertApiIntoDb(long groupId, JSONObject jsonCourse, Set<String> popularCourses) {
		try {
//			DateUtil dateUtil = new DateUtil();
			Date endDate = DateUtil.parse(jsonCourse.getString("end_date"), this.sdf);
			String venue = jsonCourse.getString("venue");
			boolean allowOnlinePayment = jsonCourse.getString("allow_online_payment").equalsIgnoreCase("yes");
			String courseTitle = jsonCourse.getString("course_title");
			boolean allowWebRegistration = jsonCourse.getString("allow_web_registration").equalsIgnoreCase("yes");
			String description = jsonCourse.getString("description");
			int availability = jsonCourse.getInt("availability");
			String batchId = jsonCourse.getString("batchid");
			Date webExpiry = DateUtil.parse(jsonCourse.getString("web_expiry"), this.sdf);
			boolean fundedCourseFlag = jsonCourse.getString("funded_course_flag").equalsIgnoreCase("yes");
			String courseCode = jsonCourse.getString("course_code");
			double courseDuration = jsonCourse.getDouble("course_duration");
			Date startDate = DateUtil.parse(jsonCourse.getString("start_date"), this.sdf);
			double courseFee = jsonCourse.getDouble("course_fee", 0);
			String courseType = jsonCourse.getString("course_type");
			boolean updated = false;
			boolean popular = false;
			boolean deleted = false;

			if (popularCourses.contains(courseCode)) {
				popular = true;
			}
			this.insertMappedApiData(groupId, courseCode, batchId, endDate, venue, allowOnlinePayment, courseTitle,
					allowWebRegistration, description, availability, webExpiry, fundedCourseFlag, courseDuration,
					startDate, courseFee, courseType, popular, deleted, updated);

		} catch (PortalException e) {
			e.printStackTrace();
		}
	}

	private void insertMappedApiData(long groupId, String courseCode, String batchId, Date endDate, String venue,
			boolean allowOnlinePayment, String courseTitle, boolean allowWebRegistration, String description,
			int availability, Date webExpiry, boolean fundedCourseFlag, double courseDuration, Date startDate,
			double courseFee, String courseType, boolean popular, boolean deleted, boolean updated)
			throws PortalException {
		try {
			Course existingCourse = CourseLocalServiceUtil.getCourseByCourseCodeAndBatchId(groupId, courseCode, batchId);
			if (!compareToExistingCourse(existingCourse, endDate, venue, allowOnlinePayment, courseTitle,
					allowWebRegistration, description, availability, batchId, webExpiry, fundedCourseFlag, courseCode,
					courseDuration, startDate, courseFee, courseType, popular, deleted)) {
				updated = true;
			}
			CourseLocalServiceUtil.updateCourse(existingCourse.getCourseId(), endDate, venue, allowOnlinePayment,
					courseTitle, allowWebRegistration, description, availability, batchId, webExpiry, fundedCourseFlag,
					courseCode, courseDuration, startDate, courseFee, courseType, false, updated, popular);
			log.info("updated course id = " + existingCourse.getCourseId()+" course code = "+existingCourse.getCourseCode());
		} catch (NoSuchCourseException e) {
			CourseLocalServiceUtil.addCourse(groupId, endDate, venue, allowOnlinePayment, courseTitle,
					allowWebRegistration, description, availability, batchId, webExpiry, fundedCourseFlag, courseCode,
					courseDuration, startDate, courseFee, courseType, false, true, popular);
			log.info("add course ");
		}
	}

	private boolean compareToExistingCourse(Course existingCourse, Date endDate, String venue,
			boolean allowOnlinePayment, String courseTitle, boolean allowWebRegistration, String description,
			int availability, String batchId, Date webExpiry, boolean fundedCourseFlag, String courseCode,
			double courseDuration, Date startDate, double courseFee, String courseType, boolean popular,
			boolean deleted) {
		boolean isSame = false;
		String parsedStartDate = DateUtil.toString(existingCourse.getStartDate(), this.sdf);
		String parsedEndDate = DateUtil.toString(existingCourse.getEndDate(), this.sdf);
		String pStartDate = DateUtil.toString(startDate, this.sdf);
		String pEndDate = DateUtil.toString(endDate, this.sdf);
		isSame = ComparationUtil.compare(parsedEndDate, pEndDate)
				&& ComparationUtil.compare(existingCourse.getVenue(), venue)
				&& existingCourse.getAllowOnlinePayment() == allowOnlinePayment
				&& ComparationUtil.compare(existingCourse.getCourseTitle(), courseTitle)
				&& existingCourse.getAllowWebRegistration() == allowWebRegistration
				&& ComparationUtil.compare(existingCourse.getDescription(), description)
				&& existingCourse.getAvailability() == availability
				&& ComparationUtil.compare(existingCourse.getBatchId(), batchId)
				&& ComparationUtil.compare(existingCourse.getWebExpiry(), webExpiry)
				&& existingCourse.getFundedCourseFlag() == fundedCourseFlag
				&& ComparationUtil.compare(existingCourse.getCourseCode(), courseCode)
				&& existingCourse.getCourseDuration() == courseDuration
				&& ComparationUtil.compare(parsedStartDate, pStartDate) && existingCourse.getCourseFee() == courseFee
				&& ComparationUtil.compare(existingCourse.getCourseType(), courseType)
				&& existingCourse.getPopular() == popular && existingCourse.getDeleted() == deleted;
//		log.info("is same = " + isSame);
		return isSame;
	}
}
