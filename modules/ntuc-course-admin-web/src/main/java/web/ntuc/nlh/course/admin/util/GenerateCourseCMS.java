package web.ntuc.nlh.course.admin.util;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetLink;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetCategoryServiceUtil;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetLinkLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetVocabularyLocalServiceUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.journal.constants.JournalArticleConstants;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import api.ntuc.nlh.content.util.ContentUtil;
import svc.ntuc.nlh.course.admin.model.Course;
import svc.ntuc.nlh.course.admin.service.CourseLocalServiceUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.nlh.course.admin.constants.CourseAdminWebPortletKeys;

public class GenerateCourseCMS {

	public GenerateCourseCMS() {
		// Do nothing
	}

	private static Log log = LogFactoryUtil.getLog(GenerateCourseCMS.class);

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private String globalStatus = "status";
	private String globalGroupId = "groupId";
	private String globalContent = "content";

	public String generatedXml(String courseTitle, String venue, String description, double courseFee, int availability,
			String batchId, boolean fundedCourseFlag, String courseCode, double courseDuration, Date startDate,
			Date endDate, boolean popular) {
		String formatedStartDate = Validator.isNull(startDate) ? "" : this.sdf.format(startDate);
		String formatedEndDate = Validator.isNull(endDate) ? "" : this.sdf.format(endDate);
		String localeGB = LocaleUtil.UK.toString();
		String localeUS = LocaleUtil.US.toString();

		return "<?xml version=\"1.0\"?>\r\n" + "\r\n" + "<root available-locales=\"" + localeUS + "," + localeGB
				+ "\" default-locale=\"" + localeGB + "\">\r\n"
				+ "<dynamic-element name=\"courseTitle\" type=\"text\" index-type=\"keyword\" instance-id=\"mmkaqvnn\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[" + courseTitle
				+ "]]></dynamic-content>\r\n" + "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA["
				+ courseTitle + "]]></dynamic-content>\r\n" + "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"batchID\" type=\"text\" index-type=\"keyword\" instance-id=\"ivlldvrj\">\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[" + batchId + "]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"courseCode\" type=\"text\" index-type=\"keyword\" instance-id=\"iwcasyuf\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[" + courseCode
				+ "]]></dynamic-content>\r\n" + "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA["
				+ courseCode + "]]></dynamic-content>\r\n" + "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"startDate\" type=\"ddm-date\" index-type=\"keyword\" instance-id=\"rzrynlhu\">\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[" + formatedStartDate
				+ "]]></dynamic-content>\r\n" + "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"endDate\" type=\"ddm-date\" index-type=\"keyword\" instance-id=\"meloxrhj\">\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[" + formatedEndDate
				+ "]]></dynamic-content>\r\n" + "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"venue\" type=\"text\" index-type=\"keyword\" instance-id=\"zkrwrhmz\">\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[" + venue + "]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"image\" type=\"image\" index-type=\"text\" instance-id=\"kayftqur\">\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"courseSchedule\" type=\"text\" index-type=\"keyword\" instance-id=\"uxxdvvdo\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[Course Schedule]]></dynamic-content>\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[Course Schedule]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"courseDescription\" type=\"text\" index-type=\"keyword\" instance-id=\"seqhzxpr\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[" + description
				+ "]]></dynamic-content>\r\n" + "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA["
				+ description + "]]></dynamic-content>\r\n" + "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"CourseDuration\" type=\"text\" index-type=\"keyword\" instance-id=\"iruegwvn\">\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[" + courseDuration
				+ "]]></dynamic-content>\r\n" + "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"modeofAssesment\" type=\"text\" index-type=\"keyword\" instance-id=\"ubxzupkp\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"whoShouldAttend\" type=\"text_box\" index-type=\"text\" instance-id=\"nclwmasw\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"noStudentEnrolled\" type=\"text\" index-type=\"keyword\" instance-id=\"ijeicccp\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[" + availability
				+ "]]></dynamic-content>\r\n" + "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA["
				+ availability + "]]></dynamic-content>\r\n" + "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"courseOverview\" type=\"text\" index-type=\"keyword\" instance-id=\"dnvyydxn\">\r\n"
				+ "<dynamic-element name=\"DetailCourseOverview\" instance-id=\"ltdlrmhw\" type=\"text_area\" index-type=\"text\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n" + "<dynamic-content language-id=\"" + localeUS
				+ "\"><![CDATA[Course Overview]]></dynamic-content>\r\n" + "<dynamic-content language-id=\"" + localeGB
				+ "\"><![CDATA[Course Overview]]></dynamic-content>\r\n" + "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"courseObjectives\" type=\"text\" index-type=\"keyword\" instance-id=\"mjjykuyk\">\r\n"
				+ "<dynamic-element name=\"detailCourseObjectives\" instance-id=\"vuebsmyn\" type=\"text_area\" index-type=\"text\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n" + "<dynamic-content language-id=\"" + localeUS
				+ "\"><![CDATA[Course Objectives]]></dynamic-content>\r\n" + "<dynamic-content language-id=\""
				+ localeGB + "\"><![CDATA[Course Objectives]]></dynamic-content>\r\n" + "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"prerequisites\" type=\"text\" index-type=\"keyword\" instance-id=\"ndumspqs\">\r\n"
				+ "<dynamic-element name=\"detailPrerequisites\" instance-id=\"vsohkufk\" type=\"text_area\" index-type=\"text\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n" + "<dynamic-content language-id=\"" + localeUS
				+ "\"><![CDATA[Pre-requisites]]></dynamic-content>\r\n" + "<dynamic-content language-id=\"" + localeGB
				+ "\"><![CDATA[Pre-requisites]]></dynamic-content>\r\n" + "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"courseOutline\" type=\"text\" index-type=\"keyword\" instance-id=\"hqriuymk\">\r\n"
				+ "<dynamic-element name=\"detailCourseOutline\" instance-id=\"yacysyfa\" type=\"text_area\" index-type=\"text\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n" + "<dynamic-content language-id=\"" + localeUS
				+ "\"><![CDATA[Course Outline]]></dynamic-content>\r\n" + "<dynamic-content language-id=\"" + localeGB
				+ "\"><![CDATA[Course Outline]]></dynamic-content>\r\n" + "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"certificateObtainedAndConferredBy\" type=\"text\" index-type=\"keyword\" instance-id=\"wzpwpfcl\">\r\n"
				+ "<dynamic-element name=\"detailCertificateObtainedAndConferredBy\" instance-id=\"eakogijw\" type=\"text_area\" index-type=\"text\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n" + "<dynamic-content language-id=\"" + localeUS
				+ "\"><![CDATA[Certificate Obtained and Conferred by]]></dynamic-content>\r\n"
				+ "<dynamic-content language-id=\"" + localeGB
				+ "\"><![CDATA[Certificate Obtained and Conferred by]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"whatsInItForMe\" type=\"text\" index-type=\"keyword\" instance-id=\"rsmyqfnz\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS
				+ "\"><![CDATA[What's In It for Me]]></dynamic-content>\r\n" + "<dynamic-content language-id=\""
				+ localeGB + "\"><![CDATA[What's In It for Me]]></dynamic-content>\r\n" + "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"detailWhatsInItforMe\" type=\"text_area\" index-type=\"text\" instance-id=\"ubxqnctk\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"additionalDetails\" type=\"text\" index-type=\"keyword\" instance-id=\"kxukbety\">\r\n"
				+ "<dynamic-element name=\"detailAdditionalDetails\" instance-id=\"zpocowbu\" type=\"text_area\" index-type=\"text\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n" + "<dynamic-content language-id=\"" + localeUS
				+ "\"><![CDATA[Additional Details]]></dynamic-content>\r\n" + "<dynamic-content language-id=\""
				+ localeGB + "\"><![CDATA[Additional Details]]></dynamic-content>\r\n" + "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"popular\" type=\"boolean\" index-type=\"keyword\" instance-id=\"evoxnhnw\">\r\n"
				+ "<dynamic-content language-id=\"" + localeUS + "\"><![CDATA[" + popular + "]]></dynamic-content>\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[" + popular + "]]></dynamic-content>\r\n"
				+ "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"price\" type=\"ddm-number\" index-type=\"keyword\" instance-id=\"xtpgzamk\">\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[" + courseFee
				+ "]]></dynamic-content>\r\n" + "</dynamic-element>\r\n"
				+ "<dynamic-element name=\"funded\" type=\"boolean\" index-type=\"keyword\" instance-id=\"dawwuxmy\">\r\n"
				+ "<dynamic-content language-id=\"" + localeGB + "\"><![CDATA[" + fundedCourseFlag
				+ "]]></dynamic-content>\r\n" + "</dynamic-element>\r\n" + "</root>";
	}

