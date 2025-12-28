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

package svc.ntuc.nlh.parameter.service.impl;


import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Junction;
import com.liferay.portal.kernel.dao.orm.Order;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;

import svc.ntuc.nlh.parameter.exception.NoSuchParameterException;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.service.base.ParameterLocalServiceBaseImpl;

/**
 * The implementation of the parameter local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>svc.ntuc.nlh.parameter.service.ParameterLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ParameterLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=svc.ntuc.nlh.parameter.model.Parameter",
	service = AopService.class
)
public class ParameterLocalServiceImpl extends ParameterLocalServiceBaseImpl {

	Log log = LogFactoryUtil.getLog(ParameterLocalServiceImpl.class);

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use
	 * <code>svc.afi.parameter.service.ParameterLocalService</code> via injection or
	 * a <code>org.osgi.util.tracker.ServiceTracker</code> or use
	 * <code>svc.afi.parameter.service.ParameterLocalServiceUtil</code>.
	 */

	public List<Parameter> getByParameterGroupId(long parameterGroupId, boolean deleted) {
		log.info("enter parameter service, get by parameter group ID");
		return parameterPersistence.findByGroup(parameterGroupId, deleted);
	}

	public List<Parameter> getByGroupIdCode(long groupId, String paramCode, boolean deleted) {
		log.info("enter parameter service, get by parameter group id & param code");
		return parameterPersistence.findByGroupSiteCode(groupId, paramCode, deleted);
	}
	
	public Parameter getByGroupCode(long groupId, long parameterGroupId, String paramCode, boolean deleted) {
		log.info("enter parameter service, get by parameter group ID, param groupId, paramcode");
		try {
			return parameterPersistence.findByGroupCode(groupId, parameterGroupId, paramCode, deleted);
		} catch (NoSuchParameterException e) {
			log.error("enter parameter service, get by parameter group ID, param groupId, paramcode");
			return null;
		}
	}

	public Parameter getByCode(String paramCode, boolean deleted) {
		log.info("enter parameter service, get by code");
		try {
			return parameterPersistence.findByCode(paramCode, deleted);
		} catch (NoSuchParameterException e) {
			log.error("enter parameter service, get by code");
			return null;
		}
	}

	public List<Parameter> getParameters(int start, int end, Order order, String search, Long groupId) {
		log.info("enter parameter service, get parameter list");
		List<Parameter> parameters = new ArrayList<>();
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Parameter.class, getClassLoader());

		if (groupId > 0) {
			dynamicQuery.add(RestrictionsFactoryUtil.eq("groupId", groupId));
		}

		Junction junction = RestrictionsFactoryUtil.disjunction();
		junction.add(RestrictionsFactoryUtil.ilike("paramCode", "%" + search + "%"));
		junction.add(RestrictionsFactoryUtil.ilike("paramName", "%" + search + "%"));
		junction.add(RestrictionsFactoryUtil.ilike("paramValue", "%" + search + "%"));
		junction.add(RestrictionsFactoryUtil.ilike("description", "%" + search + "%"));
		dynamicQuery.add(junction);
		dynamicQuery.add(RestrictionsFactoryUtil.eq("deleted", false));
		dynamicQuery.addOrder(order);

		parameters = parameterLocalService.dynamicQuery(dynamicQuery, start, end);
		return parameters;
	}

	public int countData(Long groupId, boolean deleted) {
		log.info("enter parameter service, count Data");
		if (groupId > 0) {
			return parameterPersistence.countByGroupSite(groupId, deleted);
		}
		return parameterPersistence.countAll();
	}
}