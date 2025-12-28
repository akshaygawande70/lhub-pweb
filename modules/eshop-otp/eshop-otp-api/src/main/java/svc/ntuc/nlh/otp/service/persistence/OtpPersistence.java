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

package svc.ntuc.nlh.otp.service.persistence;

import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

import svc.ntuc.nlh.otp.exception.NoSuchOtpException;
import svc.ntuc.nlh.otp.model.Otp;

/**
 * The persistence interface for the otp service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OtpUtil
 * @generated
 */
@ProviderType
public interface OtpPersistence extends BasePersistence<Otp> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link OtpUtil} to access the otp persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the otps where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching otps
	 */
	public java.util.List<Otp> findByUuid(String uuid);

	/**
	 * Returns a range of all the otps where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @return the range of matching otps
	 */
	public java.util.List<Otp> findByUuid(String uuid, int start, int end);

	/**
	 * Returns an ordered range of all the otps where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching otps
	 */
	public java.util.List<Otp> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Otp>
			orderByComparator);

	/**
	 * Returns an ordered range of all the otps where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching otps
	 */
	public java.util.List<Otp> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Otp> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first otp in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	public Otp findByUuid_First(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<Otp>
				orderByComparator)
		throws NoSuchOtpException;

	/**
	 * Returns the first otp in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public Otp fetchByUuid_First(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<Otp>
			orderByComparator);

	/**
	 * Returns the last otp in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	public Otp findByUuid_Last(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<Otp>
				orderByComparator)
		throws NoSuchOtpException;

	/**
	 * Returns the last otp in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public Otp fetchByUuid_Last(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<Otp>
			orderByComparator);

	/**
	 * Returns the otps before and after the current otp in the ordered set where uuid = &#63;.
	 *
	 * @param otpId the primary key of the current otp
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next otp
	 * @throws NoSuchOtpException if a otp with the primary key could not be found
	 */
	public Otp[] findByUuid_PrevAndNext(
			long otpId, String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<Otp>
				orderByComparator)
		throws NoSuchOtpException;

	/**
	 * Removes all the otps where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public void removeByUuid(String uuid);

	/**
	 * Returns the number of otps where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching otps
	 */
	public int countByUuid(String uuid);

	/**
	 * Returns the otp where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchOtpException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	public Otp findByUUID_G(String uuid, long groupId)
		throws NoSuchOtpException;

	/**
	 * Returns the otp where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public Otp fetchByUUID_G(String uuid, long groupId);

	/**
	 * Returns the otp where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public Otp fetchByUUID_G(String uuid, long groupId, boolean useFinderCache);

	/**
	 * Removes the otp where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the otp that was removed
	 */
	public Otp removeByUUID_G(String uuid, long groupId)
		throws NoSuchOtpException;

	/**
	 * Returns the number of otps where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching otps
	 */
	public int countByUUID_G(String uuid, long groupId);

	/**
	 * Returns all the otps where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching otps
	 */
	public java.util.List<Otp> findByUuid_C(String uuid, long companyId);

	/**
	 * Returns a range of all the otps where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @return the range of matching otps
	 */
	public java.util.List<Otp> findByUuid_C(
		String uuid, long companyId, int start, int end);

	/**
	 * Returns an ordered range of all the otps where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching otps
	 */
	public java.util.List<Otp> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Otp>
			orderByComparator);

	/**
	 * Returns an ordered range of all the otps where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching otps
	 */
	public java.util.List<Otp> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Otp> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first otp in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	public Otp findByUuid_C_First(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<Otp>
				orderByComparator)
		throws NoSuchOtpException;

	/**
	 * Returns the first otp in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public Otp fetchByUuid_C_First(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<Otp>
			orderByComparator);

	/**
	 * Returns the last otp in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	public Otp findByUuid_C_Last(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<Otp>
				orderByComparator)
		throws NoSuchOtpException;

	/**
	 * Returns the last otp in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public Otp fetchByUuid_C_Last(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<Otp>
			orderByComparator);

	/**
	 * Returns the otps before and after the current otp in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param otpId the primary key of the current otp
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next otp
	 * @throws NoSuchOtpException if a otp with the primary key could not be found
	 */
	public Otp[] findByUuid_C_PrevAndNext(
			long otpId, String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<Otp>
				orderByComparator)
		throws NoSuchOtpException;

	/**
	 * Removes all the otps where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public void removeByUuid_C(String uuid, long companyId);

	/**
	 * Returns the number of otps where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching otps
	 */
	public int countByUuid_C(String uuid, long companyId);

	/**
	 * Returns the otp where otpId = &#63; and userId = &#63; or throws a <code>NoSuchOtpException</code> if it could not be found.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @return the matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	public Otp findByOtpIdUserId(long otpId, long userId)
		throws NoSuchOtpException;

	/**
	 * Returns the otp where otpId = &#63; and userId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @return the matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public Otp fetchByOtpIdUserId(long otpId, long userId);

	/**
	 * Returns the otp where otpId = &#63; and userId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public Otp fetchByOtpIdUserId(
		long otpId, long userId, boolean useFinderCache);

	/**
	 * Removes the otp where otpId = &#63; and userId = &#63; from the database.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @return the otp that was removed
	 */
	public Otp removeByOtpIdUserId(long otpId, long userId)
		throws NoSuchOtpException;

	/**
	 * Returns the number of otps where otpId = &#63; and userId = &#63;.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @return the number of matching otps
	 */
	public int countByOtpIdUserId(long otpId, long userId);

	/**
	 * Caches the otp in the entity cache if it is enabled.
	 *
	 * @param otp the otp
	 */
	public void cacheResult(Otp otp);

	/**
	 * Caches the otps in the entity cache if it is enabled.
	 *
	 * @param otps the otps
	 */
	public void cacheResult(java.util.List<Otp> otps);

	/**
	 * Creates a new otp with the primary key. Does not add the otp to the database.
	 *
	 * @param otpId the primary key for the new otp
	 * @return the new otp
	 */
	public Otp create(long otpId);

	/**
	 * Removes the otp with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param otpId the primary key of the otp
	 * @return the otp that was removed
	 * @throws NoSuchOtpException if a otp with the primary key could not be found
	 */
	public Otp remove(long otpId) throws NoSuchOtpException;

	public Otp updateImpl(Otp otp);

	/**
	 * Returns the otp with the primary key or throws a <code>NoSuchOtpException</code> if it could not be found.
	 *
	 * @param otpId the primary key of the otp
	 * @return the otp
	 * @throws NoSuchOtpException if a otp with the primary key could not be found
	 */
	public Otp findByPrimaryKey(long otpId) throws NoSuchOtpException;

	/**
	 * Returns the otp with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param otpId the primary key of the otp
	 * @return the otp, or <code>null</code> if a otp with the primary key could not be found
	 */
	public Otp fetchByPrimaryKey(long otpId);

	/**
	 * Returns all the otps.
	 *
	 * @return the otps
	 */
	public java.util.List<Otp> findAll();

	/**
	 * Returns a range of all the otps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @return the range of otps
	 */
	public java.util.List<Otp> findAll(int start, int end);

	/**
	 * Returns an ordered range of all the otps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of otps
	 */
	public java.util.List<Otp> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Otp>
			orderByComparator);

	/**
	 * Returns an ordered range of all the otps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of otps
	 */
	public java.util.List<Otp> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Otp> orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the otps from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of otps.
	 *
	 * @return the number of otps
	 */
	public int countAll();

}