	public void generate() {
		log.info("=========generate start========= ");
		long startTime = System.currentTimeMillis();
		try {
			Company company = CompanyLocalServiceUtil.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
			long groupId = GroupLocalServiceUtil
					.getGroup(company.getCompanyId(), CourseAdminWebPortletKeys.GUEST_GROUP_NAME).getGroupId();
			ParameterGroup groupGlobalParam = ParameterGroupLocalServiceUtil
					.getByCode(CourseAdminWebPortletKeys.PARAMETER_GLOBAL_GROUP_CODE, false);
			Parameter globalEmailParam = ParameterLocalServiceUtil.getByGroupCode(groupId,
					groupGlobalParam.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_GLOBAL_EMAIL_CODE,
					false);
			String email = globalEmailParam.getParamValue();
			long userId = UserLocalServiceUtil.getUserIdByEmailAddress(company.getCompanyId(), email);

			ParameterGroup groupCourseAdmin = ParameterGroupLocalServiceUtil
					.getByCode(CourseAdminWebPortletKeys.PARAMETER_COURSE_ADMIN_GROUP_CODE, false);
			long siteGroupId = groupCourseAdmin.getGroupId();

			Parameter structureParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					groupCourseAdmin.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_COURSE_STRUCTURE_CODE,
					false);
			DynamicQuery structureQuery = DDMStructureLocalServiceUtil.dynamicQuery();
			structureQuery.add(PropertyFactoryUtil.forName("name").like("%>" + structureParam.getParamValue() + "<%"));
			structureQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(siteGroupId));
			List<DDMStructure> ddmStructures = DDMStructureLocalServiceUtil.dynamicQuery(structureQuery, 0, 1);

