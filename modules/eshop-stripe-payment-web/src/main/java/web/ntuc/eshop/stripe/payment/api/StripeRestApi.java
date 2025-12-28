package web.ntuc.eshop.stripe.payment.api;

import com.google.gson.JsonSyntaxException;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.model.CommerceOrderPayment;
import com.liferay.commerce.order.engine.CommerceOrderEngine;
import com.liferay.commerce.payment.engine.CommercePaymentEngine;
import com.liferay.commerce.payment.result.CommercePaymentResult;
import com.liferay.commerce.payment.util.CommercePaymentUtils;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.service.CommerceOrderLocalServiceUtil;
import com.liferay.commerce.service.CommerceOrderPaymentLocalService;
import com.liferay.commerce.service.CommerceOrderPaymentLocalServiceUtil;
import com.liferay.commerce.tax.engine.fixed.service.CommerceTaxFixedRateLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.security.auth.session.AuthenticatedSessionManager;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.PortalSessionThreadLocal;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

import api.ntuc.common.util.DownloadInvoiceUtil;
import api.ntuc.common.util.EmailUtil;
import api.ntuc.common.util.RoleUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.stripe.payment.configuration.StripeGroupServiceConfiguration;
import web.ntuc.eshop.stripe.payment.constants.StripeCommercePaymentMethodConstants;
import web.ntuc.eshop.stripe.payment.util.StripeFormatCurrencyUtil;

/**
 * @author yogienugraha
 */
@Component(property = { JaxrsWhiteboardConstants.JAX_RS_APPLICATION_BASE + "=/payment",
		JaxrsWhiteboardConstants.JAX_RS_NAME + "=Payment.Rest"}, service = Application.class)
public class StripeRestApi extends Application {
	
	private static final Log log = LogFactoryUtil.getLog(StripeRestApi.class);
	private static final String USER = "USER";
	private static final String USER_ID = "USER_ID";
	private static final String CURRENT_COMPLETE_URL = "CURRENT_COMPLETE_URL";
	private static final String INSECURE_RESPONSE = "com.liferay.portal.kernel.servlet.filters.invoker.InvokerFilterSECURE_RESPONSE";
	
	@Activate
	public void activate() {
		if (log.isInfoEnabled()) {
			log.info("Rest Api activated");
		}
	}

	@Override
	public Set<Object> getSingletons() {
		return Collections.<Object>singleton(this);
	}

