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

import svc.ntuc.nlh.parameter.exception.NoSuchParameterException;
import svc.ntuc.nlh.parameter.model.Parameter;

/**
 * The persistence interface for the parameter service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ParameterUtil
 * @generated
 */
@ProviderType
public interface ParameterPersistence extends BasePersistence<Parameter> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link ParameterUtil} to access the parameter persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the parameters where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @return the matching parameters
	 */
	public java.util.List<Parameter> findByGroup(
		long parameterGroupId, Boolean deleted);

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
	public java.util.List<Parameter> findByGroup(
		long parameterGroupId, Boolean deleted, int start, int end);

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
	public java.util.List<Parameter> findByGroup(
		long parameterGroupId, Boolean deleted, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Parameter>
			orderByComparator);

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
	public java.util.List<Parameter> findByGroup(
		long parameterGroupId, Boolean deleted, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Parameter>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first parameter in the ordered set where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	public Parameter findByGroup_First(
			long parameterGroupId, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<Parameter>
				orderByComparator)
		throws NoSuchParameterException;

	/**
	 * Returns the first parameter in the ordered set where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public Parameter fetchByGroup_First(
		long parameterGroupId, Boolean deleted,
		com.liferay.portal.kernel.util.OrderByComparator<Parameter>
			orderByComparator);

	/**
	 * Returns the last parameter in the ordered set where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	public Parameter findByGroup_Last(
			long parameterGroupId, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<Parameter>
				orderByComparator)
		throws NoSuchParameterException;

	/**
	 * Returns the last parameter in the ordered set where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public Parameter fetchByGroup_Last(
		long parameterGroupId, Boolean deleted,
		com.liferay.portal.kernel.util.OrderByComparator<Parameter>
			orderByComparator);

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
	public Parameter[] findByGroup_PrevAndNext(
			long parameterId, long parameterGroupId, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<Parameter>
				orderByComparator)
		throws NoSuchParameterException;

	/**
	 * Removes all the parameters where parameterGroupId = &#63; and deleted = &#63; from the database.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 */
	public void removeByGroup(long parameterGroupId, Boolean deleted);

	/**
	 * Returns the number of parameters where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	public int countByGroup(long parameterGroupId, Boolean deleted);

	/**
	 * Returns the parameter where paramCode = &#63; and deleted = &#63; or throws a <code>NoSuchParameterException</code> if it could not be found.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	public Parameter findByCode(String paramCode, Boolean deleted)
		throws NoSuchParameterException;

	/**
	 * Returns the parameter where paramCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public Parameter fetchByCode(String paramCode, Boolean deleted);

	/**
	 * Returns the parameter where paramCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public Parameter fetchByCode(
		String paramCode, Boolean deleted, boolean useFinderCache);

	/**
	 * Removes the parameter where paramCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the parameter that was removed
	 */
	public Parameter removeByCode(String paramCode, Boolean deleted)
		throws NoSuchParameterException;

	/**
	 * Returns the number of parameters where paramCode = &#63; and deleted = &#63;.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	public int countByCode(String paramCode, Boolean deleted);

	/**
	 * Returns all the parameters where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the matching parameters
	 */
	public java.util.List<Parameter> findByGroupSite(
		long groupId, Boolean deleted);

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
	public java.util.List<Parameter> findByGroupSite(
		long groupId, Boolean deleted, int start, int end);

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
	public java.util.List<Parameter> findByGroupSite(
		long groupId, Boolean deleted, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Parameter>
			orderByComparator);

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
	public java.util.List<Parameter> findByGroupSite(
		long groupId, Boolean deleted, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Parameter>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first parameter in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	public Parameter findByGroupSite_First(
			long groupId, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<Parameter>
				orderByComparator)
		throws NoSuchParameterException;

	/**
	 * Returns the first parameter in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public Parameter fetchByGroupSite_First(
		long groupId, Boolean deleted,
		com.liferay.portal.kernel.util.OrderByComparator<Parameter>
			orderByComparator);

	/**
	 * Returns the last parameter in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	public Parameter findByGroupSite_Last(
			long groupId, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<Parameter>
				orderByComparator)
		throws NoSuchParameterException;

	/**
	 * Returns the last parameter in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public Parameter fetchByGroupSite_Last(
		long groupId, Boolean deleted,
		com.liferay.portal.kernel.util.OrderByComparator<Parameter>
			orderByComparator);

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
	public Parameter[] findByGroupSite_PrevAndNext(
			long parameterId, long groupId, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<Parameter>
				orderByComparator)
		throws NoSuchParameterException;

	/**
	 * Removes all the parameters where groupId = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 */
	public void removeByGroupSite(long groupId, Boolean deleted);

	/**
	 * Returns the number of parameters where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	public int countByGroupSite(long groupId, Boolean deleted);

	/**
	 * Returns all the parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the matching parameters
	 */
	public java.util.List<Parameter> findByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted);

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
	public java.util.List<Parameter> findByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted, int start, int end);

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
	public java.util.List<Parameter> findByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Parameter>
			orderByComparator);

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
	public java.util.List<Parameter> findByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Parameter>
			orderByComparator,
		boolean useFinderCache);

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
	public Parameter findByGroupSiteCode_First(
			long groupId, String paramCode, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<Parameter>
				orderByComparator)
		throws NoSuchParameterException;

	/**
	 * Returns the first parameter in the ordered set where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public Parameter fetchByGroupSiteCode_First(
		long groupId, String paramCode, Boolean deleted,
		com.liferay.portal.kernel.util.OrderByComparator<Parameter>
			orderByComparator);

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
	public Parameter findByGroupSiteCode_Last(
			long groupId, String paramCode, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<Parameter>
				orderByComparator)
		throws NoSuchParameterException;

	/**
	 * Returns the last parameter in the ordered set where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public Parameter fetchByGroupSiteCode_Last(
		long groupId, String paramCode, Boolean deleted,
		com.liferay.portal.kernel.util.OrderByComparator<Parameter>
			orderByComparator);

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
	public Parameter[] findByGroupSiteCode_PrevAndNext(
			long parameterId, long groupId, String paramCode, Boolean deleted,
			com.liferay.portal.kernel.util.OrderByComparator<Parameter>
				orderByComparator)
		throws NoSuchParameterException;

	/**
	 * Removes all the parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 */
	public void removeByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted);

	/**
	 * Returns the number of parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	public int countByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted);

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
	public Parameter findByGroupCode(
			long groupId, long parameterGroupId, String paramCode,
			Boolean deleted)
		throws NoSuchParameterException;

	/**
	 * Returns the parameter where groupId = &#63; and parameterGroupId = &#63; and paramCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param parameterGroupId the parameter group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	public Parameter fetchByGroupCode(
		long groupId, long parameterGroupId, String paramCode, Boolean deleted);

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
	public Parameter fetchByGroupCode(
		long groupId, long parameterGroupId, String paramCode, Boolean deleted,
		boolean useFinderCache);

	/**
	 * Removes the parameter where groupId = &#63; and parameterGroupId = &#63; and paramCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param parameterGroupId the parameter group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the parameter that was removed
	 */
	public Parameter removeByGroupCode(
			long groupId, long parameterGroupId, String paramCode,
			Boolean deleted)
		throws NoSuchParameterException;

	/**
	 * Returns the number of parameters where groupId = &#63; and parameterGroupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param parameterGroupId the parameter group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	public int countByGroupCode(
		long groupId, long parameterGroupId, String paramCode, Boolean deleted);

	/**
	 * Caches the parameter in the entity cache if it is enabled.
	 *
	 * @param parameter the parameter
	 */
	public void cacheResult(Parameter parameter);

	/**
	 * Caches the parameters in the entity cache if it is enabled.
	 *
	 * @param parameters the parameters
	 */
	public void cacheResult(java.util.List<Parameter> parameters);

	/**
	 * Creates a new parameter with the primary key. Does not add the parameter to the database.
	 *
	 * @param parameterId the primary key for the new parameter
	 * @return the new parameter
	 */
	public Parameter create(long parameterId);

	/**
	 * Removes the parameter with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param parameterId the primary key of the parameter
	 * @return the parameter that was removed
	 * @throws NoSuchParameterException if a parameter with the primary key could not be found
	 */
	public Parameter remove(long parameterId) throws NoSuchParameterException;

	public Parameter updateImpl(Parameter parameter);

	/**
	 * Returns the parameter with the primary key or throws a <code>NoSuchParameterException</code> if it could not be found.
	 *
	 * @param parameterId the primary key of the parameter
	 * @return the parameter
	 * @throws NoSuchParameterException if a parameter with the primary key could not be found
	 */
	public Parameter findByPrimaryKey(long parameterId)
		throws NoSuchParameterException;

	/**
	 * Returns the parameter with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param parameterId the primary key of the parameter
	 * @return the parameter, or <code>null</code> if a parameter with the primary key could not be found
	 */
	public Parameter fetchByPrimaryKey(long parameterId);

	/**
	 * Returns all the parameters.
	 *
	 * @return the parameters
	 */
	public java.util.List<Parameter> findAll();

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
	public java.util.List<Parameter> findAll(int start, int end);

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
	public java.util.List<Parameter> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Parameter>
			orderByComparator);

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
	public java.util.List<Parameter> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Parameter>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the parameters from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of parameters.
	 *
	 * @return the number of parameters
	 */
	public int countAll();

}