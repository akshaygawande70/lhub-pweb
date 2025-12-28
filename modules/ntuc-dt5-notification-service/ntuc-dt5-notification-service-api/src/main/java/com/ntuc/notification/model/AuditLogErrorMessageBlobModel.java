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
 * The Blob model class for lazy loading the errorMessage column in AuditLog.
 *
 * @author Brian Wing Shun Chan
 * @see AuditLog
 * @generated
 */
public class AuditLogErrorMessageBlobModel {

	public AuditLogErrorMessageBlobModel() {
	}

	public AuditLogErrorMessageBlobModel(long auditLogId) {
		_auditLogId = auditLogId;
	}

	public AuditLogErrorMessageBlobModel(
		long auditLogId, Blob errorMessageBlob) {

		_auditLogId = auditLogId;
		_errorMessageBlob = errorMessageBlob;
	}

	public long getAuditLogId() {
		return _auditLogId;
	}

	public void setAuditLogId(long auditLogId) {
		_auditLogId = auditLogId;
	}

	public Blob getErrorMessageBlob() {
		return _errorMessageBlob;
	}

	public void setErrorMessageBlob(Blob errorMessageBlob) {
		_errorMessageBlob = errorMessageBlob;
	}

	private long _auditLogId;
	private Blob _errorMessageBlob;

}