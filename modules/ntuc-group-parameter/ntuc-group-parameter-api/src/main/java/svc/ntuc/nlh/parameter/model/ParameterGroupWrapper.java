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
 * This class is a wrapper for {@link ParameterGroup}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ParameterGroup
 * @generated
 */
public class ParameterGroupWrapper
	extends BaseModelWrapper<ParameterGroup>
	implements ModelWrapper<ParameterGroup>, ParameterGroup {

	public ParameterGroupWrapper(ParameterGroup parameterGroup) {
		super(parameterGroup);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("parameterGroupId", getParameterGroupId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("createdBy", getCreatedBy());
		attributes.put("createdDate", getCreatedDate());
		attributes.put("modifiedBy", getModifiedBy());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("parentId", getParentId());
		attributes.put("groupName", getGroupName());
		attributes.put("groupCode", getGroupCode());
		attributes.put("description", getDescription());
		attributes.put("deleted", getDeleted());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long parameterGroupId = (Long)attributes.get("parameterGroupId");

		if (parameterGroupId != null) {
			setParameterGroupId(parameterGroupId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
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

		Long parentId = (Long)attributes.get("parentId");

		if (parentId != null) {
			setParentId(parentId);
		}

		String groupName = (String)attributes.get("groupName");

		if (groupName != null) {
			setGroupName(groupName);
		}

		String groupCode = (String)attributes.get("groupCode");

		if (groupCode != null) {
			setGroupCode(groupCode);
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
	 * Returns the company ID of this parameter group.
	 *
	 * @return the company ID of this parameter group
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the created by of this parameter group.
	 *
	 * @return the created by of this parameter group
	 */
	@Override
	public long getCreatedBy() {
		return model.getCreatedBy();
	}

	/**
	 * Returns the created date of this parameter group.
	 *
	 * @return the created date of this parameter group
	 */
	@Override
	public Date getCreatedDate() {
		return model.getCreatedDate();
	}

	/**
	 * Returns the deleted of this parameter group.
	 *
	 * @return the deleted of this parameter group
	 */
	@Override
	public Boolean getDeleted() {
		return model.getDeleted();
	}

	/**
	 * Returns the description of this parameter group.
	 *
	 * @return the description of this parameter group
	 */
	@Override
	public String getDescription() {
		return model.getDescription();
	}

	/**
	 * Returns the group code of this parameter group.
	 *
	 * @return the group code of this parameter group
	 */
	@Override
	public String getGroupCode() {
		return model.getGroupCode();
	}

	/**
	 * Returns the group ID of this parameter group.
	 *
	 * @return the group ID of this parameter group
	 */
	@Override
	public long getGroupId() {
		return model.getGroupId();
	}

	/**
	 * Returns the group name of this parameter group.
	 *
	 * @return the group name of this parameter group
	 */
	@Override
	public String getGroupName() {
		return model.getGroupName();
	}

	/**
	 * Returns the modified by of this parameter group.
	 *
	 * @return the modified by of this parameter group
	 */
	@Override
	public long getModifiedBy() {
		return model.getModifiedBy();
	}

	/**
	 * Returns the modified date of this parameter group.
	 *
	 * @return the modified date of this parameter group
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the parameter group ID of this parameter group.
	 *
	 * @return the parameter group ID of this parameter group
	 */
	@Override
	public long getParameterGroupId() {
		return model.getParameterGroupId();
	}

	/**
	 * Returns the parent ID of this parameter group.
	 *
	 * @return the parent ID of this parameter group
	 */
	@Override
	public long getParentId() {
		return model.getParentId();
	}

	/**
	 * Returns the primary key of this parameter group.
	 *
	 * @return the primary key of this parameter group
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
	 * Sets the company ID of this parameter group.
	 *
	 * @param companyId the company ID of this parameter group
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the created by of this parameter group.
	 *
	 * @param createdBy the created by of this parameter group
	 */
	@Override
	public void setCreatedBy(long createdBy) {
		model.setCreatedBy(createdBy);
	}

	/**
	 * Sets the created date of this parameter group.
	 *
	 * @param createdDate the created date of this parameter group
	 */
	@Override
	public void setCreatedDate(Date createdDate) {
		model.setCreatedDate(createdDate);
	}

	/**
	 * Sets the deleted of this parameter group.
	 *
	 * @param deleted the deleted of this parameter group
	 */
	@Override
	public void setDeleted(Boolean deleted) {
		model.setDeleted(deleted);
	}

	/**
	 * Sets the description of this parameter group.
	 *
	 * @param description the description of this parameter group
	 */
	@Override
	public void setDescription(String description) {
		model.setDescription(description);
	}

	/**
	 * Sets the group code of this parameter group.
	 *
	 * @param groupCode the group code of this parameter group
	 */
	@Override
	public void setGroupCode(String groupCode) {
		model.setGroupCode(groupCode);
	}

	/**
	 * Sets the group ID of this parameter group.
	 *
	 * @param groupId the group ID of this parameter group
	 */
	@Override
	public void setGroupId(long groupId) {
		model.setGroupId(groupId);
	}

	/**
	 * Sets the group name of this parameter group.
	 *
	 * @param groupName the group name of this parameter group
	 */
	@Override
	public void setGroupName(String groupName) {
		model.setGroupName(groupName);
	}

	/**
	 * Sets the modified by of this parameter group.
	 *
	 * @param modifiedBy the modified by of this parameter group
	 */
	@Override
	public void setModifiedBy(long modifiedBy) {
		model.setModifiedBy(modifiedBy);
	}

	/**
	 * Sets the modified date of this parameter group.
	 *
	 * @param modifiedDate the modified date of this parameter group
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the parameter group ID of this parameter group.
	 *
	 * @param parameterGroupId the parameter group ID of this parameter group
	 */
	@Override
	public void setParameterGroupId(long parameterGroupId) {
		model.setParameterGroupId(parameterGroupId);
	}

	/**
	 * Sets the parent ID of this parameter group.
	 *
	 * @param parentId the parent ID of this parameter group
	 */
	@Override
	public void setParentId(long parentId) {
		model.setParentId(parentId);
	}

	/**
	 * Sets the primary key of this parameter group.
	 *
	 * @param primaryKey the primary key of this parameter group
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	@Override
	protected ParameterGroupWrapper wrap(ParameterGroup parameterGroup) {
		return new ParameterGroupWrapper(parameterGroup);
	}

}