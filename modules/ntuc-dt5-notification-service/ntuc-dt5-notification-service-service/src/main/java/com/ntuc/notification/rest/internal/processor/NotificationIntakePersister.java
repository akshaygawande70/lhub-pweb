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
 * Persists inbound notifications into NtucSB (ntuc_service_dt_notification).
 *
 * Non-negotiables:
 * - Every accepted event MUST be stored before async processing starts.
 * - companyId MUST be persisted (tenant correctness for cron + parameter/prefs resolution).
 * - canRetry MUST always be true at intake time.
 *
 * Pure Java: depends only on CounterLocalService + NtucSBLocalService.
 */
public class NotificationIntakePersister {

    private static final String TS_PATTERN = "dd/MM/yyyy HH:mm:ss";

    private final NtucSBLocalService ntucSBLocalService;
    private final CounterLocalService counterLocalService;
    private final SimpleDateFormat tsFormat;

    public NotificationIntakePersister(
            NtucSBLocalService ntucSBLocalService,
            CounterLocalService counterLocalService) {

        this.ntucSBLocalService = Objects.requireNonNull(ntucSBLocalService, "ntucSBLocalService");
        this.counterLocalService = Objects.requireNonNull(counterLocalService, "counterLocalService");

        tsFormat = new SimpleDateFormat(TS_PATTERN, Locale.ENGLISH);
        tsFormat.setLenient(false);
    }

    /**
     * Create and persist an NtucSB record for the given event.
     *
     * @param event accepted event (validator ensures required ones are present)
     * @param companyId tenant id from request context (MUST be > 0)
     * @return persisted NtucSB
     */
    public NtucSB persist(CourseEvent event, long companyId) {
        Objects.requireNonNull(event, "event");

        if (companyId <= 0) {
            throw new IllegalArgumentException("companyId must be > 0 for NtucSB persistence");
        }

        long pk = counterLocalService.increment(NtucSB.class.getName());
        NtucSB sb = ntucSBLocalService.createNtucSB(pk);

        // Tenant (NON-NEGOTIABLE)
        sb.setCompanyId(companyId);

        // Best-effort fields (validator ensures required ones are present)
        sb.setCourseCode(safe(event.getCourseCodeSingle()));
        sb.setCourseType(safe(event.getCourseTypeSingle()));

        sb.setNotificationId(safe(event.getNotificationId()));
        sb.setEvent(safe(event.getEventType()));
        sb.setChangeFrom(joinChangeFrom(event.getChangeFrom()));

        Date now = new Date();
        sb.setSystemDate(now);
        sb.setNotificationDate(parseTimestampOrNull(event.getTimestamp()));

        // Processing flags - default initial state
        sb.setProcessingStatus("RECEIVED");
        sb.setCourseStatus("");

        sb.setIsCriticalProcessed(false);
        sb.setIsNonCriticalProcessed(false);
        sb.setIsCronProcessed(false);

        // HARD RULE: always retryable
        sb.setCanRetry(true);

        sb.setLastRetried(null);
        sb.setTotalRetries(0);

        sb.setIsRowLockFailed(false);

        return ntucSBLocalService.addNtucSB(sb);
    }

    private Date parseTimestampOrNull(String timestamp) {
        String ts = safe(timestamp);
        if (ts.isEmpty()) {
            return null;
        }
        try {
            return tsFormat.parse(ts);
        }
        catch (ParseException e) {
            // Validator should prevent this; keep DB resilient.
            return null;
        }
    }

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

    private static String safe(String s) {
        return (s == null) ? "" : s.trim();
    }
}
