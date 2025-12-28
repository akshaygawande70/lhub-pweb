package web.ntuc.nlh.assetfilter.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucRenderFilter;
import web.ntuc.nlh.assetfilter.constants.AssetFilterPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + AssetFilterPortletKeys.ASSETFILTER_PORTLET, }, service = PortletFilter.class)
public class AssetFilterRenderFilter extends NtucRenderFilter {

	public AssetFilterRenderFilter() {
		// Do nothing because not used.
	}
}
