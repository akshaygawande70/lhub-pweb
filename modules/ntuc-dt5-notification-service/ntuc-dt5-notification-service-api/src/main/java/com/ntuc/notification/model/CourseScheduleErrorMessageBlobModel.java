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

import java.sql.Blob;

/**
 * The Blob model class for lazy loading the errorMessage column in CourseSchedule.
 *
 * @author Brian Wing Shun Chan
 * @see CourseSchedule
 * @generated
 */
public class CourseScheduleErrorMessageBlobModel {

	public CourseScheduleErrorMessageBlobModel() {
	}

	public CourseScheduleErrorMessageBlobModel(long courseScheduleId) {
		_courseScheduleId = courseScheduleId;
	}

	public CourseScheduleErrorMessageBlobModel(
		long courseScheduleId, Blob errorMessageBlob) {

		_courseScheduleId = courseScheduleId;
		_errorMessageBlob = errorMessageBlob;
	}

	public long getCourseScheduleId() {
		return _courseScheduleId;
	}

	public void setCourseScheduleId(long courseScheduleId) {
		_courseScheduleId = courseScheduleId;
	}

	public Blob getErrorMessageBlob() {
		return _errorMessageBlob;
	}

	public void setErrorMessageBlob(Blob errorMessageBlob) {
		_errorMessageBlob = errorMessageBlob;
	}

	private long _courseScheduleId;
	private Blob _errorMessageBlob;

}