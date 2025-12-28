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

import svc.ntuc.nlh.parameter.model.ParameterGroup;

/**
 * The cache model class for representing ParameterGroup in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class ParameterGroupCacheModel
	implements CacheModel<ParameterGroup>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ParameterGroupCacheModel)) {
			return false;
		}

		ParameterGroupCacheModel parameterGroupCacheModel =
			(ParameterGroupCacheModel)object;

		if (parameterGroupId == parameterGroupCacheModel.parameterGroupId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, parameterGroupId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(25);

		sb.append("{parameterGroupId=");
		sb.append(parameterGroupId);
		sb.append(", groupId=");
		sb.append(groupId);
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
		sb.append(", parentId=");
		sb.append(parentId);
		sb.append(", groupName=");
		sb.append(groupName);
		sb.append(", groupCode=");
		sb.append(groupCode);
		sb.append(", description=");
		sb.append(description);
		sb.append(", deleted=");
		sb.append(deleted);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public ParameterGroup toEntityModel() {
		ParameterGroupImpl parameterGroupImpl = new ParameterGroupImpl();

		parameterGroupImpl.setParameterGroupId(parameterGroupId);
		parameterGroupImpl.setGroupId(groupId);
		parameterGroupImpl.setCompanyId(companyId);
		parameterGroupImpl.setCreatedBy(createdBy);

		if (createdDate == Long.MIN_VALUE) {
			parameterGroupImpl.setCreatedDate(null);
		}
		else {
			parameterGroupImpl.setCreatedDate(new Date(createdDate));
		}

		parameterGroupImpl.setModifiedBy(modifiedBy);

		if (modifiedDate == Long.MIN_VALUE) {
			parameterGroupImpl.setModifiedDate(null);
		}
		else {
			parameterGroupImpl.setModifiedDate(new Date(modifiedDate));
		}

		parameterGroupImpl.setParentId(parentId);

		if (groupName == null) {
			parameterGroupImpl.setGroupName("");
		}
		else {
			parameterGroupImpl.setGroupName(groupName);
		}

		if (groupCode == null) {
			parameterGroupImpl.setGroupCode("");
		}
		else {
			parameterGroupImpl.setGroupCode(groupCode);
		}

		if (description == null) {
			parameterGroupImpl.setDescription("");
		}
		else {
			parameterGroupImpl.setDescription(description);
		}

		parameterGroupImpl.setDeleted(deleted);

		parameterGroupImpl.resetOriginalValues();

		return parameterGroupImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		parameterGroupId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		createdBy = objectInput.readLong();
		createdDate = objectInput.readLong();

		modifiedBy = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		parentId = objectInput.readLong();
		groupName = objectInput.readUTF();
		groupCode = objectInput.readUTF();
		description = objectInput.readUTF();

		deleted = objectInput.readBoolean();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(parameterGroupId);

		objectOutput.writeLong(groupId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(createdBy);
		objectOutput.writeLong(createdDate);

		objectOutput.writeLong(modifiedBy);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeLong(parentId);

		if (groupName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(groupName);
		}

		if (groupCode == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(groupCode);
		}

		if (description == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(description);
		}

		objectOutput.writeBoolean(deleted);
	}

	public long parameterGroupId;
	public long groupId;
	public long companyId;
	public long createdBy;
	public long createdDate;
	public long modifiedBy;
	public long modifiedDate;
	public long parentId;
	public String groupName;
	public String groupCode;
	public String description;
	public boolean deleted;

}