	@POST
	@Path("/stripe_webhooks")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response stripeWebhooks(String request, @Context HttpServletRequest httpServletRequest, @HeaderParam("Stripe-Signature") String stripeSignature) {
		JSONObject response = JSONFactoryUtil.createJSONObject();		
		try {
			String vkeyApi = ParameterLocalServiceUtil.getByCode("stripe_vkey_api", false).getParamValue();
			log.info(Webhook.Signature.EXPECTED_SCHEME);
			if(!Webhook.Signature.verifyHeader(request, stripeSignature, vkeyApi, Integer.MAX_VALUE)) {
				log.error("Invalid payload, validation no error");
				return Response.status(403).build();
			}
			
			JSONObject jsonRequest = JSONFactoryUtil.createJSONObject(request);
//			log.info(jsonRequest.getJSONObject("data").toJSONString());
//			String returnUrl = jsonRequest.getJSONObject("object").getJSONObject("metadata").getString("success_url");
//			log.info(">>>>>> returnUrl : " + returnUrl);
			
//			URL url = new URL(returnUrl);
			long groupId = new Long(jsonRequest.getJSONObject("data").getJSONObject("object").getJSONObject("metadata").getString("groupId"));
			String uuid = jsonRequest.getJSONObject("data").getJSONObject("object").getJSONObject("metadata").getString("uuid");
			
			CommerceOrder commerceOrder =
					_commerceOrderLocalService.getCommerceOrderByUuidAndGroupId(
					uuid, groupId);
			User user = UserLocalServiceUtil.getUser(commerceOrder.getUserId());
			
			log.info("START OF CHECK API PARAM");
			
//			log.info("request : " + request);
//			log.info("stripeSignature : " + stripeSignature);
//			log.info(">>>> groupId : " + groupId);
//			log.info(">>>> uuid : " + uuid);
			
			StripeGroupServiceConfiguration stripConfig = _getStripeGroupServiceConfiguration(commerceOrder.getGroupId());
			
			if (log.isDebugEnabled()) {
				log.debug("apiKey=" + stripConfig.secretKey());
			}
			
			Stripe.apiKey = stripConfig.secretKey();	
			
//			log.info("key : >> " + stripConfig.secretKey());
//			if(!stripeSignature.equalsIgnoreCase(stripConfig.secretKey())) {
//				response.put("success", false);
//				return response.toJSONString();
//			}
			
			log.info("END OF CHECK API PARAM");
			
			httpServletRequest.setAttribute(USER, user);
			httpServletRequest.setAttribute(USER_ID, user.getUserId());
//			httpServletRequest.setAttribute(CURRENT_COMPLETE_URL, url.getHost());
			httpServletRequest.setAttribute(INSECURE_RESPONSE, Boolean.TRUE);
			
//			log.info("stripeSignature : " + stripeSignature);
//			log.info("vkeyApi : " + vkeyApi);
			int accType = 0;
			List<Role> roleList = new ArrayList<Role>();
			boolean corporateRolePresent = false;
			log.info("acctType before setting "+accType);
			log.info("get Roles  "+ user.getRoleIds().length);
			for (Long roleId : user.getRoleIds()) {
				log.info("role id  "+ roleId);
				if(roleId == 1612015) {
					corporateRolePresent = true;
				}
				if(roleId == 1784107) {
					corporateRolePresent = true;
				}
			}
			log.info("corporateRolePresent  "+ corporateRolePresent);
			if (corporateRolePresent) {
				accType = 2;
			} else {
				accType = 1;
			}
			log.info("acctType after setting "+accType);
			try {
				CommerceOrderPayment cop = CommerceOrderPaymentLocalServiceUtil.fetchLatestCommerceOrderPayment(commerceOrder.getCommerceOrderId());
				String contentCop = cop.getContent().replace("[", "").replace("]", "");
//				log.info("contentCop : " +contentCop);
//				log.info("cop.getStatus() : " +cop.getStatus());
				if((Validator.isNull(contentCop) || contentCop.isEmpty()) && cop.getStatus()!=0) {
					Webhook.constructEvent(request, stripeSignature, vkeyApi);
				}
			} catch (JsonSyntaxException e) {
				// Invalid payload
				log.error("Invalid payload, validation error : " + e.getMessage(), e);
				return Response.status(403).build();
			} catch (SignatureVerificationException e) {
				// Invalid signature
				log.error("Invalid signature, validation error : " + e.getMessage(), e);
				return Response.status(403).build();
			}
			
			
			HttpSession httpSession = httpServletRequest.getSession();
			
			if (PortalSessionThreadLocal.getHttpSession() == null) {
				log.info("session null");
				PortalSessionThreadLocal.setHttpSession(httpSession);
			}

			PermissionChecker permissionChecker =
					PermissionCheckerFactoryUtil.create(_portal.getUser(httpServletRequest));

			PermissionThreadLocal.setPermissionChecker(permissionChecker);
			
			boolean cancel = ParamUtil.getBoolean(httpServletRequest, "cancel");

			String redirect = ParamUtil.getString(
				httpServletRequest, "redirect");
			if (cancel) {
				_commercePaymentEngine.cancelPayment(
					commerceOrder.getCommerceOrderId(), null,
					httpServletRequest);
			}
			else {
				log.info(commerceOrder.getTransactionId());
				CommerceOrderPayment cop = CommerceOrderPaymentLocalServiceUtil.fetchLatestCommerceOrderPayment(commerceOrder.getCommerceOrderId());
				
				String contentCop = cop.getContent().replace("[", "").replace("]", "");
				if((Validator.isNull(contentCop) || contentCop.isEmpty()) && cop.getStatus()!=0) {
					CommercePaymentResult cpr = _commercePaymentEngine.completePayment(
							commerceOrder.getCommerceOrderId(), commerceOrder.getTransactionId(),
							httpServletRequest);
				}
//				CommerceOrderPaymentLocalServiceUtil.updateCommerceOrderPayment(commerceOrderPayment)
//				CommercePaymentResult cpr = _commercePaymentEngine.completePayment(
//						commerceOrder.getCommerceOrderId(), commerceOrder.getTransactionId(),
//						httpServletRequest);
//				log.info(cpr.getNewPaymentStatus());
//				for(String s : cpr.getResultMessages()) {
//					log.info(s);
//				}
				Locale locale = LocaleUtil.getSiteDefault();

				ParameterGroup parameterGroup = ParameterGroupLocalServiceUtil
						.getByCode("eshopCheckout", false);
				long siteGroupId = parameterGroup.getGroupId();
				Parameter parameterImg = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
						parameterGroup.getParameterGroupId(), "eshopFooterImage", false);
				String imgSrc = parameterImg.getParamValue();
				Parameter parameterCcMail = ParameterLocalServiceUtil.getByGroupCode(siteGroupId, parameterGroup.getParameterGroupId(), "eshopCcMail", false);
				String ccMail = parameterCcMail.getParamValue();
				CommerceOrderItem commerceOrderItem = commerceOrder.getCommerceOrderItems().get(0);
				String address = commerceOrder.getBillingAddress().getStreet1() + commerceOrder.getBillingAddress().getStreet2() + commerceOrder.getBillingAddress().getStreet3();
				String orderLink = (String) commerceOrder.getExpandoBridge().getAttribute("Order Link");
				
				List<CommerceOrderItem> commerceOrderItemList = commerceOrder.getCommerceOrderItems();
				
				String contentTable = 
						"        <tr>\r\n" + 
						"            <th style=\"text-align:left;\"><strong>Item</strong></th>\r\n" +
						"            <th><strong>SKU</strong></th>\r\n" + 
						"            <th><strong>Qty</strong></th>\r\n" + 
						"            <th><strong>Per Unit</strong></th>\r\n" +
						"            <th><strong>Discount</strong></th>\r\n" +
						"            <th><strong>Net Price</strong></th>\r\n" +
						"            <th><strong>GST</strong></th>\r\n" +
						"            <th><strong>Total</strong></th>\r\n" + 
						"        </tr>\r\n";
				String contentItem="";
				double subtotalUnitPrice = 0;
	 			double totalUnitPrice = 0;
	 			double discountFromCommerceOrder = commerceOrder.getShippingDiscountAmount().doubleValue() + 
	 					commerceOrder.getSubtotalDiscountAmount().doubleValue()+
	 					commerceOrder.getTotalDiscountAmount().doubleValue(); 
	 			double commerceTaxFixedRate = CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).size()>0?
	 					CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).get(0).getRate():0;
	 					
