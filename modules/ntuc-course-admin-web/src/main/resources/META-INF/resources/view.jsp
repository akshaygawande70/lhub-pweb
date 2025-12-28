<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="web.ntuc.nlh.course.admin.constants.MVCCommandNames"%>
<%@ include file="/init.jsp" %>

<portlet:resourceURL id="<%=MVCCommandNames.COURSE_DATA_RESOURCES%>"
	var="searchCourseURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<portlet:renderURL var="courseViewURL"
	windowState="<%=LiferayWindowState.POP_UP.toString()%>">
	<portlet:param name="mvcRenderCommandName"
		value="<%=MVCCommandNames.COURSE_VIEW_RENDER%>" />
	<portlet:param name="redirect"
		value="<%=themeDisplay.getURLCurrent()%>" />
</portlet:renderURL>

<portlet:actionURL var="addCourseApiURL"
	name="<%=MVCCommandNames.ADD_COURSE_API_ACTION%>">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>

<portlet:actionURL var="convertCourseURL"
	name="<%=MVCCommandNames.CONVERT_COURSE_ACTION%>">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>

<portlet:actionURL var="moveCourseURL"
	name="<%=MVCCommandNames.MOVE_COURSE_ACTION%>">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>

<portlet:actionURL var="updateCourseURL"
	name="<%=MVCCommandNames.UPDATE_COURSE_ACTION%>">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>

<portlet:actionURL var="deleteCourseURL"
	name="<%=MVCCommandNames.DELETE_COURSE_ACTION%>">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>

<section class="section-main data-list">
	<div class="container-fluid">

		<!-- HEADER ACTION -->
		<div class="row header_action">
			<div class="col-lg-12">
				<div class="mb-2 mt-3 clearfix">
					<div class="float-sm-right text-zero">
						<!-- <button type="button"
							class="btn btn-danger btn-lg top-right-button mr-1">
							Delete <span id="del_sel"></span>
						</button> -->
						<a href="${addCourseApiURL}"
							class="btn btn-success btn-lg top-right-button mr-1">Manual Trigger</a>
						<a href="${convertCourseURL}"
							class="btn btn-primary btn-lg top-right-button mr-1">Convert</a>
						<a href="${moveCourseURL}"
							class="btn btn-warning btn-lg top-right-button mr-1">Move</a>
						<a href="${updateCourseURL}"
							class="btn btn-success btn-lg top-right-button mr-1">Update</a>
						<a href="${deleteCourseURL}"
							class="btn btn-danger btn-lg top-right-button mr-1">Delete</a>
					</div>
				</div>

				<div class="separator mb-5"></div>
			</div>
		</div>
		<!-- HEADER ACTION -->

		<!-- CONTENT -->
		<div class="displayContentTable">
			<table
				id="adminCourseList" class="dataTable table table-striped table-bordered" aria-label="Courses Admin">
				<thead>
					<tr role="row">
						<th class="sorting" id="course-id"><liferay-ui:message key="course-id" /></th>
						<th class="sorting" id="venue"><liferay-ui:message key="venue" /></th>
						<th class="sorting" id="allow-online-payment"><liferay-ui:message
								key="allow-online-payment" /></th>
						<th class="sorting" id="course-title"><liferay-ui:message key="course-title" /></th>
						<th class="sorting" id="allow-web-registration"><liferay-ui:message
								key="allow-web-registration" /></th>
						<th class="sorting" id="description"><liferay-ui:message key="description" /></th>
						<th class="sorting" id="course-fee"><liferay-ui:message key="course-fee" /></th>
						<th class="sorting" id="course-type"><liferay-ui:message key="course-type" /></th>
						<th class="sorting" id="availability"><liferay-ui:message key="availability" /></th>
						<th class="sorting" id="batch-id"><liferay-ui:message key="batch-id" /></th>
						<th class="sorting" id="web-expiry"><liferay-ui:message key="web-expiry" /></th>
						<th class="sorting" id="funded-course-flag"><liferay-ui:message
								key="funded-course-flag" /></th>
						<th class="sorting" id="course-code"><liferay-ui:message key="course-code" /></th>
						<th class="sorting" id="course-duration"><liferay-ui:message key="course-duration" /></th>
						<th class="sorting" id="popular"><liferay-ui:message key="popular" /></th>
						<th class="sorting" id="start-date"><liferay-ui:message key="start-date" /></th>
						<th class="sorting" id="end-date"><liferay-ui:message key="end-date" /></th>
						<th class="sorting" id="create-date"><liferay-ui:message key="create-date" /></th>
						<th class="sorting" id="modified-date"><liferay-ui:message key="modified-date" /></th>
						<th style="text-align: center !important;" id="action"><liferay-ui:message
										key="action" /></th>
					</tr>
				</thead>

			</table>
		</div>
		<!-- CONTENT -->
	</div>
