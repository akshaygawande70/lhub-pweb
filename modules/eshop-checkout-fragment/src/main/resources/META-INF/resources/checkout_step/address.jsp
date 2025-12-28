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
<%@page import="com.liferay.portal.kernel.model.User"%>
<%@page import="com.liferay.portal.kernel.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.model.Country"%>
<%@page import="com.liferay.portal.kernel.service.CountryServiceUtil" %>
<%@page import="com.liferay.commerce.service.CommerceCountryLocalServiceUtil" %>
<%@page import="com.liferay.commerce.model.CommerceCountry" %>

<%
	CommerceContext commerceContext = (CommerceContext) request.getAttribute(CommerceWebKeys.COMMERCE_CONTEXT);

	BaseAddressCheckoutStepDisplayContext baseAddressCheckoutStepDisplayContext = (BaseAddressCheckoutStepDisplayContext) request
			.getAttribute(CommerceCheckoutWebKeys.COMMERCE_CHECKOUT_STEP_DISPLAY_CONTEXT);

	List<CommerceAddress> commerceAddresses = baseAddressCheckoutStepDisplayContext.getCommerceAddresses();
	long defaultCommerceAddressId = baseAddressCheckoutStepDisplayContext.getDefaultCommerceAddressId();

	String paramName = baseAddressCheckoutStepDisplayContext.getParamName();

	long commerceAddressId = BeanParamUtil.getLong(baseAddressCheckoutStepDisplayContext.getCommerceOrder(),
			request, paramName);

	if (commerceAddressId == 0) {
		commerceAddressId = defaultCommerceAddressId;
	}

	String selectLabel = "choose-" + baseAddressCheckoutStepDisplayContext.getTitle();

	CommerceOrder commerceOrder = commerceContext.getCommerceOrder();

	if (commerceOrder.isGuestOrder()) {
		commerceAddressId = 0;
	}

	CommerceAddress currentCommerceAddress = baseAddressCheckoutStepDisplayContext
			.getCommerceAddress(commerceAddressId);

	long commerceCountryId = BeanParamUtil.getLong(currentCommerceAddress, request, "commerceCountryId", 0);
	long commerceRegionId = BeanParamUtil.getLong(currentCommerceAddress, request, "commerceRegionId", 0);
	
	////System.out.println("getCommerceCountrySelectionMethodName : "+baseAddressCheckoutStepDisplayContext.getCommerceCountrySelectionMethodName());
	
	
	
%>
<div class="box-input">
	<c:if test="<%=!commerceOrder.isGuestOrder()%>">
		<aui:select label="<%=selectLabel%>" name="commerceAddress"
			onChange='<%=liferayPortletResponse.getNamespace() + "selectAddress();"%>'
			wrapperCssClass="commerce-form-group-item-row form-group-item" cssClass="clearable cs-form "
		>
			<aui:option label="add-new-address" value="0" />
			<%
				boolean addressWasFound = false;

						for (CommerceAddress commerceAddress : commerceAddresses) {
							boolean selectedAddress = commerceAddressId == commerceAddress.getCommerceAddressId();

							if (selectedAddress) {
								addressWasFound = true;
							}
			%>
			<aui:option
				data-city="<%=HtmlUtil.escapeAttribute(commerceAddress.getCity())%>"
				data-country="<%=HtmlUtil.escapeAttribute(String.valueOf(commerceAddress.getCommerceCountryId()))%>"
				data-name="<%=HtmlUtil.escapeAttribute(commerceAddress.getName())%>"
				data-phone-number="<%=HtmlUtil.escapeAttribute(commerceAddress.getPhoneNumber())%>"
				data-region="<%=HtmlUtil.escapeAttribute(String.valueOf(commerceAddress.getCommerceRegionId()))%>"
				data-street-1="<%=HtmlUtil.escapeAttribute(commerceAddress.getStreet1())%>"
				data-street-2="<%=Validator.isNotNull(commerceAddress.getStreet2())
								? HtmlUtil.escapeAttribute(commerceAddress.getStreet2())
								: StringPool.BLANK%>"
				data-street-3="<%=Validator.isNotNull(commerceAddress.getStreet3())
								? HtmlUtil.escapeAttribute(commerceAddress.getStreet3())
								: StringPool.BLANK%>"
				data-zip="<%=HtmlUtil.escapeAttribute(commerceAddress.getZip())%>"
				label="<%=HtmlUtil.escapeAttribute(commerceAddress.getName())%>"
				selected="<%=selectedAddress%>"
				value="<%=commerceAddress.getCommerceAddressId()%>"
			/>
			<%
				}
			%>
			<c:if
				test="<%=(currentCommerceAddress != null) && !addressWasFound%>"
			>
				<aui:option
					label="<%=HtmlUtil.escapeAttribute(currentCommerceAddress.getName())%>"
					selected="<%=true%>"
					value="<%=currentCommerceAddress.getCommerceAddressId()%>"
				/>
			</c:if>
		</aui:select>
	</c:if>
	<aui:input disabled="<%=commerceAddresses.isEmpty() ? true : false%>"
		name="<%=paramName%>" type="hidden" value="<%=commerceAddressId%>"
	/>
	<aui:input name="newAddress" type="hidden"
		value='<%=(commerceAddressId > 0) ? "0" : "1"%>'
	/>
