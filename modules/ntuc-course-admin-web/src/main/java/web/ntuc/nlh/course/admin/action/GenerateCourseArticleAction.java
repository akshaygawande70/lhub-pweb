package web.ntuc.nlh.course.admin.action;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetVocabularyLocalServiceUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.course.admin.exception.CourseValidationException;
import svc.ntuc.nlh.course.admin.model.Course;
import svc.ntuc.nlh.course.admin.service.CourseLocalServiceUtil;
import svc.ntuc.nlh.parameter.exception.ParameterValidationException;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.nlh.course.admin.constants.CourseAdminMessagesKey;
import web.ntuc.nlh.course.admin.constants.CourseAdminWebPortletKeys;
import web.ntuc.nlh.course.admin.constants.MVCCommandNames;
import web.ntuc.nlh.course.admin.util.GenerateCourseCMS;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.GENERATE_COURSE_ARTICLE_ACTION,
		"javax.portlet.name=" + CourseAdminWebPortletKeys.COURSE_ADMIN_PORTLET }, service = MVCActionCommand.class)
public class GenerateCourseArticleAction extends BaseMVCActionCommand {
	private static Log log = LogFactoryUtil.getLog(GenerateCourseArticleAction.class);
	private static String groupId = "groupId";
	
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Generate course action - start");
		try {
			Company company = CompanyLocalServiceUtil.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));

			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long globalGroupId = GroupLocalServiceUtil.getGroup(company.getCompanyId(), CourseAdminWebPortletKeys.GUEST_GROUP_NAME)
					.getGroupId();
			
			PortletCommandUtil.actionAndResourceCommand(actionRequest, themeDisplay);
			
			ParameterGroup groupGlobalParam = ParameterGroupLocalServiceUtil.getByCode(CourseAdminWebPortletKeys.PARAMETER_GLOBAL_GROUP_CODE, false);
			Parameter globalEmailParam = ParameterLocalServiceUtil.getByGroupCode(globalGroupId, groupGlobalParam.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_GLOBAL_EMAIL_CODE, false);
			String email = globalEmailParam.getParamValue();
			
			long userId = UserLocalServiceUtil.getUserIdByEmailAddress(company.getCompanyId(), email);
			
			String courseCode = ParamUtil.getString(actionRequest, "courseCode");
			
			ParameterGroup groupCourseAdmin = ParameterGroupLocalServiceUtil.getByCode(CourseAdminWebPortletKeys.PARAMETER_COURSE_ADMIN_GROUP_CODE, false);
			long siteGroupId = groupCourseAdmin.getGroupId();
			List<Course> courses = CourseLocalServiceUtil.getCourseByCourseCode(siteGroupId, courseCode, false);
			Course course = courses.get(0);
			log.info("course code = "+courseCode);
			
			Parameter structureParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId, groupCourseAdmin.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_COURSE_STRUCTURE_CODE, false);
			DynamicQuery structureQuery = DDMStructureLocalServiceUtil.dynamicQuery();
			structureQuery.add(PropertyFactoryUtil.forName("name")
					.like("%>" + structureParam.getParamValue() + "<%"));
			structureQuery.add(PropertyFactoryUtil.forName(groupId).eq(siteGroupId));
			List<DDMStructure> ddmStructures = DDMStructureLocalServiceUtil.dynamicQuery(structureQuery, 0, 1);
			log.info("ddmStructures.size() = " + ddmStructures.size());

			Parameter templateParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId, groupCourseAdmin.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_COURSE_TEMPLATE_CODE, false);
			DynamicQuery templateQuery = DDMTemplateLocalServiceUtil.dynamicQuery();
			templateQuery.add(PropertyFactoryUtil.forName("name")
					.like("%>" + templateParam.getParamValue() + "<%"));
			templateQuery.add(PropertyFactoryUtil.forName(groupId).eq(siteGroupId));
			List<DDMTemplate> ddmTemplates = DDMTemplateLocalServiceUtil.dynamicQuery(templateQuery);
			log.info("ddmTemplates.size() = " + ddmTemplates.size());

			Parameter folderParam = ParameterLocalServiceUtil.getByGroupCode(siteGroupId, groupCourseAdmin.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_COURSE_FOLDER_CODE, false);
			DynamicQuery folderQuery = JournalFolderLocalServiceUtil.dynamicQuery();
			folderQuery.add(PropertyFactoryUtil.forName(groupId).eq(siteGroupId));
			folderQuery.add(PropertyFactoryUtil.forName("name").eq(folderParam.getParamValue()));
			List<JournalFolder> folders = JournalFolderLocalServiceUtil.dynamicQuery(folderQuery);
			log.info("folders.size() = " + folders.size());
			long folderId = folders.get(0).getFolderId();

			Map<Locale, String> titleMap = new HashMap<>();
			Map<Locale, String> descriptionMap = new HashMap<>();
			Map<String, byte[]> articleUrlMap = new HashMap<>();

			titleMap.put(LocaleUtil.getDefault(), course.getCourseTitle());
			descriptionMap.put(LocaleUtil.getDefault(), course.getDescription());
			articleUrlMap.put(LocaleUtil.getDefault().toString(), course.getCourseTitle().replace(" ", "-").getBytes());
			log.info("articleUrlMap = "+articleUrlMap);
			Parameter globalTopicParam = ParameterLocalServiceUtil.getByGroupCode(globalGroupId, groupGlobalParam.getParameterGroupId(), CourseAdminWebPortletKeys.PARAMETER_GLOBAL_SEARCH_TOPIC_CODE, false);
			AssetVocabulary topicVocabulary = AssetVocabularyLocalServiceUtil.fetchGroupVocabulary(company.getGroup().getGroupId(),
					globalTopicParam.getParamValue());
			OrderByComparator<AssetCategory> order = null;
			List<AssetCategory> assetCategories = AssetCategoryLocalServiceUtil
					.getVocabularyCategories(topicVocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS,
							order);
			List<AssetCategory> courseCategory = assetCategories.stream().filter(category -> category.getName().equals(CourseAdminWebPortletKeys.COURSE_CATEGORY_NAME)).collect(Collectors.toList());
			long[] assetCategoryIds = new long[courseCategory.size()];
			for (int i=0; i<courseCategory.size(); i++) {
				assetCategoryIds[i] = courseCategory.get(i).getCategoryId();
			}
			
			ServiceContext serviceContext = new ServiceContext();
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);
			serviceContext.setScopeGroupId(siteGroupId);
			serviceContext.setWorkflowAction(WorkflowConstants.ACTION_PUBLISH);
			
			
			DynamicQuery subfolderQuery = JournalFolderLocalServiceUtil.dynamicQuery();
			subfolderQuery.add(PropertyFactoryUtil.forName(groupId).eq(siteGroupId));
			subfolderQuery.add(PropertyFactoryUtil.forName("name").eq(course.getCourseType()));
			subfolderQuery.add(PropertyFactoryUtil.forName("parentFolderId").eq(folderId));
			List<JournalFolder> subFolders = JournalFolderLocalServiceUtil.dynamicQuery(subfolderQuery);
			log.info("subFolders size = "+subFolders.size());
			long courseFolderId = 0; 
			if(!subFolders.isEmpty() ) {
				courseFolderId = subFolders.get(0).getFolderId();
			} else {
				courseFolderId = JournalFolderLocalServiceUtil.addFolder(userId, siteGroupId, folderId, course.getCourseType(), null, serviceContext).getFolderId();
			}
			
			DynamicQuery articleQuery = JournalArticleLocalServiceUtil.dynamicQuery();
			articleQuery.add(PropertyFactoryUtil.forName(groupId).eq(siteGroupId));
			articleQuery.add(PropertyFactoryUtil.forName("content").like("%[" + course.getCourseCode() + "]%"));
			articleQuery.add(PropertyFactoryUtil.forName("status").ne(WorkflowConstants.STATUS_IN_TRASH));
			List<JournalArticle> journalArticles = JournalArticleLocalServiceUtil.dynamicQuery(articleQuery);
			List<JournalArticle> filteredArticles = journalArticles.stream().filter(x -> x.getTemplateId().equals(ddmTemplates.get(0).getTemplateKey()) && x.getStructureId().equals(ddmStructures.get(0).getStructureKey())).collect(Collectors.toList());
			List<JournalArticle> distinctFilteredArticles = filteredArticles.stream().filter(distinctByKey(JournalArticle::getArticleId)).collect(Collectors.toList());
			if (!filteredArticles.isEmpty()) {
				for(int j = 0; j<distinctFilteredArticles.size(); j++) {
					log.info("updating course code = "+course.getCourseCode()+" completed successfully");
				}
			} else {
				serviceContext.setAssetCategoryIds(assetCategoryIds);
				GenerateCourseCMS generateCourseCMS = new GenerateCourseCMS();
				String xml = generateCourseCMS.generatedXml(course.getCourseTitle(), course.getVenue(),
						course.getDescription(), course.getCourseFee(), course.getAvailability(),
						course.getBatchId(), course.getFundedCourseFlag(), course.getCourseCode(),
						course.getCourseDuration(), course.getStartDate(), course.getEndDate(), course.getPopular());
				Layout layout = LayoutLocalServiceUtil.getLayoutByFriendlyURL(siteGroupId, false, "/detail-courses-number-2");
				log.info("layout uuid = "+layout.getUuid()+" url = "+layout.getFriendlyURL());
				
				long nullLong = 0;
				double nullDouble = 0;
				
				JournalArticleLocalServiceUtil.addArticle(userId, siteGroupId, courseFolderId, 
						nullLong, nullLong, "", true, nullDouble, titleMap, 
						descriptionMap, xml, ddmStructures.get(0).getStructureKey(), 
						ddmTemplates.get(0).getTemplateKey(),layout.getUuid(), 
						0, 0, 0, 0, 
						0, 0, 0,
						0, 0, 0, 
						true, 0, 0, 0, 
						0, 0, true, true, 
						false, "", null, null, "course/"+course.getCourseTitle().replace(" ", "-"), serviceContext);
				log.info("adding course code = "+course.getCourseCode()+" completed successfully");
			}
		} catch (Exception e) {
			log.error("Error while generating course : " + e.getMessage(), e);
		}
		log.info("Generate course action - end");

	}
	
	private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

}
