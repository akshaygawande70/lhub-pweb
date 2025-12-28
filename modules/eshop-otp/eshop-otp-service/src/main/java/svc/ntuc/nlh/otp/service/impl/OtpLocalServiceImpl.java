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

package svc.ntuc.nlh.otp.service.impl;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PrefsPropsUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.EmailUtil;
import svc.ntuc.nlh.otp.exception.NoSuchOtpException;
import svc.ntuc.nlh.otp.model.Otp;
import svc.ntuc.nlh.otp.service.base.OtpLocalServiceBaseImpl;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;

/**
 * The implementation of the otp local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>svc.ntuc.nlh.otp.service.OtpLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OtpLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=svc.ntuc.nlh.otp.model.Otp",
	service = AopService.class
)
public class OtpLocalServiceImpl extends OtpLocalServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use <code>svc.ntuc.nlh.otp.service.OtpLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>svc.ntuc.nlh.otp.service.OtpLocalServiceUtil</code>.
	 */
	private static Log log = LogFactoryUtil.getLog(OtpLocalServiceImpl.class);
	
//	finder
	public Otp getOtpByOtpIdAndUserId(long otpId, long userId) throws NoSuchOtpException {
		return otpPersistence.findByOtpIdUserId(otpId, userId);
	}
	
	public Otp addOtp(long groupId, long userId, int keyLength, int otpLength, String fullName, String email, ServiceContext serviceContext, PortletPreferences portletPreferences, String languageId) throws PortalException {
		Date now = new Date();
		User user = UserLocalServiceUtil.getUser(userId);
		Group group = GroupLocalServiceUtil.getGroup(groupId);
		int otpCode = generate(keyLength, otpLength);
		Otp otp = otpPersistence.create(CounterLocalServiceUtil.increment(Otp.class.getName()));
		otp.setOtpCode(otpCode);
		otp.setUserId(userId);
		otp.setUserName(user.getScreenName());
		otp.setGroupId(groupId);
		otp.setCompanyId(group.getCompanyId());
		otp.setCreateDate(now);
		
		ParameterGroup parameterGroup = ParameterGroupLocalServiceUtil
				.getByCode("eshopCheckout", false);
		long siteGroupId = parameterGroup.getGroupId();
		Parameter parameter = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
				parameterGroup.getParameterGroupId(), "eshopFooterImage", false);
		String imgSrc = parameter.getParamValue();
		Company company = CompanyLocalServiceUtil.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
		try {
//			User checkedUser = UserLocalServiceUtil.getUserByEmailAddress(company.getCompanyId(), email);
			
				String emailFromName = PrefsPropsUtil.getStringFromNames(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_NAME);
				String emailFromAddress = PrefsPropsUtil.getStringFromNames(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);
				String emailParam = "emailPasswordSent";
				if(company.isSendPasswordResetLink()) {
					emailParam = "emailPasswordReset";
				}
//				String subject = PrefsPropsUtil.getContent(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_PASSWORD_RESET_SUBJECT);
//				String body = PrefsPropsUtil.getContent(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_PASSWORD_RESET_BODY);
				String subject = portletPreferences.getValue(
						emailParam + "Subject_" + languageId, null);
					String body = portletPreferences.getValue(
						emailParam + "Body_" + languageId, null);
//				ServiceContext serviceContext = ServiceContextFactory.getInstance(
//						User.class.getName(), actionRequest);
//				ServiceContext serviceContext = new ServiceContext();
				serviceContext.setScopeGroupId(groupId);
				UserLocalServiceUtil.sendPassword(
						company.getCompanyId(), email, emailFromName, emailFromAddress, subject,
						body, serviceContext);
			
		}catch (Exception e) {
			log.error("Error while sending OTP email = "+e.getMessage());
			String message = "<p>Dear +preferedName, </p> \r\n" + 
					"<p> </p>\r\n" + 
					"<p>You have requested an email One-Time Passcode (OTP) to complete your registration. Your email OTP is[+otp_code]</p>\r\n" + 
					"<p>This OTP is valid for 2 minutes.</p>\r\n" + 
					"<img src=\""+imgSrc+"\"></img> \r\n" + 
					"<p> </p>\r\n" + 
					"<p>This is a system generated email. Please do not reply directly to this email.</p>";
			message = message.replace("+preferedName", fullName).replace("+otp_code",String.valueOf(otp.getOtpCode()));
			String fromAddress = PrefsPropsUtil.getStringFromNames(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);
			String fromName = PrefsPropsUtil.getStringFromNames(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_NAME);
			ArrayList<String> listReceiver = new ArrayList<String>();
			listReceiver.add(email);
			EmailUtil.sendMailWithHTMLFormat(fromName, fromAddress, listReceiver, "Registration Email OTP", message);
		}
		return super.addOtp(otp);
	}
	
	private int generate(int keyLength, int otpLength) {
		char[] otpApiKey = generateOTPApiKey(keyLength);
		String secretKey = generateOTPSecretKey();
		int otpCode = generateOTPRegister(otpApiKey, secretKey, otpLength);
		return otpCode;
	}
	
	private int generateOTPRegister(char[] otpApiKey, String secretKey, int generate) {

		if (!isOTPApiKey(otpApiKey)) {
			return 0;
		}
		if (!isOTPSecretKey(secretKey)) {
			return 0;
		}

		String str = new String(generatorOTP(generate));
		return Integer.parseInt(str);
	}

	private char[] generatorOTP(int length) {
		// Creating object of Random class
		Random obj = new Random();
		char[] otp = new char[length];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < 1000; j++) {
				otp[i] = (char) (obj.nextInt(10) + 48);
				if (otp[i] == '0') {
					otp[i] = (char) (obj.nextInt(10) + 48);
					if (otp[i] == '0') {
						otp[i] = (char) (obj.nextInt(10) + 48);
						if (otp[i] == '0') {
							otp[i] = (char) (obj.nextInt(10) + 48);
							if (otp[i] == '0') {
								otp[i] = (char) (obj.nextInt(10) + 48);
							}
						}
					}
				}
			}
		}
		return otp;
	}

	private boolean isOTPSecretKey(String string) {
		try {
			UUID.fromString(string);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	private boolean isOTPApiKey(char[] value) {
		char[] otp = new char[value.length];
		otp[0] = 'O';
		otp[1] = 'T';
		otp[2] = 'P';
		otp[22] = 'K';
		otp[23] = 'E';
		otp[24] = 'Y';

		boolean validOTP = true;
		for (int i = 0; i < value.length; i++) {
			if (i < 3) {
				if (otp[i] != value[i]) {
					validOTP = false;
				}
			}
			if (i > 21 && i < 25) {
				if (otp[i] != value[i]) {
					validOTP = false;
				}
			}
			if (i > 24) {
				if (!Character.isDigit(value[i])) {
					validOTP = false;
				}
			}
		}
		return validOTP;
	}

	private char[] generateOTPApiKey(int length) {
		// Creating object of Random class
		Random obj = new Random();
		char[] otp = new char[length];
		otp[0] = 'O';
		otp[1] = 'T';
		otp[2] = 'P';
		for (int i = 0; i < length; i++) {
			if (i > 2 && i < 22) {
				otp[i] = (char) (obj.nextInt(10) + 48);
			}
			if (i > 21 && i < 25) {
				otp[22] = 'K';
				otp[23] = 'E';
				otp[24] = 'Y';
			}
			if (i > 24) {
				otp[i] = (char) (obj.nextInt(10) + 48);
			}
		}
		return otp;
	}
	
	private String generateOTPSecretKey() {
		return UUID.randomUUID().toString();
	}
	
	@Override
	public Otp addOtp(Otp otp) {
		throw new UnsupportedOperationException("Not supported");
	}
}