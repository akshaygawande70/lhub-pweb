package web.ntuc.eshop.register.util;

import com.liferay.commerce.account.constants.CommerceAccountConstants;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;

import api.ntuc.common.util.AESEncryptUtil;
import api.ntuc.common.util.HttpApiUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.register.constants.RegisterPortletKeys;

public class RegisterAccount {
	
	private static Log log = LogFactoryUtil.getLog(RegisterAccount.class); 
//	private static final String SECRET_KEY = "1FB4BC02FB22816F8FE874E58ED331AE";
	private static final String VALIDATION_URL = "/validate/companycode"; 
	
	public static void add(ActionRequest actionRequest,User user, String contactPerson, String contactPersonEmailAddress, int type, String idNumber, String companyCode, String companyName, String uenNumber) {
		try {
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
					CommerceAccount.class.getName(), actionRequest);
			String taxId = StringPool.BLANK;
			boolean active = Boolean.TRUE;
//			int type = CommerceAccountConstants.ACCOUNT_TYPE_BUSINESS;
//			long defaultBillingAddressId = 0L;
//			long defaultShippingAddressId = 0L;
			long parentCommerceAccountId = 0L;
			String externalReferenceCode = StringPool.BLANK;
			String[] emailAddresses = null;
			long[] userIds = new long[1];
			userIds[0] = user.getUserId();
			
//			long increment = CounterLocalServiceUtil.increment(CommerceAccount.class.getName());
//			CommerceAccount account = CommerceAccountLocalServiceUtil.createCommerceAccount(increment);
			if(type == CommerceAccountConstants.ACCOUNT_TYPE_BUSINESS) {
				Map<String, Serializable> expandoBridgeAttributes = new HashMap<String, Serializable>();
				expandoBridgeAttributes.put("Company Code", companyCode);
				expandoBridgeAttributes.put("Company Name", companyName);
				expandoBridgeAttributes.put("UEN Number", uenNumber);
				serviceContext.setExpandoBridgeAttributes(expandoBridgeAttributes);
				CommerceAccountLocalServiceUtil.addBusinessCommerceAccount(contactPerson, parentCommerceAccountId, contactPersonEmailAddress, taxId, active, externalReferenceCode, userIds, emailAddresses, serviceContext);
			} else {
				Map<String, Serializable> expandoBridgeAttributes = new HashMap<>();
				expandoBridgeAttributes.put("NRIC", idNumber);
				serviceContext.setExpandoBridgeAttributes(expandoBridgeAttributes);
				CommerceAccountLocalServiceUtil.addPersonalCommerceAccount(user.getUserId(), taxId, externalReferenceCode, serviceContext);
			}
			
		}catch (PortalException e) {
			log.info("error while adding account : "+e.getMessage());
		}
	}
	
	public static boolean corporateValidate(String uenNumber, String companyCode) {
		try {
			ParameterGroup parameterAuthGroup = ParameterGroupLocalServiceUtil
					.getByCode(RegisterPortletKeys.PARAMETER_AUTH_GROUP_CODE, false);
			long siteGroupId = parameterAuthGroup.getGroupId();
			Parameter parameterClientId = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_ESHOP_CLIENT_ID_CODE, false);
//			log.info("client id = "+parameterClientId.getParamValue());
			Parameter parameterClientSecret = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_ESHOP_CLIENT_SECRET_CODE, false);
//			log.info("client secret = "+parameterClientSecret.getParamValue());
//		GET API URL FROM PARAMETER
			ParameterGroup parameterApiGroup = ParameterGroupLocalServiceUtil
					.getByCode(RegisterPortletKeys.PARAMETER_URL_GROUP_CODE, false);
			Parameter parameterApiCorporateValidate = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterApiGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_ESHOP_API_CODE,
					false);

//			String encryptUenNumber = AESEncryptUtil.encrypt(uenNumber, SECRET_KEY);
//			String encryptCompanyCode = AESEncryptUtil.encrypt(companyCode, SECRET_KEY);
			
			
			JSONObject jsonRequest = JSONFactoryUtil.createJSONObject();
			jsonRequest.put("company_code", companyCode);
			jsonRequest.put("uen_number", uenNumber);
//			String encodedKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes("UTF-8"));
			String encodedKey = getSecretKey();
			String encryptedJsonRequest = AESEncryptUtil.encrypt(jsonRequest.toString(), encodedKey);
			log.info("url = "+parameterApiCorporateValidate.getParamValue()+VALIDATION_URL);
//			encryptedJsonRequest = encryptedJsonRequest.base
//			String phanniBody = "nxQgH3Mto7YA5jqfcYyoC2tEyTqNzLAizSVhqp0IFIH5iVo2W0eOcW8ZpYHVYNszEYxaqKpAHpKcQwWUhb8LCA==";
//			encryptedJsonRequest = Base64.getEncoder().encodeToString(phanniBody.getBytes());
//			encryptedJsonRequest = phanniBody;
			log.info("===== JSON REQUEST =====");
			log.info(encryptedJsonRequest);
			log.info("===== JSON REQUEST =====");
			
//			log.info("===== DECRYPT JSON REQUEST =====");
//			log.info(AESEncryptUtil.decrypt(encryptedJsonRequest, encodedKey));
//			log.info("===== DECRYPT JSON REQUEST =====");
			Object tmsResponse = HttpApiUtil.request(parameterApiCorporateValidate.getParamValue()+VALIDATION_URL,
					Http.Method.GET.name(), encryptedJsonRequest, parameterClientId.getParamValue(),
					parameterClientSecret.getParamValue(),encodedKey);

//			Object tmsResponse = HttpApiUtil.request(parameterApiCorporateValidate.getParamValue(),
//					Http.Method.GET.name(), "", parameterClientId.getParamValue(),
//					parameterClientSecret.getParamValue());
			log.info("===== JSON RESPONSE =====");
			log.info(tmsResponse);
			log.info("===== JSON RESPONSE =====");
			
			/*String decryptedJsonRequest = AESEncryptUtil.decrypt(tmsResponse.toString(), SECRET_KEY); 
			
			log.info("===== DECRYPT JSON RESPONSE =====");
			log.info(decryptedJsonRequest);
			log.info("===== DECRYPT JSON RESPONSE =====");
			
			if(Validator.isNull(decryptedJsonRequest)) {
				return false;
			}*/
			
			JSONObject jsonResponse = JSONFactoryUtil.createJSONObject(tmsResponse.toString());

			if(Validator.isNull(jsonResponse.getString("valid_company")) ) {
				return false;
			} else {
				if(jsonResponse.getString("valid_company").equals("NO")) {
					return false;
				}
			}
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}
	
	private static String getSecretKey() {
		ParameterGroup parameterAuthGroup = ParameterGroupLocalServiceUtil
				.getByCode(RegisterPortletKeys.PARAMETER_AUTH_GROUP_CODE, false);
		long siteGroupId = parameterAuthGroup.getGroupId();
		Parameter parameterSecretKey = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
				parameterAuthGroup.getParameterGroupId(), RegisterPortletKeys.PARAMETER_ESHOP_TMS_SECRET_KEY, false);
		return parameterSecretKey.getParamValue();
	}
}
