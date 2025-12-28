package com.ntuc.notification.rest.internal.processor;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.service.NtucSBLocalService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Persists inbound CLS notification events into the ServiceBuilder entity {@link NtucSB}.
 *
 * <p><b>Business purpose:</b> Ensure every accepted inbound event is durably stored so downstream
 * processing, retries, and operational reporting have a stable source of truth.</p>
 *
 * <p><b>Technical purpose:</b> Creates a new {@link NtucSB} row with tenant-safe fields, a best-effort
 * notification timestamp, and a deterministic initial processing state prior to any async execution.</p>
 *
 * <p>Non-negotiables:
 * <ul>
 *   <li>Every accepted event is persisted before async processing begins.</li>
 *   <li>{@code companyId} is persisted to guarantee tenant correctness for cron/parameter resolution.</li>
 *   <li>{@code canRetry} is always {@code true} at intake time.</li>
 * </ul>
 * </p>
 *
 * <p>Notes:
 * <ul>
 *   <li>This class is intentionally orchestration-light and does not perform auditing; auditing is owned
 *       by higher workflow layers (REST entry / async pipeline) where correlation and outcome are known.</li>
 *   <li>Timestamp parsing is defensive: invalid formats are treated as null to keep persistence resilient.</li>
 * </ul>
 * </p>
 *
 * @author @akshaygawande
 */
public class NotificationIntakePersister {

    /**
     * Timestamp format expected from CLS notification payloads.
     *
     * <p>Strict parsing is enforced via {@link SimpleDateFormat#setLenient(boolean)} to avoid
     * accepting ambiguous dates.</p>
     */
    private static final String TS_PATTERN = "dd/MM/yyyy HH:mm:ss";

    /**
     * ServiceBuilder local service for {@link NtucSB} persistence.
     */
    private final NtucSBLocalService ntucSBLocalService;

    /**
     * Counter service used to allocate a primary key for {@link NtucSB}.
     */
    private final CounterLocalService counterLocalService;

    /**
     * Reused formatter for parsing inbound timestamps.
     *
     * <p><b>Thread-safety:</b> {@link SimpleDateFormat} is not thread-safe. This instance is safe only
     * if {@link NotificationIntakePersister} is used per-request (typical in REST processors) or
     * externally synchronized. The implementation keeps it as an instance field to avoid repeated
     * allocations and to retain strict parsing configuration.</p>
     */
    private final SimpleDateFormat tsFormat;

    /**
     * Creates a persister capable of writing the intake record to {@link NtucSB}.
     *
     * <p><b>Business purpose:</b> Enables durable intake storage for accepted events.</p>
     * <p><b>Technical purpose:</b> Captures required services and configures strict timestamp parsing.</p>
     *
     * <p><b>Inputs/Invariants:</b>
     * <ul>
     *   <li>{@code ntucSBLocalService} must be non-null.</li>
     *   <li>{@code counterLocalService} must be non-null.</li>
     * </ul>
     * </p>
     *
     * <p><b>Side effects:</b> None (service references and formatter initialization only).</p>
     *
     * <p><b>Audit behavior:</b> None. Audit is expected to be performed by the workflow boundary layer.</p>
     *
     * @param ntucSBLocalService ServiceBuilder local service for NtucSB writes
     * @param counterLocalService counter service for primary key allocation
     */
    public NotificationIntakePersister(
            NtucSBLocalService ntucSBLocalService,
            CounterLocalService counterLocalService) {

        this.ntucSBLocalService = Objects.requireNonNull(ntucSBLocalService, "ntucSBLocalService");
        this.counterLocalService = Objects.requireNonNull(counterLocalService, "counterLocalService");

        tsFormat = new SimpleDateFormat(TS_PATTERN, Locale.ENGLISH);
        tsFormat.setLenient(false);
    }

