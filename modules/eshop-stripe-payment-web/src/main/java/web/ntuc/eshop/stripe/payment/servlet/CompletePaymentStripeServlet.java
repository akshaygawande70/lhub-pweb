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
package web.ntuc.eshop.stripe.payment.servlet;

import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.model.CommerceOrderPayment;
import com.liferay.commerce.payment.engine.CommercePaymentEngine;
import com.liferay.commerce.payment.result.CommercePaymentResult;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.service.CommerceOrderPaymentLocalServiceUtil;
import com.liferay.commerce.tax.engine.fixed.service.CommerceTaxFixedRateLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.security.auth.session.AuthenticatedSessionManager;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.PortalSessionThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PrefsPropsUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.CurrencyUtil;
import api.ntuc.common.util.EmailUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.stripe.payment.constants.StripeCommercePaymentMethodConstants;
import web.ntuc.eshop.stripe.payment.util.StripeFormatCurrencyUtil;

@Component(immediate = true, property = {
		"osgi.http.whiteboard.context.path=/" + StripeCommercePaymentMethodConstants.COMPLETE_PAYMENT_SERVLET_PATH,
		"osgi.http.whiteboard.servlet.name=web.ntuc.eshop.stripe.payment.servlet.CompletePaymentStripeServlet",
		"osgi.http.whiteboard.servlet.pattern=/" + StripeCommercePaymentMethodConstants.COMPLETE_PAYMENT_SERVLET_PATH
				+ "/*" }, service = Servlet.class)
