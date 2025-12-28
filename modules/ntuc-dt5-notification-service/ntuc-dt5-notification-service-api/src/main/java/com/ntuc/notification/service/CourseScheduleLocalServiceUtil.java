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

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.ntuc.notification.model.CourseSchedule;
import com.ntuc.notification.model.ScheduleResponse;

import java.io.Serializable;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for CourseSchedule. This utility wraps
 * <code>com.ntuc.notification.service.impl.CourseScheduleLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see CourseScheduleLocalService
 * @generated
 */
public class CourseScheduleLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.ntuc.notification.service.impl.CourseScheduleLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

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
	public static CourseSchedule addCourseSchedule(
		CourseSchedule courseSchedule) {

		return getService().addCourseSchedule(courseSchedule);
	}

	/**
	 * Creates a new course schedule with the primary key. Does not add the course schedule to the database.
	 *
	 * @param courseScheduleId the primary key for the new course schedule
	 * @return the new course schedule
	 */
	public static CourseSchedule
		createCourseSchedule(long courseScheduleId) {

		return getService().createCourseSchedule(courseScheduleId);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel
			createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return getService().createPersistedModel(primaryKeyObj);
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
	public static CourseSchedule
		deleteCourseSchedule(
			CourseSchedule courseSchedule) {

		return getService().deleteCourseSchedule(courseSchedule);
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
	public static CourseSchedule
			deleteCourseSchedule(long courseScheduleId)
		throws PortalException {

		return getService().deleteCourseSchedule(courseScheduleId);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel
			deletePersistedModel(
				PersistedModel persistedModel)
		throws PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static DynamicQuery
		dynamicQuery() {

		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery) {

		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>impl.CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start,
		int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>impl.CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		DynamicQuery dynamicQuery) {

		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		DynamicQuery dynamicQuery,
		Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static CourseSchedule fetchByCourseCode(
		String courseCode) {

		return getService().fetchByCourseCode(courseCode);
	}

	public static CourseSchedule
		fetchCourseSchedule(long courseScheduleId) {

		return getService().fetchCourseSchedule(courseScheduleId);
	}

	/**
	 * Returns the course schedule matching the UUID and group.
	 *
	 * @param uuid the course schedule's UUID
	 * @param groupId the primary key of the group
	 * @return the matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public static CourseSchedule
		fetchCourseScheduleByUuidAndGroupId(String uuid, long groupId) {

		return getService().fetchCourseScheduleByUuidAndGroupId(uuid, groupId);
	}

	public static ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static List<CourseSchedule>
		getAllCourseSchedules() {

		return getService().getAllCourseSchedules();
	}

	/**
	 * Returns the course schedule with the primary key.
	 *
	 * @param courseScheduleId the primary key of the course schedule
	 * @return the course schedule
	 * @throws PortalException if a course schedule with the primary key could not be found
	 */
	public static CourseSchedule getCourseSchedule(
			long courseScheduleId)
		throws PortalException {

		return getService().getCourseSchedule(courseScheduleId);
	}

	/**
	 * Returns the course schedule matching the UUID and group.
	 *
	 * @param uuid the course schedule's UUID
	 * @param groupId the primary key of the group
	 * @return the matching course schedule
	 * @throws PortalException if a matching course schedule could not be found
	 */
	public static CourseSchedule
			getCourseScheduleByUuidAndGroupId(String uuid, long groupId)
		throws PortalException {

		return getService().getCourseScheduleByUuidAndGroupId(uuid, groupId);
	}

	/**
	 * Returns a range of all the course schedules.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>impl.CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of course schedules
	 * @param end the upper bound of the range of course schedules (not inclusive)
	 * @return the range of course schedules
	 */
	public static List<CourseSchedule>
		getCourseSchedules(int start, int end) {

		return getService().getCourseSchedules(start, end);
	}

	/**
	 * Returns all the course schedules matching the UUID and company.
	 *
	 * @param uuid the UUID of the course schedules
	 * @param companyId the primary key of the company
	 * @return the matching course schedules, or an empty list if no matches were found
	 */
	public static List<CourseSchedule>
		getCourseSchedulesByUuidAndCompanyId(String uuid, long companyId) {

		return getService().getCourseSchedulesByUuidAndCompanyId(
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
	public static List<CourseSchedule>
		getCourseSchedulesByUuidAndCompanyId(
			String uuid, long companyId, int start, int end,
			OrderByComparator
				<CourseSchedule>
					orderByComparator) {

		return getService().getCourseSchedulesByUuidAndCompanyId(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the number of course schedules.
	 *
	 * @return the number of course schedules
	 */
	public static int getCourseSchedulesCount() {
		return getService().getCourseSchedulesCount();
	}

	public static ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			PortletDataContext
				portletDataContext) {

		return getService().getExportActionableDynamicQuery(portletDataContext);
	}

	public static
		IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel
			getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	public static String getScheduleForCourse(
			String courseCode,
			ServiceContext serviceContext)
		throws PortalException {

		return getService().getScheduleForCourse(courseCode, serviceContext);
	}

	public static ScheduleResponse
		getScheduleSnapshot(String courseCode) {

		return getService().getScheduleSnapshot(courseCode);
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
	public static CourseSchedule
		updateCourseSchedule(
			CourseSchedule courseSchedule) {

		return getService().updateCourseSchedule(courseSchedule);
	}

	public static CourseSchedule
			updateScheduleFromAdmin(
				long courseScheduleId, String intakeNumber,
				String startDateString, String endDateString,
				Integer availability, String venue, Integer durationHours,
				Integer durationMinutes, String availablePax,
				String availableWaitlist, String lxpBuyUrl,
				String scheduleDownloadUrl, String errorCode,
				String errorMessage,
				ServiceContext serviceContext)
		throws PortalException {

		return getService().updateScheduleFromAdmin(
			courseScheduleId, intakeNumber, startDateString, endDateString,
			availability, venue, durationHours, durationMinutes, availablePax,
			availableWaitlist, lxpBuyUrl, scheduleDownloadUrl, errorCode,
			errorMessage, serviceContext);
	}

	public static CourseSchedule upsertFromCls(
			String courseCode, ScheduleResponse dto,
			ServiceContext serviceContext,
			String rawJson)
		throws PortalException {

		return getService().upsertFromCls(
			courseCode, dto, serviceContext, rawJson);
	}

	public static CourseScheduleLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<CourseScheduleLocalService, CourseScheduleLocalService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			CourseScheduleLocalService.class);

		ServiceTracker<CourseScheduleLocalService, CourseScheduleLocalService>
			serviceTracker =
				new ServiceTracker
					<CourseScheduleLocalService, CourseScheduleLocalService>(
						bundle.getBundleContext(),
						CourseScheduleLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}