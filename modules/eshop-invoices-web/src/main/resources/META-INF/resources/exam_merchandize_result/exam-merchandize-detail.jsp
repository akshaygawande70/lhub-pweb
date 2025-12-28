<%@page import="com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil"%>
<%@page import="com.liferay.commerce.account.model.CommerceAccount"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.liferay.portal.kernel.service.ServiceContextFactory"%>
<%@page import="com.liferay.portal.kernel.service.ServiceContext"%>
<%@page import="api.ntuc.common.util.DownloadInvoiceUtil"%>
<%@page import="com.liferay.commerce.discount.service.CommerceDiscountLocalServiceUtil"%>
<%@page import="com.liferay.commerce.discount.model.CommerceDiscount"%>
<%@page import="com.liferay.commerce.constants.CommerceOrderConstants"%>
<%@page import="com.liferay.commerce.constants.CommerceConstants"%>
<%@page import="api.ntuc.common.util.CurrencyUtil"%>
<%@page import="com.liferay.commerce.service.CommerceCountryLocalServiceUtil"%>
<%@page import="com.liferay.commerce.model.CommerceCountry"%>
<%@page import="com.liferay.commerce.service.CommerceAddressLocalServiceUtil"%>
<%@page import="com.liferay.commerce.model.CommerceAddress"%>
<%@page import="com.liferay.commerce.service.CommerceOrderPaymentLocalServiceUtil"%>
<%@page import="com.liferay.commerce.model.CommerceOrderPayment"%>
<%@page import="com.liferay.commerce.model.CommerceOrderItem"%>
<%@page import="com.liferay.commerce.service.CommerceOrderItemLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.service.RoleServiceUtil"%>
<%@page import="com.liferay.portal.kernel.model.Role"%>
<%@page import="api.ntuc.common.util.RoleUtil"%>
<%@page import="java.io.Serializable"%>
<%@ include file="../init.jsp"%>
<portlet:resourceURL
	id="<%=MVCCommandNames.EXAM_MERCHANDIZE_ORDER_DETAIL_RESOURCE%>"
	var="examMerchandizeOrderDetailURL"
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
CommerceAccount account1 = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());
long accountIdofUser = 0;
Boolean isValidOrder = false;
if(account1 != null) {
	accountIdofUser = account1.getCommerceAccountId();
}
try{
	List<CommerceOrder> plainOrderListSize = CommerceOrderLocalServiceUtil
			.getCommerceOrdersByCommerceAccountId(accountIdofUser, -1, -1, null);
	
	for(CommerceOrder order : plainOrderListSize) {
		if(plainOrderListSize != null && plainOrderListSize.size()>0) {
			if(order.getCommerceOrderId() ==orderId ) {
				isValidOrder =true;
			}
		}
}}catch (Exception e) {
	e.printStackTrace();
}
	CommerceOrder commerceOrder = CommerceOrderLocalServiceUtil.getCommerceOrder(orderId);
	List<CommerceOrderItem> commerceOrderItemList = CommerceOrderItemLocalServiceUtil.getCommerceOrderItems(orderId, -1, -1);
	CommerceOrderPayment commerceOrderPayment = CommerceOrderPaymentLocalServiceUtil.fetchLatestCommerceOrderPayment(orderId);
	long commerceAddressId = commerceOrder.getBillingAddressId();
	CommerceAddress commerceAddress = CommerceAddressLocalServiceUtil.getCommerceAddress(commerceAddressId);
	long commerceCountryId = commerceAddress.getCommerceCountryId();
	CommerceCountry commerceCountry = CommerceCountryLocalServiceUtil.getCommerceCountry(commerceCountryId);
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
	String couponCode = commerceOrder.getCouponCode();
	
	//cek CITREP-Pdf expando
	String pdf = commerceOrder.getExpandoBridge().getAttribute("CITREP-Pdf").toString();
	/* System.out.println("pdf "+pdf); */
	
	long companyId = themeDisplay.getCompanyId();
	List<CommerceDiscount> commerceDiscountList = CommerceDiscountLocalServiceUtil.getCommerceDiscounts(companyId, couponCode);
