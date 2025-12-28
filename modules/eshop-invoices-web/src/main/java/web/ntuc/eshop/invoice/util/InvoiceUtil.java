package web.ntuc.eshop.invoice.util;

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.constants.CommerceOrderPaymentConstants;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.order.status.CommerceOrderStatusRegistry;
import com.liferay.commerce.service.CommerceOrderItemLocalServiceUtil;
import com.liferay.commerce.service.CommerceOrderLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.CurrencyUtil;
import api.ntuc.common.util.DateUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.invoice.constants.InvoiceConstants;
import web.ntuc.eshop.invoice.constants.InvoicePortletKeys;
import web.ntuc.eshop.invoice.constants.InvoiceSql;
import web.ntuc.eshop.invoice.constants.MVCCommandNames;
import web.ntuc.eshop.invoice.dto.OrderDto;
import web.ntuc.eshop.invoice.dto.ResultOrderListDto;
public class InvoiceUtil {
	
	private static final String ORDER_DATE = "orderDate";
	private static final Log log = LogFactoryUtil.getLog(InvoiceUtil.class);
	private static final SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy");
	private static final SimpleDateFormat QUERY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat VIEW_DATE_FORMAT = new SimpleDateFormat("dd/MMM/yyyy");

	public InvoiceUtil() {
	}

	public static String getOrderStatus(int statusId) {
		String status = "";
		if (statusId == CommerceOrderConstants.ORDER_STATUS_COMPLETED) {
			status = "COMPLETED";
		} else if (statusId == CommerceOrderConstants.ORDER_STATUS_PROCESSING) {
			status = "PROCESSING";
		} else if (statusId == CommerceOrderConstants.ORDER_STATUS_PENDING) {
			status = "PENDING";
		} else if (statusId == CommerceOrderConstants.ORDER_STATUS_CANCELLED) {
			status = "CANCELLED";
		}else if (statusId == CommerceOrderConstants.ORDER_STATUS_DECLINED) {
			status = "DECLINED";
		}else if (statusId == CommerceOrderConstants.ORDER_STATUS_IN_PROGRESS) {
			status = "IN PROGRESS";
		}else if (statusId == CommerceOrderConstants.ORDER_STATUS_ON_HOLD) {
			status = "ON HOLD";
		}else if (statusId == CommerceOrderConstants.ORDER_STATUS_OPEN) {
			status = "OPEN";
		}else if (statusId == CommerceOrderConstants.ORDER_STATUS_SHIPPED) {
			status = "SHIPPED";
		}else if (statusId == CommerceOrderConstants.ORDER_STATUS_AWAITING_PICKUP) {
			status = "AWAITING PICKUP";
		}

		return status;
	}

