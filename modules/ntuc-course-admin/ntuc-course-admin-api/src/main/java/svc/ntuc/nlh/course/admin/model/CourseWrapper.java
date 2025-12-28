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

import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link Course}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Course
 * @generated
 */
public class CourseWrapper
	extends BaseModelWrapper<Course> implements Course, ModelWrapper<Course> {

	public CourseWrapper(Course course) {
		super(course);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("courseId", getCourseId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("endDate", getEndDate());
		attributes.put("venue", getVenue());
		attributes.put("allowOnlinePayment", isAllowOnlinePayment());
		attributes.put("courseTitle", getCourseTitle());
		attributes.put("allowWebRegistration", isAllowWebRegistration());
		attributes.put("description", getDescription());
		attributes.put("availability", getAvailability());
		attributes.put("batchId", getBatchId());
		attributes.put("webExpiry", getWebExpiry());
		attributes.put("fundedCourseFlag", isFundedCourseFlag());
		attributes.put("courseCode", getCourseCode());
		attributes.put("courseDuration", getCourseDuration());
		attributes.put("courseType", getCourseType());
		attributes.put("courseFee", getCourseFee());
		attributes.put("deleted", isDeleted());
		attributes.put("status", getStatus());
		attributes.put("startDate", getStartDate());
		attributes.put("updated", isUpdated());
		attributes.put("popular", isPopular());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long courseId = (Long)attributes.get("courseId");

		if (courseId != null) {
			setCourseId(courseId);
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

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Date endDate = (Date)attributes.get("endDate");

		if (endDate != null) {
			setEndDate(endDate);
		}

		String venue = (String)attributes.get("venue");

		if (venue != null) {
			setVenue(venue);
		}

		Boolean allowOnlinePayment = (Boolean)attributes.get(
			"allowOnlinePayment");

		if (allowOnlinePayment != null) {
			setAllowOnlinePayment(allowOnlinePayment);
		}

		String courseTitle = (String)attributes.get("courseTitle");

		if (courseTitle != null) {
			setCourseTitle(courseTitle);
		}

		Boolean allowWebRegistration = (Boolean)attributes.get(
			"allowWebRegistration");

		if (allowWebRegistration != null) {
			setAllowWebRegistration(allowWebRegistration);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		Integer availability = (Integer)attributes.get("availability");

		if (availability != null) {
			setAvailability(availability);
		}

		String batchId = (String)attributes.get("batchId");

		if (batchId != null) {
			setBatchId(batchId);
		}

		Date webExpiry = (Date)attributes.get("webExpiry");

		if (webExpiry != null) {
			setWebExpiry(webExpiry);
		}

		Boolean fundedCourseFlag = (Boolean)attributes.get("fundedCourseFlag");

		if (fundedCourseFlag != null) {
			setFundedCourseFlag(fundedCourseFlag);
		}

		String courseCode = (String)attributes.get("courseCode");

		if (courseCode != null) {
			setCourseCode(courseCode);
		}

		Double courseDuration = (Double)attributes.get("courseDuration");

		if (courseDuration != null) {
			setCourseDuration(courseDuration);
		}

		String courseType = (String)attributes.get("courseType");

		if (courseType != null) {
			setCourseType(courseType);
		}

		Double courseFee = (Double)attributes.get("courseFee");

		if (courseFee != null) {
			setCourseFee(courseFee);
		}

		Boolean deleted = (Boolean)attributes.get("deleted");

		if (deleted != null) {
			setDeleted(deleted);
		}

		String status = (String)attributes.get("status");

		if (status != null) {
			setStatus(status);
		}

		Date startDate = (Date)attributes.get("startDate");

		if (startDate != null) {
			setStartDate(startDate);
		}

		Boolean updated = (Boolean)attributes.get("updated");

		if (updated != null) {
			setUpdated(updated);
		}

		Boolean popular = (Boolean)attributes.get("popular");

		if (popular != null) {
			setPopular(popular);
		}
	}

	/**
	 * Returns the allow online payment of this course.
	 *
	 * @return the allow online payment of this course
	 */
	@Override
	public boolean getAllowOnlinePayment() {
		return model.getAllowOnlinePayment();
	}

	/**
	 * Returns the allow web registration of this course.
	 *
	 * @return the allow web registration of this course
	 */
	@Override
	public boolean getAllowWebRegistration() {
		return model.getAllowWebRegistration();
	}

	/**
	 * Returns the availability of this course.
	 *
	 * @return the availability of this course
	 */
	@Override
	public int getAvailability() {
		return model.getAvailability();
	}

	/**
	 * Returns the batch ID of this course.
	 *
	 * @return the batch ID of this course
	 */
	@Override
	public String getBatchId() {
		return model.getBatchId();
	}

	/**
	 * Returns the company ID of this course.
	 *
	 * @return the company ID of this course
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the course code of this course.
	 *
	 * @return the course code of this course
	 */
	@Override
	public String getCourseCode() {
		return model.getCourseCode();
	}

	/**
	 * Returns the course duration of this course.
	 *
	 * @return the course duration of this course
	 */
	@Override
	public Double getCourseDuration() {
		return model.getCourseDuration();
	}

	/**
	 * Returns the course fee of this course.
	 *
	 * @return the course fee of this course
	 */
	@Override
	public Double getCourseFee() {
		return model.getCourseFee();
	}

	/**
	 * Returns the course ID of this course.
	 *
	 * @return the course ID of this course
	 */
	@Override
	public long getCourseId() {
		return model.getCourseId();
	}

	/**
	 * Returns the course title of this course.
	 *
	 * @return the course title of this course
	 */
	@Override
	public String getCourseTitle() {
		return model.getCourseTitle();
	}

	/**
	 * Returns the course type of this course.
	 *
	 * @return the course type of this course
	 */
	@Override
	public String getCourseType() {
		return model.getCourseType();
	}

	/**
	 * Returns the create date of this course.
	 *
	 * @return the create date of this course
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the deleted of this course.
	 *
	 * @return the deleted of this course
	 */
	@Override
	public boolean getDeleted() {
		return model.getDeleted();
	}

	/**
	 * Returns the description of this course.
	 *
	 * @return the description of this course
	 */
	@Override
	public String getDescription() {
		return model.getDescription();
	}

	/**
	 * Returns the end date of this course.
	 *
	 * @return the end date of this course
	 */
	@Override
	public Date getEndDate() {
		return model.getEndDate();
	}

	/**
	 * Returns the funded course flag of this course.
	 *
	 * @return the funded course flag of this course
	 */
	@Override
	public boolean getFundedCourseFlag() {
		return model.getFundedCourseFlag();
	}

	/**
	 * Returns the group ID of this course.
	 *
	 * @return the group ID of this course
	 */
	@Override
	public long getGroupId() {
		return model.getGroupId();
	}

	/**
	 * Returns the modified date of this course.
	 *
	 * @return the modified date of this course
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the popular of this course.
	 *
	 * @return the popular of this course
	 */
	@Override
	public boolean getPopular() {
		return model.getPopular();
	}

	/**
	 * Returns the primary key of this course.
	 *
	 * @return the primary key of this course
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the start date of this course.
	 *
	 * @return the start date of this course
	 */
	@Override
	public Date getStartDate() {
		return model.getStartDate();
	}

	/**
	 * Returns the status of this course.
	 *
	 * @return the status of this course
	 */
	@Override
	public String getStatus() {
		return model.getStatus();
	}

	/**
	 * Returns the updated of this course.
	 *
	 * @return the updated of this course
	 */
	@Override
	public boolean getUpdated() {
		return model.getUpdated();
	}

	/**
	 * Returns the user ID of this course.
	 *
	 * @return the user ID of this course
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user name of this course.
	 *
	 * @return the user name of this course
	 */
	@Override
	public String getUserName() {
		return model.getUserName();
	}

	/**
	 * Returns the user uuid of this course.
	 *
	 * @return the user uuid of this course
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	/**
	 * Returns the uuid of this course.
	 *
	 * @return the uuid of this course
	 */
	@Override
	public String getUuid() {
		return model.getUuid();
	}

	/**
	 * Returns the venue of this course.
	 *
	 * @return the venue of this course
	 */
	@Override
	public String getVenue() {
		return model.getVenue();
	}

	/**
	 * Returns the web expiry of this course.
	 *
	 * @return the web expiry of this course
	 */
	@Override
	public Date getWebExpiry() {
		return model.getWebExpiry();
	}

	/**
	 * Returns <code>true</code> if this course is allow online payment.
	 *
	 * @return <code>true</code> if this course is allow online payment; <code>false</code> otherwise
	 */
	@Override
	public boolean isAllowOnlinePayment() {
		return model.isAllowOnlinePayment();
	}

	/**
	 * Returns <code>true</code> if this course is allow web registration.
	 *
	 * @return <code>true</code> if this course is allow web registration; <code>false</code> otherwise
	 */
	@Override
	public boolean isAllowWebRegistration() {
		return model.isAllowWebRegistration();
	}

	/**
	 * Returns <code>true</code> if this course is deleted.
	 *
	 * @return <code>true</code> if this course is deleted; <code>false</code> otherwise
	 */
	@Override
	public boolean isDeleted() {
		return model.isDeleted();
	}

	/**
	 * Returns <code>true</code> if this course is funded course flag.
	 *
	 * @return <code>true</code> if this course is funded course flag; <code>false</code> otherwise
	 */
	@Override
	public boolean isFundedCourseFlag() {
		return model.isFundedCourseFlag();
	}

	/**
	 * Returns <code>true</code> if this course is popular.
	 *
	 * @return <code>true</code> if this course is popular; <code>false</code> otherwise
	 */
	@Override
	public boolean isPopular() {
		return model.isPopular();
	}

	/**
	 * Returns <code>true</code> if this course is updated.
	 *
	 * @return <code>true</code> if this course is updated; <code>false</code> otherwise
	 */
	@Override
	public boolean isUpdated() {
		return model.isUpdated();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets whether this course is allow online payment.
	 *
	 * @param allowOnlinePayment the allow online payment of this course
	 */
	@Override
	public void setAllowOnlinePayment(boolean allowOnlinePayment) {
		model.setAllowOnlinePayment(allowOnlinePayment);
	}

	/**
	 * Sets whether this course is allow web registration.
	 *
	 * @param allowWebRegistration the allow web registration of this course
	 */
	@Override
	public void setAllowWebRegistration(boolean allowWebRegistration) {
		model.setAllowWebRegistration(allowWebRegistration);
	}

	/**
	 * Sets the availability of this course.
	 *
	 * @param availability the availability of this course
	 */
	@Override
	public void setAvailability(int availability) {
		model.setAvailability(availability);
	}

	/**
	 * Sets the batch ID of this course.
	 *
	 * @param batchId the batch ID of this course
	 */
	@Override
	public void setBatchId(String batchId) {
		model.setBatchId(batchId);
	}

	/**
	 * Sets the company ID of this course.
	 *
	 * @param companyId the company ID of this course
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the course code of this course.
	 *
	 * @param courseCode the course code of this course
	 */
	@Override
	public void setCourseCode(String courseCode) {
		model.setCourseCode(courseCode);
	}

	/**
	 * Sets the course duration of this course.
	 *
	 * @param courseDuration the course duration of this course
	 */
	@Override
	public void setCourseDuration(Double courseDuration) {
		model.setCourseDuration(courseDuration);
	}

	/**
	 * Sets the course fee of this course.
	 *
	 * @param courseFee the course fee of this course
	 */
	@Override
	public void setCourseFee(Double courseFee) {
		model.setCourseFee(courseFee);
	}

	/**
	 * Sets the course ID of this course.
	 *
	 * @param courseId the course ID of this course
	 */
	@Override
	public void setCourseId(long courseId) {
		model.setCourseId(courseId);
	}

	/**
	 * Sets the course title of this course.
	 *
	 * @param courseTitle the course title of this course
	 */
	@Override
	public void setCourseTitle(String courseTitle) {
		model.setCourseTitle(courseTitle);
	}

	/**
	 * Sets the course type of this course.
	 *
	 * @param courseType the course type of this course
	 */
	@Override
	public void setCourseType(String courseType) {
		model.setCourseType(courseType);
	}

	/**
	 * Sets the create date of this course.
	 *
	 * @param createDate the create date of this course
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets whether this course is deleted.
	 *
	 * @param deleted the deleted of this course
	 */
	@Override
	public void setDeleted(boolean deleted) {
		model.setDeleted(deleted);
	}

	/**
	 * Sets the description of this course.
	 *
	 * @param description the description of this course
	 */
	@Override
	public void setDescription(String description) {
		model.setDescription(description);
	}

	/**
	 * Sets the end date of this course.
	 *
	 * @param endDate the end date of this course
	 */
	@Override
	public void setEndDate(Date endDate) {
		model.setEndDate(endDate);
	}

	/**
	 * Sets whether this course is funded course flag.
	 *
	 * @param fundedCourseFlag the funded course flag of this course
	 */
	@Override
	public void setFundedCourseFlag(boolean fundedCourseFlag) {
		model.setFundedCourseFlag(fundedCourseFlag);
	}

	/**
	 * Sets the group ID of this course.
	 *
	 * @param groupId the group ID of this course
	 */
	@Override
	public void setGroupId(long groupId) {
		model.setGroupId(groupId);
	}

	/**
	 * Sets the modified date of this course.
	 *
	 * @param modifiedDate the modified date of this course
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets whether this course is popular.
	 *
	 * @param popular the popular of this course
	 */
	@Override
	public void setPopular(boolean popular) {
		model.setPopular(popular);
	}

	/**
	 * Sets the primary key of this course.
	 *
	 * @param primaryKey the primary key of this course
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the start date of this course.
	 *
	 * @param startDate the start date of this course
	 */
	@Override
	public void setStartDate(Date startDate) {
		model.setStartDate(startDate);
	}

	/**
	 * Sets the status of this course.
	 *
	 * @param status the status of this course
	 */
	@Override
	public void setStatus(String status) {
		model.setStatus(status);
	}

	/**
	 * Sets whether this course is updated.
	 *
	 * @param updated the updated of this course
	 */
	@Override
	public void setUpdated(boolean updated) {
		model.setUpdated(updated);
	}

	/**
	 * Sets the user ID of this course.
	 *
	 * @param userId the user ID of this course
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user name of this course.
	 *
	 * @param userName the user name of this course
	 */
	@Override
	public void setUserName(String userName) {
		model.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this course.
	 *
	 * @param userUuid the user uuid of this course
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	/**
	 * Sets the uuid of this course.
	 *
	 * @param uuid the uuid of this course
	 */
	@Override
	public void setUuid(String uuid) {
		model.setUuid(uuid);
	}

	/**
	 * Sets the venue of this course.
	 *
	 * @param venue the venue of this course
	 */
	@Override
	public void setVenue(String venue) {
		model.setVenue(venue);
	}

	/**
	 * Sets the web expiry of this course.
	 *
	 * @param webExpiry the web expiry of this course
	 */
	@Override
	public void setWebExpiry(Date webExpiry) {
		model.setWebExpiry(webExpiry);
	}

	@Override
	public StagedModelType getStagedModelType() {
		return model.getStagedModelType();
	}

	@Override
	protected CourseWrapper wrap(Course course) {
		return new CourseWrapper(course);
	}

}