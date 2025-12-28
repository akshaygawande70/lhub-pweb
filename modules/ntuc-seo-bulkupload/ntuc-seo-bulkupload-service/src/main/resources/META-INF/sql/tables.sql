create table ntuc_seo_NtucBulkUpload (
	uuid_ VARCHAR(75) null,
	ntucBulkUploadId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	fileName VARCHAR(75) null,
	rowData STRING null
);