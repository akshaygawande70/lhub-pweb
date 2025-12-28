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

package svc.ntuc.nlh.course.admin.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

import svc.ntuc.nlh.course.admin.model.Course;

/**
 * The cache model class for representing Course in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class CourseCacheModel implements CacheModel<Course>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof CourseCacheModel)) {
			return false;
		}

		CourseCacheModel courseCacheModel = (CourseCacheModel)object;

		if (courseId == courseCacheModel.courseId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, courseId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(55);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", courseId=");
		sb.append(courseId);
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
		sb.append(", endDate=");
		sb.append(endDate);
		sb.append(", venue=");
		sb.append(venue);
		sb.append(", allowOnlinePayment=");
		sb.append(allowOnlinePayment);
		sb.append(", courseTitle=");
		sb.append(courseTitle);
		sb.append(", allowWebRegistration=");
		sb.append(allowWebRegistration);
		sb.append(", description=");
		sb.append(description);
		sb.append(", availability=");
		sb.append(availability);
		sb.append(", batchId=");
		sb.append(batchId);
		sb.append(", webExpiry=");
		sb.append(webExpiry);
		sb.append(", fundedCourseFlag=");
		sb.append(fundedCourseFlag);
		sb.append(", courseCode=");
		sb.append(courseCode);
		sb.append(", courseDuration=");
		sb.append(courseDuration);
		sb.append(", courseType=");
		sb.append(courseType);
		sb.append(", courseFee=");
		sb.append(courseFee);
		sb.append(", deleted=");
		sb.append(deleted);
		sb.append(", status=");
		sb.append(status);
		sb.append(", startDate=");
		sb.append(startDate);
		sb.append(", updated=");
		sb.append(updated);
		sb.append(", popular=");
		sb.append(popular);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public Course toEntityModel() {
		CourseImpl courseImpl = new CourseImpl();

		if (uuid == null) {
			courseImpl.setUuid("");
		}
		else {
			courseImpl.setUuid(uuid);
		}

		courseImpl.setCourseId(courseId);
		courseImpl.setGroupId(groupId);
		courseImpl.setCompanyId(companyId);
		courseImpl.setUserId(userId);

		if (userName == null) {
			courseImpl.setUserName("");
		}
		else {
			courseImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			courseImpl.setCreateDate(null);
		}
		else {
			courseImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			courseImpl.setModifiedDate(null);
		}
		else {
			courseImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (endDate == Long.MIN_VALUE) {
			courseImpl.setEndDate(null);
		}
		else {
			courseImpl.setEndDate(new Date(endDate));
		}

		if (venue == null) {
			courseImpl.setVenue("");
		}
		else {
			courseImpl.setVenue(venue);
		}

		courseImpl.setAllowOnlinePayment(allowOnlinePayment);

		if (courseTitle == null) {
			courseImpl.setCourseTitle("");
		}
		else {
			courseImpl.setCourseTitle(courseTitle);
		}

		courseImpl.setAllowWebRegistration(allowWebRegistration);

		if (description == null) {
			courseImpl.setDescription("");
		}
		else {
			courseImpl.setDescription(description);
		}

		courseImpl.setAvailability(availability);

		if (batchId == null) {
			courseImpl.setBatchId("");
		}
		else {
			courseImpl.setBatchId(batchId);
		}

		if (webExpiry == Long.MIN_VALUE) {
			courseImpl.setWebExpiry(null);
		}
		else {
			courseImpl.setWebExpiry(new Date(webExpiry));
		}

		courseImpl.setFundedCourseFlag(fundedCourseFlag);

		if (courseCode == null) {
			courseImpl.setCourseCode("");
		}
		else {
			courseImpl.setCourseCode(courseCode);
		}

		courseImpl.setCourseDuration(courseDuration);

		if (courseType == null) {
			courseImpl.setCourseType("");
		}
		else {
			courseImpl.setCourseType(courseType);
		}

		courseImpl.setCourseFee(courseFee);
		courseImpl.setDeleted(deleted);

		if (status == null) {
			courseImpl.setStatus("");
		}
		else {
			courseImpl.setStatus(status);
		}

		if (startDate == Long.MIN_VALUE) {
			courseImpl.setStartDate(null);
		}
		else {
			courseImpl.setStartDate(new Date(startDate));
		}

		courseImpl.setUpdated(updated);
		courseImpl.setPopular(popular);

		courseImpl.resetOriginalValues();

		return courseImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		uuid = objectInput.readUTF();

		courseId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		endDate = objectInput.readLong();
		venue = objectInput.readUTF();

		allowOnlinePayment = objectInput.readBoolean();
		courseTitle = objectInput.readUTF();

		allowWebRegistration = objectInput.readBoolean();
		description = objectInput.readUTF();

		availability = objectInput.readInt();
		batchId = objectInput.readUTF();
		webExpiry = objectInput.readLong();

		fundedCourseFlag = objectInput.readBoolean();
		courseCode = objectInput.readUTF();

		courseDuration = objectInput.readDouble();
		courseType = objectInput.readUTF();

		courseFee = objectInput.readDouble();

		deleted = objectInput.readBoolean();
		status = objectInput.readUTF();
		startDate = objectInput.readLong();

		updated = objectInput.readBoolean();

		popular = objectInput.readBoolean();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(courseId);

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
		objectOutput.writeLong(endDate);

		if (venue == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(venue);
		}

		objectOutput.writeBoolean(allowOnlinePayment);

		if (courseTitle == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(courseTitle);
		}

		objectOutput.writeBoolean(allowWebRegistration);

		if (description == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(description);
		}

		objectOutput.writeInt(availability);

		if (batchId == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(batchId);
		}

		objectOutput.writeLong(webExpiry);

		objectOutput.writeBoolean(fundedCourseFlag);

		if (courseCode == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(courseCode);
		}

		objectOutput.writeDouble(courseDuration);

		if (courseType == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(courseType);
		}

		objectOutput.writeDouble(courseFee);

		objectOutput.writeBoolean(deleted);

		if (status == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(status);
		}

		objectOutput.writeLong(startDate);

		objectOutput.writeBoolean(updated);

		objectOutput.writeBoolean(popular);
	}

	public String uuid;
	public long courseId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public long endDate;
	public String venue;
	public boolean allowOnlinePayment;
	public String courseTitle;
	public boolean allowWebRegistration;
	public String description;
	public int availability;
	public String batchId;
	public long webExpiry;
	public boolean fundedCourseFlag;
	public String courseCode;
	public double courseDuration;
	public String courseType;
	public double courseFee;
	public boolean deleted;
	public String status;
	public long startDate;
	public boolean updated;
	public boolean popular;

}