			Parameter templateParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					groupCourseAdmin.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_COURSE_TEMPLATE_CODE,
					false);

			DynamicQuery templateQuery = DDMTemplateLocalServiceUtil.dynamicQuery();
			templateQuery.add(PropertyFactoryUtil.forName("name").like("%>" + templateParam.getParamValue() + "<%"));
			templateQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(siteGroupId));
			List<DDMTemplate> ddmTemplates = DDMTemplateLocalServiceUtil.dynamicQuery(templateQuery);

			Parameter folderParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					groupCourseAdmin.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_COURSE_FOLDER_CODE,
					false);
			DynamicQuery folderQuery = JournalFolderLocalServiceUtil.dynamicQuery();
			folderQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(siteGroupId));
			folderQuery.add(PropertyFactoryUtil.forName("name").eq(folderParam.getParamValue()));
			List<JournalFolder> folders = JournalFolderLocalServiceUtil.dynamicQuery(folderQuery);
			long folderId = folders.get(0).getFolderId();

			Parameter globalTopicParam = ParameterLocalServiceUtil.getByGroupCode(groupId,
					groupGlobalParam.getParameterGroupId(),
					CourseAdminWebPortletKeys.PARAMETER_GLOBAL_SEARCH_TOPIC_CODE, false);
			AssetVocabulary topicVocabulary = AssetVocabularyLocalServiceUtil
					.fetchGroupVocabulary(company.getGroup().getGroupId(), globalTopicParam.getParamValue());
			OrderByComparator<AssetCategory> order = null;
			List<AssetCategory> assetCategories = AssetCategoryLocalServiceUtil.getVocabularyCategories(
					topicVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, order);
			List<AssetCategory> courseCategory = assetCategories.stream()
					.filter(category -> category.getName().equals(CourseAdminWebPortletKeys.COURSE_CATEGORY_NAME))
					.collect(Collectors.toList());
			long[] assetCategoryIds = new long[courseCategory.size()];
			for (int i = 0; i < courseCategory.size(); i++) {
				assetCategoryIds[i] = courseCategory.get(i).getCategoryId();
			}
			
//			CourseLocalServiceUtil.updateCoursesFlagFromPopular(siteGroupId, ddmStructures.get(0).getStructureKey(),
//					ddmTemplates.get(0).getTemplateKey());
			List<Course> allCourses = CourseLocalServiceUtil.getAllActiveAndUpdatedCourse(siteGroupId, false, true);
