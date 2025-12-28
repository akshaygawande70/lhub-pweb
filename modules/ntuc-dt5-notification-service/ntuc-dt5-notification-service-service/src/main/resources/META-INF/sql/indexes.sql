create index IX_1FE3B65C on CSchedule (courseCode[$COLUMN_LENGTH:255$]);
create index IX_18D1A334 on CSchedule (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_511166B6 on CSchedule (uuid_[$COLUMN_LENGTH:75$], groupId);

create index IX_46BBC3DC on ntuc_audit_log (category[$COLUMN_LENGTH:75$], alertFingerprint[$COLUMN_LENGTH:75$], startTimeMs);
create index IX_FFAB1344 on ntuc_audit_log (category[$COLUMN_LENGTH:75$], status[$COLUMN_LENGTH:75$], severity[$COLUMN_LENGTH:75$]);
create index IX_55A09DD2 on ntuc_audit_log (category[$COLUMN_LENGTH:75$], status[$COLUMN_LENGTH:75$], startTimeMs);
create index IX_9232B64A on ntuc_audit_log (companyId, category[$COLUMN_LENGTH:75$], alertFingerprint[$COLUMN_LENGTH:75$], startTimeMs);
create index IX_2922F9F3 on ntuc_audit_log (correlationId[$COLUMN_LENGTH:75$], startTimeMs);
create index IX_4A68BC57 on ntuc_audit_log (courseCode[$COLUMN_LENGTH:75$]);
create index IX_13723BEA on ntuc_audit_log (errorCode[$COLUMN_LENGTH:75$]);
create index IX_5FBE15EA on ntuc_audit_log (eventId[$COLUMN_LENGTH:75$]);
create index IX_5275A5F8 on ntuc_audit_log (jobRunId[$COLUMN_LENGTH:75$]);
create index IX_ECE5FFCE on ntuc_audit_log (ntucDTId);
create index IX_B8C8F69F on ntuc_audit_log (requestId[$COLUMN_LENGTH:75$]);
create index IX_9D8DAF3B on ntuc_audit_log (step[$COLUMN_LENGTH:75$]);

create index IX_24EFBDF1 on ntuc_cschedule (courseCode[$COLUMN_LENGTH:255$]);
create index IX_3A7CE709 on ntuc_cschedule (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_5AF59BCB on ntuc_cschedule (uuid_[$COLUMN_LENGTH:75$], groupId);

create index IX_80941FB9 on ntuc_service_dt_notification (changeFrom[$COLUMN_LENGTH:75$]);
create index IX_ADF8CE71 on ntuc_service_dt_notification (courseCode[$COLUMN_LENGTH:75$], event[$COLUMN_LENGTH:75$], changeFrom[$COLUMN_LENGTH:75$]);
create index IX_8325FD16 on ntuc_service_dt_notification (event[$COLUMN_LENGTH:75$], isCriticalProcessed);
create index IX_F381C147 on ntuc_service_dt_notification (event[$COLUMN_LENGTH:75$], isCronProcessed);
create index IX_A8AF7A7F on ntuc_service_dt_notification (event[$COLUMN_LENGTH:75$], isNonCriticalProcessed);
create index IX_4D970D44 on ntuc_service_dt_notification (isRowLockFailed, processingStatus[$COLUMN_LENGTH:75$], changeFrom[$COLUMN_LENGTH:75$]);