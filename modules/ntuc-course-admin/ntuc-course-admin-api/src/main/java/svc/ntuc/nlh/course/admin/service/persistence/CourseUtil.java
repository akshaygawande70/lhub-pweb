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

package svc.ntuc.nlh.course.admin.service.persistence;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import svc.ntuc.nlh.course.admin.model.Course;

/**
 * The persistence utility for the course service. This utility wraps <code>svc.ntuc.nlh.course.admin.service.persistence.impl.CoursePersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CoursePersistence
 * @generated
 */
public class CourseUtil {

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
	public static void clearCache(Course course) {
		getPersistence().clearCache(course);
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
	public static Map<Serializable, Course> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<Course> findWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<Course> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<Course> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static Course update(Course course) {
		return getPersistence().update(course);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static Course update(Course course, ServiceContext serviceContext) {
		return getPersistence().update(course, serviceContext);
	}

	/**
	 * Returns all the courses where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching courses
	 */
	public static List<Course> findByUuid(String uuid) {
		return getPersistence().findByUuid(uuid);
	}

	/**
	 * Returns a range of all the courses where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of matching courses
	 */
	public static List<Course> findByUuid(String uuid, int start, int end) {
		return getPersistence().findByUuid(uuid, start, end);
	}

	/**
	 * Returns an ordered range of all the courses where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching courses
	 */
	public static List<Course> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the courses where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching courses
	 */
	public static List<Course> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<Course> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first course in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByUuid_First(
			String uuid, OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first course in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByUuid_First(
		String uuid, OrderByComparator<Course> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the last course in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByUuid_Last(
			String uuid, OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the last course in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByUuid_Last(
		String uuid, OrderByComparator<Course> orderByComparator) {

		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the courses before and after the current course in the ordered set where uuid = &#63;.
	 *
	 * @param courseId the primary key of the current course
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	public static Course[] findByUuid_PrevAndNext(
			long courseId, String uuid,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByUuid_PrevAndNext(
			courseId, uuid, orderByComparator);
	}

	/**
	 * Removes all the courses where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of courses where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching courses
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Returns the course where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchCourseException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByUUID_G(String uuid, long groupId)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the course where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByUUID_G(String uuid, long groupId) {
		return getPersistence().fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the course where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache) {

		return getPersistence().fetchByUUID_G(uuid, groupId, useFinderCache);
	}

	/**
	 * Removes the course where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the course that was removed
	 */
	public static Course removeByUUID_G(String uuid, long groupId)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().removeByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the number of courses where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching courses
	 */
	public static int countByUUID_G(String uuid, long groupId) {
		return getPersistence().countByUUID_G(uuid, groupId);
	}

	/**
	 * Returns all the courses where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching courses
	 */
	public static List<Course> findByUuid_C(String uuid, long companyId) {
		return getPersistence().findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of all the courses where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of matching courses
	 */
	public static List<Course> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return getPersistence().findByUuid_C(uuid, companyId, start, end);
	}

	/**
	 * Returns an ordered range of all the courses where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching courses
	 */
	public static List<Course> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the courses where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching courses
	 */
	public static List<Course> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Course> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first course in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the first course in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().fetchByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last course in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last course in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the courses before and after the current course in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param courseId the primary key of the current course
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	public static Course[] findByUuid_C_PrevAndNext(
			long courseId, String uuid, long companyId,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByUuid_C_PrevAndNext(
			courseId, uuid, companyId, orderByComparator);
	}

	/**
	 * Removes all the courses where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public static void removeByUuid_C(String uuid, long companyId) {
		getPersistence().removeByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the number of courses where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching courses
	 */
	public static int countByUuid_C(String uuid, long companyId) {
		return getPersistence().countByUuid_C(uuid, companyId);
	}

	/**
	 * Returns all the courses where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @return the matching courses
	 */
	public static List<Course> findByCourseCode(
		long groupId, String courseCode, boolean deleted) {

		return getPersistence().findByCourseCode(groupId, courseCode, deleted);
	}

	/**
	 * Returns a range of all the courses where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of matching courses
	 */
	public static List<Course> findByCourseCode(
		long groupId, String courseCode, boolean deleted, int start, int end) {

		return getPersistence().findByCourseCode(
			groupId, courseCode, deleted, start, end);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching courses
	 */
	public static List<Course> findByCourseCode(
		long groupId, String courseCode, boolean deleted, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().findByCourseCode(
			groupId, courseCode, deleted, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching courses
	 */
	public static List<Course> findByCourseCode(
		long groupId, String courseCode, boolean deleted, int start, int end,
		OrderByComparator<Course> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByCourseCode(
			groupId, courseCode, deleted, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByCourseCode_First(
			long groupId, String courseCode, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByCourseCode_First(
			groupId, courseCode, deleted, orderByComparator);
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByCourseCode_First(
		long groupId, String courseCode, boolean deleted,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().fetchByCourseCode_First(
			groupId, courseCode, deleted, orderByComparator);
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByCourseCode_Last(
			long groupId, String courseCode, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByCourseCode_Last(
			groupId, courseCode, deleted, orderByComparator);
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByCourseCode_Last(
		long groupId, String courseCode, boolean deleted,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().fetchByCourseCode_Last(
			groupId, courseCode, deleted, orderByComparator);
	}

	/**
	 * Returns the courses before and after the current course in the ordered set where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * @param courseId the primary key of the current course
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	public static Course[] findByCourseCode_PrevAndNext(
			long courseId, long groupId, String courseCode, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByCourseCode_PrevAndNext(
			courseId, groupId, courseCode, deleted, orderByComparator);
	}

	/**
	 * Removes all the courses where groupId = &#63; and courseCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 */
	public static void removeByCourseCode(
		long groupId, String courseCode, boolean deleted) {

		getPersistence().removeByCourseCode(groupId, courseCode, deleted);
	}

	/**
	 * Returns the number of courses where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @return the number of matching courses
	 */
	public static int countByCourseCode(
		long groupId, String courseCode, boolean deleted) {

		return getPersistence().countByCourseCode(groupId, courseCode, deleted);
	}

	/**
	 * Returns all the courses where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @return the matching courses
	 */
	public static List<Course> findByCourseTitle(
		long groupId, String courseTitle, boolean deleted) {

		return getPersistence().findByCourseTitle(
			groupId, courseTitle, deleted);
	}

	/**
	 * Returns a range of all the courses where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of matching courses
	 */
	public static List<Course> findByCourseTitle(
		long groupId, String courseTitle, boolean deleted, int start, int end) {

		return getPersistence().findByCourseTitle(
			groupId, courseTitle, deleted, start, end);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching courses
	 */
	public static List<Course> findByCourseTitle(
		long groupId, String courseTitle, boolean deleted, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().findByCourseTitle(
			groupId, courseTitle, deleted, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching courses
	 */
	public static List<Course> findByCourseTitle(
		long groupId, String courseTitle, boolean deleted, int start, int end,
		OrderByComparator<Course> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByCourseTitle(
			groupId, courseTitle, deleted, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByCourseTitle_First(
			long groupId, String courseTitle, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByCourseTitle_First(
			groupId, courseTitle, deleted, orderByComparator);
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByCourseTitle_First(
		long groupId, String courseTitle, boolean deleted,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().fetchByCourseTitle_First(
			groupId, courseTitle, deleted, orderByComparator);
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByCourseTitle_Last(
			long groupId, String courseTitle, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByCourseTitle_Last(
			groupId, courseTitle, deleted, orderByComparator);
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByCourseTitle_Last(
		long groupId, String courseTitle, boolean deleted,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().fetchByCourseTitle_Last(
			groupId, courseTitle, deleted, orderByComparator);
	}

	/**
	 * Returns the courses before and after the current course in the ordered set where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * @param courseId the primary key of the current course
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	public static Course[] findByCourseTitle_PrevAndNext(
			long courseId, long groupId, String courseTitle, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByCourseTitle_PrevAndNext(
			courseId, groupId, courseTitle, deleted, orderByComparator);
	}

	/**
	 * Removes all the courses where groupId = &#63; and courseTitle = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 */
	public static void removeByCourseTitle(
		long groupId, String courseTitle, boolean deleted) {

		getPersistence().removeByCourseTitle(groupId, courseTitle, deleted);
	}

	/**
	 * Returns the number of courses where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @return the number of matching courses
	 */
	public static int countByCourseTitle(
		long groupId, String courseTitle, boolean deleted) {

		return getPersistence().countByCourseTitle(
			groupId, courseTitle, deleted);
	}

	/**
	 * Returns the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; and deleted = &#63; or throws a <code>NoSuchCourseException</code> if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @param deleted the deleted
	 * @return the matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByCourseCodeBatchIdActive(
			long groupId, String courseCode, String batchId, boolean deleted)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByCourseCodeBatchIdActive(
			groupId, courseCode, batchId, deleted);
	}

	/**
	 * Returns the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @param deleted the deleted
	 * @return the matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByCourseCodeBatchIdActive(
		long groupId, String courseCode, String batchId, boolean deleted) {

		return getPersistence().fetchByCourseCodeBatchIdActive(
			groupId, courseCode, batchId, deleted);
	}

	/**
	 * Returns the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @param deleted the deleted
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByCourseCodeBatchIdActive(
		long groupId, String courseCode, String batchId, boolean deleted,
		boolean useFinderCache) {

		return getPersistence().fetchByCourseCodeBatchIdActive(
			groupId, courseCode, batchId, deleted, useFinderCache);
	}

	/**
	 * Removes the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @param deleted the deleted
	 * @return the course that was removed
	 */
	public static Course removeByCourseCodeBatchIdActive(
			long groupId, String courseCode, String batchId, boolean deleted)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().removeByCourseCodeBatchIdActive(
			groupId, courseCode, batchId, deleted);
	}

	/**
	 * Returns the number of courses where groupId = &#63; and courseCode = &#63; and batchId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @param deleted the deleted
	 * @return the number of matching courses
	 */
	public static int countByCourseCodeBatchIdActive(
		long groupId, String courseCode, String batchId, boolean deleted) {

		return getPersistence().countByCourseCodeBatchIdActive(
			groupId, courseCode, batchId, deleted);
	}

	/**
	 * Returns the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; or throws a <code>NoSuchCourseException</code> if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @return the matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByCourseCodeBatchId(
			long groupId, String courseCode, String batchId)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByCourseCodeBatchId(
			groupId, courseCode, batchId);
	}

	/**
	 * Returns the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @return the matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByCourseCodeBatchId(
		long groupId, String courseCode, String batchId) {

		return getPersistence().fetchByCourseCodeBatchId(
			groupId, courseCode, batchId);
	}

	/**
	 * Returns the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByCourseCodeBatchId(
		long groupId, String courseCode, String batchId,
		boolean useFinderCache) {

		return getPersistence().fetchByCourseCodeBatchId(
			groupId, courseCode, batchId, useFinderCache);
	}

	/**
	 * Removes the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @return the course that was removed
	 */
	public static Course removeByCourseCodeBatchId(
			long groupId, String courseCode, String batchId)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().removeByCourseCodeBatchId(
			groupId, courseCode, batchId);
	}

	/**
	 * Returns the number of courses where groupId = &#63; and courseCode = &#63; and batchId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @return the number of matching courses
	 */
	public static int countByCourseCodeBatchId(
		long groupId, String courseCode, String batchId) {

		return getPersistence().countByCourseCodeBatchId(
			groupId, courseCode, batchId);
	}

	/**
	 * Returns all the courses where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the matching courses
	 */
	public static List<Course> findByActiveCourse(
		long groupId, boolean deleted) {

		return getPersistence().findByActiveCourse(groupId, deleted);
	}

	/**
	 * Returns a range of all the courses where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of matching courses
	 */
	public static List<Course> findByActiveCourse(
		long groupId, boolean deleted, int start, int end) {

		return getPersistence().findByActiveCourse(
			groupId, deleted, start, end);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching courses
	 */
	public static List<Course> findByActiveCourse(
		long groupId, boolean deleted, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().findByActiveCourse(
			groupId, deleted, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching courses
	 */
	public static List<Course> findByActiveCourse(
		long groupId, boolean deleted, int start, int end,
		OrderByComparator<Course> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByActiveCourse(
			groupId, deleted, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByActiveCourse_First(
			long groupId, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByActiveCourse_First(
			groupId, deleted, orderByComparator);
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByActiveCourse_First(
		long groupId, boolean deleted,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().fetchByActiveCourse_First(
			groupId, deleted, orderByComparator);
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByActiveCourse_Last(
			long groupId, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByActiveCourse_Last(
			groupId, deleted, orderByComparator);
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByActiveCourse_Last(
		long groupId, boolean deleted,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().fetchByActiveCourse_Last(
			groupId, deleted, orderByComparator);
	}

	/**
	 * Returns the courses before and after the current course in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param courseId the primary key of the current course
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	public static Course[] findByActiveCourse_PrevAndNext(
			long courseId, long groupId, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByActiveCourse_PrevAndNext(
			courseId, groupId, deleted, orderByComparator);
	}

	/**
	 * Removes all the courses where groupId = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 */
	public static void removeByActiveCourse(long groupId, boolean deleted) {
		getPersistence().removeByActiveCourse(groupId, deleted);
	}

	/**
	 * Returns the number of courses where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the number of matching courses
	 */
	public static int countByActiveCourse(long groupId, boolean deleted) {
		return getPersistence().countByActiveCourse(groupId, deleted);
	}

	/**
	 * Returns all the courses where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @return the matching courses
	 */
	public static List<Course> findByActiveCourseUpdated(
		long groupId, boolean deleted, boolean updated) {

		return getPersistence().findByActiveCourseUpdated(
			groupId, deleted, updated);
	}

	/**
	 * Returns a range of all the courses where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of matching courses
	 */
	public static List<Course> findByActiveCourseUpdated(
		long groupId, boolean deleted, boolean updated, int start, int end) {

		return getPersistence().findByActiveCourseUpdated(
			groupId, deleted, updated, start, end);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching courses
	 */
	public static List<Course> findByActiveCourseUpdated(
		long groupId, boolean deleted, boolean updated, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().findByActiveCourseUpdated(
			groupId, deleted, updated, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching courses
	 */
	public static List<Course> findByActiveCourseUpdated(
		long groupId, boolean deleted, boolean updated, int start, int end,
		OrderByComparator<Course> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByActiveCourseUpdated(
			groupId, deleted, updated, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByActiveCourseUpdated_First(
			long groupId, boolean deleted, boolean updated,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByActiveCourseUpdated_First(
			groupId, deleted, updated, orderByComparator);
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByActiveCourseUpdated_First(
		long groupId, boolean deleted, boolean updated,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().fetchByActiveCourseUpdated_First(
			groupId, deleted, updated, orderByComparator);
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByActiveCourseUpdated_Last(
			long groupId, boolean deleted, boolean updated,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByActiveCourseUpdated_Last(
			groupId, deleted, updated, orderByComparator);
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByActiveCourseUpdated_Last(
		long groupId, boolean deleted, boolean updated,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().fetchByActiveCourseUpdated_Last(
			groupId, deleted, updated, orderByComparator);
	}

	/**
	 * Returns the courses before and after the current course in the ordered set where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * @param courseId the primary key of the current course
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	public static Course[] findByActiveCourseUpdated_PrevAndNext(
			long courseId, long groupId, boolean deleted, boolean updated,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByActiveCourseUpdated_PrevAndNext(
			courseId, groupId, deleted, updated, orderByComparator);
	}

	/**
	 * Removes all the courses where groupId = &#63; and deleted = &#63; and updated = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 */
	public static void removeByActiveCourseUpdated(
		long groupId, boolean deleted, boolean updated) {

		getPersistence().removeByActiveCourseUpdated(groupId, deleted, updated);
	}

	/**
	 * Returns the number of courses where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @return the number of matching courses
	 */
	public static int countByActiveCourseUpdated(
		long groupId, boolean deleted, boolean updated) {

		return getPersistence().countByActiveCourseUpdated(
			groupId, deleted, updated);
	}

	/**
	 * Returns all the courses where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @return the matching courses
	 */
	public static List<Course> findByActivePopularCourse(
		long groupId, boolean deleted, boolean popular) {

		return getPersistence().findByActivePopularCourse(
			groupId, deleted, popular);
	}

	/**
	 * Returns a range of all the courses where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of matching courses
	 */
	public static List<Course> findByActivePopularCourse(
		long groupId, boolean deleted, boolean popular, int start, int end) {

		return getPersistence().findByActivePopularCourse(
			groupId, deleted, popular, start, end);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching courses
	 */
	public static List<Course> findByActivePopularCourse(
		long groupId, boolean deleted, boolean popular, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().findByActivePopularCourse(
			groupId, deleted, popular, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching courses
	 */
	public static List<Course> findByActivePopularCourse(
		long groupId, boolean deleted, boolean popular, int start, int end,
		OrderByComparator<Course> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByActivePopularCourse(
			groupId, deleted, popular, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByActivePopularCourse_First(
			long groupId, boolean deleted, boolean popular,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByActivePopularCourse_First(
			groupId, deleted, popular, orderByComparator);
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByActivePopularCourse_First(
		long groupId, boolean deleted, boolean popular,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().fetchByActivePopularCourse_First(
			groupId, deleted, popular, orderByComparator);
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	public static Course findByActivePopularCourse_Last(
			long groupId, boolean deleted, boolean popular,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByActivePopularCourse_Last(
			groupId, deleted, popular, orderByComparator);
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course, or <code>null</code> if a matching course could not be found
	 */
	public static Course fetchByActivePopularCourse_Last(
		long groupId, boolean deleted, boolean popular,
		OrderByComparator<Course> orderByComparator) {

		return getPersistence().fetchByActivePopularCourse_Last(
			groupId, deleted, popular, orderByComparator);
	}

	/**
	 * Returns the courses before and after the current course in the ordered set where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * @param courseId the primary key of the current course
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	public static Course[] findByActivePopularCourse_PrevAndNext(
			long courseId, long groupId, boolean deleted, boolean popular,
			OrderByComparator<Course> orderByComparator)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByActivePopularCourse_PrevAndNext(
			courseId, groupId, deleted, popular, orderByComparator);
	}

	/**
	 * Removes all the courses where groupId = &#63; and deleted = &#63; and popular = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 */
	public static void removeByActivePopularCourse(
		long groupId, boolean deleted, boolean popular) {

		getPersistence().removeByActivePopularCourse(groupId, deleted, popular);
	}

	/**
	 * Returns the number of courses where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @return the number of matching courses
	 */
	public static int countByActivePopularCourse(
		long groupId, boolean deleted, boolean popular) {

		return getPersistence().countByActivePopularCourse(
			groupId, deleted, popular);
	}

	/**
	 * Caches the course in the entity cache if it is enabled.
	 *
	 * @param course the course
	 */
	public static void cacheResult(Course course) {
		getPersistence().cacheResult(course);
	}

	/**
	 * Caches the courses in the entity cache if it is enabled.
	 *
	 * @param courses the courses
	 */
	public static void cacheResult(List<Course> courses) {
		getPersistence().cacheResult(courses);
	}

	/**
	 * Creates a new course with the primary key. Does not add the course to the database.
	 *
	 * @param courseId the primary key for the new course
	 * @return the new course
	 */
	public static Course create(long courseId) {
		return getPersistence().create(courseId);
	}

	/**
	 * Removes the course with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param courseId the primary key of the course
	 * @return the course that was removed
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	public static Course remove(long courseId)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().remove(courseId);
	}

	public static Course updateImpl(Course course) {
		return getPersistence().updateImpl(course);
	}

	/**
	 * Returns the course with the primary key or throws a <code>NoSuchCourseException</code> if it could not be found.
	 *
	 * @param courseId the primary key of the course
	 * @return the course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	public static Course findByPrimaryKey(long courseId)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return getPersistence().findByPrimaryKey(courseId);
	}

	/**
	 * Returns the course with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param courseId the primary key of the course
	 * @return the course, or <code>null</code> if a course with the primary key could not be found
	 */
	public static Course fetchByPrimaryKey(long courseId) {
		return getPersistence().fetchByPrimaryKey(courseId);
	}

	/**
	 * Returns all the courses.
	 *
	 * @return the courses
	 */
	public static List<Course> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the courses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of courses
	 */
	public static List<Course> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the courses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of courses
	 */
	public static List<Course> findAll(
		int start, int end, OrderByComparator<Course> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the courses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of courses
	 */
	public static List<Course> findAll(
		int start, int end, OrderByComparator<Course> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the courses from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of courses.
	 *
	 * @return the number of courses
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static CoursePersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<CoursePersistence, CoursePersistence>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(CoursePersistence.class);

		ServiceTracker<CoursePersistence, CoursePersistence> serviceTracker =
			new ServiceTracker<CoursePersistence, CoursePersistence>(
				bundle.getBundleContext(), CoursePersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}