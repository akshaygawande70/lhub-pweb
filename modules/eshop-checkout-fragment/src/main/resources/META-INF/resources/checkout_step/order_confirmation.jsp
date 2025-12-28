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
<%-- <%@page import="javax.portlet.PortletURL"%>
<%@page import="javax.portlet.WindowState"%>
<%@page import="com.liferay.portal.kernel.portlet.PortletURLFactoryUtil"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayPortletMode"%>
<%@page import="com.liferay.portal.kernel.security.auth.AuthTokenUtil"%>
<%@page import="com.liferay.portal.kernel.util.PortalUtil"%>
<%@page import="com.liferay.portal.kernel.model.Layout"%>
<%@page import="com.liferay.portal.kernel.service.LayoutLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.model.PortletPreferences" %>
<%@page import="java.util.List" %>
<%@page import="com.liferay.portal.kernel.service.PortletPreferencesLocalServiceUtil" %>
<%@page import="java.util.stream.Collectors" %>
<%@page import="com.liferay.portal.kernel.model.User" %>
<%@page import="com.liferay.portal.kernel.model.Role" %>
<%@page import="com.liferay.portal.kernel.service.RoleServiceUtil" %>
<%@page import="com.liferay.commerce.account.model.CommerceAccount" %>
<%@page import="com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil" %>
<%@page import="java.util.ArrayList" %> --%>
<%
	OrderConfirmationCheckoutStepDisplayContext orderConfirmationCheckoutStepDisplayContext = (OrderConfirmationCheckoutStepDisplayContext) request
			.getAttribute(CommerceCheckoutWebKeys.COMMERCE_CHECKOUT_STEP_DISPLAY_CONTEXT);

	CommerceOrderPayment commerceOrderPayment = orderConfirmationCheckoutStepDisplayContext
			.getCommerceOrderPayment();
	CommerceOrder commerceOrder = orderConfirmationCheckoutStepDisplayContext.getCommerceOrder();
	String commerceOrderPaymentContent = null;

	if (commerceOrderPayment != null) {
		commerceOrderPaymentContent = commerceOrderPayment.getContent();
	}

	int paymentStatus = CommerceOrderPaymentConstants.STATUS_PENDING;

	if (commerceOrderPayment != null) {
		paymentStatus = commerceOrderPayment.getStatus();
	}
	String invoiceURL = (String) commerceOrder.getExpandoBridge().getAttribute("Order Link");
%>
<div class="row justify-content-center">
	<div align="center" class="alert alert-warning">
	    Please do not click on your browser's back button. <br/>Instead, navigate by clicking on the buttons below.
	</div>
