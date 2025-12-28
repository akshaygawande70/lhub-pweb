<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="web.ntuc.nlh.course.admin.constants.MVCCommandNames"%>
<%@ include file="/init.jsp" %>

<portlet:resourceURL id="<%=MVCCommandNames.COURSE_LIST_RESOURCES%>"
	var="dataResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<portlet:resourceURL id="<%=MVCCommandNames.EXPORT_RESOURCE%>"  var="exportCsvUrl" >
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<section class="section-main data-list">
	<div class="container-fluid">
		<div class="row header_action">
			<div class="col-lg-12">
				<div class="mb-2 mt-3 clearfix">
					<div class="float-sm-right text-zero">
						<!-- <button type="button"
							class="btn btn-danger btn-lg top-right-button mr-1">
							Delete <span id="del_sel"></span>
						</button> -->
						<a href="${exportCsvUrl}"
							class="btn btn-success btn-lg top-right-button mr-1">Export</a>
					</div>
				</div>

				<div class="separator mb-5"></div>
			</div>
		</div>
		<div class="displayContentTable">
			<table
				id="courseList" class="dataTable table table-striped table-bordered" aria-label="Courses Admin">
				<thead>
					<tr role="row">
						<th id="course-code"><liferay-ui:message key="course-code" /></th>
						<th id="course-title"><liferay-ui:message key="course-title" /></th>
						<th id="course-status"><liferay-ui:message key="course-status" /></th>
						<th id="course-fee"><liferay-ui:message key="course-fee" /></th>
						<th id="funded-course-flag"><liferay-ui:message key="funded-course-flag" /></th>
						<th id="popular"><liferay-ui:message key="popular" /></th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</section>
<script type="text/javascript">
	$(document).ready(function() {
		var table = $('#courseList')
		.DataTable(
				{

					"initComplete" : function(settings,
							json) {
						/*console.log("finished");*/
					},
					"sAjaxSource" : '${dataResourceURL}',
					"bFilter" : true,
					"responsive" : true,
					"bServerSide" : true,
					"bAutowidth" : false,
					"bLengthChange" : false,
					"bInfo" : true,
					"bDestroy": true,
					'columnDefs' : [
							
							{
								"mDataProp" : "courseCode",
								"bSortable" : true,
								"aTargets" : [ 0 ]
							},
							{
								"mDataProp" : "courseTitle",
								"bSortable" : true,
								"aTargets" : [ 1 ]
							},
							{
								"mDataProp" : "courseStatus",
								"bSortable" : true,
								"aTargets" : [ 2 ]
							},
							{
								"mDataProp" : "courseFee",
								"bSortable" : true,
								"aTargets" : [ 3 ]
							},
							{
								"mDataProp" : "fundedCourseFlag",
								"bSortable" : true,
								"aTargets" : [ 4 ]
							},
							{
								"mDataProp" : "popular",
								"bSortable" : true,
								"aTargets" : [ 5 ]
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
	});
	
	/*function loadDataList(url) {
		return jQuery.ajax({
			url : url,
			type : "GET",
			dataType : "json",
			async : "false",
			cache : "false",
			beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
                $('#courses-result .loader').removeClass('hidden')
    		},
			success : function(data,textStatus,XMLHttpRequest) {
				var htmlFinal = generateCourseResult(data.courses);
				$('#courses-result').html(htmlFinal);
				console.log(data);
			},
			complete: function () { // Set our complete callback, adding the .hidden class and hiding the spinner.
            	$('#courses-result .loader').hide();
            }
		});
	}
	
	function generateCourseResult(data) {
		var html = '<span>List Courses ('+data.length+')</span><br>';
		data.forEach(function(item, idx){
			var no = idx + 1;
			html += '<span>- '+no+'^ '+item.courseCode+' ^ '+item.title+' ^ '+item.status+' ^ '+item.price+' ^ '+item.funded+' ^ '+item.popular+'</span> <br>'
		});
		return html;
	}*/
</script>