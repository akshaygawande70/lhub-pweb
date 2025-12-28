package com.ntuc.notification.portlet.portlet;

/**
 * Pure builder that maps an AuditLogDetailsSource into a UI-safe view model.
 *
 * IMPORTANT:
 * - No OSGi annotations.
 * - No Liferay services.
 * - Best-effort mapping (null-safe).
 */
public final class AuditLogDetailsViewModelBuilder {

    private AuditLogDetailsViewModelBuilder() {
        // util
    }

    public static AuditLogDetailsViewModel build(AuditLogDetailsSource src, AuditLogBlobExtractor blobExtractor) {
        if (src == null) {
            return new AuditLogDetailsViewModel(
                0L, 0L, "", "", "", "", "", "",
                "", "", "", "",
                "", ""
            );
        }

        AuditLogBlobExtractor be = (blobExtractor == null)
            ? blob -> ""
            : blobExtractor;

        String detailsJson = safe(be.toText(src.getDetailsJson()));
        String stackTraceTruncated = safe(be.toText(src.getStackTraceTruncated()));

        return new AuditLogDetailsViewModel(
            src.getAuditLogId(),
            src.getNtucDTId(),
            safe(src.getCourseCode()),
            safe(src.getSeverity()),
            safe(src.getStatus()),
            safe(src.getStep()),
            safe(src.getCategory()),
            safe(src.getMessage()),
            safe(src.getErrorCode()),
            safe(src.getErrorMessage()),
            safe(src.getExceptionClass()),
            safe(src.getStackTraceHash()),
            detailsJson,
            stackTraceTruncated
        );
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }
}
