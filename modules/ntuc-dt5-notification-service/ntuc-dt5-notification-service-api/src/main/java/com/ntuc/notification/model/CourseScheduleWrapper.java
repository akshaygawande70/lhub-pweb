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

import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link CourseSchedule}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CourseSchedule
 * @generated
 */
public class CourseScheduleWrapper
	extends BaseModelWrapper<CourseSchedule>
	implements CourseSchedule, ModelWrapper<CourseSchedule> {

	public CourseScheduleWrapper(CourseSchedule courseSchedule) {
		super(courseSchedule);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("courseScheduleId", getCourseScheduleId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("courseCode", getCourseCode());
		attributes.put("intakeNumber", getIntakeNumber());
		attributes.put("startDate", getStartDate());
		attributes.put("endDate", getEndDate());
		attributes.put("durationHours", getDurationHours());
		attributes.put("durationMinutes", getDurationMinutes());
		attributes.put("availability", getAvailability());
		attributes.put("venue", getVenue());
		attributes.put("description", getDescription());
		attributes.put("availablePax", getAvailablePax());
		attributes.put("availableWaitlist", getAvailableWaitlist());
		attributes.put("lxpBuyUrl", getLxpBuyUrl());
		attributes.put("lxpRoiUrl", getLxpRoiUrl());
		attributes.put("importantNote", getImportantNote());
		attributes.put("scheduleDownloadUrl", getScheduleDownloadUrl());
		attributes.put("errorCode", getErrorCode());
		attributes.put("errorMessage", getErrorMessage());
		attributes.put("rawJson", getRawJson());
		attributes.put("modifiedBy", getModifiedBy());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long courseScheduleId = (Long)attributes.get("courseScheduleId");

		if (courseScheduleId != null) {
			setCourseScheduleId(courseScheduleId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String courseCode = (String)attributes.get("courseCode");

		if (courseCode != null) {
			setCourseCode(courseCode);
		}

		String intakeNumber = (String)attributes.get("intakeNumber");

		if (intakeNumber != null) {
			setIntakeNumber(intakeNumber);
		}

		Date startDate = (Date)attributes.get("startDate");

		if (startDate != null) {
			setStartDate(startDate);
		}

		Date endDate = (Date)attributes.get("endDate");

		if (endDate != null) {
			setEndDate(endDate);
		}

		Integer durationHours = (Integer)attributes.get("durationHours");

		if (durationHours != null) {
			setDurationHours(durationHours);
		}

		Integer durationMinutes = (Integer)attributes.get("durationMinutes");

		if (durationMinutes != null) {
			setDurationMinutes(durationMinutes);
		}

		Integer availability = (Integer)attributes.get("availability");

		if (availability != null) {
			setAvailability(availability);
		}

		String venue = (String)attributes.get("venue");

		if (venue != null) {
			setVenue(venue);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		String availablePax = (String)attributes.get("availablePax");

		if (availablePax != null) {
			setAvailablePax(availablePax);
		}

		String availableWaitlist = (String)attributes.get("availableWaitlist");

		if (availableWaitlist != null) {
			setAvailableWaitlist(availableWaitlist);
		}

		String lxpBuyUrl = (String)attributes.get("lxpBuyUrl");

		if (lxpBuyUrl != null) {
			setLxpBuyUrl(lxpBuyUrl);
		}

		String lxpRoiUrl = (String)attributes.get("lxpRoiUrl");

		if (lxpRoiUrl != null) {
			setLxpRoiUrl(lxpRoiUrl);
		}

		String importantNote = (String)attributes.get("importantNote");

		if (importantNote != null) {
			setImportantNote(importantNote);
		}

		String scheduleDownloadUrl = (String)attributes.get(
			"scheduleDownloadUrl");

		if (scheduleDownloadUrl != null) {
			setScheduleDownloadUrl(scheduleDownloadUrl);
		}

		String errorCode = (String)attributes.get("errorCode");

		if (errorCode != null) {
			setErrorCode(errorCode);
		}

		String errorMessage = (String)attributes.get("errorMessage");

		if (errorMessage != null) {
			setErrorMessage(errorMessage);
		}

		String rawJson = (String)attributes.get("rawJson");

		if (rawJson != null) {
			setRawJson(rawJson);
		}

		Long modifiedBy = (Long)attributes.get("modifiedBy");

		if (modifiedBy != null) {
			setModifiedBy(modifiedBy);
		}
	}

	/**
	 * Returns the availability of this course schedule.
	 *
	 * @return the availability of this course schedule
	 */
	@Override
	public int getAvailability() {
		return model.getAvailability();
	}

	/**
	 * Returns the available pax of this course schedule.
	 *
	 * @return the available pax of this course schedule
	 */
	@Override
	public String getAvailablePax() {
		return model.getAvailablePax();
	}

	/**
	 * Returns the available waitlist of this course schedule.
	 *
	 * @return the available waitlist of this course schedule
	 */
	@Override
	public String getAvailableWaitlist() {
		return model.getAvailableWaitlist();
	}

	/**
	 * Returns the company ID of this course schedule.
	 *
	 * @return the company ID of this course schedule
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the course code of this course schedule.
	 *
	 * @return the course code of this course schedule
	 */
	@Override
	public String getCourseCode() {
		return model.getCourseCode();
	}

	/**
	 * Returns the course schedule ID of this course schedule.
	 *
	 * @return the course schedule ID of this course schedule
	 */
	@Override
	public long getCourseScheduleId() {
		return model.getCourseScheduleId();
	}

	/**
	 * Returns the create date of this course schedule.
	 *
	 * @return the create date of this course schedule
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the description of this course schedule.
	 *
	 * @return the description of this course schedule
	 */
	@Override
	public String getDescription() {
		return model.getDescription();
	}

	/**
	 * Returns the duration hours of this course schedule.
	 *
	 * @return the duration hours of this course schedule
	 */
	@Override
	public int getDurationHours() {
		return model.getDurationHours();
	}

	/**
	 * Returns the duration minutes of this course schedule.
	 *
	 * @return the duration minutes of this course schedule
	 */
	@Override
	public int getDurationMinutes() {
		return model.getDurationMinutes();
	}

	/**
	 * Returns the end date of this course schedule.
	 *
	 * @return the end date of this course schedule
	 */
	@Override
	public Date getEndDate() {
		return model.getEndDate();
	}

	/**
	 * Returns the error code of this course schedule.
	 *
	 * @return the error code of this course schedule
	 */
	@Override
	public String getErrorCode() {
		return model.getErrorCode();
	}

	/**
	 * Returns the error message of this course schedule.
	 *
	 * @return the error message of this course schedule
	 */
	@Override
	public String getErrorMessage() {
		return model.getErrorMessage();
	}

	/**
	 * Returns the group ID of this course schedule.
	 *
	 * @return the group ID of this course schedule
	 */
	@Override
	public long getGroupId() {
		return model.getGroupId();
	}

	/**
	 * Returns the important note of this course schedule.
	 *
	 * @return the important note of this course schedule
	 */
	@Override
	public String getImportantNote() {
		return model.getImportantNote();
	}

	/**
	 * Returns the intake number of this course schedule.
	 *
	 * @return the intake number of this course schedule
	 */
	@Override
	public String getIntakeNumber() {
		return model.getIntakeNumber();
	}

	/**
	 * Returns the lxp buy url of this course schedule.
	 *
	 * @return the lxp buy url of this course schedule
	 */
	@Override
	public String getLxpBuyUrl() {
		return model.getLxpBuyUrl();
	}

	/**
	 * Returns the lxp roi url of this course schedule.
	 *
	 * @return the lxp roi url of this course schedule
	 */
	@Override
	public String getLxpRoiUrl() {
		return model.getLxpRoiUrl();
	}

	/**
	 * Returns the modified by of this course schedule.
	 *
	 * @return the modified by of this course schedule
	 */
	@Override
	public long getModifiedBy() {
		return model.getModifiedBy();
	}

	/**
	 * Returns the modified date of this course schedule.
	 *
	 * @return the modified date of this course schedule
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the primary key of this course schedule.
	 *
	 * @return the primary key of this course schedule
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the raw json of this course schedule.
	 *
	 * @return the raw json of this course schedule
	 */
	@Override
	public String getRawJson() {
		return model.getRawJson();
	}

	/**
	 * Returns the schedule download url of this course schedule.
	 *
	 * @return the schedule download url of this course schedule
	 */
	@Override
	public String getScheduleDownloadUrl() {
		return model.getScheduleDownloadUrl();
	}

	/**
	 * Returns the start date of this course schedule.
	 *
	 * @return the start date of this course schedule
	 */
	@Override
	public Date getStartDate() {
		return model.getStartDate();
	}

	/**
	 * Returns the uuid of this course schedule.
	 *
	 * @return the uuid of this course schedule
	 */
	@Override
	public String getUuid() {
		return model.getUuid();
	}

	/**
	 * Returns the venue of this course schedule.
	 *
	 * @return the venue of this course schedule
	 */
	@Override
	public String getVenue() {
		return model.getVenue();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the availability of this course schedule.
	 *
	 * @param availability the availability of this course schedule
	 */
	@Override
	public void setAvailability(int availability) {
		model.setAvailability(availability);
	}

	/**
	 * Sets the available pax of this course schedule.
	 *
	 * @param availablePax the available pax of this course schedule
	 */
	@Override
	public void setAvailablePax(String availablePax) {
		model.setAvailablePax(availablePax);
	}

	/**
	 * Sets the available waitlist of this course schedule.
	 *
	 * @param availableWaitlist the available waitlist of this course schedule
	 */
	@Override
	public void setAvailableWaitlist(String availableWaitlist) {
		model.setAvailableWaitlist(availableWaitlist);
	}

	/**
	 * Sets the company ID of this course schedule.
	 *
	 * @param companyId the company ID of this course schedule
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the course code of this course schedule.
	 *
	 * @param courseCode the course code of this course schedule
	 */
	@Override
	public void setCourseCode(String courseCode) {
		model.setCourseCode(courseCode);
	}

	/**
	 * Sets the course schedule ID of this course schedule.
	 *
	 * @param courseScheduleId the course schedule ID of this course schedule
	 */
	@Override
	public void setCourseScheduleId(long courseScheduleId) {
		model.setCourseScheduleId(courseScheduleId);
	}

	/**
	 * Sets the create date of this course schedule.
	 *
	 * @param createDate the create date of this course schedule
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the description of this course schedule.
	 *
	 * @param description the description of this course schedule
	 */
	@Override
	public void setDescription(String description) {
		model.setDescription(description);
	}

	/**
	 * Sets the duration hours of this course schedule.
	 *
	 * @param durationHours the duration hours of this course schedule
	 */
	@Override
	public void setDurationHours(int durationHours) {
		model.setDurationHours(durationHours);
	}

	/**
	 * Sets the duration minutes of this course schedule.
	 *
	 * @param durationMinutes the duration minutes of this course schedule
	 */
	@Override
	public void setDurationMinutes(int durationMinutes) {
		model.setDurationMinutes(durationMinutes);
	}

	/**
	 * Sets the end date of this course schedule.
	 *
	 * @param endDate the end date of this course schedule
	 */
	@Override
	public void setEndDate(Date endDate) {
		model.setEndDate(endDate);
	}

	/**
	 * Sets the error code of this course schedule.
	 *
	 * @param errorCode the error code of this course schedule
	 */
	@Override
	public void setErrorCode(String errorCode) {
		model.setErrorCode(errorCode);
	}

	/**
	 * Sets the error message of this course schedule.
	 *
	 * @param errorMessage the error message of this course schedule
	 */
	@Override
	public void setErrorMessage(String errorMessage) {
		model.setErrorMessage(errorMessage);
	}

	/**
	 * Sets the group ID of this course schedule.
	 *
	 * @param groupId the group ID of this course schedule
	 */
	@Override
	public void setGroupId(long groupId) {
		model.setGroupId(groupId);
	}

	/**
	 * Sets the important note of this course schedule.
	 *
	 * @param importantNote the important note of this course schedule
	 */
	@Override
	public void setImportantNote(String importantNote) {
		model.setImportantNote(importantNote);
	}

	/**
	 * Sets the intake number of this course schedule.
	 *
	 * @param intakeNumber the intake number of this course schedule
	 */
	@Override
	public void setIntakeNumber(String intakeNumber) {
		model.setIntakeNumber(intakeNumber);
	}

	/**
	 * Sets the lxp buy url of this course schedule.
	 *
	 * @param lxpBuyUrl the lxp buy url of this course schedule
	 */
	@Override
	public void setLxpBuyUrl(String lxpBuyUrl) {
		model.setLxpBuyUrl(lxpBuyUrl);
	}

	/**
	 * Sets the lxp roi url of this course schedule.
	 *
	 * @param lxpRoiUrl the lxp roi url of this course schedule
	 */
	@Override
	public void setLxpRoiUrl(String lxpRoiUrl) {
		model.setLxpRoiUrl(lxpRoiUrl);
	}

	/**
	 * Sets the modified by of this course schedule.
	 *
	 * @param modifiedBy the modified by of this course schedule
	 */
	@Override
	public void setModifiedBy(long modifiedBy) {
		model.setModifiedBy(modifiedBy);
	}

	/**
	 * Sets the modified date of this course schedule.
	 *
	 * @param modifiedDate the modified date of this course schedule
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the primary key of this course schedule.
	 *
	 * @param primaryKey the primary key of this course schedule
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the raw json of this course schedule.
	 *
	 * @param rawJson the raw json of this course schedule
	 */
	@Override
	public void setRawJson(String rawJson) {
		model.setRawJson(rawJson);
	}

	/**
	 * Sets the schedule download url of this course schedule.
	 *
	 * @param scheduleDownloadUrl the schedule download url of this course schedule
	 */
	@Override
	public void setScheduleDownloadUrl(String scheduleDownloadUrl) {
		model.setScheduleDownloadUrl(scheduleDownloadUrl);
	}

	/**
	 * Sets the start date of this course schedule.
	 *
	 * @param startDate the start date of this course schedule
	 */
	@Override
	public void setStartDate(Date startDate) {
		model.setStartDate(startDate);
	}

	/**
	 * Sets the uuid of this course schedule.
	 *
	 * @param uuid the uuid of this course schedule
	 */
	@Override
	public void setUuid(String uuid) {
		model.setUuid(uuid);
	}

	/**
	 * Sets the venue of this course schedule.
	 *
	 * @param venue the venue of this course schedule
	 */
	@Override
	public void setVenue(String venue) {
		model.setVenue(venue);
	}

	@Override
	public StagedModelType getStagedModelType() {
		return model.getStagedModelType();
	}

	@Override
	protected CourseScheduleWrapper wrap(CourseSchedule courseSchedule) {
		return new CourseScheduleWrapper(courseSchedule);
	}

}