<%@page import="web.ntuc.nlh.comments.admin.constants.MVCCommandNames"%>
<%@ include file="init.jsp"%>

<portlet:resourceURL id="<%=MVCCommandNames.COMMENT_DATA_RESOURCES%>"
	var="commentResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<portlet:renderURL var="editCommentURL">
	<portlet:param name="mvcRenderCommandName"
		value="<%=MVCCommandNames.EDIT_COMMENT_RENDER%>" />
	<portlet:param name="authToken" value="${authToken}" />
</portlet:renderURL>

<portlet:actionURL name="<%=MVCCommandNames.DELETE_COMMENT_ACTION%>"
	var="deleteCommentURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>

<section class="section-main data-list">
	<div class="container-fluid">
		<!-- CONTENT -->
		<div class="displayContentTable">
			<table
				class="CX-datatable dataTable table table-striped table-bordered"
				id="<portlet:namespace/>comments" aria-label="Comment Admin">
				<thead style="text-align: center;">
					<tr role="row">
						<th id="comment">Comment</th>
						<th id="name">Name</th>
						<th id="email">Email</th>
						<th id="createdDate">Created Date</th>
						<th id="modifiedDate">Modified Date</th>
						<th id="status">Status</th>
						<th id="action">Action</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</section>

<script type="text/javascript">
	var comments = "";
	
	$(document).ready(function(){
		var commentTblId = "#<portlet:namespace/>comments";
		var commentDataTable = $(commentTblId).dataTable({
			"bFilter" : false,
			"responsive" : true,
			"bServerSide" : true,
			"bAutowidth" : true,
			"bLengthChange" : true,
			"bInfo" : true,
			"sAjaxSource" : '${commentResourceURL}',
			"aoColumnDefs" : [ {
				"mDataProp" : "commentText",
				"bSortable" : true,
				"aTargets" : [ 0 ]
			}, {
				"mDataProp" : "commentName",
				"bSortable" : true,
				"aTargets" : [ 1 ]
			}, {
				"mDataProp" : "commentEmail",
				"bSortable" : true,
				"aTargets" : [ 2 ]
			},{
				"mDataProp" : "createdDate",
				"bSortable" : true,
				"aTargets" : [ 3 ]
			}, {
				"mDataProp" : "modifiedDate",
				"bSortable" : true,
				"aTargets" : [ 4 ]
			}, {
				"mDataProp" : "commentStatus",
				"bSortable" : true,
				"aTargets" : [ 5 ]
			},{
				"aTargets" : [ 6 ],
				"bSortable" : false,
				"mData" : null,
				"mRender" : function(data, type, full) {
					return commentActionButton(data);
				}
			}]
		});
	});

		function commentActionButton(data) {
			var btn = "<div class='box-action text-center'>";
			btn += '<a class="btn btn-xs btn-border" href="javascript:showPopupGroup('
					+ data.commentId + ');">';
			btn += '<liferay-ui:message key="view"/><i class="fa fa-eye"></i></a>';
			btn += '<a href=\"'+ data.editUrl+ '\" class="btn btn-xs btn-border">';
			btn += '<liferay-ui:message key="edit"/><i class="far fa-pencil-alt"></i></a>';
			btn += '&nbsp;';
			btn += '<a href=\"'
					+ data.deleteUrl
					+ '\" onclick="return confirmDelete()" class="btn btn-xs btn-border">';
			btn += '<liferay-ui:message key="delete"/><i class="far fa-trash-alt"></i></a>';
			return btn + "</div>";
		} 

	function confirmDelete() {
		var m = '<liferay-ui:message key="delete-pop-up"/>';
		return confirm(m);
	}
</script>