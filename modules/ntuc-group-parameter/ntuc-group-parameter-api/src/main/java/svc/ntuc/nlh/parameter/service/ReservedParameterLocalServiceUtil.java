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
 * Provides the local service utility for ReservedParameter. This utility wraps
 * <code>svc.ntuc.nlh.parameter.service.impl.ReservedParameterLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see ReservedParameterLocalService
 * @generated
 */
public class ReservedParameterLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>svc.ntuc.nlh.parameter.service.impl.ReservedParameterLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the reserved parameter to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ReservedParameterLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param reservedParameter the reserved parameter
	 * @return the reserved parameter that was added
	 */
	public static svc.ntuc.nlh.parameter.model.ReservedParameter
		addReservedParameter(
			svc.ntuc.nlh.parameter.model.ReservedParameter reservedParameter) {

		return getService().addReservedParameter(reservedParameter);
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
	 * Creates a new reserved parameter with the primary key. Does not add the reserved parameter to the database.
	 *
	 * @param reservedParameterId the primary key for the new reserved parameter
	 * @return the new reserved parameter
	 */
	public static svc.ntuc.nlh.parameter.model.ReservedParameter
		createReservedParameter(long reservedParameterId) {

		return getService().createReservedParameter(reservedParameterId);
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

	/**
	 * Deletes the reserved parameter with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ReservedParameterLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param reservedParameterId the primary key of the reserved parameter
	 * @return the reserved parameter that was removed
	 * @throws PortalException if a reserved parameter with the primary key could not be found
	 */
	public static svc.ntuc.nlh.parameter.model.ReservedParameter
			deleteReservedParameter(long reservedParameterId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteReservedParameter(reservedParameterId);
	}

	/**
	 * Deletes the reserved parameter from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ReservedParameterLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param reservedParameter the reserved parameter
	 * @return the reserved parameter that was removed
	 */
	public static svc.ntuc.nlh.parameter.model.ReservedParameter
		deleteReservedParameter(
			svc.ntuc.nlh.parameter.model.ReservedParameter reservedParameter) {

		return getService().deleteReservedParameter(reservedParameter);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.parameter.model.impl.ReservedParameterModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.parameter.model.impl.ReservedParameterModelImpl</code>.
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

	public static svc.ntuc.nlh.parameter.model.ReservedParameter
		fetchReservedParameter(long reservedParameterId) {

		return getService().fetchReservedParameter(reservedParameterId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
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
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the reserved parameter with the primary key.
	 *
	 * @param reservedParameterId the primary key of the reserved parameter
	 * @return the reserved parameter
	 * @throws PortalException if a reserved parameter with the primary key could not be found
	 */
	public static svc.ntuc.nlh.parameter.model.ReservedParameter
			getReservedParameter(long reservedParameterId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getReservedParameter(reservedParameterId);
	}

	/**
	 * Returns a range of all the reserved parameters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.parameter.model.impl.ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @return the range of reserved parameters
	 */
	public static java.util.List<svc.ntuc.nlh.parameter.model.ReservedParameter>
		getReservedParameters(int start, int end) {

		return getService().getReservedParameters(start, end);
	}

	/**
	 * Returns the number of reserved parameters.
	 *
	 * @return the number of reserved parameters
	 */
	public static int getReservedParametersCount() {
		return getService().getReservedParametersCount();
	}

	/**
	 * Updates the reserved parameter in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ReservedParameterLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param reservedParameter the reserved parameter
	 * @return the reserved parameter that was updated
	 */
	public static svc.ntuc.nlh.parameter.model.ReservedParameter
		updateReservedParameter(
			svc.ntuc.nlh.parameter.model.ReservedParameter reservedParameter) {

		return getService().updateReservedParameter(reservedParameter);
	}

	public static ReservedParameterLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<ReservedParameterLocalService, ReservedParameterLocalService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			ReservedParameterLocalService.class);

		ServiceTracker
			<ReservedParameterLocalService, ReservedParameterLocalService>
				serviceTracker =
					new ServiceTracker
						<ReservedParameterLocalService,
						 ReservedParameterLocalService>(
							 bundle.getBundleContext(),
							 ReservedParameterLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}