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
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;

import com.ntuc.notification.exception.NoSuchNtucSBException;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.model.impl.NtucSBImpl;
import com.ntuc.notification.model.impl.NtucSBModelImpl;
import com.ntuc.notification.service.persistence.NtucSBPersistence;
import com.ntuc.notification.service.persistence.impl.constants.ntucnicesbdigitalPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

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
 * The persistence implementation for the ntuc sb service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = NtucSBPersistence.class)
public class NtucSBPersistenceImpl
	extends BasePersistenceImpl<NtucSB> implements NtucSBPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>NtucSBUtil</code> to access the ntuc sb persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		NtucSBImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByisRowLockFailed;
	private FinderPath _finderPathWithoutPaginationFindByisRowLockFailed;
	private FinderPath _finderPathCountByisRowLockFailed;

	/**
	 * Returns all the ntuc sbs where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @return the matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByisRowLockFailed(boolean isRowLockFailed) {
		return findByisRowLockFailed(
			isRowLockFailed, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the ntuc sbs where isRowLockFailed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByisRowLockFailed(
		boolean isRowLockFailed, int start, int end) {

		return findByisRowLockFailed(isRowLockFailed, start, end, null);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where isRowLockFailed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByisRowLockFailed(
		boolean isRowLockFailed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return findByisRowLockFailed(
			isRowLockFailed, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where isRowLockFailed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByisRowLockFailed(
		boolean isRowLockFailed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator, boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByisRowLockFailed;
				finderArgs = new Object[] {isRowLockFailed};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByisRowLockFailed;
			finderArgs = new Object[] {
				isRowLockFailed, start, end, orderByComparator
			};
		}

		List<NtucSB> list = null;

		if (useFinderCache) {
			list = (List<NtucSB>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (NtucSB ntucSB : list) {
					if (isRowLockFailed != ntucSB.isIsRowLockFailed()) {
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

			sb.append(_SQL_SELECT_NTUCSB_WHERE);

			sb.append(_FINDER_COLUMN_ISROWLOCKFAILED_ISROWLOCKFAILED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(NtucSBModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(isRowLockFailed);

				list = (List<NtucSB>)QueryUtil.list(
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
	 * Returns the first ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB findByisRowLockFailed_First(
			boolean isRowLockFailed,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByisRowLockFailed_First(
			isRowLockFailed, orderByComparator);

		if (ntucSB != null) {
			return ntucSB;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("isRowLockFailed=");
		sb.append(isRowLockFailed);

		sb.append("}");

		throw new NoSuchNtucSBException(sb.toString());
	}

	/**
	 * Returns the first ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB fetchByisRowLockFailed_First(
		boolean isRowLockFailed, OrderByComparator<NtucSB> orderByComparator) {

		List<NtucSB> list = findByisRowLockFailed(
			isRowLockFailed, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB findByisRowLockFailed_Last(
			boolean isRowLockFailed,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByisRowLockFailed_Last(
			isRowLockFailed, orderByComparator);

		if (ntucSB != null) {
			return ntucSB;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("isRowLockFailed=");
		sb.append(isRowLockFailed);

		sb.append("}");

		throw new NoSuchNtucSBException(sb.toString());
	}

	/**
	 * Returns the last ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB fetchByisRowLockFailed_Last(
		boolean isRowLockFailed, OrderByComparator<NtucSB> orderByComparator) {

		int count = countByisRowLockFailed(isRowLockFailed);

		if (count == 0) {
			return null;
		}

		List<NtucSB> list = findByisRowLockFailed(
			isRowLockFailed, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where isRowLockFailed = &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param isRowLockFailed the is row lock failed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	@Override
	public NtucSB[] findByisRowLockFailed_PrevAndNext(
			long ntucDTId, boolean isRowLockFailed,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = findByPrimaryKey(ntucDTId);

		Session session = null;

		try {
			session = openSession();

			NtucSB[] array = new NtucSBImpl[3];

			array[0] = getByisRowLockFailed_PrevAndNext(
				session, ntucSB, isRowLockFailed, orderByComparator, true);

			array[1] = ntucSB;

			array[2] = getByisRowLockFailed_PrevAndNext(
				session, ntucSB, isRowLockFailed, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected NtucSB getByisRowLockFailed_PrevAndNext(
		Session session, NtucSB ntucSB, boolean isRowLockFailed,
		OrderByComparator<NtucSB> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_NTUCSB_WHERE);

		sb.append(_FINDER_COLUMN_ISROWLOCKFAILED_ISROWLOCKFAILED_2);

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
			sb.append(NtucSBModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(isRowLockFailed);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(ntucSB)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<NtucSB> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the ntuc sbs where isRowLockFailed = &#63; from the database.
	 *
	 * @param isRowLockFailed the is row lock failed
	 */
	@Override
	public void removeByisRowLockFailed(boolean isRowLockFailed) {
		for (NtucSB ntucSB :
				findByisRowLockFailed(
					isRowLockFailed, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(ntucSB);
		}
	}

	/**
	 * Returns the number of ntuc sbs where isRowLockFailed = &#63;.
	 *
	 * @param isRowLockFailed the is row lock failed
	 * @return the number of matching ntuc sbs
	 */
	@Override
	public int countByisRowLockFailed(boolean isRowLockFailed) {
		FinderPath finderPath = _finderPathCountByisRowLockFailed;

		Object[] finderArgs = new Object[] {isRowLockFailed};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_NTUCSB_WHERE);

			sb.append(_FINDER_COLUMN_ISROWLOCKFAILED_ISROWLOCKFAILED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(isRowLockFailed);

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
		_FINDER_COLUMN_ISROWLOCKFAILED_ISROWLOCKFAILED_2 =
			"ntucSB.isRowLockFailed = ?";

	private FinderPath _finderPathWithPaginationFindByCourseCodeAndEvent;
	private FinderPath _finderPathWithoutPaginationFindByCourseCodeAndEvent;
	private FinderPath _finderPathCountByCourseCodeAndEvent;

	/**
	 * Returns all the ntuc sbs where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @return the matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByCourseCodeAndEvent(
		String courseCode, String event) {

		return findByCourseCodeAndEvent(
			courseCode, event, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the ntuc sbs where courseCode = &#63; and event = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByCourseCodeAndEvent(
		String courseCode, String event, int start, int end) {

		return findByCourseCodeAndEvent(courseCode, event, start, end, null);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where courseCode = &#63; and event = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByCourseCodeAndEvent(
		String courseCode, String event, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return findByCourseCodeAndEvent(
			courseCode, event, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where courseCode = &#63; and event = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByCourseCodeAndEvent(
		String courseCode, String event, int start, int end,
		OrderByComparator<NtucSB> orderByComparator, boolean useFinderCache) {

		courseCode = Objects.toString(courseCode, "");
		event = Objects.toString(event, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByCourseCodeAndEvent;
				finderArgs = new Object[] {courseCode, event};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCourseCodeAndEvent;
			finderArgs = new Object[] {
				courseCode, event, start, end, orderByComparator
			};
		}

		List<NtucSB> list = null;

		if (useFinderCache) {
			list = (List<NtucSB>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (NtucSB ntucSB : list) {
					if (!courseCode.equals(ntucSB.getCourseCode()) ||
						!event.equals(ntucSB.getEvent())) {

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

			sb.append(_SQL_SELECT_NTUCSB_WHERE);

			boolean bindCourseCode = false;

			if (courseCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODEANDEVENT_COURSECODE_3);
			}
			else {
				bindCourseCode = true;

				sb.append(_FINDER_COLUMN_COURSECODEANDEVENT_COURSECODE_2);
			}

			boolean bindEvent = false;

			if (event.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODEANDEVENT_EVENT_3);
			}
			else {
				bindEvent = true;

				sb.append(_FINDER_COLUMN_COURSECODEANDEVENT_EVENT_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(NtucSBModelImpl.ORDER_BY_JPQL);
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

				if (bindEvent) {
					queryPos.add(event);
				}

				list = (List<NtucSB>)QueryUtil.list(
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
	 * Returns the first ntuc sb in the ordered set where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB findByCourseCodeAndEvent_First(
			String courseCode, String event,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByCourseCodeAndEvent_First(
			courseCode, event, orderByComparator);

		if (ntucSB != null) {
			return ntucSB;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("courseCode=");
		sb.append(courseCode);

		sb.append(", event=");
		sb.append(event);

		sb.append("}");

		throw new NoSuchNtucSBException(sb.toString());
	}

	/**
	 * Returns the first ntuc sb in the ordered set where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB fetchByCourseCodeAndEvent_First(
		String courseCode, String event,
		OrderByComparator<NtucSB> orderByComparator) {

		List<NtucSB> list = findByCourseCodeAndEvent(
			courseCode, event, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last ntuc sb in the ordered set where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB findByCourseCodeAndEvent_Last(
			String courseCode, String event,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByCourseCodeAndEvent_Last(
			courseCode, event, orderByComparator);

		if (ntucSB != null) {
			return ntucSB;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("courseCode=");
		sb.append(courseCode);

		sb.append(", event=");
		sb.append(event);

		sb.append("}");

		throw new NoSuchNtucSBException(sb.toString());
	}

	/**
	 * Returns the last ntuc sb in the ordered set where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB fetchByCourseCodeAndEvent_Last(
		String courseCode, String event,
		OrderByComparator<NtucSB> orderByComparator) {

		int count = countByCourseCodeAndEvent(courseCode, event);

		if (count == 0) {
			return null;
		}

		List<NtucSB> list = findByCourseCodeAndEvent(
			courseCode, event, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where courseCode = &#63; and event = &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param courseCode the course code
	 * @param event the event
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	@Override
	public NtucSB[] findByCourseCodeAndEvent_PrevAndNext(
			long ntucDTId, String courseCode, String event,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		courseCode = Objects.toString(courseCode, "");
		event = Objects.toString(event, "");

		NtucSB ntucSB = findByPrimaryKey(ntucDTId);

		Session session = null;

		try {
			session = openSession();

			NtucSB[] array = new NtucSBImpl[3];

			array[0] = getByCourseCodeAndEvent_PrevAndNext(
				session, ntucSB, courseCode, event, orderByComparator, true);

			array[1] = ntucSB;

			array[2] = getByCourseCodeAndEvent_PrevAndNext(
				session, ntucSB, courseCode, event, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected NtucSB getByCourseCodeAndEvent_PrevAndNext(
		Session session, NtucSB ntucSB, String courseCode, String event,
		OrderByComparator<NtucSB> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_NTUCSB_WHERE);

		boolean bindCourseCode = false;

		if (courseCode.isEmpty()) {
			sb.append(_FINDER_COLUMN_COURSECODEANDEVENT_COURSECODE_3);
		}
		else {
			bindCourseCode = true;

			sb.append(_FINDER_COLUMN_COURSECODEANDEVENT_COURSECODE_2);
		}

		boolean bindEvent = false;

		if (event.isEmpty()) {
			sb.append(_FINDER_COLUMN_COURSECODEANDEVENT_EVENT_3);
		}
		else {
			bindEvent = true;

			sb.append(_FINDER_COLUMN_COURSECODEANDEVENT_EVENT_2);
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
			sb.append(NtucSBModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindCourseCode) {
			queryPos.add(courseCode);
		}

		if (bindEvent) {
			queryPos.add(event);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(ntucSB)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<NtucSB> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the ntuc sbs where courseCode = &#63; and event = &#63; from the database.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 */
	@Override
	public void removeByCourseCodeAndEvent(String courseCode, String event) {
		for (NtucSB ntucSB :
				findByCourseCodeAndEvent(
					courseCode, event, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(ntucSB);
		}
	}

	/**
	 * Returns the number of ntuc sbs where courseCode = &#63; and event = &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @return the number of matching ntuc sbs
	 */
	@Override
	public int countByCourseCodeAndEvent(String courseCode, String event) {
		courseCode = Objects.toString(courseCode, "");
		event = Objects.toString(event, "");

		FinderPath finderPath = _finderPathCountByCourseCodeAndEvent;

		Object[] finderArgs = new Object[] {courseCode, event};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_NTUCSB_WHERE);

			boolean bindCourseCode = false;

			if (courseCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODEANDEVENT_COURSECODE_3);
			}
			else {
				bindCourseCode = true;

				sb.append(_FINDER_COLUMN_COURSECODEANDEVENT_COURSECODE_2);
			}

			boolean bindEvent = false;

			if (event.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODEANDEVENT_EVENT_3);
			}
			else {
				bindEvent = true;

				sb.append(_FINDER_COLUMN_COURSECODEANDEVENT_EVENT_2);
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

				if (bindEvent) {
					queryPos.add(event);
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

	private static final String _FINDER_COLUMN_COURSECODEANDEVENT_COURSECODE_2 =
		"ntucSB.courseCode = ? AND ";

	private static final String _FINDER_COLUMN_COURSECODEANDEVENT_COURSECODE_3 =
		"(ntucSB.courseCode IS NULL OR ntucSB.courseCode = '') AND ";

	private static final String _FINDER_COLUMN_COURSECODEANDEVENT_EVENT_2 =
		"ntucSB.event = ?";

	private static final String _FINDER_COLUMN_COURSECODEANDEVENT_EVENT_3 =
		"(ntucSB.event IS NULL OR ntucSB.event = '')";

	private FinderPath
		_finderPathWithPaginationFindByCourseCodeEventAndChangeFrom;
	private FinderPath
		_finderPathWithPaginationCountByCourseCodeEventAndChangeFrom;

	/**
	 * Returns all the ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @return the matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom) {

		return findByCourseCodeEventAndChangeFrom(
			courseCode, event, changeFrom, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom, int start,
		int end) {

		return findByCourseCodeEventAndChangeFrom(
			courseCode, event, changeFrom, start, end, null);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return findByCourseCodeEventAndChangeFrom(
			courseCode, event, changeFrom, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom, int start, int end,
		OrderByComparator<NtucSB> orderByComparator, boolean useFinderCache) {

		courseCode = Objects.toString(courseCode, "");
		event = Objects.toString(event, "");
		changeFrom = Objects.toString(changeFrom, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath =
			_finderPathWithPaginationFindByCourseCodeEventAndChangeFrom;
		finderArgs = new Object[] {
			courseCode, event, changeFrom, start, end, orderByComparator
		};

		List<NtucSB> list = null;

		if (useFinderCache) {
			list = (List<NtucSB>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (NtucSB ntucSB : list) {
					if (!courseCode.equals(ntucSB.getCourseCode()) ||
						!event.equals(ntucSB.getEvent()) ||
						!StringUtil.wildcardMatches(
							ntucSB.getChangeFrom(), changeFrom, '_', '%', '\\',
							true)) {

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

			sb.append(_SQL_SELECT_NTUCSB_WHERE);

			boolean bindCourseCode = false;

			if (courseCode.isEmpty()) {
				sb.append(
					_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_COURSECODE_3);
			}
			else {
				bindCourseCode = true;

				sb.append(
					_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_COURSECODE_2);
			}

			boolean bindEvent = false;

			if (event.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_EVENT_3);
			}
			else {
				bindEvent = true;

				sb.append(_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_EVENT_2);
			}

			boolean bindChangeFrom = false;

			if (changeFrom.isEmpty()) {
				sb.append(
					_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_CHANGEFROM_3);
			}
			else {
				bindChangeFrom = true;

				sb.append(
					_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_CHANGEFROM_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(NtucSBModelImpl.ORDER_BY_JPQL);
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

				if (bindEvent) {
					queryPos.add(event);
				}

				if (bindChangeFrom) {
					queryPos.add(changeFrom);
				}

				list = (List<NtucSB>)QueryUtil.list(
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
	 * Returns the first ntuc sb in the ordered set where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB findByCourseCodeEventAndChangeFrom_First(
			String courseCode, String event, String changeFrom,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByCourseCodeEventAndChangeFrom_First(
			courseCode, event, changeFrom, orderByComparator);

		if (ntucSB != null) {
			return ntucSB;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("courseCode=");
		sb.append(courseCode);

		sb.append(", event=");
		sb.append(event);

		sb.append(", changeFromLIKE");
		sb.append(changeFrom);

		sb.append("}");

		throw new NoSuchNtucSBException(sb.toString());
	}

	/**
	 * Returns the first ntuc sb in the ordered set where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB fetchByCourseCodeEventAndChangeFrom_First(
		String courseCode, String event, String changeFrom,
		OrderByComparator<NtucSB> orderByComparator) {

		List<NtucSB> list = findByCourseCodeEventAndChangeFrom(
			courseCode, event, changeFrom, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last ntuc sb in the ordered set where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB findByCourseCodeEventAndChangeFrom_Last(
			String courseCode, String event, String changeFrom,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByCourseCodeEventAndChangeFrom_Last(
			courseCode, event, changeFrom, orderByComparator);

		if (ntucSB != null) {
			return ntucSB;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("courseCode=");
		sb.append(courseCode);

		sb.append(", event=");
		sb.append(event);

		sb.append(", changeFromLIKE");
		sb.append(changeFrom);

		sb.append("}");

		throw new NoSuchNtucSBException(sb.toString());
	}

	/**
	 * Returns the last ntuc sb in the ordered set where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB fetchByCourseCodeEventAndChangeFrom_Last(
		String courseCode, String event, String changeFrom,
		OrderByComparator<NtucSB> orderByComparator) {

		int count = countByCourseCodeEventAndChangeFrom(
			courseCode, event, changeFrom);

		if (count == 0) {
			return null;
		}

		List<NtucSB> list = findByCourseCodeEventAndChangeFrom(
			courseCode, event, changeFrom, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	@Override
	public NtucSB[] findByCourseCodeEventAndChangeFrom_PrevAndNext(
			long ntucDTId, String courseCode, String event, String changeFrom,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		courseCode = Objects.toString(courseCode, "");
		event = Objects.toString(event, "");
		changeFrom = Objects.toString(changeFrom, "");

		NtucSB ntucSB = findByPrimaryKey(ntucDTId);

		Session session = null;

		try {
			session = openSession();

			NtucSB[] array = new NtucSBImpl[3];

			array[0] = getByCourseCodeEventAndChangeFrom_PrevAndNext(
				session, ntucSB, courseCode, event, changeFrom,
				orderByComparator, true);

			array[1] = ntucSB;

			array[2] = getByCourseCodeEventAndChangeFrom_PrevAndNext(
				session, ntucSB, courseCode, event, changeFrom,
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

	protected NtucSB getByCourseCodeEventAndChangeFrom_PrevAndNext(
		Session session, NtucSB ntucSB, String courseCode, String event,
		String changeFrom, OrderByComparator<NtucSB> orderByComparator,
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

		sb.append(_SQL_SELECT_NTUCSB_WHERE);

		boolean bindCourseCode = false;

		if (courseCode.isEmpty()) {
			sb.append(_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_COURSECODE_3);
		}
		else {
			bindCourseCode = true;

			sb.append(_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_COURSECODE_2);
		}

		boolean bindEvent = false;

		if (event.isEmpty()) {
			sb.append(_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_EVENT_3);
		}
		else {
			bindEvent = true;

			sb.append(_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_EVENT_2);
		}

		boolean bindChangeFrom = false;

		if (changeFrom.isEmpty()) {
			sb.append(_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_CHANGEFROM_3);
		}
		else {
			bindChangeFrom = true;

			sb.append(_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_CHANGEFROM_2);
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
			sb.append(NtucSBModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindCourseCode) {
			queryPos.add(courseCode);
		}

		if (bindEvent) {
			queryPos.add(event);
		}

		if (bindChangeFrom) {
			queryPos.add(changeFrom);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(ntucSB)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<NtucSB> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63; from the database.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 */
	@Override
	public void removeByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom) {

		for (NtucSB ntucSB :
				findByCourseCodeEventAndChangeFrom(
					courseCode, event, changeFrom, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(ntucSB);
		}
	}

	/**
	 * Returns the number of ntuc sbs where courseCode = &#63; and event = &#63; and changeFrom LIKE &#63;.
	 *
	 * @param courseCode the course code
	 * @param event the event
	 * @param changeFrom the change from
	 * @return the number of matching ntuc sbs
	 */
	@Override
	public int countByCourseCodeEventAndChangeFrom(
		String courseCode, String event, String changeFrom) {

		courseCode = Objects.toString(courseCode, "");
		event = Objects.toString(event, "");
		changeFrom = Objects.toString(changeFrom, "");

		FinderPath finderPath =
			_finderPathWithPaginationCountByCourseCodeEventAndChangeFrom;

		Object[] finderArgs = new Object[] {courseCode, event, changeFrom};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_NTUCSB_WHERE);

			boolean bindCourseCode = false;

			if (courseCode.isEmpty()) {
				sb.append(
					_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_COURSECODE_3);
			}
			else {
				bindCourseCode = true;

				sb.append(
					_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_COURSECODE_2);
			}

			boolean bindEvent = false;

			if (event.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_EVENT_3);
			}
			else {
				bindEvent = true;

				sb.append(_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_EVENT_2);
			}

			boolean bindChangeFrom = false;

			if (changeFrom.isEmpty()) {
				sb.append(
					_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_CHANGEFROM_3);
			}
			else {
				bindChangeFrom = true;

				sb.append(
					_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_CHANGEFROM_2);
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

				if (bindEvent) {
					queryPos.add(event);
				}

				if (bindChangeFrom) {
					queryPos.add(changeFrom);
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
		_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_COURSECODE_2 =
			"ntucSB.courseCode = ? AND ";

	private static final String
		_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_COURSECODE_3 =
			"(ntucSB.courseCode IS NULL OR ntucSB.courseCode = '') AND ";

	private static final String
		_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_EVENT_2 =
			"ntucSB.event = ? AND ";

	private static final String
		_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_EVENT_3 =
			"(ntucSB.event IS NULL OR ntucSB.event = '') AND ";

	private static final String
		_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_CHANGEFROM_2 =
			"ntucSB.changeFrom LIKE ?";

	private static final String
		_FINDER_COLUMN_COURSECODEEVENTANDCHANGEFROM_CHANGEFROM_3 =
			"(ntucSB.changeFrom IS NULL OR ntucSB.changeFrom LIKE '')";

	private FinderPath _finderPathWithPaginationFindByRecordsByChangeFrom;
	private FinderPath _finderPathWithPaginationCountByRecordsByChangeFrom;

	/**
	 * Returns all the ntuc sbs where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @return the matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByRecordsByChangeFrom(String changeFrom) {
		return findByRecordsByChangeFrom(
			changeFrom, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the ntuc sbs where changeFrom LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param changeFrom the change from
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByRecordsByChangeFrom(
		String changeFrom, int start, int end) {

		return findByRecordsByChangeFrom(changeFrom, start, end, null);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where changeFrom LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param changeFrom the change from
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByRecordsByChangeFrom(
		String changeFrom, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return findByRecordsByChangeFrom(
			changeFrom, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where changeFrom LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param changeFrom the change from
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByRecordsByChangeFrom(
		String changeFrom, int start, int end,
		OrderByComparator<NtucSB> orderByComparator, boolean useFinderCache) {

		changeFrom = Objects.toString(changeFrom, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = _finderPathWithPaginationFindByRecordsByChangeFrom;
		finderArgs = new Object[] {changeFrom, start, end, orderByComparator};

		List<NtucSB> list = null;

		if (useFinderCache) {
			list = (List<NtucSB>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (NtucSB ntucSB : list) {
					if (!StringUtil.wildcardMatches(
							ntucSB.getChangeFrom(), changeFrom, '_', '%', '\\',
							true)) {

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

			sb.append(_SQL_SELECT_NTUCSB_WHERE);

			boolean bindChangeFrom = false;

			if (changeFrom.isEmpty()) {
				sb.append(_FINDER_COLUMN_RECORDSBYCHANGEFROM_CHANGEFROM_3);
			}
			else {
				bindChangeFrom = true;

				sb.append(_FINDER_COLUMN_RECORDSBYCHANGEFROM_CHANGEFROM_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(NtucSBModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindChangeFrom) {
					queryPos.add(changeFrom);
				}

				list = (List<NtucSB>)QueryUtil.list(
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
	 * Returns the first ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB findByRecordsByChangeFrom_First(
			String changeFrom, OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByRecordsByChangeFrom_First(
			changeFrom, orderByComparator);

		if (ntucSB != null) {
			return ntucSB;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("changeFromLIKE");
		sb.append(changeFrom);

		sb.append("}");

		throw new NoSuchNtucSBException(sb.toString());
	}

	/**
	 * Returns the first ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB fetchByRecordsByChangeFrom_First(
		String changeFrom, OrderByComparator<NtucSB> orderByComparator) {

		List<NtucSB> list = findByRecordsByChangeFrom(
			changeFrom, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB findByRecordsByChangeFrom_Last(
			String changeFrom, OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByRecordsByChangeFrom_Last(
			changeFrom, orderByComparator);

		if (ntucSB != null) {
			return ntucSB;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("changeFromLIKE");
		sb.append(changeFrom);

		sb.append("}");

		throw new NoSuchNtucSBException(sb.toString());
	}

	/**
	 * Returns the last ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB fetchByRecordsByChangeFrom_Last(
		String changeFrom, OrderByComparator<NtucSB> orderByComparator) {

		int count = countByRecordsByChangeFrom(changeFrom);

		if (count == 0) {
			return null;
		}

		List<NtucSB> list = findByRecordsByChangeFrom(
			changeFrom, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where changeFrom LIKE &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param changeFrom the change from
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	@Override
	public NtucSB[] findByRecordsByChangeFrom_PrevAndNext(
			long ntucDTId, String changeFrom,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		changeFrom = Objects.toString(changeFrom, "");

		NtucSB ntucSB = findByPrimaryKey(ntucDTId);

		Session session = null;

		try {
			session = openSession();

			NtucSB[] array = new NtucSBImpl[3];

			array[0] = getByRecordsByChangeFrom_PrevAndNext(
				session, ntucSB, changeFrom, orderByComparator, true);

			array[1] = ntucSB;

			array[2] = getByRecordsByChangeFrom_PrevAndNext(
				session, ntucSB, changeFrom, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected NtucSB getByRecordsByChangeFrom_PrevAndNext(
		Session session, NtucSB ntucSB, String changeFrom,
		OrderByComparator<NtucSB> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_NTUCSB_WHERE);

		boolean bindChangeFrom = false;

		if (changeFrom.isEmpty()) {
			sb.append(_FINDER_COLUMN_RECORDSBYCHANGEFROM_CHANGEFROM_3);
		}
		else {
			bindChangeFrom = true;

			sb.append(_FINDER_COLUMN_RECORDSBYCHANGEFROM_CHANGEFROM_2);
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
			sb.append(NtucSBModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindChangeFrom) {
			queryPos.add(changeFrom);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(ntucSB)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<NtucSB> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the ntuc sbs where changeFrom LIKE &#63; from the database.
	 *
	 * @param changeFrom the change from
	 */
	@Override
	public void removeByRecordsByChangeFrom(String changeFrom) {
		for (NtucSB ntucSB :
				findByRecordsByChangeFrom(
					changeFrom, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(ntucSB);
		}
	}

	/**
	 * Returns the number of ntuc sbs where changeFrom LIKE &#63;.
	 *
	 * @param changeFrom the change from
	 * @return the number of matching ntuc sbs
	 */
	@Override
	public int countByRecordsByChangeFrom(String changeFrom) {
		changeFrom = Objects.toString(changeFrom, "");

		FinderPath finderPath =
			_finderPathWithPaginationCountByRecordsByChangeFrom;

		Object[] finderArgs = new Object[] {changeFrom};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_NTUCSB_WHERE);

			boolean bindChangeFrom = false;

			if (changeFrom.isEmpty()) {
				sb.append(_FINDER_COLUMN_RECORDSBYCHANGEFROM_CHANGEFROM_3);
			}
			else {
				bindChangeFrom = true;

				sb.append(_FINDER_COLUMN_RECORDSBYCHANGEFROM_CHANGEFROM_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindChangeFrom) {
					queryPos.add(changeFrom);
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
		_FINDER_COLUMN_RECORDSBYCHANGEFROM_CHANGEFROM_2 =
			"ntucSB.changeFrom LIKE ?";

	private static final String
		_FINDER_COLUMN_RECORDSBYCHANGEFROM_CHANGEFROM_3 =
			"(ntucSB.changeFrom IS NULL OR ntucSB.changeFrom LIKE '')";

	private FinderPath _finderPathWithPaginationFindByStatusAndIsCronProcessed;
	private FinderPath
		_finderPathWithoutPaginationFindByStatusAndIsCronProcessed;
	private FinderPath _finderPathCountByStatusAndIsCronProcessed;

	/**
	 * Returns all the ntuc sbs where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @return the matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed) {

		return findByStatusAndIsCronProcessed(
			event, isCronProcessed, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the ntuc sbs where event = &#63; and isCronProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed, int start, int end) {

		return findByStatusAndIsCronProcessed(
			event, isCronProcessed, start, end, null);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where event = &#63; and isCronProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return findByStatusAndIsCronProcessed(
			event, isCronProcessed, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where event = &#63; and isCronProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator, boolean useFinderCache) {

		event = Objects.toString(event, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByStatusAndIsCronProcessed;
				finderArgs = new Object[] {event, isCronProcessed};
			}
		}
		else if (useFinderCache) {
			finderPath =
				_finderPathWithPaginationFindByStatusAndIsCronProcessed;
			finderArgs = new Object[] {
				event, isCronProcessed, start, end, orderByComparator
			};
		}

		List<NtucSB> list = null;

		if (useFinderCache) {
			list = (List<NtucSB>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (NtucSB ntucSB : list) {
					if (!event.equals(ntucSB.getEvent()) ||
						(isCronProcessed != ntucSB.isIsCronProcessed())) {

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

			sb.append(_SQL_SELECT_NTUCSB_WHERE);

			boolean bindEvent = false;

			if (event.isEmpty()) {
				sb.append(_FINDER_COLUMN_STATUSANDISCRONPROCESSED_EVENT_3);
			}
			else {
				bindEvent = true;

				sb.append(_FINDER_COLUMN_STATUSANDISCRONPROCESSED_EVENT_2);
			}

			sb.append(
				_FINDER_COLUMN_STATUSANDISCRONPROCESSED_ISCRONPROCESSED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(NtucSBModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindEvent) {
					queryPos.add(event);
				}

				queryPos.add(isCronProcessed);

				list = (List<NtucSB>)QueryUtil.list(
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
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB findByStatusAndIsCronProcessed_First(
			String event, boolean isCronProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByStatusAndIsCronProcessed_First(
			event, isCronProcessed, orderByComparator);

		if (ntucSB != null) {
			return ntucSB;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("event=");
		sb.append(event);

		sb.append(", isCronProcessed=");
		sb.append(isCronProcessed);

		sb.append("}");

		throw new NoSuchNtucSBException(sb.toString());
	}

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB fetchByStatusAndIsCronProcessed_First(
		String event, boolean isCronProcessed,
		OrderByComparator<NtucSB> orderByComparator) {

		List<NtucSB> list = findByStatusAndIsCronProcessed(
			event, isCronProcessed, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB findByStatusAndIsCronProcessed_Last(
			String event, boolean isCronProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByStatusAndIsCronProcessed_Last(
			event, isCronProcessed, orderByComparator);

		if (ntucSB != null) {
			return ntucSB;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("event=");
		sb.append(event);

		sb.append(", isCronProcessed=");
		sb.append(isCronProcessed);

		sb.append("}");

		throw new NoSuchNtucSBException(sb.toString());
	}

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB fetchByStatusAndIsCronProcessed_Last(
		String event, boolean isCronProcessed,
		OrderByComparator<NtucSB> orderByComparator) {

		int count = countByStatusAndIsCronProcessed(event, isCronProcessed);

		if (count == 0) {
			return null;
		}

		List<NtucSB> list = findByStatusAndIsCronProcessed(
			event, isCronProcessed, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	@Override
	public NtucSB[] findByStatusAndIsCronProcessed_PrevAndNext(
			long ntucDTId, String event, boolean isCronProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		event = Objects.toString(event, "");

		NtucSB ntucSB = findByPrimaryKey(ntucDTId);

		Session session = null;

		try {
			session = openSession();

			NtucSB[] array = new NtucSBImpl[3];

			array[0] = getByStatusAndIsCronProcessed_PrevAndNext(
				session, ntucSB, event, isCronProcessed, orderByComparator,
				true);

			array[1] = ntucSB;

			array[2] = getByStatusAndIsCronProcessed_PrevAndNext(
				session, ntucSB, event, isCronProcessed, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected NtucSB getByStatusAndIsCronProcessed_PrevAndNext(
		Session session, NtucSB ntucSB, String event, boolean isCronProcessed,
		OrderByComparator<NtucSB> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_NTUCSB_WHERE);

		boolean bindEvent = false;

		if (event.isEmpty()) {
			sb.append(_FINDER_COLUMN_STATUSANDISCRONPROCESSED_EVENT_3);
		}
		else {
			bindEvent = true;

			sb.append(_FINDER_COLUMN_STATUSANDISCRONPROCESSED_EVENT_2);
		}

		sb.append(_FINDER_COLUMN_STATUSANDISCRONPROCESSED_ISCRONPROCESSED_2);

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
			sb.append(NtucSBModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindEvent) {
			queryPos.add(event);
		}

		queryPos.add(isCronProcessed);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(ntucSB)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<NtucSB> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the ntuc sbs where event = &#63; and isCronProcessed = &#63; from the database.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 */
	@Override
	public void removeByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed) {

		for (NtucSB ntucSB :
				findByStatusAndIsCronProcessed(
					event, isCronProcessed, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(ntucSB);
		}
	}

	/**
	 * Returns the number of ntuc sbs where event = &#63; and isCronProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCronProcessed the is cron processed
	 * @return the number of matching ntuc sbs
	 */
	@Override
	public int countByStatusAndIsCronProcessed(
		String event, boolean isCronProcessed) {

		event = Objects.toString(event, "");

		FinderPath finderPath = _finderPathCountByStatusAndIsCronProcessed;

		Object[] finderArgs = new Object[] {event, isCronProcessed};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_NTUCSB_WHERE);

			boolean bindEvent = false;

			if (event.isEmpty()) {
				sb.append(_FINDER_COLUMN_STATUSANDISCRONPROCESSED_EVENT_3);
			}
			else {
				bindEvent = true;

				sb.append(_FINDER_COLUMN_STATUSANDISCRONPROCESSED_EVENT_2);
			}

			sb.append(
				_FINDER_COLUMN_STATUSANDISCRONPROCESSED_ISCRONPROCESSED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindEvent) {
					queryPos.add(event);
				}

				queryPos.add(isCronProcessed);

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
		_FINDER_COLUMN_STATUSANDISCRONPROCESSED_EVENT_2 =
			"ntucSB.event = ? AND ";

	private static final String
		_FINDER_COLUMN_STATUSANDISCRONPROCESSED_EVENT_3 =
			"(ntucSB.event IS NULL OR ntucSB.event = '') AND ";

	private static final String
		_FINDER_COLUMN_STATUSANDISCRONPROCESSED_ISCRONPROCESSED_2 =
			"ntucSB.isCronProcessed = ?";

	private FinderPath
		_finderPathWithPaginationFindByEventAndIsCriticalProcessed;
	private FinderPath
		_finderPathWithoutPaginationFindByEventAndIsCriticalProcessed;
	private FinderPath _finderPathCountByEventAndIsCriticalProcessed;

	/**
	 * Returns all the ntuc sbs where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @return the matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed) {

		return findByEventAndIsCriticalProcessed(
			event, isCriticalProcessed, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the ntuc sbs where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed, int start, int end) {

		return findByEventAndIsCriticalProcessed(
			event, isCriticalProcessed, start, end, null);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return findByEventAndIsCriticalProcessed(
			event, isCriticalProcessed, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator, boolean useFinderCache) {

		event = Objects.toString(event, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByEventAndIsCriticalProcessed;
				finderArgs = new Object[] {event, isCriticalProcessed};
			}
		}
		else if (useFinderCache) {
			finderPath =
				_finderPathWithPaginationFindByEventAndIsCriticalProcessed;
			finderArgs = new Object[] {
				event, isCriticalProcessed, start, end, orderByComparator
			};
		}

		List<NtucSB> list = null;

		if (useFinderCache) {
			list = (List<NtucSB>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (NtucSB ntucSB : list) {
					if (!event.equals(ntucSB.getEvent()) ||
						(isCriticalProcessed !=
							ntucSB.isIsCriticalProcessed())) {

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

			sb.append(_SQL_SELECT_NTUCSB_WHERE);

			boolean bindEvent = false;

			if (event.isEmpty()) {
				sb.append(_FINDER_COLUMN_EVENTANDISCRITICALPROCESSED_EVENT_3);
			}
			else {
				bindEvent = true;

				sb.append(_FINDER_COLUMN_EVENTANDISCRITICALPROCESSED_EVENT_2);
			}

			sb.append(
				_FINDER_COLUMN_EVENTANDISCRITICALPROCESSED_ISCRITICALPROCESSED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(NtucSBModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindEvent) {
					queryPos.add(event);
				}

				queryPos.add(isCriticalProcessed);

				list = (List<NtucSB>)QueryUtil.list(
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
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB findByEventAndIsCriticalProcessed_First(
			String event, boolean isCriticalProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByEventAndIsCriticalProcessed_First(
			event, isCriticalProcessed, orderByComparator);

		if (ntucSB != null) {
			return ntucSB;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("event=");
		sb.append(event);

		sb.append(", isCriticalProcessed=");
		sb.append(isCriticalProcessed);

		sb.append("}");

		throw new NoSuchNtucSBException(sb.toString());
	}

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB fetchByEventAndIsCriticalProcessed_First(
		String event, boolean isCriticalProcessed,
		OrderByComparator<NtucSB> orderByComparator) {

		List<NtucSB> list = findByEventAndIsCriticalProcessed(
			event, isCriticalProcessed, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB findByEventAndIsCriticalProcessed_Last(
			String event, boolean isCriticalProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByEventAndIsCriticalProcessed_Last(
			event, isCriticalProcessed, orderByComparator);

		if (ntucSB != null) {
			return ntucSB;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("event=");
		sb.append(event);

		sb.append(", isCriticalProcessed=");
		sb.append(isCriticalProcessed);

		sb.append("}");

		throw new NoSuchNtucSBException(sb.toString());
	}

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB fetchByEventAndIsCriticalProcessed_Last(
		String event, boolean isCriticalProcessed,
		OrderByComparator<NtucSB> orderByComparator) {

		int count = countByEventAndIsCriticalProcessed(
			event, isCriticalProcessed);

		if (count == 0) {
			return null;
		}

		List<NtucSB> list = findByEventAndIsCriticalProcessed(
			event, isCriticalProcessed, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	@Override
	public NtucSB[] findByEventAndIsCriticalProcessed_PrevAndNext(
			long ntucDTId, String event, boolean isCriticalProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		event = Objects.toString(event, "");

		NtucSB ntucSB = findByPrimaryKey(ntucDTId);

		Session session = null;

		try {
			session = openSession();

			NtucSB[] array = new NtucSBImpl[3];

			array[0] = getByEventAndIsCriticalProcessed_PrevAndNext(
				session, ntucSB, event, isCriticalProcessed, orderByComparator,
				true);

			array[1] = ntucSB;

			array[2] = getByEventAndIsCriticalProcessed_PrevAndNext(
				session, ntucSB, event, isCriticalProcessed, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected NtucSB getByEventAndIsCriticalProcessed_PrevAndNext(
		Session session, NtucSB ntucSB, String event,
		boolean isCriticalProcessed,
		OrderByComparator<NtucSB> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_NTUCSB_WHERE);

		boolean bindEvent = false;

		if (event.isEmpty()) {
			sb.append(_FINDER_COLUMN_EVENTANDISCRITICALPROCESSED_EVENT_3);
		}
		else {
			bindEvent = true;

			sb.append(_FINDER_COLUMN_EVENTANDISCRITICALPROCESSED_EVENT_2);
		}

		sb.append(
			_FINDER_COLUMN_EVENTANDISCRITICALPROCESSED_ISCRITICALPROCESSED_2);

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
			sb.append(NtucSBModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindEvent) {
			queryPos.add(event);
		}

		queryPos.add(isCriticalProcessed);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(ntucSB)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<NtucSB> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the ntuc sbs where event = &#63; and isCriticalProcessed = &#63; from the database.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 */
	@Override
	public void removeByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed) {

		for (NtucSB ntucSB :
				findByEventAndIsCriticalProcessed(
					event, isCriticalProcessed, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(ntucSB);
		}
	}

	/**
	 * Returns the number of ntuc sbs where event = &#63; and isCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isCriticalProcessed the is critical processed
	 * @return the number of matching ntuc sbs
	 */
	@Override
	public int countByEventAndIsCriticalProcessed(
		String event, boolean isCriticalProcessed) {

		event = Objects.toString(event, "");

		FinderPath finderPath = _finderPathCountByEventAndIsCriticalProcessed;

		Object[] finderArgs = new Object[] {event, isCriticalProcessed};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_NTUCSB_WHERE);

			boolean bindEvent = false;

			if (event.isEmpty()) {
				sb.append(_FINDER_COLUMN_EVENTANDISCRITICALPROCESSED_EVENT_3);
			}
			else {
				bindEvent = true;

				sb.append(_FINDER_COLUMN_EVENTANDISCRITICALPROCESSED_EVENT_2);
			}

			sb.append(
				_FINDER_COLUMN_EVENTANDISCRITICALPROCESSED_ISCRITICALPROCESSED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindEvent) {
					queryPos.add(event);
				}

				queryPos.add(isCriticalProcessed);

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
		_FINDER_COLUMN_EVENTANDISCRITICALPROCESSED_EVENT_2 =
			"ntucSB.event = ? AND ";

	private static final String
		_FINDER_COLUMN_EVENTANDISCRITICALPROCESSED_EVENT_3 =
			"(ntucSB.event IS NULL OR ntucSB.event = '') AND ";

	private static final String
		_FINDER_COLUMN_EVENTANDISCRITICALPROCESSED_ISCRITICALPROCESSED_2 =
			"ntucSB.isCriticalProcessed = ?";

	private FinderPath
		_finderPathWithPaginationFindByEventAndIsNonCriticalProcessed;
	private FinderPath
		_finderPathWithoutPaginationFindByEventAndIsNonCriticalProcessed;
	private FinderPath _finderPathCountByEventAndIsNonCriticalProcessed;

	/**
	 * Returns all the ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @return the matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed) {

		return findByEventAndIsNonCriticalProcessed(
			event, isNonCriticalProcessed, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed, int start, int end) {

		return findByEventAndIsNonCriticalProcessed(
			event, isNonCriticalProcessed, start, end, null);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator) {

		return findByEventAndIsNonCriticalProcessed(
			event, isNonCriticalProcessed, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching ntuc sbs
	 */
	@Override
	public List<NtucSB> findByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed, int start, int end,
		OrderByComparator<NtucSB> orderByComparator, boolean useFinderCache) {

		event = Objects.toString(event, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByEventAndIsNonCriticalProcessed;
				finderArgs = new Object[] {event, isNonCriticalProcessed};
			}
		}
		else if (useFinderCache) {
			finderPath =
				_finderPathWithPaginationFindByEventAndIsNonCriticalProcessed;
			finderArgs = new Object[] {
				event, isNonCriticalProcessed, start, end, orderByComparator
			};
		}

		List<NtucSB> list = null;

		if (useFinderCache) {
			list = (List<NtucSB>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (NtucSB ntucSB : list) {
					if (!event.equals(ntucSB.getEvent()) ||
						(isNonCriticalProcessed !=
							ntucSB.isIsNonCriticalProcessed())) {

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

			sb.append(_SQL_SELECT_NTUCSB_WHERE);

			boolean bindEvent = false;

			if (event.isEmpty()) {
				sb.append(
					_FINDER_COLUMN_EVENTANDISNONCRITICALPROCESSED_EVENT_3);
			}
			else {
				bindEvent = true;

				sb.append(
					_FINDER_COLUMN_EVENTANDISNONCRITICALPROCESSED_EVENT_2);
			}

			sb.append(
				_FINDER_COLUMN_EVENTANDISNONCRITICALPROCESSED_ISNONCRITICALPROCESSED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(NtucSBModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindEvent) {
					queryPos.add(event);
				}

				queryPos.add(isNonCriticalProcessed);

				list = (List<NtucSB>)QueryUtil.list(
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
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB findByEventAndIsNonCriticalProcessed_First(
			String event, boolean isNonCriticalProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByEventAndIsNonCriticalProcessed_First(
			event, isNonCriticalProcessed, orderByComparator);

		if (ntucSB != null) {
			return ntucSB;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("event=");
		sb.append(event);

		sb.append(", isNonCriticalProcessed=");
		sb.append(isNonCriticalProcessed);

		sb.append("}");

		throw new NoSuchNtucSBException(sb.toString());
	}

	/**
	 * Returns the first ntuc sb in the ordered set where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB fetchByEventAndIsNonCriticalProcessed_First(
		String event, boolean isNonCriticalProcessed,
		OrderByComparator<NtucSB> orderByComparator) {

		List<NtucSB> list = findByEventAndIsNonCriticalProcessed(
			event, isNonCriticalProcessed, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb
	 * @throws NoSuchNtucSBException if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB findByEventAndIsNonCriticalProcessed_Last(
			String event, boolean isNonCriticalProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByEventAndIsNonCriticalProcessed_Last(
			event, isNonCriticalProcessed, orderByComparator);

		if (ntucSB != null) {
			return ntucSB;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("event=");
		sb.append(event);

		sb.append(", isNonCriticalProcessed=");
		sb.append(isNonCriticalProcessed);

		sb.append("}");

		throw new NoSuchNtucSBException(sb.toString());
	}

	/**
	 * Returns the last ntuc sb in the ordered set where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching ntuc sb, or <code>null</code> if a matching ntuc sb could not be found
	 */
	@Override
	public NtucSB fetchByEventAndIsNonCriticalProcessed_Last(
		String event, boolean isNonCriticalProcessed,
		OrderByComparator<NtucSB> orderByComparator) {

		int count = countByEventAndIsNonCriticalProcessed(
			event, isNonCriticalProcessed);

		if (count == 0) {
			return null;
		}

		List<NtucSB> list = findByEventAndIsNonCriticalProcessed(
			event, isNonCriticalProcessed, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the ntuc sbs before and after the current ntuc sb in the ordered set where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param ntucDTId the primary key of the current ntuc sb
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	@Override
	public NtucSB[] findByEventAndIsNonCriticalProcessed_PrevAndNext(
			long ntucDTId, String event, boolean isNonCriticalProcessed,
			OrderByComparator<NtucSB> orderByComparator)
		throws NoSuchNtucSBException {

		event = Objects.toString(event, "");

		NtucSB ntucSB = findByPrimaryKey(ntucDTId);

		Session session = null;

		try {
			session = openSession();

			NtucSB[] array = new NtucSBImpl[3];

			array[0] = getByEventAndIsNonCriticalProcessed_PrevAndNext(
				session, ntucSB, event, isNonCriticalProcessed,
				orderByComparator, true);

			array[1] = ntucSB;

			array[2] = getByEventAndIsNonCriticalProcessed_PrevAndNext(
				session, ntucSB, event, isNonCriticalProcessed,
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

	protected NtucSB getByEventAndIsNonCriticalProcessed_PrevAndNext(
		Session session, NtucSB ntucSB, String event,
		boolean isNonCriticalProcessed,
		OrderByComparator<NtucSB> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_NTUCSB_WHERE);

		boolean bindEvent = false;

		if (event.isEmpty()) {
			sb.append(_FINDER_COLUMN_EVENTANDISNONCRITICALPROCESSED_EVENT_3);
		}
		else {
			bindEvent = true;

			sb.append(_FINDER_COLUMN_EVENTANDISNONCRITICALPROCESSED_EVENT_2);
		}

		sb.append(
			_FINDER_COLUMN_EVENTANDISNONCRITICALPROCESSED_ISNONCRITICALPROCESSED_2);

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
			sb.append(NtucSBModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindEvent) {
			queryPos.add(event);
		}

		queryPos.add(isNonCriticalProcessed);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(ntucSB)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<NtucSB> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63; from the database.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 */
	@Override
	public void removeByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed) {

		for (NtucSB ntucSB :
				findByEventAndIsNonCriticalProcessed(
					event, isNonCriticalProcessed, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(ntucSB);
		}
	}

	/**
	 * Returns the number of ntuc sbs where event = &#63; and isNonCriticalProcessed = &#63;.
	 *
	 * @param event the event
	 * @param isNonCriticalProcessed the is non critical processed
	 * @return the number of matching ntuc sbs
	 */
	@Override
	public int countByEventAndIsNonCriticalProcessed(
		String event, boolean isNonCriticalProcessed) {

		event = Objects.toString(event, "");

		FinderPath finderPath =
			_finderPathCountByEventAndIsNonCriticalProcessed;

		Object[] finderArgs = new Object[] {event, isNonCriticalProcessed};

		Long count = (Long)dummyFinderCache.getResult(
			finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_NTUCSB_WHERE);

			boolean bindEvent = false;

			if (event.isEmpty()) {
				sb.append(
					_FINDER_COLUMN_EVENTANDISNONCRITICALPROCESSED_EVENT_3);
			}
			else {
				bindEvent = true;

				sb.append(
					_FINDER_COLUMN_EVENTANDISNONCRITICALPROCESSED_EVENT_2);
			}

			sb.append(
				_FINDER_COLUMN_EVENTANDISNONCRITICALPROCESSED_ISNONCRITICALPROCESSED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindEvent) {
					queryPos.add(event);
				}

				queryPos.add(isNonCriticalProcessed);

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
		_FINDER_COLUMN_EVENTANDISNONCRITICALPROCESSED_EVENT_2 =
			"ntucSB.event = ? AND ";

	private static final String
		_FINDER_COLUMN_EVENTANDISNONCRITICALPROCESSED_EVENT_3 =
			"(ntucSB.event IS NULL OR ntucSB.event = '') AND ";

	private static final String
		_FINDER_COLUMN_EVENTANDISNONCRITICALPROCESSED_ISNONCRITICALPROCESSED_2 =
			"ntucSB.isNonCriticalProcessed = ?";

	public NtucSBPersistenceImpl() {
		setModelClass(NtucSB.class);

		setModelImplClass(NtucSBImpl.class);
		setModelPKClass(long.class);
	}

	/**
	 * Caches the ntuc sb in the entity cache if it is enabled.
	 *
	 * @param ntucSB the ntuc sb
	 */
	@Override
	public void cacheResult(NtucSB ntucSB) {
		dummyEntityCache.putResult(
			NtucSBImpl.class, ntucSB.getPrimaryKey(), ntucSB);
	}

	/**
	 * Caches the ntuc sbs in the entity cache if it is enabled.
	 *
	 * @param ntucSBs the ntuc sbs
	 */
	@Override
	public void cacheResult(List<NtucSB> ntucSBs) {
		for (NtucSB ntucSB : ntucSBs) {
			if (dummyEntityCache.getResult(
					NtucSBImpl.class, ntucSB.getPrimaryKey()) == null) {

				cacheResult(ntucSB);
			}
		}
	}

	/**
	 * Clears the cache for all ntuc sbs.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>com.liferay.portal.kernel.dao.orm.FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		dummyEntityCache.clearCache(NtucSBImpl.class);

		dummyFinderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		dummyFinderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		dummyFinderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the ntuc sb.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>com.liferay.portal.kernel.dao.orm.FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(NtucSB ntucSB) {
		dummyEntityCache.removeResult(NtucSBImpl.class, ntucSB);
	}

	@Override
	public void clearCache(List<NtucSB> ntucSBs) {
		for (NtucSB ntucSB : ntucSBs) {
			dummyEntityCache.removeResult(NtucSBImpl.class, ntucSB);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		dummyFinderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		dummyFinderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		dummyFinderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			dummyEntityCache.removeResult(NtucSBImpl.class, primaryKey);
		}
	}

	/**
	 * Creates a new ntuc sb with the primary key. Does not add the ntuc sb to the database.
	 *
	 * @param ntucDTId the primary key for the new ntuc sb
	 * @return the new ntuc sb
	 */
	@Override
	public NtucSB create(long ntucDTId) {
		NtucSB ntucSB = new NtucSBImpl();

		ntucSB.setNew(true);
		ntucSB.setPrimaryKey(ntucDTId);

		ntucSB.setCompanyId(CompanyThreadLocal.getCompanyId());

		return ntucSB;
	}

	/**
	 * Removes the ntuc sb with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param ntucDTId the primary key of the ntuc sb
	 * @return the ntuc sb that was removed
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	@Override
	public NtucSB remove(long ntucDTId) throws NoSuchNtucSBException {
		return remove((Serializable)ntucDTId);
	}

	/**
	 * Removes the ntuc sb with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the ntuc sb
	 * @return the ntuc sb that was removed
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	@Override
	public NtucSB remove(Serializable primaryKey) throws NoSuchNtucSBException {
		Session session = null;

		try {
			session = openSession();

			NtucSB ntucSB = (NtucSB)session.get(NtucSBImpl.class, primaryKey);

			if (ntucSB == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchNtucSBException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(ntucSB);
		}
		catch (NoSuchNtucSBException noSuchEntityException) {
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
	protected NtucSB removeImpl(NtucSB ntucSB) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(ntucSB)) {
				ntucSB = (NtucSB)session.get(
					NtucSBImpl.class, ntucSB.getPrimaryKeyObj());
			}

			if (ntucSB != null) {
				session.delete(ntucSB);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (ntucSB != null) {
			clearCache(ntucSB);
		}

		return ntucSB;
	}

	@Override
	public NtucSB updateImpl(NtucSB ntucSB) {
		boolean isNew = ntucSB.isNew();

		if (!(ntucSB instanceof NtucSBModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(ntucSB.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(ntucSB);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in ntucSB proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom NtucSB implementation " +
					ntucSB.getClass());
		}

		NtucSBModelImpl ntucSBModelImpl = (NtucSBModelImpl)ntucSB;

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(ntucSB);
			}
			else {
				ntucSB = (NtucSB)session.merge(ntucSB);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		dummyEntityCache.putResult(
			NtucSBImpl.class, ntucSBModelImpl, false, true);

		if (isNew) {
			ntucSB.setNew(false);
		}

		ntucSB.resetOriginalValues();

		return ntucSB;
	}

	/**
	 * Returns the ntuc sb with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the ntuc sb
	 * @return the ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	@Override
	public NtucSB findByPrimaryKey(Serializable primaryKey)
		throws NoSuchNtucSBException {

		NtucSB ntucSB = fetchByPrimaryKey(primaryKey);

		if (ntucSB == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchNtucSBException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return ntucSB;
	}

	/**
	 * Returns the ntuc sb with the primary key or throws a <code>NoSuchNtucSBException</code> if it could not be found.
	 *
	 * @param ntucDTId the primary key of the ntuc sb
	 * @return the ntuc sb
	 * @throws NoSuchNtucSBException if a ntuc sb with the primary key could not be found
	 */
	@Override
	public NtucSB findByPrimaryKey(long ntucDTId) throws NoSuchNtucSBException {
		return findByPrimaryKey((Serializable)ntucDTId);
	}

	/**
	 * Returns the ntuc sb with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param ntucDTId the primary key of the ntuc sb
	 * @return the ntuc sb, or <code>null</code> if a ntuc sb with the primary key could not be found
	 */
	@Override
	public NtucSB fetchByPrimaryKey(long ntucDTId) {
		return fetchByPrimaryKey((Serializable)ntucDTId);
	}

	/**
	 * Returns all the ntuc sbs.
	 *
	 * @return the ntuc sbs
	 */
	@Override
	public List<NtucSB> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the ntuc sbs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @return the range of ntuc sbs
	 */
	@Override
	public List<NtucSB> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of ntuc sbs
	 */
	@Override
	public List<NtucSB> findAll(
		int start, int end, OrderByComparator<NtucSB> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the ntuc sbs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>NtucSBModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ntuc sbs
	 * @param end the upper bound of the range of ntuc sbs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of ntuc sbs
	 */
	@Override
	public List<NtucSB> findAll(
		int start, int end, OrderByComparator<NtucSB> orderByComparator,
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

		List<NtucSB> list = null;

		if (useFinderCache) {
			list = (List<NtucSB>)dummyFinderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_NTUCSB);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_NTUCSB;

				sql = sql.concat(NtucSBModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<NtucSB>)QueryUtil.list(
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
	 * Removes all the ntuc sbs from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (NtucSB ntucSB : findAll()) {
			remove(ntucSB);
		}
	}

	/**
	 * Returns the number of ntuc sbs.
	 *
	 * @return the number of ntuc sbs
	 */
	@Override
	public int countAll() {
		Long count = (Long)dummyFinderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_NTUCSB);

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
		return "ntucDTId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_NTUCSB;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return NtucSBModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the ntuc sb persistence.
	 */
	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_argumentsResolverServiceRegistration = _bundleContext.registerService(
			ArgumentsResolver.class, new NtucSBModelArgumentsResolver(),
			MapUtil.singletonDictionary(
				"model.class.name", NtucSB.class.getName()));

		_finderPathWithPaginationFindAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathWithPaginationFindByisRowLockFailed = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByisRowLockFailed",
			new String[] {
				Boolean.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"isRowLockFailed"}, true);

		_finderPathWithoutPaginationFindByisRowLockFailed = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByisRowLockFailed",
			new String[] {Boolean.class.getName()},
			new String[] {"isRowLockFailed"}, true);

		_finderPathCountByisRowLockFailed = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByisRowLockFailed",
			new String[] {Boolean.class.getName()},
			new String[] {"isRowLockFailed"}, false);

		_finderPathWithPaginationFindByCourseCodeAndEvent = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCourseCodeAndEvent",
			new String[] {
				String.class.getName(), String.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"courseCode", "event"}, true);

		_finderPathWithoutPaginationFindByCourseCodeAndEvent =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
				"findByCourseCodeAndEvent",
				new String[] {String.class.getName(), String.class.getName()},
				new String[] {"courseCode", "event"}, true);

		_finderPathCountByCourseCodeAndEvent = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByCourseCodeAndEvent",
			new String[] {String.class.getName(), String.class.getName()},
			new String[] {"courseCode", "event"}, false);

		_finderPathWithPaginationFindByCourseCodeEventAndChangeFrom =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
				"findByCourseCodeEventAndChangeFrom",
				new String[] {
					String.class.getName(), String.class.getName(),
					String.class.getName(), Integer.class.getName(),
					Integer.class.getName(), OrderByComparator.class.getName()
				},
				new String[] {"courseCode", "event", "changeFrom"}, true);

		_finderPathWithPaginationCountByCourseCodeEventAndChangeFrom =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
				"countByCourseCodeEventAndChangeFrom",
				new String[] {
					String.class.getName(), String.class.getName(),
					String.class.getName()
				},
				new String[] {"courseCode", "event", "changeFrom"}, false);

		_finderPathWithPaginationFindByRecordsByChangeFrom = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByRecordsByChangeFrom",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"changeFrom"}, true);

		_finderPathWithPaginationCountByRecordsByChangeFrom = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"countByRecordsByChangeFrom", new String[] {String.class.getName()},
			new String[] {"changeFrom"}, false);

		_finderPathWithPaginationFindByStatusAndIsCronProcessed =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
				"findByStatusAndIsCronProcessed",
				new String[] {
					String.class.getName(), Boolean.class.getName(),
					Integer.class.getName(), Integer.class.getName(),
					OrderByComparator.class.getName()
				},
				new String[] {"event", "isCronProcessed"}, true);

		_finderPathWithoutPaginationFindByStatusAndIsCronProcessed =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
				"findByStatusAndIsCronProcessed",
				new String[] {String.class.getName(), Boolean.class.getName()},
				new String[] {"event", "isCronProcessed"}, true);

		_finderPathCountByStatusAndIsCronProcessed = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByStatusAndIsCronProcessed",
			new String[] {String.class.getName(), Boolean.class.getName()},
			new String[] {"event", "isCronProcessed"}, false);

		_finderPathWithPaginationFindByEventAndIsCriticalProcessed =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
				"findByEventAndIsCriticalProcessed",
				new String[] {
					String.class.getName(), Boolean.class.getName(),
					Integer.class.getName(), Integer.class.getName(),
					OrderByComparator.class.getName()
				},
				new String[] {"event", "isCriticalProcessed"}, true);

		_finderPathWithoutPaginationFindByEventAndIsCriticalProcessed =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
				"findByEventAndIsCriticalProcessed",
				new String[] {String.class.getName(), Boolean.class.getName()},
				new String[] {"event", "isCriticalProcessed"}, true);

		_finderPathCountByEventAndIsCriticalProcessed = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByEventAndIsCriticalProcessed",
			new String[] {String.class.getName(), Boolean.class.getName()},
			new String[] {"event", "isCriticalProcessed"}, false);

		_finderPathWithPaginationFindByEventAndIsNonCriticalProcessed =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
				"findByEventAndIsNonCriticalProcessed",
				new String[] {
					String.class.getName(), Boolean.class.getName(),
					Integer.class.getName(), Integer.class.getName(),
					OrderByComparator.class.getName()
				},
				new String[] {"event", "isNonCriticalProcessed"}, true);

		_finderPathWithoutPaginationFindByEventAndIsNonCriticalProcessed =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
				"findByEventAndIsNonCriticalProcessed",
				new String[] {String.class.getName(), Boolean.class.getName()},
				new String[] {"event", "isNonCriticalProcessed"}, true);

		_finderPathCountByEventAndIsNonCriticalProcessed = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByEventAndIsNonCriticalProcessed",
			new String[] {String.class.getName(), Boolean.class.getName()},
			new String[] {"event", "isNonCriticalProcessed"}, false);
	}

	@Deactivate
	public void deactivate() {
		dummyEntityCache.removeCache(NtucSBImpl.class.getName());

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

	private static final String _SQL_SELECT_NTUCSB =
		"SELECT ntucSB FROM NtucSB ntucSB";

	private static final String _SQL_SELECT_NTUCSB_WHERE =
		"SELECT ntucSB FROM NtucSB ntucSB WHERE ";

	private static final String _SQL_COUNT_NTUCSB =
		"SELECT COUNT(ntucSB) FROM NtucSB ntucSB";

	private static final String _SQL_COUNT_NTUCSB_WHERE =
		"SELECT COUNT(ntucSB) FROM NtucSB ntucSB WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "ntucSB.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No NtucSB exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No NtucSB exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		NtucSBPersistenceImpl.class);

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

	private static class NtucSBModelArgumentsResolver
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

			NtucSBModelImpl ntucSBModelImpl = (NtucSBModelImpl)baseModel;

			long columnBitmask = ntucSBModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(ntucSBModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |= ntucSBModelImpl.getColumnBitmask(
						columnName);
				}

				_finderPathColumnBitmasksCache.put(
					finderPath, finderPathColumnBitmask);
			}

			if ((columnBitmask & finderPathColumnBitmask) != 0) {
				return _getValue(ntucSBModelImpl, columnNames, original);
			}

			return null;
		}

		private Object[] _getValue(
			NtucSBModelImpl ntucSBModelImpl, String[] columnNames,
			boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] = ntucSBModelImpl.getColumnOriginalValue(
						columnName);
				}
				else {
					arguments[i] = ntucSBModelImpl.getColumnValue(columnName);
				}
			}

			return arguments;
		}

		private static Map<FinderPath, Long> _finderPathColumnBitmasksCache =
			new ConcurrentHashMap<>();

	}

}