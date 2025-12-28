package web.ntuc.eshop.reports.resource;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

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

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.EXPORT_SPECIAL_PRODUCT_RESOURCE, "javax.portlet.name="
		+ ReportPortletKeys.SPECIAL_PRODUCT_REPORT_PORTLET }, service = MVCResourceCommand.class)
public class ExportSpecialProductResource implements MVCResourceCommand{
	private static Log log = LogFactoryUtil.getLog(ExportSpecialProductResource.class);

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Export special product resources - start");
		SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy");
		try {
			String defaultLocale = LocaleUtil.getDefault().toString();
	        final int iSortColumnIndex = ParamUtil.getInteger(resourceRequest, ReportConstant.I_SORT_COL_0);
	        final boolean ascending = ParamUtil.getString(resourceRequest, ReportConstant.S_SORT_DIR_0)
	                .equalsIgnoreCase("asc");
	        final String orderByColumn = iSortColumnIndex == 0 ? "productName" : "citrepStatus";

	        JSONObject result = ReportUtil.getSpecialProductReportData(
	                0,
	                Integer.MAX_VALUE,
	                orderByColumn,
	                ascending,
	                defaultLocale);
	        String[] headers = { "Product Name", "CITREP Status"};
			String[] dataKeys = { "productName", "citrepStatus" };
			String sbExport = ExportUtil.exportDataToStringBuilderCsv(headers,(List<JSONObject>) result.get(ReportConstant.EXPORT_DATA), dataKeys, ReportConstant.DELIMITER);
			resourceResponse.setContentType("text/csv");
			resourceResponse.setProperty("content-disposition",
					"attachment; filename=Special Product Report.csv");
			OutputStream out = resourceResponse.getPortletOutputStream();
			out.write(sbExport.getBytes());
			out.flush();
			out.close();
		}catch (Exception e) {
			log.error("Error while exporting special product data : " + e.getMessage());
			return true;
		}
		log.info("Export special product resources - end");
		return false;
	}
}
