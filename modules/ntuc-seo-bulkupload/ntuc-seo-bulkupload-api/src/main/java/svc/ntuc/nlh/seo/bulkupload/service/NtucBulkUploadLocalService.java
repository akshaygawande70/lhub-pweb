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

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;

import javax.portlet.PortletException;

import org.osgi.annotation.versioning.ProviderType;

import svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload;

/**
 * Provides the local service interface for NtucBulkUpload. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Brian Wing Shun Chan
 * @see NtucBulkUploadLocalServiceUtil
 * @generated
 */
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface NtucBulkUploadLocalService
	extends BaseLocalService, PersistedModelLocalService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add custom service methods to <code>svc.ntuc.nlh.seo.bulkupload.service.impl.NtucBulkUploadLocalServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface. Consume the ntuc bulk upload local service via injection or a <code>org.osgi.util.tracker.ServiceTracker</code>. Use {@link NtucBulkUploadLocalServiceUtil} if injection and service tracking are not available.
	 */
	@Indexable(type = IndexableType.REINDEX)
	public NtucBulkUpload addNtucBulkUpload(
			long groupId, long companyId, long userId, String userName,
			String fileName, String rowData)
		throws PortletException;

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
	@Indexable(type = IndexableType.REINDEX)
	public NtucBulkUpload addNtucBulkUpload(NtucBulkUpload ntucBulkUpload);

	/**
	 * Creates a new ntuc bulk upload with the primary key. Does not add the ntuc bulk upload to the database.
	 *
	 * @param ntucBulkUploadId the primary key for the new ntuc bulk upload
	 * @return the new ntuc bulk upload
	 */
	@Transactional(enabled = false)
	public NtucBulkUpload createNtucBulkUpload(long ntucBulkUploadId);

	/**
	 * @throws PortalException
	 */
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

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
	@Indexable(type = IndexableType.DELETE)
	public NtucBulkUpload deleteNtucBulkUpload(long ntucBulkUploadId)
		throws PortalException;

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
	@Indexable(type = IndexableType.DELETE)
	public NtucBulkUpload deleteNtucBulkUpload(NtucBulkUpload ntucBulkUpload);

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public DynamicQuery dynamicQuery();

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery);

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end);

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator);

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long dynamicQueryCount(DynamicQuery dynamicQuery);

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public NtucBulkUpload fetchNtucBulkUpload(long ntucBulkUploadId);

	/**
	 * Returns the ntuc bulk upload matching the UUID and group.
	 *
	 * @param uuid the ntuc bulk upload's UUID
	 * @param groupId the primary key of the group
	 * @return the matching ntuc bulk upload, or <code>null</code> if a matching ntuc bulk upload could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public NtucBulkUpload fetchNtucBulkUploadByUuidAndGroupId(
		String uuid, long groupId);

	public NtucBulkUpload findByNtucBulkUploadId(long ntucBulkUploadId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ActionableDynamicQuery getActionableDynamicQuery();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		PortletDataContext portletDataContext);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery();

	/**
	 * Returns the ntuc bulk upload with the primary key.
	 *
	 * @param ntucBulkUploadId the primary key of the ntuc bulk upload
	 * @return the ntuc bulk upload
	 * @throws PortalException if a ntuc bulk upload with the primary key could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public NtucBulkUpload getNtucBulkUpload(long ntucBulkUploadId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<NtucBulkUpload> getNtucBulkUploadByKeywords(
		long groupId, long companyId, String keywords, int start, int end,
		OrderByComparator<NtucBulkUpload> orderByComparator);

	/**
	 * Returns the ntuc bulk upload matching the UUID and group.
	 *
	 * @param uuid the ntuc bulk upload's UUID
	 * @param groupId the primary key of the group
	 * @return the matching ntuc bulk upload
	 * @throws PortalException if a matching ntuc bulk upload could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public NtucBulkUpload getNtucBulkUploadByUuidAndGroupId(
			String uuid, long groupId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long getNtucBulkUploadCountsByKeywords(
		long groupId, long companyId, String keywords);

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<NtucBulkUpload> getNtucBulkUploads(int start, int end);

	/**
	 * Returns all the ntuc bulk uploads matching the UUID and company.
	 *
	 * @param uuid the UUID of the ntuc bulk uploads
	 * @param companyId the primary key of the company
	 * @return the matching ntuc bulk uploads, or an empty list if no matches were found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<NtucBulkUpload> getNtucBulkUploadsByUuidAndCompanyId(
		String uuid, long companyId);

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<NtucBulkUpload> getNtucBulkUploadsByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<NtucBulkUpload> orderByComparator);

	/**
	 * Returns the number of ntuc bulk uploads.
	 *
	 * @return the number of ntuc bulk uploads
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getNtucBulkUploadsCount();

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

	/**
	 * @throws PortalException
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

	@Indexable(type = IndexableType.REINDEX)
	public NtucBulkUpload updateNtucBulkUpload(
			long ntucBulkUploadId, long groupId, long companyId, long userId,
			String userName, String fileName, String rowData)
		throws PortalException, PortletException;

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
	@Indexable(type = IndexableType.REINDEX)
	public NtucBulkUpload updateNtucBulkUpload(NtucBulkUpload ntucBulkUpload);

}