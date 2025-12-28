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

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.constants.CommerceOrderPaymentConstants;
import com.liferay.commerce.constants.CommercePaymentConstants;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.payment.method.CommercePaymentMethod;
import com.liferay.commerce.payment.request.CommercePaymentRequest;
import com.liferay.commerce.payment.result.CommercePaymentResult;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.PaymentIntentData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import web.ntuc.eshop.stripe.payment.configuration.StripeGroupServiceConfiguration;
import web.ntuc.eshop.stripe.payment.constants.StripeCommercePaymentMethodConstants;


/**
 * @author qtle
 */
@Component(
		immediate = true, 
		property = "commerce.payment.engine.method.key="+ StripeCommercePaymentMethod.KEY, 
		service = CommercePaymentMethod.class)
public class StripeCommercePaymentMethod implements CommercePaymentMethod {
	public static final String KEY = "Stripe";

	@Override
	public String getDescription(Locale locale) {
		return _getResource(locale, "stripe-description");
	}

	private String _getResource(Locale locale, String key) {
		if (locale == null) {
			locale = LocaleUtil.getSiteDefault();
		}

		ResourceBundle resourceBundle = _getResourceBundle(locale);

		return LanguageUtil.get(resourceBundle, key);
	}

	private ResourceBundle _getResourceBundle(Locale locale) {
		return ResourceBundleUtil.getBundle("content.Language", locale, getClass());
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getName(Locale locale) {
		return _getResource(locale, KEY);
	}

	@Override
	public int getPaymentType() {
		return CommercePaymentConstants.COMMERCE_PAYMENT_METHOD_TYPE_ONLINE_REDIRECT;
	}

	@Override
	public String getServletPath() {
		return StripeCommercePaymentMethodConstants.COMPLETE_PAYMENT_SERVLET_PATH;
	}

	@Override
	public boolean isCancelEnabled() {
		return true;
	}

	@Override
	public boolean isCompleteEnabled() {
		return true;
	}

	@Override
	public boolean isProcessPaymentEnabled() {
		return true;
	}

	@Override
	public CommercePaymentResult processPayment(CommercePaymentRequest commercePaymentRequest) throws Exception {
		StripeCommercePaymentRequest stripeCommercePaymentRequest = (StripeCommercePaymentRequest) commercePaymentRequest;
		
		CommerceOrder commerceOrder = _commerceOrderLocalService
				.getCommerceOrder(stripeCommercePaymentRequest.getCommerceOrderId());
		
//		log.info("tx id == "+stripeCommercePaymentRequest.getTransactionId());
		StripeGroupServiceConfiguration stripConfig = _getStripeGroupServiceConfiguration(commerceOrder.getGroupId());
		
		if (log.isDebugEnabled()) {
			log.debug("apiKey=" + stripConfig.secretKey());
		}
		
		
		Stripe.apiKey = stripConfig.secretKey();		
		
//		PromotionCode promotionCode = PromotionCode.retrieve("test");
//		Coupon coupon = Coupon.retrieve("test");
//		log.info(coupon.getId());
//		log.info(coupon.getName());	
		
//		if(subtotalDiscountAmount != null) {
//			CouponCreateParams couponParams = CouponCreateParams.builder()
//					.setPercentOff(new BigDecimal(5))
//					.setId("test1")
//					.setDuration(CouponCreateParams.Duration.ONCE)
//					.build();
//			Coupon coupon = Coupon.create(couponParams);
//			
//			PromotionCodeCreateParams promoParams = PromotionCodeCreateParams.builder()
//					.setCode(commerceOrder.getCouponCode())
//					.setCoupon(coupon.getId())
//					.setActive(true)
//					.build();
//			promotionCode = PromotionCode.create(promoParams);

//		}
		
//		log.info("commercePaymentRequest.getReturnUrl() = "+commercePaymentRequest.getReturnUrl());
//		log.info("commercePaymentRequest.getCancelUrl() = "+commercePaymentRequest.getCancelUrl());
		PaymentIntentData paymentIntenData = PaymentIntentData.builder().
				putMetadata("uuid", commerceOrder.getUuid()).
				putMetadata("groupId", commerceOrder.getGroupId()+"").build();
		SessionCreateParams params =
				  SessionCreateParams.builder()
				    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				    .addAllLineItem(_getItem(commerceOrder, commercePaymentRequest.getLocale()))
				    .setSuccessUrl(commercePaymentRequest.getReturnUrl())
				    .setPaymentIntentData(paymentIntenData)
				    .setCancelUrl(commercePaymentRequest.getCancelUrl())
				    .build();
		Session session = Session.create(params);
		log.info("get Total Before : " + commerceOrder.getTotal());

		String url = StringBundler.concat(
				_getStartPaymentServletUrl(stripeCommercePaymentRequest));
		
		url = _http.addParameter(url, StripeCommercePaymentMethodConstants.STRIPE_SESSION_ID,
				session.getId());
		url = _http.addParameter(url, StripeCommercePaymentMethodConstants.PUBLIC_KEY, stripConfig.publicKey());

//		log.info("url = "+url);
		if (log.isDebugEnabled()) {
			log.debug("url=" + url);
		}
		
		int status = CommerceOrderConstants.PAYMENT_STATUS_AUTHORIZED;
	

		List<String> messages = Arrays.asList();
//		log.info(messages);
		CommercePaymentResult cpr =new CommercePaymentResult(session.getId(), commercePaymentRequest.getCommerceOrderId(), 
				status, true,
				url, null, messages,true );
//		log.info(cpr.getResultMessages());
//		log.info(cpr.getAuthTransactionId());
		return cpr;

	}

	private List<LineItem> _getItem(CommerceOrder commerceOrder, Locale locale)
			throws PortalException {

			String languageId = LanguageUtil.getLanguageId(locale);

			List<CommerceOrderItem> commerceOrderItems =
				commerceOrder.getCommerceOrderItems();

			List<LineItem> items = new ArrayList<>(commerceOrderItems.size());

			CommerceCurrency commerceCurrency = commerceOrder.getCommerceCurrency();

			BigDecimal finalPrice = commerceOrder.getTotal();
			
			/*	
			 * amountOriginal = price per Item!!!
			 * amout de Item Stripe = amountOriginal*100
			 */
			
			Long amountItem = Math.round(Math.round(finalPrice.multiply(new BigDecimal(100)).doubleValue() * 100.0) / 100.0);
			
			LineItem item = 
			SessionCreateParams.LineItem.builder()
				.setName("Payment for your Order number #"+commerceOrder.getCommerceOrderId())
				
				.setDescription("Order #"+commerceOrder.getCommerceOrderId())
				
				.setAmount(amountItem)

				.setCurrency(StringUtil.toUpperCase(commerceCurrency.getCode()))
				
				.setQuantity(1L)
				
				.build();
			items.add(item);
			if(log.isDebugEnabled()){
				log.debug("item: {name=" + item.getName()+",desc="+ item.getDescription()
					+ ",currency="+ item.getCurrency()
					+",quantity=" + item.getQuantity()
					+",amount="+ item.getAmount()+"}");
			}
			

			//items = _getShipping(commerceOrder, locale, items);

			return items;
		}
	
	private List<LineItem> _getItems(CommerceOrder commerceOrder, Locale locale)
			throws PortalException {

			String languageId = LanguageUtil.getLanguageId(locale);

			List<CommerceOrderItem> commerceOrderItems =
				commerceOrder.getCommerceOrderItems();

			List<LineItem> items = new ArrayList<>(commerceOrderItems.size());

			CommerceCurrency commerceCurrency = commerceOrder.getCommerceCurrency();

			for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
				String desc = commerceOrderItem.getCPDefinition().getDescription(languageId);
				if( Objects.equals("", desc))
					desc = _getResource(locale, "stripe-empty-description");
				
				BigDecimal finalPrice = commerceOrderItem.getFinalPrice();
//				log.info(commerceOrderItem);
				
				/*	
				 * amountOriginal = price per Item!!!
				 * amout de Item Stripe = amountOriginal*100
				 */
				
				Long amountItem = finalPrice.divide(new BigDecimal(commerceOrderItem.getQuantity())).multiply(new BigDecimal(100)).longValue();
				
				LineItem item = 
				SessionCreateParams.LineItem.builder()
					.setName(commerceOrderItem.getName(languageId))

					.setDescription(desc)
					
					.setAmount(amountItem)

					.setCurrency(StringUtil.toUpperCase(commerceCurrency.getCode()))
					
					.setQuantity( Long.valueOf((commerceOrderItem.getQuantity())))	
					
					.build();
				if(log.isDebugEnabled()){
					log.debug("item: {name=" + item.getName()+",desc="+ item.getDescription()
						+ ",currency="+ item.getCurrency()
						+",quantity=" + item.getQuantity()
						+",amount="+ item.getAmount()+"}");
				}
				
				items.add(item);
			}

//			items = _getOrderDiscounts(commerceOrder, locale, items);

			items = _getShipping(commerceOrder, locale, items);

//			items = _getTaxes(commerceOrder, locale, items);

			return items;
		}

