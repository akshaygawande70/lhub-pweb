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

package svc.ntuc.nlh.course.admin.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link CourseLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see CourseLocalService
 * @generated
 */
public class CourseLocalServiceWrapper
	implements CourseLocalService, ServiceWrapper<CourseLocalService> {

	public CourseLocalServiceWrapper(CourseLocalService courseLocalService) {
		_courseLocalService = courseLocalService;
	}

	/**
	 * Adds the course to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CourseLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param course the course
	 * @return the course that was added
	 */
	@Override
	public svc.ntuc.nlh.course.admin.model.Course addCourse(
		svc.ntuc.nlh.course.admin.model.Course course) {

		return _courseLocalService.addCourse(course);
	}

	@Override
	public svc.ntuc.nlh.course.admin.model.Course addCourse(
			long groupId, java.util.Date endDate, String venue,
			boolean allowOnlinePayment, String courseTitle,
			boolean allowWebRegistration, String description, int availability,
			String batchId, java.util.Date webExpiry, boolean fundedCourseFlag,
			String courseCode, double courseDuration, java.util.Date startDate,
			double courseFee, String courseType, boolean deleted,
			boolean updated, boolean popular)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseLocalService.addCourse(
			groupId, endDate, venue, allowOnlinePayment, courseTitle,
			allowWebRegistration, description, availability, batchId, webExpiry,
			fundedCourseFlag, courseCode, courseDuration, startDate, courseFee,
			courseType, deleted, updated, popular);
	}

	/**
	 * Creates a new course with the primary key. Does not add the course to the database.
	 *
	 * @param courseId the primary key for the new course
	 * @return the new course
	 */
	@Override
	public svc.ntuc.nlh.course.admin.model.Course createCourse(long courseId) {
		return _courseLocalService.createCourse(courseId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseLocalService.createPersistedModel(primaryKeyObj);
	}

	/**
	 * Deletes the course from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CourseLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param course the course
	 * @return the course that was removed
	 */
	@Override
	public svc.ntuc.nlh.course.admin.model.Course deleteCourse(
		svc.ntuc.nlh.course.admin.model.Course course) {

		return _courseLocalService.deleteCourse(course);
	}

	/**
	 * Deletes the course with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CourseLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param courseId the primary key of the course
	 * @return the course that was removed
	 * @throws PortalException if a course with the primary key could not be found
	 */
	@Override
	public svc.ntuc.nlh.course.admin.model.Course deleteCourse(long courseId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseLocalService.deleteCourse(courseId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _courseLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _courseLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.course.admin.model.impl.CourseModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _courseLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.course.admin.model.impl.CourseModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _courseLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _courseLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _courseLocalService.dynamicQueryCount(dynamicQuery, projection);
	}

	@Override
	public void executeMoveCourse(
		com.liferay.journal.model.JournalArticle article, long siteGroupId,
		java.util.List<com.liferay.dynamic.data.mapping.model.DDMStructure>
			ddmStructures,
		java.util.List<com.liferay.dynamic.data.mapping.model.DDMTemplate>
			ddmTemplates,
		long userId,
		com.liferay.portal.kernel.service.ServiceContext serviceContext) {

		_courseLocalService.executeMoveCourse(
			article, siteGroupId, ddmStructures, ddmTemplates, userId,
			serviceContext);
	}

	@Override
	public svc.ntuc.nlh.course.admin.model.Course fetchCourse(long courseId) {
		return _courseLocalService.fetchCourse(courseId);
	}

	/**
	 * Returns the course matching the UUID and group.
	 *
	 * @param uuid the course's UUID
	 * @param groupId the primary key of the group
	 * @return the matching course, or <code>null</code> if a matching course could not be found
	 */
	@Override
	public svc.ntuc.nlh.course.admin.model.Course fetchCourseByUuidAndGroupId(
		String uuid, long groupId) {

		return _courseLocalService.fetchCourseByUuidAndGroupId(uuid, groupId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _courseLocalService.getActionableDynamicQuery();
	}

	@Override
	public java.util.List<svc.ntuc.nlh.course.admin.model.Course>
		getAllActiveAndPopularCourse(
			long groupId, boolean deleted, boolean popular) {

		return _courseLocalService.getAllActiveAndPopularCourse(
			groupId, deleted, popular);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.course.admin.model.Course>
		getAllActiveAndUpdatedCourse(
			long groupId, boolean deleted, boolean updated) {

		return _courseLocalService.getAllActiveAndUpdatedCourse(
			groupId, deleted, updated);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.course.admin.model.Course>
		getAllActiveCourse(long groupId) {

		return _courseLocalService.getAllActiveCourse(groupId);
	}

	@Override
	public long getArticleFolderByThemeName(
		com.liferay.journal.model.JournalArticle article, long groupId,
		long userId) {

		return _courseLocalService.getArticleFolderByThemeName(
			article, groupId, userId);
	}

	/**
	 * Returns the course with the primary key.
	 *
	 * @param courseId the primary key of the course
	 * @return the course
	 * @throws PortalException if a course with the primary key could not be found
	 */
	@Override
	public svc.ntuc.nlh.course.admin.model.Course getCourse(long courseId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseLocalService.getCourse(courseId);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.course.admin.model.Course>
		getCourseByCourseCode(
			long groupId, String courseCode, boolean deleted) {

		return _courseLocalService.getCourseByCourseCode(
			groupId, courseCode, deleted);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.course.admin.model.Course>
		getCourseByCourseCode(
			long groupId, String courseCode, boolean deleted, int start,
			int end) {

		return _courseLocalService.getCourseByCourseCode(
			groupId, courseCode, deleted, start, end);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.course.admin.model.Course>
		getCourseByCourseCode(
			long groupId, String courseCode, boolean deleted, int start,
			int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<svc.ntuc.nlh.course.admin.model.Course> orderByComparator) {

		return _courseLocalService.getCourseByCourseCode(
			groupId, courseCode, deleted, start, end, orderByComparator);
	}

	@Override
	public svc.ntuc.nlh.course.admin.model.Course
			getCourseByCourseCodeAndBatchId(
				long groupId, String courseCode, String batchId)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return _courseLocalService.getCourseByCourseCodeAndBatchId(
			groupId, courseCode, batchId);
	}

	@Override
	public svc.ntuc.nlh.course.admin.model.Course
			getCourseByCourseCodeAndBatchIdActive(
				long groupId, String courseCode, String batchId,
				boolean deleted)
		throws svc.ntuc.nlh.course.admin.exception.NoSuchCourseException {

		return _courseLocalService.getCourseByCourseCodeAndBatchIdActive(
			groupId, courseCode, batchId, deleted);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.course.admin.model.Course>
		getCourseByCourseTitle(
			long groupId, String courseTitle, boolean deleted) {

		return _courseLocalService.getCourseByCourseTitle(
			groupId, courseTitle, deleted);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.course.admin.model.Course>
		getCourseByCourseTitle(
			long groupId, String courseTitle, boolean deleted, int start,
			int end) {

		return _courseLocalService.getCourseByCourseTitle(
			groupId, courseTitle, deleted, start, end);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.course.admin.model.Course>
		getCourseByCourseTitle(
			long groupId, String courseTitle, boolean deleted, int start,
			int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<svc.ntuc.nlh.course.admin.model.Course> orderByComparator) {

		return _courseLocalService.getCourseByCourseTitle(
			groupId, courseTitle, deleted, start, end, orderByComparator);
	}

	/**
	 * Returns the course matching the UUID and group.
	 *
	 * @param uuid the course's UUID
	 * @param groupId the primary key of the group
	 * @return the matching course
	 * @throws PortalException if a matching course could not be found
	 */
	@Override
	public svc.ntuc.nlh.course.admin.model.Course getCourseByUuidAndGroupId(
			String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseLocalService.getCourseByUuidAndGroupId(uuid, groupId);
	}

	/**
	 * Returns a range of all the courses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>svc.ntuc.nlh.course.admin.model.impl.CourseModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @return the range of courses
	 */
	@Override
	public java.util.List<svc.ntuc.nlh.course.admin.model.Course> getCourses(
		int start, int end) {

		return _courseLocalService.getCourses(start, end);
	}

	@Override
	public java.util.List<svc.ntuc.nlh.course.admin.model.Course>
		getCoursesByKeywords(
			long groupId, String keywords, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<svc.ntuc.nlh.course.admin.model.Course> orderByComparator) {

		return _courseLocalService.getCoursesByKeywords(
			groupId, keywords, start, end, orderByComparator);
	}

	/**
	 * Returns all the courses matching the UUID and company.
	 *
	 * @param uuid the UUID of the courses
	 * @param companyId the primary key of the company
	 * @return the matching courses, or an empty list if no matches were found
	 */
	@Override
	public java.util.List<svc.ntuc.nlh.course.admin.model.Course>
		getCoursesByUuidAndCompanyId(String uuid, long companyId) {

		return _courseLocalService.getCoursesByUuidAndCompanyId(
			uuid, companyId);
	}

	/**
	 * Returns a range of courses matching the UUID and company.
	 *
	 * @param uuid the UUID of the courses
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of courses
	 * @param end the upper bound of the range of courses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching courses, or an empty list if no matches were found
	 */
	@Override
	public java.util.List<svc.ntuc.nlh.course.admin.model.Course>
		getCoursesByUuidAndCompanyId(
			String uuid, long companyId, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<svc.ntuc.nlh.course.admin.model.Course> orderByComparator) {

		return _courseLocalService.getCoursesByUuidAndCompanyId(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the number of courses.
	 *
	 * @return the number of courses
	 */
	@Override
	public int getCoursesCount() {
		return _courseLocalService.getCoursesCount();
	}

	@Override
	public long getCoursesCountByKeywords(long groupId, String keywords) {
		return _courseLocalService.getCoursesCountByKeywords(groupId, keywords);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return _courseLocalService.getExportActionableDynamicQuery(
			portletDataContext);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _courseLocalService.getIndexableActionableDynamicQuery();
	}

	@Override
	public java.util.List<com.liferay.portal.kernel.json.JSONObject>
		getLatestJournalByCourseCode(
			long groupId, String structureKey, String templateKey) {

		return _courseLocalService.getLatestJournalByCourseCode(
			groupId, structureKey, templateKey);
	}

	@Override
	public java.util.List<com.liferay.portal.kernel.json.JSONObject>
		getLatestJournalByCourseCode(
			long groupId, String courseCode, String structureKey,
			String templateKey) {

		return _courseLocalService.getLatestJournalByCourseCode(
			groupId, courseCode, structureKey, templateKey);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _courseLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseLocalService.getPersistedModel(primaryKeyObj);
	}

	@Override
	public java.util.Set<String> getPopularArticles(
		long resourcePrimaryKey, long groupId, String ddmStructureKey,
		String ddmTemplateKey) {

		return _courseLocalService.getPopularArticles(
			resourcePrimaryKey, groupId, ddmStructureKey, ddmTemplateKey);
	}

	@Override
	public void moveSpecifiedApprovedCourse(long resourcePrimKey) {
		_courseLocalService.moveSpecifiedApprovedCourse(resourcePrimKey);
	}

	@Override
	public void setRelatedAsset(
		long groupId, long userId, String currentArticleId,
		java.util.Set<String> targetArticleIds) {

		_courseLocalService.setRelatedAsset(
			groupId, userId, currentArticleId, targetArticleIds);
	}

	/**
	 * Updates the course in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CourseLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param course the course
	 * @return the course that was updated
	 */
	@Override
	public svc.ntuc.nlh.course.admin.model.Course updateCourse(
		svc.ntuc.nlh.course.admin.model.Course course) {

		return _courseLocalService.updateCourse(course);
	}

	@Override
	public svc.ntuc.nlh.course.admin.model.Course updateCourse(
			long courseId, java.util.Date endDate, String venue,
			boolean allowOnlinePayment, String courseTitle,
			boolean allowWebRegistration, String description, int availability,
			String batchId, java.util.Date webExpiry, boolean fundedCourseFlag,
			String courseCode, double courseDuration, java.util.Date startDate,
			double courseFee, String courseType, boolean deleted,
			boolean updated, boolean popular)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _courseLocalService.updateCourse(
			courseId, endDate, venue, allowOnlinePayment, courseTitle,
			allowWebRegistration, description, availability, batchId, webExpiry,
			fundedCourseFlag, courseCode, courseDuration, startDate, courseFee,
			courseType, deleted, updated, popular);
	}

	@Override
	public void updateCoursesFlagFromPopular(
		long groupId, String ddmStructureKey, String ddmTemplateKey) {

		_courseLocalService.updateCoursesFlagFromPopular(
			groupId, ddmStructureKey, ddmTemplateKey);
	}

	@Override
	public CourseLocalService getWrappedService() {
		return _courseLocalService;
	}

	@Override
	public void setWrappedService(CourseLocalService courseLocalService) {
		_courseLocalService = courseLocalService;
	}

	private CourseLocalService _courseLocalService;

}