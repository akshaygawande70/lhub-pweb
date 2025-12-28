<%@ include file="init.jsp"%>
<%@page import="web.ntuc.nlh.comments.constants.MVCCommandNames"%>

<portlet:resourceURL id="<%=MVCCommandNames.LIST_COMMENT_RESOURCE%>"
	var="commentResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<portlet:actionURL name="<%=MVCCommandNames.ADD_COMMENT%>"
	var="addCommentURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>

<h1 class="title-5">Comments</h1>
<div id="comment-list">
	<div class="loader"></div>
	<!-- INSERTED BY JS -->
</div>

<div id="respond" class="comment-respond">
	<h3 id="reply-title" class="comment-reply-title">Post A Comment</h3>
	<aui:form name="fm" method="post" action="${addCommentURL}">
		<p class="comment-notes">
			<span id="email-notes">Your email address will not be
				published.</span> Required fields are marked <span class="required">*</span>
		</p>
		<div class="inrow">
			<aui:input type="textarea" name="commentText" required="true"
				placeholder="Your Comment" label="" />
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="inrow">
					<aui:input type="text" name="commentName" required="true"
						placeholder="Your Name" label="" />
				</div>
			</div>
			<div class="col-md-6">
				<div class="inrow">
					<aui:input type="email" name="commentEmail" required="true"
						placeholder="Your Email" label="">
						<aui:validator name="commentEmail"
							errorMessage="wrong-email-pattern" />
					</aui:input>
				</div>
			</div>
		</div>
		<div class="custom-checkbox">
			<aui:input type="checkbox" name="commentOption" label="option-text" />
		</div>
		<p class="form-submit">
			<aui:button type="submit" name="submit" cssClass="btn-1 minw-3"
				value="Submit" />
		</p>
	</aui:form>
</div>

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
	var comments = "";
	
	$(document).ready(function(){
		initComments();
	});

	function initComments() {
		return jQuery.ajax({
			url : "<%=commentResourceURL.toString()%>",
			type : "GET",
			dataType : "json",
			async : false,
			cache : false,
			beforeSend : function() { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
				$('.loader').removeClass('hidden')
			},
			success : function(data, textStatus, XMLHttpRequest) {

				var htmlComments = '';

				if (data.comments.length > 0) {
					data.comments.forEach(function(comment) {
						htmlComments += generateCommentList(comment);
					});
				}

				$('#comment-list').html(htmlComments);

				comments = data.comments;
			},
			complete : function() { // Set our complete callback, adding the .hidden class and hiding the spinner.
				$('.loader').hide();
			},
			error : function(data, textStatus, XMLHttpRequest) {
				//return null;
			}
		});
	}

	function generateCommentList(data) {
		var html = '<div class="box-5">'
		html += '<div class="title">';
		html += '<h3>' + data.name + '</h3>';
		html += '<p>' + data.createDate + '</p>';
		html += '</div>';
		html += '<p>' + data.comment + '</p>';
		html += '</div>';

		return html;
	}
</script>