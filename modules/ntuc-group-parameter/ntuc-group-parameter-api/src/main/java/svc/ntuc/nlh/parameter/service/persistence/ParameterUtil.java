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

import svc.ntuc.nlh.parameter.model.Parameter;

/**
 * The persistence utility for the parameter service. This utility wraps <code>svc.ntuc.nlh.parameter.service.persistence.impl.ParameterPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ParameterPersistence
 * @generated
 */
public class ParameterUtil {

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
	public static void clearCache(Parameter parameter) {
		getPersistence().clearCache(parameter);
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
	public static Map<Serializable, Parameter> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<Parameter> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<Parameter> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<Parameter> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<Parameter> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static Parameter update(Parameter parameter) {
		return getPersistence().update(parameter);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static Parameter update(
		Parameter parameter, ServiceContext serviceContext) {

		return getPersistence().update(parameter, serviceContext);
	}

	/**
	 * Returns all the parameters where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @return the matching parameters
	 */
	public static List<Parameter> findByGroup(
		long parameterGroupId, Boolean deleted) {

		return getPersistence().findByGroup(parameterGroupId, deleted);
	}

	/**
	 * Returns a range of all the parameters where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @return the range of matching parameters
	 */
	public static List<Parameter> findByGroup(
		long parameterGroupId, Boolean deleted, int start, int end) {

		return getPersistence().findByGroup(
			parameterGroupId, deleted, start, end);
	}

	/**
	 * Returns an ordered range of all the parameters where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching parameters
	 */
	public static List<Parameter> findByGroup(
		long parameterGroupId, Boolean deleted, int start, int end,
		OrderByComparator<Parameter> orderByComparator) {

		return getPersistence().findByGroup(
			parameterGroupId, deleted, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the parameters where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching parameters
	 */
	public static List<Parameter> findByGroup(
		long parameterGroupId, Boolean deleted, int start, int end,
		OrderByComparator<Parameter> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByGroup(
			parameterGroupId, deleted, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first parameter in the ordered set where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	public static Parameter findByGroup_First(
			long parameterGroupId, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().findByGroup_First(
			parameterGroupId, deleted, orderByComparator);
	}

	/**
	 * Returns the first parameter in the ordered set where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public static Parameter fetchByGroup_First(
		long parameterGroupId, Boolean deleted,
		OrderByComparator<Parameter> orderByComparator) {

		return getPersistence().fetchByGroup_First(
			parameterGroupId, deleted, orderByComparator);
	}

	/**
	 * Returns the last parameter in the ordered set where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	public static Parameter findByGroup_Last(
			long parameterGroupId, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().findByGroup_Last(
			parameterGroupId, deleted, orderByComparator);
	}

	/**
	 * Returns the last parameter in the ordered set where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public static Parameter fetchByGroup_Last(
		long parameterGroupId, Boolean deleted,
		OrderByComparator<Parameter> orderByComparator) {

		return getPersistence().fetchByGroup_Last(
			parameterGroupId, deleted, orderByComparator);
	}

	/**
	 * Returns the parameters before and after the current parameter in the ordered set where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterId the primary key of the current parameter
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next parameter
	 * @throws NoSuchParameterException if a parameter with the primary key could not be found
	 */
	public static Parameter[] findByGroup_PrevAndNext(
			long parameterId, long parameterGroupId, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().findByGroup_PrevAndNext(
			parameterId, parameterGroupId, deleted, orderByComparator);
	}

	/**
	 * Removes all the parameters where parameterGroupId = &#63; and deleted = &#63; from the database.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 */
	public static void removeByGroup(long parameterGroupId, Boolean deleted) {
		getPersistence().removeByGroup(parameterGroupId, deleted);
	}

	/**
	 * Returns the number of parameters where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	public static int countByGroup(long parameterGroupId, Boolean deleted) {
		return getPersistence().countByGroup(parameterGroupId, deleted);
	}

	/**
	 * Returns the parameter where paramCode = &#63; and deleted = &#63; or throws a <code>NoSuchParameterException</code> if it could not be found.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	public static Parameter findByCode(String paramCode, Boolean deleted)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().findByCode(paramCode, deleted);
	}

	/**
	 * Returns the parameter where paramCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public static Parameter fetchByCode(String paramCode, Boolean deleted) {
		return getPersistence().fetchByCode(paramCode, deleted);
	}

	/**
	 * Returns the parameter where paramCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public static Parameter fetchByCode(
		String paramCode, Boolean deleted, boolean useFinderCache) {

		return getPersistence().fetchByCode(paramCode, deleted, useFinderCache);
	}

	/**
	 * Removes the parameter where paramCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the parameter that was removed
	 */
	public static Parameter removeByCode(String paramCode, Boolean deleted)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().removeByCode(paramCode, deleted);
	}

	/**
	 * Returns the number of parameters where paramCode = &#63; and deleted = &#63;.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	public static int countByCode(String paramCode, Boolean deleted) {
		return getPersistence().countByCode(paramCode, deleted);
	}

	/**
	 * Returns all the parameters where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the matching parameters
	 */
	public static List<Parameter> findByGroupSite(
		long groupId, Boolean deleted) {

		return getPersistence().findByGroupSite(groupId, deleted);
	}

	/**
	 * Returns a range of all the parameters where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @return the range of matching parameters
	 */
	public static List<Parameter> findByGroupSite(
		long groupId, Boolean deleted, int start, int end) {

		return getPersistence().findByGroupSite(groupId, deleted, start, end);
	}

	/**
	 * Returns an ordered range of all the parameters where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching parameters
	 */
	public static List<Parameter> findByGroupSite(
		long groupId, Boolean deleted, int start, int end,
		OrderByComparator<Parameter> orderByComparator) {

		return getPersistence().findByGroupSite(
			groupId, deleted, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the parameters where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching parameters
	 */
	public static List<Parameter> findByGroupSite(
		long groupId, Boolean deleted, int start, int end,
		OrderByComparator<Parameter> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByGroupSite(
			groupId, deleted, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first parameter in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	public static Parameter findByGroupSite_First(
			long groupId, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().findByGroupSite_First(
			groupId, deleted, orderByComparator);
	}

	/**
	 * Returns the first parameter in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public static Parameter fetchByGroupSite_First(
		long groupId, Boolean deleted,
		OrderByComparator<Parameter> orderByComparator) {

		return getPersistence().fetchByGroupSite_First(
			groupId, deleted, orderByComparator);
	}

	/**
	 * Returns the last parameter in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	public static Parameter findByGroupSite_Last(
			long groupId, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().findByGroupSite_Last(
			groupId, deleted, orderByComparator);
	}

	/**
	 * Returns the last parameter in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public static Parameter fetchByGroupSite_Last(
		long groupId, Boolean deleted,
		OrderByComparator<Parameter> orderByComparator) {

		return getPersistence().fetchByGroupSite_Last(
			groupId, deleted, orderByComparator);
	}

	/**
	 * Returns the parameters before and after the current parameter in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterId the primary key of the current parameter
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next parameter
	 * @throws NoSuchParameterException if a parameter with the primary key could not be found
	 */
	public static Parameter[] findByGroupSite_PrevAndNext(
			long parameterId, long groupId, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().findByGroupSite_PrevAndNext(
			parameterId, groupId, deleted, orderByComparator);
	}

	/**
	 * Removes all the parameters where groupId = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 */
	public static void removeByGroupSite(long groupId, Boolean deleted) {
		getPersistence().removeByGroupSite(groupId, deleted);
	}

	/**
	 * Returns the number of parameters where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	public static int countByGroupSite(long groupId, Boolean deleted) {
		return getPersistence().countByGroupSite(groupId, deleted);
	}

	/**
	 * Returns all the parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the matching parameters
	 */
	public static List<Parameter> findByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted) {

		return getPersistence().findByGroupSiteCode(
			groupId, paramCode, deleted);
	}

	/**
	 * Returns a range of all the parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @return the range of matching parameters
	 */
	public static List<Parameter> findByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted, int start, int end) {

		return getPersistence().findByGroupSiteCode(
			groupId, paramCode, deleted, start, end);
	}

	/**
	 * Returns an ordered range of all the parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching parameters
	 */
	public static List<Parameter> findByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted, int start, int end,
		OrderByComparator<Parameter> orderByComparator) {

		return getPersistence().findByGroupSiteCode(
			groupId, paramCode, deleted, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching parameters
	 */
	public static List<Parameter> findByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted, int start, int end,
		OrderByComparator<Parameter> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByGroupSiteCode(
			groupId, paramCode, deleted, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first parameter in the ordered set where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	public static Parameter findByGroupSiteCode_First(
			long groupId, String paramCode, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().findByGroupSiteCode_First(
			groupId, paramCode, deleted, orderByComparator);
	}

	/**
	 * Returns the first parameter in the ordered set where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public static Parameter fetchByGroupSiteCode_First(
		long groupId, String paramCode, Boolean deleted,
		OrderByComparator<Parameter> orderByComparator) {

		return getPersistence().fetchByGroupSiteCode_First(
			groupId, paramCode, deleted, orderByComparator);
	}

	/**
	 * Returns the last parameter in the ordered set where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	public static Parameter findByGroupSiteCode_Last(
			long groupId, String paramCode, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().findByGroupSiteCode_Last(
			groupId, paramCode, deleted, orderByComparator);
	}

	/**
	 * Returns the last parameter in the ordered set where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public static Parameter fetchByGroupSiteCode_Last(
		long groupId, String paramCode, Boolean deleted,
		OrderByComparator<Parameter> orderByComparator) {

		return getPersistence().fetchByGroupSiteCode_Last(
			groupId, paramCode, deleted, orderByComparator);
	}

	/**
	 * Returns the parameters before and after the current parameter in the ordered set where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param parameterId the primary key of the current parameter
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next parameter
	 * @throws NoSuchParameterException if a parameter with the primary key could not be found
	 */
	public static Parameter[] findByGroupSiteCode_PrevAndNext(
			long parameterId, long groupId, String paramCode, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().findByGroupSiteCode_PrevAndNext(
			parameterId, groupId, paramCode, deleted, orderByComparator);
	}

	/**
	 * Removes all the parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 */
	public static void removeByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted) {

		getPersistence().removeByGroupSiteCode(groupId, paramCode, deleted);
	}

	/**
	 * Returns the number of parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	public static int countByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted) {

		return getPersistence().countByGroupSiteCode(
			groupId, paramCode, deleted);
	}

	/**
	 * Returns the parameter where groupId = &#63; and parameterGroupId = &#63; and paramCode = &#63; and deleted = &#63; or throws a <code>NoSuchParameterException</code> if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param parameterGroupId the parameter group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	public static Parameter findByGroupCode(
			long groupId, long parameterGroupId, String paramCode,
			Boolean deleted)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().findByGroupCode(
			groupId, parameterGroupId, paramCode, deleted);
	}

	/**
	 * Returns the parameter where groupId = &#63; and parameterGroupId = &#63; and paramCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param parameterGroupId the parameter group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public static Parameter fetchByGroupCode(
		long groupId, long parameterGroupId, String paramCode,
		Boolean deleted) {

		return getPersistence().fetchByGroupCode(
			groupId, parameterGroupId, paramCode, deleted);
	}

	/**
	 * Returns the parameter where groupId = &#63; and parameterGroupId = &#63; and paramCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param parameterGroupId the parameter group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public static Parameter fetchByGroupCode(
		long groupId, long parameterGroupId, String paramCode, Boolean deleted,
		boolean useFinderCache) {

		return getPersistence().fetchByGroupCode(
			groupId, parameterGroupId, paramCode, deleted, useFinderCache);
	}

	/**
	 * Removes the parameter where groupId = &#63; and parameterGroupId = &#63; and paramCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param parameterGroupId the parameter group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the parameter that was removed
	 */
	public static Parameter removeByGroupCode(
			long groupId, long parameterGroupId, String paramCode,
			Boolean deleted)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().removeByGroupCode(
			groupId, parameterGroupId, paramCode, deleted);
	}

	/**
	 * Returns the number of parameters where groupId = &#63; and parameterGroupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param parameterGroupId the parameter group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	public static int countByGroupCode(
		long groupId, long parameterGroupId, String paramCode,
		Boolean deleted) {

		return getPersistence().countByGroupCode(
			groupId, parameterGroupId, paramCode, deleted);
	}

	/**
	 * Caches the parameter in the entity cache if it is enabled.
	 *
	 * @param parameter the parameter
	 */
	public static void cacheResult(Parameter parameter) {
		getPersistence().cacheResult(parameter);
	}

	/**
	 * Caches the parameters in the entity cache if it is enabled.
	 *
	 * @param parameters the parameters
	 */
	public static void cacheResult(List<Parameter> parameters) {
		getPersistence().cacheResult(parameters);
	}

	/**
	 * Creates a new parameter with the primary key. Does not add the parameter to the database.
	 *
	 * @param parameterId the primary key for the new parameter
	 * @return the new parameter
	 */
	public static Parameter create(long parameterId) {
		return getPersistence().create(parameterId);
	}

	/**
	 * Removes the parameter with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param parameterId the primary key of the parameter
	 * @return the parameter that was removed
	 * @throws NoSuchParameterException if a parameter with the primary key could not be found
	 */
	public static Parameter remove(long parameterId)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().remove(parameterId);
	}

	public static Parameter updateImpl(Parameter parameter) {
		return getPersistence().updateImpl(parameter);
	}

	/**
	 * Returns the parameter with the primary key or throws a <code>NoSuchParameterException</code> if it could not be found.
	 *
	 * @param parameterId the primary key of the parameter
	 * @return the parameter
	 * @throws NoSuchParameterException if a parameter with the primary key could not be found
	 */
	public static Parameter findByPrimaryKey(long parameterId)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterException {

		return getPersistence().findByPrimaryKey(parameterId);
	}

	/**
	 * Returns the parameter with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param parameterId the primary key of the parameter
	 * @return the parameter, or <code>null</code> if a parameter with the primary key could not be found
	 */
	public static Parameter fetchByPrimaryKey(long parameterId) {
		return getPersistence().fetchByPrimaryKey(parameterId);
	}

	/**
	 * Returns all the parameters.
	 *
	 * @return the parameters
	 */
	public static List<Parameter> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the parameters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @return the range of parameters
	 */
	public static List<Parameter> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the parameters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of parameters
	 */
	public static List<Parameter> findAll(
		int start, int end, OrderByComparator<Parameter> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the parameters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of parameters
	 */
	public static List<Parameter> findAll(
		int start, int end, OrderByComparator<Parameter> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the parameters from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of parameters.
	 *
	 * @return the number of parameters
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static ParameterPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<ParameterPersistence, ParameterPersistence>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(ParameterPersistence.class);

		ServiceTracker<ParameterPersistence, ParameterPersistence>
			serviceTracker =
				new ServiceTracker<ParameterPersistence, ParameterPersistence>(
					bundle.getBundleContext(), ParameterPersistence.class,
					null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}