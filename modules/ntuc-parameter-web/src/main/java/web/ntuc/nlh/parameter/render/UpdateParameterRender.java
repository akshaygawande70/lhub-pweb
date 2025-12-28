package web.ntuc.nlh.parameter.render;

import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.ParamUtil;

import svc.ntuc.nlh.parameter.exception.ParameterValidationException;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.nlh.parameter.config.ParameterConfig;
import web.ntuc.nlh.parameter.constants.MVCCommandNames;
import web.ntuc.nlh.parameter.constants.ParameterPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.UPDATE_PARAMETER_RENDER,
		"javax.portlet.name=" + ParameterPortletKeys.PARAMETER_PORTLET }, service = MVCRenderCommand.class)
public class UpdateParameterRender implements MVCRenderCommand {

	private static Log log = LogFactoryUtil.getLog(UpdateParameterRender.class);

	private static String parameterPage = "/parameter/form.jsp";
	private static String errorPage = "/error.jsp";

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		log.info("Update Parameter render - start");
		try {
			String groupId = renderRequest.getPreferences().getValue(ParameterConfig.GROUP_ID, "0");
			boolean isAuthorized = ParamUtil.getBoolean(renderRequest, "isAuthorized");
			boolean validCSRF = ParamUtil.getBoolean(renderRequest, "validCSRF");
			boolean xssPass = ParamUtil.getBoolean(renderRequest, "xssPass");

			if (!isAuthorized || !validCSRF || !xssPass) {
				String msg = "isAuthorized : " + isAuthorized + " | validCSRF : " + validCSRF + " | xssPass : "
						+ xssPass;
				throw new ParameterValidationException(msg);
			}

			long parameterId = ParamUtil.getLong(renderRequest, "parameterId", 0);
			Parameter parameter = null;

			if (parameterId > 0) {
				parameter = ParameterLocalServiceUtil.getParameter(parameterId);
			}
			renderRequest.setAttribute("parameter", parameter);
			
			if(Long.valueOf(groupId) > 0) {
				List<ParameterGroup> parameterGroups = ParameterGroupLocalServiceUtil
						.getParameterGroupsByGroupId(Long.valueOf(groupId), false);
				renderRequest.setAttribute("groups", parameterGroups);
			}
			
		} catch (Exception e) {
			log.error("Error while rendering parameter: " + e.getMessage());
			return errorPage;
		}
		log.info("Update Parameter render - end");
		return parameterPage;
	}

}
