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

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model interface for the AuditLog service. Represents a row in the &quot;ntuc_audit_log&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see AuditLogModel
 * @generated
 */
@ImplementationClassName("com.ntuc.notification.model.impl.AuditLogImpl")
@ProviderType
public interface AuditLog extends AuditLogModel, PersistedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.ntuc.notification.model.impl.AuditLogImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<AuditLog, Long> AUDIT_LOG_ID_ACCESSOR =
		new Accessor<AuditLog, Long>() {

			@Override
			public Long get(AuditLog auditLog) {
				return auditLog.getAuditLogId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<AuditLog> getTypeClass() {
				return AuditLog.class;
			}

		};

}