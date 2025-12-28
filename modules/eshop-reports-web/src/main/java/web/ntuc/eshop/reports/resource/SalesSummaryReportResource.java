package web.ntuc.eshop.reports.resource;

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.constants.CommercePaymentConstants;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.model.CommerceOrderPayment;
import com.liferay.commerce.service.CommerceOrderItemLocalServiceUtil;
import com.liferay.commerce.service.CommerceOrderPaymentLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Order;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.CurrencyUtil;
import api.ntuc.common.util.DateUtil;
import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.reports.constants.MVCCommandNames;
import web.ntuc.eshop.reports.constants.ReportConstant;
import web.ntuc.eshop.reports.constants.ReportPortletKeys;
import web.ntuc.eshop.reports.dto.SalesDto;
import web.ntuc.eshop.reports.util.ReportUtil;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.SALES_AND_SUMMARY_RESOURCE,
		"javax.portlet.name="
				+ ReportPortletKeys.SALES_AND_SUMMARY_REPORT_PORTLET }, service = MVCResourceCommand.class)
public class SalesSummaryReportResource implements MVCResourceCommand {

	private Log log = LogFactoryUtil.getLog(SalesSummaryReportResource.class);
	private SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy");
	Locale locale = new Locale("en", "US");
	private SimpleDateFormat newFmt = new SimpleDateFormat("dd/MM/yyyy", locale);
	private final SimpleDateFormat VIEW_DATE_FORMAT = new SimpleDateFormat("dd/MMM/yyyy");
//	private final SimpleDateFormat VIEW_DATE_FORMAT = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Sales and Summary Report Resource - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			Locale locale = themeDisplay.getLocale();
			PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

			HttpServletRequest httpRequest = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
			Integer iDisplayStart = ParamUtil.getInteger(httpRequest, "iDisplayStart");
			Integer iDisplayLength = ParamUtil.getInteger(httpRequest, "iDisplayLength");
			if (iDisplayLength == 0) {
				iDisplayLength = 5;
			}

			Integer iSortColumnIndex = ParamUtil.getInteger(httpRequest, "iSortCol_0");
			String sSortDirection = ParamUtil.getString(httpRequest, "sSortDir_0");
			int sEcho = Integer.parseInt(ParamUtil.getString(httpRequest, "sEcho", "0"));

			Order order = null;
			boolean ascending = sSortDirection.equals("asc") ? true : false;
//			log.info("sort = "+sSortDirection.equals("asc"));
			String orderByColumn = this.getOrderByColumn(iSortColumnIndex);

			if (ascending) {
				order = OrderFactoryUtil.asc(orderByColumn);
			} else {
				order = OrderFactoryUtil.desc(orderByColumn);
			}

			int start = iDisplayStart;
			int end = start + iDisplayLength;


			String fromDate = ParamUtil.getString(resourceRequest, "fromDate", "");
			String toDate = ParamUtil.getString(resourceRequest, "toDate", "");
			int status = ParamUtil.getInteger(resourceRequest, ReportConstant.STATUS); 
			
			Date tempFromDate = null;
			Date tempToDate = null;
			if (!fromDate.equals("") && !toDate.equals("")) {
				tempFromDate = fmt.parse(fromDate);
				tempToDate = fmt.parse(toDate);
			}

