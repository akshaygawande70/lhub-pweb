package web.ntuc.eshop.reports.util;

import com.liferay.chat.service.persistence.StatusUtil;
import com.liferay.commerce.constants.CommerceConstants;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.discount.constants.CommerceDiscountConstants;
import com.liferay.commerce.discount.model.CommerceDiscount;
import com.liferay.commerce.discount.model.CommerceDiscountModel;
import com.liferay.commerce.discount.model.CommerceDiscountTable;
import com.liferay.commerce.discount.rule.type.CommerceDiscountRuleType;
import com.liferay.commerce.discount.rule.type.CommerceDiscountRuleTypeRegistry;
import com.liferay.commerce.inventory.constants.CommerceInventoryConstants;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem;
import com.liferay.commerce.inventory.service.CommerceInventoryWarehouseItemLocalServiceUtil;
import com.liferay.commerce.inventory.service.persistence.CommerceInventoryWarehousePersistence;
import com.liferay.commerce.inventory.service.persistence.CommerceInventoryWarehouseUtil;
import com.liferay.commerce.item.selector.criterion.CommerceProductInstanceItemSelectorCriterion;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.order.status.CommerceOrderStatus;
import com.liferay.commerce.order.status.CommerceOrderStatusRegistry;
import com.liferay.commerce.price.CommerceProductPrice;
import com.liferay.commerce.product.constants.CPInstanceConstants;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPDefinitionLocalization;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CProduct;
import com.liferay.commerce.product.service.CProductLocalServiceUtil;
import com.liferay.commerce.product.service.persistence.CPDefinitionPersistence;
import com.liferay.commerce.product.service.persistence.CPDefinitionUtil;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.commerce.service.persistence.CommerceOrderPersistence;
import com.liferay.commerce.service.persistence.CommerceOrderUtil;
import com.liferay.headless.commerce.admin.inventory.dto.v1_0.Warehouse;
import com.liferay.headless.commerce.admin.inventory.dto.v1_0.WarehouseItem;
import com.liferay.headless.commerce.admin.pricing.dto.v1_0.Discount;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Order;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowDefinition;
import com.liferay.portal.kernel.workflow.WorkflowHandler;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil;
import com.liferay.portal.kernel.workflow.WorkflowStatusManagerUtil;
import com.liferay.portal.workflow.constants.WorkflowDefinitionConstants;
import com.liferay.registry.RegistryUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;

import api.ntuc.common.util.CurrencyUtil;
import api.ntuc.common.util.DateUtil;
import web.ntuc.eshop.reports.constants.ReportConstant;
import web.ntuc.eshop.reports.constants.ReportSql;
import web.ntuc.eshop.reports.dto.ListCategoryDto;

public class ReportUtil {

	private static final String ORDER_DATE = "orderDate";
	private static final Log log = LogFactoryUtil.getLog(ReportUtil.class);
	private static final SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy");
	private static final SimpleDateFormat QUERY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat VIEW_DATE_FORMAT = new SimpleDateFormat("dd/MMM/yyyy");
//	private static final SimpleDateFormat VIEW_DATE_FORMAT = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
	
	public static List<CommerceOrder> getList(Date fromDate, Date toDate, Integer start, Integer end, Order order,
			int orderStatus) {
		log.info("Getting Report List By Query - Start");
		CommerceOrderPersistence op = CommerceOrderUtil.getPersistence();
		ClassLoader cl = op.getClass().getClassLoader();
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(CommerceOrder.class, cl);
		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();
		if (Validator.isNotNull(fromDate) && Validator.isNotNull(toDate)) {
			if (fromDate != null) {
				Calendar calStart = setDate(fromDate);
				Property property = PropertyFactoryUtil.forName(ORDER_DATE);
				conjunction.add(property.ge(calStart.getTime()));
			}

			if (toDate != null) {
				Calendar calEnd = setEndDate(toDate);
				Property property = PropertyFactoryUtil.forName(ORDER_DATE);
				conjunction.add(property.le(calEnd.getTime()));
			}
		}
		if (orderStatus != -1) {
			Property property = PropertyFactoryUtil.forName("orderStatus");
            conjunction.add(property.eq(orderStatus));
		}
		Property prop = PropertyFactoryUtil.forName("paymentStatus");
		conjunction.add(prop.eq(CommerceOrderConstants.PAYMENT_STATUS_PAID));
		

		dynamicQuery.addOrder(order);
		dynamicQuery.add(conjunction);

		log.info("Getting Report List By Query - Stop");
		return dynamicQuery(dynamicQuery, start, end);
	}

