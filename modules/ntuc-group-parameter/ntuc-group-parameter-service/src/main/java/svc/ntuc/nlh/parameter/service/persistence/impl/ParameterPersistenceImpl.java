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

import svc.ntuc.nlh.parameter.exception.NoSuchParameterException;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.impl.ParameterImpl;
import svc.ntuc.nlh.parameter.model.impl.ParameterModelImpl;
import svc.ntuc.nlh.parameter.service.persistence.ParameterPersistence;
import svc.ntuc.nlh.parameter.service.persistence.impl.constants.NTUC_PARAMETERPersistenceConstants;

/**
 * The persistence implementation for the parameter service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = ParameterPersistence.class)
public class ParameterPersistenceImpl
	extends BasePersistenceImpl<Parameter> implements ParameterPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>ParameterUtil</code> to access the parameter persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		ParameterImpl.class.getName();

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
	 * Returns all the parameters where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @return the matching parameters
	 */
	@Override
	public List<Parameter> findByGroup(long parameterGroupId, Boolean deleted) {
		return findByGroup(
			parameterGroupId, deleted, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the parameters where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @return the range of matching parameters
	 */
	@Override
	public List<Parameter> findByGroup(
		long parameterGroupId, Boolean deleted, int start, int end) {

		return findByGroup(parameterGroupId, deleted, start, end, null);
	}

	/**
	 * Returns an ordered range of all the parameters where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching parameters
	 */
	@Override
	public List<Parameter> findByGroup(
		long parameterGroupId, Boolean deleted, int start, int end,
		OrderByComparator<Parameter> orderByComparator) {

		return findByGroup(
			parameterGroupId, deleted, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the parameters where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching parameters
	 */
	@Override
	public List<Parameter> findByGroup(
		long parameterGroupId, Boolean deleted, int start, int end,
		OrderByComparator<Parameter> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByGroup;
				finderArgs = new Object[] {parameterGroupId, deleted};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByGroup;
			finderArgs = new Object[] {
				parameterGroupId, deleted, start, end, orderByComparator
			};
		}

		List<Parameter> list = null;

		if (useFinderCache) {
			list = (List<Parameter>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Parameter parameter : list) {
					if ((parameterGroupId != parameter.getParameterGroupId()) ||
						!Objects.equals(deleted, parameter.getDeleted())) {

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

			sb.append(_SQL_SELECT_PARAMETER_WHERE);

			sb.append(_FINDER_COLUMN_GROUP_PARAMETERGROUPID_2);

			sb.append(_FINDER_COLUMN_GROUP_DELETED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(ParameterModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(parameterGroupId);

				queryPos.add(deleted.booleanValue());

				list = (List<Parameter>)QueryUtil.list(
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
	 * Returns the first parameter in the ordered set where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	@Override
	public Parameter findByGroup_First(
			long parameterGroupId, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws NoSuchParameterException {

		Parameter parameter = fetchByGroup_First(
			parameterGroupId, deleted, orderByComparator);

		if (parameter != null) {
			return parameter;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("parameterGroupId=");
		sb.append(parameterGroupId);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchParameterException(sb.toString());
	}

	/**
	 * Returns the first parameter in the ordered set where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	@Override
	public Parameter fetchByGroup_First(
		long parameterGroupId, Boolean deleted,
		OrderByComparator<Parameter> orderByComparator) {

		List<Parameter> list = findByGroup(
			parameterGroupId, deleted, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last parameter in the ordered set where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	@Override
	public Parameter findByGroup_Last(
			long parameterGroupId, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws NoSuchParameterException {

		Parameter parameter = fetchByGroup_Last(
			parameterGroupId, deleted, orderByComparator);

		if (parameter != null) {
			return parameter;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("parameterGroupId=");
		sb.append(parameterGroupId);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchParameterException(sb.toString());
	}

	/**
	 * Returns the last parameter in the ordered set where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	@Override
	public Parameter fetchByGroup_Last(
		long parameterGroupId, Boolean deleted,
		OrderByComparator<Parameter> orderByComparator) {

		int count = countByGroup(parameterGroupId, deleted);

		if (count == 0) {
			return null;
		}

		List<Parameter> list = findByGroup(
			parameterGroupId, deleted, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the parameters before and after the current parameter in the ordered set where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterId the primary key of the current parameter
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next parameter
	 * @throws NoSuchParameterException if a parameter with the primary key could not be found
	 */
	@Override
	public Parameter[] findByGroup_PrevAndNext(
			long parameterId, long parameterGroupId, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws NoSuchParameterException {

		Parameter parameter = findByPrimaryKey(parameterId);

		Session session = null;

		try {
			session = openSession();

			Parameter[] array = new ParameterImpl[3];

			array[0] = getByGroup_PrevAndNext(
				session, parameter, parameterGroupId, deleted,
				orderByComparator, true);

			array[1] = parameter;

			array[2] = getByGroup_PrevAndNext(
				session, parameter, parameterGroupId, deleted,
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

	protected Parameter getByGroup_PrevAndNext(
		Session session, Parameter parameter, long parameterGroupId,
		Boolean deleted, OrderByComparator<Parameter> orderByComparator,
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

		sb.append(_SQL_SELECT_PARAMETER_WHERE);

		sb.append(_FINDER_COLUMN_GROUP_PARAMETERGROUPID_2);

		sb.append(_FINDER_COLUMN_GROUP_DELETED_2);

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
			sb.append(ParameterModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(parameterGroupId);

		queryPos.add(deleted.booleanValue());

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(parameter)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Parameter> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the parameters where parameterGroupId = &#63; and deleted = &#63; from the database.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 */
	@Override
	public void removeByGroup(long parameterGroupId, Boolean deleted) {
		for (Parameter parameter :
				findByGroup(
					parameterGroupId, deleted, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(parameter);
		}
	}

	/**
	 * Returns the number of parameters where parameterGroupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the parameter group ID
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	@Override
	public int countByGroup(long parameterGroupId, Boolean deleted) {
		FinderPath finderPath = _finderPathCountByGroup;

		Object[] finderArgs = new Object[] {parameterGroupId, deleted};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_PARAMETER_WHERE);

			sb.append(_FINDER_COLUMN_GROUP_PARAMETERGROUPID_2);

			sb.append(_FINDER_COLUMN_GROUP_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(parameterGroupId);

				queryPos.add(deleted.booleanValue());

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
		"parameter.parameterGroupId = ? AND ";

	private static final String _FINDER_COLUMN_GROUP_DELETED_2 =
		"parameter.deleted = ?";

	private FinderPath _finderPathFetchByCode;
	private FinderPath _finderPathCountByCode;

	/**
	 * Returns the parameter where paramCode = &#63; and deleted = &#63; or throws a <code>NoSuchParameterException</code> if it could not be found.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	@Override
	public Parameter findByCode(String paramCode, Boolean deleted)
		throws NoSuchParameterException {

		Parameter parameter = fetchByCode(paramCode, deleted);

		if (parameter == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("paramCode=");
			sb.append(paramCode);

			sb.append(", deleted=");
			sb.append(deleted);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchParameterException(sb.toString());
		}

		return parameter;
	}

	/**
	 * Returns the parameter where paramCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	@Override
	public Parameter fetchByCode(String paramCode, Boolean deleted) {
		return fetchByCode(paramCode, deleted, true);
	}

	/**
	 * Returns the parameter where paramCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	@Override
	public Parameter fetchByCode(
		String paramCode, Boolean deleted, boolean useFinderCache) {

		paramCode = Objects.toString(paramCode, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {paramCode, deleted};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByCode, finderArgs, this);
		}

		if (result instanceof Parameter) {
			Parameter parameter = (Parameter)result;

			if (!Objects.equals(paramCode, parameter.getParamCode()) ||
				!Objects.equals(deleted, parameter.getDeleted())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_PARAMETER_WHERE);

			boolean bindParamCode = false;

			if (paramCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_CODE_PARAMCODE_3);
			}
			else {
				bindParamCode = true;

				sb.append(_FINDER_COLUMN_CODE_PARAMCODE_2);
			}

			sb.append(_FINDER_COLUMN_CODE_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindParamCode) {
					queryPos.add(paramCode);
				}

				queryPos.add(deleted.booleanValue());

				List<Parameter> list = query.list();

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
								finderArgs = new Object[] {paramCode, deleted};
							}

							_log.warn(
								"ParameterPersistenceImpl.fetchByCode(String, Boolean, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					Parameter parameter = list.get(0);

					result = parameter;

					cacheResult(parameter);
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
			return (Parameter)result;
		}
	}

	/**
	 * Removes the parameter where paramCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the parameter that was removed
	 */
	@Override
	public Parameter removeByCode(String paramCode, Boolean deleted)
		throws NoSuchParameterException {

		Parameter parameter = findByCode(paramCode, deleted);

		return remove(parameter);
	}

	/**
	 * Returns the number of parameters where paramCode = &#63; and deleted = &#63;.
	 *
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	@Override
	public int countByCode(String paramCode, Boolean deleted) {
		paramCode = Objects.toString(paramCode, "");

		FinderPath finderPath = _finderPathCountByCode;

		Object[] finderArgs = new Object[] {paramCode, deleted};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_PARAMETER_WHERE);

			boolean bindParamCode = false;

			if (paramCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_CODE_PARAMCODE_3);
			}
			else {
				bindParamCode = true;

				sb.append(_FINDER_COLUMN_CODE_PARAMCODE_2);
			}

			sb.append(_FINDER_COLUMN_CODE_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindParamCode) {
					queryPos.add(paramCode);
				}

				queryPos.add(deleted.booleanValue());

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
		"parameter.paramCode = ? AND ";

	private static final String _FINDER_COLUMN_CODE_PARAMCODE_3 =
		"(parameter.paramCode IS NULL OR parameter.paramCode = '') AND ";

	private static final String _FINDER_COLUMN_CODE_DELETED_2 =
		"parameter.deleted = ?";

	private FinderPath _finderPathWithPaginationFindByGroupSite;
	private FinderPath _finderPathWithoutPaginationFindByGroupSite;
	private FinderPath _finderPathCountByGroupSite;

	/**
	 * Returns all the parameters where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the matching parameters
	 */
	@Override
	public List<Parameter> findByGroupSite(long groupId, Boolean deleted) {
		return findByGroupSite(
			groupId, deleted, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the parameters where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @return the range of matching parameters
	 */
	@Override
	public List<Parameter> findByGroupSite(
		long groupId, Boolean deleted, int start, int end) {

		return findByGroupSite(groupId, deleted, start, end, null);
	}

	/**
	 * Returns an ordered range of all the parameters where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching parameters
	 */
	@Override
	public List<Parameter> findByGroupSite(
		long groupId, Boolean deleted, int start, int end,
		OrderByComparator<Parameter> orderByComparator) {

		return findByGroupSite(
			groupId, deleted, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the parameters where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching parameters
	 */
	@Override
	public List<Parameter> findByGroupSite(
		long groupId, Boolean deleted, int start, int end,
		OrderByComparator<Parameter> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByGroupSite;
				finderArgs = new Object[] {groupId, deleted};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByGroupSite;
			finderArgs = new Object[] {
				groupId, deleted, start, end, orderByComparator
			};
		}

		List<Parameter> list = null;

		if (useFinderCache) {
			list = (List<Parameter>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Parameter parameter : list) {
					if ((groupId != parameter.getGroupId()) ||
						!Objects.equals(deleted, parameter.getDeleted())) {

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

			sb.append(_SQL_SELECT_PARAMETER_WHERE);

			sb.append(_FINDER_COLUMN_GROUPSITE_GROUPID_2);

			sb.append(_FINDER_COLUMN_GROUPSITE_DELETED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(ParameterModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(deleted.booleanValue());

				list = (List<Parameter>)QueryUtil.list(
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
	 * Returns the first parameter in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	@Override
	public Parameter findByGroupSite_First(
			long groupId, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws NoSuchParameterException {

		Parameter parameter = fetchByGroupSite_First(
			groupId, deleted, orderByComparator);

		if (parameter != null) {
			return parameter;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchParameterException(sb.toString());
	}

	/**
	 * Returns the first parameter in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	@Override
	public Parameter fetchByGroupSite_First(
		long groupId, Boolean deleted,
		OrderByComparator<Parameter> orderByComparator) {

		List<Parameter> list = findByGroupSite(
			groupId, deleted, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last parameter in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	@Override
	public Parameter findByGroupSite_Last(
			long groupId, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws NoSuchParameterException {

		Parameter parameter = fetchByGroupSite_Last(
			groupId, deleted, orderByComparator);

		if (parameter != null) {
			return parameter;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchParameterException(sb.toString());
	}

	/**
	 * Returns the last parameter in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	@Override
	public Parameter fetchByGroupSite_Last(
		long groupId, Boolean deleted,
		OrderByComparator<Parameter> orderByComparator) {

		int count = countByGroupSite(groupId, deleted);

		if (count == 0) {
			return null;
		}

		List<Parameter> list = findByGroupSite(
			groupId, deleted, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the parameters before and after the current parameter in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterId the primary key of the current parameter
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next parameter
	 * @throws NoSuchParameterException if a parameter with the primary key could not be found
	 */
	@Override
	public Parameter[] findByGroupSite_PrevAndNext(
			long parameterId, long groupId, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws NoSuchParameterException {

		Parameter parameter = findByPrimaryKey(parameterId);

		Session session = null;

		try {
			session = openSession();

			Parameter[] array = new ParameterImpl[3];

			array[0] = getByGroupSite_PrevAndNext(
				session, parameter, groupId, deleted, orderByComparator, true);

			array[1] = parameter;

			array[2] = getByGroupSite_PrevAndNext(
				session, parameter, groupId, deleted, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected Parameter getByGroupSite_PrevAndNext(
		Session session, Parameter parameter, long groupId, Boolean deleted,
		OrderByComparator<Parameter> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_PARAMETER_WHERE);

		sb.append(_FINDER_COLUMN_GROUPSITE_GROUPID_2);

		sb.append(_FINDER_COLUMN_GROUPSITE_DELETED_2);

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
			sb.append(ParameterModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		queryPos.add(deleted.booleanValue());

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(parameter)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Parameter> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the parameters where groupId = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 */
	@Override
	public void removeByGroupSite(long groupId, Boolean deleted) {
		for (Parameter parameter :
				findByGroupSite(
					groupId, deleted, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(parameter);
		}
	}

	/**
	 * Returns the number of parameters where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	@Override
	public int countByGroupSite(long groupId, Boolean deleted) {
		FinderPath finderPath = _finderPathCountByGroupSite;

		Object[] finderArgs = new Object[] {groupId, deleted};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_PARAMETER_WHERE);

			sb.append(_FINDER_COLUMN_GROUPSITE_GROUPID_2);

			sb.append(_FINDER_COLUMN_GROUPSITE_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(deleted.booleanValue());

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

	private static final String _FINDER_COLUMN_GROUPSITE_GROUPID_2 =
		"parameter.groupId = ? AND ";

	private static final String _FINDER_COLUMN_GROUPSITE_DELETED_2 =
		"parameter.deleted = ?";

	private FinderPath _finderPathWithPaginationFindByGroupSiteCode;
	private FinderPath _finderPathWithoutPaginationFindByGroupSiteCode;
	private FinderPath _finderPathCountByGroupSiteCode;

	/**
	 * Returns all the parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the matching parameters
	 */
	@Override
	public List<Parameter> findByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted) {

		return findByGroupSiteCode(
			groupId, paramCode, deleted, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @return the range of matching parameters
	 */
	@Override
	public List<Parameter> findByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted, int start, int end) {

		return findByGroupSiteCode(
			groupId, paramCode, deleted, start, end, null);
	}

	/**
	 * Returns an ordered range of all the parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching parameters
	 */
	@Override
	public List<Parameter> findByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted, int start, int end,
		OrderByComparator<Parameter> orderByComparator) {

		return findByGroupSiteCode(
			groupId, paramCode, deleted, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching parameters
	 */
	@Override
	public List<Parameter> findByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted, int start, int end,
		OrderByComparator<Parameter> orderByComparator,
		boolean useFinderCache) {

		paramCode = Objects.toString(paramCode, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByGroupSiteCode;
				finderArgs = new Object[] {groupId, paramCode, deleted};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByGroupSiteCode;
			finderArgs = new Object[] {
				groupId, paramCode, deleted, start, end, orderByComparator
			};
		}

		List<Parameter> list = null;

		if (useFinderCache) {
			list = (List<Parameter>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Parameter parameter : list) {
					if ((groupId != parameter.getGroupId()) ||
						!paramCode.equals(parameter.getParamCode()) ||
						!Objects.equals(deleted, parameter.getDeleted())) {

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

			sb.append(_SQL_SELECT_PARAMETER_WHERE);

			sb.append(_FINDER_COLUMN_GROUPSITECODE_GROUPID_2);

			boolean bindParamCode = false;

			if (paramCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_GROUPSITECODE_PARAMCODE_3);
			}
			else {
				bindParamCode = true;

				sb.append(_FINDER_COLUMN_GROUPSITECODE_PARAMCODE_2);
			}

			sb.append(_FINDER_COLUMN_GROUPSITECODE_DELETED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(ParameterModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindParamCode) {
					queryPos.add(paramCode);
				}

				queryPos.add(deleted.booleanValue());

				list = (List<Parameter>)QueryUtil.list(
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
	 * Returns the first parameter in the ordered set where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	@Override
	public Parameter findByGroupSiteCode_First(
			long groupId, String paramCode, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws NoSuchParameterException {

		Parameter parameter = fetchByGroupSiteCode_First(
			groupId, paramCode, deleted, orderByComparator);

		if (parameter != null) {
			return parameter;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", paramCode=");
		sb.append(paramCode);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchParameterException(sb.toString());
	}

	/**
	 * Returns the first parameter in the ordered set where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	@Override
	public Parameter fetchByGroupSiteCode_First(
		long groupId, String paramCode, Boolean deleted,
		OrderByComparator<Parameter> orderByComparator) {

		List<Parameter> list = findByGroupSiteCode(
			groupId, paramCode, deleted, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last parameter in the ordered set where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	@Override
	public Parameter findByGroupSiteCode_Last(
			long groupId, String paramCode, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws NoSuchParameterException {

		Parameter parameter = fetchByGroupSiteCode_Last(
			groupId, paramCode, deleted, orderByComparator);

		if (parameter != null) {
			return parameter;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", paramCode=");
		sb.append(paramCode);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchParameterException(sb.toString());
	}

	/**
	 * Returns the last parameter in the ordered set where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	@Override
	public Parameter fetchByGroupSiteCode_Last(
		long groupId, String paramCode, Boolean deleted,
		OrderByComparator<Parameter> orderByComparator) {

		int count = countByGroupSiteCode(groupId, paramCode, deleted);

		if (count == 0) {
			return null;
		}

		List<Parameter> list = findByGroupSiteCode(
			groupId, paramCode, deleted, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the parameters before and after the current parameter in the ordered set where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param parameterId the primary key of the current parameter
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next parameter
	 * @throws NoSuchParameterException if a parameter with the primary key could not be found
	 */
	@Override
	public Parameter[] findByGroupSiteCode_PrevAndNext(
			long parameterId, long groupId, String paramCode, Boolean deleted,
			OrderByComparator<Parameter> orderByComparator)
		throws NoSuchParameterException {

		paramCode = Objects.toString(paramCode, "");

		Parameter parameter = findByPrimaryKey(parameterId);

		Session session = null;

		try {
			session = openSession();

			Parameter[] array = new ParameterImpl[3];

			array[0] = getByGroupSiteCode_PrevAndNext(
				session, parameter, groupId, paramCode, deleted,
				orderByComparator, true);

			array[1] = parameter;

			array[2] = getByGroupSiteCode_PrevAndNext(
				session, parameter, groupId, paramCode, deleted,
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

	protected Parameter getByGroupSiteCode_PrevAndNext(
		Session session, Parameter parameter, long groupId, String paramCode,
		Boolean deleted, OrderByComparator<Parameter> orderByComparator,
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

		sb.append(_SQL_SELECT_PARAMETER_WHERE);

		sb.append(_FINDER_COLUMN_GROUPSITECODE_GROUPID_2);

		boolean bindParamCode = false;

		if (paramCode.isEmpty()) {
			sb.append(_FINDER_COLUMN_GROUPSITECODE_PARAMCODE_3);
		}
		else {
			bindParamCode = true;

			sb.append(_FINDER_COLUMN_GROUPSITECODE_PARAMCODE_2);
		}

		sb.append(_FINDER_COLUMN_GROUPSITECODE_DELETED_2);

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
			sb.append(ParameterModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		if (bindParamCode) {
			queryPos.add(paramCode);
		}

		queryPos.add(deleted.booleanValue());

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(parameter)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Parameter> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 */
	@Override
	public void removeByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted) {

		for (Parameter parameter :
				findByGroupSiteCode(
					groupId, paramCode, deleted, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(parameter);
		}
	}

	/**
	 * Returns the number of parameters where groupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	@Override
	public int countByGroupSiteCode(
		long groupId, String paramCode, Boolean deleted) {

		paramCode = Objects.toString(paramCode, "");

		FinderPath finderPath = _finderPathCountByGroupSiteCode;

		Object[] finderArgs = new Object[] {groupId, paramCode, deleted};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_PARAMETER_WHERE);

			sb.append(_FINDER_COLUMN_GROUPSITECODE_GROUPID_2);

			boolean bindParamCode = false;

			if (paramCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_GROUPSITECODE_PARAMCODE_3);
			}
			else {
				bindParamCode = true;

				sb.append(_FINDER_COLUMN_GROUPSITECODE_PARAMCODE_2);
			}

			sb.append(_FINDER_COLUMN_GROUPSITECODE_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindParamCode) {
					queryPos.add(paramCode);
				}

				queryPos.add(deleted.booleanValue());

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

	private static final String _FINDER_COLUMN_GROUPSITECODE_GROUPID_2 =
		"parameter.groupId = ? AND ";

	private static final String _FINDER_COLUMN_GROUPSITECODE_PARAMCODE_2 =
		"parameter.paramCode = ? AND ";

	private static final String _FINDER_COLUMN_GROUPSITECODE_PARAMCODE_3 =
		"(parameter.paramCode IS NULL OR parameter.paramCode = '') AND ";

	private static final String _FINDER_COLUMN_GROUPSITECODE_DELETED_2 =
		"parameter.deleted = ?";

	private FinderPath _finderPathFetchByGroupCode;
	private FinderPath _finderPathCountByGroupCode;

	/**
	 * Returns the parameter where groupId = &#63; and parameterGroupId = &#63; and paramCode = &#63; and deleted = &#63; or throws a <code>NoSuchParameterException</code> if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param parameterGroupId the parameter group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the matching parameter
	 * @throws NoSuchParameterException if a matching parameter could not be found
	 */
	@Override
	public Parameter findByGroupCode(
			long groupId, long parameterGroupId, String paramCode,
			Boolean deleted)
		throws NoSuchParameterException {

		Parameter parameter = fetchByGroupCode(
			groupId, parameterGroupId, paramCode, deleted);

		if (parameter == null) {
			StringBundler sb = new StringBundler(10);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("groupId=");
			sb.append(groupId);

			sb.append(", parameterGroupId=");
			sb.append(parameterGroupId);

			sb.append(", paramCode=");
			sb.append(paramCode);

			sb.append(", deleted=");
			sb.append(deleted);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchParameterException(sb.toString());
		}

		return parameter;
	}

	/**
	 * Returns the parameter where groupId = &#63; and parameterGroupId = &#63; and paramCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param parameterGroupId the parameter group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	@Override
	public Parameter fetchByGroupCode(
		long groupId, long parameterGroupId, String paramCode,
		Boolean deleted) {

		return fetchByGroupCode(
			groupId, parameterGroupId, paramCode, deleted, true);
	}

	/**
	 * Returns the parameter where groupId = &#63; and parameterGroupId = &#63; and paramCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param parameterGroupId the parameter group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching parameter, or <code>null</code> if a matching parameter could not be found
	 */
	@Override
	public Parameter fetchByGroupCode(
		long groupId, long parameterGroupId, String paramCode, Boolean deleted,
		boolean useFinderCache) {

		paramCode = Objects.toString(paramCode, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {
				groupId, parameterGroupId, paramCode, deleted
			};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByGroupCode, finderArgs, this);
		}

		if (result instanceof Parameter) {
			Parameter parameter = (Parameter)result;

			if ((groupId != parameter.getGroupId()) ||
				(parameterGroupId != parameter.getParameterGroupId()) ||
				!Objects.equals(paramCode, parameter.getParamCode()) ||
				!Objects.equals(deleted, parameter.getDeleted())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_SQL_SELECT_PARAMETER_WHERE);

			sb.append(_FINDER_COLUMN_GROUPCODE_GROUPID_2);

			sb.append(_FINDER_COLUMN_GROUPCODE_PARAMETERGROUPID_2);

			boolean bindParamCode = false;

			if (paramCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_GROUPCODE_PARAMCODE_3);
			}
			else {
				bindParamCode = true;

				sb.append(_FINDER_COLUMN_GROUPCODE_PARAMCODE_2);
			}

			sb.append(_FINDER_COLUMN_GROUPCODE_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(parameterGroupId);

				if (bindParamCode) {
					queryPos.add(paramCode);
				}

				queryPos.add(deleted.booleanValue());

				List<Parameter> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByGroupCode, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {
									groupId, parameterGroupId, paramCode,
									deleted
								};
							}

							_log.warn(
								"ParameterPersistenceImpl.fetchByGroupCode(long, long, String, Boolean, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					Parameter parameter = list.get(0);

					result = parameter;

					cacheResult(parameter);
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
			return (Parameter)result;
		}
	}

	/**
	 * Removes the parameter where groupId = &#63; and parameterGroupId = &#63; and paramCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param parameterGroupId the parameter group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the parameter that was removed
	 */
	@Override
	public Parameter removeByGroupCode(
			long groupId, long parameterGroupId, String paramCode,
			Boolean deleted)
		throws NoSuchParameterException {

		Parameter parameter = findByGroupCode(
			groupId, parameterGroupId, paramCode, deleted);

		return remove(parameter);
	}

	/**
	 * Returns the number of parameters where groupId = &#63; and parameterGroupId = &#63; and paramCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param parameterGroupId the parameter group ID
	 * @param paramCode the param code
	 * @param deleted the deleted
	 * @return the number of matching parameters
	 */
	@Override
	public int countByGroupCode(
		long groupId, long parameterGroupId, String paramCode,
		Boolean deleted) {

		paramCode = Objects.toString(paramCode, "");

		FinderPath finderPath = _finderPathCountByGroupCode;

		Object[] finderArgs = new Object[] {
			groupId, parameterGroupId, paramCode, deleted
		};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(5);

			sb.append(_SQL_COUNT_PARAMETER_WHERE);

			sb.append(_FINDER_COLUMN_GROUPCODE_GROUPID_2);

			sb.append(_FINDER_COLUMN_GROUPCODE_PARAMETERGROUPID_2);

			boolean bindParamCode = false;

			if (paramCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_GROUPCODE_PARAMCODE_3);
			}
			else {
				bindParamCode = true;

				sb.append(_FINDER_COLUMN_GROUPCODE_PARAMCODE_2);
			}

			sb.append(_FINDER_COLUMN_GROUPCODE_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(parameterGroupId);

				if (bindParamCode) {
					queryPos.add(paramCode);
				}

				queryPos.add(deleted.booleanValue());

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

	private static final String _FINDER_COLUMN_GROUPCODE_GROUPID_2 =
		"parameter.groupId = ? AND ";

	private static final String _FINDER_COLUMN_GROUPCODE_PARAMETERGROUPID_2 =
		"parameter.parameterGroupId = ? AND ";

	private static final String _FINDER_COLUMN_GROUPCODE_PARAMCODE_2 =
		"parameter.paramCode = ? AND ";

	private static final String _FINDER_COLUMN_GROUPCODE_PARAMCODE_3 =
		"(parameter.paramCode IS NULL OR parameter.paramCode = '') AND ";

	private static final String _FINDER_COLUMN_GROUPCODE_DELETED_2 =
		"parameter.deleted = ?";

	public ParameterPersistenceImpl() {
		setModelClass(Parameter.class);

		setModelImplClass(ParameterImpl.class);
		setModelPKClass(long.class);
	}

	/**
	 * Caches the parameter in the entity cache if it is enabled.
	 *
	 * @param parameter the parameter
	 */
	@Override
	public void cacheResult(Parameter parameter) {
		entityCache.putResult(
			ParameterImpl.class, parameter.getPrimaryKey(), parameter);

		finderCache.putResult(
			_finderPathFetchByCode,
			new Object[] {parameter.getParamCode(), parameter.getDeleted()},
			parameter);

		finderCache.putResult(
			_finderPathFetchByGroupCode,
			new Object[] {
				parameter.getGroupId(), parameter.getParameterGroupId(),
				parameter.getParamCode(), parameter.getDeleted()
			},
			parameter);
	}

	/**
	 * Caches the parameters in the entity cache if it is enabled.
	 *
	 * @param parameters the parameters
	 */
	@Override
	public void cacheResult(List<Parameter> parameters) {
		for (Parameter parameter : parameters) {
			if (entityCache.getResult(
					ParameterImpl.class, parameter.getPrimaryKey()) == null) {

				cacheResult(parameter);
			}
		}
	}

	/**
	 * Clears the cache for all parameters.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(ParameterImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the parameter.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Parameter parameter) {
		entityCache.removeResult(ParameterImpl.class, parameter);
	}

	@Override
	public void clearCache(List<Parameter> parameters) {
		for (Parameter parameter : parameters) {
			entityCache.removeResult(ParameterImpl.class, parameter);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(ParameterImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		ParameterModelImpl parameterModelImpl) {

		Object[] args = new Object[] {
			parameterModelImpl.getParamCode(), parameterModelImpl.getDeleted()
		};

		finderCache.putResult(
			_finderPathCountByCode, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByCode, args, parameterModelImpl, false);

		args = new Object[] {
			parameterModelImpl.getGroupId(),
			parameterModelImpl.getParameterGroupId(),
			parameterModelImpl.getParamCode(), parameterModelImpl.getDeleted()
		};

		finderCache.putResult(
			_finderPathCountByGroupCode, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByGroupCode, args, parameterModelImpl, false);
	}

	/**
	 * Creates a new parameter with the primary key. Does not add the parameter to the database.
	 *
	 * @param parameterId the primary key for the new parameter
	 * @return the new parameter
	 */
	@Override
	public Parameter create(long parameterId) {
		Parameter parameter = new ParameterImpl();

		parameter.setNew(true);
		parameter.setPrimaryKey(parameterId);

		parameter.setCompanyId(CompanyThreadLocal.getCompanyId());

		return parameter;
	}

	/**
	 * Removes the parameter with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param parameterId the primary key of the parameter
	 * @return the parameter that was removed
	 * @throws NoSuchParameterException if a parameter with the primary key could not be found
	 */
	@Override
	public Parameter remove(long parameterId) throws NoSuchParameterException {
		return remove((Serializable)parameterId);
	}

	/**
	 * Removes the parameter with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the parameter
	 * @return the parameter that was removed
	 * @throws NoSuchParameterException if a parameter with the primary key could not be found
	 */
	@Override
	public Parameter remove(Serializable primaryKey)
		throws NoSuchParameterException {

		Session session = null;

		try {
			session = openSession();

			Parameter parameter = (Parameter)session.get(
				ParameterImpl.class, primaryKey);

			if (parameter == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchParameterException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(parameter);
		}
		catch (NoSuchParameterException noSuchEntityException) {
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
	protected Parameter removeImpl(Parameter parameter) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(parameter)) {
				parameter = (Parameter)session.get(
					ParameterImpl.class, parameter.getPrimaryKeyObj());
			}

			if (parameter != null) {
				session.delete(parameter);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (parameter != null) {
			clearCache(parameter);
		}

		return parameter;
	}

	@Override
	public Parameter updateImpl(Parameter parameter) {
		boolean isNew = parameter.isNew();

		if (!(parameter instanceof ParameterModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(parameter.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(parameter);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in parameter proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom Parameter implementation " +
					parameter.getClass());
		}

		ParameterModelImpl parameterModelImpl = (ParameterModelImpl)parameter;

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(parameter);
			}
			else {
				parameter = (Parameter)session.merge(parameter);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			ParameterImpl.class, parameterModelImpl, false, true);

		cacheUniqueFindersCache(parameterModelImpl);

		if (isNew) {
			parameter.setNew(false);
		}

		parameter.resetOriginalValues();

		return parameter;
	}

	/**
	 * Returns the parameter with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the parameter
	 * @return the parameter
	 * @throws NoSuchParameterException if a parameter with the primary key could not be found
	 */
	@Override
	public Parameter findByPrimaryKey(Serializable primaryKey)
		throws NoSuchParameterException {

		Parameter parameter = fetchByPrimaryKey(primaryKey);

		if (parameter == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchParameterException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return parameter;
	}

	/**
	 * Returns the parameter with the primary key or throws a <code>NoSuchParameterException</code> if it could not be found.
	 *
	 * @param parameterId the primary key of the parameter
	 * @return the parameter
	 * @throws NoSuchParameterException if a parameter with the primary key could not be found
	 */
	@Override
	public Parameter findByPrimaryKey(long parameterId)
		throws NoSuchParameterException {

		return findByPrimaryKey((Serializable)parameterId);
	}

	/**
	 * Returns the parameter with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param parameterId the primary key of the parameter
	 * @return the parameter, or <code>null</code> if a parameter with the primary key could not be found
	 */
	@Override
	public Parameter fetchByPrimaryKey(long parameterId) {
		return fetchByPrimaryKey((Serializable)parameterId);
	}

	/**
	 * Returns all the parameters.
	 *
	 * @return the parameters
	 */
	@Override
	public List<Parameter> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the parameters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @return the range of parameters
	 */
	@Override
	public List<Parameter> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the parameters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of parameters
	 */
	@Override
	public List<Parameter> findAll(
		int start, int end, OrderByComparator<Parameter> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the parameters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of parameters
	 * @param end the upper bound of the range of parameters (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of parameters
	 */
	@Override
	public List<Parameter> findAll(
		int start, int end, OrderByComparator<Parameter> orderByComparator,
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

		List<Parameter> list = null;

		if (useFinderCache) {
			list = (List<Parameter>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_PARAMETER);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_PARAMETER;

				sql = sql.concat(ParameterModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<Parameter>)QueryUtil.list(
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
	 * Removes all the parameters from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (Parameter parameter : findAll()) {
			remove(parameter);
		}
	}

	/**
	 * Returns the number of parameters.
	 *
	 * @return the number of parameters
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_PARAMETER);

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
		return "parameterId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_PARAMETER;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return ParameterModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the parameter persistence.
	 */
	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_argumentsResolverServiceRegistration = _bundleContext.registerService(
			ArgumentsResolver.class, new ParameterModelArgumentsResolver(),
			MapUtil.singletonDictionary(
				"model.class.name", Parameter.class.getName()));

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
				Long.class.getName(), Boolean.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"parameterGroupId", "deleted"}, true);

		_finderPathWithoutPaginationFindByGroup = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroup",
			new String[] {Long.class.getName(), Boolean.class.getName()},
			new String[] {"parameterGroupId", "deleted"}, true);

		_finderPathCountByGroup = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroup",
			new String[] {Long.class.getName(), Boolean.class.getName()},
			new String[] {"parameterGroupId", "deleted"}, false);

		_finderPathFetchByCode = _createFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByCode",
			new String[] {String.class.getName(), Boolean.class.getName()},
			new String[] {"paramCode", "deleted"}, true);

		_finderPathCountByCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCode",
			new String[] {String.class.getName(), Boolean.class.getName()},
			new String[] {"paramCode", "deleted"}, false);

		_finderPathWithPaginationFindByGroupSite = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByGroupSite",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"groupId", "deleted"}, true);

		_finderPathWithoutPaginationFindByGroupSite = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroupSite",
			new String[] {Long.class.getName(), Boolean.class.getName()},
			new String[] {"groupId", "deleted"}, true);

		_finderPathCountByGroupSite = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroupSite",
			new String[] {Long.class.getName(), Boolean.class.getName()},
			new String[] {"groupId", "deleted"}, false);

		_finderPathWithPaginationFindByGroupSiteCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByGroupSiteCode",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Boolean.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"groupId", "paramCode", "deleted"}, true);

		_finderPathWithoutPaginationFindByGroupSiteCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroupSiteCode",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Boolean.class.getName()
			},
			new String[] {"groupId", "paramCode", "deleted"}, true);

		_finderPathCountByGroupSiteCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroupSiteCode",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Boolean.class.getName()
			},
			new String[] {"groupId", "paramCode", "deleted"}, false);

		_finderPathFetchByGroupCode = _createFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByGroupCode",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName(), Boolean.class.getName()
			},
			new String[] {
				"groupId", "parameterGroupId", "paramCode", "deleted"
			},
			true);

		_finderPathCountByGroupCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroupCode",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName(), Boolean.class.getName()
			},
			new String[] {
				"groupId", "parameterGroupId", "paramCode", "deleted"
			},
			false);
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(ParameterImpl.class.getName());

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

	private static final String _SQL_SELECT_PARAMETER =
		"SELECT parameter FROM Parameter parameter";

	private static final String _SQL_SELECT_PARAMETER_WHERE =
		"SELECT parameter FROM Parameter parameter WHERE ";

	private static final String _SQL_COUNT_PARAMETER =
		"SELECT COUNT(parameter) FROM Parameter parameter";

	private static final String _SQL_COUNT_PARAMETER_WHERE =
		"SELECT COUNT(parameter) FROM Parameter parameter WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "parameter.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No Parameter exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No Parameter exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		ParameterPersistenceImpl.class);

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

	private static class ParameterModelArgumentsResolver
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

			ParameterModelImpl parameterModelImpl =
				(ParameterModelImpl)baseModel;

			long columnBitmask = parameterModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(parameterModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |=
						parameterModelImpl.getColumnBitmask(columnName);
				}

				_finderPathColumnBitmasksCache.put(
					finderPath, finderPathColumnBitmask);
			}

			if ((columnBitmask & finderPathColumnBitmask) != 0) {
				return _getValue(parameterModelImpl, columnNames, original);
			}

			return null;
		}

		private Object[] _getValue(
			ParameterModelImpl parameterModelImpl, String[] columnNames,
			boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] = parameterModelImpl.getColumnOriginalValue(
						columnName);
				}
				else {
					arguments[i] = parameterModelImpl.getColumnValue(
						columnName);
				}
			}

			return arguments;
		}

		private static Map<FinderPath, Long> _finderPathColumnBitmasksCache =
			new ConcurrentHashMap<>();

	}

}