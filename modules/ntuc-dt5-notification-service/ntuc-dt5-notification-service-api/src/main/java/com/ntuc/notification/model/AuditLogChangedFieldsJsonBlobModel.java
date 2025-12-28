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
 * The Blob model class for lazy loading the changedFieldsJson column in AuditLog.
 *
 * @author Brian Wing Shun Chan
 * @see AuditLog
 * @generated
 */
public class AuditLogChangedFieldsJsonBlobModel {

	public AuditLogChangedFieldsJsonBlobModel() {
	}

	public AuditLogChangedFieldsJsonBlobModel(long auditLogId) {
		_auditLogId = auditLogId;
	}

	public AuditLogChangedFieldsJsonBlobModel(
		long auditLogId, Blob changedFieldsJsonBlob) {

		_auditLogId = auditLogId;
		_changedFieldsJsonBlob = changedFieldsJsonBlob;
	}

	public long getAuditLogId() {
		return _auditLogId;
	}

	public void setAuditLogId(long auditLogId) {
		_auditLogId = auditLogId;
	}

	public Blob getChangedFieldsJsonBlob() {
		return _changedFieldsJsonBlob;
	}

	public void setChangedFieldsJsonBlob(Blob changedFieldsJsonBlob) {
		_changedFieldsJsonBlob = changedFieldsJsonBlob;
	}

	private long _auditLogId;
	private Blob _changedFieldsJsonBlob;

}