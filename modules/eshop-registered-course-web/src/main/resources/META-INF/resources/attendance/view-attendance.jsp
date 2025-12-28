<%@page
	import="web.ntuc.eshop.registeredcourse.constants.MVCCommandNames"%>
<%@ include file="../init.jsp"%>

<portlet:resourceURL
	id="<%=MVCCommandNames.COURSE_ATTENDANCES_RESOURCE%>"
	var="courseAttendanceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="nric" value="${nric}" />
	<portlet:param name="birthDate" value="${birthDate}" />
	<portlet:param name="batchId" value="${batchId}" />
</portlet:resourceURL>

<portlet:renderURL var="viewAttendancesDetailURL">
	<portlet:param name="mvcRenderCommandName"
		value="<%=MVCCommandNames.VIEW_ATTENDANCES_DETAIL_RENDER%>" />
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="nric" value="${nric}" />
	<portlet:param name="birthDate" value="${birthDate}" />
	<portlet:param name="batchId" value="${batchId}" />
</portlet:renderURL>

<section class="general-info myaccount-cxrus">
	<%@include file="/layout/header.jsp"%>
	<div class="container">
		<div class="row">
			<div class="box-name">
				<div class="name" id="capital-name">ATTENDANCES</div>
				<div class="email">Attendances for all courses.</div>
			</div>
			<hr class="hr-bold" />
		</div>
	</div>
	<div class="container">
		<div class="row">
			<%@include file="/layout/side-menu.jsp"%>
			<div class="col-md-9">
				<!--Start content-->
				<div class="content active" id="register">
					<div class="box-nav-content">
						<div class="text-link">
							<a href="/account/course_listing"><i
								class="fas fa-angle-left"></i> Back To Courses</a>&nbsp;
						</div>
					</div>
					<table id="<portlet:namespace/>attendance"
						class="table cxrus-table dataTable no-footer"
						style="width: 100%">
						<thead>
							<tr>
								<th>Course Title</th>
								<th>Batch ID</th>
								<th>Attendance Hours</th>
								<th>Attendance %</th>
								<th>&nbsp;</th>
							</tr>
						</thead>
					</table>
				</div>
				<!--End content-->
			</div>
		</div>
	</div>
</section>

<script type="text/javascript">
	$(document).ready(function() {
		var attendanceTblId = "#<portlet:namespace/>attendance";
		var attendanceDataTable = $(attendanceTblId).dataTable({
			"bFilter" : false,
			"responsive" : true,
			"bServerSide" : true,
			"bAutowidth" : true,
			"bLengthChange" : false,
			"bInfo" : false,
			"language" : {
				"emptyTable" : "<%=ContentConstants.NO_DATA_AVAILABLE%>"
			},
			"sAjaxSource" : '${courseAttendanceURL}',
			"aoColumnDefs" : [ {
				"mDataProp" : "course_title",
				"bSortable" : false,
				"aTargets" : [ 0 ]
			}, {
				"mDataProp" : "batch_id",
				"bSortable" : false,
				"aTargets" : [ 1 ]
			}, {
				"mDataProp" : "attendance_hr",
				"bSortable" : false,
				"aTargets" : [ 2 ]
			}, {
				"bSortable" : false,
				"aTargets" : [ 3 ],
				"mData" : null,
				"mRender" : function(data,type, full) {
					return generatePercentageHtml(data);
				}
			}, {
				"aTargets" : [ 4 ],
				"bSortable" : false,
				"mData" : null,
				"mRender" : function(data, type, full) {
					return viewButton(data);
				}
			} ],
			"initComplete": function(settings, json) {
				circleProgress();
			  }
		});
	});
	
	function generatePercentageHtml(data) {
		var html = "<div class='circle_percent' data-percent='" + data.attendance_percentage
		+ "'><div class='circle_inner'><div class='round_per'></div></div></div>"
		return html;
	}

	function viewButton(data) {
		var btn = '<a href=\"'+ data.viewUrl+ '\">VIEW</a>';
		return btn;
	}
</script>