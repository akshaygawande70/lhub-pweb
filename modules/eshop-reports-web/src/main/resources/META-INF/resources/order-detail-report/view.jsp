<%@ include file="../init.jsp"%>

<portlet:resourceURL
	id="<%=MVCCommandNames.ORDER_DETAIL_RESOURCE%>" var="orderDetailReportURL"
>
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<portlet:resourceURL id="<%=MVCCommandNames.EXPORT_ORDER_DETAIL_RESOURCE%>"
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
			<div class="heading-content">ORDER DETAIL REPORT</div>
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
		
		<div class="row" style="padding-top: 10px">
             <div class="col-md-2">
                 <div class="form-group">
                     <div class="title" for="<portlet:namespace/>orderId">Order No.</div>
                     <input type="text" name="orderId" id="<portlet:namespace/>orderId" class="form-control">
                 </div>
             </div>
                <div class="col-md-4">
                    <div class="form-group">
                    	<div class="title" for="<portlet:namespace/>productName">Product Name </div>
                        <input type="text" name="productName" id="<portlet:namespace/>productName" class="form-control">
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
                    <table id="<portlet:namespace/>report-table" class="table dataTable no-footer table-bordered w-100">
                        <thead>
                        <tr>
                            <th style="width: 72px !important">Order Created Date</th>
                            <th>Order No</th>
                            <th>Stripe Id</th>
                            <th>Customer Email</th>
                            <th>Customer Name</th>
                            <th style="white-space: nowrap;">Order Status</th>
                            <th>Product Name</th>
                            <th>SKU</th>
                            <th>Qty</th>
                            <th>Unit Price</th>
                            <th>Discount</th>
                            <th>Net Price</th>
                            <th>GST</th>
                            <th>Total</th>
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
	vertical-align:baseline
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
<script>
    $(document).ready(function(){
    	
        /*let $fromDateEl = $('#<portlet:namespace/>fromDate')
        let $toDateEl = $('#<portlet:namespace/>toDate')
        const datepickerOption = {
            format: 'yyyy-mm-dd',
            autoclose: true
        }*/

        //$fromDateEl.add($toDateEl).datepicker(datepickerOption)
        /*$fromDateEl.datepicker(datepickerOption).on('change', function() {
            if($toDateEl.val() !== '') reportTbl.fnPageChange('first')
        })

        $toDateEl.datepicker(datepickerOption).on('change', function() {
            if($fromDateEl.val() !== '') reportTbl.fnPageChange('first')
        })*/

        $("#<portlet:namespace/>btnFilter").on('click', function (e){
            e.preventDefault()
            reportTbl.fnPageChange('first')
            console.log(e.preventDefault());
        })

        let reportTbl = $('#<portlet:namespace/>report-table').dataTable({
            'deferRender': true,
            'pageLength': 10,
            'bFilter': false,
            'responsive': true,
            'bServerSide': true,
            'bLengthChange': false,
            'bInfo': true,
            'order': [[0, 'desc']],
            'sAjaxSource': '${orderDetailReportURL}',
            'fnServerParams': function (aoData) {
                aoData.push({
                    'name': '<portlet:namespace/>fromDate',
                    'value':  $('#<portlet:namespace/>fromDate').val()
                }, {
                    'name': '<portlet:namespace/>toDate',
                    'value': $('#<portlet:namespace/>toDate').val()
                }, {
                    'name': '<portlet:namespace/>orderId',
                    'value': $('#<portlet:namespace/>orderId').val()
                }, {
                    'name': '<portlet:namespace/>status',
                    'value': $('#<portlet:namespace/>status').val()
                }, {
                    'name': '<portlet:namespace/>productName',
                    'value': $('#<portlet:namespace/>productName').val()
                }
                )
            },
            'columnDefs': [{
                'targets': 0,
                'orderable': true,
                "className": 'no-wrap',
                'data': 'orderCreatedDate',
            }, {
                'targets': 1,
                'orderable': false,
                'data': 'orderId',
            }, {
				"aTargets" : [ 2 ],
				"bSortable" : false,
				"mData" : null,
				"mRender" : function(data, type, full) {
					return stripeIdButton(data);
				}
            }, 
            {
                'targets': 3,
                'orderable': false,
                'data': 'custEmail',
                "className": 'text-wrap',
            }, {
                'targets': 4,
                'orderable': false,
                'data': 'custName',
            }, {
                'targets': 5,
                'orderable': false,
                'data': 'status',
            }, {
                'targets': 6,
                'orderable': false,
                'data': 'productName',
            }, {
                'targets': 7,
                'orderable': false,
                'data': 'sku',
            }, {
                'targets': 8,
                'orderable': false,
                'data': 'quantity',
            },
            {
                'targets': 9,
                'orderable': false,
                'data': 'unitPrice',
            },
            {
                'targets': 10,
                'orderable': false,
                'data': 'discount',
            },
            {
                'targets': 11,
                'orderable': false,
                'data': 'basePrice',
            },
            {
                'targets': 12,
                'orderable': false,
                'data': 'gst',
            },{
                'targets': 13,
                'orderable': false,
                'data': 'total',
            }],
            "language" : {
				"emptyTable" : "<%=ContentConstants.NO_DATA_AVAILABLE%>"
			}
        });

    });
    
    function donwloadCsv() {
		var exportUrl = "${exportCsvUrl}";
		exportUrl += '&<portlet:namespace/>fromDate='+$('#<portlet:namespace/>fromDate').val();
		exportUrl += '&<portlet:namespace/>toDate='+$('#<portlet:namespace/>toDate').val();
		exportUrl += '&<portlet:namespace/>orderId='+$('#<portlet:namespace/>orderId').val();
		exportUrl += '&<portlet:namespace/>status='+$('#<portlet:namespace/>status').val();
		exportUrl += '&<portlet:namespace/>productName='+$('#<portlet:namespace/>productName').val();
		exportUrl += '&<portlet:namespace/>sku='+$('#<portlet:namespace/>sku').val();
		exportUrl += '&<portlet:namespace/>iSortCol_0='+$('#<portlet:namespace/>report-table').dataTable().fnSettings().aaSorting[0][0];
		exportUrl += '&<portlet:namespace/>sSortDir_0='+$('#<portlet:namespace/>report-table').dataTable().fnSettings().aaSorting[0][1];
		window.open(exportUrl,'_blank');
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
