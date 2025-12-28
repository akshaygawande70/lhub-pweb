<%@page import="web.ntuc.eshop.reports.constants.MVCCommandNames"%>
<%@ include file="init.jsp"%>
<portlet:resourceURL
	id="<%=MVCCommandNames.SALES_AND_SUMMARY_RESOURCE%>" var="reportURL"
>
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>
<portlet:resourceURL id="<%=MVCCommandNames.EXPORT_RESOURCE%>"
	var="exportCsvUrl"
>
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>
<%
	String pageUrl = themeDisplay.getURLCurrent();
%>
<section class="general-info reporting-cxrus">
	<div class="container">
		<div class="row">
			<div class="col-md-9">
				<div class="heading-content">SALES DETAILS AND SUMMARY REPORT</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-2">
				<div class="title">From Date</div>
				<div class="date-wrap date-report">
					<input name="fromDate" id="<portlet:namespace/>fromDate"
						type="text" value="" class="form-control nogetdate"
					> <em class="fas fa-calendar-alt"></em>
				</div>
			</div>
			<div class="col-md-2">
				<div class="title">To Date</div>
				<div class="date-wrap date-report">
					<input name="toDate" id="<portlet:namespace/>toDate" type="text"
						value="" class="form-control nogetdate"
					> <em class="fas fa-calendar-alt"></em>
				</div>
			</div>
			<div class="col-md-2">
				<div class="title">Order Status</div>
				<select class="form-control" id="<portlet:namespace/>status">
					<c:forEach var="status" items="${statuses}">
						<option value="${status.key}">${status.value}</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-6 mt-3 clearfix">
				<div class="float-sm-right text-zero">
					<btn onClick="donwloadCsv()"
						class="btn btn-primary btn-blue top-right-button"
					>Export</btn>
				</div>
				<div class="float-sm-right text-zero">
					<a class="btn btn-primary btn-blue"
						onclick="javascript:printDiv('print-area');"
					>Print</a>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12 mt-3 alert alert-info">Status Legends:
				Completed - Order Fulfilled | Processing - Paid Order in Process |
				Pending - Paid Order Yet to be Accepted | On Hold - Paid Order put
				On-Hold by NTUC | Cancelled - Paid Order Cancelled by NTUC</div>
		</div>
		<div class="row" style="padding-top: 20px">
			<div class="col-md-4">
				<button class="btn-primary btn-blue" type="button"
					id="<portlet:namespace/>btnFilter"
				>Generate Report</button>
				<a class="btn-white" href="<%=pageUrl%>">CLEAR </a>
			</div>
		</div>
		<div class="row" style="padding-top: 20px">
			<div class="col-md-12">
				<div class="box-info" id="print-area">
					<div class="date-picker">
						<br />
						<table id="<portlet:namespace/>report-table"
							class="table cxrus-table dataTable no-footer table-bordered"
							style="width: 100%"
						>
							<thead>
								<tr>
									<th style="width: 60px !important">Invoice Date</th>
									<th id="order_id">Invoice No</th>
									<th id="stripe_id">Stripe Id</th>
									<th id="item">Item</th>
									<th id="sku">SKU</th>
									<th id="orderStatus">Order Status</th>
									<th id="quantity">Qty</th>
									<th id="net_price">Unit Price</th>
									<th style="width: 60px !important"	>Discount</th>
									<th id="unit_price">Net Price</th>
									<th id="gst">GST</th>
									<th id="total">Total</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
<iframe name="print_frame" src="about:blank"
	style="border: 0px; display: none"
></iframe>
<textarea id="printing-css" style="display: none;">
html,body,div,span,applet,object,iframe,h1,h2,h3,h4,h5,h6,p,blockquote,pre,a,abbr,acronym,address,big,cite,code,del,dfn,em,img,ins,kbd,q,s,samp,small,strike,strong,sub,sup,tt,var,b,u,i,center,dl,dt,dd,ol,ul,li,fieldset,form,label,legend,table,caption,tbody,tfoot,thead,tr,th,td,article,aside,canvas,details,embed,figure,figcaption,footer,header,hgroup,menu,nav,output,ruby,section,summary,time,mark,audio,video{
	margin:0;
	padding:0;
	border:0;
	font-size:100%;
	font:inherit;
	vertical-align:baseline;
}
article,aside,details,figcaption,figure,footer,header,hgroup,menu,nav,section{
	display:block
}
body{
	line-height:1
}
table[border="1"] th,table[border="1"] td,table[border="1"] caption{
	border:1px solid #000;
	padding:.5em 1em;
	text-align:left;
	vertical-align:top;
	margin-top:20px;
	text-align:center;
}
th{
   padding:10px 8px;
	  text-align: center;
	  background-color: #18355f;
	  color: white;
	 vertical-align: middle;
}
td{
	 padding:10px 8px;
	 text-align: center;
	 background-color:#dcedf5;
	 vertical-align: middle;
}
.btn-1{
	background: #18355f;
	border: #fcd205 solid 2px;
	color: #fcd205 !important;
	display:block;
	padding:10px 8px;
	text-decoration:none;
	display:none;
}
.type-rs{
	display:none;
}
.payment{
		opacity:1;
	}