%>
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
						<li class="list-item active" data-id="#exam-order-history"><a
							href="/account/examinvoices"
						>Exam Order History</a></li>
						<!-- <li class="list-item active" data-id="#merchandize-order-history"><a
							href="/account/merchandizeinvoices">Merchandize Order History</a></li>
						<li class="list-item active" data-id="#exam-merchandize-order-history"><a
							href="/account/exammerchandizeinvoices">Exam & Merchandize Order History</a></li> -->
					</ul>
				</div>
			</div>
			<div class="col-md-9">
				<!--Start content-->
				<div class="content active" id="course-order-history">
					<div class="text-link">
						<a href="/account/examinvoices"><i class="fas fa-angle-left"></i>
							Back To Exams </a>&nbsp;
					</div>
					<div class="box-nav-content">
						<div class="box-nav-dp">
							<div class="btn-list list-download">
								<%
									String invoicePdf = (String) commerceOrder.getExpandoBridge().getAttribute("INVOICE-Pdf");
								int accType = 0;
								List<Role> roleList = new ArrayList<Role>();
								for (Long roleId : user.getRoleIds()) {
									Role newRole = RoleServiceUtil.getRole(roleId);
									roleList.add(newRole);
								}
								if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Corporate_Role")) {
									accType = 2;
								} else {
									accType = 1;
								}
								if(invoicePdf ==null || invoicePdf ==""){
									
//									System.out.println("jsp::::::new code writr 30112023");
								 invoicePdf =	DownloadInvoiceUtil.downLoadInvoiceRetry(commerceOrder.getCommerceOrderId(), user, themeDisplay.getLocale());
								 
									ServiceContext serviceContext = ServiceContextFactory.getInstance(CommerceOrder.class.getName(),
											(PortletRequest) renderRequest);
									Map<String, Serializable> exapandoBridgeAttributes = new HashMap<>();
									exapandoBridgeAttributes.put("INVOICE-Pdf", invoicePdf);
									serviceContext.setExpandoBridgeAttributes(exapandoBridgeAttributes);
									CommerceOrderLocalServiceUtil.updateCustomFields(orderId, serviceContext);
								
								}
								
							   if(isValidOrder){
								   
								%>
								<i class="fas fa-download"></i> <a onclick="downloadPdf('<%=invoicePdf%>')">Download
									Invoice</a>
							</div>
							<%
							   }
							if(commerceOrder.getPaymentStatus() != CommerceOrderPaymentConstants.STATUS_COMPLETED) {%>
								<%
									String paymentUrl = themeDisplay.getPortalURL();
									paymentUrl += "/checkout/-/checkout/payment-process/"+commerceOrder.getUuid();
									if(isValidOrder){
								%>
								<div class="btn-list list-download">
									<i class="fas fa-arrow"></i> <a href="<%=paymentUrl%>">Pay Now</a>
								</div>
							<% }} %>
							<!-- <div class="btn-list list-print">
								<i class="fas fa-print"></i> <a
									onclick="javascript:printDiv('print-area');">Print</a>
							</div> -->
						</div>
					</div>
					<%if(isValidOrder){ %>
					<div class="heading-content">TRANSACTION ID : ${invoiceNo}</div>
					<%} %>
					<div class="box-info" id="print-area">
						<div class="heading-content">EXAM ORDER DETAIL</div>
						<%
						if(accType == 2) {%>
							<div style="flex-direction: column;align-items: flex-start;padding: 24px;line-height: 22px;gap: 16px;left: 599px;top: 253px;background:#F2F2F2;">The invoice is password protected. To view the invoice, simply enter your unique password in the following format:
							<p><b><span style="color:#047E46;">&lt;Company Code&gt;</span><span style="color:#0097ce;">&lt;last 4 characters of UEN&gt;</span></b>.</p>
					   	    For example, if your Company Code is ABCD and the last 4 characters of your UEN is 123A, your unique password would be <b><span style="color:#047E46;">ABCD</span><span style="color:#0097ce;">123A</span></b>.
							</div>
						<%}
						else{ %>
							<div style="flex-direction: column;align-items: flex-start;padding: 24px;line-height: 22px;gap: 16px;left: 599px;top: 253px;background:#F2F2F2;">The invoice is password protected. To view the invoice, simply enter your unique password in the following format:
							<p><b><span style="color:#047E46;">&lt;Date of Birth&gt;</span><span style="color:#0097ce;">&lt;last 4 characters of NRIC&gt;</span></b>, where your date of birth should be in the 'ddmmyyyy' format.</p>
					   	    For example, if your date of birth is on 1 January 1985 and the last 4 characters of your NRIC is 123A, your unique password would be <b><span style="color:#047E46;">01011985</span><span style="color:#0097ce;">123A</span></b>.
							</div>						
						<% } %>						
						<table id="<portlet:namespace/>exam-merchandize-order-history"
							class="table cxrus-table dataTable no-footer" style="width: 100%"
						>
							<thead>
								<tr>
									<th id="exam_name">Exam Title</th>
									<th id="exam_code">Exam Code</th>
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
		
		var tempTblId = "#<portlet:namespace/>exam-merchandize-order-history";
		var table = $(tempTblId).DataTable({
			"sAjaxSource" : '${examMerchandizeOrderDetailURL}',
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
				"mDataProp" : "exam_merchandize_name",
				"bSortable" : false,
				"aTargets" : [ 0 ]
			}, {
				"mDataProp" : "exam_merchandize_code",
				"bSortable" : false,
				"aTargets" : [ 1 ]
			}, {
				"mDataProp" : "quantity",
				"bSortable" : false,
				"aTargets" : [ 2 ]
			}, {
				"mDataProp" : "per_unit",
				"bSortable" : false,
				"aTargets" : [ 3 ]
			}, {
				"mDataProp" : "amount",
				"bSortable" : false,
				"aTargets" : [ 4 ]
			},{
				"mDataProp" : "invoice_no",
				"bSortable" : false,
				"aTargets" : [ 5 ]
			},
			{
				"mDataProp" : "order_status",
				"bSortable" : false,
				"aTargets" : [ 6 ]
			},
			{
				"mDataProp" : "payment_status",
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

	function downloadPdf(data) {
		const pdf = '<%=invoicePdf%>';
		const byteCharacters = atob(pdf);
		const byteNumbers = new Array(byteCharacters.length);
		for (let i = 0; i < byteCharacters.length; i++) {
		    byteNumbers[i] = byteCharacters.charCodeAt(i);
		}
		const byteArray = new Uint8Array(byteNumbers);
		console.log(byteArray, " byteArray");
		// create the blob object with content-type "application/pdf"               
		const blob = new Blob( [byteArray], { type: "application/pdf" });
		console.log(blob, " blob");
		var url = URL.createObjectURL(blob);
		
		var downloadLink = document.createElement('a');
		document.body.appendChild(downloadLink);
		
		downloadLink.href = url;
		downloadLink.target = '_blank';
		downloadLink.download = 'INVOICE_<%=orderId%>.pdf';
		downloadLink.click();
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