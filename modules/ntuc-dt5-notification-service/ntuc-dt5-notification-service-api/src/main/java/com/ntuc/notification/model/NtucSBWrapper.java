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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link NtucSB}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see NtucSB
 * @generated
 */
public class NtucSBWrapper
	extends BaseModelWrapper<NtucSB> implements ModelWrapper<NtucSB>, NtucSB {

	public NtucSBWrapper(NtucSB ntucSB) {
		super(ntucSB);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("ntucDTId", getNtucDTId());
		attributes.put("companyId", getCompanyId());
		attributes.put("courseCode", getCourseCode());
		attributes.put("courseType", getCourseType());
		attributes.put("notificationDate", getNotificationDate());
		attributes.put("systemDate", getSystemDate());
		attributes.put("notificationId", getNotificationId());
		attributes.put("event", getEvent());
		attributes.put("changeFrom", getChangeFrom());
		attributes.put("processingStatus", getProcessingStatus());
		attributes.put("courseStatus", getCourseStatus());
		attributes.put("isCriticalProcessed", isIsCriticalProcessed());
		attributes.put("isNonCriticalProcessed", isIsNonCriticalProcessed());
		attributes.put("isCronProcessed", isIsCronProcessed());
		attributes.put("canRetry", isCanRetry());
		attributes.put("lastRetried", getLastRetried());
		attributes.put("totalRetries", getTotalRetries());
		attributes.put("isRowLockFailed", isIsRowLockFailed());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long ntucDTId = (Long)attributes.get("ntucDTId");

		if (ntucDTId != null) {
			setNtucDTId(ntucDTId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		String courseCode = (String)attributes.get("courseCode");

		if (courseCode != null) {
			setCourseCode(courseCode);
		}

		String courseType = (String)attributes.get("courseType");

		if (courseType != null) {
			setCourseType(courseType);
		}

		Date notificationDate = (Date)attributes.get("notificationDate");

		if (notificationDate != null) {
			setNotificationDate(notificationDate);
		}

		Date systemDate = (Date)attributes.get("systemDate");

		if (systemDate != null) {
			setSystemDate(systemDate);
		}

		String notificationId = (String)attributes.get("notificationId");

		if (notificationId != null) {
			setNotificationId(notificationId);
		}

		String event = (String)attributes.get("event");

		if (event != null) {
			setEvent(event);
		}

		String changeFrom = (String)attributes.get("changeFrom");

		if (changeFrom != null) {
			setChangeFrom(changeFrom);
		}

		String processingStatus = (String)attributes.get("processingStatus");

		if (processingStatus != null) {
			setProcessingStatus(processingStatus);
		}

		String courseStatus = (String)attributes.get("courseStatus");

		if (courseStatus != null) {
			setCourseStatus(courseStatus);
		}

		Boolean isCriticalProcessed = (Boolean)attributes.get(
			"isCriticalProcessed");

		if (isCriticalProcessed != null) {
			setIsCriticalProcessed(isCriticalProcessed);
		}

		Boolean isNonCriticalProcessed = (Boolean)attributes.get(
			"isNonCriticalProcessed");

		if (isNonCriticalProcessed != null) {
			setIsNonCriticalProcessed(isNonCriticalProcessed);
		}

		Boolean isCronProcessed = (Boolean)attributes.get("isCronProcessed");

		if (isCronProcessed != null) {
			setIsCronProcessed(isCronProcessed);
		}

		Boolean canRetry = (Boolean)attributes.get("canRetry");

		if (canRetry != null) {
			setCanRetry(canRetry);
		}

		Date lastRetried = (Date)attributes.get("lastRetried");

		if (lastRetried != null) {
			setLastRetried(lastRetried);
		}

		Integer totalRetries = (Integer)attributes.get("totalRetries");

		if (totalRetries != null) {
			setTotalRetries(totalRetries);
		}

		Boolean isRowLockFailed = (Boolean)attributes.get("isRowLockFailed");

		if (isRowLockFailed != null) {
			setIsRowLockFailed(isRowLockFailed);
		}
	}

	/**
	 * Returns the can retry of this ntuc sb.
	 *
	 * @return the can retry of this ntuc sb
	 */
	@Override
	public boolean getCanRetry() {
		return model.getCanRetry();
	}

	/**
	 * Returns the change from of this ntuc sb.
	 *
	 * @return the change from of this ntuc sb
	 */
	@Override
	public String getChangeFrom() {
		return model.getChangeFrom();
	}

	/**
	 * Returns the company ID of this ntuc sb.
	 *
	 * @return the company ID of this ntuc sb
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the course code of this ntuc sb.
	 *
	 * @return the course code of this ntuc sb
	 */
	@Override
	public String getCourseCode() {
		return model.getCourseCode();
	}

	/**
	 * Returns the course status of this ntuc sb.
	 *
	 * @return the course status of this ntuc sb
	 */
	@Override
	public String getCourseStatus() {
		return model.getCourseStatus();
	}

	/**
	 * Returns the course type of this ntuc sb.
	 *
	 * @return the course type of this ntuc sb
	 */
	@Override
	public String getCourseType() {
		return model.getCourseType();
	}

	/**
	 * Returns the event of this ntuc sb.
	 *
	 * @return the event of this ntuc sb
	 */
	@Override
	public String getEvent() {
		return model.getEvent();
	}

	/**
	 * Returns the is critical processed of this ntuc sb.
	 *
	 * @return the is critical processed of this ntuc sb
	 */
	@Override
	public boolean getIsCriticalProcessed() {
		return model.getIsCriticalProcessed();
	}

	/**
	 * Returns the is cron processed of this ntuc sb.
	 *
	 * @return the is cron processed of this ntuc sb
	 */
	@Override
	public boolean getIsCronProcessed() {
		return model.getIsCronProcessed();
	}

	/**
	 * Returns the is non critical processed of this ntuc sb.
	 *
	 * @return the is non critical processed of this ntuc sb
	 */
	@Override
	public boolean getIsNonCriticalProcessed() {
		return model.getIsNonCriticalProcessed();
	}

	/**
	 * Returns the is row lock failed of this ntuc sb.
	 *
	 * @return the is row lock failed of this ntuc sb
	 */
	@Override
	public boolean getIsRowLockFailed() {
		return model.getIsRowLockFailed();
	}

	/**
	 * Returns the last retried of this ntuc sb.
	 *
	 * @return the last retried of this ntuc sb
	 */
	@Override
	public Date getLastRetried() {
		return model.getLastRetried();
	}

	/**
	 * Returns the notification date of this ntuc sb.
	 *
	 * @return the notification date of this ntuc sb
	 */
	@Override
	public Date getNotificationDate() {
		return model.getNotificationDate();
	}

	/**
	 * Returns the notification ID of this ntuc sb.
	 *
	 * @return the notification ID of this ntuc sb
	 */
	@Override
	public String getNotificationId() {
		return model.getNotificationId();
	}

	/**
	 * Returns the ntuc dt ID of this ntuc sb.
	 *
	 * @return the ntuc dt ID of this ntuc sb
	 */
	@Override
	public long getNtucDTId() {
		return model.getNtucDTId();
	}

	/**
	 * Returns the primary key of this ntuc sb.
	 *
	 * @return the primary key of this ntuc sb
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the processing status of this ntuc sb.
	 *
	 * @return the processing status of this ntuc sb
	 */
	@Override
	public String getProcessingStatus() {
		return model.getProcessingStatus();
	}

	/**
	 * Returns the system date of this ntuc sb.
	 *
	 * @return the system date of this ntuc sb
	 */
	@Override
	public Date getSystemDate() {
		return model.getSystemDate();
	}

	/**
	 * Returns the total retries of this ntuc sb.
	 *
	 * @return the total retries of this ntuc sb
	 */
	@Override
	public int getTotalRetries() {
		return model.getTotalRetries();
	}

	/**
	 * Returns <code>true</code> if this ntuc sb is can retry.
	 *
	 * @return <code>true</code> if this ntuc sb is can retry; <code>false</code> otherwise
	 */
	@Override
	public boolean isCanRetry() {
		return model.isCanRetry();
	}

	/**
	 * Returns <code>true</code> if this ntuc sb is is critical processed.
	 *
	 * @return <code>true</code> if this ntuc sb is is critical processed; <code>false</code> otherwise
	 */
	@Override
	public boolean isIsCriticalProcessed() {
		return model.isIsCriticalProcessed();
	}

	/**
	 * Returns <code>true</code> if this ntuc sb is is cron processed.
	 *
	 * @return <code>true</code> if this ntuc sb is is cron processed; <code>false</code> otherwise
	 */
	@Override
	public boolean isIsCronProcessed() {
		return model.isIsCronProcessed();
	}

	/**
	 * Returns <code>true</code> if this ntuc sb is is non critical processed.
	 *
	 * @return <code>true</code> if this ntuc sb is is non critical processed; <code>false</code> otherwise
	 */
	@Override
	public boolean isIsNonCriticalProcessed() {
		return model.isIsNonCriticalProcessed();
	}

	/**
	 * Returns <code>true</code> if this ntuc sb is is row lock failed.
	 *
	 * @return <code>true</code> if this ntuc sb is is row lock failed; <code>false</code> otherwise
	 */
	@Override
	public boolean isIsRowLockFailed() {
		return model.isIsRowLockFailed();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets whether this ntuc sb is can retry.
	 *
	 * @param canRetry the can retry of this ntuc sb
	 */
	@Override
	public void setCanRetry(boolean canRetry) {
		model.setCanRetry(canRetry);
	}

	/**
	 * Sets the change from of this ntuc sb.
	 *
	 * @param changeFrom the change from of this ntuc sb
	 */
	@Override
	public void setChangeFrom(String changeFrom) {
		model.setChangeFrom(changeFrom);
	}

	/**
	 * Sets the company ID of this ntuc sb.
	 *
	 * @param companyId the company ID of this ntuc sb
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the course code of this ntuc sb.
	 *
	 * @param courseCode the course code of this ntuc sb
	 */
	@Override
	public void setCourseCode(String courseCode) {
		model.setCourseCode(courseCode);
	}

	/**
	 * Sets the course status of this ntuc sb.
	 *
	 * @param courseStatus the course status of this ntuc sb
	 */
	@Override
	public void setCourseStatus(String courseStatus) {
		model.setCourseStatus(courseStatus);
	}

	/**
	 * Sets the course type of this ntuc sb.
	 *
	 * @param courseType the course type of this ntuc sb
	 */
	@Override
	public void setCourseType(String courseType) {
		model.setCourseType(courseType);
	}

	/**
	 * Sets the event of this ntuc sb.
	 *
	 * @param event the event of this ntuc sb
	 */
	@Override
	public void setEvent(String event) {
		model.setEvent(event);
	}

	/**
	 * Sets whether this ntuc sb is is critical processed.
	 *
	 * @param isCriticalProcessed the is critical processed of this ntuc sb
	 */
	@Override
	public void setIsCriticalProcessed(boolean isCriticalProcessed) {
		model.setIsCriticalProcessed(isCriticalProcessed);
	}

	/**
	 * Sets whether this ntuc sb is is cron processed.
	 *
	 * @param isCronProcessed the is cron processed of this ntuc sb
	 */
	@Override
	public void setIsCronProcessed(boolean isCronProcessed) {
		model.setIsCronProcessed(isCronProcessed);
	}

	/**
	 * Sets whether this ntuc sb is is non critical processed.
	 *
	 * @param isNonCriticalProcessed the is non critical processed of this ntuc sb
	 */
	@Override
	public void setIsNonCriticalProcessed(boolean isNonCriticalProcessed) {
		model.setIsNonCriticalProcessed(isNonCriticalProcessed);
	}

	/**
	 * Sets whether this ntuc sb is is row lock failed.
	 *
	 * @param isRowLockFailed the is row lock failed of this ntuc sb
	 */
	@Override
	public void setIsRowLockFailed(boolean isRowLockFailed) {
		model.setIsRowLockFailed(isRowLockFailed);
	}

	/**
	 * Sets the last retried of this ntuc sb.
	 *
	 * @param lastRetried the last retried of this ntuc sb
	 */
	@Override
	public void setLastRetried(Date lastRetried) {
		model.setLastRetried(lastRetried);
	}

	/**
	 * Sets the notification date of this ntuc sb.
	 *
	 * @param notificationDate the notification date of this ntuc sb
	 */
	@Override
	public void setNotificationDate(Date notificationDate) {
		model.setNotificationDate(notificationDate);
	}

	/**
	 * Sets the notification ID of this ntuc sb.
	 *
	 * @param notificationId the notification ID of this ntuc sb
	 */
	@Override
	public void setNotificationId(String notificationId) {
		model.setNotificationId(notificationId);
	}

	/**
	 * Sets the ntuc dt ID of this ntuc sb.
	 *
	 * @param ntucDTId the ntuc dt ID of this ntuc sb
	 */
	@Override
	public void setNtucDTId(long ntucDTId) {
		model.setNtucDTId(ntucDTId);
	}

	/**
	 * Sets the primary key of this ntuc sb.
	 *
	 * @param primaryKey the primary key of this ntuc sb
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the processing status of this ntuc sb.
	 *
	 * @param processingStatus the processing status of this ntuc sb
	 */
	@Override
	public void setProcessingStatus(String processingStatus) {
		model.setProcessingStatus(processingStatus);
	}

	/**
	 * Sets the system date of this ntuc sb.
	 *
	 * @param systemDate the system date of this ntuc sb
	 */
	@Override
	public void setSystemDate(Date systemDate) {
		model.setSystemDate(systemDate);
	}

	/**
	 * Sets the total retries of this ntuc sb.
	 *
	 * @param totalRetries the total retries of this ntuc sb
	 */
	@Override
	public void setTotalRetries(int totalRetries) {
		model.setTotalRetries(totalRetries);
	}

	@Override
	protected NtucSBWrapper wrap(NtucSB ntucSB) {
		return new NtucSBWrapper(ntucSB);
	}

}