<%@ include file="../init.jsp"%>
<portlet:resourceURL
	id="<%=MVCCommandNames.MERCHANDIZE_ORDER_DETAIL_RESOURCE%>"
	var="merchandizeOrderDetailURL"
>
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="accountId" value="${accountId}" />
	<portlet:param name="accType" value="${accType}" />
	<portlet:param name="companyName" value="${companyName}" />
	<portlet:param name="orderId" value="${orderId}" />
	<portlet:param name="phoneNumber" value="${phoneNumber}" />
	<portlet:param name="fullName" value="${fullName}" />
	<portlet:param name="invoiceNo" value="${invoiceNo}" />
</portlet:resourceURL>
<%
	int orderId = (int) renderRequest.getAttribute("orderId");
	CommerceOrder commerceOrder = CommerceOrderLocalServiceUtil.getCommerceOrder(orderId);

	/*List<OrderDto> orderList = (List<OrderDto>) portletSession.getAttribute("orderItemList_" + orderId,
			PortletSession.PORTLET_SCOPE);*/
	List<OrderDto> orderList = (List<OrderDto>) renderRequest.getAttribute("orderItemList");
	//System.out.println("authtoken = ${authToken}");
	//System.out.println("accountId = ${accountId}");
	//System.out.println("accType = ${accType}");
	//System.out.println("companyName = ${companyName}");
	//System.out.println("orderId = ${orderId}");
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.UK);
	double commerceTaxFixedRate = CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).get(0)
			.getRate();
