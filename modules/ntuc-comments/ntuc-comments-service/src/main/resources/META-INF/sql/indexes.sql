create index IX_EFC3E581 on NTUC_COMMENT_Comment (groupId, commentEmail[$COLUMN_LENGTH:75$]);
create index IX_C3DF3AEA on NTUC_COMMENT_Comment (groupId, commentName[$COLUMN_LENGTH:75$]);
create index IX_AFBD8F0E on NTUC_COMMENT_Comment (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_F5DA8110 on NTUC_COMMENT_Comment (uuid_[$COLUMN_LENGTH:75$], groupId);