				for(CommerceOrderItem coi : commerceOrderItemList) {
					float getPriceQty = coi.getPromoPrice()!=null && coi.getPromoPrice().floatValue()>0 ? 
							coi.getPromoPrice().floatValue() : coi.getUnitPrice().floatValue() ;
			 		getPriceQty = getPriceQty * coi.getQuantity();
			 		double discountAmountFloat = coi.getDiscountAmount().floatValue();
			 		discountFromCommerceOrder = discountFromCommerceOrder + discountAmountFloat;
			 		double taxAmountPerProduct = getPriceQty * (commerceTaxFixedRate) * 0.01;
			 		double totalEachProduct = taxAmountPerProduct + getPriceQty - discountAmountFloat;
			 		totalUnitPrice = totalUnitPrice + getPriceQty;
			 		subtotalUnitPrice = subtotalUnitPrice + coi.getFinalPrice().doubleValue();
					contentItem = contentItem + "        <tr>\r\n" + 
							"            <td style=\"text-align:left;\">"+coi.getName(locale)+"</td>\r\n" +
							"            <td style=\"text-align:center;min-width:100px;\">"+coi.getSku()+"</td>\r\n" +
							"            <td style=\"text-align:center;\">"+coi.getQuantity()+"</td>\r\n" + 
							"            <td style=\"text-align:right; min-width:100px;\">$ "+StripeFormatCurrencyUtil.formatCurrency(coi.getPromoPrice()!=null && coi.getPromoPrice().floatValue()>0 ? 
					 																		coi.getPromoPrice().floatValue() : coi.getUnitPrice().floatValue())+"</td>\r\n" +
							"            <td style=\"text-align:right; min-width:100px;\">$ "+StripeFormatCurrencyUtil.formatCurrency(discountAmountFloat)+"</td>\r\n" +
							"            <td style=\"text-align:right; min-width:100px;\">$ "+StripeFormatCurrencyUtil.formatCurrency(coi.getFinalPrice().floatValue())+"</td>\r\n" +
							"            <td style=\"text-align:right; min-width:100px;\">$ "+StripeFormatCurrencyUtil.formatCurrency(taxAmountPerProduct)+"</td>\r\n" +
							"            <td style=\"text-align:right; min-width:100px;\">$ "+StripeFormatCurrencyUtil.formatCurrency(totalEachProduct)+"</td>\r\n" + 
							"        </tr>";
				}
						
