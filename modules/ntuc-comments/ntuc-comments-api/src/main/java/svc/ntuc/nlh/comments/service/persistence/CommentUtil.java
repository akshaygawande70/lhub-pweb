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

package svc.ntuc.nlh.comments.service.persistence;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import svc.ntuc.nlh.comments.model.Comment;

/**
 * The persistence utility for the comment service. This utility wraps <code>svc.ntuc.nlh.comments.service.persistence.impl.CommentPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CommentPersistence
 * @generated
 */
public class CommentUtil {

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
	public static void clearCache(Comment comment) {
		getPersistence().clearCache(comment);
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
	public static Map<Serializable, Comment> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<Comment> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<Comment> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<Comment> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<Comment> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static Comment update(Comment comment) {
		return getPersistence().update(comment);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static Comment update(
		Comment comment, ServiceContext serviceContext) {

		return getPersistence().update(comment, serviceContext);
	}

	/**
	 * Returns all the comments where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching comments
	 */
	public static List<Comment> findByUuid(String uuid) {
		return getPersistence().findByUuid(uuid);
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
	public static List<Comment> findByUuid(String uuid, int start, int end) {
		return getPersistence().findByUuid(uuid, start, end);
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
	public static List<Comment> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<Comment> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
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
	public static List<Comment> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<Comment> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first comment in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching comment
	 * @throws NoSuchCommentException if a matching comment could not be found
	 */
	public static Comment findByUuid_First(
			String uuid, OrderByComparator<Comment> orderByComparator)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first comment in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching comment, or <code>null</code> if a matching comment could not be found
	 */
	public static Comment fetchByUuid_First(
		String uuid, OrderByComparator<Comment> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the last comment in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching comment
	 * @throws NoSuchCommentException if a matching comment could not be found
	 */
	public static Comment findByUuid_Last(
			String uuid, OrderByComparator<Comment> orderByComparator)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the last comment in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching comment, or <code>null</code> if a matching comment could not be found
	 */
	public static Comment fetchByUuid_Last(
		String uuid, OrderByComparator<Comment> orderByComparator) {

		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
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
	public static Comment[] findByUuid_PrevAndNext(
			long commentId, String uuid,
			OrderByComparator<Comment> orderByComparator)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().findByUuid_PrevAndNext(
			commentId, uuid, orderByComparator);
	}

	/**
	 * Removes all the comments where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of comments where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching comments
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Returns the comment where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchCommentException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching comment
	 * @throws NoSuchCommentException if a matching comment could not be found
	 */
	public static Comment findByUUID_G(String uuid, long groupId)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the comment where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching comment, or <code>null</code> if a matching comment could not be found
	 */
	public static Comment fetchByUUID_G(String uuid, long groupId) {
		return getPersistence().fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the comment where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching comment, or <code>null</code> if a matching comment could not be found
	 */
	public static Comment fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache) {

		return getPersistence().fetchByUUID_G(uuid, groupId, useFinderCache);
	}

	/**
	 * Removes the comment where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the comment that was removed
	 */
	public static Comment removeByUUID_G(String uuid, long groupId)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().removeByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the number of comments where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching comments
	 */
	public static int countByUUID_G(String uuid, long groupId) {
		return getPersistence().countByUUID_G(uuid, groupId);
	}

	/**
	 * Returns all the comments where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching comments
	 */
	public static List<Comment> findByUuid_C(String uuid, long companyId) {
		return getPersistence().findByUuid_C(uuid, companyId);
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
	public static List<Comment> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return getPersistence().findByUuid_C(uuid, companyId, start, end);
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
	public static List<Comment> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Comment> orderByComparator) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
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
	public static List<Comment> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Comment> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator, useFinderCache);
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
	public static Comment findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<Comment> orderByComparator)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().findByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the first comment in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching comment, or <code>null</code> if a matching comment could not be found
	 */
	public static Comment fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<Comment> orderByComparator) {

		return getPersistence().fetchByUuid_C_First(
			uuid, companyId, orderByComparator);
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
	public static Comment findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<Comment> orderByComparator)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().findByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last comment in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching comment, or <code>null</code> if a matching comment could not be found
	 */
	public static Comment fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<Comment> orderByComparator) {

		return getPersistence().fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);
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
	public static Comment[] findByUuid_C_PrevAndNext(
			long commentId, String uuid, long companyId,
			OrderByComparator<Comment> orderByComparator)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().findByUuid_C_PrevAndNext(
			commentId, uuid, companyId, orderByComparator);
	}

