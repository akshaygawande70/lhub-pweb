package web.ntuc.nlh.comments.resource;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.comments.model.Comment;
import svc.ntuc.nlh.comments.service.CommentLocalServiceUtil;
import web.ntuc.nlh.comments.constants.CommentsPortletKeys;
import web.ntuc.nlh.comments.constants.MVCCommandNames;
import web.ntuc.nlh.comments.dto.CommentDto;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.LIST_COMMENT_RESOURCE,
		"javax.portlet.name=" + CommentsPortletKeys.COMMENTS_PORTLET }, service = MVCResourceCommand.class)
public class ListCommentResource implements MVCResourceCommand {

	private static Log log = LogFactoryUtil.getLog(ListCommentResource.class);

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("List Comment Resource - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			Locale locale = themeDisplay.getLocale();

			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

			List<Comment> comments = CommentLocalServiceUtil.getAllComment();
			log.info("Count all comment : " + comments.size());
			
//			Mapped Comment List to Comment DTO
			List<CommentDto> mappedCommentList = new ArrayList<>();
			
			for (Comment comment : comments) {
//				Check if comment status was true
				if (!comment.getCommentStatus()) {
					CommentDto commentDto = new CommentDto();
					commentDto.setCommentId(comment.getCommentId());
					commentDto.setComment(comment.getCommentText());
					commentDto.setName(comment.getCommentName());
					commentDto.setEmail(comment.getCommentEmail());
					commentDto.setOption(commentDto.getOption());
					commentDto.setStatus(comment.getCommentStatus());
					commentDto.setCreateDate(getCalculateTimeAgo(comment.getCreateDate()));	

					mappedCommentList.add(commentDto);
				}
			}

			resourceResponse.setContentType("application/json");
			PrintWriter out = null;
			out = resourceResponse.getWriter();

			JSONObject rowData = JSONFactoryUtil.createJSONObject();
			rowData.put("comments", mappedCommentList);
			rowData.put("status", HttpServletResponse.SC_OK);
			out.print(rowData.toString());
			out.flush();
		} catch (Exception e) {
			log.error("Failed Load List Comment at : " + e.getMessage());
		}

		log.info("List Comment Resource - End");
		return false;
	}
	
	public String getCalculateTimeAgo(Date createDate) {
		Date currentDate = new Date();
		String calculateTime = null;
		float currentTime = currentDate.getTime();
		float createTime = createDate.getTime();
		
		float milliSecPerMinute = 60f * 1000; // Milliseconds Per Minute
		float milliSecPerHour = milliSecPerMinute * 60; // Milliseconds Per Hour
		float milliSecPerDay = milliSecPerHour * 24; // Milliseconds Per Day
		float milliSecPerMonth = milliSecPerDay * 30; // Milliseconds Per Month
		float milliSecPerYear = milliSecPerDay * 365; // Milliseconds Per Year
		
		// Difference in Milliseconds between two dates
		float msExpired = currentTime - createTime;
		
		// Second or Seconds ago calculation
		if (msExpired < milliSecPerMinute) {
			if (Math.round(msExpired / 1000) == 1) {
				calculateTime = String.valueOf(Math.round(msExpired / 1000)) + " second ago";
			} else {
				calculateTime = String.valueOf(Math.round(msExpired / 1000) + " seconds ago");
			}
		}
		// Minute or Minutes ago calculation
		else if (msExpired < milliSecPerHour) {
			if (Math.round(msExpired / milliSecPerMinute) == 1) {
				calculateTime = String.valueOf(Math.round(msExpired / milliSecPerMinute)) + " minute ago";
			} else {
				calculateTime = String.valueOf(Math.round(msExpired / milliSecPerMinute)) + " minutes ago";
			}
		}
		// Hour or Hours ago calculation
		else if (msExpired < milliSecPerDay) {
			if (Math.round(msExpired / milliSecPerHour) == 1) {
				calculateTime = String.valueOf(Math.round(msExpired / milliSecPerHour)) + " hour ago";
			} else {
				calculateTime = String.valueOf(Math.round(msExpired / milliSecPerHour)) + " hours ago";
			}
		}
		// Day or Days ago calculation
		else if (msExpired < milliSecPerMonth) {
			if (Math.round(msExpired / milliSecPerDay) == 1) {
				calculateTime = String.valueOf(Math.round(msExpired / milliSecPerDay)) + " day ago";
			} else {
				calculateTime = String.valueOf(Math.round(msExpired / milliSecPerDay)) + " days ago";
			}
		}
		// Month or Months ago calculation
		else if (msExpired < milliSecPerYear) {
			if (Math.round(msExpired / milliSecPerMonth) == 1) {
				calculateTime = String.valueOf(Math.round(msExpired / milliSecPerMonth)) + "  month ago";
			} else {
				calculateTime = String.valueOf(Math.round(msExpired / milliSecPerMonth)) + "  months ago";
			}
		}
		// Year or Years ago calculation
		else {
			if (Math.round(msExpired / milliSecPerYear) == 1) {
				calculateTime = String.valueOf(Math.round(msExpired / milliSecPerYear)) + " year ago";
			} else {
				calculateTime = String.valueOf(Math.round(msExpired / milliSecPerYear)) + " years ago";
			}
		}

		return calculateTime;
	}

}
