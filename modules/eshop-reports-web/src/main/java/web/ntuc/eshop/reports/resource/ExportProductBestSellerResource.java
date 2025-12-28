package web.ntuc.eshop.reports.resource;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.StringJoiner;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.ExportUtil;
import web.ntuc.eshop.reports.constants.MVCCommandNames;
import web.ntuc.eshop.reports.constants.ReportConstant;
import web.ntuc.eshop.reports.constants.ReportPortletKeys;
import web.ntuc.eshop.reports.util.ReportUtil;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.EXPORT_PRODUCT_BEST_SELLER_RESOURCE, "javax.portlet.name="
		+ ReportPortletKeys.PRODUCT_BEST_SELLER_REPORT_PORTLET }, service = MVCResourceCommand.class)
public class ExportProductBestSellerResource implements MVCResourceCommand {
	private static Log log = LogFactoryUtil.getLog(ExportProductBestSellerResource.class);
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Export product best seller resources - start");
//		SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy");
		try {
			String defaultLocale = LocaleUtil.getDefault().toString();
	       
	        final int iSortColumnIndex = ParamUtil.getInteger(resourceRequest, ReportConstant.I_SORT_COL_0);
	        final boolean ascending = ParamUtil.getString(resourceRequest, ReportConstant.S_SORT_DIR_0)
	                .equalsIgnoreCase("asc");
	        final String orderByColumn = getOrderByColumn(iSortColumnIndex);
	        String searchKeyword = ParamUtil.getString(resourceRequest, ReportConstant.S_SEARCH);
	        String fromDate = ParamUtil.getString(resourceRequest, ReportConstant.FROM_DATE);
	        String toDate = ParamUtil.getString(resourceRequest, ReportConstant.TO_DATE);
	        
	        log.info("getting product best seller with keyword:"
	                + searchKeyword + " from:" + fromDate + " to:" + toDate+" orderByColumn : "+orderByColumn+" ascending : "+ascending);
	        
	        JSONObject result = ReportUtil.getProductBestSellerData(
	                0,
	                Integer.MAX_VALUE,
	                orderByColumn,
	                ascending,
	                searchKeyword,
	                fromDate,
	                toDate,
	                defaultLocale);
	        
	        String[] headers = { "Transaction Start", "Transaction End", "Category", "Product Name", "SKU", "Sold Qty", "Unit Cost of Product", "Admin Fee", "Unit Gross Selling Price", "Total Gross Selling Price", "Total Cost of Product", "Total Gros Profit", "GP Margin %"};
			String[] dataKeys = { "transactionStart", "transactionEnd", "category", "productName","sku", "soldQty", "costPrice","admin_fee", "basePrice", "grossSellingPrice", "costOfProduct", "grossProfit", "gpMargin", "total" };
			String sbExport = ExportUtil.exportDataToStringBuilderCsv(headers,(List<JSONObject>) result.get(ReportConstant.EXPORT_DATA), dataKeys, ReportConstant.DELIMITER);
			String fileName = "Product Best Seller Report ";
			fileName +="[";
			StringJoiner joiner = new StringJoiner(" ");
			if(!Validator.isBlank(fromDate) && !Validator.isBlank(toDate)) joiner.add("From="+fromDate+" "+"To="+toDate);
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
			log.error("Error while exporting product best seller data : " + e.getMessage());
			return true;
		}
		log.info("Export product best seller resources - end");
		return false;
	}
	
	private static String getOrderByColumn(final int iSortColumnIndex) {
        String orderBy = "transactionStart";
        switch (iSortColumnIndex) {
            case 1:
                orderBy = "transactionEnd";
                break;
            case 2:
                orderBy = "category";
                break;
            case 3:
                orderBy = "productName";
                break;
            case 4:
                orderBy = "sku";
                break;
            case 5:
                orderBy = "soldQty";
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
                orderBy = "grossSellingPrice";
                break;
            case 10:
                orderBy = "costOfProduct";
                break;
            case 11:
                orderBy = "grossProfit";
                break;
            case 12:
                orderBy = "gpMargin";
                break;
            default:
                break;
        }
        return orderBy;
    }
}
