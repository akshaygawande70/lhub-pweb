package web.ntuc.eshop.discount.portlet;

import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.discount.constants.CommerceDiscountPortletKeys;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import java.io.IOException;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = { "mvc.command.name=/commerce_discount_content/apply_coupon_ajax", 
		"javax.portlet.name=" + CommerceDiscountPortletKeys.COMMERCE_DISCOUNT_CONTENT_WEB }, service = MVCResourceCommand.class)
public class ApplyCommerceDiscountResource implements MVCResourceCommand{

	private static final Log log = LogFactoryUtil.getLog(ApplyCommerceDiscountResource.class);
			
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		boolean status = false;
		
		ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		CommerceContext commerceContext = (CommerceContext) resourceRequest.getAttribute(CommerceWebKeys.COMMERCE_CONTEXT);
		
		JSONObject response = JSONFactoryUtil.createJSONObject();
		
		CommerceOrder commerceOrder = null;
		try {
			commerceOrder = commerceContext.getCommerceOrder();
		} catch (PortalException e) {
			e.printStackTrace();
		}
		
		String cmd = ParamUtil.getString(resourceRequest, "cmd");

		if (cmd.equals(Constants.ADD)) {
			String couponCode = ParamUtil.getString(resourceRequest, "couponCode");
			log.info(">>>>>>>> commerceOrder.getCouponCode() : " + commerceOrder.getCouponCode());
			try {			
				int temp = 0;
				float discountAmountFloat = 0;
				CommerceOrder commerceOrder2 = _commerceOrderService.applyCouponCode(commerceOrder.getCommerceOrderId(), couponCode,commerceContext);
			
				for (CommerceOrderItem commerceOrderItem : commerceOrder2.getCommerceOrderItems()) {
					discountAmountFloat = discountAmountFloat + commerceOrderItem.getDiscountAmount().floatValue() +
							commerceOrderItem.getDiscountPercentageLevel1().floatValue() + commerceOrderItem.getDiscountPercentageLevel2().floatValue() +
							commerceOrderItem.getDiscountPercentageLevel3().floatValue() + commerceOrderItem.getDiscountPercentageLevel4().floatValue();
				}
				
				log.info("discount 1 : " + discountAmountFloat);
				log.info("discount 2 : " + (commerceOrder2.getShippingDiscountAmount().floatValue() + commerceOrder2.getShippingDiscountPercentageLevel1().floatValue() +
						commerceOrder2.getShippingDiscountPercentageLevel2().floatValue() + commerceOrder2.getShippingDiscountPercentageLevel3().floatValue() + commerceOrder2.getShippingDiscountPercentageLevel4().floatValue() +
						commerceOrder2.getShippingDiscountWithTaxAmount().floatValue()
						));
				
				log.info("discount 3 : " + (commerceOrder2.getSubtotalDiscountAmount().floatValue() + commerceOrder2.getSubtotalDiscountPercentageLevel1().floatValue() + commerceOrder2.getSubtotalDiscountPercentageLevel2().floatValue() +
						commerceOrder2.getSubtotalDiscountPercentageLevel3().floatValue() + commerceOrder2.getSubtotalDiscountPercentageLevel4().floatValue()
						));
				log.info("discount 4 : " + (commerceOrder2.getTotalDiscountAmount().floatValue() + commerceOrder2.getTotalDiscountPercentageLevel1().floatValue() + commerceOrder2.getTotalDiscountPercentageLevel2().floatValue() +
						commerceOrder2.getTotalDiscountPercentageLevel3().floatValue() + commerceOrder2.getTotalDiscountPercentageLevel4().floatValue()
						));
				
				discountAmountFloat = discountAmountFloat + commerceOrder2.getShippingDiscountAmount().floatValue() + commerceOrder2.getShippingDiscountPercentageLevel1().floatValue() +
						commerceOrder2.getShippingDiscountPercentageLevel2().floatValue() + commerceOrder2.getShippingDiscountPercentageLevel3().floatValue() + commerceOrder2.getShippingDiscountPercentageLevel4().floatValue() +
						commerceOrder2.getSubtotalDiscountAmount().floatValue() + commerceOrder2.getSubtotalDiscountPercentageLevel1().floatValue() + commerceOrder2.getSubtotalDiscountPercentageLevel2().floatValue() +
						commerceOrder2.getSubtotalDiscountPercentageLevel3().floatValue() + commerceOrder2.getSubtotalDiscountPercentageLevel4().floatValue() +
						commerceOrder2.getTotalDiscountAmount().floatValue() + commerceOrder2.getTotalDiscountPercentageLevel1().floatValue() + commerceOrder2.getTotalDiscountPercentageLevel2().floatValue() +
						commerceOrder2.getTotalDiscountPercentageLevel3().floatValue() + commerceOrder2.getTotalDiscountPercentageLevel4().floatValue();
				if(discountAmountFloat > 0) {
					status = true;
					SessionMessages.add(resourceRequest, "discount-applied");
					response.put("status", status);
				}
				else{
					status = false;
					SessionErrors.add(resourceRequest, "product-not-assigned");
					response.put("status", status);
					_commerceOrderService.applyCouponCode(commerceOrder.getCommerceOrderId(), null, commerceContext);
				}
				
				try {
					resourceResponse.getWriter().println(response.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return status;
			}
			catch (PortalException exception) {
				SessionErrors.add(resourceRequest, "discount-failed");
				response.put("status", status);
				try {
					resourceResponse.getWriter().println(response.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return status;
			}
		}
		else if (cmd.equals(Constants.REMOVE)) {
			try {
				_commerceOrderService.applyCouponCode(commerceOrder.getCommerceOrderId(), null, commerceContext);
				SessionMessages.add(resourceRequest, "discount-removed");
				status = true;
				response.put("status", status);
				try {
					resourceResponse.getWriter().println(response.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return status;
			} catch (PortalException e) {
				e.printStackTrace();
			}
			response.put("status", status);
			try {
				resourceResponse.getWriter().println(response.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return status;
		}
		
		return false;
	}
	
	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private Portal _portal;
	
}


