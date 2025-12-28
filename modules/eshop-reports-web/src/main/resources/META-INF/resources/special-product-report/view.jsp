<%@ include file="../init.jsp" %>
<portlet:resourceURL
	id="<%=MVCCommandNames.SPECIAL_PRODUCT_RESOURCE%>" var="specialProductReportURL"
>
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>
<portlet:resourceURL id="<%=MVCCommandNames.EXPORT_SPECIAL_PRODUCT_RESOURCE%>"
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
				<div class="heading-content">SPECIAL PRODUCT REPORT</div>
			</div>
		</div>
		<div class="row">
		 <div class="col-md-12 mt-3 clearfix">
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
                            <th>Name</th>
                            <th>CITREP status</th>
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
    $(document).ready(function () {
    	$("#<portlet:namespace/>btnFilter").click(function() {
			console.log("Success");
			oTable.draw();
			//oTable.fnPageChange('first');
			return false;
		});

        let reportTbl = $('#<portlet:namespace/>report-table').dataTable({
            'deferRender': true,
            'pageLength': 10,
            'bFilter': false,
            'responsive': true,
            'bServerSide': true,
            'bLengthChange': false,
            'bInfo': true,
            'order': [[0, 'desc']],
            'sAjaxSource': '${specialProductReportURL}',
           
            "aoColumnDefs" : [ {
				"mDataProp" : "productName",
				"bSortable" : true,
				"aTargets" : [ 0 ]
			}, {
				"mDataProp" : "citrepStatus",
				"bSortable" : true,
				"aTargets" : [ 1 ]
            },],
            "language" : {
				"emptyTable" : "<%=ContentConstants.NO_DATA_AVAILABLE%>"
			}
        });

    });
    
    function donwloadCsv() {
		var exportUrl = "${exportCsvUrl}";
		exportUrl += '&<portlet:namespace/>fromDate='+$('#<portlet:namespace/>fromDate').val();
		exportUrl += '&<portlet:namespace/>toDate='+$('#<portlet:namespace/>toDate').val();
		exportUrl += '&<portlet:namespace/>status='+$('#<portlet:namespace/>status').val();
		exportUrl += '&<portlet:namespace/>iSortCol_0='+$('#<portlet:namespace/>report-table').dataTable().fnSettings().aaSorting[0][0];
		exportUrl += '&<portlet:namespace/>sSortDir_0='+$('#<portlet:namespace/>report-table').dataTable().fnSettings().aaSorting[0][1];
		window.open(exportUrl,'_blank');
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