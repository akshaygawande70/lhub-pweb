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

<%@page import="com.liferay.commerce.model.CommerceOrder"%>
<%@page import="com.liferay.commerce.model.CommerceOrderItem"%>
<%@page import="com.liferay.asset.entry.rel.model.AssetEntryAssetCategoryRel"%>
<%@page import="com.liferay.asset.entry.rel.service.AssetEntryAssetCategoryRelLocalServiceUtil"%>
<%@page import="com.liferay.asset.kernel.model.AssetCategory"%>
<%@page import="com.liferay.asset.kernel.model.AssetEntry"%>
<%@page import="com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil"%>
<%@page import="com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil"%>
<%@page import="com.liferay.commerce.price.CommerceProductPrice"%>
<%@page import="com.liferay.commerce.product.type.virtual.constants.VirtualCPTypeConstants"%>

<%@page import="java.util.stream.Collectors"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayPortletMode"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portal.kernel.portlet.PortletURLFactoryUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.service.PortletPreferencesLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.model.PortletPreferences"%>
<%@page import="com.liferay.portal.kernel.service.LayoutLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.model.Layout"%>
<%@page import="com.liferay.portal.kernel.security.auth.AuthTokenUtil"%>
<%@page import="java.util.List"%>

<%@page import="com.liferay.portal.kernel.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.model.Role"%>	
<%@page import="com.liferay.portal.kernel.model.User"%>	
<%@page import="com.liferay.portal.kernel.service.RoleServiceUtil"%>	
<%@page import="com.liferay.commerce.account.model.CommerceAccount"%>	
<%@page import="com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil"%>	
<%@page import="api.ntuc.common.util.RoleUtil"%>
<%@page import="java.io.Serializable" %>

<%@ include file="/init.jsp"%>

<%
CommerceCartContentDisplayContext commerceCartContentDisplayContext = (CommerceCartContentDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

Map<String, Object> contextObjects = HashMapBuilder.<String, Object>put(
	"commerceCartContentDisplayContext", commerceCartContentDisplayContext
).build();

SearchContainer<CommerceOrderItem> commerceOrderItemSearchContainer = commerceCartContentDisplayContext.getSearchContainer();

PortletURL portletURL = commerceCartContentDisplayContext.getPortletURL();

portletURL.setParameter("searchContainerId", "commerceOrderItems");

request.setAttribute("view.jsp-portletURL", portletURL);

List<CommerceOrderValidatorResult> commerceOrderValidatorResults = new ArrayList<>();

Map<Long, List<CommerceOrderValidatorResult>> commerceOrderValidatorResultMap = commerceCartContentDisplayContext.getCommerceOrderValidatorResults();

CommerceOrder commerceOrder = commerceCartContentDisplayContext.getCommerceOrder();
List<String> productCategoryList = new ArrayList<String>();

float discountFromCommerceOrder = commerceOrder.getSubtotalDiscountAmount().floatValue();

for (CommerceOrderItem item : commerceOrder.getCommerceOrderItems()) {
	try {
		CPDefinition cpDefinition = item.getCPDefinition();
		AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(CPDefinition.class.getName(), cpDefinition.getCPDefinitionId());
	    long entryId = assetEntry.getEntryId();
	    List<AssetEntryAssetCategoryRel> aeacRelLocalService = AssetEntryAssetCategoryRelLocalServiceUtil.getAssetEntryAssetCategoryRelsByAssetEntryId(entryId);
	    long assetCategoryId = aeacRelLocalService.get(0).getAssetCategoryId();
	    AssetEntryAssetCategoryRel assetEntryAssetEntryCategoryRel = AssetEntryAssetCategoryRelLocalServiceUtil.fetchAssetEntryAssetCategoryRel(entryId, assetCategoryId);
	    long categoryId = assetEntryAssetEntryCategoryRel.getAssetCategoryId();
	    AssetCategory assetCategory = AssetCategoryLocalServiceUtil.fetchAssetCategory(categoryId);
	    String categoryName = assetCategory.getName();
	    productCategoryList.add(categoryName);
	} catch (Exception e) {
		productCategoryList.add("Doesnt have category");
	}
}

int temp = 0;
int temp2 = 0;
int discountDetector = 0;

%>

<%
	int accType = 0;
	String companyName = "";
	long accountId = 0;
	List<Role> roleList = new ArrayList<Role>();
	for (Long roleId : user.getRoleIds()) {
		Role newRole = RoleServiceUtil.getRole(roleId);
		roleList.add(newRole);
	}
	CommerceAccount account2 = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());
	if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Individual_Role")) {
		accType = 1;
	} else if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Corporate_Role")) {
		accType = 2;
		companyName = (String) account2.getExpandoBridge().getAttribute("Company Name");
	}
	
	int type = accType;
	String authToken = AuthTokenUtil.getToken(request);
	String invoicePortletName = "NTUC_Invoice_ExamMerchandize_Portlet"; 
	String invoiceUrl = "/account/examinvoices";
	Layout layout2 = LayoutLocalServiceUtil.getFriendlyURLLayout(themeDisplay.getSiteGroupId(), false, invoiceUrl);
	long plid2 = layout2.getPlid();
	List<PortletPreferences> preferences = PortletPreferencesLocalServiceUtil.getPortletPreferencesByPlid(plid2);
	List<PortletPreferences> filteredPreferences = preferences.stream().filter(x->x.getPortletId().contains(invoicePortletName)).collect(Collectors.toList());
	
	PortletURL url = null;
	if(filteredPreferences.size()>0){
		String portletId2 = filteredPreferences.get(0).getPortletId();
		
		url = PortletURLFactoryUtil.create(request, portletId2, plid2 ,
			PortletRequest.RENDER_PHASE);
		
		url.setParameter("mvcRenderCommandName", "examMerchandizeOrderDetailRender");
		url.setParameter("accType", String.valueOf(type));
		url.setParameter("orderId", String.valueOf(commerceOrder.getCommerceOrderId()));
		url.setParameter("authToken", authToken);
		url.setWindowState(LiferayWindowState.MAXIMIZED);
		url.setPortletMode(LiferayPortletMode.VIEW);
	}
	
	commerceOrder.getExpandoBridge().setAttribute("Order Link", url.toString());
