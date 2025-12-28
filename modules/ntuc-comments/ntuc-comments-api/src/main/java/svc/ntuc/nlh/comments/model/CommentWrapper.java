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

package svc.ntuc.nlh.comments.model;

import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link Comment}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Comment
 * @generated
 */
public class CommentWrapper
	extends BaseModelWrapper<Comment>
	implements Comment, ModelWrapper<Comment> {

	public CommentWrapper(Comment comment) {
		super(comment);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("commentId", getCommentId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("commentText", getCommentText());
		attributes.put("commentName", getCommentName());
		attributes.put("commentEmail", getCommentEmail());
		attributes.put("commentOption", isCommentOption());
		attributes.put("commentStatus", isCommentStatus());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long commentId = (Long)attributes.get("commentId");

		if (commentId != null) {
			setCommentId(commentId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String commentText = (String)attributes.get("commentText");

		if (commentText != null) {
			setCommentText(commentText);
		}

		String commentName = (String)attributes.get("commentName");

		if (commentName != null) {
			setCommentName(commentName);
		}

		String commentEmail = (String)attributes.get("commentEmail");

		if (commentEmail != null) {
			setCommentEmail(commentEmail);
		}

		Boolean commentOption = (Boolean)attributes.get("commentOption");

		if (commentOption != null) {
			setCommentOption(commentOption);
		}

		Boolean commentStatus = (Boolean)attributes.get("commentStatus");

		if (commentStatus != null) {
			setCommentStatus(commentStatus);
		}
	}

	/**
	 * Returns the comment email of this comment.
	 *
	 * @return the comment email of this comment
	 */
	@Override
	public String getCommentEmail() {
		return model.getCommentEmail();
	}

	/**
	 * Returns the comment ID of this comment.
	 *
	 * @return the comment ID of this comment
	 */
	@Override
	public long getCommentId() {
		return model.getCommentId();
	}

	/**
	 * Returns the comment name of this comment.
	 *
	 * @return the comment name of this comment
	 */
	@Override
	public String getCommentName() {
		return model.getCommentName();
	}

	/**
	 * Returns the comment option of this comment.
	 *
	 * @return the comment option of this comment
	 */
	@Override
	public boolean getCommentOption() {
		return model.getCommentOption();
	}

	/**
	 * Returns the comment status of this comment.
	 *
	 * @return the comment status of this comment
	 */
	@Override
	public boolean getCommentStatus() {
		return model.getCommentStatus();
	}

	/**
	 * Returns the comment text of this comment.
	 *
	 * @return the comment text of this comment
	 */
	@Override
	public String getCommentText() {
		return model.getCommentText();
	}

	/**
	 * Returns the company ID of this comment.
	 *
	 * @return the company ID of this comment
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the create date of this comment.
	 *
	 * @return the create date of this comment
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the group ID of this comment.
	 *
	 * @return the group ID of this comment
	 */
	@Override
	public long getGroupId() {
		return model.getGroupId();
	}

	/**
	 * Returns the modified date of this comment.
	 *
	 * @return the modified date of this comment
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the primary key of this comment.
	 *
	 * @return the primary key of this comment
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the user ID of this comment.
	 *
	 * @return the user ID of this comment
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user name of this comment.
	 *
	 * @return the user name of this comment
	 */
	@Override
	public String getUserName() {
		return model.getUserName();
	}

	/**
	 * Returns the user uuid of this comment.
	 *
	 * @return the user uuid of this comment
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	/**
	 * Returns the uuid of this comment.
	 *
	 * @return the uuid of this comment
	 */
	@Override
	public String getUuid() {
		return model.getUuid();
	}

	/**
	 * Returns <code>true</code> if this comment is comment option.
	 *
	 * @return <code>true</code> if this comment is comment option; <code>false</code> otherwise
	 */
	@Override
	public boolean isCommentOption() {
		return model.isCommentOption();
	}

	/**
	 * Returns <code>true</code> if this comment is comment status.
	 *
	 * @return <code>true</code> if this comment is comment status; <code>false</code> otherwise
	 */
	@Override
	public boolean isCommentStatus() {
		return model.isCommentStatus();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the comment email of this comment.
	 *
	 * @param commentEmail the comment email of this comment
	 */
	@Override
	public void setCommentEmail(String commentEmail) {
		model.setCommentEmail(commentEmail);
	}

	/**
	 * Sets the comment ID of this comment.
	 *
	 * @param commentId the comment ID of this comment
	 */
	@Override
	public void setCommentId(long commentId) {
		model.setCommentId(commentId);
	}

	/**
	 * Sets the comment name of this comment.
	 *
	 * @param commentName the comment name of this comment
	 */
	@Override
	public void setCommentName(String commentName) {
		model.setCommentName(commentName);
	}

	/**
	 * Sets whether this comment is comment option.
	 *
	 * @param commentOption the comment option of this comment
	 */
	@Override
	public void setCommentOption(boolean commentOption) {
		model.setCommentOption(commentOption);
	}

	/**
	 * Sets whether this comment is comment status.
	 *
	 * @param commentStatus the comment status of this comment
	 */
	@Override
	public void setCommentStatus(boolean commentStatus) {
		model.setCommentStatus(commentStatus);
	}

	/**
	 * Sets the comment text of this comment.
	 *
	 * @param commentText the comment text of this comment
	 */
	@Override
	public void setCommentText(String commentText) {
		model.setCommentText(commentText);
	}

	/**
	 * Sets the company ID of this comment.
	 *
	 * @param companyId the company ID of this comment
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this comment.
	 *
	 * @param createDate the create date of this comment
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the group ID of this comment.
	 *
	 * @param groupId the group ID of this comment
	 */
	@Override
	public void setGroupId(long groupId) {
		model.setGroupId(groupId);
	}

	/**
	 * Sets the modified date of this comment.
	 *
	 * @param modifiedDate the modified date of this comment
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the primary key of this comment.
	 *
	 * @param primaryKey the primary key of this comment
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the user ID of this comment.
	 *
	 * @param userId the user ID of this comment
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user name of this comment.
	 *
	 * @param userName the user name of this comment
	 */
	@Override
	public void setUserName(String userName) {
		model.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this comment.
	 *
	 * @param userUuid the user uuid of this comment
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	/**
	 * Sets the uuid of this comment.
	 *
	 * @param uuid the uuid of this comment
	 */
	@Override
	public void setUuid(String uuid) {
		model.setUuid(uuid);
	}

	@Override
	public StagedModelType getStagedModelType() {
		return model.getStagedModelType();
	}

	@Override
	protected CommentWrapper wrap(Comment comment) {
		return new CommentWrapper(comment);
	}

}