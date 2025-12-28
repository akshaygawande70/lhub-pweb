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

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.EXPORT_PRODUCT_INVENTORY_DETAILS_RESOURCE, "javax.portlet.name="
		+ ReportPortletKeys.PRODUCT_INVENTORY_DETAILS_REPORT_PORTLET }, service = MVCResourceCommand.class)
public class ExportProductInventoryDetailsResource implements MVCResourceCommand{
	private static Log log = LogFactoryUtil.getLog(ExportProductInventoryDetailsResource.class);
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Export product inventory detail resources - start");
//		SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			String defaultLocale = LocaleUtil.getDefault().toString();
	        HttpServletRequest httpRequest = PortalUtil
	                .getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
	        final int iDisplayStart = ParamUtil.getInteger(httpRequest, ReportConstant.I_DISPLAY_START);
	        final int iDisplayLength = ParamUtil.getInteger(httpRequest, ReportConstant.I_DISPLAY_LENGTH);
	        final boolean ascending = ParamUtil.getString(httpRequest, ReportConstant.S_SORT_DIR_0)
	                .equalsIgnoreCase("asc");
	        final String orderByColumn = getOrderByColumn(ParamUtil.getInteger(httpRequest, ReportConstant.I_SORT_COL_0));
	        String category = ParamUtil.getString(resourceRequest, ReportConstant.CATEGORY);
	        final int status = ParamUtil.getInteger(resourceRequest, ReportConstant.STATUS);
	        String productName = ParamUtil.getString(resourceRequest, ReportConstant.PRODUCT_NAME);
	        String sku = ParamUtil.getString(resourceRequest, ReportConstant.SKU);
	        String availableStock = ParamUtil.getString(resourceRequest, ReportConstant.AVAILABLE_STOCK);
	        String fromDate = ParamUtil.getString(resourceRequest, ReportConstant.FROM_DATE);

	        JSONObject result = ReportUtil.getProductInventoryDetailsData(
	                0,
	                Integer.MAX_VALUE,
	                orderByColumn,
	                ascending,
	                category,
	                status,
	                productName,
	                sku,
	                availableStock,
	                defaultLocale,
	                fromDate);
	        
	        String[] headers = { "Creation Date", "Category", "Product Name", "SKU",  "Status", "Unit Price", "Admin Fee", "Net Price","Total Gross Profit","GP Margin","Sold Qty", "Available Stock"};
			String[] dataKeys = { "productCreationDate", "category", "productName", "sku","status", "costPrice", "admin_fee", "basePrice","grossProfit","gpMargin","soldQty","availableStock"};
			String sbExport = ExportUtil.exportDataToStringBuilderCsv(headers,(List<JSONObject>) result.get(ReportConstant.EXPORT_DATA), dataKeys, ReportConstant.DELIMITER);

			
			String fileName = "Product Inventory Details Report ";
			fileName +="[";
			StringJoiner joiner = new StringJoiner(" ");
			if(!Validator.isBlank(fromDate)) joiner.add("From="+fromDate);
			if(!Validator.isBlank(productName)) joiner.add("Item="+productName);
			if(!Validator.isBlank(sku)) joiner.add("SKU="+sku);
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
			log.error("Error while exporting product inventory detail data : " + e.getMessage());
			return true;
		}
		log.info("Export product inventory detail resources - end");
		return false;
	}
	
	 private static String getOrderByColumn(final int iSortColumnIndex) {
	        String orderBy = "productCreationDate";
	        switch (iSortColumnIndex) {
	            case 1:
	                orderBy = "category";
	                break;
	            case 3:
	                orderBy = "productName";
	                break;
	            case 4:
	                orderBy = "sku";
	                break;
	            case 5:
	                orderBy = "status";
	                break;
	            case 6:
	                orderBy = "costPrice";
	                break;
	            case 7:
	                orderBy = "admin_fee";
	                break;
	            case 8:
	                orderBy = "basePrice";
	                break;
	            case 9:
	                orderBy = "grossProfit";
	                break;
	            case 10:
	                orderBy = "gpMargin";
	                break;
	            case 11:
	                orderBy = "soldQty";
	                break;
	            case 12:
	                orderBy = "availableStock";
	                break;
	            default:
	                break;
	        }
	        return orderBy;
	    }
    
}