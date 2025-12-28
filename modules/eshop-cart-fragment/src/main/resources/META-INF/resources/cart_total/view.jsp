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

<%@page import="com.liferay.asset.entry.rel.model.AssetEntryAssetCategoryRel"%>
<%@page import="com.liferay.asset.entry.rel.service.AssetEntryAssetCategoryRelLocalServiceUtil"%>
<%@page import="com.liferay.asset.kernel.model.AssetCategory"%>
<%@page import="com.liferay.asset.kernel.model.AssetEntry"%>
<%@page import="com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil"%>
<%@page import="com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil"%>

<%@ include file="/init.jsp"%>

<%
		
		CommerceCartContentDisplayContext commerceCartContentDisplayContext = (CommerceCartContentDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
		List<CommerceOrderValidatorResult> commerceOrderValidatorResults = new ArrayList<>();
		Map<Long, List<CommerceOrderValidatorResult>> commerceOrderValidatorResultMap = commerceCartContentDisplayContext.getCommerceOrderValidatorResults();
		CommerceCartContentTotalDisplayContext commerceCartContentTotalDisplayContext = (CommerceCartContentTotalDisplayContext) request
				.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
		CommerceMoney taxValueCommerceMoney = null;
		CommerceMoney totalOrderCommerceMoney = null;
		CommerceMoney shippingValueCommerceMoney = null;
		String priceDisplayType = commerceCartContentTotalDisplayContext.getCommercePriceDisplayType();
		CommerceOrderPrice commerceOrderPrice = commerceCartContentTotalDisplayContext.getCommerceOrderPrice();
		double commerceTaxFixedRate = CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).size()>0?
				CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).get(0).getRate():0;
		
		Map<String, Object> contextObjects = HashMapBuilder.<String, Object>put(
				"commerceCartContentTotalDisplayContext", commerceCartContentTotalDisplayContext).build();
	
		SearchContainer<CommerceOrderItem> commerceOrderItemSearchContainer = commerceCartContentTotalDisplayContext
				.getSearchContainer();
		
		PortletURL portletURL = commerceCartContentDisplayContext.getPortletURL();
		portletURL.setParameter("searchContainerId", "commerceOrderItems");
		request.setAttribute("view.jsp-portletURL", portletURL);
		float discountAmountFloat = 0;
		
		CommerceOrder commerceOrder = commerceCartContentTotalDisplayContext.getCommerceOrder();
		float discountFromCommerceOrder = commerceOrder.getShippingDiscountAmount().floatValue() + 
				commerceOrder.getSubtotalDiscountAmount().floatValue()+
				commerceOrder.getTotalDiscountAmount().floatValue(); 
		
		float totalProductPrice = 0;
		
		if (commerceOrderPrice != null) {			
			
			shippingValueCommerceMoney = commerceOrderPrice.getShippingValue();
	
			taxValueCommerceMoney = commerceOrderPrice.getTaxValue();
			totalOrderCommerceMoney = commerceOrderPrice.getTotal();
		
			if (priceDisplayType.equals(CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {	
				totalOrderCommerceMoney = commerceOrderPrice.getTotalWithTaxAmount();	
			}		
		}
		
		List<String> productCategoryList = new ArrayList<String>();
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
		
		float calculatedProductPrice = 0;
	%>
	

							
							<div class="box-header">
								
								<div class="title-header">ORDER REVIEW</div>
								
								<div class="overflow-order">
									<liferay-ddm:template-renderer
									className="<%=CommerceCartContentTotalPortlet.class.getName()%>"
									contextObjects="<%=contextObjects%>"
									displayStyle="<%=commerceCartContentTotalDisplayContext.getDisplayStyle()%>"
									displayStyleGroupId="<%=commerceCartContentTotalDisplayContext.getDisplayStyleGroupId()%>"
									entries="<%=commerceOrderItemSearchContainer.getResults()%>">
										<liferay-ui:search-container id="commerceOrderItems" iteratorURL="<%=portletURL%>" searchContainer="<%=commerceOrderItemSearchContainer%>">
											<liferay-ui:search-container-row className="com.liferay.commerce.model.CommerceOrderItem" cssClass="entry-display-style" 
												keyProperty="CommerceOrderItemId" modelVar="commerceOrderItem">
									
									<%
										discountAmountFloat = discountAmountFloat + commerceOrderItem.getDiscountAmount().floatValue();
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
									
									<div class="row box-produk">
										<div class="col-3 nopadding">
											<div class="img-produk"><img alt="" class="image-course" src="<%=thumbnailSrc%>" style="width:60px;height:auto;"/></div>
										</div>
										
										<div class="col-6 nopadding">
			                                <div class="title-order"><%=HtmlUtil.escape(commerceOrderItem.getName(languageId))%></div>
			                            </div>
			                            <%
			                            	float getPriceUnit = commerceOrderItem.getPromoPrice()!=null && commerceOrderItem.getPromoPrice().floatValue()>0 ? 
		                            			commerceOrderItem.getPromoPrice().floatValue() : commerceOrderItem.getUnitPrice().floatValue() ;
		                            		float getPriceQty = getPriceUnit*commerceOrderItem.getQuantity();
			                            	totalProductPrice = totalProductPrice + getPriceQty;
			                            %>
			                            <div class="col-3 nopadding">
			                            	<div class="price">$ <%=HtmlUtil.escape(String.format(locale, "%,.2f", getPriceUnit))%></div>
			                            </div>
			                            <div class="category-general">
			                            	<%=productCategoryList.get(temp)%>
			                            </div>
									</div>
									
									<%
										temp++;
									%>
								
								
									<br>
											</liferay-ui:search-container-row>
										</liferay-ui:search-container>
									</liferay-ddm:template-renderer>
								</div>
									<hr />
									<div class="box-text">
											<div class="text-left">Product Price</div>
											<div class="text-right">$ <%=HtmlUtil.escape(String.format(locale, "%,.2f", totalProductPrice))%></div>
									</div>
									<div class="box-text">
										<%
											if(commerceTaxFixedRate == (long) commerceTaxFixedRate) {%>
												<div class="text-left">including <%=String.format("%d",(long) commerceTaxFixedRate)%>% GST Included</div>
										<% } else { %>
												<div class="text-left">including <%=String.format("%s", commerceTaxFixedRate)%>% GST Included</div>
										<% } %>
										<div class="text-right">$ <%= HtmlUtil.escape(taxValueCommerceMoney.format(locale)) %></div>
									</div>
									<div class="box-text">
										<div class="text-left">Shipment</div>
										<div class="text-right">$ <%= HtmlUtil.escape(shippingValueCommerceMoney.format(locale)) %></div>
									</div>
									<div class="box-text">
										<div class="text-left">Total Discount</div>
										<div class="text-right">$ <%=HtmlUtil.escape(String.format(locale, "%,.2f", (discountAmountFloat+ discountFromCommerceOrder)))%></div>
									</div>
									<div class="box-text total-price">
										<div class="text-left">TOTAL</div>
										<div class="text-right">$ <%=HtmlUtil.escape(totalOrderCommerceMoney.format(locale))%></div>
									</div>
							</div>										

	<aui:script>
		Liferay.after('commerce:productAddedToCart', function (event) {
			Liferay.Portlet.refresh('#p_p_id<portlet:namespace />');
		});
	</aui:script>
	
		