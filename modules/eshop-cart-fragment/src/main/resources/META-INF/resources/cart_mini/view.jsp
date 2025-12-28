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

<%@page import="com.liferay.commerce.price.CommerceProductPrice"%>
<%@page import="com.liferay.commerce.product.type.virtual.constants.VirtualCPTypeConstants"%>
<%@ page import="com.liferay.commerce.constants.CommercePortletKeys" %>
<%@page import="com.liferay.commerce.tax.engine.fixed.service.CommerceTaxFixedRateLocalServiceUtil"%>
<%@page import="com.liferay.commerce.model.CommerceOrder"%>
<%@ include file="/init.jsp" %>

<%
CommerceCartContentMiniDisplayContext commerceCartContentMiniDisplayContext = (CommerceCartContentMiniDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
CommerceCartContentDisplayContext commerceCartContentDisplayContext = (CommerceCartContentDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

String addToCartId = PortalUtil.generateRandomKey(request, "add-to-cart");

Map<String, Object> contextObjects = HashMapBuilder.<String, Object>put(
	"commerceCartContentMiniDisplayContext", commerceCartContentMiniDisplayContext
).build();

CommerceMoney taxValueCommerceMoney = null;
CommerceMoney totalOrderCommerceMoney = null;

CommerceMoney shippingValueCommerceMoney = null;

String priceDisplayType = commerceCartContentMiniDisplayContext.getCommercePriceDisplayType();

double commerceTaxFixedRate = CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).size()>0?
		CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).get(0).getRate():0;

CommerceOrderPrice commerceOrderPrice = commerceCartContentMiniDisplayContext.getCommerceOrderPrice();

CommerceOrder commerceOrder = commerceCartContentDisplayContext.getCommerceOrder();
float discountFromCommerceOrder = 0;
if(commerceOrder!=null){
	discountFromCommerceOrder = commerceOrder.getShippingDiscountAmount().floatValue() +
			commerceOrder.getSubtotalDiscountAmount().floatValue() + 
			commerceOrder.getTotalDiscountAmount().floatValue();
}
float discountAmountFloat = 0;
float totalProductPrice = 0;

