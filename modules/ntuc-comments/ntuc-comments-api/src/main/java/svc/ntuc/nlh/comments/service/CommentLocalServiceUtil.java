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

package svc.ntuc.nlh.comments.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for Comment. This utility wraps
 * <code>svc.ntuc.nlh.comments.service.impl.CommentLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see CommentLocalService
 * @generated
 */
public class CommentLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>svc.ntuc.nlh.comments.service.impl.CommentLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the comment to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommentLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param comment the comment
	 * @return the comment that was added
	 */
	public static svc.ntuc.nlh.comments.model.Comment addComment(
		svc.ntuc.nlh.comments.model.Comment comment) {

		return getService().addComment(comment);
	}

	/**
	 * Creates a new comment with the primary key. Does not add the comment to the database.
	 *
	 * @param commentId the primary key for the new comment
	 * @return the new comment
	 */
	public static svc.ntuc.nlh.comments.model.Comment createComment(
		long commentId) {

		return getService().createComment(commentId);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			createPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().createPersistedModel(primaryKeyObj);
	}

	/**
	 * Deletes the comment from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommentLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param comment the comment
	 * @return the comment that was removed
	 */
	public static svc.ntuc.nlh.comments.model.Comment deleteComment(
		svc.ntuc.nlh.comments.model.Comment comment) {

		return getService().deleteComment(comment);
	}

	/**
	 * Deletes the comment with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommentLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commentId the primary key of the comment
	 * @return the comment that was removed
	 * @throws PortalException if a comment with the primary key could not be found
	 */
	public static svc.ntuc.nlh.comments.model.Comment deleteComment(
			long commentId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteComment(commentId);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			deletePersistedModel(
				com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery
		dynamicQuery() {

		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.comments.model.impl.CommentModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.comments.model.impl.CommentModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static svc.ntuc.nlh.comments.model.Comment fetchComment(
		long commentId) {

		return getService().fetchComment(commentId);
	}

	/**
	 * Returns the comment matching the UUID and group.
	 *
	 * @param uuid the comment's UUID
	 * @param groupId the primary key of the group
	 * @return the matching comment, or <code>null</code> if a matching comment could not be found
	 */
	public static svc.ntuc.nlh.comments.model.Comment
		fetchCommentByUuidAndGroupId(String uuid, long groupId) {

		return getService().fetchCommentByUuidAndGroupId(uuid, groupId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static java.util.List<svc.ntuc.nlh.comments.model.Comment>
		getAllComment() {

		return getService().getAllComment();
	}

	/**
	 * Returns the comment with the primary key.
	 *
	 * @param commentId the primary key of the comment
	 * @return the comment
	 * @throws PortalException if a comment with the primary key could not be found
	 */
	public static svc.ntuc.nlh.comments.model.Comment getComment(long commentId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getComment(commentId);
	}

	public static svc.ntuc.nlh.comments.model.Comment getCommentByEmail(
			long groupId, String commentEmail)
		throws svc.ntuc.nlh.comments.exception.NoSuchCommentException {

		return getService().getCommentByEmail(groupId, commentEmail);
	}

	public static java.util.List<svc.ntuc.nlh.comments.model.Comment>
		getCommentByName(long groupId, String commentName) {

		return getService().getCommentByName(groupId, commentName);
	}

	/**
	 * Returns the comment matching the UUID and group.
	 *
	 * @param uuid the comment's UUID
	 * @param groupId the primary key of the group
	 * @return the matching comment
	 * @throws PortalException if a matching comment could not be found
	 */
	public static svc.ntuc.nlh.comments.model.Comment
			getCommentByUuidAndGroupId(String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCommentByUuidAndGroupId(uuid, groupId);
	}

	/**
	 * Returns a range of all the comments.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.comments.model.impl.CommentModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of comments
	 * @param end the upper bound of the range of comments (not inclusive)
	 * @return the range of comments
	 */
	public static java.util.List<svc.ntuc.nlh.comments.model.Comment>
		getComments(int start, int end) {

		return getService().getComments(start, end);
	}

	/**
	 * Returns all the comments matching the UUID and company.
	 *
	 * @param uuid the UUID of the comments
	 * @param companyId the primary key of the company
	 * @return the matching comments, or an empty list if no matches were found
	 */
	public static java.util.List<svc.ntuc.nlh.comments.model.Comment>
		getCommentsByUuidAndCompanyId(String uuid, long companyId) {

		return getService().getCommentsByUuidAndCompanyId(uuid, companyId);
	}

	/**
	 * Returns a range of comments matching the UUID and company.
	 *
	 * @param uuid the UUID of the comments
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of comments
	 * @param end the upper bound of the range of comments (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching comments, or an empty list if no matches were found
	 */
	public static java.util.List<svc.ntuc.nlh.comments.model.Comment>
		getCommentsByUuidAndCompanyId(
			String uuid, long companyId, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<svc.ntuc.nlh.comments.model.Comment> orderByComparator) {

		return getService().getCommentsByUuidAndCompanyId(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the number of comments.
	 *
	 * @return the number of comments
	 */
	public static int getCommentsCount() {
		return getService().getCommentsCount();
	}

	public static com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return getService().getExportActionableDynamicQuery(portletDataContext);
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Updates the comment in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommentLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param comment the comment
	 * @return the comment that was updated
	 */
	public static svc.ntuc.nlh.comments.model.Comment updateComment(
		svc.ntuc.nlh.comments.model.Comment comment) {

		return getService().updateComment(comment);
	}

	public static svc.ntuc.nlh.comments.model.Comment updateComment(
			long commentId, String commentText, String commentName,
			String commentEmail, boolean commentOption, boolean commentStatus,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateComment(
			commentId, commentText, commentName, commentEmail, commentOption,
			commentStatus, serviceContext);
	}

	public static CommentLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<CommentLocalService, CommentLocalService>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(CommentLocalService.class);

		ServiceTracker<CommentLocalService, CommentLocalService>
			serviceTracker =
				new ServiceTracker<CommentLocalService, CommentLocalService>(
					bundle.getBundleContext(), CommentLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}