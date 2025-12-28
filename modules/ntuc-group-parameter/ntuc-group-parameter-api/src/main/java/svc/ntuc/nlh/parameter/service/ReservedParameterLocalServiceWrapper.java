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
 * Provides a wrapper for {@link ReservedParameterLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see ReservedParameterLocalService
 * @generated
 */
public class ReservedParameterLocalServiceWrapper
	implements ReservedParameterLocalService,
			   ServiceWrapper<ReservedParameterLocalService> {

	public ReservedParameterLocalServiceWrapper(
		ReservedParameterLocalService reservedParameterLocalService) {

		_reservedParameterLocalService = reservedParameterLocalService;
	}

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
	@Override
	public svc.ntuc.nlh.parameter.model.ReservedParameter addReservedParameter(
		svc.ntuc.nlh.parameter.model.ReservedParameter reservedParameter) {

		return _reservedParameterLocalService.addReservedParameter(
			reservedParameter);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _reservedParameterLocalService.createPersistedModel(
			primaryKeyObj);
	}

	/**
	 * Creates a new reserved parameter with the primary key. Does not add the reserved parameter to the database.
	 *
	 * @param reservedParameterId the primary key for the new reserved parameter
	 * @return the new reserved parameter
	 */
	@Override
	public svc.ntuc.nlh.parameter.model.ReservedParameter
		createReservedParameter(long reservedParameterId) {

		return _reservedParameterLocalService.createReservedParameter(
			reservedParameterId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _reservedParameterLocalService.deletePersistedModel(
			persistedModel);
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
	@Override
	public svc.ntuc.nlh.parameter.model.ReservedParameter
			deleteReservedParameter(long reservedParameterId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _reservedParameterLocalService.deleteReservedParameter(
			reservedParameterId);
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
	@Override
	public svc.ntuc.nlh.parameter.model.ReservedParameter
		deleteReservedParameter(
			svc.ntuc.nlh.parameter.model.ReservedParameter reservedParameter) {

		return _reservedParameterLocalService.deleteReservedParameter(
			reservedParameter);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _reservedParameterLocalService.dynamicQuery();
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

		return _reservedParameterLocalService.dynamicQuery(dynamicQuery);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _reservedParameterLocalService.dynamicQuery(
			dynamicQuery, start, end);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _reservedParameterLocalService.dynamicQuery(
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

		return _reservedParameterLocalService.dynamicQueryCount(dynamicQuery);
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

		return _reservedParameterLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public svc.ntuc.nlh.parameter.model.ReservedParameter
		fetchReservedParameter(long reservedParameterId) {

		return _reservedParameterLocalService.fetchReservedParameter(
			reservedParameterId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _reservedParameterLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _reservedParameterLocalService.
			getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _reservedParameterLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _reservedParameterLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the reserved parameter with the primary key.
	 *
	 * @param reservedParameterId the primary key of the reserved parameter
	 * @return the reserved parameter
	 * @throws PortalException if a reserved parameter with the primary key could not be found
	 */
	@Override
	public svc.ntuc.nlh.parameter.model.ReservedParameter getReservedParameter(
			long reservedParameterId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _reservedParameterLocalService.getReservedParameter(
			reservedParameterId);
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
	@Override
	public java.util.List<svc.ntuc.nlh.parameter.model.ReservedParameter>
		getReservedParameters(int start, int end) {

		return _reservedParameterLocalService.getReservedParameters(start, end);
	}

	/**
	 * Returns the number of reserved parameters.
	 *
	 * @return the number of reserved parameters
	 */
	@Override
	public int getReservedParametersCount() {
		return _reservedParameterLocalService.getReservedParametersCount();
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
	@Override
	public svc.ntuc.nlh.parameter.model.ReservedParameter
		updateReservedParameter(
			svc.ntuc.nlh.parameter.model.ReservedParameter reservedParameter) {

		return _reservedParameterLocalService.updateReservedParameter(
			reservedParameter);
	}

	@Override
	public ReservedParameterLocalService getWrappedService() {
		return _reservedParameterLocalService;
	}

	@Override
	public void setWrappedService(
		ReservedParameterLocalService reservedParameterLocalService) {

		_reservedParameterLocalService = reservedParameterLocalService;
	}

	private ReservedParameterLocalService _reservedParameterLocalService;

}