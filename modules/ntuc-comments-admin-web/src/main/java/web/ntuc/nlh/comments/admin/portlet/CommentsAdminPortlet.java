package web.ntuc.nlh.comments.admin.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.constants.PortletDisplayCategoryConstant;
import api.ntuc.common.util.CSRFValidationUtil;
import web.ntuc.nlh.comments.admin.constants.CommentsAdminPortletKeys;

/**
 * @author muhamadpangestu
 */
@Component(immediate = true, property = {
		"com.liferay.portlet.display-category=" + PortletDisplayCategoryConstant.ADMIN,
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Comments Management", "javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + CommentsAdminPortletKeys.COMMENTSADMIN_PORTLET,
		"javax.portlet.resource-bundle=content.Language", "javax.portlet.version=3.0",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class CommentsAdminPortlet extends MVCPortlet {

	private static Log log = LogFactoryUtil.getLog(CommentsAdminPortlet.class);

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		log.info("Course admin portlet render - start");
		try {
			String authToken = CSRFValidationUtil.authToken(renderRequest);

			renderRequest.setAttribute("authToken", authToken);
		} catch (Exception e) {
			log.error("Failed when render Course admin, error:" + e.getMessage());
		}

		log.info("Course admin portlet render - start");
		super.render(renderRequest, renderResponse);
	}
}