//			List<Course> allCourses = CourseLocalServiceUtil.getAllActiveCourse(siteGroupId);
			List<String> courseCodes = allCourses.stream().map(course -> course.getCourseCode()).distinct()
					.collect(Collectors.toList());
			for (String courseCode : courseCodes) {
				log.info("course code = "+courseCode);
				this.executeGenerateCourse(userId, siteGroupId, courseCode, folderId, ddmTemplates, ddmStructures,
						assetCategoryIds);
			}

		} catch (Exception e) {
			log.error("Found some error in code " + e.getMessage());
		}
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		String elapsedTimeString = String.format("%02d hours, %02d min, %02d sec", 
				TimeUnit.MILLISECONDS.toHours(elapsedTime),
				TimeUnit.MILLISECONDS.toMinutes(elapsedTime) -
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedTime)),
			    TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime))
			);
		log.info("Elapsed Time = "+elapsedTimeString);
		log.info("=========generate end========= ");
	}

	private void executeGenerateCourse(long userId, long siteGroupId, String courseCode, long folderId,
			List<DDMTemplate> ddmTemplates, List<DDMStructure> ddmStructures, long[] assetCategoryIds) {
		try {
			generateCourse(userId, siteGroupId, courseCode, folderId, ddmTemplates, ddmStructures, assetCategoryIds);
		} catch (PortalException e) {
			log.error("Found some error in code " + e.getMessage());
		}
	}

	private void generateCourse(long userId, long siteGroupId, String courseCode, long folderId,
			List<DDMTemplate> ddmTemplates, List<DDMStructure> ddmStructures, long[] assetCategoryIds)
			throws PortalException {

		List<Course> courses = CourseLocalServiceUtil.getCourseByCourseCode(siteGroupId, courseCode, false);
		Course course = courses.get(0);
		Map<Locale, String> titleMap = new HashMap<>();
		Map<Locale, String> descriptionMap = new HashMap<>();
		Map<Locale, String> articleUrlMap = new HashMap<>();
		String urlTitle = "course/" + course.getCourseTitle().replace(" ", "-");

		// not using LocaleUtil.getDefault() because there's some case that default
		// article was UK
		titleMap.put(LocaleUtil.UK, course.getCourseCode() + ": " + course.getCourseTitle().toUpperCase());
		titleMap.put(LocaleUtil.US, course.getCourseCode() + ": " + course.getCourseTitle().toUpperCase());
		descriptionMap.put(LocaleUtil.UK, course.getDescription());
		descriptionMap.put(LocaleUtil.US, course.getDescription());
		articleUrlMap.put(LocaleUtil.UK, urlTitle);
		articleUrlMap.put(LocaleUtil.US, urlTitle);

		ServiceContext serviceContext = new ServiceContext();
		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setScopeGroupId(siteGroupId);
		serviceContext.setWorkflowAction(WorkflowConstants.ACTION_PUBLISH);
		
//		JournalArticleLocalServiceUtil.dynamicQuery()
//		DynamicQuery subQuery = DynamicQueryFactoryUtil.forClass(JournalArticle.class, "articleSub", PortalClassLoaderUtil.getClassLoader())
//	   	.add(PropertyFactoryUtil.forName("articleId").eqProperty("articleParent.articleId"))
//	    .setProjection(ProjectionFactoryUtil.max("id"));
		
//		DynamicQuery subQuery = DynamicQueryFactoryUtil.forClass(JournalArticle.class, "articleSub",JournalArticle.class.getClassLoader());
//		DynamicQuery subQuery = JournalArticleLocalServiceUtil.dynamicQuery();
//		subQuery.add(PropertyFactoryUtil.forName("articleId").eqProperty("articleParent.articleId"));
//		subQuery.setProjection(ProjectionFactoryUtil.max("version"));
		
//		DynamicQuery articleQuery = DynamicQueryFactoryUtil.forClass(JournalArticle.class, "articleParent",JournalArticle.class.getClassLoader());
		/*DynamicQuery articleQuery = JournalArticleLocalServiceUtil.dynamicQuery();
		articleQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(siteGroupId));
		articleQuery.add(PropertyFactoryUtil.forName(globalContent).like("%[" + course.getCourseCode() + "]%"));
		articleQuery.add(PropertyFactoryUtil.forName(globalStatus).ne(WorkflowConstants.STATUS_IN_TRASH));
//		articleQuery.add(PropertyFactoryUtil.forName("version").eq(subQuery));
		
		List<JournalArticle> journalArticles = JournalArticleLocalServiceUtil.dynamicQuery(articleQuery);
		List<JournalArticle> filteredArticles = journalArticles.stream()
				.filter(x -> x.getTemplateId().equals(ddmTemplates.get(0).getTemplateKey())
						&& x.getStructureId().equals(ddmStructures.get(0).getStructureKey()))
				.collect(Collectors.toList());
		
		List<JournalArticle> distinctFilteredArticles = filteredArticles.stream()
				.filter(distinctByKey(JournalArticle::getArticleId)).collect(Collectors.toList());
		JournalArticle resultJournalArticle = null;
		
		List<JournalArticle> maxJournalArticles = new ArrayList<>();
		for(int i = 0; i<distinctFilteredArticles.size(); i++) {
			JournalArticle latestArticle = JournalArticleLocalServiceUtil
					.getLatestArticle(distinctFilteredArticles.get(i).getResourcePrimKey());
			maxJournalArticles.add(latestArticle);
		}
		List<JournalArticle> filteredMaxJournals = maxJournalArticles.stream().filter(x -> x.getContent().contains("[" + course.getCourseCode() + "]")).collect(Collectors.toList());*/
		
//		List<JournalArticle> filteredMaxJournals = CourseLocalServiceUtil.getLatestJournalByCourseCode(siteGroupId, courseCode, ddmStructures.get(0).getStructureKey(), ddmTemplates.get(0).getTemplateKey());
		List<JournalArticle> filteredMaxJournals = new ArrayList<>();
		List<JSONObject> journals = CourseLocalServiceUtil.getLatestJournalByCourseCode(siteGroupId, courseCode, ddmStructures.get(0).getStructureKey(), ddmTemplates.get(0).getTemplateKey());
		for(JSONObject j : journals) {
			JournalArticle journal = JournalArticleLocalServiceUtil.getJournalArticleByUuidAndGroupId(j.getString("uuid"), siteGroupId);
			filteredMaxJournals.add(journal);
		}
		
		if (!filteredMaxJournals.isEmpty()) {
			for (int j = 0; j < filteredMaxJournals.size(); j++) {
				try {
					JournalArticle article = filteredMaxJournals.get(j);
					double version = JournalArticleLocalServiceUtil.getLatestVersion(siteGroupId,
							article.getArticleId());
//					JournalArticle latestArticle = JournalArticleLocalServiceUtil
//							.getLatestArticle(article.getResourcePrimKey());
					AssetEntry currentAssetEntry = AssetEntryLocalServiceUtil.getEntry(JournalArticle.class.getName(),
							article.getResourcePrimKey());
					List<AssetLink> currentAssetLinks = AssetLinkLocalServiceUtil
							.getDirectLinks(currentAssetEntry.getEntryId());
					long[] assetLinkEntryIds = currentAssetLinks.stream().mapToLong(x -> x.getEntryId2()).toArray();
					serviceContext.setAssetLinkEntryIds(assetLinkEntryIds);

//					String languageId = LocaleUtil.getDefault().getLanguage();
					
					String formatedStartDate = Validator.isNull(course.getStartDate()) ? ""
							: this.sdf.format(course.getStartDate());
					String formatedEndDate = Validator.isNull(course.getEndDate()) ? ""
							: this.sdf.format(course.getEndDate());
					
//					String xmlContent = article.getContentByLocale(languageId);
					
					String[] langIds = article.getAvailableLanguageIds();
					
					String xmlContent = article.getContent();
					Map<String, String> existingFieldMap = new HashMap<>();
					existingFieldMap.put("courseTitle", course.getCourseTitle());
					existingFieldMap.put("venue", course.getVenue());
					existingFieldMap.put("courseDescription", course.getDescription());
					existingFieldMap.put("price", course.getCourseFee().toString());
					existingFieldMap.put("batchID", course.getBatchId());
					existingFieldMap.put("courseCode", course.getCourseCode());
					existingFieldMap.put("CourseDuration", course.getCourseDuration().toString());
					existingFieldMap.put("noStudentEnrolled", String.valueOf(course.getAvailability()));
					existingFieldMap.put("funded", String.valueOf(course.getFundedCourseFlag()));
					existingFieldMap.put("popular", String.valueOf(course.getPopular()));
					existingFieldMap.put("startDate", formatedStartDate);
					existingFieldMap.put("endDate", formatedEndDate);

					String editedXmlContent = ContentUtil.getAndReplaceWebContentVal(xmlContent, 1, existingFieldMap,
							langIds);
					
					int status = 0;

					if (article.getStatus() == WorkflowConstants.STATUS_EXPIRED) {
						status = WorkflowConstants.STATUS_APPROVED;

					} else {
						status = article.getStatus();
					}
					PermissionChecker checker = PermissionCheckerFactoryUtil
							.create(UserLocalServiceUtil.getUser(userId));
					PermissionThreadLocal.setPermissionChecker(checker);
					List<AssetCategory> assets = AssetCategoryServiceUtil.getCategories(JournalArticle.class.getName(),
							article.getResourcePrimKey());
					AssetVocabulary themeVocabulary = AssetVocabularyLocalServiceUtil.fetchGroupVocabulary(siteGroupId,
							CourseAdminWebPortletKeys.COURSE_THEME_VOCAB_NAME);
					List<AssetCategory> withoutThemeCategories = assets.stream()
							.filter(x -> x.getVocabularyId() != themeVocabulary.getVocabularyId())
							.collect(Collectors.toList());
					List<AssetCategory> themeCategories = assets.stream()
							.filter(x -> x.getVocabularyId() == themeVocabulary.getVocabularyId())
							.collect(Collectors.toList());
					List<AssetCategory> finalAsset = withoutThemeCategories;
					if (!themeCategories.isEmpty()) {
						finalAsset.add(themeCategories.get(0));
					}

					long[] courseCategoryIds = new long[finalAsset.size()];
					for (int i = 0; i < finalAsset.size(); i++) {
						courseCategoryIds[i] = finalAsset.get(i).getCategoryId();
					}
					serviceContext.setAssetCategoryIds(courseCategoryIds);

					Calendar calendar = Calendar.getInstance();
					calendar.setTime(
							Validator.isNotNull(article.getDisplayDate()) ? article.getDisplayDate()
									: new Date());
					int displayDateMonth = calendar.get(Calendar.MONTH);
					int displayDateDay = calendar.get(Calendar.DATE);
					int displayDateYear = calendar.get(Calendar.YEAR);
					int displayDateHour = calendar.get(Calendar.HOUR);
					int displayDateMinute = calendar.get(Calendar.MINUTE);
					
					if(article.getUrlTitle().contains("course/")) {
						articleUrlMap = article.getFriendlyURLMap();
					}
//					coba async
					updatePreviousJournal(userId, article.getFriendlyURLsXML(), siteGroupId,
							article.getArticleId(), status, serviceContext);
					JournalArticleLocalServiceUtil.updateArticle(userId, siteGroupId,
							article.getFolderId(), article.getArticleId(), version, titleMap,
							descriptionMap, articleUrlMap, editedXmlContent, ddmStructures.get(0).getStructureKey(),
							ddmTemplates.get(0).getTemplateKey(), article.getLayoutUuid(), displayDateMonth,
							displayDateDay, displayDateYear, displayDateHour, displayDateMinute, 0, 0, 0, 0, 0, true, 0,
							0, 0, 0, 0, true, true, false, "", null, null, null, serviceContext);
					
				} catch (Exception e) {
					log.error("Found some error in code : " + e.getMessage());
				}
				log.info("updating course code = " + course.getCourseCode() + " completed successfully");
			}
		} else {
			DynamicQuery subfolderQuery = JournalFolderLocalServiceUtil.dynamicQuery();
			subfolderQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(siteGroupId));
			subfolderQuery.add(PropertyFactoryUtil.forName("name").eq(course.getCourseType()));
			subfolderQuery.add(PropertyFactoryUtil.forName("parentFolderId").eq(folderId));
			List<JournalFolder> subFolders = JournalFolderLocalServiceUtil.dynamicQuery(subfolderQuery);
			long courseFolderId = 0;
			if (!subFolders.isEmpty()) {
				courseFolderId = subFolders.get(0).getFolderId();
			} else {
				courseFolderId = JournalFolderLocalServiceUtil
						.addFolder(userId, siteGroupId, folderId, course.getCourseType(), null, serviceContext)
						.getFolderId();
			}
			
			serviceContext.setAssetCategoryIds(assetCategoryIds);
			GenerateCourseCMS generateCourseCMS = new GenerateCourseCMS();
			String xml = generateCourseCMS.generatedXml(course.getCourseTitle().toUpperCase(), course.getVenue(),
					course.getDescription(), course.getCourseFee(), course.getAvailability(), course.getBatchId(),
					course.getFundedCourseFlag(), course.getCourseCode(), course.getCourseDuration(),
					course.getStartDate(), course.getEndDate(), course.getPopular());
			Layout layout = LayoutLocalServiceUtil.getLayoutByFriendlyURL(siteGroupId, false,
					"/detail-courses-number-2");

			long nullLong = 0;

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			int displayDateMonth = calendar.get(Calendar.MONTH);
			int displayDateDay = calendar.get(Calendar.DATE);
			int displayDateYear = calendar.get(Calendar.YEAR);
			int displayDateHour = calendar.get(Calendar.HOUR);
			int displayDateMinute = calendar.get(Calendar.MINUTE);

			JournalArticleLocalServiceUtil.addArticle(userId, siteGroupId, courseFolderId,
					nullLong, nullLong, "", true, JournalArticleConstants.VERSION_DEFAULT, titleMap, descriptionMap,
					articleUrlMap, xml, ddmStructures.get(0).getStructureKey(), ddmTemplates.get(0).getTemplateKey(),
					layout.getUuid(), displayDateMonth, displayDateDay, displayDateYear, displayDateHour,
					displayDateMinute, 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 0, true, true, false, "", null, null, urlTitle,
					serviceContext);

			log.info("adding course code = " + course.getCourseCode() + " completed successfully");
		}
		/*if (resultJournalArticle != null) {
			//coba async
			final JournalArticle finalArticle = resultJournalArticle;
			Executor executor = Executors.newFixedThreadPool(10);
			CompletableFuture.supplyAsync(() -> {
				asyncSetRelatedArticle(finalArticle, siteGroupId, userId, ddmStructures.get(0).getStructureKey(), ddmTemplates.get(0).getTemplateKey());
				return null;
			}, executor);
		}*/

	}
	
	private void asyncSetRelatedArticle(JournalArticle resultJournalArticle, long siteGroupId, long userId, String structureKey, String templateKey) {
		Set<String> relatedArticleIds = CourseLocalServiceUtil.getPopularArticles(
				resultJournalArticle.getResourcePrimKey(), siteGroupId, structureKey,
				templateKey);
		Set<String> finalRelatedArticlesIds = new HashSet<>();
		for (String a : relatedArticleIds) {
			if (!a.equals(resultJournalArticle.getArticleId())) {
				finalRelatedArticlesIds.add(a);
			}
		}

		if (!finalRelatedArticlesIds.isEmpty()) {
			CourseLocalServiceUtil.setRelatedAsset(siteGroupId, userId, resultJournalArticle.getArticleId(),
					finalRelatedArticlesIds);
		}
	}

	private void updatePreviousJournal(long userId, String url, long groupId, String articleId, int workflowStatus,
			ServiceContext serviceContext) {
		List<Double> journalArticlesVersionsToUpdate = new ArrayList<>();

		DynamicQuery dq = JournalArticleLocalServiceUtil.dynamicQuery()
				.setProjection(ProjectionFactoryUtil.projectionList().add(ProjectionFactoryUtil.property("id"))
						.add(ProjectionFactoryUtil.property("version"))
						.add(ProjectionFactoryUtil.property(globalStatus)))
				.add(PropertyFactoryUtil.forName(globalGroupId).eq(groupId))
				.add(PropertyFactoryUtil.forName(globalStatus).ne(workflowStatus))
				.add(PropertyFactoryUtil.forName("articleId").eq(articleId)).addOrder(OrderFactoryUtil.asc("version"));
		List<Object[]> result = JournalArticleLocalServiceUtil.dynamicQuery(dq);
//		List<Object[]> filteredResult = result.stream().filter(x -> (int) x[2] != workflowStatus)
//				.collect(Collectors.toList());
		
		for (int i = 0; i < result.size(); i++) {
			double version = (double) result.get(i)[1];
//			int status = (int) result.get(i)[2];

//			if ((status == WorkflowConstants.STATUS_APPROVED)) {
			journalArticlesVersionsToUpdate.add(version);
//			}
		}

		for (double v : journalArticlesVersionsToUpdate) {
			try {
				JournalArticleLocalServiceUtil.updateStatus(userId, groupId, articleId, v, workflowStatus, url,
						new HashMap<>(), serviceContext);
			} catch (PortalException e) {
				log.error("Found some error in code " + e.getMessage());
			}
		}
	}

	public void moveApprovedCourse() {
		long startTime = System.currentTimeMillis();
		try {
			log.info("=========move start=========");
			Company company = CompanyLocalServiceUtil.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
			long groupId = GroupLocalServiceUtil
					.getGroup(company.getCompanyId(), CourseAdminWebPortletKeys.GUEST_GROUP_NAME).getGroupId();
			ParameterGroup groupGlobalParam = ParameterGroupLocalServiceUtil
					.getByCode(CourseAdminWebPortletKeys.PARAMETER_GLOBAL_GROUP_CODE, false);
			Parameter globalEmailParam = ParameterLocalServiceUtil.getByGroupCode(groupId,
					groupGlobalParam.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_GLOBAL_EMAIL_CODE,
					false);
			String email = globalEmailParam.getParamValue();
			long userId = UserLocalServiceUtil.getUserIdByEmailAddress(company.getCompanyId(), email);
			ParameterGroup groupCourseAdmin = ParameterGroupLocalServiceUtil
					.getByCode(CourseAdminWebPortletKeys.PARAMETER_COURSE_ADMIN_GROUP_CODE, false);
			long siteGroupId = groupCourseAdmin.getGroupId();

			Parameter folderParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					groupCourseAdmin.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_COURSE_FOLDER_CODE,
					false);

			Parameter structureParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					groupCourseAdmin.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_COURSE_STRUCTURE_CODE,
					false);
			DynamicQuery structureQuery = DDMStructureLocalServiceUtil.dynamicQuery();
			structureQuery.add(PropertyFactoryUtil.forName("name").like("%>" + structureParam.getParamValue() + "<%"));
			structureQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(siteGroupId));
			List<DDMStructure> ddmStructures = DDMStructureLocalServiceUtil.dynamicQuery(structureQuery, 0, 1);

			Parameter templateParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					groupCourseAdmin.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_COURSE_TEMPLATE_CODE,
					false);
			DynamicQuery templateQuery = DDMTemplateLocalServiceUtil.dynamicQuery();
			templateQuery.add(PropertyFactoryUtil.forName("name").like("%>" + templateParam.getParamValue() + "<%"));
			templateQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(siteGroupId));
			List<DDMTemplate> ddmTemplates = DDMTemplateLocalServiceUtil.dynamicQuery(templateQuery);

			DynamicQuery folderQuery = JournalFolderLocalServiceUtil.dynamicQuery();
			folderQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(siteGroupId));
			folderQuery.add(PropertyFactoryUtil.forName("name").eq(folderParam.getParamValue()));
			List<JournalFolder> generatedCourseFolders = JournalFolderLocalServiceUtil.dynamicQuery(folderQuery);

			List<JournalFolder> generatedChildFolders = JournalFolderLocalServiceUtil.getFolders(siteGroupId,
					generatedCourseFolders.get(0).getFolderId());
			List<JournalArticle> allJournalArticles = new ArrayList<>();
			for (JournalFolder journalFolder : generatedChildFolders) {
				List<JournalArticle> journalArticles = JournalArticleLocalServiceUtil.getArticles(siteGroupId,
						journalFolder.getFolderId());
				allJournalArticles.addAll(journalArticles);
			}
			List<JournalArticle> activeJournalArticles = allJournalArticles.stream()
					.filter(x -> x.getStatus() == WorkflowConstants.STATUS_APPROVED).collect(Collectors.toList());

			ServiceContext serviceContext = new ServiceContext();
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);
			serviceContext.setScopeGroupId(siteGroupId);
			serviceContext.setWorkflowAction(WorkflowConstants.ACTION_PUBLISH);
			for (JournalArticle article : activeJournalArticles) {
				CourseLocalServiceUtil.executeMoveCourse(article, siteGroupId, ddmStructures, ddmTemplates, userId,
						serviceContext);
			}
		} catch (Exception e) {
			log.error("Found some error in code " + e.getMessage());
		}
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		String elapsedTimeString = String.format("%02d hours, %02d min, %02d sec", 
				TimeUnit.MILLISECONDS.toHours(elapsedTime),
				TimeUnit.MILLISECONDS.toMinutes(elapsedTime) -
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedTime)),
			    TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime))
			);
		log.info("Elapsed Time = "+elapsedTimeString);
		log.info("=========move end=========");
	}

	private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	public void deleteNonTMSCourse() {
		log.info("=========delete start=========");
		long startTime = System.currentTimeMillis();
		try {
			Company company = CompanyLocalServiceUtil.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
			long groupId = GroupLocalServiceUtil
					.getGroup(company.getCompanyId(), CourseAdminWebPortletKeys.GUEST_GROUP_NAME).getGroupId();
			ParameterGroup groupGlobalParam = ParameterGroupLocalServiceUtil
					.getByCode(CourseAdminWebPortletKeys.PARAMETER_GLOBAL_GROUP_CODE, false);
			Parameter globalEmailParam = ParameterLocalServiceUtil.getByGroupCode(groupId,
					groupGlobalParam.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_GLOBAL_EMAIL_CODE,
					false);
			String email = globalEmailParam.getParamValue();
			long userId = UserLocalServiceUtil.getUserIdByEmailAddress(company.getCompanyId(), email);

			ParameterGroup groupCourseAdmin = ParameterGroupLocalServiceUtil
					.getByCode(CourseAdminWebPortletKeys.PARAMETER_COURSE_ADMIN_GROUP_CODE, false);
			long siteGroupId = groupCourseAdmin.getGroupId();

			Parameter structureParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					groupCourseAdmin.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_COURSE_STRUCTURE_CODE,
					false);
			DynamicQuery structureQuery = DDMStructureLocalServiceUtil.dynamicQuery();
			structureQuery.add(PropertyFactoryUtil.forName("name").like("%>" + structureParam.getParamValue() + "<%"));
			structureQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(siteGroupId));
			List<DDMStructure> ddmStructures = DDMStructureLocalServiceUtil.dynamicQuery(structureQuery, 0, 1);

			Parameter templateParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					groupCourseAdmin.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_COURSE_TEMPLATE_CODE,
					false);
			DynamicQuery templateQuery = DDMTemplateLocalServiceUtil.dynamicQuery();
			templateQuery.add(PropertyFactoryUtil.forName("name").like("%>" + templateParam.getParamValue() + "<%"));
			templateQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(siteGroupId));
			List<DDMTemplate> ddmTemplates = DDMTemplateLocalServiceUtil.dynamicQuery(templateQuery);

			ServiceContext serviceContext = new ServiceContext();
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);
			serviceContext.setScopeGroupId(siteGroupId);

