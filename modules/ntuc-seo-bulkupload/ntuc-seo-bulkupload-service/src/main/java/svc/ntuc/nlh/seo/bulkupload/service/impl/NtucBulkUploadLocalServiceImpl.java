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

package svc.ntuc.nlh.seo.bulkupload.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletException;
import org.osgi.service.component.annotations.Component;
import svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload;
import svc.ntuc.nlh.seo.bulkupload.service.base.NtucBulkUploadLocalServiceBaseImpl;

/**
 * The implementation of the ntuc bulk upload local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>svc.ntuc.nlh.seo.bulkupload.service.NtucBulkUploadLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see NtucBulkUploadLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload",
	service = AopService.class
)
public class NtucBulkUploadLocalServiceImpl extends NtucBulkUploadLocalServiceBaseImpl {
	@Override
	@Indexable(type = IndexableType.REINDEX)
	public NtucBulkUpload addNtucBulkUpload(long groupId, long companyId, long userId, String userName, String fileName, String rowData) throws PortletException {
		
		// Validate Data
		validateData(fileName ,rowData);
		
		// Generate primary key id
		long ntucBulkUploadId = counterLocalService.increment(NtucBulkUpload.class.getName());
		
		NtucBulkUpload ntucBulkUpload = createNtucBulkUpload(ntucBulkUploadId);
		ntucBulkUpload.setGroupId(groupId);
		ntucBulkUpload.setCompanyId(companyId);
		ntucBulkUpload.setUserId(userId);
		ntucBulkUpload.setUserName(userName);
		ntucBulkUpload.setCreateDate(new Date());
		ntucBulkUpload.setFileName(fileName);
		ntucBulkUpload.setRowData(rowData);
		ntucBulkUpload = super.addNtucBulkUpload(ntucBulkUpload);
		return ntucBulkUpload;
	}

	@Override
	@Indexable(type = IndexableType.REINDEX)
	public NtucBulkUpload updateNtucBulkUpload(long ntucBulkUploadId, long groupId ,long companyId, long userId, String userName, String fileName, String rowData) throws PortletException, PortalException {
		
		// Validate Data
		if(Validator.isNull(ntucBulkUploadId)) {
			throw new PortletException("ntucBulkUploadId PkId is Missing...");
		}
		validateData(fileName ,rowData);
		
		NtucBulkUpload ntucBulkUpload = getNtucBulkUpload(ntucBulkUploadId);
		ntucBulkUpload.setGroupId(groupId);
		ntucBulkUpload.setCompanyId(companyId);
		ntucBulkUpload.setUserId(userId);
		ntucBulkUpload.setUserName(userName);
		ntucBulkUpload.setModifiedDate(new Date());
		ntucBulkUpload.setFileName(fileName);
		ntucBulkUpload.setRowData(rowData);
		ntucBulkUpload = super.updateNtucBulkUpload(ntucBulkUpload);
		return ntucBulkUpload;
	}
	
	@Override
	@Indexable(type = IndexableType.DELETE)
	public NtucBulkUpload deleteNtucBulkUpload(NtucBulkUpload ntucBulkUpload) {
		ntucBulkUploadPersistence.remove(ntucBulkUpload);
		return ntucBulkUpload;
	}
	
	private void validateData(String fileName, String rowData) throws PortletException {
		if (Validator.isBlank(fileName)) {
			throw new PortletException("File Name is Missing...");
		}
		if (Validator.isBlank(rowData)) {
			throw new PortletException("Row Data is Missing...");
		}
		
	}
	
	public 	NtucBulkUpload findByNtucBulkUploadId(long ntucBulkUploadId) {
		return ntucBulkUploadPersistence.fetchByntucBulkUploadId(ntucBulkUploadId);
	}
	
	private DynamicQuery getKeywordSearchDynamicQuery(long groupId, long companyId, String keywords) {
		// Site Related Part Of The Search
		DynamicQuery dynamicQuery = dynamicQuery().add(RestrictionsFactoryUtil.eq("groupId", groupId));
		if (Validator.isNotNull(keywords)) {
			Disjunction disjunctionQuery = RestrictionsFactoryUtil.disjunction();
			disjunctionQuery.add(RestrictionsFactoryUtil.like("fileName", "%" + keywords + "%"));
			disjunctionQuery.add(RestrictionsFactoryUtil.like("userName", "%" + keywords + "%"));
			dynamicQuery.add(disjunctionQuery);
		}
		return dynamicQuery;
	}

	public long getNtucBulkUploadCountsByKeywords(long groupId, long companyId, String keywords) {
		return dynamicQueryCount(getKeywordSearchDynamicQuery(groupId, companyId, keywords));
	}

	public List<NtucBulkUpload> getNtucBulkUploadByKeywords(long groupId, long companyId, String keywords, int start,
			int end, OrderByComparator<NtucBulkUpload> orderByComparator) {
		return dynamicQuery(getKeywordSearchDynamicQuery(groupId, companyId, keywords), start, end, orderByComparator);
	}
}