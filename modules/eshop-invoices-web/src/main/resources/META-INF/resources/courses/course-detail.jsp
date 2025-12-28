<%@page import="javax.ws.rs.sse.InboundSseEvent"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="javax.portlet.PortletSession"%>
<%@page import="java.util.ArrayList"%>
<%@page import="web.ntuc.eshop.invoice.dto.InvoiceDto"%>
<%@page import="java.util.List"%>
<%@ include file="../init.jsp"%>

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
						<div class="name" id="capital-name">ORDER HISTORY DETAIL</div>
						<div class="email">Your order history detail</div>
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
							href="/account/examinvoices" class="text-dark">Merchandize Order
								History</a></li> -->
					</ul>
				</div>
			</div>
			<div class="col-md-9">
				<!--Start content-->
				<div class="content active" id="course-order-history">
					<div class="text-link">
						<a href="/account/courseinvoices"><i
						class="fas fa-angle-left"></i> Back To Courses</a>&nbsp;
					</div>
					<div class="box-nav-content">
						<div class="box-nav-dp">
							<div class="btn-list list-download">
								<i class="fas fa-download"></i> <a href="${invoice.invoiceURL}" target="_blank">Download
									PDF</a>
							</div>
							<!-- <div class="btn-list list-print">
								<i class="fas fa-print"></i> <a
									onclick="javascript:printDiv('print-area');">Print</a>
							</div> -->
						</div>
					</div>
					<c:choose>
						<c:when test="${accType == 1}">
					<div class="heading-content">REGISTRATION ID :
						${invoice.invoiceNo}</div>
						</c:when>
						<c:otherwise>
					<div class="heading-content">REGISTRATION ID :
						${invoice.bano}</div>
						</c:otherwise>
					</c:choose>
					<div class="box-info" id="print-area">
						<div class="heading-content">COURSE ORDER DETAIL</div>
						<table id="<portlet:namespace/>course-order"
							class="table cxrus-table dataTable no-footer"
							style="width: 100%">
							<thead>
								<tr>
									<th id="course_title">Course Title</th>
									<th id="course_code">Course Code</th>
									<th id="batch_id">Batch ID</th>
									<th id="course_start_date">Start Date</th>
									<th id="amount">Amount (SGD)</th>
									<th id="invoice_no">Invoice Number</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>${invoice.courseTitle}</td>
									<td>${invoice.courseCode}</td>
									<td>${invoice.batchId}</td>
									<td>${invoice.courseStartDate}</td>
									<td>${invoice.amount}</td>
									<c:choose>
										<c:when test="${accType == 1}">
											<td>${invoice.invoiceNo}</td>
										</c:when>
										<c:otherwise>
											<td>${invoice.bano}</td>
										</c:otherwise>
									</c:choose>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<!--End content-->
			</div>
		</div>
	</div>
</section>
<iframe name="print_frame" src="about:blank"
	style="border: 0px; display: none"></iframe>
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
</textarea>
<script type="text/javascript">

	var imgStr = "";
	$(document).ready(function() {
		getImgStr();
	});
	
	function getImgStr(){
		var src = "<%=request.getContextPath()%>/img/ntuclhub-logo.png";
		console.log("img: " + src);
		var img = new Image();
		img.crossOrigin = 'Anonymous';
		img.onload = function() {
			var canvas = document.createElement('CANVAS');
			var ctx = canvas.getContext('2d');
			var dataURL;
			canvas.height = this.naturalHeight;
			canvas.width = this.naturalWidth;
			ctx.drawImage(this, 0, 0);
			dataURL = canvas.toDataURL();
			imgStr = dataURL;
		};
		img.src = src;
		if (img.complete || img.complete === undefined) {
			img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
			img.src = src;
		}
	}

	function downloadPdf() {
		var amount = "${invoice.amount}";
		var courseTitle = "${invoice.courseTitle}";
		var fullName = "${fullName}";
		var paymentMethod = "${invoice.paymentMethod}";
		var status = "${invoice.status}";
		if("${accType}" == 1){
			var invoiceNo = "${invoice.invoiceNo}";			
		}else{
			var invoiceNo = "${invoice.bano}";			
		}
		var purchaseDate = "${invoice.purchaseDate}";
		var taxPrice = amount/0.7;
		var totalPrice = amount + taxPrice;
		
		var rows = [];
		rows.push(['Item', 'SKU', 'Net Price', 'Quantity', 'Total']);
		rows.push([courseTitle, 'SKU', amount, '1', amount]);

		var fullName = "";
		try {
			var dd = {
				pageMargins: [
					15, 15, 15, 15
				],
				footer : {
					columns : [ {
						text : 'NTUC Learning Hub',
						alignment : 'center'
					} ]
				},
				content : [
						{
							columns : [
									{
										// auto-sized columns have their widths based on their content
										width : 150,
										image : imgStr
									// image: 'sampleImage.jpg'
									},
									{
										// % width
										width : 300,
										text : [
											'Tax Invoice/Receipt\n\n',
											'GST Reg No.: 20-0409359-E \n\n',
											'NTUC LEARNINGHUB PTE LTD\n',
											'73 BRAS BASAH ROAD\n',
											'#02-01 NTUC TRADE UNION HOUSE\n',
											'SINGAPORE 189556\n',
											'FAX 65 64867824\nwww.ntuclearninghub.com\n',
											'Company Registration Number: 200409359E'
										] 
									} ],
							// optional space between columns
							columnGap : 130
						},
						'\n\n',
						{
							columns : [
									{
										// auto-sized columns have their widths based on their content
										width : 150,
										text : 'Bill-To:\n' + '${fullName}' + '\n001 Bedok Reservoir Rd\n#01-1110\nSINGAPORE 470719\nREP. OF SINGAPORE\n+65124121255'
									},
									{
										// % width
										width : 300,
										text : '\nInvoice No. : ' + invoiceNo + '\nInvoice Date : ' + purchaseDate + '\n Payment Method : ' + paymentMethod + '\nTerm : ' + status
									} ],
							// optional space between columns
							columnGap : 130
						},
						'\n\n\n',
						'Attn: Catherine Tan',
						'\n\n',
						{
							style : 'tableExample',
							table : {
								widths : [ 200, '*', 100, '*', '*' ],
								body : rows
							}
						},
						// {
						//     style: 'courseTable',
						//     table: {
						//         headerRows: 1,
						//         body: ['Item', 'SKU', 'Net Price', 'Quantity', 'Total'],

						//     },
						//     layout: 'headerLineOnly'
						// },
						'\n\nFull name per ID (NRIC/FIN/Passport):',
						'${fullName}',
						'\n\n\n',
						{
							columns : [
									{
										// auto-sized columns have their widths based on their content
										width : 150,
										text : '7% GST on SGD ' + amount + '\n(GST calculation is based on full course fee and services renderer)'
									},
									{
										// % width
										width : 220,
										text : '\nSubtotal SGD ' + amount + '\n\nGST (7%) SGD ' + taxPrice + '\nAmount Payable SGD ' + totalPrice,
										style : 'rightalign',
									} ],
							// optional space between columns
							columnGap : 130
						}, ],
				styles : {
					rightalign : {
						alignment : 'right'
					}
				}
			};
			var today = new Date();
			var date = today.getFullYear() + '' + (today.getMonth() + 1) + ''
					+ today.getDate();
				var fileName = invoiceNo + '-' + date;
			console.log(fileName);
			pdfMake.createPdf(dd).download(fileName);
		} catch (err) {
			alert(err.message);
		}
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
</script>