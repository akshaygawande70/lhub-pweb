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

package svc.ntuc.nlh.parameter.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
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

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
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

import svc.ntuc.nlh.parameter.exception.NoSuchReservedParameterException;
import svc.ntuc.nlh.parameter.model.ReservedParameter;
import svc.ntuc.nlh.parameter.model.impl.ReservedParameterImpl;
import svc.ntuc.nlh.parameter.model.impl.ReservedParameterModelImpl;
import svc.ntuc.nlh.parameter.service.persistence.ReservedParameterPersistence;
import svc.ntuc.nlh.parameter.service.persistence.impl.constants.NTUC_PARAMETERPersistenceConstants;

/**
 * The persistence implementation for the reserved parameter service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = ReservedParameterPersistence.class)
public class ReservedParameterPersistenceImpl
	extends BasePersistenceImpl<ReservedParameter>
	implements ReservedParameterPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>ReservedParameterUtil</code> to access the reserved parameter persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		ReservedParameterImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByGroup;
	private FinderPath _finderPathWithoutPaginationFindByGroup;
	private FinderPath _finderPathCountByGroup;

	/**
	 * Returns all the reserved parameters where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @return the matching reserved parameters
	 */
	@Override
	public List<ReservedParameter> findByGroup(long parameterGroupId) {
		return findByGroup(
			parameterGroupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the reserved parameters where parameterGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @return the range of matching reserved parameters
	 */
	@Override
	public List<ReservedParameter> findByGroup(
		long parameterGroupId, int start, int end) {

		return findByGroup(parameterGroupId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the reserved parameters where parameterGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching reserved parameters
	 */
	@Override
	public List<ReservedParameter> findByGroup(
		long parameterGroupId, int start, int end,
		OrderByComparator<ReservedParameter> orderByComparator) {

		return findByGroup(
			parameterGroupId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the reserved parameters where parameterGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching reserved parameters
	 */
	@Override
	public List<ReservedParameter> findByGroup(
		long parameterGroupId, int start, int end,
		OrderByComparator<ReservedParameter> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByGroup;
				finderArgs = new Object[] {parameterGroupId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByGroup;
			finderArgs = new Object[] {
				parameterGroupId, start, end, orderByComparator
			};
		}

		List<ReservedParameter> list = null;

		if (useFinderCache) {
			list = (List<ReservedParameter>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (ReservedParameter reservedParameter : list) {
					if (parameterGroupId !=
							reservedParameter.getParameterGroupId()) {

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

			sb.append(_SQL_SELECT_RESERVEDPARAMETER_WHERE);

			sb.append(_FINDER_COLUMN_GROUP_PARAMETERGROUPID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(ReservedParameterModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(parameterGroupId);

				list = (List<ReservedParameter>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
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
	 * Returns the first reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	@Override
	public ReservedParameter findByGroup_First(
			long parameterGroupId,
			OrderByComparator<ReservedParameter> orderByComparator)
		throws NoSuchReservedParameterException {

		ReservedParameter reservedParameter = fetchByGroup_First(
			parameterGroupId, orderByComparator);

		if (reservedParameter != null) {
			return reservedParameter;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("parameterGroupId=");
		sb.append(parameterGroupId);

		sb.append("}");

		throw new NoSuchReservedParameterException(sb.toString());
	}

	/**
	 * Returns the first reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	@Override
	public ReservedParameter fetchByGroup_First(
		long parameterGroupId,
		OrderByComparator<ReservedParameter> orderByComparator) {

		List<ReservedParameter> list = findByGroup(
			parameterGroupId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	@Override
	public ReservedParameter findByGroup_Last(
			long parameterGroupId,
			OrderByComparator<ReservedParameter> orderByComparator)
		throws NoSuchReservedParameterException {

		ReservedParameter reservedParameter = fetchByGroup_Last(
			parameterGroupId, orderByComparator);

		if (reservedParameter != null) {
			return reservedParameter;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("parameterGroupId=");
		sb.append(parameterGroupId);

		sb.append("}");

		throw new NoSuchReservedParameterException(sb.toString());
	}

	/**
	 * Returns the last reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	@Override
	public ReservedParameter fetchByGroup_Last(
		long parameterGroupId,
		OrderByComparator<ReservedParameter> orderByComparator) {

		int count = countByGroup(parameterGroupId);

		if (count == 0) {
			return null;
		}

		List<ReservedParameter> list = findByGroup(
			parameterGroupId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the reserved parameters before and after the current reserved parameter in the ordered set where parameterGroupId = &#63;.
	 *
	 * @param reservedParameterId the primary key of the current reserved parameter
	 * @param parameterGroupId the parameter group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next reserved parameter
	 * @throws NoSuchReservedParameterException if a reserved parameter with the primary key could not be found
	 */
	@Override
	public ReservedParameter[] findByGroup_PrevAndNext(
			long reservedParameterId, long parameterGroupId,
			OrderByComparator<ReservedParameter> orderByComparator)
		throws NoSuchReservedParameterException {

		ReservedParameter reservedParameter = findByPrimaryKey(
			reservedParameterId);

		Session session = null;

		try {
			session = openSession();

			ReservedParameter[] array = new ReservedParameterImpl[3];

			array[0] = getByGroup_PrevAndNext(
				session, reservedParameter, parameterGroupId, orderByComparator,
				true);

			array[1] = reservedParameter;

			array[2] = getByGroup_PrevAndNext(
				session, reservedParameter, parameterGroupId, orderByComparator,
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

	protected ReservedParameter getByGroup_PrevAndNext(
		Session session, ReservedParameter reservedParameter,
		long parameterGroupId,
		OrderByComparator<ReservedParameter> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_RESERVEDPARAMETER_WHERE);

		sb.append(_FINDER_COLUMN_GROUP_PARAMETERGROUPID_2);

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
			sb.append(ReservedParameterModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(parameterGroupId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						reservedParameter)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<ReservedParameter> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the reserved parameters where parameterGroupId = &#63; from the database.
	 *
	 * @param parameterGroupId the parameter group ID
	 */
	@Override
	public void removeByGroup(long parameterGroupId) {
		for (ReservedParameter reservedParameter :
				findByGroup(
					parameterGroupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(reservedParameter);
		}
	}

	/**
	 * Returns the number of reserved parameters where parameterGroupId = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @return the number of matching reserved parameters
	 */
	@Override
	public int countByGroup(long parameterGroupId) {
		FinderPath finderPath = _finderPathCountByGroup;

		Object[] finderArgs = new Object[] {parameterGroupId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_RESERVEDPARAMETER_WHERE);

			sb.append(_FINDER_COLUMN_GROUP_PARAMETERGROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(parameterGroupId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
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

	private static final String _FINDER_COLUMN_GROUP_PARAMETERGROUPID_2 =
		"reservedParameter.parameterGroupId = ?";

	private FinderPath _finderPathFetchByCode;
	private FinderPath _finderPathCountByCode;

	/**
	 * Returns the reserved parameter where paramCode = &#63; or throws a <code>NoSuchReservedParameterException</code> if it could not be found.
	 *
	 * @param paramCode the param code
	 * @return the matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	@Override
	public ReservedParameter findByCode(String paramCode)
		throws NoSuchReservedParameterException {

		ReservedParameter reservedParameter = fetchByCode(paramCode);

		if (reservedParameter == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("paramCode=");
			sb.append(paramCode);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchReservedParameterException(sb.toString());
		}

		return reservedParameter;
	}

	/**
	 * Returns the reserved parameter where paramCode = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param paramCode the param code
	 * @return the matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	@Override
	public ReservedParameter fetchByCode(String paramCode) {
		return fetchByCode(paramCode, true);
	}

	/**
	 * Returns the reserved parameter where paramCode = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param paramCode the param code
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	@Override
	public ReservedParameter fetchByCode(
		String paramCode, boolean useFinderCache) {

		paramCode = Objects.toString(paramCode, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {paramCode};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByCode, finderArgs, this);
		}

		if (result instanceof ReservedParameter) {
			ReservedParameter reservedParameter = (ReservedParameter)result;

			if (!Objects.equals(paramCode, reservedParameter.getParamCode())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_SELECT_RESERVEDPARAMETER_WHERE);

			boolean bindParamCode = false;

			if (paramCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_CODE_PARAMCODE_3);
			}
			else {
				bindParamCode = true;

				sb.append(_FINDER_COLUMN_CODE_PARAMCODE_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindParamCode) {
					queryPos.add(paramCode);
				}

				List<ReservedParameter> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByCode, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {paramCode};
							}

							_log.warn(
								"ReservedParameterPersistenceImpl.fetchByCode(String, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					ReservedParameter reservedParameter = list.get(0);

					result = reservedParameter;

					cacheResult(reservedParameter);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (ReservedParameter)result;
		}
	}

	/**
	 * Removes the reserved parameter where paramCode = &#63; from the database.
	 *
	 * @param paramCode the param code
	 * @return the reserved parameter that was removed
	 */
	@Override
	public ReservedParameter removeByCode(String paramCode)
		throws NoSuchReservedParameterException {

		ReservedParameter reservedParameter = findByCode(paramCode);

		return remove(reservedParameter);
	}

	/**
	 * Returns the number of reserved parameters where paramCode = &#63;.
	 *
	 * @param paramCode the param code
	 * @return the number of matching reserved parameters
	 */
	@Override
	public int countByCode(String paramCode) {
		paramCode = Objects.toString(paramCode, "");

		FinderPath finderPath = _finderPathCountByCode;

		Object[] finderArgs = new Object[] {paramCode};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_RESERVEDPARAMETER_WHERE);

			boolean bindParamCode = false;

			if (paramCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_CODE_PARAMCODE_3);
			}
			else {
				bindParamCode = true;

				sb.append(_FINDER_COLUMN_CODE_PARAMCODE_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindParamCode) {
					queryPos.add(paramCode);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
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

	private static final String _FINDER_COLUMN_CODE_PARAMCODE_2 =
		"reservedParameter.paramCode = ?";

	private static final String _FINDER_COLUMN_CODE_PARAMCODE_3 =
		"(reservedParameter.paramCode IS NULL OR reservedParameter.paramCode = '')";

	private FinderPath _finderPathWithPaginationFindByReservedBy;
	private FinderPath _finderPathWithoutPaginationFindByReservedBy;
	private FinderPath _finderPathCountByReservedBy;

	/**
	 * Returns all the reserved parameters where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @return the matching reserved parameters
	 */
	@Override
	public List<ReservedParameter> findByReservedBy(String reservedBy) {
		return findByReservedBy(
			reservedBy, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the reserved parameters where reservedBy = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param reservedBy the reserved by
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @return the range of matching reserved parameters
	 */
	@Override
	public List<ReservedParameter> findByReservedBy(
		String reservedBy, int start, int end) {

		return findByReservedBy(reservedBy, start, end, null);
	}

	/**
	 * Returns an ordered range of all the reserved parameters where reservedBy = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param reservedBy the reserved by
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching reserved parameters
	 */
	@Override
	public List<ReservedParameter> findByReservedBy(
		String reservedBy, int start, int end,
		OrderByComparator<ReservedParameter> orderByComparator) {

		return findByReservedBy(
			reservedBy, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the reserved parameters where reservedBy = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param reservedBy the reserved by
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching reserved parameters
	 */
	@Override
	public List<ReservedParameter> findByReservedBy(
		String reservedBy, int start, int end,
		OrderByComparator<ReservedParameter> orderByComparator,
		boolean useFinderCache) {

		reservedBy = Objects.toString(reservedBy, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByReservedBy;
				finderArgs = new Object[] {reservedBy};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByReservedBy;
			finderArgs = new Object[] {
				reservedBy, start, end, orderByComparator
			};
		}

		List<ReservedParameter> list = null;

		if (useFinderCache) {
			list = (List<ReservedParameter>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (ReservedParameter reservedParameter : list) {
					if (!reservedBy.equals(reservedParameter.getReservedBy())) {
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

			sb.append(_SQL_SELECT_RESERVEDPARAMETER_WHERE);

			boolean bindReservedBy = false;

			if (reservedBy.isEmpty()) {
				sb.append(_FINDER_COLUMN_RESERVEDBY_RESERVEDBY_3);
			}
			else {
				bindReservedBy = true;

				sb.append(_FINDER_COLUMN_RESERVEDBY_RESERVEDBY_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(ReservedParameterModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindReservedBy) {
					queryPos.add(reservedBy);
				}

				list = (List<ReservedParameter>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
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
	 * Returns the first reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	@Override
	public ReservedParameter findByReservedBy_First(
			String reservedBy,
			OrderByComparator<ReservedParameter> orderByComparator)
		throws NoSuchReservedParameterException {

		ReservedParameter reservedParameter = fetchByReservedBy_First(
			reservedBy, orderByComparator);

		if (reservedParameter != null) {
			return reservedParameter;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("reservedBy=");
		sb.append(reservedBy);

		sb.append("}");

		throw new NoSuchReservedParameterException(sb.toString());
	}

	/**
	 * Returns the first reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	@Override
	public ReservedParameter fetchByReservedBy_First(
		String reservedBy,
		OrderByComparator<ReservedParameter> orderByComparator) {

		List<ReservedParameter> list = findByReservedBy(
			reservedBy, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching reserved parameter
	 * @throws NoSuchReservedParameterException if a matching reserved parameter could not be found
	 */
	@Override
	public ReservedParameter findByReservedBy_Last(
			String reservedBy,
			OrderByComparator<ReservedParameter> orderByComparator)
		throws NoSuchReservedParameterException {

		ReservedParameter reservedParameter = fetchByReservedBy_Last(
			reservedBy, orderByComparator);

		if (reservedParameter != null) {
			return reservedParameter;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("reservedBy=");
		sb.append(reservedBy);

		sb.append("}");

		throw new NoSuchReservedParameterException(sb.toString());
	}

	/**
	 * Returns the last reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching reserved parameter, or <code>null</code> if a matching reserved parameter could not be found
	 */
	@Override
	public ReservedParameter fetchByReservedBy_Last(
		String reservedBy,
		OrderByComparator<ReservedParameter> orderByComparator) {

		int count = countByReservedBy(reservedBy);

		if (count == 0) {
			return null;
		}

		List<ReservedParameter> list = findByReservedBy(
			reservedBy, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the reserved parameters before and after the current reserved parameter in the ordered set where reservedBy = &#63;.
	 *
	 * @param reservedParameterId the primary key of the current reserved parameter
	 * @param reservedBy the reserved by
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next reserved parameter
	 * @throws NoSuchReservedParameterException if a reserved parameter with the primary key could not be found
	 */
	@Override
	public ReservedParameter[] findByReservedBy_PrevAndNext(
			long reservedParameterId, String reservedBy,
			OrderByComparator<ReservedParameter> orderByComparator)
		throws NoSuchReservedParameterException {

		reservedBy = Objects.toString(reservedBy, "");

		ReservedParameter reservedParameter = findByPrimaryKey(
			reservedParameterId);

		Session session = null;

		try {
			session = openSession();

			ReservedParameter[] array = new ReservedParameterImpl[3];

			array[0] = getByReservedBy_PrevAndNext(
				session, reservedParameter, reservedBy, orderByComparator,
				true);

			array[1] = reservedParameter;

			array[2] = getByReservedBy_PrevAndNext(
				session, reservedParameter, reservedBy, orderByComparator,
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

	protected ReservedParameter getByReservedBy_PrevAndNext(
		Session session, ReservedParameter reservedParameter, String reservedBy,
		OrderByComparator<ReservedParameter> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_RESERVEDPARAMETER_WHERE);

		boolean bindReservedBy = false;

		if (reservedBy.isEmpty()) {
			sb.append(_FINDER_COLUMN_RESERVEDBY_RESERVEDBY_3);
		}
		else {
			bindReservedBy = true;

			sb.append(_FINDER_COLUMN_RESERVEDBY_RESERVEDBY_2);
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
			sb.append(ReservedParameterModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindReservedBy) {
			queryPos.add(reservedBy);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						reservedParameter)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<ReservedParameter> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the reserved parameters where reservedBy = &#63; from the database.
	 *
	 * @param reservedBy the reserved by
	 */
	@Override
	public void removeByReservedBy(String reservedBy) {
		for (ReservedParameter reservedParameter :
				findByReservedBy(
					reservedBy, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(reservedParameter);
		}
	}

	/**
	 * Returns the number of reserved parameters where reservedBy = &#63;.
	 *
	 * @param reservedBy the reserved by
	 * @return the number of matching reserved parameters
	 */
	@Override
	public int countByReservedBy(String reservedBy) {
		reservedBy = Objects.toString(reservedBy, "");

		FinderPath finderPath = _finderPathCountByReservedBy;

		Object[] finderArgs = new Object[] {reservedBy};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_RESERVEDPARAMETER_WHERE);

			boolean bindReservedBy = false;

			if (reservedBy.isEmpty()) {
				sb.append(_FINDER_COLUMN_RESERVEDBY_RESERVEDBY_3);
			}
			else {
				bindReservedBy = true;

				sb.append(_FINDER_COLUMN_RESERVEDBY_RESERVEDBY_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindReservedBy) {
					queryPos.add(reservedBy);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
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

	private static final String _FINDER_COLUMN_RESERVEDBY_RESERVEDBY_2 =
		"reservedParameter.reservedBy = ?";

	private static final String _FINDER_COLUMN_RESERVEDBY_RESERVEDBY_3 =
		"(reservedParameter.reservedBy IS NULL OR reservedParameter.reservedBy = '')";

	public ReservedParameterPersistenceImpl() {
		setModelClass(ReservedParameter.class);

		setModelImplClass(ReservedParameterImpl.class);
		setModelPKClass(long.class);
	}

	/**
	 * Caches the reserved parameter in the entity cache if it is enabled.
	 *
	 * @param reservedParameter the reserved parameter
	 */
	@Override
	public void cacheResult(ReservedParameter reservedParameter) {
		entityCache.putResult(
			ReservedParameterImpl.class, reservedParameter.getPrimaryKey(),
			reservedParameter);

		finderCache.putResult(
			_finderPathFetchByCode,
			new Object[] {reservedParameter.getParamCode()}, reservedParameter);
	}

	/**
	 * Caches the reserved parameters in the entity cache if it is enabled.
	 *
	 * @param reservedParameters the reserved parameters
	 */
	@Override
	public void cacheResult(List<ReservedParameter> reservedParameters) {
		for (ReservedParameter reservedParameter : reservedParameters) {
			if (entityCache.getResult(
					ReservedParameterImpl.class,
					reservedParameter.getPrimaryKey()) == null) {

				cacheResult(reservedParameter);
			}
		}
	}

	/**
	 * Clears the cache for all reserved parameters.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(ReservedParameterImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the reserved parameter.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(ReservedParameter reservedParameter) {
		entityCache.removeResult(
			ReservedParameterImpl.class, reservedParameter);
	}

	@Override
	public void clearCache(List<ReservedParameter> reservedParameters) {
		for (ReservedParameter reservedParameter : reservedParameters) {
			entityCache.removeResult(
				ReservedParameterImpl.class, reservedParameter);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(ReservedParameterImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		ReservedParameterModelImpl reservedParameterModelImpl) {

		Object[] args = new Object[] {
			reservedParameterModelImpl.getParamCode()
		};

		finderCache.putResult(
			_finderPathCountByCode, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByCode, args, reservedParameterModelImpl, false);
	}

	/**
	 * Creates a new reserved parameter with the primary key. Does not add the reserved parameter to the database.
	 *
	 * @param reservedParameterId the primary key for the new reserved parameter
	 * @return the new reserved parameter
	 */
	@Override
	public ReservedParameter create(long reservedParameterId) {
		ReservedParameter reservedParameter = new ReservedParameterImpl();

		reservedParameter.setNew(true);
		reservedParameter.setPrimaryKey(reservedParameterId);

		reservedParameter.setCompanyId(CompanyThreadLocal.getCompanyId());

		return reservedParameter;
	}

	/**
	 * Removes the reserved parameter with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param reservedParameterId the primary key of the reserved parameter
	 * @return the reserved parameter that was removed
	 * @throws NoSuchReservedParameterException if a reserved parameter with the primary key could not be found
	 */
	@Override
	public ReservedParameter remove(long reservedParameterId)
		throws NoSuchReservedParameterException {

		return remove((Serializable)reservedParameterId);
	}

	/**
	 * Removes the reserved parameter with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the reserved parameter
	 * @return the reserved parameter that was removed
	 * @throws NoSuchReservedParameterException if a reserved parameter with the primary key could not be found
	 */
	@Override
	public ReservedParameter remove(Serializable primaryKey)
		throws NoSuchReservedParameterException {

		Session session = null;

		try {
			session = openSession();

			ReservedParameter reservedParameter =
				(ReservedParameter)session.get(
					ReservedParameterImpl.class, primaryKey);

			if (reservedParameter == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchReservedParameterException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(reservedParameter);
		}
		catch (NoSuchReservedParameterException noSuchEntityException) {
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
	protected ReservedParameter removeImpl(
		ReservedParameter reservedParameter) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(reservedParameter)) {
				reservedParameter = (ReservedParameter)session.get(
					ReservedParameterImpl.class,
					reservedParameter.getPrimaryKeyObj());
			}

			if (reservedParameter != null) {
				session.delete(reservedParameter);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (reservedParameter != null) {
			clearCache(reservedParameter);
		}

		return reservedParameter;
	}

	@Override
	public ReservedParameter updateImpl(ReservedParameter reservedParameter) {
		boolean isNew = reservedParameter.isNew();

		if (!(reservedParameter instanceof ReservedParameterModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(reservedParameter.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					reservedParameter);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in reservedParameter proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom ReservedParameter implementation " +
					reservedParameter.getClass());
		}

		ReservedParameterModelImpl reservedParameterModelImpl =
			(ReservedParameterModelImpl)reservedParameter;

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(reservedParameter);
			}
			else {
				reservedParameter = (ReservedParameter)session.merge(
					reservedParameter);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			ReservedParameterImpl.class, reservedParameterModelImpl, false,
			true);

		cacheUniqueFindersCache(reservedParameterModelImpl);

		if (isNew) {
			reservedParameter.setNew(false);
		}

		reservedParameter.resetOriginalValues();

		return reservedParameter;
	}

	/**
	 * Returns the reserved parameter with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the reserved parameter
	 * @return the reserved parameter
	 * @throws NoSuchReservedParameterException if a reserved parameter with the primary key could not be found
	 */
	@Override
	public ReservedParameter findByPrimaryKey(Serializable primaryKey)
		throws NoSuchReservedParameterException {

		ReservedParameter reservedParameter = fetchByPrimaryKey(primaryKey);

		if (reservedParameter == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchReservedParameterException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return reservedParameter;
	}

	/**
	 * Returns the reserved parameter with the primary key or throws a <code>NoSuchReservedParameterException</code> if it could not be found.
	 *
	 * @param reservedParameterId the primary key of the reserved parameter
	 * @return the reserved parameter
	 * @throws NoSuchReservedParameterException if a reserved parameter with the primary key could not be found
	 */
	@Override
	public ReservedParameter findByPrimaryKey(long reservedParameterId)
		throws NoSuchReservedParameterException {

		return findByPrimaryKey((Serializable)reservedParameterId);
	}

	/**
	 * Returns the reserved parameter with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param reservedParameterId the primary key of the reserved parameter
	 * @return the reserved parameter, or <code>null</code> if a reserved parameter with the primary key could not be found
	 */
	@Override
	public ReservedParameter fetchByPrimaryKey(long reservedParameterId) {
		return fetchByPrimaryKey((Serializable)reservedParameterId);
	}

	/**
	 * Returns all the reserved parameters.
	 *
	 * @return the reserved parameters
	 */
	@Override
	public List<ReservedParameter> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the reserved parameters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @return the range of reserved parameters
	 */
	@Override
	public List<ReservedParameter> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the reserved parameters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of reserved parameters
	 */
	@Override
	public List<ReservedParameter> findAll(
		int start, int end,
		OrderByComparator<ReservedParameter> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the reserved parameters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ReservedParameterModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of reserved parameters
	 * @param end the upper bound of the range of reserved parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of reserved parameters
	 */
	@Override
	public List<ReservedParameter> findAll(
		int start, int end,
		OrderByComparator<ReservedParameter> orderByComparator,
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

		List<ReservedParameter> list = null;

		if (useFinderCache) {
			list = (List<ReservedParameter>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_RESERVEDPARAMETER);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_RESERVEDPARAMETER;

				sql = sql.concat(ReservedParameterModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<ReservedParameter>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
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
	 * Removes all the reserved parameters from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (ReservedParameter reservedParameter : findAll()) {
			remove(reservedParameter);
		}
	}

	/**
	 * Returns the number of reserved parameters.
	 *
	 * @return the number of reserved parameters
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_RESERVEDPARAMETER);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
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
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "reservedParameterId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_RESERVEDPARAMETER;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return ReservedParameterModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the reserved parameter persistence.
	 */
	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_argumentsResolverServiceRegistration = _bundleContext.registerService(
			ArgumentsResolver.class,
			new ReservedParameterModelArgumentsResolver(),
			MapUtil.singletonDictionary(
				"model.class.name", ReservedParameter.class.getName()));

		_finderPathWithPaginationFindAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathWithPaginationFindByGroup = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByGroup",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"parameterGroupId"}, true);

		_finderPathWithoutPaginationFindByGroup = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroup",
			new String[] {Long.class.getName()},
			new String[] {"parameterGroupId"}, true);

		_finderPathCountByGroup = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroup",
			new String[] {Long.class.getName()},
			new String[] {"parameterGroupId"}, false);

		_finderPathFetchByCode = _createFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByCode",
			new String[] {String.class.getName()}, new String[] {"paramCode"},
			true);

		_finderPathCountByCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCode",
			new String[] {String.class.getName()}, new String[] {"paramCode"},
			false);

		_finderPathWithPaginationFindByReservedBy = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByReservedBy",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"reservedBy"}, true);

		_finderPathWithoutPaginationFindByReservedBy = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByReservedBy",
			new String[] {String.class.getName()}, new String[] {"reservedBy"},
			true);

		_finderPathCountByReservedBy = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByReservedBy",
			new String[] {String.class.getName()}, new String[] {"reservedBy"},
			false);
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(ReservedParameterImpl.class.getName());

		_argumentsResolverServiceRegistration.unregister();

		for (ServiceRegistration<FinderPath> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	@Override
	@Reference(
		target = NTUC_PARAMETERPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = NTUC_PARAMETERPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = NTUC_PARAMETERPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	private BundleContext _bundleContext;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_RESERVEDPARAMETER =
		"SELECT reservedParameter FROM ReservedParameter reservedParameter";

	private static final String _SQL_SELECT_RESERVEDPARAMETER_WHERE =
		"SELECT reservedParameter FROM ReservedParameter reservedParameter WHERE ";

	private static final String _SQL_COUNT_RESERVEDPARAMETER =
		"SELECT COUNT(reservedParameter) FROM ReservedParameter reservedParameter";

	private static final String _SQL_COUNT_RESERVEDPARAMETER_WHERE =
		"SELECT COUNT(reservedParameter) FROM ReservedParameter reservedParameter WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "reservedParameter.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No ReservedParameter exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No ReservedParameter exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		ReservedParameterPersistenceImpl.class);

	static {
		try {
			Class.forName(NTUC_PARAMETERPersistenceConstants.class.getName());
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

	private static class ReservedParameterModelArgumentsResolver
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

			ReservedParameterModelImpl reservedParameterModelImpl =
				(ReservedParameterModelImpl)baseModel;

			long columnBitmask = reservedParameterModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(
					reservedParameterModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |=
						reservedParameterModelImpl.getColumnBitmask(columnName);
				}

				_finderPathColumnBitmasksCache.put(
					finderPath, finderPathColumnBitmask);
			}

			if ((columnBitmask & finderPathColumnBitmask) != 0) {
				return _getValue(
					reservedParameterModelImpl, columnNames, original);
			}

			return null;
		}

		private Object[] _getValue(
			ReservedParameterModelImpl reservedParameterModelImpl,
			String[] columnNames, boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] =
						reservedParameterModelImpl.getColumnOriginalValue(
							columnName);
				}
				else {
					arguments[i] = reservedParameterModelImpl.getColumnValue(
						columnName);
				}
			}

			return arguments;
		}

		private static Map<FinderPath, Long> _finderPathColumnBitmasksCache =
			new ConcurrentHashMap<>();

	}

}