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

package svc.ntuc.nlh.otp.service.persistence.impl;

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

import svc.ntuc.nlh.otp.exception.NoSuchOtpException;
import svc.ntuc.nlh.otp.model.Otp;
import svc.ntuc.nlh.otp.model.impl.OtpImpl;
import svc.ntuc.nlh.otp.model.impl.OtpModelImpl;
import svc.ntuc.nlh.otp.service.persistence.OtpPersistence;
import svc.ntuc.nlh.otp.service.persistence.impl.constants.NTUC_OTPPersistenceConstants;

/**
 * The persistence implementation for the otp service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = OtpPersistence.class)
public class OtpPersistenceImpl
	extends BasePersistenceImpl<Otp> implements OtpPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>OtpUtil</code> to access the otp persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		OtpImpl.class.getName();

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
	 * Returns all the otps where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching otps
	 */
	@Override
	public List<Otp> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the otps where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @return the range of matching otps
	 */
	@Override
	public List<Otp> findByUuid(String uuid, int start, int end) {
		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the otps where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching otps
	 */
	@Override
	public List<Otp> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<Otp> orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the otps where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching otps
	 */
	@Override
	public List<Otp> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<Otp> orderByComparator, boolean useFinderCache) {

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

		List<Otp> list = null;

		if (useFinderCache) {
			list = (List<Otp>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Otp otp : list) {
					if (!uuid.equals(otp.getUuid())) {
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

			sb.append(_SQL_SELECT_OTP_WHERE);

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
				sb.append(OtpModelImpl.ORDER_BY_JPQL);
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

				list = (List<Otp>)QueryUtil.list(
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
	 * Returns the first otp in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	@Override
	public Otp findByUuid_First(
			String uuid, OrderByComparator<Otp> orderByComparator)
		throws NoSuchOtpException {

		Otp otp = fetchByUuid_First(uuid, orderByComparator);

		if (otp != null) {
			return otp;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchOtpException(sb.toString());
	}

	/**
	 * Returns the first otp in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching otp, or <code>null</code> if a matching otp could not be found
	 */
	@Override
	public Otp fetchByUuid_First(
		String uuid, OrderByComparator<Otp> orderByComparator) {

		List<Otp> list = findByUuid(uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last otp in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	@Override
	public Otp findByUuid_Last(
			String uuid, OrderByComparator<Otp> orderByComparator)
		throws NoSuchOtpException {

		Otp otp = fetchByUuid_Last(uuid, orderByComparator);

		if (otp != null) {
			return otp;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchOtpException(sb.toString());
	}

	/**
	 * Returns the last otp in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching otp, or <code>null</code> if a matching otp could not be found
	 */
	@Override
	public Otp fetchByUuid_Last(
		String uuid, OrderByComparator<Otp> orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<Otp> list = findByUuid(uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the otps before and after the current otp in the ordered set where uuid = &#63;.
	 *
	 * @param otpId the primary key of the current otp
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next otp
	 * @throws NoSuchOtpException if a otp with the primary key could not be found
	 */
	@Override
	public Otp[] findByUuid_PrevAndNext(
			long otpId, String uuid, OrderByComparator<Otp> orderByComparator)
		throws NoSuchOtpException {

		uuid = Objects.toString(uuid, "");

		Otp otp = findByPrimaryKey(otpId);

		Session session = null;

		try {
			session = openSession();

			Otp[] array = new OtpImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, otp, uuid, orderByComparator, true);

			array[1] = otp;

			array[2] = getByUuid_PrevAndNext(
				session, otp, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected Otp getByUuid_PrevAndNext(
		Session session, Otp otp, String uuid,
		OrderByComparator<Otp> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_OTP_WHERE);

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
			sb.append(OtpModelImpl.ORDER_BY_JPQL);
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
					orderByComparator.getOrderByConditionValues(otp)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Otp> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the otps where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (Otp otp :
				findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(otp);
		}
	}

	/**
	 * Returns the number of otps where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching otps
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid;

		Object[] finderArgs = new Object[] {uuid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_OTP_WHERE);

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

	private static final String _FINDER_COLUMN_UUID_UUID_2 = "otp.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(otp.uuid IS NULL OR otp.uuid = '')";

	private FinderPath _finderPathFetchByUUID_G;
	private FinderPath _finderPathCountByUUID_G;

	/**
	 * Returns the otp where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchOtpException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	@Override
	public Otp findByUUID_G(String uuid, long groupId)
		throws NoSuchOtpException {

		Otp otp = fetchByUUID_G(uuid, groupId);

		if (otp == null) {
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

			throw new NoSuchOtpException(sb.toString());
		}

		return otp;
	}

	/**
	 * Returns the otp where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching otp, or <code>null</code> if a matching otp could not be found
	 */
	@Override
	public Otp fetchByUUID_G(String uuid, long groupId) {
		return fetchByUUID_G(uuid, groupId, true);
	}

	/**
	 * Returns the otp where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching otp, or <code>null</code> if a matching otp could not be found
	 */
	@Override
	public Otp fetchByUUID_G(
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

		if (result instanceof Otp) {
			Otp otp = (Otp)result;

			if (!Objects.equals(uuid, otp.getUuid()) ||
				(groupId != otp.getGroupId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_OTP_WHERE);

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

				List<Otp> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByUUID_G, finderArgs, list);
					}
				}
				else {
					Otp otp = list.get(0);

					result = otp;

					cacheResult(otp);
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
			return (Otp)result;
		}
	}

	/**
	 * Removes the otp where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the otp that was removed
	 */
	@Override
	public Otp removeByUUID_G(String uuid, long groupId)
		throws NoSuchOtpException {

		Otp otp = findByUUID_G(uuid, groupId);

		return remove(otp);
	}

	/**
	 * Returns the number of otps where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching otps
	 */
	@Override
	public int countByUUID_G(String uuid, long groupId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUUID_G;

		Object[] finderArgs = new Object[] {uuid, groupId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_OTP_WHERE);

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
		"otp.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_G_UUID_3 =
		"(otp.uuid IS NULL OR otp.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_G_GROUPID_2 =
		"otp.groupId = ?";

	private FinderPath _finderPathWithPaginationFindByUuid_C;
	private FinderPath _finderPathWithoutPaginationFindByUuid_C;
	private FinderPath _finderPathCountByUuid_C;

	/**
	 * Returns all the otps where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching otps
	 */
	@Override
	public List<Otp> findByUuid_C(String uuid, long companyId) {
		return findByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the otps where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @return the range of matching otps
	 */
	@Override
	public List<Otp> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the otps where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching otps
	 */
	@Override
	public List<Otp> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Otp> orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the otps where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching otps
	 */
	@Override
	public List<Otp> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Otp> orderByComparator, boolean useFinderCache) {

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

		List<Otp> list = null;

		if (useFinderCache) {
			list = (List<Otp>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Otp otp : list) {
					if (!uuid.equals(otp.getUuid()) ||
						(companyId != otp.getCompanyId())) {

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

			sb.append(_SQL_SELECT_OTP_WHERE);

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
				sb.append(OtpModelImpl.ORDER_BY_JPQL);
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

				list = (List<Otp>)QueryUtil.list(
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
	 * Returns the first otp in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	@Override
	public Otp findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<Otp> orderByComparator)
		throws NoSuchOtpException {

		Otp otp = fetchByUuid_C_First(uuid, companyId, orderByComparator);

		if (otp != null) {
			return otp;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchOtpException(sb.toString());
	}

	/**
	 * Returns the first otp in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching otp, or <code>null</code> if a matching otp could not be found
	 */
	@Override
	public Otp fetchByUuid_C_First(
		String uuid, long companyId, OrderByComparator<Otp> orderByComparator) {

		List<Otp> list = findByUuid_C(uuid, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last otp in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	@Override
	public Otp findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<Otp> orderByComparator)
		throws NoSuchOtpException {

		Otp otp = fetchByUuid_C_Last(uuid, companyId, orderByComparator);

		if (otp != null) {
			return otp;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchOtpException(sb.toString());
	}

	/**
	 * Returns the last otp in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching otp, or <code>null</code> if a matching otp could not be found
	 */
	@Override
	public Otp fetchByUuid_C_Last(
		String uuid, long companyId, OrderByComparator<Otp> orderByComparator) {

		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<Otp> list = findByUuid_C(
			uuid, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the otps before and after the current otp in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param otpId the primary key of the current otp
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next otp
	 * @throws NoSuchOtpException if a otp with the primary key could not be found
	 */
	@Override
	public Otp[] findByUuid_C_PrevAndNext(
			long otpId, String uuid, long companyId,
			OrderByComparator<Otp> orderByComparator)
		throws NoSuchOtpException {

		uuid = Objects.toString(uuid, "");

		Otp otp = findByPrimaryKey(otpId);

		Session session = null;

		try {
			session = openSession();

			Otp[] array = new OtpImpl[3];

			array[0] = getByUuid_C_PrevAndNext(
				session, otp, uuid, companyId, orderByComparator, true);

			array[1] = otp;

			array[2] = getByUuid_C_PrevAndNext(
				session, otp, uuid, companyId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected Otp getByUuid_C_PrevAndNext(
		Session session, Otp otp, String uuid, long companyId,
		OrderByComparator<Otp> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_OTP_WHERE);

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
			sb.append(OtpModelImpl.ORDER_BY_JPQL);
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
					orderByComparator.getOrderByConditionValues(otp)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Otp> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the otps where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (Otp otp :
				findByUuid_C(
					uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(otp);
		}
	}

	/**
	 * Returns the number of otps where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching otps
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid_C;

		Object[] finderArgs = new Object[] {uuid, companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_OTP_WHERE);

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
		"otp.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3 =
		"(otp.uuid IS NULL OR otp.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 =
		"otp.companyId = ?";

	private FinderPath _finderPathFetchByOtpIdUserId;
	private FinderPath _finderPathCountByOtpIdUserId;

	/**
	 * Returns the otp where otpId = &#63; and userId = &#63; or throws a <code>NoSuchOtpException</code> if it could not be found.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @return the matching otp
	 * @throws NoSuchOtpException if a matching otp could not be found
	 */
	@Override
	public Otp findByOtpIdUserId(long otpId, long userId)
		throws NoSuchOtpException {

		Otp otp = fetchByOtpIdUserId(otpId, userId);

		if (otp == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("otpId=");
			sb.append(otpId);

			sb.append(", userId=");
			sb.append(userId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchOtpException(sb.toString());
		}

		return otp;
	}

	/**
	 * Returns the otp where otpId = &#63; and userId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @return the matching otp, or <code>null</code> if a matching otp could not be found
	 */
	@Override
	public Otp fetchByOtpIdUserId(long otpId, long userId) {
		return fetchByOtpIdUserId(otpId, userId, true);
	}

	/**
	 * Returns the otp where otpId = &#63; and userId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching otp, or <code>null</code> if a matching otp could not be found
	 */
	@Override
	public Otp fetchByOtpIdUserId(
		long otpId, long userId, boolean useFinderCache) {

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {otpId, userId};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByOtpIdUserId, finderArgs, this);
		}

		if (result instanceof Otp) {
			Otp otp = (Otp)result;

			if ((otpId != otp.getOtpId()) || (userId != otp.getUserId())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_OTP_WHERE);

			sb.append(_FINDER_COLUMN_OTPIDUSERID_OTPID_2);

			sb.append(_FINDER_COLUMN_OTPIDUSERID_USERID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(otpId);

				queryPos.add(userId);

				List<Otp> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByOtpIdUserId, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {otpId, userId};
							}

							_log.warn(
								"OtpPersistenceImpl.fetchByOtpIdUserId(long, long, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					Otp otp = list.get(0);

					result = otp;

					cacheResult(otp);
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
			return (Otp)result;
		}
	}

	/**
	 * Removes the otp where otpId = &#63; and userId = &#63; from the database.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @return the otp that was removed
	 */
	@Override
	public Otp removeByOtpIdUserId(long otpId, long userId)
		throws NoSuchOtpException {

		Otp otp = findByOtpIdUserId(otpId, userId);

		return remove(otp);
	}

	/**
	 * Returns the number of otps where otpId = &#63; and userId = &#63;.
	 *
	 * @param otpId the otp ID
	 * @param userId the user ID
	 * @return the number of matching otps
	 */
	@Override
	public int countByOtpIdUserId(long otpId, long userId) {
		FinderPath finderPath = _finderPathCountByOtpIdUserId;

		Object[] finderArgs = new Object[] {otpId, userId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_OTP_WHERE);

			sb.append(_FINDER_COLUMN_OTPIDUSERID_OTPID_2);

			sb.append(_FINDER_COLUMN_OTPIDUSERID_USERID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(otpId);

				queryPos.add(userId);

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

	private static final String _FINDER_COLUMN_OTPIDUSERID_OTPID_2 =
		"otp.otpId = ? AND ";

	private static final String _FINDER_COLUMN_OTPIDUSERID_USERID_2 =
		"otp.userId = ?";

	public OtpPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");

		setDBColumnNames(dbColumnNames);

		setModelClass(Otp.class);

		setModelImplClass(OtpImpl.class);
		setModelPKClass(long.class);
	}

	/**
	 * Caches the otp in the entity cache if it is enabled.
	 *
	 * @param otp the otp
	 */
	@Override
	public void cacheResult(Otp otp) {
		entityCache.putResult(OtpImpl.class, otp.getPrimaryKey(), otp);

		finderCache.putResult(
			_finderPathFetchByUUID_G,
			new Object[] {otp.getUuid(), otp.getGroupId()}, otp);

		finderCache.putResult(
			_finderPathFetchByOtpIdUserId,
			new Object[] {otp.getOtpId(), otp.getUserId()}, otp);
	}

	/**
	 * Caches the otps in the entity cache if it is enabled.
	 *
	 * @param otps the otps
	 */
	@Override
	public void cacheResult(List<Otp> otps) {
		for (Otp otp : otps) {
			if (entityCache.getResult(OtpImpl.class, otp.getPrimaryKey()) ==
					null) {

				cacheResult(otp);
			}
		}
	}

	/**
	 * Clears the cache for all otps.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(OtpImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the otp.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Otp otp) {
		entityCache.removeResult(OtpImpl.class, otp);
	}

	@Override
	public void clearCache(List<Otp> otps) {
		for (Otp otp : otps) {
			entityCache.removeResult(OtpImpl.class, otp);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(OtpImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(OtpModelImpl otpModelImpl) {
		Object[] args = new Object[] {
			otpModelImpl.getUuid(), otpModelImpl.getGroupId()
		};

		finderCache.putResult(
			_finderPathCountByUUID_G, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByUUID_G, args, otpModelImpl, false);

		args = new Object[] {otpModelImpl.getOtpId(), otpModelImpl.getUserId()};

		finderCache.putResult(
			_finderPathCountByOtpIdUserId, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByOtpIdUserId, args, otpModelImpl, false);
	}

	/**
	 * Creates a new otp with the primary key. Does not add the otp to the database.
	 *
	 * @param otpId the primary key for the new otp
	 * @return the new otp
	 */
	@Override
	public Otp create(long otpId) {
		Otp otp = new OtpImpl();

		otp.setNew(true);
		otp.setPrimaryKey(otpId);

		String uuid = PortalUUIDUtil.generate();

		otp.setUuid(uuid);

		otp.setCompanyId(CompanyThreadLocal.getCompanyId());

		return otp;
	}

	/**
	 * Removes the otp with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param otpId the primary key of the otp
	 * @return the otp that was removed
	 * @throws NoSuchOtpException if a otp with the primary key could not be found
	 */
	@Override
	public Otp remove(long otpId) throws NoSuchOtpException {
		return remove((Serializable)otpId);
	}

	/**
	 * Removes the otp with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the otp
	 * @return the otp that was removed
	 * @throws NoSuchOtpException if a otp with the primary key could not be found
	 */
	@Override
	public Otp remove(Serializable primaryKey) throws NoSuchOtpException {
		Session session = null;

		try {
			session = openSession();

			Otp otp = (Otp)session.get(OtpImpl.class, primaryKey);

			if (otp == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchOtpException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(otp);
		}
		catch (NoSuchOtpException noSuchEntityException) {
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
	protected Otp removeImpl(Otp otp) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(otp)) {
				otp = (Otp)session.get(OtpImpl.class, otp.getPrimaryKeyObj());
			}

			if (otp != null) {
				session.delete(otp);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (otp != null) {
			clearCache(otp);
		}

		return otp;
	}

	@Override
	public Otp updateImpl(Otp otp) {
		boolean isNew = otp.isNew();

		if (!(otp instanceof OtpModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(otp.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(otp);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in otp proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom Otp implementation " +
					otp.getClass());
		}

		OtpModelImpl otpModelImpl = (OtpModelImpl)otp;

		if (Validator.isNull(otp.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			otp.setUuid(uuid);
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (otp.getCreateDate() == null)) {
			if (serviceContext == null) {
				otp.setCreateDate(now);
			}
			else {
				otp.setCreateDate(serviceContext.getCreateDate(now));
			}
		}

		if (!otpModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				otp.setModifiedDate(now);
			}
			else {
				otp.setModifiedDate(serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(otp);
			}
			else {
				otp = (Otp)session.merge(otp);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(OtpImpl.class, otpModelImpl, false, true);

		cacheUniqueFindersCache(otpModelImpl);

		if (isNew) {
			otp.setNew(false);
		}

		otp.resetOriginalValues();

		return otp;
	}

	/**
	 * Returns the otp with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the otp
	 * @return the otp
	 * @throws NoSuchOtpException if a otp with the primary key could not be found
	 */
	@Override
	public Otp findByPrimaryKey(Serializable primaryKey)
		throws NoSuchOtpException {

		Otp otp = fetchByPrimaryKey(primaryKey);

		if (otp == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchOtpException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return otp;
	}

	/**
	 * Returns the otp with the primary key or throws a <code>NoSuchOtpException</code> if it could not be found.
	 *
	 * @param otpId the primary key of the otp
	 * @return the otp
	 * @throws NoSuchOtpException if a otp with the primary key could not be found
	 */
	@Override
	public Otp findByPrimaryKey(long otpId) throws NoSuchOtpException {
		return findByPrimaryKey((Serializable)otpId);
	}

	/**
	 * Returns the otp with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param otpId the primary key of the otp
	 * @return the otp, or <code>null</code> if a otp with the primary key could not be found
	 */
	@Override
	public Otp fetchByPrimaryKey(long otpId) {
		return fetchByPrimaryKey((Serializable)otpId);
	}

	/**
	 * Returns all the otps.
	 *
	 * @return the otps
	 */
	@Override
	public List<Otp> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the otps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @return the range of otps
	 */
	@Override
	public List<Otp> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the otps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of otps
	 */
	@Override
	public List<Otp> findAll(
		int start, int end, OrderByComparator<Otp> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the otps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OtpModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of otps
	 * @param end the upper bound of the range of otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of otps
	 */
	@Override
	public List<Otp> findAll(
		int start, int end, OrderByComparator<Otp> orderByComparator,
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

		List<Otp> list = null;

		if (useFinderCache) {
			list = (List<Otp>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_OTP);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_OTP;

				sql = sql.concat(OtpModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<Otp>)QueryUtil.list(
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
	 * Removes all the otps from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (Otp otp : findAll()) {
			remove(otp);
		}
	}

	/**
	 * Returns the number of otps.
	 *
	 * @return the number of otps
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_OTP);

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
		return "otpId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_OTP;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return OtpModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the otp persistence.
	 */
	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_argumentsResolverServiceRegistration = _bundleContext.registerService(
			ArgumentsResolver.class, new OtpModelArgumentsResolver(),
			MapUtil.singletonDictionary(
				"model.class.name", Otp.class.getName()));

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

		_finderPathFetchByOtpIdUserId = _createFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByOtpIdUserId",
			new String[] {Long.class.getName(), Long.class.getName()},
			new String[] {"otpId", "userId"}, true);

		_finderPathCountByOtpIdUserId = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByOtpIdUserId",
			new String[] {Long.class.getName(), Long.class.getName()},
			new String[] {"otpId", "userId"}, false);
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(OtpImpl.class.getName());

		_argumentsResolverServiceRegistration.unregister();

		for (ServiceRegistration<FinderPath> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	@Override
	@Reference(
		target = NTUC_OTPPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = NTUC_OTPPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = NTUC_OTPPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
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

	private static final String _SQL_SELECT_OTP = "SELECT otp FROM Otp otp";

	private static final String _SQL_SELECT_OTP_WHERE =
		"SELECT otp FROM Otp otp WHERE ";

	private static final String _SQL_COUNT_OTP =
		"SELECT COUNT(otp) FROM Otp otp";

	private static final String _SQL_COUNT_OTP_WHERE =
		"SELECT COUNT(otp) FROM Otp otp WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "otp.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No Otp exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No Otp exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		OtpPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid"});

	static {
		try {
			Class.forName(NTUC_OTPPersistenceConstants.class.getName());
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

	private static class OtpModelArgumentsResolver
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

			OtpModelImpl otpModelImpl = (OtpModelImpl)baseModel;

			long columnBitmask = otpModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(otpModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |= otpModelImpl.getColumnBitmask(
						columnName);
				}

				_finderPathColumnBitmasksCache.put(
					finderPath, finderPathColumnBitmask);
			}

			if ((columnBitmask & finderPathColumnBitmask) != 0) {
				return _getValue(otpModelImpl, columnNames, original);
			}

			return null;
		}

		private Object[] _getValue(
			OtpModelImpl otpModelImpl, String[] columnNames, boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] = otpModelImpl.getColumnOriginalValue(
						columnName);
				}
				else {
					arguments[i] = otpModelImpl.getColumnValue(columnName);
				}
			}

			return arguments;
		}

		private static Map<FinderPath, Long> _finderPathColumnBitmasksCache =
			new ConcurrentHashMap<>();

	}

}