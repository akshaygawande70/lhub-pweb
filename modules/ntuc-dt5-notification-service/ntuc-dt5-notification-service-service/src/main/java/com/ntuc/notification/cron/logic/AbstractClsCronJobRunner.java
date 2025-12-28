package com.ntuc.notification.cron.logic;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.audit.util.AuditEventFactory;
import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.service.ClsCourseFieldsProcessor;
import com.ntuc.notification.service.NtucSBLocalService;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Shared plain-Java runner for CLS cron jobs.
 *
 * Non-negotiables:
 * - AuditLog (via AuditEventWriter) is the single source of truth.
 * - Full timeline reconstruction must be possible from audit entries alone.
 * - No operational reliance on server logs.
 *
 * Email triggering must be audit-driven and handled by the audit-email trigger pipeline,
 * never directly from this cron runner.
 *
 * Actor semantics:
 * - Cron runs as SYSTEM (no request context).
 * - Therefore groupId/userId are explicitly 0 and details.actor="CRON".
 */
public abstract class AbstractClsCronJobRunner {

    protected static final ZoneId ZONE_SG = ZoneId.of("Asia/Singapore");
    protected static final DateTimeFormatter TS_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZONE_SG);

    private static final long SYSTEM_GROUP_ID = 0L;
    private static final long SYSTEM_USER_ID = 0L;

    private final NtucSBLocalService ntucSBLocalService;
    private final ClsCourseFieldsProcessor clsCourseFieldsProcessor;
    private final AuditEventWriter auditEventWriter;
    private final CorrelationIdSupplier correlationIdSupplier;
    private final JobRunIdSupplier jobRunIdSupplier;
    private final Clock clock;

    protected AbstractClsCronJobRunner(
            NtucSBLocalService ntucSBLocalService,
            ClsCourseFieldsProcessor clsCourseFieldsProcessor,
            AuditEventWriter auditEventWriter,
            CorrelationIdSupplier correlationIdSupplier,
            JobRunIdSupplier jobRunIdSupplier,
            Clock clock) {

        this.ntucSBLocalService = Objects.requireNonNull(ntucSBLocalService, "ntucSBLocalService");
        this.clsCourseFieldsProcessor = Objects.requireNonNull(clsCourseFieldsProcessor, "clsCourseFieldsProcessor");
        this.auditEventWriter = Objects.requireNonNull(auditEventWriter, "auditEventWriter");
        this.correlationIdSupplier = Objects.requireNonNull(correlationIdSupplier, "correlationIdSupplier");
        this.jobRunIdSupplier = Objects.requireNonNull(jobRunIdSupplier, "jobRunIdSupplier");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public final void auditActivated(String jobName, String groupName, String cronExpression) {
        String correlationId = correlationIdSupplier.get();
        String jobRunId = "ACTIVATE-" + jobRunIdSupplier.get();
        long t = clock.millis();

        Map<String, String> details = new LinkedHashMap<String, String>();
        details.put("actor", "CRON");
        details.put("jobName", safe(jobName));
        details.put("groupName", safe(groupName));
        details.put("cronExpression", safe(cronExpression));

        auditEventWriter.write(AuditEvent.builder()
                .startTimeMs(t)
                .endTimeMs(t)
                .correlationId(correlationId)
                .jobRunId(jobRunId)
                // Cron has no request context; do not guess.
                .companyId(0L)
                .groupId(SYSTEM_GROUP_ID)
                .userId(SYSTEM_USER_ID)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.SUCCESS)
                .category(AuditCategory.DT5_FLOW)
                .step(AuditStep.CRON_ACTIVATED)
                .message("Cron scheduler activated")
                .details(details)
                .build());
    }

    public final void run(String jobName, String groupName) {
        final Instant start = Instant.ofEpochMilli(clock.millis());
        final String correlationId = correlationIdSupplier.get();
        final String jobRunId = jobRunIdSupplier.get();

        int processed = 0;
        int succeeded = 0;
        int failed = 0;

        // JOB STARTED
        auditEventWriter.write(buildJobEvent(
                correlationId,
                jobRunId,
                AuditSeverity.INFO,
                AuditStatus.STARTED,
                AuditStep.CRON_JOB_STARTED,
                "Cron job started",
                start.toEpochMilli(),
                0L,
                jobStartDetails(jobName, groupName, start),
                null
        ));

        try {
            List<NtucSB> toProcess = fetchRecordsToProcess(ntucSBLocalService);
            int total = toProcess == null ? 0 : toProcess.size();

            Map<String, String> discovered = new LinkedHashMap<String, String>();
            discovered.put("actor", "CRON");
            discovered.put("jobName", safe(jobName));
            discovered.put("groupName", safe(groupName));
            discovered.put("recordsFound", String.valueOf(total));
            discovered.put("selector", safe(getSelectorDescription()));

            long tDisc = clock.millis();
            auditEventWriter.write(buildJobEvent(
                    correlationId,
                    jobRunId,
                    AuditSeverity.INFO,
                    AuditStatus.SUCCESS,
                    AuditStep.CRON_RECORDS_DISCOVERED,
                    "Cron records discovered",
                    tDisc,
                    tDisc,
                    discovered,
                    null
            ));

            if (toProcess != null) {
                for (NtucSB record : toProcess) {
                    processed++;
                    final long rowStartMs = clock.millis();

                    auditEventWriter.write(buildRecordEvent(
                            correlationId,
                            jobRunId,
                            record,
                            AuditSeverity.INFO,
                            AuditStatus.STARTED,
                            AuditStep.CRON_RECORD_STARTED,
                            "Cron record processing started",
                            rowStartMs,
                            0L,
                            null
                    ));

                    try {
                        CourseEvent event = buildCourseEvent(record);

                        clsCourseFieldsProcessor.handleCourseNotification(event, true);

                        markRecordSuccess(ntucSBLocalService, record);

                        succeeded++;
                        final long rowEndMs = clock.millis();

                        auditEventWriter.write(buildRecordEvent(
                                correlationId,
                                jobRunId,
                                record,
                                AuditSeverity.INFO,
                                AuditStatus.SUCCESS,
                                AuditStep.CRON_RECORD_SUCCESS,
                                "Cron record processed successfully",
                                rowStartMs,
                                rowEndMs,
                                null
                        ));
                    }
                    catch (Exception rowEx) {
                        failed++;
                        final long rowEndMs = clock.millis();

                        auditEventWriter.write(buildRecordEvent(
                                correlationId,
                                jobRunId,
                                record,
                                AuditSeverity.ERROR,
                                AuditStatus.FAILED,
                                AuditStep.CRON_RECORD_FAILED,
                                "Cron record processing failed",
                                rowStartMs,
                                rowEndMs,
                                rowEx
                        ));

                        // Continue to next record - cron must be resilient.
                    }
                }
            }

            final long endMs = clock.millis();
            final AuditStatus endStatus = failed > 0 ? AuditStatus.PARTIAL : AuditStatus.SUCCESS;
            final AuditSeverity endSeverity = failed > 0 ? AuditSeverity.WARNING : AuditSeverity.INFO;

            auditEventWriter.write(buildJobEvent(
                    correlationId,
                    jobRunId,
                    endSeverity,
                    endStatus,
                    AuditStep.CRON_JOB_FINISHED,
                    "Cron job finished",
                    start.toEpochMilli(),
                    endMs,
                    jobSummaryDetails(jobName, groupName, processed, succeeded, failed, start, endMs),
                    null
            ));
        }
        catch (Exception fatal) {
            final long endMs = clock.millis();

            Map<String, String> summary = jobSummaryDetails(jobName, groupName, processed, succeeded, failed, start, endMs);
            summary.put("fatal", "true");

            auditEventWriter.write(buildJobEvent(
                    correlationId,
                    jobRunId,
                    AuditSeverity.ERROR,
                    AuditStatus.FAILED,
                    AuditStep.CRON_JOB_FAILED,
                    "Cron job failed",
                    start.toEpochMilli(),
                    endMs,
                    summary,
                    fatal
            ));
        }
    }

    protected abstract List<NtucSB> fetchRecordsToProcess(NtucSBLocalService ntucSBLocalService);

    protected abstract String getSelectorDescription();

    protected abstract void markRecordSuccess(NtucSBLocalService ntucSBLocalService, NtucSB record);

    protected CourseEvent buildCourseEvent(NtucSB record) {
        CourseEvent event = new CourseEvent();
        event.setCourseCodeSingle(record.getCourseCode());
        event.setCourseTypeSingle(record.getCourseType());
        event.setEventType(record.getEvent());

        event.setTimestamp(record.getNotificationDate() == null
                ? null
                : TS_FORMAT.format(record.getNotificationDate().toInstant()));

        event.setNtucSB(record);

        return event;
    }

    private AuditEvent buildJobEvent(
            String correlationId,
            String jobRunId,
            AuditSeverity severity,
            AuditStatus status,
            AuditStep step,
            String message,
            long startTimeMs,
            long endTimeMs,
            Map<String, String> details,
            Exception ex) {

        AuditEvent.Builder b = AuditEvent.builder()
                .startTimeMs(startTimeMs)
                .endTimeMs(endTimeMs)
                .correlationId(correlationId)
                .jobRunId(jobRunId)
                .companyId(0L)
                .groupId(SYSTEM_GROUP_ID)
                .userId(SYSTEM_USER_ID)
                .severity(severity)
                .status(status)
                .category(AuditCategory.DT5_FLOW)
                .step(step)
                .message(AuditEventFactory.oneLine(message));

        if (details != null && !details.isEmpty()) {
            if (!details.containsKey("actor")) {
                details.put("actor", "CRON");
            }
            b.details(details);
        } else {
            Map<String, String> d = new LinkedHashMap<String, String>();
            d.put("actor", "CRON");
            b.details(d);
        }

        if (ex != null) {
            AuditErrorCode code = (step == AuditStep.CRON_JOB_FAILED)
                    ? AuditErrorCode.CRON_JOB_FAILED
                    : AuditErrorCode.PROCESSING_FAILED;

            AuditEventFactory.applyException(b, code, ex);
        }

        return b.build();
    }

    private AuditEvent buildRecordEvent(
            String correlationId,
            String jobRunId,
            NtucSB record,
            AuditSeverity severity,
            AuditStatus status,
            AuditStep step,
            String message,
            long startTimeMs,
            long endTimeMs,
            Exception ex) {

        Map<String, String> details = new LinkedHashMap<String, String>();
        details.put("actor", "CRON");
        details.put("ntucDTId", String.valueOf(record.getNtucDTId()));
        details.put("companyId", String.valueOf(record.getCompanyId()));
        details.put("courseCode", safe(record.getCourseCode()));
        details.put("courseType", safe(record.getCourseType()));
        details.put("eventType", safe(record.getEvent()));
        details.put("notificationDate", record.getNotificationDate() == null ? "" : String.valueOf(record.getNotificationDate()));
        details.put("changeFrom", safe(record.getChangeFrom()));

        AuditEvent.Builder b = AuditEvent.builder()
                .startTimeMs(startTimeMs)
                .endTimeMs(endTimeMs)
                .correlationId(correlationId)
                .jobRunId(jobRunId)
                .companyId(record.getCompanyId())
                .groupId(SYSTEM_GROUP_ID)
                .userId(SYSTEM_USER_ID)
                .courseCode(safe(record.getCourseCode()))
                .ntucDTId(record.getNtucDTId())
                .severity(severity)
                .status(status)
                .category(AuditCategory.DT5_FLOW)
                .step(step)
                .message(AuditEventFactory.oneLine(message))
                .details(details);

        if (ex != null) {
            AuditEventFactory.applyException(b, AuditErrorCode.CRON_RECORD_FAILED, ex);
        }

        return b.build();
    }

    private Map<String, String> jobStartDetails(String jobName, String groupName, Instant start) {
        Map<String, String> m = new LinkedHashMap<String, String>();
        m.put("actor", "CRON");
        m.put("jobName", safe(jobName));
        m.put("groupName", safe(groupName));
        m.put("firedAt", start.toString());
        return m;
    }

    private Map<String, String> jobSummaryDetails(
            String jobName,
            String groupName,
            int processed,
            int succeeded,
            int failed,
            Instant start,
            long endMs) {

        Map<String, String> m = new LinkedHashMap<String, String>();
        m.put("actor", "CRON");
        m.put("jobName", safe(jobName));
        m.put("groupName", safe(groupName));
        m.put("processed", String.valueOf(processed));
        m.put("succeeded", String.valueOf(succeeded));
        m.put("failed", String.valueOf(failed));
        m.put("startTs", start.toString());
        m.put("endTs", Instant.ofEpochMilli(endMs).toString());
        m.put("durationMs", String.valueOf(Math.max(0L, endMs - start.toEpochMilli())));
        return m;
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    @FunctionalInterface
    public interface CorrelationIdSupplier {
        String get();
    }

    @FunctionalInterface
    public interface JobRunIdSupplier {
        String get();
    }
}
