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

package svc.ntuc.nlh.otp.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

import svc.ntuc.nlh.otp.model.Otp;

/**
 * The cache model class for representing Otp in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class OtpCacheModel implements CacheModel<Otp>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof OtpCacheModel)) {
			return false;
		}

		OtpCacheModel otpCacheModel = (OtpCacheModel)object;

		if (otpId == otpCacheModel.otpId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, otpId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(21);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", otpId=");
		sb.append(otpId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", otpCode=");
		sb.append(otpCode);
		sb.append(", oTPValidatedFlag=");
		sb.append(oTPValidatedFlag);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public Otp toEntityModel() {
		OtpImpl otpImpl = new OtpImpl();

		if (uuid == null) {
			otpImpl.setUuid("");
		}
		else {
			otpImpl.setUuid(uuid);
		}

		otpImpl.setOtpId(otpId);
		otpImpl.setGroupId(groupId);
		otpImpl.setCompanyId(companyId);
		otpImpl.setUserId(userId);

		if (userName == null) {
			otpImpl.setUserName("");
		}
		else {
			otpImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			otpImpl.setCreateDate(null);
		}
		else {
			otpImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			otpImpl.setModifiedDate(null);
		}
		else {
			otpImpl.setModifiedDate(new Date(modifiedDate));
		}

		otpImpl.setOtpCode(otpCode);
		otpImpl.setOTPValidatedFlag(oTPValidatedFlag);

		otpImpl.resetOriginalValues();

		return otpImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		uuid = objectInput.readUTF();

		otpId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		otpCode = objectInput.readInt();

		oTPValidatedFlag = objectInput.readBoolean();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(otpId);

		objectOutput.writeLong(groupId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeInt(otpCode);

		objectOutput.writeBoolean(oTPValidatedFlag);
	}

	public String uuid;
	public long otpId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public int otpCode;
	public boolean oTPValidatedFlag;

}