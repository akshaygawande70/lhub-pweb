package web.ntuc.nlh.comments.action;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
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
import web.ntuc.nlh.comments.constants.CommentsMessagesKeys;
import web.ntuc.nlh.comments.constants.CommentsPortletKeys;
import web.ntuc.nlh.comments.constants.MVCCommandNames;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.ADD_COMMENT,
		"javax.portlet.name=" + CommentsPortletKeys.COMMENTS_PORTLET }, service = MVCActionCommand.class)
public class SubmitCommentAction extends BaseMVCActionCommand {

	private static Log log = LogFactoryUtil.getLog(SubmitCommentAction.class);

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Submit Comment Action - Start");

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		long groupId = themeDisplay.getScopeGroupId();

		PortletCommandUtil.actionAndResourceCommand(actionRequest, themeDisplay);

//		Get Param from Form JSP
		String commentText = ParamUtil.getString(actionRequest, "commentText");
		String commentName = ParamUtil.getString(actionRequest, "commentName");
		String commentEmail = ParamUtil.getString(actionRequest, "commentEmail");
		boolean commentOption = ParamUtil.getBoolean(actionRequest, "commentOption");

		try {
			Comment comment = CommentLocalServiceUtil
					.createComment(CounterLocalServiceUtil.increment(Comment.class.getCanonicalName()));

//			Required Param
			comment.setCommentText(commentText);
			comment.setCommentName(commentName);
			comment.setCommentEmail(commentEmail);
			comment.setCommentOption(commentOption);
			comment.setCommentStatus(false);

//			Default Param
			comment.setUserId(themeDisplay.getUserId());
			comment.setCreateDate(new Date());
			comment.setCompanyId(themeDisplay.getCompanyId());
			comment.setGroupId(groupId);
			comment.setUserName(themeDisplay.getUser().getFullName());

			CommentLocalServiceUtil.updateComment(comment);
			log.info("Submit Comment Successfully");

		} catch (Exception e) {
			log.error("Submit Comment Error at : " + e.getMessage());
		}

		actionResponse.getRenderParameters().setValue("mvcPath", "/view.jsp");
		log.info("Submit Comment Action - End");
	}

}