			List<CommerceOrder> orderList = ReportUtil.getList(tempFromDate, tempToDate, start, end, order, status);
			List<CommerceOrder> filteredOrderList = orderList.stream().filter(x -> x.getTotal().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
//			log.info(orderList);
			String authToken = CSRFValidationUtil.authToken(resourceRequest);

			List<SalesDto> salesList = new ArrayList<SalesDto>();
			for (CommerceOrder cOrder : filteredOrderList) {
//				log.info("====order=======");
//				log.info(cOrder);
//				log.info("====order=======");
//				log.info("commerceOrderId = "+cOrder.getCommerceOrderId());
				List<CommerceOrderItem> orderItemList = cOrder.getCommerceOrderItems();
				for (CommerceOrderItem item : orderItemList) {
//					log.info("====item=======");
//					log.info(item);
//					log.info("====item=======");
					SalesDto dto = new SalesDto();
					dto.setInvoiceId(cOrder.getCommerceOrderId());
					dto.setInvoiceDate(cOrder.getOrderDate());
					dto.setInvoiceNo(cOrder.getPurchaseOrderNumber());
					dto.setItem(item.getName());
					dto.setSKU(item.getSku());
					dto.setOrderStatus(cOrder.getOrderStatus());
					dto.setQuantity(item.getQuantity());
//					dto.setTotal(CurrencyUtil.roundUpDollarAmount(String.valueOf(item.getFinalPriceWithTaxAmount())));
					dto.setUnitPrice(CurrencyUtil.roundUpDollarAmount(String.valueOf(item.getUnitPrice())));
					dto.setTotalDiscountAmount(CurrencyUtil.roundUpDollarAmount(String.valueOf(item.getDiscountAmount())));
					dto.setNetPrice(CurrencyUtil.roundUpDollarAmount(String.valueOf(item.getFinalPrice())));
					dto.setTaxAmount(CurrencyUtil.roundUpDollarAmount(String.valueOf(cOrder.getTaxAmount())));
					dto.setTotal(CurrencyUtil.roundUpDollarAmount(String.valueOf(cOrder.getTotal())));
					DynamicQuery orderPaymentQuery = CommerceOrderPaymentLocalServiceUtil.dynamicQuery();
					orderPaymentQuery.add(PropertyFactoryUtil.forName("commerceOrderId").eq(cOrder.getCommerceOrderId()));
					orderPaymentQuery.add(PropertyFactoryUtil.forName("status").eq(CommerceOrderConstants.PAYMENT_STATUS_PAID));
					List<CommerceOrderPayment> commerceOrderPayments = CommerceOrderPaymentLocalServiceUtil.dynamicQuery(orderPaymentQuery);
//					List<CommerceOrderPayment> commerceOrderPayments = CommerceOrderPaymentLocalServiceUtil.getCommerceOrderPayments(cOrder.getCommerceOrderId(), -1, -1, null);
//					log.info(commerceOrderPayments);
					String stripeId = StringPool.BLANK;
	                String stripeLink = "#";
					if(commerceOrderPayments.size() > 0) {
						String contentStr = commerceOrderPayments.get(0).getContent();
						List<String> contentArr = new ArrayList<>();
		                if(contentStr.length()>2) {
		                	contentStr = contentStr.substring(1, contentStr.length()-1);
		                	contentArr = Arrays.asList(contentStr.split(","));
		                }
		                
		                for(int i =0; i<contentArr.size(); i++) {
		                	if(i==0) stripeId = contentArr.get(i);
		                	if(i==1) stripeLink = contentArr.get(i);
		                }
		                
					}
					dto.setStripeId(stripeId);
	                dto.setStripeLink(stripeLink);
					salesList.add(dto);
				}
			}
//			log.info("size ===== "+salesList.size());
			int allCount = 0;
//			if (!fromDate.equals("") && !toDate.equals("")) {
//				allCount = salesList.size();
//			} else {
				List<CommerceOrder> allOrders = ReportUtil.getList(tempFromDate, tempToDate, -1, -1, order,status);
				List<CommerceOrder> filteredAllOrders = allOrders.stream().filter(x -> x.getTotal().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
				allCount = filteredAllOrders.size();
//			}

			JSONArray parameterJsonArray = JSONFactoryUtil.createJSONArray();
			for (SalesDto dto : salesList) {
				JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();
				jsonBranch.put("orderDate", DateUtil.toString(dto.getInvoiceDate(),VIEW_DATE_FORMAT));
				jsonBranch.put("purchaseOrderNumber", dto.getInvoiceNo());
				jsonBranch.put("orderId", dto.getInvoiceId());
				jsonBranch.put("item", dto.getItem());
				jsonBranch.put("sku", dto.getSKU());
				jsonBranch.put("orderStatus",  CommerceOrderConstants.getOrderStatusLabel(dto.getOrderStatus()).toUpperCase());
				jsonBranch.put("quantity", dto.getQuantity());
				jsonBranch.put("unitPrice", dto.getUnitPrice());
				jsonBranch.put("promotion", dto.getTotalDiscountAmount());
				jsonBranch.put("netPrice", dto.getNetPrice());
				jsonBranch.put("gst", dto.getTaxAmount());
				jsonBranch.put("totalPrice", dto.getTotal());
				jsonBranch.put("stripeId", dto.getStripeId());
				jsonBranch.put("stripeLink", dto.getStripeLink());

				parameterJsonArray.put(jsonBranch);
			}
//			log.info(parameterJsonArray);
			JSONObject tableData = JSONFactoryUtil.createJSONObject();
			tableData.put("iTotalRecords", allCount);
			tableData.put("iTotalDisplayRecords", allCount);
			tableData.put("sEcho", sEcho);
			tableData.put("aaData", parameterJsonArray);
			resourceResponse.getWriter().println(tableData.toString());
//			} else {
//				JSONArray emptyArr = JSONFactoryUtil.createJSONArray();
//				JSONObject tableData = JSONFactoryUtil.createJSONObject();
//				tableData.put("iTotalRecords", 0);
//				tableData.put("iTotalDisplayRecords", 0);
//				tableData.put("sEcho", 1);
//				tableData.put("aaData", emptyArr);
//				resourceResponse.getWriter().println(tableData.toString());
//			}

		} catch (Exception e) {
			log.error("Found error at " + e.getMessage());
		}

		log.info("Sales and Summary Report Resource - End");
		return false;
	}

	private String getOrderByColumn(Integer iSortColumnIndex) {
		String orderBy = "";
		if (iSortColumnIndex == 0) {
			orderBy = "orderDate";
		} else if (iSortColumnIndex == 1) {
			orderBy = "orderId";
		} else if (iSortColumnIndex == 2) {
			orderBy = "item";
		} else if (iSortColumnIndex == 3) {
			orderBy = "sku";
		} else if (iSortColumnIndex == 4) {
			orderBy = "orderStatus";
		} else if (iSortColumnIndex == 5) {
			orderBy = "quantity";
		} else if (iSortColumnIndex == 6) {
			orderBy = "unitPrice";
		} else if (iSortColumnIndex == 7) {
			orderBy = "totalPrice";
		}
		return orderBy;
	}
}
