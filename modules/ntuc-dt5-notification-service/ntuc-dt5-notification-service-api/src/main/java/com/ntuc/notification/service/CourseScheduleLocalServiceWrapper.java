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

package com.ntuc.notification.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link CourseScheduleLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see CourseScheduleLocalService
 * @generated
 */
public class CourseScheduleLocalServiceWrapper
	implements CourseScheduleLocalService,
			   ServiceWrapper<CourseScheduleLocalService> {

	public CourseScheduleLocalServiceWrapper(
		CourseScheduleLocalService courseScheduleLocalService) {

		_courseScheduleLocalService = courseScheduleLocalService;
	}

	/**
	 * Adds the course schedule to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CourseScheduleLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param courseSchedule the course schedule
	 * @return the course schedule that was added
	 */
	@Override
	public com.ntuc.notification.model.CourseSchedule addCourseSchedule(
		com.ntuc.notification.model.CourseSchedule courseSchedule) {

		return _courseScheduleLocalService.addCourseSchedule(courseSchedule);
	}

	/**
	 * Creates a new course schedule with the primary key. Does not add the course schedule to the database.
	 *
	 * @param courseScheduleId the primary key for the new course schedule
	 * @return the new course schedule
	 */
	@Override
	public com.ntuc.notification.model.CourseSchedule createCourseSchedule(
		long courseScheduleId) {

		return _courseScheduleLocalService.createCourseSchedule(
			courseScheduleId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseScheduleLocalService.createPersistedModel(primaryKeyObj);
	}

	/**
	 * Deletes the course schedule from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CourseScheduleLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param courseSchedule the course schedule
	 * @return the course schedule that was removed
	 */
	@Override
	public com.ntuc.notification.model.CourseSchedule deleteCourseSchedule(
		com.ntuc.notification.model.CourseSchedule courseSchedule) {

		return _courseScheduleLocalService.deleteCourseSchedule(courseSchedule);
	}

	/**
	 * Deletes the course schedule with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CourseScheduleLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param courseScheduleId the primary key of the course schedule
	 * @return the course schedule that was removed
	 * @throws PortalException if a course schedule with the primary key could not be found
	 */
	@Override
	public com.ntuc.notification.model.CourseSchedule deleteCourseSchedule(
			long courseScheduleId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseScheduleLocalService.deleteCourseSchedule(
			courseScheduleId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseScheduleLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _courseScheduleLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _courseScheduleLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.ntuc.notification.model.impl.CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _courseScheduleLocalService.dynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.ntuc.notification.model.impl.CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _courseScheduleLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _courseScheduleLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _courseScheduleLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.ntuc.notification.model.CourseSchedule fetchByCourseCode(
		String courseCode) {

		return _courseScheduleLocalService.fetchByCourseCode(courseCode);
	}

	@Override
	public com.ntuc.notification.model.CourseSchedule fetchCourseSchedule(
		long courseScheduleId) {

		return _courseScheduleLocalService.fetchCourseSchedule(
			courseScheduleId);
	}

	/**
	 * Returns the course schedule matching the UUID and group.
	 *
	 * @param uuid the course schedule's UUID
	 * @param groupId the primary key of the group
	 * @return the matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	@Override
	public com.ntuc.notification.model.CourseSchedule
		fetchCourseScheduleByUuidAndGroupId(String uuid, long groupId) {

		return _courseScheduleLocalService.fetchCourseScheduleByUuidAndGroupId(
			uuid, groupId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _courseScheduleLocalService.getActionableDynamicQuery();
	}

	@Override
	public java.util.List<com.ntuc.notification.model.CourseSchedule>
		getAllCourseSchedules() {

		return _courseScheduleLocalService.getAllCourseSchedules();
	}

	/**
	 * Returns the course schedule with the primary key.
	 *
	 * @param courseScheduleId the primary key of the course schedule
	 * @return the course schedule
	 * @throws PortalException if a course schedule with the primary key could not be found
	 */
	@Override
	public com.ntuc.notification.model.CourseSchedule getCourseSchedule(
			long courseScheduleId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseScheduleLocalService.getCourseSchedule(courseScheduleId);
	}

	/**
	 * Returns the course schedule matching the UUID and group.
	 *
	 * @param uuid the course schedule's UUID
	 * @param groupId the primary key of the group
	 * @return the matching course schedule
	 * @throws PortalException if a matching course schedule could not be found
	 */
	@Override
	public com.ntuc.notification.model.CourseSchedule
			getCourseScheduleByUuidAndGroupId(String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseScheduleLocalService.getCourseScheduleByUuidAndGroupId(
			uuid, groupId);
	}

	/**
	 * Returns a range of all the course schedules.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.ntuc.notification.model.impl.CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of course schedules
	 * @param end the upper bound of the range of course schedules (not inclusive)
	 * @return the range of course schedules
	 */
	@Override
	public java.util.List<com.ntuc.notification.model.CourseSchedule>
		getCourseSchedules(int start, int end) {

		return _courseScheduleLocalService.getCourseSchedules(start, end);
	}

	/**
	 * Returns all the course schedules matching the UUID and company.
	 *
	 * @param uuid the UUID of the course schedules
	 * @param companyId the primary key of the company
	 * @return the matching course schedules, or an empty list if no matches were found
	 */
	@Override
	public java.util.List<com.ntuc.notification.model.CourseSchedule>
		getCourseSchedulesByUuidAndCompanyId(String uuid, long companyId) {

		return _courseScheduleLocalService.getCourseSchedulesByUuidAndCompanyId(
			uuid, companyId);
	}

	/**
	 * Returns a range of course schedules matching the UUID and company.
	 *
	 * @param uuid the UUID of the course schedules
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of course schedules
	 * @param end the upper bound of the range of course schedules (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching course schedules, or an empty list if no matches were found
	 */
	@Override
	public java.util.List<com.ntuc.notification.model.CourseSchedule>
		getCourseSchedulesByUuidAndCompanyId(
			String uuid, long companyId, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.ntuc.notification.model.CourseSchedule>
					orderByComparator) {

		return _courseScheduleLocalService.getCourseSchedulesByUuidAndCompanyId(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the number of course schedules.
	 *
	 * @return the number of course schedules
	 */
	@Override
	public int getCourseSchedulesCount() {
		return _courseScheduleLocalService.getCourseSchedulesCount();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return _courseScheduleLocalService.getExportActionableDynamicQuery(
			portletDataContext);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _courseScheduleLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _courseScheduleLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseScheduleLocalService.getPersistedModel(primaryKeyObj);
	}

	@Override
	public String getScheduleForCourse(
			String courseCode,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseScheduleLocalService.getScheduleForCourse(
			courseCode, serviceContext);
	}

	@Override
	public com.ntuc.notification.model.ScheduleResponse getScheduleSnapshot(
		String courseCode) {

		return _courseScheduleLocalService.getScheduleSnapshot(courseCode);
	}

	/**
	 * Updates the course schedule in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CourseScheduleLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param courseSchedule the course schedule
	 * @return the course schedule that was updated
	 */
	@Override
	public com.ntuc.notification.model.CourseSchedule updateCourseSchedule(
		com.ntuc.notification.model.CourseSchedule courseSchedule) {

		return _courseScheduleLocalService.updateCourseSchedule(courseSchedule);
	}

	@Override
	public com.ntuc.notification.model.CourseSchedule updateScheduleFromAdmin(
			long courseScheduleId, String intakeNumber, String startDateString,
			String endDateString, Integer availability, String venue,
			Integer durationHours, Integer durationMinutes, String availablePax,
			String availableWaitlist, String lxpBuyUrl,
			String scheduleDownloadUrl, String errorCode, String errorMessage,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseScheduleLocalService.updateScheduleFromAdmin(
			courseScheduleId, intakeNumber, startDateString, endDateString,
			availability, venue, durationHours, durationMinutes, availablePax,
			availableWaitlist, lxpBuyUrl, scheduleDownloadUrl, errorCode,
			errorMessage, serviceContext);
	}

	@Override
	public com.ntuc.notification.model.CourseSchedule upsertFromCls(
			String courseCode, com.ntuc.notification.model.ScheduleResponse dto,
			com.liferay.portal.kernel.service.ServiceContext serviceContext,
			String rawJson)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseScheduleLocalService.upsertFromCls(
			courseCode, dto, serviceContext, rawJson);
	}

	@Override
	public CourseScheduleLocalService getWrappedService() {
		return _courseScheduleLocalService;
	}

	@Override
	public void setWrappedService(
		CourseScheduleLocalService courseScheduleLocalService) {

		_courseScheduleLocalService = courseScheduleLocalService;
	}

	private CourseScheduleLocalService _courseScheduleLocalService;

}