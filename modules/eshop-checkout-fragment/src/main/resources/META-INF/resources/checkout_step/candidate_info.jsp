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
<%@page import="com.liferay.commerce.model.CommerceOrder"%>
<%@page import="com.liferay.commerce.model.CommerceOrderItem"%>
<%@page import="com.liferay.commerce.tax.engine.fixed.service.CommerceTaxFixedRateLocalServiceUtil"%>
<%@page import="com.liferay.commerce.tax.engine.fixed.model.CommerceTaxFixedRate"%>
<%@ include file="/init.jsp"%>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>



<%
	CommerceContext commerceContext = (CommerceContext) request.getAttribute(CommerceWebKeys.COMMERCE_CONTEXT);

	BaseAddressCheckoutStepDisplayContext baseAddressCheckoutStepDisplayContext = (BaseAddressCheckoutStepDisplayContext) request
			.getAttribute(CommerceCheckoutWebKeys.COMMERCE_CHECKOUT_STEP_DISPLAY_CONTEXT);
	
	//OrderSummaryCheckoutStepDisplayContext orderSummaryCheckoutdisplayContext = (OrderSummaryCheckoutStepDisplayContext) request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
			
	/* CommerceOrder commerceOrderForImg = baseAddressCheckoutStepDisplayContext.getCommerceOrder();
	List<CommerceOrderItem> commerceOrderItemsForImg = commerceOrderForImg.getCommerceOrderItems();
	CommerceOrderItem commerceOrderItemForImg = commerceOrderItemsForImg.get(0); */

	List<CommerceAddress> commerceAddresses = baseAddressCheckoutStepDisplayContext.getCommerceAddresses();
	long defaultCommerceAddressId = baseAddressCheckoutStepDisplayContext.getDefaultCommerceAddressId();

	String paramName = baseAddressCheckoutStepDisplayContext.getParamName();

	long commerceAddressId = BeanParamUtil.getLong(baseAddressCheckoutStepDisplayContext.getCommerceOrder(),
			request, paramName);

	if (commerceAddressId == 0) {
		commerceAddressId = defaultCommerceAddressId;
	}

	String selectLabel = "choose-" + baseAddressCheckoutStepDisplayContext.getTitle();

	//CommerceOrder commerceOrder = commerceContext.getCommerceOrder();
	CommerceOrder commerceOrder = baseAddressCheckoutStepDisplayContext.getCommerceOrder();
	

	if (commerceOrder.isGuestOrder()) {
		commerceAddressId = 0;
	}

	CommerceAddress currentCommerceAddress = baseAddressCheckoutStepDisplayContext.getCommerceAddress(commerceAddressId);

	long commerceCountryId = BeanParamUtil.getLong(currentCommerceAddress, request, "commerceCountryId", 0);
	long commerceRegionId = BeanParamUtil.getLong(currentCommerceAddress, request, "commerceRegionId", 0);
	
	
	/* ============================================================================================================= */
	
	List<CommerceOrderItem> commerceOrderItems = commerceOrder.getCommerceOrderItems();
	CommerceOrderItem commerceOrderItem = commerceOrderItems.get(0);
	/* ============================================================================================================= */
%>

