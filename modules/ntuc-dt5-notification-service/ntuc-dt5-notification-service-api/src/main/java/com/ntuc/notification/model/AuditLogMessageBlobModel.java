/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.ntuc.notification.model;

import java.sql.Blob;

/**
 * The Blob model class for lazy loading the message column in AuditLog.
 *
 * @author Brian Wing Shun Chan
 * @see AuditLog
 * @generated
 */
public class AuditLogMessageBlobModel {

	public AuditLogMessageBlobModel() {
	}

	public AuditLogMessageBlobModel(long auditLogId) {
		_auditLogId = auditLogId;
	}

	public AuditLogMessageBlobModel(long auditLogId, Blob messageBlob) {
		_auditLogId = auditLogId;
		_messageBlob = messageBlob;
	}

	public long getAuditLogId() {
		return _auditLogId;
	}

	public void setAuditLogId(long auditLogId) {
		_auditLogId = auditLogId;
	}

	public Blob getMessageBlob() {
		return _messageBlob;
	}

	public void setMessageBlob(Blob messageBlob) {
		_messageBlob = messageBlob;
	}

	private long _auditLogId;
	private Blob _messageBlob;

}