	public static JSONObject getSpecialProductReportData(int start, int limit, String columnToOrder, boolean isAscending, String defaultLocale) {

		Connection connection = null;
		PreparedStatement pStatement = null;
		PreparedStatement pStatementCount = null;
		ResultSet result = null;
		ResultSet resultCount = null;
		JSONArray temp = JSONFactoryUtil.createJSONArray();
		List<JSONObject> jsonObjects = new ArrayList<>();
		long totalCount = 0;
		try {
			connection = DataAccess.getConnection();
			String order = isAscending ? "ASC" : "DESC";
			log.info("full query = "+String.format(ReportSql.specialOrderQuery, columnToOrder, order));
			pStatement = connection.prepareStatement(String.format(ReportSql.specialOrderQuery, columnToOrder, order));
			final String customFieldName = "CITREP";
			final String className = CPDefinition.class.getName();
			
			pStatement.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
			pStatement.setString(2, customFieldName);
			pStatement.setString(3, className);
			pStatement.setInt(4, start);
			pStatement.setInt(5, limit);
			result = pStatement.executeQuery();
			while (result.next()) {
				JSONObject item = JSONFactoryUtil.createJSONObject().put("productName", result.getString("productName"))
						.put("citrepStatus", result.getString("citrepStatus"));
				jsonObjects.add(item);
				temp.put(item);
			}
			pStatementCount = connection.prepareStatement(String.format(ReportSql.specialOrderQueryCount));
			pStatementCount.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
			pStatementCount.setString(2, customFieldName);
			pStatementCount.setString(3, className);
			resultCount = pStatementCount.executeQuery();
			while (resultCount.next()) {
				totalCount = resultCount.getLong("countItem");
			}
		} catch (Exception e) {
			log.error("Error getting special product report. REASON:" + e.getMessage());
		} finally {
			try {
				if (result != null)
					result.close();
				if (pStatement != null)
					pStatement.close();
				if (resultCount != null)
					resultCount.close();
				if (pStatementCount != null)
					pStatementCount.close();
				if (connection != null)
					connection.close();
			} catch (Exception ignore) {
			}
		}
		return JSONFactoryUtil.createJSONObject().put(ReportConstant.DATA, temp).put(ReportConstant.TOTAL_COUNT,
				totalCount).put(ReportConstant.EXPORT_DATA, jsonObjects);
	}

