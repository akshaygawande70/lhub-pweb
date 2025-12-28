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

package svc.ntuc.nlh.course.admin.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link svc.ntuc.nlh.course.admin.service.http.CourseServiceSoap}.
 *
 * @author Brian Wing Shun Chan
 * @deprecated As of Athanasius (7.3.x), with no direct replacement
 * @generated
 */
@Deprecated
public class CourseSoap implements Serializable {

	public static CourseSoap toSoapModel(Course model) {
		CourseSoap soapModel = new CourseSoap();

		soapModel.setUuid(model.getUuid());
		soapModel.setCourseId(model.getCourseId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setEndDate(model.getEndDate());
		soapModel.setVenue(model.getVenue());
		soapModel.setAllowOnlinePayment(model.isAllowOnlinePayment());
		soapModel.setCourseTitle(model.getCourseTitle());
		soapModel.setAllowWebRegistration(model.isAllowWebRegistration());
		soapModel.setDescription(model.getDescription());
		soapModel.setAvailability(model.getAvailability());
		soapModel.setBatchId(model.getBatchId());
		soapModel.setWebExpiry(model.getWebExpiry());
		soapModel.setFundedCourseFlag(model.isFundedCourseFlag());
		soapModel.setCourseCode(model.getCourseCode());
		soapModel.setCourseDuration(model.getCourseDuration());
		soapModel.setCourseType(model.getCourseType());
		soapModel.setCourseFee(model.getCourseFee());
		soapModel.setDeleted(model.isDeleted());
		soapModel.setStatus(model.getStatus());
		soapModel.setStartDate(model.getStartDate());
		soapModel.setUpdated(model.isUpdated());
		soapModel.setPopular(model.isPopular());

		return soapModel;
	}

	public static CourseSoap[] toSoapModels(Course[] models) {
		CourseSoap[] soapModels = new CourseSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static CourseSoap[][] toSoapModels(Course[][] models) {
		CourseSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new CourseSoap[models.length][models[0].length];
		}
		else {
			soapModels = new CourseSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static CourseSoap[] toSoapModels(List<Course> models) {
		List<CourseSoap> soapModels = new ArrayList<CourseSoap>(models.size());

		for (Course model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new CourseSoap[soapModels.size()]);
	}

	public CourseSoap() {
	}

	public long getPrimaryKey() {
		return _courseId;
	}

	public void setPrimaryKey(long pk) {
		setCourseId(pk);
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getCourseId() {
		return _courseId;
	}

	public void setCourseId(long courseId) {
		_courseId = courseId;
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

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
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

	public Date getEndDate() {
		return _endDate;
	}

	public void setEndDate(Date endDate) {
		_endDate = endDate;
	}

	public String getVenue() {
		return _venue;
	}

	public void setVenue(String venue) {
		_venue = venue;
	}

	public boolean getAllowOnlinePayment() {
		return _allowOnlinePayment;
	}

	public boolean isAllowOnlinePayment() {
		return _allowOnlinePayment;
	}

	public void setAllowOnlinePayment(boolean allowOnlinePayment) {
		_allowOnlinePayment = allowOnlinePayment;
	}

	public String getCourseTitle() {
		return _courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		_courseTitle = courseTitle;
	}

	public boolean getAllowWebRegistration() {
		return _allowWebRegistration;
	}

	public boolean isAllowWebRegistration() {
		return _allowWebRegistration;
	}

	public void setAllowWebRegistration(boolean allowWebRegistration) {
		_allowWebRegistration = allowWebRegistration;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public int getAvailability() {
		return _availability;
	}

	public void setAvailability(int availability) {
		_availability = availability;
	}

	public String getBatchId() {
		return _batchId;
	}

	public void setBatchId(String batchId) {
		_batchId = batchId;
	}

	public Date getWebExpiry() {
		return _webExpiry;
	}

	public void setWebExpiry(Date webExpiry) {
		_webExpiry = webExpiry;
	}

	public boolean getFundedCourseFlag() {
		return _fundedCourseFlag;
	}

	public boolean isFundedCourseFlag() {
		return _fundedCourseFlag;
	}

	public void setFundedCourseFlag(boolean fundedCourseFlag) {
		_fundedCourseFlag = fundedCourseFlag;
	}

	public String getCourseCode() {
		return _courseCode;
	}

	public void setCourseCode(String courseCode) {
		_courseCode = courseCode;
	}

	public Double getCourseDuration() {
		return _courseDuration;
	}

	public void setCourseDuration(Double courseDuration) {
		_courseDuration = courseDuration;
	}

	public String getCourseType() {
		return _courseType;
	}

	public void setCourseType(String courseType) {
		_courseType = courseType;
	}

	public Double getCourseFee() {
		return _courseFee;
	}

	public void setCourseFee(Double courseFee) {
		_courseFee = courseFee;
	}

	public boolean getDeleted() {
		return _deleted;
	}

	public boolean isDeleted() {
		return _deleted;
	}

	public void setDeleted(boolean deleted) {
		_deleted = deleted;
	}

	public String getStatus() {
		return _status;
	}

	public void setStatus(String status) {
		_status = status;
	}

	public Date getStartDate() {
		return _startDate;
	}

	public void setStartDate(Date startDate) {
		_startDate = startDate;
	}

	public boolean getUpdated() {
		return _updated;
	}

	public boolean isUpdated() {
		return _updated;
	}

	public void setUpdated(boolean updated) {
		_updated = updated;
	}

	public boolean getPopular() {
		return _popular;
	}

	public boolean isPopular() {
		return _popular;
	}

	public void setPopular(boolean popular) {
		_popular = popular;
	}

	private String _uuid;
	private long _courseId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private Date _endDate;
	private String _venue;
	private boolean _allowOnlinePayment;
	private String _courseTitle;
	private boolean _allowWebRegistration;
	private String _description;
	private int _availability;
	private String _batchId;
	private Date _webExpiry;
	private boolean _fundedCourseFlag;
	private String _courseCode;
	private Double _courseDuration;
	private String _courseType;
	private Double _courseFee;
	private boolean _deleted;
	private String _status;
	private Date _startDate;
	private boolean _updated;
	private boolean _popular;

}