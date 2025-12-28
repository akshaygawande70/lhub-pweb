package web.ntuc.nlh.comments.admin.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucRenderFilter;
import web.ntuc.nlh.comments.admin.constants.CommentsAdminPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + CommentsAdminPortletKeys.COMMENTSADMIN_PORTLET, }, service = PortletFilter.class)
public class CommentsAdminRenderFilter extends NtucRenderFilter {

	public CommentsAdminRenderFilter() {
		// Do nothing because not used.
	}

}
