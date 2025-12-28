create table ntuc_otp (
	uuid_ VARCHAR(75) null,
	otpId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	otpCode INTEGER,
	oTPValidatedFlag BOOLEAN
);