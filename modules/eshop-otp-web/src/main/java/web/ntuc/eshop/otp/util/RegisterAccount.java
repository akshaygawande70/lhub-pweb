package web.ntuc.eshop.otp.util;

import com.liferay.commerce.account.constants.CommerceAccountConstants;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ResourceRequest;

import api.ntuc.common.dto.UserOtpDto;

public class RegisterAccount {
	
	private static Log log = LogFactoryUtil.getLog(RegisterAccount.class); 
	
	public static void add(ResourceRequest resourceRequest,User user, UserOtpDto userOtpDto) {
		try {
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
					CommerceAccount.class.getName(), resourceRequest);
			String taxId = StringPool.BLANK;
//			boolean active = Boolean.TRUE;
//			int type = CommerceAccountConstants.ACCOUNT_TYPE_BUSINESS;
//			long defaultBillingAddressId = 0L;
//			long defaultShippingAddressId = 0L;
//			long parentCommerceAccountId = 0L;
			String externalReferenceCode = StringPool.BLANK;
//			String[] emailAddresses = null;
			long[] userIds = new long[1];
			userIds[0] = user.getUserId();
			
//			long increment = CounterLocalServiceUtil.increment(CommerceAccount.class.getName());
//			CommerceAccount account = CommerceAccountLocalServiceUtil.createCommerceAccount(increment);
//			if(type == CommerceAccountConstants.ACCOUNT_TYPE_BUSINESS) {
//				Map<String, Serializable> expandoBridgeAttributes = new HashMap();
//				expandoBridgeAttributes.put("Company Code", companyCode);
//				expandoBridgeAttributes.put("Company Name", companyName);
//				expandoBridgeAttributes.put("UEN Number", uenNumber);
//				serviceContext.setExpandoBridgeAttributes(expandoBridgeAttributes);
//				CommerceAccountLocalServiceUtil.addBusinessCommerceAccount(contactPerson, parentCommerceAccountId, contactPersonEmailAddress, taxId, active, externalReferenceCode, userIds, emailAddresses, serviceContext);
//			} else {
				Map<String, Serializable> expandoBridgeAttributes = new HashMap<String, Serializable>();
				expandoBridgeAttributes.put("Company Code", userOtpDto.getCompanyCode());
				expandoBridgeAttributes.put("Company Name", userOtpDto.getCompanyName());
				expandoBridgeAttributes.put("UEN Number", userOtpDto.getUenNumber());
				expandoBridgeAttributes.put("NRIC", userOtpDto.getNric());
				expandoBridgeAttributes.put("Preferred Name", userOtpDto.getPreferredName());
				serviceContext.setExpandoBridgeAttributes(expandoBridgeAttributes);
				CommerceAccountLocalServiceUtil.addPersonalCommerceAccount(user.getUserId(), taxId, externalReferenceCode, serviceContext);
//			}
			
		}catch (PortalException e) {
			log.info("error while adding account : "+e.getMessage());
		}
	}
}
