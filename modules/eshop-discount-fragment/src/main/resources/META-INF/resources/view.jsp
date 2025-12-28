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
<%
	CommerceContext commerceContext = (CommerceContext) request.getAttribute(CommerceWebKeys.COMMERCE_CONTEXT);

	String couponCode = null;
	boolean isThereAnyCoupon = false;
	CommerceOrder commerceOrder = commerceContext.getCommerceOrder();

	if (commerceOrder != null) {
		couponCode = commerceOrder.getCouponCode();
		
		long comId = commerceOrder.getCommerceOrderId();
		String userName = commerceOrder.getUserName();
	}

	if (Validator.isNotNull(couponCode)) {
		isThereAnyCoupon = true;
	}
	else{
		
	}
	
	
%>
<!-- <portlet:actionURL name="/commerce_discount_content/apply_commerce_discount_coupon_code" var="applyCommerceDiscountCouponCodeActionURL" /> -->

<portlet:resourceURL id="/commerce_discount_content/apply_coupon_ajax" var="applyCommerceDiscountCouponCodeActionURL" >
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<section class="cart-detail">
	<div class="container">
		<div class="row">
			<table>
				<tbody class="float-right">
					<tr class="code-voucer">
					
						
						<aui:form action="<%=applyCommerceDiscountCouponCodeActionURL%>" name="fCoupon" id="fCoupon">
						
							<liferay-ui:error key="discount-failed" message="Please enter a valid discount code." />
							
							<liferay-ui:error key="product-not-assigned" message="Selected product has not been registered in this promo code" />
							
							<liferay-ui:success key="discount-applied" message="Your discount code successfully applied" />
							
							<liferay-ui:success key="discount-removed" message="Your discount code successfully removed" />
							
							<aui:input name="redirect" type="hidden" value="<%=currentURL%>" />
							<c:choose>
								<c:when test="<%=Validator.isNull(commerceOrder)%>">
								</c:when>
								<c:when test="<%=Validator.isNotNull(couponCode)%>">
									<aui:input name="<%=Constants.CMD%>" type="hidden"
										value="<%=Constants.REMOVE%>"
									/>
									<td colspan="5">
										<div class="box-tags" id="closevc1">
											<i class="fas fa-tags"></i><span><%=HtmlUtil.escape(couponCode)%></span>
											<a href="javascript:;"
												id="<portlet:namespace />couponCodeIconRemove"
											><i
												class="fas fa-times close" 
											></i></a>
										</div>
									</td>
									
									<script>
										$('#<portlet:namespace/>couponCodeIconRemove').click(function(){
											var couponCode = document.getElementById("<portlet:namespace />couponCode").value;
											$.ajax({
												url : "${applyCommerceDiscountCouponCodeActionURL}",
												type : 'POST',
												datatype : 'json',
												data : {
													<portlet:namespace/>couponCode : couponCode, <portlet:namespace/>cmd : "remove"
												},
												success : function(data) {
													location.reload();
												}
											});
										});
									</script>
					
								</c:when>
							</c:choose>
							
							<td>
								<aui:input name="<%=Constants.CMD%>" type="hidden" value="<%=Constants.ADD%>"/> 
								<input placeholder="Enter Gift voucher or Discount code" name="<portlet:namespace/>couponCode" id="<portlet:namespace/>couponCode"type="text"/>
							</td>
							<td>
								<button class="btn-code-voucer" type="button" value="apply" id="<portlet:namespace />couponButtonSubmit">Apply</button> 
							</td>
							
						</aui:form>
						
						<script>
							$('#<portlet:namespace/>couponButtonSubmit').click(function(){
								var couponCode = document.getElementById("<portlet:namespace />couponCode").value;
								$.ajax({
									url : "${applyCommerceDiscountCouponCodeActionURL}",
									type : 'POST',
									datatype : 'json',
									data : {
										<portlet:namespace/>couponCode : couponCode, <portlet:namespace/>cmd : "add"
									},
									success : function(data) {
										var content = JSON.parse(data);
										var status = content.status;
										location.reload();
									}
								});
							});
						</script>
							
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</section>



