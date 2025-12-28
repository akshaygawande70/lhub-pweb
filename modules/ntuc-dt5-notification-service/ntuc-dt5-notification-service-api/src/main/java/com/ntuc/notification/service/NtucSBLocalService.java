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

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
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

import com.ntuc.notification.model.NtucSB;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the local service interface for NtucSB. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Brian Wing Shun Chan
 * @see NtucSBLocalServiceUtil
 * @generated
 */
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface NtucSBLocalService
	extends BaseLocalService, PersistedModelLocalService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add custom service methods to <code>com.ntuc.notification.service.impl.NtucSBLocalServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface. Consume the ntuc sb local service via injection or a <code>org.osgi.util.tracker.ServiceTracker</code>. Use {@link NtucSBLocalServiceUtil} if injection and service tracking are not available.
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
	@Indexable(type = IndexableType.REINDEX)
	public NtucSB addNtucSB(NtucSB ntucSB);

	/**
	 * Creates a new ntuc sb with the primary key. Does not add the ntuc sb to the database.
	 *
	 * @param ntucDTId the primary key for the new ntuc sb
	 * @return the new ntuc sb
	 */
	@Transactional(enabled = false)
	public NtucSB createNtucSB(long ntucDTId);

	/**
	 * @throws PortalException
	 */
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

	public void deleteByDateRange(Date fromDate, Date toDate);

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
	@Indexable(type = IndexableType.DELETE)
	public NtucSB deleteNtucSB(long ntucDTId) throws PortalException;

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
	@Indexable(type = IndexableType.DELETE)
	public NtucSB deleteNtucSB(NtucSB ntucSB);

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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.ntuc.notification.model.impl.NtucSBModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.ntuc.notification.model.impl.NtucSBModelImpl</code>.
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
	public List<NtucSB> fetchByIsRowLockFailed(boolean isRowLockFailed);

	/**
	 * Fetch the latest (by systemDate DESC) record for courseCode + event,
	 * excluding the current ntucDTId. Uses Service Builder finder.
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Optional<NtucSB> fetchLatestByCourseAndEvent(
		String courseCode, String event, long excludeNtucDTId);

	/**
	 * Fetch the latest (by systemDate DESC) record for courseCode + event + changeType,
	 * where changeFrom is a JSON-array string. We match using: changeFrom LIKE '%"type"%' .
	 * Excludes the current ntucDTId.
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Optional<NtucSB> fetchLatestByCourseEventAndChangeFrom(
		String courseCode, String event, String changeType,
		long excludeNtucDTId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public NtucSB fetchNtucSB(long ntucDTId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ActionableDynamicQuery getActionableDynamicQuery();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery();

	/**
	 * Returns the ntuc sb with the primary key.
	 *
	 * @param ntucDTId the primary key of the ntuc sb
	 * @return the ntuc sb
	 * @throws PortalException if a ntuc sb with the primary key could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public NtucSB getNtucSB(long ntucDTId) throws PortalException;

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<NtucSB> getNtucSBs(int start, int end);

	/**
	 * Returns the number of ntuc sbs.
	 *
	 * @return the number of ntuc sbs
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getNtucSBsCount();

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

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<NtucSB> getRecordsByChangeFrom(String changeFrom);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<NtucSB> getUnprocessedCronRecords(String status);

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
	@Indexable(type = IndexableType.REINDEX)
	public NtucSB updateNtucSB(NtucSB ntucSB);

}