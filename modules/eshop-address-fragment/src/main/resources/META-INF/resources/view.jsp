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
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%
	CommerceAddressDisplayContext commerceAddressDisplayContext = (CommerceAddressDisplayContext) request
			.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
%>
<%-- <liferay-util:include page="/edit_address.jsp" servletContext="<%= application %>" /> --%>

<style>
	.general-info-address {
	    padding-bottom: 5rem;
	}
</style>

<c:choose>
	<c:when
		test="<%=!commerceAddressDisplayContext.hasCommerceChannel()%>"
	>
		<div class="alert alert-info mx-auto">
			<liferay-ui:message key="this-site-does-not-have-a-channel" />
		</div>
	</c:when>
	<c:otherwise>
		<section class=".general-info-address myaccount-cxrus">
			<div class="container">
				<div class="row">
					<div class="col-md-3"></div>
					<div class="col-md-9">
						<!--Start content-->
						<div class="content active" id="pi">
							<div class="row w-100">
								<div class="col-md-12">
									<div class="heading-content">Address</div>
									<%@ include file="/edit_address.jsp"%> 
									
									<%
										Map<String, Object> contextObjects = HashMapBuilder
														.<String, Object>put("commerceAddressDisplayContext", commerceAddressDisplayContext)
														.build();

												SearchContainer<CommerceAddress> commerceAddressSearchContainer = commerceAddressDisplayContext
														.getSearchContainer();

												PortletURL portletURL = commerceAddressDisplayContext.getPortletURL();

												portletURL.setParameter("searchContainerId", "commerceAddresses");

												request.setAttribute("view.jsp-portletURL", portletURL);
									%>
									<div class="heading-content mt-10" style="margin-top:5rem;">Address List</div>
									<liferay-ddm:template-renderer
										className="<%=CommerceAddressContentPortlet.class.getName()%>"
										contextObjects="<%=contextObjects%>"
										displayStyle="<%=commerceAddressDisplayContext.getDisplayStyle()%>"
										displayStyleGroupId="<%=commerceAddressDisplayContext.getDisplayStyleGroupId()%>"
										entries="<%=commerceAddressSearchContainer.getResults()%>"
									>
										<div class="row"
											id="<portlet:namespace />addressesContainer"
										>
											<aui:form action="<%=portletURL.toString()%>"
												cssClass="container-fluid-1280" method="post" name="fm"
											>
												<aui:input name="<%=Constants.CMD%>" type="hidden"
													value="<%=Constants.DELETE%>"
												/>
												<aui:input name="redirect" type="hidden"
													value="<%=portletURL.toString()%>"
												/>
												<div class="addresses-container"
													id="<portlet:namespace />entriesContainer"
												>
													<liferay-ui:search-container id="commerceAddresses"
														iteratorURL="<%=portletURL%>"
														searchContainer="<%=commerceAddressSearchContainer%>"
													>
													
													
														<liferay-ui:search-container-row
															className="com.liferay.commerce.model.CommerceAddress"
															cssClass="entry-display-style"
															keyProperty="commerceAddressId"
															modelVar="commerceAddress"
														>
														<%if(commerceAddress.getType()==1){ %>
															<liferay-ui:search-container-column-text
																name="name" value="<%=HtmlUtil.escapeAttribute(commerceAddress.getName())%>"
															/>
															<liferay-ui:search-container-column-text
																cssClass="table-cell-content" name="street1" value="<%=HtmlUtil.escapeAttribute(commerceAddress.getStreet1())%>"
															/>
															<liferay-ui:search-container-column-text name="city" value="<%=HtmlUtil.escapeAttribute(commerceAddress.getCity())%>" />
															<liferay-ui:search-container-column-text name="zip" value="<%=HtmlUtil.escapeAttribute(commerceAddress.getZip())%>" />
															<liferay-ui:search-container-column-text name="country"
																property="commerceCountry.name" 
															/>
															<%-- <%
																CommerceRegion commerceRegion = commerceAddress.getCommerceRegion();
															%>
															<liferay-ui:search-container-column-text name="region"
																value="<%=(commerceRegion != null) ? commerceRegion.getName() : StringPool.BLANK%>"
															/> --%>
															<liferay-ui:search-container-column-jsp
																cssClass="entry-action-column"
																path="/address_action.jsp"
															/>
															<%} %>
														</liferay-ui:search-container-row>
														
														<liferay-ui:search-iterator displayStyle="list"
															markupView="lexicon"
														/>
													</liferay-ui:search-container>
												</div>
											</aui:form>
										</div>
									</liferay-ddm:template-renderer>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>
	</c:otherwise>
</c:choose>