</section>

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						var table = $('#adminCourseList')
								.DataTable(
										{

											"initComplete" : function(settings,
													json) {
												/*console.log("finished");*/
											},
											"sAjaxSource" : '${searchCourseURL}',
											"bFilter" : true,
											"responsive" : true,
											"bServerSide" : true,
											"bAutowidth" : false,
											"bLengthChange" : false,
											"bInfo" : true,
											'columnDefs' : [
													
													{
														"mDataProp" : "courseId",
														"bSortable" : true,
														"aTargets" : [ 0 ]
													},
													{
														"mDataProp" : "venue",
														"bSortable" : true,
														"aTargets" : [ 1 ]
													},
													{
														"mDataProp" : "allowOnlinePayment",
														"bSortable" : true,
														"aTargets" : [ 2 ]
													},
													{
														"mDataProp" : "courseTitle",
														"bSortable" : true,
														"aTargets" : [ 3 ]
													},
													{
														"mDataProp" : "allowWebRegistration",
														"bSortable" : true,
														"aTargets" : [ 4 ]
													},
													{
														"mDataProp" : "description",
														"bSortable" : true,
														"aTargets" : [ 5 ]
													},
													{
														"mDataProp" : "courseFee",
														"bSortable" : true,
														"aTargets" : [ 6 ]
													},
													{
														"mDataProp" : "courseType",
														"bSortable" : true,
														"aTargets" : [ 7 ]
													},
													{
														"mDataProp" : "availability",
														"bSortable" : true,
														"aTargets" : [ 8 ]
													},
													{
														"mDataProp" : "batchId",
														"bSortable" : true,
														"aTargets" : [ 9 ]
													},
													{
														"mDataProp" : "webExpiry",
														"bSortable" : true,
														"aTargets" : [ 10 ]
													},
													{
														"mDataProp" : "fundedCourseFlag",
														"bSortable" : true,
														"aTargets" : [ 11 ]
													},
													{
														"mDataProp" : "courseCode",
														"bSortable" : true,
														"aTargets" : [ 12 ]
													},
													{
														"mDataProp" : "courseDuration",
														"bSortable" : true,
														"aTargets" : [ 13 ]
													},
													{
														"mDataProp" : "popular",
														"bSortable" : true,
														"aTargets" : [ 14 ]
													},
													{
														"mDataProp" : "startDate",
														"bSortable" : true,
														"aTargets" : [ 15 ]
													},
													{
														"mDataProp" : "endDate",
														"bSortable" : true,
														"aTargets" : [ 16 ]
													},
													{
														"mDataProp" : "createDate",
														"bSortable" : true,
														"aTargets" : [ 17 ]
													},
													{
														"mDataProp" : "modifiedDate",
														"bSortable" : true,
														"aTargets" : [ 18 ]
													},
													{
														"aTargets" : [ 19 ],
														"bSortable" : false,
														"mData" : null,
														"mRender" : function(data, type, full) {
															return courseActionButton(data);
														}
													}
													],
											select : true,
											order : [ [ 1, 'asc' ] ],
											language : {
												paginate : {
													next : '<i class="fal fa-chevron-right"></i>',
													previous : '<i class="fal fa-chevron-left"></i>'
												}
											}
										});

						// Handle form submission event
						$('#frm-example')
								.on(
										'submit',
										function(e) {
											var form = this;

											var rows_selected = table.column(0).checkboxes
													.selected();

											// Iterate over all selected checkboxes
											$.each(rows_selected, function(
													index, rowId) {
												// Create a hidden element
												$(form).append(
														$('<input>').attr(
																'type',
																'hidden').attr(
																'name', 'id[]')
																.val(rowId));
											});
										});

					});
					
					//button action
					function courseActionButton(data) {
						var btn = "<div class='box-action text-center'>";
						btn += '<a class="btn btn-xs btn-border" href="javascript:showPopup('
								+ data.courseId + ');">';
						btn += '<liferay-ui:message key="view"/><i class="fa fa-eye"></i></a>';
						return btn + "</div>";
					}
					
					function showPopup(id) {
						var popupURL = '${courseViewURL}';
						popupURL = encodeURI(popupURL) + "&<portlet:namespace/>id=" + id;

						Liferay.Util.openWindow({
							cache : false,
							dialog : {
								align : Liferay.Util.Window.ALIGN_CENTER,
								destroyOnHide : true,
								modal : true,
								width : 1000,
								height : 700,
							},
							id : 'course-' + id,
							title : 'Course Detail',
							uri : popupURL
						});
					}
					
</script>