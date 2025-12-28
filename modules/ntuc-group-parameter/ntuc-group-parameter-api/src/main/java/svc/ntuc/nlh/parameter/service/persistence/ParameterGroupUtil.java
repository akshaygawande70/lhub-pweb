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

import svc.ntuc.nlh.parameter.model.ParameterGroup;

/**
 * The persistence utility for the parameter group service. This utility wraps <code>svc.ntuc.nlh.parameter.service.persistence.impl.ParameterGroupPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ParameterGroupPersistence
 * @generated
 */
public class ParameterGroupUtil {

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
	public static void clearCache(ParameterGroup parameterGroup) {
		getPersistence().clearCache(parameterGroup);
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
	public static Map<Serializable, ParameterGroup> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<ParameterGroup> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<ParameterGroup> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<ParameterGroup> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static ParameterGroup update(ParameterGroup parameterGroup) {
		return getPersistence().update(parameterGroup);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static ParameterGroup update(
		ParameterGroup parameterGroup, ServiceContext serviceContext) {

		return getPersistence().update(parameterGroup, serviceContext);
	}

	/**
	 * Returns the parameter group where groupCode = &#63; and deleted = &#63; or throws a <code>NoSuchParameterGroupException</code> if it could not be found.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	public static ParameterGroup findByCode(String groupCode, Boolean deleted)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return getPersistence().findByCode(groupCode, deleted);
	}

	/**
	 * Returns the parameter group where groupCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public static ParameterGroup fetchByCode(
		String groupCode, Boolean deleted) {

		return getPersistence().fetchByCode(groupCode, deleted);
	}

	/**
	 * Returns the parameter group where groupCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public static ParameterGroup fetchByCode(
		String groupCode, Boolean deleted, boolean useFinderCache) {

		return getPersistence().fetchByCode(groupCode, deleted, useFinderCache);
	}

	/**
	 * Removes the parameter group where groupCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the parameter group that was removed
	 */
	public static ParameterGroup removeByCode(String groupCode, Boolean deleted)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return getPersistence().removeByCode(groupCode, deleted);
	}

	/**
	 * Returns the number of parameter groups where groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the number of matching parameter groups
	 */
	public static int countByCode(String groupCode, Boolean deleted) {
		return getPersistence().countByCode(groupCode, deleted);
	}

	/**
	 * Returns all the parameter groups where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @return the matching parameter groups
	 */
	public static List<ParameterGroup> findByParentId(
		long parentId, Boolean deleted) {

		return getPersistence().findByParentId(parentId, deleted);
	}

	/**
	 * Returns a range of all the parameter groups where parentId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @return the range of matching parameter groups
	 */
	public static List<ParameterGroup> findByParentId(
		long parentId, Boolean deleted, int start, int end) {

		return getPersistence().findByParentId(parentId, deleted, start, end);
	}

	/**
	 * Returns an ordered range of all the parameter groups where parentId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching parameter groups
	 */
	public static List<ParameterGroup> findByParentId(
		long parentId, Boolean deleted, int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return getPersistence().findByParentId(
			parentId, deleted, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the parameter groups where parentId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching parameter groups
	 */
	public static List<ParameterGroup> findByParentId(
		long parentId, Boolean deleted, int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByParentId(
			parentId, deleted, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first parameter group in the ordered set where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	public static ParameterGroup findByParentId_First(
			long parentId, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return getPersistence().findByParentId_First(
			parentId, deleted, orderByComparator);
	}

	/**
	 * Returns the first parameter group in the ordered set where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public static ParameterGroup fetchByParentId_First(
		long parentId, Boolean deleted,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return getPersistence().fetchByParentId_First(
			parentId, deleted, orderByComparator);
	}

	/**
	 * Returns the last parameter group in the ordered set where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	public static ParameterGroup findByParentId_Last(
			long parentId, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return getPersistence().findByParentId_Last(
			parentId, deleted, orderByComparator);
	}

	/**
	 * Returns the last parameter group in the ordered set where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public static ParameterGroup fetchByParentId_Last(
		long parentId, Boolean deleted,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return getPersistence().fetchByParentId_Last(
			parentId, deleted, orderByComparator);
	}

	/**
	 * Returns the parameter groups before and after the current parameter group in the ordered set where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the primary key of the current parameter group
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next parameter group
	 * @throws NoSuchParameterGroupException if a parameter group with the primary key could not be found
	 */
	public static ParameterGroup[] findByParentId_PrevAndNext(
			long parameterGroupId, long parentId, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return getPersistence().findByParentId_PrevAndNext(
			parameterGroupId, parentId, deleted, orderByComparator);
	}

	/**
	 * Removes all the parameter groups where parentId = &#63; and deleted = &#63; from the database.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 */
	public static void removeByParentId(long parentId, Boolean deleted) {
		getPersistence().removeByParentId(parentId, deleted);
	}

	/**
	 * Returns the number of parameter groups where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @return the number of matching parameter groups
	 */
	public static int countByParentId(long parentId, Boolean deleted) {
		return getPersistence().countByParentId(parentId, deleted);
	}

	/**
	 * Returns all the parameter groups where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the matching parameter groups
	 */
	public static List<ParameterGroup> findByGroupSite(
		long groupId, Boolean deleted) {

		return getPersistence().findByGroupSite(groupId, deleted);
	}

	/**
	 * Returns a range of all the parameter groups where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @return the range of matching parameter groups
	 */
	public static List<ParameterGroup> findByGroupSite(
		long groupId, Boolean deleted, int start, int end) {

		return getPersistence().findByGroupSite(groupId, deleted, start, end);
	}

	/**
	 * Returns an ordered range of all the parameter groups where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching parameter groups
	 */
	public static List<ParameterGroup> findByGroupSite(
		long groupId, Boolean deleted, int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return getPersistence().findByGroupSite(
			groupId, deleted, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the parameter groups where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching parameter groups
	 */
	public static List<ParameterGroup> findByGroupSite(
		long groupId, Boolean deleted, int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByGroupSite(
			groupId, deleted, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first parameter group in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	public static ParameterGroup findByGroupSite_First(
			long groupId, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return getPersistence().findByGroupSite_First(
			groupId, deleted, orderByComparator);
	}

	/**
	 * Returns the first parameter group in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public static ParameterGroup fetchByGroupSite_First(
		long groupId, Boolean deleted,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return getPersistence().fetchByGroupSite_First(
			groupId, deleted, orderByComparator);
	}

	/**
	 * Returns the last parameter group in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	public static ParameterGroup findByGroupSite_Last(
			long groupId, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return getPersistence().findByGroupSite_Last(
			groupId, deleted, orderByComparator);
	}

	/**
	 * Returns the last parameter group in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public static ParameterGroup fetchByGroupSite_Last(
		long groupId, Boolean deleted,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return getPersistence().fetchByGroupSite_Last(
			groupId, deleted, orderByComparator);
	}

	/**
	 * Returns the parameter groups before and after the current parameter group in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the primary key of the current parameter group
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next parameter group
	 * @throws NoSuchParameterGroupException if a parameter group with the primary key could not be found
	 */
	public static ParameterGroup[] findByGroupSite_PrevAndNext(
			long parameterGroupId, long groupId, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return getPersistence().findByGroupSite_PrevAndNext(
			parameterGroupId, groupId, deleted, orderByComparator);
	}

	/**
	 * Removes all the parameter groups where groupId = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 */
	public static void removeByGroupSite(long groupId, Boolean deleted) {
		getPersistence().removeByGroupSite(groupId, deleted);
	}

	/**
	 * Returns the number of parameter groups where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the number of matching parameter groups
	 */
	public static int countByGroupSite(long groupId, Boolean deleted) {
		return getPersistence().countByGroupSite(groupId, deleted);
	}

	/**
	 * Returns all the parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the matching parameter groups
	 */
	public static List<ParameterGroup> findByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted) {

		return getPersistence().findByGroupSiteCode(
			groupId, groupCode, deleted);
	}

	/**
	 * Returns a range of all the parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @return the range of matching parameter groups
	 */
	public static List<ParameterGroup> findByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted, int start, int end) {

		return getPersistence().findByGroupSiteCode(
			groupId, groupCode, deleted, start, end);
	}

	/**
	 * Returns an ordered range of all the parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching parameter groups
	 */
	public static List<ParameterGroup> findByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted, int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return getPersistence().findByGroupSiteCode(
			groupId, groupCode, deleted, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching parameter groups
	 */
	public static List<ParameterGroup> findByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted, int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByGroupSiteCode(
			groupId, groupCode, deleted, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first parameter group in the ordered set where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	public static ParameterGroup findByGroupSiteCode_First(
			long groupId, String groupCode, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return getPersistence().findByGroupSiteCode_First(
			groupId, groupCode, deleted, orderByComparator);
	}

	/**
	 * Returns the first parameter group in the ordered set where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public static ParameterGroup fetchByGroupSiteCode_First(
		long groupId, String groupCode, Boolean deleted,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return getPersistence().fetchByGroupSiteCode_First(
			groupId, groupCode, deleted, orderByComparator);
	}

	/**
	 * Returns the last parameter group in the ordered set where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	public static ParameterGroup findByGroupSiteCode_Last(
			long groupId, String groupCode, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return getPersistence().findByGroupSiteCode_Last(
			groupId, groupCode, deleted, orderByComparator);
	}

	/**
	 * Returns the last parameter group in the ordered set where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public static ParameterGroup fetchByGroupSiteCode_Last(
		long groupId, String groupCode, Boolean deleted,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return getPersistence().fetchByGroupSiteCode_Last(
			groupId, groupCode, deleted, orderByComparator);
	}

	/**
	 * Returns the parameter groups before and after the current parameter group in the ordered set where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the primary key of the current parameter group
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next parameter group
	 * @throws NoSuchParameterGroupException if a parameter group with the primary key could not be found
	 */
	public static ParameterGroup[] findByGroupSiteCode_PrevAndNext(
			long parameterGroupId, long groupId, String groupCode,
			Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return getPersistence().findByGroupSiteCode_PrevAndNext(
			parameterGroupId, groupId, groupCode, deleted, orderByComparator);
	}

	/**
	 * Removes all the parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 */
	public static void removeByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted) {

		getPersistence().removeByGroupSiteCode(groupId, groupCode, deleted);
	}

	/**
	 * Returns the number of parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the number of matching parameter groups
	 */
	public static int countByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted) {

		return getPersistence().countByGroupSiteCode(
			groupId, groupCode, deleted);
	}

	/**
	 * Caches the parameter group in the entity cache if it is enabled.
	 *
	 * @param parameterGroup the parameter group
	 */
	public static void cacheResult(ParameterGroup parameterGroup) {
		getPersistence().cacheResult(parameterGroup);
	}

	/**
	 * Caches the parameter groups in the entity cache if it is enabled.
	 *
	 * @param parameterGroups the parameter groups
	 */
	public static void cacheResult(List<ParameterGroup> parameterGroups) {
		getPersistence().cacheResult(parameterGroups);
	}

	/**
	 * Creates a new parameter group with the primary key. Does not add the parameter group to the database.
	 *
	 * @param parameterGroupId the primary key for the new parameter group
	 * @return the new parameter group
	 */
	public static ParameterGroup create(long parameterGroupId) {
		return getPersistence().create(parameterGroupId);
	}

	/**
	 * Removes the parameter group with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param parameterGroupId the primary key of the parameter group
	 * @return the parameter group that was removed
	 * @throws NoSuchParameterGroupException if a parameter group with the primary key could not be found
	 */
	public static ParameterGroup remove(long parameterGroupId)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return getPersistence().remove(parameterGroupId);
	}

	public static ParameterGroup updateImpl(ParameterGroup parameterGroup) {
		return getPersistence().updateImpl(parameterGroup);
	}

	/**
	 * Returns the parameter group with the primary key or throws a <code>NoSuchParameterGroupException</code> if it could not be found.
	 *
	 * @param parameterGroupId the primary key of the parameter group
	 * @return the parameter group
	 * @throws NoSuchParameterGroupException if a parameter group with the primary key could not be found
	 */
	public static ParameterGroup findByPrimaryKey(long parameterGroupId)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return getPersistence().findByPrimaryKey(parameterGroupId);
	}

	/**
	 * Returns the parameter group with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param parameterGroupId the primary key of the parameter group
	 * @return the parameter group, or <code>null</code> if a parameter group with the primary key could not be found
	 */
	public static ParameterGroup fetchByPrimaryKey(long parameterGroupId) {
		return getPersistence().fetchByPrimaryKey(parameterGroupId);
	}

	/**
	 * Returns all the parameter groups.
	 *
	 * @return the parameter groups
	 */
	public static List<ParameterGroup> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the parameter groups.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @return the range of parameter groups
	 */
	public static List<ParameterGroup> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the parameter groups.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of parameter groups
	 */
	public static List<ParameterGroup> findAll(
		int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the parameter groups.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of parameter groups
	 */
	public static List<ParameterGroup> findAll(
		int start, int end, OrderByComparator<ParameterGroup> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the parameter groups from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of parameter groups.
	 *
	 * @return the number of parameter groups
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static ParameterGroupPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<ParameterGroupPersistence, ParameterGroupPersistence> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			ParameterGroupPersistence.class);

		ServiceTracker<ParameterGroupPersistence, ParameterGroupPersistence>
			serviceTracker =
				new ServiceTracker
					<ParameterGroupPersistence, ParameterGroupPersistence>(
						bundle.getBundleContext(),
						ParameterGroupPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}