public class CompletePaymentStripeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String USER = "USER";
	private static final String USER_ID = "USER_ID";
	private static final String CURRENT_COMPLETE_URL = "CURRENT_COMPLETE_URL";
	private static final String INSECURE_RESPONSE = "com.liferay.portal.kernel.servlet.filters.invoker.InvokerFilterSECURE_RESPONSE";
	private static final Log log = LogFactoryUtil.getLog(CompletePaymentStripeServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		try {
			long groupId = ParamUtil.getLong(httpServletRequest, "groupId");
			String uuid = ParamUtil.getString(httpServletRequest, "uuid");
//			
			CommerceOrder commerceOrder =
					_commerceOrderLocalService.getCommerceOrderByUuidAndGroupId(
					uuid, groupId);
			User user = UserLocalServiceUtil.fetchUser(commerceOrder.getUserId());
			
			if(Validator.isNull(httpServletRequest.getAttribute(USER))) {
				httpServletRequest.setAttribute(USER, user);
			}
			
			if(Validator.isNull(httpServletRequest.getAttribute(USER_ID))) {
				httpServletRequest.setAttribute(USER_ID, user.getUserId());
			}
//			
			if(Validator.isNotNull(httpServletRequest.getAttribute(CURRENT_COMPLETE_URL))) {
				String currentUrl = httpServletRequest.getAttribute(CURRENT_COMPLETE_URL).toString();
				String replacedCurrentUrl = currentUrl.replace("http://", "https://");
				httpServletRequest.setAttribute(CURRENT_COMPLETE_URL, replacedCurrentUrl);
			}
//			
			if(Validator.isNotNull(httpServletRequest.getAttribute(INSECURE_RESPONSE))) {
				httpServletRequest.setAttribute(INSECURE_RESPONSE, Boolean.TRUE);
			}
			
			HttpSession httpSession = httpServletRequest.getSession();
			
			if (PortalSessionThreadLocal.getHttpSession() == null) {
				_log.info("session null");
				PortalSessionThreadLocal.setHttpSession(httpSession);
			}

			PermissionChecker permissionChecker =
					PermissionCheckerFactoryUtil.create(_portal.getUser(httpServletRequest));

			PermissionThreadLocal.setPermissionChecker(permissionChecker);
//			
			boolean cancel = ParamUtil.getBoolean(httpServletRequest, "cancel");
//
			String redirect = ParamUtil.getString(
				httpServletRequest, "redirect");
			if (cancel) {
				_commercePaymentEngine.cancelPayment(
					commerceOrder.getCommerceOrderId(), commerceOrder.getTransactionId(),
					httpServletRequest);
			}
			else {
//				log.info(commerceOrder.getTransactionId());
				CommerceOrderPayment cop = CommerceOrderPaymentLocalServiceUtil.fetchLatestCommerceOrderPayment(commerceOrder.getCommerceOrderId());
			
				String contentCop = cop.getContent().replace("[", "").replace("]", "");
				if((Validator.isNull(contentCop) || contentCop.isEmpty()) && cop.getStatus()!=0) {
					CommercePaymentResult cpr = _commercePaymentEngine.completePayment(
							commerceOrder.getCommerceOrderId(), commerceOrder.getTransactionId(),
							httpServletRequest);
				}
//				log.info(cpr.getNewPaymentStatus());
//				for(String s : cpr.getResultMessages()) {
//					log.info(s);
//				}
//				Locale locale = LocaleUtil.getSiteDefault();
//
//				ParameterGroup parameterGroup = ParameterGroupLocalServiceUtil
//						.getByCode("eshopCheckout", false);
//				long siteGroupId = parameterGroup.getGroupId();
//				Parameter parameterImg = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
//						parameterGroup.getParameterGroupId(), "eshopFooterImage", false);
//				String imgSrc = parameterImg.getParamValue();
//				Parameter parameterCcMail = ParameterLocalServiceUtil.getByGroupCode(siteGroupId, parameterGroup.getParameterGroupId(), "eshopCcMail", false);
//				String ccMail = parameterCcMail.getParamValue();
//				CommerceOrderItem commerceOrderItem = commerceOrder.getCommerceOrderItems().get(0);
//				String address = commerceOrder.getBillingAddress().getStreet1() + commerceOrder.getBillingAddress().getStreet2() + commerceOrder.getBillingAddress().getStreet3();
//				String orderLink = (String) commerceOrder.getExpandoBridge().getAttribute("Order Link");
//				
//				List<CommerceOrderItem> commerceOrderItemList = commerceOrder.getCommerceOrderItems();
//				
//				String contentTable = 
//						"        <tr>\r\n" + 
//						"            <th style=\"text-align:left;\"><strong>Item</strong></th>\r\n" +
//						"            <th><strong>SKU</strong></th>\r\n" + 
//						"            <th><strong>Qty</strong></th>\r\n" + 
//						"            <th><strong>Per Unit</strong></th>\r\n" +
//						"            <th><strong>Discount</strong></th>\r\n" +
//						"            <th><strong>Net Price</strong></th>\r\n" +
//						"            <th><strong>GST</strong></th>\r\n" +
//						"            <th><strong>Total</strong></th>\r\n" + 
//						"        </tr>\r\n";
//				String contentItem="";
//				double subtotalUnitPrice = 0;
//	 			double totalUnitPrice = 0;
//	 			double discountFromCommerceOrder = commerceOrder.getShippingDiscountAmount().doubleValue() + 
//	 					commerceOrder.getSubtotalDiscountAmount().doubleValue()+
//	 					commerceOrder.getTotalDiscountAmount().doubleValue(); 
//	 			double commerceTaxFixedRate = CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).size()>0?
//	 					CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).get(0).getRate():0;
//	 					
//				for(CommerceOrderItem coi : commerceOrderItemList) {
//					float getPriceQty = coi.getPromoPrice()!=null && coi.getPromoPrice().floatValue()>0 ? 
//							coi.getPromoPrice().floatValue() : coi.getUnitPrice().floatValue() ;
//			 		getPriceQty = getPriceQty * coi.getQuantity();
//			 		double discountAmountFloat = coi.getDiscountAmount().floatValue();
//			 		discountFromCommerceOrder = discountFromCommerceOrder + discountAmountFloat;
//			 		double taxAmountPerProduct = getPriceQty * (commerceTaxFixedRate) * 0.01;
//			 		double totalEachProduct = taxAmountPerProduct + getPriceQty - discountAmountFloat;
//			 		totalUnitPrice = totalUnitPrice + getPriceQty;
//			 		subtotalUnitPrice = subtotalUnitPrice + coi.getFinalPrice().doubleValue();
//					contentItem = contentItem + "        <tr>\r\n" + 
//							"            <td style=\"text-align:left;\">"+coi.getName(locale)+"</td>\r\n" +
//							"            <td style=\"text-align:center;min-width:100px;\">"+coi.getSku()+"</td>\r\n" +
//							"            <td style=\"text-align:center;\">"+coi.getQuantity()+"</td>\r\n" + 
//							"            <td style=\"text-align:right; min-width:100px;\">$ "+StripeFormatCurrencyUtil.formatCurrency(coi.getPromoPrice()!=null && coi.getPromoPrice().floatValue()>0 ? 
//					 																		coi.getPromoPrice().floatValue() : coi.getUnitPrice().floatValue())+"</td>\r\n" +
//							"            <td style=\"text-align:right; min-width:100px;\">$ "+StripeFormatCurrencyUtil.formatCurrency(discountAmountFloat)+"</td>\r\n" +
//							"            <td style=\"text-align:right; min-width:100px;\">$ "+StripeFormatCurrencyUtil.formatCurrency(coi.getFinalPrice().floatValue())+"</td>\r\n" +
//							"            <td style=\"text-align:right; min-width:100px;\">$ "+StripeFormatCurrencyUtil.formatCurrency(taxAmountPerProduct)+"</td>\r\n" +
//							"            <td style=\"text-align:right; min-width:100px;\">$ "+StripeFormatCurrencyUtil.formatCurrency(totalEachProduct)+"</td>\r\n" + 
//							"        </tr>";
//				}
//						
//				contentTable = contentTable + contentItem;
//			
//				String message = "<style>\r\n" + 
//						"    .div-right {\r\n" + 
//						"        margin-left: auto;\r\n" + 
//						"        margin-right: 0;\r\n" + 
//						"        right: 0px;\r\n" + 
//						"        width: 300px;\r\n" + 
//						"        padding: 10px;\r\n" + 
//						"    }\r\n" + 
//						"\r\n" + 
//						"    .div-middle {\r\n" + 
//						"        margin-left: auto;\r\n" + 
//						"        margin-right: auto;\r\n" + 
//						"        right: 0px;\r\n" + 
//						"        width: 300px;\r\n" + 
//						"        padding: 10px;\r\n" + 
//						"    }\r\n" + 
//						"\r\n" + 
//						"    .text-right {\r\n" + 
//						"        text-align: right;\r\n" + 
//						"    }\r\n" + 
//						"\r\n" + 
//						"    .logo {\r\n" + 
//						"        width: 150px;\r\n" + 
//						"    }\r\n" + 
//						"\r\n" + 
//						"    .table {\r\n" + 
//						"        text-align: center;\r\n" + 
//						"    }\r\n" + 
//						".table td, th {\r\n" + 
//						"      border-collapse: collapse;\r\n" + 
//						"      border: 1px solid #dddddd;\r\n" + 
//						"      text-align: left;\r\n" + 
//						"      padding: 8px;\r\n" + 
//						"    }"+
//						"\r\n" + 
//						"    .table th {\r\n" + 
//						"        white-space: nowrap;\r\n" + 
//						"\r\n" + 
//						"    }\r\n" + 
//						"</style>" +
//						"<p>Dear "+user.getFullName()+", </p>  \r\n" + 
//						"<br>\r\n" + 
//						"<p>Thank you for shopping with us.</p>\r\n" + 
//						"<p>To see your order detail, please visit : <a href=\""+orderLink+"\">here</a></p>\r\n" + 
//						"<p><strong>Order Summary</strong></p>\r\n" + 
//						"<div>\r\n" + 
//						"    <table>\r\n" + 
//						"        <tr>\r\n" + 
//						"            <td>Subtotal</td>\r\n" + 
//						"            <td>$</td>\r\n" + 
//						"            <td >"+StripeFormatCurrencyUtil.formatCurrency(totalUnitPrice)+"</td>\r\n" + 
//						"        </tr>\r\n" + 
//						"        <tr>\r\n" + 
//						"            <td>Total Discount</td>\r\n" + 
//						"            <td>$</td>\r\n" + 
//						"            <td >"+StripeFormatCurrencyUtil.formatCurrency(discountFromCommerceOrder)+"</td>\r\n" + 
//						"        </tr>\r\n" + 
//						"        <tr>\r\n" + 
//						"            <td>Net Price</td>\r\n" + 
//						"            <td>$</td>\r\n" + 
//						"            <td >"+StripeFormatCurrencyUtil.formatCurrency(subtotalUnitPrice)+"</td>\r\n" + 
//						"        </tr>\r\n" + 
//						"        <tr>\r\n" + 
//						"            <td>GST ("+StripeFormatCurrencyUtil.formatGSTCurrency(commerceTaxFixedRate)+"%) </td>\r\n" + 
//						"            <td>$</td>\r\n" + 
//						"            <td >"+StripeFormatCurrencyUtil.formatCurrency(commerceOrder.getTaxAmount().floatValue())+"</td>\r\n" + 
//						"        </tr>\r\n" + 
//						"        <tr>\r\n" + 
//						"            <td>Shipping</td>\r\n" + 
//						"            <td>$</td>\r\n" + 
//						"            <td >"+StripeFormatCurrencyUtil.formatCurrency(commerceOrder.getShippingAmount().floatValue())+"</td>\r\n" + 
//						"        </tr>\r\n" + 
//						"        <tr>\r\n" + 
//						"            <td><strong>Amount Payable</strong></td>\r\n" + 
//						"            <td><strong>$</strong></td>\r\n" + 
//						"            <td ><strong>"+StripeFormatCurrencyUtil.formatCurrency(commerceOrder.getTotal().floatValue())+"</strong></td>\r\n" + 
//						"        </tr>\r\n" + 
//						"    </table>\r\n" + 
//						"</div>\r\n" + 
//						"<br>\r\n" + 
//						"<div class=\"table\">\r\n" + 
//						"    <table>\r\n" + 
//								contentTable+ 
//						"    \r\n</table>\r\n" + 
//						"</div>\r\n" + 
//						"<br>\r\n" + 
//						"<table>\r\n" + 
//						"    <tr>\r\n" + 
//						"        <td colspan=\"2\">Billing Address</td>\r\n" + 
//						"        <td style=\"min-width:50px\"></td>\r\n" +
//						"        <td>Shipping Address</td>\r\n" + 
//						"    </tr>\r\n" + 
//						"    <tr>\r\n" + 
//						"        <td colspan=\"2\">"+commerceOrder.getBillingAddress().getName()+"<br>\r\n" + 
//						"            "+address+"<br>\r\n" + 
//						"            "+commerceOrder.getBillingAddress().getCity()+" "+commerceOrder.getBillingAddress().getZip()+"<br>\r\n" + 
//						"            "+commerceOrder.getBillingAddress().getCommerceCountry().getName(locale)+"<br>\r\n" + 
//						"            "+commerceOrder.getBillingAddress().getPhoneNumber()+"</td>\r\n" + 
//						"        <td></td>\r\n";
//						if(commerceOrder.getShippingAddress()!=null && !commerceOrder.getShippingAddress().getCommerceCountry().getName(locale).isEmpty()) {
//							message = message + "        <td colspan=\"2\">"+commerceOrder.getShippingAddress().getName()+"<br>\r\n" + 
//									"            "+address+"<br>\r\n" + 
//									"            "+commerceOrder.getShippingAddress().getCity()+" "+commerceOrder.getShippingAddress().getZip()+"<br>\r\n" + 
//									"            "+commerceOrder.getShippingAddress().getCommerceCountry().getName(locale)+"<br>\r\n" + 
//									"            "+commerceOrder.getShippingAddress().getPhoneNumber()+"</td>\r\n" + 
//									"    </tr>\r\n";
//						}else {
//							message = message + "<td>No Shipping required</td>\r\n";
//						}
//						
//						message = message + "</table>\r\n" + 
//						"<img src=\""+imgSrc+"\"></img> \r\n" + 
//						"<p> </p>\r\n" + 
//						"<p>This is a system generated email. Please do not reply directly to this email.</p>";
////				PortletPreferences portletPreferences = PrefsPropsUtil.getPreferences();			
////				String from = portletPreferences.getValue(PropsKeys.ADMIN_EMAIL_FROM_ADDRESS, "0");
//				Company company = CompanyLocalServiceUtil.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
//				String fromAddress = PrefsPropsUtil.getStringFromNames(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);
//				String fromName = PrefsPropsUtil.getStringFromNames(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_NAME);
//				
//				ArrayList<String> listReceiver = new ArrayList<>();
//				String email = user.getEmailAddress();
//				listReceiver.add(email);
//				Map<String, String> attachmentMaps = new HashMap<>();
//				boolean isCitrep = (boolean) commerceOrder.getExpandoBridge().getAttribute("CITREP");
//				if(isCitrep) {
//					String citrepPdf = (String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Pdf");
//					attachmentMaps.put("CITREP.pdf", citrepPdf);
//				}
//				String invocePdf = (String) commerceOrder.getExpandoBridge().getAttribute("INVOICE-Pdf");
//				attachmentMaps.put("INVOICE.pdf", invocePdf);
//				String subject = "Your NTUC LearningHub E-Shop Order Confirmation ["+commerceOrder.getCommerceOrderId()+"]";
//				EmailUtil.sendMailWithHTMLFormatAndAttachment(fromName, fromAddress,ccMail ,listReceiver, subject, message,attachmentMaps);
			}			
			httpServletResponse.sendRedirect(redirect);
//			_log.info("===========ater complete payment===========");
		}
		catch (Exception e) {
			_portal.sendError(e, httpServletRequest, httpServletResponse);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(CompletePaymentStripeServlet.class);

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;
	

	@Reference
	private CommercePaymentEngine _commercePaymentEngine;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private Portal _portal;
	
	@Reference
	private AuthenticatedSessionManager _authenticatedSessionManager;

	@Reference(target = "(osgi.web.symbolicname=web.ntuc.eshop.stripe.payment)")
	private ServletContext _servletContext;

}