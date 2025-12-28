package web.ntuc.nlh.parameter.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucActionFilter;
import web.ntuc.nlh.parameter.constants.ParameterPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + ParameterPortletKeys.PARAMETER_PORTLET, }, service = PortletFilter.class)
public class ParameterActionFilter extends NtucActionFilter{
	
	public ParameterActionFilter() {
		// Do nothing because not used.
	}
}
