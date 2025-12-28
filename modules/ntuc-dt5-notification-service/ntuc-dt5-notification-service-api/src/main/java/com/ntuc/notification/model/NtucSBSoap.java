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
 * This class is used by SOAP remote services, specifically {@link com.ntuc.notification.service.http.NtucSBServiceSoap}.
 *
 * @author Brian Wing Shun Chan
 * @deprecated As of Athanasius (7.3.x), with no direct replacement
 * @generated
 */
@Deprecated
public class NtucSBSoap implements Serializable {

	public static NtucSBSoap toSoapModel(NtucSB model) {
		NtucSBSoap soapModel = new NtucSBSoap();

		soapModel.setNtucDTId(model.getNtucDTId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setCourseCode(model.getCourseCode());
		soapModel.setCourseType(model.getCourseType());
		soapModel.setNotificationDate(model.getNotificationDate());
		soapModel.setSystemDate(model.getSystemDate());
		soapModel.setNotificationId(model.getNotificationId());
		soapModel.setEvent(model.getEvent());
		soapModel.setChangeFrom(model.getChangeFrom());
		soapModel.setProcessingStatus(model.getProcessingStatus());
		soapModel.setCourseStatus(model.getCourseStatus());
		soapModel.setIsCriticalProcessed(model.isIsCriticalProcessed());
		soapModel.setIsNonCriticalProcessed(model.isIsNonCriticalProcessed());
		soapModel.setIsCronProcessed(model.isIsCronProcessed());
		soapModel.setCanRetry(model.isCanRetry());
		soapModel.setLastRetried(model.getLastRetried());
		soapModel.setTotalRetries(model.getTotalRetries());
		soapModel.setIsRowLockFailed(model.isIsRowLockFailed());

		return soapModel;
	}

	public static NtucSBSoap[] toSoapModels(NtucSB[] models) {
		NtucSBSoap[] soapModels = new NtucSBSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static NtucSBSoap[][] toSoapModels(NtucSB[][] models) {
		NtucSBSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new NtucSBSoap[models.length][models[0].length];
		}
		else {
			soapModels = new NtucSBSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static NtucSBSoap[] toSoapModels(List<NtucSB> models) {
		List<NtucSBSoap> soapModels = new ArrayList<NtucSBSoap>(models.size());

		for (NtucSB model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new NtucSBSoap[soapModels.size()]);
	}

	public NtucSBSoap() {
	}

	public long getPrimaryKey() {
		return _ntucDTId;
	}

	public void setPrimaryKey(long pk) {
		setNtucDTId(pk);
	}

	public long getNtucDTId() {
		return _ntucDTId;
	}

	public void setNtucDTId(long ntucDTId) {
		_ntucDTId = ntucDTId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public String getCourseCode() {
		return _courseCode;
	}

	public void setCourseCode(String courseCode) {
		_courseCode = courseCode;
	}

	public String getCourseType() {
		return _courseType;
	}

	public void setCourseType(String courseType) {
		_courseType = courseType;
	}

	public Date getNotificationDate() {
		return _notificationDate;
	}

	public void setNotificationDate(Date notificationDate) {
		_notificationDate = notificationDate;
	}

	public Date getSystemDate() {
		return _systemDate;
	}

	public void setSystemDate(Date systemDate) {
		_systemDate = systemDate;
	}

	public String getNotificationId() {
		return _notificationId;
	}

	public void setNotificationId(String notificationId) {
		_notificationId = notificationId;
	}

	public String getEvent() {
		return _event;
	}

	public void setEvent(String event) {
		_event = event;
	}

	public String getChangeFrom() {
		return _changeFrom;
	}

	public void setChangeFrom(String changeFrom) {
		_changeFrom = changeFrom;
	}

	public String getProcessingStatus() {
		return _processingStatus;
	}

	public void setProcessingStatus(String processingStatus) {
		_processingStatus = processingStatus;
	}

	public String getCourseStatus() {
		return _courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		_courseStatus = courseStatus;
	}

	public boolean getIsCriticalProcessed() {
		return _isCriticalProcessed;
	}

	public boolean isIsCriticalProcessed() {
		return _isCriticalProcessed;
	}

	public void setIsCriticalProcessed(boolean isCriticalProcessed) {
		_isCriticalProcessed = isCriticalProcessed;
	}

	public boolean getIsNonCriticalProcessed() {
		return _isNonCriticalProcessed;
	}

	public boolean isIsNonCriticalProcessed() {
		return _isNonCriticalProcessed;
	}

	public void setIsNonCriticalProcessed(boolean isNonCriticalProcessed) {
		_isNonCriticalProcessed = isNonCriticalProcessed;
	}

	public boolean getIsCronProcessed() {
		return _isCronProcessed;
	}

	public boolean isIsCronProcessed() {
		return _isCronProcessed;
	}

	public void setIsCronProcessed(boolean isCronProcessed) {
		_isCronProcessed = isCronProcessed;
	}

	public boolean getCanRetry() {
		return _canRetry;
	}

	public boolean isCanRetry() {
		return _canRetry;
	}

	public void setCanRetry(boolean canRetry) {
		_canRetry = canRetry;
	}

	public Date getLastRetried() {
		return _lastRetried;
	}

	public void setLastRetried(Date lastRetried) {
		_lastRetried = lastRetried;
	}

	public int getTotalRetries() {
		return _totalRetries;
	}

	public void setTotalRetries(int totalRetries) {
		_totalRetries = totalRetries;
	}

	public boolean getIsRowLockFailed() {
		return _isRowLockFailed;
	}

	public boolean isIsRowLockFailed() {
		return _isRowLockFailed;
	}

	public void setIsRowLockFailed(boolean isRowLockFailed) {
		_isRowLockFailed = isRowLockFailed;
	}

	private long _ntucDTId;
	private long _companyId;
	private String _courseCode;
	private String _courseType;
	private Date _notificationDate;
	private Date _systemDate;
	private String _notificationId;
	private String _event;
	private String _changeFrom;
	private String _processingStatus;
	private String _courseStatus;
	private boolean _isCriticalProcessed;
	private boolean _isNonCriticalProcessed;
	private boolean _isCronProcessed;
	private boolean _canRetry;
	private Date _lastRetried;
	private int _totalRetries;
	private boolean _isRowLockFailed;

}