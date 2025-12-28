package web.ntuc.nlh.parameter.config.portlet;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.persistence.GroupPersistence;
import com.liferay.portal.kernel.service.persistence.GroupUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

import aQute.bnd.annotation.metatype.Configurable;
import web.ntuc.nlh.parameter.config.ParameterConfig;
import web.ntuc.nlh.parameter.constants.ParameterPortletKeys;

@Component(configurationPid = "web.afi.parameter.config.ParameterConfig", configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true, property = {
		"javax.portlet.name=" + ParameterPortletKeys.PARAMETER_PORTLET }, service = ConfigurationAction.class)
public class ParameterConfigPortlet extends DefaultConfigurationAction {

	private ParameterConfig parameterConfig;

	@Override
	public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		String groupId = ParamUtil.getString(actionRequest, ParameterConfig.GROUP_ID, "0");

		setPreference(actionRequest, ParameterConfig.GROUP_ID, groupId);

		super.processAction(portletConfig, actionRequest, actionResponse);
	}

	@Override
	public void include(PortletConfig portletConfig, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GroupPersistence gp = GroupUtil.getPersistence();
		ClassLoader cgp = gp.getClass().getClassLoader();
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Group.class, cgp);
		dynamicQuery.add(RestrictionsFactoryUtil.eq("type", 1));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("site", true));
		List<Group> group = GroupLocalServiceUtil.dynamicQuery(dynamicQuery);

		request.setAttribute(ParameterConfig.class.getName(), parameterConfig);
		request.setAttribute("groups", group);

		super.include(portletConfig, request, response);
	}

	@Override
	public String getJspPath(HttpServletRequest request) {
		return "/config/config.jsp";
	}

	@Activate
	@Modified
	protected void activate(Map<Object, Object> properties) {
		parameterConfig = Configurable.createConfigurable(ParameterConfig.class, properties);
	}

}
