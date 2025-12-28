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

package com.ntuc.notification.service.persistence;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import com.ntuc.notification.model.AuditLog;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the audit log service. This utility wraps <code>com.ntuc.notification.service.persistence.impl.AuditLogPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AuditLogPersistence
 * @generated
 */
public class AuditLogUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(AuditLog auditLog) {
		getPersistence().clearCache(auditLog);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, AuditLog> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<AuditLog> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<AuditLog> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<AuditLog> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static AuditLog update(AuditLog auditLog) {
		return getPersistence().update(auditLog);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static AuditLog update(
		AuditLog auditLog, ServiceContext serviceContext) {

		return getPersistence().update(auditLog, serviceContext);
	}

	/**
	 * Returns all the audit logs where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @return the matching audit logs
	 */
	public static List<AuditLog> findByCorrelationId(String correlationId) {
		return getPersistence().findByCorrelationId(correlationId);
	}

	/**
	 * Returns a range of all the audit logs where correlationId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param correlationId the correlation ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @return the range of matching audit logs
	 */
	public static List<AuditLog> findByCorrelationId(
		String correlationId, int start, int end) {

		return getPersistence().findByCorrelationId(correlationId, start, end);
	}

	/**
	 * Returns an ordered range of all the audit logs where correlationId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param correlationId the correlation ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByCorrelationId(
		String correlationId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().findByCorrelationId(
			correlationId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the audit logs where correlationId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param correlationId the correlation ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByCorrelationId(
		String correlationId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByCorrelationId(
			correlationId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first audit log in the ordered set where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByCorrelationId_First(
			String correlationId, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCorrelationId_First(
			correlationId, orderByComparator);
	}

	/**
	 * Returns the first audit log in the ordered set where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByCorrelationId_First(
		String correlationId, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByCorrelationId_First(
			correlationId, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByCorrelationId_Last(
			String correlationId, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCorrelationId_Last(
			correlationId, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByCorrelationId_Last(
		String correlationId, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByCorrelationId_Last(
			correlationId, orderByComparator);
	}

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where correlationId = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param correlationId the correlation ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public static AuditLog[] findByCorrelationId_PrevAndNext(
			long auditLogId, String correlationId,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCorrelationId_PrevAndNext(
			auditLogId, correlationId, orderByComparator);
	}

	/**
	 * Removes all the audit logs where correlationId = &#63; from the database.
	 *
	 * @param correlationId the correlation ID
	 */
	public static void removeByCorrelationId(String correlationId) {
		getPersistence().removeByCorrelationId(correlationId);
	}

	/**
	 * Returns the number of audit logs where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @return the number of matching audit logs
	 */
	public static int countByCorrelationId(String correlationId) {
		return getPersistence().countByCorrelationId(correlationId);
	}

	/**
	 * Returns all the audit logs where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @return the matching audit logs
	 */
	public static List<AuditLog> findByJobRunId(String jobRunId) {
		return getPersistence().findByJobRunId(jobRunId);
	}

	/**
	 * Returns a range of all the audit logs where jobRunId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param jobRunId the job run ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @return the range of matching audit logs
	 */
	public static List<AuditLog> findByJobRunId(
		String jobRunId, int start, int end) {

		return getPersistence().findByJobRunId(jobRunId, start, end);
	}

	/**
	 * Returns an ordered range of all the audit logs where jobRunId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param jobRunId the job run ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByJobRunId(
		String jobRunId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().findByJobRunId(
			jobRunId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the audit logs where jobRunId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param jobRunId the job run ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByJobRunId(
		String jobRunId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByJobRunId(
			jobRunId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first audit log in the ordered set where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByJobRunId_First(
			String jobRunId, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByJobRunId_First(
			jobRunId, orderByComparator);
	}

	/**
	 * Returns the first audit log in the ordered set where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByJobRunId_First(
		String jobRunId, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByJobRunId_First(
			jobRunId, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByJobRunId_Last(
			String jobRunId, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByJobRunId_Last(
			jobRunId, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByJobRunId_Last(
		String jobRunId, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByJobRunId_Last(
			jobRunId, orderByComparator);
	}

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where jobRunId = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param jobRunId the job run ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public static AuditLog[] findByJobRunId_PrevAndNext(
			long auditLogId, String jobRunId,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByJobRunId_PrevAndNext(
			auditLogId, jobRunId, orderByComparator);
	}

	/**
	 * Removes all the audit logs where jobRunId = &#63; from the database.
	 *
	 * @param jobRunId the job run ID
	 */
	public static void removeByJobRunId(String jobRunId) {
		getPersistence().removeByJobRunId(jobRunId);
	}

	/**
	 * Returns the number of audit logs where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @return the number of matching audit logs
	 */
	public static int countByJobRunId(String jobRunId) {
		return getPersistence().countByJobRunId(jobRunId);
	}

	/**
	 * Returns all the audit logs where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @return the matching audit logs
	 */
	public static List<AuditLog> findByRequestId(String requestId) {
		return getPersistence().findByRequestId(requestId);
	}

	/**
	 * Returns a range of all the audit logs where requestId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param requestId the request ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @return the range of matching audit logs
	 */
	public static List<AuditLog> findByRequestId(
		String requestId, int start, int end) {

		return getPersistence().findByRequestId(requestId, start, end);
	}

	/**
	 * Returns an ordered range of all the audit logs where requestId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param requestId the request ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByRequestId(
		String requestId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().findByRequestId(
			requestId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the audit logs where requestId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param requestId the request ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByRequestId(
		String requestId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByRequestId(
			requestId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first audit log in the ordered set where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByRequestId_First(
			String requestId, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByRequestId_First(
			requestId, orderByComparator);
	}

	/**
	 * Returns the first audit log in the ordered set where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByRequestId_First(
		String requestId, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByRequestId_First(
			requestId, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByRequestId_Last(
			String requestId, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByRequestId_Last(
			requestId, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByRequestId_Last(
		String requestId, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByRequestId_Last(
			requestId, orderByComparator);
	}

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where requestId = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param requestId the request ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public static AuditLog[] findByRequestId_PrevAndNext(
			long auditLogId, String requestId,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByRequestId_PrevAndNext(
			auditLogId, requestId, orderByComparator);
	}

	/**
	 * Removes all the audit logs where requestId = &#63; from the database.
	 *
	 * @param requestId the request ID
	 */
	public static void removeByRequestId(String requestId) {
		getPersistence().removeByRequestId(requestId);
	}

	/**
	 * Returns the number of audit logs where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @return the number of matching audit logs
	 */
	public static int countByRequestId(String requestId) {
		return getPersistence().countByRequestId(requestId);
	}

	/**
	 * Returns all the audit logs where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @return the matching audit logs
	 */
	public static List<AuditLog> findByEventId(String eventId) {
		return getPersistence().findByEventId(eventId);
	}

	/**
	 * Returns a range of all the audit logs where eventId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param eventId the event ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @return the range of matching audit logs
	 */
	public static List<AuditLog> findByEventId(
		String eventId, int start, int end) {

		return getPersistence().findByEventId(eventId, start, end);
	}

	/**
	 * Returns an ordered range of all the audit logs where eventId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param eventId the event ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByEventId(
		String eventId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().findByEventId(
			eventId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the audit logs where eventId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param eventId the event ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByEventId(
		String eventId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByEventId(
			eventId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first audit log in the ordered set where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByEventId_First(
			String eventId, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByEventId_First(eventId, orderByComparator);
	}

	/**
	 * Returns the first audit log in the ordered set where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByEventId_First(
		String eventId, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByEventId_First(
			eventId, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByEventId_Last(
			String eventId, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByEventId_Last(eventId, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByEventId_Last(
		String eventId, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByEventId_Last(eventId, orderByComparator);
	}

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where eventId = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param eventId the event ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public static AuditLog[] findByEventId_PrevAndNext(
			long auditLogId, String eventId,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByEventId_PrevAndNext(
			auditLogId, eventId, orderByComparator);
	}

	/**
	 * Removes all the audit logs where eventId = &#63; from the database.
	 *
	 * @param eventId the event ID
	 */
	public static void removeByEventId(String eventId) {
		getPersistence().removeByEventId(eventId);
	}

	/**
	 * Returns the number of audit logs where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @return the number of matching audit logs
	 */
	public static int countByEventId(String eventId) {
		return getPersistence().countByEventId(eventId);
	}

	/**
	 * Returns all the audit logs where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @return the matching audit logs
	 */
	public static List<AuditLog> findByCourseCode(String courseCode) {
		return getPersistence().findByCourseCode(courseCode);
	}

	/**
	 * Returns a range of all the audit logs where courseCode = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @return the range of matching audit logs
	 */
	public static List<AuditLog> findByCourseCode(
		String courseCode, int start, int end) {

		return getPersistence().findByCourseCode(courseCode, start, end);
	}

	/**
	 * Returns an ordered range of all the audit logs where courseCode = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByCourseCode(
		String courseCode, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().findByCourseCode(
			courseCode, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the audit logs where courseCode = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByCourseCode(
		String courseCode, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByCourseCode(
			courseCode, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first audit log in the ordered set where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByCourseCode_First(
			String courseCode, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCourseCode_First(
			courseCode, orderByComparator);
	}

	/**
	 * Returns the first audit log in the ordered set where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByCourseCode_First(
		String courseCode, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByCourseCode_First(
			courseCode, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByCourseCode_Last(
			String courseCode, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCourseCode_Last(
			courseCode, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByCourseCode_Last(
		String courseCode, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByCourseCode_Last(
			courseCode, orderByComparator);
	}

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where courseCode = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param courseCode the course code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public static AuditLog[] findByCourseCode_PrevAndNext(
			long auditLogId, String courseCode,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCourseCode_PrevAndNext(
			auditLogId, courseCode, orderByComparator);
	}

	/**
	 * Removes all the audit logs where courseCode = &#63; from the database.
	 *
	 * @param courseCode the course code
	 */
	public static void removeByCourseCode(String courseCode) {
		getPersistence().removeByCourseCode(courseCode);
	}

	/**
	 * Returns the number of audit logs where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @return the number of matching audit logs
	 */
	public static int countByCourseCode(String courseCode) {
		return getPersistence().countByCourseCode(courseCode);
	}

	/**
	 * Returns all the audit logs where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @return the matching audit logs
	 */
	public static List<AuditLog> findByNtucDTId(long ntucDTId) {
		return getPersistence().findByNtucDTId(ntucDTId);
	}

	/**
	 * Returns a range of all the audit logs where ntucDTId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @return the range of matching audit logs
	 */
	public static List<AuditLog> findByNtucDTId(
		long ntucDTId, int start, int end) {

		return getPersistence().findByNtucDTId(ntucDTId, start, end);
	}

	/**
	 * Returns an ordered range of all the audit logs where ntucDTId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByNtucDTId(
		long ntucDTId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().findByNtucDTId(
			ntucDTId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the audit logs where ntucDTId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByNtucDTId(
		long ntucDTId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByNtucDTId(
			ntucDTId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first audit log in the ordered set where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByNtucDTId_First(
			long ntucDTId, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByNtucDTId_First(
			ntucDTId, orderByComparator);
	}

	/**
	 * Returns the first audit log in the ordered set where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByNtucDTId_First(
		long ntucDTId, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByNtucDTId_First(
			ntucDTId, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByNtucDTId_Last(
			long ntucDTId, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByNtucDTId_Last(
			ntucDTId, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByNtucDTId_Last(
		long ntucDTId, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByNtucDTId_Last(
			ntucDTId, orderByComparator);
	}

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where ntucDTId = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param ntucDTId the ntuc dt ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public static AuditLog[] findByNtucDTId_PrevAndNext(
			long auditLogId, long ntucDTId,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByNtucDTId_PrevAndNext(
			auditLogId, ntucDTId, orderByComparator);
	}

	/**
	 * Removes all the audit logs where ntucDTId = &#63; from the database.
	 *
	 * @param ntucDTId the ntuc dt ID
	 */
	public static void removeByNtucDTId(long ntucDTId) {
		getPersistence().removeByNtucDTId(ntucDTId);
	}

	/**
	 * Returns the number of audit logs where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @return the number of matching audit logs
	 */
	public static int countByNtucDTId(long ntucDTId) {
		return getPersistence().countByNtucDTId(ntucDTId);
	}

	/**
	 * Returns all the audit logs where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @return the matching audit logs
	 */
	public static List<AuditLog> findByCategoryStatusSeverity(
		String category, String status, String severity) {

		return getPersistence().findByCategoryStatusSeverity(
			category, status, severity);
	}

	/**
	 * Returns a range of all the audit logs where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @return the range of matching audit logs
	 */
	public static List<AuditLog> findByCategoryStatusSeverity(
		String category, String status, String severity, int start, int end) {

		return getPersistence().findByCategoryStatusSeverity(
			category, status, severity, start, end);
	}

	/**
	 * Returns an ordered range of all the audit logs where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByCategoryStatusSeverity(
		String category, String status, String severity, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().findByCategoryStatusSeverity(
			category, status, severity, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the audit logs where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByCategoryStatusSeverity(
		String category, String status, String severity, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByCategoryStatusSeverity(
			category, status, severity, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first audit log in the ordered set where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByCategoryStatusSeverity_First(
			String category, String status, String severity,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCategoryStatusSeverity_First(
			category, status, severity, orderByComparator);
	}

	/**
	 * Returns the first audit log in the ordered set where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByCategoryStatusSeverity_First(
		String category, String status, String severity,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByCategoryStatusSeverity_First(
			category, status, severity, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByCategoryStatusSeverity_Last(
			String category, String status, String severity,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCategoryStatusSeverity_Last(
			category, status, severity, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByCategoryStatusSeverity_Last(
		String category, String status, String severity,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByCategoryStatusSeverity_Last(
			category, status, severity, orderByComparator);
	}

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public static AuditLog[] findByCategoryStatusSeverity_PrevAndNext(
			long auditLogId, String category, String status, String severity,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCategoryStatusSeverity_PrevAndNext(
			auditLogId, category, status, severity, orderByComparator);
	}

	/**
	 * Removes all the audit logs where category = &#63; and status = &#63; and severity = &#63; from the database.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 */
	public static void removeByCategoryStatusSeverity(
		String category, String status, String severity) {

		getPersistence().removeByCategoryStatusSeverity(
			category, status, severity);
	}

	/**
	 * Returns the number of audit logs where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @return the number of matching audit logs
	 */
	public static int countByCategoryStatusSeverity(
		String category, String status, String severity) {

		return getPersistence().countByCategoryStatusSeverity(
			category, status, severity);
	}

	/**
	 * Returns all the audit logs where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @return the matching audit logs
	 */
	public static List<AuditLog> findByErrorCode(String errorCode) {
		return getPersistence().findByErrorCode(errorCode);
	}

	/**
	 * Returns a range of all the audit logs where errorCode = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param errorCode the error code
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @return the range of matching audit logs
	 */
	public static List<AuditLog> findByErrorCode(
		String errorCode, int start, int end) {

		return getPersistence().findByErrorCode(errorCode, start, end);
	}

	/**
	 * Returns an ordered range of all the audit logs where errorCode = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param errorCode the error code
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByErrorCode(
		String errorCode, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().findByErrorCode(
			errorCode, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the audit logs where errorCode = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param errorCode the error code
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByErrorCode(
		String errorCode, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByErrorCode(
			errorCode, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first audit log in the ordered set where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByErrorCode_First(
			String errorCode, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByErrorCode_First(
			errorCode, orderByComparator);
	}

	/**
	 * Returns the first audit log in the ordered set where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByErrorCode_First(
		String errorCode, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByErrorCode_First(
			errorCode, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByErrorCode_Last(
			String errorCode, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByErrorCode_Last(
			errorCode, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByErrorCode_Last(
		String errorCode, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByErrorCode_Last(
			errorCode, orderByComparator);
	}

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where errorCode = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param errorCode the error code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public static AuditLog[] findByErrorCode_PrevAndNext(
			long auditLogId, String errorCode,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByErrorCode_PrevAndNext(
			auditLogId, errorCode, orderByComparator);
	}

	/**
	 * Removes all the audit logs where errorCode = &#63; from the database.
	 *
	 * @param errorCode the error code
	 */
	public static void removeByErrorCode(String errorCode) {
		getPersistence().removeByErrorCode(errorCode);
	}

	/**
	 * Returns the number of audit logs where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @return the number of matching audit logs
	 */
	public static int countByErrorCode(String errorCode) {
		return getPersistence().countByErrorCode(errorCode);
	}

	/**
	 * Returns all the audit logs where step = &#63;.
	 *
	 * @param step the step
	 * @return the matching audit logs
	 */
	public static List<AuditLog> findByStep(String step) {
		return getPersistence().findByStep(step);
	}

	/**
	 * Returns a range of all the audit logs where step = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param step the step
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @return the range of matching audit logs
	 */
	public static List<AuditLog> findByStep(String step, int start, int end) {
		return getPersistence().findByStep(step, start, end);
	}

	/**
	 * Returns an ordered range of all the audit logs where step = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param step the step
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByStep(
		String step, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().findByStep(step, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the audit logs where step = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param step the step
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByStep(
		String step, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByStep(
			step, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first audit log in the ordered set where step = &#63;.
	 *
	 * @param step the step
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByStep_First(
			String step, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByStep_First(step, orderByComparator);
	}

	/**
	 * Returns the first audit log in the ordered set where step = &#63;.
	 *
	 * @param step the step
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByStep_First(
		String step, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByStep_First(step, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where step = &#63;.
	 *
	 * @param step the step
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByStep_Last(
			String step, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByStep_Last(step, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where step = &#63;.
	 *
	 * @param step the step
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByStep_Last(
		String step, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByStep_Last(step, orderByComparator);
	}

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where step = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param step the step
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public static AuditLog[] findByStep_PrevAndNext(
			long auditLogId, String step,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByStep_PrevAndNext(
			auditLogId, step, orderByComparator);
	}

	/**
	 * Removes all the audit logs where step = &#63; from the database.
	 *
	 * @param step the step
	 */
	public static void removeByStep(String step) {
		getPersistence().removeByStep(step);
	}

	/**
	 * Returns the number of audit logs where step = &#63;.
	 *
	 * @param step the step
	 * @return the number of matching audit logs
	 */
	public static int countByStep(String step) {
		return getPersistence().countByStep(step);
	}

	/**
	 * Returns all the audit logs where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @return the matching audit logs
	 */
	public static List<AuditLog> findByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs) {

		return getPersistence().findByCorrelationIdStartTimeMs(
			correlationId, startTimeMs);
	}