%>
<section class="general-info myaccount-cxrus">
	<div class="container">
		<div class="row">
			<div class="breadcrumb">
				<ul>
					<li><a href="/home" class="text-dark">Home</a></li>
					<li><a href="/account/merchandizeinvoices" class="text-dark">Invoices</a></li>
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
							href="/account/profile" class="text-dark"
						>Personal Info</a></li>
						<li class="list-item" data-id="#cp"><a
							href="/account/change_password" class="text-dark"
						>Change Password</a></li>
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
							href="/account/courseinvoices" class="text-dark"
						>Course Order History</a></li>
						<li class="list-item" data-id="#exam-order-history"><a
							href="/account/examinvoices" class="text-dark"
						>Exam Order History</a></li>
						<li class="list-item active" data-id="#merchandize-order-history"><a
							href="/account/merchandizeinvoices">Merchandize Order History</a></li>
					</ul>
				</div>
			</div>
			<div class="col-md-9">
				<!--Start content-->
				<div class="content active" id="course-order-history">
					<div class="text-link">
						<a href="/account/merchandizeinvoices"><i class="fas fa-angle-left"></i>
							Back To Merchandizes</a>&nbsp;
					</div>
					<div class="box-nav-content">
						<div class="box-nav-dp">
							<div class="btn-list list-download">
								<i class="fas fa-download"></i> <a onclick="downloadPdf()">Download
									Invoice</a>
							</div>
							<%
							if(commerceOrder.getPaymentStatus() != CommerceOrderPaymentConstants.STATUS_COMPLETED) {%>
								<%
									String paymentUrl = themeDisplay.getPortalURL();
									paymentUrl += "/checkout/-/checkout/payment-process/"+commerceOrder.getUuid();
								%>
								<div class="btn-list list-download">
									<i class="fas fa-arrow"></i> <a href="<%=paymentUrl%>">Pay Now</a>
								</div>
							<% } %>
							<!-- <div class="btn-list list-print">
								<i class="fas fa-print"></i> <a
									onclick="javascript:printDiv('print-area');">Print</a>
							</div> -->
						</div>
					</div>
					<div class="heading-content">REGISTRATION ID : ${invoiceNo}</div>
					<div class="box-info" id="print-area">
						<div class="heading-content">MERCHANDIZE ORDER DETAIL</div>
						<table id="<portlet:namespace/>merchandize-order"
							class="table cxrus-table dataTable no-footer" style="width: 100%"
						>
							<thead>
								<tr>
									<th id="merchandize_name">Merchandize Title</th>
									<th id="merchandize_code">Merchandize Code</th>
									<th id="quantity">Quantity</th>
									<th id="per_unit">Per Unit</th>
									<th id="amount">Amount (SGD)</th>
									<th id="invoice_no">Invoice Number</th>
									<th id="invoice_no">Order Status</th>
									<th id="invoice_no">Payment Status</th>
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
<script type="text/javascript">

	var imgStr = "";
	$(document).ready(function() {
		
		var tempTblId = "#<portlet:namespace/>merchandize-order";
		var table = $(tempTblId).DataTable({
			"sAjaxSource" : '${merchandizeOrderDetailURL}',
			"bFilter" : false,
			"sPaginationType" : "full_numbers",
			"bPaginate" : true,
			"bServerSide" : true,
			"aLengthMenu" : [ 5, 10, 25, 50 ],
			"responsive" : true,
			"bAutowidth" : false,
			"bLengthChange" : false,
			"bInfo" : true,
			"columnDefs" : [ {
				"mDataProp" : "merchTitle",
				"bSortable" : false,
				"aTargets" : [ 0 ]
			}, {
				"mDataProp" : "merchCode",
				"bSortable" : false,
				"aTargets" : [ 1 ]
			}, {
				"mDataProp" : "quantity",
				"bSortable" : false,
				"aTargets" : [ 2 ]
			}, {
				"mDataProp" : "perUnit",
				"bSortable" : false,
				"aTargets" : [ 3 ]
			}, {
				"mDataProp" : "amount",
				"bSortable" : false,
				"aTargets" : [ 4 ]
			},{
				"mDataProp" : "invoiceNumber",
				"bSortable" : false,
				"aTargets" : [ 5 ]
			},
			{
				"mDataProp" : "orderStatus",
				"bSortable" : false,
				"aTargets" : [ 6 ]
			},
			{
				"mDataProp" : "paymentStatus",
				"bSortable" : false,
				"aTargets" : [ 7 ]
			}],
			"language" : {
				"emptyTable" : "<%=ContentConstants.NO_DATA_AVAILABLE%>"
			}
		});
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
 		var taxAmount = "<%=orderList.get(0).getTaxAmount()%>";
 		var netPrice = "<%=orderList.get(0).getNetPrice()%>";
 		var amount = "<%=orderList.get(0).getAmount()%>";
 		var courseTitle = "<%=orderList.get(0).getExamName()%>";
 		var sku = "<%=orderList.get(0).getSku()%>";
 		var quantity = "<%=orderList.get(0).getQuantity()%>";
 		var unitPrice = "<%=orderList.get(0).getUnitPrice()%>";
 		var discount = "<%=orderList.get(0).getTotalDiscountAmount()%>";
 		var fullName = "${user.fullName}";
 		var companyName = "${companyName}";
 		var accType = "${accType}";
 		var displayName = accType == '2' ? companyName : fullName;
 		var paymentMethod = "<%=orderList.get(0).getPaymentMethod()%>";
 		var status = "<%=orderList.get(0).getOrderStatus()%>";
 		var invoiceNo = "<%=orderList.get(0).getInvoiceNo()%>";			
 		var purchaseDate = "<%=sdf.format(orderList.get(0).getOrderDate())%>";
 		var taxRate = "<%=commerceTaxFixedRate%>";
 		var taxPercentage = taxRate * 0.01;
 		// address
 		var billingName = "<%=Validator.isNotNull(orderList.get(0).getBillingAddress())
					? orderList.get(0).getBillingAddress().getName()
					: ""%>";
 		var street1 = "<%=Validator.isNotNull(orderList.get(0).getBillingAddress())
					? orderList.get(0).getBillingAddress().getStreet1()
					: ""%>";
 		var street2 = "<%=Validator.isNotNull(orderList.get(0).getBillingAddress())
					? orderList.get(0).getBillingAddress().getStreet2()
					: ""%>";
 		var street3 = "<%=Validator.isNotNull(orderList.get(0).getBillingAddress())
					? orderList.get(0).getBillingAddress().getStreet3()
					: ""%>";
 		var address = street1 + street2 + street3;
 		var city = "<%=Validator.isNotNull(orderList.get(0).getBillingAddress())
					? orderList.get(0).getBillingAddress().getCity()
					: ""%>";
 		var zip = "<%=Validator.isNotNull(orderList.get(0).getBillingAddress())
					? orderList.get(0).getBillingAddress().getZip()
					: ""%>";
 		var country = "<%=Validator.isNotNull(orderList.get(0).getBillingAddress())
					? orderList.get(0).getBillingAddress().getCommerceCountry().getName(themeDisplay.getLocale())
					: ""%>";
 		var phone = "<%=Validator.isNotNull(orderList.get(0).getBillingAddress())
					? orderList.get(0).getBillingAddress().getPhoneNumber()
					: ""%>";

		var rows = [];
		rows.push([ 'Item', 'SKU', 'Qty', 'Unit Price', 'Discount', 'Net Price', 'GST', 'Total' ]);
		rows.push([ courseTitle, sku, quantity, unitPrice, discount, netPrice, taxAmount, amount ]);

		try {
			var dd = {
				footer : {
					columns : [ {
						text : 'NTUC Learning Hub',
						alignment : 'center',
						style: 'boldHeader'
					} ]
				},
				content : [
						{
							columns : [
									{
										image : imgStr,
										width : 120
									},
									{
										stack : [
												{text:'Tax Invoice/Receipt', style:"boldHeader"},
												{text:'GST Reg No.: 20-0409359-E ',style:"boldHeader", margin:[0,5,0,5]},
												'NTUC LEARNINGHUB PTE LTD',
												'73 BRAS BASAH ROAD',
												'#02-01 NTUC TRADE UNION HOUSE',
												'SINGAPORE 189556',
												'FAX 65 64867824\nwww.ntuclearninghub.com',
												'Company Registration Number: 200409359E' 
												],
										style: 'header',
										width : '*',
										margin: [70,0,0,0]
										
									} ],
							// optional space between columns
							columnGap : 130
						},
						'\n\n',
						{
							columns : [
									{
										// auto-sized columns have their widths based on their content
										//width : 150,
										//text : 'Bill-To:\n' + '${fullName}' + '\n001 Bedok Reservoir Rd\n#01-1110\nSINGAPORE 470719\nREP. OF SINGAPORE\n+65124121255'
										table : {
											widths : [ '*' ],
											body : [ [ {text: 'Bill-To:', style:'boldHeader'} ],
													[ billingName ],
													[ address ],
													[ city + ' ' + zip ],
													[ country ], [ phone ] ]
										},
										layout : 'noBorders',
										style:'content',
										width : '*'
									},
									{
										// % width
										//width : 300,
										//text : '\nInvoice No. : ' + invoiceNo + '\nInvoice Date : ' + purchaseDate + '\n Payment Method : ' + paymentMethod + '\nTerm : ' + status
										table : {
											widths : [ '*', 'auto', 'auto' ],
											body : [
													[ 'Invoice No.', ':', invoiceNo],
													[ 'Invoice Date', ':', purchaseDate ],
													[ 'Payment Method', ':', paymentMethod ],
													[ 'Term', ':', status ] ]
										},
										layout : 'noBorders',
										style:'content',
										width: '*'
									} ],
							// optional space between columns
							columnGap : 130
						},
						'\n\n',
						{text: 'Attn: ' + fullName, style:'content'},
						'\n',
						{
							style : 'content',
							table : {
								widths : [ '*', 'auto', 'auto', 'auto', 'auto','auto','auto','auto' ],
								body : rows,
								style:'content'
							}
						},
						{text : '\nFull name per ID (NRIC/FIN/Passport):',style:'content'},
						{text : fullName,style:'content'},
						'\n\n',
						{
							columns : [
									{
										// auto-sized columns have their widths based on their content
										//width : 150,
										//text : '7% GST on SGD ' + amount + '\n(GST calculation is based on full course fee and services renderer)'
										table : {
											widths : [ '*' ],
											body : [
													[ Math.round(taxRate)
															+ '% GST on SGD $'
															+ netPrice ],
													[ '(GST calculation is based on full course fee and services renderer)' ] ]
										},
										style:'content',
										layout : 'noBorders'
									},
									{
										// % width
										//width : 220,
										//text : '\nSubtotal SGD ' + amount + '\n\nGST (7%) SGD ' + roundTaxPrice + '\nAmount Payable SGD ' + totalPrice + ' ',
										//style : 'rightalign',
										style : 'content',
										
										table : {
											widths : [ '*', 'auto', 'auto' ],
											body : [
												['Subtotal', ':', {text:'$ '+unitPrice , style: 'rightalign'}],
 												['Total Discount', ':', {text:'$ '+discount, style: 'rightalign'}],
 												['Net Price', ':', {text:'$ '+netPrice, style: 'rightalign'}],
 												['GST ('+Math.round(taxRate)+'%)', ':', {text:'$ '+taxAmount , style: 'rightalign'}],
 												['Amount Payable', ':', {text:'$ '+amount, style: 'rightalign'}],
											]
										},
										layout : 'noBorders'
									} ],
							// optional space between columns
							columnGap : 130
						}, ],
				styles : {
					rightalign : {
						alignment : 'right'
					},
					content : {
						fontSize : 8
					},
					header : {
						fontSize : 8,
						color : 'grey',
						alignment : 'left'
					},
					boldHeader : {
						fontSize : 10,
						bold : true,
					}
				}
			};
			var today = new Date();
			var date = today.getFullYear() + '' + (today.getMonth() + 1) + ''
					+ today.getDate();
			var fileName = 'invoice-' + date;
			pdfMake.createPdf(dd).download(fileName);
		} catch (err) {
			console.log(err);
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