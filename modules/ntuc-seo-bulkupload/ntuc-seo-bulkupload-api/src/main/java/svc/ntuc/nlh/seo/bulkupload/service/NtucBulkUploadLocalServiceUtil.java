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

package svc.ntuc.nlh.seo.bulkupload.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for NtucBulkUpload. This utility wraps
 * <code>svc.ntuc.nlh.seo.bulkupload.service.impl.NtucBulkUploadLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see NtucBulkUploadLocalService
 * @generated
 */
public class NtucBulkUploadLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>svc.ntuc.nlh.seo.bulkupload.service.impl.NtucBulkUploadLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
			addNtucBulkUpload(
				long groupId, long companyId, long userId, String userName,
				String fileName, String rowData)
		throws javax.portlet.PortletException {

		return getService().addNtucBulkUpload(
			groupId, companyId, userId, userName, fileName, rowData);
	}

	/**
	 * Adds the ntuc bulk upload to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect NtucBulkUploadLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param ntucBulkUpload the ntuc bulk upload
	 * @return the ntuc bulk upload that was added
	 */
	public static svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
		addNtucBulkUpload(
			svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload ntucBulkUpload) {

		return getService().addNtucBulkUpload(ntucBulkUpload);
	}

	/**
	 * Creates a new ntuc bulk upload with the primary key. Does not add the ntuc bulk upload to the database.
	 *
	 * @param ntucBulkUploadId the primary key for the new ntuc bulk upload
	 * @return the new ntuc bulk upload
	 */
	public static svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
		createNtucBulkUpload(long ntucBulkUploadId) {

		return getService().createNtucBulkUpload(ntucBulkUploadId);
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
	 * Deletes the ntuc bulk upload with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect NtucBulkUploadLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param ntucBulkUploadId the primary key of the ntuc bulk upload
	 * @return the ntuc bulk upload that was removed
	 * @throws PortalException if a ntuc bulk upload with the primary key could not be found
	 */
	public static svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
			deleteNtucBulkUpload(long ntucBulkUploadId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteNtucBulkUpload(ntucBulkUploadId);
	}

	/**
	 * Deletes the ntuc bulk upload from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect NtucBulkUploadLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param ntucBulkUpload the ntuc bulk upload
	 * @return the ntuc bulk upload that was removed
	 */
	public static svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
		deleteNtucBulkUpload(
			svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload ntucBulkUpload) {

		return getService().deleteNtucBulkUpload(ntucBulkUpload);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.seo.bulkupload.model.impl.NtucBulkUploadModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.seo.bulkupload.model.impl.NtucBulkUploadModelImpl</code>.
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

	public static svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
		fetchNtucBulkUpload(long ntucBulkUploadId) {

		return getService().fetchNtucBulkUpload(ntucBulkUploadId);
	}

	/**
	 * Returns the ntuc bulk upload matching the UUID and group.
	 *
	 * @param uuid the ntuc bulk upload's UUID
	 * @param groupId the primary key of the group
	 * @return the matching ntuc bulk upload, or <code>null</code> if a matching ntuc bulk upload could not be found
	 */
	public static svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
		fetchNtucBulkUploadByUuidAndGroupId(String uuid, long groupId) {

		return getService().fetchNtucBulkUploadByUuidAndGroupId(uuid, groupId);
	}

	public static svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
		findByNtucBulkUploadId(long ntucBulkUploadId) {

		return getService().findByNtucBulkUploadId(ntucBulkUploadId);
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
	 * Returns the ntuc bulk upload with the primary key.
	 *
	 * @param ntucBulkUploadId the primary key of the ntuc bulk upload
	 * @return the ntuc bulk upload
	 * @throws PortalException if a ntuc bulk upload with the primary key could not be found
	 */
	public static svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
			getNtucBulkUpload(long ntucBulkUploadId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getNtucBulkUpload(ntucBulkUploadId);
	}

	public static java.util.List
		<svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload>
			getNtucBulkUploadByKeywords(
				long groupId, long companyId, String keywords, int start,
				int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload>
						orderByComparator) {

		return getService().getNtucBulkUploadByKeywords(
			groupId, companyId, keywords, start, end, orderByComparator);
	}

	/**
	 * Returns the ntuc bulk upload matching the UUID and group.
	 *
	 * @param uuid the ntuc bulk upload's UUID
	 * @param groupId the primary key of the group
	 * @return the matching ntuc bulk upload
	 * @throws PortalException if a matching ntuc bulk upload could not be found
	 */
	public static svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
			getNtucBulkUploadByUuidAndGroupId(String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getNtucBulkUploadByUuidAndGroupId(uuid, groupId);
	}

	public static long getNtucBulkUploadCountsByKeywords(
		long groupId, long companyId, String keywords) {

		return getService().getNtucBulkUploadCountsByKeywords(
			groupId, companyId, keywords);
	}

	/**
	 * Returns a range of all the ntuc bulk uploads.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.seo.bulkupload.model.impl.NtucBulkUploadModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ntuc bulk uploads
	 * @param end the upper bound of the range of ntuc bulk uploads (not inclusive)
	 * @return the range of ntuc bulk uploads
	 */
	public static java.util.List
		<svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload> getNtucBulkUploads(
			int start, int end) {

		return getService().getNtucBulkUploads(start, end);
	}

	/**
	 * Returns all the ntuc bulk uploads matching the UUID and company.
	 *
	 * @param uuid the UUID of the ntuc bulk uploads
	 * @param companyId the primary key of the company
	 * @return the matching ntuc bulk uploads, or an empty list if no matches were found
	 */
	public static java.util.List
		<svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload>
			getNtucBulkUploadsByUuidAndCompanyId(String uuid, long companyId) {

		return getService().getNtucBulkUploadsByUuidAndCompanyId(
			uuid, companyId);
	}

	/**
	 * Returns a range of ntuc bulk uploads matching the UUID and company.
	 *
	 * @param uuid the UUID of the ntuc bulk uploads
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of ntuc bulk uploads
	 * @param end the upper bound of the range of ntuc bulk uploads (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching ntuc bulk uploads, or an empty list if no matches were found
	 */
	public static java.util.List
		<svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload>
			getNtucBulkUploadsByUuidAndCompanyId(
				String uuid, long companyId, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload>
						orderByComparator) {

		return getService().getNtucBulkUploadsByUuidAndCompanyId(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the number of ntuc bulk uploads.
	 *
	 * @return the number of ntuc bulk uploads
	 */
	public static int getNtucBulkUploadsCount() {
		return getService().getNtucBulkUploadsCount();
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

	public static svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
			updateNtucBulkUpload(
				long ntucBulkUploadId, long groupId, long companyId,
				long userId, String userName, String fileName, String rowData)
		throws com.liferay.portal.kernel.exception.PortalException,
			   javax.portlet.PortletException {

		return getService().updateNtucBulkUpload(
			ntucBulkUploadId, groupId, companyId, userId, userName, fileName,
			rowData);
	}

	/**
	 * Updates the ntuc bulk upload in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect NtucBulkUploadLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param ntucBulkUpload the ntuc bulk upload
	 * @return the ntuc bulk upload that was updated
	 */
	public static svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
		updateNtucBulkUpload(
			svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload ntucBulkUpload) {

		return getService().updateNtucBulkUpload(ntucBulkUpload);
	}

	public static NtucBulkUploadLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<NtucBulkUploadLocalService, NtucBulkUploadLocalService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			NtucBulkUploadLocalService.class);

		ServiceTracker<NtucBulkUploadLocalService, NtucBulkUploadLocalService>
			serviceTracker =
				new ServiceTracker
					<NtucBulkUploadLocalService, NtucBulkUploadLocalService>(
						bundle.getBundleContext(),
						NtucBulkUploadLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}