	private List<LineItem> _getOrderDiscounts(
			CommerceOrder commerceOrder, Locale locale, List<LineItem> items)
		throws PortalException {

		BigDecimal subtotalDiscountAmount =
			commerceOrder.getSubtotalDiscountAmount();

		CommerceCurrency commerceCurrency = commerceOrder.getCommerceCurrency();

		if ((subtotalDiscountAmount != null) &&
			(subtotalDiscountAmount.compareTo(BigDecimal.ZERO) > 0)) {

			_addItem(
				commerceCurrency,
				_getResource(locale, "stripe-subtotal-discount-description"),
				true, items, _getResource(locale, "stripe-subtotal-discount"),
				subtotalDiscountAmount);
		}

		BigDecimal totalDiscountAmount = commerceOrder.getTotalDiscountAmount();

		if ((totalDiscountAmount != null) &&
			(totalDiscountAmount.compareTo(BigDecimal.ZERO) > 0)) {

			_addItem(
				commerceCurrency,
				_getResource(locale, "stripe-total-discount-description"), true,
				items, _getResource(locale, "stripe-total-discount"),
				totalDiscountAmount);
		}

		return items;
	}

		
	private List<LineItem> _getShipping(
			CommerceOrder commerceOrder, Locale locale, List<LineItem> items)
		throws PortalException {

		BigDecimal shippingAmount = commerceOrder.getShippingAmount();

		CommerceCurrency commerceCurrency = commerceOrder.getCommerceCurrency();

		if ((shippingAmount != null) &&
			(shippingAmount.compareTo(BigDecimal.ZERO) > 0)) {

			_addItem(
				commerceCurrency,
				_getResource(locale, "stripe-shipping-description"), false,
				items, _getResource(locale, "stripe-shipping"), shippingAmount);

			BigDecimal shippingDiscountAmount =
				commerceOrder.getShippingDiscountAmount();

			if ((shippingDiscountAmount != null) &&
				(shippingDiscountAmount.compareTo(BigDecimal.ZERO) > 0)) {

				_addItem(
					commerceCurrency,
					_getResource(
						locale, "stripe-shipping-discount-description"),
					true, items,
					_getResource(locale, "stripe-shipping-discount"),
					shippingDiscountAmount);
			}
		}

		return items;
	}
	