	/**
	 * Removes all the comments where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public static void removeByUuid_C(String uuid, long companyId) {
		getPersistence().removeByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the number of comments where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching comments
	 */
	public static int countByUuid_C(String uuid, long companyId) {
		return getPersistence().countByUuid_C(uuid, companyId);
	}

	/**
	 * Returns all the comments where groupId = &#63; and commentName = &#63;.
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 * @return the matching comments
	 */
	public static List<Comment> findByName(long groupId, String commentName) {
		return getPersistence().findByName(groupId, commentName);
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
	public static List<Comment> findByName(
		long groupId, String commentName, int start, int end) {

		return getPersistence().findByName(groupId, commentName, start, end);
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
	public static List<Comment> findByName(
		long groupId, String commentName, int start, int end,
		OrderByComparator<Comment> orderByComparator) {

		return getPersistence().findByName(
			groupId, commentName, start, end, orderByComparator);
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
	public static List<Comment> findByName(
		long groupId, String commentName, int start, int end,
		OrderByComparator<Comment> orderByComparator, boolean useFinderCache) {

		return getPersistence().findByName(
			groupId, commentName, start, end, orderByComparator,
			useFinderCache);
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
	public static Comment findByName_First(
			long groupId, String commentName,
			OrderByComparator<Comment> orderByComparator)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().findByName_First(
			groupId, commentName, orderByComparator);
	}

	/**
	 * Returns the first comment in the ordered set where groupId = &#63; and commentName = &#63;.
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching comment, or <code>null</code> if a matching comment could not be found
	 */
	public static Comment fetchByName_First(
		long groupId, String commentName,
		OrderByComparator<Comment> orderByComparator) {

		return getPersistence().fetchByName_First(
			groupId, commentName, orderByComparator);
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
	public static Comment findByName_Last(
			long groupId, String commentName,
			OrderByComparator<Comment> orderByComparator)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().findByName_Last(
			groupId, commentName, orderByComparator);
	}

	/**
	 * Returns the last comment in the ordered set where groupId = &#63; and commentName = &#63;.
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching comment, or <code>null</code> if a matching comment could not be found
	 */
	public static Comment fetchByName_Last(
		long groupId, String commentName,
		OrderByComparator<Comment> orderByComparator) {

		return getPersistence().fetchByName_Last(
			groupId, commentName, orderByComparator);
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
	public static Comment[] findByName_PrevAndNext(
			long commentId, long groupId, String commentName,
			OrderByComparator<Comment> orderByComparator)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().findByName_PrevAndNext(
			commentId, groupId, commentName, orderByComparator);
	}

	/**
	 * Removes all the comments where groupId = &#63; and commentName = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 */
	public static void removeByName(long groupId, String commentName) {
		getPersistence().removeByName(groupId, commentName);
	}

	/**
	 * Returns the number of comments where groupId = &#63; and commentName = &#63;.
	 *
	 * @param groupId the group ID
	 * @param commentName the comment name
	 * @return the number of matching comments
	 */
	public static int countByName(long groupId, String commentName) {
		return getPersistence().countByName(groupId, commentName);
	}

	/**
	 * Returns the comment where groupId = &#63; and commentEmail = &#63; or throws a <code>NoSuchCommentException</code> if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param commentEmail the comment email
	 * @return the matching comment
	 * @throws NoSuchCommentException if a matching comment could not be found
	 */
	public static Comment findByEmail(long groupId, String commentEmail)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().findByEmail(groupId, commentEmail);
	}

	/**
	 * Returns the comment where groupId = &#63; and commentEmail = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param commentEmail the comment email
	 * @return the matching comment, or <code>null</code> if a matching comment could not be found
	 */
	public static Comment fetchByEmail(long groupId, String commentEmail) {
		return getPersistence().fetchByEmail(groupId, commentEmail);
	}

	/**
	 * Returns the comment where groupId = &#63; and commentEmail = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param commentEmail the comment email
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching comment, or <code>null</code> if a matching comment could not be found
	 */
	public static Comment fetchByEmail(
		long groupId, String commentEmail, boolean useFinderCache) {

		return getPersistence().fetchByEmail(
			groupId, commentEmail, useFinderCache);
	}

	/**
	 * Removes the comment where groupId = &#63; and commentEmail = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param commentEmail the comment email
	 * @return the comment that was removed
	 */
	public static Comment removeByEmail(long groupId, String commentEmail)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().removeByEmail(groupId, commentEmail);
	}

	/**
	 * Returns the number of comments where groupId = &#63; and commentEmail = &#63;.
	 *
	 * @param groupId the group ID
	 * @param commentEmail the comment email
	 * @return the number of matching comments
	 */
	public static int countByEmail(long groupId, String commentEmail) {
		return getPersistence().countByEmail(groupId, commentEmail);
	}

	/**
	 * Caches the comment in the entity cache if it is enabled.
	 *
	 * @param comment the comment
	 */
	public static void cacheResult(Comment comment) {
		getPersistence().cacheResult(comment);
	}

	/**
	 * Caches the comments in the entity cache if it is enabled.
	 *
	 * @param comments the comments
	 */
	public static void cacheResult(List<Comment> comments) {
		getPersistence().cacheResult(comments);
	}

	/**
	 * Creates a new comment with the primary key. Does not add the comment to the database.
	 *
	 * @param commentId the primary key for the new comment
	 * @return the new comment
	 */
	public static Comment create(long commentId) {
		return getPersistence().create(commentId);
	}

	/**
	 * Removes the comment with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commentId the primary key of the comment
	 * @return the comment that was removed
	 * @throws NoSuchCommentException if a comment with the primary key could not be found
	 */
	public static Comment remove(long commentId)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().remove(commentId);
	}

