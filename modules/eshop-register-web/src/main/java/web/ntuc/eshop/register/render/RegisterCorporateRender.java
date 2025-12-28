package web.ntuc.eshop.register.render;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.register.constants.MVCCommandNames;
import web.ntuc.eshop.register.constants.RegisterPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.REGISTER_CORPORATE_RENDER,
		"javax.portlet.name=" + RegisterPortletKeys.REGISTER }, service = MVCRenderCommand.class)
public class RegisterCorporateRender implements MVCRenderCommand {

	private static Log log = LogFactoryUtil.getLog(RegisterCorporateRender.class);
	private static String registerCorporate = "/register/register-corporate.jsp";
	
	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		try {
			/*boolean isAuthorized = ParamUtil.getBoolean(renderRequest, "isAuthorized");
			boolean xssPass = ParamUtil.getBoolean(renderRequest, "xssPass");

			if (!(isAuthorized && xssPass)) {
				log.error("course view render - isAuthorized : " + isAuthorized + " | xssPass : " + xssPass);
			}*/
			PortletCommandUtil.renderCommand(renderRequest);
		} catch (Exception e) {
			log.error("Register Individual Render Error : " + e.getMessage());
			SessionErrors.add(renderRequest, "error-on-individual");
		}
		return registerCorporate;
	}

}
