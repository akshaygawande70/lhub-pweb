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

import svc.ntuc.nlh.parameter.exception.NoSuchReservedParameterException;
import svc.ntuc.nlh.parameter.model.ReservedParameter;

/**
 * The persistence interface for the reserved parameter service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ReservedParameterUtil
 * @generated
 */
@ProviderType
public interface ReservedParameterPersistence
	extends BasePersistence<ReservedParameter> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link ReservedParameterUtil} to access the reserved parameter persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the reserved parameters where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @return the matching reserved parameters
	 */
	public java.util.List<ReservedParameter> findByGroup(long parameterGroupId);

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
	public java.util.List<ReservedParameter> findByGroup(
		long parameterGroupId, int start, int end);

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
	public java.util.List<ReservedParameter> findByGroup(
		long parameterGroupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
			orderByComparator);

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
	public java.util.List<ReservedParameter> findByGroup(
		long parameterGroupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	public ReservedParameter findByGroup_First(
			long parameterGroupId,
			com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
				orderByComparator)
		throws NoSuchReservedParameterException;

	/**
	 * Returns the first reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	public ReservedParameter fetchByGroup_First(
		long parameterGroupId,
		com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
			orderByComparator);

	/**
	 * Returns the last reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	public ReservedParameter findByGroup_Last(
			long parameterGroupId,
			com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
				orderByComparator)
		throws NoSuchReservedParameterException;

	/**
	 * Returns the last reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	public ReservedParameter fetchByGroup_Last(
		long parameterGroupId,
		com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
			orderByComparator);

	/**
	 * Returns the reserved parameters before and after the current reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param reservedParameterId the primary key of the current reserved parameter
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next reserved parameter
	 * @throws NoSuchReservedParameterException if a reserved parameter with the primary key could not be found
	 */
	public ReservedParameter[] findByGroup_PrevAndNext(
			long reservedParameterId, long parameterGroupId,
			com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
				orderByComparator)
		throws NoSuchReservedParameterException;

	/**
	 * Removes all the reserved parameters where parameterGroupId = &#63; from the database.
	 *
	 * @param parameterGroupId the parameter group ID
	 */
	public void removeByGroup(long parameterGroupId);

	/**
	 * Returns the number of reserved parameters where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @return the number of matching reserved parameters
	 */
	public int countByGroup(long parameterGroupId);

	/**
	 * Returns the reserved parameter where paramCode = &#63; or throws a <code>NoSuchReservedParameterException</code> if it could not be found.
	 *
	 * @param paramCode the param code
	 * @return the matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	public ReservedParameter findByCode(String paramCode)
		throws NoSuchReservedParameterException;

	/**
	 * Returns the reserved parameter where paramCode = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param paramCode the param code
	 * @return the matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	public ReservedParameter fetchByCode(String paramCode);

	/**
	 * Returns the reserved parameter where paramCode = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param paramCode the param code
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	public ReservedParameter fetchByCode(
		String paramCode, boolean useFinderCache);

	/**
	 * Removes the reserved parameter where paramCode = &#63; from the database.
	 *
	 * @param paramCode the param code
	 * @return the reserved parameter that was removed
	 */
	public ReservedParameter removeByCode(String paramCode)
		throws NoSuchReservedParameterException;

	/**
	 * Returns the number of reserved parameters where paramCode = &#63;.
	 *
	 * @param paramCode the param code
	 * @return the number of matching reserved parameters
	 */
	public int countByCode(String paramCode);

	/**
	 * Returns all the reserved parameters where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @return the matching reserved parameters
	 */
	public java.util.List<ReservedParameter> findByReservedBy(
		String reservedBy);

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
	public java.util.List<ReservedParameter> findByReservedBy(
		String reservedBy, int start, int end);

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
	public java.util.List<ReservedParameter> findByReservedBy(
		String reservedBy, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
			orderByComparator);

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
	public java.util.List<ReservedParameter> findByReservedBy(
		String reservedBy, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	public ReservedParameter findByReservedBy_First(
			String reservedBy,
			com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
				orderByComparator)
		throws NoSuchReservedParameterException;

	/**
	 * Returns the first reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	public ReservedParameter fetchByReservedBy_First(
		String reservedBy,
		com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
			orderByComparator);

	/**
	 * Returns the last reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	public ReservedParameter findByReservedBy_Last(
			String reservedBy,
			com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
				orderByComparator)
		throws NoSuchReservedParameterException;

	/**
	 * Returns the last reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	public ReservedParameter fetchByReservedBy_Last(
		String reservedBy,
		com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
			orderByComparator);

	/**
	 * Returns the reserved parameters before and after the current reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedParameterId the primary key of the current reserved parameter
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next reserved parameter
	 * @throws NoSuchReservedParameterException if a reserved parameter with the primary key could not be found
	 */
	public ReservedParameter[] findByReservedBy_PrevAndNext(
			long reservedParameterId, String reservedBy,
			com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
				orderByComparator)
		throws NoSuchReservedParameterException;

	/**
	 * Removes all the reserved parameters where reservedBy = &#63; from the database.
	 *
	 * @param reservedBy the reserved by
	 */
	public void removeByReservedBy(String reservedBy);

	/**
	 * Returns the number of reserved parameters where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @return the number of matching reserved parameters
	 */
	public int countByReservedBy(String reservedBy);

	/**
	 * Caches the reserved parameter in the entity cache if it is enabled.
	 *
	 * @param reservedParameter the reserved parameter
	 */
	public void cacheResult(ReservedParameter reservedParameter);

	/**
	 * Caches the reserved parameters in the entity cache if it is enabled.
	 *
	 * @param reservedParameters the reserved parameters
	 */
	public void cacheResult(
		java.util.List<ReservedParameter> reservedParameters);

	/**
	 * Creates a new reserved parameter with the primary key. Does not add the reserved parameter to the database.
	 *
	 * @param reservedParameterId the primary key for the new reserved parameter
	 * @return the new reserved parameter
	 */
	public ReservedParameter create(long reservedParameterId);

	/**
	 * Removes the reserved parameter with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param reservedParameterId the primary key of the reserved parameter
	 * @return the reserved parameter that was removed
	 * @throws NoSuchReservedParameterException if a reserved parameter with the primary key could not be found
	 */
	public ReservedParameter remove(long reservedParameterId)
		throws NoSuchReservedParameterException;

	public ReservedParameter updateImpl(ReservedParameter reservedParameter);

	/**
	 * Returns the reserved parameter with the primary key or throws a <code>NoSuchReservedParameterException</code> if it could not be found.
	 *
	 * @param reservedParameterId the primary key of the reserved parameter
	 * @return the reserved parameter
	 * @throws NoSuchReservedParameterException if a reserved parameter with the primary key could not be found
	 */
	public ReservedParameter findByPrimaryKey(long reservedParameterId)
		throws NoSuchReservedParameterException;

	/**
	 * Returns the reserved parameter with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param reservedParameterId the primary key of the reserved parameter
	 * @return the reserved parameter, or <code>null</code> if a reserved parameter with the primary key could not be found
	 */
	public ReservedParameter fetchByPrimaryKey(long reservedParameterId);

	/**
	 * Returns all the reserved parameters.
	 *
	 * @return the reserved parameters
	 */
	public java.util.List<ReservedParameter> findAll();

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
	public java.util.List<ReservedParameter> findAll(int start, int end);

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
	public java.util.List<ReservedParameter> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
			orderByComparator);

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
	public java.util.List<ReservedParameter> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<ReservedParameter>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the reserved parameters from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of reserved parameters.
	 *
	 * @return the number of reserved parameters
	 */
	public int countAll();

}