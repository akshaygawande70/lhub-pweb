package com.ntuc.notification.service.api.dto;

import java.io.Serializable;

/**
 * Result of CRITICAL phase that can be passed to NON-CRITICAL to avoid extra CLS call.
 *
 * Notes:
 * - Immutable DTO (API-safe).
 * - payloadJson is typically JSON of CourseResponse (mapped object), not raw CLS HTTP body.
 */
public final class CriticalProcessingResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String journalArticleUuid;
    private final long journalArticleId;
    private final String payloadJson;

    public CriticalProcessingResult(String journalArticleUuid, long journalArticleId, String payloadJson) {
        this.journalArticleUuid = (journalArticleUuid == null) ? "" : journalArticleUuid;
        this.journalArticleId = journalArticleId;
        this.payloadJson = (payloadJson == null) ? "" : payloadJson;
    }

    public String getJournalArticleUuid() {
        return journalArticleUuid;
    }

    public long getJournalArticleId() {
        return journalArticleId;
    }

    public String getPayloadJson() {
        return payloadJson;
    }

    public boolean hasPayload() {
        return payloadJson != null && !payloadJson.trim().isEmpty();
    }
}
