package web.ntuc.nlh.comments.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucActionFilter;
import web.ntuc.nlh.comments.constants.CommentsPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + CommentsPortletKeys.COMMENTS_PORTLET, }, service = PortletFilter.class)
public class CommentActionFilter extends NtucActionFilter {

	public CommentActionFilter() {
		// Do nothing because not used.
	}
}
