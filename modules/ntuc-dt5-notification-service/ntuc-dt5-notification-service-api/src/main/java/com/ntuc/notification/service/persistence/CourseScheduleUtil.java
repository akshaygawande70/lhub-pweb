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

package com.ntuc.notification.service.persistence;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import com.ntuc.notification.model.CourseSchedule;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the course schedule service. This utility wraps <code>com.ntuc.notification.service.persistence.impl.CourseSchedulePersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CourseSchedulePersistence
 * @generated
 */
public class CourseScheduleUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(CourseSchedule courseSchedule) {
		getPersistence().clearCache(courseSchedule);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, CourseSchedule> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<CourseSchedule> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<CourseSchedule> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<CourseSchedule> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<CourseSchedule> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static CourseSchedule update(CourseSchedule courseSchedule) {
		return getPersistence().update(courseSchedule);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static CourseSchedule update(
		CourseSchedule courseSchedule, ServiceContext serviceContext) {

		return getPersistence().update(courseSchedule, serviceContext);
	}

	/**
	 * Returns all the course schedules where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching course schedules
	 */
	public static List<CourseSchedule> findByUuid(String uuid) {
		return getPersistence().findByUuid(uuid);
	}

	/**
	 * Returns a range of all the course schedules where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of course schedules
	 * @param end the upper bound of the range of course schedules (not inclusive)
	 * @return the range of matching course schedules
	 */
	public static List<CourseSchedule> findByUuid(
		String uuid, int start, int end) {

		return getPersistence().findByUuid(uuid, start, end);
	}

	/**
	 * Returns an ordered range of all the course schedules where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of course schedules
	 * @param end the upper bound of the range of course schedules (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching course schedules
	 */
	public static List<CourseSchedule> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<CourseSchedule> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the course schedules where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of course schedules
	 * @param end the upper bound of the range of course schedules (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching course schedules
	 */
	public static List<CourseSchedule> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<CourseSchedule> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first course schedule in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course schedule
	 * @throws NoSuchCourseScheduleException if a matching course schedule could not be found
	 */
	public static CourseSchedule findByUuid_First(
			String uuid, OrderByComparator<CourseSchedule> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchCourseScheduleException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first course schedule in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public static CourseSchedule fetchByUuid_First(
		String uuid, OrderByComparator<CourseSchedule> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the last course schedule in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course schedule
	 * @throws NoSuchCourseScheduleException if a matching course schedule could not be found
	 */
	public static CourseSchedule findByUuid_Last(
			String uuid, OrderByComparator<CourseSchedule> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchCourseScheduleException {

		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the last course schedule in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public static CourseSchedule fetchByUuid_Last(
		String uuid, OrderByComparator<CourseSchedule> orderByComparator) {

		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the course schedules before and after the current course schedule in the ordered set where uuid = &#63;.
	 *
	 * @param courseScheduleId the primary key of the current course schedule
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course schedule
	 * @throws NoSuchCourseScheduleException if a course schedule with the primary key could not be found
	 */
	public static CourseSchedule[] findByUuid_PrevAndNext(
			long courseScheduleId, String uuid,
			OrderByComparator<CourseSchedule> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchCourseScheduleException {

		return getPersistence().findByUuid_PrevAndNext(
			courseScheduleId, uuid, orderByComparator);
	}

	/**
	 * Removes all the course schedules where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of course schedules where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching course schedules
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Returns the course schedule where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchCourseScheduleException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching course schedule
	 * @throws NoSuchCourseScheduleException if a matching course schedule could not be found
	 */
	public static CourseSchedule findByUUID_G(String uuid, long groupId)
		throws com.ntuc.notification.exception.NoSuchCourseScheduleException {

		return getPersistence().findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the course schedule where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public static CourseSchedule fetchByUUID_G(String uuid, long groupId) {
		return getPersistence().fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the course schedule where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public static CourseSchedule fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache) {

		return getPersistence().fetchByUUID_G(uuid, groupId, useFinderCache);
	}

	/**
	 * Removes the course schedule where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the course schedule that was removed
	 */
	public static CourseSchedule removeByUUID_G(String uuid, long groupId)
		throws com.ntuc.notification.exception.NoSuchCourseScheduleException {

		return getPersistence().removeByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the number of course schedules where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching course schedules
	 */
	public static int countByUUID_G(String uuid, long groupId) {
		return getPersistence().countByUUID_G(uuid, groupId);
	}

	/**
	 * Returns all the course schedules where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching course schedules
	 */
	public static List<CourseSchedule> findByUuid_C(
		String uuid, long companyId) {

		return getPersistence().findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of all the course schedules where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of course schedules
	 * @param end the upper bound of the range of course schedules (not inclusive)
	 * @return the range of matching course schedules
	 */
	public static List<CourseSchedule> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return getPersistence().findByUuid_C(uuid, companyId, start, end);
	}

	/**
	 * Returns an ordered range of all the course schedules where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of course schedules
	 * @param end the upper bound of the range of course schedules (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching course schedules
	 */
	public static List<CourseSchedule> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<CourseSchedule> orderByComparator) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the course schedules where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of course schedules
	 * @param end the upper bound of the range of course schedules (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching course schedules
	 */
	public static List<CourseSchedule> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<CourseSchedule> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first course schedule in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course schedule
	 * @throws NoSuchCourseScheduleException if a matching course schedule could not be found
	 */
	public static CourseSchedule findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<CourseSchedule> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchCourseScheduleException {

		return getPersistence().findByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the first course schedule in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public static CourseSchedule fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<CourseSchedule> orderByComparator) {

		return getPersistence().fetchByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last course schedule in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course schedule
	 * @throws NoSuchCourseScheduleException if a matching course schedule could not be found
	 */
	public static CourseSchedule findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<CourseSchedule> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchCourseScheduleException {

		return getPersistence().findByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last course schedule in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public static CourseSchedule fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<CourseSchedule> orderByComparator) {

		return getPersistence().fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the course schedules before and after the current course schedule in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param courseScheduleId the primary key of the current course schedule
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course schedule
	 * @throws NoSuchCourseScheduleException if a course schedule with the primary key could not be found
	 */
	public static CourseSchedule[] findByUuid_C_PrevAndNext(
			long courseScheduleId, String uuid, long companyId,
			OrderByComparator<CourseSchedule> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchCourseScheduleException {

		return getPersistence().findByUuid_C_PrevAndNext(
			courseScheduleId, uuid, companyId, orderByComparator);
	}

	/**
	 * Removes all the course schedules where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public static void removeByUuid_C(String uuid, long companyId) {
		getPersistence().removeByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the number of course schedules where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching course schedules
	 */
	public static int countByUuid_C(String uuid, long companyId) {
		return getPersistence().countByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the course schedule where courseCode = &#63; or throws a <code>NoSuchCourseScheduleException</code> if it could not be found.
	 *
	 * @param courseCode the course code
	 * @return the matching course schedule
	 * @throws NoSuchCourseScheduleException if a matching course schedule could not be found
	 */
	public static CourseSchedule findByCourseCode(String courseCode)
		throws com.ntuc.notification.exception.NoSuchCourseScheduleException {

		return getPersistence().findByCourseCode(courseCode);
	}

	/**
	 * Returns the course schedule where courseCode = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param courseCode the course code
	 * @return the matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public static CourseSchedule fetchByCourseCode(String courseCode) {
		return getPersistence().fetchByCourseCode(courseCode);
	}

	/**
	 * Returns the course schedule where courseCode = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param courseCode the course code
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public static CourseSchedule fetchByCourseCode(
		String courseCode, boolean useFinderCache) {

		return getPersistence().fetchByCourseCode(courseCode, useFinderCache);
	}

	/**
	 * Removes the course schedule where courseCode = &#63; from the database.
	 *
	 * @param courseCode the course code
	 * @return the course schedule that was removed
	 */
	public static CourseSchedule removeByCourseCode(String courseCode)
		throws com.ntuc.notification.exception.NoSuchCourseScheduleException {

		return getPersistence().removeByCourseCode(courseCode);
	}

	/**
	 * Returns the number of course schedules where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @return the number of matching course schedules
	 */
	public static int countByCourseCode(String courseCode) {
		return getPersistence().countByCourseCode(courseCode);
	}

	/**
	 * Caches the course schedule in the entity cache if it is enabled.
	 *
	 * @param courseSchedule the course schedule
	 */
	public static void cacheResult(CourseSchedule courseSchedule) {
		getPersistence().cacheResult(courseSchedule);
	}

	/**
	 * Caches the course schedules in the entity cache if it is enabled.
	 *
	 * @param courseSchedules the course schedules
	 */
	public static void cacheResult(List<CourseSchedule> courseSchedules) {
		getPersistence().cacheResult(courseSchedules);
	}

	/**
	 * Creates a new course schedule with the primary key. Does not add the course schedule to the database.
	 *
	 * @param courseScheduleId the primary key for the new course schedule
	 * @return the new course schedule
	 */
	public static CourseSchedule create(long courseScheduleId) {
		return getPersistence().create(courseScheduleId);
	}

	/**
	 * Removes the course schedule with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param courseScheduleId the primary key of the course schedule
	 * @return the course schedule that was removed
	 * @throws NoSuchCourseScheduleException if a course schedule with the primary key could not be found
	 */
	public static CourseSchedule remove(long courseScheduleId)
		throws com.ntuc.notification.exception.NoSuchCourseScheduleException {

		return getPersistence().remove(courseScheduleId);
	}

	public static CourseSchedule updateImpl(CourseSchedule courseSchedule) {
		return getPersistence().updateImpl(courseSchedule);
	}

	/**
	 * Returns the course schedule with the primary key or throws a <code>NoSuchCourseScheduleException</code> if it could not be found.
	 *
	 * @param courseScheduleId the primary key of the course schedule
	 * @return the course schedule
	 * @throws NoSuchCourseScheduleException if a course schedule with the primary key could not be found
	 */
	public static CourseSchedule findByPrimaryKey(long courseScheduleId)
		throws com.ntuc.notification.exception.NoSuchCourseScheduleException {

		return getPersistence().findByPrimaryKey(courseScheduleId);
	}

	/**
	 * Returns the course schedule with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param courseScheduleId the primary key of the course schedule
	 * @return the course schedule, or <code>null</code> if a course schedule with the primary key could not be found
	 */
	public static CourseSchedule fetchByPrimaryKey(long courseScheduleId) {
		return getPersistence().fetchByPrimaryKey(courseScheduleId);
	}

	/**
	 * Returns all the course schedules.
	 *
	 * @return the course schedules
	 */
	public static List<CourseSchedule> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the course schedules.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of course schedules
	 * @param end the upper bound of the range of course schedules (not inclusive)
	 * @return the range of course schedules
	 */
	public static List<CourseSchedule> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the course schedules.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of course schedules
	 * @param end the upper bound of the range of course schedules (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of course schedules
	 */
	public static List<CourseSchedule> findAll(
		int start, int end,
		OrderByComparator<CourseSchedule> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the course schedules.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of course schedules
	 * @param end the upper bound of the range of course schedules (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of course schedules
	 */
	public static List<CourseSchedule> findAll(
		int start, int end, OrderByComparator<CourseSchedule> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the course schedules from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of course schedules.
	 *
	 * @return the number of course schedules
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static CourseSchedulePersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<CourseSchedulePersistence, CourseSchedulePersistence> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			CourseSchedulePersistence.class);

		ServiceTracker<CourseSchedulePersistence, CourseSchedulePersistence>
			serviceTracker =
				new ServiceTracker
					<CourseSchedulePersistence, CourseSchedulePersistence>(
						bundle.getBundleContext(),
						CourseSchedulePersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}