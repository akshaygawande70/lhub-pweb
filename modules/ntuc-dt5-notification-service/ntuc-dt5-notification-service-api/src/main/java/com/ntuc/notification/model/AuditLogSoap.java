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

import java.io.Serializable;

import java.sql.Blob;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author Brian Wing Shun Chan
 * @deprecated As of Athanasius (7.3.x), with no direct replacement
 * @generated
 */
@Deprecated
public class AuditLogSoap implements Serializable {

	public static AuditLogSoap toSoapModel(AuditLog model) {
		AuditLogSoap soapModel = new AuditLogSoap();

		soapModel.setAuditLogId(model.getAuditLogId());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setCourseCode(model.getCourseCode());
		soapModel.setNtucDTId(model.getNtucDTId());
		soapModel.setStartTimeMs(model.getStartTimeMs());
		soapModel.setEndTimeMs(model.getEndTimeMs());
		soapModel.setDurationMs(model.getDurationMs());
		soapModel.setCorrelationId(model.getCorrelationId());
		soapModel.setJobRunId(model.getJobRunId());
		soapModel.setRequestId(model.getRequestId());
		soapModel.setEventId(model.getEventId());
		soapModel.setSeverity(model.getSeverity());
		soapModel.setStatus(model.getStatus());
		soapModel.setStep(model.getStep());
		soapModel.setCategory(model.getCategory());
		soapModel.setMessage(model.getMessage());
		soapModel.setErrorCode(model.getErrorCode());
		soapModel.setErrorMessage(model.getErrorMessage());
		soapModel.setExceptionClass(model.getExceptionClass());
		soapModel.setAlertFingerprint(model.getAlertFingerprint());
		soapModel.setStackTraceHash(model.getStackTraceHash());
		soapModel.setStackTraceTruncated(model.getStackTraceTruncated());
		soapModel.setDetailsJson(model.getDetailsJson());

		return soapModel;
	}

	public static AuditLogSoap[] toSoapModels(AuditLog[] models) {
		AuditLogSoap[] soapModels = new AuditLogSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static AuditLogSoap[][] toSoapModels(AuditLog[][] models) {
		AuditLogSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new AuditLogSoap[models.length][models[0].length];
		}
		else {
			soapModels = new AuditLogSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static AuditLogSoap[] toSoapModels(List<AuditLog> models) {
		List<AuditLogSoap> soapModels = new ArrayList<AuditLogSoap>(
			models.size());

		for (AuditLog model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new AuditLogSoap[soapModels.size()]);
	}

	public AuditLogSoap() {
	}

	public long getPrimaryKey() {
		return _auditLogId;
	}

	public void setPrimaryKey(long pk) {
		setAuditLogId(pk);
	}

	public long getAuditLogId() {
		return _auditLogId;
	}

	public void setAuditLogId(long auditLogId) {
		_auditLogId = auditLogId;
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

	public String getCourseCode() {
		return _courseCode;
	}

	public void setCourseCode(String courseCode) {
		_courseCode = courseCode;
	}

	public long getNtucDTId() {
		return _ntucDTId;
	}

	public void setNtucDTId(long ntucDTId) {
		_ntucDTId = ntucDTId;
	}

	public long getStartTimeMs() {
		return _startTimeMs;
	}

	public void setStartTimeMs(long startTimeMs) {
		_startTimeMs = startTimeMs;
	}

	public long getEndTimeMs() {
		return _endTimeMs;
	}

	public void setEndTimeMs(long endTimeMs) {
		_endTimeMs = endTimeMs;
	}

	public long getDurationMs() {
		return _durationMs;
	}

	public void setDurationMs(long durationMs) {
		_durationMs = durationMs;
	}

	public String getCorrelationId() {
		return _correlationId;
	}

	public void setCorrelationId(String correlationId) {
		_correlationId = correlationId;
	}

	public String getJobRunId() {
		return _jobRunId;
	}

	public void setJobRunId(String jobRunId) {
		_jobRunId = jobRunId;
	}

	public String getRequestId() {
		return _requestId;
	}

	public void setRequestId(String requestId) {
		_requestId = requestId;
	}

	public String getEventId() {
		return _eventId;
	}

	public void setEventId(String eventId) {
		_eventId = eventId;
	}

	public String getSeverity() {
		return _severity;
	}

	public void setSeverity(String severity) {
		_severity = severity;
	}

	public String getStatus() {
		return _status;
	}

	public void setStatus(String status) {
		_status = status;
	}

	public String getStep() {
		return _step;
	}

	public void setStep(String step) {
		_step = step;
	}

	public String getCategory() {
		return _category;
	}

	public void setCategory(String category) {
		_category = category;
	}

	public String getMessage() {
		return _message;
	}

	public void setMessage(String message) {
		_message = message;
	}

	public String getErrorCode() {
		return _errorCode;
	}

	public void setErrorCode(String errorCode) {
		_errorCode = errorCode;
	}

	public String getErrorMessage() {
		return _errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		_errorMessage = errorMessage;
	}

	public String getExceptionClass() {
		return _exceptionClass;
	}

	public void setExceptionClass(String exceptionClass) {
		_exceptionClass = exceptionClass;
	}

	public String getAlertFingerprint() {
		return _alertFingerprint;
	}

	public void setAlertFingerprint(String alertFingerprint) {
		_alertFingerprint = alertFingerprint;
	}

	public String getStackTraceHash() {
		return _stackTraceHash;
	}

	public void setStackTraceHash(String stackTraceHash) {
		_stackTraceHash = stackTraceHash;
	}

	public Blob getStackTraceTruncated() {
		return _stackTraceTruncated;
	}

	public void setStackTraceTruncated(Blob stackTraceTruncated) {
		_stackTraceTruncated = stackTraceTruncated;
	}

	public Blob getDetailsJson() {
		return _detailsJson;
	}

	public void setDetailsJson(Blob detailsJson) {
		_detailsJson = detailsJson;
	}

	private long _auditLogId;
	private Date _createDate;
	private Date _modifiedDate;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _courseCode;
	private long _ntucDTId;
	private long _startTimeMs;
	private long _endTimeMs;
	private long _durationMs;
	private String _correlationId;
	private String _jobRunId;
	private String _requestId;
	private String _eventId;
	private String _severity;
	private String _status;
	private String _step;
	private String _category;
	private String _message;
	private String _errorCode;
	private String _errorMessage;
	private String _exceptionClass;
	private String _alertFingerprint;
	private String _stackTraceHash;
	private Blob _stackTraceTruncated;
	private Blob _detailsJson;

}