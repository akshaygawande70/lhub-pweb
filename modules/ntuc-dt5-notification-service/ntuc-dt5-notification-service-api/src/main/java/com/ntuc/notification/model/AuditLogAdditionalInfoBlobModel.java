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
 * The Blob model class for lazy loading the additionalInfo column in AuditLog.
 *
 * @author Brian Wing Shun Chan
 * @see AuditLog
 * @generated
 */
public class AuditLogAdditionalInfoBlobModel {

	public AuditLogAdditionalInfoBlobModel() {
	}

	public AuditLogAdditionalInfoBlobModel(long auditLogId) {
		_auditLogId = auditLogId;
	}

	public AuditLogAdditionalInfoBlobModel(
		long auditLogId, Blob additionalInfoBlob) {

		_auditLogId = auditLogId;
		_additionalInfoBlob = additionalInfoBlob;
	}

	public long getAuditLogId() {
		return _auditLogId;
	}

	public void setAuditLogId(long auditLogId) {
		_auditLogId = auditLogId;
	}

	public Blob getAdditionalInfoBlob() {
		return _additionalInfoBlob;
	}

	public void setAdditionalInfoBlob(Blob additionalInfoBlob) {
		_additionalInfoBlob = additionalInfoBlob;
	}

	private long _auditLogId;
	private Blob _additionalInfoBlob;

}