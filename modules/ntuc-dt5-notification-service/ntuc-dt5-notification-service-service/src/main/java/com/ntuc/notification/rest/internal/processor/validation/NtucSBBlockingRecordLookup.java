package com.ntuc.notification.rest.internal.processor.validation;

import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.service.NtucSBLocalService;

import java.util.Optional;

/**
 * Production adapter that converts NtucSB ServiceBuilder records
 * into pure {@link BlockingRecordView}.
 *
 * <p>NOT unit-tested.
 * <p>Used only at the service boundary.
 */
public class NtucSBBlockingRecordLookup implements BlockingRecordLookup {

    private final NtucSBLocalService ntucSBLocalService;

    public NtucSBBlockingRecordLookup(NtucSBLocalService ntucSBLocalService) {
        this.ntucSBLocalService = ntucSBLocalService;
    }

    @Override
    public Optional<BlockingRecordView> fetchLatestByCourseAndEvent(
        String courseCode, String eventType, long currentDtId) {

        return ntucSBLocalService
            .fetchLatestByCourseAndEvent(courseCode, eventType, currentDtId)
            .map(this::toView);
    }

    @Override
    public Optional<BlockingRecordView> fetchLatestByCourseEventAndChangeFrom(
        String courseCode, String eventType, String changeFrom, long currentDtId) {

        return ntucSBLocalService
            .fetchLatestByCourseEventAndChangeFrom(courseCode, eventType, changeFrom, currentDtId)
            .map(this::toView);
    }

    private BlockingRecordView toView(NtucSB sb) {
        return new BlockingRecordView(
            sb.getNtucDTId(),
            sb.getProcessingStatus()
        );
    }
}