	public static JSONObject getTotalOrderSummaryData(int start, int limit, String columnToOrder, boolean isAscending,
			String fromDate, String toDate) {
		SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
		Connection connection = null;
		PreparedStatement pStatement = null;
		PreparedStatement pStatementCount = null;
		ResultSet result = null;
		ResultSet resultCount = null;
		JSONArray temp = JSONFactoryUtil.createJSONArray();
		List<JSONObject> jsonObjects = new ArrayList<>();
		long totalCount = 0;
		String totalOrder = StringPool.BLANK;
		try {
			connection = DataAccess.getConnection();
			String dateRange = StringPool.BLANK;
			if (Validator.isNotNull(fromDate) && Validator.isNotNull(toDate)) {
				String tempStart = dateRangeConverter(fromDate, "00:00:00");
				String tempEnd = dateRangeConverter(toDate, "23:59:59");
				dateRange = "AND CO.createDate >= " + StringPool.APOSTROPHE + tempStart + StringPool.APOSTROPHE
						+ " AND CO.createDate <= " + StringPool.APOSTROPHE + tempEnd + StringPool.APOSTROPHE;
			}
			String order = isAscending ? "ASC" : "DESC";
			String plainQuery = String.format(ReportSql.totalOrderSummary, dateRange, columnToOrder, order);

			pStatement = connection.prepareStatement(plainQuery);
			pStatement.setInt(1, start);
			pStatement.setInt(2, limit);
			result = pStatement.executeQuery();
			while (result.next()) {

				JSONObject item = JSONFactoryUtil.createJSONObject()
						.put("orderDate", DateUtil.toString(result.getTimestamp("orderDate"), format))
						.put("orderId", result.getString("orderId"))
						.put("costPrice",  Validator.isNull(result.getString("costPrice")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("costPrice")))
						.put("grossProfit", Validator.isNull(result.getString("grossProfit")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("grossProfit")))
						.put("grossPrice", Validator.isNull(result.getString("grossPrice")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("grossPrice")));

				String contentStr = result.getString("content");
				List<String> contentArr = new ArrayList<>();
				if (contentStr.length() > 2) {
					contentStr = contentStr.substring(1, contentStr.length() - 1);
					contentArr = Arrays.asList(contentStr.split(","));
				}
				String stripeId = StringPool.BLANK;
				String stripeLink = "#";
				for (int i = 0; i < contentArr.size(); i++) {
					if (i == 0)
						stripeId = contentArr.get(i);
					if (i == 1)
						stripeLink = contentArr.get(i);
				}
				item.put("stripeLink", stripeLink);
				item.put("stripeId", stripeId);

				temp.put(item);
				jsonObjects.add(item);
			}
			pStatementCount = connection.prepareStatement(String.format(ReportSql.totalOrderSummaryCount, dateRange));
			resultCount = pStatementCount.executeQuery();
			while (resultCount.next()) {
				totalCount = resultCount.getLong("countItem");
				totalOrder = Validator.isNull(result.getString("totalOrder")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(resultCount.getString("totalOrder"));
			}
		} catch (Exception e) {
			log.error("Error getting total summary report. REASON:" + e.getMessage());
		} finally {
			try {
				if (result != null)
					result.close();
				if (pStatement != null)
					pStatement.close();
				if (resultCount != null)
					resultCount.close();
				if (pStatementCount != null)
					pStatementCount.close();
				if (connection != null)
					connection.close();
			} catch (Exception ignore) {
			}
		}
		return JSONFactoryUtil.createJSONObject().put(ReportConstant.DATA, temp)
				.put(ReportConstant.TOTAL_COUNT, totalCount).put("totalOrder", totalOrder)
				.put(ReportConstant.EXPORT_DATA, jsonObjects);
	}

	public static JSONObject getOrderDetailData(int start, int limit, String columnToOrder, boolean isAscending,
			String fromDate, String toDate, long orderId, int status, String productName,
			String defaultLocale) {

		Connection connection = null;
		PreparedStatement pStatement = null;
		PreparedStatement pStatementCount = null;
		ResultSet result = null;
		ResultSet resultCount = null;
		JSONArray temp = JSONFactoryUtil.createJSONArray();
		List<JSONObject> jsonObjects = new ArrayList<>();
		long totalCount = 0;
		try {
			connection = DataAccess.getConnection();
			String dateRangeFilter = StringPool.BLANK;
			String orderIdFilter = StringPool.BLANK;
			String statusFilter = StringPool.BLANK;
			if (orderId > 0) {
				orderIdFilter = "AND CO.commerceOrderId = " + orderId;
			}
			if (Validator.isNotNull(fromDate) && Validator.isNotNull(toDate)) {
				String tempStart = dateRangeConverter(fromDate, "00:00:00");
				String tempEnd = dateRangeConverter(toDate, "23:59:59");
				dateRangeFilter = "AND CO.createDate >= " + StringPool.APOSTROPHE + tempStart + StringPool.APOSTROPHE
						+ " AND CO.createDate <= " + StringPool.APOSTROPHE + tempEnd + StringPool.APOSTROPHE;
			}
			if (status > -1) {
				statusFilter = "AND O.orderStatus = " + status;
			} else {
				statusFilter = "AND 1 = 1";
			}
			String order = isAscending ? "ASC" : "DESC";
			String plainQuery = String.format(ReportSql.orderDetailQuery, dateRangeFilter, orderIdFilter, statusFilter,
					columnToOrder, order);
			log.info(plainQuery);
			pStatement = connection.prepareStatement(plainQuery);
			pStatement.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
			pStatement.setString(2, "%" + productName + "%");
			pStatement.setInt(3, start);
			pStatement.setInt(4, limit);
			result = pStatement.executeQuery();
			while (result.next()) {
				JSONObject item = JSONFactoryUtil.createJSONObject()
						.put("orderCreatedDate",
								DateUtil.toString(result.getTimestamp("orderCreatedDate"), VIEW_DATE_FORMAT))
						.put("orderId", result.getString("orderId"))
						.put("productId", result.getString("productId"))
						.put("custEmail", result.getString("custEmail"))
						.put("custName", result.getString("custName"))
						.put("status", CommerceOrderConstants.getOrderStatusLabel(result.getInt("status")).toUpperCase())
						.put("productName", result.getString("productName"))
						.put("sku", result.getString("sku"))
						.put("quantity", result.getString("quantity"))
						.put("unitPrice", Validator.isNull(result.getString("unitPrice")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("unitPrice")))
						.put("discount", Validator.isNull(result.getString("discount")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("discount")))
						.put("basePrice", Validator.isNull(result.getString("basePrice")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("basePrice")))
						.put("gst", Validator.isNull(result.getString("gst")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("gst")))
						.put("total", Validator.isNull(result.getString("total")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("total")));
				String contentStr = result.getString("content");
				List<String> contentArr = new ArrayList<>();
				if (contentStr.length() > 2) {
					contentStr = contentStr.substring(1, contentStr.length() - 1);
					contentArr = Arrays.asList(contentStr.split(","));
				}
				String stripeId = StringPool.BLANK;
				String stripeLink = "#";
				for (int i = 0; i < contentArr.size(); i++) {
					if (i == 0)
						stripeId = contentArr.get(i);
					if (i == 1)
						stripeLink = contentArr.get(i);
				}
				item.put("stripeLink", stripeLink);
				item.put("stripeId", stripeId);
				temp.put(item);
				jsonObjects.add(item);
			}

			pStatementCount = connection.prepareStatement(
					String.format(ReportSql.orderDetailQueryCount, dateRangeFilter, orderIdFilter, statusFilter));
			pStatementCount.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
			pStatementCount.setString(2, "%" + productName + "%");
			resultCount = pStatementCount.executeQuery();
			while (resultCount.next()) {
				totalCount = resultCount.getLong("countItem") ;
			}
		} catch (Exception e) {
			log.error("Error getting order detail report. REASON:" + e.getMessage());
		} finally {
			try {
				if (result != null)
					result.close();
				if (pStatement != null)
					pStatement.close();
				if (resultCount != null)
					resultCount.close();
				if (pStatementCount != null)
					pStatementCount.close();
				if (connection != null)
					connection.close();
			} catch (Exception ignore) {
			}
		}
		return JSONFactoryUtil.createJSONObject().put(ReportConstant.DATA, temp)
				.put(ReportConstant.TOTAL_COUNT, totalCount).put(ReportConstant.EXPORT_DATA, jsonObjects);
	}
	
	public static JSONObject getProductBestSellerData(int start, int limit, String columnToOrder, boolean isAscending,
			String searchKeyword, String fromDate, String toDate, String defaultLocale) {
		
		Connection connection = null;
		PreparedStatement pStatement = null;
		PreparedStatement pStatementCount = null;
		ResultSet result = null;
		ResultSet resultCount = null;
		JSONArray temp = JSONFactoryUtil.createJSONArray();
		List<JSONObject> jsonObjects = new ArrayList<>();
		long totalCount = 0;
		try {
			connection = DataAccess.getConnection();
			String dateRange = StringPool.BLANK;
			if (Validator.isNotNull(fromDate) && Validator.isNotNull(toDate)) {
//				Date fmtStart = INPUT_DATE_FORMAT.parse(fromDate);
//				Date fmtEnd = INPUT_DATE_FORMAT.parse(toDate);
				String tempStart = dateRangeConverter(fromDate, "00:00:00");
				String tempEnd = dateRangeConverter(toDate, "23:59:59");

				dateRange = " WHERE COI.createDate >= " + StringPool.APOSTROPHE + tempStart + StringPool.APOSTROPHE
						+ " AND COI.createDate <= " + StringPool.APOSTROPHE + tempEnd + StringPool.APOSTROPHE;
			}
			log.info(dateRange);
			String order = isAscending ? "ASC" : "DESC";

			String query = String.format(ReportSql.bestProductQuery, dateRange, columnToOrder, order);
			log.info(query);
			log.info("=========== START ======="+start);
			log.info("==========="+limit);
			pStatement = connection.prepareStatement(query);
			pStatement.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
//			pStatement.setString(2, "%" + searchKeyword + "%");
			pStatement.setInt(2, start);
			pStatement.setInt(3, limit);

			result = pStatement.executeQuery();
			log.info("========="+result.getFetchSize());
			while (result.next()) {
				JSONObject item = JSONFactoryUtil.createJSONObject()
						.put("transactionStart",
								DateUtil.toString(result.getDate("transactionStart"),VIEW_DATE_FORMAT))
						.put("transactionEnd",
								DateUtil.toString(result.getDate("transactionEnd"), VIEW_DATE_FORMAT))
						.put("category", result.getString("category"))
						.put("productName", result.getString("productName"))
						.put("sku", result.getString("sku"))
						.put("soldQty", result.getString("soldQty"))
						.put("costPrice", Validator.isNull(result.getString("costPrice")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("costPrice")))
						.put("admin_fee", Validator.isNull(result.getString("admin_fee")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("admin_fee")))
						.put("basePrice", Validator.isNull(result.getString("basePrice")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("basePrice")))
						.put("grossSellingPrice", Validator.isNull(result.getString("grossSellingPrice")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("grossSellingPrice")))
						.put("costOfProduct", Validator.isNull(result.getString("costOfProduct")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("costOfProduct")))
						.put("grossProfit", Validator.isNull(result.getString("grossProfit")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("grossProfit")))
						.put("gpMargin", Validator.isNull(result.getString("gpMargin")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("gpMargin")));
				temp.put(item);
				jsonObjects.add(item);
			}
			pStatementCount = connection.prepareStatement(String.format(ReportSql.bestProductQueryCount, dateRange));
			pStatementCount.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
			resultCount = pStatementCount.executeQuery();
			while (resultCount.next()) {
				totalCount = resultCount.getLong("countItem");
			}
		} catch (Exception e) {
			log.error("Error getting best product report. REASON:" + e.getMessage());
		} finally {
			try {
				if (result != null)
					result.close();
				if (pStatement != null)
					pStatement.close();
				if (resultCount != null)
					resultCount.close();
				if (pStatementCount != null)
					pStatementCount.close();
				if (connection != null)
					connection.close();
			} catch (Exception ignore) {
			}
		}
		return JSONFactoryUtil.createJSONObject().put(ReportConstant.DATA, temp)
				.put(ReportConstant.TOTAL_COUNT, totalCount).put(ReportConstant.EXPORT_DATA, jsonObjects);
	}
	
	//ga bisa dibuka di local, karena ada kondisi WHERE AC.vocabularyId yang sesuai UAT
	public static JSONObject getProductInventoryDetailsData(int start, int limit, String columnToOrder, boolean isAscending,
			 String category, int status, String productName, String sku, String availableStock,
			String defaultLocale, String fromDate) {

		Connection connection = null;
		PreparedStatement pStatement = null;
		PreparedStatement pStatementCount = null;
		ResultSet result = null;
		ResultSet resultCount = null;
		JSONArray temp = JSONFactoryUtil.createJSONArray();
		List<JSONObject> jsonObjects = new ArrayList<>();
		long totalCount = 0;
		try {
			connection = DataAccess.getConnection();
//			String dateRange = StringPool.BLANK;
//			if (Validator.isNotNull(fromDate)) {
////				Date fmtStart = INPUT_DATE_FORMAT.parse(fromDate);
////				Date fmtEnd = INPUT_DATE_FORMAT.parse(toDate);
//				String tempStart = dateRangeConverter(fromDate, "00:00:00");
//
//				dateRange = "AND CO.createDate >= " + StringPool.APOSTROPHE + tempStart + StringPool.APOSTROPHE;
//			}
			
			String categoryFilter = StringPool.BLANK;
			if (!category.equalsIgnoreCase("FFF")) {
				categoryFilter = "HAVING  category LIKE '"+ category +"'";
			} else {
				categoryFilter = "HAVING  category LIKE '%%'";
			}
			
			String categoryFilterCount = StringPool.BLANK;
			if (!category.equalsIgnoreCase("FFF")) {
				categoryFilterCount = "WHERE  PR.category LIKE '"+ category +"'";
			} else {
				categoryFilterCount = "WHERE  PR.category LIKE '%%'";
			}
			
			String order = isAscending ? "ASC" : "DESC";
			String plainQuery = String.format(ReportSql.productInventoryDetailsQuery, categoryFilter,
					columnToOrder, order);
			log.info(plainQuery);
			pStatement = connection.prepareStatement(plainQuery);
			pStatement.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
//			pStatement.setString(1, "%" + category + "%");
//			pStatement.setString(3, "%" + sku + "%");
//			pStatement.setString(3, "%" + status + "%");
			pStatement.setInt(2, start);
			pStatement.setInt(3, limit);
			result = pStatement.executeQuery();
			while (result.next()) {
				JSONObject item = JSONFactoryUtil.createJSONObject()
						.put("productCreationDate",
								DateUtil.toString(result.getTimestamp("productCreationDate"), VIEW_DATE_FORMAT))
						.put("category", result.getString("category"))
						.put("productName", result.getString("productName"))
						.put("sku", result.getString("sku"))
						.put("status", WorkflowConstants.getStatusLabel(result.getInt("status")).toUpperCase())
						.put("costPrice", Validator.isNull(result.getString("costPrice")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("costPrice")))
						.put("admin_fee", Validator.isNull(result.getString("admin_fee")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("admin_fee")))
						.put("basePrice", Validator.isNull(result.getString("basePrice")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("basePrice")))
						.put("grossProfit", Validator.isNull(result.getString("grossProfit")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("grossProfit")))
						.put("gpMargin", Validator.isNull(result.getString("gpMargin")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("gpMargin")))
						.put("soldQty", result.getString("soldQty"))
						.put("availableStock", result.getString("availableStock"));
				temp.put(item);
				jsonObjects.add(item);
			}

			pStatementCount = connection.prepareStatement(
					String.format(ReportSql.productInventoryDetailsQueryCount, categoryFilterCount	));
//			pStatementCount.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
//			pStatementCount.setString(1, "%" + category + "%");
//			pStatementCount.setString(3, "%" + sku + "%");
//			pStatementCount.setString(4, "%" + status + "%");
			resultCount = pStatementCount.executeQuery();
			log.info(pStatementCount);
			while (resultCount.next()) {
				totalCount = resultCount.getLong("countItem") ;
				log.info(resultCount.getLong("countItem"));
			}
		} catch (Exception e) {
			log.error("Error getting product inventory details report. REASON:" + e.getMessage());
		} finally {
			try {
				if (result != null)
					result.close();
				if (pStatement != null)
					pStatement.close();
				if (resultCount != null)
					resultCount.close();
				if (pStatementCount != null)
					pStatementCount.close();
				if (connection != null)
					connection.close();
			} catch (Exception ignore) {
			}
		}
		return JSONFactoryUtil.createJSONObject().put(ReportConstant.DATA, temp)
				.put(ReportConstant.TOTAL_COUNT, totalCount).put(ReportConstant.EXPORT_DATA, jsonObjects);
	}
	
	//ga bisa dibuka di local, karena ada kondisi WHERE AC.vocabularyId yang sesuai UAT
	public static JSONObject getLowStockDetailsProductData(int start, int limit, String columnToOrder, boolean isAscending,
			 String category, int status, String productName, String sku, String availableStock,
			String defaultLocale, String fromDate) {

		Connection connection = null;
		PreparedStatement pStatement = null;
		PreparedStatement pStatementCount = null;
		ResultSet result = null;
		ResultSet resultCount = null;
		JSONArray temp = JSONFactoryUtil.createJSONArray();
		List<JSONObject> jsonObjects = new ArrayList<>();
		long totalCount = 0;
		try {
			connection = DataAccess.getConnection();
//			String dateRange = StringPool.BLANK;
//			if (Validator.isNotNull(fromDate)) {
////				Date fmtStart = INPUT_DATE_FORMAT.parse(fromDate);
////				Date fmtEnd = INPUT_DATE_FORMAT.parse(toDate);
//				String tempStart = dateRangeConverter(fromDate, "00:00:00");
//
//				dateRange = "AND CO.createDate >= " + StringPool.APOSTROPHE + tempStart + StringPool.APOSTROPHE;
//			}
			
			String categoryFilter = StringPool.BLANK;
			if (!category.equalsIgnoreCase("FFF")) {
				categoryFilter = "AND  category LIKE '"+ category +"'";
			} else {
				categoryFilter = "AND  category LIKE '%%'";
			}
			
			String order = isAscending ? "ASC" : "DESC";
			String plainQuery = String.format(ReportSql.lowStockQuery, categoryFilter,
					columnToOrder, order);
			log.info(plainQuery);
			pStatement = connection.prepareStatement(plainQuery);
			pStatement.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
//			pStatement.setString(2, "%" + category + "%");
//			pStatement.setString(3, "%" + productName + "%");
//			pStatement.setString(3, "%" + sku + "%");
//			pStatement.setString(3, "%" + status + "%");
			pStatement.setInt(2, start);
			pStatement.setInt(3, limit);
			result = pStatement.executeQuery();
			while (result.next()) {
				JSONObject item = JSONFactoryUtil.createJSONObject()
						.put("productCreationDate",
								DateUtil.toString(result.getTimestamp("productCreationDate"), VIEW_DATE_FORMAT))
						.put("category", result.getString("category"))
						.put("productName", result.getString("productName"))
						.put("sku", result.getString("sku"))
						.put("status", WorkflowConstants.getStatusLabel(result.getInt("status")).toUpperCase())
						.put("costPrice", Validator.isNull(result.getString("costPrice")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("costPrice")))
						.put("admin_fee", Validator.isNull(result.getString("admin_fee")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("admin_fee")))
						.put("basePrice", Validator.isNull(result.getString("basePrice")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("basePrice")))
						.put("lowStock", result.getString("lowStock"));
				temp.put(item);
				jsonObjects.add(item);
			}

			pStatementCount = connection.prepareStatement(
					String.format(ReportSql.lowStockQueryCount, categoryFilter));
			pStatementCount.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
//			pStatementCount.setString(2, "%" + category + "%");
//			pStatementCount.setString(3, "%" + productName + "%");
//			pStatementCount.setString(4, "%" + status + "%");
			resultCount = pStatementCount.executeQuery();
			log.info(pStatementCount);
			while (resultCount.next()) {
				totalCount = resultCount.getLong("countItem") ;
				log.info(resultCount.getLong("countItem"));
			}
		} catch (Exception e) {
			log.error("Error getting low stock details product report. REASON:" + e.getMessage());
		} finally {
			try {
				if (result != null)
					result.close();
				if (pStatement != null)
					pStatement.close();
				if (resultCount != null)
					resultCount.close();
				if (pStatementCount != null)
					pStatementCount.close();
				if (connection != null)
					connection.close();
			} catch (Exception ignore) {
			}
		}
		return JSONFactoryUtil.createJSONObject().put(ReportConstant.DATA, temp)
				.put(ReportConstant.TOTAL_COUNT, totalCount).put(ReportConstant.EXPORT_DATA, jsonObjects);
	}
	
	public static JSONObject getPromotionDiscountData(int start, int limit, String columnToOrder, boolean isAscending,
			 String status, String defaultLocale, String fromDate, String toDate,
			 String discountType, String discountCode) {

		Connection connection = null;
		PreparedStatement pStatement = null;
		PreparedStatement pStatementCount = null;
		ResultSet result = null;
		ResultSet resultCount = null;
		JSONArray temp = JSONFactoryUtil.createJSONArray();
		List<JSONObject> jsonObjects = new ArrayList<>();
		long totalCount = 0;
		try {
			connection = DataAccess.getConnection();
			String dateRange = StringPool.BLANK;
			if (Validator.isNotNull(fromDate) && Validator.isNotNull(toDate)) {
//				Date fmtStart = INPUT_DATE_FORMAT.parse(fromDate);
//				Date fmtEnd = INPUT_DATE_FORMAT.parse(toDate);
				String tempStart = dateRangeConverter(fromDate, "00:00:00");
				String tempEnd = dateRangeConverter(toDate, "23:59:59");

				dateRange = " AND PR.startDate >= " + StringPool.APOSTROPHE + tempStart + StringPool.APOSTROPHE
						+ " AND PR.startDate <= " + StringPool.APOSTROPHE + tempEnd + StringPool.APOSTROPHE;
			}
		
			String statusFilter = StringPool.BLANK;
			if (!status.equalsIgnoreCase("FFF")) {
				statusFilter = "AND PR.statusDiscount LIKE '"+ status +"'";
			} else {
				statusFilter = "AND (PR.statusDiscount LIKE '%%')";
			}
			String order = isAscending ? "ASC" : "DESC";
			String plainQuery = String.format(ReportSql.discountQuery,dateRange,statusFilter,
					columnToOrder, order);
			log.info(plainQuery);
			pStatement = connection.prepareStatement(plainQuery);
//			pStatement.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
			pStatement.setString(1, "%" + discountCode + "%");
			pStatement.setString(2, "%" + discountType + "%");
//			pStatement.setString(3, "%" + status + "%");
			pStatement.setInt(3, start);
			pStatement.setInt(4, limit);
			result = pStatement.executeQuery();
			while (result.next()) {
				JSONObject item = JSONFactoryUtil.createJSONObject()
						.put("startDate",
								DateUtil.toString(result.getTimestamp("startDate"), VIEW_DATE_FORMAT))
						.put("endDate",
								DateUtil.toString(result.getTimestamp("endDate"), VIEW_DATE_FORMAT))
						.put("discountDescription", result.getString("discountDescription"))
						.put("discountType", result.getString("discountType"))
						.put("discountCode", result.getString("discountCode"))
						.put("statusDiscount",  result.getString("statusDiscount"));
				temp.put(item);
				jsonObjects.add(item);
			}

			pStatementCount = connection.prepareStatement(
					String.format(ReportSql.discountQueryCount, dateRange, statusFilter));
//			pStatementCount.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
			pStatementCount.setString(1, "%" + discountCode + "%");
			pStatementCount.setString(2, "%" + discountType + "%");
//			pStatementCount.setString(3, "%" + status + "%");
			resultCount = pStatementCount.executeQuery();
			log.info(pStatementCount);
			while (resultCount.next()) {
				totalCount = resultCount.getLong("countItem") ;
				log.info(resultCount.getLong("countItem"));
			}
		} catch (Exception e) {
			log.error("Error getting promotion & discount report. REASON:" + e.getMessage());
		} finally {
			try {
				if (result != null)
					result.close();
				if (pStatement != null)
					pStatement.close();
				if (resultCount != null)
					resultCount.close();
				if (pStatementCount != null)
					pStatementCount.close();
				if (connection != null)
					connection.close();
			} catch (Exception ignore) {
			}
		}
		return JSONFactoryUtil.createJSONObject().put(ReportConstant.DATA, temp)
				.put(ReportConstant.TOTAL_COUNT, totalCount).put(ReportConstant.EXPORT_DATA, jsonObjects);
	}

	private static String dateRangeConverter(String date, String time) throws ParseException {
		Date temp = INPUT_DATE_FORMAT.parse(date);
		String returnString = QUERY_DATE_FORMAT.format(temp) + " " + time;
		return returnString;
	}

	public static Calendar setDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	public static Calendar setEndDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar;
	}
	
