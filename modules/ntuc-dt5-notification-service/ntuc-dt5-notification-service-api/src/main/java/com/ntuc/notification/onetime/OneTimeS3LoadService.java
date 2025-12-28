package com.ntuc.notification.onetime;

import com.liferay.portal.kernel.exception.PortalException;

import org.osgi.annotation.versioning.ProviderType;

/**
 * One-time S3 loader (admin utility).
 *
 * Rules:
 * - Must never log secrets.
 * - AuditLog DB is the single source of truth (no ops reliance on server logs).
 * - Implementation must use persisted AuditEvent records for observability.
 */
@ProviderType
public interface OneTimeS3LoadService {

    /**
     * Executes one-time load by iterating all JSON files in the given bucket/prefix,
     * processing only those with courseCatalogueStatus in {published, inactive}.
     *
     * @param bucket S3 bucket name (required)
     * @param prefix optional key prefix (may be null/empty)
     * @throws PortalException on Liferay-side failures (e.g., user/group/template resolution)
     */
    void execute(String bucket, String prefix) throws PortalException;
}