				contentTable = contentTable + contentItem;
			
				String message = "<style>\r\n" + 
						"    .div-right {\r\n" + 
						"        margin-left: auto;\r\n" + 
						"        margin-right: 0;\r\n" + 
						"        right: 0px;\r\n" + 
						"        width: 300px;\r\n" + 
						"        padding: 10px;\r\n" + 
						"    }\r\n" + 
						"\r\n" + 
						"    .div-middle {\r\n" + 
						"        margin-left: auto;\r\n" + 
						"        margin-right: auto;\r\n" + 
						"        right: 0px;\r\n" + 
						"        width: 300px;\r\n" + 
						"        padding: 10px;\r\n" + 
						"    }\r\n" + 
						"\r\n" + 
						"    .text-right {\r\n" + 
						"        text-align: right;\r\n" + 
						"    }\r\n" + 
						"\r\n" + 
						"    .logo {\r\n" + 
						"        width: 150px;\r\n" + 
						"    }\r\n" + 
						"\r\n" + 
						"    .table {\r\n" + 
						"        text-align: center;\r\n" + 
						"    }\r\n" + 
						".table td, th {\r\n" + 
						"      border-collapse: collapse;\r\n" + 
						"      border: 1px solid #dddddd;\r\n" + 
						"      text-align: left;\r\n" + 
						"      padding: 8px;\r\n" + 
						"    }"+
						"\r\n" + 
						"    .table th {\r\n" + 
						"        white-space: nowrap;\r\n" + 
						"\r\n" + 
						"    }\r\n" + 
						"</style>" +
						"<p>Dear "+user.getFullName()+", </p>  \r\n" + 
						"<br>\r\n" + 
						"<p>Thank you for shopping with us.</p>\r\n" + 
						"<p>To see your order detail, please visit : <a href=\""+orderLink+"\">here</a></p>\r\n" + 
						"<p><strong>Order Summary</strong></p>\r\n" + 
						"<div>\r\n" + 
						"    <table>\r\n" + 
						"        <tr>\r\n" + 
						"            <td>Subtotal</td>\r\n" + 
						"            <td>$</td>\r\n" + 
						"            <td >"+StripeFormatCurrencyUtil.formatCurrency(totalUnitPrice)+"</td>\r\n" + 
						"        </tr>\r\n" + 
						"        <tr>\r\n" + 
						"            <td>Total Discount</td>\r\n" + 
						"            <td>$</td>\r\n" + 
						"            <td >"+StripeFormatCurrencyUtil.formatCurrency(discountFromCommerceOrder)+"</td>\r\n" + 
						"        </tr>\r\n" + 
						"        <tr>\r\n" + 
						"            <td>Net Price</td>\r\n" + 
						"            <td>$</td>\r\n" + 
						"            <td >"+StripeFormatCurrencyUtil.formatCurrency(subtotalUnitPrice)+"</td>\r\n" + 
						"        </tr>\r\n" + 
						"        <tr>\r\n" + 
						"            <td>GST ("+StripeFormatCurrencyUtil.formatGSTCurrency(commerceTaxFixedRate)+"%) </td>\r\n" + 
						"            <td>$</td>\r\n" + 
						"            <td >"+StripeFormatCurrencyUtil.formatCurrency(commerceOrder.getTaxAmount().floatValue())+"</td>\r\n" + 
						"        </tr>\r\n" + 
						"        <tr>\r\n" + 
						"            <td>Shipping</td>\r\n" + 
						"            <td>$</td>\r\n" + 
						"            <td >"+StripeFormatCurrencyUtil.formatCurrency(commerceOrder.getShippingAmount().floatValue())+"</td>\r\n" + 
						"        </tr>\r\n" + 
						"        <tr>\r\n" + 
						"            <td><strong>Amount Payable</strong></td>\r\n" + 
						"            <td><strong>$</strong></td>\r\n" + 
						"            <td ><strong>"+StripeFormatCurrencyUtil.formatCurrency(commerceOrder.getTotal().floatValue())+"</strong></td>\r\n" + 
						"        </tr>\r\n" + 
						"    </table>\r\n" + 
						"</div>\r\n" + 
						"<br>\r\n" + 
						"<div class=\"table\">\r\n" + 
						"    <table>\r\n" + 
								contentTable+ 
						"    \r\n</table>\r\n" + 
						"</div>\r\n" + 
						"<br>\r\n" + 
						"<table>\r\n" + 
						"    <tr>\r\n" + 
						"        <td colspan=\"2\">Billing Address</td>\r\n" + 
						"        <td style=\"min-width:50px\"></td>\r\n" +
						"        <td>Shipping Address</td>\r\n" + 
						"    </tr>\r\n" + 
						"    <tr>\r\n" + 
						"        <td colspan=\"2\">"+commerceOrder.getBillingAddress().getName()+"<br>\r\n" + 
						"            "+address+"<br>\r\n" + 
						"            "+commerceOrder.getBillingAddress().getCity()+" "+commerceOrder.getBillingAddress().getZip()+"<br>\r\n" + 
						"            "+commerceOrder.getBillingAddress().getCommerceCountry().getName(locale)+"<br>\r\n" + 
						"            "+commerceOrder.getBillingAddress().getPhoneNumber()+"</td>\r\n" + 
						"        <td></td>\r\n";
						if(commerceOrder.getShippingAddress()!=null && !commerceOrder.getShippingAddress().getCommerceCountry().getName(locale).isEmpty()) {
							message = message + "        <td colspan=\"2\">"+commerceOrder.getShippingAddress().getName()+"<br>\r\n" + 
									"            "+address+"<br>\r\n" + 
									"            "+commerceOrder.getShippingAddress().getCity()+" "+commerceOrder.getShippingAddress().getZip()+"<br>\r\n" + 
									"            "+commerceOrder.getShippingAddress().getCommerceCountry().getName(locale)+"<br>\r\n" + 
									"            "+commerceOrder.getShippingAddress().getPhoneNumber()+"</td>\r\n" + 
									"    </tr>\r\n";
						}else {
							message = message + "<td>No Shipping required</td>\r\n";
						}
						if(accType == 2) {
							message = message + ""
									+ "</table>\r\n"
									+ "<table><tr>"
									+ "<td>"
									+ "<div style=\"flex-direction: column;align-items: flex-start;padding: 24px;gap: 16px;line-height: 16px;left: 599px;top: 253px;background:#F2F2F2;\">"
									+ "The attached invoice is password protected. To view the invoice, simply enter your unique password in the following format:"
									+ "<b><p><span style=\"color:#047E46;\">&lt;Company Code&gt;</span><span style=\"color:#0097ce;\">&lt;last 4 characters of UEN&gt;</span></b>.</p>"
									+ "For example, if your Company Code is ABCD and the last 4 characters of your UEN is 123A, your unique password would be <b><span style=\"color:#047E46;\">ABCD</span><span style=\"color:#0097ce;\">123A</span></b>."
									+ "</div>"
									+ "</td>"
									+ "</tr></table>" ;
						}
						else {
							message = message + ""
									+ "</table>\r\n"
									+ "<table><tr>"
									+ "<td>"
									+ "<div style=\"flex-direction: column;align-items: flex-start;padding: 24px;gap: 16px;line-height: 16px;left: 599px;top: 253px;background:#F2F2F2;\">"
									+ "The attached invoice is password protected. To view the invoice, simply enter your unique password in the following format:"
									+ "<b><p><span style=\"color:#047E46;\">&lt;Date of Birth&gt;</span><span style=\"color:#0097ce;\">&lt;last 4 characters of NRIC&gt;</span></b>, where your date of birth should be in the 'ddmmyyyy' format.</p>"
									+ "For example, if your date of birth is on 1 January 1985 and the last 4 characters of your NRIC is 123A, your unique password would be <b><span style=\"color:#047E46;\">01011985</span><span style=\"color:#0097ce;\">123A</span></b>."
									+ "</div>"
									+ "</td>"
									+ "</tr></table>";
						}

