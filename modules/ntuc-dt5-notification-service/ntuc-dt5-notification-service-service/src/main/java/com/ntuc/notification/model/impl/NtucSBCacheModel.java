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

import com.ntuc.notification.model.NtucSB;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing NtucSB in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class NtucSBCacheModel implements CacheModel<NtucSB>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof NtucSBCacheModel)) {
			return false;
		}

		NtucSBCacheModel ntucSBCacheModel = (NtucSBCacheModel)object;

		if (ntucDTId == ntucSBCacheModel.ntucDTId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, ntucDTId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(37);

		sb.append("{ntucDTId=");
		sb.append(ntucDTId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", courseCode=");
		sb.append(courseCode);
		sb.append(", courseType=");
		sb.append(courseType);
		sb.append(", notificationDate=");
		sb.append(notificationDate);
		sb.append(", systemDate=");
		sb.append(systemDate);
		sb.append(", notificationId=");
		sb.append(notificationId);
		sb.append(", event=");
		sb.append(event);
		sb.append(", changeFrom=");
		sb.append(changeFrom);
		sb.append(", processingStatus=");
		sb.append(processingStatus);
		sb.append(", courseStatus=");
		sb.append(courseStatus);
		sb.append(", isCriticalProcessed=");
		sb.append(isCriticalProcessed);
		sb.append(", isNonCriticalProcessed=");
		sb.append(isNonCriticalProcessed);
		sb.append(", isCronProcessed=");
		sb.append(isCronProcessed);
		sb.append(", canRetry=");
		sb.append(canRetry);
		sb.append(", lastRetried=");
		sb.append(lastRetried);
		sb.append(", totalRetries=");
		sb.append(totalRetries);
		sb.append(", isRowLockFailed=");
		sb.append(isRowLockFailed);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public NtucSB toEntityModel() {
		NtucSBImpl ntucSBImpl = new NtucSBImpl();

		ntucSBImpl.setNtucDTId(ntucDTId);
		ntucSBImpl.setCompanyId(companyId);

		if (courseCode == null) {
			ntucSBImpl.setCourseCode("");
		}
		else {
			ntucSBImpl.setCourseCode(courseCode);
		}

		if (courseType == null) {
			ntucSBImpl.setCourseType("");
		}
		else {
			ntucSBImpl.setCourseType(courseType);
		}

		if (notificationDate == Long.MIN_VALUE) {
			ntucSBImpl.setNotificationDate(null);
		}
		else {
			ntucSBImpl.setNotificationDate(new Date(notificationDate));
		}

		if (systemDate == Long.MIN_VALUE) {
			ntucSBImpl.setSystemDate(null);
		}
		else {
			ntucSBImpl.setSystemDate(new Date(systemDate));
		}

		if (notificationId == null) {
			ntucSBImpl.setNotificationId("");
		}
		else {
			ntucSBImpl.setNotificationId(notificationId);
		}

		if (event == null) {
			ntucSBImpl.setEvent("");
		}
		else {
			ntucSBImpl.setEvent(event);
		}

		if (changeFrom == null) {
			ntucSBImpl.setChangeFrom("");
		}
		else {
			ntucSBImpl.setChangeFrom(changeFrom);
		}

		if (processingStatus == null) {
			ntucSBImpl.setProcessingStatus("");
		}
		else {
			ntucSBImpl.setProcessingStatus(processingStatus);
		}

		if (courseStatus == null) {
			ntucSBImpl.setCourseStatus("");
		}
		else {
			ntucSBImpl.setCourseStatus(courseStatus);
		}

		ntucSBImpl.setIsCriticalProcessed(isCriticalProcessed);
		ntucSBImpl.setIsNonCriticalProcessed(isNonCriticalProcessed);
		ntucSBImpl.setIsCronProcessed(isCronProcessed);
		ntucSBImpl.setCanRetry(canRetry);

		if (lastRetried == Long.MIN_VALUE) {
			ntucSBImpl.setLastRetried(null);
		}
		else {
			ntucSBImpl.setLastRetried(new Date(lastRetried));
		}

		ntucSBImpl.setTotalRetries(totalRetries);
		ntucSBImpl.setIsRowLockFailed(isRowLockFailed);

		ntucSBImpl.resetOriginalValues();

		return ntucSBImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		ntucDTId = objectInput.readLong();

		companyId = objectInput.readLong();
		courseCode = objectInput.readUTF();
		courseType = objectInput.readUTF();
		notificationDate = objectInput.readLong();
		systemDate = objectInput.readLong();
		notificationId = objectInput.readUTF();
		event = objectInput.readUTF();
		changeFrom = objectInput.readUTF();
		processingStatus = objectInput.readUTF();
		courseStatus = objectInput.readUTF();

		isCriticalProcessed = objectInput.readBoolean();

		isNonCriticalProcessed = objectInput.readBoolean();

		isCronProcessed = objectInput.readBoolean();

		canRetry = objectInput.readBoolean();
		lastRetried = objectInput.readLong();

		totalRetries = objectInput.readInt();

		isRowLockFailed = objectInput.readBoolean();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(ntucDTId);

		objectOutput.writeLong(companyId);

		if (courseCode == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(courseCode);
		}

		if (courseType == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(courseType);
		}

		objectOutput.writeLong(notificationDate);
		objectOutput.writeLong(systemDate);

		if (notificationId == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(notificationId);
		}

		if (event == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(event);
		}

		if (changeFrom == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(changeFrom);
		}

		if (processingStatus == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(processingStatus);
		}

		if (courseStatus == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(courseStatus);
		}

		objectOutput.writeBoolean(isCriticalProcessed);

		objectOutput.writeBoolean(isNonCriticalProcessed);

		objectOutput.writeBoolean(isCronProcessed);

		objectOutput.writeBoolean(canRetry);
		objectOutput.writeLong(lastRetried);

		objectOutput.writeInt(totalRetries);

		objectOutput.writeBoolean(isRowLockFailed);
	}

	public long ntucDTId;
	public long companyId;
	public String courseCode;
	public String courseType;
	public long notificationDate;
	public long systemDate;
	public String notificationId;
	public String event;
	public String changeFrom;
	public String processingStatus;
	public String courseStatus;
	public boolean isCriticalProcessed;
	public boolean isNonCriticalProcessed;
	public boolean isCronProcessed;
	public boolean canRetry;
	public long lastRetried;
	public int totalRetries;
	public boolean isRowLockFailed;

}