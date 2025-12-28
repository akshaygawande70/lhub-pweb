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

package svc.ntuc.nlh.otp.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for Otp. This utility wraps
 * <code>svc.ntuc.nlh.otp.service.impl.OtpLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see OtpLocalService
 * @generated
 */
public class OtpLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>svc.ntuc.nlh.otp.service.impl.OtpLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static svc.ntuc.nlh.otp.model.Otp addOtp(
			long groupId, long userId, int keyLength, int otpLength,
			String fullName, String email,
			com.liferay.portal.kernel.service.ServiceContext serviceContext,
			javax.portlet.PortletPreferences portletPreferences,
			String languageId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addOtp(
			groupId, userId, keyLength, otpLength, fullName, email,
			serviceContext, portletPreferences, languageId);
	}

	/**
	 * Adds the otp to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect OtpLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param otp the otp
	 * @return the otp that was added
	 */
	public static svc.ntuc.nlh.otp.model.Otp addOtp(
		svc.ntuc.nlh.otp.model.Otp otp) {

		return getService().addOtp(otp);
	}

	/**
	 * Creates a new otp with the primary key. Does not add the otp to the database.
	 *
	 * @param otpId the primary key for the new otp
	 * @return the new otp
	 */
	public static svc.ntuc.nlh.otp.model.Otp createOtp(long otpId) {
		return getService().createOtp(otpId);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			createPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().createPersistedModel(primaryKeyObj);
	}

	/**
	 * Deletes the otp with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect OtpLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param otpId the primary key of the otp
	 * @return the otp that was removed
	 * @throws PortalException if a otp with the primary key could not be found
	 */
	public static svc.ntuc.nlh.otp.model.Otp deleteOtp(long otpId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteOtp(otpId);
	}

	/**
	 * Deletes the otp from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect OtpLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param otp the otp
	 * @return the otp that was removed
	 */
	public static svc.ntuc.nlh.otp.model.Otp deleteOtp(
		svc.ntuc.nlh.otp.model.Otp otp) {

		return getService().deleteOtp(otp);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			deletePersistedModel(
				com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery
		dynamicQuery() {

		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.otp.model.impl.OtpModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.otp.model.impl.OtpModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static svc.ntuc.nlh.otp.model.Otp fetchOtp(long otpId) {
		return getService().fetchOtp(otpId);
	}

	/**
	 * Returns the otp matching the UUID and group.
	 *
	 * @param uuid the otp's UUID
	 * @param groupId the primary key of the group
	 * @return the matching otp, or <code>null</code> if a matching otp could not be found
	 */
	public static svc.ntuc.nlh.otp.model.Otp fetchOtpByUuidAndGroupId(
		String uuid, long groupId) {

		return getService().fetchOtpByUuidAndGroupId(uuid, groupId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return getService().getExportActionableDynamicQuery(portletDataContext);
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * Returns the otp with the primary key.
	 *
	 * @param otpId the primary key of the otp
	 * @return the otp
	 * @throws PortalException if a otp with the primary key could not be found
	 */
	public static svc.ntuc.nlh.otp.model.Otp getOtp(long otpId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getOtp(otpId);
	}

	public static svc.ntuc.nlh.otp.model.Otp getOtpByOtpIdAndUserId(
			long otpId, long userId)
		throws svc.ntuc.nlh.otp.exception.NoSuchOtpException {

		return getService().getOtpByOtpIdAndUserId(otpId, userId);
	}

	/**
	 * Returns the otp matching the UUID and group.
	 *
	 * @param uuid the otp's UUID
	 * @param groupId the primary key of the group
	 * @return the matching otp
	 * @throws PortalException if a matching otp could not be found
	 */
	public static svc.ntuc.nlh.otp.model.Otp getOtpByUuidAndGroupId(
			String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getOtpByUuidAndGroupId(uuid, groupId);
	}

	/**
	 * Returns a range of all the otps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.otp.model.impl.OtpModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @return the range of otps
	 */
	public static java.util.List<svc.ntuc.nlh.otp.model.Otp> getOtps(
		int start, int end) {

		return getService().getOtps(start, end);
	}

	/**
	 * Returns all the otps matching the UUID and company.
	 *
	 * @param uuid the UUID of the otps
	 * @param companyId the primary key of the company
	 * @return the matching otps, or an empty list if no matches were found
	 */
	public static java.util.List<svc.ntuc.nlh.otp.model.Otp>
		getOtpsByUuidAndCompanyId(String uuid, long companyId) {

		return getService().getOtpsByUuidAndCompanyId(uuid, companyId);
	}

	/**
	 * Returns a range of otps matching the UUID and company.
	 *
	 * @param uuid the UUID of the otps
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching otps, or an empty list if no matches were found
	 */
	public static java.util.List<svc.ntuc.nlh.otp.model.Otp>
		getOtpsByUuidAndCompanyId(
			String uuid, long companyId, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<svc.ntuc.nlh.otp.model.Otp> orderByComparator) {

		return getService().getOtpsByUuidAndCompanyId(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the number of otps.
	 *
	 * @return the number of otps
	 */
	public static int getOtpsCount() {
		return getService().getOtpsCount();
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Updates the otp in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect OtpLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param otp the otp
	 * @return the otp that was updated
	 */
	public static svc.ntuc.nlh.otp.model.Otp updateOtp(
		svc.ntuc.nlh.otp.model.Otp otp) {

		return getService().updateOtp(otp);
	}

	public static OtpLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<OtpLocalService, OtpLocalService>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(OtpLocalService.class);

		ServiceTracker<OtpLocalService, OtpLocalService> serviceTracker =
			new ServiceTracker<OtpLocalService, OtpLocalService>(
				bundle.getBundleContext(), OtpLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}