
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ include file="/init.jsp"%>
<%@page import="com.liferay.commerce.model.CommerceOrderItem"%>
<%@page import="com.liferay.commerce.tax.engine.fixed.service.CommerceTaxFixedRateLocalServiceUtil"%>
<%@page import="com.liferay.commerce.tax.engine.fixed.model.CommerceTaxFixedRate"%>

<%@page import="com.liferay.asset.entry.rel.model.AssetEntryAssetCategoryRel"%>
<%@page import="com.liferay.asset.entry.rel.service.AssetEntryAssetCategoryRelLocalServiceUtil"%>
<%@page import="com.liferay.asset.kernel.model.AssetCategory"%>
<%@page import="com.liferay.asset.kernel.model.AssetEntry"%>
<%@page import="com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil"%>
<%@page import="com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil"%>
<%@page import="com.liferay.commerce.product.type.virtual.constants.VirtualCPTypeConstants"%>
<%@page import="java.text.ParseException" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date" %>

<%
	OrderSummaryCheckoutStepDisplayContext orderSummaryCheckoutStepDisplayContext = (OrderSummaryCheckoutStepDisplayContext) request
			.getAttribute(CommerceCheckoutWebKeys.COMMERCE_CHECKOUT_STEP_DISPLAY_CONTEXT);
	
	CommerceOrder commerceOrder = orderSummaryCheckoutStepDisplayContext.getCommerceOrder();
	float discountFromCommerceOrder = commerceOrder.getShippingDiscountAmount().floatValue() + 
			commerceOrder.getSubtotalDiscountAmount().floatValue() +
			commerceOrder.getTotalDiscountAmount().floatValue(); 
	
	float totalProductPrice = 0;
	float discountAmountFloat = 0;
	
	double commerceTaxFixedRate = CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).size()>0?
			CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).get(0).getRate():0;
	
	CommerceOrderPrice commerceOrderPrice = orderSummaryCheckoutStepDisplayContext.getCommerceOrderPrice();

	CommerceMoney shippingValueCommerceMoney = commerceOrderPrice.getShippingValue();
	CommerceMoney taxValueCommerceMoney = commerceOrderPrice.getTaxValue();
	CommerceMoney totalOrderCommerceMoney = commerceOrderPrice.getTotal();
	
	String preferedDate1 = null;
	String preferedDate2 = null;
	String preferedDate3 = null;
	String dobCandidate = null;
	
	String priceDisplayType = orderSummaryCheckoutStepDisplayContext.getCommercePriceDisplayType();

	if (priceDisplayType.equals(CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {
		shippingValueCommerceMoney = commerceOrderPrice.getShippingValueWithTaxAmount();
		totalOrderCommerceMoney = commerceOrderPrice.getTotalWithTaxAmount();
	}

	CommerceDiscountValue itemTotalDiscountValue = null;
	if(commerceOrder.getCommerceOrderItems().size()>0){
		CommerceOrderItem commerceOrderItem2 = commerceOrder.getCommerceOrderItems().get(0);
		CommerceProductPrice commerceProductPriceItem = orderSummaryCheckoutStepDisplayContext.getCommerceProductPrice(commerceOrderItem2);
		itemTotalDiscountValue = commerceProductPriceItem.getDiscountValue();
		itemTotalDiscountValue = commerceProductPriceItem.getDiscountValue();
	}
	
	
	
	String commercePaymentMethodName = StringPool.BLANK;

	String commercePaymentMethodKey = commerceOrder.getCommercePaymentMethodKey();

	if (commercePaymentMethodKey != null) {
		commercePaymentMethodName = orderSummaryCheckoutStepDisplayContext
				.getPaymentMethodName(commercePaymentMethodKey, locale);
	}

	String commerceShippingOptionName = commerceOrder.getShippingOptionName();

	Map<Long, List<CommerceOrderValidatorResult>> commerceOrderValidatorResultMap = orderSummaryCheckoutStepDisplayContext
			.getCommerceOrderValidatorResults();
	
	float subTotalNew = 0;
	String subTotalNewString = "";
	
	CPDefinition cpDefinition = null;
	boolean flagExam = false;
	boolean flagNonExam = false;
	String imageExam = null;
	String descExam = null;
	String categoryExam = null;
	
	List<String> productCategoryList = new ArrayList<String>();
	for (CommerceOrderItem item : commerceOrder.getCommerceOrderItems()) {
		cpDefinition = item.getCPDefinition();
		AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(CPDefinition.class.getName(), cpDefinition.getCPDefinitionId());
	    long entryId = assetEntry.getEntryId();
	    List<AssetEntryAssetCategoryRel> aeacRelLocalService = AssetEntryAssetCategoryRelLocalServiceUtil.getAssetEntryAssetCategoryRelsByAssetEntryId(entryId);
	    if(aeacRelLocalService.size()>0){
	    	long assetCategoryId = aeacRelLocalService.get(0).getAssetCategoryId();
		    AssetEntryAssetCategoryRel assetEntryAssetEntryCategoryRel = AssetEntryAssetCategoryRelLocalServiceUtil.fetchAssetEntryAssetCategoryRel(entryId, assetCategoryId);
		    long categoryId = assetEntryAssetEntryCategoryRel.getAssetCategoryId();
		    AssetCategory assetCategory = AssetCategoryLocalServiceUtil.fetchAssetCategory(categoryId);
		    String categoryName = assetCategory.getName();
		    productCategoryList.add(categoryName);
	    }	
	}
	
	int temp = 0;
	
	float calculatedProductPrice = 0;
%>


<section class="order-review">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="notice-alert alert-blue">
                    <img alt="" src="/o/web.ntuc.eshop.register/img/Info_Icon.png">Please review and check through all
                    details you have input previously.
                </div>
            </div>
        </div>
    </div>
    
    

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="box-order-review">
                    <div class="title-order">ORDER & REVIEW INFORMATION</div>
					<div class="row box-shadow">
						<div class="col-md-12">
							<div class="box-review">
								<%
									for (CommerceOrderItem commerceOrderItem : commerceOrder.getCommerceOrderItems()) {
								%>
									<div class="box-row-list row">
									<div class="col-md-4"><img alt="" class="img-fluid"src="<%=orderSummaryCheckoutStepDisplayContext.getCommerceOrderItemThumbnailSrc(commerceOrderItem)%>"/></div>									
									<div class="col-md-8">
									<div class="desc-exams"><%=HtmlUtil.escape(cpDefinition.getName(themeDisplay.getLanguageId()))%></div>
									<c:if test="<%=productCategoryList.size()>0 %>">
										<div class="category"><%=productCategoryList.get(temp)%></div>
									</c:if>
									</div>									
									<%
											if(commerceOrderItem.getCPDefinition().getProductTypeName().equals(VirtualCPTypeConstants.NAME) == true ){
												flagExam = true;
												imageExam = orderSummaryCheckoutStepDisplayContext.getCommerceOrderItemThumbnailSrc(commerceOrderItem);
												descExam = commerceOrderItem.getCPDefinition().getName(themeDisplay.getLanguageId());
												categoryExam = productCategoryList.get(temp);
												
												SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
												SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
												
												preferedDate1 = sdf.format((Date) commerceOrder.getExpandoBridge().getAttribute("Preferred Date 1"));
												preferedDate2 = sdf.format((Date) commerceOrder.getExpandoBridge().getAttribute("Preferred Date 2"));
												preferedDate3 = sdf.format((Date) commerceOrder.getExpandoBridge().getAttribute("Preferred Date 3"));
												dobCandidate = sdf2.format((Date) commerceOrder.getExpandoBridge().getAttribute("Candidate Date of Birth"));
											}else{
												flagNonExam = true;
											}
									
											float getPriceQty = commerceOrderItem.getPromoPrice()!=null && commerceOrderItem.getPromoPrice().floatValue()>0 ? 
			                            			commerceOrderItem.getPromoPrice().floatValue() : commerceOrderItem.getUnitPrice().floatValue() ;
			                            	getPriceQty = getPriceQty*commerceOrderItem.getQuantity();
			                            	totalProductPrice = totalProductPrice + getPriceQty;
											
											discountAmountFloat = discountAmountFloat + commerceOrderItem.getDiscountAmount().floatValue();
											
											System.out.println("preferedDate1 = " +preferedDate1);
											System.out.println("preferedDate2 = " +preferedDate2);
											System.out.println("preferedDate3 = " +preferedDate3);
									%>
									
									</div>
									
								<%
									temp++;
									}
								%>
								
								<div class="row totalrow">
								<div class="col-md-12">
									<div class="box-text">
										<div class="text-left">Sub Total</div>
										<div class="text-right">$ <%=HtmlUtil.escape(String.format(locale, "%,.2f", totalProductPrice))%></div>
									</div>
									<div class="box-text">
									<%
									if(commerceTaxFixedRate == (long) commerceTaxFixedRate) {%>
										<div class="tax-left"><%=String.format("%d",(long) commerceTaxFixedRate)%>% GST Included</div>
									<% } else { %>
										<div class="tax-left"><%=String.format("%s", commerceTaxFixedRate)%>% GST Included</div>
									<% } %>
										<div class="text-right">$ <%=HtmlUtil.escape(taxValueCommerceMoney.format(locale))%></div>
									</div>
									<div class="box-text">
										<div class="text-left">Shipment</div>
										<div class="text-right">$ <%=HtmlUtil.escape(shippingValueCommerceMoney.format(locale))%></div>
									</div>
									<div class="box-text">
										<div class="text-left">Total Discount</div>
										<div class="text-right">$ <%=HtmlUtil.escape(String.format(locale, "%,.2f", (discountAmountFloat+ discountFromCommerceOrder)))%></div>
									</div>
									
									<div class="box-text total-price">
										<div class="text-left">TOTAL</div>
										<div class="text-right">$ <%=HtmlUtil.escape(totalOrderCommerceMoney.format(locale))%></div>
									</div>
								</div>
								</div>
							</div>
						</div>
					</div>
                </div>
				<div class="box-list-detail">
					<c:if test="<%=flagExam %>">
						<div class="heading-list" data-id="#candidate">
							<div class="title-list">Candidate Information</div>
						</div>
						<div class="isi-list" id="candidate">
							<div class="box-candidate">
								<div class="title-name">Full Name (NRIC/Passport)</div>
								<div class="dot">:</div>
								<div class="isi-title"><%=HtmlUtil.escape((String) commerceOrder.getExpandoBridge().getAttribute("Candidate Full Name")) %></div>
							</div>
							<div class="box-candidate">
								<div class="title-name">Date of Birth</div>
								<div class="dot">:</div>
								<div class="isi-title"><%=dobCandidate%></div>
							</div>
							<div class="box-candidate">
								<div class="title-name">Email Address</div>
								<div class="dot">:</div>
								<div class="isi-title"><%=HtmlUtil.escape((String) commerceOrder.getExpandoBridge().getAttribute("Candidate Email Address")) %></div>
							</div>
							<c:if test="<%=commerceOrder.getExpandoBridge().getAttribute("ACCA Registration No")!=null && !((String) commerceOrder.getExpandoBridge().getAttribute("ACCA Registration No")).isEmpty() %>">
								<div class="box-candidate">
									<div class="title-name">ACCA Registration No</div>
									<div class="dot">:</div>
									<div class="isi-title"><%=(String) commerceOrder.getExpandoBridge().getAttribute("ACCA Registration No") %></div>
								</div>
							</c:if>
							<c:if test="<%=commerceOrder.getExpandoBridge().getAttribute("MS ID")!=null && !((String) commerceOrder.getExpandoBridge().getAttribute("MS ID")).isEmpty() %>">
								<div class="box-candidate">
									<div class="title-name">Microsoft ID</div>
									<div class="dot">:</div>
									<div class="isi-title"><%=(String) commerceOrder.getExpandoBridge().getAttribute("MS ID") %></div>
								</div>
							</c:if>
							<c:if test="<%=commerceOrder.getExpandoBridge().getAttribute("PMI ID")!=null && !((String) commerceOrder.getExpandoBridge().getAttribute("PMI ID")).isEmpty() %>">
								<div class="box-candidate">
									<div class="title-name">PMI ID</div>
									<div class="dot">:</div>
									<div class="isi-title"><%=(String) commerceOrder.getExpandoBridge().getAttribute("PMI ID") %></div>
								</div>
							</c:if>
							<c:if test="<%=((Boolean)commerceOrder.getExpandoBridge().getAttribute("PMI Membership"))%>">
								<div class="box-candidate">
									<div class="title-name">PMI
											Membership</div>
									<div class="dot">:</div>
									<div class="isi-title"><%=((Boolean) commerceOrder.getExpandoBridge().getAttribute("PMI Membership"))? "Member" : "Non-Member" %></div>
								</div>
							</c:if>
						</div>

						<div class="heading-list" data-id="#dateandtime">
							<div class="title-list">Date and Time selected for exam</div>
						</div>
						<div class="isi-list dateandtime-list" id="dateandtime">
							<div class="row box-produk">
								<div class="col-md-3">
									<img alt="" src="<%=imageExam %>" />
								</div>
								<div class="col-md-9">
									<div class="box-review">
										<div class="desc-exams"><%=descExam %>
										</div>
										<div class="category"><%=categoryExam %></div>
									</div>
								</div>
							</div>
							<div class="box-candidate">
								<div class="title-name">Selection 1</div>
								<div class="dot">:</div>
								<div class="isi-title"><%=preferedDate1%></div>
							</div>
							<div class="box-candidate">
								<div class="title-name">Selection 2</div>
								<div class="dot">:</div>
								<div class="isi-title"><%=preferedDate2%></div>
							</div>
							<div class="box-candidate">
								<div class="title-name">Selection 3</div>
								<div class="dot">:</div>
								<div class="isi-title"><%=preferedDate3%></div>
							</div>
							<div class="box-candidate">
								<div class="title-name d-block w-100">Additional Notes</div>
								<div class="isi-title d-block w-100"><%=HtmlUtil.escape((String) commerceOrder.getExpandoBridge().getAttribute("Additional Notes")) %></div>
							</div>
						</div>
						<c:if
							test="<%=commerceOrder.getExpandoBridge().getAttribute("CITREP-Pdf")!=null && !((String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Pdf")).isEmpty() %>">
							<div class="heading-list" data-id="#citrep">
								<div class="title-list">CITREP+ funding detail</div>
								<%
									String citrepPdf = (String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Pdf");
								%>
								<div class="edit-mode download">
									<a onclick="downloadPdf('<%=citrepPdf%>')"><i
										class="fas fa-edit"></i> Download</a>
								</div>
							</div>
							<div class="isi-list citrep-list" id="citrep">
								<div class="box-candidate">
									<div class="title-name">Name (As per NRIC)</div>
									<div class="dot">:</div>
									<div class="isi-title"><%=HtmlUtil.escape((String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Full-Name-NRIC")) %></div>
								</div>
								<div class="box-candidate">
									<div class="title-name">NRIC Number</div>
									<div class="dot">:</div>
									<div class="isi-title"><%=HtmlUtil.escape((String) commerceOrder.getExpandoBridge().getAttribute("CITREP-NRIC")) %></div>
								</div>
								<div class="box-candidate">
									<div class="title-name">Colour of NRIC</div>
									<div class="dot">:</div>
									<div class="isi-title"><%=HtmlUtil.escape((String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Nationality-Detail")) %></div>
								</div>
								<div class="box-candidate">
									<div class="title-name">Email Address</div>
									<div class="dot">:</div>
									<div class="isi-title"><%=HtmlUtil.escape((String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Email")) %></div>
								</div>
								<div class="box-candidate">
									<div class="title-name">Contact Number</div>
									<div class="dot">:</div>
									<div class="isi-title"><%=HtmlUtil.escape((String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Mobile-Number")) %></div>
								</div>
								<div class="box-candidate">
									<div class="title-name">Company Sponsored</div>
									<div class="dot">:</div>
									<div class="isi-title"><%=((String[]) commerceOrder.getExpandoBridge().getAttribute("CITREP-Company-Sponsored"))[0] %></div>
								</div>
								<div class="box-candidate">
									<div class="title-name">PMI ID</div>
									<div class="dot">:</div>
									<div class="isi-title"><%=HtmlUtil.escape((String) commerceOrder.getExpandoBridge().getAttribute("PMI ID")) %></div>
								</div>
								<div class="row box-text-check">
									<div class="col-12">
										<div class="title-underline">Exam Title</div>
									</div>
									<div class="col-12">
										<div class="form-group form-check">
											<label class="form-check-label position-relative"><input
												class="form-check-input " type="checkbox" checked="checked"
												disabled="disabled" /><%=HtmlUtil.escape(descExam) %></label>
										</div>

									</div>
									<div class="col-12">
										<div class="title-underline">Declaration :</div>
									</div>
									<div class="col-12">
										<div class="form-group form-check">
											<label class="form-check-label position-relative"><input
												class="form-check-input " type="checkbox" checked="checked"
												disabled="disabled" />I acknowledge the use of these details
												are for CITREP+ funding enrolment. </label>
										</div>

									</div>
									<div class="col-12">
										<div class="form-group form-check">
											<label class="form-check-label position-relative"><input
												class="form-check-input " type="checkbox" checked="checked"
												disabled="disabled" />I understand that CITREP + funding for
												LHUB candidates expires on 31 March 2022 and I need to take
												my first exam attempt beofre the eligibility date for
												funding </label>
										</div>

									</div>
									<div class="col-12">
										<div class="form-group form-check">
											<label class="form-check-label position-relative"><input
												class="form-check-input " type="checkbox" checked="checked"
												disabled="disabled" />All the information given in this form
												is true and accurate to the best of my knwledge. Any false
												or misleading declation will result in claim application
												being rejected. </label>
										</div>

									</div>
								</div>

							</div>
						</c:if>
					</c:if>
					<c:if test="<%=commerceOrder.getShippingAddress()!=null || commerceOrder.getBillingAddress()!=null %>">
						<div class="heading-list" data-id="#address">
							<div class="title-list">Address</div>
						</div>

						<div class="isi-list address-list" id="address">
							<h6><b>Billing Address</b></h6>
							<div class="box-candidate">
								<div class="title-name">Name</div>
	
								<div class="dot">:</div>
	
								<div class="isi-title"><%=HtmlUtil.escape(commerceOrder.getBillingAddress().getName()) %></div>
							</div>
							
							<div class="box-candidate">
								<div class="title-name">Phone Address</div>

								<div class="dot">:</div>

								<div class="isi-title"><%=HtmlUtil.escape(commerceOrder.getBillingAddress().getPhoneNumber()) %></div>
							</div>
							<%
							String addressBilling = "";
							String regionNameBilling = "";
							if(commerceOrder.getBillingAddress().getStreet1()!=null && !commerceOrder.getBillingAddress().getStreet1().isEmpty()) {
								addressBilling = commerceOrder.getBillingAddress().getStreet1();
							}
							if(commerceOrder.getBillingAddress().getStreet2()!=null && !commerceOrder.getBillingAddress().getStreet2().isEmpty()) {
								addressBilling = !addressBilling.isEmpty()? addressBilling+"<br>"+ commerceOrder.getBillingAddress().getStreet2() : commerceOrder.getBillingAddress().getStreet2();
							}
							if(commerceOrder.getBillingAddress().getStreet3()!=null && !commerceOrder.getBillingAddress().getStreet3().isEmpty()) {
								addressBilling = !addressBilling.isEmpty()? addressBilling+"<br>"+ commerceOrder.getBillingAddress().getStreet3() : commerceOrder.getBillingAddress().getStreet3();
							}
							
							if(commerceOrder.getBillingAddress().getCommerceRegion()!=null && commerceOrder.getBillingAddress().getCommerceRegion().getName()!=null){
								regionNameBilling = commerceOrder.getBillingAddress().getCommerceRegion().getName();
							}
						%>
							<div class="box-candidate">
								<div class="title-name">Address</div>

								<div class="dot">:</div>

								<div class="isi-title"><%=HtmlUtil.escape(addressBilling) %></div>
							</div>

							<div class="box-candidate">
								<div class="title-name">Postal Code</div>

								<div class="dot">:</div>

								<div class="isi-title"><%=HtmlUtil.escape(commerceOrder.getBillingAddress().getZip())%></div>
							</div>

							<div class="box-candidate">
								<div class="title-name">City</div>

								<div class="dot">:</div>

								<div class="isi-title"><%=HtmlUtil.escape(commerceOrder.getBillingAddress().getCity())%></div>
							</div>

							<div class="box-candidate">
								<div class="title-name">Region</div>

								<div class="dot">:</div>

								<div class="isi-title"><%=HtmlUtil.escape(regionNameBilling) %></div>
							</div>
							<%-- <div class="row box-text-check">
							<div class="col-12">
								<div class="form-group form-check">
									<label class="form-check-label position-relative"><input
										class="form-check-input " type="checkbox" checked="<%=commerceOrder.getShippingAddress().getCommerceRegion().getName()%>"/>Billing
										and Shipping address is the same</label>
								</div>
							</div>
						</div> --%>
							<c:if test="<%=commerceOrder.getShippingAddress()!=null && flagNonExam %>">
								<br>
								<h6><b>Shipping Address</b></h6>
								<div class="box-candidate">
									<div class="title-name">Name</div>
		
									<div class="dot">:</div>
		
									<div class="isi-title"><%=HtmlUtil.escape(commerceOrder.getShippingAddress().getName()) %></div>
								</div>
								
								<div class="box-candidate">
									<div class="title-name">Phone Address</div>
	
									<div class="dot">:</div>
	
									<div class="isi-title"><%=HtmlUtil.escape(commerceOrder.getShippingAddress().getPhoneNumber()) %></div>
								</div>
								<%
									String addressShipping = "";
									String regionNameShipping = "";
									if(commerceOrder.getShippingAddress().getStreet1()!=null && !commerceOrder.getShippingAddress().getStreet1().isEmpty()) {
										addressShipping = commerceOrder.getShippingAddress().getStreet1();
									}
									if(commerceOrder.getShippingAddress().getStreet2()!=null && !commerceOrder.getShippingAddress().getStreet2().isEmpty()) {
										addressShipping = !addressShipping.isEmpty()? addressShipping+"<br>"+ commerceOrder.getShippingAddress().getStreet2() : commerceOrder.getShippingAddress().getStreet2();
									}
									if(commerceOrder.getShippingAddress().getStreet3()!=null && !commerceOrder.getShippingAddress().getStreet3().isEmpty()) {
										addressShipping = !addressShipping.isEmpty()? addressShipping+"<br>"+ commerceOrder.getShippingAddress().getStreet3() : commerceOrder.getShippingAddress().getStreet3();
									}
									
									if(commerceOrder.getShippingAddress().getCommerceRegion()!=null && commerceOrder.getShippingAddress().getCommerceRegion().getName()!=null){
										regionNameShipping = commerceOrder.getShippingAddress().getCommerceRegion().getName();
									}
								%>
								<div class="box-candidate">
									<div class="title-name">Address</div>
	
									<div class="dot">:</div>
	
									<div class="isi-title"><%=HtmlUtil.escape(addressShipping) %></div>
								</div>
	
								<div class="box-candidate">
									<div class="title-name">Postal Code</div>
	
									<div class="dot">:</div>
	
									<div class="isi-title"><%=HtmlUtil.escape(commerceOrder.getShippingAddress().getZip())%></div>
								</div>
	
								<div class="box-candidate">
									<div class="title-name">City</div>
	
									<div class="dot">:</div>
	
									<div class="isi-title"><%=HtmlUtil.escape(commerceOrder.getShippingAddress().getCity())%></div>
								</div>
	
								<div class="box-candidate">
									<div class="title-name">Region</div>
	
									<div class="dot">:</div>
	
									<div class="isi-title"><%=HtmlUtil.escape(regionNameShipping) %></div>
								</div>
							</c:if>
						</div>
					</c:if>
				</div>
			</div>
        </div>
</section>
<script>
    $(document).ready(function () {
        $(".heading-list").click(function () {
            var id = $(this).attr("data-id");
            $(id).toggleClass("active");
            $(this).toggleClass("active");
        });
    });
    
   /*  function downloadPdf(data) {
		var linkSource = 'data:application/pdf;base64,' + data;
		var downloadLink = document.createElement('a');
		document.body.appendChild(downloadLink);

		downloadLink.href = linkSource;
		downloadLink.target = '_self';
		downloadLink.download = 'CITREP.pdf';
		downloadLink.click();

	} */
    
    function downloadPdf(data) {
		<%
			String citrepPdf = (String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Pdf");
		%>
		const pdf = '<%=citrepPdf%>';
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
		downloadLink.download = 'CITREP.pdf';
		downloadLink.click();
	}
</script>
