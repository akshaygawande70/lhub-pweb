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

package svc.ntuc.nlh.course.admin.service.impl;

import com.liferay.asset.entry.rel.model.AssetEntryAssetCategoryRel;
import com.liferay.asset.entry.rel.service.AssetEntryAssetCategoryRelLocalServiceUtil;
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetLinkConstants;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetLinkLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetVocabularyLocalServiceUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.constants.CourseAdminConstant;
import api.ntuc.nlh.content.util.ContentUtil;
import svc.ntuc.nlh.course.admin.exception.NoSuchCourseException;
import svc.ntuc.nlh.course.admin.model.Course;
import svc.ntuc.nlh.course.admin.service.base.CourseLocalServiceBaseImpl;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;

/**
 * The implementation of the course local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * <code>svc.ntuc.nlh.course.admin.service.CourseLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CourseLocalServiceBaseImpl
 */
@Component(property = "model.class.name=svc.ntuc.nlh.course.admin.model.Course", service = AopService.class)
public class CourseLocalServiceImpl extends CourseLocalServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use
	 * <code>svc.ntuc.nlh.course.admin.service.CourseLocalService</code> via
	 * injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use
	 * <code>svc.ntuc.nlh.course.admin.service.this</code>.
	 */
	private static Log log = LogFactoryUtil.getLog(CourseLocalServiceImpl.class);
	private String globalStatus = "status";
	private String globalGroupId = "groupId";
	private String globalContent = "content";

	public Course addCourse(long groupId, Date endDate, String venue, boolean allowOnlinePayment, String courseTitle,
			boolean allowWebRegistration, String description, int availability, String batchId, Date webExpiry,
			boolean fundedCourseFlag, String courseCode, double courseDuration, Date startDate, double courseFee,
			String courseType, boolean deleted, boolean updated, boolean popular) throws PortalException {

		Group group = groupLocalService.getGroup(groupId);

		long courseId = counterLocalService.increment(Course.class.getName());
		Course course = createCourse(courseId);
		course.setCompanyId(group.getCompanyId());
		course.setGroupId(groupId);
		course.setCreateDate(new Date());
		course.setEndDate(endDate);
		course.setVenue(venue);
		course.setAllowOnlinePayment(allowOnlinePayment);
		course.setCourseTitle(courseTitle);
		course.setAllowWebRegistration(allowWebRegistration);
		course.setDescription(description);
		course.setAvailability(availability);
		course.setBatchId(batchId);
		course.setWebExpiry(webExpiry);
		course.setFundedCourseFlag(fundedCourseFlag);
		course.setCourseCode(courseCode);
		course.setCourseDuration(courseDuration);
		course.setStartDate(startDate);
		course.setCourseFee(courseFee);
		course.setCourseType(courseType);
		course.setDeleted(deleted);
		course.setUpdated(updated);
		course.setPopular(popular);
		return super.addCourse(course);

	}

	public Course updateCourse(long courseId, Date endDate, String venue, boolean allowOnlinePayment,
			String courseTitle, boolean allowWebRegistration, String description, int availability, String batchId,
			Date webExpiry, boolean fundedCourseFlag, String courseCode, double courseDuration, Date startDate,
			double courseFee, String courseType, boolean deleted, boolean updated, boolean popular)
			throws PortalException {
		Course course = getCourse(courseId);
		course.setModifiedDate(new Date());
		course.setEndDate(endDate);
		course.setVenue(venue);
		course.setAllowOnlinePayment(allowOnlinePayment);
		course.setCourseTitle(courseTitle);
		course.setAllowWebRegistration(allowWebRegistration);
		course.setDescription(description);
		course.setAvailability(availability);
		course.setBatchId(batchId);
		course.setWebExpiry(webExpiry);
		course.setFundedCourseFlag(fundedCourseFlag);
		course.setCourseCode(courseCode);
		course.setCourseDuration(courseDuration);
		course.setStartDate(startDate);
		course.setCourseFee(courseFee);
		course.setCourseType(courseType);
		course.setDeleted(deleted);
		course.setUpdated(updated);
		course.setPopular(popular);
		return super.updateCourse(course);
	}

