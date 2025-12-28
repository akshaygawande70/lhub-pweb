<%@page
	import="web.ntuc.eshop.registeredcourse.constants.MVCCommandNames"%>
<%@ include file="../init.jsp"%>

<portlet:resourceURL
	id="<%=MVCCommandNames.COURSE_EXAM_RESULT_RESOURCE%>"
	var="examResultURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="accType" value="${accType}" />
	<portlet:param name="nric" value="${nric}" />
	<portlet:param name="birthDate" value="${birthDate}" />
	<portlet:param name="companyCode" value="${companyCode}" />
	<portlet:param name="companyName" value="${companyName}" />
	<portlet:param name="uenNumber" value="${uenNumber}" />
	<portlet:param name="batchId" value="${batchId}" />
</portlet:resourceURL>

<section class="general-info myaccount-cxrus">
	<%@include file="/layout/header.jsp"%>
	<div class="container">
		<div class="row">
			<div class="box-name">
				<div class="name" id="capital-name">EXAM RESULTS</div>
				<div class="email">Exam results for all courses.</div>
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
					<table id="<portlet:namespace/>examResult"
						class="table cxrus-table dataTable no-footer"
						style="width: 100%">
						<thead>
							<tr>
								<th>Exam Name</th>
								<th>Exam Date</th>
								<th>Exam Results</th>
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
		var examTblId = "#<portlet:namespace/>examResult";
		var examDataTable = $(examTblId).dataTable({
			"bFilter" : false,
			"responsive" : true,
			"bServerSide" : true,
			"bAutowidth" : true,
			"bLengthChange" : false,
			"bInfo" : false,
			"className" : 'select-checkbox',
			"language" : {
				"emptyTable" : "<%=ContentConstants.NO_DATA_AVAILABLE%>"
			},
			"sAjaxSource" : '${examResultURL}',
			"aoColumnDefs" : [ {
				"mDataProp" : "exam_name",
				"bSortable" : false,
				"aTargets" : [ 0 ]
			}, {
				"mDataProp" : "exam_date",
				"bSortable" : false,
				"aTargets" : [ 1 ]
			}, {
				"mDataProp" : "exam_result",
				"bSortable" : false,
				"aTargets" : [ 2 ]
			}, {
				"aTargets" : [ 3 ],
				"bSortable" : false,
				"mData" : null,
				"mRender" : function(data, type, full) {
					return viewButton(data);
				}
			} ]
		});
	});

	function viewButton(data) {
		var btn = '<a href=\"'+ data.viewUrl+ '\">VIEW</a>';
		return btn;
	}
</script>