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

package svc.ntuc.nlh.parameter.model;

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
public class ParameterGroupSoap implements Serializable {

	public static ParameterGroupSoap toSoapModel(ParameterGroup model) {
		ParameterGroupSoap soapModel = new ParameterGroupSoap();

		soapModel.setParameterGroupId(model.getParameterGroupId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setCreatedBy(model.getCreatedBy());
		soapModel.setCreatedDate(model.getCreatedDate());
		soapModel.setModifiedBy(model.getModifiedBy());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setParentId(model.getParentId());
		soapModel.setGroupName(model.getGroupName());
		soapModel.setGroupCode(model.getGroupCode());
		soapModel.setDescription(model.getDescription());
		soapModel.setDeleted(model.getDeleted());

		return soapModel;
	}

	public static ParameterGroupSoap[] toSoapModels(ParameterGroup[] models) {
		ParameterGroupSoap[] soapModels = new ParameterGroupSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static ParameterGroupSoap[][] toSoapModels(
		ParameterGroup[][] models) {

		ParameterGroupSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels =
				new ParameterGroupSoap[models.length][models[0].length];
		}
		else {
			soapModels = new ParameterGroupSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static ParameterGroupSoap[] toSoapModels(
		List<ParameterGroup> models) {

		List<ParameterGroupSoap> soapModels = new ArrayList<ParameterGroupSoap>(
			models.size());

		for (ParameterGroup model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new ParameterGroupSoap[soapModels.size()]);
	}

	public ParameterGroupSoap() {
	}

	public long getPrimaryKey() {
		return _parameterGroupId;
	}

	public void setPrimaryKey(long pk) {
		setParameterGroupId(pk);
	}

	public long getParameterGroupId() {
		return _parameterGroupId;
	}

	public void setParameterGroupId(long parameterGroupId) {
		_parameterGroupId = parameterGroupId;
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

	public long getCreatedBy() {
		return _createdBy;
	}

	public void setCreatedBy(long createdBy) {
		_createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return _createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		_createdDate = createdDate;
	}

	public long getModifiedBy() {
		return _modifiedBy;
	}

	public void setModifiedBy(long modifiedBy) {
		_modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public long getParentId() {
		return _parentId;
	}

	public void setParentId(long parentId) {
		_parentId = parentId;
	}

	public String getGroupName() {
		return _groupName;
	}

	public void setGroupName(String groupName) {
		_groupName = groupName;
	}

	public String getGroupCode() {
		return _groupCode;
	}

	public void setGroupCode(String groupCode) {
		_groupCode = groupCode;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public Boolean getDeleted() {
		return _deleted;
	}

	public void setDeleted(Boolean deleted) {
		_deleted = deleted;
	}

	private long _parameterGroupId;
	private long _groupId;
	private long _companyId;
	private long _createdBy;
	private Date _createdDate;
	private long _modifiedBy;
	private Date _modifiedDate;
	private long _parentId;
	private String _groupName;
	private String _groupCode;
	private String _description;
	private Boolean _deleted;

}