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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link svc.ntuc.nlh.comments.service.http.CommentServiceSoap}.
 *
 * @author Brian Wing Shun Chan
 * @deprecated As of Athanasius (7.3.x), with no direct replacement
 * @generated
 */
@Deprecated
public class CommentSoap implements Serializable {

	public static CommentSoap toSoapModel(Comment model) {
		CommentSoap soapModel = new CommentSoap();

		soapModel.setUuid(model.getUuid());
		soapModel.setCommentId(model.getCommentId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setCommentText(model.getCommentText());
		soapModel.setCommentName(model.getCommentName());
		soapModel.setCommentEmail(model.getCommentEmail());
		soapModel.setCommentOption(model.isCommentOption());
		soapModel.setCommentStatus(model.isCommentStatus());

		return soapModel;
	}

	public static CommentSoap[] toSoapModels(Comment[] models) {
		CommentSoap[] soapModels = new CommentSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static CommentSoap[][] toSoapModels(Comment[][] models) {
		CommentSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new CommentSoap[models.length][models[0].length];
		}
		else {
			soapModels = new CommentSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static CommentSoap[] toSoapModels(List<Comment> models) {
		List<CommentSoap> soapModels = new ArrayList<CommentSoap>(
			models.size());

		for (Comment model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new CommentSoap[soapModels.size()]);
	}

	public CommentSoap() {
	}

	public long getPrimaryKey() {
		return _commentId;
	}

	public void setPrimaryKey(long pk) {
		setCommentId(pk);
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getCommentId() {
		return _commentId;
	}

	public void setCommentId(long commentId) {
		_commentId = commentId;
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

	public String getCommentText() {
		return _commentText;
	}

	public void setCommentText(String commentText) {
		_commentText = commentText;
	}

	public String getCommentName() {
		return _commentName;
	}

	public void setCommentName(String commentName) {
		_commentName = commentName;
	}

	public String getCommentEmail() {
		return _commentEmail;
	}

	public void setCommentEmail(String commentEmail) {
		_commentEmail = commentEmail;
	}

	public boolean getCommentOption() {
		return _commentOption;
	}

	public boolean isCommentOption() {
		return _commentOption;
	}

	public void setCommentOption(boolean commentOption) {
		_commentOption = commentOption;
	}

	public boolean getCommentStatus() {
		return _commentStatus;
	}

	public boolean isCommentStatus() {
		return _commentStatus;
	}

	public void setCommentStatus(boolean commentStatus) {
		_commentStatus = commentStatus;
	}

	private String _uuid;
	private long _commentId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private String _commentText;
	private String _commentName;
	private String _commentEmail;
	private boolean _commentOption;
	private boolean _commentStatus;

}