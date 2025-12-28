package com.ntuc.notification.audit.util;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditRequestContext;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * Single source of truth for creating immutable {@link AuditEvent} instances.
 *
 * Rules:
 * - Null context is allowed and must be normalized to safe defaults.
 * - correlationId must always be present for traceability.
 * - stack traces must be truncated + hashed.
 *
 * Pure Java logic, safe for unit tests.
 */
public final class AuditEventFactory {

    private static final int STACKTRACE_MAX = 4000;

    private AuditEventFactory() {
        // util
    }

    public static AuditEvent start(
            AuditCategory category,
            AuditStep step,
            AuditSeverity severity,
            AuditRequestContext ctx,
            AuditStatus status,
            String message,
            AuditErrorCode errorCode
    ) {
        return start(category, step, severity, ctx, status, message, errorCode, null);
    }

    public static AuditEvent start(
            AuditCategory category,
            AuditStep step,
            AuditSeverity severity,
            AuditRequestContext ctx,
            AuditStatus status,
            String message,
            AuditErrorCode errorCode,
            String errorMessage
    ) {
        NormalizedCtx n = normalize(ctx);
        long now = System.currentTimeMillis();

        return AuditEvent.builder()
                .correlationId(n.correlationId)
                .companyId(n.companyId)
                .groupId(n.groupId)
                .userId(n.userId)
                .courseCode(n.courseCode)
                .ntucDTId(n.ntucDTId)
                .category(category)
                .step(step)
                .severity(severity)
                .status(status)
                .message(oneLine(message))
                .errorCode(errorCode == null ? AuditErrorCode.NONE : errorCode)
                .errorMessage(oneLine(errorMessage))
                .startTimeMs(now)
                .build();
    }

    public static AuditEvent end(
            AuditCategory category,
            AuditStep step,
            AuditSeverity severity,
            AuditRequestContext ctx,
            AuditStatus status,
            String message,
            AuditErrorCode errorCode
    ) {
        return end(category, step, severity, ctx, status, message, errorCode, null);
    }

    public static AuditEvent end(
            AuditCategory category,
            AuditStep step,
            AuditSeverity severity,
            AuditRequestContext ctx,
            AuditStatus status,
            String message,
            AuditErrorCode errorCode,
            String errorMessage
    ) {
        NormalizedCtx n = normalize(ctx);
        long now = System.currentTimeMillis();

        return AuditEvent.builder()
                .correlationId(n.correlationId)
                .companyId(n.companyId)
                .groupId(n.groupId)
                .userId(n.userId)
                .courseCode(n.courseCode)
                .ntucDTId(n.ntucDTId)
                .category(category)
                .step(step)
                .severity(severity)
                .status(status)
                .message(oneLine(message))
                .errorCode(errorCode == null ? AuditErrorCode.NONE : errorCode)
                .errorMessage(oneLine(errorMessage))
                .startTimeMs(now)
                .endTimeMs(now)
                .build();
    }

    public static AuditEvent success(
            AuditCategory category,
            AuditStep step,
            AuditSeverity severity,
            AuditRequestContext ctx,
            String message,
            AuditErrorCode errorCode
    ) {
        return success(category, step, severity, ctx, message, errorCode, null);
    }

    public static AuditEvent success(
            AuditCategory category,
            AuditStep step,
            AuditSeverity severity,
            AuditRequestContext ctx,
            String message,
            AuditErrorCode errorCode,
            String errorMessage
    ) {
        NormalizedCtx n = normalize(ctx);

        long start = System.currentTimeMillis();
        long end = start;

        return AuditEvent.builder()
                .correlationId(n.correlationId)
                .companyId(n.companyId)
                .groupId(n.groupId)
                .userId(n.userId)
                .courseCode(n.courseCode)
                .ntucDTId(n.ntucDTId)
                .category(category)
                .step(step)
                .severity(severity)
                .status(AuditStatus.SUCCESS)
                .message(oneLine(message))
                .errorCode(errorCode == null ? AuditErrorCode.NONE : errorCode)
                .errorMessage(oneLine(errorMessage))
                .startTimeMs(start)
                .endTimeMs(end)
                .build();
    }

