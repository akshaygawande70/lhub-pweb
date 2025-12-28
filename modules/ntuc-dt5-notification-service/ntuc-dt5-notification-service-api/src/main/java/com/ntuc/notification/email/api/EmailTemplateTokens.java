package com.ntuc.notification.email.api;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Central token catalog for DT5 email templates.
 *
 * <p>Tokens MUST be stable and documented. No ad-hoc placeholders.</p>
 */
public final class EmailTemplateTokens {

    private EmailTemplateTokens() {
        // util
    }

    public static final String AUDIT_ID = "[$AUDIT_ID$]";
    public static final String CORRELATION_ID = "[$CORRELATION_ID$]";
    public static final String JOB_RUN_ID = "[$JOB_RUN_ID$]";
    public static final String REQUEST_ID = "[$REQUEST_ID$]";

    public static final String TIMESTAMP = "[$TIMESTAMP$]";
    public static final String STEP = "[$STEP$]";
    public static final String CATEGORY = "[$CATEGORY$]";
    public static final String STATUS = "[$STATUS$]";
    public static final String SEVERITY = "[$SEVERITY$]";

    public static final String ERROR_CODE = "[$ERROR_CODE$]";
    public static final String ERROR_MESSAGE = "[$ERROR_MESSAGE$]";
    public static final String EXCEPTION_CLASS = "[$EXCEPTION_CLASS$]";

    public static final String COURSE_CODE = "[$COURSE_CODE$]";
    public static final String NTUC_SB_ID = "[$NTUC_SB_ID$]";

    // CLS-focused (must be sanitized)
    public static final String ENDPOINT = "[$ENDPOINT$]";
    public static final String RESPONSE_CODE = "[$RESPONSE_CODE$]";

    // JA-focused
    public static final String DDM_STRUCTURE_KEY = "[$DDM_STRUCTURE_KEY$]";
    public static final String DDM_TEMPLATE_KEY = "[$DDM_TEMPLATE_KEY$]";

    public static Set<String> allTokens() {
        LinkedHashSet<String> s = new LinkedHashSet<>();
        s.add(AUDIT_ID);
        s.add(CORRELATION_ID);
        s.add(JOB_RUN_ID);
        s.add(REQUEST_ID);
        s.add(TIMESTAMP);
        s.add(STEP);
        s.add(CATEGORY);
        s.add(STATUS);
        s.add(SEVERITY);
        s.add(ERROR_CODE);
        s.add(ERROR_MESSAGE);
        s.add(EXCEPTION_CLASS);
        s.add(COURSE_CODE);
        s.add(NTUC_SB_ID);
        s.add(ENDPOINT);
        s.add(RESPONSE_CODE);
        s.add(DDM_STRUCTURE_KEY);
        s.add(DDM_TEMPLATE_KEY);
        return Collections.unmodifiableSet(s);
    }
}
