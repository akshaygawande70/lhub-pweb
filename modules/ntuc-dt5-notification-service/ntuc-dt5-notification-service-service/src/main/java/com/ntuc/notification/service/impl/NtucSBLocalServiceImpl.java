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

package com.ntuc.notification.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.ntuc.notification.constants.NotificationType;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.service.base.NtucSBLocalServiceBaseImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;

/**
 * The implementation of the ntuc sb local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * <code>com.ntuc.notification.service.NtucSBLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see NtucSBLocalServiceBaseImpl
 */
@Component(property = "model.class.name=com.ntuc.notification.model.NtucSB", service = AopService.class)
public class NtucSBLocalServiceImpl extends NtucSBLocalServiceBaseImpl {

	@Override
	public void deleteByDateRange(Date fromDate, Date toDate) {
		System.out.println("From Date => " + fromDate + "To date " + toDate);
		System.out.println("In delete method \n");
		DynamicQuery query = DynamicQueryFactoryUtil.forClass(NtucSB.class, getClassLoader())
				.add(RestrictionsFactoryUtil.ge("notificationDate", fromDate)).add(RestrictionsFactoryUtil.le("notificationDate", toDate));

		List<NtucSB> entries = dynamicQuery(query);
		System.out.println("Entries count : " + entries.size());
		for (NtucSB entry : entries) {
			System.out.println("entry : " + entry);
			deleteNtucSB(entry);
		}
	}
	
	
	@Override
	public List<NtucSB> getUnprocessedCronRecords(String status) {
	    return ntucSBPersistence.findByStatusAndIsCronProcessed(NotificationType.CHANGED, false);
	}
	
	
	@Override
	public List<NtucSB> getRecordsByChangeFrom(String changeFrom) {
	    return ntucSBPersistence.findByRecordsByChangeFrom(changeFrom);
	}


	/**
     * Fetch the latest (by systemDate DESC) record for courseCode + event,
     * excluding the current ntucDTId. Uses Service Builder finder.
     */
    public Optional<NtucSB> fetchLatestByCourseAndEvent(
            String courseCode, String event, long excludeNtucDTId) {

        OrderByComparator<NtucSB> obc =
            OrderByComparatorFactoryUtil.create(
                "ntuc_service_dt_notification", "systemDate", false /* desc */);

        List<NtucSB> top = ntucSBPersistence.findByCourseCodeAndEvent(
                courseCode, event, 0, 5, obc);

        return top.stream()
                  .filter(r -> r.getNtucDTId() != excludeNtucDTId)
                  .findFirst();
    }

    /**
     * Fetch the latest (by systemDate DESC) record for courseCode + event + changeType,
     * where changeFrom is a JSON-array string. We match using: changeFrom LIKE '%"type"%' .
     * Excludes the current ntucDTId.
     */
    public Optional<NtucSB> fetchLatestByCourseEventAndChangeFrom(
            String courseCode, String event, String changeType, long excludeNtucDTId) {

        // Build LIKE pattern that matches the quoted token inside your JSON-array string
        final String likePattern = "%\"" + changeType + "\"%";

        List<NtucSB> top = ntucSBPersistence.findByCourseCodeEventAndChangeFrom(
                courseCode, event, likePattern, 0, 5, null);

        return top.stream()
                  .filter(r -> r.getNtucDTId() != excludeNtucDTId)
                  .findFirst();
    }


	@Override
	public List<NtucSB> fetchByIsRowLockFailed(boolean isRowLockFailed) {
		return ntucSBPersistence.findByisRowLockFailed(isRowLockFailed);
	}

	 @Override
    public NtucSB addNtucSB(NtucSB ntucSB) {
        forceRetryable(ntucSB);
        return super.addNtucSB(ntucSB);
    }

    @Override
    public NtucSB updateNtucSB(NtucSB ntucSB) {
        forceRetryable(ntucSB);
        return super.updateNtucSB(ntucSB);
    }

    private static void forceRetryable(NtucSB ntucSB) {
        if (ntucSB != null) {
            ntucSB.setCanRetry(true);
        }
    }

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use
	 * <code>com.ntuc.notification.service.NtucSBLocalService</code> via injection
	 * or a <code>org.osgi.util.tracker.ServiceTracker</code> or use
	 * <code>com.ntuc.notification.service.NtucSBLocalServiceUtil</code>.
	 */

}