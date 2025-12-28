package com.ntuc.notification.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;

/**
 * FTL-facing gateway for CLS "dummy" APIs
 *
 * Contract:
 * - No DB writes.
 * - No transformation logic (raw response JSON returned).
 * - Audited via AuditEventWriter so Ops can reconstruct timeline from audit DB alone.
 *
 */
public interface ClsDummyService {

    /**
     * Single FTL-friendly method.
     *
     * @param kind Either "COURSES" or "SUBSCRIPTIONS" (case-insensitive).
     * @param courseCode CLS course code
     * @param serviceContext optional; may be null (thread-local fallback)
     * @return raw JSON string from CLS dummy endpoint; null on validation failure / CLS failure
     */
    String getDummyJson(String kind, String courseCode, ServiceContext serviceContext) throws PortalException;

    /**
     * Convenience wrapper for courses
     */
    String getCoursesDummyJson(String courseCode, ServiceContext serviceContext) throws PortalException;

    /**
     * Convenience wrapper for subscriptions.
     */
    String getSubscriptionsDummyJson(String courseCode, ServiceContext serviceContext) throws PortalException;
}