						message = message + "<img src=\""+imgSrc+"\"></img> \r\n" + 
						"<p> </p>\r\n" + 
						"<p>This is a system generated email. Please do not reply directly to this email.</p>";
//				PortletPreferences portletPreferences = PrefsPropsUtil.getPreferences();			
//				String from = portletPreferences.getValue(PropsKeys.ADMIN_EMAIL_FROM_ADDRESS, "0");
				Company company = CompanyLocalServiceUtil.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
				String fromAddress = PrefsPropsUtil.getStringFromNames(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);
				String fromName = PrefsPropsUtil.getStringFromNames(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_NAME);
				
				ArrayList<String> listReceiver = new ArrayList<>();
				String email = user.getEmailAddress();
				listReceiver.add(email);
				Map<String, String> attachmentMaps = new HashMap<>();
				boolean isCitrep = (boolean) commerceOrder.getExpandoBridge().getAttribute("CITREP");
				if(isCitrep) {
					String citrepPdf = (String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Pdf");
					attachmentMaps.put("CITREP.pdf", citrepPdf);
				}
				String invocePdf = (String) commerceOrder.getExpandoBridge().getAttribute("INVOICE-Pdf");
				
				//if(invocePdf ==null || invocePdf ==""){
					
					invocePdf =	DownloadInvoiceUtil.downLoadInvoiceRetry(commerceOrder.getCommerceOrderId(), user, locale);
		//			log.info("INVOICE-Pdf::::::::::is null :::::::::downLoadInvoiceRetry"+invocePdf);
//					String invocePdf =	DownloadInvoiceUtil1.downloadInvoicePdf(commerceOrder.getCommerceOrderId(), user, locale);
					ServiceContext serviceContext = ServiceContextFactory.getInstance(CommerceOrder.class.getName(),
							httpServletRequest);
					Map<String, Serializable> exapandoBridgeAttributes = new HashMap<>();
					exapandoBridgeAttributes.put("INVOICE-Pdf", invocePdf);
					serviceContext.setExpandoBridgeAttributes(exapandoBridgeAttributes);
					CommerceOrderLocalServiceUtil.updateCustomFields(commerceOrder.getCommerceOrderId(), serviceContext);
					
					
				//}
				
			
				attachmentMaps.put("INVOICE.pdf", invocePdf);
				String subject = "Your NTUC LearningHub E-Shop Order Confirmation ["+commerceOrder.getCommerceOrderId()+"]";
				EmailUtil.sendMailWithHTMLFormatAndAttachment(fromName, fromAddress,ccMail ,listReceiver, subject, message,attachmentMaps);
			}			
				
			response.put("success", true);
//			log.info("===========ater complete payment===========");
		}
		catch (Exception e) {
			log.error("ERROR ON CALLBACK PAYMENT : " +e.getMessage(), e);
			response.put("success", false);
		}

		if((boolean)response.get("success")) {
			return Response.ok().build();
		}else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	private StripeGroupServiceConfiguration _getStripeGroupServiceConfiguration(long groupId) throws PortalException {

		return _configurationProvider.getConfiguration(StripeGroupServiceConfiguration.class,
				new GroupServiceSettingsLocator(groupId, StripeCommercePaymentMethodConstants.SERVICE_NAME));
	}
	
	@Reference
	private CommerceOrderEngine _commerceOrderEngine;
	
	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;
	
	@Reference
	private CommercePaymentEngine _commercePaymentEngine;
	
	@Reference
	private CommerceOrderPaymentLocalService _commerceOrderPaymentLocalService;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private Portal _portal;
	
	@Reference
	private CommercePaymentUtils _commercePaymentUtils;
	
	@Reference
	private AuthenticatedSessionManager _authenticatedSessionManager;
}