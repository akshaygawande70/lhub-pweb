<%@page
	import="web.ntuc.eshop.registeredcourse.constants.MVCCommandNames"%>
<%@ include file="init.jsp"%>

<portlet:resourceURL
	id="<%=MVCCommandNames.REGISTERED_COURSE_RESOURCE%>"
	var="registeredCourseURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="accType" value="${accType}" />
	<portlet:param name="nric" value="${nric}" />
	<portlet:param name="birthDate" value="${birthDate}" />
	<portlet:param name="companyCode" value="${companyCode}" />
	<portlet:param name="companyName" value="${companyName}" />
	<portlet:param name="uenNumber" value="${uenNumber}" />
</portlet:resourceURL>

<style>

.dataTables_filter, .dataTables_info { display: none; }
</style>

<section class="general-info myaccount-cxrus">
	<%@include file="layout/header.jsp"%>
	<div class="container">
		<div class="row">
			<c:choose>
				<c:when test="${accType == 1}">
					<div class="box-name">
						<div class="name" id="capital-name">COURSES</div>
						<div class="email">Your list of registered courses.</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="box-name">
						<div class="name" id="capital-name">${companyName}</div>
					</div>
				</c:otherwise>
			</c:choose>
			<hr class="hr-bold" />
		</div>
	</div>
	<div class="container">
		<div class="row">
			<%@include file="layout/side-menu.jsp"%>
			<div class="col-md-9">
				<!--Start content-->
				<div class="content active" id="register">
					<table id="<portlet:namespace/>registered-courses"
						class="table cxrus-table dataTable no-footer"
						style="width: 100%">
						<thead>
							<tr>
								<th id="course-title">Course Title</th>
								<th id="course-id">Course ID</th>
								<th id="batch-id">Batch ID</th>
								<c:choose>
									<c:when test="${accType == 2}">
										<th>Sub Booking ID</th>
									</c:when>
									<c:otherwise>
										<th id="start-date">Start Date</th>
									</c:otherwise>
								</c:choose>
								<th>Attendance</th>
								<th>Exam Results</th>
								<c:if test="${accType == 2}">
									<th>Course Evaluation</th>
								</c:if>

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
		var courseTblId = "#<portlet:namespace/>registered-courses";
		if(${accType} == 1){
			var courseDataTable = $(courseTblId).dataTable({
				"sAjaxSource" : '${registeredCourseURL}',
				"bFilter" : false,
				"sPaginationType" : "full_numbers",
				"bPaginate" : true,
				"bServerSide" : true,
				"aLengthMenu" : [ 10, 25, 50, 100 ],
				"responsive" : true,
				"bAutowidth" : false,
				"bLengthChange" : false,
				"bInfo" : false,
				"aoColumnDefs" : [ {
					"mDataProp" : "course_title",
					"bSortable" : false,
					"aTargets" : [ 0 ]
				}, {
					"mDataProp" : "course_code",
					"bSortable" : false,
					"aTargets" : [ 1 ]
				}, {
					"mDataProp" : "batch_id",
					"bSortable" : false,
					"aTargets" : [ 2 ]
				}, {
					"mDataProp" : "course_start_date",
					"bSortable" : false,
					"aTargets" : [ 3 ]
				}, {
					"aTargets" : [ 4 ],
					"bSortable" : false,
					"mData" : null,
					"mRender" : function(data, type, full) {
						return attendanceButton(data);
					}
				}, {
					"aTargets" : [ 5 ],
					"bSortable" : false,
					"mData" : null,
					"mRender" : function(data, type, full) {
						return examResultButton(data);
					}
				} ],
				"language" : {
					"emptyTable" : "<%=ContentConstants.NO_DATA_AVAILABLE%>"
				}
			});
		}else{
			var courseDataTable = $(courseTblId).dataTable({
				"sAjaxSource" : '${registeredCourseURL}',
				"sPaginationType" : "full_numbers",
				"bPaginate" : true,
				"bServerSide" : true,
				"aLengthMenu" : [ 10, 25, 50, 100 ],
				"responsive" : true,
				"bAutowidth" : false,
				"bLengthChange" : false,
				"bInfo" : true,
				"aoColumnDefs" : [ {
					"mDataProp" : "course_title",
					"bSortable" : false,
					"aTargets" : [ 0 ]
				}, {
					"mDataProp" : "course_code",
					"bSortable" : false,
					"aTargets" : [ 1 ]
				}, {
					"mDataProp" : "batch_id",
					"bSortable" : false,
					"aTargets" : [ 2 ]
				}, {
					"mDataProp" : "subbooking_id",
					"bSortable" : false,
					"aTargets" : [ 3 ]
				}, {
					"aTargets" : [ 4 ],
					"bSortable" : false,
					"mData" : null,
					"mRender" : function(data, type, full) {
						return attendanceButton(data);
					}
				}, {
					"aTargets" : [ 5 ],
					"bSortable" : false,
					"mData" : null,
					"mRender" : function(data, type, full) {
						return examResultButton(data);
					}
				}, {
					"aTargets" : [ 6 ],
					"bSortable" : false,
					"mData" : null,
					"mRender" : function(data, type, full) {
						return evaluationButton(data);
					}
				} ],
				"language" : {
					"emptyTable" : "<%=ContentConstants.NO_DATA_AVAILABLE%>"
				}
			});
		}

	});

	function attendanceButton(data) {
		var btn = '<a href=\"'+ data.attendanceUrl+ '\">VIEW ATTENDANCE</a>';
		return btn;
	}

	function examResultButton(data) {
		var btn = '<a href=\"'+ data.examUrl+ '\">VIEW RESULTS</a>';
		return btn;
	}
	
	function evaluationButton(data) {
		var btn = '<a href=\"'+ data.evaluationUrl+ '\">VIEW EVALUATION</a>';
		return btn;
	}
</script>