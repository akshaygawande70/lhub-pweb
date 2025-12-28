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

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for AuditLog. This utility wraps
 * <code>com.ntuc.notification.service.impl.AuditLogLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see AuditLogLocalService
 * @generated
 */
public class AuditLogLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.ntuc.notification.service.impl.AuditLogLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the audit log to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AuditLogLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param auditLog the audit log
	 * @return the audit log that was added
	 */
	public static com.ntuc.notification.model.AuditLog addAuditLog(
		com.ntuc.notification.model.AuditLog auditLog) {

		return getService().addAuditLog(auditLog);
	}

	/**
	 * Creates a new audit log with the primary key. Does not add the audit log to the database.
	 *
	 * @param auditLogId the primary key for the new audit log
	 * @return the new audit log
	 */
	public static com.ntuc.notification.model.AuditLog createAuditLog(
		long auditLogId) {

		return getService().createAuditLog(auditLogId);
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
	 * Deletes the audit log from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AuditLogLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param auditLog the audit log
	 * @return the audit log that was removed
	 */
	public static com.ntuc.notification.model.AuditLog deleteAuditLog(
		com.ntuc.notification.model.AuditLog auditLog) {

		return getService().deleteAuditLog(auditLog);
	}

	/**
	 * Deletes the audit log with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AuditLogLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param auditLogId the primary key of the audit log
	 * @return the audit log that was removed
	 * @throws PortalException if a audit log with the primary key could not be found
	 */
	public static com.ntuc.notification.model.AuditLog deleteAuditLog(
			long auditLogId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteAuditLog(auditLogId);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.ntuc.notification.model.impl.AuditLogModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.ntuc.notification.model.impl.AuditLogModelImpl</code>.
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

	/**
	 * DB-backed dedupe check for alert emails (cluster-safe).
	 *
	 * We consider both EMAIL_SENT and EMAIL_SUPPRESSED as "recent outcomes" so we do not spam ops.
	 * NOTE: we intentionally ignore EMAIL_SEND_FAILED so that transient mail outages can retry.
	 *
	 * @param companyId company scope
	 * @param fingerprint dedupe fingerprint (required)
	 * @param sinceStartTimeMs window start (inclusive)
	 * @return true if a recent outcome exists within the window
	 */
	public static boolean existsRecentAlertOutcome(
		long companyId, String fingerprint, long sinceStartTimeMs) {

		return getService().existsRecentAlertOutcome(
			companyId, fingerprint, sinceStartTimeMs);
	}

	public static com.ntuc.notification.model.AuditLog fetchAuditLog(
		long auditLogId) {

		return getService().fetchAuditLog(auditLogId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	/**
	 * Returns the audit log with the primary key.
	 *
	 * @param auditLogId the primary key of the audit log
	 * @return the audit log
	 * @throws PortalException if a audit log with the primary key could not be found
	 */
	public static com.ntuc.notification.model.AuditLog getAuditLog(
			long auditLogId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAuditLog(auditLogId);
	}

	/**
	 * Returns a range of all the audit logs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.ntuc.notification.model.impl.AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @return the range of audit logs
	 */
	public static java.util.List<com.ntuc.notification.model.AuditLog>
		getAuditLogs(int start, int end) {

		return getService().getAuditLogs(start, end);
	}

	/**
	 * Returns the number of audit logs.
	 *
	 * @return the number of audit logs
	 */
	public static int getAuditLogsCount() {
		return getService().getAuditLogsCount();
	}

	public static com.ntuc.notification.model.AuditLogDetailsJsonBlobModel
		getDetailsJsonBlobModel(java.io.Serializable primaryKey) {

		return getService().getDetailsJsonBlobModel(primaryKey);
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

	public static
		com.ntuc.notification.model.AuditLogStackTraceTruncatedBlobModel
			getStackTraceTruncatedBlobModel(java.io.Serializable primaryKey) {

		return getService().getStackTraceTruncatedBlobModel(primaryKey);
	}

	public static java.io.InputStream openDetailsJsonInputStream(
		long auditLogId) {

		return getService().openDetailsJsonInputStream(auditLogId);
	}

	public static java.io.InputStream openStackTraceTruncatedInputStream(
		long auditLogId) {

		return getService().openStackTraceTruncatedInputStream(auditLogId);
	}

	/**
	 * Updates the audit log in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AuditLogLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param auditLog the audit log
	 * @return the audit log that was updated
	 */
	public static com.ntuc.notification.model.AuditLog updateAuditLog(
		com.ntuc.notification.model.AuditLog auditLog) {

		return getService().updateAuditLog(auditLog);
	}

	public static AuditLogLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<AuditLogLocalService, AuditLogLocalService>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(AuditLogLocalService.class);

		ServiceTracker<AuditLogLocalService, AuditLogLocalService>
			serviceTracker =
				new ServiceTracker<AuditLogLocalService, AuditLogLocalService>(
					bundle.getBundleContext(), AuditLogLocalService.class,
					null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}