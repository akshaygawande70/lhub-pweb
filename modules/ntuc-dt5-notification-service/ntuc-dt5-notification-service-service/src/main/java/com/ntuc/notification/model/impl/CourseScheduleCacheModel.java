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

package com.ntuc.notification.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;

import com.ntuc.notification.model.CourseSchedule;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing CourseSchedule in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class CourseScheduleCacheModel
	implements CacheModel<CourseSchedule>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof CourseScheduleCacheModel)) {
			return false;
		}

		CourseScheduleCacheModel courseScheduleCacheModel =
			(CourseScheduleCacheModel)object;

		if (courseScheduleId == courseScheduleCacheModel.courseScheduleId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, courseScheduleId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(51);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", courseScheduleId=");
		sb.append(courseScheduleId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", courseCode=");
		sb.append(courseCode);
		sb.append(", intakeNumber=");
		sb.append(intakeNumber);
		sb.append(", startDate=");
		sb.append(startDate);
		sb.append(", endDate=");
		sb.append(endDate);
		sb.append(", durationHours=");
		sb.append(durationHours);
		sb.append(", durationMinutes=");
		sb.append(durationMinutes);
		sb.append(", availability=");
		sb.append(availability);
		sb.append(", venue=");
		sb.append(venue);
		sb.append(", description=");
		sb.append(description);
		sb.append(", availablePax=");
		sb.append(availablePax);
		sb.append(", availableWaitlist=");
		sb.append(availableWaitlist);
		sb.append(", lxpBuyUrl=");
		sb.append(lxpBuyUrl);
		sb.append(", lxpRoiUrl=");
		sb.append(lxpRoiUrl);
		sb.append(", importantNote=");
		sb.append(importantNote);
		sb.append(", scheduleDownloadUrl=");
		sb.append(scheduleDownloadUrl);
		sb.append(", errorCode=");
		sb.append(errorCode);
		sb.append(", errorMessage=");
		sb.append(errorMessage);
		sb.append(", rawJson=");
		sb.append(rawJson);
		sb.append(", modifiedBy=");
		sb.append(modifiedBy);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public CourseSchedule toEntityModel() {
		CourseScheduleImpl courseScheduleImpl = new CourseScheduleImpl();

		if (uuid == null) {
			courseScheduleImpl.setUuid("");
		}
		else {
			courseScheduleImpl.setUuid(uuid);
		}

		courseScheduleImpl.setCourseScheduleId(courseScheduleId);
		courseScheduleImpl.setGroupId(groupId);
		courseScheduleImpl.setCompanyId(companyId);

		if (createDate == Long.MIN_VALUE) {
			courseScheduleImpl.setCreateDate(null);
		}
		else {
			courseScheduleImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			courseScheduleImpl.setModifiedDate(null);
		}
		else {
			courseScheduleImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (courseCode == null) {
			courseScheduleImpl.setCourseCode("");
		}
		else {
			courseScheduleImpl.setCourseCode(courseCode);
		}

		if (intakeNumber == null) {
			courseScheduleImpl.setIntakeNumber("");
		}
		else {
			courseScheduleImpl.setIntakeNumber(intakeNumber);
		}

		if (startDate == Long.MIN_VALUE) {
			courseScheduleImpl.setStartDate(null);
		}
		else {
			courseScheduleImpl.setStartDate(new Date(startDate));
		}

		if (endDate == Long.MIN_VALUE) {
			courseScheduleImpl.setEndDate(null);
		}
		else {
			courseScheduleImpl.setEndDate(new Date(endDate));
		}

		courseScheduleImpl.setDurationHours(durationHours);
		courseScheduleImpl.setDurationMinutes(durationMinutes);
		courseScheduleImpl.setAvailability(availability);

		if (venue == null) {
			courseScheduleImpl.setVenue("");
		}
		else {
			courseScheduleImpl.setVenue(venue);
		}

		if (description == null) {
			courseScheduleImpl.setDescription("");
		}
		else {
			courseScheduleImpl.setDescription(description);
		}

		if (availablePax == null) {
			courseScheduleImpl.setAvailablePax("");
		}
		else {
			courseScheduleImpl.setAvailablePax(availablePax);
		}

		if (availableWaitlist == null) {
			courseScheduleImpl.setAvailableWaitlist("");
		}
		else {
			courseScheduleImpl.setAvailableWaitlist(availableWaitlist);
		}

		if (lxpBuyUrl == null) {
			courseScheduleImpl.setLxpBuyUrl("");
		}
		else {
			courseScheduleImpl.setLxpBuyUrl(lxpBuyUrl);
		}

		if (lxpRoiUrl == null) {
			courseScheduleImpl.setLxpRoiUrl("");
		}
		else {
			courseScheduleImpl.setLxpRoiUrl(lxpRoiUrl);
		}

		if (importantNote == null) {
			courseScheduleImpl.setImportantNote("");
		}
		else {
			courseScheduleImpl.setImportantNote(importantNote);
		}

		if (scheduleDownloadUrl == null) {
			courseScheduleImpl.setScheduleDownloadUrl("");
		}
		else {
			courseScheduleImpl.setScheduleDownloadUrl(scheduleDownloadUrl);
		}

		if (errorCode == null) {
			courseScheduleImpl.setErrorCode("");
		}
		else {
			courseScheduleImpl.setErrorCode(errorCode);
		}

		if (errorMessage == null) {
			courseScheduleImpl.setErrorMessage("");
		}
		else {
			courseScheduleImpl.setErrorMessage(errorMessage);
		}

		if (rawJson == null) {
			courseScheduleImpl.setRawJson("");
		}
		else {
			courseScheduleImpl.setRawJson(rawJson);
		}

		courseScheduleImpl.setModifiedBy(modifiedBy);

		courseScheduleImpl.resetOriginalValues();

		return courseScheduleImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		uuid = objectInput.readUTF();

		courseScheduleId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		courseCode = objectInput.readUTF();
		intakeNumber = objectInput.readUTF();
		startDate = objectInput.readLong();
		endDate = objectInput.readLong();

		durationHours = objectInput.readInt();

		durationMinutes = objectInput.readInt();

		availability = objectInput.readInt();
		venue = objectInput.readUTF();
		description = objectInput.readUTF();
		availablePax = objectInput.readUTF();
		availableWaitlist = objectInput.readUTF();
		lxpBuyUrl = objectInput.readUTF();
		lxpRoiUrl = objectInput.readUTF();
		importantNote = objectInput.readUTF();
		scheduleDownloadUrl = objectInput.readUTF();
		errorCode = objectInput.readUTF();
		errorMessage = objectInput.readUTF();
		rawJson = objectInput.readUTF();

		modifiedBy = objectInput.readLong();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(courseScheduleId);

		objectOutput.writeLong(groupId);

		objectOutput.writeLong(companyId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		if (courseCode == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(courseCode);
		}

		if (intakeNumber == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(intakeNumber);
		}

		objectOutput.writeLong(startDate);
		objectOutput.writeLong(endDate);

		objectOutput.writeInt(durationHours);

		objectOutput.writeInt(durationMinutes);

		objectOutput.writeInt(availability);

		if (venue == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(venue);
		}

		if (description == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(description);
		}

		if (availablePax == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(availablePax);
		}

		if (availableWaitlist == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(availableWaitlist);
		}

		if (lxpBuyUrl == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(lxpBuyUrl);
		}

		if (lxpRoiUrl == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(lxpRoiUrl);
		}

		if (importantNote == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(importantNote);
		}

		if (scheduleDownloadUrl == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(scheduleDownloadUrl);
		}

		if (errorCode == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(errorCode);
		}

		if (errorMessage == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(errorMessage);
		}

		if (rawJson == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(rawJson);
		}

		objectOutput.writeLong(modifiedBy);
	}

	public String uuid;
	public long courseScheduleId;
	public long groupId;
	public long companyId;
	public long createDate;
	public long modifiedDate;
	public String courseCode;
	public String intakeNumber;
	public long startDate;
	public long endDate;
	public int durationHours;
	public int durationMinutes;
	public int availability;
	public String venue;
	public String description;
	public String availablePax;
	public String availableWaitlist;
	public String lxpBuyUrl;
	public String lxpRoiUrl;
	public String importantNote;
	public String scheduleDownloadUrl;
	public String errorCode;
	public String errorMessage;
	public String rawJson;
	public long modifiedBy;

}