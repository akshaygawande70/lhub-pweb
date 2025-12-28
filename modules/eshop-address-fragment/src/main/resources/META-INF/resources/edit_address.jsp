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
<%@page import="com.liferay.portal.kernel.model.Country"%>
<%@page import="com.liferay.portal.kernel.service.CountryServiceUtil" %>
<%@page import="com.liferay.commerce.service.CommerceCountryLocalServiceUtil" %>
<%@page import="com.liferay.commerce.model.CommerceCountry" %>
<%
	CommerceContext commerceContext = (CommerceContext) request.getAttribute(CommerceWebKeys.COMMERCE_CONTEXT);

	String redirect = ParamUtil.getString(request, "redirect");

	String backURL = ParamUtil.getString(request, "backURL", redirect);

	String languageId = LanguageUtil.getLanguageId(locale);

	CommerceAddressDisplayContext commerceAddressDisplayContextEdit = (CommerceAddressDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

	CommerceAddress commerceAddressEdit = commerceAddressDisplayContextEdit.getCommerceAddress();

	long commerceAddressId = commerceAddressDisplayContextEdit.getCommerceAddressId();
	long commerceCountryId = commerceAddressDisplayContextEdit.getCommerceCountryId();
	long commerceRegionId = commerceAddressDisplayContextEdit.getCommerceRegionId();

	CommerceAccount commerceAccount = commerceAddressDisplayContextEdit.getCommerceAccount();
%>
<portlet:actionURL
	name="/commerce_address_content/edit_commerce_address"
	var="editCommerceAddressActionURL"
/>
<aui:form action="<%=editCommerceAddressActionURL%>" method="post" name="edit_fm">
	<aui:input name="<%=Constants.CMD%>" type="hidden" value="<%=(commerceAddressEdit == null) ? Constants.ADD : Constants.UPDATE%>"
	/>
	<aui:input name="redirect" type="hidden" value="<%=redirect%>" />
	<aui:input name="backURL" type="hidden" value="<%=backURL%>" />
	<aui:input name="commerceAddressId" type="hidden" value="<%=commerceAddressId%>"/>
	<aui:input name="commerceAccountId" type="hidden" value="<%=(commerceAddressEdit == null) ? commerceAccount.getCommerceAccountId(): commerceAddressEdit.getClassPK()%>"/>
	<aui:model-context bean="<%=commerceAddressEdit%>"
		model="<%=CommerceAddress.class%>"
	/>
	<liferay-ui:error exception="<%=CommerceAddressCityException.class%>"
		message="please-enter-a-valid-city"
	/>
	<liferay-ui:error
		exception="<%=CommerceAddressCountryException.class%>"
		message="please-select-a-country"
	/>
	<liferay-ui:error exception="<%=CommerceAddressStreetException.class%>"
		message="please-enter-a-valid-street"
	/>
	<liferay-ui:error exception="<%=CommerceAddressZipException.class%>"
		message="please-enter-a-valid-zip"
	/>
	<div class="lfr-form-content">
		<aui:fieldset cssClass="addresses">
			<div class="row">
				<div class="col-md-3" style="display: none;">
					<aui:input
						checked="<%=(commerceAddressId > 0) && (commerceAddressEdit.getType()==2 || commerceAddressEdit.getType()==3) %>"
						disabled="<%=false%>"
						label="Shipping" name="defaultShipping"
						type="checkbox"
					/>
				</div>
				<div class="col-md-3" style="display: none;">
					<aui:input
						checked="true"
						disabled="<%=false%>"
						label="Billing" name="defaultBilling"
						type="checkbox"
					/>
				</div>
			</div>
			<div class="box-input">
				<label for="name">Name</label>
				<aui:input cssClass="cs-form" label="" name="name">
					<aui:validator errorMessage="No special character allowed" name="custom">
						function(val, fieldNode, ruleValue) {
							var strRegex = '[a-zA-Z0-9]$';
							var regex = new RegExp(strRegex);
							var flag = regex.test(val);
							return flag;
						}
					</aui:validator>
				</aui:input>
			</div>
			<aui:input name="description" type="hidden" />
			<div class="row">
				<div class="col-md-6">
					<div class="box-input">
						<label for="street1">Street 1</label>
						<aui:input cssClass="cs-form" label="" name="street1">
							<aui:validator errorMessage="No special character allowed" name="custom">
						function(val, fieldNode, ruleValue) {
							var strRegex = '[a-zA-Z0-9]$';
							var regex = new RegExp(strRegex);
							var flag = regex.test(val);
							return flag;
						}
					</aui:validator>
					<aui:validator name="maxLength">150</aui:validator>
						</aui:input>
					</div>
				</div>
				<div class="col-md-6">
					<div class="box-input">
						<label for="street2">Street2</label>
						<aui:input cssClass="cs-form" label="" name="street2">
						<aui:validator errorMessage="No special character allowed" name="custom">
						function(val, fieldNode, ruleValue) {
							var strRegex = '[a-zA-Z0-9]$';
							var regex = new RegExp(strRegex);
							var flag = regex.test(val);
							return flag;
						}
					</aui:validator>
					<aui:validator name="maxLength">150</aui:validator>
						</aui:input>
					</div>
				</div>
			</div>
			<aui:input name="street3" type="hidden" />
			<div class="box-input">
				<label for="city">City</label>
				<aui:input cssClass="cs-form" label="" name="city">
				<aui:validator errorMessage="No special character allowed" name="custom">
						function(val, fieldNode, ruleValue) {
							var strRegex = '[a-zA-Z0-9]$';
							var regex = new RegExp(strRegex);
							var flag = regex.test(val);
							return flag;
						}
					</aui:validator>
				</aui:input>
			</div>
			<div class="box-input">
				<label for="postal-code">Postal Code</label>
				<aui:input cssClass="cs-form" label="" name="zip" type="text"  required="true" maxlength="6" wrapperCssClass="form-group-item" >
					<aui:validator name="maxLength">6</aui:validator>
					<aui:validator errorMessage="Only digits allowed" name="custom">
						function(val, fieldNode, ruleValue) {
							var strRegex = /^\d+$/;
							var regex = new RegExp(strRegex);
							var flag = regex.test(val);
							return flag;
						}
					</aui:validator>
					<aui:validator errorMessage="Length should be 6 digits" name="custom">
						function(val, fieldNode, ruleValue) {
							var strRegex = /^\d{6}$/;
							var regex = new RegExp(strRegex);
							var flag = regex.test(val);
							return flag;
						}
					</aui:validator>
				</aui:input>
			</div>
			<div class="box-input">
				<label for="country">Country</label>
				
				<%List<CommerceCountry> commerceCountries = CommerceCountryLocalServiceUtil.getCommerceCountries(-1, -1);%>
				<aui:select cssClass="cs-form" label="" name="commerceCountryId"
					showEmptyOption="<%=true%>">
					<%
					for(int a=0;a<commerceCountries.size();a++){
						if(commerceCountries.get(a).isShippingAllowed() && !commerceCountries.get(a).isBillingAllowed()){
					%>
							<aui:option value="<%=commerceCountries.get(a).getCommerceCountryId()%>" cssClass="shipping" style="display:none;"> <%=commerceCountries.get(a).getNameCurrentValue()%> </aui:option>

					<%
						}else if(commerceCountries.get(a).isBillingAllowed() && !commerceCountries.get(a).isShippingAllowed()){
					%>
							<aui:option value="<%=commerceCountries.get(a).getCommerceCountryId()%>" cssClass="billing" style="display:none;"> <%=commerceCountries.get(a).getNameCurrentValue()%> </aui:option>
					<%		
						}else if(commerceCountries.get(a).isBillingAllowed() && commerceCountries.get(a).isShippingAllowed()){
					%>
							<aui:option value="<%=commerceCountries.get(a).getCommerceCountryId()%>" cssClass="shipping billing" style="display:block;"> <%=commerceCountries.get(a).getNameCurrentValue()%> </aui:option>
					<%
						}
					}
					%> 
				
					<aui:validator
						errorMessage='<%=LanguageUtil.get(request, "please-enter-a-valid-country")%>'
						name="required"/>
					
				</aui:select>
			</div>
			<%-- <aui:select style="display:none;" label="" name="commerceRegionId"
				showEmptyOption="<%=true%>"
			>
				<%
					List<CommerceRegion> commerceRegions = commerceAddressDisplayContextEdit.getCommerceRegions();

								for (CommerceRegion commerceRegion : commerceRegions) {
				%>
				<aui:option label="<%=commerceRegion.getName()%>"
					selected="<%=(commerceAddressEdit != null) && (commerceAddressEdit
									.getCommerceRegionId() == commerceRegion.getCommerceRegionId())%>"
					value="<%=commerceRegion.getCommerceRegionId()%>"
				/>
				<%
					}
				%>
			</aui:select> --%>
			<aui:input name="commerceRegionId" type="hidden" value="0" />
			
			<div class="box-input">
				<label for="phone-number">Phone Number</label>
				<aui:input cssClass="cs-form" label="" name="phoneNumber" type="text"  required="true" maxlength="8">
					<aui:validator name="maxLength">8</aui:validator>
					<aui:validator errorMessage="Enter an 8-digit number that begins with 6, 8, or 9." name="custom">
						function(val, fieldNode, ruleValue) {
							var strRegex = /^[689]\d{7}$/;
							var regex = new RegExp(strRegex);
							var flag = regex.test(val);
							return flag;
						}
					</aui:validator>
				</aui:input>
			</div>
		</aui:fieldset>
		<aui:button-row>
			<a class="btn-white" href="<%=backURL%>" > Cancel </a>
			<aui:button cssClass="btn-blue text-capitalize" type="submit" />
		</aui:button-row>
	</div>
