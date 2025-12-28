package web.ntuc.eshop.reports.resource;
import com.liferay.commerce.order.status.CommerceOrderStatusRegistry;
import com.liferay.petra.string.StringPool;
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

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.EXPORT_PROMOTION_AND_DISCOUNT_DETAILS_RESOURCE, "javax.portlet.name="
		+ ReportPortletKeys.PROMOTION_AND_DISCOUNT_DETAILS_REPORT_PORTLET }, service = MVCResourceCommand.class)

public class ExportPromotionAndDiscountResource implements MVCResourceCommand {
	private static Log log = LogFactoryUtil.getLog(ExportPromotionAndDiscountResource.class);
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
	        final String status = ParamUtil.getString(resourceRequest, ReportConstant.STATUS);
	        String discountType = ParamUtil.getString(resourceRequest, ReportConstant.DISCOUNT_TYPE);
	        String discountCode = ParamUtil.getString(resourceRequest, ReportConstant.DISCOUNT_CODE);
	        String fromDate = ParamUtil.getString(resourceRequest, ReportConstant.FROM_DATE);
	        String toDate = ParamUtil.getString(resourceRequest, ReportConstant.TO_DATE);

	        JSONObject result = ReportUtil.getPromotionDiscountData(
	                0,
	                Integer.MAX_VALUE,
	                orderByColumn,
	                ascending,
	                status,
	                defaultLocale,
	                fromDate,
	                toDate,
	                discountType,
	                discountCode);
	        
	        String[] headers = { "Promotion Start Date", "Promotion End Date", "Promotion Description", "Discount Type", "Discount Code", "Status"};
			String[] dataKeys = { "startDate", "endDate", "discountDescription", "discountType","discountCode", "statusDiscount"};
			String sbExport = ExportUtil.exportDataToStringBuilderCsv(headers,(List<JSONObject>) result.get(ReportConstant.EXPORT_DATA), dataKeys, ReportConstant.DELIMITER);
			
			
			String fileName = "Promotion and Discount Details Report ";
			fileName +="[";
			StringJoiner joiner = new StringJoiner(" ");
			if(!Validator.isBlank(fromDate)) joiner.add("From="+fromDate);
			if(!Validator.isBlank(toDate)) joiner.add("To="+toDate);
			if(!Validator.isBlank(discountType)) joiner.add("Discount Type="+discountType);
			if(!Validator.isBlank(discountCode)) joiner.add("Discount Code="+discountCode);
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
		log.info("Export promotion and discount resources - end");
		return false;
	}
	
	 private static String getOrderByColumn(final int iSortColumnIndex) {
	        String orderBy = "startDate";
	        switch (iSortColumnIndex) {
	            case 1:
	                orderBy = "endDate";
	                break;
	            case 2:
	                orderBy = "discountDescription";
	                break;
	            case 3:
	                orderBy = "discountType";
	                break;
	            case 4:
	                orderBy = "discountCode";
	                break;
	            case 5:
	                orderBy = "statusDiscount";
	                break;
	            default:
	                break;
	        }
	        return orderBy;
	    }
    
	@Reference
	private CommerceOrderStatusRegistry _commerceOrderStatusRegistry;
}
