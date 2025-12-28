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
 * Provides the local service utility for Parameter. This utility wraps
 * <code>svc.ntuc.nlh.parameter.service.impl.ParameterLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see ParameterLocalService
 * @generated
 */
public class ParameterLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>svc.ntuc.nlh.parameter.service.impl.ParameterLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the parameter to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ParameterLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param parameter the parameter
	 * @return the parameter that was added
	 */
	public static svc.ntuc.nlh.parameter.model.Parameter addParameter(
		svc.ntuc.nlh.parameter.model.Parameter parameter) {

		return getService().addParameter(parameter);
	}

	public static int countData(Long groupId, boolean deleted) {
		return getService().countData(groupId, deleted);
	}

	/**
	 * Creates a new parameter with the primary key. Does not add the parameter to the database.
	 *
	 * @param parameterId the primary key for the new parameter
	 * @return the new parameter
	 */
	public static svc.ntuc.nlh.parameter.model.Parameter createParameter(
		long parameterId) {

		return getService().createParameter(parameterId);
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
	 * Deletes the parameter with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ParameterLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param parameterId the primary key of the parameter
	 * @return the parameter that was removed
	 * @throws PortalException if a parameter with the primary key could not be found
	 */
	public static svc.ntuc.nlh.parameter.model.Parameter deleteParameter(
			long parameterId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteParameter(parameterId);
	}

	/**
	 * Deletes the parameter from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ParameterLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param parameter the parameter
	 * @return the parameter that was removed
	 */
	public static svc.ntuc.nlh.parameter.model.Parameter deleteParameter(
		svc.ntuc.nlh.parameter.model.Parameter parameter) {

		return getService().deleteParameter(parameter);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.parameter.model.impl.ParameterModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.parameter.model.impl.ParameterModelImpl</code>.
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

	public static svc.ntuc.nlh.parameter.model.Parameter fetchParameter(
		long parameterId) {

		return getService().fetchParameter(parameterId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static svc.ntuc.nlh.parameter.model.Parameter getByCode(
		String paramCode, boolean deleted) {

		return getService().getByCode(paramCode, deleted);
	}

	public static svc.ntuc.nlh.parameter.model.Parameter getByGroupCode(
		long groupId, long parameterGroupId, String paramCode,
		boolean deleted) {

		return getService().getByGroupCode(
			groupId, parameterGroupId, paramCode, deleted);
	}

	public static java.util.List<svc.ntuc.nlh.parameter.model.Parameter>
		getByGroupIdCode(long groupId, String paramCode, boolean deleted) {

		return getService().getByGroupIdCode(groupId, paramCode, deleted);
	}

	public static java.util.List<svc.ntuc.nlh.parameter.model.Parameter>
		getByParameterGroupId(long parameterGroupId, boolean deleted) {

		return getService().getByParameterGroupId(parameterGroupId, deleted);
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
	 * Returns the parameter with the primary key.
	 *
	 * @param parameterId the primary key of the parameter
	 * @return the parameter
	 * @throws PortalException if a parameter with the primary key could not be found
	 */
	public static svc.ntuc.nlh.parameter.model.Parameter getParameter(
			long parameterId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getParameter(parameterId);
	}

	/**
	 * Returns a range of all the parameters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.parameter.model.impl.ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @return the range of parameters
	 */
	public static java.util.List<svc.ntuc.nlh.parameter.model.Parameter>
		getParameters(int start, int end) {

		return getService().getParameters(start, end);
	}

	public static java.util.List<svc.ntuc.nlh.parameter.model.Parameter>
		getParameters(
			int start, int end, com.liferay.portal.kernel.dao.orm.Order order,
			String search, Long groupId) {

		return getService().getParameters(start, end, order, search, groupId);
	}

	/**
	 * Returns the number of parameters.
	 *
	 * @return the number of parameters
	 */
	public static int getParametersCount() {
		return getService().getParametersCount();
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Updates the parameter in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ParameterLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param parameter the parameter
	 * @return the parameter that was updated
	 */
	public static svc.ntuc.nlh.parameter.model.Parameter updateParameter(
		svc.ntuc.nlh.parameter.model.Parameter parameter) {

		return getService().updateParameter(parameter);
	}

	public static ParameterLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<ParameterLocalService, ParameterLocalService>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(ParameterLocalService.class);

		ServiceTracker<ParameterLocalService, ParameterLocalService>
			serviceTracker =
				new ServiceTracker
					<ParameterLocalService, ParameterLocalService>(
						bundle.getBundleContext(), ParameterLocalService.class,
						null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}