if (commerceOrderPrice != null) {
	taxValueCommerceMoney = commerceOrderPrice.getTaxValue();

	totalOrderCommerceMoney = commerceOrderPrice.getTotal();
	shippingValueCommerceMoney = commerceOrderPrice.getShippingValueWithTaxAmount();

	if (priceDisplayType.equals(CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {
		totalOrderCommerceMoney = commerceOrderPrice.getTotalWithTaxAmount();
	}
}


SearchContainer<CommerceOrderItem> commerceOrderItemSearchContainer = commerceCartContentMiniDisplayContext.getSearchContainer();

PortletURL portletURL = commerceCartContentMiniDisplayContext.getPortletURL();

portletURL.setParameter("searchContainerId", "commerceOrderItems");

request.setAttribute("view.jsp-portletURL", portletURL);
%>
<c:if test="<%= commerceOrderItemSearchContainer.getResults().size() > 0 %>">
<liferay-ddm:template-renderer
	className="<%=CommerceCartContentMiniPortlet.class.getName()%>"
	contextObjects="<%=contextObjects%>"
	displayStyle="<%=commerceCartContentMiniDisplayContext.getDisplayStyle()%>"
	displayStyleGroupId="<%=commerceCartContentMiniDisplayContext.getDisplayStyleGroupId()%>"
	entries="<%=commerceOrderItemSearchContainer.getResults()%>">
	<section class="mini-cart-custom-cxrus">
		<div class="btn-close-cart">
			<i class="fas fa-times"></i>
		</div>
		<div class="title">Shopping cart</div>
		<div class="box-list-cart scroll-small">
			<div class="table">
				<table>
					<thead class="bb1px">
						<tr>
							<th colspan="4">Product</th>
							<th>Quantity</th>
							<th>Price</th>
						</tr>
					</thead>
					<!--Start catalog cart-->
					<tbody>
						<liferay-ui:search-container
							cssClass="list-group-flush"
							id="commerceOrderItems"
							iteratorURL="<%= portletURL %>"
							searchContainer="<%= commerceOrderItemSearchContainer %>"
						>
							<liferay-ui:search-container-row
								className="com.liferay.commerce.model.CommerceOrderItem"
								cssClass="entry-display-style"
								keyProperty="CommerceOrderItemId"
								modelVar="commerceOrderItem"
							>

							<%
								CPDefinition cpDefinition = null;
								if(commerceOrderItem!=null){
									cpDefinition = commerceOrderItem.getCPDefinition();
								}
							
								if(commerceOrderItem!=null){
									discountAmountFloat = discountAmountFloat + commerceOrderItem.getDiscountAmount().floatValue();
								}
								
							%>
							<tr class="ball1px">
								<td colspan="3"><img alt="" class="img-fluid"
									src="<%= commerceCartContentMiniDisplayContext.getCommerceOrderItemThumbnailSrc(commerceOrderItem) %>" /></td>
								<td><span class="title-img"><%= HtmlUtil.escape(cpDefinition.getName(languageId)) %></span></td>
								<td class="price-check">
									<c:if test="<%=cpDefinition.getProductTypeName().equals(VirtualCPTypeConstants.NAME) == false %>">
										<liferay-commerce-cart:quantity-control
											commerceOrderItemId="<%= commerceOrderItem.getCommerceOrderItemId() %>"
											useSelect="<%= true %>"
											updateOnChange="<%= true %>"
										/>
									</c:if>
									<c:if test="<%=cpDefinition.getProductTypeName().equals(VirtualCPTypeConstants.NAME) == true %>">
										<%=commerceOrderItem.getQuantity()%>
									</c:if>
								</td>
								<td class="price-check">
									<%
		                            	float getPriceUnit = commerceOrderItem.getPromoPrice()!=null && commerceOrderItem.getPromoPrice().floatValue()>0 ? 
		                            			commerceOrderItem.getPromoPrice().floatValue() : commerceOrderItem.getUnitPrice().floatValue() ;
		                            	float getPriceQty = getPriceUnit*commerceOrderItem.getQuantity();
		                            	totalProductPrice = totalProductPrice + getPriceQty;
		                            	System.out.println(getPriceUnit + " = getPriceUnit");
		                            %>
		                            $ <%=HtmlUtil.escape(String.format(locale, "%,.2f", getPriceUnit))%>
								</td>
								<portlet:actionURL var="actionDeleteURL"
									name="/commerce_cart_content/edit_commerce_order_item">
									<portlet:param name="<%=Constants.CMD %>" value="<%=Constants.DELETE %>" />
									<portlet:param name="redirect" value="<%= commerceCartContentMiniDisplayContext.getCPDefinitionURL(cpDefinition.getCPDefinitionId(), themeDisplay) %>" />
									<portlet:param name="commerceOrderItemId" value="<%=commerceOrderItem.getCommerceOrderItemId()+"" %>" />
								</portlet:actionURL>
								<td><a href="${actionDeleteURL }">
													<i class="fas fa-trash-alt text-center delete"></i></a></td>
							</tr>
							
							</liferay-ui:search-container-row>
						</liferay-ui:search-container>
					</tbody>

				</table>
			</div>
		</div>

		<div class="box-total-minicart">
			<div class="box-list">
				<div class="list-title">Sub Total</div>
				<div class="list-price">$ <%=HtmlUtil.escape(String.format(locale, "%,.2f", totalProductPrice))%></div>
			</div>
			<div class="box-list">
				<%
					if(commerceTaxFixedRate == (long) commerceTaxFixedRate) {%>
						<div class="list-title"><%=String.format("%d",(long) commerceTaxFixedRate)%>% GST Included</div>
				<% } else { %>
						<div class="list-title"><%=String.format("%s", commerceTaxFixedRate)%>% GST Included</div>
				<% } %>
				<div class="list-price">$ <%= HtmlUtil.escape(taxValueCommerceMoney.format(locale)) %></div>
			</div>
			<div class="box-list">
				<div class="list-title">Shipment</div>
				<div class="list-price">$ <%= HtmlUtil.escape(shippingValueCommerceMoney.format(locale)) %></div>
			</div>
			<div class="box-list">
				<div class="list-title">Total Discount</div>
				<div class="list-price">$ <%=HtmlUtil.escape(String.format(locale, "%,.2f", (discountAmountFloat+ discountFromCommerceOrder)))%></div>
			</div>
		</div>
		<div class="box-total-all">
			<div class="box-list-total">
				<div class="total-title">Total</div>
				<div class="total-price">$ <%=HtmlUtil.escape(totalOrderCommerceMoney.format(locale))%></div>
			</div>
		</div>
		<div class="box-checkout">
			<liferay-commerce:order-transitions
				commerceOrderId="<%=commerceCartContentMiniDisplayContext.getCommerceOrderId()%>"
				cssClass="btn-checkout"
			/>
		</div>
	</section>
	<script>
		$( document ).ready(function() {
			setTimeout(function(){
				$("#_com_liferay_commerce_cart_content_web_internal_portlet_CommerceCartContentPortlet_redirect").val(window.location.href);
			},500);
		});
	</script>

	<%@ include file="/cart_mini/transition.jspf" %>

	<aui:script use="aui-base">
		var orderTransition = A.one('#<portlet:namespace />orderTransition');

		if (orderTransition) {
			orderTransition.delegate(
				'click',
				function (event) {
					<portlet:namespace />transition(event);
				},
				'.transition-link'
			);
		}
	</aui:script>
	<aui:script>
		Liferay.after('commerce:productAddedToCart', function (event) {
			Liferay.Portlet.refresh('#p_p_id<portlet:namespace />');
		});
	</aui:script>
</liferay-ddm:template-renderer>
</c:if>
<c:if test="<%= commerceOrderItemSearchContainer.getResults().size() <= 0 %>">
<section class="mini-cart-custom-cxrus">
    <div class="btn-close-cart"><i class="fas fa-times"></i></div>
    <div class="title">Shopping cart</div>
    <div class="box-list-cart scroll-small">
        <div class="table">
            <table>
                <thead class="bb1px">
                    <tr>
                        <th class="pd-first "colspan="4">Product</th>
                        <th>Quantity</th>
                        <th>Price</th>
                    </tr>
                </thead>
            </table>
        </div>
    </div>
    <div class="box-nocart">
        <div class="img"><img src="/o/ntuclearninghub-commerce-theme/images/commerce-asset/cart.svg"></div>
        <div class="text">Your shopping cart is empty.<br>Start exploring for products to add into your cart!</div>
    </div>
</section>
</c:if>
