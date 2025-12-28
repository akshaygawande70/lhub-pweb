package com.ntuc.notification.audit.api.constants;

/**
 * Controls dedupe / suppression scope for audit-driven email alerts.
 *
 * The scope determines which "key" is used as part of the alert fingerprint,
 * and therefore how aggressively emails are deduplicated.
 *
 * COURSE:
 * - Dedupe is scoped per courseCode.
 * - Best when the same course repeatedly fails and you want only one email per window.
 *
 * FLOW:
 * - Dedupe is scoped per correlationId (fallback to requestId).
 * - Best when you only want one email per execution flow, but allow repeats across flows.
 *
 * JOB:
 * - Dedupe is scoped per jobRunId.
 * - Best for batch/cron runs, one email per job window regardless of how many items fail.
 */
public enum AlertScope {
    COURSE,
    FLOW,
    JOB
}
