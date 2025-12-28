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

package com.ntuc.notification.service;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
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

import com.ntuc.notification.model.CourseSchedule;
import com.ntuc.notification.model.ScheduleResponse;

import java.io.Serializable;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the local service interface for CourseSchedule. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Brian Wing Shun Chan
 * @see CourseScheduleLocalServiceUtil
 * @generated
 */
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface CourseScheduleLocalService
	extends BaseLocalService, PersistedModelLocalService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add custom service methods to <code>com.ntuc.notification.service.impl.CourseScheduleLocalServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface. Consume the course schedule local service via injection or a <code>org.osgi.util.tracker.ServiceTracker</code>. Use {@link CourseScheduleLocalServiceUtil} if injection and service tracking are not available.
	 */

	/**
	 * Adds the course schedule to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CourseScheduleLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param courseSchedule the course schedule
	 * @return the course schedule that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	public CourseSchedule addCourseSchedule(CourseSchedule courseSchedule);

	/**
	 * Creates a new course schedule with the primary key. Does not add the course schedule to the database.
	 *
	 * @param courseScheduleId the primary key for the new course schedule
	 * @return the new course schedule
	 */
	@Transactional(enabled = false)
	public CourseSchedule createCourseSchedule(long courseScheduleId);

	/**
	 * @throws PortalException
	 */
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

	/**
	 * Deletes the course schedule from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CourseScheduleLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param courseSchedule the course schedule
	 * @return the course schedule that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	public CourseSchedule deleteCourseSchedule(CourseSchedule courseSchedule);

	/**
	 * Deletes the course schedule with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CourseScheduleLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param courseScheduleId the primary key of the course schedule
	 * @return the course schedule that was removed
	 * @throws PortalException if a course schedule with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	public CourseSchedule deleteCourseSchedule(long courseScheduleId)
		throws PortalException;

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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.ntuc.notification.model.impl.CourseScheduleModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.ntuc.notification.model.impl.CourseScheduleModelImpl</code>.
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

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CourseSchedule fetchByCourseCode(String courseCode);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CourseSchedule fetchCourseSchedule(long courseScheduleId);

	/**
	 * Returns the course schedule matching the UUID and group.
	 *
	 * @param uuid the course schedule's UUID
	 * @param groupId the primary key of the group
	 * @return the matching course schedule, or <code>null</code> if a matching course schedule could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CourseSchedule fetchCourseScheduleByUuidAndGroupId(
		String uuid, long groupId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ActionableDynamicQuery getActionableDynamicQuery();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<CourseSchedule> getAllCourseSchedules();

	/**
	 * Returns the course schedule with the primary key.
	 *
	 * @param courseScheduleId the primary key of the course schedule
	 * @return the course schedule
	 * @throws PortalException if a course schedule with the primary key could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CourseSchedule getCourseSchedule(long courseScheduleId)
		throws PortalException;

	/**
	 * Returns the course schedule matching the UUID and group.
	 *
	 * @param uuid the course schedule's UUID
	 * @param groupId the primary key of the group
	 * @return the matching course schedule
	 * @throws PortalException if a matching course schedule could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CourseSchedule getCourseScheduleByUuidAndGroupId(
			String uuid, long groupId)
		throws PortalException;

	/**
	 * Returns a range of all the course schedules.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.ntuc.notification.model.impl.CourseScheduleModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of course schedules
	 * @param end the upper bound of the range of course schedules (not inclusive)
	 * @return the range of course schedules
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<CourseSchedule> getCourseSchedules(int start, int end);

	/**
	 * Returns all the course schedules matching the UUID and company.
	 *
	 * @param uuid the UUID of the course schedules
	 * @param companyId the primary key of the company
	 * @return the matching course schedules, or an empty list if no matches were found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<CourseSchedule> getCourseSchedulesByUuidAndCompanyId(
		String uuid, long companyId);

	/**
	 * Returns a range of course schedules matching the UUID and company.
	 *
	 * @param uuid the UUID of the course schedules
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of course schedules
	 * @param end the upper bound of the range of course schedules (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching course schedules, or an empty list if no matches were found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<CourseSchedule> getCourseSchedulesByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<CourseSchedule> orderByComparator);

	/**
	 * Returns the number of course schedules.
	 *
	 * @return the number of course schedules
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getCourseSchedulesCount();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		PortletDataContext portletDataContext);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery();

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
	public String getScheduleForCourse(
			String courseCode, ServiceContext serviceContext)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ScheduleResponse getScheduleSnapshot(String courseCode);

	/**
	 * Updates the course schedule in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CourseScheduleLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param courseSchedule the course schedule
	 * @return the course schedule that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	public CourseSchedule updateCourseSchedule(CourseSchedule courseSchedule);

	public CourseSchedule updateScheduleFromAdmin(
			long courseScheduleId, String intakeNumber, String startDateString,
			String endDateString, Integer availability, String venue,
			Integer durationHours, Integer durationMinutes, String availablePax,
			String availableWaitlist, String lxpBuyUrl,
			String scheduleDownloadUrl, String errorCode, String errorMessage,
			ServiceContext serviceContext)
		throws PortalException;

	public CourseSchedule upsertFromCls(
			String courseCode, ScheduleResponse dto,
			ServiceContext serviceContext, String rawJson)
		throws PortalException;

}