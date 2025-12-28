create index IX_454BD2E3 on ntuc_parameter (groupId, deleted);
create index IX_788C78E5 on ntuc_parameter (groupId, paramCode[$COLUMN_LENGTH:75$], deleted);
create index IX_8227EFAA on ntuc_parameter (groupId, parameterGroupId, paramCode[$COLUMN_LENGTH:75$], deleted);
create index IX_20810543 on ntuc_parameter (paramCode[$COLUMN_LENGTH:75$], deleted);
create index IX_8F5B5320 on ntuc_parameter (parameterGroupId, deleted);

create index IX_DD60E811 on ntuc_parameter_group (groupCode[$COLUMN_LENGTH:75$], deleted);
create index IX_E9335E3 on ntuc_parameter_group (groupId, deleted);
create index IX_C7BE75B3 on ntuc_parameter_group (groupId, groupCode[$COLUMN_LENGTH:75$], deleted);
create index IX_92E67E4C on ntuc_parameter_group (parentId, deleted);

create index IX_E7D4EE47 on ntuc_reserved_parameter (paramCode[$COLUMN_LENGTH:75$]);
create index IX_C8829928 on ntuc_reserved_parameter (parameterGroupId);
create index IX_3B3E96F6 on ntuc_reserved_parameter (reservedBy[$COLUMN_LENGTH:75$]);