create table NTUC_COMMENT_Comment (
	uuid_ VARCHAR(75) null,
	commentId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	commentText VARCHAR(75) null,
	commentName VARCHAR(75) null,
	commentEmail VARCHAR(75) null,
	commentOption BOOLEAN,
	commentStatus BOOLEAN
);