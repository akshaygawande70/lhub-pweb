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

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link Parameter}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Parameter
 * @generated
 */
public class ParameterWrapper
	extends BaseModelWrapper<Parameter>
	implements ModelWrapper<Parameter>, Parameter {

	public ParameterWrapper(Parameter parameter) {
		super(parameter);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("parameterId", getParameterId());
		attributes.put("groupId", getGroupId());
		attributes.put("parameterGroupId", getParameterGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("createdBy", getCreatedBy());
		attributes.put("createdDate", getCreatedDate());
		attributes.put("modifiedBy", getModifiedBy());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("paramCode", getParamCode());
		attributes.put("paramName", getParamName());
		attributes.put("paramValue", getParamValue());
		attributes.put("description", getDescription());
		attributes.put("deleted", getDeleted());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long parameterId = (Long)attributes.get("parameterId");

		if (parameterId != null) {
			setParameterId(parameterId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long parameterGroupId = (Long)attributes.get("parameterGroupId");

		if (parameterGroupId != null) {
			setParameterGroupId(parameterGroupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long createdBy = (Long)attributes.get("createdBy");

		if (createdBy != null) {
			setCreatedBy(createdBy);
		}

		Date createdDate = (Date)attributes.get("createdDate");

		if (createdDate != null) {
			setCreatedDate(createdDate);
		}

		Long modifiedBy = (Long)attributes.get("modifiedBy");

		if (modifiedBy != null) {
			setModifiedBy(modifiedBy);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String paramCode = (String)attributes.get("paramCode");

		if (paramCode != null) {
			setParamCode(paramCode);
		}

		String paramName = (String)attributes.get("paramName");

		if (paramName != null) {
			setParamName(paramName);
		}

		String paramValue = (String)attributes.get("paramValue");

		if (paramValue != null) {
			setParamValue(paramValue);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		Boolean deleted = (Boolean)attributes.get("deleted");

		if (deleted != null) {
			setDeleted(deleted);
		}
	}

	/**
	 * Returns the company ID of this parameter.
	 *
	 * @return the company ID of this parameter
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the created by of this parameter.
	 *
	 * @return the created by of this parameter
	 */
	@Override
	public long getCreatedBy() {
		return model.getCreatedBy();
	}

	/**
	 * Returns the created date of this parameter.
	 *
	 * @return the created date of this parameter
	 */
	@Override
	public Date getCreatedDate() {
		return model.getCreatedDate();
	}

	/**
	 * Returns the deleted of this parameter.
	 *
	 * @return the deleted of this parameter
	 */
	@Override
	public Boolean getDeleted() {
		return model.getDeleted();
	}

	/**
	 * Returns the description of this parameter.
	 *
	 * @return the description of this parameter
	 */
	@Override
	public String getDescription() {
		return model.getDescription();
	}

	/**
	 * Returns the group ID of this parameter.
	 *
	 * @return the group ID of this parameter
	 */
	@Override
	public long getGroupId() {
		return model.getGroupId();
	}

	/**
	 * Returns the modified by of this parameter.
	 *
	 * @return the modified by of this parameter
	 */
	@Override
	public long getModifiedBy() {
		return model.getModifiedBy();
	}

	/**
	 * Returns the modified date of this parameter.
	 *
	 * @return the modified date of this parameter
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the param code of this parameter.
	 *
	 * @return the param code of this parameter
	 */
	@Override
	public String getParamCode() {
		return model.getParamCode();
	}

	/**
	 * Returns the parameter group ID of this parameter.
	 *
	 * @return the parameter group ID of this parameter
	 */
	@Override
	public long getParameterGroupId() {
		return model.getParameterGroupId();
	}

	/**
	 * Returns the parameter ID of this parameter.
	 *
	 * @return the parameter ID of this parameter
	 */
	@Override
	public long getParameterId() {
		return model.getParameterId();
	}

	/**
	 * Returns the param name of this parameter.
	 *
	 * @return the param name of this parameter
	 */
	@Override
	public String getParamName() {
		return model.getParamName();
	}

	/**
	 * Returns the param value of this parameter.
	 *
	 * @return the param value of this parameter
	 */
	@Override
	public String getParamValue() {
		return model.getParamValue();
	}

	/**
	 * Returns the primary key of this parameter.
	 *
	 * @return the primary key of this parameter
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the company ID of this parameter.
	 *
	 * @param companyId the company ID of this parameter
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the created by of this parameter.
	 *
	 * @param createdBy the created by of this parameter
	 */
	@Override
	public void setCreatedBy(long createdBy) {
		model.setCreatedBy(createdBy);
	}

	/**
	 * Sets the created date of this parameter.
	 *
	 * @param createdDate the created date of this parameter
	 */
	@Override
	public void setCreatedDate(Date createdDate) {
		model.setCreatedDate(createdDate);
	}

	/**
	 * Sets the deleted of this parameter.
	 *
	 * @param deleted the deleted of this parameter
	 */
	@Override
	public void setDeleted(Boolean deleted) {
		model.setDeleted(deleted);
	}

	/**
	 * Sets the description of this parameter.
	 *
	 * @param description the description of this parameter
	 */
	@Override
	public void setDescription(String description) {
		model.setDescription(description);
	}

	/**
	 * Sets the group ID of this parameter.
	 *
	 * @param groupId the group ID of this parameter
	 */
	@Override
	public void setGroupId(long groupId) {
		model.setGroupId(groupId);
	}

	/**
	 * Sets the modified by of this parameter.
	 *
	 * @param modifiedBy the modified by of this parameter
	 */
	@Override
	public void setModifiedBy(long modifiedBy) {
		model.setModifiedBy(modifiedBy);
	}

	/**
	 * Sets the modified date of this parameter.
	 *
	 * @param modifiedDate the modified date of this parameter
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the param code of this parameter.
	 *
	 * @param paramCode the param code of this parameter
	 */
	@Override
	public void setParamCode(String paramCode) {
		model.setParamCode(paramCode);
	}

	/**
	 * Sets the parameter group ID of this parameter.
	 *
	 * @param parameterGroupId the parameter group ID of this parameter
	 */
	@Override
	public void setParameterGroupId(long parameterGroupId) {
		model.setParameterGroupId(parameterGroupId);
	}

	/**
	 * Sets the parameter ID of this parameter.
	 *
	 * @param parameterId the parameter ID of this parameter
	 */
	@Override
	public void setParameterId(long parameterId) {
		model.setParameterId(parameterId);
	}

	/**
	 * Sets the param name of this parameter.
	 *
	 * @param paramName the param name of this parameter
	 */
	@Override
	public void setParamName(String paramName) {
		model.setParamName(paramName);
	}

	/**
	 * Sets the param value of this parameter.
	 *
	 * @param paramValue the param value of this parameter
	 */
	@Override
	public void setParamValue(String paramValue) {
		model.setParamValue(paramValue);
	}

	/**
	 * Sets the primary key of this parameter.
	 *
	 * @param primaryKey the primary key of this parameter
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	@Override
	protected ParameterWrapper wrap(Parameter parameter) {
		return new ParameterWrapper(parameter);
	}

}