    public static AuditEvent fail(
            AuditCategory category,
            AuditStep step,
            AuditSeverity severity,
            AuditRequestContext ctx,
            String message,
            AuditErrorCode errorCode,
            Throwable ex
    ) {
        NormalizedCtx n = normalize(ctx);

        long start = System.currentTimeMillis();
        long end = start;

        AuditEvent.Builder b = AuditEvent.builder()
                .correlationId(n.correlationId)
                .companyId(n.companyId)
                .groupId(n.groupId)
                .userId(n.userId)
                .courseCode(n.courseCode)
                .ntucDTId(n.ntucDTId)
                .category(category)
                .step(step)
                .severity(severity)
                .status(AuditStatus.FAILED)
                .message(oneLine(message))
                .errorCode(errorCode == null ? AuditErrorCode.NONE : errorCode)
                .startTimeMs(start)
                .endTimeMs(end);

        if (ex != null) {
            applyException(b, errorCode, ex);
        }

        return b.build();
    }

    /**
     * Applies a sanitized exception representation onto an {@link AuditEvent.Builder}.
     *
     * Rules:
     * - errorMessage is 1-line
     * - stackTraceTruncated is size-capped
     * - stackTraceHash is SHA-256 of the truncated stack trace (stable and bounded)
     *
     * Caller retains control of:
     * - step/category/status/severity/timestamps/details
     */
    public static void applyException(AuditEvent.Builder b, AuditErrorCode code, Throwable ex) {
        if (b == null || ex == null) {
            return;
        }

        b.errorCode(code == null ? AuditErrorCode.NONE : code);
        b.exceptionClass(ex.getClass().getName());
        b.errorMessage(oneLine(ex.getMessage()));

        String full = toStackTrace(ex);
        String truncated = truncate(full, STACKTRACE_MAX);

        b.stackTraceTruncated(truncated);
        b.stackTraceHash(sha256Hex(truncated));
    }

    /**
     * Normalizes text to single-line (no CR/LF).
     */
    public static String oneLine(String s) {
        if (s == null) {
            return "";
        }
        return s.replace('\n', ' ').replace('\r', ' ').trim();
    }

    // -------------------------
    // Internals
    // -------------------------

    private static NormalizedCtx normalize(AuditRequestContext ctx) {
        if (ctx == null) {
            return new NormalizedCtx(
                    UUID.randomUUID().toString(),
                    0L, 0L, 0L,
                    "",
                    0L
            );
        }

        String corr = safeTrim(ctx.getCorrelationId());
        if (corr.isEmpty()) {
            corr = UUID.randomUUID().toString();
        }

        return new NormalizedCtx(
                corr,
                ctx.getCompanyId(),
                ctx.getGroupId(),
                ctx.getUserId(),
                safeTrim(ctx.getCourseCode()),
                ctx.getNtucDTId()
        );
    }

    private static String safeTrim(String s) {
        return (s == null) ? "" : s.trim();
    }

    private static String toStackTrace(Throwable t) {
        StringWriter sw = new StringWriter(8 * 1024);
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    private static String truncate(String s, int max) {
        if (s == null) {
            return null;
        }
        if (s.length() <= max) {
            return s;
        }
        return s.substring(0, max);
    }

    private static String sha256Hex(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest((s == null ? "" : s).getBytes(StandardCharsets.UTF_8));
            return toHex(digest);
        }
        catch (Exception e) {
            return "sha256_error";
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            int v = b & 0xFF;
            if (v < 16) sb.append('0');
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }

    private static final class NormalizedCtx {
        private final String correlationId;
        private final long companyId;
        private final long groupId;
        private final long userId;
        private final String courseCode;
        private final long ntucDTId;

        private NormalizedCtx(
                String correlationId,
                long companyId,
                long groupId,
                long userId,
                String courseCode,
                long ntucDTId
        ) {
            this.correlationId = correlationId;
            this.companyId = companyId;
            this.groupId = groupId;
            this.userId = userId;
            this.courseCode = courseCode == null ? "" : courseCode;
            this.ntucDTId = ntucDTId;
        }
    }
}
