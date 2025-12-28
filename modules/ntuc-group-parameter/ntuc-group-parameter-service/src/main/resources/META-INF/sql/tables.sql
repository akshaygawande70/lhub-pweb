create table ntuc_parameter (
	parameterId LONG not null primary key,
	groupId LONG,
	parameterGroupId LONG,
	companyId LONG,
	createdBy LONG,
	createdDate DATE null,
	modifiedBy LONG,
	modifiedDate DATE null,
	paramCode VARCHAR(75) null,
	paramName VARCHAR(75) null,
	paramValue VARCHAR(75) null,
	description VARCHAR(75) null,
	deleted BOOLEAN
);

create table ntuc_parameter_group (
	parameterGroupId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	createdBy LONG,
	createdDate DATE null,
	modifiedBy LONG,
	modifiedDate DATE null,
	parentId LONG,
	groupName VARCHAR(75) null,
	groupCode VARCHAR(75) null,
	description VARCHAR(75) null,
	deleted BOOLEAN
);

create table ntuc_reserved_parameter (
	reservedParameterId LONG not null primary key,
	groupId LONG,
	parameterId LONG,
	parameterGroupId LONG,
	companyId LONG,
	createdBy LONG,
	createdDate DATE null,
	modifiedBy LONG,
	modifiedDate DATE null,
	paramCode VARCHAR(75) null,
	reservedBy VARCHAR(75) null
);