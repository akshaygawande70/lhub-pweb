package web.ntuc.eshop.registeredcourse.render;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.RoleServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.DateUtil;
import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.registeredcourse.constants.RegisteredCoursePortletKeys;
import web.ntuc.eshop.registeredcourse.dto.RegisteredCourseDto;
import web.ntuc.eshop.registeredcourse.util.RegisterCourseUtil;

@Component(immediate = true, property = {
		"mvc.command.name=" + web.ntuc.eshop.registeredcourse.constants.MVCCommandNames.VIEW_EXAM_RESULT_RENDER,
		"javax.portlet.name="
				+ RegisteredCoursePortletKeys.REGISTERED_COURSE_PORTLET }, service = MVCRenderCommand.class)
public class ViewExamResultRender implements MVCRenderCommand {

	private static Log log = LogFactoryUtil.getLog(ViewExamResultRender.class);
	private static String examResultPage = "/exam-result/view-exam-result.jsp";

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		log.info("View Exam Result Render - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String authToken = CSRFValidationUtil.authToken(renderRequest);
			PortletCommandUtil.renderCommand(renderRequest);
			String nric = "";
			Date tempDate = null;
			String companyName = "";
			String companyCode = "";
			String uenNumber = "";
			String birthDate = "";

			User user = themeDisplay.getUser();
			long userId = user.getUserId();

			CommerceAccount account = null;

			List<Role> roleList = new ArrayList<>();
			for (Long roleId : user.getRoleIds()) {
				Role newRole = RoleServiceUtil.getRole(roleId);
				roleList.add(newRole);
			}

			account = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());

			nric = (String) account.getExpandoBridge().getAttribute("NRIC");
			birthDate = DateUtil.toString(user.getBirthday(), format);

			String batchId = ParamUtil.getString(renderRequest, "batchId");

			PortletSession session = renderRequest.getPortletSession();
			Object courseListSession = (Object) session.getAttribute("courseListResponse_" + user.getUuid(),
					PortletSession.PORTLET_SCOPE);
//			RegisteredCourseDto selectedCourse = (RegisteredCourseDto) session
//					.getAttribute("registeredCourse_" + user.getUuid(), PortletSession.PORTLET_SCOPE);

//			if (Validator.isNull(selectedCourse)) {
				RegisteredCourseDto selectedCourse = RegisterCourseUtil.getSelectedCourse(1, batchId,
						courseListSession, log);
//				selectedCourse = selectedCourseTemp;

				session.setAttribute("registeredCourse_" + user.getUuid(), selectedCourse,
						PortletSession.PORTLET_SCOPE);
//			}

			renderRequest.setAttribute("titlePage", "Exam Results");
			renderRequest.setAttribute("accType", 1);
			renderRequest.setAttribute("nric", nric);
			renderRequest.setAttribute("birthDate", birthDate);
			renderRequest.setAttribute("uenNumber", uenNumber);
			renderRequest.setAttribute("companyCode", companyCode);
			renderRequest.setAttribute("companyName", companyName);

			renderRequest.setAttribute("batchId", batchId);
			renderRequest.setAttribute("authToken", authToken);

		} catch (Exception e) {
			log.error("Failed when render data from " + e.getMessage());
		}
		log.info("View Exam Result Render - Stop");
		return examResultPage;
	}

}
