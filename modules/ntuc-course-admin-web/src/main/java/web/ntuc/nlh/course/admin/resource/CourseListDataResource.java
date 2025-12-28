package web.ntuc.nlh.course.admin.resource;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.legacy.searcher.SearchRequestBuilderFactory;

import java.util.List;
import java.util.Locale;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.course.admin.model.Course;
import web.ntuc.nlh.course.admin.constants.CourseAdminWebPortletKeys;
import web.ntuc.nlh.course.admin.constants.MVCCommandNames;
import web.ntuc.nlh.course.admin.dto.ResultDto;
import web.ntuc.nlh.course.admin.dto.SearchResultDto;
import web.ntuc.nlh.course.admin.util.CourseSearch;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.COURSE_LIST_RESOURCES,
		"javax.portlet.name=" + CourseAdminWebPortletKeys.COURSE_LIST_PORTLET }, service = MVCResourceCommand.class)
public class CourseListDataResource implements MVCResourceCommand {
	private static Log log = LogFactoryUtil.getLog(CourseListDataResource.class);
	

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("course list data resources - start");
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
			
			log.info("start = "+start);
			log.info("end = "+end);
			ResultDto result = CourseSearch.searchCourse(resourceRequest, resourceResponse, themeDisplay, start, end, search, Boolean.TRUE, _searchRequestBuilderFactory);			
			List<SearchResultDto> courses = result.getCourses();
			int allCount = result.getCount();

			JSONObject tableData = JSONFactoryUtil.createJSONObject();
			tableData.put("iTotalRecords", allCount);
			tableData.put("iTotalDisplayRecords", allCount);
			tableData.put("sEcho", Integer.parseInt(sEcho));
			tableData.put("aaData", this.mappingCouseToTable(courses));
			resourceResponse.getWriter().println(tableData.toString());
			
//			resourceResponse.setContentType("application/json");
//			PrintWriter out = null;
//			out = resourceResponse.getWriter();
//
//			JSONObject rowData = JSONFactoryUtil.createJSONObject();
//			rowData.put("courses", courses);
//			rowData.put("status", HttpServletResponse.SC_OK);
//			out.print(rowData.toString());
//			out.flush();

//			resourceResponse.getWriter().println(listCourse.toString());

		} catch (Exception e) {
			log.error("Error while loading course list data : " + e.getMessage());
			return true;
		}
		log.info("course list data resources - start");
		return false;
	}
	
	private JSONArray mappingCouseToTable(List<SearchResultDto> courses) {
		JSONArray courseJsonArray = JSONFactoryUtil.createJSONArray();
		for (SearchResultDto course : courses) {
			JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();
			jsonBranch.put("courseCode", course.getCourseCode());
			jsonBranch.put("courseTitle", course.getTitle());
			jsonBranch.put("courseStatus", course.getStatus());
			jsonBranch.put("courseFee", course.getPrice());
			jsonBranch.put("fundedCourseFlag", course.getFunded());
			jsonBranch.put("popular", course.getPopular());
			courseJsonArray.put(jsonBranch);
		}
		return courseJsonArray;
	}
	
	private String getOrderByColumn(Integer iSortColumnIndex) {
		String orderBy = "";
		if (iSortColumnIndex == 0) {
			orderBy = "courseCode";
		} else if (iSortColumnIndex == 1) {
			orderBy = "courseTitle";
		} else if (iSortColumnIndex == 2) {
			orderBy = "courseStatus";
		} else if (iSortColumnIndex == 3) {
			orderBy = "courseFee";
		} else if (iSortColumnIndex == 4) {
			orderBy = "fundedCourseFlag";
		} else if (iSortColumnIndex == 5) {
			orderBy = "popular";
		}
		return orderBy;
	}
	
	@Reference
	SearchRequestBuilderFactory _searchRequestBuilderFactory;
}
