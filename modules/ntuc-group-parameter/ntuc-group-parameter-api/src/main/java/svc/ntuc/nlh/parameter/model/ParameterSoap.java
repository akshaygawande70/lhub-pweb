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
public class ParameterSoap implements Serializable {

	public static ParameterSoap toSoapModel(Parameter model) {
		ParameterSoap soapModel = new ParameterSoap();

		soapModel.setParameterId(model.getParameterId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setParameterGroupId(model.getParameterGroupId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setCreatedBy(model.getCreatedBy());
		soapModel.setCreatedDate(model.getCreatedDate());
		soapModel.setModifiedBy(model.getModifiedBy());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setParamCode(model.getParamCode());
		soapModel.setParamName(model.getParamName());
		soapModel.setParamValue(model.getParamValue());
		soapModel.setDescription(model.getDescription());
		soapModel.setDeleted(model.getDeleted());

		return soapModel;
	}

	public static ParameterSoap[] toSoapModels(Parameter[] models) {
		ParameterSoap[] soapModels = new ParameterSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static ParameterSoap[][] toSoapModels(Parameter[][] models) {
		ParameterSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new ParameterSoap[models.length][models[0].length];
		}
		else {
			soapModels = new ParameterSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static ParameterSoap[] toSoapModels(List<Parameter> models) {
		List<ParameterSoap> soapModels = new ArrayList<ParameterSoap>(
			models.size());

		for (Parameter model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new ParameterSoap[soapModels.size()]);
	}

	public ParameterSoap() {
	}

	public long getPrimaryKey() {
		return _parameterId;
	}

	public void setPrimaryKey(long pk) {
		setParameterId(pk);
	}

	public long getParameterId() {
		return _parameterId;
	}

	public void setParameterId(long parameterId) {
		_parameterId = parameterId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public long getParameterGroupId() {
		return _parameterGroupId;
	}

	public void setParameterGroupId(long parameterGroupId) {
		_parameterGroupId = parameterGroupId;
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

	public String getParamCode() {
		return _paramCode;
	}

	public void setParamCode(String paramCode) {
		_paramCode = paramCode;
	}

	public String getParamName() {
		return _paramName;
	}

	public void setParamName(String paramName) {
		_paramName = paramName;
	}

	public String getParamValue() {
		return _paramValue;
	}

	public void setParamValue(String paramValue) {
		_paramValue = paramValue;
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

	private long _parameterId;
	private long _groupId;
	private long _parameterGroupId;
	private long _companyId;
	private long _createdBy;
	private Date _createdDate;
	private long _modifiedBy;
	private Date _modifiedDate;
	private String _paramCode;
	private String _paramName;
	private String _paramValue;
	private String _description;
	private Boolean _deleted;

}