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
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;

import javax.portlet.PortletPreferences;

import org.osgi.annotation.versioning.ProviderType;

import svc.ntuc.nlh.otp.exception.NoSuchOtpException;
import svc.ntuc.nlh.otp.model.Otp;

/**
 * Provides the local service interface for Otp. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Brian Wing Shun Chan
 * @see OtpLocalServiceUtil
 * @generated
 */
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface OtpLocalService
	extends BaseLocalService, PersistedModelLocalService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add custom service methods to <code>svc.ntuc.nlh.otp.service.impl.OtpLocalServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface. Consume the otp local service via injection or a <code>org.osgi.util.tracker.ServiceTracker</code>. Use {@link OtpLocalServiceUtil} if injection and service tracking are not available.
	 */
	public Otp addOtp(
			long groupId, long userId, int keyLength, int otpLength,
			String fullName, String email, ServiceContext serviceContext,
			PortletPreferences portletPreferences, String languageId)
		throws PortalException;

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
	@Indexable(type = IndexableType.REINDEX)
	public Otp addOtp(Otp otp);

	/**
	 * Creates a new otp with the primary key. Does not add the otp to the database.
	 *
	 * @param otpId the primary key for the new otp
	 * @return the new otp
	 */
	@Transactional(enabled = false)
	public Otp createOtp(long otpId);

	/**
	 * @throws PortalException
	 */
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

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
	@Indexable(type = IndexableType.DELETE)
	public Otp deleteOtp(long otpId) throws PortalException;

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
	@Indexable(type = IndexableType.DELETE)
	public Otp deleteOtp(Otp otp);

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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.otp.model.impl.OtpModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.otp.model.impl.OtpModelImpl</code>.
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
	public Otp fetchOtp(long otpId);

	/**
	 * Returns the otp matching the UUID and group.
	 *
	 * @param uuid the otp's UUID
	 * @param groupId the primary key of the group
	 * @return the matching otp, or <code>null</code> if a matching otp could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Otp fetchOtpByUuidAndGroupId(String uuid, long groupId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ActionableDynamicQuery getActionableDynamicQuery();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		PortletDataContext portletDataContext);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery();

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

	/**
	 * Returns the otp with the primary key.
	 *
	 * @param otpId the primary key of the otp
	 * @return the otp
	 * @throws PortalException if a otp with the primary key could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Otp getOtp(long otpId) throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Otp getOtpByOtpIdAndUserId(long otpId, long userId)
		throws NoSuchOtpException;

	/**
	 * Returns the otp matching the UUID and group.
	 *
	 * @param uuid the otp's UUID
	 * @param groupId the primary key of the group
	 * @return the matching otp
	 * @throws PortalException if a matching otp could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Otp getOtpByUuidAndGroupId(String uuid, long groupId)
		throws PortalException;

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Otp> getOtps(int start, int end);

	/**
	 * Returns all the otps matching the UUID and company.
	 *
	 * @param uuid the UUID of the otps
	 * @param companyId the primary key of the company
	 * @return the matching otps, or an empty list if no matches were found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Otp> getOtpsByUuidAndCompanyId(String uuid, long companyId);

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Otp> getOtpsByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Otp> orderByComparator);

	/**
	 * Returns the number of otps.
	 *
	 * @return the number of otps
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getOtpsCount();

	/**
	 * @throws PortalException
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

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
	@Indexable(type = IndexableType.REINDEX)
	public Otp updateOtp(Otp otp);

}