	private void _addItem(
			CommerceCurrency commerceCurrency, String description, boolean discount,
			List<LineItem> items, String name, BigDecimal amount) {

		//amout de Item Stripe = amountOriginal*100
		BigDecimal price = amount.multiply(new BigDecimal(100));

		if (discount) {
			price = amount.multiply(new BigDecimal(-1));
		}
		
//		if(discount) {
//			Map<String, Object> params = new HashMap<>();
//			params.put("percent_off", 5);
//			Coupon coupon = null;
//			try {
//				Coupon.create(params);
//			} catch (StripeException e) {
//				log.error("error adding coupon : "+e.getMessage());
//			}
//		}
		
		LineItem item = 
			SessionCreateParams.LineItem.builder()
				.setName(name)

				.setDescription(description)
				
				.setAmount(price.longValue())

				.setCurrency(StringUtil.toUpperCase(commerceCurrency.getCode()))

				.setQuantity(1L)	

				.build();
			
		if (log.isDebugEnabled()) {
			log.debug("_addItem: {name=" + item.getName()+",desc="+ item.getDescription()
				+ ",currency="+ item.getCurrency()
				+",quantity=" + item.getQuantity()
				+",amount="+ item.getAmount()+"}");
		}
		
		
		items.add(item);
	}
	
