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

package svc.ntuc.nlh.comments.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

import svc.ntuc.nlh.comments.model.Comment;

/**
 * The cache model class for representing Comment in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class CommentCacheModel implements CacheModel<Comment>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof CommentCacheModel)) {
			return false;
		}

		CommentCacheModel commentCacheModel = (CommentCacheModel)object;

		if (commentId == commentCacheModel.commentId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, commentId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(27);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", commentId=");
		sb.append(commentId);
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
		sb.append(", commentText=");
		sb.append(commentText);
		sb.append(", commentName=");
		sb.append(commentName);
		sb.append(", commentEmail=");
		sb.append(commentEmail);
		sb.append(", commentOption=");
		sb.append(commentOption);
		sb.append(", commentStatus=");
		sb.append(commentStatus);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public Comment toEntityModel() {
		CommentImpl commentImpl = new CommentImpl();

		if (uuid == null) {
			commentImpl.setUuid("");
		}
		else {
			commentImpl.setUuid(uuid);
		}

		commentImpl.setCommentId(commentId);
		commentImpl.setGroupId(groupId);
		commentImpl.setCompanyId(companyId);
		commentImpl.setUserId(userId);

		if (userName == null) {
			commentImpl.setUserName("");
		}
		else {
			commentImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			commentImpl.setCreateDate(null);
		}
		else {
			commentImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			commentImpl.setModifiedDate(null);
		}
		else {
			commentImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (commentText == null) {
			commentImpl.setCommentText("");
		}
		else {
			commentImpl.setCommentText(commentText);
		}

		if (commentName == null) {
			commentImpl.setCommentName("");
		}
		else {
			commentImpl.setCommentName(commentName);
		}

		if (commentEmail == null) {
			commentImpl.setCommentEmail("");
		}
		else {
			commentImpl.setCommentEmail(commentEmail);
		}

		commentImpl.setCommentOption(commentOption);
		commentImpl.setCommentStatus(commentStatus);

		commentImpl.resetOriginalValues();

		return commentImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		uuid = objectInput.readUTF();

		commentId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		commentText = objectInput.readUTF();
		commentName = objectInput.readUTF();
		commentEmail = objectInput.readUTF();

		commentOption = objectInput.readBoolean();

		commentStatus = objectInput.readBoolean();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(commentId);

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

		if (commentText == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(commentText);
		}

		if (commentName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(commentName);
		}

		if (commentEmail == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(commentEmail);
		}

		objectOutput.writeBoolean(commentOption);

		objectOutput.writeBoolean(commentStatus);
	}

	public String uuid;
	public long commentId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public String commentText;
	public String commentName;
	public String commentEmail;
	public boolean commentOption;
	public boolean commentStatus;

}