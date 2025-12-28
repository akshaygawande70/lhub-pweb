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

import svc.ntuc.nlh.otp.model.Otp;

/**
 * The persistence utility for the otp service. This utility wraps <code>svc.ntuc.nlh.otp.service.persistence.impl.OtpPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OtpPersistence
 * @generated
 */
public class OtpUtil {

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
	public static void clearCache(Otp otp) {
		getPersistence().clearCache(otp);
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
	public static Map<Serializable, Otp> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<Otp> findWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<Otp> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<Otp> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<Otp> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static Otp update(Otp otp) {
		return getPersistence().update(otp);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static Otp update(Otp otp, ServiceContext serviceContext) {
		return getPersistence().update(otp, serviceContext);
	}

	/**
	 * Returns all the otps where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching otps
	 */
	public static List<Otp> findByUuid(String uuid) {
		return getPersistence().findByUuid(uuid);
	}

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
	public static List<Otp> findByUuid(String uuid, int start, int end) {
		return getPersistence().findByUuid(uuid, start, end);
	}

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
	public static List<Otp> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<Otp> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

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
	public static List<Otp> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<Otp> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first otp in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	public static Otp findByUuid_First(
			String uuid, OrderByComparator<Otp> orderByComparator)
		throws svc.ntuc.nlh.otp.exception.NoSuchOtpException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first otp in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public static Otp fetchByUuid_First(
		String uuid, OrderByComparator<Otp> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the last otp in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	public static Otp findByUuid_Last(
			String uuid, OrderByComparator<Otp> orderByComparator)
		throws svc.ntuc.nlh.otp.exception.NoSuchOtpException {

		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the last otp in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public static Otp fetchByUuid_Last(
		String uuid, OrderByComparator<Otp> orderByComparator) {

		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the otps before and after the current otp in the ordered set where uuid = &#63;.
	 *
	 * @param otpId the primary key of the current otp
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next otp
	 * @throws NoSuchOtpException if a otp with the primary key could not be found
	 */
	public static Otp[] findByUuid_PrevAndNext(
			long otpId, String uuid, OrderByComparator<Otp> orderByComparator)
		throws svc.ntuc.nlh.otp.exception.NoSuchOtpException {

		return getPersistence().findByUuid_PrevAndNext(
			otpId, uuid, orderByComparator);
	}

	/**
	 * Removes all the otps where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of otps where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching otps
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Returns the otp where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchOtpException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	public static Otp findByUUID_G(String uuid, long groupId)
		throws svc.ntuc.nlh.otp.exception.NoSuchOtpException {

		return getPersistence().findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the otp where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public static Otp fetchByUUID_G(String uuid, long groupId) {
		return getPersistence().fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the otp where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public static Otp fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache) {

		return getPersistence().fetchByUUID_G(uuid, groupId, useFinderCache);
	}

	/**
	 * Removes the otp where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the otp that was removed
	 */
	public static Otp removeByUUID_G(String uuid, long groupId)
		throws svc.ntuc.nlh.otp.exception.NoSuchOtpException {

		return getPersistence().removeByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the number of otps where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching otps
	 */
	public static int countByUUID_G(String uuid, long groupId) {
		return getPersistence().countByUUID_G(uuid, groupId);
	}

	/**
	 * Returns all the otps where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching otps
	 */
	public static List<Otp> findByUuid_C(String uuid, long companyId) {
		return getPersistence().findByUuid_C(uuid, companyId);
	}

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
	public static List<Otp> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return getPersistence().findByUuid_C(uuid, companyId, start, end);
	}

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
	public static List<Otp> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Otp> orderByComparator) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

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
	public static List<Otp> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Otp> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first otp in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	public static Otp findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<Otp> orderByComparator)
		throws svc.ntuc.nlh.otp.exception.NoSuchOtpException {

		return getPersistence().findByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the first otp in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public static Otp fetchByUuid_C_First(
		String uuid, long companyId, OrderByComparator<Otp> orderByComparator) {

		return getPersistence().fetchByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last otp in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	public static Otp findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<Otp> orderByComparator)
		throws svc.ntuc.nlh.otp.exception.NoSuchOtpException {

		return getPersistence().findByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last otp in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public static Otp fetchByUuid_C_Last(
		String uuid, long companyId, OrderByComparator<Otp> orderByComparator) {

		return getPersistence().fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

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
	public static Otp[] findByUuid_C_PrevAndNext(
			long otpId, String uuid, long companyId,
			OrderByComparator<Otp> orderByComparator)
		throws svc.ntuc.nlh.otp.exception.NoSuchOtpException {

		return getPersistence().findByUuid_C_PrevAndNext(
			otpId, uuid, companyId, orderByComparator);
	}

	/**
	 * Removes all the otps where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public static void removeByUuid_C(String uuid, long companyId) {
		getPersistence().removeByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the number of otps where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching otps
	 */
	public static int countByUuid_C(String uuid, long companyId) {
		return getPersistence().countByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the otp where otpId = &#63; and userId = &#63; or throws a <code>NoSuchOtpException</code> if it could not be found.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @return the matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	public static Otp findByOtpIdUserId(long otpId, long userId)
		throws svc.ntuc.nlh.otp.exception.NoSuchOtpException {

		return getPersistence().findByOtpIdUserId(otpId, userId);
	}

	/**
	 * Returns the otp where otpId = &#63; and userId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @return the matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public static Otp fetchByOtpIdUserId(long otpId, long userId) {
		return getPersistence().fetchByOtpIdUserId(otpId, userId);
	}

	/**
	 * Returns the otp where otpId = &#63; and userId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public static Otp fetchByOtpIdUserId(
		long otpId, long userId, boolean useFinderCache) {

		return getPersistence().fetchByOtpIdUserId(
			otpId, userId, useFinderCache);
	}

	/**
	 * Removes the otp where otpId = &#63; and userId = &#63; from the database.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @return the otp that was removed
	 */
	public static Otp removeByOtpIdUserId(long otpId, long userId)
		throws svc.ntuc.nlh.otp.exception.NoSuchOtpException {

		return getPersistence().removeByOtpIdUserId(otpId, userId);
	}

	/**
	 * Returns the number of otps where otpId = &#63; and userId = &#63;.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @return the number of matching otps
	 */
	public static int countByOtpIdUserId(long otpId, long userId) {
		return getPersistence().countByOtpIdUserId(otpId, userId);
	}

	/**
	 * Caches the otp in the entity cache if it is enabled.
	 *
	 * @param otp the otp
	 */
	public static void cacheResult(Otp otp) {
		getPersistence().cacheResult(otp);
	}

	/**
	 * Caches the otps in the entity cache if it is enabled.
	 *
	 * @param otps the otps
	 */
	public static void cacheResult(List<Otp> otps) {
		getPersistence().cacheResult(otps);
	}

	/**
	 * Creates a new otp with the primary key. Does not add the otp to the database.
	 *
	 * @param otpId the primary key for the new otp
	 * @return the new otp
	 */
	public static Otp create(long otpId) {
		return getPersistence().create(otpId);
	}

	/**
	 * Removes the otp with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param otpId the primary key of the otp
	 * @return the otp that was removed
	 * @throws NoSuchOtpException if a otp with the primary key could not be found
	 */
	public static Otp remove(long otpId)
		throws svc.ntuc.nlh.otp.exception.NoSuchOtpException {

		return getPersistence().remove(otpId);
	}

	public static Otp updateImpl(Otp otp) {
		return getPersistence().updateImpl(otp);
	}

	/**
	 * Returns the otp with the primary key or throws a <code>NoSuchOtpException</code> if it could not be found.
	 *
	 * @param otpId the primary key of the otp
	 * @return the otp
	 * @throws NoSuchOtpException if a otp with the primary key could not be found
	 */
	public static Otp findByPrimaryKey(long otpId)
		throws svc.ntuc.nlh.otp.exception.NoSuchOtpException {

		return getPersistence().findByPrimaryKey(otpId);
	}

	/**
	 * Returns the otp with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param otpId the primary key of the otp
	 * @return the otp, or <code>null</code> if a otp with the primary key could not be found
	 */
	public static Otp fetchByPrimaryKey(long otpId) {
		return getPersistence().fetchByPrimaryKey(otpId);
	}

	/**
	 * Returns all the otps.
	 *
	 * @return the otps
	 */
	public static List<Otp> findAll() {
		return getPersistence().findAll();
	}

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
	public static List<Otp> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

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
	public static List<Otp> findAll(
		int start, int end, OrderByComparator<Otp> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

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
	public static List<Otp> findAll(
		int start, int end, OrderByComparator<Otp> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the otps from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of otps.
	 *
	 * @return the number of otps
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static OtpPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<OtpPersistence, OtpPersistence>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(OtpPersistence.class);

		ServiceTracker<OtpPersistence, OtpPersistence> serviceTracker =
			new ServiceTracker<OtpPersistence, OtpPersistence>(
				bundle.getBundleContext(), OtpPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}