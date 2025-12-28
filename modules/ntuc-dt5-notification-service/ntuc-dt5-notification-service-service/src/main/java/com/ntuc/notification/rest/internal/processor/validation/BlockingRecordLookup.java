package com.ntuc.notification.rest.internal.processor.validation;

import java.util.Optional;

/**
 * Pure lookup abstraction for "in-progress" blocking records.
 *
 * Production implementations may adapt ServiceBuilder LocalServices,
 * but unit tests should mock this interface (no Liferay kernel required).
 */
public interface BlockingRecordLookup {

    Optional<BlockingRecordView> fetchLatestByCourseAndEvent(
        String courseCode, String eventType, long currentDtId);

    Optional<BlockingRecordView> fetchLatestByCourseEventAndChangeFrom(
        String courseCode, String eventType, String changeFrom, long currentDtId);
}
