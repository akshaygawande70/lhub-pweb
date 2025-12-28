package com.acme.q4f7.web.internal.commerce.product.content.renderer;

import com.liferay.commerce.inventory.engine.CommerceInventoryEngine;
import com.liferay.commerce.product.catalog.CPCatalogEntry;
import com.liferay.commerce.product.catalog.CPSku;
import com.liferay.commerce.product.content.constants.CPContentWebKeys;
import com.liferay.commerce.product.content.render.CPContentRenderer;
import com.liferay.commerce.product.content.util.CPContentHelper;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.service.CPDefinitionLocalServiceUtil;
import com.liferay.commerce.product.type.simple.constants.SimpleCPTypeConstants;
import com.liferay.commerce.product.type.virtual.constants.VirtualCPTypeConstants;
import com.liferay.commerce.product.type.virtual.util.VirtualCPTypeHelper;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	property = {
		"commerce.product.content.renderer.key=q4f7",
		"commerce.product.content.renderer.order=" + Integer.MIN_VALUE,
		"commerce.product.content.renderer.type=" + SimpleCPTypeConstants.NAME,
		"commerce.product.content.renderer.type=" + VirtualCPTypeConstants.NAME
	},
	service = CPContentRenderer.class
)
public class Q4F7CPContentRenderer implements CPContentRenderer {

	@Override
	public String getKey() {
		return "q4f7";
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return LanguageUtil.get(
			resourceBundle, "q4f7-commerce-product-content-renderer");
	}

	@Override
	public void render(
			CPCatalogEntry cpCatalogEntry,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {
		
		httpServletRequest.setAttribute("virtualCPTypeHelper", _virtualCPTypeHelper);
		CPContentHelper cpContentHelper = (CPContentHelper)httpServletRequest.getAttribute(CPContentWebKeys.CP_CONTENT_HELPER);
		CPDefinition cpDefinition = CPDefinitionLocalServiceUtil.fetchCPDefinition(cpCatalogEntry.getCPDefinitionId());
				
		CPSku cpSku = cpContentHelper.getDefaultCPSku(cpContentHelper.getCPCatalogEntry(httpServletRequest));
		int availableQuantity = _commerceInventoryEngine.getStockQuantity(
				cpDefinition.getCompanyId(), cpSku.getSku());
		httpServletRequest.setAttribute("availableQuantity", availableQuantity);
		if(cpCatalogEntry.getProductTypeName().equalsIgnoreCase(SimpleCPTypeConstants.NAME)) {
			_jspRenderer.renderJSP(
					_servletContext, httpServletRequest, httpServletResponse,
					"/view.jsp");
		}else if (cpCatalogEntry.getProductTypeName().equalsIgnoreCase(VirtualCPTypeConstants.NAME)) {
			_jspRenderer.renderJSP(
					_servletContext, httpServletRequest, httpServletResponse,
					"/virtual_view.jsp");
		}
	}

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference(target = "(osgi.web.symbolicname=com.acme.q4f7.web)")
	private ServletContext _servletContext;
	
	@Reference
	private VirtualCPTypeHelper _virtualCPTypeHelper;
	
	@Reference
	private CommerceInventoryEngine _commerceInventoryEngine;
}