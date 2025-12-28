<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>
<%@ include file="/init.jsp"%>
<%@page import="com.liferay.commerce.model.CommerceOrderItem"%>
<%@page import="com.liferay.commerce.tax.engine.fixed.service.CommerceTaxFixedRateLocalServiceUtil"%>
<%@page import="com.liferay.commerce.tax.engine.fixed.model.CommerceTaxFixedRate"%>
<%
	OrderSummaryCheckoutStepDisplayContext orderSummaryCheckoutStepDisplayContext = (OrderSummaryCheckoutStepDisplayContext) request
			.getAttribute(CommerceCheckoutWebKeys.COMMERCE_CHECKOUT_STEP_DISPLAY_CONTEXT);

	CommerceOrder commerceOrder = orderSummaryCheckoutStepDisplayContext.getCommerceOrder();
	
	// product discount
	CommerceOrderItem commerceOrderItem2 = commerceOrder.getCommerceOrderItems().get(0);
	CommerceProductPrice commerceProductPriceItem = orderSummaryCheckoutStepDisplayContext.getCommerceProductPrice(commerceOrderItem2);
	CommerceDiscountValue itemTotalDiscountValue = commerceProductPriceItem.getDiscountValue();
	
	
	
	CommerceOrderPrice commerceOrderPrice = orderSummaryCheckoutStepDisplayContext.getCommerceOrderPrice();

	CommerceDiscountValue shippingDiscountValue = commerceOrderPrice.getShippingDiscountValue();
	CommerceMoney shippingValueCommerceMoney = commerceOrderPrice.getShippingValue();
	CommerceMoney subtotalCommerceMoney = commerceOrderPrice.getSubtotal();
	CommerceDiscountValue subtotalCommerceDiscountValue = commerceOrderPrice.getSubtotalDiscountValue();
	CommerceMoney taxValueCommerceMoney = commerceOrderPrice.getTaxValue();
	CommerceDiscountValue totalCommerceDiscountValue = commerceOrderPrice.getTotalDiscountValue();
	CommerceMoney totalOrderCommerceMoney = commerceOrderPrice.getTotal();
	
	double commerceTaxFixedRate = CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).get(0).getRate();
	
	String priceDisplayType = orderSummaryCheckoutStepDisplayContext.getCommercePriceDisplayType();

	if (priceDisplayType.equals(CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {
		shippingDiscountValue = commerceOrderPrice.getShippingDiscountValueWithTaxAmount();
		shippingValueCommerceMoney = commerceOrderPrice.getShippingValueWithTaxAmount();
		subtotalCommerceMoney = commerceOrderPrice.getSubtotalWithTaxAmount();
		subtotalCommerceDiscountValue = commerceOrderPrice.getSubtotalDiscountValueWithTaxAmount();
		totalCommerceDiscountValue = commerceOrderPrice.getTotalDiscountValueWithTaxAmount();
		totalOrderCommerceMoney = commerceOrderPrice.getTotalWithTaxAmount();
	}
	// product discount
	subtotalCommerceMoney = commerceProductPriceItem.getUnitPrice();
	itemTotalDiscountValue = commerceProductPriceItem.getDiscountValue();
	
	
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
	
%>
<style>
.currency {
	color: #18355e;
	text-align: right;
	white-space: nowrap;
}
.currency:before {
   content: "$";
   float: left;
   padding-right: 2px;
}
.price-content.currency:before {
   content: "$";
   float: left;
   font-size: 20px;
   padding-right: 2px;
}
</style>
<div class="commerce-order-summary">
	<liferay-ui:error
		exception="<%=CommerceDiscountLimitationTimesException.class%>"
		message="the-inserted-coupon-code-has-reached-its-usage-limit"
	/>
	<liferay-ui:error
		exception="<%=CommerceOrderBillingAddressException.class%>"
		message="please-select-a-valid-billing-address"
	/>
	<liferay-ui:error
		exception="<%=CommerceOrderGuestCheckoutException.class%>"
		message="you-must-sign-in-to-complete-this-order"
	/>
	<liferay-ui:error
		exception="<%=CommerceOrderPaymentMethodException.class%>"
		message="please-select-a-valid-payment-method"
	/>
	<liferay-ui:error
		exception="<%=CommerceOrderShippingAddressException.class%>"
		message="please-select-a-valid-shipping-address"
	/>
	<liferay-ui:error
		exception="<%=CommerceOrderShippingMethodException.class%>"
		message="please-select-a-valid-shipping-method"
	/>
	<liferay-ui:error exception="<%=NoSuchDiscountException.class%>"
		message="the-inserted-coupon-is-no-longer-valid"
	/>
	<section class="order-view">
		<div class="container">
			<div class="row justify-content-center">
				<div class="col-md-6">
					<div class="box-review">
						<div class="title">Order Summary</div>
						<%
							for (CommerceOrderItem commerceOrderItem : commerceOrder.getCommerceOrderItems()) {
								CPDefinition cpDefinition = commerceOrderItem.getCPDefinition();
								
								
								
						%>
						<div class="catalog"><%=HtmlUtil.escape(cpDefinition.getName(themeDisplay.getLanguageId()))%></div>
						<div class="box-content">
							<div class="img-content col-4">
								<img alt="" class="img-fluid"
									src="<%=orderSummaryCheckoutStepDisplayContext.getCommerceOrderItemThumbnailSrc(commerceOrderItem)%>"
								/>
							</div>
							<%-- <div class="title-content"><%=HtmlUtil.escape(cpDefinition.getShortDescription(themeDisplay.getLanguageId()))%></div> --%>
							<%
									CommerceProductPrice commerceProductPrice = orderSummaryCheckoutStepDisplayContext
											.getCommerceProductPrice(commerceOrderItem);
									CPInstance cpInstance = commerceOrderItem.fetchCPInstance();
									
									
									//get final price per unit
									int quantity = commerceProductPrice.getQuantity();
									float pricePerUnitFloat = Float.valueOf(commerceProductPrice.getUnitPrice().format(locale)).floatValue();
									//System.out.println("Unit Price = "+pricePerUnitFloat);
									
									//CommerceMoney finalPricePerUnit = commerceOrderItem.getUnitPriceWithTaxAmountMoney();
									CommerceMoney finalPricePerUnit = commerceOrderItem.getFinalPriceMoney();
									float totalPricePerUnit = Float.valueOf(finalPricePerUnit.format(locale)).floatValue();;
									//System.out.println("totalPricePerUnit (with tax amount)= "+totalPricePerUnit);
									String totalPricePerUnitString = String.valueOf(totalPricePerUnit);
									
									subTotalNew = subTotalNew + totalPricePerUnit;
									subTotalNewString = String.valueOf(subTotalNew);
							%>
							<c:if test="<%=commerceProductPrice != null%>">
								<%
								    CommerceMoney finalPriceCommerceMoney = commerceOrderItem.getUnitPriceWithTaxAmountMoney();
									CommerceMoney unitPriceCommerceMoney = commerceProductPrice.getUnitPrice();
									CommerceMoney unitPromoPriceCommerceMoney = commerceProductPrice.getUnitPromoPrice();

									if (priceDisplayType.equals(CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {
										unitPriceCommerceMoney = commerceProductPrice.getUnitPriceWithTaxAmount();
										unitPromoPriceCommerceMoney = commerceProductPrice.getUnitPromoPriceWithTaxAmount();
									}
									
									CommerceMoney unitPriceCommerceMoneyNew = commerceOrderItem.getFinalPriceMoney();
									
								%>
								<div class="price-content bold-blue col-6 pull-right">
									<%-- <c:choose>
										<c:when
											test="<%=!unitPromoPriceCommerceMoney.isEmpty() && CommerceBigDecimalUtil.gt(unitPromoPriceCommerceMoney.getPrice(), BigDecimal.ZERO)%>">
											<span class="price-value price-value-promo"> $<%=HtmlUtil.escape(unitPromoPriceCommerceMoney.format(locale))%>
											</span>
											<span class="price-value price-value-inactive row ml-1">
												$<%=HtmlUtil.escape(unitPriceCommerceMoney.format(locale))%>
											</span>
										</c:when>
										<c:otherwise>
											<span class="price-value {$additionalPriceClasses}">
												$<%=HtmlUtil.escape(unitPriceCommerceMoney.format(locale))%>
											</span>
										</c:otherwise>
									</c:choose> --%>
									<span class="price-value {$additionalPriceClasses}">
										$<%=HtmlUtil.escape(unitPriceCommerceMoneyNew.format(locale))%>
									</span>
								</div>
							</c:if>
						</div>
						<%
							}
						%>
						<div class="box-content">
							<hr class="aaa" />
							<div class="row">
								<div class="col-6">Sub total</div>
								<div class="col-6">
									<%-- <%=HtmlUtil.escape(subtotalCommerceMoney.format(locale))%> --%>
									$<%=subTotalNewString%>
									
								</div>
							</div>
							<c:if test="<%=subtotalCommerceDiscountValue != null%>">
								<%
									CommerceMoney subtotalDiscountAmountCommerceMoney = subtotalCommerceDiscountValue.getDiscountAmount();
								%>
								<div class="row">
									<div class="col-6">
										Discount
										<%=HtmlUtil.escape(orderSummaryCheckoutStepDisplayContext
						.getLocalizedPercentage(subtotalCommerceDiscountValue.getDiscountPercentage(), locale))%></div>
									<div class="col-6">
										<%=HtmlUtil.escape(subtotalDiscountAmountCommerceMoney.format(locale))%></div>
								</div>
							</c:if>
							<c:if test="<%=shippingDiscountValue != null%>">
								<%
									CommerceMoney shippingDiscountAmountCommerceMoney = shippingDiscountValue.getDiscountAmount();
								%>
								<div class="row">
									<div class="col-6">
										Shipment
										<%=HtmlUtil.escape(orderSummaryCheckoutStepDisplayContext
						.getLocalizedPercentage(shippingDiscountValue.getDiscountPercentage(), locale))%></div>
									<div class="col-6">
										<%=HtmlUtil.escape(shippingDiscountAmountCommerceMoney.format(locale))%></div>
								</div>
							</c:if>
							<%
								CommerceMoney totalDiscountAmountCommerceAmount = null;
								if(totalCommerceDiscountValue != null) {
									totalDiscountAmountCommerceAmount = totalCommerceDiscountValue.getDiscountAmount();
								} 
								
								if(itemTotalDiscountValue != null) {
									totalDiscountAmountCommerceAmount = itemTotalDiscountValue.getDiscountAmount();
									totalCommerceDiscountValue = itemTotalDiscountValue;
								}
							%>
							<c:if test="<%=totalCommerceDiscountValue != null || itemTotalDiscountValue != null%>">
								<div class="row">
									<div class="col-6">
										Total Discount
										<%=HtmlUtil.escape(orderSummaryCheckoutStepDisplayContext
						.getLocalizedPercentage(totalCommerceDiscountValue.getDiscountPercentage(), locale))%></div>
									<div class="col-6">
										$<%=HtmlUtil.escape(totalDiscountAmountCommerceAmount.format(locale))%></div>
								</div>
							</c:if>
							<c:if test="<%=priceDisplayType.equals(CommercePricingConstants.TAX_EXCLUDED_FROM_PRICE)%>">
								<div class="row">
									<div class="col-6">GST (<%=commerceTaxFixedRate%>%)</div>
									<div class="col-6">
										$<%=HtmlUtil.escape(taxValueCommerceMoney.format(locale))%></div>
								</div>
							</c:if> 
							<%-- div class="row">
								<div class="col-6">GST (<%=commerceTaxFixedRate%>%)</div>
								<div class="col-6 bold-blue currency">
									<%=HtmlUtil.escape(taxValueCommerceMoney.format(locale))%></div>
							</div> --%>
							<hr class="aaa" />
							<div class="row">
								<div class="col-6">Total</div>
								<div class="col-6">
									$<%=HtmlUtil.escape(totalOrderCommerceMoney.format(locale))%>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
</div>