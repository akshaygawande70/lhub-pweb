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

import com.liferay.portal.kernel.service.persistence.BasePersistence;

import com.ntuc.notification.exception.NoSuchAuditLogException;
import com.ntuc.notification.model.AuditLog;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the audit log service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AuditLogUtil
 * @generated
 */
@ProviderType
public interface AuditLogPersistence extends BasePersistence<AuditLog> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link AuditLogUtil} to access the audit log persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the audit logs where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @return the matching audit logs
	 */
	public java.util.List<AuditLog> findByCorrelationId(String correlationId);

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
	public java.util.List<AuditLog> findByCorrelationId(
		String correlationId, int start, int end);

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
	public java.util.List<AuditLog> findByCorrelationId(
		String correlationId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public java.util.List<AuditLog> findByCorrelationId(
		String correlationId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first audit log in the ordered set where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByCorrelationId_First(
			String correlationId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the first audit log in the ordered set where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByCorrelationId_First(
		String correlationId,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the last audit log in the ordered set where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByCorrelationId_Last(
			String correlationId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the last audit log in the ordered set where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByCorrelationId_Last(
		String correlationId,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where correlationId = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param correlationId the correlation ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public AuditLog[] findByCorrelationId_PrevAndNext(
			long auditLogId, String correlationId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Removes all the audit logs where correlationId = &#63; from the database.
	 *
	 * @param correlationId the correlation ID
	 */
	public void removeByCorrelationId(String correlationId);

	/**
	 * Returns the number of audit logs where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @return the number of matching audit logs
	 */
	public int countByCorrelationId(String correlationId);

	/**
	 * Returns all the audit logs where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @return the matching audit logs
	 */
	public java.util.List<AuditLog> findByJobRunId(String jobRunId);

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
	public java.util.List<AuditLog> findByJobRunId(
		String jobRunId, int start, int end);

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
	public java.util.List<AuditLog> findByJobRunId(
		String jobRunId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public java.util.List<AuditLog> findByJobRunId(
		String jobRunId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first audit log in the ordered set where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByJobRunId_First(
			String jobRunId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the first audit log in the ordered set where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByJobRunId_First(
		String jobRunId,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the last audit log in the ordered set where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByJobRunId_Last(
			String jobRunId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the last audit log in the ordered set where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByJobRunId_Last(
		String jobRunId,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where jobRunId = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param jobRunId the job run ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public AuditLog[] findByJobRunId_PrevAndNext(
			long auditLogId, String jobRunId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Removes all the audit logs where jobRunId = &#63; from the database.
	 *
	 * @param jobRunId the job run ID
	 */
	public void removeByJobRunId(String jobRunId);

	/**
	 * Returns the number of audit logs where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @return the number of matching audit logs
	 */
	public int countByJobRunId(String jobRunId);

	/**
	 * Returns all the audit logs where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @return the matching audit logs
	 */
	public java.util.List<AuditLog> findByRequestId(String requestId);

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
	public java.util.List<AuditLog> findByRequestId(
		String requestId, int start, int end);

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
	public java.util.List<AuditLog> findByRequestId(
		String requestId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public java.util.List<AuditLog> findByRequestId(
		String requestId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first audit log in the ordered set where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByRequestId_First(
			String requestId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the first audit log in the ordered set where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByRequestId_First(
		String requestId,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the last audit log in the ordered set where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByRequestId_Last(
			String requestId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the last audit log in the ordered set where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByRequestId_Last(
		String requestId,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where requestId = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param requestId the request ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public AuditLog[] findByRequestId_PrevAndNext(
			long auditLogId, String requestId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Removes all the audit logs where requestId = &#63; from the database.
	 *
	 * @param requestId the request ID
	 */
	public void removeByRequestId(String requestId);

	/**
	 * Returns the number of audit logs where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @return the number of matching audit logs
	 */
	public int countByRequestId(String requestId);

	/**
	 * Returns all the audit logs where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @return the matching audit logs
	 */
	public java.util.List<AuditLog> findByEventId(String eventId);

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
	public java.util.List<AuditLog> findByEventId(
		String eventId, int start, int end);

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
	public java.util.List<AuditLog> findByEventId(
		String eventId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public java.util.List<AuditLog> findByEventId(
		String eventId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first audit log in the ordered set where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByEventId_First(
			String eventId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the first audit log in the ordered set where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByEventId_First(
		String eventId,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the last audit log in the ordered set where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByEventId_Last(
			String eventId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the last audit log in the ordered set where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByEventId_Last(
		String eventId,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where eventId = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param eventId the event ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public AuditLog[] findByEventId_PrevAndNext(
			long auditLogId, String eventId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Removes all the audit logs where eventId = &#63; from the database.
	 *
	 * @param eventId the event ID
	 */
	public void removeByEventId(String eventId);

	/**
	 * Returns the number of audit logs where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @return the number of matching audit logs
	 */
	public int countByEventId(String eventId);

	/**
	 * Returns all the audit logs where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @return the matching audit logs
	 */
	public java.util.List<AuditLog> findByCourseCode(String courseCode);

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
	public java.util.List<AuditLog> findByCourseCode(
		String courseCode, int start, int end);

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
	public java.util.List<AuditLog> findByCourseCode(
		String courseCode, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public java.util.List<AuditLog> findByCourseCode(
		String courseCode, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first audit log in the ordered set where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByCourseCode_First(
			String courseCode,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the first audit log in the ordered set where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByCourseCode_First(
		String courseCode,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the last audit log in the ordered set where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByCourseCode_Last(
			String courseCode,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the last audit log in the ordered set where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByCourseCode_Last(
		String courseCode,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where courseCode = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param courseCode the course code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public AuditLog[] findByCourseCode_PrevAndNext(
			long auditLogId, String courseCode,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Removes all the audit logs where courseCode = &#63; from the database.
	 *
	 * @param courseCode the course code
	 */
	public void removeByCourseCode(String courseCode);

	/**
	 * Returns the number of audit logs where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @return the number of matching audit logs
	 */
	public int countByCourseCode(String courseCode);

	/**
	 * Returns all the audit logs where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @return the matching audit logs
	 */
	public java.util.List<AuditLog> findByNtucDTId(long ntucDTId);

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
	public java.util.List<AuditLog> findByNtucDTId(
		long ntucDTId, int start, int end);

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
	public java.util.List<AuditLog> findByNtucDTId(
		long ntucDTId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public java.util.List<AuditLog> findByNtucDTId(
		long ntucDTId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first audit log in the ordered set where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByNtucDTId_First(
			long ntucDTId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the first audit log in the ordered set where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByNtucDTId_First(
		long ntucDTId,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the last audit log in the ordered set where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByNtucDTId_Last(
			long ntucDTId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the last audit log in the ordered set where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByNtucDTId_Last(
		long ntucDTId,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where ntucDTId = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param ntucDTId the ntuc dt ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public AuditLog[] findByNtucDTId_PrevAndNext(
			long auditLogId, long ntucDTId,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Removes all the audit logs where ntucDTId = &#63; from the database.
	 *
	 * @param ntucDTId the ntuc dt ID
	 */
	public void removeByNtucDTId(long ntucDTId);

	/**
	 * Returns the number of audit logs where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @return the number of matching audit logs
	 */
	public int countByNtucDTId(long ntucDTId);

	/**
	 * Returns all the audit logs where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @return the matching audit logs
	 */
	public java.util.List<AuditLog> findByCategoryStatusSeverity(
		String category, String status, String severity);

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
	public java.util.List<AuditLog> findByCategoryStatusSeverity(
		String category, String status, String severity, int start, int end);

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
	public java.util.List<AuditLog> findByCategoryStatusSeverity(
		String category, String status, String severity, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public java.util.List<AuditLog> findByCategoryStatusSeverity(
		String category, String status, String severity, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator,
		boolean useFinderCache);

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
	public AuditLog findByCategoryStatusSeverity_First(
			String category, String status, String severity,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the first audit log in the ordered set where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByCategoryStatusSeverity_First(
		String category, String status, String severity,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public AuditLog findByCategoryStatusSeverity_Last(
			String category, String status, String severity,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the last audit log in the ordered set where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByCategoryStatusSeverity_Last(
		String category, String status, String severity,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public AuditLog[] findByCategoryStatusSeverity_PrevAndNext(
			long auditLogId, String category, String status, String severity,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Removes all the audit logs where category = &#63; and status = &#63; and severity = &#63; from the database.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 */
	public void removeByCategoryStatusSeverity(
		String category, String status, String severity);

	/**
	 * Returns the number of audit logs where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @return the number of matching audit logs
	 */
	public int countByCategoryStatusSeverity(
		String category, String status, String severity);

	/**
	 * Returns all the audit logs where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @return the matching audit logs
	 */
	public java.util.List<AuditLog> findByErrorCode(String errorCode);

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
	public java.util.List<AuditLog> findByErrorCode(
		String errorCode, int start, int end);

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
	public java.util.List<AuditLog> findByErrorCode(
		String errorCode, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public java.util.List<AuditLog> findByErrorCode(
		String errorCode, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first audit log in the ordered set where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByErrorCode_First(
			String errorCode,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the first audit log in the ordered set where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByErrorCode_First(
		String errorCode,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the last audit log in the ordered set where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByErrorCode_Last(
			String errorCode,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the last audit log in the ordered set where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByErrorCode_Last(
		String errorCode,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where errorCode = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param errorCode the error code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public AuditLog[] findByErrorCode_PrevAndNext(
			long auditLogId, String errorCode,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Removes all the audit logs where errorCode = &#63; from the database.
	 *
	 * @param errorCode the error code
	 */
	public void removeByErrorCode(String errorCode);

	/**
	 * Returns the number of audit logs where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @return the number of matching audit logs
	 */
	public int countByErrorCode(String errorCode);

	/**
	 * Returns all the audit logs where step = &#63;.
	 *
	 * @param step the step
	 * @return the matching audit logs
	 */
	public java.util.List<AuditLog> findByStep(String step);

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
	public java.util.List<AuditLog> findByStep(String step, int start, int end);

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
	public java.util.List<AuditLog> findByStep(
		String step, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public java.util.List<AuditLog> findByStep(
		String step, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first audit log in the ordered set where step = &#63;.
	 *
	 * @param step the step
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByStep_First(
			String step,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the first audit log in the ordered set where step = &#63;.
	 *
	 * @param step the step
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByStep_First(
		String step,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the last audit log in the ordered set where step = &#63;.
	 *
	 * @param step the step
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByStep_Last(
			String step,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the last audit log in the ordered set where step = &#63;.
	 *
	 * @param step the step
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByStep_Last(
		String step,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the audit logs before and after the current audit log in the ordered set where step = &#63;.
	 *
	 * @param auditLogId the primary key of the current audit log
	 * @param step the step
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public AuditLog[] findByStep_PrevAndNext(
			long auditLogId, String step,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Removes all the audit logs where step = &#63; from the database.
	 *
	 * @param step the step
	 */
	public void removeByStep(String step);

	/**
	 * Returns the number of audit logs where step = &#63;.
	 *
	 * @param step the step
	 * @return the number of matching audit logs
	 */
	public int countByStep(String step);

	/**
	 * Returns all the audit logs where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @return the matching audit logs
	 */
	public java.util.List<AuditLog> findByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs);

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
	public java.util.List<AuditLog> findByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs, int start, int end);

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
	public java.util.List<AuditLog> findByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public java.util.List<AuditLog> findByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first audit log in the ordered set where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByCorrelationIdStartTimeMs_First(
			String correlationId, long startTimeMs,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the first audit log in the ordered set where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByCorrelationIdStartTimeMs_First(
		String correlationId, long startTimeMs,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

	/**
	 * Returns the last audit log in the ordered set where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	public AuditLog findByCorrelationIdStartTimeMs_Last(
			String correlationId, long startTimeMs,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the last audit log in the ordered set where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByCorrelationIdStartTimeMs_Last(
		String correlationId, long startTimeMs,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public AuditLog[] findByCorrelationIdStartTimeMs_PrevAndNext(
			long auditLogId, String correlationId, long startTimeMs,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Removes all the audit logs where correlationId = &#63; and startTimeMs &ge; &#63; from the database.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 */
	public void removeByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs);

	/**
	 * Returns the number of audit logs where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @return the number of matching audit logs
	 */
	public int countByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs);

	/**
	 * Returns all the audit logs where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @return the matching audit logs
	 */
	public java.util.List<AuditLog> findByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs);

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
	public java.util.List<AuditLog> findByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs, int start, int end);

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
	public java.util.List<AuditLog> findByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public java.util.List<AuditLog> findByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator,
		boolean useFinderCache);

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
	public AuditLog findByCategoryStatusStartTimeMs_First(
			String category, String status, long startTimeMs,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the first audit log in the ordered set where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByCategoryStatusStartTimeMs_First(
		String category, String status, long startTimeMs,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public AuditLog findByCategoryStatusStartTimeMs_Last(
			String category, String status, long startTimeMs,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Returns the last audit log in the ordered set where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	public AuditLog fetchByCategoryStatusStartTimeMs_Last(
		String category, String status, long startTimeMs,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public AuditLog[] findByCategoryStatusStartTimeMs_PrevAndNext(
			long auditLogId, String category, String status, long startTimeMs,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Removes all the audit logs where category = &#63; and status = &#63; and startTimeMs &ge; &#63; from the database.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 */
	public void removeByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs);

	/**
	 * Returns the number of audit logs where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @return the number of matching audit logs
	 */
	public int countByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs);

	/**
	 * Returns all the audit logs where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 * @return the matching audit logs
	 */
	public java.util.List<AuditLog>
		findByCompanyCategoryAlertFingerprintStartTimeMs(
			long companyId, String category, String alertFingerprint,
			long startTimeMs);

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
	public java.util.List<AuditLog>
		findByCompanyCategoryAlertFingerprintStartTimeMs(
			long companyId, String category, String alertFingerprint,
			long startTimeMs, int start, int end);

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
	public java.util.List<AuditLog>
		findByCompanyCategoryAlertFingerprintStartTimeMs(
			long companyId, String category, String alertFingerprint,
			long startTimeMs, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator);

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
	public java.util.List<AuditLog>
		findByCompanyCategoryAlertFingerprintStartTimeMs(
			long companyId, String category, String alertFingerprint,
			long startTimeMs, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator,
			boolean useFinderCache);

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
	public AuditLog findByCompanyCategoryAlertFingerprintStartTimeMs_First(
			long companyId, String category, String alertFingerprint,
			long startTimeMs,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

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
	public AuditLog fetchByCompanyCategoryAlertFingerprintStartTimeMs_First(
		long companyId, String category, String alertFingerprint,
		long startTimeMs,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public AuditLog findByCompanyCategoryAlertFingerprintStartTimeMs_Last(
			long companyId, String category, String alertFingerprint,
			long startTimeMs,
			com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
				orderByComparator)
		throws NoSuchAuditLogException;

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
	public AuditLog fetchByCompanyCategoryAlertFingerprintStartTimeMs_Last(
		long companyId, String category, String alertFingerprint,
		long startTimeMs,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public AuditLog[]
			findByCompanyCategoryAlertFingerprintStartTimeMs_PrevAndNext(
				long auditLogId, long companyId, String category,
				String alertFingerprint, long startTimeMs,
				com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
					orderByComparator)
		throws NoSuchAuditLogException;

	/**
	 * Removes all the audit logs where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 */
	public void removeByCompanyCategoryAlertFingerprintStartTimeMs(
		long companyId, String category, String alertFingerprint,
		long startTimeMs);

	/**
	 * Returns the number of audit logs where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 * @return the number of matching audit logs
	 */
	public int countByCompanyCategoryAlertFingerprintStartTimeMs(
		long companyId, String category, String alertFingerprint,
		long startTimeMs);

	/**
	 * Caches the audit log in the entity cache if it is enabled.
	 *
	 * @param auditLog the audit log
	 */
	public void cacheResult(AuditLog auditLog);

	/**
	 * Caches the audit logs in the entity cache if it is enabled.
	 *
	 * @param auditLogs the audit logs
	 */
	public void cacheResult(java.util.List<AuditLog> auditLogs);

	/**
	 * Creates a new audit log with the primary key. Does not add the audit log to the database.
	 *
	 * @param auditLogId the primary key for the new audit log
	 * @return the new audit log
	 */
	public AuditLog create(long auditLogId);

	/**
	 * Removes the audit log with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param auditLogId the primary key of the audit log
	 * @return the audit log that was removed
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public AuditLog remove(long auditLogId) throws NoSuchAuditLogException;

	public AuditLog updateImpl(AuditLog auditLog);

	/**
	 * Returns the audit log with the primary key or throws a <code>NoSuchAuditLogException</code> if it could not be found.
	 *
	 * @param auditLogId the primary key of the audit log
	 * @return the audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	public AuditLog findByPrimaryKey(long auditLogId)
		throws NoSuchAuditLogException;

	/**
	 * Returns the audit log with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param auditLogId the primary key of the audit log
	 * @return the audit log, or <code>null</code> if a audit log with the primary key could not be found
	 */
	public AuditLog fetchByPrimaryKey(long auditLogId);

	/**
	 * Returns all the audit logs.
	 *
	 * @return the audit logs
	 */
	public java.util.List<AuditLog> findAll();

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
	public java.util.List<AuditLog> findAll(int start, int end);

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
	public java.util.List<AuditLog> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator);

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
	public java.util.List<AuditLog> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AuditLog>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the audit logs from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of audit logs.
	 *
	 * @return the number of audit logs
	 */
	public int countAll();

}