	public static Map<Integer, String> getStatusList(ThemeDisplay themeDisplay, CommerceOrderStatusRegistry _commerceOrderStatusRegistry) {
		Map<Integer, String> statuses = new HashMap<>();
		statuses.put(-1, "Choose Order Status");
		for(CommerceOrderStatus status : _commerceOrderStatusRegistry.getCommerceOrderStatuses()) {
			if(status.getLabel(themeDisplay.getLocale()).equals("Pending") 
					|| status.getLabel(themeDisplay.getLocale()).equals("Processing")
					|| status.getLabel(themeDisplay.getLocale()).equals("Completed")
					|| status.getLabel(themeDisplay.getLocale()).equals("In Progress")
					|| status.getLabel(themeDisplay.getLocale()).equals("On Hold")
					|| status.getLabel(themeDisplay.getLocale()).equals("Cancelled"))
			statuses.put(status.getKey(), status.getLabel(themeDisplay.getLocale()));
		}
		return statuses;
	}
	
	
	public static List<ListCategoryDto> getCategoryLists(ThemeDisplay themeDisplay) {
		Connection connection = null;
		PreparedStatement pStatement = null;
		PreparedStatement pStatementCount = null;
		ResultSet result = null;
		ResultSet resultCount = null;
		List<ListCategoryDto> listCategory = new ArrayList<>();
		try {
			connection = DataAccess.getConnection();
			String plainQuery = String.format(ReportSql.categoryLists);
			log.info(plainQuery);
			pStatement = connection.prepareStatement(plainQuery);
			result = pStatement.executeQuery();
			while (result.next()) {
				String category = result.getString("category");
				ListCategoryDto object = new ListCategoryDto(category);
				
				listCategory.add(object);
				log.info(result);
			}
		} catch (Exception e) {
			log.error("Error getting Category Lists. REASON:" + e.getMessage());
		} finally {
			try {
				if (result != null)
					result.close();
				if (pStatement != null)
					pStatement.close();
				if (resultCount != null)
					resultCount.close();
				if (pStatementCount != null)
					pStatementCount.close();
				if (connection != null)
					connection.close();
			} catch (Exception ignore) {
			}
		}
		return listCategory;
	}
	
//	public static List<ListSubcategoryDto> getSubCategoryLists(ThemeDisplay themeDisplay) {
//		Connection connection = null;
//		PreparedStatement pStatement = null;
//		PreparedStatement pStatementCount = null;
//		ResultSet result = null;
//		ResultSet resultCount = null;
//		List<ListSubcategoryDto> listSubCategory = new ArrayList<>();
//		try {
//			connection = DataAccess.getConnection();
//			String plainQuery = String.format(ReportSql.subCategoryLists);
//			log.info(plainQuery);
//			pStatement = connection.prepareStatement(plainQuery);
//			result = pStatement.executeQuery();
//			while (result.next()) {
//				String subCategory = result.getString("subCategory");
//				ListSubcategoryDto object = new ListSubcategoryDto(subCategory);
//				
//				listSubCategory.add(object);
//				log.info(result);
//			}
//		} catch (Exception e) {
//			log.error("Error getting Subcategory Lists. REASON:" + e.getMessage());
//		} finally {
//			try {
//				if (result != null)
//					result.close();
//				if (pStatement != null)
//					pStatement.close();
//				if (resultCount != null)
//					resultCount.close();
//				if (pStatementCount != null)
//					pStatementCount.close();
//				if (connection != null)
//					connection.close();
//			} catch (Exception ignore) {
//			}
//		}
//		return listSubCategory;
//	}
	
//	public static Map<String, Object> processWriteToUs(Contact contact,HttpServletRequest request, HttpServletResponse response){
//		Map<String, Object> responseMap = new HashMap<String, Object>();
//		    try { 
//		         CommerceDiscountConstants contact1 = new CommerceDiscountConstants();
//		         contact.set(request.getParameter("email"));
//		         contact.setFirstName(request.getParameter("firstname"));
//		         contact.setLastName(request.getParameter("lastname"));
//		         contact.setType(request.getParameter("ddl"));
//		                     //process the form details...
//		                     //responseMap.put("key","value")
//		        }
//		        catch(Exception e){
//		          e.printStackTrace();
//		        }
//		            return responseMap;
//      }
			
	public static <T> java.util.List<T> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
			int start, int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	public static CommerceOrderLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<CommerceOrderLocalService, CommerceOrderLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(CommerceOrderService.class);

		ServiceTracker<CommerceOrderLocalService, CommerceOrderLocalService> serviceTracker = new ServiceTracker<CommerceOrderLocalService, CommerceOrderLocalService>(
				bundle.getBundleContext(), CommerceOrderLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}
	
	

}