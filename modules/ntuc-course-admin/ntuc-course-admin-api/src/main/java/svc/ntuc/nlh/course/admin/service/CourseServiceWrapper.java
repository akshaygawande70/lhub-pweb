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

package svc.ntuc.nlh.course.admin.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link CourseService}.
 *
 * @author Brian Wing Shun Chan
 * @see CourseService
 * @generated
 */
public class CourseServiceWrapper
	implements CourseService, ServiceWrapper<CourseService> {

	public CourseServiceWrapper(CourseService courseService) {
		_courseService = courseService;
	}

	@Override
	public com.liferay.portal.kernel.json.JSONArray getAllActiveCourse(
		long groupId) {

		return _courseService.getAllActiveCourse(groupId);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.course.admin.model.Course>
		getCourseByCourseCode(
			long groupId, String courseCode, boolean deleted) {

		return _courseService.getCourseByCourseCode(
			groupId, courseCode, deleted);
	}

	@Override
	public com.liferay.portal.kernel.json.JSONArray getCourseScheduleFromTMS(
		long groupId, String courseCode) {

		return _courseService.getCourseScheduleFromTMS(groupId, courseCode);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _courseService.getOSGiServiceIdentifier();
	}

	@Override
	public CourseService getWrappedService() {
		return _courseService;
	}

	@Override
	public void setWrappedService(CourseService courseService) {
		_courseService = courseService;
	}

	private CourseService _courseService;

}