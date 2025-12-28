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
public class CourseScheduleSoap implements Serializable {

	public static CourseScheduleSoap toSoapModel(CourseSchedule model) {
		CourseScheduleSoap soapModel = new CourseScheduleSoap();

		soapModel.setUuid(model.getUuid());
		soapModel.setCourseScheduleId(model.getCourseScheduleId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setCourseCode(model.getCourseCode());
		soapModel.setIntakeNumber(model.getIntakeNumber());
		soapModel.setStartDate(model.getStartDate());
		soapModel.setEndDate(model.getEndDate());
		soapModel.setDurationHours(model.getDurationHours());
		soapModel.setDurationMinutes(model.getDurationMinutes());
		soapModel.setAvailability(model.getAvailability());
		soapModel.setVenue(model.getVenue());
		soapModel.setDescription(model.getDescription());
		soapModel.setAvailablePax(model.getAvailablePax());
		soapModel.setAvailableWaitlist(model.getAvailableWaitlist());
		soapModel.setLxpBuyUrl(model.getLxpBuyUrl());
		soapModel.setLxpRoiUrl(model.getLxpRoiUrl());
		soapModel.setImportantNote(model.getImportantNote());
		soapModel.setScheduleDownloadUrl(model.getScheduleDownloadUrl());
		soapModel.setErrorCode(model.getErrorCode());
		soapModel.setErrorMessage(model.getErrorMessage());
		soapModel.setRawJson(model.getRawJson());
		soapModel.setModifiedBy(model.getModifiedBy());

		return soapModel;
	}

	public static CourseScheduleSoap[] toSoapModels(CourseSchedule[] models) {
		CourseScheduleSoap[] soapModels = new CourseScheduleSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static CourseScheduleSoap[][] toSoapModels(
		CourseSchedule[][] models) {

		CourseScheduleSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels =
				new CourseScheduleSoap[models.length][models[0].length];
		}
		else {
			soapModels = new CourseScheduleSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static CourseScheduleSoap[] toSoapModels(
		List<CourseSchedule> models) {

		List<CourseScheduleSoap> soapModels = new ArrayList<CourseScheduleSoap>(
			models.size());

		for (CourseSchedule model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new CourseScheduleSoap[soapModels.size()]);
	}

	public CourseScheduleSoap() {
	}

	public long getPrimaryKey() {
		return _courseScheduleId;
	}

	public void setPrimaryKey(long pk) {
		setCourseScheduleId(pk);
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getCourseScheduleId() {
		return _courseScheduleId;
	}

	public void setCourseScheduleId(long courseScheduleId) {
		_courseScheduleId = courseScheduleId;
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

	public String getCourseCode() {
		return _courseCode;
	}

	public void setCourseCode(String courseCode) {
		_courseCode = courseCode;
	}

	public String getIntakeNumber() {
		return _intakeNumber;
	}

	public void setIntakeNumber(String intakeNumber) {
		_intakeNumber = intakeNumber;
	}

	public Date getStartDate() {
		return _startDate;
	}

	public void setStartDate(Date startDate) {
		_startDate = startDate;
	}

	public Date getEndDate() {
		return _endDate;
	}

	public void setEndDate(Date endDate) {
		_endDate = endDate;
	}

	public int getDurationHours() {
		return _durationHours;
	}

	public void setDurationHours(int durationHours) {
		_durationHours = durationHours;
	}

	public int getDurationMinutes() {
		return _durationMinutes;
	}

	public void setDurationMinutes(int durationMinutes) {
		_durationMinutes = durationMinutes;
	}

	public int getAvailability() {
		return _availability;
	}

	public void setAvailability(int availability) {
		_availability = availability;
	}

	public String getVenue() {
		return _venue;
	}

	public void setVenue(String venue) {
		_venue = venue;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public String getAvailablePax() {
		return _availablePax;
	}

	public void setAvailablePax(String availablePax) {
		_availablePax = availablePax;
	}

	public String getAvailableWaitlist() {
		return _availableWaitlist;
	}

	public void setAvailableWaitlist(String availableWaitlist) {
		_availableWaitlist = availableWaitlist;
	}

	public String getLxpBuyUrl() {
		return _lxpBuyUrl;
	}

	public void setLxpBuyUrl(String lxpBuyUrl) {
		_lxpBuyUrl = lxpBuyUrl;
	}

	public String getLxpRoiUrl() {
		return _lxpRoiUrl;
	}

	public void setLxpRoiUrl(String lxpRoiUrl) {
		_lxpRoiUrl = lxpRoiUrl;
	}

	public String getImportantNote() {
		return _importantNote;
	}

	public void setImportantNote(String importantNote) {
		_importantNote = importantNote;
	}

	public String getScheduleDownloadUrl() {
		return _scheduleDownloadUrl;
	}

	public void setScheduleDownloadUrl(String scheduleDownloadUrl) {
		_scheduleDownloadUrl = scheduleDownloadUrl;
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

	public String getRawJson() {
		return _rawJson;
	}

	public void setRawJson(String rawJson) {
		_rawJson = rawJson;
	}

	public long getModifiedBy() {
		return _modifiedBy;
	}

	public void setModifiedBy(long modifiedBy) {
		_modifiedBy = modifiedBy;
	}

	private String _uuid;
	private long _courseScheduleId;
	private long _groupId;
	private long _companyId;
	private Date _createDate;
	private Date _modifiedDate;
	private String _courseCode;
	private String _intakeNumber;
	private Date _startDate;
	private Date _endDate;
	private int _durationHours;
	private int _durationMinutes;
	private int _availability;
	private String _venue;
	private String _description;
	private String _availablePax;
	private String _availableWaitlist;
	private String _lxpBuyUrl;
	private String _lxpRoiUrl;
	private String _importantNote;
	private String _scheduleDownloadUrl;
	private String _errorCode;
	private String _errorMessage;
	private String _rawJson;
	private long _modifiedBy;

}