<%@ include file="init.jsp"%>

<portlet:resourceURL id="<%=MVCCommandNames.COURSE_ORDER_RESOURCE%>"
	var="courseOrderURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="accType" value="${accType}" />
	<portlet:param name="nric" value="${nric}" />
	<portlet:param name="birthDate" value="${birthDate}" />
	<portlet:param name="companyCode" value="${companyCode}" />
	<portlet:param name="companyName" value="${companyName}" />
	<portlet:param name="uenNumber" value="${uenNumber}" />
</portlet:resourceURL>


<%
	List<InvoiceDto> invoiceList = (List<InvoiceDto>) portletSession.getAttribute("invoiceList",
			PortletSession.PORTLET_SCOPE);
%>

<section class="general-info myaccount-cxrus">
	<div class="container">
		<div class="row">
			<div class="breadcrumb">
				<ul>
					<li><a href="/home" class="text-dark">Home</a></li>
					<li><a href="/account/courseinvoices" class="text-dark">Invoices</a></li>
					<li>Course Order History</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="text-center w100">
				<h1 class="h1-commerce">INVOICES</h1>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<c:choose>
				<c:when test="${accType == 1}">
					<div class="box-name">
						<div class="name" id="capital-name">ORDER HISTORY</div>
						<div class="email">All your order history</div>
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
			<div class="col-md-3">
				<div class="heading-title" data-id="#myacc">MY ACCOUNT</div>
				<div class="isi-account" id="myacc">
					<ul>
						<li class="list-item" data-id="#pi"><a
							href="/account/profile" class="text-dark">Personal Info</a></li>
						<li class="list-item" data-id="#cp"><a
							href="/account/change_password" class="text-dark">Change
								Password</li>
					</ul>
				</div>
				<div class="heading-title" data-id="#register">
					<a href="/account/course_listing" class="text-dark">REGISTERED
						COURSE </a>
				</div>
				<div class="heading-title" data-id="#invoices">INVOICES</div>
				<div class="isi-account active" id="invoices">
					<ul>
						<li class="list-item active" data-id="#course-order-history"><a
							href="/account/courseinvoices">Course Order History</a></li>

						<li class="list-item" data-id="#exam-order-history"><a
							href="/account/examinvoices" class="text-dark">Exam Order History</a></li>
								
						<!-- <li class="list-item" data-id="#merchandize-order-history"><a
							href="/account/merchandizeinvoices" class="text-dark">Merchandize Order
								History</a></li>
						<li class="list-item" data-id="#exam-merchandize-order-history"><a
							href="/account/exammerchandizeinvoices" class="text-dark">Exam & Merchandize Order History</a></li> -->
					</ul>
				</div>
			</div>
			<div class="col-md-9">
				<!--Start content-->
				<div class="content active" id="course-order-history">
					<div class="box-info">
						<div class="heading-content">COURSES</div>
						<table id="<portlet:namespace/>course-order"
							class="table cxrus-table dataTable no-footer"
							style="width: 100%">
							<thead>
								<tr>
									<th id="course_title">Course Title</th>
									<th id="course_code">Course Code</th>
									<th id="batch_id">Batch ID</th>
									<th id="amount">Amount (SGD)</th>
									<th id="invoice_no">Invoice Number</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>

				<!--End content-->
			</div>
		</div>
	</div>
</section>
<script type="text/javascript">
	$(document).ready(function() {
		var tempTblId = "#<portlet:namespace/>course-order";
		var table = $(tempTblId).DataTable({
			"sAjaxSource" : '${courseOrderURL}',
			"bFilter" : false,
			"sPaginationType" : "full_numbers",
			"bPaginate" : true,
			"bServerSide" : true,
			"aLengthMenu" : [ 10, 25, 50, 100 ],
			"responsive" : true,
			"bAutowidth" : false,
			"bLengthChange" : false,
			"bInfo" : true,
			"columnDefs" : [ {
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
				"mDataProp" : "amount",
				"bSortable" : false,
				"aTargets" : [ 3 ]
			}, {
				"aTargets" : [ 4 ],
				"bSortable" : false,
				"mData" : null,
				"mRender" : function(data, type, full) {
					return detailUrl(data);
				}
			} ],
			"language" : {
				"emptyTable" : "<%=ContentConstants.NO_DATA_AVAILABLE%>",
			}
		});
	});

	function detailUrl(data) {
		var btn = '<a href=\"'+ data.detailUrl + '\">' + data.invoice_no
				+ '</a>';
		return btn;
	}
</script>