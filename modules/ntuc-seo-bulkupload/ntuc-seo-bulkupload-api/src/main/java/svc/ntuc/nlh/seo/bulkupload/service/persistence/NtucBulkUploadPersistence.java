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

package svc.ntuc.nlh.seo.bulkupload.service.persistence;

import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

import svc.ntuc.nlh.seo.bulkupload.exception.NoSuchNtucBulkUploadException;
import svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload;

/**
 * The persistence interface for the ntuc bulk upload service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see NtucBulkUploadUtil
 * @generated
 */
@ProviderType
public interface NtucBulkUploadPersistence
	extends BasePersistence<NtucBulkUpload> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link NtucBulkUploadUtil} to access the ntuc bulk upload persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the ntuc bulk uploads where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching ntuc bulk uploads
	 */
	public java.util.List<NtucBulkUpload> findByUuid(String uuid);

	/**
	 * Returns a range of all the ntuc bulk uploads where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucBulkUploadModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of ntuc bulk uploads
	 * @param end the upper bound of the range of ntuc bulk uploads (not inclusive)
	 * @return the range of matching ntuc bulk uploads
	 */
	public java.util.List<NtucBulkUpload> findByUuid(
		String uuid, int start, int end);

	/**
	 * Returns an ordered range of all the ntuc bulk uploads where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucBulkUploadModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of ntuc bulk uploads
	 * @param end the upper bound of the range of ntuc bulk uploads (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc bulk uploads
	 */
	public java.util.List<NtucBulkUpload> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
			orderByComparator);

	/**
	 * Returns an ordered range of all the ntuc bulk uploads where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucBulkUploadModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of ntuc bulk uploads
	 * @param end the upper bound of the range of ntuc bulk uploads (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc bulk uploads
	 */
	public java.util.List<NtucBulkUpload> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first ntuc bulk upload in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc bulk upload
	 * @throws NoSuchNtucBulkUploadException if a matching ntuc bulk upload could not be found
	 */
	public NtucBulkUpload findByUuid_First(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
				orderByComparator)
		throws NoSuchNtucBulkUploadException;

	/**
	 * Returns the first ntuc bulk upload in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc bulk upload, or <code>null</code> if a matching ntuc bulk upload could not be found
	 */
	public NtucBulkUpload fetchByUuid_First(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
			orderByComparator);

	/**
	 * Returns the last ntuc bulk upload in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc bulk upload
	 * @throws NoSuchNtucBulkUploadException if a matching ntuc bulk upload could not be found
	 */
	public NtucBulkUpload findByUuid_Last(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
				orderByComparator)
		throws NoSuchNtucBulkUploadException;

	/**
	 * Returns the last ntuc bulk upload in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc bulk upload, or <code>null</code> if a matching ntuc bulk upload could not be found
	 */
	public NtucBulkUpload fetchByUuid_Last(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
			orderByComparator);

	/**
	 * Returns the ntuc bulk uploads before and after the current ntuc bulk upload in the ordered set where uuid = &#63;.
	 *
	 * @param ntucBulkUploadId the primary key of the current ntuc bulk upload
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc bulk upload
	 * @throws NoSuchNtucBulkUploadException if a ntuc bulk upload with the primary key could not be found
	 */
	public NtucBulkUpload[] findByUuid_PrevAndNext(
			long ntucBulkUploadId, String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
				orderByComparator)
		throws NoSuchNtucBulkUploadException;

	/**
	 * Removes all the ntuc bulk uploads where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public void removeByUuid(String uuid);

	/**
	 * Returns the number of ntuc bulk uploads where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching ntuc bulk uploads
	 */
	public int countByUuid(String uuid);

	/**
	 * Returns the ntuc bulk upload where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchNtucBulkUploadException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching ntuc bulk upload
	 * @throws NoSuchNtucBulkUploadException if a matching ntuc bulk upload could not be found
	 */
	public NtucBulkUpload findByUUID_G(String uuid, long groupId)
		throws NoSuchNtucBulkUploadException;

	/**
	 * Returns the ntuc bulk upload where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching ntuc bulk upload, or <code>null</code> if a matching ntuc bulk upload could not be found
	 */
	public NtucBulkUpload fetchByUUID_G(String uuid, long groupId);

	/**
	 * Returns the ntuc bulk upload where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching ntuc bulk upload, or <code>null</code> if a matching ntuc bulk upload could not be found
	 */
	public NtucBulkUpload fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache);

	/**
	 * Removes the ntuc bulk upload where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the ntuc bulk upload that was removed
	 */
	public NtucBulkUpload removeByUUID_G(String uuid, long groupId)
		throws NoSuchNtucBulkUploadException;

	/**
	 * Returns the number of ntuc bulk uploads where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching ntuc bulk uploads
	 */
	public int countByUUID_G(String uuid, long groupId);

	/**
	 * Returns all the ntuc bulk uploads where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching ntuc bulk uploads
	 */
	public java.util.List<NtucBulkUpload> findByUuid_C(
		String uuid, long companyId);

	/**
	 * Returns a range of all the ntuc bulk uploads where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucBulkUploadModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of ntuc bulk uploads
	 * @param end the upper bound of the range of ntuc bulk uploads (not inclusive)
	 * @return the range of matching ntuc bulk uploads
	 */
	public java.util.List<NtucBulkUpload> findByUuid_C(
		String uuid, long companyId, int start, int end);

	/**
	 * Returns an ordered range of all the ntuc bulk uploads where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucBulkUploadModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of ntuc bulk uploads
	 * @param end the upper bound of the range of ntuc bulk uploads (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc bulk uploads
	 */
	public java.util.List<NtucBulkUpload> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
			orderByComparator);

	/**
	 * Returns an ordered range of all the ntuc bulk uploads where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucBulkUploadModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of ntuc bulk uploads
	 * @param end the upper bound of the range of ntuc bulk uploads (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc bulk uploads
	 */
	public java.util.List<NtucBulkUpload> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first ntuc bulk upload in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc bulk upload
	 * @throws NoSuchNtucBulkUploadException if a matching ntuc bulk upload could not be found
	 */
	public NtucBulkUpload findByUuid_C_First(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
				orderByComparator)
		throws NoSuchNtucBulkUploadException;

	/**
	 * Returns the first ntuc bulk upload in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc bulk upload, or <code>null</code> if a matching ntuc bulk upload could not be found
	 */
	public NtucBulkUpload fetchByUuid_C_First(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
			orderByComparator);

	/**
	 * Returns the last ntuc bulk upload in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc bulk upload
	 * @throws NoSuchNtucBulkUploadException if a matching ntuc bulk upload could not be found
	 */
	public NtucBulkUpload findByUuid_C_Last(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
				orderByComparator)
		throws NoSuchNtucBulkUploadException;

	/**
	 * Returns the last ntuc bulk upload in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc bulk upload, or <code>null</code> if a matching ntuc bulk upload could not be found
	 */
	public NtucBulkUpload fetchByUuid_C_Last(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
			orderByComparator);

	/**
	 * Returns the ntuc bulk uploads before and after the current ntuc bulk upload in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param ntucBulkUploadId the primary key of the current ntuc bulk upload
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc bulk upload
	 * @throws NoSuchNtucBulkUploadException if a ntuc bulk upload with the primary key could not be found
	 */
	public NtucBulkUpload[] findByUuid_C_PrevAndNext(
			long ntucBulkUploadId, String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
				orderByComparator)
		throws NoSuchNtucBulkUploadException;

	/**
	 * Removes all the ntuc bulk uploads where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public void removeByUuid_C(String uuid, long companyId);

	/**
	 * Returns the number of ntuc bulk uploads where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching ntuc bulk uploads
	 */
	public int countByUuid_C(String uuid, long companyId);

	/**
	 * Returns the ntuc bulk upload where ntucBulkUploadId = &#63; or throws a <code>NoSuchNtucBulkUploadException</code> if it could not be found.
	 *
	 * @param ntucBulkUploadId the ntuc bulk upload ID
	 * @return the matching ntuc bulk upload
	 * @throws NoSuchNtucBulkUploadException if a matching ntuc bulk upload could not be found
	 */
	public NtucBulkUpload findByntucBulkUploadId(long ntucBulkUploadId)
		throws NoSuchNtucBulkUploadException;

	/**
	 * Returns the ntuc bulk upload where ntucBulkUploadId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param ntucBulkUploadId the ntuc bulk upload ID
	 * @return the matching ntuc bulk upload, or <code>null</code> if a matching ntuc bulk upload could not be found
	 */
	public NtucBulkUpload fetchByntucBulkUploadId(long ntucBulkUploadId);

	/**
	 * Returns the ntuc bulk upload where ntucBulkUploadId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param ntucBulkUploadId the ntuc bulk upload ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching ntuc bulk upload, or <code>null</code> if a matching ntuc bulk upload could not be found
	 */
	public NtucBulkUpload fetchByntucBulkUploadId(
		long ntucBulkUploadId, boolean useFinderCache);

	/**
	 * Removes the ntuc bulk upload where ntucBulkUploadId = &#63; from the database.
	 *
	 * @param ntucBulkUploadId the ntuc bulk upload ID
	 * @return the ntuc bulk upload that was removed
	 */
	public NtucBulkUpload removeByntucBulkUploadId(long ntucBulkUploadId)
		throws NoSuchNtucBulkUploadException;

	/**
	 * Returns the number of ntuc bulk uploads where ntucBulkUploadId = &#63;.
	 *
	 * @param ntucBulkUploadId the ntuc bulk upload ID
	 * @return the number of matching ntuc bulk uploads
	 */
	public int countByntucBulkUploadId(long ntucBulkUploadId);

	/**
	 * Caches the ntuc bulk upload in the entity cache if it is enabled.
	 *
	 * @param ntucBulkUpload the ntuc bulk upload
	 */
	public void cacheResult(NtucBulkUpload ntucBulkUpload);

	/**
	 * Caches the ntuc bulk uploads in the entity cache if it is enabled.
	 *
	 * @param ntucBulkUploads the ntuc bulk uploads
	 */
	public void cacheResult(java.util.List<NtucBulkUpload> ntucBulkUploads);

	/**
	 * Creates a new ntuc bulk upload with the primary key. Does not add the ntuc bulk upload to the database.
	 *
	 * @param ntucBulkUploadId the primary key for the new ntuc bulk upload
	 * @return the new ntuc bulk upload
	 */
	public NtucBulkUpload create(long ntucBulkUploadId);

	/**
	 * Removes the ntuc bulk upload with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param ntucBulkUploadId the primary key of the ntuc bulk upload
	 * @return the ntuc bulk upload that was removed
	 * @throws NoSuchNtucBulkUploadException if a ntuc bulk upload with the primary key could not be found
	 */
	public NtucBulkUpload remove(long ntucBulkUploadId)
		throws NoSuchNtucBulkUploadException;

	public NtucBulkUpload updateImpl(NtucBulkUpload ntucBulkUpload);

	/**
	 * Returns the ntuc bulk upload with the primary key or throws a <code>NoSuchNtucBulkUploadException</code> if it could not be found.
	 *
	 * @param ntucBulkUploadId the primary key of the ntuc bulk upload
	 * @return the ntuc bulk upload
	 * @throws NoSuchNtucBulkUploadException if a ntuc bulk upload with the primary key could not be found
	 */
	public NtucBulkUpload findByPrimaryKey(long ntucBulkUploadId)
		throws NoSuchNtucBulkUploadException;

	/**
	 * Returns the ntuc bulk upload with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param ntucBulkUploadId the primary key of the ntuc bulk upload
	 * @return the ntuc bulk upload, or <code>null</code> if a ntuc bulk upload with the primary key could not be found
	 */
	public NtucBulkUpload fetchByPrimaryKey(long ntucBulkUploadId);

	/**
	 * Returns all the ntuc bulk uploads.
	 *
	 * @return the ntuc bulk uploads
	 */
	public java.util.List<NtucBulkUpload> findAll();

	/**
	 * Returns a range of all the ntuc bulk uploads.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucBulkUploadModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ntuc bulk uploads
	 * @param end the upper bound of the range of ntuc bulk uploads (not inclusive)
	 * @return the range of ntuc bulk uploads
	 */
	public java.util.List<NtucBulkUpload> findAll(int start, int end);

	/**
	 * Returns an ordered range of all the ntuc bulk uploads.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucBulkUploadModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ntuc bulk uploads
	 * @param end the upper bound of the range of ntuc bulk uploads (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of ntuc bulk uploads
	 */
	public java.util.List<NtucBulkUpload> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
			orderByComparator);

	/**
	 * Returns an ordered range of all the ntuc bulk uploads.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucBulkUploadModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ntuc bulk uploads
	 * @param end the upper bound of the range of ntuc bulk uploads (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of ntuc bulk uploads
	 */
	public java.util.List<NtucBulkUpload> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<NtucBulkUpload>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the ntuc bulk uploads from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of ntuc bulk uploads.
	 *
	 * @return the number of ntuc bulk uploads
	 */
	public int countAll();

}