//			List<Course> courses = CourseLocalServiceUtil.getAllActiveCourse(siteGroupId);
//			List<String> courseCodes = courses.stream().map(course -> course.getCourseCode()).distinct()
//					.collect(Collectors.toList());

//			DynamicQuery articleQuery = JournalArticleLocalServiceUtil.dynamicQuery();
//			articleQuery.add(PropertyFactoryUtil.forName(globalGroupId).eq(siteGroupId));
//			articleQuery.add(PropertyFactoryUtil.forName(globalStatus).ne(WorkflowConstants.STATUS_IN_TRASH));
//			for (String c : courseCodes) {
//				articleQuery.add(
//						RestrictionsFactoryUtil.not(PropertyFactoryUtil.forName(globalContent).like("%[" + c + "]%")));
//			}
//
//			List<JournalArticle> journalArticles = JournalArticleLocalServiceUtil.dynamicQuery(articleQuery);
			
			List<JournalArticle> journalArticles = new ArrayList<>();
			List<JSONObject> journals = CourseLocalServiceUtil.getLatestJournalByCourseCode(siteGroupId, ddmStructures.get(0).getStructureKey(), ddmTemplates.get(0).getTemplateKey());
			for(JSONObject j : journals) {
				JournalArticle journal = JournalArticleLocalServiceUtil.getJournalArticleByUuidAndGroupId(j.getString("uuid"), siteGroupId);
				journalArticles.add(journal);
			}
			
			
//			List<JournalArticle> filteredArticles = journalArticles.stream()
//					.filter(x -> x.getTemplateId().equals(ddmTemplates.get(0).getTemplateKey())
//							&& x.getStructureId().equals(ddmStructures.get(0).getStructureKey()))
//					.collect(Collectors.toList());
//
//			List<JournalArticle> distinctFilteredArticles = filteredArticles.stream()
//					.filter(distinctByKey(JournalArticle::getArticleId)).collect(Collectors.toList());
//			
			
				for (JournalArticle article : journalArticles) {
//					log.info(article.getArticleId());
				updatePreviousJournal(userId, article.getFriendlyURLsXML(), siteGroupId, article.getArticleId(),
						WorkflowConstants.STATUS_EXPIRED, serviceContext);
			}
		} catch (Exception e) {
			log.error("Found some error in code " + e.getMessage());
		}
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		String elapsedTimeString = String.format("%02d min, %02d sec", 
			    TimeUnit.MILLISECONDS.toMinutes(elapsedTime),
			    TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime))
			);
		log.info("Elapsed Time = "+elapsedTimeString);
		log.info("=========delete end=========");
	}

}