</div>
<div class="commerce-checkout-confirmation">
	<c:choose>
		<c:when
			test="<%=(paymentStatus == CommerceOrderPaymentConstants.STATUS_CANCELLED)
							|| (paymentStatus == CommerceOrderPaymentConstants.STATUS_FAILED)%>"
		>
			<%-- <section class="payment-completed">
				<div class="container">
					<div class="row justify-content-center">
						<div class="box-payment text-center">
							<div class="alert alert-warning">
								<%
									String taglibMessageKey = "an-error-occurred-while-processing-your-payment";
											String taglibValue = "retry";

											if (paymentStatus == CommerceOrderPaymentConstants.STATUS_CANCELLED) {
												taglibMessageKey = "your-payment-has-been-cancelled";
												taglibValue = "pay-now";
											}
								%>
								<div class="medium-bold">
									<liferay-ui:message key="<%=taglibMessageKey%>" />
								</div>
								<aui:button-row>
									<a class="btn-blue" href="<%=invoiceURL%>">My Order</a>
									<aui:button cssClass="btn-blue"
										href="<%=orderConfirmationCheckoutStepDisplayContext.getRetryPaymentURL()%>"
										type="submit" value="<%=taglibValue%>"
									/>
								</aui:button-row>
							</div>
						</div>
					</div>
				</div>
			</section> --%>
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
							<a class="btn-blue" href="<%=orderConfirmationCheckoutStepDisplayContext.getRetryPaymentURL()%>">Continue to payment</a>
							<div class="abstrak">OR</div>
							<div class="abstrak">Review your current order here:</div>
							<a class="btn-blue" href="<%=invoiceURL%>">View my order</a>
							<div class="abstrak">
								<i aria-hidden="true" class="far fa-arrow-alt-circle-right"></i>
								If you wish to cancel the order and look at the other exams:
							</div>
							<div class="link-back">
								<a href="/e-shop"><i aria-hidden="true" class="fas fa-angle-left"></i>
									Back to e-shop product listing page</a>
							</div>
						</div>
					</div>
				</div>
			</section>
		</c:when>
		<c:when
			test="<%=paymentStatus == CommerceOrderPaymentConstants.STATUS_COMPLETED%>"
		>
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
							<%
								/*int accType = 0;
																						CommerceAccount account2 = null;
																						List<Role> roleList = new ArrayList<Role>();
																						for (Long roleId : user.getRoleIds()) {
																							Role newRole = RoleServiceUtil.getRole(roleId);
																							roleList.add(newRole);
																						}
																						
																						account2 = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());
																						
																						for(Role role : roleList) {
																							if(role.getName().equals("Eshop_Individual_Role")) {
																								accType = 1;
																							} else if(role.getName().equals("Eshop_Corporate_Role")){
																								accType = 2;
																							}
																						}
																						
																						String authToken = AuthTokenUtil.getToken(request);
																						String invoicePortletName = "NTUC_Invoice_Exam_Portlet";
																						String invoiceUrl = "/account/examinvoices";
																						Layout layout2 = LayoutLocalServiceUtil.getFriendlyURLLayout(themeDisplay.getSiteGroupId(), false, invoiceUrl);
																						long plid2 = layout2.getPlid();
																						List<PortletPreferences> preferences = PortletPreferencesLocalServiceUtil.getPortletPreferencesByPlid(plid2);
																						List<PortletPreferences> filteredPreferences = preferences.stream().filter(x->x.getPortletId().contains(invoicePortletName)).collect(Collectors.toList());
																						String portletId2 = filteredPreferences.get(0).getPortletId();
																						PortletURL invoiceURL = PortletURLFactoryUtil.create(request, portletId2, plid2 ,
																							PortletRequest.RENDER_PHASE);
																						invoiceURL.setParameter("mvcRenderCommandName", "examOrderDetailRender");
																						invoiceURL.setParameter("accType", String.valueOf(accType));
																						invoiceURL.setParameter("orderId", String.valueOf(commerceOrder.getCommerceOrderId()));
																						invoiceURL.setParameter("authToken", authToken);
																						invoiceURL.setWindowState(LiferayWindowState.MAXIMIZED);
																						invoiceURL.setPortletMode(LiferayPortletMode.VIEW);*/
							%>
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
		<c:otherwise>
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
									<aui:button cssClass="btn-blue"
										href="<%=orderConfirmationCheckoutStepDisplayContext.getRetryPaymentURL()%>"
										type="submit" value="<%=taglibValue%>"
									/>
								</aui:button-row>
							</div>
						</div>
					</div>
				</div>
			</section> --%>
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
							<a class="btn-blue" href="<%=orderConfirmationCheckoutStepDisplayContext.getRetryPaymentURL()%>">Continue to payment</a>
							<div class="abstrak">OR</div>
							<div class="abstrak">Review your current order here:</div>
							<a class="btn-blue" href="<%=invoiceURL%>">View my order</a>
							<div class="abstrak">
								<i aria-hidden="true" class="far fa-arrow-alt-circle-right"></i>
								If you wish to cancel the order and look at the other exams:
							</div>
							<div class="link-back">
								<a href="/e-shop"><i aria-hidden="true" class="fas fa-angle-left"></i>
									Back to e-shop product listing page</a>
							</div>
						</div>
					</div>
				</div>
			</section>
		</c:otherwise>
	</c:choose>
</div>
<script type="text/javascript">
	/* function downloadPdf(data) {
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