	public static String parseDate(String newFormat, Date inputDate, Log log) {
		String tempDate = "";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(newFormat);
			tempDate = sdf.format(inputDate);

		} catch (Exception e) {
			log.error("Error while parsing the date cause " + e.getMessage());
		}
		return tempDate;
	}
	
	public static JSONObject getTableData(int allCount, int countAfterFilter, String sEcho, JSONArray paramArray) {
		JSONObject tableData = JSONFactoryUtil.createJSONObject();
		tableData.put("iTotalRecords", allCount);
		tableData.put("iTotalDisplayRecords", countAfterFilter);
		tableData.put("sEcho", Integer.parseInt(sEcho));
		tableData.put("aaData", paramArray);

		return tableData;
	}
	
	public static String getSecretKey() {
		ParameterGroup parameterAuthGroup = ParameterGroupLocalServiceUtil
				.getByCode(InvoicePortletKeys.PARAMETER_AUTH_GROUP_CODE, false);
		long siteGroupId = parameterAuthGroup.getGroupId();
		Parameter parameterSecretKey = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
				parameterAuthGroup.getParameterGroupId(), InvoicePortletKeys.PARAMETER_ESHOP_TMS_SECRET_KEY, false);
		return parameterSecretKey.getParamValue();
	}
	
	public static ResultOrderListDto getOrderItemList(CommerceOrder commerceOrder,int commerceOrderId, String invoiceNo, String fullName, String phoneNumber,String status, ThemeDisplay themeDisplay) throws PortalException {
		List<OrderDto> orderItemList = new ArrayList<>();
		int allOrderItemCount = CommerceOrderItemLocalServiceUtil.getCommerceOrderItemsCount(commerceOrderId);

//		CommerceOrder commerceOrder = CommerceOrderLocalServiceUtil.getCommerceOrder(commerceOrderId);
		invoiceNo = String.valueOf(commerceOrder.getCommerceOrderId());
		List<CommerceOrderItem> itemList = CommerceOrderItemLocalServiceUtil
				.getCommerceOrderItems(commerceOrderId, 0, allOrderItemCount);
		for (CommerceOrderItem commerceOrderItem : itemList) {
			Locale locale = LocaleUtil.getSiteDefault();
			OrderDto item = new OrderDto();
			item.setCommerceOrderId(commerceOrderId);
			item.setFullName(fullName);
			item.setPhoneNumber(phoneNumber);
			item.setOrderDate(commerceOrder.getOrderDate());
			item.setPaymentMethod(commerceOrder.getCommercePaymentMethodKey());
			item.setOrderStatus(status);
			item.setPaymentStatus(commerceOrder.getPaymentStatus());
			item.setInvoiceNo(String.valueOf(commerceOrder.getCommerceOrderId()));
			item.setSku(commerceOrderItem.getSku());
			item.setTaxAmount(CurrencyUtil.roundUpDollarAmount(String.valueOf(commerceOrder.getTaxAmount())));
			item.setTotalDiscountAmount(CurrencyUtil.roundUpDollarAmount(String.valueOf(commerceOrderItem.getDiscountAmount())));
			item.setNetPrice(CurrencyUtil.roundUpDollarAmount(String.valueOf(commerceOrderItem.getFinalPrice())));
			item.setExamName(commerceOrderItem.getName(locale));
			item.setUnitPrice(CurrencyUtil.roundUpDollarAmount(String.valueOf(commerceOrderItem.getPromoPrice()!=null && commerceOrderItem.getPromoPrice().floatValue()>0 ? 
	 				commerceOrderItem.getPromoPrice().floatValue() : commerceOrderItem.getUnitPrice().floatValue())));
			item.setQuantity(commerceOrderItem.getQuantity());
			item.setAmount(CurrencyUtil.roundUpDollarAmount(String.valueOf(commerceOrderItem.getFinalPriceWithTaxAmount()))); //amount including tax
			item.setBillingAddress(commerceOrder.getBillingAddress());
			item.setShippingAddress(commerceOrder.getShippingAddress());

			orderItemList.add(item);
		}
		ResultOrderListDto resultDto = new ResultOrderListDto(orderItemList, invoiceNo);
		return resultDto;
	}
	
	public static JSONObject getMerchandizeOrderHistory (int start, int limit, long accountId, ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
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
			String plainQuery = String.format(InvoiceSql.merchandizeSql);
			log.info(plainQuery);
			pStatement = connection.prepareStatement(plainQuery);
			pStatement.setLong(1, accountId);
			pStatement.setInt(2, start);
			pStatement.setInt(3, limit);
			result = pStatement.executeQuery();
			
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			String portletName = (String) resourceRequest.getAttribute(WebKeys.PORTLET_ID);

			String authToken = CSRFValidationUtil.authToken(resourceRequest);
			int accType = ParamUtil.getInteger(resourceRequest, "accType");
			String companyName = ParamUtil.getString(resourceRequest, "companyName");
			while (result.next()) {
				JSONObject item = JSONFactoryUtil.createJSONObject()
					.put("transactionId", result.getString("transactionId"))
					.put("amount", Validator.isNull(result.getString("amount")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("amount")))
					.put("orderDate",
							DateUtil.toString(result.getTimestamp("orderDate"), VIEW_DATE_FORMAT))
					.put("orderStatus", CommerceOrderConstants.getOrderStatusLabel(result.getInt("orderStatus")).toUpperCase())
					.put("paymentStatus", CommerceOrderPaymentConstants.getOrderPaymentStatusLabel(result.getInt("paymentStatus")).toUpperCase());
				PortletURL detailUrl = PortletURLFactoryUtil.create(
						PortalUtil.getHttpServletRequest(resourceRequest), portletName,
						themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
				detailUrl.getRenderParameters().setValue("mvcRenderCommandName",
						MVCCommandNames.MERCHANDIZE_ORDER_DETAIL_RENDER);
				detailUrl.getRenderParameters().setValue("companyName", companyName);
				detailUrl.getRenderParameters().setValue("accType", String.valueOf(accType));
				detailUrl.getRenderParameters().setValue("orderId", result.getString("transactionId"));
				detailUrl.getRenderParameters().setValue("authToken", authToken);
					item.put("detailUrl", detailUrl);
				temp.put(item);
				jsonObjects.add(item);
			}

			pStatementCount = connection.prepareStatement(
					String.format(InvoiceSql.merchandizeSqlCount));
			pStatementCount.setLong(1, accountId);
			pStatementCount.setInt(2, start);
			pStatementCount.setInt(3, limit);
			resultCount = pStatementCount.executeQuery();
			log.info(pStatementCount);
			while (resultCount.next()) {
				totalCount = resultCount.getLong("countItem") ;
				log.info(resultCount.getLong("countItem"));
			}
		} catch (Exception e) {
			log.error("Error getting merchandize order history. REASON:" + e.getMessage());
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
		return JSONFactoryUtil.createJSONObject().put(InvoiceConstants.DATA, temp)
				.put(InvoiceConstants.TOTAL_COUNT, totalCount);
	}
	
	public static JSONObject getExamOrderHistory (int start, int limit, long accountId, ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
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
			String plainQuery = String.format(InvoiceSql.examSql);
			log.info(plainQuery);
			pStatement = connection.prepareStatement(plainQuery);
			pStatement.setLong(1, accountId);
			pStatement.setInt(2, start);
			pStatement.setInt(3, limit);
			result = pStatement.executeQuery();
			
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			String portletName = (String) resourceRequest.getAttribute(WebKeys.PORTLET_ID);

			String authToken = CSRFValidationUtil.authToken(resourceRequest);
			int accType = ParamUtil.getInteger(resourceRequest, "accType");
			String companyName = ParamUtil.getString(resourceRequest, "companyName");
			while (result.next()) {
				JSONObject item = JSONFactoryUtil.createJSONObject()
					.put("transactionId", result.getString("transactionId"))
					.put("amount", Validator.isNull(result.getString("amount")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("amount")))
					.put("orderDate",
							DateUtil.toString(result.getTimestamp("orderDate"), VIEW_DATE_FORMAT))
					.put("orderStatus", CommerceOrderConstants.getOrderStatusLabel(result.getInt("orderStatus")).toUpperCase())
					.put("paymentStatus", CommerceOrderPaymentConstants.getOrderPaymentStatusLabel(result.getInt("paymentStatus")).toUpperCase());
				PortletURL detailUrl = PortletURLFactoryUtil.create(
						PortalUtil.getHttpServletRequest(resourceRequest), portletName,
						themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
				detailUrl.getRenderParameters().setValue("mvcRenderCommandName",
						MVCCommandNames.EXAM_ORDER_DETAIL_RENDER);
				detailUrl.getRenderParameters().setValue("companyName", companyName);
				detailUrl.getRenderParameters().setValue("accType", String.valueOf(accType));
				detailUrl.getRenderParameters().setValue("orderId", result.getString("transactionId"));
				detailUrl.getRenderParameters().setValue("authToken", authToken);
					item.put("detailUrl", detailUrl);
				temp.put(item);
				jsonObjects.add(item);
			}

			pStatementCount = connection.prepareStatement(
					String.format(InvoiceSql.examSqlCount));
			pStatementCount.setLong(1, accountId);
			pStatementCount.setInt(2, start);
			pStatementCount.setInt(3, limit);
			resultCount = pStatementCount.executeQuery();
			log.info(pStatementCount);
			while (resultCount.next()) {
				totalCount = resultCount.getLong("countItem") ;
				log.info(resultCount.getLong("countItem"));
			}
		} catch (Exception e) {
			log.error("Error getting exam order history. REASON:" + e.getMessage());
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
		return JSONFactoryUtil.createJSONObject().put(InvoiceConstants.DATA, temp)
				.put(InvoiceConstants.TOTAL_COUNT, totalCount);
	}
	
	public static JSONObject getExamDetailOrderHistory (int start, int limit, long accountId, String defaultLocale, long orderId, ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
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
			String plainQuery = String.format(InvoiceSql.examDetailSql);
			log.info(plainQuery);
			pStatement = connection.prepareStatement(plainQuery);
			pStatement.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
			pStatement.setLong(2, accountId);
			pStatement.setLong(3, orderId);
			pStatement.setInt(4, start);
			pStatement.setInt(5, limit);
			result = pStatement.executeQuery();
			
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			String portletName = (String) resourceRequest.getAttribute(WebKeys.PORTLET_ID);

			String authToken = CSRFValidationUtil.authToken(resourceRequest);
			int accType = ParamUtil.getInteger(resourceRequest, "accType");
			String companyName = ParamUtil.getString(resourceRequest, "companyName");
			while (result.next()) {
				JSONObject item = JSONFactoryUtil.createJSONObject()
					.put("invoiceNumber", result.getString("invoiceNumber"))
					.put("examTitle", result.getString("examTitle"))
					.put("examCode", result.getString("examCode"))
					.put("quantity", result.getInt("quantity"))
					.put("perUnit", Validator.isNull(result.getString("perUnit")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("perUnit")))
					.put("amount", Validator.isNull(result.getString("amount")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("amount")))
					.put("orderStatus", CommerceOrderConstants.getOrderStatusLabel(result.getInt("orderStatus")).toUpperCase())
					.put("paymentStatus", CommerceOrderPaymentConstants.getOrderPaymentStatusLabel(result.getInt("paymentStatus")).toUpperCase());
				temp.put(item);
				jsonObjects.add(item);
			}

			pStatementCount = connection.prepareStatement(
					String.format(InvoiceSql.examDetailSqlCount));
			pStatementCount.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
			pStatementCount.setLong(2, accountId);
			pStatementCount.setLong(3, orderId);
			pStatementCount.setInt(4, start);
			pStatementCount.setInt(5, limit);
			resultCount = pStatementCount.executeQuery();
			log.info(pStatementCount);
			while (resultCount.next()) {
				totalCount = resultCount.getLong("countItem") ;
				log.info(resultCount.getLong("countItem"));
			}
		} catch (Exception e) {
			log.error("Error getting exam order history. REASON:" + e.getMessage());
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
		return JSONFactoryUtil.createJSONObject().put(InvoiceConstants.DATA, temp)
				.put(InvoiceConstants.TOTAL_COUNT, totalCount);
	}
	
	public static JSONObject getMerchandizeDetailOrderHistory (int start, int limit, long accountId, String defaultLocale, long orderId, ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
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
			String plainQuery = String.format(InvoiceSql.merchandizeDetailSql);
			log.info(plainQuery);
			pStatement = connection.prepareStatement(plainQuery);
			pStatement.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
			pStatement.setLong(2, accountId);
			pStatement.setLong(3, orderId);
			pStatement.setInt(4, start);
			pStatement.setInt(5, limit);
			result = pStatement.executeQuery();
			
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			String portletName = (String) resourceRequest.getAttribute(WebKeys.PORTLET_ID);

			String authToken = CSRFValidationUtil.authToken(resourceRequest);
			int accType = ParamUtil.getInteger(resourceRequest, "accType");
			String companyName = ParamUtil.getString(resourceRequest, "companyName");
			while (result.next()) {
				JSONObject item = JSONFactoryUtil.createJSONObject()
					.put("invoiceNumber", result.getString("invoiceNumber"))
					.put("merchTitle", result.getString("merchTitle"))
					.put("merchCode", result.getString("merchCode"))
					.put("quantity", result.getInt("quantity"))
					.put("perUnit", Validator.isNull(result.getString("perUnit")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("perUnit")))
					.put("amount", Validator.isNull(result.getString("amount")) ? CurrencyUtil.roundUpDollarAmount("0") : CurrencyUtil.roundUpDollarAmount(result.getString("amount")))
					.put("orderStatus", CommerceOrderConstants.getOrderStatusLabel(result.getInt("orderStatus")).toUpperCase())
					.put("paymentStatus", CommerceOrderPaymentConstants.getOrderPaymentStatusLabel(result.getInt("paymentStatus")).toUpperCase());
				temp.put(item);
				jsonObjects.add(item);
			}

			pStatementCount = connection.prepareStatement(
					String.format(InvoiceSql.merchDetailSqlCount));
			pStatementCount.setString(1, "root/Name[@language-id='" + defaultLocale + "']");
			pStatementCount.setLong(2, accountId);
			pStatementCount.setLong(3, orderId);
			pStatementCount.setInt(4, start);
			pStatementCount.setInt(5, limit);
			resultCount = pStatementCount.executeQuery();
			log.info(pStatementCount);
			while (resultCount.next()) {
				totalCount = resultCount.getLong("countItem") ;
				log.info(resultCount.getLong("countItem"));
			}
		} catch (Exception e) {
			log.error("Error getting merchandize order history. REASON:" + e.getMessage());
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
		return JSONFactoryUtil.createJSONObject().put(InvoiceConstants.DATA, temp)
				.put(InvoiceConstants.TOTAL_COUNT, totalCount);
	}
	
//	@Reference
//	private static CommerceOrderStatusRegistry _commerceOrderStatusRegistry;

}
