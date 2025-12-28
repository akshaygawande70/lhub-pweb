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

import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

import svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException;
import svc.ntuc.nlh.parameter.model.ParameterGroup;

/**
 * The persistence interface for the parameter group service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ParameterGroupUtil
 * @generated
 */
@ProviderType
public interface ParameterGroupPersistence
	extends BasePersistence<ParameterGroup> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link ParameterGroupUtil} to access the parameter group persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns the parameter group where groupCode = &#63; and deleted = &#63; or throws a <code>NoSuchParameterGroupException</code> if it could not be found.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	public ParameterGroup findByCode(String groupCode, Boolean deleted)
		throws NoSuchParameterGroupException;

	/**
	 * Returns the parameter group where groupCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public ParameterGroup fetchByCode(String groupCode, Boolean deleted);

	/**
	 * Returns the parameter group where groupCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public ParameterGroup fetchByCode(
		String groupCode, Boolean deleted, boolean useFinderCache);

	/**
	 * Removes the parameter group where groupCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the parameter group that was removed
	 */
	public ParameterGroup removeByCode(String groupCode, Boolean deleted)
		throws NoSuchParameterGroupException;

	/**
	 * Returns the number of parameter groups where groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the number of matching parameter groups
	 */
	public int countByCode(String groupCode, Boolean deleted);

	/**
	 * Returns all the parameter groups where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @return the matching parameter groups
	 */
	public java.util.List<ParameterGroup> findByParentId(
		long parentId, Boolean deleted);

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
	public java.util.List<ParameterGroup> findByParentId(
		long parentId, Boolean deleted, int start, int end);

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
	public java.util.List<ParameterGroup> findByParentId(
		long parentId, Boolean deleted, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
			orderByComparator);

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
	public java.util.List<ParameterGroup> findByParentId(
		long parentId, Boolean deleted, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first parameter group in the ordered set where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	public ParameterGroup findByParentId_First(
			long parentId, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
				orderByComparator)
		throws NoSuchParameterGroupException;

	/**
	 * Returns the first parameter group in the ordered set where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public ParameterGroup fetchByParentId_First(
		long parentId, Boolean deleted,
		com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
			orderByComparator);

	/**
	 * Returns the last parameter group in the ordered set where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	public ParameterGroup findByParentId_Last(
			long parentId, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
				orderByComparator)
		throws NoSuchParameterGroupException;

	/**
	 * Returns the last parameter group in the ordered set where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public ParameterGroup fetchByParentId_Last(
		long parentId, Boolean deleted,
		com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
			orderByComparator);

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
	public ParameterGroup[] findByParentId_PrevAndNext(
			long parameterGroupId, long parentId, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
				orderByComparator)
		throws NoSuchParameterGroupException;

	/**
	 * Removes all the parameter groups where parentId = &#63; and deleted = &#63; from the database.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 */
	public void removeByParentId(long parentId, Boolean deleted);

	/**
	 * Returns the number of parameter groups where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @return the number of matching parameter groups
	 */
	public int countByParentId(long parentId, Boolean deleted);

	/**
	 * Returns all the parameter groups where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the matching parameter groups
	 */
	public java.util.List<ParameterGroup> findByGroupSite(
		long groupId, Boolean deleted);

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
	public java.util.List<ParameterGroup> findByGroupSite(
		long groupId, Boolean deleted, int start, int end);

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
	public java.util.List<ParameterGroup> findByGroupSite(
		long groupId, Boolean deleted, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
			orderByComparator);

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
	public java.util.List<ParameterGroup> findByGroupSite(
		long groupId, Boolean deleted, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first parameter group in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	public ParameterGroup findByGroupSite_First(
			long groupId, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
				orderByComparator)
		throws NoSuchParameterGroupException;

	/**
	 * Returns the first parameter group in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public ParameterGroup fetchByGroupSite_First(
		long groupId, Boolean deleted,
		com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
			orderByComparator);

	/**
	 * Returns the last parameter group in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	public ParameterGroup findByGroupSite_Last(
			long groupId, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
				orderByComparator)
		throws NoSuchParameterGroupException;

	/**
	 * Returns the last parameter group in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public ParameterGroup fetchByGroupSite_Last(
		long groupId, Boolean deleted,
		com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
			orderByComparator);

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
	public ParameterGroup[] findByGroupSite_PrevAndNext(
			long parameterGroupId, long groupId, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
				orderByComparator)
		throws NoSuchParameterGroupException;

	/**
	 * Removes all the parameter groups where groupId = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 */
	public void removeByGroupSite(long groupId, Boolean deleted);

	/**
	 * Returns the number of parameter groups where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the number of matching parameter groups
	 */
	public int countByGroupSite(long groupId, Boolean deleted);

	/**
	 * Returns all the parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the matching parameter groups
	 */
	public java.util.List<ParameterGroup> findByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted);

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
	public java.util.List<ParameterGroup> findByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted, int start, int end);

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
	public java.util.List<ParameterGroup> findByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
			orderByComparator);

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
	public java.util.List<ParameterGroup> findByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
			orderByComparator,
		boolean useFinderCache);

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
	public ParameterGroup findByGroupSiteCode_First(
			long groupId, String groupCode, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
				orderByComparator)
		throws NoSuchParameterGroupException;

	/**
	 * Returns the first parameter group in the ordered set where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public ParameterGroup fetchByGroupSiteCode_First(
		long groupId, String groupCode, Boolean deleted,
		com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
			orderByComparator);

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
	public ParameterGroup findByGroupSiteCode_Last(
			long groupId, String groupCode, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
				orderByComparator)
		throws NoSuchParameterGroupException;

	/**
	 * Returns the last parameter group in the ordered set where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	public ParameterGroup fetchByGroupSiteCode_Last(
		long groupId, String groupCode, Boolean deleted,
		com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
			orderByComparator);

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
	public ParameterGroup[] findByGroupSiteCode_PrevAndNext(
			long parameterGroupId, long groupId, String groupCode,
			Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
				orderByComparator)
		throws NoSuchParameterGroupException;

	/**
	 * Removes all the parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 */
	public void removeByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted);

	/**
	 * Returns the number of parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the number of matching parameter groups
	 */
	public int countByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted);

	/**
	 * Caches the parameter group in the entity cache if it is enabled.
	 *
	 * @param parameterGroup the parameter group
	 */
	public void cacheResult(ParameterGroup parameterGroup);

	/**
	 * Caches the parameter groups in the entity cache if it is enabled.
	 *
	 * @param parameterGroups the parameter groups
	 */
	public void cacheResult(java.util.List<ParameterGroup> parameterGroups);

	/**
	 * Creates a new parameter group with the primary key. Does not add the parameter group to the database.
	 *
	 * @param parameterGroupId the primary key for the new parameter group
	 * @return the new parameter group
	 */
	public ParameterGroup create(long parameterGroupId);

	/**
	 * Removes the parameter group with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param parameterGroupId the primary key of the parameter group
	 * @return the parameter group that was removed
	 * @throws NoSuchParameterGroupException if a parameter group with the primary key could not be found
	 */
	public ParameterGroup remove(long parameterGroupId)
		throws NoSuchParameterGroupException;

	public ParameterGroup updateImpl(ParameterGroup parameterGroup);

	/**
	 * Returns the parameter group with the primary key or throws a <code>NoSuchParameterGroupException</code> if it could not be found.
	 *
	 * @param parameterGroupId the primary key of the parameter group
	 * @return the parameter group
	 * @throws NoSuchParameterGroupException if a parameter group with the primary key could not be found
	 */
	public ParameterGroup findByPrimaryKey(long parameterGroupId)
		throws NoSuchParameterGroupException;

	/**
	 * Returns the parameter group with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param parameterGroupId the primary key of the parameter group
	 * @return the parameter group, or <code>null</code> if a parameter group with the primary key could not be found
	 */
	public ParameterGroup fetchByPrimaryKey(long parameterGroupId);

	/**
	 * Returns all the parameter groups.
	 *
	 * @return the parameter groups
	 */
	public java.util.List<ParameterGroup> findAll();

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
	public java.util.List<ParameterGroup> findAll(int start, int end);

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
	public java.util.List<ParameterGroup> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
			orderByComparator);

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
	public java.util.List<ParameterGroup> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ParameterGroup>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the parameter groups from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of parameter groups.
	 *
	 * @return the number of parameter groups
	 */
	public int countAll();

}