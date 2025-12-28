package web.ntuc.eshop.reports.resource;

import com.liferay.commerce.order.status.CommerceOrderStatusRegistry;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.ExportUtil;
import web.ntuc.eshop.reports.constants.MVCCommandNames;
import web.ntuc.eshop.reports.constants.ReportConstant;
import web.ntuc.eshop.reports.constants.ReportPortletKeys;
import web.ntuc.eshop.reports.util.ReportUtil;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.EXPORT_ORDER_DETAIL_RESOURCE, "javax.portlet.name="
		+ ReportPortletKeys.ORDER_DETAIL_REPORT_PORTLET }, service = MVCResourceCommand.class)
public class ExportOrderDetailResource implements MVCResourceCommand{
	private static Log log = LogFactoryUtil.getLog(ExportOrderDetailResource.class);
	
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Export order detail resources - start");
//		SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy");
		try {
			String defaultLocale = LocaleUtil.getDefault().toString();
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			HttpServletRequest httpRequest = PortalUtil
	                .getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
	        final int iDisplayStart = ParamUtil.getInteger(httpRequest, ReportConstant.I_DISPLAY_START);
	        final int iDisplayLength = ParamUtil.getInteger(httpRequest, ReportConstant.I_DISPLAY_LENGTH);
			final boolean ascending = ParamUtil.getString(resourceRequest, ReportConstant.S_SORT_DIR_0)
	                .equalsIgnoreCase("asc");
	        final String orderByColumn = getOrderByColumn(ParamUtil.getInteger(resourceRequest, ReportConstant.I_SORT_COL_0));
	        String fromDate = ParamUtil.getString(resourceRequest, ReportConstant.FROM_DATE);
	        String toDate = ParamUtil.getString(resourceRequest, ReportConstant.TO_DATE);
	        final long orderId = ParamUtil.getLong(resourceRequest, ReportConstant.ORDER_ID);
	        final int status = ParamUtil.getInteger(resourceRequest, ReportConstant.STATUS);
	        String productName = ParamUtil.getString(resourceRequest, ReportConstant.PRODUCT_NAME);

	        JSONObject result = ReportUtil.getOrderDetailData(
	                0,
	                Integer.MAX_VALUE,
	                orderByColumn,
	                ascending,
	                fromDate,
	                toDate,
	                orderId,
	                status,
	                productName,
	                defaultLocale);
	        log.info(result);
	        
	        String[] headers = { "Order Created Date", "Order Id", "Stripe Id","Customer Email","Customer Name", "Order Status", "Product Name", "SKU", "Quantity","Unit Price","Discount","Net Price","GST","Total"};
			String[] dataKeys = { "orderCreatedDate", "orderId", "stripeId", "custEmail", "custName", "status","productName", "sku", "quantity","unitPrice","discount","basePrice", "gst", "total" };
			String sbExport = ExportUtil.exportDataToStringBuilderCsv(headers,(List<JSONObject>) result.get(ReportConstant.EXPORT_DATA), dataKeys, ReportConstant.DELIMITER);

			Map<Integer, String> statuses = ReportUtil.getStatusList(themeDisplay,_commerceOrderStatusRegistry);
			String fileName = "Order Detail Report ";
			fileName +="[";
			StringJoiner joiner = new StringJoiner(" ");
			if(!Validator.isBlank(fromDate) && !Validator.isBlank(toDate)) joiner.add("From="+fromDate+" "+"To="+toDate);
			if(orderId != 0) joiner.add("Order Number= "+orderId);
			if(status != -1) joiner.add("Status="+statuses.get(status));
			if(!Validator.isBlank(productName)) joiner.add("Item="+productName);
			fileName += joiner.toString();
			fileName +="]";
			fileName +=".csv";
			resourceResponse.setContentType("text/csv");
			resourceResponse.setProperty("content-disposition",
					"attachment; filename=" + fileName);
			OutputStream out = resourceResponse.getPortletOutputStream();
			out.write(sbExport.getBytes());
			out.flush();
			out.close();
		}catch (Exception e) {
			log.error("Error while exporting order detail data : " + e.getMessage());
			return true;
		}
		log.info("Export order detail resources - end");
		return false;
	}
	
	private static String getOrderByColumn(final int iSortColumnIndex) {
        String orderBy = "orderCreatedDate";
        switch (iSortColumnIndex) {
            case 1:
                orderBy = "orderId";
                break;
            case 3:
                orderBy = "custEmail";
                break;
            case 4:
                orderBy = "custName";
                break;
            case 5:
                orderBy = "status";
                break;
            case 6:
                orderBy = "productName";
                break;
            case 7:
                orderBy = "sku";
                break;
            case 8:
                orderBy = "quantity";
                break;
            case 9:
                orderBy = "unitPrice";
                break;
            case 10:
                orderBy = "discount";
                break;
            case 11:
                orderBy = "netPrice";
                break;
            case 12:
                orderBy = "gst";
                break;
            case 13:
                orderBy = "total";
                break;
            default:
                break;
        }
        return orderBy;
    }
    
	@Reference
	private CommerceOrderStatusRegistry _commerceOrderStatusRegistry;
}
