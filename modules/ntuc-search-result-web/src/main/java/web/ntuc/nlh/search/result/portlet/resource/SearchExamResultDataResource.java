package web.ntuc.nlh.search.result.portlet.resource;

import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalServiceUtil;
import com.liferay.asset.kernel.service.persistence.AssetCategoryPersistence;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPDefinitionLocalServiceUtil;
import com.liferay.commerce.tax.engine.fixed.service.CommerceTaxFixedRateLocalServiceUtil;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.persistence.GroupPersistence;
import com.liferay.portal.kernel.service.persistence.GroupUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.aggregation.Aggregations;
import com.liferay.portal.search.facet.category.CategoryFacetFactory;
import com.liferay.portal.search.legacy.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.legacy.searcher.SearchResponseBuilderFactory;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.sort.Sorts;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.nlh.search.result.constants.CustomSqlConstant;
import web.ntuc.nlh.search.result.constants.MVCCommandNames;
import web.ntuc.nlh.search.result.constants.SearchResultPortletKeys;
import web.ntuc.nlh.search.result.dto.SearchResultDto;
import web.ntuc.nlh.search.result.util.CommonSearchResult;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.SEARCH_EXAM_DATA_RESOURCES,
		"javax.portlet.name=" + SearchResultPortletKeys.SEARCH_RESULT_PORTLET }, service = MVCResourceCommand.class)
