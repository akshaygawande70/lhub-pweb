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
 * The extended model interface for the NtucSB service. Represents a row in the &quot;ntuc_service_dt_notification&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see NtucSBModel
 * @generated
 */
@ImplementationClassName("com.ntuc.notification.model.impl.NtucSBImpl")
@ProviderType
public interface NtucSB extends NtucSBModel, PersistedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.ntuc.notification.model.impl.NtucSBImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<NtucSB, Long> NTUC_DT_ID_ACCESSOR =
		new Accessor<NtucSB, Long>() {

			@Override
			public Long get(NtucSB ntucSB) {
				return ntucSB.getNtucDTId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<NtucSB> getTypeClass() {
				return NtucSB.class;
			}

		};

}