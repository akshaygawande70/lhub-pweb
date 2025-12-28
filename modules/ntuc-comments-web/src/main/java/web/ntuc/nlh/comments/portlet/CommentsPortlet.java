package web.ntuc.nlh.comments.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.CSRFValidationUtil;
import web.ntuc.nlh.comments.constants.CommentsPortletKeys;

/**
 * @author muhamadpangestu
 */
@Component(immediate = true, property = { "com.liferay.portlet.display-category=ntuc-module",
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Comments", "javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + CommentsPortletKeys.COMMENTS_PORTLET, "javax.portlet.resource-bundle=content.Language",
		"javax.portlet.version=3.0", "javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class CommentsPortlet extends MVCPortlet {

	Log log = LogFactoryUtil.getLog(CommentsPortlet.class);

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		log.info("Comments portlet render - start");
		try {
			
			String authToken = CSRFValidationUtil.authToken(renderRequest);

			renderRequest.setAttribute("authToken", authToken);

		} catch (Exception e) {
			log.error("Failed when render Comments, error:" + e.getMessage());
		}
		log.info("Comments portlet render - end");
		super.render(renderRequest, renderResponse);
	}
}