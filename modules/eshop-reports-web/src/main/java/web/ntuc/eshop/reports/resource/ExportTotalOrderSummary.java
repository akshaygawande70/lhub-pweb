package web.ntuc.eshop.reports.resource;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.StringJoiner;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.ExportUtil;
import web.ntuc.eshop.reports.constants.MVCCommandNames;
import web.ntuc.eshop.reports.constants.ReportConstant;
import web.ntuc.eshop.reports.constants.ReportPortletKeys;
import web.ntuc.eshop.reports.util.ReportUtil;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.EXPORT_TOTAL_ORDER_SUMMARY_RESOURCE, "javax.portlet.name="
		+ ReportPortletKeys.TOTAL_ORDER_SUMMARY_REPORT_PORTLET }, service = MVCResourceCommand.class)
public class ExportTotalOrderSummary implements MVCResourceCommand {
	private static Log log = LogFactoryUtil.getLog(ExportTotalOrderSummary.class);

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Export Total Order Summary resources - start");
//		SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy");
		try {
			final int iSortColumnIndex = ParamUtil.getInteger(resourceRequest, ReportConstant.I_SORT_COL_0);
	        final boolean ascending = ParamUtil.getString(resourceRequest, ReportConstant.S_SORT_DIR_0)
	                .equalsIgnoreCase("asc");
	        final String orderByColumn = getOrderByColumn(iSortColumnIndex);
	        String fromDate = ParamUtil.getString(resourceRequest, ReportConstant.FROM_DATE);
	        String toDate = ParamUtil.getString(resourceRequest, ReportConstant.TO_DATE);
	        
	        JSONObject result = ReportUtil.getTotalOrderSummaryData(
	                0,
	                Integer.MAX_VALUE,
	                orderByColumn,
	                ascending,
	                fromDate,
	                toDate);
	        log.info(result);
	        String[] headers = { "Order Date", "Order Id", "Stripe Id", "Gross Price", "Cost Price", "Gross Profit"};
			String[] dataKeys = { "orderDate", "orderId", "stripeId", "grossPrice","costPrice", "grossProfit" };
			String sbExport = ExportUtil.exportDataToStringBuilderCsv(headers,(List<JSONObject>) result.get(ReportConstant.EXPORT_DATA), dataKeys, ReportConstant.DELIMITER);
			
			String fileName = "Total Order Summary Report ";
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
			log.error("Error while exporting Total Order Summary data : " + e.getMessage());
			return true;
		}
		log.info("Export Total Order Summary resources - end");
		return false;
	}
	
	private static String getOrderByColumn(final int iSortColumnIndex) {
        String orderBy = "orderDate";
        if (iSortColumnIndex == 3) {
            orderBy = "grossPrice";
        }
        return orderBy;
    }
}
