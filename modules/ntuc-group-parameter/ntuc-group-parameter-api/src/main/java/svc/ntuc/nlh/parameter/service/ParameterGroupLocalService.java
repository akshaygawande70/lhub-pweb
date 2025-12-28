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

package svc.ntuc.nlh.parameter.service;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Order;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

import svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException;
import svc.ntuc.nlh.parameter.model.ParameterGroup;

/**
 * Provides the local service interface for ParameterGroup. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Brian Wing Shun Chan
 * @see ParameterGroupLocalServiceUtil
 * @generated
 */
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface ParameterGroupLocalService
	extends BaseLocalService, PersistedModelLocalService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add custom service methods to <code>svc.ntuc.nlh.parameter.service.impl.ParameterGroupLocalServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface. Consume the parameter group local service via injection or a <code>org.osgi.util.tracker.ServiceTracker</code>. Use {@link ParameterGroupLocalServiceUtil} if injection and service tracking are not available.
	 */

	/**
	 * Adds the parameter group to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ParameterGroupLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param parameterGroup the parameter group
	 * @return the parameter group that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	public ParameterGroup addParameterGroup(ParameterGroup parameterGroup);

	public int countData(Long groupId, boolean deleted);

	/**
	 * Creates a new parameter group with the primary key. Does not add the parameter group to the database.
	 *
	 * @param parameterGroupId the primary key for the new parameter group
	 * @return the new parameter group
	 */
	@Transactional(enabled = false)
	public ParameterGroup createParameterGroup(long parameterGroupId);

	public ParameterGroup createParameterGroup(
		long groupId, long paramgroupId, long parentGroupId, String code,
		String name, String descriptions, ThemeDisplay themeDisplay);

	/**
	 * @throws PortalException
	 */
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

	/**
	 * Deletes the parameter group with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ParameterGroupLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param parameterGroupId the primary key of the parameter group
	 * @return the parameter group that was removed
	 * @throws PortalException if a parameter group with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	public ParameterGroup deleteParameterGroup(long parameterGroupId)
		throws PortalException;

	/**
	 * Deletes the parameter group from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ParameterGroupLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param parameterGroup the parameter group
	 * @return the parameter group that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	public ParameterGroup deleteParameterGroup(ParameterGroup parameterGroup);

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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.parameter.model.impl.ParameterGroupModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.parameter.model.impl.ParameterGroupModelImpl</code>.
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
	public ParameterGroup fetchParameterGroup(long parameterGroupId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ActionableDynamicQuery getActionableDynamicQuery();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ParameterGroup getByCode(String code, boolean deleted);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ParameterGroup> getByGroupIdCode(
		long groupId, String code, boolean deleted);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ParameterGroup> getByParentId(long parentId, boolean deleted);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery();

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

	/**
	 * Returns the parameter group with the primary key.
	 *
	 * @param parameterGroupId the primary key of the parameter group
	 * @return the parameter group
	 * @throws PortalException if a parameter group with the primary key could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ParameterGroup getParameterGroup(long parameterGroupId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ParameterGroup> getParameterGroupByKeywords(
		long groupId, String keywords, int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long getParameterGroupCountByKeywords(long groupId, String keywords);

	/**
	 * Returns a range of all the parameter groups.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.parameter.model.impl.ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @return the range of parameter groups
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ParameterGroup> getParameterGroups(int start, int end);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ParameterGroup> getParameterGroups(
		int start, int end, Order order, String search, Long groupId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ParameterGroup> getParameterGroupsByGroupId(
		long groupId, boolean deleted);

	/**
	 * Returns the number of parameter groups.
	 *
	 * @return the number of parameter groups
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getParameterGroupsCount();

	/**
	 * @throws PortalException
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

	public ParameterGroup updateParameterGroup(
			long groupId, long paramgroupId, long parentGroupId, String code,
			String name, String descriptions, ThemeDisplay themeDisplay)
		throws NoSuchParameterGroupException;

	/**
	 * Updates the parameter group in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ParameterGroupLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param parameterGroup the parameter group
	 * @return the parameter group that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	public ParameterGroup updateParameterGroup(ParameterGroup parameterGroup);

}