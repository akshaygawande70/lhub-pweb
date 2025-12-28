package web.ntuc.eshop.register.render;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.register.constants.MVCCommandNames;
import web.ntuc.eshop.register.constants.RegisterPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.REGISTER_INDIVIDUAL_RENDER,
		"javax.portlet.name=" + RegisterPortletKeys.REGISTER }, service = MVCRenderCommand.class)
public class RegisterIndividualRender implements MVCRenderCommand {

	private static Log log = LogFactoryUtil.getLog(RegisterIndividualRender.class);
	private static String registerIndividu = "/register/register-individual.jsp";
	
	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		try {
			/*boolean isAuthorized = ParamUtil.getBoolean(renderRequest, "isAuthorized");
			boolean xssPass = ParamUtil.getBoolean(renderRequest, "xssPass");

			if (!(isAuthorized && xssPass)) {
				log.error("course view render - isAuthorized : " + isAuthorized + " | xssPass : " + xssPass);
			}*/
//			HttpServletRequest httpReq = PortalUtil
//					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(renderRequest));
//			String code = httpReq.getParameter("code");
			String code = ParamUtil.getString(renderRequest, "code");
			renderRequest.setAttribute("code", code);
			PortletCommandUtil.renderCommand(renderRequest);
		} catch (Exception e) {
			log.error("Register Individual Render Error : " + e.getMessage());
			SessionErrors.add(renderRequest, "error-on-individual");
		}
		return registerIndividu;
	}

}
