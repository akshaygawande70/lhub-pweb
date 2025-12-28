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

import com.ntuc.notification.model.NtucSB;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the ntuc sb service. This utility wraps <code>com.ntuc.notification.service.persistence.impl.NtucSBPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see NtucSBPersistence
 * @generated
 */
public class NtucSBUtil {

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
	public static void clearCache(NtucSB ntucSB) {
		getPersistence().clearCache(ntucSB);
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
	public static Map<Serializable, NtucSB> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<NtucSB> findWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<NtucSB> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<NtucSB> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static NtucSB update(NtucSB ntucSB) {
		return getPersistence().update(ntucSB);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static NtucSB update(NtucSB ntucSB, ServiceContext serviceContext) {
		return getPersistence().update(ntucSB, serviceContext);
	}

	/**
	 * Returns all the ntuc sbs where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @return the matching ntuc sbs
	 */
	public static List<NtucSB> findByisRowLockFailed(boolean isRowLockFailed) {
		return getPersistence().findByisRowLockFailed(isRowLockFailed);
	}

	/**
	 * Returns a range of all the ntuc sbs where isRowLockFailed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of matching ntuc sbs
	 */
	public static List<NtucSB> findByisRowLockFailed(
		boolean isRowLockFailed, int start, int end) {

		return getPersistence().findByisRowLockFailed(
			isRowLockFailed, start, end);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where isRowLockFailed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc sbs
	 */
	public static List<NtucSB> findByisRowLockFailed(
		boolean isRowLockFailed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().findByisRowLockFailed(
			isRowLockFailed, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where isRowLockFailed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc sbs
	 */
	public static List<NtucSB> findByisRowLockFailed(
		boolean isRowLockFailed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByisRowLockFailed(
			isRowLockFailed, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public static NtucSB findByisRowLockFailed_First(
			boolean isRowLockFailed,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByisRowLockFailed_First(
			isRowLockFailed, orderByComparator);
	}

	/**
	 * Returns the first ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public static NtucSB fetchByisRowLockFailed_First(
		boolean isRowLockFailed, OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().fetchByisRowLockFailed_First(
			isRowLockFailed, orderByComparator);
	}

	/**
	 * Returns the last ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public static NtucSB findByisRowLockFailed_Last(
			boolean isRowLockFailed,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByisRowLockFailed_Last(
			isRowLockFailed, orderByComparator);
	}

	/**
	 * Returns the last ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public static NtucSB fetchByisRowLockFailed_Last(
		boolean isRowLockFailed, OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().fetchByisRowLockFailed_Last(
			isRowLockFailed, orderByComparator);
	}

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	public static NtucSB[] findByisRowLockFailed_PrevAndNext(
			long ntucDTId, boolean isRowLockFailed,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByisRowLockFailed_PrevAndNext(
			ntucDTId, isRowLockFailed, orderByComparator);
	}

	/**
	 * Removes all the ntuc sbs where isRowLockFailed = &#63; from the database.
	 *
	 * @param isRowLockFailed the is row lock failed
	 */
	public static void removeByisRowLockFailed(boolean isRowLockFailed) {
		getPersistence().removeByisRowLockFailed(isRowLockFailed);
	}

	/**
	 * Returns the number of ntuc sbs where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @return the number of matching ntuc sbs
	 */
	public static int countByisRowLockFailed(boolean isRowLockFailed) {
		return getPersistence().countByisRowLockFailed(isRowLockFailed);
	}

	/**
	 * Returns all the ntuc sbs where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @return the matching ntuc sbs
	 */
	public static List<NtucSB> findByCourseCodeAndEvent(
		String courseCode, String event) {

		return getPersistence().findByCourseCodeAndEvent(courseCode, event);
	}

	/**
	 * Returns a range of all the ntuc sbs where courseCode = &#63; and event = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of matching ntuc sbs
	 */
	public static List<NtucSB> findByCourseCodeAndEvent(
		String courseCode, String event, int start, int end) {

		return getPersistence().findByCourseCodeAndEvent(
			courseCode, event, start, end);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where courseCode = &#63; and event = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc sbs
	 */
	public static List<NtucSB> findByCourseCodeAndEvent(
		String courseCode, String event, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().findByCourseCodeAndEvent(
			courseCode, event, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where courseCode = &#63; and event = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc sbs
	 */
	public static List<NtucSB> findByCourseCodeAndEvent(
		String courseCode, String event, int start, int end,
		OrderByComparator<NtucSB> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByCourseCodeAndEvent(
			courseCode, event, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first ntuc sb in the ordered set where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public static NtucSB findByCourseCodeAndEvent_First(
			String courseCode, String event,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByCourseCodeAndEvent_First(
			courseCode, event, orderByComparator);
	}

	/**
	 * Returns the first ntuc sb in the ordered set where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public static NtucSB fetchByCourseCodeAndEvent_First(
		String courseCode, String event,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().fetchByCourseCodeAndEvent_First(
			courseCode, event, orderByComparator);
	}

	/**
	 * Returns the last ntuc sb in the ordered set where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public static NtucSB findByCourseCodeAndEvent_Last(
			String courseCode, String event,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByCourseCodeAndEvent_Last(
			courseCode, event, orderByComparator);
	}

	/**
	 * Returns the last ntuc sb in the ordered set where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public static NtucSB fetchByCourseCodeAndEvent_Last(
		String courseCode, String event,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().fetchByCourseCodeAndEvent_Last(
			courseCode, event, orderByComparator);
	}

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where courseCode = &#63; and event = &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param courseCode the course code
	 * @param event the event
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	public static NtucSB[] findByCourseCodeAndEvent_PrevAndNext(
			long ntucDTId, String courseCode, String event,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByCourseCodeAndEvent_PrevAndNext(
			ntucDTId, courseCode, event, orderByComparator);
	}

	/**
	 * Removes all the ntuc sbs where courseCode = &#63; and event = &#63; from the database.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 */
	public static void removeByCourseCodeAndEvent(
		String courseCode, String event) {

		getPersistence().removeByCourseCodeAndEvent(courseCode, event);
	}

	/**
	 * Returns the number of ntuc sbs where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @return the number of matching ntuc sbs
	 */
	public static int countByCourseCodeAndEvent(
		String courseCode, String event) {

		return getPersistence().countByCourseCodeAndEvent(courseCode, event);
	}

	/**
	 * Returns all the ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @return the matching ntuc sbs
	 */
	public static List<NtucSB> findByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom) {

		return getPersistence().findByCourseCodeEventAndChangeFrom(
			courseCode, event, changeFrom);
	}

	/**
	 * Returns a range of all the ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of matching ntuc sbs
	 */
	public static List<NtucSB> findByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom, int start,
		int end) {

		return getPersistence().findByCourseCodeEventAndChangeFrom(
			courseCode, event, changeFrom, start, end);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc sbs
	 */
	public static List<NtucSB> findByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().findByCourseCodeEventAndChangeFrom(
			courseCode, event, changeFrom, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc sbs
	 */
	public static List<NtucSB> findByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom, int start, int end,
		OrderByComparator<NtucSB> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByCourseCodeEventAndChangeFrom(
			courseCode, event, changeFrom, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first ntuc sb in the ordered set where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public static NtucSB findByCourseCodeEventAndChangeFrom_First(
			String courseCode, String event, String changeFrom,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByCourseCodeEventAndChangeFrom_First(
			courseCode, event, changeFrom, orderByComparator);
	}

	/**
	 * Returns the first ntuc sb in the ordered set where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public static NtucSB fetchByCourseCodeEventAndChangeFrom_First(
		String courseCode, String event, String changeFrom,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().fetchByCourseCodeEventAndChangeFrom_First(
			courseCode, event, changeFrom, orderByComparator);
	}

	/**
	 * Returns the last ntuc sb in the ordered set where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public static NtucSB findByCourseCodeEventAndChangeFrom_Last(
			String courseCode, String event, String changeFrom,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByCourseCodeEventAndChangeFrom_Last(
			courseCode, event, changeFrom, orderByComparator);
	}

	/**
	 * Returns the last ntuc sb in the ordered set where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public static NtucSB fetchByCourseCodeEventAndChangeFrom_Last(
		String courseCode, String event, String changeFrom,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().fetchByCourseCodeEventAndChangeFrom_Last(
			courseCode, event, changeFrom, orderByComparator);
	}

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	public static NtucSB[] findByCourseCodeEventAndChangeFrom_PrevAndNext(
			long ntucDTId, String courseCode, String event, String changeFrom,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByCourseCodeEventAndChangeFrom_PrevAndNext(
			ntucDTId, courseCode, event, changeFrom, orderByComparator);
	}

	/**
	 * Removes all the ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63; from the database.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 */
	public static void removeByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom) {

		getPersistence().removeByCourseCodeEventAndChangeFrom(
			courseCode, event, changeFrom);
	}

	/**
	 * Returns the number of ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @return the number of matching ntuc sbs
	 */
	public static int countByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom) {

		return getPersistence().countByCourseCodeEventAndChangeFrom(
			courseCode, event, changeFrom);
	}

	/**
	 * Returns all the ntuc sbs where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @return the matching ntuc sbs
	 */
	public static List<NtucSB> findByRecordsByChangeFrom(String changeFrom) {
		return getPersistence().findByRecordsByChangeFrom(changeFrom);
	}

	/**
	 * Returns a range of all the ntuc sbs where changeFrom LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param changeFrom the change from
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of matching ntuc sbs
	 */
	public static List<NtucSB> findByRecordsByChangeFrom(
		String changeFrom, int start, int end) {

		return getPersistence().findByRecordsByChangeFrom(
			changeFrom, start, end);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where changeFrom LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param changeFrom the change from
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc sbs
	 */
	public static List<NtucSB> findByRecordsByChangeFrom(
		String changeFrom, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().findByRecordsByChangeFrom(
			changeFrom, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where changeFrom LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param changeFrom the change from
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc sbs
	 */
	public static List<NtucSB> findByRecordsByChangeFrom(
		String changeFrom, int start, int end,
		OrderByComparator<NtucSB> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByRecordsByChangeFrom(
			changeFrom, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public static NtucSB findByRecordsByChangeFrom_First(
			String changeFrom, OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByRecordsByChangeFrom_First(
			changeFrom, orderByComparator);
	}

	/**
	 * Returns the first ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public static NtucSB fetchByRecordsByChangeFrom_First(
		String changeFrom, OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().fetchByRecordsByChangeFrom_First(
			changeFrom, orderByComparator);
	}

	/**
	 * Returns the last ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public static NtucSB findByRecordsByChangeFrom_Last(
			String changeFrom, OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByRecordsByChangeFrom_Last(
			changeFrom, orderByComparator);
	}

	/**
	 * Returns the last ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public static NtucSB fetchByRecordsByChangeFrom_Last(
		String changeFrom, OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().fetchByRecordsByChangeFrom_Last(
			changeFrom, orderByComparator);
	}

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	public static NtucSB[] findByRecordsByChangeFrom_PrevAndNext(
			long ntucDTId, String changeFrom,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByRecordsByChangeFrom_PrevAndNext(
			ntucDTId, changeFrom, orderByComparator);
	}

	/**
	 * Removes all the ntuc sbs where changeFrom LIKE &#63; from the database.
	 *
	 * @param changeFrom the change from
	 */
	public static void removeByRecordsByChangeFrom(String changeFrom) {
		getPersistence().removeByRecordsByChangeFrom(changeFrom);
	}

	/**
	 * Returns the number of ntuc sbs where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @return the number of matching ntuc sbs
	 */
	public static int countByRecordsByChangeFrom(String changeFrom) {
		return getPersistence().countByRecordsByChangeFrom(changeFrom);
	}

	/**
	 * Returns all the ntuc sbs where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @return the matching ntuc sbs
	 */
	public static List<NtucSB> findByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed) {

		return getPersistence().findByStatusAndIsCronProcessed(
			event, isCronProcessed);
	}

	/**
	 * Returns a range of all the ntuc sbs where event = &#63; and isCronProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of matching ntuc sbs
	 */
	public static List<NtucSB> findByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed, int start, int end) {

		return getPersistence().findByStatusAndIsCronProcessed(
			event, isCronProcessed, start, end);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where event = &#63; and isCronProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc sbs
	 */
	public static List<NtucSB> findByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().findByStatusAndIsCronProcessed(
			event, isCronProcessed, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where event = &#63; and isCronProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc sbs
	 */
	public static List<NtucSB> findByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByStatusAndIsCronProcessed(
			event, isCronProcessed, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public static NtucSB findByStatusAndIsCronProcessed_First(
			String event, boolean isCronProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByStatusAndIsCronProcessed_First(
			event, isCronProcessed, orderByComparator);
	}

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public static NtucSB fetchByStatusAndIsCronProcessed_First(
		String event, boolean isCronProcessed,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().fetchByStatusAndIsCronProcessed_First(
			event, isCronProcessed, orderByComparator);
	}

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public static NtucSB findByStatusAndIsCronProcessed_Last(
			String event, boolean isCronProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByStatusAndIsCronProcessed_Last(
			event, isCronProcessed, orderByComparator);
	}

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public static NtucSB fetchByStatusAndIsCronProcessed_Last(
		String event, boolean isCronProcessed,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().fetchByStatusAndIsCronProcessed_Last(
			event, isCronProcessed, orderByComparator);
	}

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	public static NtucSB[] findByStatusAndIsCronProcessed_PrevAndNext(
			long ntucDTId, String event, boolean isCronProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByStatusAndIsCronProcessed_PrevAndNext(
			ntucDTId, event, isCronProcessed, orderByComparator);
	}

	/**
	 * Removes all the ntuc sbs where event = &#63; and isCronProcessed = &#63; from the database.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 */
	public static void removeByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed) {

		getPersistence().removeByStatusAndIsCronProcessed(
			event, isCronProcessed);
	}

	/**
	 * Returns the number of ntuc sbs where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @return the number of matching ntuc sbs
	 */
	public static int countByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed) {

		return getPersistence().countByStatusAndIsCronProcessed(
			event, isCronProcessed);
	}

	/**
	 * Returns all the ntuc sbs where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @return the matching ntuc sbs
	 */
	public static List<NtucSB> findByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed) {

		return getPersistence().findByEventAndIsCriticalProcessed(
			event, isCriticalProcessed);
	}

	/**
	 * Returns a range of all the ntuc sbs where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of matching ntuc sbs
	 */
	public static List<NtucSB> findByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed, int start, int end) {

		return getPersistence().findByEventAndIsCriticalProcessed(
			event, isCriticalProcessed, start, end);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc sbs
	 */
	public static List<NtucSB> findByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().findByEventAndIsCriticalProcessed(
			event, isCriticalProcessed, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc sbs
	 */
	public static List<NtucSB> findByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByEventAndIsCriticalProcessed(
			event, isCriticalProcessed, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public static NtucSB findByEventAndIsCriticalProcessed_First(
			String event, boolean isCriticalProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByEventAndIsCriticalProcessed_First(
			event, isCriticalProcessed, orderByComparator);
	}

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public static NtucSB fetchByEventAndIsCriticalProcessed_First(
		String event, boolean isCriticalProcessed,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().fetchByEventAndIsCriticalProcessed_First(
			event, isCriticalProcessed, orderByComparator);
	}

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public static NtucSB findByEventAndIsCriticalProcessed_Last(
			String event, boolean isCriticalProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByEventAndIsCriticalProcessed_Last(
			event, isCriticalProcessed, orderByComparator);
	}

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public static NtucSB fetchByEventAndIsCriticalProcessed_Last(
		String event, boolean isCriticalProcessed,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().fetchByEventAndIsCriticalProcessed_Last(
			event, isCriticalProcessed, orderByComparator);
	}

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	public static NtucSB[] findByEventAndIsCriticalProcessed_PrevAndNext(
			long ntucDTId, String event, boolean isCriticalProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByEventAndIsCriticalProcessed_PrevAndNext(
			ntucDTId, event, isCriticalProcessed, orderByComparator);
	}

	/**
	 * Removes all the ntuc sbs where event = &#63; and isCriticalProcessed = &#63; from the database.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 */
	public static void removeByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed) {

		getPersistence().removeByEventAndIsCriticalProcessed(
			event, isCriticalProcessed);
	}

	/**
	 * Returns the number of ntuc sbs where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @return the number of matching ntuc sbs
	 */
	public static int countByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed) {

		return getPersistence().countByEventAndIsCriticalProcessed(
			event, isCriticalProcessed);
	}

	/**
	 * Returns all the ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @return the matching ntuc sbs
	 */
	public static List<NtucSB> findByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed) {

		return getPersistence().findByEventAndIsNonCriticalProcessed(
			event, isNonCriticalProcessed);
	}

	/**
	 * Returns a range of all the ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of matching ntuc sbs
	 */
	public static List<NtucSB> findByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed, int start, int end) {

		return getPersistence().findByEventAndIsNonCriticalProcessed(
			event, isNonCriticalProcessed, start, end);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc sbs
	 */
	public static List<NtucSB> findByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().findByEventAndIsNonCriticalProcessed(
			event, isNonCriticalProcessed, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc sbs
	 */
	public static List<NtucSB> findByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByEventAndIsNonCriticalProcessed(
			event, isNonCriticalProcessed, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public static NtucSB findByEventAndIsNonCriticalProcessed_First(
			String event, boolean isNonCriticalProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByEventAndIsNonCriticalProcessed_First(
			event, isNonCriticalProcessed, orderByComparator);
	}

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public static NtucSB fetchByEventAndIsNonCriticalProcessed_First(
		String event, boolean isNonCriticalProcessed,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().fetchByEventAndIsNonCriticalProcessed_First(
			event, isNonCriticalProcessed, orderByComparator);
	}

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public static NtucSB findByEventAndIsNonCriticalProcessed_Last(
			String event, boolean isNonCriticalProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByEventAndIsNonCriticalProcessed_Last(
			event, isNonCriticalProcessed, orderByComparator);
	}

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public static NtucSB fetchByEventAndIsNonCriticalProcessed_Last(
		String event, boolean isNonCriticalProcessed,
		OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().fetchByEventAndIsNonCriticalProcessed_Last(
			event, isNonCriticalProcessed, orderByComparator);
	}

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	public static NtucSB[] findByEventAndIsNonCriticalProcessed_PrevAndNext(
			long ntucDTId, String event, boolean isNonCriticalProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().
			findByEventAndIsNonCriticalProcessed_PrevAndNext(
				ntucDTId, event, isNonCriticalProcessed, orderByComparator);
	}

	/**
	 * Removes all the ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63; from the database.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 */
	public static void removeByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed) {

		getPersistence().removeByEventAndIsNonCriticalProcessed(
			event, isNonCriticalProcessed);
	}

	/**
	 * Returns the number of ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @return the number of matching ntuc sbs
	 */
	public static int countByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed) {

		return getPersistence().countByEventAndIsNonCriticalProcessed(
			event, isNonCriticalProcessed);
	}

	/**
	 * Caches the ntuc sb in the entity cache if it is enabled.
	 *
	 * @param ntucSB the ntuc sb
	 */
	public static void cacheResult(NtucSB ntucSB) {
		getPersistence().cacheResult(ntucSB);
	}

	/**
	 * Caches the ntuc sbs in the entity cache if it is enabled.
	 *
	 * @param ntucSBs the ntuc sbs
	 */
	public static void cacheResult(List<NtucSB> ntucSBs) {
		getPersistence().cacheResult(ntucSBs);
	}

	/**
	 * Creates a new ntuc sb with the primary key. Does not add the ntuc sb to the database.
	 *
	 * @param ntucDTId the primary key for the new ntuc sb
	 * @return the new ntuc sb
	 */
	public static NtucSB create(long ntucDTId) {
		return getPersistence().create(ntucDTId);
	}

	/**
	 * Removes the ntuc sb with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param ntucDTId the primary key of the ntuc sb
	 * @return the ntuc sb that was removed
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	public static NtucSB remove(long ntucDTId)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().remove(ntucDTId);
	}

	public static NtucSB updateImpl(NtucSB ntucSB) {
		return getPersistence().updateImpl(ntucSB);
	}

	/**
	 * Returns the ntuc sb with the primary key or throws a <code>NoSuchNtucSBException</code> if it could not be found.
	 *
	 * @param ntucDTId the primary key of the ntuc sb
	 * @return the ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	public static NtucSB findByPrimaryKey(long ntucDTId)
		throws com.ntuc.notification.exception.NoSuchNtucSBException {

		return getPersistence().findByPrimaryKey(ntucDTId);
	}

	/**
	 * Returns the ntuc sb with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param ntucDTId the primary key of the ntuc sb
	 * @return the ntuc sb, or <code>null</code> if a ntuc sb with the primary key could not be found
	 */
	public static NtucSB fetchByPrimaryKey(long ntucDTId) {
		return getPersistence().fetchByPrimaryKey(ntucDTId);
	}

	/**
	 * Returns all the ntuc sbs.
	 *
	 * @return the ntuc sbs
	 */
	public static List<NtucSB> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the ntuc sbs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of ntuc sbs
	 */
	public static List<NtucSB> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of ntuc sbs
	 */
	public static List<NtucSB> findAll(
		int start, int end, OrderByComparator<NtucSB> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of ntuc sbs
	 */
	public static List<NtucSB> findAll(
		int start, int end, OrderByComparator<NtucSB> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the ntuc sbs from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of ntuc sbs.
	 *
	 * @return the number of ntuc sbs
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static NtucSBPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<NtucSBPersistence, NtucSBPersistence>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(NtucSBPersistence.class);

		ServiceTracker<NtucSBPersistence, NtucSBPersistence> serviceTracker =
			new ServiceTracker<NtucSBPersistence, NtucSBPersistence>(
				bundle.getBundleContext(), NtucSBPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}