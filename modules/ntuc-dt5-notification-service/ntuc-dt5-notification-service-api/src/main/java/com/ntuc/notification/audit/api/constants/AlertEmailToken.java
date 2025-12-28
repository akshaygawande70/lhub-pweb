package com.ntuc.notification.audit.api.constants;

/**
 * Allowed tokens for alert email subject/body templates.
 *
 * <p>IMPORTANT:</p>
 * <ul>
 *   <li>Tokens are a strict whitelist; unknown tokens must NOT be rendered.</li>
 *   <li>No secrets, raw payloads, or sensitive data tokens are allowed.</li>
 * </ul>
 *
 * <p>Examples:</p>
 * <pre>
 * Subject: [DT5] Failure - [$ERROR_CODE$] - Correlation [$CORRELATION_ID$]
 * Body: AuditId=[$AUDIT_ID$], Step=[$STEP$], Message=[$ERROR_MESSAGE$]
 * </pre>
 */
public enum AlertEmailToken {

    AUDIT_ID("[$AUDIT_ID$]", "Audit log primary key"),
    CORRELATION_ID("[$CORRELATION_ID$]", "Correlation id for the flow"),
    JOB_RUN_ID("[$JOB_RUN_ID$]", "Job run identifier"),
    REQUEST_ID("[$REQUEST_ID$]", "Inbound request identifier"),
    TIMESTAMP("[$TIMESTAMP$]", "Event timestamp (UTC recommended)"),

    STEP("[$STEP$]", "Audit step (enum)"),
    CATEGORY("[$CATEGORY$]", "Audit category (enum)"),
    SEVERITY("[$SEVERITY$]", "Audit severity (enum)"),
    STATUS("[$STATUS$]", "Audit status (enum)"),

    ERROR_CODE("[$ERROR_CODE$]", "Stable error code enum"),
    ERROR_MESSAGE("[$ERROR_MESSAGE$]", "One-line error summary"),
    EXCEPTION_CLASS("[$EXCEPTION_CLASS$]", "Exception class name"),
    STACK_HASH("[$STACK_HASH$]", "Hash of truncated stack trace"),

    COURSE_CODE("[$COURSE_CODE$]", "Course code business key"),
    NTUC_DT_ID("[$NTUC_DT_ID$]", "NTUC DT business key"),
    ENDPOINT("[$ENDPOINT$]", "External endpoint name/url label"),
    RESPONSE_CODE("[$RESPONSE_CODE$]", "HTTP status/response code"),

    DDM_STRUCTURE_KEY("[$DDM_STRUCTURE_KEY$]", "DDM structure key (if applicable)"),
    DDM_TEMPLATE_KEY("[$DDM_TEMPLATE_KEY$]", "DDM template key (if applicable)");

    private final String token;
    private final String description;

    AlertEmailToken(String token, String description) {
        this.token = token;
        this.description = description;
    }

    public String token() {
        return token;
    }

    public String description() {
        return description;
    }
}