</div>
<liferay-ui:error exception="<%=CommerceAddressCityException.class%>"
	message="please-enter-a-valid-city"
/>
<liferay-ui:error
	exception="<%=CommerceAddressCountryException.class%>"
	message="please-enter-a-valid-country"
/>
<liferay-ui:error exception="<%=CommerceAddressNameException.class%>"
	message="please-enter-a-valid-name"
/>
<liferay-ui:error
	exception="<%=CommerceAddressStreetException.class%>"
	message="please-enter-a-valid-street"
/>
<liferay-ui:error exception="<%=CommerceAddressZipException.class%>"
	message="please-enter-a-valid-zip"
/>
<liferay-ui:error
	exception="<%=CommerceOrderBillingAddressException.class%>"
	message="please-enter-a-valid-address"
/>
<liferay-ui:error
	exception="<%=CommerceOrderShippingAddressException.class%>"
	message="please-enter-a-valid-address"
/>
<aui:model-context
	bean="<%=baseAddressCheckoutStepDisplayContext.getCommerceAddress(commerceAddressId)%>"
	model="<%=CommerceAddress.class%>"
/>
<div class="address-fields">
	<div class="box-input">
		<label class="control-label" for="<portlet:namespace />name">Name
			<span style="color: red;">*</span>
		</label>
		<aui:input disabled="<%=commerceAddressId > 0%>" label=""
			name="name" wrapperCssClass="form-group-item" cssClass="clearable cs-form "
		/>
	</div>
	<div class="box-input">
		<label class="control-label" for="<portlet:namespace />phoneNumber">Phone Number
			<span style="color: red;">*</span>
		</label>
		<aui:input disabled="<%=commerceAddressId > 0%>" label=""
			name="phoneNumber" cssClass="clearable cs-form "
			wrapperCssClass="form-group-item"  type="text"  required="true" maxlength="8">
				<aui:validator name="required" />
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
	<div class="box-input">
		<label class="control-label" for="<portlet:namespace />street1">Address
			<span style="color: red;">*</span>
		</label>
		<aui:input disabled="<%=commerceAddressId > 0%>" label=""
			name="street1" cssClass="clearable cs-form "
			wrapperCssClass="form-group-item"
		/>
	</div>
	<div class="box-input">
		<label class="control-label" for="<portlet:namespace />country">Country
			<span style="color: red;">*</span>
		</label>
		
		<%
		/* List<Country> countries = CountryServiceUtil.getCountries();
		//System.out.println("countries : "+countries);
		String testCountryName = countries.get(0).getName(); */
		List<CommerceCountry> commerceCountryList = CommerceCountryLocalServiceUtil.getCommerceCountries(-1, -1);
		String countryList = "";
		%>
		
		<aui:select disabled="<%=commerceAddressId > 0%>" label=""
			name="commerceCountryId" cssClass="clearable cs-form " title="country"
			wrapperCssClass="form-group-item">
			<%
			for(int a=0;a<commerceCountryList.size();a++){
				if(commerceCountryList.get(a).isBillingAllowed()){
					countryList = commerceCountryList.get(a).getCommerceCountryId() + "," + countryList;
				}
				
				if(commerceCountryList.get(a).isShippingAllowed() && Objects.equals(CommerceCheckoutWebKeys.SHIPPING_ADDRESS_PARAM_NAME, paramName)){
			%>
				<aui:option value="<%=commerceCountryList.get(a).getCommerceCountryId()%>"> <%=commerceCountryList.get(a).getNameCurrentValue()%> </aui:option>
			<%
				}else if(commerceCountryList.get(a).isBillingAllowed() && Objects.equals(CommerceCheckoutWebKeys.BILLING_ADDRESS_PARAM_NAME, paramName)){
			%>
				<aui:option value="<%=commerceCountryList.get(a).getCommerceCountryId()%>"> <%=commerceCountryList.get(a).getNameCurrentValue()%> </aui:option>
			<%		
				}
			}
			%> 
		
			<aui:validator
				errorMessage='<%=LanguageUtil.get(request, "please-enter-a-valid-country")%>'
				name="required"/>
			
		</aui:select>
	</div>
	<div class="add-street-link form-group-autofit box-input">
		<aui:a disabled="<%=commerceAddressId > 0%>" href="javascript:;"
			label="+-add-address-line"
			onClick='<%=liferayPortletResponse.getNamespace() + "addStreetAddress();"%>'
		/>
	</div>
	<div class="add-street-fields hide">
		<div class="box-input ">
			<label class="control-label" for="<portlet:namespace />address2">Address
				2 </label>
			<aui:input disabled="<%=commerceAddressId > 0%>" label=""
				name="street2" cssClass="clearable cs-form "
				wrapperCssClass="form-group-item"
			/>
		</div>
		<div class="box-input">
			<label class="control-label" for="<portlet:namespace />address3">Address
				3 </label>
			<aui:input disabled="<%=commerceAddressId > 0%>" label=""
				name="street3" cssClass="clearable cs-form "
				wrapperCssClass="form-group-item"
			/>
		</div>
	</div>
	<div class="box-input">
		<label class="control-label" for="<portlet:namespace />zip">Postal Code
			<span style="color: red;">*</span>
		</label>
		<aui:input disabled="<%=commerceAddressId > 0%>" label="" name="zip" type="text"  required="true" maxlength="6"  
			cssClass="clearable cs-form " wrapperCssClass="form-group-item"
		>
			<aui:validator name="maxLength">6</aui:validator>
			<aui:validator errorMessage="Length should be 6 digits" name="custom">
				function(val, fieldNode, ruleValue) {
					var strRegex = /^\d{6}$/;
					var regex = new RegExp(strRegex);
					var flag = regex.test(val);
					return flag;
				}
			</aui:validator>
				<aui:validator errorMessage="Only digits allowed" name="custom">
				function(val, fieldNode, ruleValue) {
					var strRegex = /^\d+$/;
					var regex = new RegExp(strRegex);
					var flag = regex.test(val);
					return flag;
								                	}
			</aui:validator>
		</aui:input>
	</div>
	<div class="box-input">
		<label class="control-label" for="<portlet:namespace/>city">City
			<span style="color: red;">*</span>
		</label>
		<aui:input disabled="<%=commerceAddressId > 0%>" label=""
			name="city" cssClass="clearable cs-form " wrapperCssClass="form-group-item"
		/>
	</div>
	<div class="box-input">
		<label class="control-label" for="<portlet:namespace/>region">Region
		</label>
		<aui:select disabled="<%=commerceAddressId > 0%>" label=""
			name="commerceRegionId" cssClass="clearable cs-form " title="region"
			wrapperCssClass="form-group-item"
		/>
	</div>
	<div class="box-input">
		<label class="control-label d-none" for="<portlet:namespace/>region">Region
		</label>
		<aui:input disabled="<%=commerceAddressId > 0%>"
			id="commerceRegionIdInput" label="" name="commerceRegionId"
			cssClass="clearable cs-form" title="region"
			wrapperCssClass="d-none form-group-item"
		/>
	</div>
	<div class="box-input">
		<label class="control-label d-none" for="<portlet:namespace/>region">Region
		</label>
		<aui:input disabled="<%=commerceAddressId > 0%>"
			id="commerceRegionIdName" label="" name="commerceRegionId"
			cssClass="clearable cs-form " title="region"
			wrapperCssClass="d-none form-group-item"
		/>
	</div>
	<div class="box-input">
		<c:if test="<%=commerceOrder.isGuestOrder()%>">
			<label class="control-label" for="<portlet:namespace/>email">Email
				<span style="color: red;">*</span>
			</label>
			<aui:input name="email" type="text" cssClass="clearable cs-form " wrapperCssClass="form-group-item">
				<aui:validator name="email" />
				<aui:validator name="required" />
			</aui:input>
		</c:if>
	</div>
