package web.ntuc.eshop.registeredcourse.resource;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.registeredcourse.constants.MVCCommandNames;
import web.ntuc.eshop.registeredcourse.constants.RegisteredCoursePortletKeys;
import web.ntuc.eshop.registeredcourse.dto.ExamResultDto;
import web.ntuc.eshop.registeredcourse.dto.RegisteredCourseDto;
import web.ntuc.eshop.registeredcourse.util.RegisterCourseUtil;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.COURSE_EXAM_RESULT_DETAIL_RESOURCE,
		"javax.portlet.name="
				+ RegisteredCoursePortletKeys.REGISTERED_COURSE_PORTLET }, service = MVCResourceCommand.class)
public class ExamResultDetailResource implements MVCResourceCommand {

	private Log log = LogFactoryUtil.getLog(ExamResultDetailResource.class);

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Exam Result Data Resource - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

			User user = themeDisplay.getUser();
			
			String examName = ParamUtil.getString(resourceRequest, "examName");

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

			String authToken = CSRFValidationUtil.authToken(resourceRequest);

			JSONArray parameterJsonArray = JSONFactoryUtil.createJSONArray();

			PortletSession session = resourceRequest.getPortletSession();
			RegisteredCourseDto registeredCourse = (RegisteredCourseDto) session
					.getAttribute("registeredCourse_" + user.getUuid(), PortletSession.PORTLET_SCOPE);

			ExamResultDto examResultDto = (ExamResultDto) session.getAttribute("examResult_" + examName,
					PortletSession.PORTLET_SCOPE);

			if (!Validator.isNull(examResultDto)) {
				JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();

				jsonBranch.put("exam_name", examResultDto.getExamName());
				jsonBranch.put("exam_date", examResultDto.getExamDate());
				jsonBranch.put("exam_result", examResultDto.getExamResult());

				parameterJsonArray.put(jsonBranch);

				int allCount = parameterJsonArray.length();
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
		log.info("Exam Result Data Resource - End");
		return false;
	}

}
