package web.ntuc.nlh.comments.admin.action;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.validation.ValidationException;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.comments.model.Comment;
import svc.ntuc.nlh.comments.service.CommentLocalServiceUtil;
import web.ntuc.nlh.comments.admin.constants.CommentsAdminMessagesKey;
import web.ntuc.nlh.comments.admin.constants.CommentsAdminPortletKeys;
import web.ntuc.nlh.comments.admin.constants.MVCCommandNames;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.EDIT_COMMENT_ACTION,
		"javax.portlet.name=" + CommentsAdminPortletKeys.COMMENTSADMIN_PORTLET }, service = MVCActionCommand.class)
public class UpdateCommentAction extends BaseMVCActionCommand {

	private static Log log = LogFactoryUtil.getLog(UpdateCommentAction.class);

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Update Comment Action - Start");

		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

			PortletCommandUtil.actionAndResourceCommand(actionRequest, themeDisplay);

//			Get Param from Form JSP
			long commentId = ParamUtil.getLong(actionRequest, "commentId");
			String commentText = ParamUtil.getString(actionRequest, "commentText");
			String commentName = ParamUtil.getString(actionRequest, "commentName");
			String commentEmail = ParamUtil.getString(actionRequest, "commentEmail");
			boolean commentStatus = ParamUtil.getBoolean(actionRequest, "commentStatus");

			Comment comment = CommentLocalServiceUtil.getComment(commentId);

//			Required Param
			comment.setCommentText(commentText);
			comment.setCommentName(commentName);
			comment.setCommentEmail(commentEmail);
			comment.setCommentStatus(commentStatus);
			comment.setModifiedDate(new Date());

			CommentLocalServiceUtil.updateComment(comment);
			log.info("Update Comment Successfully");

		} catch (Exception e) {
			log.error("Update Comment Error at : " + e.getMessage());
		}

		actionResponse.getRenderParameters().setValue("mvcPath", "/view.jsp");
		log.info("Update Comment Action - End");
	}

}
