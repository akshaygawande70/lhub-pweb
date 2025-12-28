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

package web.ntuc.eshop.address.portlet;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.constants.CommercePortletKeys;
import com.liferay.commerce.exception.CommerceAddressCityException;
import com.liferay.commerce.exception.CommerceAddressCountryException;
import com.liferay.commerce.exception.CommerceAddressStreetException;
import com.liferay.commerce.exception.NoSuchAddressException;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.RedirectUtil;
import web.ntuc.eshop.address.constants.AddressHookPortletKeys;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	enabled = true, immediate = true,
	property = {
		"javax.portlet.name=" + CommercePortletKeys.COMMERCE_ADDRESS_CONTENT,
		"mvc.command.name=/commerce_address_content/edit_commerce_address",
		"service.ranking:Integer="+Integer.MAX_VALUE
	},
	service = MVCActionCommand.class
)
public class EditCommerceAddressMVCActionCommand extends BaseMVCActionCommand {
	private static final Log log = LogFactoryUtil.getLog(EditCommerceAddressMVCActionCommand.class);
	protected void deleteCommerceAddress(ActionRequest actionRequest)
		throws Exception {

		long commerceAddressId = ParamUtil.getLong(
			actionRequest, "commerceAddressId");

		if (commerceAddressId > 0) {
			_commerceAddressService.deleteCommerceAddress(commerceAddressId);
		}
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.DELETE)) {
				deleteCommerceAddress(actionRequest);
			}
			else if (cmd.equals(Constants.ADD) ||
					 cmd.equals(Constants.UPDATE)) {

				updateCommerceAddress(actionRequest);
			}
			RedirectUtil.redirectByUrl(actionRequest, actionResponse, AddressHookPortletKeys.REDIRECT_URL);
			
		}
		catch (Exception exception) {
			if (exception instanceof NoSuchAddressException ||
				exception instanceof PrincipalException) {

				SessionErrors.add(actionRequest, exception.getClass());

				actionResponse.setRenderParameter("mvcPath", "/error.jsp");
			}
			else if (exception instanceof CommerceAddressCityException ||
					 exception instanceof CommerceAddressCountryException ||
					 exception instanceof CommerceAddressStreetException) {

				hideDefaultErrorMessage(actionRequest);

				SessionErrors.add(actionRequest, exception.getClass());

				String redirect = _portal.getCurrentURL(actionRequest);

				sendRedirect(actionRequest, actionResponse, redirect);
			}
			else {
				throw exception;
			}
		}

		hideDefaultSuccessMessage(actionRequest);
	}
	
	private String escapeChar(String field) {
		return field.replaceAll("[^a-zA-Z0-9\\s]", "_");
	}
	
	protected void updateCommerceAddress(ActionRequest actionRequest)
		throws Exception {

		long commerceAddressId = ParamUtil.getLong(
			actionRequest, "commerceAddressId");

		String name = escapeChar(ParamUtil.getString(actionRequest, "name"));
		String description = escapeChar(ParamUtil.getString(actionRequest, "description"));
		String street1 = escapeChar(ParamUtil.getString(actionRequest, "street1"));
		String street2 = escapeChar(ParamUtil.getString(actionRequest, "street2"));
		String street3 = escapeChar(ParamUtil.getString(actionRequest, "street3"));
		String city = escapeChar(ParamUtil.getString(actionRequest, "city"));
		String zip = escapeChar(ParamUtil.getString(actionRequest, "zip"));
		boolean validZipcode = false;
		boolean validPhone = false;
		String strRegexZip = "^\\d{6}$";
		if (zip.matches(strRegexZip)) {
			log.info("EditCommerceAddressMVCActionCommand::Valid zip code.");
			validZipcode = true;
		} else {
			log.info("EditCommerceAddressMVCActionCommand::Invalid zip code.");
		}
		long commerceCountryId = ParamUtil.getLong(
			actionRequest, "commerceCountryId");
		long commerceRegionId = ParamUtil.getLong(
			actionRequest, "commerceRegionId");
		String phoneNumber = HtmlUtil.escapeXPath(ParamUtil.getString(actionRequest, "phoneNumber"));
		String strRegexPhone = "^[689]\\d{7}$";
		if (phoneNumber.matches(strRegexPhone)) {
			log.info("EditCommerceAddressMVCActionCommand::Valid phone number.");
			validPhone = true;
		} else {
			log.info("EditCommerceAddressMVCActionCommand::Invalid phonenumber.");
		}
		boolean defaultBilling = ParamUtil.getBoolean(
			actionRequest, "defaultBilling");
		boolean defaultShipping = ParamUtil.getBoolean(
			actionRequest, "defaultShipping");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			CommerceAddress.class.getName(), actionRequest);
        if(validPhone && validZipcode) {
    		if (commerceAddressId <= 0) {
    			long commerceAccountId = ParamUtil.getLong(
    				actionRequest, "commerceAccountId");

    			_commerceAddressService.addCommerceAddress(
    				CommerceAccount.class.getName(), commerceAccountId, name,
    				description, street1, street2, street3, city, zip,
    				commerceRegionId, commerceCountryId, phoneNumber,
    				defaultBilling, defaultShipping, serviceContext);
    		}
    		else {
    			_commerceAddressService.updateCommerceAddress(
    				commerceAddressId, name, description, street1, street2, street3,
    				city, zip, commerceRegionId, commerceCountryId, phoneNumber,
    				defaultBilling, defaultShipping, serviceContext);
    		}
        	
        }else {
        	log.info("EditCommerceAddressMVCActionCommand::Invalid phonenumber or zip code.");
        }

		
	}

	@Reference
	private CommerceAddressService _commerceAddressService;

	@Reference
	private Portal _portal;

}