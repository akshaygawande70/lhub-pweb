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

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.osgi.annotation.versioning.ProviderType;

import svc.ntuc.nlh.course.admin.exception.NoSuchCourseException;
import svc.ntuc.nlh.course.admin.model.Course;

/**
 * Provides the local service interface for Course. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Brian Wing Shun Chan
 * @see CourseLocalServiceUtil
 * @generated
 */
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface CourseLocalService
	extends BaseLocalService, PersistedModelLocalService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add custom service methods to <code>svc.ntuc.nlh.course.admin.service.impl.CourseLocalServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface. Consume the course local service via injection or a <code>org.osgi.util.tracker.ServiceTracker</code>. Use {@link CourseLocalServiceUtil} if injection and service tracking are not available.
	 */

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
	@Indexable(type = IndexableType.REINDEX)
	public Course addCourse(Course course);

	public Course addCourse(
			long groupId, Date endDate, String venue,
			boolean allowOnlinePayment, String courseTitle,
			boolean allowWebRegistration, String description, int availability,
			String batchId, Date webExpiry, boolean fundedCourseFlag,
			String courseCode, double courseDuration, Date startDate,
			double courseFee, String courseType, boolean deleted,
			boolean updated, boolean popular)
		throws PortalException;

	/**
	 * Creates a new course with the primary key. Does not add the course to the database.
	 *
	 * @param courseId the primary key for the new course
	 * @return the new course
	 */
	@Transactional(enabled = false)
	public Course createCourse(long courseId);

	/**
	 * @throws PortalException
	 */
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

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
	@Indexable(type = IndexableType.DELETE)
	public Course deleteCourse(Course course);

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
	@Indexable(type = IndexableType.DELETE)
	public Course deleteCourse(long courseId) throws PortalException;

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public DynamicQuery dynamicQuery();

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery);

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end);

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator);

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long dynamicQueryCount(DynamicQuery dynamicQuery);

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection);

	public void executeMoveCourse(
		JournalArticle article, long siteGroupId,
		List<DDMStructure> ddmStructures, List<DDMTemplate> ddmTemplates,
		long userId, ServiceContext serviceContext);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Course fetchCourse(long courseId);

	/**
	 * Returns the course matching the UUID and group.
	 *
	 * @param uuid the course's UUID
	 * @param groupId the primary key of the group
	 * @return the matching course, or <code>null</code> if a matching course could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Course fetchCourseByUuidAndGroupId(String uuid, long groupId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ActionableDynamicQuery getActionableDynamicQuery();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Course> getAllActiveAndPopularCourse(
		long groupId, boolean deleted, boolean popular);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Course> getAllActiveAndUpdatedCourse(
		long groupId, boolean deleted, boolean updated);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Course> getAllActiveCourse(long groupId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long getArticleFolderByThemeName(
		JournalArticle article, long groupId, long userId);

	/**
	 * Returns the course with the primary key.
	 *
	 * @param courseId the primary key of the course
	 * @return the course
	 * @throws PortalException if a course with the primary key could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Course getCourse(long courseId) throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Course> getCourseByCourseCode(
		long groupId, String courseCode, boolean deleted);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Course> getCourseByCourseCode(
		long groupId, String courseCode, boolean deleted, int start, int end);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Course> getCourseByCourseCode(
		long groupId, String courseCode, boolean deleted, int start, int end,
		OrderByComparator<Course> orderByComparator);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Course getCourseByCourseCodeAndBatchId(
			long groupId, String courseCode, String batchId)
		throws NoSuchCourseException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Course getCourseByCourseCodeAndBatchIdActive(
			long groupId, String courseCode, String batchId, boolean deleted)
		throws NoSuchCourseException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Course> getCourseByCourseTitle(
		long groupId, String courseTitle, boolean deleted);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Course> getCourseByCourseTitle(
		long groupId, String courseTitle, boolean deleted, int start, int end);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Course> getCourseByCourseTitle(
		long groupId, String courseTitle, boolean deleted, int start, int end,
		OrderByComparator<Course> orderByComparator);

	/**
	 * Returns the course matching the UUID and group.
	 *
	 * @param uuid the course's UUID
	 * @param groupId the primary key of the group
	 * @return the matching course
	 * @throws PortalException if a matching course could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Course getCourseByUuidAndGroupId(String uuid, long groupId)
		throws PortalException;

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Course> getCourses(int start, int end);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Course> getCoursesByKeywords(
		long groupId, String keywords, int start, int end,
		OrderByComparator<Course> orderByComparator);

	/**
	 * Returns all the courses matching the UUID and company.
	 *
	 * @param uuid the UUID of the courses
	 * @param companyId the primary key of the company
	 * @return the matching courses, or an empty list if no matches were found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Course> getCoursesByUuidAndCompanyId(
		String uuid, long companyId);

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Course> getCoursesByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Course> orderByComparator);

	/**
	 * Returns the number of courses.
	 *
	 * @return the number of courses
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getCoursesCount();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long getCoursesCountByKeywords(long groupId, String keywords);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		PortletDataContext portletDataContext);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<JSONObject> getLatestJournalByCourseCode(
		long groupId, String structureKey, String templateKey);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<JSONObject> getLatestJournalByCourseCode(
		long groupId, String courseCode, String structureKey,
		String templateKey);

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

	/**
	 * @throws PortalException
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Set<String> getPopularArticles(
		long resourcePrimaryKey, long groupId, String ddmStructureKey,
		String ddmTemplateKey);

	public void moveSpecifiedApprovedCourse(long resourcePrimKey);

	public void setRelatedAsset(
		long groupId, long userId, String currentArticleId,
		Set<String> targetArticleIds);

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
	@Indexable(type = IndexableType.REINDEX)
	public Course updateCourse(Course course);

	public Course updateCourse(
			long courseId, Date endDate, String venue,
			boolean allowOnlinePayment, String courseTitle,
			boolean allowWebRegistration, String description, int availability,
			String batchId, Date webExpiry, boolean fundedCourseFlag,
			String courseCode, double courseDuration, Date startDate,
			double courseFee, String courseType, boolean deleted,
			boolean updated, boolean popular)
		throws PortalException;

	public void updateCoursesFlagFromPopular(
		long groupId, String ddmStructureKey, String ddmTemplateKey);

}