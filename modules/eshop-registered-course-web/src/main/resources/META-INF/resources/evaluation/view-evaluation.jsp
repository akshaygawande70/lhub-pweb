<%@page
	import="web.ntuc.eshop.registeredcourse.constants.MVCCommandNames"%>
<%@ include file="../init.jsp"%>

<portlet:resourceURL
	id="<%=MVCCommandNames.COURSE_EVALUATION_RESOURCE%>"
	var="evaluationURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="accType" value="${accType}" />
	<portlet:param name="companyCode" value="${companyCode}" />
	<portlet:param name="companyName" value="${companyName}" />
	<portlet:param name="uenNumber" value="${uenNumber}" />
	<portlet:param name="batchId" value="${batchId}" />
	<portlet:param name="courseCode" value="${courseCode}" />

</portlet:resourceURL>

<section class="general-info myaccount-cxrus">
	<%@include file="/layout/header.jsp"%>
	<div class="container">
		<div class="row">
			<div class="box-name">
				<div class="name" id="capital-name">${companyName}</div>
			</div>
			<hr class="hr-bold" />
		</div>
	</div>
	<div class="container">
		<div class="row">
			<%@include file="/layout/side-menu.jsp"%>
			<div class="col-md-9">
				<!--Start content-->
				<div class="content active" id="pi">
					<div class="box-nav-content">
						<div class="text-link">
							<a href="/account/course_listing"><i
								class="fas fa-angle-left"></i> Back To Courses</a>&nbsp;
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="box-info">
								<div class="row">
									<div class="col-md-6">
										<div class="box-table">
											<div class="title">Course Title</div>
											<div class="isi">${courseTitle}</div>
										</div>
										<div class="box-table">
											<div class="title">Batch ID</div>
											<div class="isi">${batchId}</div>
										</div>
										<div class="box-table">
											<div class="title">Sub Booking ID</div>
											<div class="isi">${subBookingId}</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="box-table">
											<div class="title">Course Start Date & Time</div>
											<div class="isi">${startDate}</div>
										</div>
										<div class="box-table">
											<div class="title">Course End Date & Time</div>
											<div class="isi">${endDate}</div>
										</div>
										<div class="box-table">
											<div class="title">Total No of Trainees</div>
											<div class="isi">${totalTrainee}</div>
										</div>

									</div>
								</div>
							</div>
							<table id="<portlet:namespace/>course-evaluation"
								class="CX-datatable dataTable table table-bordered"
								style="width: 100%">
								<thead>
									<tr>
										<th id="customer-consent">Customer's Consent</th>
										<th id="facilities">Facilities</th>
										<th id="overall-programme">Overall Programme</th>
										<th id="programme-material">Programme Material</th>
										<th id="trainer-effectiveness">Trainer Effectiveness</th>
										<th id="training-programme">Training Programme</th>
										<th id="average">Average</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
				<!--End content-->
			</div>
		</div>
	</div>
</section>

<script type="text/javascript">
	$(document).ready(function() {
		var courseTblId = "#<portlet:namespace/>course-evaluation";
		var courseDataTable = $(courseTblId).dataTable({
			"language" : {
				"emptyTable" : "<%=ContentConstants.NO_DATA_AVAILABLE%>"
			},
			"bFilter" : false,
			"responsive" : true,
			"bServerSide" : true,
			"bAutowidth" : true,
			"bProcessing" : true,
			"bLengthChange" : false,
			"bInfo" : false,
			"sAjaxSource" : '${evaluationURL}',
			"aoColumnDefs" : [ {
				"mDataProp" : "customer_consent",
				"bSortable" : false,
				"aTargets" : [ 0 ]
			}, {
				"mDataProp" : "facilities",
				"bSortable" : false,
				"aTargets" : [ 1 ]
			}, {
				"mDataProp" : "overall_programme",
				"bSortable" : false,
				"aTargets" : [ 2 ]
			}, {
				"mDataProp" : "programme_material",
				"bSortable" : false,
				"aTargets" : [ 3 ]
			}, {
				"mDataProp" : "trainer_effectiveness",
				"bSortable" : false,
				"aTargets" : [ 4 ]
			}, {
				"mDataProp" : "training_programme",
				"bSortable" : false,
				"aTargets" : [ 5 ]
			}, {
				"mDataProp" : "average",
				"bSortable" : false,
				"aTargets" : [ 6 ]
			} ]
		});
	});
</script>