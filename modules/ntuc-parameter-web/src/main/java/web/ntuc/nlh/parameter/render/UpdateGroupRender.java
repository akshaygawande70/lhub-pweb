package web.ntuc.nlh.parameter.render;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.List;
import java.util.stream.Collectors;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import svc.ntuc.nlh.parameter.exception.ParameterValidationException;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import web.ntuc.nlh.parameter.config.ParameterConfig;
import web.ntuc.nlh.parameter.constants.MVCCommandNames;
import web.ntuc.nlh.parameter.constants.ParameterPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.UPDATE_GROUP_RENDER,
		"javax.portlet.name=" + ParameterPortletKeys.PARAMETER_PORTLET }, service = MVCRenderCommand.class)
public class UpdateGroupRender implements MVCRenderCommand {

	private static Log log = LogFactoryUtil.getLog(UpdateGroupRender.class);

	private static String parameterGroupPage = "/parameter-group/form.jsp";
	private static String errorPage = "/error.jsp";

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		log.info("Update Parameter Group render - start");
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

			long parameterGroupId = ParamUtil.getLong(renderRequest, "parameterGroupId", 0);
			ParameterGroup parameterGroup = null;

			if (parameterGroupId > 0) {
				parameterGroup = ParameterGroupLocalServiceUtil.getParameterGroup(parameterGroupId);				
			}
			
			if(Long.valueOf(groupId)>0) {
				try {
					List<ParameterGroup> parameterGroups = ParameterGroupLocalServiceUtil
							.getParameterGroupsByGroupId(Long.valueOf(groupId), false);
					parameterGroups = parameterGroups.stream().filter(a -> (a.getParameterGroupId() != parameterGroupId))
							.collect(Collectors.toList());
					renderRequest.setAttribute("groupParent", parameterGroups);
				} catch (Exception e) {
					log.info("Error no group available yet: " + e.getMessage());
				}
			}
			renderRequest.setAttribute("parameterGroup", parameterGroup);
			
			
			
		} catch (Exception e) {
			log.error("Error while rendering parameter group: " + e.getMessage());
			return errorPage;
		}
		log.info("Update Parameter Group render - end");
		return parameterGroupPage;
	}

}
