package web.ntuc.nlh.course.admin.resource;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;
import java.util.Locale;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.course.admin.exception.CourseValidationException;
import svc.ntuc.nlh.course.admin.model.Course;
import svc.ntuc.nlh.course.admin.service.CourseLocalServiceUtil;
import web.ntuc.nlh.course.admin.constants.CourseAdminMessagesKey;
import web.ntuc.nlh.course.admin.constants.CourseAdminWebPortletKeys;
import web.ntuc.nlh.course.admin.constants.MVCCommandNames;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.COURSE_DATA_RESOURCES,
		"javax.portlet.name=" + CourseAdminWebPortletKeys.COURSE_ADMIN_PORTLET }, service = MVCResourceCommand.class)
public class CourseAdminDataResource implements MVCResourceCommand {

	private static Log log = LogFactoryUtil.getLog(CourseAdminDataResource.class);

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("course admin data resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			Locale locale = themeDisplay.getLocale();

			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

			HttpServletRequest httpRequest = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
			Integer iDisplayStart = ParamUtil.getInteger(httpRequest, "iDisplayStart");
			Integer iDisplayLength = ParamUtil.getInteger(httpRequest, "iDisplayLength");
			if (iDisplayLength == 0) {
				iDisplayLength = 10;
			}
			Integer iSortColumnIndex = ParamUtil.getInteger(httpRequest, "iSortCol_0");
			String sSortDirection = ParamUtil.getString(httpRequest, "sSortDir_0");
			String sEcho = ParamUtil.getString(httpRequest, "sEcho", "0");

			String search = ParamUtil.getString(httpRequest, "sSearch");

			boolean ascending = sSortDirection.equals("asc");
			String orderByColumn = this.getOrderByColumn(iSortColumnIndex);

			OrderByComparator<Course> comparator = OrderByComparatorFactoryUtil.create("ntuc_course", orderByColumn,
					ascending);
			int start = iDisplayStart;
			int end = start + iDisplayLength;

			List<Course> courses = CourseLocalServiceUtil.getCoursesByKeywords(themeDisplay.getScopeGroupId(), search,
					start, end, comparator);
			int allCount = (int) CourseLocalServiceUtil.getCoursesCountByKeywords(themeDisplay.getScopeGroupId(),
					search);
			int countAfterFilter = CourseLocalServiceUtil.getCoursesByKeywords(themeDisplay.getScopeGroupId(), search,
					QueryUtil.ALL_POS, QueryUtil.ALL_POS, comparator).size();
			JSONObject tableData = JSONFactoryUtil.createJSONObject();
			tableData.put("iTotalRecords", allCount);
			tableData.put("iTotalDisplayRecords", countAfterFilter);
			tableData.put("sEcho", Integer.parseInt(sEcho));
			tableData.put("aaData", this.mappingCouseToTable(courses));
			resourceResponse.getWriter().println(tableData.toString());

		} catch (Exception e) {
			log.error("Error while searching course data : " + e.getMessage());
			return true;
		}
		log.info("course admin data resources - end");
		return false;
	}

	private JSONArray mappingCouseToTable(List<Course> courses) {
		JSONArray courseJsonArray = JSONFactoryUtil.createJSONArray();
		for (Course course : courses) {
			JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();
			jsonBranch.put("courseId", course.getCourseId());
			jsonBranch.put("venue", course.getVenue());
			jsonBranch.put("allowOnlinePayment", course.getAllowOnlinePayment());
			jsonBranch.put("courseTitle", course.getCourseTitle());
			jsonBranch.put("allowWebRegistration", course.getAllowWebRegistration());
			jsonBranch.put("description", course.getDescription());
			jsonBranch.put("availability", course.getAvailability());
			jsonBranch.put("batchId", course.getBatchId());
			jsonBranch.put("webExpiry", Validator.isNull(course.getWebExpiry()) ? "-" : course.getWebExpiry());
			jsonBranch.put("fundedCourseFlag", course.getFundedCourseFlag());
			jsonBranch.put("courseCode", course.getCourseCode());
			jsonBranch.put("courseDuration", course.getCourseDuration());
			jsonBranch.put("startDate", Validator.isNull(course.getStartDate()) ? "-" : course.getStartDate());
			jsonBranch.put("courseFee", course.getCourseFee());
			jsonBranch.put("courseType", course.getCourseType());
			jsonBranch.put("endDate", Validator.isNull(course.getEndDate()) ? "-" : course.getEndDate());
			jsonBranch.put("createDate", Validator.isNull(course.getCreateDate()) ? "-" : course.getCreateDate());
			jsonBranch.put("modifiedDate", Validator.isNull(course.getModifiedDate()) ? "-" : course.getModifiedDate());
			jsonBranch.put("popular", course.getPopular());
			courseJsonArray.put(jsonBranch);
		}
		return courseJsonArray;
	}

	private String getOrderByColumn(Integer iSortColumnIndex) {
		String orderBy = "";
		if (iSortColumnIndex == 0) {
			orderBy = "courseId";
		} else if (iSortColumnIndex == 1) {
			orderBy = "venue";
		} else if (iSortColumnIndex == 2) {
			orderBy = "allowOnlinePayment";
		} else if (iSortColumnIndex == 3) {
			orderBy = "courseTitle";
		} else if (iSortColumnIndex == 4) {
			orderBy = "allowWebRegistration";
		} else if (iSortColumnIndex == 5) {
			orderBy = "description";
		} else if (iSortColumnIndex == 6) {
			orderBy = "courseFee";
		} else if (iSortColumnIndex == 7) {
			orderBy = "courseType";
		} else if (iSortColumnIndex == 8) {
			orderBy = "availability";
		} else if (iSortColumnIndex == 9) {
			orderBy = "batchId";
		} else if (iSortColumnIndex == 10) {
			orderBy = "webExpiry";
		} else if (iSortColumnIndex == 11) {
			orderBy = "fundedCourseFlag";
		} else {
			this.getOrderByColumnExtend(iSortColumnIndex);
		}
		return orderBy;
	}

	private String getOrderByColumnExtend(Integer iSortColumnIndex) {
		String orderBy = "";
		if (iSortColumnIndex == 12) {
			orderBy = "courseCode";
		} else if (iSortColumnIndex == 13) {
			orderBy = "courseDuration";
		} else if (iSortColumnIndex == 14) {
			orderBy = "popular";
		} else if (iSortColumnIndex == 15) {
			orderBy = "startDate";
		} else if (iSortColumnIndex == 16) {
			orderBy = "endDate";
		} else if (iSortColumnIndex == 17) {
			orderBy = "createDate";
		} else if (iSortColumnIndex == 18) {
			orderBy = "modifiedDate";
		}
		return orderBy;
	}
}
