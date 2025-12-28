package web.ntuc.eshop.registeredcourse.portlet;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.RoleServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.DateUtil;
import api.ntuc.common.util.RoleUtil;
import web.ntuc.eshop.registeredcourse.constants.RegisteredCoursePortletKeys;

/**
 * @author muhamadpangestu
 */
@Component(immediate = true, property = { "com.liferay.portlet.display-category=category.eshop",
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=RegisteredCourse", "javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + RegisteredCoursePortletKeys.REGISTERED_COURSE_PORTLET,
		"javax.portlet.resource-bundle=content.Language", "javax.portlet.version=3.0",
		"com.liferay.portlet.render-weight=0",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class RegisteredCoursePortlet extends MVCPortlet {

	private Log log = LogFactoryUtil.getLog(RegisteredCoursePortlet.class);

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		log.info("Registered Course Portlet - Start");
		try {
			String authToken = CSRFValidationUtil.authToken(renderRequest);
			String nric = "";
			String companyName = "";
			String companyCode = "";
			String uenNumber = "";
			int accType = 0;
			String birthDate = "";

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			User user = themeDisplay.getUser();

			CommerceAccount account = null;

			List<Role> roleList = new ArrayList<>();
			for (Long roleId : user.getRoleIds()) {
				Role newRole = RoleServiceUtil.getRole(roleId);
				roleList.add(newRole);
			}

			account = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());

			if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Individual_Role")) {
				accType = 1;
				nric = (String) account.getExpandoBridge().getAttribute("NRIC");
				birthDate = DateUtil.toString(user.getBirthday(), format);
			} else if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Corporate_Role")) {
				accType = 2;
				uenNumber = (String) account.getExpandoBridge().getAttribute("UEN Number");
				companyCode = (String) account.getExpandoBridge().getAttribute("Company Code");
				companyName = (String) account.getExpandoBridge().getAttribute("Company Name");
			}

			renderRequest.setAttribute("titlePage", "Courses");
			renderRequest.setAttribute("accType", accType);
			renderRequest.setAttribute("nric", nric);
			renderRequest.setAttribute("birthDate", birthDate);
			renderRequest.setAttribute("uenNumber", uenNumber);
			renderRequest.setAttribute("companyCode", companyCode);
			renderRequest.setAttribute("companyName", companyName);
			renderRequest.setAttribute("authToken", authToken);
		} catch (Exception e) {
			log.error("Failed when render My Account, error:" + e.getMessage());
		}
		log.info("Registered Course Portlet - End");
		super.render(renderRequest, renderResponse);
	}

	public static String capitalizeString(String text) {
		return Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}
}