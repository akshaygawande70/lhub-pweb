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

import com.ntuc.notification.exception.NoSuchNtucSBException;
import com.ntuc.notification.model.NtucSB;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the ntuc sb service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see NtucSBUtil
 * @generated
 */
@ProviderType
public interface NtucSBPersistence extends BasePersistence<NtucSB> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link NtucSBUtil} to access the ntuc sb persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the ntuc sbs where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @return the matching ntuc sbs
	 */
	public java.util.List<NtucSB> findByisRowLockFailed(
		boolean isRowLockFailed);

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
	public java.util.List<NtucSB> findByisRowLockFailed(
		boolean isRowLockFailed, int start, int end);

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
	public java.util.List<NtucSB> findByisRowLockFailed(
		boolean isRowLockFailed, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

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
	public java.util.List<NtucSB> findByisRowLockFailed(
		boolean isRowLockFailed, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public NtucSB findByisRowLockFailed_First(
			boolean isRowLockFailed,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Returns the first ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public NtucSB fetchByisRowLockFailed_First(
		boolean isRowLockFailed,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

	/**
	 * Returns the last ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public NtucSB findByisRowLockFailed_Last(
			boolean isRowLockFailed,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Returns the last ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public NtucSB fetchByisRowLockFailed_Last(
		boolean isRowLockFailed,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	public NtucSB[] findByisRowLockFailed_PrevAndNext(
			long ntucDTId, boolean isRowLockFailed,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Removes all the ntuc sbs where isRowLockFailed = &#63; from the database.
	 *
	 * @param isRowLockFailed the is row lock failed
	 */
	public void removeByisRowLockFailed(boolean isRowLockFailed);

	/**
	 * Returns the number of ntuc sbs where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @return the number of matching ntuc sbs
	 */
	public int countByisRowLockFailed(boolean isRowLockFailed);

	/**
	 * Returns all the ntuc sbs where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @return the matching ntuc sbs
	 */
	public java.util.List<NtucSB> findByCourseCodeAndEvent(
		String courseCode, String event);

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
	public java.util.List<NtucSB> findByCourseCodeAndEvent(
		String courseCode, String event, int start, int end);

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
	public java.util.List<NtucSB> findByCourseCodeAndEvent(
		String courseCode, String event, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

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
	public java.util.List<NtucSB> findByCourseCodeAndEvent(
		String courseCode, String event, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first ntuc sb in the ordered set where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public NtucSB findByCourseCodeAndEvent_First(
			String courseCode, String event,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Returns the first ntuc sb in the ordered set where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public NtucSB fetchByCourseCodeAndEvent_First(
		String courseCode, String event,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

	/**
	 * Returns the last ntuc sb in the ordered set where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public NtucSB findByCourseCodeAndEvent_Last(
			String courseCode, String event,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Returns the last ntuc sb in the ordered set where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public NtucSB fetchByCourseCodeAndEvent_Last(
		String courseCode, String event,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

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
	public NtucSB[] findByCourseCodeAndEvent_PrevAndNext(
			long ntucDTId, String courseCode, String event,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Removes all the ntuc sbs where courseCode = &#63; and event = &#63; from the database.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 */
	public void removeByCourseCodeAndEvent(String courseCode, String event);

	/**
	 * Returns the number of ntuc sbs where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @return the number of matching ntuc sbs
	 */
	public int countByCourseCodeAndEvent(String courseCode, String event);

	/**
	 * Returns all the ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @return the matching ntuc sbs
	 */
	public java.util.List<NtucSB> findByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom);

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
	public java.util.List<NtucSB> findByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom, int start, int end);

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
	public java.util.List<NtucSB> findByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

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
	public java.util.List<NtucSB> findByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator,
		boolean useFinderCache);

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
	public NtucSB findByCourseCodeEventAndChangeFrom_First(
			String courseCode, String event, String changeFrom,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Returns the first ntuc sb in the ordered set where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public NtucSB fetchByCourseCodeEventAndChangeFrom_First(
		String courseCode, String event, String changeFrom,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

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
	public NtucSB findByCourseCodeEventAndChangeFrom_Last(
			String courseCode, String event, String changeFrom,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Returns the last ntuc sb in the ordered set where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public NtucSB fetchByCourseCodeEventAndChangeFrom_Last(
		String courseCode, String event, String changeFrom,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

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
	public NtucSB[] findByCourseCodeEventAndChangeFrom_PrevAndNext(
			long ntucDTId, String courseCode, String event, String changeFrom,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Removes all the ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63; from the database.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 */
	public void removeByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom);

	/**
	 * Returns the number of ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @return the number of matching ntuc sbs
	 */
	public int countByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom);

	/**
	 * Returns all the ntuc sbs where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @return the matching ntuc sbs
	 */
	public java.util.List<NtucSB> findByRecordsByChangeFrom(String changeFrom);

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
	public java.util.List<NtucSB> findByRecordsByChangeFrom(
		String changeFrom, int start, int end);

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
	public java.util.List<NtucSB> findByRecordsByChangeFrom(
		String changeFrom, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

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
	public java.util.List<NtucSB> findByRecordsByChangeFrom(
		String changeFrom, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public NtucSB findByRecordsByChangeFrom_First(
			String changeFrom,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Returns the first ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public NtucSB fetchByRecordsByChangeFrom_First(
		String changeFrom,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

	/**
	 * Returns the last ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public NtucSB findByRecordsByChangeFrom_Last(
			String changeFrom,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Returns the last ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public NtucSB fetchByRecordsByChangeFrom_Last(
		String changeFrom,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	public NtucSB[] findByRecordsByChangeFrom_PrevAndNext(
			long ntucDTId, String changeFrom,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Removes all the ntuc sbs where changeFrom LIKE &#63; from the database.
	 *
	 * @param changeFrom the change from
	 */
	public void removeByRecordsByChangeFrom(String changeFrom);

	/**
	 * Returns the number of ntuc sbs where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @return the number of matching ntuc sbs
	 */
	public int countByRecordsByChangeFrom(String changeFrom);

	/**
	 * Returns all the ntuc sbs where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @return the matching ntuc sbs
	 */
	public java.util.List<NtucSB> findByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed);

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
	public java.util.List<NtucSB> findByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed, int start, int end);

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
	public java.util.List<NtucSB> findByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

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
	public java.util.List<NtucSB> findByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public NtucSB findByStatusAndIsCronProcessed_First(
			String event, boolean isCronProcessed,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public NtucSB fetchByStatusAndIsCronProcessed_First(
		String event, boolean isCronProcessed,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public NtucSB findByStatusAndIsCronProcessed_Last(
			String event, boolean isCronProcessed,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public NtucSB fetchByStatusAndIsCronProcessed_Last(
		String event, boolean isCronProcessed,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

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
	public NtucSB[] findByStatusAndIsCronProcessed_PrevAndNext(
			long ntucDTId, String event, boolean isCronProcessed,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Removes all the ntuc sbs where event = &#63; and isCronProcessed = &#63; from the database.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 */
	public void removeByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed);

	/**
	 * Returns the number of ntuc sbs where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @return the number of matching ntuc sbs
	 */
	public int countByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed);

	/**
	 * Returns all the ntuc sbs where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @return the matching ntuc sbs
	 */
	public java.util.List<NtucSB> findByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed);

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
	public java.util.List<NtucSB> findByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed, int start, int end);

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
	public java.util.List<NtucSB> findByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

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
	public java.util.List<NtucSB> findByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public NtucSB findByEventAndIsCriticalProcessed_First(
			String event, boolean isCriticalProcessed,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public NtucSB fetchByEventAndIsCriticalProcessed_First(
		String event, boolean isCriticalProcessed,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public NtucSB findByEventAndIsCriticalProcessed_Last(
			String event, boolean isCriticalProcessed,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public NtucSB fetchByEventAndIsCriticalProcessed_Last(
		String event, boolean isCriticalProcessed,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

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
	public NtucSB[] findByEventAndIsCriticalProcessed_PrevAndNext(
			long ntucDTId, String event, boolean isCriticalProcessed,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Removes all the ntuc sbs where event = &#63; and isCriticalProcessed = &#63; from the database.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 */
	public void removeByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed);

	/**
	 * Returns the number of ntuc sbs where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @return the number of matching ntuc sbs
	 */
	public int countByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed);

	/**
	 * Returns all the ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @return the matching ntuc sbs
	 */
	public java.util.List<NtucSB> findByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed);

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
	public java.util.List<NtucSB> findByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed, int start, int end);

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
	public java.util.List<NtucSB> findByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

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
	public java.util.List<NtucSB> findByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public NtucSB findByEventAndIsNonCriticalProcessed_First(
			String event, boolean isNonCriticalProcessed,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public NtucSB fetchByEventAndIsNonCriticalProcessed_First(
		String event, boolean isNonCriticalProcessed,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	public NtucSB findByEventAndIsNonCriticalProcessed_Last(
			String event, boolean isNonCriticalProcessed,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	public NtucSB fetchByEventAndIsNonCriticalProcessed_Last(
		String event, boolean isNonCriticalProcessed,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

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
	public NtucSB[] findByEventAndIsNonCriticalProcessed_PrevAndNext(
			long ntucDTId, String event, boolean isNonCriticalProcessed,
			com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
				orderByComparator)
		throws NoSuchNtucSBException;

	/**
	 * Removes all the ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63; from the database.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 */
	public void removeByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed);

	/**
	 * Returns the number of ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @return the number of matching ntuc sbs
	 */
	public int countByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed);

	/**
	 * Caches the ntuc sb in the entity cache if it is enabled.
	 *
	 * @param ntucSB the ntuc sb
	 */
	public void cacheResult(NtucSB ntucSB);

	/**
	 * Caches the ntuc sbs in the entity cache if it is enabled.
	 *
	 * @param ntucSBs the ntuc sbs
	 */
	public void cacheResult(java.util.List<NtucSB> ntucSBs);

	/**
	 * Creates a new ntuc sb with the primary key. Does not add the ntuc sb to the database.
	 *
	 * @param ntucDTId the primary key for the new ntuc sb
	 * @return the new ntuc sb
	 */
	public NtucSB create(long ntucDTId);

	/**
	 * Removes the ntuc sb with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param ntucDTId the primary key of the ntuc sb
	 * @return the ntuc sb that was removed
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	public NtucSB remove(long ntucDTId) throws NoSuchNtucSBException;

	public NtucSB updateImpl(NtucSB ntucSB);

	/**
	 * Returns the ntuc sb with the primary key or throws a <code>NoSuchNtucSBException</code> if it could not be found.
	 *
	 * @param ntucDTId the primary key of the ntuc sb
	 * @return the ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	public NtucSB findByPrimaryKey(long ntucDTId) throws NoSuchNtucSBException;

	/**
	 * Returns the ntuc sb with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param ntucDTId the primary key of the ntuc sb
	 * @return the ntuc sb, or <code>null</code> if a ntuc sb with the primary key could not be found
	 */
	public NtucSB fetchByPrimaryKey(long ntucDTId);

	/**
	 * Returns all the ntuc sbs.
	 *
	 * @return the ntuc sbs
	 */
	public java.util.List<NtucSB> findAll();

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
	public java.util.List<NtucSB> findAll(int start, int end);

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
	public java.util.List<NtucSB> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator);

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
	public java.util.List<NtucSB> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucSB>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the ntuc sbs from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of ntuc sbs.
	 *
	 * @return the number of ntuc sbs
	 */
	public int countAll();

}