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

package svc.ntuc.nlh.seo.bulkupload.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

import svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload;

/**
 * The cache model class for representing NtucBulkUpload in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class NtucBulkUploadCacheModel
	implements CacheModel<NtucBulkUpload>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof NtucBulkUploadCacheModel)) {
			return false;
		}

		NtucBulkUploadCacheModel ntucBulkUploadCacheModel =
			(NtucBulkUploadCacheModel)object;

		if (ntucBulkUploadId == ntucBulkUploadCacheModel.ntucBulkUploadId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, ntucBulkUploadId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(21);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", ntucBulkUploadId=");
		sb.append(ntucBulkUploadId);
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
		sb.append(", fileName=");
		sb.append(fileName);
		sb.append(", rowData=");
		sb.append(rowData);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public NtucBulkUpload toEntityModel() {
		NtucBulkUploadImpl ntucBulkUploadImpl = new NtucBulkUploadImpl();

		if (uuid == null) {
			ntucBulkUploadImpl.setUuid("");
		}
		else {
			ntucBulkUploadImpl.setUuid(uuid);
		}

		ntucBulkUploadImpl.setNtucBulkUploadId(ntucBulkUploadId);
		ntucBulkUploadImpl.setGroupId(groupId);
		ntucBulkUploadImpl.setCompanyId(companyId);
		ntucBulkUploadImpl.setUserId(userId);

		if (userName == null) {
			ntucBulkUploadImpl.setUserName("");
		}
		else {
			ntucBulkUploadImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			ntucBulkUploadImpl.setCreateDate(null);
		}
		else {
			ntucBulkUploadImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			ntucBulkUploadImpl.setModifiedDate(null);
		}
		else {
			ntucBulkUploadImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (fileName == null) {
			ntucBulkUploadImpl.setFileName("");
		}
		else {
			ntucBulkUploadImpl.setFileName(fileName);
		}

		if (rowData == null) {
			ntucBulkUploadImpl.setRowData("");
		}
		else {
			ntucBulkUploadImpl.setRowData(rowData);
		}

		ntucBulkUploadImpl.resetOriginalValues();

		return ntucBulkUploadImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		uuid = objectInput.readUTF();

		ntucBulkUploadId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		fileName = objectInput.readUTF();
		rowData = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(ntucBulkUploadId);

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

		if (fileName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(fileName);
		}

		if (rowData == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(rowData);
		}
	}

	public String uuid;
	public long ntucBulkUploadId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public String fileName;
	public String rowData;

}