</div>
<c:if
	test="<%=Objects.equals(CommerceCheckoutWebKeys.SHIPPING_ADDRESS_PARAM_NAME, paramName)%>"
>
	<div class="shipping-as-billing" style="display: none;">
		<aui:input
			checked="<%=(commerceAddressId > 0) && baseAddressCheckoutStepDisplayContext.isShippingUsedAsBilling()%>"
			disabled="<%=false%>"
			label="use-shipping-address-as-billing-address" name="use-as-billing"
			type="checkbox"
		/>
	</div>
</c:if>
<aui:script>
	Liferay.provide(
		window,
		'<portlet:namespace />addStreetAddress',
		function <portlet:namespace />addStreetAddress() {
			var A = AUI();

			var addStreetFields = A.one('.add-street-fields');
			var addStreetLink = A.one('.add-street-link');

			if (addStreetFields) {
				addStreetFields.show();
			}
			if (addStreetLink) {
				addStreetLink.hide();
			}
		},
		['aui-base']
	);

	Liferay.provide(
		window,
		'<portlet:namespace />clearAddressFields',
		function <portlet:namespace />clearAddressFields() {
			var A = AUI();

			A.all('.address-fields select').set('selectedIndex', 0);
			A.all('.address-fields input').val('');

			var useAsBillingField = A.one('#<portlet:namespace />use-as-billing');

			if (useAsBillingField) {
				useAsBillingField.attr(
					'checked',
					<%=baseAddressCheckoutStepDisplayContext.isShippingUsedAsBilling()%>
				);
			}
		},
		['aui-base']
	);

	Liferay.provide(
		window,
		'<portlet:namespace />selectAddress',
		function <portlet:namespace />selectAddress() {
			var A = AUI();

			var commerceAddress = A.one('#<portlet:namespace />commerceAddress');
			var commerceAddressParamName = A.one(
				'#<%=liferayPortletResponse.getNamespace() + paramName%>'
			);
			var newAddress = A.one('#<portlet:namespace />newAddress');
			
			var countryList = "<%=countryList%>";
			var countrySelected = $('#<portlet:namespace />commerceAddress').find(":selected").attr('data-country');

			if(countryList.indexOf(countrySelected) != -1){
			    $(".shipping-as-billing").show();
			}else{
				$(".shipping-as-billing").hide();
			}

			if (newAddress && commerceAddress && commerceAddressParamName) {
				var commerceAddressVal = commerceAddress.val();

				if (commerceAddressVal === '0') {
					<portlet:namespace />clearAddressFields();
					<portlet:namespace />toggleAddressFields(false);
				}
				else {
					<portlet:namespace />updateAddressFields(
						commerceAddress.get('selectedIndex')
					);
					Liferay.Form.get(
						'<portlet:namespace />fm'
					).formValidator.validate();
				}

				commerceAddressParamName.val(commerceAddressVal);
				newAddress.val(Number(commerceAddressVal === '0'));
			}
		},
		['aui-base']
	);

	Liferay.provide(
		window,
		'<portlet:namespace />toggleAddressFields',
		function <portlet:namespace />toggleAddressFields(state) {
			var A = AUI();

			Liferay.Util.toggleDisabled(A.all('.address-fields input'), state);
			Liferay.Util.toggleDisabled(A.all('.address-fields select'), state);

			var commerceRegionIdSelect = A.one(
				'#<portlet:namespace />commerceRegionId'
			).getDOMNode();
			var commerceRegionIdInput = A.one(
				'#<portlet:namespace />commerceRegionIdInput'
			).getDOMNode();
			var commerceRegionIdName = A.one(
				'#<portlet:namespace />commerceRegionIdName'
			).getDOMNode();

			commerceRegionIdSelect.setAttribute(
				'name',
				'<portlet:namespace />commerceRegionId'
			);
			commerceRegionIdSelect.parentElement.classList.remove('d-none');

			commerceRegionIdInput.setAttribute(
				'name',
				'commerceRegionIdInputDisabled'
			);
			commerceRegionIdInput.parentElement.classList.add('d-none');
			commerceRegionIdName.setAttribute(
				'name',
				'commerceRegionIdInputDisabled'
			);
			commerceRegionIdName.parentElement.classList.add('d-none');
		},
		['aui-base']
	);

	Liferay.provide(
		window,
		'<portlet:namespace />updateAddressFields',
		function <portlet:namespace />updateAddressFields(selectedVal) {
			if (!selectedVal || selectedVal === '0') {
				return;
			}

			var A = AUI();

			var commerceAddress = A.one('#<portlet:namespace />commerceAddress');

			if (commerceAddress) {
				<portlet:namespace />addStreetAddress();
				<portlet:namespace />toggleAddressFields(true);

				var city = A.one('#<portlet:namespace />city');
				var commerceCountryId = A.one(
					'#<portlet:namespace />commerceCountryId'
				);
				var commerceRegionIdInput = A.one(
					'#<portlet:namespace />commerceRegionIdInput'
				).getDOMNode();
				var commerceRegionIdName = A.one(
					'#<portlet:namespace />commerceRegionIdName'
				).getDOMNode();
				var commerceRegionIdSelect = A.one(
					'#<portlet:namespace />commerceRegionId'
				).getDOMNode();
				var name = A.one('#<portlet:namespace />name');
				var phoneNumber = A.one('#<portlet:namespace />phoneNumber');
				var street1 = A.one('#<portlet:namespace />street1');
				var street2 = A.one('#<portlet:namespace />street2');
				var street3 = A.one('#<portlet:namespace />street3');
				var zip = A.one('#<portlet:namespace />zip');

				if (
					city &&
					commerceCountryId &&
					commerceRegionIdInput &&
					commerceRegionIdSelect &&
					commerceRegionIdName &&
					name &&
					phoneNumber &&
					street1 &&
					street2 &&
					street3 &&
					zip
				) {
					var selectedOption = commerceAddress
						.get('options')
						.item(selectedVal);

					city.val(selectedOption.getData('city'));
					commerceCountryId.val(selectedOption.getData('country'));
					name.val(selectedOption.getData('name'));
					phoneNumber.val(selectedOption.getData('phone-number'));
					street1.val(selectedOption.getData('street-1'));
					street2.val(selectedOption.getData('street-2'));
					street3.val(selectedOption.getData('street-3'));
					zip.val(selectedOption.getData('zip'));

					commerceRegionIdSelect.setAttribute(
						'name',
						'commerceRegionIdSelectIgnore'
					);
					commerceRegionIdSelect.parentElement.classList.add('d-none');

					commerceRegionIdInput.value = selectedOption.getData('region');
					commerceRegionIdInput.setAttribute(
						'name',
						'<portlet:namespace />commerceRegionId'
					);
					commerceRegionIdInput.parentElement.classList.add('d-none');

					commerceRegionIdName.setAttribute(
						'name',
						'commerceRegionIdNameIgnore'
					);
					commerceRegionIdName.parentElement.classList.remove('d-none');

					Liferay.Service(
						'/commerce.commerceregion/get-commerce-regions',
						{
							commerceCountryId: parseInt(
								selectedOption.getData('country'),
								10
							),
							active: true,
						},
						function setUIOnlyInputRegionName(regions) {
							for (var i = 0; i < regions.length; i++) {
								if (
									regions[i].commerceRegionId ===
									selectedOption.getData('region')
								) {
									commerceRegionIdName.value = regions[i].name;

									break;
								}
							}
						}
					);
				}
			}
		},
		['aui-base']
	);
