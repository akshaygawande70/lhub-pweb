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

package com.ntuc.notification.model.impl;

/**
 * The extended model implementation for the NtucSB service. Represents a row in the &quot;ntuc_service_dt&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.ntuc.notification.model.NtucSB</code> interface.
 * </p>
 *
 * @author Brian Wing Shun Chan
 */
public class NtucSBImpl extends NtucSBBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. All methods that expect a ntuc sb model instance should use the {@link com.ntuc.notification.model.NtucSB} interface instead.
	 */
	public NtucSBImpl() {
	}
	
	@Override
	public boolean getCanRetry() {
	    return true; // Always true
	}

	@Override
	public boolean isCanRetry() {
	    return true; // For boolean-style accessor
	}

	@Override
	public void setCanRetry(boolean canRetry) {
	    // Intentionally no-op to ignore any attempts to set it
	}

}