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

import svc.ntuc.nlh.parameter.model.Parameter;

/**
 * The cache model class for representing Parameter in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class ParameterCacheModel
	implements CacheModel<Parameter>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ParameterCacheModel)) {
			return false;
		}

		ParameterCacheModel parameterCacheModel = (ParameterCacheModel)object;

		if (parameterId == parameterCacheModel.parameterId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, parameterId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(27);

		sb.append("{parameterId=");
		sb.append(parameterId);
		sb.append(", groupId=");
		sb.append(groupId);
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
		sb.append(", paramName=");
		sb.append(paramName);
		sb.append(", paramValue=");
		sb.append(paramValue);
		sb.append(", description=");
		sb.append(description);
		sb.append(", deleted=");
		sb.append(deleted);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public Parameter toEntityModel() {
		ParameterImpl parameterImpl = new ParameterImpl();

		parameterImpl.setParameterId(parameterId);
		parameterImpl.setGroupId(groupId);
		parameterImpl.setParameterGroupId(parameterGroupId);
		parameterImpl.setCompanyId(companyId);
		parameterImpl.setCreatedBy(createdBy);

		if (createdDate == Long.MIN_VALUE) {
			parameterImpl.setCreatedDate(null);
		}
		else {
			parameterImpl.setCreatedDate(new Date(createdDate));
		}

		parameterImpl.setModifiedBy(modifiedBy);

		if (modifiedDate == Long.MIN_VALUE) {
			parameterImpl.setModifiedDate(null);
		}
		else {
			parameterImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (paramCode == null) {
			parameterImpl.setParamCode("");
		}
		else {
			parameterImpl.setParamCode(paramCode);
		}

		if (paramName == null) {
			parameterImpl.setParamName("");
		}
		else {
			parameterImpl.setParamName(paramName);
		}

		if (paramValue == null) {
			parameterImpl.setParamValue("");
		}
		else {
			parameterImpl.setParamValue(paramValue);
		}

		if (description == null) {
			parameterImpl.setDescription("");
		}
		else {
			parameterImpl.setDescription(description);
		}

		parameterImpl.setDeleted(deleted);

		parameterImpl.resetOriginalValues();

		return parameterImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		parameterId = objectInput.readLong();

		groupId = objectInput.readLong();

		parameterGroupId = objectInput.readLong();

		companyId = objectInput.readLong();

		createdBy = objectInput.readLong();
		createdDate = objectInput.readLong();

		modifiedBy = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		paramCode = objectInput.readUTF();
		paramName = objectInput.readUTF();
		paramValue = objectInput.readUTF();
		description = objectInput.readUTF();

		deleted = objectInput.readBoolean();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(parameterId);

		objectOutput.writeLong(groupId);

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

		if (paramName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(paramName);
		}

		if (paramValue == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(paramValue);
		}

		if (description == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(description);
		}

		objectOutput.writeBoolean(deleted);
	}

	public long parameterId;
	public long groupId;
	public long parameterGroupId;
	public long companyId;
	public long createdBy;
	public long createdDate;
	public long modifiedBy;
	public long modifiedDate;
	public String paramCode;
	public String paramName;
	public String paramValue;
	public String description;
	public boolean deleted;

}