<br>
<br>
<br>
<div class="section-step-two">
	<div class="container">
		<div class="row">
			<div class="col-md-12 col-xl-8">
				<div class="box-candidate">
					<div class="title">CANDIDATE INFORMATION</div>
					<div class="decs">Please check if all information in the
						fields are correct. All fileds are mandatory.</div>
					<div class="abstrak">Pesonal Detail:</div>
					<div class="notice-alert alert-blue">
						<img alt="" src="/o/web.ntuc.eshop.register/img/Info_Icon.png" />
						<div>Please ensure that the information filled here is for
							the individual sitting for the exam.</div>
					</div>
					<div class="box-field">
						<%-- <div class="box-input">
							<div class="form-group">
								<label for="<portlet:namespace />name">Full Name (NRIC/Passport)
									<span style="color: red;">*</span>
								</label>
								<aui:input disabled="<%=commerceAddressId > 0%>" label=""
									name="name" wrapperCssClass="form-group-item" cssClass="clearable cs-form "
								/>
							</div>
						</div> --%>
						
						<div class="box-input">
                            <div class="form-group">
                                <label for="name">Full Name (NRIC/Passport)<span class="red">*</span></label>
                                <input type="text" class="cs-form" id="name" placeholder="" required="" />
                            </div>
                        </div>
                        
						<div class="box-input">
							<div class="form-group">
								<label for="date">Date of Birth <span class="red">*</span>
								</label> <input class="cs-form date" id="date" placeholder=""
									required="" type="date" />
							</div>
						</div>
						
						<div class="box-input">
                            <div class="form-group">
                                <label for="name">Email Address<span class="red">*</span></label>
                                <input type="text" class="cs-form" id="name" placeholder="" required="" />
                            </div>
                        </div>
						
						<div class="box-input">
							<div class="form-group">
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
					</div>
					<div class="abstrak">Please proceed to select the date and
						time for your exam.</div>
					<div class="notice-alert alert-red">
						<img alt="" src="/o/web.ntuc.eshop.register/img/Info_Icon.png" />
						<div>
							selected dates/times are subject to availability. A confirmation
							email will be sent once booked successfully. You should receive
							it within the <b>next 2-3 working days</b>
						</div>
					</div>
					<div class="select-time">Select your 3 prefereed date and
						timr for exam below: Monday - Friday, from 9am - 2pm.</div>
						
						
					<!-- ======================================================================================================= -->
						
						
					<div class="box-date-exams">
					
						<%
							CPInstance cpInstance = commerceOrderItem.fetchCPInstance();
	
							long cpDefinitionId = 0;
	
							String thumbnailSrc = StringPool.BLANK;
	
							StringJoiner stringJoiner = new StringJoiner(StringPool.COMMA);
							
							if (cpInstance != null) {
								CPDefinition cpDefinition = commerceOrderItem.getCPDefinition();
	
								cpDefinitionId = cpDefinition.getCPDefinitionId();
	
								 //thumbnailSrc = baseAddressCheckoutStepDisplayContext.getCommerceOrderItemThumbnailSrc(commerceOrderItem); 
	
								 /* List<KeyValuePair> keyValuePairs = commerceContext.getKeyValuePairs(
										commerceOrderItem.getCPDefinitionId(), commerceOrderItem.getJson(), locale);
	
								for (KeyValuePair keyValuePair : keyValuePairs) {
									stringJoiner.add(keyValuePair.getValue());
								}   */
							}
						%> 
						
						<%-- <div class="image-course">
							<img alt="" class="image-course" src="<%=thumbnailSrc%>" style="width:60px;height:40px;"/>
						</div>  --%> 
						
						<div class="image-course">
							<img alt="" class="img-fluid" src="https://via.placeholder.com/300x150" />
						</div>
						
						<div class="box-name">
                            <div class="desc-exams"><%=HtmlUtil.escape(commerceOrderItem.getName("en-GB"))%></div>
                            <!-- <div class="category">CISCO</div> -->
                        </div> 
                        
                        <%-- <liferay-portlet:runtime portletName="com_liferay_commerce_cart_content_web_internal_portlet_CommerceCartContentPortlet"></liferay-portlet:runtime> --%>                       
					</div>
					
					
					
					<!-- ======================================================================================================== -->
					
					
					<!--Selcetion exam one-->
					<div class="section-exams">
						<div class="section-title">Selection 1 :</div>
						<div class="toggle-select" data-id="#one" id="toggle-select-one">Select
							Date &amp; Time</div>
						<div class="box-date-select active" id="one">
							<div class="box-input">
								<div class="form-group">
									<label for="date">Date <span class="red">*</span>
									</label> <input class="cs-form date" id="date-input-one" required=""
										type="date" />
								</div>
							</div>
							<div class="box-input">
								<div class="form-group">
									<label for="date">Time <span class="red">*</span>
									</label> <select class="cs-form-option" id="timeone">
										<option value="09.30 a.m">09.30 a.m</option>
										<option value="10.10 a.m">10.10 a.m</option>
										<option value="15.30 a.m">15.30 a.m</option>
										<option value="18.30 a.m">18.30 a.m</option>
									</select>
								</div>
							</div>
							<div class="btn" id="submit-one">submit</div>
						</div>
						<div class="box-value d-none" id="box-value-one">
							<span class="val-target" id="val-target-one"></span> <span
								class="koma">, </span> <span class="val-target-time"
								id="val-target-time-one"></span> <span class="edit-mode d-none"
								id="edit-one">edit <i class="fas fa-edit"></i>
							</span>
						</div>
						<script>
                $('#edit-one').on('click', function() {
                  $('#toggle-select-one').removeClass('d-flex');
                  $('#toggle-select-one').removeClass('d-none');
                  $('#one').removeClass('d-none');
                  $('#one').addClass('active');
                  $('#box-value-one').addClass('d-none');
                });
                $('#submit-one').on('click', function() {
                  const month = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
                  var date = new Date($('#date-input-one').val());
                  var day = date.getDate();
                  var namemonth = month[date.getMonth()];
                  var year = date.getFullYear();
                  var val = $('#timeone').val();
                  $('#val-target-one').text(day + " " + namemonth + " " + year);
                  $('#val-target-time-one').text(val);
                  if (date == "Invalid Date") {
                    $('#one').addClass('active');
                    $('#box-value-one').addClass('d-none');
                    alert("please input date")
                  } else {
                    $('#one').addClass('d-none');
                    $('#one').removeClass('d-flex');
                    $('#edit-one').removeClass('d-none');
                    $('#edit-one').addClass('d-block');
                    $('#toggle-select-one').addClass('d-none');
                    $('#box-value-one').removeClass('d-none');
                  }
                });
              </script>
					</div>
					<!--Selcetion exam one end-->
					<!--Selcetion exam two-->
					<div class="section-exams">
						<div class="section-title">Selection 2 :</div>
						<div class="toggle-select" data-id="#two" id="toggle-select-two">Select
							Date &amp; Time</div>
						<div class="box-date-select" id="two">
							<div class="box-input">
								<div class="form-group">
									<label for="date">Date <span class="red">*</span>
									</label> <input class="cs-form date" id="date-input-two" required=""
										type="date" />
								</div>
							</div>
							<div class="box-input">
								<div class="form-group">
									<label for="date">Time <span class="red">*</span>
									</label> <select class="cs-form-option" id="timetwo">
										<option value="09.30 a.m">09.30 a.m</option>
										<option value="10.10 a.m">10.10 a.m</option>
										<option value="15.30 a.m">15.30 a.m</option>
										<option value="18.30 a.m">18.30 a.m</option>
									</select>
								</div>
							</div>
							<div class="btn" id="submit-two">submit</div>
						</div>
						<div class="box-value d-none" id="box-value-two">
							<span class="val-target" id="val-target-two"></span> <span
								class="koma">, </span> <span class="val-target-time"
								id="val-target-time-two"></span> <span class="edit-mode d-none"
								id="edit-two">edit <i class="fas fa-edit"></i>
							</span>
						</div>
						<script>
                $('#edit-two').on('click', function() {
                  $('#toggle-select-two').removeClass('d-flex');
                  $('#toggle-select-two').removeClass('d-none');
                  $('#two').removeClass('d-none');
                  $('#two').addClass('active');
                  $('#box-value-two').addClass('d-none');
                });
                $('#submit-two').on('click', function() {
                  const month = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
                  var date = new Date($('#date-input-two').val());
                  var day = date.getDate();
                  var namemonth = month[date.getMonth()];
                  var year = date.getFullYear();
                  var val = $('#timetwo').val();
                  $('#val-target-two').text(day + " " + namemonth + " " + year);
                  $('#val-target-time-two').text(val);
                  if (date == "Invalid Date") {
                    $('#two').addClass('active');
                    $('#box-value-two').addClass('d-none');
                    alert("please input date")
                  } else {
                    $('#two').addClass('d-none');
                    $('#two').removeClass('d-flex');
                    $('#edit-two').removeClass('d-none');
                    $('#edit-two').addClass('d-block');
                    $('#toggle-select-two').addClass('d-none');
                    $('#box-value-two').removeClass('d-none');
                  }
                });
              </script>
					</div>
					<!--Selcetion exam two end-->
					<!--Selcetion exam three-->
					<div class="section-exams">
						<div class="section-title">Selection 3 :</div>
						<div class="toggle-select" data-id="#three"
							id="toggle-select-three">Select Date &amp; Time</div>
						<div class="box-date-select" id="three">
							<div class="box-input">
								<div class="form-group">
									<label for="date">Date <span class="red">*</span>
									</label> <input class="cs-form date" id="date-input-three" required=""
										type="date" />
								</div>
							</div>
							<div class="box-input">
								<div class="form-group">
									<label for="date">Time <span class="red">*</span>
									</label> <select class="cs-form-option" id="timethree">
										<option value="09.30 a.m">09.30 a.m</option>
										<option value="10.10 a.m">10.10 a.m</option>
										<option value="15.30 a.m">15.30 a.m</option>
										<option value="18.30 a.m">18.30 a.m</option>
									</select>
								</div>
							</div>
							<div class="btn" id="submit-three">submit</div>
						</div>
						<div class="box-value d-none" id="box-value-three">
							<span class="val-target" id="val-target-three"></span> <span
								class="koma">, </span> <span class="val-target-time"
								id="val-target-time-three"></span> <span
								class="edit-mode d-none" id="edit-three">edit <i
								class="fas fa-edit"></i>
							</span>
						</div>
						<script>
                $('#edit-three').on('click', function() {
                  $('#toggle-select-three').removeClass('d-flex');
                  $('#toggle-select-three').removeClass('d-none');
                  $('#three').removeClass('d-none');
                  $('#three').addClass('active');
                  $('#box-value-three').addClass('d-none');
                });
                $('#submit-three').on('click', function() {
                  const month = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
                  var date = new Date($('#date-input-three').val());
                  var day = date.getDate();
                  var namemonth = month[date.getMonth()];
                  var year = date.getFullYear();
                  var val = $('#timethree').val();
                  $('#val-target-three').text(day + " " + namemonth + " " + year);
                  $('#val-target-time-three').text(val);
                  if (date == "Invalid Date") {
                    $('#three').addClass('active');
                    $('#box-value-three').addClass('d-none');
                    alert("please input date")
                  } else {
                    $('#three').addClass('d-none');
                    $('#three').removeClass('d-flex');
                    $('#edit-three').removeClass('d-none');
                    $('#edit-three').addClass('d-block');
                    $('#toggle-select-three').addClass('d-none');
                    $('#box-value-three').removeClass('d-none');
                  }
                });
              </script>
					</div>
					<!--Selcetion exam three end-->
					<div class="box-additional">
						<div class="additional-notes">ADDITIONAL NOTES</div>
						<textarea class="additional-box" id="" name=""></textarea>
					</div>
					<div class="citred">
						<img alt="" src="/o/web.ntuc.eshop.register/img/Info_Icon.png" />
						If you would like to get funding for this, please apply for <a
							href="#">CITREP+funding</a> here.
					</div>
					<hr />
					
					
					<%-- <div class="box-billing-address">
						<c:if test="<%=!commerceOrder.isGuestOrder()%>">
							<!-- <div class="title">Billing Address :</div> -->
							<div class="box-input">
								<div class="form-group">									
									<aui:select label="<%=selectLabel%>" name="commerceAddress"
										onChange='<%=liferayPortletResponse.getNamespace() + "selectAddress();"%>'
										wrapperCssClass="commerce-form-group-item-row form-group-item" 
										style="width: 100% !important;"
										cssClass="clearable cs-form "
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
											label="<%=commerceAddress.getName()%>"
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
									
								</div>
							</div>
						</c:if>
						<aui:input disabled="<%=commerceAddresses.isEmpty() ? true : false%>"
							name="<%=paramName%>" type="hidden" value="<%=commerceAddressId%>"
						/>
						<aui:input name="newAddress" type="hidden"
							value='<%=(commerceAddressId > 0) ? "0" : "1"%>'
						/>
						<div class="box-input">
							<div class="form-group">
								<label class="control-label" for="<portlet:namespace />street1">Address
									<span style="color: red;">*</span>
								</label>
								<aui:input disabled="<%=commerceAddressId > 0%>" label=""
									name="street1" cssClass="clearable cs-form "
									wrapperCssClass="form-group-item"
								/>
							</div>
						</div>
						<div class="box-input">
							<div class="form-group">
								<label class="control-label" for="<portlet:namespace />address2">Address
									2 </label>
								<aui:input disabled="<%=commerceAddressId > 0%>" label=""
									name="street2" cssClass="clearable cs-form "
									wrapperCssClass="form-group-item"
								/>
							</div>
						</div>
						<div class="box-input">
							<div class="form-group">
								<label class="control-label" for="<portlet:namespace />zip">Postal Code
									<span style="color: red;">*</span>
								</label>
								<aui:input disabled="<%=commerceAddressId > 0%>" label="" name="zip"
									cssClass="clearable cs-form " wrapperCssClass="form-group-item"
								>
									<aui:validator name="maxLength">7</aui:validator>
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
						</div>
						<div class="box-input">
							<div class="form-group">
								<label class="control-label" for="<portlet:namespace/>city">City
									<span style="color: red;">*</span>
								</label>
								<aui:input disabled="<%=commerceAddressId > 0%>" label=""
									name="city" cssClass="clearable cs-form " wrapperCssClass="form-group-item"
								/>
							</div>
						</div>
						<div class="box-input">
							<div class="form-group">
								<label class="control-label" for="<portlet:namespace />country">Country
									<span style="color: red;">*</span>
								</label>
								<aui:select disabled="<%=commerceAddressId > 0%>" label=""
									name="commerceCountryId" cssClass="clearable cs-form " title="country"
									wrapperCssClass="form-group-item"
								>
									<aui:validator
										errorMessage='<%=LanguageUtil.get(request, "please-enter-a-valid-country")%>'
										name="min"
									>1</aui:validator>
								</aui:select>
							</div>
						</div>
						<div class="box-input">
							<div class="form-group">
								<label class="control-label" for="<portlet:namespace />phoneNumber">Contact Number
									<span style="color: red;">*</span>
								</label>
								<aui:input disabled="<%=commerceAddressId > 0%>" label=""
									name="phoneNumber" cssClass="clearable cs-form "
									wrapperCssClass="form-group-item">
										<aui:validator name="required" />
								</aui:input>
							</div>
						</div>
						<div class="form-group form-check">
							<aui:input id="agree" name="agree" type="checkbox" value="agree"
								label="Iggree to Terms of condition By clicking
								submit, I consent , and agree to submit information relating to
								another individual, I repseent and warrant that such individual
								also consents, to NTUC Learning Hub companies Terms of
								condition."
								cssClass="form-check-input" wrapperCssClass="inline-remover"
								inlineLabel="right"
							>
								<aui:validator name="required" errorMessage="Please check this box to proceed."/>
							</aui:input>
						</div>
					</div> --%>
				</div>
			</div>
			
			<div class="col-md-12 col-xl-4">
				<div class="box-stick-global">
					<div class="box-header">
						<liferay-portlet:runtime portletName="com_liferay_commerce_cart_content_web_internal_portlet_CommerceCartContentTotalPortlet"> </liferay-portlet:runtime>						
					</div>
				</div>
			</div>
		</div>
	</div>
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
<%-- <div class="address-fields">
	<div class="add-street-fields hide">
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
</div> --%>
<c:if
	test="<%=Objects.equals(CommerceCheckoutWebKeys.SHIPPING_ADDRESS_PARAM_NAME, paramName)%>"
>
	<div class="shipping-as-billing">
		<aui:input
			checked="<%=baseAddressCheckoutStepDisplayContext.isShippingUsedAsBilling() || (commerceAddressId == 0)%>"
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
			{
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
			},
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