public class SearchExamResultDataResource implements MVCResourceCommand {
	private static Log log = LogFactoryUtil.getLog(SearchExamResultDataResource.class);
	CommonSearchResult common = new CommonSearchResult();

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("search other data resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long localGroupId = themeDisplay.getScopeGroupId();
			Company company = _companyLocalService.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
			long globalGroupId = company.getGroup().getGroupId();
			String keyword = ParamUtil.getString(resourceRequest, "keyword", " ");
			int course = ParamUtil.getInteger(resourceRequest, "course", 0);

			int pageNumber = ParamUtil.getInteger(resourceRequest, "pageNum");
			int limit = ParamUtil.getInteger(resourceRequest, "limit");
			int[] filterCategoryIds = ParamUtil.getIntegerValues(resourceRequest, "categoryIds[]");
			String orders = ParamUtil.getString(resourceRequest, "orders");
			int showFrom = limit * (pageNumber - 1);
			int showTo = showFrom + limit;

			log.info(">>>>>>>>>>>>>>>>>>keyword : " + keyword);

			Parameter examIdParameter = ParameterLocalServiceUtil
					.getByCode(SearchResultPortletKeys.PARAMETER_CATALOG_EXAM_ID, false);

			GroupPersistence groupPersistance = GroupUtil.getPersistence();
			Class<?> groupClass = groupPersistance.getClass();
			ClassLoader groupLoader = groupClass.getClassLoader();
			DynamicQuery dqGroup = DynamicQueryFactoryUtil.forClass(Group.class, "group", groupLoader);
			dqGroup.add(RestrictionsFactoryUtil.eq("groupKey", examIdParameter.getParamValue()));
			dqGroup.setProjection(PropertyFactoryUtil.forName("group.groupId"));
			List<Long> groupList = GroupLocalServiceUtil.dynamicQuery(dqGroup);

			Long[] groupIds = new Long[groupList.size()];
			groupList.toArray(groupIds);
			long[] groupIdsLong = Arrays.stream(groupIds).filter(Objects::nonNull).mapToLong(Long::longValue).toArray();
			List<SearchResultDto> exams = new ArrayList<SearchResultDto>();
			long[] groupIdsVocabFilter = {globalGroupId};
			List<AssetVocabulary> listAssetVocabulary = AssetVocabularyLocalServiceUtil.getGroupsVocabularies(groupIdsVocabFilter);
			
			String defIdList ="";
			if(filterCategoryIds.length>0) {
				defIdList = getDefinitionIdFilter(filterCategoryIds,listAssetVocabulary);
			}
			
			List<CPDefinition> listCpDef = CPDefinitionLocalServiceUtil.searchCPDefinitions(company.getCompanyId(),
					groupIdsLong, keyword, WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					new Sort("name", true)).getBaseModels();
			double tax = CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).size()>0?
					CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).get(0).getRate():0;
			List<Long> cpDefIdList = new ArrayList<Long>();
			for (CPDefinition cpDef : listCpDef) {
				for (CPInstance cpInstance : cpDef.getCPInstances()) {
					SearchResultDto v = new SearchResultDto();
					v.setTitle(cpDef.getName() + " - " + cpInstance.getSku());
					v.setDesc(cpDef.getDescription());
					log.info(cpDef.getName());
					log.info(cpInstance.getPromoPrice());
					
					Double promoPrice = cpInstance.getPromoPrice()!=null && cpInstance.getPromoPrice().doubleValue()>0 && cpInstance.getPromoPrice().doubleValue()!= cpInstance.getPrice().doubleValue()? 
							cpInstance.getPromoPrice().doubleValue() : 0;
					Double price = promoPrice>0 ? promoPrice : cpInstance.getPrice().doubleValue();
					
					v.setOriginalPrice(promoPrice>0 ? (( cpInstance.getPrice().doubleValue()*tax)/100) +  cpInstance.getPrice().doubleValue()
							: 0);
					v.setPrice(((price*tax)/100) + price);
					try {
						v.setUrlImage(cpDef.getDefaultImageFileURL());
					} catch (Exception e) {
						v.setUrlImage("");
					}
					
					
					v.setUrlMore("/p/" + cpDef.getURL(cpDef.getDefaultLanguageId()));
					if(filterCategoryIds.length>0 && defIdList.contains(Long.toString(cpDef.getCPDefinitionId()))) {
						exams.add(v);
					}else if(filterCategoryIds.length<=0) {
						exams.add(v);
						cpDefIdList.add(cpDef.getCPDefinitionId());
					}
				}
			}
			
			//dont Filters
			List<JSONObject> categoryList = new ArrayList<JSONObject>();
			if(filterCategoryIds.length<=0 && cpDefIdList.size()>0) {
				categoryList = getCategoryEshop(cpDefIdList,listAssetVocabulary);
			}

			Collections.sort(exams, new Comparator<SearchResultDto>() {
				@Override
				public int compare(SearchResultDto u1, SearchResultDto u2) {
					if (orders.equalsIgnoreCase("asc")) {
						return u1.getPrice().compareTo(u2.getPrice());
					} else {
						return u2.getPrice().compareTo(u1.getPrice());
					}

				}
			});
			
			

			resourceResponse.setContentType("application/json");
			PrintWriter out = null;
			out = resourceResponse.getWriter();
			JSONObject rowData = JSONFactoryUtil.createJSONObject();
			rowData.put("exams", exams);
			rowData.put("categoryList", categoryList);
			rowData.put("status", HttpServletResponse.SC_OK);
			out.print(rowData.toString());
			out.flush();

		} catch (Exception e) {
			log.error("Error while searching other data : " + e.getMessage(), e);
			return true;
		}
		log.info("search result other data resources - end");
		return false;
	}
	
	private String getDefinitionIdFilter(int[] categoryIds, List<AssetVocabulary> listAssetVocab){
		String categoryIdString="";
		for(int lg : categoryIds) {
			categoryIdString=categoryIdString+"'"+lg+"',"; 
		}
		categoryIdString = "("+categoryIdString.substring(0, categoryIdString.length()-1)+")";
		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet result = null;
		String resultString = "";
		try {
			connection = DataAccess.getConnection();
			String plainQuery = String.format(CustomSqlConstant.getDefinitionId, "definitionId", "ASC");
			plainQuery = plainQuery.replace("#ListCategoryId#", categoryIdString);
			pStatement = connection.prepareStatement(plainQuery);
			result = pStatement.executeQuery();
			
			while (result.next()) {
				resultString = resultString+ Long.toString(result.getLong("definitionId")) +",";
			}
		}catch (Exception e) {
			log.error(e);
		}finally {
			try {
				if (result != null)
					result.close();
				if (pStatement != null)
					pStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception ignore) {
			}
		}
		return resultString.substring(0, resultString.length()-1);
	}
	
	private List<JSONObject> getCategoryEshop(List<Long> cpDefIdList, List<AssetVocabulary> listAssetVocab) {
		String cpDefinitionId="";
		for(Long lg : cpDefIdList) {
			cpDefinitionId=cpDefinitionId+"'"+lg+"',"; 
		}
		cpDefinitionId = "("+cpDefinitionId.substring(0, cpDefinitionId.length()-1)+")";
		
		String assetVocab="";
		for(AssetVocabulary av : listAssetVocab) {
			assetVocab=assetVocab+"'"+av.getName().toLowerCase()+"',"; 
		}
		assetVocab = "("+assetVocab.substring(0, assetVocab.length()-1)+")";
		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet result = null;
		List<JSONObject> jsonObjects = new ArrayList<>();
		try {
			connection = DataAccess.getConnection();
			String plainQuery = String.format(CustomSqlConstant.getCategoryFromExams, "ac.name", "ASC");
			plainQuery = plainQuery.replace("#ListDefId#", cpDefinitionId);
			plainQuery = plainQuery.replace("#ListVocabName#", assetVocab);
			pStatement = connection.prepareStatement(plainQuery);
			result = pStatement.executeQuery();
			while (result.next()) {
				JSONObject item = JSONFactoryUtil.createJSONObject().put("id", result.getString("id"))
						.put("name", result.getString("name"));
				jsonObjects.add(item);
			}
		}catch (Exception e) {
			log.error(e);
		}finally {
			try {
				if (result != null)
					result.close();
				if (pStatement != null)
					pStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception ignore) {
			}
		}
				
		return jsonObjects;
		
	}

	@Reference
	DDMIndexer _ddmIndexer;

	@Reference
	SearchRequestBuilderFactory _searchRequestBuilderFactory;

	@Reference
	Queries _queries;

	@Reference
	Sorts _sorts;

	@Reference
	Aggregations _aggregations;

	@Reference
	SearchResponseBuilderFactory _searchResponseBuilderFactory;

	@Reference
	CategoryFacetFactory _categoryFacetFactory;

	@Reference
	AssetVocabularyLocalService _assetVocabularyLocalService;

	@Reference
	AssetCategoryLocalService _assetCategoryLocalService;
	
	@Reference
	AssetCategoryPersistence  _assetCategoryPersistence;

	@Reference
	CompanyLocalService _companyLocalService;
}
