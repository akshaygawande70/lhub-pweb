package com.ntuc.notification.auditlog.api;

import java.io.Serializable;
import java.util.Objects;

/**
 * Immutable query request for AuditLog search used by the Audit Log Portlet.
 *
 * <p>Security:
 * - No raw payloads
 * - No secrets
 * - Free-text query must be size-capped and sanitized by service-layer logic</p>
 *
 * <p>Filters map to AuditLog entity fields:
 * - category, status, severity, step
 * - time range from/to (yyyy-MM-dd) resolved by service</p>
 */
public final class AuditLogQueryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String category;
    private final String status;
    private final String severity;
    private final String step;

    private final String fromYmd;
    private final String toYmd;

    private final String query;

    private final int start;
    private final int end;

    private AuditLogQueryRequest(Builder b) {
        this.category = trimToEmpty(b.category);
        this.status = trimToEmpty(b.status);
        this.severity = trimToEmpty(b.severity);
        this.step = trimToEmpty(b.step);

        this.fromYmd = trimToEmpty(b.fromYmd);
        this.toYmd = trimToEmpty(b.toYmd);

        this.query = trimToEmpty(b.query);
        this.start = b.start;
        this.end = b.end;
    }

    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public String getSeverity() { return severity; }
    public String getStep() { return step; }
    public String getFromYmd() { return fromYmd; }
    public String getToYmd() { return toYmd; }
    public String getQuery() { return query; }
    public int getStart() { return start; }
    public int getEnd() { return end; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String category;
        private String status;
        private String severity;
        private String step;
        private String fromYmd;
        private String toYmd;
        private String query;
        private int start;
        private int end;

        private Builder() {
        }

        public Builder category(String v) { this.category = v; return this; }
        public Builder status(String v) { this.status = v; return this; }
        public Builder severity(String v) { this.severity = v; return this; }
        public Builder step(String v) { this.step = v; return this; }
        public Builder fromYmd(String v) { this.fromYmd = v; return this; }
        public Builder toYmd(String v) { this.toYmd = v; return this; }
        public Builder query(String v) { this.query = v; return this; }
        public Builder start(int v) { this.start = v; return this; }
        public Builder end(int v) { this.end = v; return this; }

        public AuditLogQueryRequest build() {
            return new AuditLogQueryRequest(this);
        }
    }

    private static String trimToEmpty(String s) {
        return (s == null) ? "" : s.trim();
    }

    @Override
    public String toString() {
        return "AuditLogQueryRequest{" +
            "category='" + category + '\'' +
            ", status='" + status + '\'' +
            ", severity='" + severity + '\'' +
            ", step='" + step + '\'' +
            ", fromYmd='" + fromYmd + '\'' +
            ", toYmd='" + toYmd + '\'' +
            ", query.len=" + (query == null ? 0 : query.length()) +
            ", start=" + start +
            ", end=" + end +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AuditLogQueryRequest)) return false;
        AuditLogQueryRequest other = (AuditLogQueryRequest) o;
        return Objects.equals(category, other.category) &&
            Objects.equals(status, other.status) &&
            Objects.equals(severity, other.severity) &&
            Objects.equals(step, other.step) &&
            Objects.equals(fromYmd, other.fromYmd) &&
            Objects.equals(toYmd, other.toYmd) &&
            Objects.equals(query, other.query) &&
            start == other.start &&
            end == other.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, status, severity, step, fromYmd, toYmd, query, start, end);
    }
}
