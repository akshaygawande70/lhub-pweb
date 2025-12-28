package com.ntuc.notification.audit.util;

import com.ntuc.notification.audit.api.constants.AuditConstants;

import java.util.Map;

import org.slf4j.MDC;

/**
 * MDC helpers for propagating corrId / ntucDTId into async tasks.
 *
 * Rules:
 * - MDC is thread-local
 * - corrId MUST be explicitly set at REST entry
 * - async execution MUST wrap Runnable via MdcUtil.wrap(...)
 */
public final class MdcUtil {

    private MdcUtil() {}

    /** Wrap a Runnable, propagating the current MDC (incl. corrId/ntucDTId). */
    public static Runnable wrap(Runnable delegate) {
        final Map<String, String> snapshot = MDC.getCopyOfContextMap();
        return () -> {
            Map<String, String> old = MDC.getCopyOfContextMap();
            try {
                if (snapshot != null) {
                    MDC.setContextMap(snapshot);
                } else {
                    MDC.clear();
                }
                delegate.run();
            }
            finally {
                if (old != null) {
                    MDC.setContextMap(old);
                } else {
                    MDC.clear();
                }
            }
        };
    }

    /** Current corrId from MDC, or null. */
    public static String getCorrId() {
        return MDC.get(AuditConstants.MDC_CORR_ID);
    }

    /** Explicitly set corrId into MDC. */
    public static void setCorrId(String corrId) {
        if (corrId == null || corrId.trim().isEmpty()) {
            MDC.remove(AuditConstants.MDC_CORR_ID);
        } else {
            MDC.put(AuditConstants.MDC_CORR_ID, corrId);
        }
    }

    /** Clear corrId from MDC. */
    public static void clearCorrId() {
        MDC.remove(AuditConstants.MDC_CORR_ID);
    }

    /** Current ntucDTId from MDC, or null. */
    public static Long getNtucDTId() {
        String s = MDC.get(AuditConstants.MDC_NTUC_DT_ID);
        if (s == null) return null;
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            return null;
        }
    }

    /** Explicitly set ntucDTId into MDC. */
    public static void setNtucDTId(Long ntucDTId) {
        if (ntucDTId == null || ntucDTId.longValue() <= 0) {
            MDC.remove(AuditConstants.MDC_NTUC_DT_ID);
        } else {
            MDC.put(AuditConstants.MDC_NTUC_DT_ID, String.valueOf(ntucDTId));
        }
    }

    /** Clear ntucDTId from MDC. */
    public static void clearNtucDTId() {
        MDC.remove(AuditConstants.MDC_NTUC_DT_ID);
    }
}
