package web.ntuc.nlh.course.admin.resource;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.legacy.searcher.SearchRequestBuilderFactory;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.ExportUtil;
import web.ntuc.nlh.course.admin.constants.CourseAdminWebPortletKeys;
import web.ntuc.nlh.course.admin.constants.MVCCommandNames;
import web.ntuc.nlh.course.admin.dto.ResultDto;
import web.ntuc.nlh.course.admin.dto.SearchResultDto;
import web.ntuc.nlh.course.admin.util.CourseSearch;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.EXPORT_RESOURCE,
		"javax.portlet.name=" + CourseAdminWebPortletKeys.COURSE_LIST_PORTLET }, service = MVCResourceCommand.class)
public class ExportResource implements MVCResourceCommand {
	private static Log log = LogFactoryUtil.getLog(ExportResource.class);
	
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Export course resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			ResultDto result = CourseSearch.searchCourse(resourceRequest, resourceResponse, themeDisplay, 0, 0, "", Boolean.FALSE, _searchRequestBuilderFactory);
			List<SearchResultDto> courses = result.getCourses();
			List<JSONObject> jsonObjects = new ArrayList<>();
			for(SearchResultDto s : courses) {
				JSONObject json = JSONFactoryUtil.createJSONObject();
				json.put("courseCode", s.getCourseCode());
				json.put("courseTitle", s.getTitle());
				json.put("courseStatus", s.getStatus());
				json.put("courseFee", s.getPrice());
				json.put("fundedCourseFlag", s.getFunded());
				json.put("popular", s.getPopular());
				
				jsonObjects.add(json);
			}
			
			String[] headers = {"Course Code","Title","Status","Price","Funded","Popular"};
			String[] dataKeys = {"courseCode","courseTitle","courseStatus","courseFee","fundedCourseFlag","popular"};
			String sbExport = ExportUtil.exportDataToStringBuilderCsv(headers, jsonObjects, dataKeys, "^");
			resourceResponse.setContentType("text/csv");
			resourceResponse.setProperty("content-disposition", "attachment; filename="+CourseAdminWebPortletKeys.CSV_FILE_NAME);
			OutputStream out = resourceResponse.getPortletOutputStream();
			out.write(sbExport.getBytes());
			out.flush();
			out.close();
			
		}catch (Exception e) {
			log.error("Error while exporting data : " + e.getMessage());
			return true;
		}
		log.info("Export course resources - end");
		return false;
	}
	
	@Reference
	SearchRequestBuilderFactory _searchRequestBuilderFactory;
	
}