</aui:form>
<aui:script use="aui-base,liferay-dynamic-select">
	new Liferay.DynamicSelect([
		{
			select: '<portlet:namespace />commerceRegionId',
			selectData: function (callback, selectKey) {
				Liferay.Service(
					'/commerce.commerceregion/get-commerce-regions',
					{
						active: true,
						commerceCountryId: Number(selectKey),
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
	$('#<portlet:namespace />defaultShipping').change(function () {
		if(this.checked){
			if($('#<portlet:namespace />defaultBilling').is(':checked')){
				$(".billing").hide();
				$(".shipping.billing").show();
			}else{
				$(".billing").hide();
				$(".shipping").show();
			}
		}else{
			if($('#<portlet:namespace />defaultBilling').is(':checked')){
				$(".billing").show();
			}else{
				$(".billing").hide();
				$(".shipping").hide();
			}
		}
	});
	
	<%
		if((commerceAddressId > 0) && (commerceAddressEdit.getType()==2 || commerceAddressEdit.getType()==3)){
			if((commerceAddressId > 0) && (commerceAddressEdit.getType()==2 || commerceAddressEdit.getType()==1)){
	%>
				$(".shipping.billing").show();
	<%
			}else{
	%>
				$(".shipping").show();
	<%
			}
		}
	%>
	
	$('#<portlet:namespace />defaultBilling').change(function () {
		if(this.checked){
			if($('#<portlet:namespace />defaultShipping').is(':checked')){
				$(".shipping").hide();
				$(".shipping.billing").show();
			}else{
				$(".shipping").hide();
				$(".billing").show();
			}
		}else{
			if($('#<portlet:namespace />defaultShipping').is(':checked')){
				$(".shipping").show();
			}else{
				$(".billing").hide();
				$(".shipping").hide();
			}
		}
	});
	
	<%
		if((commerceAddressId > 0) && (commerceAddressEdit.getType()==2 || commerceAddressEdit.getType()==1)){
			if((commerceAddressId > 0) && (commerceAddressEdit.getType()==2 || commerceAddressEdit.getType()==3)){
	%>
				$(".shipping.billing").show();
	<%
			}else{
	%>
				$(".billing").show();
	<%
			}
		}
	%>
</script>