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

<%@page import="com.liferay.portal.kernel.service.RoleServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.portal.kernel.model.Role"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.petra.string.StringPool"%>
<%@page import="api.ntuc.common.util.RoleUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/init.jsp" %>

<%
CommerceAccountDisplayContext commerceAccountDisplayContext = (CommerceAccountDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommerceAccount commerceAccount = commerceAccountDisplayContext.getCurrentCommerceAccount();
CommerceAddress billingAddress = commerceAccountDisplayContext.getDefaultBillingCommerceAddress();
CommerceAddress shippingAddress = commerceAccountDisplayContext.getDefaultShippingCommerceAddress();

User selectedUser = commerceAccountDisplayContext.getSelectedUser();

SimpleDateFormat formatter = new SimpleDateFormat("dd / MM / yyyy");

PortletURL portletURL = commerceAccountDisplayContext.getPortletURL();
portletURL.setParameter("mvcRenderCommandName", "/commerce_account/view_commerce_account");

List<Role> roleList = new ArrayList<Role>();
for (Long roleId : selectedUser.getRoleIds()) {
	Role newRole = RoleServiceUtil.getRole(roleId);
	roleList.add(newRole);
}

String nric = StringPool.BLANK;
String companyName = StringPool.BLANK;
String uenNumber = StringPool.BLANK;
String preferredName = (String) commerceAccount.getExpandoBridge().getAttribute("Preferred Name");
boolean isIndividu = Boolean.FALSE;
if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Individual_Role")) {
	nric = (String) commerceAccount.getExpandoBridge().getAttribute("NRIC");
	isIndividu = Boolean.TRUE;
} else {
	companyName = (String) commerceAccount.getExpandoBridge().getAttribute("Company Name");
	uenNumber = (String) commerceAccount.getExpandoBridge().getAttribute("UEN Number");	
}
%>

<portlet:renderURL var="editCommerceAccountURL">
	<portlet:param name="mvcRenderCommandName" value="/commerce_account/edit_commerce_account" />
	<portlet:param name="commerceAccountId" value="<%= String.valueOf(commerceAccount.getCommerceAccountId()) %>" />
	<portlet:param name='<%= PortletQName.PUBLIC_RENDER_PARAMETER_NAMESPACE + "backURL" %>' value="<%= portletURL.toString() %>" />
</portlet:renderURL>

<section class="general-info myaccount-cxrus">
	<div class="container">
		<div class="row">
			<div class="breadcrumb">
				<ul>
					<li><a href="/home" class="text-dark">Home</a></li>
					<li><a href="/account/profile" class="text-dark">My Account</a></li>
					<li>Personal Info</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="text-center w100">
				<h1 class="h1-commerce">MY ACCOUNT</h1>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="box-name">
				<div class="name" id="capital-name">
					HI,
					<%= HtmlUtil.escape(preferredName) %></div>
				<div class="email"><%=HtmlUtil.escape(commerceAccount.getEmail())%></div>
			</div>
			<hr class="hr-bold" />
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-md-3">
				<div class="heading-title" data-id="#myacc">MY ACCOUNT</div>
				<div class="isi-account active" id="myacc">
					<ul>
						<li class="list-item active" data-id="#pi"><a
							href="/account/profile"
						>Personal Info</a></li>
						<li class="list-item" data-id="#cp"><a
							href="/account/change_password" class="text-dark"
						>Change Password</li>
					</ul>
				</div>
				<div class="heading-title" data-id="#register">
					<a href="/account/course_listing" class="text-dark">REGISTERED
						COURSE </a>
				</div>
				<div class="heading-title" data-id="#invoices">INVOICES</div>
				<div class="isi-account" id="invoices">
					<ul>
						<li class="list-item" data-id="#course-order-history"><a
							href="/account/courseinvoices" class="text-dark"
						>Course Order History</a></li>
						<li class="list-item" data-id="#exam-order-history"><a
							href="/account/examinvoices" class="text-dark"
						>Exam Order History</a></li>
						<!-- <li class="list-item" data-id="#merchandize-order-history"><a
							href="/account/merchandizeinvoices" class="text-dark">Merchandize Order
								History</a></li>
						<li class="list-item" data-id="#exam-merchandize-order-history"><a
							href="/account/exammerchandizeinvoices" class="text-dark">Exam & Merchandize Order History</a></li> -->
					</ul>
				</div>
			</div>
			<div class="col-md-9">
				<div class="content active" id="pi">
					<div class="fly-box-right">
							<div class="box-sc-image">
								<img alt="avatar" class="rounded" id="imgPrev" 
									src="<%=commerceAccountDisplayContext.getLogo(commerceAccount)%>"
								/> 
								<!-- <label class="upload-image"> SELECT IMAGE </label>
								<div class="normal">
									File size: Maximum 1 MB<br />File Extension: JPEG, PNG
								</div> -->
							</div>
					</div>
					<div class="row w-100">
						<div class="col-md-8">
							<div class="heading-content">Personal Information</div>
							<div class="box-info">
								<div class="small">Preferred Name</div>
								<div class="medium"><%=HtmlUtil.escape(preferredName)%></div>
								<c:choose>
									<c:when test="<%=isIndividu%>">
										<div class="small">Full Name (as NRIC/FIN/Passport)</div>
										<div class="medium"><%=HtmlUtil.escape(commerceAccount.getName())%></div>
										<div class="small">NRIC/FIN/Passport Number</div>
										<div class="medium"><%=HtmlUtil.escape(nric)%></div>
										<div class="small">Date of Birth</div>
										<div class="medium"><%=formatter.format(selectedUser.getBirthday())%></div>
									</c:when>
									<c:otherwise>
										<div class="small">Company Name</div>
										<div class="medium"><%=HtmlUtil.escape(companyName)%></div>
										<div class="small">ACRA/UEN Number</div>
										<div class="medium"><%=HtmlUtil.escape(uenNumber)%></div>
										<div class="small">Corporate Administrator Name</div>
										<div class="medium"><%=HtmlUtil.escape(commerceAccount.getName())%></div>
										<div class="small">Corporate Administrator Email</div>
										<div class="medium"><%=HtmlUtil.escape(commerceAccount.getEmail())%></div>
									</c:otherwise>
								</c:choose>
							</div>
							<c:if
								test="<%=commerceAccountDisplayContext
						.hasCommerceAccountModelPermissions(commerceAccount.getCommerceAccountId(), ActionKeys.UPDATE)%>"
							>
								<div class="btn-box-left">
									<aui:button cssClass="btn-blue text-capitalize"
										href="<%=editCommerceAccountURL%>"
										value='Update Account'
									/>
								</div>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>