<%@page import="com.liferay.commerce.currency.model.CommerceMoney"%>
<%@page import="com.liferay.commerce.tax.engine.fixed.service.CommerceTaxFixedRateLocalServiceUtil"%>
<%@page import="com.liferay.friendly.url.model.FriendlyURLEntry"%>
<%@page import="com.liferay.friendly.url.service.FriendlyURLEntryLocalServiceUtil"%>
<%@page import="com.liferay.friendly.url.service.FriendlyURLEntryLocalService"%>
<%@page import="com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil"%>
<%@page import="com.liferay.asset.entry.rel.model.AssetEntryAssetCategoryRel"%>
<%@page import="com.liferay.asset.entry.rel.service.AssetEntryAssetCategoryRelLocalServiceUtil"%>
<%@page import="com.liferay.asset.entry.rel.service.AssetEntryAssetCategoryRelLocalService"%>
<%@page import="com.liferay.asset.kernel.model.AssetEntry"%>
<%@page import="com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil"%>
<%@page import="com.liferay.commerce.product.service.CPDefinitionLocalServiceUtil"%>
<%@page import="com.liferay.commerce.product.model.CPDefinition"%>
<%@page import="com.liferay.commerce.product.service.CProductLocalServiceUtil"%>
<%@page import="com.liferay.commerce.product.model.CProduct"%>
<%@ include file="init.jsp"%>

<!-- Virtual Product -->

<%
	CPContentHelper cpContentHelper = (CPContentHelper)request.getAttribute(CPContentWebKeys.CP_CONTENT_HELPER);
	
	CPCatalogEntry cpCatalogEntry = cpContentHelper.getCPCatalogEntry(request);
	
	CPSku cpSku = cpContentHelper.getDefaultCPSku(cpContentHelper.getCPCatalogEntry(request));
	
	long cpDefinitionId = cpCatalogEntry.getCPDefinitionId();
	
	long cProductId = cpCatalogEntry.getCProductId();
	
	String img = cpCatalogEntry.getDefaultImageFileUrl();
	
	CProduct cProduct = CProductLocalServiceUtil.fetchCProduct(cProductId);
	
	String ext = cProduct.getExternalReferenceCode();
	
	CPDefinition cpDefinition = CPDefinitionLocalServiceUtil.fetchCPDefinition(cpDefinitionId);
	
	long publishedCPDefinitionId = cProduct.getPublishedCPDefinitionId();
	//System.out.println(publishedCPDefinitionId + " ini publishedCPDefinitionId");
	
	String addToCartId = PortalUtil.generateRandomKey(request, "add-to-cart");
	//System.out.println(addToCartId + " ini addToCartId");
	
	//System.out.println(cpSku.getCPInstanceId() + " ini CPInstanceId");
	
	AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry("com.liferay.commerce.product.model.CPDefinition", publishedCPDefinitionId);
	//System.out.println(assetEntry + " ini assetEntry");
	long entryId = assetEntry.getEntryId();
	//System.out.println(entryId + " ini entryId");
	List<AssetEntryAssetCategoryRel> aeacRelLocalService = AssetEntryAssetCategoryRelLocalServiceUtil.getAssetEntryAssetCategoryRelsByAssetEntryId(entryId);
	String categoryName = "";
	String friendlyURL = "";
	try {
		long assetCategoryId = aeacRelLocalService.get(0).getAssetCategoryId();
		//System.out.println(assetCategoryId + " ini assetCategoryId");
		AssetEntryAssetCategoryRel assetEntryAssetEntryCategoryRel = AssetEntryAssetCategoryRelLocalServiceUtil.fetchAssetEntryAssetCategoryRel(entryId, assetCategoryId);
		//System.out.println(assetEntryAssetEntryCategoryRel + " ini assetEntryAssetEntryCategoryRel");
		long categoryId = assetEntryAssetEntryCategoryRel.getAssetCategoryId();
		//System.out.println(categoryId + " ini categoryId");
		AssetCategory assetCategory = AssetCategoryLocalServiceUtil.fetchAssetCategory(categoryId);
		//System.out.println(assetCategory + " ini assetCategory");
		categoryName = assetCategory.getName();
		//System.out.println(categoryName + " ini categoryName");
		
		long classNameId = PortalUtil.getClassNameId(AssetCategory.class);
		//System.out.println(classNameId + " ini classNameId");
		long groupId = assetCategory.getGroupId();
		//System.out.println(groupId + " ini groupId");
		FriendlyURLEntryLocalService fueLocalService = FriendlyURLEntryLocalServiceUtil.getService();
		List<FriendlyURLEntry> fueList = fueLocalService.getFriendlyURLEntries(groupId, classNameId, categoryId);
		//System.out.println(classNameId + " ini classNameId");
		//System.out.println(fueList + " ini fueList");
		FriendlyURLEntry friendlyURLEntry = fueList.get(0);
		friendlyURL = friendlyURLEntry.getUrlTitle();
		//System.out.println(friendlyURL + " ini friendlyURL");
    } catch (Exception e) {
        // TODO: handle exception
    }
