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

package svc.ntuc.nlh.parameter.service.persistence;

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

import svc.ntuc.nlh.parameter.model.ReservedParameter;

/**
 * The persistence utility for the reserved parameter service. This utility wraps <code>svc.ntuc.nlh.parameter.service.persistence.impl.ReservedParameterPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ReservedParameterPersistence
 * @generated
 */
public class ReservedParameterUtil {

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
	public static void clearCache(ReservedParameter reservedParameter) {
		getPersistence().clearCache(reservedParameter);
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
	public static Map<Serializable, ReservedParameter> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<ReservedParameter> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<ReservedParameter> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<ReservedParameter> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<ReservedParameter> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static ReservedParameter update(
		ReservedParameter reservedParameter) {

		return getPersistence().update(reservedParameter);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static ReservedParameter update(
		ReservedParameter reservedParameter, ServiceContext serviceContext) {

		return getPersistence().update(reservedParameter, serviceContext);
	}

	/**
	 * Returns all the reserved parameters where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @return the matching reserved parameters
	 */
	public static List<ReservedParameter> findByGroup(long parameterGroupId) {
		return getPersistence().findByGroup(parameterGroupId);
	}

	/**
	 * Returns a range of all the reserved parameters where parameterGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @return the range of matching reserved parameters
	 */
	public static List<ReservedParameter> findByGroup(
		long parameterGroupId, int start, int end) {

		return getPersistence().findByGroup(parameterGroupId, start, end);
	}

	/**
	 * Returns an ordered range of all the reserved parameters where parameterGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching reserved parameters
	 */
	public static List<ReservedParameter> findByGroup(
		long parameterGroupId, int start, int end,
		OrderByComparator<ReservedParameter> orderByComparator) {

		return getPersistence().findByGroup(
			parameterGroupId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the reserved parameters where parameterGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching reserved parameters
	 */
	public static List<ReservedParameter> findByGroup(
		long parameterGroupId, int start, int end,
		OrderByComparator<ReservedParameter> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByGroup(
			parameterGroupId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	public static ReservedParameter findByGroup_First(
			long parameterGroupId,
			OrderByComparator<ReservedParameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.
			NoSuchReservedParameterException {

		return getPersistence().findByGroup_First(
			parameterGroupId, orderByComparator);
	}

	/**
	 * Returns the first reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	public static ReservedParameter fetchByGroup_First(
		long parameterGroupId,
		OrderByComparator<ReservedParameter> orderByComparator) {

		return getPersistence().fetchByGroup_First(
			parameterGroupId, orderByComparator);
	}

	/**
	 * Returns the last reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	public static ReservedParameter findByGroup_Last(
			long parameterGroupId,
			OrderByComparator<ReservedParameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.
			NoSuchReservedParameterException {

		return getPersistence().findByGroup_Last(
			parameterGroupId, orderByComparator);
	}

	/**
	 * Returns the last reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	public static ReservedParameter fetchByGroup_Last(
		long parameterGroupId,
		OrderByComparator<ReservedParameter> orderByComparator) {

		return getPersistence().fetchByGroup_Last(
			parameterGroupId, orderByComparator);
	}

	/**
	 * Returns the reserved parameters before and after the current reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param reservedParameterId the primary key of the current reserved parameter
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next reserved parameter
	 * @throws NoSuchReservedParameterException if a reserved parameter with the primary key could not be found
	 */
	public static ReservedParameter[] findByGroup_PrevAndNext(
			long reservedParameterId, long parameterGroupId,
			OrderByComparator<ReservedParameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.
			NoSuchReservedParameterException {

		return getPersistence().findByGroup_PrevAndNext(
			reservedParameterId, parameterGroupId, orderByComparator);
	}

	/**
	 * Removes all the reserved parameters where parameterGroupId = &#63; from the database.
	 *
	 * @param parameterGroupId the parameter group ID
	 */
	public static void removeByGroup(long parameterGroupId) {
		getPersistence().removeByGroup(parameterGroupId);
	}

	/**
	 * Returns the number of reserved parameters where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @return the number of matching reserved parameters
	 */
	public static int countByGroup(long parameterGroupId) {
		return getPersistence().countByGroup(parameterGroupId);
	}

	/**
	 * Returns the reserved parameter where paramCode = &#63; or throws a <code>NoSuchReservedParameterException</code> if it could not be found.
	 *
	 * @param paramCode the param code
	 * @return the matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	public static ReservedParameter findByCode(String paramCode)
		throws svc.ntuc.nlh.parameter.exception.
			NoSuchReservedParameterException {

		return getPersistence().findByCode(paramCode);
	}

	/**
	 * Returns the reserved parameter where paramCode = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param paramCode the param code
	 * @return the matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	public static ReservedParameter fetchByCode(String paramCode) {
		return getPersistence().fetchByCode(paramCode);
	}

	/**
	 * Returns the reserved parameter where paramCode = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param paramCode the param code
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	public static ReservedParameter fetchByCode(
		String paramCode, boolean useFinderCache) {

		return getPersistence().fetchByCode(paramCode, useFinderCache);
	}

	/**
	 * Removes the reserved parameter where paramCode = &#63; from the database.
	 *
	 * @param paramCode the param code
	 * @return the reserved parameter that was removed
	 */
	public static ReservedParameter removeByCode(String paramCode)
		throws svc.ntuc.nlh.parameter.exception.
			NoSuchReservedParameterException {

		return getPersistence().removeByCode(paramCode);
	}

	/**
	 * Returns the number of reserved parameters where paramCode = &#63;.
	 *
	 * @param paramCode the param code
	 * @return the number of matching reserved parameters
	 */
	public static int countByCode(String paramCode) {
		return getPersistence().countByCode(paramCode);
	}

	/**
	 * Returns all the reserved parameters where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @return the matching reserved parameters
	 */
	public static List<ReservedParameter> findByReservedBy(String reservedBy) {
		return getPersistence().findByReservedBy(reservedBy);
	}

	/**
	 * Returns a range of all the reserved parameters where reservedBy = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param reservedBy the reserved by
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @return the range of matching reserved parameters
	 */
	public static List<ReservedParameter> findByReservedBy(
		String reservedBy, int start, int end) {

		return getPersistence().findByReservedBy(reservedBy, start, end);
	}

	/**
	 * Returns an ordered range of all the reserved parameters where reservedBy = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param reservedBy the reserved by
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching reserved parameters
	 */
	public static List<ReservedParameter> findByReservedBy(
		String reservedBy, int start, int end,
		OrderByComparator<ReservedParameter> orderByComparator) {

		return getPersistence().findByReservedBy(
			reservedBy, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the reserved parameters where reservedBy = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param reservedBy the reserved by
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching reserved parameters
	 */
	public static List<ReservedParameter> findByReservedBy(
		String reservedBy, int start, int end,
		OrderByComparator<ReservedParameter> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByReservedBy(
			reservedBy, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	public static ReservedParameter findByReservedBy_First(
			String reservedBy,
			OrderByComparator<ReservedParameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.
			NoSuchReservedParameterException {

		return getPersistence().findByReservedBy_First(
			reservedBy, orderByComparator);
	}

	/**
	 * Returns the first reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	public static ReservedParameter fetchByReservedBy_First(
		String reservedBy,
		OrderByComparator<ReservedParameter> orderByComparator) {

		return getPersistence().fetchByReservedBy_First(
			reservedBy, orderByComparator);
	}

	/**
	 * Returns the last reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	public static ReservedParameter findByReservedBy_Last(
			String reservedBy,
			OrderByComparator<ReservedParameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.
			NoSuchReservedParameterException {

		return getPersistence().findByReservedBy_Last(
			reservedBy, orderByComparator);
	}

	/**
	 * Returns the last reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	public static ReservedParameter fetchByReservedBy_Last(
		String reservedBy,
		OrderByComparator<ReservedParameter> orderByComparator) {

		return getPersistence().fetchByReservedBy_Last(
			reservedBy, orderByComparator);
	}

	/**
	 * Returns the reserved parameters before and after the current reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedParameterId the primary key of the current reserved parameter
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next reserved parameter
	 * @throws NoSuchReservedParameterException if a reserved parameter with the primary key could not be found
	 */
	public static ReservedParameter[] findByReservedBy_PrevAndNext(
			long reservedParameterId, String reservedBy,
			OrderByComparator<ReservedParameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.
			NoSuchReservedParameterException {

		return getPersistence().findByReservedBy_PrevAndNext(
			reservedParameterId, reservedBy, orderByComparator);
	}

	/**
	 * Removes all the reserved parameters where reservedBy = &#63; from the database.
	 *
	 * @param reservedBy the reserved by
	 */
	public static void removeByReservedBy(String reservedBy) {
		getPersistence().removeByReservedBy(reservedBy);
	}

	/**
	 * Returns the number of reserved parameters where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @return the number of matching reserved parameters
	 */
	public static int countByReservedBy(String reservedBy) {
		return getPersistence().countByReservedBy(reservedBy);
	}

	/**
	 * Caches the reserved parameter in the entity cache if it is enabled.
	 *
	 * @param reservedParameter the reserved parameter
	 */
	public static void cacheResult(ReservedParameter reservedParameter) {
		getPersistence().cacheResult(reservedParameter);
	}

	/**
	 * Caches the reserved parameters in the entity cache if it is enabled.
	 *
	 * @param reservedParameters the reserved parameters
	 */
	public static void cacheResult(List<ReservedParameter> reservedParameters) {
		getPersistence().cacheResult(reservedParameters);
	}

	/**
	 * Creates a new reserved parameter with the primary key. Does not add the reserved parameter to the database.
	 *
	 * @param reservedParameterId the primary key for the new reserved parameter
	 * @return the new reserved parameter
	 */
	public static ReservedParameter create(long reservedParameterId) {
		return getPersistence().create(reservedParameterId);
	}

	/**
	 * Removes the reserved parameter with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param reservedParameterId the primary key of the reserved parameter
	 * @return the reserved parameter that was removed
	 * @throws NoSuchReservedParameterException if a reserved parameter with the primary key could not be found
	 */
	public static ReservedParameter remove(long reservedParameterId)
		throws svc.ntuc.nlh.parameter.exception.
			NoSuchReservedParameterException {

		return getPersistence().remove(reservedParameterId);
	}

	public static ReservedParameter updateImpl(
		ReservedParameter reservedParameter) {

		return getPersistence().updateImpl(reservedParameter);
	}

	/**
	 * Returns the reserved parameter with the primary key or throws a <code>NoSuchReservedParameterException</code> if it could not be found.
	 *
	 * @param reservedParameterId the primary key of the reserved parameter
	 * @return the reserved parameter
	 * @throws NoSuchReservedParameterException if a reserved parameter with the primary key could not be found
	 */
	public static ReservedParameter findByPrimaryKey(long reservedParameterId)
		throws svc.ntuc.nlh.parameter.exception.
			NoSuchReservedParameterException {

		return getPersistence().findByPrimaryKey(reservedParameterId);
	}

	/**
	 * Returns the reserved parameter with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param reservedParameterId the primary key of the reserved parameter
	 * @return the reserved parameter, or <code>null</code> if a reserved parameter with the primary key could not be found
	 */
	public static ReservedParameter fetchByPrimaryKey(
		long reservedParameterId) {

		return getPersistence().fetchByPrimaryKey(reservedParameterId);
	}

	/**
	 * Returns all the reserved parameters.
	 *
	 * @return the reserved parameters
	 */
	public static List<ReservedParameter> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the reserved parameters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @return the range of reserved parameters
	 */
	public static List<ReservedParameter> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the reserved parameters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of reserved parameters
	 */
	public static List<ReservedParameter> findAll(
		int start, int end,
		OrderByComparator<ReservedParameter> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the reserved parameters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of reserved parameters
	 */
	public static List<ReservedParameter> findAll(
		int start, int end,
		OrderByComparator<ReservedParameter> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the reserved parameters from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of reserved parameters.
	 *
	 * @return the number of reserved parameters
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static ReservedParameterPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<ReservedParameterPersistence, ReservedParameterPersistence>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			ReservedParameterPersistence.class);

		ServiceTracker
			<ReservedParameterPersistence, ReservedParameterPersistence>
				serviceTracker =
					new ServiceTracker
						<ReservedParameterPersistence,
						 ReservedParameterPersistence>(
							 bundle.getBundleContext(),
							 ReservedParameterPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}