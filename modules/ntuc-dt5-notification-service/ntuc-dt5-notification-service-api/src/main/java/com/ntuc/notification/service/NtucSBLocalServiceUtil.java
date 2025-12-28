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

package com.ntuc.notification.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for NtucSB. This utility wraps
 * <code>com.ntuc.notification.service.impl.NtucSBLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see NtucSBLocalService
 * @generated
 */
public class NtucSBLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.ntuc.notification.service.impl.NtucSBLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the ntuc sb to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect NtucSBLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param ntucSB the ntuc sb
	 * @return the ntuc sb that was added
	 */
	public static com.ntuc.notification.model.NtucSB addNtucSB(
		com.ntuc.notification.model.NtucSB ntucSB) {

		return getService().addNtucSB(ntucSB);
	}

	/**
	 * Creates a new ntuc sb with the primary key. Does not add the ntuc sb to the database.
	 *
	 * @param ntucDTId the primary key for the new ntuc sb
	 * @return the new ntuc sb
	 */
	public static com.ntuc.notification.model.NtucSB createNtucSB(
		long ntucDTId) {

		return getService().createNtucSB(ntucDTId);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			createPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().createPersistedModel(primaryKeyObj);
	}

	public static void deleteByDateRange(
		java.util.Date fromDate, java.util.Date toDate) {

		getService().deleteByDateRange(fromDate, toDate);
	}

	/**
	 * Deletes the ntuc sb with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect NtucSBLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param ntucDTId the primary key of the ntuc sb
	 * @return the ntuc sb that was removed
	 * @throws PortalException if a ntuc sb with the primary key could not be found
	 */
	public static com.ntuc.notification.model.NtucSB deleteNtucSB(long ntucDTId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteNtucSB(ntucDTId);
	}

	/**
	 * Deletes the ntuc sb from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect NtucSBLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param ntucSB the ntuc sb
	 * @return the ntuc sb that was removed
	 */
	public static com.ntuc.notification.model.NtucSB deleteNtucSB(
		com.ntuc.notification.model.NtucSB ntucSB) {

		return getService().deleteNtucSB(ntucSB);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.ntuc.notification.model.impl.NtucSBModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.ntuc.notification.model.impl.NtucSBModelImpl</code>.
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

	public static java.util.List<com.ntuc.notification.model.NtucSB>
		fetchByIsRowLockFailed(boolean isRowLockFailed) {

		return getService().fetchByIsRowLockFailed(isRowLockFailed);
	}

	/**
	 * Fetch the latest (by systemDate DESC) record for courseCode + event,
	 * excluding the current ntucDTId. Uses Service Builder finder.
	 */
	public static java.util.Optional<com.ntuc.notification.model.NtucSB>
		fetchLatestByCourseAndEvent(
			String courseCode, String event, long excludeNtucDTId) {

		return getService().fetchLatestByCourseAndEvent(
			courseCode, event, excludeNtucDTId);
	}

	/**
	 * Fetch the latest (by systemDate DESC) record for courseCode + event + changeType,
	 * where changeFrom is a JSON-array string. We match using: changeFrom LIKE '%"type"%' .
	 * Excludes the current ntucDTId.
	 */
	public static java.util.Optional<com.ntuc.notification.model.NtucSB>
		fetchLatestByCourseEventAndChangeFrom(
			String courseCode, String event, String changeType,
			long excludeNtucDTId) {

		return getService().fetchLatestByCourseEventAndChangeFrom(
			courseCode, event, changeType, excludeNtucDTId);
	}

	public static com.ntuc.notification.model.NtucSB fetchNtucSB(
		long ntucDTId) {

		return getService().fetchNtucSB(ntucDTId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the ntuc sb with the primary key.
	 *
	 * @param ntucDTId the primary key of the ntuc sb
	 * @return the ntuc sb
	 * @throws PortalException if a ntuc sb with the primary key could not be found
	 */
	public static com.ntuc.notification.model.NtucSB getNtucSB(long ntucDTId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getNtucSB(ntucDTId);
	}

	/**
	 * Returns a range of all the ntuc sbs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.ntuc.notification.model.impl.NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of ntuc sbs
	 */
	public static java.util.List<com.ntuc.notification.model.NtucSB> getNtucSBs(
		int start, int end) {

		return getService().getNtucSBs(start, end);
	}

	/**
	 * Returns the number of ntuc sbs.
	 *
	 * @return the number of ntuc sbs
	 */
	public static int getNtucSBsCount() {
		return getService().getNtucSBsCount();
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
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	public static java.util.List<com.ntuc.notification.model.NtucSB>
		getRecordsByChangeFrom(String changeFrom) {

		return getService().getRecordsByChangeFrom(changeFrom);
	}

	public static java.util.List<com.ntuc.notification.model.NtucSB>
		getUnprocessedCronRecords(String status) {

		return getService().getUnprocessedCronRecords(status);
	}

	/**
	 * Updates the ntuc sb in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect NtucSBLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param ntucSB the ntuc sb
	 * @return the ntuc sb that was updated
	 */
	public static com.ntuc.notification.model.NtucSB updateNtucSB(
		com.ntuc.notification.model.NtucSB ntucSB) {

		return getService().updateNtucSB(ntucSB);
	}

	public static NtucSBLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<NtucSBLocalService, NtucSBLocalService>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(NtucSBLocalService.class);

		ServiceTracker<NtucSBLocalService, NtucSBLocalService> serviceTracker =
			new ServiceTracker<NtucSBLocalService, NtucSBLocalService>(
				bundle.getBundleContext(), NtucSBLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}