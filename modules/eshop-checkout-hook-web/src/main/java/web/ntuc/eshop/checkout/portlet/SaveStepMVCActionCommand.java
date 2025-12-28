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

package web.ntuc.eshop.checkout.portlet;

import com.liferay.commerce.constants.CommercePortletKeys;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.service.CommerceOrderLocalServiceUtil;
import com.liferay.commerce.util.CommerceCheckoutStep;
import com.liferay.commerce.util.CommerceCheckoutStepServicesTracker;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 */
@Component(
	enabled = true, immediate = true,
	property = {
		"javax.portlet.name=" + CommercePortletKeys.COMMERCE_CHECKOUT,
		"mvc.command.name=/commerce_checkout/save_step",
		"service.ranking:Integer="+Integer.MAX_VALUE
	},
	service = MVCActionCommand.class
)
public class SaveStepMVCActionCommand extends BaseMVCActionCommand {
	private static final Log log = LogFactoryUtil.getLog(SaveStepMVCActionCommand.class);
	public String getRedirect(
			ActionRequest actionRequest, ActionResponse actionResponse,
			String checkoutStepName)
		throws Exception {
		
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		boolean isSignIn = themeDisplay.isSignedIn();
		if(isSignIn) {
			String redirect = GetterUtil.getString(
					actionRequest.getAttribute(WebKeys.REDIRECT));

				if (Validator.isNotNull(redirect)) {
					return redirect;
				}

				if (!SessionErrors.isEmpty(actionRequest)) {
					return getPortletURL(
						actionRequest, actionResponse, checkoutStepName);
				}

				CommerceCheckoutStep commerceCheckoutStep =
					_commerceCheckoutStepServicesTracker.getNextCommerceCheckoutStep(
						checkoutStepName, _portal.getHttpServletRequest(actionRequest),
						_portal.getHttpServletResponse(actionResponse));

				if (commerceCheckoutStep == null) {
					return ParamUtil.getString(actionRequest, "redirect");
				}

				return getPortletURL(
					actionRequest, actionResponse, commerceCheckoutStep.getName());
		} else {
			String paramUrl ="/registration?_com_liferay_login_web_portlet_LoginPortlet_redirect=https://www.fandi.com:8443/checkout";
			String finalUrl = paramUrl;
			return finalUrl;
		}
		
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {
		log.info("SaveStepMVCActionCommand::::::::doProcessAction");
		Map<String, String[]> parameterMap = actionRequest.getParameterMap();
		 
        // Iterate over the map and print all parameter names and values
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String paramName = entry.getKey();
            String[] paramValues = entry.getValue();
 
            // Print parameter name and its values (which could be multiple)
            log.info("SaveStepMVCActionCommand::::::::Parameter Name: " + paramName);
            for (String value : paramValues) {
            	log.info("SaveStepMVCActionCommand::::::::  Value: " + value);
            }
        }
		String checkoutStepName = ParamUtil.getString(
			actionRequest, "checkoutStepName");
		String zip = ParamUtil.getString(actionRequest, "zip");
		String phoneNumber = HtmlUtil.escapeXPath(ParamUtil.getString(actionRequest, "phoneNumber"));
		if(checkoutStepName.equals("billing-address") && phoneNumber != null && phoneNumber.length() > 0 && zip != null && zip.length() > 0) {

			
			boolean validZipcode = false;
			boolean validPhone = false;
			String strRegexZip = "^\\d{6}$";
			if (zip.matches(strRegexZip)) {
				log.info("SaveStepMVCActionCommand::Valid zip code."+zip);
				validZipcode = true;
			} else {
				log.info("SaveStepMVCActionCommand::Invalid zip code."+zip);
			}
			
			String strRegexPhone = "^[689]\\d{7}$";
			if (phoneNumber.matches(strRegexPhone)) {
				log.info("SaveStepMVCActionCommand::Valid phone number."+phoneNumber);
				validPhone = true;
			} else {
				log.info("SaveStepMVCActionCommand::Invalid phonenumber."+phoneNumber);
			}
			
			if(validPhone && validZipcode) {
				CommerceCheckoutStep commerceCheckoutStep =
						_commerceCheckoutStepServicesTracker.getCommerceCheckoutStep(
							checkoutStepName);

					commerceCheckoutStep.processAction(actionRequest, actionResponse);

					hideDefaultSuccessMessage(actionRequest);

					String redirect = getRedirect(
						actionRequest, actionResponse, checkoutStepName);

					sendRedirect(actionRequest, actionResponse, redirect);
			}else {
				log.info("SaveStepMVCActionCommand::Invalid phonenumber or zip code.");
			}
			
		}else {
			if(checkoutStepName.equals("custom-candidate-info-step")) {
				String date1 = ParamUtil.getString((PortletRequest) actionRequest, "date1");
				String time1 = ParamUtil.getString((PortletRequest) actionRequest, "time1");
				time1 = time1.replace(".", ":");
				time1 = time1.replace("a:m", "AM");
				String dateTimeStr1 = date1 + " " + time1;

				String date2 = ParamUtil.getString((PortletRequest) actionRequest, "date2");
				String time2 = ParamUtil.getString((PortletRequest) actionRequest, "time2");
				time2 = time2.replace(".", ":");
				time2 = time2.replace("a:m", "AM");
				String dateTimeStr2 = date2 + " " + time2;

				String date3 = ParamUtil.getString((PortletRequest) actionRequest, "date3");
				String time3 = ParamUtil.getString((PortletRequest) actionRequest, "time3");
				time3 = time3.replace(".", ":");
				time3 = time3.replace("a:m", "AM");
				String dateTimeStr3 = date3 + " " + time3;

				log.info("SaveStepMVCActionCommand::::::::Preferred Date 1"+ dateTimeStr1);
				log.info("SaveStepMVCActionCommand::::::::Preferred Date 2"+ dateTimeStr2);
				log.info("SaveStepMVCActionCommand::::::::Preferred Date 3"+ dateTimeStr3);
				 String dateFormat = "yyyy-MM-dd hh:mm a";
				 SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				 Date dateTime1 = sdf.parse(dateTimeStr1);
				 Date dateTime2 = sdf.parse(dateTimeStr2);
				 Date dateTime3 = sdf.parse(dateTimeStr3);
	             Date today = new Date();	
	             boolean isValidRequest = true;
	            // Compare today's date with the input date
	            if (today.before(dateTime1) && today.before(dateTime2) && today.before(dateTime3)) {
	            	log.info("SaveStepMVCActionCommand::::::::all exam  date greater than today's date.-- valid request");
	            } else {
	            	isValidRequest = false;
	            	//hideDefaultSuccessMessage(actionRequest);
	            	log.error("SaveStepMVCActionCommand::::::::exam  date less than or equal todays date-- invalid request");
	            	//throw new RuntimeException("exam  date less than or equal todays date-- invalid request");
	            	SessionErrors.add(actionRequest, "examdatevalidation-fail");
	            }
	            String email = ParamUtil.getString((PortletRequest) actionRequest, "email_address"); 	
	            String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		        if (email !=null && email !="" && email.matches(EMAIL_REGEX)) {
		        	log.info("SaveStepMVCActionCommand::::::::-  valid email pattern");
		        }else {
		        	isValidRequest = false;
		        	log.error("SaveStepMVCActionCommand:::::::: -  email regex pattern not matched");
		        	SessionErrors.add(actionRequest, "email-fail");
		        }
		        String dob = ParamUtil.getString((PortletRequest) actionRequest, "dob"); 	
		        if(dob !=null && dob != "") {
			        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			        LocalDate date = LocalDate.parse(dob, formatter);
			        LocalDate todayDate = LocalDate.now();
		            if (date.isAfter(todayDate)) {
		            	isValidRequest = false;
		            	log.error("SaveStepMVCActionCommand:::::::::::::::: -  Date of birth cannot be in the future");
		            	SessionErrors.add(actionRequest, "dob-fail");
		            }
		        }else {
		        	SessionErrors.add(actionRequest, "dob-null");
		        	log.error("SaveStepMVCActionCommand:::::::::::::::: -  Date of birth cannot be null");
		        	isValidRequest = false;
		        	
		        }

	            if(isValidRequest) {
	            	log.error("SaveStepMVCActionCommand:::::::: -  valid request");
					CommerceCheckoutStep commerceCheckoutStep =
							_commerceCheckoutStepServicesTracker.getCommerceCheckoutStep(
								checkoutStepName);

						commerceCheckoutStep.processAction(actionRequest, actionResponse);

						hideDefaultSuccessMessage(actionRequest);

						String redirect = getRedirect(
							actionRequest, actionResponse, checkoutStepName);

						sendRedirect(actionRequest, actionResponse, redirect);
	            }
			}else {
				CommerceCheckoutStep commerceCheckoutStep =
						_commerceCheckoutStepServicesTracker.getCommerceCheckoutStep(
							checkoutStepName);

					commerceCheckoutStep.processAction(actionRequest, actionResponse);

					hideDefaultSuccessMessage(actionRequest);

					String redirect = getRedirect(
						actionRequest, actionResponse, checkoutStepName);

					sendRedirect(actionRequest, actionResponse, redirect);
				
			}
		
		}

	}

	protected String getPortletURL(
		ActionRequest actionRequest, ActionResponse actionResponse,
		String checkoutStepName) {

		LiferayPortletResponse liferayPortletResponse =
			_portal.getLiferayPortletResponse(actionResponse);

		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		String commerceOrderUuid = ParamUtil.getString(
			actionRequest, "commerceOrderUuid");

		portletURL.setParameter("commerceOrderUuid", commerceOrderUuid);

		portletURL.setParameter("checkoutStepName", checkoutStepName);

		return portletURL.toString();
	}

	@Reference
	private CommerceCheckoutStepServicesTracker
		_commerceCheckoutStepServicesTracker;

	@Reference
	private Portal _portal;

}