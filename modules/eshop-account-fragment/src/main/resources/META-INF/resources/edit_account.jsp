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
<%@page import="com.liferay.petra.string.StringPool"%>
<%@page import="api.ntuc.common.util.RoleUtil"%>
<%@page import="com.liferay.portal.kernel.service.RoleServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.portal.kernel.model.Role"%>
<%@page import="java.util.List"%>
<%@ include file="/init.jsp"%>
<%
	CommerceAccountDisplayContext commerceAccountDisplayContext = (CommerceAccountDisplayContext) request
			.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

	CommerceAccount commerceAccount = commerceAccountDisplayContext.getCurrentCommerceAccount();
	CommerceAddress commerceAddress = commerceAccountDisplayContext.getDefaultBillingCommerceAddress();

	long commerceCountryId = 0;
	long commerceRegionId = 0;

	if (commerceAddress != null) {
		commerceCountryId = commerceAddress.getCommerceCountryId();
		commerceRegionId = commerceAddress.getCommerceRegionId();
	}
	backURL = themeDisplay.getPortalURL() + "/account/profile";

	User selectedUser = commerceAccountDisplayContext.getSelectedUser();

	List<Role> roleList = new ArrayList<Role>();
	for (Long roleId : selectedUser.getRoleIds()) {
		Role newRole = RoleServiceUtil.getRole(roleId);
		roleList.add(newRole);
	}

	boolean isIndividu = Boolean.FALSE;
	
	if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Individual_Role")) {
		isIndividu = Boolean.TRUE;
	}
	String preferredName = (String) commerceAccount.getExpandoBridge().getAttribute("Preferred Name");
	String nameLabel = isIndividu ? "Full Name (as NRIC/FIN/Passport)" : "Corporate Administrator Name";
%>
<portlet:actionURL name="/commerce_account/edit_commerce_account"
	var="editCommerceAccountActionURL"