//	finder
	public List<Course> getCourseByCourseCode(long groupId, String courseCode, boolean deleted) {
		return coursePersistence.findByCourseCode(groupId, courseCode, deleted);
	}

	public List<Course> getCourseByCourseCode(long groupId, String courseCode, boolean deleted, int start, int end) {
		return coursePersistence.findByCourseCode(groupId, courseCode, deleted, start, end);
	}

	public List<Course> getCourseByCourseCode(long groupId, String courseCode, boolean deleted, int start, int end,
			OrderByComparator<Course> orderByComparator) {
		return coursePersistence.findByCourseCode(groupId, courseCode, deleted, start, end, orderByComparator);
	}

	public List<Course> getCourseByCourseTitle(long groupId, String courseTitle, boolean deleted) {
		return coursePersistence.findByCourseTitle(groupId, courseTitle, deleted);
	}

	public List<Course> getCourseByCourseTitle(long groupId, String courseTitle, boolean deleted, int start, int end) {
		return coursePersistence.findByCourseTitle(groupId, courseTitle, deleted, start, end);
	}

	public List<Course> getCourseByCourseTitle(long groupId, String courseTitle, boolean deleted, int start, int end,
			OrderByComparator<Course> orderByComparator) {
		return coursePersistence.findByCourseTitle(groupId, courseTitle, deleted, start, end, orderByComparator);
	}

	public Course getCourseByCourseCodeAndBatchIdActive(long groupId, String courseCode, String batchId,
			boolean deleted) throws NoSuchCourseException {
		return coursePersistence.findByCourseCodeBatchIdActive(groupId, courseCode, batchId, deleted);
	}

	public Course getCourseByCourseCodeAndBatchId(long groupId, String courseCode, String batchId)
			throws NoSuchCourseException {
		return coursePersistence.findByCourseCodeBatchId(groupId, courseCode, batchId);
	}

	public List<Course> getAllActiveCourse(long groupId) {
		return coursePersistence.findByActiveCourse(groupId, false);
	}

	public List<Course> getAllActiveAndUpdatedCourse(long groupId, boolean deleted, boolean updated) {
		return coursePersistence.findByActiveCourseUpdated(groupId, deleted, updated);
	}

	public List<Course> getAllActiveAndPopularCourse(long groupId, boolean deleted, boolean popular) {
		return coursePersistence.findByActivePopularCourse(groupId, deleted, popular);
	}

	public List<Course> getCoursesByKeywords(long groupId, String keywords, int start, int end,
			OrderByComparator<Course> orderByComparator) {
		return courseLocalService.dynamicQuery(getKeywordSearchDynamicQuery(groupId, keywords), start, end,
				orderByComparator);
	}

	public long getCoursesCountByKeywords(long groupId, String keywords) {
		return courseLocalService.dynamicQueryCount(getKeywordSearchDynamicQuery(groupId, keywords));
	}

	private DynamicQuery getKeywordSearchDynamicQuery(long groupId, String keywords) {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Course.class, getClassLoader());
		Conjunction conjunctionQuery = RestrictionsFactoryUtil.conjunction();
		conjunctionQuery.add(RestrictionsFactoryUtil.eq("groupId", groupId));
		conjunctionQuery.add(RestrictionsFactoryUtil.eq("deleted", false));
		dynamicQuery.add(conjunctionQuery);
		if (!Validator.isBlank(keywords)) {
			Disjunction disjunctionQuery = RestrictionsFactoryUtil.disjunction();

			disjunctionQuery.add(RestrictionsFactoryUtil.like("courseTitle", "%" + keywords + "%"));
			disjunctionQuery.add(RestrictionsFactoryUtil.like("description", "%" + keywords + "%"));
			disjunctionQuery.add(RestrictionsFactoryUtil.like("courseCode", "%" + keywords + "%"));
			disjunctionQuery.add(RestrictionsFactoryUtil.like("batchId", "%" + keywords + "%"));
			disjunctionQuery.add(RestrictionsFactoryUtil.like("venue", "%" + keywords + "%"));
			disjunctionQuery.add(RestrictionsFactoryUtil.like("courseType", "%" + keywords + "%"));
			dynamicQuery.add(disjunctionQuery);
		}
		return dynamicQuery;
	}
	
	public void moveSpecifiedApprovedCourse(long resourcePrimKey) {
		try {
			log.info("=========move start=========");
			Company company = CompanyLocalServiceUtil.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
			long groupId = GroupLocalServiceUtil.getGroup(company.getCompanyId(), CourseAdminConstant.GUEST_GROUP_NAME)
					.getGroupId();
			ParameterGroup groupGlobalParam = ParameterGroupLocalServiceUtil
					.getByCode(CourseAdminConstant.PARAMETER_GLOBAL_GROUP_CODE, false);
			Parameter globalEmailParam = ParameterLocalServiceUtil.getByGroupCode(groupId,
					groupGlobalParam.getParameterGroupId(), CourseAdminConstant.PARAMETER_GLOBAL_EMAIL_CODE, false);
			String email = globalEmailParam.getParamValue();
			long userId = UserLocalServiceUtil.getUserIdByEmailAddress(company.getCompanyId(), email);
			ParameterGroup groupCourseAdmin = ParameterGroupLocalServiceUtil
					.getByCode(CourseAdminConstant.PARAMETER_COURSE_ADMIN_GROUP_CODE, false);
			long siteGroupId = groupCourseAdmin.getGroupId();

			Parameter structureParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					groupCourseAdmin.getParameterGroupId(), CourseAdminConstant.PARAMETER_COURSE_STRUCTURE_CODE, false);
			DynamicQuery structureQuery = DDMStructureLocalServiceUtil.dynamicQuery();
			structureQuery.add(PropertyFactoryUtil.forName("name").like("%>" + structureParam.getParamValue() + "<%"));
			structureQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(siteGroupId));
			List<DDMStructure> ddmStructures = DDMStructureLocalServiceUtil.dynamicQuery(structureQuery, 0, 1);

			Parameter templateParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					groupCourseAdmin.getParameterGroupId(), CourseAdminConstant.PARAMETER_COURSE_TEMPLATE_CODE, false);
			DynamicQuery templateQuery = DDMTemplateLocalServiceUtil.dynamicQuery();
			templateQuery.add(PropertyFactoryUtil.forName("name").like("%>" + templateParam.getParamValue() + "<%"));
			templateQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(siteGroupId));
			List<DDMTemplate> ddmTemplates = DDMTemplateLocalServiceUtil.dynamicQuery(templateQuery);

			List<JournalArticle> movedJournals = JournalArticleLocalServiceUtil
					.getArticlesByResourcePrimKey(resourcePrimKey);

			ServiceContext serviceContext = new ServiceContext();
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);
			serviceContext.setScopeGroupId(siteGroupId);
			serviceContext.setWorkflowAction(WorkflowConstants.ACTION_PUBLISH);
			for (JournalArticle article : movedJournals) {
				this.executeMoveCourse(article, siteGroupId, ddmStructures, ddmTemplates, userId, serviceContext);
			}
		} catch (Exception e) {
			log.error("Found some error in code " + e.getMessage());
		}
		log.info("=========move end=========");
	}

	public void executeMoveCourse(JournalArticle article, long siteGroupId, List<DDMStructure> ddmStructures,
			List<DDMTemplate> ddmTemplates, long userId, ServiceContext serviceContext) {
		try {
//			Set<String> relatedArticleIds = getPopularArticles(article.getResourcePrimKey(), siteGroupId,
//					ddmStructures.get(0).getStructureKey(), ddmTemplates.get(0).getTemplateKey());
//			setRelatedAsset(siteGroupId, userId, article.getArticleId(), relatedArticleIds);

			long targetFolderId = getArticleFolderByThemeName(article, siteGroupId, userId);
			log.info("targetFolderid = " + targetFolderId);
			JournalArticleLocalServiceUtil.moveArticle(siteGroupId, article.getArticleId(), targetFolderId,
					serviceContext);
		} catch (Exception e) {
			log.error("Found some error in code " + e.getMessage());
		}
	}

	public Set<String> getPopularArticles(long resourcePrimaryKey, long groupId, String ddmStructureKey,
			String ddmTemplateKey) {
		Set<String> relatedArticleIds = new HashSet<>();
		List<Course> popularCourse = this.getAllActiveAndPopularCourse(groupId, false, true);

		List<AssetCategory> existingJournalCategories = AssetCategoryLocalServiceUtil
				.getCategories(JournalArticle.class.getName(), resourcePrimaryKey);
		AssetVocabulary topicVocabulary = AssetVocabularyLocalServiceUtil.fetchGroupVocabulary(groupId,
				CourseAdminConstant.COURSE_TOPIC_VOCAB_NAME);
		List<AssetCategory> existingTopicCategories = existingJournalCategories.stream()
				.filter(x -> x.getVocabularyId() == topicVocabulary.getVocabularyId()).collect(Collectors.toList());
		Set<Long> existingTopicCategoryIds = existingTopicCategories.stream().map(x -> x.getCategoryId())
				.collect(Collectors.toSet());
		for (Course course : popularCourse) {
			DynamicQuery articleQuery = JournalArticleLocalServiceUtil.dynamicQuery();
			articleQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(groupId));
			articleQuery.add(PropertyFactoryUtil.forName(globalContent).like("%[" + course.getCourseCode() + "]%"));
			articleQuery.add(PropertyFactoryUtil.forName(globalStatus).ne(WorkflowConstants.STATUS_IN_TRASH));
			List<JournalArticle> journalArticles = JournalArticleLocalServiceUtil.dynamicQuery(articleQuery);
			List<JournalArticle> filteredArticles = journalArticles.stream()
					.filter(x -> x.getTemplateId().equals(ddmTemplateKey) && x.getStructureId().equals(ddmStructureKey))
					.collect(Collectors.toList());

			if (!filteredArticles.isEmpty()) {
				List<AssetCategory> popularJournalCategories = AssetCategoryLocalServiceUtil
						.getCategories(JournalArticle.class.getName(), filteredArticles.get(0).getResourcePrimKey());
				Set<Long> popularTopicCategoryIds = popularJournalCategories.stream()
						.filter(x -> x.getVocabularyId() == topicVocabulary.getVocabularyId())
						.map(x -> x.getCategoryId()).collect(Collectors.toSet());
				for (long topicId : popularTopicCategoryIds) {
					if (existingTopicCategoryIds.contains(topicId)) {
						relatedArticleIds.add(filteredArticles.get(0).getArticleId());
					}
				}
			}
		}
		return relatedArticleIds;
	}

	public void updateCoursesFlagFromPopular(long groupId, String ddmStructureKey, String ddmTemplateKey) {
		try {
			List<Course> popularCourse = this.getAllActiveAndPopularCourse(groupId, false, true);
			List<String> distinctPopularCourse = popularCourse.stream().map(course -> course.getCourseCode()).distinct()
					.collect(Collectors.toList());
			List<JournalArticle> sameTopicWithPopularArticles = new ArrayList<>();
			Set<String> courseCodes = new HashSet<>();
			AssetVocabulary topicVocabulary = AssetVocabularyLocalServiceUtil.fetchGroupVocabulary(groupId,
					CourseAdminConstant.COURSE_TOPIC_VOCAB_NAME);
			Set<Long> popularTopicCategoryIds = new HashSet<>();

			for (String courseCode : distinctPopularCourse) {
//				log.info("coursecode = "+courseCode);
				DynamicQuery articleQuery = JournalArticleLocalServiceUtil.dynamicQuery();
				articleQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(groupId));
				articleQuery.add(PropertyFactoryUtil.forName(globalContent).like("%[" + courseCode + "]%"));
				articleQuery.add(PropertyFactoryUtil.forName(globalStatus).ne(WorkflowConstants.STATUS_IN_TRASH));
				List<JournalArticle> journalArticles = JournalArticleLocalServiceUtil.dynamicQuery(articleQuery);
				List<JournalArticle> filteredArticles = journalArticles.stream().filter(
						x -> x.getTemplateId().equals(ddmTemplateKey) && x.getStructureId().equals(ddmStructureKey))
						.collect(Collectors.toList());
//				log.info("filteredArticles.size() = "+filteredArticles.size());
				if (!filteredArticles.isEmpty()) {
					List<AssetCategory> popularJournalCategories = AssetCategoryLocalServiceUtil.getCategories(
							JournalArticle.class.getName(), filteredArticles.get(0).getResourcePrimKey());
//					log.info("popularJournalCategories.size() = "+popularJournalCategories.size());
					Set<Long> popularTopicCategoryId = popularJournalCategories.stream()
							.filter(x -> x.getVocabularyId() == topicVocabulary.getVocabularyId())
							.map(x -> x.getCategoryId()).collect(Collectors.toSet());
//					log.info("popularTopicCategoryId.size() = "+popularTopicCategoryId.size());
					popularTopicCategoryIds.addAll(popularTopicCategoryId);
				}
			}
//			log.info("popularTopicCategoryIds size = "+popularTopicCategoryIds.size());
			
			Set<AssetEntry> assetEntries = new HashSet<>();
			for (long id : popularTopicCategoryIds) {
				List<AssetEntryAssetCategoryRel> rels = AssetEntryAssetCategoryRelLocalServiceUtil
						.getAssetEntryAssetCategoryRelsByAssetCategoryId(id);
				for (AssetEntryAssetCategoryRel a : rels) {
					AssetEntry asset = AssetEntryLocalServiceUtil.getAssetEntry(a.getAssetEntryId());
					assetEntries.add(asset);
				}
			}
			
			for (AssetEntry a : assetEntries) {
				List<JournalArticle> journals = JournalArticleLocalServiceUtil
						.getArticlesByResourcePrimKey(a.getClassPK());
				if (!journals.isEmpty()) {
					sameTopicWithPopularArticles.add(journals.get(0));
				}
			}
//			log.info("sameTopicWithPopularArticles size = "+sameTopicWithPopularArticles.size());
			
			for (JournalArticle j : sameTopicWithPopularArticles) {
				String xmlContent = j.getContent();
				String courseCode = ContentUtil.getWebContentVal(xmlContent, 1, "courseCode", j.getDefaultLanguageId());
				courseCodes.add(courseCode);
			}
//			log.info("courseCodes size = "+courseCodes.size());

			for (String courseCode : courseCodes) {
//				log.info("couseCode = " + courseCode);
				List<Course> courses = this.getCourseByCourseCode(groupId, courseCode, false);
				for (Course co : courses) {
					co.setUpdated(true);
					this.updateCourse(co.getCourseId(), co.getEndDate(), co.getVenue(), co.getAllowOnlinePayment(),
							co.getCourseTitle(), co.getAllowWebRegistration(), co.getDescription(),
							co.getAvailability(), co.getBatchId(), co.getWebExpiry(), co.getFundedCourseFlag(),
							co.getCourseCode(), co.getCourseDuration(), co.getStartDate(), co.getCourseFee(),
							co.getCourseType(), co.getDeleted(), co.getUpdated(), co.getPopular());
				}
			}
		} catch (PortalException e) {
			log.error(e.getMessage());
		}
	}

	public void setRelatedAsset(long groupId, long userId, String currentArticleId, Set<String> targetArticleIds) {
		try {
			JournalArticle currentArticle = JournalArticleLocalServiceUtil.getArticle(groupId, currentArticleId);
			AssetEntry currentAssetEntry = AssetEntryLocalServiceUtil.getEntry(JournalArticle.class.getName(),
					currentArticle.getResourcePrimKey());

			Set<Long> targetEntryIds = new HashSet<>();

			for (String targetArticleId : targetArticleIds) {
				JournalArticle targetArticle = JournalArticleLocalServiceUtil.getArticle(groupId, targetArticleId);
				AssetEntry targetAssetEntry = AssetEntryLocalServiceUtil.getEntry(JournalArticle.class.getName(),
						targetArticle.getResourcePrimKey());

				targetEntryIds.add(targetAssetEntry.getEntryId());
			}

			AssetLinkLocalServiceUtil.deleteLinks(currentAssetEntry.getEntryId());

			for (long targetEntryId : targetEntryIds) {
				if (targetEntryId != currentAssetEntry.getEntryId()) {
					AssetLinkLocalServiceUtil.addLink(userId, targetEntryId, currentAssetEntry.getEntryId(),
							AssetLinkConstants.TYPE_RELATED, 0);
				}
			}
		} catch (PortalException e) {
			log.error("Found some error in code " + e.getMessage());
		}

	}

	public long getArticleFolderByThemeName(JournalArticle article, long groupId, long userId) {
		List<AssetCategory> journalCategories = AssetCategoryLocalServiceUtil
				.getCategories(JournalArticle.class.getName(), article.getResourcePrimKey());
		AssetVocabulary themeVocabulary = AssetVocabularyLocalServiceUtil.fetchGroupVocabulary(groupId,
				CourseAdminConstant.COURSE_THEME_VOCAB_NAME);
		List<AssetCategory> courseThemeCategories = journalCategories.stream()
				.filter(x -> x.getVocabularyId() == themeVocabulary.getVocabularyId()).collect(Collectors.toList());
		AssetCategory courseThemeCategory = courseThemeCategories.get(0);

		DynamicQuery folderQuery = JournalFolderLocalServiceUtil.dynamicQuery();
		folderQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(groupId));
		folderQuery.add(PropertyFactoryUtil.forName("name").eq(CourseAdminConstant.COURSE_CATEGORY_NAME));
		List<JournalFolder> coursesFolders = JournalFolderLocalServiceUtil.dynamicQuery(folderQuery);
		long parentFolderId = coursesFolders.get(0).getFolderId();

		DynamicQuery subfolderQuery = JournalFolderLocalServiceUtil.dynamicQuery();
		subfolderQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(groupId));
		subfolderQuery.add(PropertyFactoryUtil.forName("name").eq(courseThemeCategory.getName()));
		subfolderQuery.add(PropertyFactoryUtil.forName("parentFolderId").eq(parentFolderId));
		List<JournalFolder> subFolders = JournalFolderLocalServiceUtil.dynamicQuery(subfolderQuery);

		ServiceContext serviceContext = new ServiceContext();
		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setScopeGroupId(groupId);
		serviceContext.setWorkflowAction(WorkflowConstants.ACTION_PUBLISH);

		long themeFolderId = 0;
		if (!subFolders.isEmpty()) {
			themeFolderId = subFolders.get(0).getFolderId();
		} else {
			try {
				themeFolderId = JournalFolderLocalServiceUtil
						.addFolder(userId, groupId, parentFolderId, courseThemeCategory.getName(), null, serviceContext)
						.getFolderId();
			} catch (PortalException e) {
				log.error("Found some error in code " + e.getMessage());
			}
		}

		return themeFolderId;
	}
	
	@SuppressWarnings("unchecked")
	public List<JSONObject> getLatestJournalByCourseCode(long groupId, String courseCode, String structureKey, String templateKey) {
		Session session = null;
		String sql = "";
		List<JSONObject> journals = new ArrayList<>();
		try {
			session = coursePersistence.openSession();
			sql = "select j.uuid_ , j.articleId, j.resourcePrimKey from JournalArticle j where \r\n" + 
					"j.groupId = "+groupId+" and \r\n" + 
					"j.content like '%["+courseCode+"]%'\r\n" + 
					"and DDMStructureKey = '"+structureKey+"'\r\n" + 
					"and DDMTemplateKey = '"+templateKey+"'\r\n" + 
					"and j.status <> 8 \r\n" +
					"and j.version = (select max(j1.version) from JournalArticle j1 where j1.articleId = j.articleId)";
			SQLQuery query = session.createSQLQuery(sql);
			List<Object[]> listObject = query.list();
			for (Object[] o : listObject) {
				JSONObject json = JSONFactoryUtil.createJSONObject();
				json.put("uuid", o[0]);
				json.put("articleId", o[1]);
				json.put("resourcePrimKey", o[2]);
				journals.add(json);
			}
		}catch (Exception e) {
			log.info("SQL : " + sql);
			log.error("Error while get all latest Journal: " + e.getMessage());
		} finally {
			coursePersistence.closeSession(session);
		}
		return journals;
	}
	
	@SuppressWarnings("unchecked")
	public List<JSONObject> getLatestJournalByCourseCode(long groupId, String structureKey, String templateKey) {
		Session session = null;
		String sql = "";
		List<JSONObject> journals = new ArrayList<>();
		try {
			session = coursePersistence.openSession();
			sql = "select j.uuid_ , j.articleId, j.resourcePrimKey from JournalArticle j where \r\n" + 
					"j.groupId = "+groupId+" \r\n" + 
					"and j.DDMStructureKey = '"+structureKey+"'\r\n" + 
					"and j.DDMTemplateKey = '"+templateKey+"'\r\n" + 
					"and j.status <> 8 \r\n" +
					"and j.version = (select max(j1.version) from JournalArticle j1 where j1.articleId = j.articleId) \r\n" +
					"and ExtractValue(j.content,'//root//dynamic-element[@name=\"courseCode\"]//dynamic-content[@language-id=\"en_GB\"]') \r\n" +
					"not in(select DISTINCT nc.courseCode from ntuc_course nc where nc.deleted = FALSE)";
					
//			for(String courseCode : courseCodes) {
//				sql = sql + " and j.content not like '%["+courseCode+"]%'";
//			}
			
			SQLQuery query = session.createSQLQuery(sql);
			List<Object[]> listObject = query.list();
			for (Object[] o : listObject) {
				JSONObject json = JSONFactoryUtil.createJSONObject();
				json.put("uuid", o[0]);
				json.put("articleId", o[1]);
				json.put("resourcePrimKey", o[2]);
				journals.add(json);
			}
		}catch (Exception e) {
			log.info("SQL : " + sql);
			log.error("Error while get all latest Journal: " + e.getMessage());
		} finally {
			coursePersistence.closeSession(session);
		}
		return journals;
	}
	

	@Override
	public Course addCourse(Course course) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public Course updateCourse(Course course) {
		throw new UnsupportedOperationException("Not supported");
	}
}