	public static Comment updateImpl(Comment comment) {
		return getPersistence().updateImpl(comment);
	}

	/**
	 * Returns the comment with the primary key or throws a <code>NoSuchCommentException</code> if it could not be found.
	 *
	 * @param commentId the primary key of the comment
	 * @return the comment
	 * @throws NoSuchCommentException if a comment with the primary key could not be found
	 */
	public static Comment findByPrimaryKey(long commentId)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getPersistence().findByPrimaryKey(commentId);
	}

	/**
	 * Returns the comment with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commentId the primary key of the comment
	 * @return the comment, or <code>null</code> if a comment with the primary key could not be found
	 */
	public static Comment fetchByPrimaryKey(long commentId) {
		return getPersistence().fetchByPrimaryKey(commentId);
	}

	/**
	 * Returns all the comments.
	 *
	 * @return the comments
	 */
	public static List<Comment> findAll() {
		return getPersistence().findAll();
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
	public static List<Comment> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
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
	public static List<Comment> findAll(
		int start, int end, OrderByComparator<Comment> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
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
	public static List<Comment> findAll(
		int start, int end, OrderByComparator<Comment> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the comments from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of comments.
	 *
	 * @return the number of comments
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static CommentPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<CommentPersistence, CommentPersistence>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(CommentPersistence.class);

		ServiceTracker<CommentPersistence, CommentPersistence> serviceTracker =
			new ServiceTracker<CommentPersistence, CommentPersistence>(
				bundle.getBundleContext(), CommentPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}