/>

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
					<%=HtmlUtil.escape(preferredName)%></div>
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
				<!--Start content-->
				<div class="content active" id="pi">
					<aui:form action="<%=editCommerceAccountActionURL%>" method="post"
						name="fm"
					>
						<aui:input name="<%=Constants.CMD%>" type="hidden"
							value="<%=(commerceAccount == null) ? Constants.ADD : Constants.UPDATE%>"
						/>
						<aui:input name="redirect" type="hidden" value="<%=currentURL%>" />
						<aui:input name="commerceAccountId" type="hidden"
							value="<%=(commerceAccount == null) ? 0 : commerceAccount.getCommerceAccountId()%>"
						/>
						<aui:input name="commerceAddressId" type="hidden"
							value="<%=(commerceAddress == null) ? 0 : commerceAddress.getCommerceAddressId()%>"
						/>
						<liferay-ui:error-marker key="<%=WebKeys.ERROR_SECTION%>"
							value="details"
						/>
						<aui:model-context bean="<%=commerceAccount%>"
							model="<%=CommerceAccount.class%>"
						/>
						<%
							long imageMaxSize = 0L;
						%>
						<section class="panel-secondary">
							<div class="panel-body">
								<div class="row">
									<div class="fly-box-right">
										<div class="box-sc-image">
											<aui:fieldset>
												
												<c:if test="<%=commerceAccount != null%>">
													<%
														long logoId = commerceAccount.getLogoId();

																	UserFileUploadsConfiguration userFileUploadsConfiguration = commerceAccountDisplayContext
																			.getUserFileUploadsConfiguration();
																	imageMaxSize = userFileUploadsConfiguration.imageMaxSize();
													%>
													<div id="imgPrev">
														<liferay-ui:logo-selector
															currentLogoURL='<%=themeDisplay.getPathImage() + "/organization_logo?img_id=" + logoId + "&t="
								+ WebServerServletTokenUtil.getToken(logoId)%>'
															defaultLogo="<%=logoId == 0%>"
															defaultLogoURL='<%=themeDisplay.getPathImage() + "/organization_logo?img_id=0"%>'
															logoDisplaySelector=".organization-logo"
															maxFileSize="<%=userFileUploadsConfiguration.imageMaxSize()%>"
															tempImageFileName="<%=String.valueOf(themeDisplay.getScopeGroupId())%>"
														/>	
													</div>
												</c:if>
											</aui:fieldset>
											<div class="normal">
												Maximum File size: <%=imageMaxSize/1024%>KB<br />File Extension: JPEG, PNG
											</div>
										</div>
									</div>
									<div class="row w-100">
										<div class="col-md-8">
											<aui:input label="vat-number" name="taxId" type="hidden" />
											<liferay-util:include page="/account/custom_fields.jsp"
												servletContext="<%=application%>"
											/>
											<div class="box-input">
												<label for="name"><%=nameLabel %></label>
												<aui:input readonly="true"  cssClass="cs-form disabled " name="name"  label="" 
													placeholder="<%=nameLabel %>" type="text"
												/>
											</div>
											<div class="box-input">
												<label for="email">Email</label>
												<aui:input readonly="true" cssClass="disabled cs-form" name="email" label="" 
													placeholder="Email" type="text"
												/>
											</div>
											<div class="box-input">
												<label for="default-billing">Default Billing</label>
												<aui:select label=""
													name="defaultBillingAddressId" showEmptyOption="<%=true%>" cssClass="cs-form"
												>
													<%
														for (CommerceAddress billingAddress : commerceAccountDisplayContext.getBillingCommerceAddresses()) {
													%>
													<aui:option label="<%=HtmlUtil.escape(billingAddress.getName())%>"
														selected="<%=billingAddress.getCommerceAddressId() == commerceAccount.getDefaultBillingAddressId()%>"
														value="<%=billingAddress.getCommerceAddressId()%>"
													/>
													<%
														}
													%>
												</aui:select>
											</div>
											<div class="box-input">
												<label for="default-shipping">Default Shipping</label>
												<aui:select label=""
													name="defaultShippingAddressId" showEmptyOption="<%=true%>" cssClass="cs-form"
												>
													<%
														for (CommerceAddress shippingAddress : commerceAccountDisplayContext
																		.getShippingCommerceAddresses()) {
													%>
													<aui:option label="<%=HtmlUtil.escape(shippingAddress.getName())%>"
														selected="<%=shippingAddress.getCommerceAddressId() == commerceAccount
								.getDefaultShippingAddressId()%>"
														value="<%=shippingAddress.getCommerceAddressId()%>"
													/>
													<%
														}
													%>
												</aui:select>
											</div>
										</div>
									</div>
								</div>
							</div>
						</section>
						<div class="commerce-cta is-visible">
							<div class="btn-box-left">
								<c:if test="<%=Validator.isNotNull(backURL)%>">
									<a class="btn-white" href="<%=backURL%>" > Cancel </a>
								</c:if>
								<aui:button cssClass="btn-blue" primary="<%=false%>" type="submit" />
							</div>
						</div>
					</aui:form>
				</div>
			</div>
		</div>
	</div>
</section>
<aui:script use="liferay-dynamic-select">
	new Liferay.DynamicSelect([
		{
			select: '<portlet:namespace />commerceCountryId',
			selectData: function (callback) {
				Liferay.Service(
					'/commerce.commercecountry/get-commerce-countries',
					{
						companyId: <%=company.getCompanyId()%>,
						active: true,
					},
					callback
				);
			},
			selectDesc: 'nameCurrentValue',
			selectId: 'commerceCountryId',
			selectSort: true,
			selectVal: '<%=commerceCountryId%>',
		},
		{
			select: '<portlet:namespace />commerceRegionId',
			selectData: function (callback, selectKey) {
				Liferay.Service(
					'/commerce.commerceregion/get-commerce-regions',
					{
						commerceCountryId: Number(selectKey),
						active: true,
					},
					callback
				);
			},
			selectDesc: 'name',
			selectId: 'commerceRegionId',
			selectVal: '<%=commerceRegionId%>',
		},
	]);
</aui:script>
<script>
	$(document).ready(function(){
		$("#igva___avatar").addClass("rounded");
		$(".lfr-input-text").addClass("cs-form");
		$("#_com_liferay_commerce_account_web_internal_portlet_CommerceAccountPortlet_eada___Male").addClass("cs-form");
		$( "input[name*='Expando']" ).each(function(i,obj){
			if(obj['id'].includes('Company_20_Code') || obj['id'].includes('Company_20_Name') || obj['id'].includes('UEN_20_Number') || obj['id'].includes('NRIC')){
				$(this).attr('readonly',true);
			}
		});
		$('label[for="<portlet:namespace />eada___NRIC"]').text('NRIC');
		$('label[for="<portlet:namespace />iddv___UEN Number"]').text('UEN Number');
	})
</script>