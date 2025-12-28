package com.ntuc.notification.service;

import com.ntuc.notification.model.NtucSB;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Centralized updates for NtucSB flags + status mapping helpers.
 *
 * API contract only (no OSGi annotations here).
 */
@ProviderType
public interface NotificationUpdateHelper {

    enum Phase { CRITICAL, NON_CRITICAL, CRON, ALL }

    /**
     * Fetch persisted record (if present) and apply updates atomically.
     */
    void update(
            NtucSB base,
            Phase phase,
            boolean processed,
            boolean canRetry,
            String processingStatus,
            String courseStatus);

    /**
     * Prefer persisted record if present, otherwise return the provided record.
     */
    NtucSB fetchOr(NtucSB record);

    /**
     * Maps workflow status to NTUC course status.
     */
    String courseStatusForWorkflowStatus(int wfStatus);
}
