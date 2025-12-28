package web.ntuc.nlh.parameter.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucRenderFilter;
import web.ntuc.nlh.parameter.constants.ParameterPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + ParameterPortletKeys.PARAMETER_PORTLET, }, service = PortletFilter.class)
public class ParameterRenderFilter extends NtucRenderFilter {

	public ParameterRenderFilter() {
		// Do nothing because not used.
	}

}
