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

package com.ntuc.notification.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;

import com.ntuc.notification.exception.NoSuchAuditLogException;
import com.ntuc.notification.model.AuditLog;
import com.ntuc.notification.model.impl.AuditLogImpl;
import com.ntuc.notification.model.impl.AuditLogModelImpl;
import com.ntuc.notification.service.persistence.AuditLogPersistence;
import com.ntuc.notification.service.persistence.impl.constants.ntucnicesbdigitalPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the audit log service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = AuditLogPersistence.class)
public class AuditLogPersistenceImpl
	extends BasePersistenceImpl<AuditLog> implements AuditLogPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>AuditLogUtil</code> to access the audit log persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		AuditLogImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByCorrelationId;
	private FinderPath _finderPathWithoutPaginationFindByCorrelationId;
	private FinderPath _finderPathCountByCorrelationId;

	/**
	 * Returns all the audit logs where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @return the matching audit logs
	 */
	@Override
	public List<AuditLog> findByCorrelationId(String correlationId) {
		return findByCorrelationId(
			correlationId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<AuditLog> findByCorrelationId(
		String correlationId, int start, int end) {

		return findByCorrelationId(correlationId, start, end, null);
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
	@Override
	public List<AuditLog> findByCorrelationId(
		String correlationId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return findByCorrelationId(
			correlationId, start, end, orderByComparator, true);
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
	@Override
	public List<AuditLog> findByCorrelationId(
		String correlationId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		correlationId = Objects.toString(correlationId, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByCorrelationId;
				finderArgs = new Object[] {correlationId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCorrelationId;
			finderArgs = new Object[] {
				correlationId, start, end, orderByComparator
			};
		}

		List<AuditLog> list = null;

		if (useFinderCache) {
			list = (List<AuditLog>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AuditLog auditLog : list) {
					if (!correlationId.equals(auditLog.getCorrelationId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_AUDITLOG_WHERE);

			boolean bindCorrelationId = false;

			if (correlationId.isEmpty()) {
				sb.append(_FINDER_COLUMN_CORRELATIONID_CORRELATIONID_3);
			}
			else {
				bindCorrelationId = true;

				sb.append(_FINDER_COLUMN_CORRELATIONID_CORRELATIONID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindCorrelationId) {
					queryPos.add(correlationId);
				}

				list = (List<AuditLog>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					dummyFinderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first audit log in the ordered set where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByCorrelationId_First(
			String correlationId, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByCorrelationId_First(
			correlationId, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("correlationId=");
		sb.append(correlationId);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the first audit log in the ordered set where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByCorrelationId_First(
		String correlationId, OrderByComparator<AuditLog> orderByComparator) {

		List<AuditLog> list = findByCorrelationId(
			correlationId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last audit log in the ordered set where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByCorrelationId_Last(
			String correlationId, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByCorrelationId_Last(
			correlationId, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("correlationId=");
		sb.append(correlationId);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the last audit log in the ordered set where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByCorrelationId_Last(
		String correlationId, OrderByComparator<AuditLog> orderByComparator) {

		int count = countByCorrelationId(correlationId);

		if (count == 0) {
			return null;
		}

		List<AuditLog> list = findByCorrelationId(
			correlationId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog[] findByCorrelationId_PrevAndNext(
			long auditLogId, String correlationId,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		correlationId = Objects.toString(correlationId, "");

		AuditLog auditLog = findByPrimaryKey(auditLogId);

		Session session = null;

		try {
			session = openSession();

			AuditLog[] array = new AuditLogImpl[3];

			array[0] = getByCorrelationId_PrevAndNext(
				session, auditLog, correlationId, orderByComparator, true);

			array[1] = auditLog;

			array[2] = getByCorrelationId_PrevAndNext(
				session, auditLog, correlationId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AuditLog getByCorrelationId_PrevAndNext(
		Session session, AuditLog auditLog, String correlationId,
		OrderByComparator<AuditLog> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_AUDITLOG_WHERE);

		boolean bindCorrelationId = false;

		if (correlationId.isEmpty()) {
			sb.append(_FINDER_COLUMN_CORRELATIONID_CORRELATIONID_3);
		}
		else {
			bindCorrelationId = true;

			sb.append(_FINDER_COLUMN_CORRELATIONID_CORRELATIONID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindCorrelationId) {
			queryPos.add(correlationId);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(auditLog)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AuditLog> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the audit logs where correlationId = &#63; from the database.
	 *
	 * @param correlationId the correlation ID
	 */
	@Override
	public void removeByCorrelationId(String correlationId) {
		for (AuditLog auditLog :
				findByCorrelationId(
					correlationId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(auditLog);
		}
	}

	/**
	 * Returns the number of audit logs where correlationId = &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @return the number of matching audit logs
	 */
	@Override
	public int countByCorrelationId(String correlationId) {
		correlationId = Objects.toString(correlationId, "");

		FinderPath finderPath = _finderPathCountByCorrelationId;

		Object[] finderArgs = new Object[] {correlationId};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_AUDITLOG_WHERE);

			boolean bindCorrelationId = false;

			if (correlationId.isEmpty()) {
				sb.append(_FINDER_COLUMN_CORRELATIONID_CORRELATIONID_3);
			}
			else {
				bindCorrelationId = true;

				sb.append(_FINDER_COLUMN_CORRELATIONID_CORRELATIONID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindCorrelationId) {
					queryPos.add(correlationId);
				}

				count = (Long)query.uniqueResult();

				dummyFinderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_CORRELATIONID_CORRELATIONID_2 =
		"auditLog.correlationId = ?";

	private static final String _FINDER_COLUMN_CORRELATIONID_CORRELATIONID_3 =
		"(auditLog.correlationId IS NULL OR auditLog.correlationId = '')";

	private FinderPath _finderPathWithPaginationFindByJobRunId;
	private FinderPath _finderPathWithoutPaginationFindByJobRunId;
	private FinderPath _finderPathCountByJobRunId;

	/**
	 * Returns all the audit logs where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @return the matching audit logs
	 */
	@Override
	public List<AuditLog> findByJobRunId(String jobRunId) {
		return findByJobRunId(
			jobRunId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<AuditLog> findByJobRunId(String jobRunId, int start, int end) {
		return findByJobRunId(jobRunId, start, end, null);
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
	@Override
	public List<AuditLog> findByJobRunId(
		String jobRunId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return findByJobRunId(jobRunId, start, end, orderByComparator, true);
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
	@Override
	public List<AuditLog> findByJobRunId(
		String jobRunId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		jobRunId = Objects.toString(jobRunId, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByJobRunId;
				finderArgs = new Object[] {jobRunId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByJobRunId;
			finderArgs = new Object[] {jobRunId, start, end, orderByComparator};
		}

		List<AuditLog> list = null;

		if (useFinderCache) {
			list = (List<AuditLog>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AuditLog auditLog : list) {
					if (!jobRunId.equals(auditLog.getJobRunId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_AUDITLOG_WHERE);

			boolean bindJobRunId = false;

			if (jobRunId.isEmpty()) {
				sb.append(_FINDER_COLUMN_JOBRUNID_JOBRUNID_3);
			}
			else {
				bindJobRunId = true;

				sb.append(_FINDER_COLUMN_JOBRUNID_JOBRUNID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindJobRunId) {
					queryPos.add(jobRunId);
				}

				list = (List<AuditLog>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					dummyFinderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first audit log in the ordered set where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByJobRunId_First(
			String jobRunId, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByJobRunId_First(jobRunId, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("jobRunId=");
		sb.append(jobRunId);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the first audit log in the ordered set where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByJobRunId_First(
		String jobRunId, OrderByComparator<AuditLog> orderByComparator) {

		List<AuditLog> list = findByJobRunId(jobRunId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last audit log in the ordered set where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByJobRunId_Last(
			String jobRunId, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByJobRunId_Last(jobRunId, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("jobRunId=");
		sb.append(jobRunId);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the last audit log in the ordered set where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByJobRunId_Last(
		String jobRunId, OrderByComparator<AuditLog> orderByComparator) {

		int count = countByJobRunId(jobRunId);

		if (count == 0) {
			return null;
		}

		List<AuditLog> list = findByJobRunId(
			jobRunId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog[] findByJobRunId_PrevAndNext(
			long auditLogId, String jobRunId,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		jobRunId = Objects.toString(jobRunId, "");

		AuditLog auditLog = findByPrimaryKey(auditLogId);

		Session session = null;

		try {
			session = openSession();

			AuditLog[] array = new AuditLogImpl[3];

			array[0] = getByJobRunId_PrevAndNext(
				session, auditLog, jobRunId, orderByComparator, true);

			array[1] = auditLog;

			array[2] = getByJobRunId_PrevAndNext(
				session, auditLog, jobRunId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AuditLog getByJobRunId_PrevAndNext(
		Session session, AuditLog auditLog, String jobRunId,
		OrderByComparator<AuditLog> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_AUDITLOG_WHERE);

		boolean bindJobRunId = false;

		if (jobRunId.isEmpty()) {
			sb.append(_FINDER_COLUMN_JOBRUNID_JOBRUNID_3);
		}
		else {
			bindJobRunId = true;

			sb.append(_FINDER_COLUMN_JOBRUNID_JOBRUNID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindJobRunId) {
			queryPos.add(jobRunId);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(auditLog)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AuditLog> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the audit logs where jobRunId = &#63; from the database.
	 *
	 * @param jobRunId the job run ID
	 */
	@Override
	public void removeByJobRunId(String jobRunId) {
		for (AuditLog auditLog :
				findByJobRunId(
					jobRunId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(auditLog);
		}
	}

	/**
	 * Returns the number of audit logs where jobRunId = &#63;.
	 *
	 * @param jobRunId the job run ID
	 * @return the number of matching audit logs
	 */
	@Override
	public int countByJobRunId(String jobRunId) {
		jobRunId = Objects.toString(jobRunId, "");

		FinderPath finderPath = _finderPathCountByJobRunId;

		Object[] finderArgs = new Object[] {jobRunId};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_AUDITLOG_WHERE);

			boolean bindJobRunId = false;

			if (jobRunId.isEmpty()) {
				sb.append(_FINDER_COLUMN_JOBRUNID_JOBRUNID_3);
			}
			else {
				bindJobRunId = true;

				sb.append(_FINDER_COLUMN_JOBRUNID_JOBRUNID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindJobRunId) {
					queryPos.add(jobRunId);
				}

				count = (Long)query.uniqueResult();

				dummyFinderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_JOBRUNID_JOBRUNID_2 =
		"auditLog.jobRunId = ?";

	private static final String _FINDER_COLUMN_JOBRUNID_JOBRUNID_3 =
		"(auditLog.jobRunId IS NULL OR auditLog.jobRunId = '')";

	private FinderPath _finderPathWithPaginationFindByRequestId;
	private FinderPath _finderPathWithoutPaginationFindByRequestId;
	private FinderPath _finderPathCountByRequestId;

	/**
	 * Returns all the audit logs where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @return the matching audit logs
	 */
	@Override
	public List<AuditLog> findByRequestId(String requestId) {
		return findByRequestId(
			requestId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<AuditLog> findByRequestId(
		String requestId, int start, int end) {

		return findByRequestId(requestId, start, end, null);
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
	@Override
	public List<AuditLog> findByRequestId(
		String requestId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return findByRequestId(requestId, start, end, orderByComparator, true);
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
	@Override
	public List<AuditLog> findByRequestId(
		String requestId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		requestId = Objects.toString(requestId, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByRequestId;
				finderArgs = new Object[] {requestId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByRequestId;
			finderArgs = new Object[] {
				requestId, start, end, orderByComparator
			};
		}

		List<AuditLog> list = null;

		if (useFinderCache) {
			list = (List<AuditLog>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AuditLog auditLog : list) {
					if (!requestId.equals(auditLog.getRequestId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_AUDITLOG_WHERE);

			boolean bindRequestId = false;

			if (requestId.isEmpty()) {
				sb.append(_FINDER_COLUMN_REQUESTID_REQUESTID_3);
			}
			else {
				bindRequestId = true;

				sb.append(_FINDER_COLUMN_REQUESTID_REQUESTID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindRequestId) {
					queryPos.add(requestId);
				}

				list = (List<AuditLog>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					dummyFinderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first audit log in the ordered set where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByRequestId_First(
			String requestId, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByRequestId_First(
			requestId, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("requestId=");
		sb.append(requestId);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the first audit log in the ordered set where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByRequestId_First(
		String requestId, OrderByComparator<AuditLog> orderByComparator) {

		List<AuditLog> list = findByRequestId(
			requestId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last audit log in the ordered set where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByRequestId_Last(
			String requestId, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByRequestId_Last(requestId, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("requestId=");
		sb.append(requestId);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the last audit log in the ordered set where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByRequestId_Last(
		String requestId, OrderByComparator<AuditLog> orderByComparator) {

		int count = countByRequestId(requestId);

		if (count == 0) {
			return null;
		}

		List<AuditLog> list = findByRequestId(
			requestId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog[] findByRequestId_PrevAndNext(
			long auditLogId, String requestId,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		requestId = Objects.toString(requestId, "");

		AuditLog auditLog = findByPrimaryKey(auditLogId);

		Session session = null;

		try {
			session = openSession();

			AuditLog[] array = new AuditLogImpl[3];

			array[0] = getByRequestId_PrevAndNext(
				session, auditLog, requestId, orderByComparator, true);

			array[1] = auditLog;

			array[2] = getByRequestId_PrevAndNext(
				session, auditLog, requestId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AuditLog getByRequestId_PrevAndNext(
		Session session, AuditLog auditLog, String requestId,
		OrderByComparator<AuditLog> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_AUDITLOG_WHERE);

		boolean bindRequestId = false;

		if (requestId.isEmpty()) {
			sb.append(_FINDER_COLUMN_REQUESTID_REQUESTID_3);
		}
		else {
			bindRequestId = true;

			sb.append(_FINDER_COLUMN_REQUESTID_REQUESTID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindRequestId) {
			queryPos.add(requestId);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(auditLog)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AuditLog> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the audit logs where requestId = &#63; from the database.
	 *
	 * @param requestId the request ID
	 */
	@Override
	public void removeByRequestId(String requestId) {
		for (AuditLog auditLog :
				findByRequestId(
					requestId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(auditLog);
		}
	}

	/**
	 * Returns the number of audit logs where requestId = &#63;.
	 *
	 * @param requestId the request ID
	 * @return the number of matching audit logs
	 */
	@Override
	public int countByRequestId(String requestId) {
		requestId = Objects.toString(requestId, "");

		FinderPath finderPath = _finderPathCountByRequestId;

		Object[] finderArgs = new Object[] {requestId};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_AUDITLOG_WHERE);

			boolean bindRequestId = false;

			if (requestId.isEmpty()) {
				sb.append(_FINDER_COLUMN_REQUESTID_REQUESTID_3);
			}
			else {
				bindRequestId = true;

				sb.append(_FINDER_COLUMN_REQUESTID_REQUESTID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindRequestId) {
					queryPos.add(requestId);
				}

				count = (Long)query.uniqueResult();

				dummyFinderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_REQUESTID_REQUESTID_2 =
		"auditLog.requestId = ?";

	private static final String _FINDER_COLUMN_REQUESTID_REQUESTID_3 =
		"(auditLog.requestId IS NULL OR auditLog.requestId = '')";

	private FinderPath _finderPathWithPaginationFindByEventId;
	private FinderPath _finderPathWithoutPaginationFindByEventId;
	private FinderPath _finderPathCountByEventId;

	/**
	 * Returns all the audit logs where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @return the matching audit logs
	 */
	@Override
	public List<AuditLog> findByEventId(String eventId) {
		return findByEventId(
			eventId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<AuditLog> findByEventId(String eventId, int start, int end) {
		return findByEventId(eventId, start, end, null);
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
	@Override
	public List<AuditLog> findByEventId(
		String eventId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return findByEventId(eventId, start, end, orderByComparator, true);
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
	@Override
	public List<AuditLog> findByEventId(
		String eventId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		eventId = Objects.toString(eventId, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByEventId;
				finderArgs = new Object[] {eventId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByEventId;
			finderArgs = new Object[] {eventId, start, end, orderByComparator};
		}

		List<AuditLog> list = null;

		if (useFinderCache) {
			list = (List<AuditLog>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AuditLog auditLog : list) {
					if (!eventId.equals(auditLog.getEventId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_AUDITLOG_WHERE);

			boolean bindEventId = false;

			if (eventId.isEmpty()) {
				sb.append(_FINDER_COLUMN_EVENTID_EVENTID_3);
			}
			else {
				bindEventId = true;

				sb.append(_FINDER_COLUMN_EVENTID_EVENTID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindEventId) {
					queryPos.add(eventId);
				}

				list = (List<AuditLog>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					dummyFinderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first audit log in the ordered set where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByEventId_First(
			String eventId, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByEventId_First(eventId, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("eventId=");
		sb.append(eventId);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the first audit log in the ordered set where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByEventId_First(
		String eventId, OrderByComparator<AuditLog> orderByComparator) {

		List<AuditLog> list = findByEventId(eventId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last audit log in the ordered set where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByEventId_Last(
			String eventId, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByEventId_Last(eventId, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("eventId=");
		sb.append(eventId);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the last audit log in the ordered set where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByEventId_Last(
		String eventId, OrderByComparator<AuditLog> orderByComparator) {

		int count = countByEventId(eventId);

		if (count == 0) {
			return null;
		}

		List<AuditLog> list = findByEventId(
			eventId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog[] findByEventId_PrevAndNext(
			long auditLogId, String eventId,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		eventId = Objects.toString(eventId, "");

		AuditLog auditLog = findByPrimaryKey(auditLogId);

		Session session = null;

		try {
			session = openSession();

			AuditLog[] array = new AuditLogImpl[3];

			array[0] = getByEventId_PrevAndNext(
				session, auditLog, eventId, orderByComparator, true);

			array[1] = auditLog;

			array[2] = getByEventId_PrevAndNext(
				session, auditLog, eventId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AuditLog getByEventId_PrevAndNext(
		Session session, AuditLog auditLog, String eventId,
		OrderByComparator<AuditLog> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_AUDITLOG_WHERE);

		boolean bindEventId = false;

		if (eventId.isEmpty()) {
			sb.append(_FINDER_COLUMN_EVENTID_EVENTID_3);
		}
		else {
			bindEventId = true;

			sb.append(_FINDER_COLUMN_EVENTID_EVENTID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindEventId) {
			queryPos.add(eventId);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(auditLog)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AuditLog> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the audit logs where eventId = &#63; from the database.
	 *
	 * @param eventId the event ID
	 */
	@Override
	public void removeByEventId(String eventId) {
		for (AuditLog auditLog :
				findByEventId(
					eventId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(auditLog);
		}
	}

	/**
	 * Returns the number of audit logs where eventId = &#63;.
	 *
	 * @param eventId the event ID
	 * @return the number of matching audit logs
	 */
	@Override
	public int countByEventId(String eventId) {
		eventId = Objects.toString(eventId, "");

		FinderPath finderPath = _finderPathCountByEventId;

		Object[] finderArgs = new Object[] {eventId};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_AUDITLOG_WHERE);

			boolean bindEventId = false;

			if (eventId.isEmpty()) {
				sb.append(_FINDER_COLUMN_EVENTID_EVENTID_3);
			}
			else {
				bindEventId = true;

				sb.append(_FINDER_COLUMN_EVENTID_EVENTID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindEventId) {
					queryPos.add(eventId);
				}

				count = (Long)query.uniqueResult();

				dummyFinderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_EVENTID_EVENTID_2 =
		"auditLog.eventId = ?";

	private static final String _FINDER_COLUMN_EVENTID_EVENTID_3 =
		"(auditLog.eventId IS NULL OR auditLog.eventId = '')";

	private FinderPath _finderPathWithPaginationFindByCourseCode;
	private FinderPath _finderPathWithoutPaginationFindByCourseCode;
	private FinderPath _finderPathCountByCourseCode;

	/**
	 * Returns all the audit logs where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @return the matching audit logs
	 */
	@Override
	public List<AuditLog> findByCourseCode(String courseCode) {
		return findByCourseCode(
			courseCode, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<AuditLog> findByCourseCode(
		String courseCode, int start, int end) {

		return findByCourseCode(courseCode, start, end, null);
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
	@Override
	public List<AuditLog> findByCourseCode(
		String courseCode, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return findByCourseCode(
			courseCode, start, end, orderByComparator, true);
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
	@Override
	public List<AuditLog> findByCourseCode(
		String courseCode, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		courseCode = Objects.toString(courseCode, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByCourseCode;
				finderArgs = new Object[] {courseCode};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCourseCode;
			finderArgs = new Object[] {
				courseCode, start, end, orderByComparator
			};
		}

		List<AuditLog> list = null;

		if (useFinderCache) {
			list = (List<AuditLog>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AuditLog auditLog : list) {
					if (!courseCode.equals(auditLog.getCourseCode())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_AUDITLOG_WHERE);

			boolean bindCourseCode = false;

			if (courseCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODE_COURSECODE_3);
			}
			else {
				bindCourseCode = true;

				sb.append(_FINDER_COLUMN_COURSECODE_COURSECODE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindCourseCode) {
					queryPos.add(courseCode);
				}

				list = (List<AuditLog>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					dummyFinderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first audit log in the ordered set where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByCourseCode_First(
			String courseCode, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByCourseCode_First(
			courseCode, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("courseCode=");
		sb.append(courseCode);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the first audit log in the ordered set where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByCourseCode_First(
		String courseCode, OrderByComparator<AuditLog> orderByComparator) {

		List<AuditLog> list = findByCourseCode(
			courseCode, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last audit log in the ordered set where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByCourseCode_Last(
			String courseCode, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByCourseCode_Last(
			courseCode, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("courseCode=");
		sb.append(courseCode);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the last audit log in the ordered set where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByCourseCode_Last(
		String courseCode, OrderByComparator<AuditLog> orderByComparator) {

		int count = countByCourseCode(courseCode);

		if (count == 0) {
			return null;
		}

		List<AuditLog> list = findByCourseCode(
			courseCode, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog[] findByCourseCode_PrevAndNext(
			long auditLogId, String courseCode,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		courseCode = Objects.toString(courseCode, "");

		AuditLog auditLog = findByPrimaryKey(auditLogId);

		Session session = null;

		try {
			session = openSession();

			AuditLog[] array = new AuditLogImpl[3];

			array[0] = getByCourseCode_PrevAndNext(
				session, auditLog, courseCode, orderByComparator, true);

			array[1] = auditLog;

			array[2] = getByCourseCode_PrevAndNext(
				session, auditLog, courseCode, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AuditLog getByCourseCode_PrevAndNext(
		Session session, AuditLog auditLog, String courseCode,
		OrderByComparator<AuditLog> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_AUDITLOG_WHERE);

		boolean bindCourseCode = false;

		if (courseCode.isEmpty()) {
			sb.append(_FINDER_COLUMN_COURSECODE_COURSECODE_3);
		}
		else {
			bindCourseCode = true;

			sb.append(_FINDER_COLUMN_COURSECODE_COURSECODE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindCourseCode) {
			queryPos.add(courseCode);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(auditLog)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AuditLog> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the audit logs where courseCode = &#63; from the database.
	 *
	 * @param courseCode the course code
	 */
	@Override
	public void removeByCourseCode(String courseCode) {
		for (AuditLog auditLog :
				findByCourseCode(
					courseCode, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(auditLog);
		}
	}

	/**
	 * Returns the number of audit logs where courseCode = &#63;.
	 *
	 * @param courseCode the course code
	 * @return the number of matching audit logs
	 */
	@Override
	public int countByCourseCode(String courseCode) {
		courseCode = Objects.toString(courseCode, "");

		FinderPath finderPath = _finderPathCountByCourseCode;

		Object[] finderArgs = new Object[] {courseCode};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_AUDITLOG_WHERE);

			boolean bindCourseCode = false;

			if (courseCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODE_COURSECODE_3);
			}
			else {
				bindCourseCode = true;

				sb.append(_FINDER_COLUMN_COURSECODE_COURSECODE_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindCourseCode) {
					queryPos.add(courseCode);
				}

				count = (Long)query.uniqueResult();

				dummyFinderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_COURSECODE_COURSECODE_2 =
		"auditLog.courseCode = ?";

	private static final String _FINDER_COLUMN_COURSECODE_COURSECODE_3 =
		"(auditLog.courseCode IS NULL OR auditLog.courseCode = '')";

	private FinderPath _finderPathWithPaginationFindByNtucDTId;
	private FinderPath _finderPathWithoutPaginationFindByNtucDTId;
	private FinderPath _finderPathCountByNtucDTId;

	/**
	 * Returns all the audit logs where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @return the matching audit logs
	 */
	@Override
	public List<AuditLog> findByNtucDTId(long ntucDTId) {
		return findByNtucDTId(
			ntucDTId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<AuditLog> findByNtucDTId(long ntucDTId, int start, int end) {
		return findByNtucDTId(ntucDTId, start, end, null);
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
	@Override
	public List<AuditLog> findByNtucDTId(
		long ntucDTId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return findByNtucDTId(ntucDTId, start, end, orderByComparator, true);
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
	@Override
	public List<AuditLog> findByNtucDTId(
		long ntucDTId, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByNtucDTId;
				finderArgs = new Object[] {ntucDTId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByNtucDTId;
			finderArgs = new Object[] {ntucDTId, start, end, orderByComparator};
		}

		List<AuditLog> list = null;

		if (useFinderCache) {
			list = (List<AuditLog>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AuditLog auditLog : list) {
					if (ntucDTId != auditLog.getNtucDTId()) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_AUDITLOG_WHERE);

			sb.append(_FINDER_COLUMN_NTUCDTID_NTUCDTID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(ntucDTId);

				list = (List<AuditLog>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					dummyFinderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first audit log in the ordered set where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByNtucDTId_First(
			long ntucDTId, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByNtucDTId_First(ntucDTId, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("ntucDTId=");
		sb.append(ntucDTId);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the first audit log in the ordered set where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByNtucDTId_First(
		long ntucDTId, OrderByComparator<AuditLog> orderByComparator) {

		List<AuditLog> list = findByNtucDTId(ntucDTId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last audit log in the ordered set where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByNtucDTId_Last(
			long ntucDTId, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByNtucDTId_Last(ntucDTId, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("ntucDTId=");
		sb.append(ntucDTId);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the last audit log in the ordered set where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByNtucDTId_Last(
		long ntucDTId, OrderByComparator<AuditLog> orderByComparator) {

		int count = countByNtucDTId(ntucDTId);

		if (count == 0) {
			return null;
		}

		List<AuditLog> list = findByNtucDTId(
			ntucDTId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog[] findByNtucDTId_PrevAndNext(
			long auditLogId, long ntucDTId,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = findByPrimaryKey(auditLogId);

		Session session = null;

		try {
			session = openSession();

			AuditLog[] array = new AuditLogImpl[3];

			array[0] = getByNtucDTId_PrevAndNext(
				session, auditLog, ntucDTId, orderByComparator, true);

			array[1] = auditLog;

			array[2] = getByNtucDTId_PrevAndNext(
				session, auditLog, ntucDTId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AuditLog getByNtucDTId_PrevAndNext(
		Session session, AuditLog auditLog, long ntucDTId,
		OrderByComparator<AuditLog> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_AUDITLOG_WHERE);

		sb.append(_FINDER_COLUMN_NTUCDTID_NTUCDTID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(ntucDTId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(auditLog)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AuditLog> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the audit logs where ntucDTId = &#63; from the database.
	 *
	 * @param ntucDTId the ntuc dt ID
	 */
	@Override
	public void removeByNtucDTId(long ntucDTId) {
		for (AuditLog auditLog :
				findByNtucDTId(
					ntucDTId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(auditLog);
		}
	}

	/**
	 * Returns the number of audit logs where ntucDTId = &#63;.
	 *
	 * @param ntucDTId the ntuc dt ID
	 * @return the number of matching audit logs
	 */
	@Override
	public int countByNtucDTId(long ntucDTId) {
		FinderPath finderPath = _finderPathCountByNtucDTId;

		Object[] finderArgs = new Object[] {ntucDTId};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_AUDITLOG_WHERE);

			sb.append(_FINDER_COLUMN_NTUCDTID_NTUCDTID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(ntucDTId);

				count = (Long)query.uniqueResult();

				dummyFinderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_NTUCDTID_NTUCDTID_2 =
		"auditLog.ntucDTId = ?";

	private FinderPath _finderPathWithPaginationFindByCategoryStatusSeverity;
	private FinderPath _finderPathWithoutPaginationFindByCategoryStatusSeverity;
	private FinderPath _finderPathCountByCategoryStatusSeverity;

	/**
	 * Returns all the audit logs where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @return the matching audit logs
	 */
	@Override
	public List<AuditLog> findByCategoryStatusSeverity(
		String category, String status, String severity) {

		return findByCategoryStatusSeverity(
			category, status, severity, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
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
	@Override
	public List<AuditLog> findByCategoryStatusSeverity(
		String category, String status, String severity, int start, int end) {

		return findByCategoryStatusSeverity(
			category, status, severity, start, end, null);
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
	@Override
	public List<AuditLog> findByCategoryStatusSeverity(
		String category, String status, String severity, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return findByCategoryStatusSeverity(
			category, status, severity, start, end, orderByComparator, true);
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
	@Override
	public List<AuditLog> findByCategoryStatusSeverity(
		String category, String status, String severity, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		category = Objects.toString(category, "");
		status = Objects.toString(status, "");
		severity = Objects.toString(severity, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByCategoryStatusSeverity;
				finderArgs = new Object[] {category, status, severity};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCategoryStatusSeverity;
			finderArgs = new Object[] {
				category, status, severity, start, end, orderByComparator
			};
		}

		List<AuditLog> list = null;

		if (useFinderCache) {
			list = (List<AuditLog>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AuditLog auditLog : list) {
					if (!category.equals(auditLog.getCategory()) ||
						!status.equals(auditLog.getStatus()) ||
						!severity.equals(auditLog.getSeverity())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					5 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(5);
			}

			sb.append(_SQL_SELECT_AUDITLOG_WHERE);

			boolean bindCategory = false;

			if (category.isEmpty()) {
				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_CATEGORY_3);
			}
			else {
				bindCategory = true;

				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_CATEGORY_2);
			}

			boolean bindStatus = false;

			if (status.isEmpty()) {
				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_STATUS_3);
			}
			else {
				bindStatus = true;

				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_STATUS_2);
			}

			boolean bindSeverity = false;

			if (severity.isEmpty()) {
				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_SEVERITY_3);
			}
			else {
				bindSeverity = true;

				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_SEVERITY_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindCategory) {
					queryPos.add(category);
				}

				if (bindStatus) {
					queryPos.add(status);
				}

				if (bindSeverity) {
					queryPos.add(severity);
				}

				list = (List<AuditLog>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					dummyFinderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
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
	@Override
	public AuditLog findByCategoryStatusSeverity_First(
			String category, String status, String severity,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByCategoryStatusSeverity_First(
			category, status, severity, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("category=");
		sb.append(category);

		sb.append(", status=");
		sb.append(status);

		sb.append(", severity=");
		sb.append(severity);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
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
	@Override
	public AuditLog fetchByCategoryStatusSeverity_First(
		String category, String status, String severity,
		OrderByComparator<AuditLog> orderByComparator) {

		List<AuditLog> list = findByCategoryStatusSeverity(
			category, status, severity, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog findByCategoryStatusSeverity_Last(
			String category, String status, String severity,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByCategoryStatusSeverity_Last(
			category, status, severity, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("category=");
		sb.append(category);

		sb.append(", status=");
		sb.append(status);

		sb.append(", severity=");
		sb.append(severity);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
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
	@Override
	public AuditLog fetchByCategoryStatusSeverity_Last(
		String category, String status, String severity,
		OrderByComparator<AuditLog> orderByComparator) {

		int count = countByCategoryStatusSeverity(category, status, severity);

		if (count == 0) {
			return null;
		}

		List<AuditLog> list = findByCategoryStatusSeverity(
			category, status, severity, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog[] findByCategoryStatusSeverity_PrevAndNext(
			long auditLogId, String category, String status, String severity,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		category = Objects.toString(category, "");
		status = Objects.toString(status, "");
		severity = Objects.toString(severity, "");

		AuditLog auditLog = findByPrimaryKey(auditLogId);

		Session session = null;

		try {
			session = openSession();

			AuditLog[] array = new AuditLogImpl[3];

			array[0] = getByCategoryStatusSeverity_PrevAndNext(
				session, auditLog, category, status, severity,
				orderByComparator, true);

			array[1] = auditLog;

			array[2] = getByCategoryStatusSeverity_PrevAndNext(
				session, auditLog, category, status, severity,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AuditLog getByCategoryStatusSeverity_PrevAndNext(
		Session session, AuditLog auditLog, String category, String status,
		String severity, OrderByComparator<AuditLog> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		sb.append(_SQL_SELECT_AUDITLOG_WHERE);

		boolean bindCategory = false;

		if (category.isEmpty()) {
			sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_CATEGORY_3);
		}
		else {
			bindCategory = true;

			sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_CATEGORY_2);
		}

		boolean bindStatus = false;

		if (status.isEmpty()) {
			sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_STATUS_3);
		}
		else {
			bindStatus = true;

			sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_STATUS_2);
		}

		boolean bindSeverity = false;

		if (severity.isEmpty()) {
			sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_SEVERITY_3);
		}
		else {
			bindSeverity = true;

			sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_SEVERITY_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindCategory) {
			queryPos.add(category);
		}

		if (bindStatus) {
			queryPos.add(status);
		}

		if (bindSeverity) {
			queryPos.add(severity);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(auditLog)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AuditLog> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the audit logs where category = &#63; and status = &#63; and severity = &#63; from the database.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 */
	@Override
	public void removeByCategoryStatusSeverity(
		String category, String status, String severity) {

		for (AuditLog auditLog :
				findByCategoryStatusSeverity(
					category, status, severity, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(auditLog);
		}
	}

	/**
	 * Returns the number of audit logs where category = &#63; and status = &#63; and severity = &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param severity the severity
	 * @return the number of matching audit logs
	 */
	@Override
	public int countByCategoryStatusSeverity(
		String category, String status, String severity) {

		category = Objects.toString(category, "");
		status = Objects.toString(status, "");
		severity = Objects.toString(severity, "");

		FinderPath finderPath = _finderPathCountByCategoryStatusSeverity;

		Object[] finderArgs = new Object[] {category, status, severity};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_AUDITLOG_WHERE);

			boolean bindCategory = false;

			if (category.isEmpty()) {
				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_CATEGORY_3);
			}
			else {
				bindCategory = true;

				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_CATEGORY_2);
			}

			boolean bindStatus = false;

			if (status.isEmpty()) {
				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_STATUS_3);
			}
			else {
				bindStatus = true;

				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_STATUS_2);
			}

			boolean bindSeverity = false;

			if (severity.isEmpty()) {
				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_SEVERITY_3);
			}
			else {
				bindSeverity = true;

				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_SEVERITY_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindCategory) {
					queryPos.add(category);
				}

				if (bindStatus) {
					queryPos.add(status);
				}

				if (bindSeverity) {
					queryPos.add(severity);
				}

				count = (Long)query.uniqueResult();

				dummyFinderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_CATEGORY_2 =
			"auditLog.category = ? AND ";

	private static final String
		_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_CATEGORY_3 =
			"(auditLog.category IS NULL OR auditLog.category = '') AND ";

	private static final String _FINDER_COLUMN_CATEGORYSTATUSSEVERITY_STATUS_2 =
		"auditLog.status = ? AND ";

	private static final String _FINDER_COLUMN_CATEGORYSTATUSSEVERITY_STATUS_3 =
		"(auditLog.status IS NULL OR auditLog.status = '') AND ";

	private static final String
		_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_SEVERITY_2 =
			"auditLog.severity = ?";

	private static final String
		_FINDER_COLUMN_CATEGORYSTATUSSEVERITY_SEVERITY_3 =
			"(auditLog.severity IS NULL OR auditLog.severity = '')";

	private FinderPath _finderPathWithPaginationFindByErrorCode;
	private FinderPath _finderPathWithoutPaginationFindByErrorCode;
	private FinderPath _finderPathCountByErrorCode;

	/**
	 * Returns all the audit logs where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @return the matching audit logs
	 */
	@Override
	public List<AuditLog> findByErrorCode(String errorCode) {
		return findByErrorCode(
			errorCode, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<AuditLog> findByErrorCode(
		String errorCode, int start, int end) {

		return findByErrorCode(errorCode, start, end, null);
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
	@Override
	public List<AuditLog> findByErrorCode(
		String errorCode, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return findByErrorCode(errorCode, start, end, orderByComparator, true);
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
	@Override
	public List<AuditLog> findByErrorCode(
		String errorCode, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		errorCode = Objects.toString(errorCode, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByErrorCode;
				finderArgs = new Object[] {errorCode};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByErrorCode;
			finderArgs = new Object[] {
				errorCode, start, end, orderByComparator
			};
		}

		List<AuditLog> list = null;

		if (useFinderCache) {
			list = (List<AuditLog>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AuditLog auditLog : list) {
					if (!errorCode.equals(auditLog.getErrorCode())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_AUDITLOG_WHERE);

			boolean bindErrorCode = false;

			if (errorCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_ERRORCODE_ERRORCODE_3);
			}
			else {
				bindErrorCode = true;

				sb.append(_FINDER_COLUMN_ERRORCODE_ERRORCODE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindErrorCode) {
					queryPos.add(errorCode);
				}

				list = (List<AuditLog>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					dummyFinderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first audit log in the ordered set where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByErrorCode_First(
			String errorCode, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByErrorCode_First(
			errorCode, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("errorCode=");
		sb.append(errorCode);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the first audit log in the ordered set where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByErrorCode_First(
		String errorCode, OrderByComparator<AuditLog> orderByComparator) {

		List<AuditLog> list = findByErrorCode(
			errorCode, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last audit log in the ordered set where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByErrorCode_Last(
			String errorCode, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByErrorCode_Last(errorCode, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("errorCode=");
		sb.append(errorCode);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the last audit log in the ordered set where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByErrorCode_Last(
		String errorCode, OrderByComparator<AuditLog> orderByComparator) {

		int count = countByErrorCode(errorCode);

		if (count == 0) {
			return null;
		}

		List<AuditLog> list = findByErrorCode(
			errorCode, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog[] findByErrorCode_PrevAndNext(
			long auditLogId, String errorCode,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		errorCode = Objects.toString(errorCode, "");

		AuditLog auditLog = findByPrimaryKey(auditLogId);

		Session session = null;

		try {
			session = openSession();

			AuditLog[] array = new AuditLogImpl[3];

			array[0] = getByErrorCode_PrevAndNext(
				session, auditLog, errorCode, orderByComparator, true);

			array[1] = auditLog;

			array[2] = getByErrorCode_PrevAndNext(
				session, auditLog, errorCode, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AuditLog getByErrorCode_PrevAndNext(
		Session session, AuditLog auditLog, String errorCode,
		OrderByComparator<AuditLog> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_AUDITLOG_WHERE);

		boolean bindErrorCode = false;

		if (errorCode.isEmpty()) {
			sb.append(_FINDER_COLUMN_ERRORCODE_ERRORCODE_3);
		}
		else {
			bindErrorCode = true;

			sb.append(_FINDER_COLUMN_ERRORCODE_ERRORCODE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindErrorCode) {
			queryPos.add(errorCode);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(auditLog)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AuditLog> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the audit logs where errorCode = &#63; from the database.
	 *
	 * @param errorCode the error code
	 */
	@Override
	public void removeByErrorCode(String errorCode) {
		for (AuditLog auditLog :
				findByErrorCode(
					errorCode, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(auditLog);
		}
	}

	/**
	 * Returns the number of audit logs where errorCode = &#63;.
	 *
	 * @param errorCode the error code
	 * @return the number of matching audit logs
	 */
	@Override
	public int countByErrorCode(String errorCode) {
		errorCode = Objects.toString(errorCode, "");

		FinderPath finderPath = _finderPathCountByErrorCode;

		Object[] finderArgs = new Object[] {errorCode};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_AUDITLOG_WHERE);

			boolean bindErrorCode = false;

			if (errorCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_ERRORCODE_ERRORCODE_3);
			}
			else {
				bindErrorCode = true;

				sb.append(_FINDER_COLUMN_ERRORCODE_ERRORCODE_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindErrorCode) {
					queryPos.add(errorCode);
				}

				count = (Long)query.uniqueResult();

				dummyFinderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_ERRORCODE_ERRORCODE_2 =
		"auditLog.errorCode = ?";

	private static final String _FINDER_COLUMN_ERRORCODE_ERRORCODE_3 =
		"(auditLog.errorCode IS NULL OR auditLog.errorCode = '')";

	private FinderPath _finderPathWithPaginationFindByStep;
	private FinderPath _finderPathWithoutPaginationFindByStep;
	private FinderPath _finderPathCountByStep;

	/**
	 * Returns all the audit logs where step = &#63;.
	 *
	 * @param step the step
	 * @return the matching audit logs
	 */
	@Override
	public List<AuditLog> findByStep(String step) {
		return findByStep(step, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<AuditLog> findByStep(String step, int start, int end) {
		return findByStep(step, start, end, null);
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
	@Override
	public List<AuditLog> findByStep(
		String step, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return findByStep(step, start, end, orderByComparator, true);
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
	@Override
	public List<AuditLog> findByStep(
		String step, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		step = Objects.toString(step, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByStep;
				finderArgs = new Object[] {step};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByStep;
			finderArgs = new Object[] {step, start, end, orderByComparator};
		}

		List<AuditLog> list = null;

		if (useFinderCache) {
			list = (List<AuditLog>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AuditLog auditLog : list) {
					if (!step.equals(auditLog.getStep())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_AUDITLOG_WHERE);

			boolean bindStep = false;

			if (step.isEmpty()) {
				sb.append(_FINDER_COLUMN_STEP_STEP_3);
			}
			else {
				bindStep = true;

				sb.append(_FINDER_COLUMN_STEP_STEP_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindStep) {
					queryPos.add(step);
				}

				list = (List<AuditLog>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					dummyFinderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first audit log in the ordered set where step = &#63;.
	 *
	 * @param step the step
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByStep_First(
			String step, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByStep_First(step, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("step=");
		sb.append(step);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the first audit log in the ordered set where step = &#63;.
	 *
	 * @param step the step
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByStep_First(
		String step, OrderByComparator<AuditLog> orderByComparator) {

		List<AuditLog> list = findByStep(step, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last audit log in the ordered set where step = &#63;.
	 *
	 * @param step the step
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log
	 * @throws NoSuchAuditLogException if a matching audit log could not be found
	 */
	@Override
	public AuditLog findByStep_Last(
			String step, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByStep_Last(step, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("step=");
		sb.append(step);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the last audit log in the ordered set where step = &#63;.
	 *
	 * @param step the step
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByStep_Last(
		String step, OrderByComparator<AuditLog> orderByComparator) {

		int count = countByStep(step);

		if (count == 0) {
			return null;
		}

		List<AuditLog> list = findByStep(
			step, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog[] findByStep_PrevAndNext(
			long auditLogId, String step,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		step = Objects.toString(step, "");

		AuditLog auditLog = findByPrimaryKey(auditLogId);

		Session session = null;

		try {
			session = openSession();

			AuditLog[] array = new AuditLogImpl[3];

			array[0] = getByStep_PrevAndNext(
				session, auditLog, step, orderByComparator, true);

			array[1] = auditLog;

			array[2] = getByStep_PrevAndNext(
				session, auditLog, step, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AuditLog getByStep_PrevAndNext(
		Session session, AuditLog auditLog, String step,
		OrderByComparator<AuditLog> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_AUDITLOG_WHERE);

		boolean bindStep = false;

		if (step.isEmpty()) {
			sb.append(_FINDER_COLUMN_STEP_STEP_3);
		}
		else {
			bindStep = true;

			sb.append(_FINDER_COLUMN_STEP_STEP_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindStep) {
			queryPos.add(step);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(auditLog)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AuditLog> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the audit logs where step = &#63; from the database.
	 *
	 * @param step the step
	 */
	@Override
	public void removeByStep(String step) {
		for (AuditLog auditLog :
				findByStep(step, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(auditLog);
		}
	}

	/**
	 * Returns the number of audit logs where step = &#63;.
	 *
	 * @param step the step
	 * @return the number of matching audit logs
	 */
	@Override
	public int countByStep(String step) {
		step = Objects.toString(step, "");

		FinderPath finderPath = _finderPathCountByStep;

		Object[] finderArgs = new Object[] {step};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_AUDITLOG_WHERE);

			boolean bindStep = false;

			if (step.isEmpty()) {
				sb.append(_FINDER_COLUMN_STEP_STEP_3);
			}
			else {
				bindStep = true;

				sb.append(_FINDER_COLUMN_STEP_STEP_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindStep) {
					queryPos.add(step);
				}

				count = (Long)query.uniqueResult();

				dummyFinderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_STEP_STEP_2 =
		"auditLog.step = ?";

	private static final String _FINDER_COLUMN_STEP_STEP_3 =
		"(auditLog.step IS NULL OR auditLog.step = '')";

	private FinderPath _finderPathWithPaginationFindByCorrelationIdStartTimeMs;
	private FinderPath _finderPathWithPaginationCountByCorrelationIdStartTimeMs;

	/**
	 * Returns all the audit logs where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @return the matching audit logs
	 */
	@Override
	public List<AuditLog> findByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs) {

		return findByCorrelationIdStartTimeMs(
			correlationId, startTimeMs, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
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
	@Override
	public List<AuditLog> findByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs, int start, int end) {

		return findByCorrelationIdStartTimeMs(
			correlationId, startTimeMs, start, end, null);
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
	@Override
	public List<AuditLog> findByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return findByCorrelationIdStartTimeMs(
			correlationId, startTimeMs, start, end, orderByComparator, true);
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
	@Override
	public List<AuditLog> findByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		correlationId = Objects.toString(correlationId, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = _finderPathWithPaginationFindByCorrelationIdStartTimeMs;
		finderArgs = new Object[] {
			correlationId, startTimeMs, start, end, orderByComparator
		};

		List<AuditLog> list = null;

		if (useFinderCache) {
			list = (List<AuditLog>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AuditLog auditLog : list) {
					if (!correlationId.equals(auditLog.getCorrelationId()) ||
						(startTimeMs > auditLog.getStartTimeMs())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_AUDITLOG_WHERE);

			boolean bindCorrelationId = false;

			if (correlationId.isEmpty()) {
				sb.append(
					_FINDER_COLUMN_CORRELATIONIDSTARTTIMEMS_CORRELATIONID_3);
			}
			else {
				bindCorrelationId = true;

				sb.append(
					_FINDER_COLUMN_CORRELATIONIDSTARTTIMEMS_CORRELATIONID_2);
			}

			sb.append(_FINDER_COLUMN_CORRELATIONIDSTARTTIMEMS_STARTTIMEMS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindCorrelationId) {
					queryPos.add(correlationId);
				}

				queryPos.add(startTimeMs);

				list = (List<AuditLog>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					dummyFinderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
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
	@Override
	public AuditLog findByCorrelationIdStartTimeMs_First(
			String correlationId, long startTimeMs,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByCorrelationIdStartTimeMs_First(
			correlationId, startTimeMs, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("correlationId=");
		sb.append(correlationId);

		sb.append(", startTimeMs>=");
		sb.append(startTimeMs);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the first audit log in the ordered set where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByCorrelationIdStartTimeMs_First(
		String correlationId, long startTimeMs,
		OrderByComparator<AuditLog> orderByComparator) {

		List<AuditLog> list = findByCorrelationIdStartTimeMs(
			correlationId, startTimeMs, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog findByCorrelationIdStartTimeMs_Last(
			String correlationId, long startTimeMs,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByCorrelationIdStartTimeMs_Last(
			correlationId, startTimeMs, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("correlationId=");
		sb.append(correlationId);

		sb.append(", startTimeMs>=");
		sb.append(startTimeMs);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
	}

	/**
	 * Returns the last audit log in the ordered set where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching audit log, or <code>null</code> if a matching audit log could not be found
	 */
	@Override
	public AuditLog fetchByCorrelationIdStartTimeMs_Last(
		String correlationId, long startTimeMs,
		OrderByComparator<AuditLog> orderByComparator) {

		int count = countByCorrelationIdStartTimeMs(correlationId, startTimeMs);

		if (count == 0) {
			return null;
		}

		List<AuditLog> list = findByCorrelationIdStartTimeMs(
			correlationId, startTimeMs, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog[] findByCorrelationIdStartTimeMs_PrevAndNext(
			long auditLogId, String correlationId, long startTimeMs,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		correlationId = Objects.toString(correlationId, "");

		AuditLog auditLog = findByPrimaryKey(auditLogId);

		Session session = null;

		try {
			session = openSession();

			AuditLog[] array = new AuditLogImpl[3];

			array[0] = getByCorrelationIdStartTimeMs_PrevAndNext(
				session, auditLog, correlationId, startTimeMs,
				orderByComparator, true);

			array[1] = auditLog;

			array[2] = getByCorrelationIdStartTimeMs_PrevAndNext(
				session, auditLog, correlationId, startTimeMs,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AuditLog getByCorrelationIdStartTimeMs_PrevAndNext(
		Session session, AuditLog auditLog, String correlationId,
		long startTimeMs, OrderByComparator<AuditLog> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_AUDITLOG_WHERE);

		boolean bindCorrelationId = false;

		if (correlationId.isEmpty()) {
			sb.append(_FINDER_COLUMN_CORRELATIONIDSTARTTIMEMS_CORRELATIONID_3);
		}
		else {
			bindCorrelationId = true;

			sb.append(_FINDER_COLUMN_CORRELATIONIDSTARTTIMEMS_CORRELATIONID_2);
		}

		sb.append(_FINDER_COLUMN_CORRELATIONIDSTARTTIMEMS_STARTTIMEMS_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindCorrelationId) {
			queryPos.add(correlationId);
		}

		queryPos.add(startTimeMs);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(auditLog)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AuditLog> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the audit logs where correlationId = &#63; and startTimeMs &ge; &#63; from the database.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 */
	@Override
	public void removeByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs) {

		for (AuditLog auditLog :
				findByCorrelationIdStartTimeMs(
					correlationId, startTimeMs, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(auditLog);
		}
	}

	/**
	 * Returns the number of audit logs where correlationId = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param correlationId the correlation ID
	 * @param startTimeMs the start time ms
	 * @return the number of matching audit logs
	 */
	@Override
	public int countByCorrelationIdStartTimeMs(
		String correlationId, long startTimeMs) {

		correlationId = Objects.toString(correlationId, "");

		FinderPath finderPath =
			_finderPathWithPaginationCountByCorrelationIdStartTimeMs;

		Object[] finderArgs = new Object[] {correlationId, startTimeMs};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_AUDITLOG_WHERE);

			boolean bindCorrelationId = false;

			if (correlationId.isEmpty()) {
				sb.append(
					_FINDER_COLUMN_CORRELATIONIDSTARTTIMEMS_CORRELATIONID_3);
			}
			else {
				bindCorrelationId = true;

				sb.append(
					_FINDER_COLUMN_CORRELATIONIDSTARTTIMEMS_CORRELATIONID_2);
			}

			sb.append(_FINDER_COLUMN_CORRELATIONIDSTARTTIMEMS_STARTTIMEMS_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindCorrelationId) {
					queryPos.add(correlationId);
				}

				queryPos.add(startTimeMs);

				count = (Long)query.uniqueResult();

				dummyFinderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_CORRELATIONIDSTARTTIMEMS_CORRELATIONID_2 =
			"auditLog.correlationId = ? AND ";

	private static final String
		_FINDER_COLUMN_CORRELATIONIDSTARTTIMEMS_CORRELATIONID_3 =
			"(auditLog.correlationId IS NULL OR auditLog.correlationId = '') AND ";

	private static final String
		_FINDER_COLUMN_CORRELATIONIDSTARTTIMEMS_STARTTIMEMS_2 =
			"auditLog.startTimeMs >= ?";

	private FinderPath _finderPathWithPaginationFindByCategoryStatusStartTimeMs;
	private FinderPath
		_finderPathWithPaginationCountByCategoryStatusStartTimeMs;

	/**
	 * Returns all the audit logs where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @return the matching audit logs
	 */
	@Override
	public List<AuditLog> findByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs) {

		return findByCategoryStatusStartTimeMs(
			category, status, startTimeMs, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
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
	@Override
	public List<AuditLog> findByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs, int start, int end) {

		return findByCategoryStatusStartTimeMs(
			category, status, startTimeMs, start, end, null);
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
	@Override
	public List<AuditLog> findByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return findByCategoryStatusStartTimeMs(
			category, status, startTimeMs, start, end, orderByComparator, true);
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
	@Override
	public List<AuditLog> findByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		category = Objects.toString(category, "");
		status = Objects.toString(status, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = _finderPathWithPaginationFindByCategoryStatusStartTimeMs;
		finderArgs = new Object[] {
			category, status, startTimeMs, start, end, orderByComparator
		};

		List<AuditLog> list = null;

		if (useFinderCache) {
			list = (List<AuditLog>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AuditLog auditLog : list) {
					if (!category.equals(auditLog.getCategory()) ||
						!status.equals(auditLog.getStatus()) ||
						(startTimeMs > auditLog.getStartTimeMs())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					5 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(5);
			}

			sb.append(_SQL_SELECT_AUDITLOG_WHERE);

			boolean bindCategory = false;

			if (category.isEmpty()) {
				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_CATEGORY_3);
			}
			else {
				bindCategory = true;

				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_CATEGORY_2);
			}

			boolean bindStatus = false;

			if (status.isEmpty()) {
				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_STATUS_3);
			}
			else {
				bindStatus = true;

				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_STATUS_2);
			}

			sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_STARTTIMEMS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindCategory) {
					queryPos.add(category);
				}

				if (bindStatus) {
					queryPos.add(status);
				}

				queryPos.add(startTimeMs);

				list = (List<AuditLog>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					dummyFinderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
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
	@Override
	public AuditLog findByCategoryStatusStartTimeMs_First(
			String category, String status, long startTimeMs,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByCategoryStatusStartTimeMs_First(
			category, status, startTimeMs, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("category=");
		sb.append(category);

		sb.append(", status=");
		sb.append(status);

		sb.append(", startTimeMs>=");
		sb.append(startTimeMs);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
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
	@Override
	public AuditLog fetchByCategoryStatusStartTimeMs_First(
		String category, String status, long startTimeMs,
		OrderByComparator<AuditLog> orderByComparator) {

		List<AuditLog> list = findByCategoryStatusStartTimeMs(
			category, status, startTimeMs, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog findByCategoryStatusStartTimeMs_Last(
			String category, String status, long startTimeMs,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByCategoryStatusStartTimeMs_Last(
			category, status, startTimeMs, orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("category=");
		sb.append(category);

		sb.append(", status=");
		sb.append(status);

		sb.append(", startTimeMs>=");
		sb.append(startTimeMs);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
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
	@Override
	public AuditLog fetchByCategoryStatusStartTimeMs_Last(
		String category, String status, long startTimeMs,
		OrderByComparator<AuditLog> orderByComparator) {

		int count = countByCategoryStatusStartTimeMs(
			category, status, startTimeMs);

		if (count == 0) {
			return null;
		}

		List<AuditLog> list = findByCategoryStatusStartTimeMs(
			category, status, startTimeMs, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog[] findByCategoryStatusStartTimeMs_PrevAndNext(
			long auditLogId, String category, String status, long startTimeMs,
			OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		category = Objects.toString(category, "");
		status = Objects.toString(status, "");

		AuditLog auditLog = findByPrimaryKey(auditLogId);

		Session session = null;

		try {
			session = openSession();

			AuditLog[] array = new AuditLogImpl[3];

			array[0] = getByCategoryStatusStartTimeMs_PrevAndNext(
				session, auditLog, category, status, startTimeMs,
				orderByComparator, true);

			array[1] = auditLog;

			array[2] = getByCategoryStatusStartTimeMs_PrevAndNext(
				session, auditLog, category, status, startTimeMs,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AuditLog getByCategoryStatusStartTimeMs_PrevAndNext(
		Session session, AuditLog auditLog, String category, String status,
		long startTimeMs, OrderByComparator<AuditLog> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		sb.append(_SQL_SELECT_AUDITLOG_WHERE);

		boolean bindCategory = false;

		if (category.isEmpty()) {
			sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_CATEGORY_3);
		}
		else {
			bindCategory = true;

			sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_CATEGORY_2);
		}

		boolean bindStatus = false;

		if (status.isEmpty()) {
			sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_STATUS_3);
		}
		else {
			bindStatus = true;

			sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_STATUS_2);
		}

		sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_STARTTIMEMS_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindCategory) {
			queryPos.add(category);
		}

		if (bindStatus) {
			queryPos.add(status);
		}

		queryPos.add(startTimeMs);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(auditLog)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AuditLog> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the audit logs where category = &#63; and status = &#63; and startTimeMs &ge; &#63; from the database.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 */
	@Override
	public void removeByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs) {

		for (AuditLog auditLog :
				findByCategoryStatusStartTimeMs(
					category, status, startTimeMs, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(auditLog);
		}
	}

	/**
	 * Returns the number of audit logs where category = &#63; and status = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param category the category
	 * @param status the status
	 * @param startTimeMs the start time ms
	 * @return the number of matching audit logs
	 */
	@Override
	public int countByCategoryStatusStartTimeMs(
		String category, String status, long startTimeMs) {

		category = Objects.toString(category, "");
		status = Objects.toString(status, "");

		FinderPath finderPath =
			_finderPathWithPaginationCountByCategoryStatusStartTimeMs;

		Object[] finderArgs = new Object[] {category, status, startTimeMs};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_AUDITLOG_WHERE);

			boolean bindCategory = false;

			if (category.isEmpty()) {
				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_CATEGORY_3);
			}
			else {
				bindCategory = true;

				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_CATEGORY_2);
			}

			boolean bindStatus = false;

			if (status.isEmpty()) {
				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_STATUS_3);
			}
			else {
				bindStatus = true;

				sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_STATUS_2);
			}

			sb.append(_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_STARTTIMEMS_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindCategory) {
					queryPos.add(category);
				}

				if (bindStatus) {
					queryPos.add(status);
				}

				queryPos.add(startTimeMs);

				count = (Long)query.uniqueResult();

				dummyFinderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_CATEGORY_2 =
			"auditLog.category = ? AND ";

	private static final String
		_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_CATEGORY_3 =
			"(auditLog.category IS NULL OR auditLog.category = '') AND ";

	private static final String
		_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_STATUS_2 =
			"auditLog.status = ? AND ";

	private static final String
		_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_STATUS_3 =
			"(auditLog.status IS NULL OR auditLog.status = '') AND ";

	private static final String
		_FINDER_COLUMN_CATEGORYSTATUSSTARTTIMEMS_STARTTIMEMS_2 =
			"auditLog.startTimeMs >= ?";

	private FinderPath
		_finderPathWithPaginationFindByCompanyCategoryAlertFingerprintStartTimeMs;
	private FinderPath
		_finderPathWithPaginationCountByCompanyCategoryAlertFingerprintStartTimeMs;

	/**
	 * Returns all the audit logs where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63;.
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 * @return the matching audit logs
	 */
	@Override
	public List<AuditLog> findByCompanyCategoryAlertFingerprintStartTimeMs(
		long companyId, String category, String alertFingerprint,
		long startTimeMs) {

		return findByCompanyCategoryAlertFingerprintStartTimeMs(
			companyId, category, alertFingerprint, startTimeMs,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<AuditLog> findByCompanyCategoryAlertFingerprintStartTimeMs(
		long companyId, String category, String alertFingerprint,
		long startTimeMs, int start, int end) {

		return findByCompanyCategoryAlertFingerprintStartTimeMs(
			companyId, category, alertFingerprint, startTimeMs, start, end,
			null);
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
	@Override
	public List<AuditLog> findByCompanyCategoryAlertFingerprintStartTimeMs(
		long companyId, String category, String alertFingerprint,
		long startTimeMs, int start, int end,
		OrderByComparator<AuditLog> orderByComparator) {

		return findByCompanyCategoryAlertFingerprintStartTimeMs(
			companyId, category, alertFingerprint, startTimeMs, start, end,
			orderByComparator, true);
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
	@Override
	public List<AuditLog> findByCompanyCategoryAlertFingerprintStartTimeMs(
		long companyId, String category, String alertFingerprint,
		long startTimeMs, int start, int end,
		OrderByComparator<AuditLog> orderByComparator, boolean useFinderCache) {

		category = Objects.toString(category, "");
		alertFingerprint = Objects.toString(alertFingerprint, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath =
			_finderPathWithPaginationFindByCompanyCategoryAlertFingerprintStartTimeMs;
		finderArgs = new Object[] {
			companyId, category, alertFingerprint, startTimeMs, start, end,
			orderByComparator
		};

		List<AuditLog> list = null;

		if (useFinderCache) {
			list = (List<AuditLog>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AuditLog auditLog : list) {
					if ((companyId != auditLog.getCompanyId()) ||
						!category.equals(auditLog.getCategory()) ||
						!alertFingerprint.equals(
							auditLog.getAlertFingerprint()) ||
						(startTimeMs > auditLog.getStartTimeMs())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					6 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(6);
			}

			sb.append(_SQL_SELECT_AUDITLOG_WHERE);

			sb.append(
				_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_COMPANYID_2);

			boolean bindCategory = false;

			if (category.isEmpty()) {
				sb.append(
					_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_CATEGORY_3);
			}
			else {
				bindCategory = true;

				sb.append(
					_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_CATEGORY_2);
			}

			boolean bindAlertFingerprint = false;

			if (alertFingerprint.isEmpty()) {
				sb.append(
					_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_ALERTFINGERPRINT_3);
			}
			else {
				bindAlertFingerprint = true;

				sb.append(
					_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_ALERTFINGERPRINT_2);
			}

			sb.append(
				_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_STARTTIMEMS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				if (bindCategory) {
					queryPos.add(category);
				}

				if (bindAlertFingerprint) {
					queryPos.add(alertFingerprint);
				}

				queryPos.add(startTimeMs);

				list = (List<AuditLog>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					dummyFinderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
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
	@Override
	public AuditLog findByCompanyCategoryAlertFingerprintStartTimeMs_First(
			long companyId, String category, String alertFingerprint,
			long startTimeMs, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog =
			fetchByCompanyCategoryAlertFingerprintStartTimeMs_First(
				companyId, category, alertFingerprint, startTimeMs,
				orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(10);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append(", category=");
		sb.append(category);

		sb.append(", alertFingerprint=");
		sb.append(alertFingerprint);

		sb.append(", startTimeMs>=");
		sb.append(startTimeMs);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
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
	@Override
	public AuditLog fetchByCompanyCategoryAlertFingerprintStartTimeMs_First(
		long companyId, String category, String alertFingerprint,
		long startTimeMs, OrderByComparator<AuditLog> orderByComparator) {

		List<AuditLog> list = findByCompanyCategoryAlertFingerprintStartTimeMs(
			companyId, category, alertFingerprint, startTimeMs, 0, 1,
			orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog findByCompanyCategoryAlertFingerprintStartTimeMs_Last(
			long companyId, String category, String alertFingerprint,
			long startTimeMs, OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		AuditLog auditLog =
			fetchByCompanyCategoryAlertFingerprintStartTimeMs_Last(
				companyId, category, alertFingerprint, startTimeMs,
				orderByComparator);

		if (auditLog != null) {
			return auditLog;
		}

		StringBundler sb = new StringBundler(10);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append(", category=");
		sb.append(category);

		sb.append(", alertFingerprint=");
		sb.append(alertFingerprint);

		sb.append(", startTimeMs>=");
		sb.append(startTimeMs);

		sb.append("}");

		throw new NoSuchAuditLogException(sb.toString());
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
	@Override
	public AuditLog fetchByCompanyCategoryAlertFingerprintStartTimeMs_Last(
		long companyId, String category, String alertFingerprint,
		long startTimeMs, OrderByComparator<AuditLog> orderByComparator) {

		int count = countByCompanyCategoryAlertFingerprintStartTimeMs(
			companyId, category, alertFingerprint, startTimeMs);

		if (count == 0) {
			return null;
		}

		List<AuditLog> list = findByCompanyCategoryAlertFingerprintStartTimeMs(
			companyId, category, alertFingerprint, startTimeMs, count - 1,
			count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public AuditLog[]
			findByCompanyCategoryAlertFingerprintStartTimeMs_PrevAndNext(
				long auditLogId, long companyId, String category,
				String alertFingerprint, long startTimeMs,
				OrderByComparator<AuditLog> orderByComparator)
		throws NoSuchAuditLogException {

		category = Objects.toString(category, "");
		alertFingerprint = Objects.toString(alertFingerprint, "");

		AuditLog auditLog = findByPrimaryKey(auditLogId);

		Session session = null;

		try {
			session = openSession();

			AuditLog[] array = new AuditLogImpl[3];

			array[0] =
				getByCompanyCategoryAlertFingerprintStartTimeMs_PrevAndNext(
					session, auditLog, companyId, category, alertFingerprint,
					startTimeMs, orderByComparator, true);

			array[1] = auditLog;

			array[2] =
				getByCompanyCategoryAlertFingerprintStartTimeMs_PrevAndNext(
					session, auditLog, companyId, category, alertFingerprint,
					startTimeMs, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AuditLog
		getByCompanyCategoryAlertFingerprintStartTimeMs_PrevAndNext(
			Session session, AuditLog auditLog, long companyId, String category,
			String alertFingerprint, long startTimeMs,
			OrderByComparator<AuditLog> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				7 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(6);
		}

		sb.append(_SQL_SELECT_AUDITLOG_WHERE);

		sb.append(
			_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_COMPANYID_2);

		boolean bindCategory = false;

		if (category.isEmpty()) {
			sb.append(
				_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_CATEGORY_3);
		}
		else {
			bindCategory = true;

			sb.append(
				_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_CATEGORY_2);
		}

		boolean bindAlertFingerprint = false;

		if (alertFingerprint.isEmpty()) {
			sb.append(
				_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_ALERTFINGERPRINT_3);
		}
		else {
			bindAlertFingerprint = true;

			sb.append(
				_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_ALERTFINGERPRINT_2);
		}

		sb.append(
			_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_STARTTIMEMS_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AuditLogModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(companyId);

		if (bindCategory) {
			queryPos.add(category);
		}

		if (bindAlertFingerprint) {
			queryPos.add(alertFingerprint);
		}

		queryPos.add(startTimeMs);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(auditLog)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AuditLog> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the audit logs where companyId = &#63; and category = &#63; and alertFingerprint = &#63; and startTimeMs &ge; &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param category the category
	 * @param alertFingerprint the alert fingerprint
	 * @param startTimeMs the start time ms
	 */
	@Override
	public void removeByCompanyCategoryAlertFingerprintStartTimeMs(
		long companyId, String category, String alertFingerprint,
		long startTimeMs) {

		for (AuditLog auditLog :
				findByCompanyCategoryAlertFingerprintStartTimeMs(
					companyId, category, alertFingerprint, startTimeMs,
					QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(auditLog);
		}
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
	@Override
	public int countByCompanyCategoryAlertFingerprintStartTimeMs(
		long companyId, String category, String alertFingerprint,
		long startTimeMs) {

		category = Objects.toString(category, "");
		alertFingerprint = Objects.toString(alertFingerprint, "");

		FinderPath finderPath =
			_finderPathWithPaginationCountByCompanyCategoryAlertFingerprintStartTimeMs;

		Object[] finderArgs = new Object[] {
			companyId, category, alertFingerprint, startTimeMs
		};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(5);

			sb.append(_SQL_COUNT_AUDITLOG_WHERE);

			sb.append(
				_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_COMPANYID_2);

			boolean bindCategory = false;

			if (category.isEmpty()) {
				sb.append(
					_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_CATEGORY_3);
			}
			else {
				bindCategory = true;

				sb.append(
					_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_CATEGORY_2);
			}

			boolean bindAlertFingerprint = false;

			if (alertFingerprint.isEmpty()) {
				sb.append(
					_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_ALERTFINGERPRINT_3);
			}
			else {
				bindAlertFingerprint = true;

				sb.append(
					_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_ALERTFINGERPRINT_2);
			}

			sb.append(
				_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_STARTTIMEMS_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				if (bindCategory) {
					queryPos.add(category);
				}

				if (bindAlertFingerprint) {
					queryPos.add(alertFingerprint);
				}

				queryPos.add(startTimeMs);

				count = (Long)query.uniqueResult();

				dummyFinderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_COMPANYID_2 =
			"auditLog.companyId = ? AND ";

	private static final String
		_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_CATEGORY_2 =
			"auditLog.category = ? AND ";

	private static final String
		_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_CATEGORY_3 =
			"(auditLog.category IS NULL OR auditLog.category = '') AND ";

	private static final String
		_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_ALERTFINGERPRINT_2 =
			"auditLog.alertFingerprint = ? AND ";

	private static final String
		_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_ALERTFINGERPRINT_3 =
			"(auditLog.alertFingerprint IS NULL OR auditLog.alertFingerprint = '') AND ";

	private static final String
		_FINDER_COLUMN_COMPANYCATEGORYALERTFINGERPRINTSTARTTIMEMS_STARTTIMEMS_2 =
			"auditLog.startTimeMs >= ?";

	public AuditLogPersistenceImpl() {
		setModelClass(AuditLog.class);

		setModelImplClass(AuditLogImpl.class);
		setModelPKClass(long.class);
	}

	/**
	 * Caches the audit log in the entity cache if it is enabled.
	 *
	 * @param auditLog the audit log
	 */
	@Override
	public void cacheResult(AuditLog auditLog) {
		dummyEntityCache.putResult(
			AuditLogImpl.class, auditLog.getPrimaryKey(), auditLog);
	}

	/**
	 * Caches the audit logs in the entity cache if it is enabled.
	 *
	 * @param auditLogs the audit logs
	 */
	@Override
	public void cacheResult(List<AuditLog> auditLogs) {
		for (AuditLog auditLog : auditLogs) {
			if (dummyEntityCache.getResult(
					AuditLogImpl.class, auditLog.getPrimaryKey()) == null) {

				cacheResult(auditLog);
			}
		}
	}

	/**
	 * Clears the cache for all audit logs.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>com.liferay.portal.kernel.dao.orm.FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		dummyEntityCache.clearCache(AuditLogImpl.class);

		dummyFinderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		dummyFinderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		dummyFinderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the audit log.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>com.liferay.portal.kernel.dao.orm.FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(AuditLog auditLog) {
		dummyEntityCache.removeResult(AuditLogImpl.class, auditLog);
	}

	@Override
	public void clearCache(List<AuditLog> auditLogs) {
		for (AuditLog auditLog : auditLogs) {
			dummyEntityCache.removeResult(AuditLogImpl.class, auditLog);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		dummyFinderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		dummyFinderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		dummyFinderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			dummyEntityCache.removeResult(AuditLogImpl.class, primaryKey);
		}
	}

	/**
	 * Creates a new audit log with the primary key. Does not add the audit log to the database.
	 *
	 * @param auditLogId the primary key for the new audit log
	 * @return the new audit log
	 */
	@Override
	public AuditLog create(long auditLogId) {
		AuditLog auditLog = new AuditLogImpl();

		auditLog.setNew(true);
		auditLog.setPrimaryKey(auditLogId);

		auditLog.setCompanyId(CompanyThreadLocal.getCompanyId());

		return auditLog;
	}

	/**
	 * Removes the audit log with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param auditLogId the primary key of the audit log
	 * @return the audit log that was removed
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	@Override
	public AuditLog remove(long auditLogId) throws NoSuchAuditLogException {
		return remove((Serializable)auditLogId);
	}

	/**
	 * Removes the audit log with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the audit log
	 * @return the audit log that was removed
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	@Override
	public AuditLog remove(Serializable primaryKey)
		throws NoSuchAuditLogException {

		Session session = null;

		try {
			session = openSession();

			AuditLog auditLog = (AuditLog)session.get(
				AuditLogImpl.class, primaryKey);

			if (auditLog == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchAuditLogException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(auditLog);
		}
		catch (NoSuchAuditLogException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected AuditLog removeImpl(AuditLog auditLog) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(auditLog)) {
				auditLog = (AuditLog)session.get(
					AuditLogImpl.class, auditLog.getPrimaryKeyObj());
			}

			if (auditLog != null) {
				session.delete(auditLog);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (auditLog != null) {
			clearCache(auditLog);
		}

		return auditLog;
	}

	@Override
	public AuditLog updateImpl(AuditLog auditLog) {
		boolean isNew = auditLog.isNew();

		if (!(auditLog instanceof AuditLogModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(auditLog.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(auditLog);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in auditLog proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom AuditLog implementation " +
					auditLog.getClass());
		}

		AuditLogModelImpl auditLogModelImpl = (AuditLogModelImpl)auditLog;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (auditLog.getCreateDate() == null)) {
			if (serviceContext == null) {
				auditLog.setCreateDate(now);
			}
			else {
				auditLog.setCreateDate(serviceContext.getCreateDate(now));
			}
		}

		if (!auditLogModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				auditLog.setModifiedDate(now);
			}
			else {
				auditLog.setModifiedDate(serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(auditLog);
			}
			else {
				session.evict(AuditLogImpl.class, auditLog.getPrimaryKeyObj());

				session.saveOrUpdate(auditLog);
			}

			session.flush();
			session.clear();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		dummyEntityCache.putResult(
			AuditLogImpl.class, auditLogModelImpl, false, true);

		if (isNew) {
			auditLog.setNew(false);
		}

		auditLog.resetOriginalValues();

		return auditLog;
	}

	/**
	 * Returns the audit log with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the audit log
	 * @return the audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	@Override
	public AuditLog findByPrimaryKey(Serializable primaryKey)
		throws NoSuchAuditLogException {

		AuditLog auditLog = fetchByPrimaryKey(primaryKey);

		if (auditLog == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchAuditLogException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return auditLog;
	}

	/**
	 * Returns the audit log with the primary key or throws a <code>NoSuchAuditLogException</code> if it could not be found.
	 *
	 * @param auditLogId the primary key of the audit log
	 * @return the audit log
	 * @throws NoSuchAuditLogException if a audit log with the primary key could not be found
	 */
	@Override
	public AuditLog findByPrimaryKey(long auditLogId)
		throws NoSuchAuditLogException {

		return findByPrimaryKey((Serializable)auditLogId);
	}

	/**
	 * Returns the audit log with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param auditLogId the primary key of the audit log
	 * @return the audit log, or <code>null</code> if a audit log with the primary key could not be found
	 */
	@Override
	public AuditLog fetchByPrimaryKey(long auditLogId) {
		return fetchByPrimaryKey((Serializable)auditLogId);
	}

	/**
	 * Returns all the audit logs.
	 *
	 * @return the audit logs
	 */
	@Override
	public List<AuditLog> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<AuditLog> findAll(int start, int end) {
		return findAll(start, end, null);
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
	@Override
	public List<AuditLog> findAll(
		int start, int end, OrderByComparator<AuditLog> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
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
	@Override
	public List<AuditLog> findAll(
		int start, int end, OrderByComparator<AuditLog> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<AuditLog> list = null;

		if (useFinderCache) {
			list = (List<AuditLog>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_AUDITLOG);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_AUDITLOG;

				sql = sql.concat(AuditLogModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<AuditLog>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					dummyFinderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the audit logs from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (AuditLog auditLog : findAll()) {
			remove(auditLog);
		}
	}

	/**
	 * Returns the number of audit logs.
	 *
	 * @return the number of audit logs
	 */
	@Override
	public int countAll() {
		Long count = (Long)dummyFinderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_AUDITLOG);

				count = (Long)query.uniqueResult();

				dummyFinderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected EntityCache getEntityCache() {
		return dummyEntityCache;
	}

	@Override
	protected String getPKDBName() {
		return "auditLogId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_AUDITLOG;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return AuditLogModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the audit log persistence.
	 */
	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_argumentsResolverServiceRegistration = _bundleContext.registerService(
			ArgumentsResolver.class, new AuditLogModelArgumentsResolver(),
			MapUtil.singletonDictionary(
				"model.class.name", AuditLog.class.getName()));

		_finderPathWithPaginationFindAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathWithPaginationFindByCorrelationId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCorrelationId",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"correlationId"}, true);

		_finderPathWithoutPaginationFindByCorrelationId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCorrelationId",
			new String[] {String.class.getName()},
			new String[] {"correlationId"}, true);

		_finderPathCountByCorrelationId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCorrelationId",
			new String[] {String.class.getName()},
			new String[] {"correlationId"}, false);

		_finderPathWithPaginationFindByJobRunId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByJobRunId",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"jobRunId"}, true);

		_finderPathWithoutPaginationFindByJobRunId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByJobRunId",
			new String[] {String.class.getName()}, new String[] {"jobRunId"},
			true);

		_finderPathCountByJobRunId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByJobRunId",
			new String[] {String.class.getName()}, new String[] {"jobRunId"},
			false);

		_finderPathWithPaginationFindByRequestId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByRequestId",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"requestId"}, true);

		_finderPathWithoutPaginationFindByRequestId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByRequestId",
			new String[] {String.class.getName()}, new String[] {"requestId"},
			true);

		_finderPathCountByRequestId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByRequestId",
			new String[] {String.class.getName()}, new String[] {"requestId"},
			false);

		_finderPathWithPaginationFindByEventId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByEventId",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"eventId"}, true);

		_finderPathWithoutPaginationFindByEventId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByEventId",
			new String[] {String.class.getName()}, new String[] {"eventId"},
			true);

		_finderPathCountByEventId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByEventId",
			new String[] {String.class.getName()}, new String[] {"eventId"},
			false);

		_finderPathWithPaginationFindByCourseCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCourseCode",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"courseCode"}, true);

		_finderPathWithoutPaginationFindByCourseCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCourseCode",
			new String[] {String.class.getName()}, new String[] {"courseCode"},
			true);

		_finderPathCountByCourseCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCourseCode",
			new String[] {String.class.getName()}, new String[] {"courseCode"},
			false);

		_finderPathWithPaginationFindByNtucDTId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByNtucDTId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"ntucDTId"}, true);

		_finderPathWithoutPaginationFindByNtucDTId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByNtucDTId",
			new String[] {Long.class.getName()}, new String[] {"ntucDTId"},
			true);

		_finderPathCountByNtucDTId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByNtucDTId",
			new String[] {Long.class.getName()}, new String[] {"ntucDTId"},
			false);

		_finderPathWithPaginationFindByCategoryStatusSeverity =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
				"findByCategoryStatusSeverity",
				new String[] {
					String.class.getName(), String.class.getName(),
					String.class.getName(), Integer.class.getName(),
					Integer.class.getName(), OrderByComparator.class.getName()
				},
				new String[] {"category", "status", "severity"}, true);

		_finderPathWithoutPaginationFindByCategoryStatusSeverity =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
				"findByCategoryStatusSeverity",
				new String[] {
					String.class.getName(), String.class.getName(),
					String.class.getName()
				},
				new String[] {"category", "status", "severity"}, true);

		_finderPathCountByCategoryStatusSeverity = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByCategoryStatusSeverity",
			new String[] {
				String.class.getName(), String.class.getName(),
				String.class.getName()
			},
			new String[] {"category", "status", "severity"}, false);

		_finderPathWithPaginationFindByErrorCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByErrorCode",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"errorCode"}, true);

		_finderPathWithoutPaginationFindByErrorCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByErrorCode",
			new String[] {String.class.getName()}, new String[] {"errorCode"},
			true);

		_finderPathCountByErrorCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByErrorCode",
			new String[] {String.class.getName()}, new String[] {"errorCode"},
			false);

		_finderPathWithPaginationFindByStep = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByStep",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"step"}, true);

		_finderPathWithoutPaginationFindByStep = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByStep",
			new String[] {String.class.getName()}, new String[] {"step"}, true);

		_finderPathCountByStep = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByStep",
			new String[] {String.class.getName()}, new String[] {"step"},
			false);

		_finderPathWithPaginationFindByCorrelationIdStartTimeMs =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
				"findByCorrelationIdStartTimeMs",
				new String[] {
					String.class.getName(), Long.class.getName(),
					Integer.class.getName(), Integer.class.getName(),
					OrderByComparator.class.getName()
				},
				new String[] {"correlationId", "startTimeMs"}, true);

		_finderPathWithPaginationCountByCorrelationIdStartTimeMs =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
				"countByCorrelationIdStartTimeMs",
				new String[] {String.class.getName(), Long.class.getName()},
				new String[] {"correlationId", "startTimeMs"}, false);

		_finderPathWithPaginationFindByCategoryStatusStartTimeMs =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
				"findByCategoryStatusStartTimeMs",
				new String[] {
					String.class.getName(), String.class.getName(),
					Long.class.getName(), Integer.class.getName(),
					Integer.class.getName(), OrderByComparator.class.getName()
				},
				new String[] {"category", "status", "startTimeMs"}, true);

		_finderPathWithPaginationCountByCategoryStatusStartTimeMs =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
				"countByCategoryStatusStartTimeMs",
				new String[] {
					String.class.getName(), String.class.getName(),
					Long.class.getName()
				},
				new String[] {"category", "status", "startTimeMs"}, false);

		_finderPathWithPaginationFindByCompanyCategoryAlertFingerprintStartTimeMs =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
				"findByCompanyCategoryAlertFingerprintStartTimeMs",
				new String[] {
					Long.class.getName(), String.class.getName(),
					String.class.getName(), Long.class.getName(),
					Integer.class.getName(), Integer.class.getName(),
					OrderByComparator.class.getName()
				},
				new String[] {
					"companyId", "category", "alertFingerprint", "startTimeMs"
				},
				true);

		_finderPathWithPaginationCountByCompanyCategoryAlertFingerprintStartTimeMs =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
				"countByCompanyCategoryAlertFingerprintStartTimeMs",
				new String[] {
					Long.class.getName(), String.class.getName(),
					String.class.getName(), Long.class.getName()
				},
				new String[] {
					"companyId", "category", "alertFingerprint", "startTimeMs"
				},
				false);
	}

	@Deactivate
	public void deactivate() {
		dummyEntityCache.removeCache(AuditLogImpl.class.getName());

		_argumentsResolverServiceRegistration.unregister();

		for (ServiceRegistration<FinderPath> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	@Override
	@Reference(
		target = ntucnicesbdigitalPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = ntucnicesbdigitalPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = ntucnicesbdigitalPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	private BundleContext _bundleContext;

	private static final String _SQL_SELECT_AUDITLOG =
		"SELECT auditLog FROM AuditLog auditLog";

	private static final String _SQL_SELECT_AUDITLOG_WHERE =
		"SELECT auditLog FROM AuditLog auditLog WHERE ";

	private static final String _SQL_COUNT_AUDITLOG =
		"SELECT COUNT(auditLog) FROM AuditLog auditLog";

	private static final String _SQL_COUNT_AUDITLOG_WHERE =
		"SELECT COUNT(auditLog) FROM AuditLog auditLog WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "auditLog.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No AuditLog exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No AuditLog exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		AuditLogPersistenceImpl.class);

	static {
		try {
			Class.forName(
				ntucnicesbdigitalPersistenceConstants.class.getName());
		}
		catch (ClassNotFoundException classNotFoundException) {
			throw new ExceptionInInitializerError(classNotFoundException);
		}
	}

	private FinderPath _createFinderPath(
		String cacheName, String methodName, String[] params,
		String[] columnNames, boolean baseModelResult) {

		FinderPath finderPath = new FinderPath(
			cacheName, methodName, params, columnNames, baseModelResult);

		if (!cacheName.equals(FINDER_CLASS_NAME_LIST_WITH_PAGINATION)) {
			_serviceRegistrations.add(
				_bundleContext.registerService(
					FinderPath.class, finderPath,
					MapUtil.singletonDictionary("cache.name", cacheName)));
		}

		return finderPath;
	}

	private Set<ServiceRegistration<FinderPath>> _serviceRegistrations =
		new HashSet<>();
	private ServiceRegistration<ArgumentsResolver>
		_argumentsResolverServiceRegistration;

	private static class AuditLogModelArgumentsResolver
		implements ArgumentsResolver {

		@Override
		public Object[] getArguments(
			FinderPath finderPath, BaseModel<?> baseModel, boolean checkColumn,
			boolean original) {

			String[] columnNames = finderPath.getColumnNames();

			if ((columnNames == null) || (columnNames.length == 0)) {
				if (baseModel.isNew()) {
					return FINDER_ARGS_EMPTY;
				}

				return null;
			}

			AuditLogModelImpl auditLogModelImpl = (AuditLogModelImpl)baseModel;

			long columnBitmask = auditLogModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(auditLogModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |=
						auditLogModelImpl.getColumnBitmask(columnName);
				}

				_finderPathColumnBitmasksCache.put(
					finderPath, finderPathColumnBitmask);
			}

			if ((columnBitmask & finderPathColumnBitmask) != 0) {
				return _getValue(auditLogModelImpl, columnNames, original);
			}

			return null;
		}

		private Object[] _getValue(
			AuditLogModelImpl auditLogModelImpl, String[] columnNames,
			boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] = auditLogModelImpl.getColumnOriginalValue(
						columnName);
				}
				else {
					arguments[i] = auditLogModelImpl.getColumnValue(columnName);
				}
			}

			return arguments;
		}

		private static Map<FinderPath, Long> _finderPathColumnBitmasksCache =
			new ConcurrentHashMap<>();

	}

}