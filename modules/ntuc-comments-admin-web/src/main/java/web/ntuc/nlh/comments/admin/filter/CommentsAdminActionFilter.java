package web.ntuc.nlh.comments.admin.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucActionFilter;
import web.ntuc.nlh.comments.admin.constants.CommentsAdminPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + CommentsAdminPortletKeys.COMMENTSADMIN_PORTLET, }, service = PortletFilter.class)
public class CommentsAdminActionFilter extends NtucActionFilter {

	public CommentsAdminActionFilter() {
		// Do nothing because not used.
	}
}
