package com.ntuc.notification.emailtemplate.api;

import java.io.Serializable;

/**
 * Immutable system-level update for DT5 alert email templates.
 *
 * Security:
 * - Templates may contain HTML.
 * - Must never contain secrets/raw payloads (enforced by admin process and auditing rules).
 */
public final class Dt5EmailTemplateUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String redirect;

    private final String dt5FailureSubject;
    private final String dt5FailureBody;

    private final String clsFailureSubject;
    private final String clsFailureBody;

    private final String jaFailureSubject;
    private final String jaFailureBody;

    private Dt5EmailTemplateUpdateRequest(Builder b) {
        redirect = b.redirect;
        dt5FailureSubject = b.dt5FailureSubject;
        dt5FailureBody = b.dt5FailureBody;
        clsFailureSubject = b.clsFailureSubject;
        clsFailureBody = b.clsFailureBody;
        jaFailureSubject = b.jaFailureSubject;
        jaFailureBody = b.jaFailureBody;
    }

    public String getRedirect() { return redirect; }

    public String getDt5FailureSubject() { return dt5FailureSubject; }
    public String getDt5FailureBody() { return dt5FailureBody; }

    public String getClsFailureSubject() { return clsFailureSubject; }
    public String getClsFailureBody() { return clsFailureBody; }

    public String getJaFailureSubject() { return jaFailureSubject; }
    public String getJaFailureBody() { return jaFailureBody; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String redirect;

        private String dt5FailureSubject;
        private String dt5FailureBody;

        private String clsFailureSubject;
        private String clsFailureBody;

        private String jaFailureSubject;
        private String jaFailureBody;

        private Builder() {
        }

        public Builder redirect(String v) { redirect = v; return this; }

        public Builder dt5FailureSubject(String v) { dt5FailureSubject = v; return this; }
        public Builder dt5FailureBody(String v) { dt5FailureBody = v; return this; }

        public Builder clsFailureSubject(String v) { clsFailureSubject = v; return this; }
        public Builder clsFailureBody(String v) { clsFailureBody = v; return this; }

        public Builder jaFailureSubject(String v) { jaFailureSubject = v; return this; }
        public Builder jaFailureBody(String v) { jaFailureBody = v; return this; }

        public Dt5EmailTemplateUpdateRequest build() { return new Dt5EmailTemplateUpdateRequest(this); }
    }
}
