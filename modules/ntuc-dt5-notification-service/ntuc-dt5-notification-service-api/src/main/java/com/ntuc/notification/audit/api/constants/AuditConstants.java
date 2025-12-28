package com.ntuc.notification.audit.api.constants;

/**
 * Central place for audit actions, statuses, header/MDC keys, and meta keys.
 * Keep keys stable — they are persisted in DB JSON and used in logs.
 */
public final class AuditConstants {

	private AuditConstants() {
	}

	// ===== Notification Actions =====
	public static final String ACTION_NOTIFICATION_RECEIVED = "NOTIFICATION_RECEIVED";
	public static final String ACTION_EVENT_INGEST = "EVENT_INGEST";
	public static final String ACTION_EVENT_PERSIST = "EVENT_PERSIST";
	public static final String ACTION_EVENT_ENQUEUE = "EVENT_ENQUEUE";
	public static final String ACTION_EVENT_PROCESS = "EVENT_PROCESS";
	public static final String ACTION_NOTIFICATION_COMPLETE = "NOTIFICATION_COMPLETE";
	public static final String ACTION_ONE_TIME_LOAD = "ACTION_ONE_TIME_LOAD";
	public static final String ACTION_ONE_TIME_LOAD_FILE = "ACTION_ONE_TIME_LOAD_FILE";
	public static final String ACTION_ONE_TIME_LOAD_FILE_PROCESS = "ACTION_ONE_TIME_LOAD_FILE_PROCESS";
	public static final String ACTION_ONE_TIME_LOAD_PROCESS = "ACTION_ONE_TIME_LOAD_PROCESS";
	
	
	// ===== CLS actions =====
	public static final String ACTION_CLS_AUTH = "CLS_AUTH";
	public static final String ACTION_CLS_COURSE_FETCH = "CLS_COURSE_FETCH";
	public static final String ACTION_SCHEDULE_FETCH = "CLS_SCHEDULE_FETCH";

	// ===== Schedule actions =====
	public static final String ACTION_SCH_PROCESS = "SCH_PROCESS";
	public static final String ACTION_SCH_CRITICAL = "SCH_CRITICAL";
	public static final String ACTION_SCH_NONCRITICAL = "SCH_NONCRITICAL";

	// ===== CRON actions =====
	public static final String ACTION_CRON_JOB_START = "CRON_JOB_START";
	public static final String ACTION_CRON_JOB_END = "CRON_JOB_END";
	public static final String ACTION_CRON_JOB_ERROR = "CRON_JOB_ERROR";
	public static final String ACTION_CRON_PROCESS_START = "CRON_PROCESS_START";
	public static final String ACTION_CRON_PROCESS_SUCCESS = "CRON_PROCESS_SUCCESS";
	public static final String ACTION_CRON_PROCESS_FAILURE = "CRON_PROCESS_FAILURE";

	// ===== Email actions =====
	public static final String ACTION_EMAIL_FROM_RESOLVED = "EMAIL_FROM_RESOLVED";
	public static final String ACTION_EMAIL_SENT = "EMAIL_SENT";
	public static final String ACTION_EMAIL_FAILED = "EMAIL_FAILED";
	public static final String ACTION_EMAIL_TEMPLATE_RENDER_FAILED = "EMAIL_TEMPLATE_RENDER_FAILED";
	
	// ===== JournalArticle actions =====
	public static final String ACTION_JA_PROCESS_FIELDS = "JA_PROCESS_FIELDS";
	public static final String ACTION_JA_CREATE = "JA_CREATE";
	public static final String ACTION_JA_UPDATE = "JA_UPDATE";
	public static final String ACTION_JA_STATUS = "JA_STATUS";
	public static final String ACTION_JA_LOOKUP = "JA_LOOKUP";
	public static final String ACTION_JA_FIND_LAYOUT = "JA_FIND_LAYOUT";

	// ===== Statuses =====
	public static final String STATUS_START = "START";
	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String STATUS_FAILED = "FAILED";
	public static final String STATUS_PARTIAL = "PARTIAL";
	public static final String STATUS_NOT_FOUND = "NOT_FOUND";
	public static final String STATUS_ENQUEUED = "ENQUEUED";
	public static final String STATUS_SKIPPED = "SKIPPED";
	

	// ===== HTTP header keys =====
	public static final String HEADER_CORR_ID = "X-Correlation-Id";
	public static final String HEADER_NTUC_DT_ID = "X-NtucDTId"; // optional inbound header

	// ===== MDC keys (for logs) =====
	public static final String MDC_CORR_ID = "corrId";
	public static final String MDC_NTUC_DT_ID = "ntucDTId";

	// ===== JAX-RS request property keys =====
	public static final String REQPROP_CORR_ID = "com.ntuc.corrId";
	public static final String REQPROP_NTUC_DT_ID = "com.ntuc.ntucDTId";

	// ===== Meta keys (used inside additionalInfo/changedFields) =====
	public static final String META_CORR_ID = "corrId";
	public static final String META_FQCN = "fqcn";
	public static final String META_COUNT = "count";
	public static final String META_TOTAL = "total";
	public static final String META_ERRORS = "errors";
	public static final String META_REASON = "reason";
	public static final String META_ERROR = "error";
	public static final String META_MESSAGE = "message";
	public static final String META_EVENT_TYPE = "eventType";
	public static final String META_COURSE_TYPE = "courseType";
	public static final String META_TIMESTAMP = "timestamp";
	public static final String META_COURSE_CODE = "courseCode";
	public static final String META_NTUC_DT_ID = "ntucDTId";
	public static final String META_COMPANY_ID = "companyId";
	public static final String META_GROUP_ID = "groupId";
	public static final String META_USER_ID = "userId";

	// ===== CRON meta keys =====
	public static final String META_JOB = "job";
	public static final String META_GROUP = "group";
	public static final String META_PHASE = "phase";
	public static final String META_DURATION_MS = "durationMs";
	public static final String META_CRON_EXPRESSION = "cron";
	public static final String META_FIRED_AT = "firedAt";
	public static final String META_CHANGE_FROM = "changeFrom";
}
