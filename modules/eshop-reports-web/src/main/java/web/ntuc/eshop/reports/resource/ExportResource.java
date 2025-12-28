package web.ntuc.eshop.reports.resource;

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.model.CommerceOrderPayment;
import com.liferay.commerce.order.status.CommerceOrderStatusRegistry;
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
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.legacy.searcher.SearchRequestBuilderFactory;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.CurrencyUtil;
import api.ntuc.common.util.DateUtil;
import api.ntuc.common.util.ExportUtil;
import web.ntuc.eshop.reports.constants.MVCCommandNames;
import web.ntuc.eshop.reports.constants.ReportConstant;
import web.ntuc.eshop.reports.constants.ReportPortletKeys;
import web.ntuc.eshop.reports.dto.SalesDto;
import web.ntuc.eshop.reports.util.ReportUtil;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.EXPORT_RESOURCE, "javax.portlet.name="
		+ ReportPortletKeys.SALES_AND_SUMMARY_REPORT_PORTLET }, service = MVCResourceCommand.class)
public class ExportResource implements MVCResourceCommand {
	private static Log log = LogFactoryUtil.getLog(ExportResource.class);
	private static final SimpleDateFormat VIEW_DATE_FORMAT = new SimpleDateFormat("dd/MMM/yyyy");
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Export course resources - start");
		SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			String fromDate = ParamUtil.getString(resourceRequest, "fromDate", "");
			String toDate = ParamUtil.getString(resourceRequest, "toDate", "");
			int status = ParamUtil.getInteger(resourceRequest, ReportConstant.STATUS);
			Date tempFromDate = null;
			Date tempToDate = null;
			if (!fromDate.equals("") && !toDate.equals("")) {
				tempFromDate = fmt.parse(fromDate);
				tempToDate = fmt.parse(toDate);
			}
			Order order = OrderFactoryUtil.desc("orderDate");
			List<CommerceOrder> orderList = ReportUtil.getList(tempFromDate, tempToDate, -1, -1, order, status);
			List<CommerceOrder> filteredOrderList = orderList.stream().filter(x -> x.getTotal().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
			List<SalesDto> salesList = new ArrayList<SalesDto>();
			String defaultLocale = LocaleUtil.getDefault().toString();
			for (CommerceOrder cOrder : filteredOrderList) {
//				log.info("commerceOrderId = "+cOrder.getCommerceOrderId());
				List<CommerceOrderItem> orderItemList = cOrder.getCommerceOrderItems();
				for (CommerceOrderItem item : orderItemList) {
					SalesDto dto = new SalesDto();
					dto.setInvoiceId(cOrder.getCommerceOrderId());
					dto.setInvoiceDate(cOrder.getOrderDate());
					dto.setInvoiceNo(cOrder.getPurchaseOrderNumber());
					dto.setItem(item.getName(defaultLocale));
					dto.setSKU(item.getSku());
					dto.setOrderStatus(cOrder.getOrderStatus());
					dto.setQuantity(item.getQuantity());
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
			List<JSONObject> parameterJson = new ArrayList<>();
			for (SalesDto dto : salesList) {
				JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();
				jsonBranch.put("orderDate", DateUtil.toString(dto.getInvoiceDate(), VIEW_DATE_FORMAT));
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

				parameterJson.add(jsonBranch);
			}
			
			/*List<JSONObject> jsonObjects = new ArrayList<>();
			for (SalesDto s : allSalesDto) {
				JSONObject json = JSONFactoryUtil.createJSONObject();
				json.put("invoice_date", s.getInvoiceDate());
				json.put("invoice_no", s.getInvoiceNo());
				json.put("item", s.getItem());
				json.put("sku", s.getSKU());
				json.put("quantity", s.getQuantity());
				json.put("unit_price", s.getUnitPrice());
				json.put("total", s.getTotal());

				jsonObjects.add(json);
			}*/

			String[] headers = { "Invoice Date", "Invoice No", "Stripe Id", "Item", "SKU","Order Status", "Quantity", "Unit Price","Promotion / Discount","Net Price","GST","Total" };
			String[] dataKeys = { "orderDate", "orderId", "stripeId", "item","sku","orderStatus", "quantity", "unitPrice","promotion","netPrice","gst", "totalPrice" };
			String sbExport = ExportUtil.exportDataToStringBuilderCsv(headers, parameterJson, dataKeys, ReportConstant.DELIMITER);

			Map<Integer, String> statuses = ReportUtil.getStatusList(themeDisplay,_commerceOrderStatusRegistry);
			String fileName = "Sales Details and Summary ";
			fileName +="[";
			StringJoiner joiner = new StringJoiner(" ");
			if(!Validator.isBlank(fromDate) && !Validator.isBlank(toDate)) joiner.add("From="+fromDate+" "+"To="+toDate);
			if(status != -1) joiner.add("Status="+statuses.get(status));
			fileName += joiner.toString();
			fileName +="]";
			fileName += ".csv";
			resourceResponse.setContentType("text/csv");
			resourceResponse.setProperty("content-disposition",
					"attachment; filename=" + fileName);
			OutputStream out = resourceResponse.getPortletOutputStream();
			out.write(sbExport.getBytes());
			out.flush();
			out.close();

		} catch (Exception e) {
			log.error("Error while exporting data : " + e.getMessage());
			return true;
		}
		log.info("Export course resources - end");
		return false;
	}

	@Reference
	SearchRequestBuilderFactory _searchRequestBuilderFactory;
	
	@Reference
	private CommerceOrderStatusRegistry _commerceOrderStatusRegistry;

}
