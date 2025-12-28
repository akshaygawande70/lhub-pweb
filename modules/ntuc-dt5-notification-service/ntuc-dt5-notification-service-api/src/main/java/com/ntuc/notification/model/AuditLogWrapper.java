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

package com.ntuc.notification.model;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.sql.Blob;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link AuditLog}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AuditLog
 * @generated
 */
public class AuditLogWrapper
	extends BaseModelWrapper<AuditLog>
	implements AuditLog, ModelWrapper<AuditLog> {

	public AuditLogWrapper(AuditLog auditLog) {
		super(auditLog);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("auditLogId", getAuditLogId());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("courseCode", getCourseCode());
		attributes.put("ntucDTId", getNtucDTId());
		attributes.put("startTimeMs", getStartTimeMs());
		attributes.put("endTimeMs", getEndTimeMs());
		attributes.put("durationMs", getDurationMs());
		attributes.put("correlationId", getCorrelationId());
		attributes.put("jobRunId", getJobRunId());
		attributes.put("requestId", getRequestId());
		attributes.put("eventId", getEventId());
		attributes.put("severity", getSeverity());
		attributes.put("status", getStatus());
		attributes.put("step", getStep());
		attributes.put("category", getCategory());
		attributes.put("message", getMessage());
		attributes.put("errorCode", getErrorCode());
		attributes.put("errorMessage", getErrorMessage());
		attributes.put("exceptionClass", getExceptionClass());
		attributes.put("alertFingerprint", getAlertFingerprint());
		attributes.put("stackTraceHash", getStackTraceHash());
		attributes.put("stackTraceTruncated", getStackTraceTruncated());
		attributes.put("detailsJson", getDetailsJson());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long auditLogId = (Long)attributes.get("auditLogId");

		if (auditLogId != null) {
			setAuditLogId(auditLogId);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
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

		String courseCode = (String)attributes.get("courseCode");

		if (courseCode != null) {
			setCourseCode(courseCode);
		}

		Long ntucDTId = (Long)attributes.get("ntucDTId");

		if (ntucDTId != null) {
			setNtucDTId(ntucDTId);
		}

		Long startTimeMs = (Long)attributes.get("startTimeMs");

		if (startTimeMs != null) {
			setStartTimeMs(startTimeMs);
		}

		Long endTimeMs = (Long)attributes.get("endTimeMs");

		if (endTimeMs != null) {
			setEndTimeMs(endTimeMs);
		}

		Long durationMs = (Long)attributes.get("durationMs");

		if (durationMs != null) {
			setDurationMs(durationMs);
		}

		String correlationId = (String)attributes.get("correlationId");

		if (correlationId != null) {
			setCorrelationId(correlationId);
		}

		String jobRunId = (String)attributes.get("jobRunId");

		if (jobRunId != null) {
			setJobRunId(jobRunId);
		}

		String requestId = (String)attributes.get("requestId");

		if (requestId != null) {
			setRequestId(requestId);
		}

		String eventId = (String)attributes.get("eventId");

		if (eventId != null) {
			setEventId(eventId);
		}

		String severity = (String)attributes.get("severity");

		if (severity != null) {
			setSeverity(severity);
		}

		String status = (String)attributes.get("status");

		if (status != null) {
			setStatus(status);
		}

		String step = (String)attributes.get("step");

		if (step != null) {
			setStep(step);
		}

		String category = (String)attributes.get("category");

		if (category != null) {
			setCategory(category);
		}

		String message = (String)attributes.get("message");

		if (message != null) {
			setMessage(message);
		}

		String errorCode = (String)attributes.get("errorCode");

		if (errorCode != null) {
			setErrorCode(errorCode);
		}

		String errorMessage = (String)attributes.get("errorMessage");

		if (errorMessage != null) {
			setErrorMessage(errorMessage);
		}

		String exceptionClass = (String)attributes.get("exceptionClass");

		if (exceptionClass != null) {
			setExceptionClass(exceptionClass);
		}

		String alertFingerprint = (String)attributes.get("alertFingerprint");

		if (alertFingerprint != null) {
			setAlertFingerprint(alertFingerprint);
		}

		String stackTraceHash = (String)attributes.get("stackTraceHash");

		if (stackTraceHash != null) {
			setStackTraceHash(stackTraceHash);
		}

		Blob stackTraceTruncated = (Blob)attributes.get("stackTraceTruncated");

		if (stackTraceTruncated != null) {
			setStackTraceTruncated(stackTraceTruncated);
		}

		Blob detailsJson = (Blob)attributes.get("detailsJson");

		if (detailsJson != null) {
			setDetailsJson(detailsJson);
		}
	}

	/**
	 * Returns the alert fingerprint of this audit log.
	 *
	 * @return the alert fingerprint of this audit log
	 */
	@Override
	public String getAlertFingerprint() {
		return model.getAlertFingerprint();
	}

	/**
	 * Returns the audit log ID of this audit log.
	 *
	 * @return the audit log ID of this audit log
	 */
	@Override
	public long getAuditLogId() {
		return model.getAuditLogId();
	}

	/**
	 * Returns the category of this audit log.
	 *
	 * @return the category of this audit log
	 */
	@Override
	public String getCategory() {
		return model.getCategory();
	}

	/**
	 * Returns the company ID of this audit log.
	 *
	 * @return the company ID of this audit log
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the correlation ID of this audit log.
	 *
	 * @return the correlation ID of this audit log
	 */
	@Override
	public String getCorrelationId() {
		return model.getCorrelationId();
	}

	/**
	 * Returns the course code of this audit log.
	 *
	 * @return the course code of this audit log
	 */
	@Override
	public String getCourseCode() {
		return model.getCourseCode();
	}

	/**
	 * Returns the create date of this audit log.
	 *
	 * @return the create date of this audit log
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the details json of this audit log.
	 *
	 * @return the details json of this audit log
	 */
	@Override
	public Blob getDetailsJson() {
		return model.getDetailsJson();
	}

	/**
	 * Returns the duration ms of this audit log.
	 *
	 * @return the duration ms of this audit log
	 */
	@Override
	public long getDurationMs() {
		return model.getDurationMs();
	}

	/**
	 * Returns the end time ms of this audit log.
	 *
	 * @return the end time ms of this audit log
	 */
	@Override
	public long getEndTimeMs() {
		return model.getEndTimeMs();
	}

	/**
	 * Returns the error code of this audit log.
	 *
	 * @return the error code of this audit log
	 */
	@Override
	public String getErrorCode() {
		return model.getErrorCode();
	}

	/**
	 * Returns the error message of this audit log.
	 *
	 * @return the error message of this audit log
	 */
	@Override
	public String getErrorMessage() {
		return model.getErrorMessage();
	}

	/**
	 * Returns the event ID of this audit log.
	 *
	 * @return the event ID of this audit log
	 */
	@Override
	public String getEventId() {
		return model.getEventId();
	}

	/**
	 * Returns the exception class of this audit log.
	 *
	 * @return the exception class of this audit log
	 */
	@Override
	public String getExceptionClass() {
		return model.getExceptionClass();
	}

	/**
	 * Returns the group ID of this audit log.
	 *
	 * @return the group ID of this audit log
	 */
	@Override
	public long getGroupId() {
		return model.getGroupId();
	}

	/**
	 * Returns the job run ID of this audit log.
	 *
	 * @return the job run ID of this audit log
	 */
	@Override
	public String getJobRunId() {
		return model.getJobRunId();
	}

	/**
	 * Returns the message of this audit log.
	 *
	 * @return the message of this audit log
	 */
	@Override
	public String getMessage() {
		return model.getMessage();
	}

	/**
	 * Returns the modified date of this audit log.
	 *
	 * @return the modified date of this audit log
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the ntuc dt ID of this audit log.
	 *
	 * @return the ntuc dt ID of this audit log
	 */
	@Override
	public long getNtucDTId() {
		return model.getNtucDTId();
	}

	/**
	 * Returns the primary key of this audit log.
	 *
	 * @return the primary key of this audit log
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the request ID of this audit log.
	 *
	 * @return the request ID of this audit log
	 */
	@Override
	public String getRequestId() {
		return model.getRequestId();
	}

	/**
	 * Returns the severity of this audit log.
	 *
	 * @return the severity of this audit log
	 */
	@Override
	public String getSeverity() {
		return model.getSeverity();
	}

	/**
	 * Returns the stack trace hash of this audit log.
	 *
	 * @return the stack trace hash of this audit log
	 */
	@Override
	public String getStackTraceHash() {
		return model.getStackTraceHash();
	}

	/**
	 * Returns the stack trace truncated of this audit log.
	 *
	 * @return the stack trace truncated of this audit log
	 */
	@Override
	public Blob getStackTraceTruncated() {
		return model.getStackTraceTruncated();
	}

	/**
	 * Returns the start time ms of this audit log.
	 *
	 * @return the start time ms of this audit log
	 */
	@Override
	public long getStartTimeMs() {
		return model.getStartTimeMs();
	}

	/**
	 * Returns the status of this audit log.
	 *
	 * @return the status of this audit log
	 */
	@Override
	public String getStatus() {
		return model.getStatus();
	}

	/**
	 * Returns the step of this audit log.
	 *
	 * @return the step of this audit log
	 */
	@Override
	public String getStep() {
		return model.getStep();
	}

	/**
	 * Returns the user ID of this audit log.
	 *
	 * @return the user ID of this audit log
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user uuid of this audit log.
	 *
	 * @return the user uuid of this audit log
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the alert fingerprint of this audit log.
	 *
	 * @param alertFingerprint the alert fingerprint of this audit log
	 */
	@Override
	public void setAlertFingerprint(String alertFingerprint) {
		model.setAlertFingerprint(alertFingerprint);
	}

	/**
	 * Sets the audit log ID of this audit log.
	 *
	 * @param auditLogId the audit log ID of this audit log
	 */
	@Override
	public void setAuditLogId(long auditLogId) {
		model.setAuditLogId(auditLogId);
	}

	/**
	 * Sets the category of this audit log.
	 *
	 * @param category the category of this audit log
	 */
	@Override
	public void setCategory(String category) {
		model.setCategory(category);
	}

	/**
	 * Sets the company ID of this audit log.
	 *
	 * @param companyId the company ID of this audit log
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the correlation ID of this audit log.
	 *
	 * @param correlationId the correlation ID of this audit log
	 */
	@Override
	public void setCorrelationId(String correlationId) {
		model.setCorrelationId(correlationId);
	}

	/**
	 * Sets the course code of this audit log.
	 *
	 * @param courseCode the course code of this audit log
	 */
	@Override
	public void setCourseCode(String courseCode) {
		model.setCourseCode(courseCode);
	}

	/**
	 * Sets the create date of this audit log.
	 *
	 * @param createDate the create date of this audit log
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the details json of this audit log.
	 *
	 * @param detailsJson the details json of this audit log
	 */
	@Override
	public void setDetailsJson(Blob detailsJson) {
		model.setDetailsJson(detailsJson);
	}

	/**
	 * Sets the duration ms of this audit log.
	 *
	 * @param durationMs the duration ms of this audit log
	 */
	@Override
	public void setDurationMs(long durationMs) {
		model.setDurationMs(durationMs);
	}

	/**
	 * Sets the end time ms of this audit log.
	 *
	 * @param endTimeMs the end time ms of this audit log
	 */
	@Override
	public void setEndTimeMs(long endTimeMs) {
		model.setEndTimeMs(endTimeMs);
	}

	/**
	 * Sets the error code of this audit log.
	 *
	 * @param errorCode the error code of this audit log
	 */
	@Override
	public void setErrorCode(String errorCode) {
		model.setErrorCode(errorCode);
	}

	/**
	 * Sets the error message of this audit log.
	 *
	 * @param errorMessage the error message of this audit log
	 */
	@Override
	public void setErrorMessage(String errorMessage) {
		model.setErrorMessage(errorMessage);
	}

	/**
	 * Sets the event ID of this audit log.
	 *
	 * @param eventId the event ID of this audit log
	 */
	@Override
	public void setEventId(String eventId) {
		model.setEventId(eventId);
	}

	/**
	 * Sets the exception class of this audit log.
	 *
	 * @param exceptionClass the exception class of this audit log
	 */
	@Override
	public void setExceptionClass(String exceptionClass) {
		model.setExceptionClass(exceptionClass);
	}

	/**
	 * Sets the group ID of this audit log.
	 *
	 * @param groupId the group ID of this audit log
	 */
	@Override
	public void setGroupId(long groupId) {
		model.setGroupId(groupId);
	}

	/**
	 * Sets the job run ID of this audit log.
	 *
	 * @param jobRunId the job run ID of this audit log
	 */
	@Override
	public void setJobRunId(String jobRunId) {
		model.setJobRunId(jobRunId);
	}

	/**
	 * Sets the message of this audit log.
	 *
	 * @param message the message of this audit log
	 */
	@Override
	public void setMessage(String message) {
		model.setMessage(message);
	}

	/**
	 * Sets the modified date of this audit log.
	 *
	 * @param modifiedDate the modified date of this audit log
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the ntuc dt ID of this audit log.
	 *
	 * @param ntucDTId the ntuc dt ID of this audit log
	 */
	@Override
	public void setNtucDTId(long ntucDTId) {
		model.setNtucDTId(ntucDTId);
	}

	/**
	 * Sets the primary key of this audit log.
	 *
	 * @param primaryKey the primary key of this audit log
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the request ID of this audit log.
	 *
	 * @param requestId the request ID of this audit log
	 */
	@Override
	public void setRequestId(String requestId) {
		model.setRequestId(requestId);
	}

	/**
	 * Sets the severity of this audit log.
	 *
	 * @param severity the severity of this audit log
	 */
	@Override
	public void setSeverity(String severity) {
		model.setSeverity(severity);
	}

	/**
	 * Sets the stack trace hash of this audit log.
	 *
	 * @param stackTraceHash the stack trace hash of this audit log
	 */
	@Override
	public void setStackTraceHash(String stackTraceHash) {
		model.setStackTraceHash(stackTraceHash);
	}

	/**
	 * Sets the stack trace truncated of this audit log.
	 *
	 * @param stackTraceTruncated the stack trace truncated of this audit log
	 */
	@Override
	public void setStackTraceTruncated(Blob stackTraceTruncated) {
		model.setStackTraceTruncated(stackTraceTruncated);
	}

	/**
	 * Sets the start time ms of this audit log.
	 *
	 * @param startTimeMs the start time ms of this audit log
	 */
	@Override
	public void setStartTimeMs(long startTimeMs) {
		model.setStartTimeMs(startTimeMs);
	}

	/**
	 * Sets the status of this audit log.
	 *
	 * @param status the status of this audit log
	 */
	@Override
	public void setStatus(String status) {
		model.setStatus(status);
	}

	/**
	 * Sets the step of this audit log.
	 *
	 * @param step the step of this audit log
	 */
	@Override
	public void setStep(String step) {
		model.setStep(step);
	}

	/**
	 * Sets the user ID of this audit log.
	 *
	 * @param userId the user ID of this audit log
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user uuid of this audit log.
	 *
	 * @param userUuid the user uuid of this audit log
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	@Override
	protected AuditLogWrapper wrap(AuditLog auditLog) {
		return new AuditLogWrapper(auditLog);
	}

}