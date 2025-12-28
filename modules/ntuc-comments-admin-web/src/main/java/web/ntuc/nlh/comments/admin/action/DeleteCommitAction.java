package web.ntuc.nlh.comments.admin.action;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.validation.ValidationException;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.comments.service.CommentLocalServiceUtil;
import web.ntuc.nlh.comments.admin.constants.CommentsAdminMessagesKey;
import web.ntuc.nlh.comments.admin.constants.CommentsAdminPortletKeys;
import web.ntuc.nlh.comments.admin.constants.MVCCommandNames;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.DELETE_COMMENT_ACTION,
		"javax.portlet.name=" + CommentsAdminPortletKeys.COMMENTSADMIN_PORTLET }, service = DeleteCommitAction.class)
public class DeleteCommitAction extends BaseMVCActionCommand {

	private static Log log = LogFactoryUtil.getLog(DeleteCommitAction.class);

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Delete Comment Action - Start");

		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

			PortletCommandUtil.actionAndResourceCommand(actionRequest, themeDisplay);

			long commentId = ParamUtil.getLong(actionRequest, "commentId");

			CommentLocalServiceUtil.deleteComment(commentId);

		} catch (Exception e) {
			log.info("Error Deleting comment at : " + e.getMessage());
		}

		log.info("Delete Comment Action - End");
	}

}
