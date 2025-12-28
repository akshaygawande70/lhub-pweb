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

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link NtucSBLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see NtucSBLocalService
 * @generated
 */
public class NtucSBLocalServiceWrapper
	implements NtucSBLocalService, ServiceWrapper<NtucSBLocalService> {

	public NtucSBLocalServiceWrapper(NtucSBLocalService ntucSBLocalService) {
		_ntucSBLocalService = ntucSBLocalService;
	}

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
	@Override
	public com.ntuc.notification.model.NtucSB addNtucSB(
		com.ntuc.notification.model.NtucSB ntucSB) {

		return _ntucSBLocalService.addNtucSB(ntucSB);
	}

	/**
	 * Creates a new ntuc sb with the primary key. Does not add the ntuc sb to the database.
	 *
	 * @param ntucDTId the primary key for the new ntuc sb
	 * @return the new ntuc sb
	 */
	@Override
	public com.ntuc.notification.model.NtucSB createNtucSB(long ntucDTId) {
		return _ntucSBLocalService.createNtucSB(ntucDTId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _ntucSBLocalService.createPersistedModel(primaryKeyObj);
	}

	@Override
	public void deleteByDateRange(
		java.util.Date fromDate, java.util.Date toDate) {

		_ntucSBLocalService.deleteByDateRange(fromDate, toDate);
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
	@Override
	public com.ntuc.notification.model.NtucSB deleteNtucSB(long ntucDTId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _ntucSBLocalService.deleteNtucSB(ntucDTId);
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
	@Override
	public com.ntuc.notification.model.NtucSB deleteNtucSB(
		com.ntuc.notification.model.NtucSB ntucSB) {

		return _ntucSBLocalService.deleteNtucSB(ntucSB);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _ntucSBLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _ntucSBLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _ntucSBLocalService.dynamicQuery(dynamicQuery);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _ntucSBLocalService.dynamicQuery(dynamicQuery, start, end);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _ntucSBLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _ntucSBLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _ntucSBLocalService.dynamicQueryCount(dynamicQuery, projection);
	}

	@Override
	public java.util.List<com.ntuc.notification.model.NtucSB>
		fetchByIsRowLockFailed(boolean isRowLockFailed) {

		return _ntucSBLocalService.fetchByIsRowLockFailed(isRowLockFailed);
	}

	/**
	 * Fetch the latest (by systemDate DESC) record for courseCode + event,
	 * excluding the current ntucDTId. Uses Service Builder finder.
	 */
	@Override
	public java.util.Optional<com.ntuc.notification.model.NtucSB>
		fetchLatestByCourseAndEvent(
			String courseCode, String event, long excludeNtucDTId) {

		return _ntucSBLocalService.fetchLatestByCourseAndEvent(
			courseCode, event, excludeNtucDTId);
	}

	/**
	 * Fetch the latest (by systemDate DESC) record for courseCode + event + changeType,
	 * where changeFrom is a JSON-array string. We match using: changeFrom LIKE '%"type"%' .
	 * Excludes the current ntucDTId.
	 */
	@Override
	public java.util.Optional<com.ntuc.notification.model.NtucSB>
		fetchLatestByCourseEventAndChangeFrom(
			String courseCode, String event, String changeType,
			long excludeNtucDTId) {

		return _ntucSBLocalService.fetchLatestByCourseEventAndChangeFrom(
			courseCode, event, changeType, excludeNtucDTId);
	}

	@Override
	public com.ntuc.notification.model.NtucSB fetchNtucSB(long ntucDTId) {
		return _ntucSBLocalService.fetchNtucSB(ntucDTId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _ntucSBLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _ntucSBLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the ntuc sb with the primary key.
	 *
	 * @param ntucDTId the primary key of the ntuc sb
	 * @return the ntuc sb
	 * @throws PortalException if a ntuc sb with the primary key could not be found
	 */
	@Override
	public com.ntuc.notification.model.NtucSB getNtucSB(long ntucDTId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _ntucSBLocalService.getNtucSB(ntucDTId);
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
	@Override
	public java.util.List<com.ntuc.notification.model.NtucSB> getNtucSBs(
		int start, int end) {

		return _ntucSBLocalService.getNtucSBs(start, end);
	}

	/**
	 * Returns the number of ntuc sbs.
	 *
	 * @return the number of ntuc sbs
	 */
	@Override
	public int getNtucSBsCount() {
		return _ntucSBLocalService.getNtucSBsCount();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _ntucSBLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _ntucSBLocalService.getPersistedModel(primaryKeyObj);
	}

	@Override
	public java.util.List<com.ntuc.notification.model.NtucSB>
		getRecordsByChangeFrom(String changeFrom) {

		return _ntucSBLocalService.getRecordsByChangeFrom(changeFrom);
	}

	@Override
	public java.util.List<com.ntuc.notification.model.NtucSB>
		getUnprocessedCronRecords(String status) {

		return _ntucSBLocalService.getUnprocessedCronRecords(status);
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
	@Override
	public com.ntuc.notification.model.NtucSB updateNtucSB(
		com.ntuc.notification.model.NtucSB ntucSB) {

		return _ntucSBLocalService.updateNtucSB(ntucSB);
	}

	@Override
	public NtucSBLocalService getWrappedService() {
		return _ntucSBLocalService;
	}

	@Override
	public void setWrappedService(NtucSBLocalService ntucSBLocalService) {
		_ntucSBLocalService = ntucSBLocalService;
	}

	private NtucSBLocalService _ntucSBLocalService;

}