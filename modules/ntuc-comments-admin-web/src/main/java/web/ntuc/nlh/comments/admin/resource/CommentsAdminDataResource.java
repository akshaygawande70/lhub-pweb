package web.ntuc.nlh.comments.admin.resource;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;
import java.util.Locale;

import javax.portlet.ActionURL;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.comments.model.Comment;
import svc.ntuc.nlh.comments.service.CommentLocalServiceUtil;
import web.ntuc.nlh.comments.admin.constants.CommentsAdminMessagesKey;
import web.ntuc.nlh.comments.admin.constants.CommentsAdminPortletKeys;
import web.ntuc.nlh.comments.admin.constants.MVCCommandNames;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.COMMENT_DATA_RESOURCES,
		"javax.portlet.name=" + CommentsAdminPortletKeys.COMMENTSADMIN_PORTLET }, service = MVCResourceCommand.class)
public class CommentsAdminDataResource implements MVCResourceCommand {

	private static Log log = LogFactoryUtil.getLog(CommentsAdminDataResource.class);

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {

		log.info("List Comment Admin Resource - Start");

		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			Locale locale = themeDisplay.getLocale();
			String portletName = (String) resourceRequest.getAttribute(WebKeys.PORTLET_ID);

			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

			HttpServletRequest httpRequest = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
			Integer iDisplayStart = ParamUtil.getInteger(httpRequest, "iDisplayStart");
			Integer iDisplayLength = ParamUtil.getInteger(httpRequest, "iDisplayLength");
			if (iDisplayLength == 0) {
				iDisplayLength = 10;
			}
			
			String sEcho = ParamUtil.getString(httpRequest, "sEcho");

			int start = iDisplayStart;
			int end = start + iDisplayLength;

			String authToken = CSRFValidationUtil.authToken(resourceRequest);

			List<Comment> comments = CommentLocalServiceUtil.getComments(start, end);
			log.info("Count all comment : " + comments.size());

			int allCount = CommentLocalServiceUtil.getCommentsCount();
			int countAfterFilter = CommentLocalServiceUtil.getComments(start, end).size();

			JSONArray parameterJsonArray = JSONFactoryUtil.createJSONArray();
			for (Comment comment : comments) {
				JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();
//				Check if comment status was true
				long commentId = comment.getCommentId();

				Comment c = CommentLocalServiceUtil.getComment(commentId);

				final String COMMENT_ID = "commentId";

				jsonBranch.put(COMMENT_ID, c.getCommentId());
				jsonBranch.put("commentText", c.getCommentText());
				jsonBranch.put("commentName", c.getCommentName());
				jsonBranch.put("commentEmail", c.getCommentEmail());
				jsonBranch.put("commentStatus", c.getCommentStatus());
				jsonBranch.put("createdDate", c.getCreateDate());
				jsonBranch.put("modifiedDate", c.getModifiedDate());

				PortletURL editUrl = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(resourceRequest),
						portletName, themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
				editUrl.getRenderParameters().setValue("mvcRenderCommandName", MVCCommandNames.EDIT_COMMENT_RENDER);
				editUrl.getRenderParameters().setValue(COMMENT_ID, String.valueOf(c.getCommentId()));
				editUrl.getRenderParameters().setValue("authToken", authToken);
				jsonBranch.put("editUrl", editUrl);

				ActionURL deleteUrl = resourceResponse.createActionURL();
				deleteUrl.getActionParameters().setValue("javax.portlet.action", MVCCommandNames.DELETE_COMMENT_ACTION);
				deleteUrl.getActionParameters().setValue(COMMENT_ID, String.valueOf(c.getCommentId()));
				deleteUrl.getActionParameters().setValue("authToken", authToken);
				jsonBranch.put("deleteUrl", deleteUrl);

				parameterJsonArray.put(jsonBranch);
			}

			JSONObject tableData = JSONFactoryUtil.createJSONObject();
			tableData.put("iTotalRecords", allCount);
			tableData.put("iTotalDisplayRecords", countAfterFilter);
			tableData.put("sEcho", Integer.parseInt(sEcho));
			tableData.put("aaData", parameterJsonArray);
			resourceResponse.getWriter().println(tableData.toString());

		} catch (Exception e) {
			log.error("Failed Load List Comment Admin at : " + e.getMessage());
			return true;
		}

		log.info("List Comment Admin Resource - End");
		return false;
	}
}
