package web.ntuc.nlh.comments.admin.render;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.validation.ValidationException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.comments.model.Comment;
import svc.ntuc.nlh.comments.service.CommentLocalService;
import web.ntuc.nlh.comments.admin.constants.CommentsAdminPortletKeys;
import web.ntuc.nlh.comments.admin.constants.MVCCommandNames;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.EDIT_COMMENT_RENDER,
		"javax.portlet.name=" + CommentsAdminPortletKeys.COMMENTSADMIN_PORTLET }, service = MVCRenderCommand.class)
public class EditCommentRender implements MVCRenderCommand {

	private static Log log = LogFactoryUtil.getLog(EditCommentRender.class);

	private static String commentEditPage = "/edit-action.jsp";

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		log.info("Edit Comment Render - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			PortletCommandUtil.renderCommand(renderRequest);

			long commentId = ParamUtil.getLong(renderRequest, "commentId", 0);
			Comment comment = commentLocalService.getComment(commentId);
			renderRequest.setAttribute("comment", comment);
		} catch (Exception e) {
			log.error("Failed when render data by : " + e.getMessage());
		}
		log.info("Edit Comment Render - End");
		return commentEditPage;
	}

	@Reference
	private CommentLocalService commentLocalService;
}