</aui:script>
<aui:script>
	Liferay.component(
		'<portlet:namespace />countrySelects',
		new Liferay.DynamicSelect([
			<%-- {
				select: '<portlet:namespace />commerceCountryId',
				selectData: function (callback) {
					function injectCountryPlaceholder(list) {
						var callbackList = [
							{
								commerceCountryId: '0',
								nameCurrentValue:
									'- <liferay-ui:message key="select-country" />',
							},
						];

						list.forEach(function (listElement) {
							callbackList.push(listElement);
						});

						callback(callbackList);
					}

					Liferay.Service(
						'/commerce.commercecountry/<%=baseAddressCheckoutStepDisplayContext.getCommerceCountrySelectionMethodName()%>-by-channel-id',
						{
							commerceChannelId: <%=commerceContext.getCommerceChannelId()%>,
							start: -1,
							end: -1,
						},
						injectCountryPlaceholder
					);
				},
				selectDesc: 'nameCurrentValue',
				selectId: 'commerceCountryId',
				selectNullable: <%=false%>,
				selectSort: '<%=true%>',
				selectVal: '<%=commerceCountryId%>',
			},  --%>
			{
				select: '<portlet:namespace />commerceRegionId',
				selectData: function (callback, selectKey) {
					function injectRegionPlaceholder(list) {
						var callbackList = [
							{
								commerceRegionId: '0',
								name:
									'- <liferay-ui:message key="select-region" />',
								nameCurrentValue:
									'- <liferay-ui:message key="select-region" />',
							},
						];

						list.forEach(function (listElement) {
							callbackList.push(listElement);
						});

						callback(callbackList);
					}

					Liferay.Service(
						'/commerce.commerceregion/get-commerce-regions',
						{
							commerceCountryId: Number(selectKey),
							active: true,
						},
						injectRegionPlaceholder
					);
				},
				selectDesc: 'name',
				selectId: 'commerceRegionId',
				selectNullable: <%=false%>,
				selectVal: '<%=commerceRegionId%>',
			},
		])
	);
</aui:script>
<script>
<%if ((commerceAddressId > 0) && (countryList.contains(baseAddressCheckoutStepDisplayContext.getCommerceAddress(commerceAddressId).getCommerceCountryId()+""))) {%>
	$(".shipping-as-billing").show();
<%} else {%>
	$(".shipping-as-billing").hide();
<%}%>

$("#<portlet:namespace />commerceCountryId").change(function(){
	var countryList = "<%=countryList%>";
	var countrySelected = $('#<portlet:namespace />commerceCountryId').find(":selected").val();
	
	if(countryList.indexOf(countrySelected) != -1){
	    $(".shipping-as-billing").show();
	}else{
		$(".shipping-as-billing").hide();
	}
});
</script>