<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.commerce.service.CommerceOrderLocalServiceUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.liferay.portal.kernel.service.ServiceContextFactory"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portal.kernel.service.ServiceContext"%>
<%@page import="com.liferay.commerce.constants.CommerceOrderPaymentConstants"%>
<%@ include file="/init.jsp" %>

<liferay-portlet:runtime portletName="com_liferay_commerce_cart_content_web_internal_portlet_CommerceCartContentPortlet"></liferay-portlet:runtime>



<%@page import="api.ntuc.common.util.CurrencyUtil"%>
<%@page import="com.liferay.portal.kernel.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.model.Role"%>	
<%@page import="com.liferay.portal.kernel.model.User"%>	
<%@page import="com.liferay.portal.kernel.service.RoleServiceUtil"%>	
<%@page import="com.liferay.commerce.account.model.CommerceAccount"%>	
<%@page import="com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil"%>	
<%@page import="api.ntuc.common.util.RoleUtil"%>
<%@page import="com.liferay.commerce.discount.service.CommerceDiscountLocalServiceUtil"%>
<%@page import="com.liferay.commerce.discount.model.CommerceDiscount"%>
<%@page import="com.liferay.commerce.constants.CommerceOrderConstants"%>
<%@page import="com.liferay.commerce.service.CommerceCountryLocalServiceUtil"%>
<%@page import="com.liferay.commerce.model.CommerceCountry"%>
<%@page import="com.liferay.commerce.service.CommerceAddressLocalServiceUtil"%>
<%@page import="com.liferay.commerce.model.CommerceAddress"%>
<%@page import="com.liferay.commerce.service.CommerceOrderPaymentLocalServiceUtil"%>
<%@page import="com.liferay.commerce.model.CommerceOrderPayment"%>
<%@page import="com.liferay.commerce.service.CommerceOrderItemLocalServiceUtil"%>
<%@page import="java.io.Serializable" %>
<%@page import="com.liferay.commerce.model.CommerceOrder"%>

<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ include file="/init.jsp"%>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.72/pdfmake.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.72/vfs_fonts.js"></script>
<%@page import="com.liferay.commerce.model.CommerceOrderItem"%>
<%@page import="com.liferay.commerce.tax.engine.fixed.service.CommerceTaxFixedRateLocalServiceUtil"%>
<%@page import="com.liferay.commerce.tax.engine.fixed.model.CommerceTaxFixedRate"%>

<img id="logImageStr" src="<%=request.getContextPath()%>/img/ntuclhub-logo.png" style="display: none;">
<aui:input label="" name="invoicePdf"
	type="hidden"
/>
<aui:input label="" name="orderId"
	type="hidden"
/>

