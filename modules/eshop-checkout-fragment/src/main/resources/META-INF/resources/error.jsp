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
<%@page import="com.liferay.commerce.service.CommerceOrderPaymentLocalServiceUtil"  %>
<%@ include file="/init.jsp"%>
<%
	CommerceOrder commerceOrder = (CommerceOrder) request.getAttribute(CommerceCheckoutWebKeys.COMMERCE_ORDER);
	String invoiceURL = (String) commerceOrder.getExpandoBridge().getAttribute("Order Link");
	String paymentUrl = themeDisplay.getPortalURL();
	paymentUrl += "/checkout/-/checkout/payment-process/"+commerceOrder.getUuid();
	int paymentStatus = 12345;
	
	CommerceOrderPayment cop = CommerceOrderPaymentLocalServiceUtil.fetchLatestCommerceOrderPayment(commerceOrder.getCommerceOrderId());
	
	String contentCop = cop.getContent().replace("[", "").replace("]", "");
	if((Validator.isNotNull(contentCop) && !contentCop.isEmpty()) || cop.getStatus()==0) {
		paymentStatus = 0;
	}
%>
<div class="commerce-checkout-confirmation">
	<c:choose>
		<c:when test="<%=paymentStatus!=12345%>">
			<section class="payment-completed">
				<div class="container">
					<div class="row justify-content-center">
						<div class="box-payment text-center">
							<div class="medium-bold">ORDER CONFIRMATION</div>
							<img alt="" class="img-fluid"
								src="/o/ntuclearninghub-commerce-theme/images/payment-sc.jpg"
							/>
							<div class="medium-bold">Your order has been succesfully placed!</div>
							<div class="normal">
								We will process payment after the work is completed<br />An
								email confirmation will be sent to you shortly with your order
								details
							</div>
							<div class="order-number">
								You order number : <span class="number"><a
									href="<%=invoiceURL%>"
								><%=commerceOrder.getCommerceOrderId()%></a></span>
							</div>
							<div class="btn-order">
								<a class="btn-blue"
									href="<%=themeDisplay.getPortalURL()%>/account/examinvoices"
								>View my order</a>
							</div>
							<%
								boolean isCitrep = (boolean) commerceOrder.getExpandoBridge().getAttribute("CITREP");
										String citrepPdf = (String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Pdf");
							%>
							<div class="btn-form">
								<c:if test="<%=isCitrep%>">
									<a onclick="downloadPdf('<%=citrepPdf%>')"><span style="color: #0097ce">Download Citrep Form
										Here</span></a>
								</c:if>
							</div>
						</div>
					</div>
				</div>
			</section>
		</c:when>
		<c:when test="<%=paymentStatus==12345%>">
			<section class="choice-payment">
				<div class="container">
					<div class="row">
						<div class="box-content">
							<div class="title">
								<i class="fas fa-exclamation bd"></i> PAYMENT INCOMPLETE OR
								CANCELLED
							</div>
							<div class="abstrak">
								<i aria-hidden="true" class="far fa-arrow-alt-circle-right"></i>
								If you wish to continue with the payment, please proceed here:
							</div>
							<a class="btn-blue"
								href="<%=paymentUrl%>"
							>Continue to payment</a>
							<div class="abstrak">OR</div>
							<div class="abstrak">Review your current order here:</div>
							<a class="btn-blue" href="<%=invoiceURL%>">My Order</a>
							<div class="abstrak">
								<i aria-hidden="true" class="far fa-arrow-alt-circle-right"></i>
								If you wish to cancel the order and look at the other exams:
							</div>
							<div class="link-back">
								<a href="/e-shop"><i aria-hidden="true"
									class="fas fa-angle-left"
								></i> Back to e-shop product listing page</a>
							</div>
						</div>
					</div>
				</div>
			</section>
		</c:when>
	</c:choose>
	<%-- <section class="payment-completed">
		<div class="container">
			<div class="row justify-content-center">
				<div class="box-payment text-center">
					<div class="alert alert-success">
						<div class="medium-bold">
							<liferay-ui:message
								key="your-order-has-been-processed-but-not-completed-yet"
							/>
						</div>
						<aui:button-row>
							<a class="btn-blue" href="<%=invoiceURL%>">Pay Now</a>
						</aui:button-row>
					</div>
				</div>
			</div>
		</div>
	</section> --%>
	<%-- <div class="success-message">
		<liferay-ui:message key="your-order-has-already-been-placed" />
	</div>
	<aui:button-row>
		<aui:button
			href="<%=(String) request.getAttribute(CommerceCheckoutWebKeys.COMMERCE_CHECKOUT_STEP_ORDER_DETAIL_URL)%>"
			primary="<%=true%>" type="submit" value="go-to-order-details"
		/>
	</aui:button-row> --%>
</div>