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

package svc.ntuc.nlh.course.admin.service.persistence.impl;

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

import svc.ntuc.nlh.course.admin.exception.NoSuchCourseException;
import svc.ntuc.nlh.course.admin.model.Course;
import svc.ntuc.nlh.course.admin.model.impl.CourseImpl;
import svc.ntuc.nlh.course.admin.model.impl.CourseModelImpl;
import svc.ntuc.nlh.course.admin.service.persistence.CoursePersistence;
import svc.ntuc.nlh.course.admin.service.persistence.impl.constants.NTUC_COURSE_ADMINPersistenceConstants;

/**
 * The persistence implementation for the course service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = CoursePersistence.class)
public class CoursePersistenceImpl
	extends BasePersistenceImpl<Course> implements CoursePersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>CourseUtil</code> to access the course persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		CourseImpl.class.getName();

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
	 * Returns all the courses where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching courses
	 */
	@Override
	public List<Course> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the courses where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of matching courses
	 */
	@Override
	public List<Course> findByUuid(String uuid, int start, int end) {
		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the courses where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching courses
	 */
	@Override
	public List<Course> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the courses where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching courses
	 */
	@Override
	public List<Course> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<Course> orderByComparator, boolean useFinderCache) {

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

		List<Course> list = null;

		if (useFinderCache) {
			list = (List<Course>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Course course : list) {
					if (!uuid.equals(course.getUuid())) {
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

			sb.append(_SQL_SELECT_COURSE_WHERE);

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
				sb.append(CourseModelImpl.ORDER_BY_JPQL);
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

				list = (List<Course>)QueryUtil.list(
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
	 * Returns the first course in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByUuid_First(
			String uuid, OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = fetchByUuid_First(uuid, orderByComparator);

		if (course != null) {
			return course;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchCourseException(sb.toString());
	}

	/**
	 * Returns the first course in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByUuid_First(
		String uuid, OrderByComparator<Course> orderByComparator) {

		List<Course> list = findByUuid(uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last course in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByUuid_Last(
			String uuid, OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = fetchByUuid_Last(uuid, orderByComparator);

		if (course != null) {
			return course;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchCourseException(sb.toString());
	}

	/**
	 * Returns the last course in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByUuid_Last(
		String uuid, OrderByComparator<Course> orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<Course> list = findByUuid(
			uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the courses before and after the current course in the ordered set where uuid = &#63;.
	 *
	 * @param courseId the primary key of the current course
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	@Override
	public Course[] findByUuid_PrevAndNext(
			long courseId, String uuid,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		uuid = Objects.toString(uuid, "");

		Course course = findByPrimaryKey(courseId);

		Session session = null;

		try {
			session = openSession();

			Course[] array = new CourseImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, course, uuid, orderByComparator, true);

			array[1] = course;

			array[2] = getByUuid_PrevAndNext(
				session, course, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected Course getByUuid_PrevAndNext(
		Session session, Course course, String uuid,
		OrderByComparator<Course> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_COURSE_WHERE);

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
			sb.append(CourseModelImpl.ORDER_BY_JPQL);
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
					orderByComparator.getOrderByConditionValues(course)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Course> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the courses where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (Course course :
				findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(course);
		}
	}

	/**
	 * Returns the number of courses where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching courses
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid;

		Object[] finderArgs = new Object[] {uuid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_COURSE_WHERE);

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

	private static final String _FINDER_COLUMN_UUID_UUID_2 = "course.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(course.uuid IS NULL OR course.uuid = '')";

	private FinderPath _finderPathFetchByUUID_G;
	private FinderPath _finderPathCountByUUID_G;

	/**
	 * Returns the course where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchCourseException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByUUID_G(String uuid, long groupId)
		throws NoSuchCourseException {

		Course course = fetchByUUID_G(uuid, groupId);

		if (course == null) {
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

			throw new NoSuchCourseException(sb.toString());
		}

		return course;
	}

	/**
	 * Returns the course where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByUUID_G(String uuid, long groupId) {
		return fetchByUUID_G(uuid, groupId, true);
	}

	/**
	 * Returns the course where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByUUID_G(
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

		if (result instanceof Course) {
			Course course = (Course)result;

			if (!Objects.equals(uuid, course.getUuid()) ||
				(groupId != course.getGroupId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_COURSE_WHERE);

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

				List<Course> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByUUID_G, finderArgs, list);
					}
				}
				else {
					Course course = list.get(0);

					result = course;

					cacheResult(course);
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
			return (Course)result;
		}
	}

	/**
	 * Removes the course where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the course that was removed
	 */
	@Override
	public Course removeByUUID_G(String uuid, long groupId)
		throws NoSuchCourseException {

		Course course = findByUUID_G(uuid, groupId);

		return remove(course);
	}

	/**
	 * Returns the number of courses where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching courses
	 */
	@Override
	public int countByUUID_G(String uuid, long groupId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUUID_G;

		Object[] finderArgs = new Object[] {uuid, groupId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COURSE_WHERE);

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
		"course.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_G_UUID_3 =
		"(course.uuid IS NULL OR course.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_G_GROUPID_2 =
		"course.groupId = ?";

	private FinderPath _finderPathWithPaginationFindByUuid_C;
	private FinderPath _finderPathWithoutPaginationFindByUuid_C;
	private FinderPath _finderPathCountByUuid_C;

	/**
	 * Returns all the courses where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching courses
	 */
	@Override
	public List<Course> findByUuid_C(String uuid, long companyId) {
		return findByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the courses where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of matching courses
	 */
	@Override
	public List<Course> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the courses where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching courses
	 */
	@Override
	public List<Course> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the courses where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching courses
	 */
	@Override
	public List<Course> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Course> orderByComparator, boolean useFinderCache) {

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

		List<Course> list = null;

		if (useFinderCache) {
			list = (List<Course>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Course course : list) {
					if (!uuid.equals(course.getUuid()) ||
						(companyId != course.getCompanyId())) {

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

			sb.append(_SQL_SELECT_COURSE_WHERE);

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
				sb.append(CourseModelImpl.ORDER_BY_JPQL);
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

				list = (List<Course>)QueryUtil.list(
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
	 * Returns the first course in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = fetchByUuid_C_First(uuid, companyId, orderByComparator);

		if (course != null) {
			return course;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchCourseException(sb.toString());
	}

	/**
	 * Returns the first course in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<Course> orderByComparator) {

		List<Course> list = findByUuid_C(
			uuid, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last course in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = fetchByUuid_C_Last(uuid, companyId, orderByComparator);

		if (course != null) {
			return course;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchCourseException(sb.toString());
	}

	/**
	 * Returns the last course in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<Course> orderByComparator) {

		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<Course> list = findByUuid_C(
			uuid, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the courses before and after the current course in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param courseId the primary key of the current course
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	@Override
	public Course[] findByUuid_C_PrevAndNext(
			long courseId, String uuid, long companyId,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		uuid = Objects.toString(uuid, "");

		Course course = findByPrimaryKey(courseId);

		Session session = null;

		try {
			session = openSession();

			Course[] array = new CourseImpl[3];

			array[0] = getByUuid_C_PrevAndNext(
				session, course, uuid, companyId, orderByComparator, true);

			array[1] = course;

			array[2] = getByUuid_C_PrevAndNext(
				session, course, uuid, companyId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected Course getByUuid_C_PrevAndNext(
		Session session, Course course, String uuid, long companyId,
		OrderByComparator<Course> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_COURSE_WHERE);

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
			sb.append(CourseModelImpl.ORDER_BY_JPQL);
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
					orderByComparator.getOrderByConditionValues(course)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Course> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the courses where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (Course course :
				findByUuid_C(
					uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(course);
		}
	}

	/**
	 * Returns the number of courses where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching courses
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid_C;

		Object[] finderArgs = new Object[] {uuid, companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COURSE_WHERE);

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
		"course.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3 =
		"(course.uuid IS NULL OR course.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 =
		"course.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByCourseCode;
	private FinderPath _finderPathWithoutPaginationFindByCourseCode;
	private FinderPath _finderPathCountByCourseCode;

	/**
	 * Returns all the courses where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @return the matching courses
	 */
	@Override
	public List<Course> findByCourseCode(
		long groupId, String courseCode, boolean deleted) {

		return findByCourseCode(
			groupId, courseCode, deleted, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the courses where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of matching courses
	 */
	@Override
	public List<Course> findByCourseCode(
		long groupId, String courseCode, boolean deleted, int start, int end) {

		return findByCourseCode(groupId, courseCode, deleted, start, end, null);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching courses
	 */
	@Override
	public List<Course> findByCourseCode(
		long groupId, String courseCode, boolean deleted, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return findByCourseCode(
			groupId, courseCode, deleted, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching courses
	 */
	@Override
	public List<Course> findByCourseCode(
		long groupId, String courseCode, boolean deleted, int start, int end,
		OrderByComparator<Course> orderByComparator, boolean useFinderCache) {

		courseCode = Objects.toString(courseCode, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByCourseCode;
				finderArgs = new Object[] {groupId, courseCode, deleted};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCourseCode;
			finderArgs = new Object[] {
				groupId, courseCode, deleted, start, end, orderByComparator
			};
		}

		List<Course> list = null;

		if (useFinderCache) {
			list = (List<Course>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Course course : list) {
					if ((groupId != course.getGroupId()) ||
						!courseCode.equals(course.getCourseCode()) ||
						(deleted != course.isDeleted())) {

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

			sb.append(_SQL_SELECT_COURSE_WHERE);

			sb.append(_FINDER_COLUMN_COURSECODE_GROUPID_2);

			boolean bindCourseCode = false;

			if (courseCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODE_COURSECODE_3);
			}
			else {
				bindCourseCode = true;

				sb.append(_FINDER_COLUMN_COURSECODE_COURSECODE_2);
			}

			sb.append(_FINDER_COLUMN_COURSECODE_DELETED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CourseModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindCourseCode) {
					queryPos.add(courseCode);
				}

				queryPos.add(deleted);

				list = (List<Course>)QueryUtil.list(
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
	 * Returns the first course in the ordered set where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByCourseCode_First(
			long groupId, String courseCode, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = fetchByCourseCode_First(
			groupId, courseCode, deleted, orderByComparator);

		if (course != null) {
			return course;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", courseCode=");
		sb.append(courseCode);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchCourseException(sb.toString());
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByCourseCode_First(
		long groupId, String courseCode, boolean deleted,
		OrderByComparator<Course> orderByComparator) {

		List<Course> list = findByCourseCode(
			groupId, courseCode, deleted, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByCourseCode_Last(
			long groupId, String courseCode, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = fetchByCourseCode_Last(
			groupId, courseCode, deleted, orderByComparator);

		if (course != null) {
			return course;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", courseCode=");
		sb.append(courseCode);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchCourseException(sb.toString());
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByCourseCode_Last(
		long groupId, String courseCode, boolean deleted,
		OrderByComparator<Course> orderByComparator) {

		int count = countByCourseCode(groupId, courseCode, deleted);

		if (count == 0) {
			return null;
		}

		List<Course> list = findByCourseCode(
			groupId, courseCode, deleted, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the courses before and after the current course in the ordered set where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * @param courseId the primary key of the current course
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	@Override
	public Course[] findByCourseCode_PrevAndNext(
			long courseId, long groupId, String courseCode, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		courseCode = Objects.toString(courseCode, "");

		Course course = findByPrimaryKey(courseId);

		Session session = null;

		try {
			session = openSession();

			Course[] array = new CourseImpl[3];

			array[0] = getByCourseCode_PrevAndNext(
				session, course, groupId, courseCode, deleted,
				orderByComparator, true);

			array[1] = course;

			array[2] = getByCourseCode_PrevAndNext(
				session, course, groupId, courseCode, deleted,
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

	protected Course getByCourseCode_PrevAndNext(
		Session session, Course course, long groupId, String courseCode,
		boolean deleted, OrderByComparator<Course> orderByComparator,
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

		sb.append(_SQL_SELECT_COURSE_WHERE);

		sb.append(_FINDER_COLUMN_COURSECODE_GROUPID_2);

		boolean bindCourseCode = false;

		if (courseCode.isEmpty()) {
			sb.append(_FINDER_COLUMN_COURSECODE_COURSECODE_3);
		}
		else {
			bindCourseCode = true;

			sb.append(_FINDER_COLUMN_COURSECODE_COURSECODE_2);
		}

		sb.append(_FINDER_COLUMN_COURSECODE_DELETED_2);

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
			sb.append(CourseModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		if (bindCourseCode) {
			queryPos.add(courseCode);
		}

		queryPos.add(deleted);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(course)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Course> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the courses where groupId = &#63; and courseCode = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 */
	@Override
	public void removeByCourseCode(
		long groupId, String courseCode, boolean deleted) {

		for (Course course :
				findByCourseCode(
					groupId, courseCode, deleted, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(course);
		}
	}

	/**
	 * Returns the number of courses where groupId = &#63; and courseCode = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param deleted the deleted
	 * @return the number of matching courses
	 */
	@Override
	public int countByCourseCode(
		long groupId, String courseCode, boolean deleted) {

		courseCode = Objects.toString(courseCode, "");

		FinderPath finderPath = _finderPathCountByCourseCode;

		Object[] finderArgs = new Object[] {groupId, courseCode, deleted};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_COURSE_WHERE);

			sb.append(_FINDER_COLUMN_COURSECODE_GROUPID_2);

			boolean bindCourseCode = false;

			if (courseCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODE_COURSECODE_3);
			}
			else {
				bindCourseCode = true;

				sb.append(_FINDER_COLUMN_COURSECODE_COURSECODE_2);
			}

			sb.append(_FINDER_COLUMN_COURSECODE_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindCourseCode) {
					queryPos.add(courseCode);
				}

				queryPos.add(deleted);

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

	private static final String _FINDER_COLUMN_COURSECODE_GROUPID_2 =
		"course.groupId = ? AND ";

	private static final String _FINDER_COLUMN_COURSECODE_COURSECODE_2 =
		"course.courseCode = ? AND ";

	private static final String _FINDER_COLUMN_COURSECODE_COURSECODE_3 =
		"(course.courseCode IS NULL OR course.courseCode = '') AND ";

	private static final String _FINDER_COLUMN_COURSECODE_DELETED_2 =
		"course.deleted = ?";

	private FinderPath _finderPathWithPaginationFindByCourseTitle;
	private FinderPath _finderPathWithoutPaginationFindByCourseTitle;
	private FinderPath _finderPathCountByCourseTitle;

	/**
	 * Returns all the courses where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @return the matching courses
	 */
	@Override
	public List<Course> findByCourseTitle(
		long groupId, String courseTitle, boolean deleted) {

		return findByCourseTitle(
			groupId, courseTitle, deleted, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the courses where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of matching courses
	 */
	@Override
	public List<Course> findByCourseTitle(
		long groupId, String courseTitle, boolean deleted, int start, int end) {

		return findByCourseTitle(
			groupId, courseTitle, deleted, start, end, null);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching courses
	 */
	@Override
	public List<Course> findByCourseTitle(
		long groupId, String courseTitle, boolean deleted, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return findByCourseTitle(
			groupId, courseTitle, deleted, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching courses
	 */
	@Override
	public List<Course> findByCourseTitle(
		long groupId, String courseTitle, boolean deleted, int start, int end,
		OrderByComparator<Course> orderByComparator, boolean useFinderCache) {

		courseTitle = Objects.toString(courseTitle, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByCourseTitle;
				finderArgs = new Object[] {groupId, courseTitle, deleted};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCourseTitle;
			finderArgs = new Object[] {
				groupId, courseTitle, deleted, start, end, orderByComparator
			};
		}

		List<Course> list = null;

		if (useFinderCache) {
			list = (List<Course>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Course course : list) {
					if ((groupId != course.getGroupId()) ||
						!courseTitle.equals(course.getCourseTitle()) ||
						(deleted != course.isDeleted())) {

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

			sb.append(_SQL_SELECT_COURSE_WHERE);

			sb.append(_FINDER_COLUMN_COURSETITLE_GROUPID_2);

			boolean bindCourseTitle = false;

			if (courseTitle.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSETITLE_COURSETITLE_3);
			}
			else {
				bindCourseTitle = true;

				sb.append(_FINDER_COLUMN_COURSETITLE_COURSETITLE_2);
			}

			sb.append(_FINDER_COLUMN_COURSETITLE_DELETED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CourseModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindCourseTitle) {
					queryPos.add(courseTitle);
				}

				queryPos.add(deleted);

				list = (List<Course>)QueryUtil.list(
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
	 * Returns the first course in the ordered set where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByCourseTitle_First(
			long groupId, String courseTitle, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = fetchByCourseTitle_First(
			groupId, courseTitle, deleted, orderByComparator);

		if (course != null) {
			return course;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", courseTitle=");
		sb.append(courseTitle);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchCourseException(sb.toString());
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByCourseTitle_First(
		long groupId, String courseTitle, boolean deleted,
		OrderByComparator<Course> orderByComparator) {

		List<Course> list = findByCourseTitle(
			groupId, courseTitle, deleted, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByCourseTitle_Last(
			long groupId, String courseTitle, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = fetchByCourseTitle_Last(
			groupId, courseTitle, deleted, orderByComparator);

		if (course != null) {
			return course;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", courseTitle=");
		sb.append(courseTitle);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchCourseException(sb.toString());
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByCourseTitle_Last(
		long groupId, String courseTitle, boolean deleted,
		OrderByComparator<Course> orderByComparator) {

		int count = countByCourseTitle(groupId, courseTitle, deleted);

		if (count == 0) {
			return null;
		}

		List<Course> list = findByCourseTitle(
			groupId, courseTitle, deleted, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the courses before and after the current course in the ordered set where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * @param courseId the primary key of the current course
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	@Override
	public Course[] findByCourseTitle_PrevAndNext(
			long courseId, long groupId, String courseTitle, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		courseTitle = Objects.toString(courseTitle, "");

		Course course = findByPrimaryKey(courseId);

		Session session = null;

		try {
			session = openSession();

			Course[] array = new CourseImpl[3];

			array[0] = getByCourseTitle_PrevAndNext(
				session, course, groupId, courseTitle, deleted,
				orderByComparator, true);

			array[1] = course;

			array[2] = getByCourseTitle_PrevAndNext(
				session, course, groupId, courseTitle, deleted,
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

	protected Course getByCourseTitle_PrevAndNext(
		Session session, Course course, long groupId, String courseTitle,
		boolean deleted, OrderByComparator<Course> orderByComparator,
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

		sb.append(_SQL_SELECT_COURSE_WHERE);

		sb.append(_FINDER_COLUMN_COURSETITLE_GROUPID_2);

		boolean bindCourseTitle = false;

		if (courseTitle.isEmpty()) {
			sb.append(_FINDER_COLUMN_COURSETITLE_COURSETITLE_3);
		}
		else {
			bindCourseTitle = true;

			sb.append(_FINDER_COLUMN_COURSETITLE_COURSETITLE_2);
		}

		sb.append(_FINDER_COLUMN_COURSETITLE_DELETED_2);

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
			sb.append(CourseModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		if (bindCourseTitle) {
			queryPos.add(courseTitle);
		}

		queryPos.add(deleted);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(course)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Course> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the courses where groupId = &#63; and courseTitle = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 */
	@Override
	public void removeByCourseTitle(
		long groupId, String courseTitle, boolean deleted) {

		for (Course course :
				findByCourseTitle(
					groupId, courseTitle, deleted, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(course);
		}
	}

	/**
	 * Returns the number of courses where groupId = &#63; and courseTitle = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseTitle the course title
	 * @param deleted the deleted
	 * @return the number of matching courses
	 */
	@Override
	public int countByCourseTitle(
		long groupId, String courseTitle, boolean deleted) {

		courseTitle = Objects.toString(courseTitle, "");

		FinderPath finderPath = _finderPathCountByCourseTitle;

		Object[] finderArgs = new Object[] {groupId, courseTitle, deleted};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_COURSE_WHERE);

			sb.append(_FINDER_COLUMN_COURSETITLE_GROUPID_2);

			boolean bindCourseTitle = false;

			if (courseTitle.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSETITLE_COURSETITLE_3);
			}
			else {
				bindCourseTitle = true;

				sb.append(_FINDER_COLUMN_COURSETITLE_COURSETITLE_2);
			}

			sb.append(_FINDER_COLUMN_COURSETITLE_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindCourseTitle) {
					queryPos.add(courseTitle);
				}

				queryPos.add(deleted);

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

	private static final String _FINDER_COLUMN_COURSETITLE_GROUPID_2 =
		"course.groupId = ? AND ";

	private static final String _FINDER_COLUMN_COURSETITLE_COURSETITLE_2 =
		"course.courseTitle = ? AND ";

	private static final String _FINDER_COLUMN_COURSETITLE_COURSETITLE_3 =
		"(course.courseTitle IS NULL OR course.courseTitle = '') AND ";

	private static final String _FINDER_COLUMN_COURSETITLE_DELETED_2 =
		"course.deleted = ?";

	private FinderPath _finderPathFetchByCourseCodeBatchIdActive;
	private FinderPath _finderPathCountByCourseCodeBatchIdActive;

	/**
	 * Returns the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; and deleted = &#63; or throws a <code>NoSuchCourseException</code> if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @param deleted the deleted
	 * @return the matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByCourseCodeBatchIdActive(
			long groupId, String courseCode, String batchId, boolean deleted)
		throws NoSuchCourseException {

		Course course = fetchByCourseCodeBatchIdActive(
			groupId, courseCode, batchId, deleted);

		if (course == null) {
			StringBundler sb = new StringBundler(10);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("groupId=");
			sb.append(groupId);

			sb.append(", courseCode=");
			sb.append(courseCode);

			sb.append(", batchId=");
			sb.append(batchId);

			sb.append(", deleted=");
			sb.append(deleted);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchCourseException(sb.toString());
		}

		return course;
	}

	/**
	 * Returns the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @param deleted the deleted
	 * @return the matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByCourseCodeBatchIdActive(
		long groupId, String courseCode, String batchId, boolean deleted) {

		return fetchByCourseCodeBatchIdActive(
			groupId, courseCode, batchId, deleted, true);
	}

	/**
	 * Returns the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; and deleted = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @param deleted the deleted
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByCourseCodeBatchIdActive(
		long groupId, String courseCode, String batchId, boolean deleted,
		boolean useFinderCache) {

		courseCode = Objects.toString(courseCode, "");
		batchId = Objects.toString(batchId, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {groupId, courseCode, batchId, deleted};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByCourseCodeBatchIdActive, finderArgs, this);
		}

		if (result instanceof Course) {
			Course course = (Course)result;

			if ((groupId != course.getGroupId()) ||
				!Objects.equals(courseCode, course.getCourseCode()) ||
				!Objects.equals(batchId, course.getBatchId()) ||
				(deleted != course.isDeleted())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_SQL_SELECT_COURSE_WHERE);

			sb.append(_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_GROUPID_2);

			boolean bindCourseCode = false;

			if (courseCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_COURSECODE_3);
			}
			else {
				bindCourseCode = true;

				sb.append(_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_COURSECODE_2);
			}

			boolean bindBatchId = false;

			if (batchId.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_BATCHID_3);
			}
			else {
				bindBatchId = true;

				sb.append(_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_BATCHID_2);
			}

			sb.append(_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindCourseCode) {
					queryPos.add(courseCode);
				}

				if (bindBatchId) {
					queryPos.add(batchId);
				}

				queryPos.add(deleted);

				List<Course> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByCourseCodeBatchIdActive,
							finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {
									groupId, courseCode, batchId, deleted
								};
							}

							_log.warn(
								"CoursePersistenceImpl.fetchByCourseCodeBatchIdActive(long, String, String, boolean, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					Course course = list.get(0);

					result = course;

					cacheResult(course);
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
			return (Course)result;
		}
	}

	/**
	 * Removes the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @param deleted the deleted
	 * @return the course that was removed
	 */
	@Override
	public Course removeByCourseCodeBatchIdActive(
			long groupId, String courseCode, String batchId, boolean deleted)
		throws NoSuchCourseException {

		Course course = findByCourseCodeBatchIdActive(
			groupId, courseCode, batchId, deleted);

		return remove(course);
	}

	/**
	 * Returns the number of courses where groupId = &#63; and courseCode = &#63; and batchId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @param deleted the deleted
	 * @return the number of matching courses
	 */
	@Override
	public int countByCourseCodeBatchIdActive(
		long groupId, String courseCode, String batchId, boolean deleted) {

		courseCode = Objects.toString(courseCode, "");
		batchId = Objects.toString(batchId, "");

		FinderPath finderPath = _finderPathCountByCourseCodeBatchIdActive;

		Object[] finderArgs = new Object[] {
			groupId, courseCode, batchId, deleted
		};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(5);

			sb.append(_SQL_COUNT_COURSE_WHERE);

			sb.append(_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_GROUPID_2);

			boolean bindCourseCode = false;

			if (courseCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_COURSECODE_3);
			}
			else {
				bindCourseCode = true;

				sb.append(_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_COURSECODE_2);
			}

			boolean bindBatchId = false;

			if (batchId.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_BATCHID_3);
			}
			else {
				bindBatchId = true;

				sb.append(_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_BATCHID_2);
			}

			sb.append(_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindCourseCode) {
					queryPos.add(courseCode);
				}

				if (bindBatchId) {
					queryPos.add(batchId);
				}

				queryPos.add(deleted);

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

	private static final String
		_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_GROUPID_2 =
			"course.groupId = ? AND ";

	private static final String
		_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_COURSECODE_2 =
			"course.courseCode = ? AND ";

	private static final String
		_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_COURSECODE_3 =
			"(course.courseCode IS NULL OR course.courseCode = '') AND ";

	private static final String
		_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_BATCHID_2 =
			"course.batchId = ? AND ";

	private static final String
		_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_BATCHID_3 =
			"(course.batchId IS NULL OR course.batchId = '') AND ";

	private static final String
		_FINDER_COLUMN_COURSECODEBATCHIDACTIVE_DELETED_2 = "course.deleted = ?";

	private FinderPath _finderPathFetchByCourseCodeBatchId;
	private FinderPath _finderPathCountByCourseCodeBatchId;

	/**
	 * Returns the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; or throws a <code>NoSuchCourseException</code> if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @return the matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByCourseCodeBatchId(
			long groupId, String courseCode, String batchId)
		throws NoSuchCourseException {

		Course course = fetchByCourseCodeBatchId(groupId, courseCode, batchId);

		if (course == null) {
			StringBundler sb = new StringBundler(8);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("groupId=");
			sb.append(groupId);

			sb.append(", courseCode=");
			sb.append(courseCode);

			sb.append(", batchId=");
			sb.append(batchId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchCourseException(sb.toString());
		}

		return course;
	}

	/**
	 * Returns the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @return the matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByCourseCodeBatchId(
		long groupId, String courseCode, String batchId) {

		return fetchByCourseCodeBatchId(groupId, courseCode, batchId, true);
	}

	/**
	 * Returns the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByCourseCodeBatchId(
		long groupId, String courseCode, String batchId,
		boolean useFinderCache) {

		courseCode = Objects.toString(courseCode, "");
		batchId = Objects.toString(batchId, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {groupId, courseCode, batchId};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByCourseCodeBatchId, finderArgs, this);
		}

		if (result instanceof Course) {
			Course course = (Course)result;

			if ((groupId != course.getGroupId()) ||
				!Objects.equals(courseCode, course.getCourseCode()) ||
				!Objects.equals(batchId, course.getBatchId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(5);

			sb.append(_SQL_SELECT_COURSE_WHERE);

			sb.append(_FINDER_COLUMN_COURSECODEBATCHID_GROUPID_2);

			boolean bindCourseCode = false;

			if (courseCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODEBATCHID_COURSECODE_3);
			}
			else {
				bindCourseCode = true;

				sb.append(_FINDER_COLUMN_COURSECODEBATCHID_COURSECODE_2);
			}

			boolean bindBatchId = false;

			if (batchId.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODEBATCHID_BATCHID_3);
			}
			else {
				bindBatchId = true;

				sb.append(_FINDER_COLUMN_COURSECODEBATCHID_BATCHID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindCourseCode) {
					queryPos.add(courseCode);
				}

				if (bindBatchId) {
					queryPos.add(batchId);
				}

				List<Course> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByCourseCodeBatchId, finderArgs,
							list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {
									groupId, courseCode, batchId
								};
							}

							_log.warn(
								"CoursePersistenceImpl.fetchByCourseCodeBatchId(long, String, String, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					Course course = list.get(0);

					result = course;

					cacheResult(course);
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
			return (Course)result;
		}
	}

	/**
	 * Removes the course where groupId = &#63; and courseCode = &#63; and batchId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @return the course that was removed
	 */
	@Override
	public Course removeByCourseCodeBatchId(
			long groupId, String courseCode, String batchId)
		throws NoSuchCourseException {

		Course course = findByCourseCodeBatchId(groupId, courseCode, batchId);

		return remove(course);
	}

	/**
	 * Returns the number of courses where groupId = &#63; and courseCode = &#63; and batchId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param courseCode the course code
	 * @param batchId the batch ID
	 * @return the number of matching courses
	 */
	@Override
	public int countByCourseCodeBatchId(
		long groupId, String courseCode, String batchId) {

		courseCode = Objects.toString(courseCode, "");
		batchId = Objects.toString(batchId, "");

		FinderPath finderPath = _finderPathCountByCourseCodeBatchId;

		Object[] finderArgs = new Object[] {groupId, courseCode, batchId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_COURSE_WHERE);

			sb.append(_FINDER_COLUMN_COURSECODEBATCHID_GROUPID_2);

			boolean bindCourseCode = false;

			if (courseCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODEBATCHID_COURSECODE_3);
			}
			else {
				bindCourseCode = true;

				sb.append(_FINDER_COLUMN_COURSECODEBATCHID_COURSECODE_2);
			}

			boolean bindBatchId = false;

			if (batchId.isEmpty()) {
				sb.append(_FINDER_COLUMN_COURSECODEBATCHID_BATCHID_3);
			}
			else {
				bindBatchId = true;

				sb.append(_FINDER_COLUMN_COURSECODEBATCHID_BATCHID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				if (bindCourseCode) {
					queryPos.add(courseCode);
				}

				if (bindBatchId) {
					queryPos.add(batchId);
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

	private static final String _FINDER_COLUMN_COURSECODEBATCHID_GROUPID_2 =
		"course.groupId = ? AND ";

	private static final String _FINDER_COLUMN_COURSECODEBATCHID_COURSECODE_2 =
		"course.courseCode = ? AND ";

	private static final String _FINDER_COLUMN_COURSECODEBATCHID_COURSECODE_3 =
		"(course.courseCode IS NULL OR course.courseCode = '') AND ";

	private static final String _FINDER_COLUMN_COURSECODEBATCHID_BATCHID_2 =
		"course.batchId = ?";

	private static final String _FINDER_COLUMN_COURSECODEBATCHID_BATCHID_3 =
		"(course.batchId IS NULL OR course.batchId = '')";

	private FinderPath _finderPathWithPaginationFindByActiveCourse;
	private FinderPath _finderPathWithoutPaginationFindByActiveCourse;
	private FinderPath _finderPathCountByActiveCourse;

	/**
	 * Returns all the courses where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the matching courses
	 */
	@Override
	public List<Course> findByActiveCourse(long groupId, boolean deleted) {
		return findByActiveCourse(
			groupId, deleted, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the courses where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of matching courses
	 */
	@Override
	public List<Course> findByActiveCourse(
		long groupId, boolean deleted, int start, int end) {

		return findByActiveCourse(groupId, deleted, start, end, null);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching courses
	 */
	@Override
	public List<Course> findByActiveCourse(
		long groupId, boolean deleted, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return findByActiveCourse(
			groupId, deleted, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and deleted = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching courses
	 */
	@Override
	public List<Course> findByActiveCourse(
		long groupId, boolean deleted, int start, int end,
		OrderByComparator<Course> orderByComparator, boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByActiveCourse;
				finderArgs = new Object[] {groupId, deleted};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByActiveCourse;
			finderArgs = new Object[] {
				groupId, deleted, start, end, orderByComparator
			};
		}

		List<Course> list = null;

		if (useFinderCache) {
			list = (List<Course>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Course course : list) {
					if ((groupId != course.getGroupId()) ||
						(deleted != course.isDeleted())) {

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

			sb.append(_SQL_SELECT_COURSE_WHERE);

			sb.append(_FINDER_COLUMN_ACTIVECOURSE_GROUPID_2);

			sb.append(_FINDER_COLUMN_ACTIVECOURSE_DELETED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CourseModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(deleted);

				list = (List<Course>)QueryUtil.list(
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
	 * Returns the first course in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByActiveCourse_First(
			long groupId, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = fetchByActiveCourse_First(
			groupId, deleted, orderByComparator);

		if (course != null) {
			return course;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchCourseException(sb.toString());
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByActiveCourse_First(
		long groupId, boolean deleted,
		OrderByComparator<Course> orderByComparator) {

		List<Course> list = findByActiveCourse(
			groupId, deleted, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByActiveCourse_Last(
			long groupId, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = fetchByActiveCourse_Last(
			groupId, deleted, orderByComparator);

		if (course != null) {
			return course;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append("}");

		throw new NoSuchCourseException(sb.toString());
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByActiveCourse_Last(
		long groupId, boolean deleted,
		OrderByComparator<Course> orderByComparator) {

		int count = countByActiveCourse(groupId, deleted);

		if (count == 0) {
			return null;
		}

		List<Course> list = findByActiveCourse(
			groupId, deleted, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the courses before and after the current course in the ordered set where groupId = &#63; and deleted = &#63;.
	 *
	 * @param courseId the primary key of the current course
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	@Override
	public Course[] findByActiveCourse_PrevAndNext(
			long courseId, long groupId, boolean deleted,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = findByPrimaryKey(courseId);

		Session session = null;

		try {
			session = openSession();

			Course[] array = new CourseImpl[3];

			array[0] = getByActiveCourse_PrevAndNext(
				session, course, groupId, deleted, orderByComparator, true);

			array[1] = course;

			array[2] = getByActiveCourse_PrevAndNext(
				session, course, groupId, deleted, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected Course getByActiveCourse_PrevAndNext(
		Session session, Course course, long groupId, boolean deleted,
		OrderByComparator<Course> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_COURSE_WHERE);

		sb.append(_FINDER_COLUMN_ACTIVECOURSE_GROUPID_2);

		sb.append(_FINDER_COLUMN_ACTIVECOURSE_DELETED_2);

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
			sb.append(CourseModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		queryPos.add(deleted);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(course)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Course> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the courses where groupId = &#63; and deleted = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 */
	@Override
	public void removeByActiveCourse(long groupId, boolean deleted) {
		for (Course course :
				findByActiveCourse(
					groupId, deleted, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(course);
		}
	}

	/**
	 * Returns the number of courses where groupId = &#63; and deleted = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @return the number of matching courses
	 */
	@Override
	public int countByActiveCourse(long groupId, boolean deleted) {
		FinderPath finderPath = _finderPathCountByActiveCourse;

		Object[] finderArgs = new Object[] {groupId, deleted};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COURSE_WHERE);

			sb.append(_FINDER_COLUMN_ACTIVECOURSE_GROUPID_2);

			sb.append(_FINDER_COLUMN_ACTIVECOURSE_DELETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(deleted);

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

	private static final String _FINDER_COLUMN_ACTIVECOURSE_GROUPID_2 =
		"course.groupId = ? AND ";

	private static final String _FINDER_COLUMN_ACTIVECOURSE_DELETED_2 =
		"course.deleted = ?";

	private FinderPath _finderPathWithPaginationFindByActiveCourseUpdated;
	private FinderPath _finderPathWithoutPaginationFindByActiveCourseUpdated;
	private FinderPath _finderPathCountByActiveCourseUpdated;

	/**
	 * Returns all the courses where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @return the matching courses
	 */
	@Override
	public List<Course> findByActiveCourseUpdated(
		long groupId, boolean deleted, boolean updated) {

		return findByActiveCourseUpdated(
			groupId, deleted, updated, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the courses where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of matching courses
	 */
	@Override
	public List<Course> findByActiveCourseUpdated(
		long groupId, boolean deleted, boolean updated, int start, int end) {

		return findByActiveCourseUpdated(
			groupId, deleted, updated, start, end, null);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching courses
	 */
	@Override
	public List<Course> findByActiveCourseUpdated(
		long groupId, boolean deleted, boolean updated, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return findByActiveCourseUpdated(
			groupId, deleted, updated, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching courses
	 */
	@Override
	public List<Course> findByActiveCourseUpdated(
		long groupId, boolean deleted, boolean updated, int start, int end,
		OrderByComparator<Course> orderByComparator, boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByActiveCourseUpdated;
				finderArgs = new Object[] {groupId, deleted, updated};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByActiveCourseUpdated;
			finderArgs = new Object[] {
				groupId, deleted, updated, start, end, orderByComparator
			};
		}

		List<Course> list = null;

		if (useFinderCache) {
			list = (List<Course>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Course course : list) {
					if ((groupId != course.getGroupId()) ||
						(deleted != course.isDeleted()) ||
						(updated != course.isUpdated())) {

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

			sb.append(_SQL_SELECT_COURSE_WHERE);

			sb.append(_FINDER_COLUMN_ACTIVECOURSEUPDATED_GROUPID_2);

			sb.append(_FINDER_COLUMN_ACTIVECOURSEUPDATED_DELETED_2);

			sb.append(_FINDER_COLUMN_ACTIVECOURSEUPDATED_UPDATED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CourseModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(deleted);

				queryPos.add(updated);

				list = (List<Course>)QueryUtil.list(
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
	 * Returns the first course in the ordered set where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByActiveCourseUpdated_First(
			long groupId, boolean deleted, boolean updated,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = fetchByActiveCourseUpdated_First(
			groupId, deleted, updated, orderByComparator);

		if (course != null) {
			return course;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append(", updated=");
		sb.append(updated);

		sb.append("}");

		throw new NoSuchCourseException(sb.toString());
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByActiveCourseUpdated_First(
		long groupId, boolean deleted, boolean updated,
		OrderByComparator<Course> orderByComparator) {

		List<Course> list = findByActiveCourseUpdated(
			groupId, deleted, updated, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByActiveCourseUpdated_Last(
			long groupId, boolean deleted, boolean updated,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = fetchByActiveCourseUpdated_Last(
			groupId, deleted, updated, orderByComparator);

		if (course != null) {
			return course;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append(", updated=");
		sb.append(updated);

		sb.append("}");

		throw new NoSuchCourseException(sb.toString());
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByActiveCourseUpdated_Last(
		long groupId, boolean deleted, boolean updated,
		OrderByComparator<Course> orderByComparator) {

		int count = countByActiveCourseUpdated(groupId, deleted, updated);

		if (count == 0) {
			return null;
		}

		List<Course> list = findByActiveCourseUpdated(
			groupId, deleted, updated, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the courses before and after the current course in the ordered set where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * @param courseId the primary key of the current course
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	@Override
	public Course[] findByActiveCourseUpdated_PrevAndNext(
			long courseId, long groupId, boolean deleted, boolean updated,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = findByPrimaryKey(courseId);

		Session session = null;

		try {
			session = openSession();

			Course[] array = new CourseImpl[3];

			array[0] = getByActiveCourseUpdated_PrevAndNext(
				session, course, groupId, deleted, updated, orderByComparator,
				true);

			array[1] = course;

			array[2] = getByActiveCourseUpdated_PrevAndNext(
				session, course, groupId, deleted, updated, orderByComparator,
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

	protected Course getByActiveCourseUpdated_PrevAndNext(
		Session session, Course course, long groupId, boolean deleted,
		boolean updated, OrderByComparator<Course> orderByComparator,
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

		sb.append(_SQL_SELECT_COURSE_WHERE);

		sb.append(_FINDER_COLUMN_ACTIVECOURSEUPDATED_GROUPID_2);

		sb.append(_FINDER_COLUMN_ACTIVECOURSEUPDATED_DELETED_2);

		sb.append(_FINDER_COLUMN_ACTIVECOURSEUPDATED_UPDATED_2);

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
			sb.append(CourseModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		queryPos.add(deleted);

		queryPos.add(updated);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(course)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Course> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the courses where groupId = &#63; and deleted = &#63; and updated = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 */
	@Override
	public void removeByActiveCourseUpdated(
		long groupId, boolean deleted, boolean updated) {

		for (Course course :
				findByActiveCourseUpdated(
					groupId, deleted, updated, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(course);
		}
	}

	/**
	 * Returns the number of courses where groupId = &#63; and deleted = &#63; and updated = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param updated the updated
	 * @return the number of matching courses
	 */
	@Override
	public int countByActiveCourseUpdated(
		long groupId, boolean deleted, boolean updated) {

		FinderPath finderPath = _finderPathCountByActiveCourseUpdated;

		Object[] finderArgs = new Object[] {groupId, deleted, updated};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_COURSE_WHERE);

			sb.append(_FINDER_COLUMN_ACTIVECOURSEUPDATED_GROUPID_2);

			sb.append(_FINDER_COLUMN_ACTIVECOURSEUPDATED_DELETED_2);

			sb.append(_FINDER_COLUMN_ACTIVECOURSEUPDATED_UPDATED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(deleted);

				queryPos.add(updated);

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

	private static final String _FINDER_COLUMN_ACTIVECOURSEUPDATED_GROUPID_2 =
		"course.groupId = ? AND ";

	private static final String _FINDER_COLUMN_ACTIVECOURSEUPDATED_DELETED_2 =
		"course.deleted = ? AND ";

	private static final String _FINDER_COLUMN_ACTIVECOURSEUPDATED_UPDATED_2 =
		"course.updated = ?";

	private FinderPath _finderPathWithPaginationFindByActivePopularCourse;
	private FinderPath _finderPathWithoutPaginationFindByActivePopularCourse;
	private FinderPath _finderPathCountByActivePopularCourse;

	/**
	 * Returns all the courses where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @return the matching courses
	 */
	@Override
	public List<Course> findByActivePopularCourse(
		long groupId, boolean deleted, boolean popular) {

		return findByActivePopularCourse(
			groupId, deleted, popular, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the courses where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of matching courses
	 */
	@Override
	public List<Course> findByActivePopularCourse(
		long groupId, boolean deleted, boolean popular, int start, int end) {

		return findByActivePopularCourse(
			groupId, deleted, popular, start, end, null);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching courses
	 */
	@Override
	public List<Course> findByActivePopularCourse(
		long groupId, boolean deleted, boolean popular, int start, int end,
		OrderByComparator<Course> orderByComparator) {

		return findByActivePopularCourse(
			groupId, deleted, popular, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the courses where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching courses
	 */
	@Override
	public List<Course> findByActivePopularCourse(
		long groupId, boolean deleted, boolean popular, int start, int end,
		OrderByComparator<Course> orderByComparator, boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByActivePopularCourse;
				finderArgs = new Object[] {groupId, deleted, popular};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByActivePopularCourse;
			finderArgs = new Object[] {
				groupId, deleted, popular, start, end, orderByComparator
			};
		}

		List<Course> list = null;

		if (useFinderCache) {
			list = (List<Course>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Course course : list) {
					if ((groupId != course.getGroupId()) ||
						(deleted != course.isDeleted()) ||
						(popular != course.isPopular())) {

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

			sb.append(_SQL_SELECT_COURSE_WHERE);

			sb.append(_FINDER_COLUMN_ACTIVEPOPULARCOURSE_GROUPID_2);

			sb.append(_FINDER_COLUMN_ACTIVEPOPULARCOURSE_DELETED_2);

			sb.append(_FINDER_COLUMN_ACTIVEPOPULARCOURSE_POPULAR_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CourseModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(deleted);

				queryPos.add(popular);

				list = (List<Course>)QueryUtil.list(
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
	 * Returns the first course in the ordered set where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByActivePopularCourse_First(
			long groupId, boolean deleted, boolean popular,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = fetchByActivePopularCourse_First(
			groupId, deleted, popular, orderByComparator);

		if (course != null) {
			return course;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append(", popular=");
		sb.append(popular);

		sb.append("}");

		throw new NoSuchCourseException(sb.toString());
	}

	/**
	 * Returns the first course in the ordered set where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByActivePopularCourse_First(
		long groupId, boolean deleted, boolean popular,
		OrderByComparator<Course> orderByComparator) {

		List<Course> list = findByActivePopularCourse(
			groupId, deleted, popular, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course
	 * @throws NoSuchCourseException if a matching course could not be found
	 */
	@Override
	public Course findByActivePopularCourse_Last(
			long groupId, boolean deleted, boolean popular,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = fetchByActivePopularCourse_Last(
			groupId, deleted, popular, orderByComparator);

		if (course != null) {
			return course;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", deleted=");
		sb.append(deleted);

		sb.append(", popular=");
		sb.append(popular);

		sb.append("}");

		throw new NoSuchCourseException(sb.toString());
	}

	/**
	 * Returns the last course in the ordered set where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public Course fetchByActivePopularCourse_Last(
		long groupId, boolean deleted, boolean popular,
		OrderByComparator<Course> orderByComparator) {

		int count = countByActivePopularCourse(groupId, deleted, popular);

		if (count == 0) {
			return null;
		}

		List<Course> list = findByActivePopularCourse(
			groupId, deleted, popular, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the courses before and after the current course in the ordered set where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * @param courseId the primary key of the current course
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	@Override
	public Course[] findByActivePopularCourse_PrevAndNext(
			long courseId, long groupId, boolean deleted, boolean popular,
			OrderByComparator<Course> orderByComparator)
		throws NoSuchCourseException {

		Course course = findByPrimaryKey(courseId);

		Session session = null;

		try {
			session = openSession();

			Course[] array = new CourseImpl[3];

			array[0] = getByActivePopularCourse_PrevAndNext(
				session, course, groupId, deleted, popular, orderByComparator,
				true);

			array[1] = course;

			array[2] = getByActivePopularCourse_PrevAndNext(
				session, course, groupId, deleted, popular, orderByComparator,
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

	protected Course getByActivePopularCourse_PrevAndNext(
		Session session, Course course, long groupId, boolean deleted,
		boolean popular, OrderByComparator<Course> orderByComparator,
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

		sb.append(_SQL_SELECT_COURSE_WHERE);

		sb.append(_FINDER_COLUMN_ACTIVEPOPULARCOURSE_GROUPID_2);

		sb.append(_FINDER_COLUMN_ACTIVEPOPULARCOURSE_DELETED_2);

		sb.append(_FINDER_COLUMN_ACTIVEPOPULARCOURSE_POPULAR_2);

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
			sb.append(CourseModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		queryPos.add(deleted);

		queryPos.add(popular);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(course)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Course> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the courses where groupId = &#63; and deleted = &#63; and popular = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 */
	@Override
	public void removeByActivePopularCourse(
		long groupId, boolean deleted, boolean popular) {

		for (Course course :
				findByActivePopularCourse(
					groupId, deleted, popular, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(course);
		}
	}

	/**
	 * Returns the number of courses where groupId = &#63; and deleted = &#63; and popular = &#63;.
	 *
	 * @param groupId the group ID
	 * @param deleted the deleted
	 * @param popular the popular
	 * @return the number of matching courses
	 */
	@Override
	public int countByActivePopularCourse(
		long groupId, boolean deleted, boolean popular) {

		FinderPath finderPath = _finderPathCountByActivePopularCourse;

		Object[] finderArgs = new Object[] {groupId, deleted, popular};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_COURSE_WHERE);

			sb.append(_FINDER_COLUMN_ACTIVEPOPULARCOURSE_GROUPID_2);

			sb.append(_FINDER_COLUMN_ACTIVEPOPULARCOURSE_DELETED_2);

			sb.append(_FINDER_COLUMN_ACTIVEPOPULARCOURSE_POPULAR_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(deleted);

				queryPos.add(popular);

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

	private static final String _FINDER_COLUMN_ACTIVEPOPULARCOURSE_GROUPID_2 =
		"course.groupId = ? AND ";

	private static final String _FINDER_COLUMN_ACTIVEPOPULARCOURSE_DELETED_2 =
		"course.deleted = ? AND ";

	private static final String _FINDER_COLUMN_ACTIVEPOPULARCOURSE_POPULAR_2 =
		"course.popular = ?";

	public CoursePersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");

		setDBColumnNames(dbColumnNames);

		setModelClass(Course.class);

		setModelImplClass(CourseImpl.class);
		setModelPKClass(long.class);
	}

	/**
	 * Caches the course in the entity cache if it is enabled.
	 *
	 * @param course the course
	 */
	@Override
	public void cacheResult(Course course) {
		entityCache.putResult(CourseImpl.class, course.getPrimaryKey(), course);

		finderCache.putResult(
			_finderPathFetchByUUID_G,
			new Object[] {course.getUuid(), course.getGroupId()}, course);

		finderCache.putResult(
			_finderPathFetchByCourseCodeBatchIdActive,
			new Object[] {
				course.getGroupId(), course.getCourseCode(),
				course.getBatchId(), course.isDeleted()
			},
			course);

		finderCache.putResult(
			_finderPathFetchByCourseCodeBatchId,
			new Object[] {
				course.getGroupId(), course.getCourseCode(), course.getBatchId()
			},
			course);
	}

	/**
	 * Caches the courses in the entity cache if it is enabled.
	 *
	 * @param courses the courses
	 */
	@Override
	public void cacheResult(List<Course> courses) {
		for (Course course : courses) {
			if (entityCache.getResult(
					CourseImpl.class, course.getPrimaryKey()) == null) {

				cacheResult(course);
			}
		}
	}

	/**
	 * Clears the cache for all courses.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(CourseImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the course.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Course course) {
		entityCache.removeResult(CourseImpl.class, course);
	}

	@Override
	public void clearCache(List<Course> courses) {
		for (Course course : courses) {
			entityCache.removeResult(CourseImpl.class, course);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(CourseImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(CourseModelImpl courseModelImpl) {
		Object[] args = new Object[] {
			courseModelImpl.getUuid(), courseModelImpl.getGroupId()
		};

		finderCache.putResult(
			_finderPathCountByUUID_G, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByUUID_G, args, courseModelImpl, false);

		args = new Object[] {
			courseModelImpl.getGroupId(), courseModelImpl.getCourseCode(),
			courseModelImpl.getBatchId(), courseModelImpl.isDeleted()
		};

		finderCache.putResult(
			_finderPathCountByCourseCodeBatchIdActive, args, Long.valueOf(1),
			false);
		finderCache.putResult(
			_finderPathFetchByCourseCodeBatchIdActive, args, courseModelImpl,
			false);

		args = new Object[] {
			courseModelImpl.getGroupId(), courseModelImpl.getCourseCode(),
			courseModelImpl.getBatchId()
		};

		finderCache.putResult(
			_finderPathCountByCourseCodeBatchId, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByCourseCodeBatchId, args, courseModelImpl, false);
	}

	/**
	 * Creates a new course with the primary key. Does not add the course to the database.
	 *
	 * @param courseId the primary key for the new course
	 * @return the new course
	 */
	@Override
	public Course create(long courseId) {
		Course course = new CourseImpl();

		course.setNew(true);
		course.setPrimaryKey(courseId);

		String uuid = PortalUUIDUtil.generate();

		course.setUuid(uuid);

		course.setCompanyId(CompanyThreadLocal.getCompanyId());

		return course;
	}

	/**
	 * Removes the course with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param courseId the primary key of the course
	 * @return the course that was removed
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	@Override
	public Course remove(long courseId) throws NoSuchCourseException {
		return remove((Serializable)courseId);
	}

	/**
	 * Removes the course with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the course
	 * @return the course that was removed
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	@Override
	public Course remove(Serializable primaryKey) throws NoSuchCourseException {
		Session session = null;

		try {
			session = openSession();

			Course course = (Course)session.get(CourseImpl.class, primaryKey);

			if (course == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchCourseException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(course);
		}
		catch (NoSuchCourseException noSuchEntityException) {
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
	protected Course removeImpl(Course course) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(course)) {
				course = (Course)session.get(
					CourseImpl.class, course.getPrimaryKeyObj());
			}

			if (course != null) {
				session.delete(course);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (course != null) {
			clearCache(course);
		}

		return course;
	}

	@Override
	public Course updateImpl(Course course) {
		boolean isNew = course.isNew();

		if (!(course instanceof CourseModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(course.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(course);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in course proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom Course implementation " +
					course.getClass());
		}

		CourseModelImpl courseModelImpl = (CourseModelImpl)course;

		if (Validator.isNull(course.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			course.setUuid(uuid);
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (course.getCreateDate() == null)) {
			if (serviceContext == null) {
				course.setCreateDate(now);
			}
			else {
				course.setCreateDate(serviceContext.getCreateDate(now));
			}
		}

		if (!courseModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				course.setModifiedDate(now);
			}
			else {
				course.setModifiedDate(serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(course);
			}
			else {
				course = (Course)session.merge(course);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(CourseImpl.class, courseModelImpl, false, true);

		cacheUniqueFindersCache(courseModelImpl);

		if (isNew) {
			course.setNew(false);
		}

		course.resetOriginalValues();

		return course;
	}

	/**
	 * Returns the course with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the course
	 * @return the course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	@Override
	public Course findByPrimaryKey(Serializable primaryKey)
		throws NoSuchCourseException {

		Course course = fetchByPrimaryKey(primaryKey);

		if (course == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchCourseException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return course;
	}

	/**
	 * Returns the course with the primary key or throws a <code>NoSuchCourseException</code> if it could not be found.
	 *
	 * @param courseId the primary key of the course
	 * @return the course
	 * @throws NoSuchCourseException if a course with the primary key could not be found
	 */
	@Override
	public Course findByPrimaryKey(long courseId) throws NoSuchCourseException {
		return findByPrimaryKey((Serializable)courseId);
	}

	/**
	 * Returns the course with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param courseId the primary key of the course
	 * @return the course, or <code>null</code> if a course with the primary key could not be found
	 */
	@Override
	public Course fetchByPrimaryKey(long courseId) {
		return fetchByPrimaryKey((Serializable)courseId);
	}

	/**
	 * Returns all the courses.
	 *
	 * @return the courses
	 */
	@Override
	public List<Course> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the courses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of courses
	 */
	@Override
	public List<Course> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the courses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of courses
	 */
	@Override
	public List<Course> findAll(
		int start, int end, OrderByComparator<Course> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the courses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CourseModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of courses
	 */
	@Override
	public List<Course> findAll(
		int start, int end, OrderByComparator<Course> orderByComparator,
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

		List<Course> list = null;

		if (useFinderCache) {
			list = (List<Course>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_COURSE);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_COURSE;

				sql = sql.concat(CourseModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<Course>)QueryUtil.list(
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
	 * Removes all the courses from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (Course course : findAll()) {
			remove(course);
		}
	}

	/**
	 * Returns the number of courses.
	 *
	 * @return the number of courses
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_COURSE);

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
		return "courseId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_COURSE;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return CourseModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the course persistence.
	 */
	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_argumentsResolverServiceRegistration = _bundleContext.registerService(
			ArgumentsResolver.class, new CourseModelArgumentsResolver(),
			MapUtil.singletonDictionary(
				"model.class.name", Course.class.getName()));

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

		_finderPathWithPaginationFindByCourseCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCourseCode",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Boolean.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"groupId", "courseCode", "deleted"}, true);

		_finderPathWithoutPaginationFindByCourseCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCourseCode",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Boolean.class.getName()
			},
			new String[] {"groupId", "courseCode", "deleted"}, true);

		_finderPathCountByCourseCode = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCourseCode",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Boolean.class.getName()
			},
			new String[] {"groupId", "courseCode", "deleted"}, false);

		_finderPathWithPaginationFindByCourseTitle = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCourseTitle",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Boolean.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"groupId", "courseTitle", "deleted"}, true);

		_finderPathWithoutPaginationFindByCourseTitle = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCourseTitle",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Boolean.class.getName()
			},
			new String[] {"groupId", "courseTitle", "deleted"}, true);

		_finderPathCountByCourseTitle = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCourseTitle",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Boolean.class.getName()
			},
			new String[] {"groupId", "courseTitle", "deleted"}, false);

		_finderPathFetchByCourseCodeBatchIdActive = _createFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByCourseCodeBatchIdActive",
			new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName(), Boolean.class.getName()
			},
			new String[] {"groupId", "courseCode", "batchId", "deleted"}, true);

		_finderPathCountByCourseCodeBatchIdActive = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByCourseCodeBatchIdActive",
			new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName(), Boolean.class.getName()
			},
			new String[] {"groupId", "courseCode", "batchId", "deleted"},
			false);

		_finderPathFetchByCourseCodeBatchId = _createFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByCourseCodeBatchId",
			new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName()
			},
			new String[] {"groupId", "courseCode", "batchId"}, true);

		_finderPathCountByCourseCodeBatchId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByCourseCodeBatchId",
			new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName()
			},
			new String[] {"groupId", "courseCode", "batchId"}, false);

		_finderPathWithPaginationFindByActiveCourse = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByActiveCourse",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"groupId", "deleted"}, true);

		_finderPathWithoutPaginationFindByActiveCourse = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByActiveCourse",
			new String[] {Long.class.getName(), Boolean.class.getName()},
			new String[] {"groupId", "deleted"}, true);

		_finderPathCountByActiveCourse = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByActiveCourse",
			new String[] {Long.class.getName(), Boolean.class.getName()},
			new String[] {"groupId", "deleted"}, false);

		_finderPathWithPaginationFindByActiveCourseUpdated = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByActiveCourseUpdated",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Boolean.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"groupId", "deleted", "updated"}, true);

		_finderPathWithoutPaginationFindByActiveCourseUpdated =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
				"findByActiveCourseUpdated",
				new String[] {
					Long.class.getName(), Boolean.class.getName(),
					Boolean.class.getName()
				},
				new String[] {"groupId", "deleted", "updated"}, true);

		_finderPathCountByActiveCourseUpdated = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByActiveCourseUpdated",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Boolean.class.getName()
			},
			new String[] {"groupId", "deleted", "updated"}, false);

		_finderPathWithPaginationFindByActivePopularCourse = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByActivePopularCourse",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Boolean.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"groupId", "deleted", "popular"}, true);

		_finderPathWithoutPaginationFindByActivePopularCourse =
			_createFinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
				"findByActivePopularCourse",
				new String[] {
					Long.class.getName(), Boolean.class.getName(),
					Boolean.class.getName()
				},
				new String[] {"groupId", "deleted", "popular"}, true);

		_finderPathCountByActivePopularCourse = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByActivePopularCourse",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Boolean.class.getName()
			},
			new String[] {"groupId", "deleted", "popular"}, false);
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(CourseImpl.class.getName());

		_argumentsResolverServiceRegistration.unregister();

		for (ServiceRegistration<FinderPath> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	@Override
	@Reference(
		target = NTUC_COURSE_ADMINPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = NTUC_COURSE_ADMINPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = NTUC_COURSE_ADMINPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
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

	private static final String _SQL_SELECT_COURSE =
		"SELECT course FROM Course course";

	private static final String _SQL_SELECT_COURSE_WHERE =
		"SELECT course FROM Course course WHERE ";

	private static final String _SQL_COUNT_COURSE =
		"SELECT COUNT(course) FROM Course course";

	private static final String _SQL_COUNT_COURSE_WHERE =
		"SELECT COUNT(course) FROM Course course WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "course.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No Course exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No Course exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		CoursePersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid"});

	static {
		try {
			Class.forName(
				NTUC_COURSE_ADMINPersistenceConstants.class.getName());
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

	private static class CourseModelArgumentsResolver
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

			CourseModelImpl courseModelImpl = (CourseModelImpl)baseModel;

			long columnBitmask = courseModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(courseModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |= courseModelImpl.getColumnBitmask(
						columnName);
				}

				_finderPathColumnBitmasksCache.put(
					finderPath, finderPathColumnBitmask);
			}

			if ((columnBitmask & finderPathColumnBitmask) != 0) {
				return _getValue(courseModelImpl, columnNames, original);
			}

			return null;
		}

		private Object[] _getValue(
			CourseModelImpl courseModelImpl, String[] columnNames,
			boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] = courseModelImpl.getColumnOriginalValue(
						columnName);
				}
				else {
					arguments[i] = courseModelImpl.getColumnValue(columnName);
				}
			}

			return arguments;
		}

		private static Map<FinderPath, Long> _finderPathColumnBitmasksCache =
			new ConcurrentHashMap<>();

	}

}