package web.ntuc.nlh.parameter.render;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;

import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import web.ntuc.nlh.parameter.constants.MVCCommandNames;
import web.ntuc.nlh.parameter.constants.ParameterPortletKeys;
import web.ntuc.nlh.parameter.dto.ParameterViewDto;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.VIEW_GROUP_RENDER,
		"javax.portlet.name=" + ParameterPortletKeys.PARAMETER_PORTLET }, service = MVCRenderCommand.class)
public class ViewGroupRender implements MVCRenderCommand {

	private static Log log = LogFactoryUtil.getLog(ViewGroupRender.class);
	private static String viewGroup = "/parameter-group/view-action.jsp";

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		try {
			boolean isAuthorized = ParamUtil.getBoolean(renderRequest, "isAuthorized");
			boolean xssPass = ParamUtil.getBoolean(renderRequest, "xssPass");

			if (!(isAuthorized && xssPass)) {
				log.error("parameter group view render - isAuthorized : " + isAuthorized + " | xssPass : " + xssPass);
			}

			long parameterGroupId = ParamUtil.getLong(renderRequest, "id", 0);
			ParameterGroup parameterGroup = ParameterGroupLocalServiceUtil.getParameterGroup(parameterGroupId);
			ParameterViewDto dto = ParameterViewDto.mapFromParameterGroup(parameterGroup);

			renderRequest.setAttribute("p", dto);
		} catch (Exception e) {
			log.error("Parameter Group View Render Error : " + e.getMessage());
			SessionErrors.add(renderRequest, "no-group-parameter-available");
		}
		return viewGroup;
	}
}
