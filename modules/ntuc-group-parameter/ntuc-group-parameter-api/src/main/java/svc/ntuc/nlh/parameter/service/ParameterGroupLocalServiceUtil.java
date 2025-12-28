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

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for ParameterGroup. This utility wraps
 * <code>svc.ntuc.nlh.parameter.service.impl.ParameterGroupLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see ParameterGroupLocalService
 * @generated
 */
public class ParameterGroupLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>svc.ntuc.nlh.parameter.service.impl.ParameterGroupLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
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
	public static svc.ntuc.nlh.parameter.model.ParameterGroup addParameterGroup(
		svc.ntuc.nlh.parameter.model.ParameterGroup parameterGroup) {

		return getService().addParameterGroup(parameterGroup);
	}

	public static int countData(Long groupId, boolean deleted) {
		return getService().countData(groupId, deleted);
	}

	/**
	 * Creates a new parameter group with the primary key. Does not add the parameter group to the database.
	 *
	 * @param parameterGroupId the primary key for the new parameter group
	 * @return the new parameter group
	 */
	public static svc.ntuc.nlh.parameter.model.ParameterGroup
		createParameterGroup(long parameterGroupId) {

		return getService().createParameterGroup(parameterGroupId);
	}

	public static svc.ntuc.nlh.parameter.model.ParameterGroup
		createParameterGroup(
			long groupId, long paramgroupId, long parentGroupId, String code,
			String name, String descriptions,
			com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay) {

		return getService().createParameterGroup(
			groupId, paramgroupId, parentGroupId, code, name, descriptions,
			themeDisplay);
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
	public static svc.ntuc.nlh.parameter.model.ParameterGroup
			deleteParameterGroup(long parameterGroupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteParameterGroup(parameterGroupId);
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
	public static svc.ntuc.nlh.parameter.model.ParameterGroup
		deleteParameterGroup(
			svc.ntuc.nlh.parameter.model.ParameterGroup parameterGroup) {

		return getService().deleteParameterGroup(parameterGroup);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.parameter.model.impl.ParameterGroupModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.parameter.model.impl.ParameterGroupModelImpl</code>.
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

	public static svc.ntuc.nlh.parameter.model.ParameterGroup
		fetchParameterGroup(long parameterGroupId) {

		return getService().fetchParameterGroup(parameterGroupId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static svc.ntuc.nlh.parameter.model.ParameterGroup getByCode(
		String code, boolean deleted) {

		return getService().getByCode(code, deleted);
	}

	public static java.util.List<svc.ntuc.nlh.parameter.model.ParameterGroup>
		getByGroupIdCode(long groupId, String code, boolean deleted) {

		return getService().getByGroupIdCode(groupId, code, deleted);
	}

	public static java.util.List<svc.ntuc.nlh.parameter.model.ParameterGroup>
		getByParentId(long parentId, boolean deleted) {

		return getService().getByParentId(parentId, deleted);
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
	 * Returns the parameter group with the primary key.
	 *
	 * @param parameterGroupId the primary key of the parameter group
	 * @return the parameter group
	 * @throws PortalException if a parameter group with the primary key could not be found
	 */
	public static svc.ntuc.nlh.parameter.model.ParameterGroup getParameterGroup(
			long parameterGroupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getParameterGroup(parameterGroupId);
	}

	public static java.util.List<svc.ntuc.nlh.parameter.model.ParameterGroup>
		getParameterGroupByKeywords(
			long groupId, String keywords, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<svc.ntuc.nlh.parameter.model.ParameterGroup>
					orderByComparator) {

		return getService().getParameterGroupByKeywords(
			groupId, keywords, start, end, orderByComparator);
	}

	public static long getParameterGroupCountByKeywords(
		long groupId, String keywords) {

		return getService().getParameterGroupCountByKeywords(groupId, keywords);
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
	public static java.util.List<svc.ntuc.nlh.parameter.model.ParameterGroup>
		getParameterGroups(int start, int end) {

		return getService().getParameterGroups(start, end);
	}

	public static java.util.List<svc.ntuc.nlh.parameter.model.ParameterGroup>
		getParameterGroups(
			int start, int end, com.liferay.portal.kernel.dao.orm.Order order,
			String search, Long groupId) {

		return getService().getParameterGroups(
			start, end, order, search, groupId);
	}

	public static java.util.List<svc.ntuc.nlh.parameter.model.ParameterGroup>
		getParameterGroupsByGroupId(long groupId, boolean deleted) {

		return getService().getParameterGroupsByGroupId(groupId, deleted);
	}

	/**
	 * Returns the number of parameter groups.
	 *
	 * @return the number of parameter groups
	 */
	public static int getParameterGroupsCount() {
		return getService().getParameterGroupsCount();
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	public static svc.ntuc.nlh.parameter.model.ParameterGroup
			updateParameterGroup(
				long groupId, long paramgroupId, long parentGroupId,
				String code, String name, String descriptions,
				com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay)
		throws svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException {

		return getService().updateParameterGroup(
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
	public static svc.ntuc.nlh.parameter.model.ParameterGroup
		updateParameterGroup(
			svc.ntuc.nlh.parameter.model.ParameterGroup parameterGroup) {

		return getService().updateParameterGroup(parameterGroup);
	}

	public static ParameterGroupLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<ParameterGroupLocalService, ParameterGroupLocalService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			ParameterGroupLocalService.class);

		ServiceTracker<ParameterGroupLocalService, ParameterGroupLocalService>
			serviceTracker =
				new ServiceTracker
					<ParameterGroupLocalService, ParameterGroupLocalService>(
						bundle.getBundleContext(),
						ParameterGroupLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}