    /**
     * Creates and persists an {@link NtucSB} record representing the inbound notification event.
     *
     * <p><b>Business purpose:</b> Guarantees an intake record exists for downstream processing,
     * troubleshooting, and retry control.</p>
     *
     * <p><b>Technical purpose:</b> Allocates a primary key, maps event fields to NtucSB columns,
     * initializes processing flags, and writes the entity via {@link NtucSBLocalService#addNtucSB(NtucSB)}.</p>
     *
     * <p><b>Inputs/Invariants:</b>
     * <ul>
     *   <li>{@code event} must be non-null.</li>
     *   <li>{@code companyId} must be {@code > 0} and is always persisted (tenant correctness rule).</li>
     *   <li>Validator is assumed to have ensured mandatory fields; mapping remains defensive.</li>
     * </ul>
     * </p>
     *
     * <p><b>Side effects:</b>
     * <ul>
     *   <li>DB write: creates a new row in {@code ntuc_service_dt_notification} via ServiceBuilder.</li>
     *   <li>Consumes a counter increment for {@link NtucSB} primary key allocation.</li>
     * </ul>
     * </p>
     *
     * <p><b>Audit behavior:</b> None. This method is a persistence utility; workflow boundary layers
     * should record ENTRY/EXECUTION audit outcomes around the overall intake + async kickoff.</p>
     *
     * <p><b>Return semantics:</b>
     * <ul>
     *   <li>Returns the persisted {@link NtucSB} entity (never null).</li>
     *   <li>Throws {@link IllegalArgumentException} if {@code companyId <= 0}.</li>
     *   <li>Runtime exceptions may propagate from underlying ServiceBuilder services.</li>
     * </ul>
     * </p>
     *
     * @param event accepted event (validator ensures required ones are present)
     * @param companyId tenant id from request context (must be > 0)
     * @return persisted NtucSB record
     */
    public NtucSB persist(CourseEvent event, long companyId) {
        Objects.requireNonNull(event, "event");

        // Tenant correctness guard: companyId is required to support cron + parameter/prefs resolution.
        if (companyId <= 0) {
            throw new IllegalArgumentException("companyId must be > 0 for NtucSB persistence");
        }

        long pk = counterLocalService.increment(NtucSB.class.getName());
        NtucSB sb = ntucSBLocalService.createNtucSB(pk);

        // Tenant (NON-NEGOTIABLE)
        sb.setCompanyId(companyId);

        // Best-effort event fields (validator enforces presence; persistence remains defensive).
        sb.setCourseCode(safe(event.getCourseCodeSingle()));
        sb.setCourseType(safe(event.getCourseTypeSingle()));

        sb.setNotificationId(safe(event.getNotificationId()));
        sb.setEvent(safe(event.getEventType()));
        sb.setChangeFrom(joinChangeFrom(event.getChangeFrom()));

        // systemDate is always set to "now" to reflect intake time.
        Date now = new Date();
        sb.setSystemDate(now);

        // notificationDate is derived from payload timestamp; invalid formats are stored as null.
        sb.setNotificationDate(parseTimestampOrNull(event.getTimestamp()));

        // Processing flags - deterministic initial state used by downstream processors.
        sb.setProcessingStatus("RECEIVED");
        sb.setCourseStatus("");

        sb.setIsCriticalProcessed(false);
        sb.setIsNonCriticalProcessed(false);
        sb.setIsCronProcessed(false);

        // HARD RULE: intake is always retryable; retry policy/limits are applied later.
        sb.setCanRetry(true);

        sb.setLastRetried(null);
        sb.setTotalRetries(0);

        // Downstream processors may set this when DB row-locking fails; starts false at intake.
        sb.setIsRowLockFailed(false);

        return ntucSBLocalService.addNtucSB(sb);
    }

    /**
     * Parses the inbound timestamp using the strict {@link #TS_PATTERN} format.
     *
     * <p><b>Business purpose:</b> Preserves the sender-reported notification time when valid.</p>
     * <p><b>Technical purpose:</b> Converts a string timestamp into {@link Date} with strict validation.</p>
     *
     * <p><b>Inputs/Invariants:</b>
     * <ul>
     *   <li>Blank/empty timestamps return null.</li>
     *   <li>Strict parsing is applied; non-conforming values return null.</li>
     * </ul>
     * </p>
     *
     * <p><b>Side effects:</b> None (pure parsing; no DB writes).</p>
     *
     * <p><b>Audit behavior:</b> None.</p>
     *
     * <p><b>Return semantics:</b>
     * <ul>
     *   <li>Returns parsed {@link Date} when valid.</li>
     *   <li>Returns null when blank or invalid (defensive; validator should prevent invalid values).</li>
     * </ul>
     * </p>
     *
     * @param timestamp inbound timestamp string
     * @return parsed date or null when blank/invalid
     */
    private Date parseTimestampOrNull(String timestamp) {
        String ts = safe(timestamp);
        if (ts.isEmpty()) {
            return null;
        }

        try {
            return tsFormat.parse(ts);
        }
        catch (ParseException e) {
            // Validator should prevent this; null keeps persistence resilient and avoids failing intake.
            return null;
        }
    }

    /**
     * Joins changeFrom values into a comma-separated string suitable for DB persistence.
     *
     * <p><b>Business purpose:</b> Captures which fields changed for "changed" events to support
     * downstream processing and reporting.</p>
     *
     * <p><b>Technical purpose:</b> Normalizes, trims, and concatenates non-empty values with comma separators.</p>
     *
     * <p><b>Inputs/Invariants:</b>
     * <ul>
     *   <li>Null/empty input returns an empty string.</li>
     *   <li>Null/blank elements are skipped.</li>
     * </ul>
     * </p>
     *
     * <p><b>Side effects:</b> None.</p>
     *
     * <p><b>Audit behavior:</b> None.</p>
     *
     * <p><b>Return semantics:</b> Never returns null.</p>
     *
     * @param values changeFrom list from inbound payload
     * @return comma-separated list (never null)
     */
    private static String joinChangeFrom(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String v : values) {
            String s = safe(v);
            if (s.isEmpty()) {
                continue;
            }

            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(s);
        }

        return sb.toString();
    }

    /**
     * Null-safe string normalizer used for persistence fields.
     *
     * <p><b>Business purpose:</b> Avoids storing nulls for string columns where empty values are acceptable.</p>
     * <p><b>Technical purpose:</b> Trims whitespace and converts null to empty string.</p>
     *
     * <p><b>Inputs/Invariants:</b> Accepts null.</p>
     * <p><b>Side effects:</b> None.</p>
     * <p><b>Audit behavior:</b> None.</p>
     * <p><b>Return semantics:</b> Never returns null.</p>
     *
     * @param s input string
     * @return trimmed string or empty
     */
    private static String safe(String s) {
        return (s == null) ? "" : s.trim();
    }
}
