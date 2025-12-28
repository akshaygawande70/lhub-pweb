/*******************************************************************************
 * Copyright (C) 2020 qtle
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package web.ntuc.eshop.stripe.payment.portlet;

import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.payment.request.CommercePaymentRequest;
import com.liferay.commerce.payment.request.CommercePaymentRequestProvider;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import web.ntuc.eshop.stripe.payment.constants.StripeCommercePaymentMethodConstants;

/**
 * @author qtle
 */
@Component(
	immediate = true,
	property = "commerce.payment.engine.method.key=" + StripeCommercePaymentMethod.KEY,
	service = CommercePaymentRequestProvider.class
)
public class StripeCommercePaymentRequestProvider
	implements CommercePaymentRequestProvider {
	
	private static final Log log = LogFactoryUtil.getLog(StripeCommercePaymentRequestProvider.class);

	@Override
	public CommercePaymentRequest getCommercePaymentRequest(
			String cancelUrl, long commerceOrderId,
			HttpServletRequest httpServletRequest, Locale locale,
			String returnUrl, String transactionId)
		throws PortalException {
		log.info("StripeCommercePaymentRequestProvider txId = "+transactionId);
		CommerceOrder commerceOrder =
			_commerceOrderLocalService.getCommerceOrder(commerceOrderId);

		String sessionId = null;

		if (httpServletRequest != null) {
			sessionId = ParamUtil.getString(httpServletRequest, StripeCommercePaymentMethodConstants.STRIPE_SESSION_ID);
		}
		
//		log.info("get Total AFTER  : " + commerceOrder.getTotal());
//		log.info(">>> returnUrl : " + returnUrl);
//		log.info(">>> commerceOrder.getTotal() : " + commerceOrder.getTotal());
//		log.info(">>> cancelUrl : " + cancelUrl);
//		log.info(">>> commerceOrderId : " + commerceOrderId);
//		log.info(">>> locale : " + locale);
//		log.info(">>> sessionId : " + sessionId);
//		log.info(">>> returnUrl : " + returnUrl);
//		log.info(">>> transactionId : " + transactionId);
//		Enumeration e = httpServletRequest.getAttributeNames();
//		while (e.hasMoreElements()) //hasNext() is used to loop. It is a method of Iterator()
//        {
//			try {
//				String key = e.nextElement().toString();
//				log.info("XXXXXXXX key : " + key + " XXXXXXXX value : " + httpServletRequest.getAttribute(key));
//			} catch (Exception e2) {
//			}	
//			
//        }
//		
//		log.info(">>> locale : " + locale);
		
		return new StripeCommercePaymentRequest(
			commerceOrder.getTotal(), cancelUrl, commerceOrderId, locale,
			sessionId,  httpServletRequest,returnUrl, transactionId);
	}

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

}
