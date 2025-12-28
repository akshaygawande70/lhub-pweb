package com.ntuc.notification.rest.internal.processor.validation;

import java.io.Serializable;

/**
 * Immutable view of a potentially blocking record.
 *
 * <p>Pure DTO:
 * - No Liferay kernel dependencies
 * - Safe for unit tests
 * - Used by BlockingRecordFinder only
 */
public final class BlockingRecordView implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long ntucDTId;
    private final String processingStatus;

    public BlockingRecordView(long ntucDTId, String processingStatus) {
        this.ntucDTId = ntucDTId;
        this.processingStatus = processingStatus;
    }

    public long getNtucDTId() {
        return ntucDTId;
    }

    public String getProcessingStatus() {
        return processingStatus;
    }
}