	private List<LineItem> _getTaxes(
			CommerceOrder commerceOrder, Locale locale, List<LineItem> items)
		throws PortalException {

		BigDecimal taxAmount = commerceOrder.getTaxAmount();

		if ((taxAmount != null) && (taxAmount.compareTo(BigDecimal.ZERO) > 0)) {
			_addItem(
				commerceOrder.getCommerceCurrency(),
				_getResource(locale, "stripe-taxes-description"), false, items,
				_getResource(locale, "stripe-taxes"), taxAmount);
		}

		return items;
	}
	
	private String _getStartPaymentServletUrl(
			StripeCommercePaymentRequest StripeCommercePaymentRequest) {
			return StringBundler.concat(
				_portal.getPortalURL(
					StripeCommercePaymentRequest.getHttpServletRequest()),
				_portal.getPathModule(), StringPool.SLASH,
				StripeCommercePaymentMethodConstants.START_PAYMENT_SERVLET_PATH);
		}


	@Override
	public CommercePaymentResult cancelPayment(CommercePaymentRequest commercePaymentRequest) throws Exception {
		StripeCommercePaymentRequest stripeCommercePaymentRequest = (StripeCommercePaymentRequest) commercePaymentRequest;
		log.info("stripeCommercePaymentRequest.getTransactionId() in cancel"+stripeCommercePaymentRequest.getTransactionId());
		return new CommercePaymentResult(stripeCommercePaymentRequest.getTransactionId(), commercePaymentRequest.getCommerceOrderId(),
				CommerceOrderPaymentConstants.STATUS_CANCELLED, false, null, null, Collections.emptyList(), true);
	}

	@Override
	public CommercePaymentResult completePayment(CommercePaymentRequest commercePaymentRequest) throws Exception {
//		log.info("==============completePayment method============");
		boolean success = true;
		
		StripeCommercePaymentRequest stripeCommercePaymentRequest = (StripeCommercePaymentRequest) commercePaymentRequest;
//		log.info("txId = "+stripeCommercePaymentRequest.getTransactionId()); 
		
		List<String> messages = new ArrayList<>();
		Charge charge = this._getStripePaymentCharge(stripeCommercePaymentRequest);
		messages.add(charge.getId());
		messages.add(charge.getReceiptUrl());
//		log.info(body);
		return new CommercePaymentResult(
				stripeCommercePaymentRequest.getTransactionId(),
				stripeCommercePaymentRequest.getCommerceOrderId(),
				CommerceOrderPaymentConstants.STATUS_COMPLETED, true, null, null,
				messages, success);
		
	}
	
	public Charge _getStripePaymentCharge(StripeCommercePaymentRequest stripeCommercePaymentRequest) throws Exception {
//		CommerceOrder commerceOrder = CommerceOrderLocalServiceUtil.getCommerceOrder(stripeCommercePaymentRequest.getCommerceOrderId());
//		StripeGroupServiceConfiguration stripConfig = _getStripeGroupServiceConfiguration(commerceOrder.getGroupId());
//		RequestOptions requestOptions = RequestOptions.builder().setApiKey(stripConfig.secretKey()).build();
		
		Session session = Session.retrieve(stripeCommercePaymentRequest.getTransactionId());
		PaymentIntent pi = PaymentIntent.retrieve(session.getPaymentIntent());
		return pi.getCharges().getData().get(0);
	}
	
	
	private StripeGroupServiceConfiguration _getStripeGroupServiceConfiguration(long groupId) throws PortalException {

		return _configurationProvider.getConfiguration(StripeGroupServiceConfiguration.class,
				new GroupServiceSettingsLocator(groupId, StripeCommercePaymentMethodConstants.SERVICE_NAME));
	}

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private Http _http;
	
	@Reference
	private Portal _portal;
	
	private static final Log log = LogFactoryUtil.getLog(StripeCommercePaymentMethod.class); 

}