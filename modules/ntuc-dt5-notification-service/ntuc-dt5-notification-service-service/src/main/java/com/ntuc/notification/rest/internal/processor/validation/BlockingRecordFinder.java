package com.ntuc.notification.rest.internal.processor.validation;

import com.ntuc.notification.constants.NotificationType;
import com.ntuc.notification.constants.ProcessingStatusConstants;
import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.service.NtucSBLocalService;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Determines whether a new event should be blocked due to an existing "PROCESSING" record.
 *
 * <p><b>Unit-test target:</b>
 * Use {@link #findBlockingRecord(long, CourseEvent, BlockingRecordLookup)} in tests (pure, no Liferay types).
 *
 * <p><b>Production usage:</b>
 * Call {@link #findBlockingRecord(long, CourseEvent, NtucSBLocalService)} to adapt ServiceBuilder types at the edge.
 */
public final class BlockingRecordFinder {

    private BlockingRecordFinder() {
    }

    /**
     * Pure overload (preferred for unit tests).
     *
     * @param currentDtId current NtucDTId for this flow (must not block itself)
     * @param event incoming event (may be null; treated as "no blocking")
     * @param lookup pure lookup abstraction
     * @return optional blocking record if a different record is still PROCESSING
     */
    public static Optional<BlockingRecordView> findBlockingRecord(
        long currentDtId, CourseEvent event, BlockingRecordLookup lookup) {

        if (event == null || lookup == null) {
            return Optional.empty();
        }

        final String eventType = safeTrim(event.getEventType());
        final String courseCode = safeTrim(event.getCourseCodeSingle());

        final Predicate<BlockingRecordView> excludeCurrent =
            record -> record != null && record.getNtucDTId() != currentDtId;

        final Predicate<String> isInProgress =
            status -> status != null && ProcessingStatusConstants.PROCESSING.equalsIgnoreCase(status);

        if (NotificationType.PUBLISHED.equalsIgnoreCase(eventType)) {
            return lookup.fetchLatestByCourseAndEvent(courseCode, eventType, currentDtId)
                .filter(Objects::nonNull)
                .filter(excludeCurrent)
                .filter(r -> isInProgress.test(r.getProcessingStatus()));
        }

        if (NotificationType.CHANGED.equalsIgnoreCase(eventType)) {
            final Predicate<BlockingRecordView> validBlocking =
                excludeCurrent.and(r -> isInProgress.test(r.getProcessingStatus()));

            return Optional.ofNullable(event.getChangeFrom())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(BlockingRecordFinder::safeTrim)
                .filter(s -> !s.isEmpty())
                .map(changeFrom -> lookup.fetchLatestByCourseEventAndChangeFrom(courseCode, eventType, changeFrom, currentDtId)
                    .filter(Objects::nonNull)
                    .filter(validBlocking))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
        }

        return Optional.empty();
    }

    /**
     * Production convenience overload (keeps existing call sites compiling).
     * This overload is NOT recommended for unit tests.
     */
    public static Optional<BlockingRecordView> findBlockingRecord(
        long currentDtId, CourseEvent event, NtucSBLocalService ntucSBLocalService) {

        return findBlockingRecord(currentDtId, event, new NtucSBBlockingRecordLookup(ntucSBLocalService));
    }

    private static String safeTrim(String s) {
        return (s == null) ? "" : s.trim();
    }
}