%> 

<liferay-ui:error exception="<%= CommerceOrderValidatorException.class %>">

	<%
	CommerceOrderValidatorException commerceOrderValidatorException = (CommerceOrderValidatorException)errorException;

	if (commerceOrderValidatorException != null) {
		commerceOrderValidatorResults = commerceOrderValidatorException.getCommerceOrderValidatorResults();
	}

	for (CommerceOrderValidatorResult commerceOrderValidatorResult : commerceOrderValidatorResults) {
	%>
		<liferay-ui:message key="<%= commerceOrderValidatorResult.getLocalizedMessage() %>" />
	<%
	}
	%>

</liferay-ui:error>

<div class="box-checkout-ver2">
	<div class="container" id="header-title" style="display: none;">
		<div class="row justify-content-center">
			<div class="col-md-6">
				<div class="notice-alert alert-blue">
					<img alt="" src="/o/web.ntuc.eshop.register/img/Info_Icon.png" />
					You are only allowed to add one exam into the cart at one time.
				</div>
			</div>
		</div>
	</div>

	<div class="section-step-one">
		<div class="container">
			<div class="row">
				
				<div class="col-md-12 col-xl-8">
				
				
					<div class="display-desktop">
						<!---for desktop-->
						<div class="row">
							<table>
								<tbody>
									<tr>
										<th>Product</th>
										<th>Price</th>
										<th>Quantity</th>
										<th>Total</th>
										<th> </th>
									</tr>
									
									<liferay-ddm:template-renderer
									className="<%=CommerceCartContentPortlet.class.getName()%>"
									contextObjects="<%=contextObjects%>"
									displayStyle="<%=commerceCartContentDisplayContext.getDisplayStyle()%>"
									displayStyleGroupId="<%=commerceCartContentDisplayContext.getDisplayStyleGroupId()%>"
									entries="<%=commerceOrderItemSearchContainer.getResults()%>">
									<liferay-ui:search-container id="commerceOrderItems"
										iteratorURL="<%=portletURL%>"
										searchContainer="<%=commerceOrderItemSearchContainer%>">
										<liferay-ui:search-container-row
											className="com.liferay.commerce.model.CommerceOrderItem"
											cssClass="entry-display-style" keyProperty="CommerceOrderItemId"
											modelVar="commerceOrderItem">
									
									<tr>
										<td> <!-- Image, Name, category product -->
											<div class="box-produk">
												<%
													CPInstance cpInstance = commerceOrderItem.fetchCPInstance();

													long cpDefinitionId = 0;

													String thumbnailSrc = StringPool.BLANK;

													StringJoiner stringJoiner = new StringJoiner(StringPool.COMMA);
													CPDefinition cpDefinition = null;
													if (cpInstance != null) {
														cpDefinition = commerceOrderItem.getCPDefinition();

														cpDefinitionId = cpDefinition.getCPDefinitionId();

														thumbnailSrc = commerceCartContentDisplayContext
																.getCommerceOrderItemThumbnailSrc(commerceOrderItem);

														List<KeyValuePair> keyValuePairs = commerceCartContentDisplayContext.getKeyValuePairs(
																commerceOrderItem.getCPDefinitionId(), commerceOrderItem.getJson(), locale);

														for (KeyValuePair keyValuePair : keyValuePairs) {
															stringJoiner.add(keyValuePair.getValue());
														}
													}
												%>
												<c:if test="<%=cpDefinition.getProductTypeName().equals(VirtualCPTypeConstants.NAME) == true %>">
													<script>
														setTimeout(function(){
															$("#header-title").show();
														},500);
													</script>
												</c:if>
												<div class="img-produk">
													<img alt="" class="img-fluid" src="<%=thumbnailSrc%>" />
												</div>

												<div class="box-produk">
													<div class="name-produk">
														<a class="font-weight-bold"
															href="<%=(cpDefinitionId == 0) ? StringPool.BLANK : commerceCartContentDisplayContext.getCPDefinitionURL(cpDefinitionId, themeDisplay)%>">
															<%=HtmlUtil.escape(commerceOrderItem.getName(languageId))%>
														</a>
														<h6 class="text-default">
															<%=HtmlUtil.escape(stringJoiner.toString())%>
														</h6>
														<c:if test="<%= !commerceOrderValidatorResultMap.isEmpty() %>">
															<%
															commerceOrderValidatorResults = commerceOrderValidatorResultMap.get(commerceOrderItem.getCommerceOrderItemId());
								
															for (CommerceOrderValidatorResult commerceOrderValidatorResult : commerceOrderValidatorResults) {
															%>
								
																<div class="alert-danger commerce-alert-danger">
																	<liferay-ui:message key="<%= commerceOrderValidatorResult.getLocalizedMessage() %>" />
																</div>
								
															<%
															}
															%>
														</c:if>
													</div>

													<div class="category-produk">
														<%=productCategoryList.get(temp)%>
													</div>
													
												</div>
											</div>
										</td>
										<td><span class="blue-color"> <!-- price -->
											<%
												String commercePriceDisplayType = commerceCartContentDisplayContext
												.getCommercePriceDisplayType();
											%> 
											<c:if test="<%=commerceCartContentDisplayContext.hasViewPricePermission()%>">
												<%
													CommerceMoney getUnitPrice = commerceOrderItem.getPromoPrice()!=null && commerceOrderItem.getPromoPrice().floatValue()>0 ? 
				                            			commerceOrderItem.getPromoPriceMoney() : commerceOrderItem.getUnitPriceMoney() ;
												%>
												$ <%=HtmlUtil.escape(getUnitPrice.format(locale))%>
												<commerce-ui:product-subscription-info
														CPInstanceId="<%=commerceOrderItem.getCPInstanceId()%>"
														showDuration="<%=false%>" />
											</c:if>
										</span></td>
										<td><!-- Quantity -->
											<span class="blue-color text-qty">
												<%=commerceOrderItem.getQuantity()%>
											</span>
										</td>
										<td> <!-- total -->
											<span class="blue-color text-total">
												<c:if test="<%= commerceCartContentDisplayContext.hasViewPricePermission() %>">
													<%
													CommerceMoney finalPriceCommerceMoney = commerceOrderItem.getFinalPriceMoney();
													
													float discountAmountFloat = 0;
													try {
														CommerceMoney discountAmount = commerceOrderItem.getDiscountAmountMoney();
														discountAmountFloat = Float.valueOf(discountAmount.getPrice().toString()).floatValue();
														discountDetector++;
													} catch (Exception e) {
														discountAmountFloat = 0;
													}
													discountAmountFloat = discountAmountFloat + discountFromCommerceOrder;
													float finalPriceCommerceMoneyFloat = Float.valueOf(finalPriceCommerceMoney.getPrice().toString()).floatValue();
													float finalPricePlusDiscount = finalPriceCommerceMoneyFloat - discountAmountFloat;
													String finalPricePlusDiscountString = String.valueOf(finalPricePlusDiscount);
						
													if (commercePriceDisplayType.equals(CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {
														finalPriceCommerceMoney = commerceOrderItem.getFinalPriceWithTaxAmountMoney();
													}
													
													float getPriceQty = commerceOrderItem.getPromoPrice()!=null && commerceOrderItem.getPromoPrice().floatValue()>0 ? 
					                            			commerceOrderItem.getPromoPrice().floatValue() : commerceOrderItem.getUnitPrice().floatValue() ;
													float getFinalPriceMoney = getPriceQty * commerceOrderItem.getQuantity();
													%>
													$ <%= HtmlUtil.escape(String.format(locale, "%,.2f", getFinalPriceMoney))%>
													
													<commerce-ui:product-subscription-info
														CPInstanceId="<%= commerceOrderItem.getCPInstanceId() %>"
														showDuration="<%= false %>"
													/>
												</c:if>
											</span>
										</td>
										<td> <!-- action -->
											<c:if test="<%=commerceCartContentDisplayContext.hasPermission(ActionKeys.UPDATE)%>">
												<a href="<%=commerceCartContentDisplayContext.getDeleteURL(commerceOrderItem)%>">
													<i class="fas fa-trash-alt text-center delete"></i></a>
											</c:if>
										</td>
									</tr>
									
									</liferay-ui:search-container-row>
								</liferay-ui:search-container>
							</liferay-ddm:template-renderer>
							
								</tbody>
							</table>
						</div>
					</div>

					<!-- taro display mobile disini  -->
					
					<liferay-ddm:template-renderer
									className="<%=CommerceCartContentPortlet.class.getName()%>"
									contextObjects="<%=contextObjects%>"
									displayStyle="<%=commerceCartContentDisplayContext.getDisplayStyle()%>"
									displayStyleGroupId="<%=commerceCartContentDisplayContext.getDisplayStyleGroupId()%>"
									entries="<%=commerceOrderItemSearchContainer.getResults()%>">
									<liferay-ui:search-container id="commerceOrderItems"
										iteratorURL="<%=portletURL%>"
										searchContainer="<%=commerceOrderItemSearchContainer%>">
										<liferay-ui:search-container-row
											className="com.liferay.commerce.model.CommerceOrderItem"
											cssClass="entry-display-style" keyProperty="CommerceOrderItemId"
											modelVar="commerceOrderItem">
											
									<div class="display-mobile">
										<!---for mobile-->
										<%
											CPInstance cpInstance = commerceOrderItem.fetchCPInstance();

											long cpDefinitionId = 0;

											String thumbnailSrc = StringPool.BLANK;

											StringJoiner stringJoiner = new StringJoiner(StringPool.COMMA);

											if (cpInstance != null) {
												CPDefinition cpDefinition = commerceOrderItem.getCPDefinition();

												cpDefinitionId = cpDefinition.getCPDefinitionId();

												thumbnailSrc = commerceCartContentDisplayContext
														.getCommerceOrderItemThumbnailSrc(commerceOrderItem);

												List<KeyValuePair> keyValuePairs = commerceCartContentDisplayContext.getKeyValuePairs(
														commerceOrderItem.getCPDefinitionId(), commerceOrderItem.getJson(), locale);

												for (KeyValuePair keyValuePair : keyValuePairs) {
													stringJoiner.add(keyValuePair.getValue());
												}
											}
										%>
										<div class="row">
											<div class="col-3">
												<div class="img-produk">
													<img alt="" class="img-fluid" src="<%=thumbnailSrc%>" />
												</div>
											</div>
									
											<div class="col-9">
												<div class="row">
													<div class="box-produk-mb">
														<div class="name-produk-mb">
															<a class="font-weight-bold"
																href="<%=(cpDefinitionId == 0) ? StringPool.BLANK : commerceCartContentDisplayContext.getCPDefinitionURL(cpDefinitionId, themeDisplay)%>">
																<%=HtmlUtil.escape(commerceOrderItem.getName(languageId))%>
															</a>
															<h6 class="text-default">
																<%=HtmlUtil.escape(stringJoiner.toString())%>
															</h6>
															<c:if test="<%= !commerceOrderValidatorResultMap.isEmpty() %>">
																<%
																commerceOrderValidatorResults = commerceOrderValidatorResultMap.get(commerceOrderItem.getCommerceOrderItemId());
									
																for (CommerceOrderValidatorResult commerceOrderValidatorResult : commerceOrderValidatorResults) {
																%>
									
																	<div class="alert-danger commerce-alert-danger">
																		<liferay-ui:message key="<%= commerceOrderValidatorResult.getLocalizedMessage() %>" />
																	</div>
									
																<%
																}
																%>
															</c:if>
														</div>
									
														<div class="category-produk-mb"><%=productCategoryList.get(temp)%>
															<%
																temp++;
															%>
														</div>
									
														<div class="price-mb">
															<%
																String commercePriceDisplayType = commerceCartContentDisplayContext
																.getCommercePriceDisplayType();
															%> 
															Price : 
															<span>
																<c:if test="<%=commerceCartContentDisplayContext.hasViewPricePermission()%>">
																	<%
																		CommerceMoney finalPriceCommerceMoney = commerceOrderItem.getUnitPriceWithTaxAmountMoney();
																	%>
																	$ <%=HtmlUtil.escape(finalPriceCommerceMoney.format(locale))%>
																	<commerce-ui:product-subscription-info
																			CPInstanceId="<%=commerceOrderItem.getCPInstanceId()%>"
																			showDuration="<%=false%>" />
																</c:if>
															</span>
														</div>
									
														<div class="quantity-mb">
															Quantity : <span><%=commerceOrderItem.getQuantity()%></span>
														</div>
														
														<div class="total-mb">
															Total : 
																<span>
																	<c:if test="<%= commerceCartContentDisplayContext.hasViewPricePermission() %>">
																		<%
																		CommerceMoney finalPriceCommerceMoney = commerceOrderItem.getFinalPriceMoney();
											
																		if (commercePriceDisplayType.equals(CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {
																			finalPriceCommerceMoney = commerceOrderItem.getFinalPriceWithTaxAmountMoney();
																		}
																		%>
											
																		<%= HtmlUtil.escape(finalPriceCommerceMoney.format(locale)) %>
											
																		<commerce-ui:product-subscription-info
																			CPInstanceId="<%= commerceOrderItem.getCPInstanceId() %>"
																			showDuration="<%= false %>"
																		/>
																	</c:if>
																</span>
														</div>
									
														<div class="delete-produk-mb">
															<c:if test="<%=commerceCartContentDisplayContext.hasPermission(ActionKeys.UPDATE)%>">
																<a href="<%=commerceCartContentDisplayContext.getDeleteURL(commerceOrderItem)%>">
																	<i class="fas fa-trash-alt text-center delete"></i></a>
															</c:if>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>	
							</liferay-ui:search-container-row>
						</liferay-ui:search-container>
					</liferay-ddm:template-renderer>
					
					
					<liferay-portlet:runtime
						portletName="com_liferay_commerce_discount_content_web_internal_portlet_CommerceDiscountContentPortlet"
					></liferay-portlet:runtime>	
				</div>
			
		
			
		<div class="col-md-12 col-xl-4">
			<div class="box-stick-price">
				<div class="box-header">
					<liferay-portlet:runtime
						portletName="com_liferay_commerce_cart_content_web_internal_portlet_CommerceCartContentTotalPortlet"
					></liferay-portlet:runtime>
				</div>
			</div>
		</div>
			</div>
		</div>
	</div>
</div>
<aui:script>
	Liferay.after('commerce:productAddedToCart', function (event) {
		Liferay.Portlet.refresh('#p_p_id<portlet:namespace />');
	});
</aui:script>
		