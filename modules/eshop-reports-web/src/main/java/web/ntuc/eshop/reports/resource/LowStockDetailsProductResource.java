package web.ntuc.eshop.reports.resource;

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem;
import com.liferay.commerce.order.status.CommerceOrderStatusRegistry;
import com.liferay.commerce.service.CommerceOrderPaymentLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Order;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.reports.constants.MVCCommandNames;
import web.ntuc.eshop.reports.constants.ReportConstant;
import web.ntuc.eshop.reports.constants.ReportPortletKeys;
import web.ntuc.eshop.reports.util.GenerateReportUtil;
import web.ntuc.eshop.reports.util.ReportUtil;

@Component(
        immediate = true,
        property = {
                "mvc.command.name=" + MVCCommandNames.LOW_STOCK_DETAILS_PRODUCT_RESOURCE,
                "javax.portlet.name=" + ReportPortletKeys.LOW_STOCK_DETAILS_PRODUCT_REPORT_PORTLET
        },
        service = MVCResourceCommand.class
)

public class LowStockDetailsProductResource extends BaseMVCResourceCommand{
	private static final Log log = LogFactoryUtil.getLog(LowStockDetailsProductResource.class);
	
	@Override
	 protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
	            throws Exception {
		 log.info("=========== Product Inventory Details Report Resource - Start");
		
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
		        String lowStock = ParamUtil.getString(resourceRequest, ReportConstant.LOW_STOCK);
		        String fromDate = ParamUtil.getString(resourceRequest, ReportConstant.FROM_DATE);
		        
		        JSONObject result = ReportUtil.getLowStockDetailsProductData(
		                iDisplayStart,
		                iDisplayLength,
		                orderByColumn,
		                ascending,
		                category,
		                status,
		                productName,
		                sku,
		                lowStock,
		                defaultLocale,
		                fromDate);
		        log.info(result);
		        JSONObject response = GenerateReportUtil.createDataTableResponse(result.getLong(ReportConstant.TOTAL_COUNT),
		                ParamUtil.getInteger(httpRequest, ReportConstant.S_ECHO),
		                result.getJSONArray(ReportConstant.DATA));
		        PrintWriter writer = resourceResponse.getWriter();
		        writer.println(response);
		        writer.close();
		        log.info(response);
		    }

	    

	 private static String getOrderByColumn(final int iSortColumnIndex) {
	        String orderBy = "productCreationDate";
	        switch (iSortColumnIndex) {
	            case 1:
	                orderBy = "category";
	                break;
	            case 2:
	                orderBy = "productName";
	                break;
	            case 3:
	                orderBy = "sku";
	                break;
	            case 4:
	                orderBy = "status";
	                break;
	            case 5:
	                orderBy = "costPrice";
	                break;
	            case 6:
	                orderBy = "basePrice";
	                break;
	            case 7:
	                orderBy = "lowStock";
	                break;
	            default:
	                break;
	        }
	        return orderBy;
	    }
	 
	 @Reference
		private CommerceOrderStatusRegistry _commerceOrderStatusRegistry;
	}