%>

<style>
.detail-produk-virtual .box-stick-price .btn-buy .btn-add-to-cart.btn.btn-primary::after {
    content:'Buy Now';
    visibility: visible;
    display: block;
    position: absolute;
    color: #FFCB05;
    background: #18355E;
    transform: inherit;
    opacity: 1;
    z-index: 2;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 2px solid #FFCB05 !important;
}
</style>

<div class="section-detail-produk detail-produk-virtual">
	<div class="container">
        <div class="row">
            <div class="col-12">
                <div class="box-breadcrumb-custom">
                    <ul class="breadcrumb-detail">
                        <li><a href="/e-shop">E-shop</a></li>
                        <li><a href="/g/<%= friendlyURL %>"><%= categoryName %></a></li>
                        <li><%= HtmlUtil.escape(cpCatalogEntry.getName()) %></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div class="row">
            <div class="col-12 col-md-8 col-lg-9">
                <div class="row">
                    <div class="col-md-3">
                        <div class="image"><img alt="" src="<%= img %>" /></div>
                    </div>

                   <div class="col-md-9">
                        <div class="title"><%= HtmlUtil.escape(cpCatalogEntry.getName()) %></div>
               			<div class="category"><%= categoryName %></div>
                    </div>
                   

                    <div class="col-md-12">
                        <div class="title-desc">Description</div>

                        <div class="desc"><%= cpCatalogEntry.getDescription() %></div>
                    </div>
                </div>
            </div>

            <div class="col-12 col-md-4 col-lg-3">
                <div class="box-stick-price">
                    <div class="box-header">
                        <div class="title">Price</div>

                        <div class="box-price">
                            <%	
	                            /* ini cara susah*/
	                        	/* CommerceTaxFixedRateLocalService commerceTaxFixedRateLocalService = CommerceTaxFixedRateLocalServiceUtil.getService();
	                        	//System.out.println(commerceTaxFixedRateLocalService + " ini commerceTaxFixedRateLocalService");
	                        	List<CommerceTaxFixedRate> commerceTaxFixedRate = commerceTaxFixedRateLocalService.getCommerceTaxFixedRates(-1, -1);                          
	                        	//System.out.println(commerceTaxFixedRate + " ini commerceTaxFixedRate");
	                        	//convert dari list ke row
	                            CommerceTaxFixedRate getIndexCommerceTaxFixedRate = commerceTaxFixedRate.get(0);
	                        	//System.out.println(getIndexCommerceTaxFixedRate + " ini getIndexCommerceTaxFixedRate"); 
	                        	long commerceTaxFixedRateId = getIndexCommerceTaxFixedRate.getCommerceTaxFixedRateId();
	                        	CommerceTaxFixedRate taxRate = CommerceTaxFixedRateLocalServiceUtil.fetchCommerceTaxFixedRate(commerceTaxFixedRateId);
	                            //System.out.println(taxRate + " ini taxRate");
	                            double taxPercentage = taxRate.getRate();
	                            //System.out.println(taxPercentage + " ini taxPercentage");
	                        	double finalPrice = cpSku.getPrice().doubleValue();
	                        	//System.out.println(finalPrice + " ini finalPrice"); 
	                        	double finalPriceWithTax = ((finalPrice*taxPercentage)/100) + finalPrice;
	                        	//System.out.println(finalPriceWithTax + " ini finalPriceWithTax"); */ 
	                        	
	                        	/* ini cara gampang*/
	                        	double taxPercentage = 
	                    		CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).get(0).getRate();
	                        	//System.out.println(taxPercentage + " ini taxPercentage");
	                        	double finalPrice = cpSku.getPromoPrice()!=null && cpSku.getPromoPrice().doubleValue()>0 ? cpSku.getPromoPrice().doubleValue() : cpSku.getPrice().doubleValue() ;
	                        	//System.out.println(finalPrice + " ini finalPrice"); 
	                        	double finalPriceWithTax = ((finalPrice*taxPercentage)/100) + finalPrice;
	                        	String finalPriceWithTaxDecimal = String.format("%,.2f", finalPriceWithTax);
	                        	double normalPriceWithTax = ((cpSku.getPrice().doubleValue()*taxPercentage)/100) + cpSku.getPrice().doubleValue();
	                        	//System.out.println(finalPriceWithTax + " ini finalPriceWithTax");
	                        	//System.out.println(finalPriceWithTaxDecimal + " ini finalPriceWithTaxDecimal");
                           	%>
                           	<c:if test="<%=(cpSku.getPromoPrice()!=null && cpSku.getPromoPrice().doubleValue()>0)%>">
                           		<span class="price-normal">$<%=String.format("%,.2f",normalPriceWithTax) %></span>
                           	</c:if>
                			<div class="price">$<%= finalPriceWithTaxDecimal %></div>
                			<%
							if(taxPercentage == (long) taxPercentage) {%>
								<div class="tax">including <%=String.format("%d",(long) taxPercentage)%>% GST</div>
							<% } else { %>
								<div class="tax">including <%=String.format("%s", taxPercentage)%>% GST</div>
							<% } %>
                        </div>
                    </div>
	            	<c:if test="${availableQuantity>0 }">
		            	<div class="btn-buy simple" onClick="popUp()">
		                    <commerce-ui:add-to-cart
								CPInstanceId="<%= (cpSku == null) ? 0 : cpSku.getCPInstanceId() %>"
								id="<%= addToCartId %>"
							/>
	                   	</div>
                   	</c:if>
                   	<c:if test="${availableQuantity<=0 }">
                   		<div class="btn-buy simple" onClick="popUp()">
                   			<div id="swpe_column1_0">
  								<center><b><h1 style="color: red;">SOLD OUT</h1></b></center>
  							</div>
  						</div>
                   	</c:if>
                    <div class="alert">Please contact us through our enquiry form if you are unable to purchase this
                        product.</div>
                </div>
            </div>
            
        </div>
    </div>