	/**
	 * Returns a range of all the audit logs where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @return the range of matching audit logs
	 */
	public static List<AuditLog> findByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs, int start, int end) {

		return getPersistence().findByCorrelationIdStartTimeMs(
			correlationId, startTimeMs, start, end);
	}

	/**
	 * Returns an ordered range of all the audit logs where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().findByCorrelationIdStartTimeMs(
			correlationId, startTimeMs, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the audit logs where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByCorrelationIdStartTimeMs(
			correlationId, startTimeMs, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first audit log in the ordered set where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByCorrelationIdStartTimeMs_First(
			String correlationId, long startTimeMs,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCorrelationIdStartTimeMs_First(
			correlationId, startTimeMs, orderByComparator);
	}

	/**
	 * Returns the first audit log in the ordered set where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByCorrelationIdStartTimeMs_First(
		String correlationId, long startTimeMs,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByCorrelationIdStartTimeMs_First(
			correlationId, startTimeMs, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByCorrelationIdStartTimeMs_Last(
			String correlationId, long startTimeMs,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCorrelationIdStartTimeMs_Last(
			correlationId, startTimeMs, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByCorrelationIdStartTimeMs_Last(
		String correlationId, long startTimeMs,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByCorrelationIdStartTimeMs_Last(
			correlationId, startTimeMs, orderByComparator);
	}

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public static AuditLog[] findByCorrelationIdStartTimeMs_PrevAndNext(
			long auditLogId, String correlationId, long startTimeMs,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCorrelationIdStartTimeMs_PrevAndNext(
			auditLogId, correlationId, startTimeMs, orderByComparator);
	}

	/**
	 * Removes all the audit logs where correlationId = &#63; and startTimeMs &ge; &#63; from the database.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 */
	public static void removeByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs) {

		getPersistence().removeByCorrelationIdStartTimeMs(
			correlationId, startTimeMs);
	}

	/**
	 * Returns the number of audit logs where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @return the number of matching audit logs
	 */
	public static int countByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs) {

		return getPersistence().countByCorrelationIdStartTimeMs(
			correlationId, startTimeMs);
	}

	/**
	 * Returns all the audit logs where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @return the matching audit logs
	 */
	public static List<AuditLog> findByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs) {

		return getPersistence().findByCategoryStatusStartTimeMs(
			category, status, startTimeMs);
	}

	/**
	 * Returns a range of all the audit logs where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @return the range of matching audit logs
	 */
	public static List<AuditLog> findByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs, int start, int end) {

		return getPersistence().findByCategoryStatusStartTimeMs(
			category, status, startTimeMs, start, end);
	}

	/**
	 * Returns an ordered range of all the audit logs where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().findByCategoryStatusStartTimeMs(
			category, status, startTimeMs, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the audit logs where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog> findByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByCategoryStatusStartTimeMs(
			category, status, startTimeMs, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first audit log in the ordered set where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByCategoryStatusStartTimeMs_First(
			String category, String status, long startTimeMs,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCategoryStatusStartTimeMs_First(
			category, status, startTimeMs, orderByComparator);
	}

	/**
	 * Returns the first audit log in the ordered set where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByCategoryStatusStartTimeMs_First(
		String category, String status, long startTimeMs,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByCategoryStatusStartTimeMs_First(
			category, status, startTimeMs, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog findByCategoryStatusStartTimeMs_Last(
			String category, String status, long startTimeMs,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCategoryStatusStartTimeMs_Last(
			category, status, startTimeMs, orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog fetchByCategoryStatusStartTimeMs_Last(
		String category, String status, long startTimeMs,
		OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().fetchByCategoryStatusStartTimeMs_Last(
			category, status, startTimeMs, orderByComparator);
	}

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public static AuditLog[] findByCategoryStatusStartTimeMs_PrevAndNext(
			long auditLogId, String category, String status, long startTimeMs,
			OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByCategoryStatusStartTimeMs_PrevAndNext(
			auditLogId, category, status, startTimeMs, orderByComparator);
	}

	/**
	 * Removes all the audit logs where category = &#63; and status = &#63; and startTimeMs &ge; &#63; from the database.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 */
	public static void removeByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs) {

		getPersistence().removeByCategoryStatusStartTimeMs(
			category, status, startTimeMs);
	}

	/**
	 * Returns the number of audit logs where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @return the number of matching audit logs
	 */
	public static int countByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs) {

		return getPersistence().countByCategoryStatusStartTimeMs(
			category, status, startTimeMs);
	}

	/**
	 * Returns all the audit logs where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 * @return the matching audit logs
	 */
	public static List<AuditLog>
		findByCompanyCategoryAlertFingerprintStartTimeMs(
			long companyId, String category, String alertFingerprint,
			long startTimeMs) {

		return getPersistence().
			findByCompanyCategoryAlertFingerprintStartTimeMs(
				companyId, category, alertFingerprint, startTimeMs);
	}

	/**
	 * Returns a range of all the audit logs where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @return the range of matching audit logs
	 */
	public static List<AuditLog>
		findByCompanyCategoryAlertFingerprintStartTimeMs(
			long companyId, String category, String alertFingerprint,
			long startTimeMs, int start, int end) {

		return getPersistence().
			findByCompanyCategoryAlertFingerprintStartTimeMs(
				companyId, category, alertFingerprint, startTimeMs, start, end);
	}

	/**
	 * Returns an ordered range of all the audit logs where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog>
		findByCompanyCategoryAlertFingerprintStartTimeMs(
			long companyId, String category, String alertFingerprint,
			long startTimeMs, int start, int end,
			OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().
			findByCompanyCategoryAlertFingerprintStartTimeMs(
				companyId, category, alertFingerprint, startTimeMs, start, end,
				orderByComparator);
	}

	/**
	 * Returns an ordered range of all the audit logs where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching audit logs
	 */
	public static List<AuditLog>
		findByCompanyCategoryAlertFingerprintStartTimeMs(
			long companyId, String category, String alertFingerprint,
			long startTimeMs, int start, int end,
			OrderByComparator<AuditLog> orderByComparator,
			boolean useFinderCache) {

		return getPersistence().
			findByCompanyCategoryAlertFingerprintStartTimeMs(
				companyId, category, alertFingerprint, startTimeMs, start, end,
				orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first audit log in the ordered set where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog
			findByCompanyCategoryAlertFingerprintStartTimeMs_First(
				long companyId, String category, String alertFingerprint,
				long startTimeMs, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().
			findByCompanyCategoryAlertFingerprintStartTimeMs_First(
				companyId, category, alertFingerprint, startTimeMs,
				orderByComparator);
	}

	/**
	 * Returns the first audit log in the ordered set where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog
		fetchByCompanyCategoryAlertFingerprintStartTimeMs_First(
			long companyId, String category, String alertFingerprint,
			long startTimeMs, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().
			fetchByCompanyCategoryAlertFingerprintStartTimeMs_First(
				companyId, category, alertFingerprint, startTimeMs,
				orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public static AuditLog
			findByCompanyCategoryAlertFingerprintStartTimeMs_Last(
				long companyId, String category, String alertFingerprint,
				long startTimeMs, OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().
			findByCompanyCategoryAlertFingerprintStartTimeMs_Last(
				companyId, category, alertFingerprint, startTimeMs,
				orderByComparator);
	}

	/**
	 * Returns the last audit log in the ordered set where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public static AuditLog
		fetchByCompanyCategoryAlertFingerprintStartTimeMs_Last(
			long companyId, String category, String alertFingerprint,
			long startTimeMs, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().
			fetchByCompanyCategoryAlertFingerprintStartTimeMs_Last(
				companyId, category, alertFingerprint, startTimeMs,
				orderByComparator);
	}

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public static AuditLog[]
			findByCompanyCategoryAlertFingerprintStartTimeMs_PrevAndNext(
				long auditLogId, long companyId, String category,
				String alertFingerprint, long startTimeMs,
				OrderByComparator<AuditLog> orderByComparator)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().
			findByCompanyCategoryAlertFingerprintStartTimeMs_PrevAndNext(
				auditLogId, companyId, category, alertFingerprint, startTimeMs,
				orderByComparator);
	}

	/**
	 * Removes all the audit logs where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 */
	public static void removeByCompanyCategoryAlertFingerprintStartTimeMs(
		long companyId, String category, String alertFingerprint,
		long startTimeMs) {

		getPersistence().removeByCompanyCategoryAlertFingerprintStartTimeMs(
			companyId, category, alertFingerprint, startTimeMs);
	}

	/**
	 * Returns the number of audit logs where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 * @return the number of matching audit logs
	 */
	public static int countByCompanyCategoryAlertFingerprintStartTimeMs(
		long companyId, String category, String alertFingerprint,
		long startTimeMs) {

		return getPersistence().
			countByCompanyCategoryAlertFingerprintStartTimeMs(
				companyId, category, alertFingerprint, startTimeMs);
	}

	/**
	 * Caches the audit log in the entity cache if it is enabled.
	 *
	 * @param auditLog the audit log
	 */
	public static void cacheResult(AuditLog auditLog) {
		getPersistence().cacheResult(auditLog);
	}

	/**
	 * Caches the audit logs in the entity cache if it is enabled.
	 *
	 * @param auditLogs the audit logs
	 */
	public static void cacheResult(List<AuditLog> auditLogs) {
		getPersistence().cacheResult(auditLogs);
	}

	/**
	 * Creates a new audit log with the primary key. Does not add the audit log to the database.
	 *
	 * @param auditLogId the primary key for the new audit log
	 * @return the new audit log
	 */
	public static AuditLog create(long auditLogId) {
		return getPersistence().create(auditLogId);
	}

	/**
	 * Removes the audit log with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param auditLogId the primary key of the audit log
	 * @return the audit log that was removed
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public static AuditLog remove(long auditLogId)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().remove(auditLogId);
	}

	public static AuditLog updateImpl(AuditLog auditLog) {
		return getPersistence().updateImpl(auditLog);
	}

	/**
	 * Returns the audit log with the primary key or throws a <code>NoSuchAuditLogException</code> if it could not be found.
	 *
	 * @param auditLogId the primary key of the audit log
	 * @return the audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public static AuditLog findByPrimaryKey(long auditLogId)
		throws com.ntuc.notification.exception.NoSuchAuditLogException {

		return getPersistence().findByPrimaryKey(auditLogId);
	}

	/**
	 * Returns the audit log with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param auditLogId the primary key of the audit log
	 * @return the audit log, or <code>null</code> if a audit log with the primary key could not be found
	 */
	public static AuditLog fetchByPrimaryKey(long auditLogId) {
		return getPersistence().fetchByPrimaryKey(auditLogId);
	}

	/**
	 * Returns all the audit logs.
	 *
	 * @return the audit logs
	 */
	public static List<AuditLog> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the audit logs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @return the range of audit logs
	 */
	public static List<AuditLog> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the audit logs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of audit logs
	 */
	public static List<AuditLog> findAll(
		int start, int end, OrderByComparator<AuditLog> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the audit logs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AuditLogModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of audit logs
	 * @param end the upper bound of the range of audit logs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of audit logs
	 */
	public static List<AuditLog> findAll(
		int start, int end, OrderByComparator<AuditLog> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the audit logs from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of audit logs.
	 *
	 * @return the number of audit logs
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static AuditLogPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<AuditLogPersistence, AuditLogPersistence>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(AuditLogPersistence.class);

		ServiceTracker<AuditLogPersistence, AuditLogPersistence>
			serviceTracker =
				new ServiceTracker<AuditLogPersistence, AuditLogPersistence>(
					bundle.getBundleContext(), AuditLogPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}