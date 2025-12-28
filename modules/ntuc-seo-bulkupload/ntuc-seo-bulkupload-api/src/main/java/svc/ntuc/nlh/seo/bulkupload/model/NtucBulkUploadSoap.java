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

package svc.ntuc.nlh.seo.bulkupload.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author Brian Wing Shun Chan
 * @deprecated As of Athanasius (7.3.x), with no direct replacement
 * @generated
 */
@Deprecated
public class NtucBulkUploadSoap implements Serializable {

	public static NtucBulkUploadSoap toSoapModel(NtucBulkUpload model) {
		NtucBulkUploadSoap soapModel = new NtucBulkUploadSoap();

		soapModel.setUuid(model.getUuid());
		soapModel.setNtucBulkUploadId(model.getNtucBulkUploadId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setFileName(model.getFileName());
		soapModel.setRowData(model.getRowData());

		return soapModel;
	}

	public static NtucBulkUploadSoap[] toSoapModels(NtucBulkUpload[] models) {
		NtucBulkUploadSoap[] soapModels = new NtucBulkUploadSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static NtucBulkUploadSoap[][] toSoapModels(
		NtucBulkUpload[][] models) {

		NtucBulkUploadSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels =
				new NtucBulkUploadSoap[models.length][models[0].length];
		}
		else {
			soapModels = new NtucBulkUploadSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static NtucBulkUploadSoap[] toSoapModels(
		List<NtucBulkUpload> models) {

		List<NtucBulkUploadSoap> soapModels = new ArrayList<NtucBulkUploadSoap>(
			models.size());

		for (NtucBulkUpload model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new NtucBulkUploadSoap[soapModels.size()]);
	}

	public NtucBulkUploadSoap() {
	}

	public long getPrimaryKey() {
		return _ntucBulkUploadId;
	}

	public void setPrimaryKey(long pk) {
		setNtucBulkUploadId(pk);
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getNtucBulkUploadId() {
		return _ntucBulkUploadId;
	}

	public void setNtucBulkUploadId(long ntucBulkUploadId) {
		_ntucBulkUploadId = ntucBulkUploadId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public String getFileName() {
		return _fileName;
	}

	public void setFileName(String fileName) {
		_fileName = fileName;
	}

	public String getRowData() {
		return _rowData;
	}

	public void setRowData(String rowData) {
		_rowData = rowData;
	}

	private String _uuid;
	private long _ntucBulkUploadId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private String _fileName;
	private String _rowData;

}