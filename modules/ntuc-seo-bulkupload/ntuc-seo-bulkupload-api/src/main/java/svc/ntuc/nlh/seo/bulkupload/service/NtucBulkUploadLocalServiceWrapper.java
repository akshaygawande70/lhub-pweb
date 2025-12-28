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

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link NtucBulkUploadLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see NtucBulkUploadLocalService
 * @generated
 */
public class NtucBulkUploadLocalServiceWrapper
	implements NtucBulkUploadLocalService,
			   ServiceWrapper<NtucBulkUploadLocalService> {

	public NtucBulkUploadLocalServiceWrapper(
		NtucBulkUploadLocalService ntucBulkUploadLocalService) {

		_ntucBulkUploadLocalService = ntucBulkUploadLocalService;
	}

	@Override
	public svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload addNtucBulkUpload(
			long groupId, long companyId, long userId, String userName,
			String fileName, String rowData)
		throws javax.portlet.PortletException {

		return _ntucBulkUploadLocalService.addNtucBulkUpload(
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
	@Override
	public svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload addNtucBulkUpload(
		svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload ntucBulkUpload) {

		return _ntucBulkUploadLocalService.addNtucBulkUpload(ntucBulkUpload);
	}

	/**
	 * Creates a new ntuc bulk upload with the primary key. Does not add the ntuc bulk upload to the database.
	 *
	 * @param ntucBulkUploadId the primary key for the new ntuc bulk upload
	 * @return the new ntuc bulk upload
	 */
	@Override
	public svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
		createNtucBulkUpload(long ntucBulkUploadId) {

		return _ntucBulkUploadLocalService.createNtucBulkUpload(
			ntucBulkUploadId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _ntucBulkUploadLocalService.createPersistedModel(primaryKeyObj);
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
	@Override
	public svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
			deleteNtucBulkUpload(long ntucBulkUploadId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _ntucBulkUploadLocalService.deleteNtucBulkUpload(
			ntucBulkUploadId);
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
	@Override
	public svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
		deleteNtucBulkUpload(
			svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload ntucBulkUpload) {

		return _ntucBulkUploadLocalService.deleteNtucBulkUpload(ntucBulkUpload);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _ntucBulkUploadLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _ntucBulkUploadLocalService.dynamicQuery();
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

		return _ntucBulkUploadLocalService.dynamicQuery(dynamicQuery);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _ntucBulkUploadLocalService.dynamicQuery(
			dynamicQuery, start, end);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _ntucBulkUploadLocalService.dynamicQuery(
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

		return _ntucBulkUploadLocalService.dynamicQueryCount(dynamicQuery);
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

		return _ntucBulkUploadLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload fetchNtucBulkUpload(
		long ntucBulkUploadId) {

		return _ntucBulkUploadLocalService.fetchNtucBulkUpload(
			ntucBulkUploadId);
	}

	/**
	 * Returns the ntuc bulk upload matching the UUID and group.
	 *
	 * @param uuid the ntuc bulk upload's UUID
	 * @param groupId the primary key of the group
	 * @return the matching ntuc bulk upload, or <code>null</code> if a matching ntuc bulk upload could not be found
	 */
	@Override
	public svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
		fetchNtucBulkUploadByUuidAndGroupId(String uuid, long groupId) {

		return _ntucBulkUploadLocalService.fetchNtucBulkUploadByUuidAndGroupId(
			uuid, groupId);
	}

	@Override
	public svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
		findByNtucBulkUploadId(long ntucBulkUploadId) {

		return _ntucBulkUploadLocalService.findByNtucBulkUploadId(
			ntucBulkUploadId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _ntucBulkUploadLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return _ntucBulkUploadLocalService.getExportActionableDynamicQuery(
			portletDataContext);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _ntucBulkUploadLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the ntuc bulk upload with the primary key.
	 *
	 * @param ntucBulkUploadId the primary key of the ntuc bulk upload
	 * @return the ntuc bulk upload
	 * @throws PortalException if a ntuc bulk upload with the primary key could not be found
	 */
	@Override
	public svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload getNtucBulkUpload(
			long ntucBulkUploadId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _ntucBulkUploadLocalService.getNtucBulkUpload(ntucBulkUploadId);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload>
		getNtucBulkUploadByKeywords(
			long groupId, long companyId, String keywords, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload>
					orderByComparator) {

		return _ntucBulkUploadLocalService.getNtucBulkUploadByKeywords(
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
	@Override
	public svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
			getNtucBulkUploadByUuidAndGroupId(String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _ntucBulkUploadLocalService.getNtucBulkUploadByUuidAndGroupId(
			uuid, groupId);
	}

	@Override
	public long getNtucBulkUploadCountsByKeywords(
		long groupId, long companyId, String keywords) {

		return _ntucBulkUploadLocalService.getNtucBulkUploadCountsByKeywords(
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
	@Override
	public java.util.List<svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload>
		getNtucBulkUploads(int start, int end) {

		return _ntucBulkUploadLocalService.getNtucBulkUploads(start, end);
	}

	/**
	 * Returns all the ntuc bulk uploads matching the UUID and company.
	 *
	 * @param uuid the UUID of the ntuc bulk uploads
	 * @param companyId the primary key of the company
	 * @return the matching ntuc bulk uploads, or an empty list if no matches were found
	 */
	@Override
	public java.util.List<svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload>
		getNtucBulkUploadsByUuidAndCompanyId(String uuid, long companyId) {

		return _ntucBulkUploadLocalService.getNtucBulkUploadsByUuidAndCompanyId(
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
	@Override
	public java.util.List<svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload>
		getNtucBulkUploadsByUuidAndCompanyId(
			String uuid, long companyId, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload>
					orderByComparator) {

		return _ntucBulkUploadLocalService.getNtucBulkUploadsByUuidAndCompanyId(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the number of ntuc bulk uploads.
	 *
	 * @return the number of ntuc bulk uploads
	 */
	@Override
	public int getNtucBulkUploadsCount() {
		return _ntucBulkUploadLocalService.getNtucBulkUploadsCount();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _ntucBulkUploadLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _ntucBulkUploadLocalService.getPersistedModel(primaryKeyObj);
	}

	@Override
	public svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
			updateNtucBulkUpload(
				long ntucBulkUploadId, long groupId, long companyId,
				long userId, String userName, String fileName, String rowData)
		throws com.liferay.portal.kernel.exception.PortalException,
			   javax.portlet.PortletException {

		return _ntucBulkUploadLocalService.updateNtucBulkUpload(
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
	@Override
	public svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload
		updateNtucBulkUpload(
			svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload ntucBulkUpload) {

		return _ntucBulkUploadLocalService.updateNtucBulkUpload(ntucBulkUpload);
	}

	@Override
	public NtucBulkUploadLocalService getWrappedService() {
		return _ntucBulkUploadLocalService;
	}

	@Override
	public void setWrappedService(
		NtucBulkUploadLocalService ntucBulkUploadLocalService) {

		_ntucBulkUploadLocalService = ntucBulkUploadLocalService;
	}

	private NtucBulkUploadLocalService _ntucBulkUploadLocalService;

}