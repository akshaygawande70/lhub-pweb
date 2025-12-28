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

package svc.ntuc.nlh.parameter.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

import svc.ntuc.nlh.parameter.model.ReservedParameter;

/**
 * The cache model class for representing ReservedParameter in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class ReservedParameterCacheModel
	implements CacheModel<ReservedParameter>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ReservedParameterCacheModel)) {
			return false;
		}

		ReservedParameterCacheModel reservedParameterCacheModel =
			(ReservedParameterCacheModel)object;

		if (reservedParameterId ==
				reservedParameterCacheModel.reservedParameterId) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, reservedParameterId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(23);

		sb.append("{reservedParameterId=");
		sb.append(reservedParameterId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", parameterId=");
		sb.append(parameterId);
		sb.append(", parameterGroupId=");
		sb.append(parameterGroupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", createdBy=");
		sb.append(createdBy);
		sb.append(", createdDate=");
		sb.append(createdDate);
		sb.append(", modifiedBy=");
		sb.append(modifiedBy);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", paramCode=");
		sb.append(paramCode);
		sb.append(", reservedBy=");
		sb.append(reservedBy);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public ReservedParameter toEntityModel() {
		ReservedParameterImpl reservedParameterImpl =
			new ReservedParameterImpl();

		reservedParameterImpl.setReservedParameterId(reservedParameterId);
		reservedParameterImpl.setGroupId(groupId);
		reservedParameterImpl.setParameterId(parameterId);
		reservedParameterImpl.setParameterGroupId(parameterGroupId);
		reservedParameterImpl.setCompanyId(companyId);
		reservedParameterImpl.setCreatedBy(createdBy);

		if (createdDate == Long.MIN_VALUE) {
			reservedParameterImpl.setCreatedDate(null);
		}
		else {
			reservedParameterImpl.setCreatedDate(new Date(createdDate));
		}

		reservedParameterImpl.setModifiedBy(modifiedBy);

		if (modifiedDate == Long.MIN_VALUE) {
			reservedParameterImpl.setModifiedDate(null);
		}
		else {
			reservedParameterImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (paramCode == null) {
			reservedParameterImpl.setParamCode("");
		}
		else {
			reservedParameterImpl.setParamCode(paramCode);
		}

		if (reservedBy == null) {
			reservedParameterImpl.setReservedBy("");
		}
		else {
			reservedParameterImpl.setReservedBy(reservedBy);
		}

		reservedParameterImpl.resetOriginalValues();

		return reservedParameterImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		reservedParameterId = objectInput.readLong();

		groupId = objectInput.readLong();

		parameterId = objectInput.readLong();

		parameterGroupId = objectInput.readLong();

		companyId = objectInput.readLong();

		createdBy = objectInput.readLong();
		createdDate = objectInput.readLong();

		modifiedBy = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		paramCode = objectInput.readUTF();
		reservedBy = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(reservedParameterId);

		objectOutput.writeLong(groupId);

		objectOutput.writeLong(parameterId);

		objectOutput.writeLong(parameterGroupId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(createdBy);
		objectOutput.writeLong(createdDate);

		objectOutput.writeLong(modifiedBy);
		objectOutput.writeLong(modifiedDate);

		if (paramCode == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(paramCode);
		}

		if (reservedBy == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(reservedBy);
		}
	}

	public long reservedParameterId;
	public long groupId;
	public long parameterId;
	public long parameterGroupId;
	public long companyId;
	public long createdBy;
	public long createdDate;
	public long modifiedBy;
	public long modifiedDate;
	public String paramCode;
	public String reservedBy;

}