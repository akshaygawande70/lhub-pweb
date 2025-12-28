package web.ntuc.eshop.checkout.custom.select.product.portlet;

import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.commerce.service.CommerceOrderLocalServiceUtil;
import com.liferay.commerce.util.BaseCommerceCheckoutStep;
import com.liferay.commerce.util.CommerceCheckoutStep;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.ParamUtil;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = { "commerce.checkout.step.name=custom-select-product-step",
		"commerce.checkout.step.order:Integer=31"}, service = { CommerceCheckoutStep.class })
public class SelectProductStepPortlet extends BaseCommerceCheckoutStep {
	private static final Log log = LogFactoryUtil.getLog(SelectProductStepPortlet.class);

	public static final String NAME = "custom-select-product-step";
	
	private CommerceOrder _commerceOrder;

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference
	private CPInstanceHelper _cpInstanceHelper;

	@Reference(target = "(osgi.web.symbolicname=web.ntuc.eshop.checkout.custom.select.product)")
	private ServletContext _servletContext;

	public String getName() {
		return "custom-select-product-step";
	}

	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		try {
			String invoicePdf = ParamUtil.getString((PortletRequest) actionRequest, "invoicePdf");
			Long orderId = ParamUtil.getLong((PortletRequest)actionRequest, "orderId");
			ServiceContext serviceContext = ServiceContextFactory.getInstance(CommerceOrder.class.getName(),
					(PortletRequest) actionRequest);
			Map<String, Serializable> exapandoBridgeAttributes = new HashMap<>();
			Date todayPlusTwo = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(todayPlusTwo);
			cal.add(Calendar.DATE, 2);
			cal.set(Calendar.HOUR_OF_DAY, 9);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			todayPlusTwo = cal.getTime();
        	String preferedDate1 = "Preferred Date 1"; 
			String preferedDate2 = "Preferred Date 2";
			String preferedDate3 = "Preferred Date 3";
			exapandoBridgeAttributes.put(preferedDate1, todayPlusTwo);
			exapandoBridgeAttributes.put(preferedDate2, todayPlusTwo);
			exapandoBridgeAttributes.put(preferedDate3, todayPlusTwo);
			exapandoBridgeAttributes.put("INVOICE-Pdf", invoicePdf);
			serviceContext.setExpandoBridgeAttributes(exapandoBridgeAttributes);
			CommerceOrderLocalServiceUtil.updateCustomFields(orderId, serviceContext);
		} catch (Exception e) {
			log.error(e);
		}
		log.info("custom select product step action");
	}

	public void render(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws Exception {
//	ThemeDisplay themeDisplay = (ThemeDisplay) httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);
//	boolean isSignIn = themeDisplay.isSignedIn();
//	log.info("isSignIn = "+isSignIn);
//	if(isSignIn) {
		httpServletRequest.setAttribute("commerceOrder", (CommerceOrder) httpServletRequest.getAttribute("COMMERCE_ORDER"));
		this._jspRenderer.renderJSP(this._servletContext, httpServletRequest, httpServletResponse, "/view.jsp");
//	} else {
//		String paramUrl ="/registration?_com_liferay_login_web_portlet_LoginPortlet_redirect=https://www.fandi.com:8443/checkout";
//		String finalUrl = paramUrl;
//		log.info("final url = "+finalUrl);
//		httpServletResponse.sendRedirect(themeDisplay.getPortalURL()+"/home");
//	}

	}
}
