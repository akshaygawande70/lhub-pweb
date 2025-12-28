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

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link ParameterGroupLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see ParameterGroupLocalService
 * @generated
 */
public class ParameterGroupLocalServiceWrapper
	implements ParameterGroupLocalService,
			   ServiceWrapper<ParameterGroupLocalService> {

	public ParameterGroupLocalServiceWrapper(
		ParameterGroupLocalService parameterGroupLocalService) {

		_parameterGroupLocalService = parameterGroupLocalService;
	}

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
	@Override
	public svc.ntuc.nlh.parameter.model.ParameterGroup addParameterGroup(
		svc.ntuc.nlh.parameter.model.ParameterGroup parameterGroup) {

		return _parameterGroupLocalService.addParameterGroup(parameterGroup);
	}

	@Override
	public int countData(Long groupId, boolean deleted) {
		return _parameterGroupLocalService.countData(groupId, deleted);
	}

	/**
	 * Creates a new parameter group with the primary key. Does not add the parameter group to the database.
	 *
	 * @param parameterGroupId the primary key for the new parameter group
	 * @return the new parameter group
	 */
	@Override
	public svc.ntuc.nlh.parameter.model.ParameterGroup createParameterGroup(
		long parameterGroupId) {

		return _parameterGroupLocalService.createParameterGroup(
			parameterGroupId);
	}

	@Override
	public svc.ntuc.nlh.parameter.model.ParameterGroup createParameterGroup(
		long groupId, long paramgroupId, long parentGroupId, String code,
		String name, String descriptions,
		com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay) {

		return _parameterGroupLocalService.createParameterGroup(
			groupId, paramgroupId, parentGroupId, code, name, descriptions,
			themeDisplay);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _parameterGroupLocalService.createPersistedModel(primaryKeyObj);
	}

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
	@Override
	public svc.ntuc.nlh.parameter.model.ParameterGroup deleteParameterGroup(
			long parameterGroupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _parameterGroupLocalService.deleteParameterGroup(
			parameterGroupId);
	}

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
	@Override
	public svc.ntuc.nlh.parameter.model.ParameterGroup deleteParameterGroup(
		svc.ntuc.nlh.parameter.model.ParameterGroup parameterGroup) {

		return _parameterGroupLocalService.deleteParameterGroup(parameterGroup);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _parameterGroupLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _parameterGroupLocalService.dynamicQuery();
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

		return _parameterGroupLocalService.dynamicQuery(dynamicQuery);
	}

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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _parameterGroupLocalService.dynamicQuery(
			dynamicQuery, start, end);
	}

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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _parameterGroupLocalService.dynamicQuery(
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

		return _parameterGroupLocalService.dynamicQueryCount(dynamicQuery);
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

		return _parameterGroupLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public svc.ntuc.nlh.parameter.model.ParameterGroup fetchParameterGroup(
		long parameterGroupId) {

		return _parameterGroupLocalService.fetchParameterGroup(
			parameterGroupId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _parameterGroupLocalService.getActionableDynamicQuery();
	}

	@Override
	public svc.ntuc.nlh.parameter.model.ParameterGroup getByCode(
		String code, boolean deleted) {

		return _parameterGroupLocalService.getByCode(code, deleted);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.parameter.model.ParameterGroup>
		getByGroupIdCode(long groupId, String code, boolean deleted) {

		return _parameterGroupLocalService.getByGroupIdCode(
			groupId, code, deleted);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.parameter.model.ParameterGroup>
		getByParentId(long parentId, boolean deleted) {

		return _parameterGroupLocalService.getByParentId(parentId, deleted);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _parameterGroupLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _parameterGroupLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * Returns the parameter group with the primary key.
	 *
	 * @param parameterGroupId the primary key of the parameter group
	 * @return the parameter group
	 * @throws PortalException if a parameter group with the primary key could not be found
	 */
	@Override
	public svc.ntuc.nlh.parameter.model.ParameterGroup getParameterGroup(
			long parameterGroupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _parameterGroupLocalService.getParameterGroup(parameterGroupId);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.parameter.model.ParameterGroup>
		getParameterGroupByKeywords(
			long groupId, String keywords, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<svc.ntuc.nlh.parameter.model.ParameterGroup>
					orderByComparator) {

		return _parameterGroupLocalService.getParameterGroupByKeywords(
			groupId, keywords, start, end, orderByComparator);
	}

	@Override
	public long getParameterGroupCountByKeywords(
		long groupId, String keywords) {

		return _parameterGroupLocalService.getParameterGroupCountByKeywords(
			groupId, keywords);
	}

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
	@Override
	public java.util.List<svc.ntuc.nlh.parameter.model.ParameterGroup>
		getParameterGroups(int start, int end) {

		return _parameterGroupLocalService.getParameterGroups(start, end);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.parameter.model.ParameterGroup>
		getParameterGroups(
			int start, int end, com.liferay.portal.kernel.dao.orm.Order order,
			String search, Long groupId) {

		return _parameterGroupLocalService.getParameterGroups(
			start, end, order, search, groupId);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.parameter.model.ParameterGroup>
		getParameterGroupsByGroupId(long groupId, boolean deleted) {

		return _parameterGroupLocalService.getParameterGroupsByGroupId(
			groupId, deleted);
	}

	/**
	 * Returns the number of parameter groups.
	 *
	 * @return the number of parameter groups
	 */
	@Override
	public int getParameterGroupsCount() {
		return _parameterGroupLocalService.getParameterGroupsCount();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _parameterGroupLocalService.getPersistedModel(primaryKeyObj);
	}

	@Override
	public svc.ntuc.nlh.parameter.model.ParameterGroup updateParameterGroup(
			long groupId, long paramgroupId, long parentGroupId, String code,
			String name, String descriptions,
			com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return _parameterGroupLocalService.updateParameterGroup(
			groupId, paramgroupId, parentGroupId, code, name, descriptions,
			themeDisplay);
	}

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
	@Override
	public svc.ntuc.nlh.parameter.model.ParameterGroup updateParameterGroup(
		svc.ntuc.nlh.parameter.model.ParameterGroup parameterGroup) {

		return _parameterGroupLocalService.updateParameterGroup(parameterGroup);
	}

	@Override
	public ParameterGroupLocalService getWrappedService() {
		return _parameterGroupLocalService;
	}

	@Override
	public void setWrappedService(
		ParameterGroupLocalService parameterGroupLocalService) {

		_parameterGroupLocalService = parameterGroupLocalService;
	}

	private ParameterGroupLocalService _parameterGroupLocalService;

}