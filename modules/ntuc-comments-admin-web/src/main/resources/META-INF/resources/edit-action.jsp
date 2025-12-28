<%@page import="web.ntuc.nlh.comments.admin.constants.MVCCommandNames"%>
<%@ include file="init.jsp"%>

<portlet:actionURL name="<%=MVCCommandNames.EDIT_COMMENT_ACTION%>"
	var="updateCommentURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>
<h1>UPDATE COMMENT</h1>
<div id="respond" class="comment-respond">
	<h3 id="reply-title" class="comment-reply-title">Edit Comment</h3>
	<aui:form name="fm" method="post" action="${updateCommentURL}">
		<aui:input type="hidden" name="commentId" label="" value="${comment.commentId}" />
		<div class="inrow">
			<aui:input type="textarea" name="commentText"
				placeholder="Your Comment" label="Comment"
				value="${comment.commentText}" />
		</div>
		<div class="row">
			<div class="col-md-4">
				<div class="inrow">
					<aui:input type="text" name="commentName" placeholder="Your Name"
						label="Name" value="${comment.commentName}" />
				</div>
			</div>
			<div class="col-md-4">
				<div class="inrow">
					<aui:input type="email" name="commentEmail"
						placeholder="Your Email" label="Email"
						value="${comment.commentEmail}">
						<aui:validator name="commentEmail"
							errorMessage="wrong-email-pattern" />
					</aui:input>
				</div>
			</div>
			<div class="col-md-4">
				<div class="inrow">
					<aui:select name="commentStatus" label="Comment Status">
						<aui:option value="" selected="true" disabled="true">
							<liferay-ui:message key="select-option" />
						</aui:option>
						<c:choose>
							<c:when test="${comment.commentStatus == true }">
								<aui:option value="1" selected="true">Show</aui:option>
								<aui:option value="0">Hide</aui:option>
							</c:when>
							<c:otherwise>
								<aui:option value="1">Show</aui:option>
								<aui:option value="0" selected="true">Hide</aui:option>
							</c:otherwise>
						</c:choose>
					</aui:select>
				</div>
			</div>
		</div>
		<p class="form-submit">
			<aui:button type="submit" name="submit" cssClass="btn-1 minw-3"
				value="Submit" />
		</p>
	</aui:form>
</div>