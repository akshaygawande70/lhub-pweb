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

import com.liferay.portal.kernel.service.persistence.BasePersistence;

import com.ntuc.notification.exception.NoSuchCourseScheduleException;
import com.ntuc.notification.model.CourseSchedule;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the course schedule service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CourseScheduleUtil
 * @generated
 */
@ProviderType
public interface CourseSchedulePersistence
	extends BasePersistence<CourseSchedule> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link CourseScheduleUtil} to access the course schedule persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the course schedules where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching course schedules
	 */
	public java.util.List<CourseSchedule> findByUuid(String uuid);

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
	public java.util.List<CourseSchedule> findByUuid(
		String uuid, int start, int end);

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
	public java.util.List<CourseSchedule> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
			orderByComparator);

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
	public java.util.List<CourseSchedule> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first course schedule in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course schedule
	 * @throws NoSuchCourseScheduleException if a matching course schedule could not be found
	 */
	public CourseSchedule findByUuid_First(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
				orderByComparator)
		throws NoSuchCourseScheduleException;

	/**
	 * Returns the first course schedule in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public CourseSchedule fetchByUuid_First(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
			orderByComparator);

	/**
	 * Returns the last course schedule in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course schedule
	 * @throws NoSuchCourseScheduleException if a matching course schedule could not be found
	 */
	public CourseSchedule findByUuid_Last(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
				orderByComparator)
		throws NoSuchCourseScheduleException;

	/**
	 * Returns the last course schedule in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public CourseSchedule fetchByUuid_Last(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
			orderByComparator);

	/**
	 * Returns the course schedules before and after the current course schedule in the ordered set where uuid = &#63;.
	 *
	 * @param courseScheduleId the primary key of the current course schedule
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course schedule
	 * @throws NoSuchCourseScheduleException if a course schedule with the primary key could not be found
	 */
	public CourseSchedule[] findByUuid_PrevAndNext(
			long courseScheduleId, String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
				orderByComparator)
		throws NoSuchCourseScheduleException;

	/**
	 * Removes all the course schedules where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public void removeByUuid(String uuid);

	/**
	 * Returns the number of course schedules where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching course schedules
	 */
	public int countByUuid(String uuid);

	/**
	 * Returns the course schedule where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchCourseScheduleException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching course schedule
	 * @throws NoSuchCourseScheduleException if a matching course schedule could not be found
	 */
	public CourseSchedule findByUUID_G(String uuid, long groupId)
		throws NoSuchCourseScheduleException;

	/**
	 * Returns the course schedule where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public CourseSchedule fetchByUUID_G(String uuid, long groupId);

	/**
	 * Returns the course schedule where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public CourseSchedule fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache);

	/**
	 * Removes the course schedule where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the course schedule that was removed
	 */
	public CourseSchedule removeByUUID_G(String uuid, long groupId)
		throws NoSuchCourseScheduleException;

	/**
	 * Returns the number of course schedules where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching course schedules
	 */
	public int countByUUID_G(String uuid, long groupId);

	/**
	 * Returns all the course schedules where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching course schedules
	 */
	public java.util.List<CourseSchedule> findByUuid_C(
		String uuid, long companyId);

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
	public java.util.List<CourseSchedule> findByUuid_C(
		String uuid, long companyId, int start, int end);

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
	public java.util.List<CourseSchedule> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
			orderByComparator);

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
	public java.util.List<CourseSchedule> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first course schedule in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course schedule
	 * @throws NoSuchCourseScheduleException if a matching course schedule could not be found
	 */
	public CourseSchedule findByUuid_C_First(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
				orderByComparator)
		throws NoSuchCourseScheduleException;

	/**
	 * Returns the first course schedule in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public CourseSchedule fetchByUuid_C_First(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
			orderByComparator);

	/**
	 * Returns the last course schedule in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course schedule
	 * @throws NoSuchCourseScheduleException if a matching course schedule could not be found
	 */
	public CourseSchedule findByUuid_C_Last(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
				orderByComparator)
		throws NoSuchCourseScheduleException;

	/**
	 * Returns the last course schedule in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public CourseSchedule fetchByUuid_C_Last(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
			orderByComparator);

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
	public CourseSchedule[] findByUuid_C_PrevAndNext(
			long courseScheduleId, String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
				orderByComparator)
		throws NoSuchCourseScheduleException;

	/**
	 * Removes all the course schedules where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public void removeByUuid_C(String uuid, long companyId);

	/**
	 * Returns the number of course schedules where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching course schedules
	 */
	public int countByUuid_C(String uuid, long companyId);

	/**
	 * Returns the course schedule where courseCode = &#63; or throws a <code>NoSuchCourseScheduleException</code> if it could not be found.
	 *
	 * @param courseCode the course code
	 * @return the matching course schedule
	 * @throws NoSuchCourseScheduleException if a matching course schedule could not be found
	 */
	public CourseSchedule findByCourseCode(String courseCode)
		throws NoSuchCourseScheduleException;

	/**
	 * Returns the course schedule where courseCode = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param courseCode the course code
	 * @return the matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public CourseSchedule fetchByCourseCode(String courseCode);

	/**
	 * Returns the course schedule where courseCode = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param courseCode the course code
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	public CourseSchedule fetchByCourseCode(
		String courseCode, boolean useFinderCache);

	/**
	 * Removes the course schedule where courseCode = &#63; from the database.
	 *
	 * @param courseCode the course code
	 * @return the course schedule that was removed
	 */
	public CourseSchedule removeByCourseCode(String courseCode)
		throws NoSuchCourseScheduleException;

	/**
	 * Returns the number of course schedules where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @return the number of matching course schedules
	 */
	public int countByCourseCode(String courseCode);

	/**
	 * Caches the course schedule in the entity cache if it is enabled.
	 *
	 * @param courseSchedule the course schedule
	 */
	public void cacheResult(CourseSchedule courseSchedule);

	/**
	 * Caches the course schedules in the entity cache if it is enabled.
	 *
	 * @param courseSchedules the course schedules
	 */
	public void cacheResult(java.util.List<CourseSchedule> courseSchedules);

	/**
	 * Creates a new course schedule with the primary key. Does not add the course schedule to the database.
	 *
	 * @param courseScheduleId the primary key for the new course schedule
	 * @return the new course schedule
	 */
	public CourseSchedule create(long courseScheduleId);

	/**
	 * Removes the course schedule with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param courseScheduleId the primary key of the course schedule
	 * @return the course schedule that was removed
	 * @throws NoSuchCourseScheduleException if a course schedule with the primary key could not be found
	 */
	public CourseSchedule remove(long courseScheduleId)
		throws NoSuchCourseScheduleException;

	public CourseSchedule updateImpl(CourseSchedule courseSchedule);

	/**
	 * Returns the course schedule with the primary key or throws a <code>NoSuchCourseScheduleException</code> if it could not be found.
	 *
	 * @param courseScheduleId the primary key of the course schedule
	 * @return the course schedule
	 * @throws NoSuchCourseScheduleException if a course schedule with the primary key could not be found
	 */
	public CourseSchedule findByPrimaryKey(long courseScheduleId)
		throws NoSuchCourseScheduleException;

	/**
	 * Returns the course schedule with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param courseScheduleId the primary key of the course schedule
	 * @return the course schedule, or <code>null</code> if a course schedule with the primary key could not be found
	 */
	public CourseSchedule fetchByPrimaryKey(long courseScheduleId);

	/**
	 * Returns all the course schedules.
	 *
	 * @return the course schedules
	 */
	public java.util.List<CourseSchedule> findAll();

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
	public java.util.List<CourseSchedule> findAll(int start, int end);

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
	public java.util.List<CourseSchedule> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
			orderByComparator);

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
	public java.util.List<CourseSchedule> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CourseSchedule>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the course schedules from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of course schedules.
	 *
	 * @return the number of course schedules
	 */
	public int countAll();

}