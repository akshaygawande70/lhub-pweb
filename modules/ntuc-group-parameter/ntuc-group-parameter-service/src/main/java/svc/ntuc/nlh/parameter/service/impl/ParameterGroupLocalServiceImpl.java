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
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Junction;
import com.liferay.portal.kernel.dao.orm.Order;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;

import svc.ntuc.nlh.parameter.exception.NoSuchParameterGroupException;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.base.ParameterGroupLocalServiceBaseImpl;

/**
 * The implementation of the parameter group local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>svc.ntuc.nlh.parameter.service.ParameterGroupLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ParameterGroupLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=svc.ntuc.nlh.parameter.model.ParameterGroup",
	service = AopService.class
)
public class ParameterGroupLocalServiceImpl
	extends ParameterGroupLocalServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use <code>svc.ntuc.nlh.parameter.service.ParameterGroupLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil</code>.
	 */
	Log log = LogFactoryUtil.getLog(ParameterGroupLocalServiceImpl.class);
	
	
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use
	 * <code>svc.afi.parameter.service.ParameterGroupLocalService</code> via
	 * injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use
	 * <code>svc.afi.parameter.service.ParameterGroupLocalServiceUtil</code>.
	 */
	
	public ParameterGroup createParameterGroup(long groupId, long paramgroupId, long parentGroupId, String code, String name, String descriptions, ThemeDisplay themeDisplay) {
		ParameterGroup pg = null;
		pg = parameterGroupPersistence.create(paramgroupId);
		
		pg.setGroupId(groupId);
		pg.setCompanyId(themeDisplay.getCompanyId());
		pg.setCreatedBy(themeDisplay.getUserId());
		pg.setCreatedDate(new Date());
		
		pg.setModifiedBy(themeDisplay.getUserId());
		pg.setModifiedDate(new Date());
		pg.setParentId(parentGroupId);
		pg.setGroupName(name);
		pg.setGroupCode(code);
		pg.setDescription(descriptions);
		pg.setDeleted(false);

		pg = parameterGroupPersistence.update(pg);
		
		return pg;
	}

	public ParameterGroup updateParameterGroup(long groupId, long paramgroupId, long parentGroupId, String code, String name, String descriptions, ThemeDisplay themeDisplay) throws NoSuchParameterGroupException {
		ParameterGroup pg = null;
		pg = parameterGroupPersistence.findByPrimaryKey(paramgroupId);
		
		
		pg.setModifiedBy(themeDisplay.getUserId());
		pg.setModifiedDate(new Date());
		pg.setParentId(parentGroupId);
		pg.setGroupName(name);
		pg.setGroupCode(code);
		pg.setDescription(descriptions);
		pg.setDeleted(false);

		pg = parameterGroupPersistence.update(pg);
		
		return pg;
	}
	public List<ParameterGroup> getParameterGroupsByGroupId(long groupId, boolean deleted) {
		log.info("get parameter group by groupId");
		return parameterGroupPersistence.findByGroupSite(groupId, deleted);
	}

	public List<ParameterGroup> getByGroupIdCode(long groupId, String code, boolean deleted) {
		log.info("get parameter group List by groupId & code");
		return parameterGroupPersistence.findByGroupSiteCode(groupId, code, deleted);
	}

	public ParameterGroup getByCode(String code, boolean deleted) {
		log.info("get parameter group by code");
		try {
			return parameterGroupPersistence.findByCode(code, deleted);
		} catch (NoSuchParameterGroupException e) {
			log.error("get parameter group by groupId");
			return null;
		}
	}

	public List<ParameterGroup> getParameterGroups(int start, int end, Order order, String search, Long groupId) {
		log.info("get parameter group list by start end");
		List<ParameterGroup> parameterGroups = new ArrayList<ParameterGroup>();
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ParameterGroup.class, getClassLoader());

		if (groupId > 0) {
			dynamicQuery.add(RestrictionsFactoryUtil.eq("groupId", groupId));
		}

		if (Validator.isNotNull(search)) {
			Junction junction = RestrictionsFactoryUtil.disjunction();
			junction.add(RestrictionsFactoryUtil.ilike("groupName", "%" + search + "%"));
			junction.add(RestrictionsFactoryUtil.ilike("groupCode", "%" + search + "%"));
			dynamicQuery.add(junction);
		}

		if (Validator.isNotNull(order)) {
			dynamicQuery.addOrder(order);
		}
		
		dynamicQuery.add(RestrictionsFactoryUtil.eq("deleted", false));
		parameterGroups = parameterGroupLocalService.dynamicQuery(dynamicQuery, start, end);
		return parameterGroups;
	}

	public int countData(Long groupId, boolean deleted) {
		log.info("get parameter count group list by group id ");
		if (groupId > 0) {
			return parameterGroupPersistence.countByGroupSite(groupId, deleted);
		}
		return parameterGroupPersistence.countAll();
	}

	public List<ParameterGroup> getParameterGroupByKeywords(long groupId, String keywords, int start, int end,
			OrderByComparator<ParameterGroup> orderByComparator) {
		log.info("get parameter group list by keyword ");
		return parameterGroupLocalService.dynamicQuery(getKeywordSearchDynamicQuery(groupId, keywords), start, end,
				orderByComparator);
	}

	public long getParameterGroupCountByKeywords(long groupId, String keywords) {
		log.info("get parameter group  by keyword & group Id");
		return parameterGroupLocalService.dynamicQueryCount(getKeywordSearchDynamicQuery(groupId, keywords));
	}

	private DynamicQuery getKeywordSearchDynamicQuery(long groupId, String keywords) {
		log.info("get parameter group list by keyword dynamic query ");
		DynamicQuery dynamicQuery = dynamicQuery().add(RestrictionsFactoryUtil.eq("groupId", groupId));

		if (Validator.isNotNull(keywords)) {
			Disjunction disjunctionQuery = RestrictionsFactoryUtil.disjunction();

			disjunctionQuery.add(RestrictionsFactoryUtil.like("groupName", "%" + keywords + "%"));
			disjunctionQuery.add(RestrictionsFactoryUtil.like("description", "%" + keywords + "%"));
			dynamicQuery.add(disjunctionQuery);
		}

		return dynamicQuery;
	}
	
	public List<ParameterGroup> getByParentId(long parentId, boolean deleted) {
		log.info("get parameter group list by parent id");
		return parameterGroupPersistence.findByParentId(parentId, deleted);
	}
	
	
}