</div>

<div class="alert-notifications alert-notifications-fixed">
	<div class="alert alert-dismissible alert-info alert-add-chart hide" role="alert">
		<span class="alert-indicator"> 
			<svg
						class="lexicon-icon lexicon-icon-check-circle-full"
						focusable="false"
						role="presentation"
					>
					<use href="/o/ntuclearninghub-commerce-theme/images/clay/icons.svg#info-circle"></use>
						<!-- <use
							xlink:href="/o/themes/images/icons/icons.svg#check-circle-full"
							
						></use> -->
					</svg>
		<!-- <i class="fas fa-exclamation"></i> -->
		</span> The product was successfully added to the cart.
		<button aria-label="Close" class="close" data-dismiss="alert" type="button">
			<svg
			class="lexicon-icon lexicon-icon-times"
			focusable="false"
			role="presentation"
		>
			<use xlink:href="/images/icons/icons.svg#times" />
		</svg>
		</button>
	</div>
</div>

<!-- pembatas -->
<script>
<c:if test="<%=themeDisplay.getDefaultUser().getUserId() == themeDisplay.getUser().getUserId()%>">
	$(document).on('click','.btn-add-to-cart',function(){
		window.location.href = "/registration?_com_liferay_login_web_portlet_LoginPortlet_redirect="+encodeURIComponent(window.location.href);
	});
</c:if>

function popUp(){
	setTimeout(function(){
		window.location.reload();
	},1000);
}
</script>