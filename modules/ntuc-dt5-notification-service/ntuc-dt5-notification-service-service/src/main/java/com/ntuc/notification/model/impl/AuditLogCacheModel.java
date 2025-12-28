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

import com.ntuc.notification.model.AuditLog;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing AuditLog in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class AuditLogCacheModel
	implements CacheModel<AuditLog>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof AuditLogCacheModel)) {
			return false;
		}

		AuditLogCacheModel auditLogCacheModel = (AuditLogCacheModel)object;

		if (auditLogId == auditLogCacheModel.auditLogId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, auditLogId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(51);

		sb.append("{auditLogId=");
		sb.append(auditLogId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", courseCode=");
		sb.append(courseCode);
		sb.append(", ntucDTId=");
		sb.append(ntucDTId);
		sb.append(", startTimeMs=");
		sb.append(startTimeMs);
		sb.append(", endTimeMs=");
		sb.append(endTimeMs);
		sb.append(", durationMs=");
		sb.append(durationMs);
		sb.append(", correlationId=");
		sb.append(correlationId);
		sb.append(", jobRunId=");
		sb.append(jobRunId);
		sb.append(", requestId=");
		sb.append(requestId);
		sb.append(", eventId=");
		sb.append(eventId);
		sb.append(", severity=");
		sb.append(severity);
		sb.append(", status=");
		sb.append(status);
		sb.append(", step=");
		sb.append(step);
		sb.append(", category=");
		sb.append(category);
		sb.append(", message=");
		sb.append(message);
		sb.append(", errorCode=");
		sb.append(errorCode);
		sb.append(", errorMessage=");
		sb.append(errorMessage);
		sb.append(", exceptionClass=");
		sb.append(exceptionClass);
		sb.append(", alertFingerprint=");
		sb.append(alertFingerprint);
		sb.append(", stackTraceHash=");
		sb.append(stackTraceHash);

		return sb.toString();
	}

	@Override
	public AuditLog toEntityModel() {
		AuditLogImpl auditLogImpl = new AuditLogImpl();

		auditLogImpl.setAuditLogId(auditLogId);

		if (createDate == Long.MIN_VALUE) {
			auditLogImpl.setCreateDate(null);
		}
		else {
			auditLogImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			auditLogImpl.setModifiedDate(null);
		}
		else {
			auditLogImpl.setModifiedDate(new Date(modifiedDate));
		}

		auditLogImpl.setGroupId(groupId);
		auditLogImpl.setCompanyId(companyId);
		auditLogImpl.setUserId(userId);

		if (courseCode == null) {
			auditLogImpl.setCourseCode("");
		}
		else {
			auditLogImpl.setCourseCode(courseCode);
		}

		auditLogImpl.setNtucDTId(ntucDTId);
		auditLogImpl.setStartTimeMs(startTimeMs);
		auditLogImpl.setEndTimeMs(endTimeMs);
		auditLogImpl.setDurationMs(durationMs);

		if (correlationId == null) {
			auditLogImpl.setCorrelationId("");
		}
		else {
			auditLogImpl.setCorrelationId(correlationId);
		}

		if (jobRunId == null) {
			auditLogImpl.setJobRunId("");
		}
		else {
			auditLogImpl.setJobRunId(jobRunId);
		}

		if (requestId == null) {
			auditLogImpl.setRequestId("");
		}
		else {
			auditLogImpl.setRequestId(requestId);
		}

		if (eventId == null) {
			auditLogImpl.setEventId("");
		}
		else {
			auditLogImpl.setEventId(eventId);
		}

		if (severity == null) {
			auditLogImpl.setSeverity("");
		}
		else {
			auditLogImpl.setSeverity(severity);
		}

		if (status == null) {
			auditLogImpl.setStatus("");
		}
		else {
			auditLogImpl.setStatus(status);
		}

		if (step == null) {
			auditLogImpl.setStep("");
		}
		else {
			auditLogImpl.setStep(step);
		}

		if (category == null) {
			auditLogImpl.setCategory("");
		}
		else {
			auditLogImpl.setCategory(category);
		}

		if (message == null) {
			auditLogImpl.setMessage("");
		}
		else {
			auditLogImpl.setMessage(message);
		}

		if (errorCode == null) {
			auditLogImpl.setErrorCode("");
		}
		else {
			auditLogImpl.setErrorCode(errorCode);
		}

		if (errorMessage == null) {
			auditLogImpl.setErrorMessage("");
		}
		else {
			auditLogImpl.setErrorMessage(errorMessage);
		}

		if (exceptionClass == null) {
			auditLogImpl.setExceptionClass("");
		}
		else {
			auditLogImpl.setExceptionClass(exceptionClass);
		}

		if (alertFingerprint == null) {
			auditLogImpl.setAlertFingerprint("");
		}
		else {
			auditLogImpl.setAlertFingerprint(alertFingerprint);
		}

		if (stackTraceHash == null) {
			auditLogImpl.setStackTraceHash("");
		}
		else {
			auditLogImpl.setStackTraceHash(stackTraceHash);
		}

		auditLogImpl.resetOriginalValues();

		return auditLogImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		auditLogId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		courseCode = objectInput.readUTF();

		ntucDTId = objectInput.readLong();

		startTimeMs = objectInput.readLong();

		endTimeMs = objectInput.readLong();

		durationMs = objectInput.readLong();
		correlationId = objectInput.readUTF();
		jobRunId = objectInput.readUTF();
		requestId = objectInput.readUTF();
		eventId = objectInput.readUTF();
		severity = objectInput.readUTF();
		status = objectInput.readUTF();
		step = objectInput.readUTF();
		category = objectInput.readUTF();
		message = objectInput.readUTF();
		errorCode = objectInput.readUTF();
		errorMessage = objectInput.readUTF();
		exceptionClass = objectInput.readUTF();
		alertFingerprint = objectInput.readUTF();
		stackTraceHash = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(auditLogId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeLong(groupId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (courseCode == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(courseCode);
		}

		objectOutput.writeLong(ntucDTId);

		objectOutput.writeLong(startTimeMs);

		objectOutput.writeLong(endTimeMs);

		objectOutput.writeLong(durationMs);

		if (correlationId == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(correlationId);
		}

		if (jobRunId == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(jobRunId);
		}

		if (requestId == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(requestId);
		}

		if (eventId == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(eventId);
		}

		if (severity == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(severity);
		}

		if (status == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(status);
		}

		if (step == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(step);
		}

		if (category == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(category);
		}

		if (message == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(message);
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

		if (exceptionClass == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(exceptionClass);
		}

		if (alertFingerprint == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(alertFingerprint);
		}

		if (stackTraceHash == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(stackTraceHash);
		}
	}

	public long auditLogId;
	public long createDate;
	public long modifiedDate;
	public long groupId;
	public long companyId;
	public long userId;
	public String courseCode;
	public long ntucDTId;
	public long startTimeMs;
	public long endTimeMs;
	public long durationMs;
	public String correlationId;
	public String jobRunId;
	public String requestId;
	public String eventId;
	public String severity;
	public String status;
	public String step;
	public String category;
	public String message;
	public String errorCode;
	public String errorMessage;
	public String exceptionClass;
	public String alertFingerprint;
	public String stackTraceHash;

}