table[border="1"] caption{
	border:none;
	font-style:italic
}
.no-print{
	display:none
}
.pagination{
	display:none !important;
}

.dataTables_info{
	display:none !important;
}
</textarea>
<script type="text/javascript">
	$(document).ready(function() {
		console.log('${reportURL}');
		console.log($("#<portlet:namespace/>status").val());
		console.log($("#<portlet:namespace/>status"));
		
		$("#<portlet:namespace/>btnFilter").click(function() {
			console.log("Success");
			oTable.draw();
			//oTable.fnPageChange('first');
			return false;
		});

		var oTable = $('#<portlet:namespace/>report-table').DataTable({
			"sAjaxSource" : '${reportURL}',
			'deferRender': true,
			"bFilter" : false,
			'pageLength' : 10,
			"sPaginationType" : "full_numbers",
			"bPaginate" : true,
			"bServerSide" : true,
			/* "aLengthMenu" : [ 5, 25, 50, 100 ], */
			"responsive" : true,
			"bServerSide" : true,
			"bLengthChange" : false,
			"bInfo" : true,
			"bAutoWidth": false,
			"order" : [ [ 0, "desc" ] ],
			"fnServerParams" : function(aoData) {
				aoData.push({
					"name" : "<portlet:namespace/>fromDate",
					"value" : $('#<portlet:namespace/>fromDate').val()
				}, {
					"name" : "<portlet:namespace/>toDate",
					"value" : $('#<portlet:namespace/>toDate').val()
				}, {
					'name' : '<portlet:namespace/>status',
					'value' : $('#<portlet:namespace/>status').val()
				});
			},
			"aoColumnDefs" : [ {"defaultContent": "-",
                "targets": "_all"},
				
				{
				"mDataProp" : "orderDate",
				"className": 'text-nowrap',
				"bSortable" : true,
				"aTargets" : [ 0 ]
			}, {
				"mDataProp" : "orderId",
				"className": 'text-nowrap',
				"bSortable" : false,
				"aTargets" : [ 1 ]
			}, {
				"aTargets" : [ 2 ],
				"bSortable" : false,
				"mData" : null,
                "mRender" : function(data, type, full) {
					return stripeIdButton(data);
				}
			}, {
				"mDataProp" : "item",
				"bSortable" : false,
				"aTargets" : [ 3 ]
			}, {
				"mDataProp" : "sku",
				"bSortable" : false,
				"aTargets" : [ 4 ]
			}, {
				"mDataProp" : "orderStatus",
				"className": 'text-nowrap',
				"bSortable" : false,
				"aTargets" : [ 5 ]
			},{
				"mDataProp" : "quantity",
				"bSortable" : false,
				"aTargets" : [ 6 ]
			},{
				"mDataProp" : "unitPrice",
				"className": 'text-nowrap',
				"bSortable" : false,
				"aTargets" : [ 7 ]
			}, {
				"mDataProp" : "promotion",
				"bSortable" : false,
				"aTargets" : [ 8 ]
			}, {
				"mDataProp" : "netPrice",
				"className": 'text-nowrap',
				"bSortable" : false,
				"aTargets" : [ 9 ]
			}, {
				"mDataProp" : "gst",
				"bSortable" : false,
				"className": 'text-nowrap',
				"aTargets" : [ 10 ]
			}, {
				"mDataProp" : "totalPrice",
				"className": 'text-nowrap',
				"bSortable" : false,
				"aTargets" : [ 11 ]
			}],
			"language" : {
				"emptyTable" : "<%=ContentConstants.NO_DATA_AVAILABLE%>"
			}
		});
	});

	function donwloadCsv() {
		var exportUrl = "${exportCsvUrl}";
		exportUrl += '&<portlet:namespace/>fromDate='
				+ $('#<portlet:namespace/>fromDate').val();
		exportUrl += '&<portlet:namespace/>toDate='
				+ $('#<portlet:namespace/>toDate').val();
		exportUrl += '&<portlet:namespace/>status='
				+ $('#<portlet:namespace/>status').val();

		window.open(exportUrl, '_blank');
	}
	function stripeIdButton(data) {
		var btn = "";
		if (data.stripeId !== null && data.stripeId !== ""
				&& data.stripeId !== "null") {
			btn = '<a target="blank_" href=\"'+ data.stripeLink+ '\">'
					+ data.stripeId + '</a>';
		} else {
			btn = '';
		}
		return btn;
	}

	function printDiv(element) {
		var a = document.getElementById('printing-css').value;
		var b = document.getElementById(element).innerHTML;
		window.frames["print_frame"].document.body.innerHTML =

		window.frames["print_frame"].document.body.innerHTML = '<style>' + a
				+ '</style>' + b;
		window.frames["print_frame"].window.focus();
		window.frames["print_frame"].window.print();
	}

	function filterReport() {
		console.log("Done");
	}
</script>