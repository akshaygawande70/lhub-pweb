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

import svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.model.impl.ParameterGroupImpl;
import svc.ntuc.nlh.parameter.model.impl.ParameterGroupModelImpl;
import svc.ntuc.nlh.parameter.service.persistence.ParameterGroupPersistence;
import svc.ntuc.nlh.parameter.service.persistence.impl.constants.NTUC_PARAMETERPersistenceConstants;

/**
 * The persistence implementation for the parameter group service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = ParameterGroupPersistence.class)
public class ParameterGroupPersistenceImpl
	extends BasePersistenceImpl<ParameterGroup>
	implements ParameterGroupPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>ParameterGroupUtil</code> to access the parameter group persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		ParameterGroupImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathFetchByCode;
	private FinderPath _finderPathCountByCode;

	/**
	 * Returns the parameter group where groupCode = &#63; and deleted = &#63; or throws a <code>NoSuchParameterGroupException</code> if it could not be found.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup findByCode(String groupCode, Boolean deleted)
		throws NoSuchParameterGroupException {

		ParameterGroup parameterGroup = fetchByCode(groupCode, deleted);

		if (parameterGroup == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("groupCode=");
			sb.append(groupCode);

			sb.append(", deleted=");
			sb.append(deleted);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchParameterGroupException(sb.toString());
		}

		return parameterGroup;
	}

	/**
	 * Returns the parameter group where groupCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup fetchByCode(String groupCode, Boolean deleted) {
		return fetchByCode(groupCode, deleted, true);
	}

	/**
	 * Returns the parameter group where groupCode = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup fetchByCode(
		String groupCode, Boolean deleted, boolean useFinderCache) {

		groupCode = Objects.toString(groupCode, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {groupCode, deleted};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByCode, finderArgs, this);
		}

		if (result instanceof ParameterGroup) {
			ParameterGroup parameterGroup = (ParameterGroup)result;

			if (!Objects.equals(groupCode, parameterGroup.getGroupCode()) ||
				!Objects.equals(deleted, parameterGroup.getDeleted())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_PARAMETERGROUP_WHERE);

			boolean bindGroupCode = false;

			if (groupCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_CODE_GROUPCODE_3);
			}
			else {
				bindGroupCode = true;

				sb.append(_FINDER_COLUMN_CODE_GROUPCODE_2);
			}

			sb.append(_FINDER_COLUMN_CODE_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindGroupCode) {
					queryPos.add(groupCode);
				}

				queryPos.add(deleted.booleanValue());

				List<ParameterGroup> list = query.list();

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
								finderArgs = new Object[] {groupCode, deleted};
							}

							_log.warn(
								"ParameterGroupPersistenceImpl.fetchByCode(String, Boolean, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					ParameterGroup parameterGroup = list.get(0);

					result = parameterGroup;

					cacheResult(parameterGroup);
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
			return (ParameterGroup)result;
		}
	}

	/**
	 * Removes the parameter group where groupCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the parameter group that was removed
	 */
	@Override
	public ParameterGroup removeByCode(String groupCode, Boolean deleted)
		throws NoSuchParameterGroupException {

		ParameterGroup parameterGroup = findByCode(groupCode, deleted);

		return remove(parameterGroup);
	}

	/**
	 * Returns the number of parameter groups where groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the number of matching parameter groups
	 */
	@Override
	public int countByCode(String groupCode, Boolean deleted) {
		groupCode = Objects.toString(groupCode, "");

		FinderPath finderPath = _finderPathCountByCode;

		Object[] finderArgs = new Object[] {groupCode, deleted};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_PARAMETERGROUP_WHERE);

			boolean bindGroupCode = false;

			if (groupCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_CODE_GROUPCODE_3);
			}
			else {
				bindGroupCode = true;

				sb.append(_FINDER_COLUMN_CODE_GROUPCODE_2);
			}

			sb.append(_FINDER_COLUMN_CODE_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindGroupCode) {
					queryPos.add(groupCode);
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

	private static final String _FINDER_COLUMN_CODE_GROUPCODE_2 =
		"parameterGroup.groupCode = ? AND ";

	private static final String _FINDER_COLUMN_CODE_GROUPCODE_3 =
		"(parameterGroup.groupCode IS NULL OR parameterGroup.groupCode = '') AND ";

	private static final String _FINDER_COLUMN_CODE_DELETED_2 =
		"parameterGroup.deleted = ?";

	private FinderPath _finderPathWithPaginationFindByParentId;
	private FinderPath _finderPathWithoutPaginationFindByParentId;
	private FinderPath _finderPathCountByParentId;

	/**
	 * Returns all the parameter groups where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @return the matching parameter groups
	 */
	@Override
	public List<ParameterGroup> findByParentId(long parentId, Boolean deleted) {
		return findByParentId(
			parentId, deleted, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the parameter groups where parentId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @return the range of matching parameter groups
	 */
	@Override
	public List<ParameterGroup> findByParentId(
		long parentId, Boolean deleted, int start, int end) {

		return findByParentId(parentId, deleted, start, end, null);
	}

	/**
	 * Returns an ordered range of all the parameter groups where parentId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching parameter groups
	 */
	@Override
	public List<ParameterGroup> findByParentId(
		long parentId, Boolean deleted, int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return findByParentId(
			parentId, deleted, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the parameter groups where parentId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching parameter groups
	 */
	@Override
	public List<ParameterGroup> findByParentId(
		long parentId, Boolean deleted, int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByParentId;
				finderArgs = new Object[] {parentId, deleted};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByParentId;
			finderArgs = new Object[] {
				parentId, deleted, start, end, orderByComparator
			};
		}

		List<ParameterGroup> list = null;

		if (useFinderCache) {
			list = (List<ParameterGroup>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (ParameterGroup parameterGroup : list) {
					if ((parentId != parameterGroup.getParentId()) ||
						!Objects.equals(deleted, parameterGroup.getDeleted())) {

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

			sb.append(_SQL_SELECT_PARAMETERGROUP_WHERE);

			sb.append(_FINDER_COLUMN_PARENTID_PARENTID_2);

			sb.append(_FINDER_COLUMN_PARENTID_DELETED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(ParameterGroupModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(parentId);

				queryPos.add(deleted.booleanValue());

				list = (List<ParameterGroup>)QueryUtil.list(
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
	 * Returns the first parameter group in the ordered set where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup findByParentId_First(
			long parentId, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws NoSuchParameterGroupException {

		ParameterGroup parameterGroup = fetchByParentId_First(
			parentId, deleted, orderByComparator);

		if (parameterGroup != null) {
			return parameterGroup;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("parentId=");
		sb.append(parentId);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchParameterGroupException(sb.toString());
	}

	/**
	 * Returns the first parameter group in the ordered set where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup fetchByParentId_First(
		long parentId, Boolean deleted,
		OrderByComparator<ParameterGroup> orderByComparator) {

		List<ParameterGroup> list = findByParentId(
			parentId, deleted, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last parameter group in the ordered set where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup findByParentId_Last(
			long parentId, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws NoSuchParameterGroupException {

		ParameterGroup parameterGroup = fetchByParentId_Last(
			parentId, deleted, orderByComparator);

		if (parameterGroup != null) {
			return parameterGroup;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("parentId=");
		sb.append(parentId);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchParameterGroupException(sb.toString());
	}

	/**
	 * Returns the last parameter group in the ordered set where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup fetchByParentId_Last(
		long parentId, Boolean deleted,
		OrderByComparator<ParameterGroup> orderByComparator) {

		int count = countByParentId(parentId, deleted);

		if (count == 0) {
			return null;
		}

		List<ParameterGroup> list = findByParentId(
			parentId, deleted, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the parameter groups before and after the current parameter group in the ordered set where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the primary key of the current parameter group
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next parameter group
	 * @throws NoSuchParameterGroupException if a parameter group with the primary key could not be found
	 */
	@Override
	public ParameterGroup[] findByParentId_PrevAndNext(
			long parameterGroupId, long parentId, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws NoSuchParameterGroupException {

		ParameterGroup parameterGroup = findByPrimaryKey(parameterGroupId);

		Session session = null;

		try {
			session = openSession();

			ParameterGroup[] array = new ParameterGroupImpl[3];

			array[0] = getByParentId_PrevAndNext(
				session, parameterGroup, parentId, deleted, orderByComparator,
				true);

			array[1] = parameterGroup;

			array[2] = getByParentId_PrevAndNext(
				session, parameterGroup, parentId, deleted, orderByComparator,
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

	protected ParameterGroup getByParentId_PrevAndNext(
		Session session, ParameterGroup parameterGroup, long parentId,
		Boolean deleted, OrderByComparator<ParameterGroup> orderByComparator,
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

		sb.append(_SQL_SELECT_PARAMETERGROUP_WHERE);

		sb.append(_FINDER_COLUMN_PARENTID_PARENTID_2);

		sb.append(_FINDER_COLUMN_PARENTID_DELETED_2);

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
			sb.append(ParameterGroupModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(parentId);

		queryPos.add(deleted.booleanValue());

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						parameterGroup)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<ParameterGroup> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the parameter groups where parentId = &#63; and deleted = &#63; from the database.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 */
	@Override
	public void removeByParentId(long parentId, Boolean deleted) {
		for (ParameterGroup parameterGroup :
				findByParentId(
					parentId, deleted, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(parameterGroup);
		}
	}

	/**
	 * Returns the number of parameter groups where parentId = &#63; and deleted = &#63;.
	 *
	 * @param parentId the parent ID
	 * @param deleted the deleted
	 * @return the number of matching parameter groups
	 */
	@Override
	public int countByParentId(long parentId, Boolean deleted) {
		FinderPath finderPath = _finderPathCountByParentId;

		Object[] finderArgs = new Object[] {parentId, deleted};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_PARAMETERGROUP_WHERE);

			sb.append(_FINDER_COLUMN_PARENTID_PARENTID_2);

			sb.append(_FINDER_COLUMN_PARENTID_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(parentId);

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

	private static final String _FINDER_COLUMN_PARENTID_PARENTID_2 =
		"parameterGroup.parentId = ? AND ";

	private static final String _FINDER_COLUMN_PARENTID_DELETED_2 =
		"parameterGroup.deleted = ?";

	private FinderPath _finderPathWithPaginationFindByGroupSite;
	private FinderPath _finderPathWithoutPaginationFindByGroupSite;
	private FinderPath _finderPathCountByGroupSite;

	/**
	 * Returns all the parameter groups where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the matching parameter groups
	 */
	@Override
	public List<ParameterGroup> findByGroupSite(long groupId, Boolean deleted) {
		return findByGroupSite(
			groupId, deleted, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the parameter groups where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @return the range of matching parameter groups
	 */
	@Override
	public List<ParameterGroup> findByGroupSite(
		long groupId, Boolean deleted, int start, int end) {

		return findByGroupSite(groupId, deleted, start, end, null);
	}

	/**
	 * Returns an ordered range of all the parameter groups where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching parameter groups
	 */
	@Override
	public List<ParameterGroup> findByGroupSite(
		long groupId, Boolean deleted, int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return findByGroupSite(
			groupId, deleted, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the parameter groups where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching parameter groups
	 */
	@Override
	public List<ParameterGroup> findByGroupSite(
		long groupId, Boolean deleted, int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator,
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

		List<ParameterGroup> list = null;

		if (useFinderCache) {
			list = (List<ParameterGroup>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (ParameterGroup parameterGroup : list) {
					if ((groupId != parameterGroup.getGroupId()) ||
						!Objects.equals(deleted, parameterGroup.getDeleted())) {

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

			sb.append(_SQL_SELECT_PARAMETERGROUP_WHERE);

			sb.append(_FINDER_COLUMN_GROUPSITE_GROUPID_2);

			sb.append(_FINDER_COLUMN_GROUPSITE_DELETED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(ParameterGroupModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(deleted.booleanValue());

				list = (List<ParameterGroup>)QueryUtil.list(
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
	 * Returns the first parameter group in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup findByGroupSite_First(
			long groupId, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws NoSuchParameterGroupException {

		ParameterGroup parameterGroup = fetchByGroupSite_First(
			groupId, deleted, orderByComparator);

		if (parameterGroup != null) {
			return parameterGroup;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchParameterGroupException(sb.toString());
	}

	/**
	 * Returns the first parameter group in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup fetchByGroupSite_First(
		long groupId, Boolean deleted,
		OrderByComparator<ParameterGroup> orderByComparator) {

		List<ParameterGroup> list = findByGroupSite(
			groupId, deleted, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last parameter group in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup findByGroupSite_Last(
			long groupId, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws NoSuchParameterGroupException {

		ParameterGroup parameterGroup = fetchByGroupSite_Last(
			groupId, deleted, orderByComparator);

		if (parameterGroup != null) {
			return parameterGroup;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchParameterGroupException(sb.toString());
	}

	/**
	 * Returns the last parameter group in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup fetchByGroupSite_Last(
		long groupId, Boolean deleted,
		OrderByComparator<ParameterGroup> orderByComparator) {

		int count = countByGroupSite(groupId, deleted);

		if (count == 0) {
			return null;
		}

		List<ParameterGroup> list = findByGroupSite(
			groupId, deleted, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the parameter groups before and after the current parameter group in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the primary key of the current parameter group
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next parameter group
	 * @throws NoSuchParameterGroupException if a parameter group with the primary key could not be found
	 */
	@Override
	public ParameterGroup[] findByGroupSite_PrevAndNext(
			long parameterGroupId, long groupId, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws NoSuchParameterGroupException {

		ParameterGroup parameterGroup = findByPrimaryKey(parameterGroupId);

		Session session = null;

		try {
			session = openSession();

			ParameterGroup[] array = new ParameterGroupImpl[3];

			array[0] = getByGroupSite_PrevAndNext(
				session, parameterGroup, groupId, deleted, orderByComparator,
				true);

			array[1] = parameterGroup;

			array[2] = getByGroupSite_PrevAndNext(
				session, parameterGroup, groupId, deleted, orderByComparator,
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

	protected ParameterGroup getByGroupSite_PrevAndNext(
		Session session, ParameterGroup parameterGroup, long groupId,
		Boolean deleted, OrderByComparator<ParameterGroup> orderByComparator,
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

		sb.append(_SQL_SELECT_PARAMETERGROUP_WHERE);

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
			sb.append(ParameterGroupModelImpl.ORDER_BY_JPQL);
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
					orderByComparator.getOrderByConditionValues(
						parameterGroup)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<ParameterGroup> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the parameter groups where groupId = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 */
	@Override
	public void removeByGroupSite(long groupId, Boolean deleted) {
		for (ParameterGroup parameterGroup :
				findByGroupSite(
					groupId, deleted, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(parameterGroup);
		}
	}

	/**
	 * Returns the number of parameter groups where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the number of matching parameter groups
	 */
	@Override
	public int countByGroupSite(long groupId, Boolean deleted) {
		FinderPath finderPath = _finderPathCountByGroupSite;

		Object[] finderArgs = new Object[] {groupId, deleted};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_PARAMETERGROUP_WHERE);

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
		"parameterGroup.groupId = ? AND ";

	private static final String _FINDER_COLUMN_GROUPSITE_DELETED_2 =
		"parameterGroup.deleted = ?";

	private FinderPath _finderPathWithPaginationFindByGroupSiteCode;
	private FinderPath _finderPathWithoutPaginationFindByGroupSiteCode;
	private FinderPath _finderPathCountByGroupSiteCode;

	/**
	 * Returns all the parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the matching parameter groups
	 */
	@Override
	public List<ParameterGroup> findByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted) {

		return findByGroupSiteCode(
			groupId, groupCode, deleted, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @return the range of matching parameter groups
	 */
	@Override
	public List<ParameterGroup> findByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted, int start, int end) {

		return findByGroupSiteCode(
			groupId, groupCode, deleted, start, end, null);
	}

	/**
	 * Returns an ordered range of all the parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching parameter groups
	 */
	@Override
	public List<ParameterGroup> findByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted, int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return findByGroupSiteCode(
			groupId, groupCode, deleted, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching parameter groups
	 */
	@Override
	public List<ParameterGroup> findByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted, int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator,
		boolean useFinderCache) {

		groupCode = Objects.toString(groupCode, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByGroupSiteCode;
				finderArgs = new Object[] {groupId, groupCode, deleted};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByGroupSiteCode;
			finderArgs = new Object[] {
				groupId, groupCode, deleted, start, end, orderByComparator
			};
		}

		List<ParameterGroup> list = null;

		if (useFinderCache) {
			list = (List<ParameterGroup>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (ParameterGroup parameterGroup : list) {
					if ((groupId != parameterGroup.getGroupId()) ||
						!groupCode.equals(parameterGroup.getGroupCode()) ||
						!Objects.equals(deleted, parameterGroup.getDeleted())) {

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

			sb.append(_SQL_SELECT_PARAMETERGROUP_WHERE);

			sb.append(_FINDER_COLUMN_GROUPSITECODE_GROUPID_2);

			boolean bindGroupCode = false;

			if (groupCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_GROUPSITECODE_GROUPCODE_3);
			}
			else {
				bindGroupCode = true;

				sb.append(_FINDER_COLUMN_GROUPSITECODE_GROUPCODE_2);
			}

			sb.append(_FINDER_COLUMN_GROUPSITECODE_DELETED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(ParameterGroupModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindGroupCode) {
					queryPos.add(groupCode);
				}

				queryPos.add(deleted.booleanValue());

				list = (List<ParameterGroup>)QueryUtil.list(
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
	 * Returns the first parameter group in the ordered set where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup findByGroupSiteCode_First(
			long groupId, String groupCode, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws NoSuchParameterGroupException {

		ParameterGroup parameterGroup = fetchByGroupSiteCode_First(
			groupId, groupCode, deleted, orderByComparator);

		if (parameterGroup != null) {
			return parameterGroup;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", groupCode=");
		sb.append(groupCode);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchParameterGroupException(sb.toString());
	}

	/**
	 * Returns the first parameter group in the ordered set where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup fetchByGroupSiteCode_First(
		long groupId, String groupCode, Boolean deleted,
		OrderByComparator<ParameterGroup> orderByComparator) {

		List<ParameterGroup> list = findByGroupSiteCode(
			groupId, groupCode, deleted, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last parameter group in the ordered set where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group
	 * @throws NoSuchParameterGroupException if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup findByGroupSiteCode_Last(
			long groupId, String groupCode, Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws NoSuchParameterGroupException {

		ParameterGroup parameterGroup = fetchByGroupSiteCode_Last(
			groupId, groupCode, deleted, orderByComparator);

		if (parameterGroup != null) {
			return parameterGroup;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", groupCode=");
		sb.append(groupCode);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchParameterGroupException(sb.toString());
	}

	/**
	 * Returns the last parameter group in the ordered set where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching parameter group, or <code>null</code> if a matching parameter group could not be found
	 */
	@Override
	public ParameterGroup fetchByGroupSiteCode_Last(
		long groupId, String groupCode, Boolean deleted,
		OrderByComparator<ParameterGroup> orderByComparator) {

		int count = countByGroupSiteCode(groupId, groupCode, deleted);

		if (count == 0) {
			return null;
		}

		List<ParameterGroup> list = findByGroupSiteCode(
			groupId, groupCode, deleted, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the parameter groups before and after the current parameter group in the ordered set where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param parameterGroupId the primary key of the current parameter group
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next parameter group
	 * @throws NoSuchParameterGroupException if a parameter group with the primary key could not be found
	 */
	@Override
	public ParameterGroup[] findByGroupSiteCode_PrevAndNext(
			long parameterGroupId, long groupId, String groupCode,
			Boolean deleted,
			OrderByComparator<ParameterGroup> orderByComparator)
		throws NoSuchParameterGroupException {

		groupCode = Objects.toString(groupCode, "");

		ParameterGroup parameterGroup = findByPrimaryKey(parameterGroupId);

		Session session = null;

		try {
			session = openSession();

			ParameterGroup[] array = new ParameterGroupImpl[3];

			array[0] = getByGroupSiteCode_PrevAndNext(
				session, parameterGroup, groupId, groupCode, deleted,
				orderByComparator, true);

			array[1] = parameterGroup;

			array[2] = getByGroupSiteCode_PrevAndNext(
				session, parameterGroup, groupId, groupCode, deleted,
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

	protected ParameterGroup getByGroupSiteCode_PrevAndNext(
		Session session, ParameterGroup parameterGroup, long groupId,
		String groupCode, Boolean deleted,
		OrderByComparator<ParameterGroup> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		sb.append(_SQL_SELECT_PARAMETERGROUP_WHERE);

		sb.append(_FINDER_COLUMN_GROUPSITECODE_GROUPID_2);

		boolean bindGroupCode = false;

		if (groupCode.isEmpty()) {
			sb.append(_FINDER_COLUMN_GROUPSITECODE_GROUPCODE_3);
		}
		else {
			bindGroupCode = true;

			sb.append(_FINDER_COLUMN_GROUPSITECODE_GROUPCODE_2);
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
			sb.append(ParameterGroupModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		if (bindGroupCode) {
			queryPos.add(groupCode);
		}

		queryPos.add(deleted.booleanValue());

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						parameterGroup)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<ParameterGroup> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 */
	@Override
	public void removeByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted) {

		for (ParameterGroup parameterGroup :
				findByGroupSiteCode(
					groupId, groupCode, deleted, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(parameterGroup);
		}
	}

	/**
	 * Returns the number of parameter groups where groupId = &#63; and groupCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param groupCode the group code
	 * @param deleted the deleted
	 * @return the number of matching parameter groups
	 */
	@Override
	public int countByGroupSiteCode(
		long groupId, String groupCode, Boolean deleted) {

		groupCode = Objects.toString(groupCode, "");

		FinderPath finderPath = _finderPathCountByGroupSiteCode;

		Object[] finderArgs = new Object[] {groupId, groupCode, deleted};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_PARAMETERGROUP_WHERE);

			sb.append(_FINDER_COLUMN_GROUPSITECODE_GROUPID_2);

			boolean bindGroupCode = false;

			if (groupCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_GROUPSITECODE_GROUPCODE_3);
			}
			else {
				bindGroupCode = true;

				sb.append(_FINDER_COLUMN_GROUPSITECODE_GROUPCODE_2);
			}

			sb.append(_FINDER_COLUMN_GROUPSITECODE_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindGroupCode) {
					queryPos.add(groupCode);
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
		"parameterGroup.groupId = ? AND ";

	private static final String _FINDER_COLUMN_GROUPSITECODE_GROUPCODE_2 =
		"parameterGroup.groupCode = ? AND ";

	private static final String _FINDER_COLUMN_GROUPSITECODE_GROUPCODE_3 =
		"(parameterGroup.groupCode IS NULL OR parameterGroup.groupCode = '') AND ";

	private static final String _FINDER_COLUMN_GROUPSITECODE_DELETED_2 =
		"parameterGroup.deleted = ?";

	public ParameterGroupPersistenceImpl() {
		setModelClass(ParameterGroup.class);

		setModelImplClass(ParameterGroupImpl.class);
		setModelPKClass(long.class);
	}

	/**
	 * Caches the parameter group in the entity cache if it is enabled.
	 *
	 * @param parameterGroup the parameter group
	 */
	@Override
	public void cacheResult(ParameterGroup parameterGroup) {
		entityCache.putResult(
			ParameterGroupImpl.class, parameterGroup.getPrimaryKey(),
			parameterGroup);

		finderCache.putResult(
			_finderPathFetchByCode,
			new Object[] {
				parameterGroup.getGroupCode(), parameterGroup.getDeleted()
			},
			parameterGroup);
	}

	/**
	 * Caches the parameter groups in the entity cache if it is enabled.
	 *
	 * @param parameterGroups the parameter groups
	 */
	@Override
	public void cacheResult(List<ParameterGroup> parameterGroups) {
		for (ParameterGroup parameterGroup : parameterGroups) {
			if (entityCache.getResult(
					ParameterGroupImpl.class, parameterGroup.getPrimaryKey()) ==
						null) {

				cacheResult(parameterGroup);
			}
		}
	}

	/**
	 * Clears the cache for all parameter groups.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(ParameterGroupImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the parameter group.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(ParameterGroup parameterGroup) {
		entityCache.removeResult(ParameterGroupImpl.class, parameterGroup);
	}

	@Override
	public void clearCache(List<ParameterGroup> parameterGroups) {
		for (ParameterGroup parameterGroup : parameterGroups) {
			entityCache.removeResult(ParameterGroupImpl.class, parameterGroup);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(ParameterGroupImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		ParameterGroupModelImpl parameterGroupModelImpl) {

		Object[] args = new Object[] {
			parameterGroupModelImpl.getGroupCode(),
			parameterGroupModelImpl.getDeleted()
		};

		finderCache.putResult(
			_finderPathCountByCode, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByCode, args, parameterGroupModelImpl, false);
	}

	/**
	 * Creates a new parameter group with the primary key. Does not add the parameter group to the database.
	 *
	 * @param parameterGroupId the primary key for the new parameter group
	 * @return the new parameter group
	 */
	@Override
	public ParameterGroup create(long parameterGroupId) {
		ParameterGroup parameterGroup = new ParameterGroupImpl();

		parameterGroup.setNew(true);
		parameterGroup.setPrimaryKey(parameterGroupId);

		parameterGroup.setCompanyId(CompanyThreadLocal.getCompanyId());

		return parameterGroup;
	}

	/**
	 * Removes the parameter group with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param parameterGroupId the primary key of the parameter group
	 * @return the parameter group that was removed
	 * @throws NoSuchParameterGroupException if a parameter group with the primary key could not be found
	 */
	@Override
	public ParameterGroup remove(long parameterGroupId)
		throws NoSuchParameterGroupException {

		return remove((Serializable)parameterGroupId);
	}

	/**
	 * Removes the parameter group with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the parameter group
	 * @return the parameter group that was removed
	 * @throws NoSuchParameterGroupException if a parameter group with the primary key could not be found
	 */
	@Override
	public ParameterGroup remove(Serializable primaryKey)
		throws NoSuchParameterGroupException {

		Session session = null;

		try {
			session = openSession();

			ParameterGroup parameterGroup = (ParameterGroup)session.get(
				ParameterGroupImpl.class, primaryKey);

			if (parameterGroup == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchParameterGroupException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(parameterGroup);
		}
		catch (NoSuchParameterGroupException noSuchEntityException) {
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
	protected ParameterGroup removeImpl(ParameterGroup parameterGroup) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(parameterGroup)) {
				parameterGroup = (ParameterGroup)session.get(
					ParameterGroupImpl.class,
					parameterGroup.getPrimaryKeyObj());
			}

			if (parameterGroup != null) {
				session.delete(parameterGroup);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (parameterGroup != null) {
			clearCache(parameterGroup);
		}

		return parameterGroup;
	}

	@Override
	public ParameterGroup updateImpl(ParameterGroup parameterGroup) {
		boolean isNew = parameterGroup.isNew();

		if (!(parameterGroup instanceof ParameterGroupModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(parameterGroup.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					parameterGroup);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in parameterGroup proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom ParameterGroup implementation " +
					parameterGroup.getClass());
		}

		ParameterGroupModelImpl parameterGroupModelImpl =
			(ParameterGroupModelImpl)parameterGroup;

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(parameterGroup);
			}
			else {
				parameterGroup = (ParameterGroup)session.merge(parameterGroup);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			ParameterGroupImpl.class, parameterGroupModelImpl, false, true);

		cacheUniqueFindersCache(parameterGroupModelImpl);

		if (isNew) {
			parameterGroup.setNew(false);
		}

		parameterGroup.resetOriginalValues();

		return parameterGroup;
	}

	/**
	 * Returns the parameter group with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the parameter group
	 * @return the parameter group
	 * @throws NoSuchParameterGroupException if a parameter group with the primary key could not be found
	 */
	@Override
	public ParameterGroup findByPrimaryKey(Serializable primaryKey)
		throws NoSuchParameterGroupException {

		ParameterGroup parameterGroup = fetchByPrimaryKey(primaryKey);

		if (parameterGroup == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchParameterGroupException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return parameterGroup;
	}

	/**
	 * Returns the parameter group with the primary key or throws a <code>NoSuchParameterGroupException</code> if it could not be found.
	 *
	 * @param parameterGroupId the primary key of the parameter group
	 * @return the parameter group
	 * @throws NoSuchParameterGroupException if a parameter group with the primary key could not be found
	 */
	@Override
	public ParameterGroup findByPrimaryKey(long parameterGroupId)
		throws NoSuchParameterGroupException {

		return findByPrimaryKey((Serializable)parameterGroupId);
	}

	/**
	 * Returns the parameter group with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param parameterGroupId the primary key of the parameter group
	 * @return the parameter group, or <code>null</code> if a parameter group with the primary key could not be found
	 */
	@Override
	public ParameterGroup fetchByPrimaryKey(long parameterGroupId) {
		return fetchByPrimaryKey((Serializable)parameterGroupId);
	}

	/**
	 * Returns all the parameter groups.
	 *
	 * @return the parameter groups
	 */
	@Override
	public List<ParameterGroup> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the parameter groups.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @return the range of parameter groups
	 */
	@Override
	public List<ParameterGroup> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the parameter groups.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of parameter groups
	 */
	@Override
	public List<ParameterGroup> findAll(
		int start, int end,
		OrderByComparator<ParameterGroup> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the parameter groups.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ParameterGroupModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of parameter groups
	 * @param end the upper bound of the range of parameter groups (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of parameter groups
	 */
	@Override
	public List<ParameterGroup> findAll(
		int start, int end, OrderByComparator<ParameterGroup> orderByComparator,
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

		List<ParameterGroup> list = null;

		if (useFinderCache) {
			list = (List<ParameterGroup>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_PARAMETERGROUP);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_PARAMETERGROUP;

				sql = sql.concat(ParameterGroupModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<ParameterGroup>)QueryUtil.list(
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
	 * Removes all the parameter groups from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (ParameterGroup parameterGroup : findAll()) {
			remove(parameterGroup);
		}
	}

	/**
	 * Returns the number of parameter groups.
	 *
	 * @return the number of parameter groups
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_PARAMETERGROUP);

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
		return "parameterGroupId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_PARAMETERGROUP;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return ParameterGroupModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the parameter group persistence.
	 */
	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_argumentsResolverServiceRegistration = _bundleContext.registerService(
			ArgumentsResolver.class, new ParameterGroupModelArgumentsResolver(),
			MapUtil.singletonDictionary(
				"model.class.name", ParameterGroup.class.getName()));

		_finderPathWithPaginationFindAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathFetchByCode = _createFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByCode",
			new String[] {String.class.getName(), Boolean.class.getName()},
			new String[] {"groupCode", "deleted"}, true);

		_finderPathCountByCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCode",
			new String[] {String.class.getName(), Boolean.class.getName()},
			new String[] {"groupCode", "deleted"}, false);

		_finderPathWithPaginationFindByParentId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByParentId",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"parentId", "deleted"}, true);

		_finderPathWithoutPaginationFindByParentId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByParentId",
			new String[] {Long.class.getName(), Boolean.class.getName()},
			new String[] {"parentId", "deleted"}, true);

		_finderPathCountByParentId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByParentId",
			new String[] {Long.class.getName(), Boolean.class.getName()},
			new String[] {"parentId", "deleted"}, false);

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
			new String[] {"groupId", "groupCode", "deleted"}, true);

		_finderPathWithoutPaginationFindByGroupSiteCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroupSiteCode",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Boolean.class.getName()
			},
			new String[] {"groupId", "groupCode", "deleted"}, true);

		_finderPathCountByGroupSiteCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroupSiteCode",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Boolean.class.getName()
			},
			new String[] {"groupId", "groupCode", "deleted"}, false);
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(ParameterGroupImpl.class.getName());

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

	private static final String _SQL_SELECT_PARAMETERGROUP =
		"SELECT parameterGroup FROM ParameterGroup parameterGroup";

	private static final String _SQL_SELECT_PARAMETERGROUP_WHERE =
		"SELECT parameterGroup FROM ParameterGroup parameterGroup WHERE ";

	private static final String _SQL_COUNT_PARAMETERGROUP =
		"SELECT COUNT(parameterGroup) FROM ParameterGroup parameterGroup";

	private static final String _SQL_COUNT_PARAMETERGROUP_WHERE =
		"SELECT COUNT(parameterGroup) FROM ParameterGroup parameterGroup WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "parameterGroup.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No ParameterGroup exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No ParameterGroup exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		ParameterGroupPersistenceImpl.class);

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

	private static class ParameterGroupModelArgumentsResolver
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

			ParameterGroupModelImpl parameterGroupModelImpl =
				(ParameterGroupModelImpl)baseModel;

			long columnBitmask = parameterGroupModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(
					parameterGroupModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |=
						parameterGroupModelImpl.getColumnBitmask(columnName);
				}

				_finderPathColumnBitmasksCache.put(
					finderPath, finderPathColumnBitmask);
			}

			if ((columnBitmask & finderPathColumnBitmask) != 0) {
				return _getValue(
					parameterGroupModelImpl, columnNames, original);
			}

			return null;
		}

		private Object[] _getValue(
			ParameterGroupModelImpl parameterGroupModelImpl,
			String[] columnNames, boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] =
						parameterGroupModelImpl.getColumnOriginalValue(
							columnName);
				}
				else {
					arguments[i] = parameterGroupModelImpl.getColumnValue(
						columnName);
				}
			}

			return arguments;
		}

		private static Map<FinderPath, Long> _finderPathColumnBitmasksCache =
			new ConcurrentHashMap<>();

	}

}