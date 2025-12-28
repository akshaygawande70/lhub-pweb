package web.ntuc.nlh.comments.filter;

import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.filter.NtucRenderFilter;
import web.ntuc.nlh.comments.constants.CommentsPortletKeys;

@Component(immediate = true, property = {
		"javax.portlet.name=" + CommentsPortletKeys.COMMENTS_PORTLET, }, service = PortletFilter.class)
public class CommentRenderFilter extends NtucRenderFilter {

	public CommentRenderFilter() {
		// Do nothing because not used.
	}

}
