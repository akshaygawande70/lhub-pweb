package web.ntuc.eshop.myaccount.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.CSRFValidationUtil;
import web.ntuc.eshop.myaccount.constants.MyAccountPortletKeys;

@Component(immediate = true, property = { "com.liferay.portlet.display-category=category.eshop",
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Change Password", "javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/change_password/view.jsp",
		"javax.portlet.name=" + MyAccountPortletKeys.CHANGE_PASSWORD_PORTLET,
		"javax.portlet.resource-bundle=content.Language", "javax.portlet.version=3.0",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class ChangePasswordPortlet extends MVCPortlet {

	private static Log log = LogFactoryUtil.getLog(ChangePasswordPortlet.class);
	List<Long> categoryId = new ArrayList<>();

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException, NullPointerException {
		log.info("Change Password Portlet render - start");
		try {
			String authToken = CSRFValidationUtil.authToken(renderRequest);
			renderRequest.setAttribute("authToken", authToken);

		} catch (Exception e) {
			log.error("Failed when render Change Password Portlet, error:" + e.getMessage());
		}
		log.info("Change Password Portlet render - end");
		super.render(renderRequest, renderResponse);
	}

	public static String capitalizeString(String text) {
		return Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}

}
