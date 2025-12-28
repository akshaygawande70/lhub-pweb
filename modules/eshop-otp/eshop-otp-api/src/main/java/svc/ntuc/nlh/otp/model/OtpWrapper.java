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

package svc.ntuc.nlh.otp.model;

import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link Otp}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Otp
 * @generated
 */
public class OtpWrapper
	extends BaseModelWrapper<Otp> implements ModelWrapper<Otp>, Otp {

	public OtpWrapper(Otp otp) {
		super(otp);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("otpId", getOtpId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("otpCode", getOtpCode());
		attributes.put("oTPValidatedFlag", getOTPValidatedFlag());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long otpId = (Long)attributes.get("otpId");

		if (otpId != null) {
			setOtpId(otpId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Integer otpCode = (Integer)attributes.get("otpCode");

		if (otpCode != null) {
			setOtpCode(otpCode);
		}

		Boolean oTPValidatedFlag = (Boolean)attributes.get("oTPValidatedFlag");

		if (oTPValidatedFlag != null) {
			setOTPValidatedFlag(oTPValidatedFlag);
		}
	}

	/**
	 * Returns the company ID of this otp.
	 *
	 * @return the company ID of this otp
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the create date of this otp.
	 *
	 * @return the create date of this otp
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the group ID of this otp.
	 *
	 * @return the group ID of this otp
	 */
	@Override
	public long getGroupId() {
		return model.getGroupId();
	}

	/**
	 * Returns the modified date of this otp.
	 *
	 * @return the modified date of this otp
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the otp code of this otp.
	 *
	 * @return the otp code of this otp
	 */
	@Override
	public int getOtpCode() {
		return model.getOtpCode();
	}

	/**
	 * Returns the otp ID of this otp.
	 *
	 * @return the otp ID of this otp
	 */
	@Override
	public long getOtpId() {
		return model.getOtpId();
	}

	/**
	 * Returns the o tp validated flag of this otp.
	 *
	 * @return the o tp validated flag of this otp
	 */
	@Override
	public Boolean getOTPValidatedFlag() {
		return model.getOTPValidatedFlag();
	}

	/**
	 * Returns the primary key of this otp.
	 *
	 * @return the primary key of this otp
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the user ID of this otp.
	 *
	 * @return the user ID of this otp
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user name of this otp.
	 *
	 * @return the user name of this otp
	 */
	@Override
	public String getUserName() {
		return model.getUserName();
	}

	/**
	 * Returns the user uuid of this otp.
	 *
	 * @return the user uuid of this otp
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	/**
	 * Returns the uuid of this otp.
	 *
	 * @return the uuid of this otp
	 */
	@Override
	public String getUuid() {
		return model.getUuid();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the company ID of this otp.
	 *
	 * @param companyId the company ID of this otp
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this otp.
	 *
	 * @param createDate the create date of this otp
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the group ID of this otp.
	 *
	 * @param groupId the group ID of this otp
	 */
	@Override
	public void setGroupId(long groupId) {
		model.setGroupId(groupId);
	}

	/**
	 * Sets the modified date of this otp.
	 *
	 * @param modifiedDate the modified date of this otp
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the otp code of this otp.
	 *
	 * @param otpCode the otp code of this otp
	 */
	@Override
	public void setOtpCode(int otpCode) {
		model.setOtpCode(otpCode);
	}

	/**
	 * Sets the otp ID of this otp.
	 *
	 * @param otpId the otp ID of this otp
	 */
	@Override
	public void setOtpId(long otpId) {
		model.setOtpId(otpId);
	}

	/**
	 * Sets the o tp validated flag of this otp.
	 *
	 * @param oTPValidatedFlag the o tp validated flag of this otp
	 */
	@Override
	public void setOTPValidatedFlag(Boolean oTPValidatedFlag) {
		model.setOTPValidatedFlag(oTPValidatedFlag);
	}

	/**
	 * Sets the primary key of this otp.
	 *
	 * @param primaryKey the primary key of this otp
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the user ID of this otp.
	 *
	 * @param userId the user ID of this otp
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user name of this otp.
	 *
	 * @param userName the user name of this otp
	 */
	@Override
	public void setUserName(String userName) {
		model.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this otp.
	 *
	 * @param userUuid the user uuid of this otp
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	/**
	 * Sets the uuid of this otp.
	 *
	 * @param uuid the uuid of this otp
	 */
	@Override
	public void setUuid(String uuid) {
		model.setUuid(uuid);
	}

	@Override
	public StagedModelType getStagedModelType() {
		return model.getStagedModelType();
	}

	@Override
	protected OtpWrapper wrap(Otp otp) {
		return new OtpWrapper(otp);
	}

}