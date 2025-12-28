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

package svc.ntuc.nlh.comments.service.persistence.impl;

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
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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

import svc.ntuc.nlh.comments.exception.NoSuchCommentException;
import svc.ntuc.nlh.comments.model.Comment;
import svc.ntuc.nlh.comments.model.impl.CommentImpl;
import svc.ntuc.nlh.comments.model.impl.CommentModelImpl;
import svc.ntuc.nlh.comments.service.persistence.CommentPersistence;
import svc.ntuc.nlh.comments.service.persistence.impl.constants.NTUC_COMMENTPersistenceConstants;

/**
 * The persistence implementation for the comment service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = CommentPersistence.class)
public class CommentPersistenceImpl
	extends BasePersistenceImpl<Comment> implements CommentPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>CommentUtil</code> to access the comment persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		CommentImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByUuid;
	private FinderPath _finderPathWithoutPaginationFindByUuid;
	private FinderPath _finderPathCountByUuid;

	/**
	 * Returns all the comments where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching comments
	 */
	@Override
	public List<Comment> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the comments where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommentModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of comments
	 * @param end the upper bound of the range of comments (not inclusive)
	 * @return the range of matching comments
	 */
	@Override
	public List<Comment> findByUuid(String uuid, int start, int end) {
		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the comments where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommentModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of comments
	 * @param end the upper bound of the range of comments (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching comments
	 */
	@Override
	public List<Comment> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<Comment> orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the comments where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommentModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of comments
	 * @param end the upper bound of the range of comments (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching comments
	 */
	@Override
	public List<Comment> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<Comment> orderByComparator, boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid;
				finderArgs = new Object[] {uuid};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid;
			finderArgs = new Object[] {uuid, start, end, orderByComparator};
		}

		List<Comment> list = null;

		if (useFinderCache) {
			list = (List<Comment>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Comment comment : list) {
					if (!uuid.equals(comment.getUuid())) {
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

			sb.append(_SQL_SELECT_COMMENT_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommentModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				list = (List<Comment>)QueryUtil.list(
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
	 * Returns the first comment in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching comment
	 * @throws NoSuchCommentException if a matching comment could not be found
	 */
	@Override
	public Comment findByUuid_First(
			String uuid, OrderByComparator<Comment> orderByComparator)
		throws NoSuchCommentException {

		Comment comment = fetchByUuid_First(uuid, orderByComparator);

		if (comment != null) {
			return comment;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchCommentException(sb.toString());
	}

	/**
	 * Returns the first comment in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching comment, or <code>null</code> if a matching comment could not be found
	 */
	@Override
	public Comment fetchByUuid_First(
		String uuid, OrderByComparator<Comment> orderByComparator) {

		List<Comment> list = findByUuid(uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last comment in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching comment
	 * @throws NoSuchCommentException if a matching comment could not be found
	 */
	@Override
	public Comment findByUuid_Last(
			String uuid, OrderByComparator<Comment> orderByComparator)
		throws NoSuchCommentException {

		Comment comment = fetchByUuid_Last(uuid, orderByComparator);

		if (comment != null) {
			return comment;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchCommentException(sb.toString());
	}

	/**
	 * Returns the last comment in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching comment, or <code>null</code> if a matching comment could not be found
	 */
	@Override
	public Comment fetchByUuid_Last(
		String uuid, OrderByComparator<Comment> orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<Comment> list = findByUuid(
			uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the comments before and after the current comment in the ordered set where uuid = &#63;.
	 *
	 * @param commentId the primary key of the current comment
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next comment
	 * @throws NoSuchCommentException if a comment with the primary key could not be found
	 */
	@Override
	public Comment[] findByUuid_PrevAndNext(
			long commentId, String uuid,
			OrderByComparator<Comment> orderByComparator)
		throws NoSuchCommentException {

		uuid = Objects.toString(uuid, "");

		Comment comment = findByPrimaryKey(commentId);

		Session session = null;

		try {
			session = openSession();

			Comment[] array = new CommentImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, comment, uuid, orderByComparator, true);

			array[1] = comment;

			array[2] = getByUuid_PrevAndNext(
				session, comment, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected Comment getByUuid_PrevAndNext(
		Session session, Comment comment, String uuid,
		OrderByComparator<Comment> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_COMMENT_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2);
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
			sb.append(CommentModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(comment)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Comment> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the comments where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (Comment comment :
				findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(comment);
		}
	}

	/**
	 * Returns the number of comments where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching comments
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid;

		Object[] finderArgs = new Object[] {uuid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_COMMENT_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
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

	private static final String _FINDER_COLUMN_UUID_UUID_2 = "comment.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(comment.uuid IS NULL OR comment.uuid = '')";

	private FinderPath _finderPathFetchByUUID_G;
	private FinderPath _finderPathCountByUUID_G;

	/**
	 * Returns the comment where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchCommentException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching comment
	 * @throws NoSuchCommentException if a matching comment could not be found
	 */
	@Override
	public Comment findByUUID_G(String uuid, long groupId)
		throws NoSuchCommentException {

		Comment comment = fetchByUUID_G(uuid, groupId);

		if (comment == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("uuid=");
			sb.append(uuid);

			sb.append(", groupId=");
			sb.append(groupId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchCommentException(sb.toString());
		}

		return comment;
	}

	/**
	 * Returns the comment where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching comment, or <code>null</code> if a matching comment could not be found
	 */
	@Override
	public Comment fetchByUUID_G(String uuid, long groupId) {
		return fetchByUUID_G(uuid, groupId, true);
	}

	/**
	 * Returns the comment where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching comment, or <code>null</code> if a matching comment could not be found
	 */
	@Override
	public Comment fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {uuid, groupId};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByUUID_G, finderArgs, this);
		}

		if (result instanceof Comment) {
			Comment comment = (Comment)result;

			if (!Objects.equals(uuid, comment.getUuid()) ||
				(groupId != comment.getGroupId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_COMMENT_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_G_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_G_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(groupId);

				List<Comment> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByUUID_G, finderArgs, list);
					}
				}
				else {
					Comment comment = list.get(0);

					result = comment;

					cacheResult(comment);
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
			return (Comment)result;
		}
	}

	/**
	 * Removes the comment where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the comment that was removed
	 */
	@Override
	public Comment removeByUUID_G(String uuid, long groupId)
		throws NoSuchCommentException {

		Comment comment = findByUUID_G(uuid, groupId);

		return remove(comment);
	}

	/**
	 * Returns the number of comments where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching comments
	 */
	@Override
	public int countByUUID_G(String uuid, long groupId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUUID_G;

		Object[] finderArgs = new Object[] {uuid, groupId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMENT_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_G_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_G_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(groupId);

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

	private static final String _FINDER_COLUMN_UUID_G_UUID_2 =
		"comment.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_G_UUID_3 =
		"(comment.uuid IS NULL OR comment.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_G_GROUPID_2 =
		"comment.groupId = ?";

	private FinderPath _finderPathWithPaginationFindByUuid_C;
	private FinderPath _finderPathWithoutPaginationFindByUuid_C;
	private FinderPath _finderPathCountByUuid_C;

	/**
	 * Returns all the comments where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching comments
	 */
	@Override
	public List<Comment> findByUuid_C(String uuid, long companyId) {
		return findByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the comments where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommentModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of comments
	 * @param end the upper bound of the range of comments (not inclusive)
	 * @return the range of matching comments
	 */
	@Override
	public List<Comment> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the comments where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommentModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of comments
	 * @param end the upper bound of the range of comments (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching comments
	 */
	@Override
	public List<Comment> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Comment> orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the comments where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommentModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of comments
	 * @param end the upper bound of the range of comments (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching comments
	 */
	@Override
	public List<Comment> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Comment> orderByComparator, boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid_C;
				finderArgs = new Object[] {uuid, companyId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid_C;
			finderArgs = new Object[] {
				uuid, companyId, start, end, orderByComparator
			};
		}

		List<Comment> list = null;

		if (useFinderCache) {
			list = (List<Comment>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Comment comment : list) {
					if (!uuid.equals(comment.getUuid()) ||
						(companyId != comment.getCompanyId())) {

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

			sb.append(_SQL_SELECT_COMMENT_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommentModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				list = (List<Comment>)QueryUtil.list(
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
	 * Returns the first comment in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching comment
	 * @throws NoSuchCommentException if a matching comment could not be found
	 */
	@Override
	public Comment findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<Comment> orderByComparator)
		throws NoSuchCommentException {

		Comment comment = fetchByUuid_C_First(
			uuid, companyId, orderByComparator);

		if (comment != null) {
			return comment;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchCommentException(sb.toString());
	}

	/**
	 * Returns the first comment in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching comment, or <code>null</code> if a matching comment could not be found
	 */
	@Override
	public Comment fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<Comment> orderByComparator) {

		List<Comment> list = findByUuid_C(
			uuid, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last comment in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching comment
	 * @throws NoSuchCommentException if a matching comment could not be found
	 */
	@Override
	public Comment findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<Comment> orderByComparator)
		throws NoSuchCommentException {

		Comment comment = fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);

		if (comment != null) {
			return comment;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchCommentException(sb.toString());
	}

	/**
	 * Returns the last comment in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching comment, or <code>null</code> if a matching comment could not be found
	 */
	@Override
	public Comment fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<Comment> orderByComparator) {

		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<Comment> list = findByUuid_C(
			uuid, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the comments before and after the current comment in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param commentId the primary key of the current comment
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next comment
	 * @throws NoSuchCommentException if a comment with the primary key could not be found
	 */
	@Override
	public Comment[] findByUuid_C_PrevAndNext(
			long commentId, String uuid, long companyId,
			OrderByComparator<Comment> orderByComparator)
		throws NoSuchCommentException {

		uuid = Objects.toString(uuid, "");

		Comment comment = findByPrimaryKey(commentId);

		Session session = null;

		try {
			session = openSession();

			Comment[] array = new CommentImpl[3];

			array[0] = getByUuid_C_PrevAndNext(
				session, comment, uuid, companyId, orderByComparator, true);

			array[1] = comment;

			array[2] = getByUuid_C_PrevAndNext(
				session, comment, uuid, companyId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected Comment getByUuid_C_PrevAndNext(
		Session session, Comment comment, String uuid, long companyId,
		OrderByComparator<Comment> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_COMMENT_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

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
			sb.append(CommentModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(comment)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Comment> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the comments where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (Comment comment :
				findByUuid_C(
					uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(comment);
		}
	}

	/**
	 * Returns the number of comments where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching comments
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid_C;

		Object[] finderArgs = new Object[] {uuid, companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMENT_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

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

	private static final String _FINDER_COLUMN_UUID_C_UUID_2 =
		"comment.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3 =
		"(comment.uuid IS NULL OR comment.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 =
		"comment.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByName;
	private FinderPath _finderPathWithoutPaginationFindByName;
	private FinderPath _finderPathCountByName;

	/**
	 * Returns all the comments where groupId = &#63; and commentName = &#63;.
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 * @return the matching comments
	 */
	@Override
	public List<Comment> findByName(long groupId, String commentName) {
		return findByName(
			groupId, commentName, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the comments where groupId = &#63; and commentName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommentModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 * @param start the lower bound of the range of comments
	 * @param end the upper bound of the range of comments (not inclusive)
	 * @return the range of matching comments
	 */
	@Override
	public List<Comment> findByName(
		long groupId, String commentName, int start, int end) {

		return findByName(groupId, commentName, start, end, null);
	}

	/**
	 * Returns an ordered range of all the comments where groupId = &#63; and commentName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommentModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 * @param start the lower bound of the range of comments
	 * @param end the upper bound of the range of comments (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching comments
	 */
	@Override
	public List<Comment> findByName(
		long groupId, String commentName, int start, int end,
		OrderByComparator<Comment> orderByComparator) {

		return findByName(
			groupId, commentName, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the comments where groupId = &#63; and commentName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommentModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 * @param start the lower bound of the range of comments
	 * @param end the upper bound of the range of comments (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching comments
	 */
	@Override
	public List<Comment> findByName(
		long groupId, String commentName, int start, int end,
		OrderByComparator<Comment> orderByComparator, boolean useFinderCache) {

		commentName = Objects.toString(commentName, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByName;
				finderArgs = new Object[] {groupId, commentName};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByName;
			finderArgs = new Object[] {
				groupId, commentName, start, end, orderByComparator
			};
		}

		List<Comment> list = null;

		if (useFinderCache) {
			list = (List<Comment>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Comment comment : list) {
					if ((groupId != comment.getGroupId()) ||
						!commentName.equals(comment.getCommentName())) {

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

			sb.append(_SQL_SELECT_COMMENT_WHERE);

			sb.append(_FINDER_COLUMN_NAME_GROUPID_2);

			boolean bindCommentName = false;

			if (commentName.isEmpty()) {
				sb.append(_FINDER_COLUMN_NAME_COMMENTNAME_3);
			}
			else {
				bindCommentName = true;

				sb.append(_FINDER_COLUMN_NAME_COMMENTNAME_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommentModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindCommentName) {
					queryPos.add(commentName);
				}

				list = (List<Comment>)QueryUtil.list(
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
	 * Returns the first comment in the ordered set where groupId = &#63; and commentName = &#63;.
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching comment
	 * @throws NoSuchCommentException if a matching comment could not be found
	 */
	@Override
	public Comment findByName_First(
			long groupId, String commentName,
			OrderByComparator<Comment> orderByComparator)
		throws NoSuchCommentException {

		Comment comment = fetchByName_First(
			groupId, commentName, orderByComparator);

		if (comment != null) {
			return comment;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", commentName=");
		sb.append(commentName);

		sb.append("}");

		throw new NoSuchCommentException(sb.toString());
	}

	/**
	 * Returns the first comment in the ordered set where groupId = &#63; and commentName = &#63;.
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching comment, or <code>null</code> if a matching comment could not be found
	 */
	@Override
	public Comment fetchByName_First(
		long groupId, String commentName,
		OrderByComparator<Comment> orderByComparator) {

		List<Comment> list = findByName(
			groupId, commentName, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last comment in the ordered set where groupId = &#63; and commentName = &#63;.
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching comment
	 * @throws NoSuchCommentException if a matching comment could not be found
	 */
	@Override
	public Comment findByName_Last(
			long groupId, String commentName,
			OrderByComparator<Comment> orderByComparator)
		throws NoSuchCommentException {

		Comment comment = fetchByName_Last(
			groupId, commentName, orderByComparator);

		if (comment != null) {
			return comment;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", commentName=");
		sb.append(commentName);

		sb.append("}");

		throw new NoSuchCommentException(sb.toString());
	}

	/**
	 * Returns the last comment in the ordered set where groupId = &#63; and commentName = &#63;.
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching comment, or <code>null</code> if a matching comment could not be found
	 */
	@Override
	public Comment fetchByName_Last(
		long groupId, String commentName,
		OrderByComparator<Comment> orderByComparator) {

		int count = countByName(groupId, commentName);

		if (count == 0) {
			return null;
		}

		List<Comment> list = findByName(
			groupId, commentName, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the comments before and after the current comment in the ordered set where groupId = &#63; and commentName = &#63;.
	 *
	 * @param commentId the primary key of the current comment
	 * @param groupId the group ID
	 * @param commentName the comment name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next comment
	 * @throws NoSuchCommentException if a comment with the primary key could not be found
	 */
	@Override
	public Comment[] findByName_PrevAndNext(
			long commentId, long groupId, String commentName,
			OrderByComparator<Comment> orderByComparator)
		throws NoSuchCommentException {

		commentName = Objects.toString(commentName, "");

		Comment comment = findByPrimaryKey(commentId);

		Session session = null;

		try {
			session = openSession();

			Comment[] array = new CommentImpl[3];

			array[0] = getByName_PrevAndNext(
				session, comment, groupId, commentName, orderByComparator,
				true);

			array[1] = comment;

			array[2] = getByName_PrevAndNext(
				session, comment, groupId, commentName, orderByComparator,
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

	protected Comment getByName_PrevAndNext(
		Session session, Comment comment, long groupId, String commentName,
		OrderByComparator<Comment> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_COMMENT_WHERE);

		sb.append(_FINDER_COLUMN_NAME_GROUPID_2);

		boolean bindCommentName = false;

		if (commentName.isEmpty()) {
			sb.append(_FINDER_COLUMN_NAME_COMMENTNAME_3);
		}
		else {
			bindCommentName = true;

			sb.append(_FINDER_COLUMN_NAME_COMMENTNAME_2);
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
			sb.append(CommentModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		if (bindCommentName) {
			queryPos.add(commentName);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(comment)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Comment> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the comments where groupId = &#63; and commentName = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 */
	@Override
	public void removeByName(long groupId, String commentName) {
		for (Comment comment :
				findByName(
					groupId, commentName, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(comment);
		}
	}

	/**
	 * Returns the number of comments where groupId = &#63; and commentName = &#63;.
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 * @return the number of matching comments
	 */
	@Override
	public int countByName(long groupId, String commentName) {
		commentName = Objects.toString(commentName, "");

		FinderPath finderPath = _finderPathCountByName;

		Object[] finderArgs = new Object[] {groupId, commentName};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMENT_WHERE);

			sb.append(_FINDER_COLUMN_NAME_GROUPID_2);

			boolean bindCommentName = false;

			if (commentName.isEmpty()) {
				sb.append(_FINDER_COLUMN_NAME_COMMENTNAME_3);
			}
			else {
				bindCommentName = true;

				sb.append(_FINDER_COLUMN_NAME_COMMENTNAME_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindCommentName) {
					queryPos.add(commentName);
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

	private static final String _FINDER_COLUMN_NAME_GROUPID_2 =
		"comment.groupId = ? AND ";

	private static final String _FINDER_COLUMN_NAME_COMMENTNAME_2 =
		"comment.commentName = ?";

	private static final String _FINDER_COLUMN_NAME_COMMENTNAME_3 =
		"(comment.commentName IS NULL OR comment.commentName = '')";

	private FinderPath _finderPathFetchByEmail;
	private FinderPath _finderPathCountByEmail;

	/**
	 * Returns the comment where groupId = &#63; and commentEmail = &#63; or throws a <code>NoSuchCommentException</code> if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param commentEmail the comment email
	 * @return the matching comment
	 * @throws NoSuchCommentException if a matching comment could not be found
	 */
	@Override
	public Comment findByEmail(long groupId, String commentEmail)
		throws NoSuchCommentException {

		Comment comment = fetchByEmail(groupId, commentEmail);

		if (comment == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("groupId=");
			sb.append(groupId);

			sb.append(", commentEmail=");
			sb.append(commentEmail);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchCommentException(sb.toString());
		}

		return comment;
	}

	/**
	 * Returns the comment where groupId = &#63; and commentEmail = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param commentEmail the comment email
	 * @return the matching comment, or <code>null</code> if a matching comment could not be found
	 */
	@Override
	public Comment fetchByEmail(long groupId, String commentEmail) {
		return fetchByEmail(groupId, commentEmail, true);
	}

	/**
	 * Returns the comment where groupId = &#63; and commentEmail = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param commentEmail the comment email
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching comment, or <code>null</code> if a matching comment could not be found
	 */
	@Override
	public Comment fetchByEmail(
		long groupId, String commentEmail, boolean useFinderCache) {

		commentEmail = Objects.toString(commentEmail, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {groupId, commentEmail};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByEmail, finderArgs, this);
		}

		if (result instanceof Comment) {
			Comment comment = (Comment)result;

			if ((groupId != comment.getGroupId()) ||
				!Objects.equals(commentEmail, comment.getCommentEmail())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_COMMENT_WHERE);

			sb.append(_FINDER_COLUMN_EMAIL_GROUPID_2);

			boolean bindCommentEmail = false;

			if (commentEmail.isEmpty()) {
				sb.append(_FINDER_COLUMN_EMAIL_COMMENTEMAIL_3);
			}
			else {
				bindCommentEmail = true;

				sb.append(_FINDER_COLUMN_EMAIL_COMMENTEMAIL_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindCommentEmail) {
					queryPos.add(commentEmail);
				}

				List<Comment> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByEmail, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {
									groupId, commentEmail
								};
							}

							_log.warn(
								"CommentPersistenceImpl.fetchByEmail(long, String, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					Comment comment = list.get(0);

					result = comment;

					cacheResult(comment);
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
			return (Comment)result;
		}
	}

	/**
	 * Removes the comment where groupId = &#63; and commentEmail = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param commentEmail the comment email
	 * @return the comment that was removed
	 */
	@Override
	public Comment removeByEmail(long groupId, String commentEmail)
		throws NoSuchCommentException {

		Comment comment = findByEmail(groupId, commentEmail);

		return remove(comment);
	}

	/**
	 * Returns the number of comments where groupId = &#63; and commentEmail = &#63;.
	 *
	 * @param groupId the group ID
	 * @param commentEmail the comment email
	 * @return the number of matching comments
	 */
	@Override
	public int countByEmail(long groupId, String commentEmail) {
		commentEmail = Objects.toString(commentEmail, "");

		FinderPath finderPath = _finderPathCountByEmail;

		Object[] finderArgs = new Object[] {groupId, commentEmail};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMENT_WHERE);

			sb.append(_FINDER_COLUMN_EMAIL_GROUPID_2);

			boolean bindCommentEmail = false;

			if (commentEmail.isEmpty()) {
				sb.append(_FINDER_COLUMN_EMAIL_COMMENTEMAIL_3);
			}
			else {
				bindCommentEmail = true;

				sb.append(_FINDER_COLUMN_EMAIL_COMMENTEMAIL_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindCommentEmail) {
					queryPos.add(commentEmail);
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

	private static final String _FINDER_COLUMN_EMAIL_GROUPID_2 =
		"comment.groupId = ? AND ";

	private static final String _FINDER_COLUMN_EMAIL_COMMENTEMAIL_2 =
		"comment.commentEmail = ?";

	private static final String _FINDER_COLUMN_EMAIL_COMMENTEMAIL_3 =
		"(comment.commentEmail IS NULL OR comment.commentEmail = '')";

	public CommentPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");

		setDBColumnNames(dbColumnNames);

		setModelClass(Comment.class);

		setModelImplClass(CommentImpl.class);
		setModelPKClass(long.class);
	}

	/**
	 * Caches the comment in the entity cache if it is enabled.
	 *
	 * @param comment the comment
	 */
	@Override
	public void cacheResult(Comment comment) {
		entityCache.putResult(
			CommentImpl.class, comment.getPrimaryKey(), comment);

		finderCache.putResult(
			_finderPathFetchByUUID_G,
			new Object[] {comment.getUuid(), comment.getGroupId()}, comment);

		finderCache.putResult(
			_finderPathFetchByEmail,
			new Object[] {comment.getGroupId(), comment.getCommentEmail()},
			comment);
	}

	/**
	 * Caches the comments in the entity cache if it is enabled.
	 *
	 * @param comments the comments
	 */
	@Override
	public void cacheResult(List<Comment> comments) {
		for (Comment comment : comments) {
			if (entityCache.getResult(
					CommentImpl.class, comment.getPrimaryKey()) == null) {

				cacheResult(comment);
			}
		}
	}

	/**
	 * Clears the cache for all comments.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(CommentImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the comment.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Comment comment) {
		entityCache.removeResult(CommentImpl.class, comment);
	}

	@Override
	public void clearCache(List<Comment> comments) {
		for (Comment comment : comments) {
			entityCache.removeResult(CommentImpl.class, comment);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(CommentImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(CommentModelImpl commentModelImpl) {
		Object[] args = new Object[] {
			commentModelImpl.getUuid(), commentModelImpl.getGroupId()
		};

		finderCache.putResult(
			_finderPathCountByUUID_G, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByUUID_G, args, commentModelImpl, false);

		args = new Object[] {
			commentModelImpl.getGroupId(), commentModelImpl.getCommentEmail()
		};

		finderCache.putResult(
			_finderPathCountByEmail, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByEmail, args, commentModelImpl, false);
	}

	/**
	 * Creates a new comment with the primary key. Does not add the comment to the database.
	 *
	 * @param commentId the primary key for the new comment
	 * @return the new comment
	 */
	@Override
	public Comment create(long commentId) {
		Comment comment = new CommentImpl();

		comment.setNew(true);
		comment.setPrimaryKey(commentId);

		String uuid = PortalUUIDUtil.generate();

		comment.setUuid(uuid);

		comment.setCompanyId(CompanyThreadLocal.getCompanyId());

		return comment;
	}

	/**
	 * Removes the comment with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commentId the primary key of the comment
	 * @return the comment that was removed
	 * @throws NoSuchCommentException if a comment with the primary key could not be found
	 */
	@Override
	public Comment remove(long commentId) throws NoSuchCommentException {
		return remove((Serializable)commentId);
	}

	/**
	 * Removes the comment with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the comment
	 * @return the comment that was removed
	 * @throws NoSuchCommentException if a comment with the primary key could not be found
	 */
	@Override
	public Comment remove(Serializable primaryKey)
		throws NoSuchCommentException {

		Session session = null;

		try {
			session = openSession();

			Comment comment = (Comment)session.get(
				CommentImpl.class, primaryKey);

			if (comment == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchCommentException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(comment);
		}
		catch (NoSuchCommentException noSuchEntityException) {
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
	protected Comment removeImpl(Comment comment) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(comment)) {
				comment = (Comment)session.get(
					CommentImpl.class, comment.getPrimaryKeyObj());
			}

			if (comment != null) {
				session.delete(comment);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (comment != null) {
			clearCache(comment);
		}

		return comment;
	}

	@Override
	public Comment updateImpl(Comment comment) {
		boolean isNew = comment.isNew();

		if (!(comment instanceof CommentModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(comment.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(comment);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in comment proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom Comment implementation " +
					comment.getClass());
		}

		CommentModelImpl commentModelImpl = (CommentModelImpl)comment;

		if (Validator.isNull(comment.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			comment.setUuid(uuid);
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (comment.getCreateDate() == null)) {
			if (serviceContext == null) {
				comment.setCreateDate(now);
			}
			else {
				comment.setCreateDate(serviceContext.getCreateDate(now));
			}
		}

		if (!commentModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				comment.setModifiedDate(now);
			}
			else {
				comment.setModifiedDate(serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(comment);
			}
			else {
				comment = (Comment)session.merge(comment);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(CommentImpl.class, commentModelImpl, false, true);

		cacheUniqueFindersCache(commentModelImpl);

		if (isNew) {
			comment.setNew(false);
		}

		comment.resetOriginalValues();

		return comment;
	}

	/**
	 * Returns the comment with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the comment
	 * @return the comment
	 * @throws NoSuchCommentException if a comment with the primary key could not be found
	 */
	@Override
	public Comment findByPrimaryKey(Serializable primaryKey)
		throws NoSuchCommentException {

		Comment comment = fetchByPrimaryKey(primaryKey);

		if (comment == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchCommentException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return comment;
	}

	/**
	 * Returns the comment with the primary key or throws a <code>NoSuchCommentException</code> if it could not be found.
	 *
	 * @param commentId the primary key of the comment
	 * @return the comment
	 * @throws NoSuchCommentException if a comment with the primary key could not be found
	 */
	@Override
	public Comment findByPrimaryKey(long commentId)
		throws NoSuchCommentException {

		return findByPrimaryKey((Serializable)commentId);
	}

	/**
	 * Returns the comment with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commentId the primary key of the comment
	 * @return the comment, or <code>null</code> if a comment with the primary key could not be found
	 */
	@Override
	public Comment fetchByPrimaryKey(long commentId) {
		return fetchByPrimaryKey((Serializable)commentId);
	}

	/**
	 * Returns all the comments.
	 *
	 * @return the comments
	 */
	@Override
	public List<Comment> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the comments.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommentModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of comments
	 * @param end the upper bound of the range of comments (not inclusive)
	 * @return the range of comments
	 */
	@Override
	public List<Comment> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the comments.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommentModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of comments
	 * @param end the upper bound of the range of comments (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of comments
	 */
	@Override
	public List<Comment> findAll(
		int start, int end, OrderByComparator<Comment> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the comments.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommentModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of comments
	 * @param end the upper bound of the range of comments (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of comments
	 */
	@Override
	public List<Comment> findAll(
		int start, int end, OrderByComparator<Comment> orderByComparator,
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

		List<Comment> list = null;

		if (useFinderCache) {
			list = (List<Comment>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_COMMENT);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_COMMENT;

				sql = sql.concat(CommentModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<Comment>)QueryUtil.list(
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
	 * Removes all the comments from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (Comment comment : findAll()) {
			remove(comment);
		}
	}

	/**
	 * Returns the number of comments.
	 *
	 * @return the number of comments
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_COMMENT);

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
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "commentId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_COMMENT;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return CommentModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the comment persistence.
	 */
	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_argumentsResolverServiceRegistration = _bundleContext.registerService(
			ArgumentsResolver.class, new CommentModelArgumentsResolver(),
			MapUtil.singletonDictionary(
				"model.class.name", Comment.class.getName()));

		_finderPathWithPaginationFindAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathWithPaginationFindByUuid = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"uuid_"}, true);

		_finderPathWithoutPaginationFindByUuid = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
			new String[] {String.class.getName()}, new String[] {"uuid_"},
			true);

		_finderPathCountByUuid = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
			new String[] {String.class.getName()}, new String[] {"uuid_"},
			false);

		_finderPathFetchByUUID_G = _createFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByUUID_G",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "groupId"}, true);

		_finderPathCountByUUID_G = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUUID_G",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "groupId"}, false);

		_finderPathWithPaginationFindByUuid_C = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid_C",
			new String[] {
				String.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"uuid_", "companyId"}, true);

		_finderPathWithoutPaginationFindByUuid_C = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "companyId"}, true);

		_finderPathCountByUuid_C = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "companyId"}, false);

		_finderPathWithPaginationFindByName = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByName",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"groupId", "commentName"}, true);

		_finderPathWithoutPaginationFindByName = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByName",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"groupId", "commentName"}, true);

		_finderPathCountByName = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByName",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"groupId", "commentName"}, false);

		_finderPathFetchByEmail = _createFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByEmail",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"groupId", "commentEmail"}, true);

		_finderPathCountByEmail = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByEmail",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"groupId", "commentEmail"}, false);
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(CommentImpl.class.getName());

		_argumentsResolverServiceRegistration.unregister();

		for (ServiceRegistration<FinderPath> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	@Override
	@Reference(
		target = NTUC_COMMENTPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = NTUC_COMMENTPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = NTUC_COMMENTPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
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

	private static final String _SQL_SELECT_COMMENT =
		"SELECT comment FROM Comment comment";

	private static final String _SQL_SELECT_COMMENT_WHERE =
		"SELECT comment FROM Comment comment WHERE ";

	private static final String _SQL_COUNT_COMMENT =
		"SELECT COUNT(comment) FROM Comment comment";

	private static final String _SQL_COUNT_COMMENT_WHERE =
		"SELECT COUNT(comment) FROM Comment comment WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "comment.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No Comment exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No Comment exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		CommentPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid"});

	static {
		try {
			Class.forName(NTUC_COMMENTPersistenceConstants.class.getName());
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

	private static class CommentModelArgumentsResolver
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

			CommentModelImpl commentModelImpl = (CommentModelImpl)baseModel;

			long columnBitmask = commentModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(commentModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |=
						commentModelImpl.getColumnBitmask(columnName);
				}

				_finderPathColumnBitmasksCache.put(
					finderPath, finderPathColumnBitmask);
			}

			if ((columnBitmask & finderPathColumnBitmask) != 0) {
				return _getValue(commentModelImpl, columnNames, original);
			}

			return null;
		}

		private Object[] _getValue(
			CommentModelImpl commentModelImpl, String[] columnNames,
			boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] = commentModelImpl.getColumnOriginalValue(
						columnName);
				}
				else {
					arguments[i] = commentModelImpl.getColumnValue(columnName);
				}
			}

			return arguments;
		}

		private static Map<FinderPath, Long> _finderPathColumnBitmasksCache =
			new ConcurrentHashMap<>();

	}

}