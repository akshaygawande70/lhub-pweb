<%@ include file="../init.jsp"%>

<portlet:resourceURL id="<%=MVCCommandNames.EXAM_MERCHANDIZE_ORDER_RESOURCE%>"
	var="examMerchandizeOrderURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="accountId" value="${accountId}" />
	<portlet:param name="accType" value="${accType}" />
	<portlet:param name="companyName" value="${companyName}" />
	
</portlet:resourceURL>

<section class="general-info myaccount-cxrus">
	<div class="container">
		<div class="row">
			<div class="breadcrumb">
				<ul>
					<li><a href="/home" class="text-dark">Home</a></li>
					<li><a href="/account/examinvoices" class="text-dark">Invoices</a></li>
					<li>Exam Order History</li>
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
						<li class="list-item" data-id="#course-order-history"><a
							href="/account/courseinvoices" class="text-dark">Course Order
								History</a></li>

						<li class="list-item active" data-id="#exam-order-history"><a
							href="/account/examinvoices">Exam Order History</a></li>
							
						<!-- <li class="list-item" data-id="#merchandize-order-history"><a
							href="/account/merchandizeinvoices" class="text-dark">Merchandize Order History</a></li>
							
						<li class="list-item active" data-id="#exam-merchandize-order-history"><a
							href="/account/exammerchandizeinvoices">Exam & Merchandize Order History</a></li> -->
					</ul>
				</div>
			</div>
			<div class="col-md-9">
				<!--Start content-->
				<div class="content active" id="#exam-order-history">
					<div class="box-info">
						<div class="heading-content">Exam Order History</div>
						<table id="<portlet:namespace/>order-list"
							class="table cxrus-table dataTable no-footer"
							style="width: 100%">
							<thead>
								<tr>
									<th id="order_no">Transaction ID</th>
									<th id="amount">Amount</th>
									<th id="order_date">Order Date</th>
									<th id="order_status">Order Status</th>
									<th id="payment_status">Payment Status</th>
									<th id="detail">Detail</th>
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
		var tempTblId = "#<portlet:namespace/>order-list";
		var table = $(tempTblId).DataTable({
			"sAjaxSource" : '${examMerchandizeOrderURL}',
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
				"mDataProp" : "order_id",
				"bSortable" : false,
				"aTargets" : [ 0 ]
			}, {
				"mDataProp" : "amount",
				"bSortable" : false,
				"aTargets" : [ 1 ]
			}, {
				"mDataProp" : "order_date",
				"bSortable" : false,
				"aTargets" : [ 2 ]
			}, {
				"mDataProp" : "order_status",
				"bSortable" : false,
				"aTargets" : [ 3 ]
			}, {
				"mDataProp" : "payment_status",
				"bSortable" : false,
				"aTargets" : [ 4 ]
			},{
				"aTargets" : [ 5 ],
				"bSortable" : false,
				"mData" : null,
				"mRender" : function(data, type, full) {
					return detailButton(data);
				}
			} ],
			"language" : {
				"emptyTable" : "<%=ContentConstants.NO_DATA_AVAILABLE%>"
			}
		});
	});

	function detailButton(data) {
		var btn = '<a href=\"'+ data.detailUrl + '\">VIEW ORDER</a>';
		return btn;
	}
</script>