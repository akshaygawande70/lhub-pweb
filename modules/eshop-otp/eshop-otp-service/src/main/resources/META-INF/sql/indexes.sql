create index IX_50E094A on ntuc_otp (otpId, userId);
create index IX_A28E577A on ntuc_otp (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_A8DA747C on ntuc_otp (uuid_[$COLUMN_LENGTH:75$], groupId);