<script>
	var imgStr= "";
    $(document).ready(function () {    	
        $(".heading-list").click(function () {
            var id = $(this).attr("data-id");
            $(id).toggleClass("active");
            $(this).toggleClass("active");
        });
        
        imgStr = getImgStr();
        downloadInvoicePdf();       
    });
    
    function getImgStr(){
    	var img = document.getElementById("logImageStr");
    	var canvas = document.createElement("canvas");
    	canvas.height = img.naturalHeight;
		canvas.width = img.naturalWidth;
		var ctx = canvas.getContext("2d");
		ctx.drawImage(img, 0, 0);
		var dataURL =  canvas.toDataURL();
		return dataURL;
 	}
    
    <%
		CommerceOrder commerceOrder = (CommerceOrder) request.getAttribute("commerceOrder");
		double discountFromCommerceOrder = commerceOrder.getShippingDiscountAmount().doubleValue() + 
				commerceOrder.getSubtotalDiscountAmount().doubleValue()+
				commerceOrder.getTotalDiscountAmount().doubleValue(); 
		double commerceTaxFixedRate = CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).size()>0?
				CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).get(0).getRate():0;
		double shippingAmount = commerceOrder.getShippingAmount().doubleValue();
	    String fullNameCap = "";
	    int accType = 0;
		String companyName = "";
		long accountId = 0;
		List<Role> roleList = new ArrayList<Role>();
		for (Long roleId : user.getRoleIds()) {
			Role newRole = RoleServiceUtil.getRole(roleId);
			roleList.add(newRole);
		}
		
		CommerceAccount account2 = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());
		if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Corporate_Role")) {
			companyName = (String) account2.getExpandoBridge().getAttribute("Company Name");
			accType = 2;
		} else {
			if (!user.getMiddleName().isEmpty()) {
				fullNameCap = user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
			} else {
				fullNameCap = user.getFirstName() + " " + user.getLastName();
			}
			accType = 1;
			fullNameCap = fullNameCap.toUpperCase();
		}
    %>
    
    function downloadInvoicePdf() {    	
    	<%
    		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			long orderId = commerceOrder.getCommerceOrderId();
			List<CommerceOrderItem> commerceOrderItemList = CommerceOrderItemLocalServiceUtil.getCommerceOrderItems(orderId, -1, -1);
			CommerceOrderPayment commerceOrderPayment = CommerceOrderPaymentLocalServiceUtil.fetchLatestCommerceOrderPayment(orderId);
			long commerceAddressId = commerceOrder.getBillingAddressId();
			CommerceAddress commerceAddress = CommerceAddressLocalServiceUtil.getCommerceAddress(commerceAddressId);
			long commerceCountryId = commerceAddress.getCommerceCountryId();
			CommerceCountry commerceCountry = CommerceCountryLocalServiceUtil.getCommerceCountry(commerceCountryId);
			//new code for dob issue resolve start DC-172
		//		User user = UserLocalServiceUtil.getUser(commerceOrder.getUserId());
			SimpleDateFormat sdfExpandoDob = new SimpleDateFormat("yyyy/MM/dd");
			String dob = sdfExpandoDob.format(user.getBirthday());
			dob = dob.replace("/", "-");
			
			ServiceContext serviceContext = ServiceContextFactory.getInstance(CommerceOrder.class.getName(),
					(PortletRequest) renderRequest);
			Map<String, Serializable> exapandoBridgeAttributes = new HashMap<>();
			exapandoBridgeAttributes.put("Candidate Date of Birth", dob);
			serviceContext.setExpandoBridgeAttributes(exapandoBridgeAttributes);
			CommerceOrderLocalServiceUtil.updateCustomFields(orderId, serviceContext);
		
			//new code for dob issue resolve end DC-172
    	%>
    	$('#<portlet:namespace />orderId').val(<%=orderId%>);
 		var fullName = "<%=fullNameCap%>";
 		var companyName = "<%=companyName%>";
 		var accType = "<%=accType%>";
 		var status = "<%=CommerceOrderConstants.getOrderStatusLabel(commerceOrder.getOrderStatus()).toUpperCase()%>";
 		var displayName = "<%=accType == 2 ? companyName : fullNameCap%>";
 		var taxRate = "<%=commerceTaxFixedRate%>";
 		var taxRateDecimalDynamic = parseInt('' + (taxRate * 100)) / 100;
 		var taxPercentage = taxRate * 0.01;
 		var totalTaxAmount = "<%=commerceOrder.getTaxAmount()%>";
 		var totalTaxAmountDecimalDynamic = parseFloat(totalTaxAmount).toFixed(2);
 		var rows = [];
		rows.push([ 'Item', 'SKU', 'Qty', 'Unit Price', 'Discount', 'Net Price', 'GST', 'Total' ]);
 		
 		//orderList
 		<%  	
 			double subtotalUnitPrice = 0;
 			double totalUnitPrice = 0;
 			for(int a=0;a<commerceOrderItemList.size();a++) { 
 		%>
 		var netPrice = "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(commerceOrderItemList.get(a).getFinalPrice()))%>";
 		var courseTitle = "<%=commerceOrderItemList.get(a).getName(themeDisplay.getLocale())%>";
 		var sku = "<%=commerceOrderItemList.get(a).getSku()%>";
 		var quantity = "<%=commerceOrderItemList.get(a).getQuantity()%>";
 		<%
	 		float getPriceUnit = commerceOrderItemList.get(a).getPromoPrice()!=null && commerceOrderItemList.get(a).getPromoPrice().floatValue()>0 ? 
	 				commerceOrderItemList.get(a).getPromoPrice().floatValue() : commerceOrderItemList.get(a).getUnitPrice().floatValue() ;
	 		float getPriceQty = getPriceUnit * commerceOrderItemList.get(a).getQuantity();
	 		double discountAmountFloat = commerceOrderItemList.get(a).getDiscountAmount().floatValue();
	 		discountFromCommerceOrder = discountFromCommerceOrder + discountAmountFloat;
	 		double taxAmountPerProduct = getPriceQty * (commerceTaxFixedRate) * 0.01;
	 		double totalEachProduct = taxAmountPerProduct + getPriceQty - discountAmountFloat;
	 		totalUnitPrice = totalUnitPrice + getPriceQty;
	 		subtotalUnitPrice = subtotalUnitPrice + commerceOrderItemList.get(a).getFinalPrice().doubleValue();
	 		
 		%>
 		var discount = "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(discountAmountFloat))%>";
 		var unitPrice = "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(getPriceUnit))%>";
 		var invoiceNo = "<%=commerceOrderItemList.get(a).getCommerceOrderId()%>";			
 		var purchaseDate = "<%=sdf.format(commerceOrderItemList.get(a).getCreateDate())%>";
 		var taxAmountPerProduct = "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(taxAmountPerProduct))%>";
 		var totalEachProduct = "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(totalEachProduct))%>";
 		var amountPayable = "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(commerceOrder.getTotal()))%>";
 		var totalUnitPrice = "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(totalUnitPrice))%>";
 		var discountFromCommerceOrder = "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(discountFromCommerceOrder))%>";
 		var subtotalUnitPrice = "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(subtotalUnitPrice))%>";
 		var shippingAmount = "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(shippingAmount))%>";
 		
 		var discountTernary = discount === ".00" ? "0" : discount.toLocaleString("en",{useGrouping: false, minimumFractionDigits: 2});
 		var totalUnitPriceTernary = totalUnitPrice === ".00" ? "0" : totalUnitPrice.toLocaleString("en",{useGrouping: false, minimumFractionDigits: 2});
 		var discountFromCommerceOrderTernary = discountFromCommerceOrder === ".00" ? "0" : discountFromCommerceOrder.toLocaleString("en",{useGrouping: false, minimumFractionDigits: 2});
 		var subtotalUnitPriceTernary = subtotalUnitPrice === ".00" ? "0" : subtotalUnitPrice.toLocaleString("en",{useGrouping: false, minimumFractionDigits: 2});
 		var shippingAmountTernary = shippingAmount === ".00" ? "0" : shippingAmount.toLocaleString("en",{useGrouping: false, minimumFractionDigits: 2});
 		
 		rows.push([ courseTitle, sku, quantity, unitPrice, discountTernary, netPrice, parseFloat(taxAmountPerProduct).toFixed(2), totalEachProduct ]);
			
 		<% } %>
 		
 		// address	
 		var totalOtherDiscount =  "<%=discountFromCommerceOrder%>";
 		var totalDiscount = totalOtherDiscount + discount;
		var billingName = "<%=HtmlUtil.escapeAttribute(commerceAddress.getName())%>";
		var street1 = "<%=HtmlUtil.escapeAttribute(commerceAddress.getStreet1())%>";
		var street2 = "<%=HtmlUtil.escapeAttribute(commerceAddress.getStreet2())%>";
		var street3 = "<%=HtmlUtil.escapeAttribute(commerceAddress.getStreet3())%>";
		var address = street1 + street2 + street3;
		var city = "<%=HtmlUtil.escapeAttribute(commerceAddress.getCity())%>";
		var zip = "<%=HtmlUtil.escapeAttribute(commerceAddress.getZip())%>";
		var phone = "<%=HtmlUtil.escapeAttribute(commerceAddress.getPhoneNumber())%>";
		var paymentMethod = "<%=commerceOrder.getCommercePaymentMethodKey()%>";
		var status = "<%=CommerceOrderPaymentConstants.getOrderPaymentStatusLabel(commerceOrder.getPaymentStatus())%>";
		// country
 		<%-- <% for(int c=0;c<commerceCountryList.size();c++){ %>
 		var country = "<%=Validator.isNotNull(commerceCountryList.get(c).getName(themeDisplay.getLocale()))
					? commerceCountryList.get(c).getName(themeDisplay.getLocale())
					: ""%>";
		<% } %> --%>
		var country = "<%=commerceCountry.getName(themeDisplay.getLocale())%>";

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
													[ 'Term', ':', status ]]
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
													[ taxRateDecimalDynamic
															+ '% GST on SGD $'
															+ <%=CurrencyUtil.roundUpDollarAmount(String.valueOf(subtotalUnitPrice))%> ],
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
												['Subtotal', ':', {text:'$ '+totalUnitPriceTernary, style: 'rightalign'}],
 												['Total Discount', ':', {text:'$ '+discountFromCommerceOrderTernary, style: 'rightalign'}],
 												['Net Price', ':', {text:'$ '+subtotalUnitPriceTernary, style: 'rightalign'}],
 												['GST ('+taxRateDecimalDynamic+'%)', ':', {text:'$ '+totalTaxAmountDecimalDynamic , style: 'rightalign'}],
 												['Shipping', ':', {text:'$ '+shippingAmountTernary, style: 'rightalign'}],
 												['Amount Payable', ':', {text:'$ '+amountPayable, style: 'rightalign'}],
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
			
			var pdfDocGenerator = pdfMake.createPdf(dd);
			pdfDocGenerator.getBase64((data) => {
 			//comment pdf generation DC-125 	$('#<portlet:namespace />invoicePdf').val(data);
 				return data;
 			});
		} catch (err) {
			console.log(err);
		}
	}
</script>