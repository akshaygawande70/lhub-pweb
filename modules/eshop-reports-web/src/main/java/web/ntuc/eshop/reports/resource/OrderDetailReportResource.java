package web.ntuc.eshop.reports.resource;

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.order.status.CommerceOrderStatusRegistry;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.CurrencyUtil;
import api.ntuc.common.util.DateUtil;
import web.ntuc.eshop.reports.constants.MVCCommandNames;
import web.ntuc.eshop.reports.constants.ReportConstant;
import web.ntuc.eshop.reports.constants.ReportPortletKeys;
import web.ntuc.eshop.reports.constants.ReportSql;
import web.ntuc.eshop.reports.util.GenerateReportUtil;
import web.ntuc.eshop.reports.util.ReportUtil;

@Component(
        immediate = true,
        property = {
                "mvc.command.name=" + MVCCommandNames.ORDER_DETAIL_RESOURCE,
                "javax.portlet.name=" + ReportPortletKeys.ORDER_DETAIL_REPORT_PORTLET
        },
        service = MVCResourceCommand.class
)
public class OrderDetailReportResource extends BaseMVCResourceCommand {

    private static final Log _log = LogFactoryUtil.getLog(OrderDetailReportResource.class);

    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
    	String defaultLocale = LocaleUtil.getDefault().toString();
    	HttpServletRequest httpRequest = PortalUtil
                .getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
        final int iDisplayStart = ParamUtil.getInteger(httpRequest, ReportConstant.I_DISPLAY_START);
        final int iDisplayLength = ParamUtil.getInteger(httpRequest, ReportConstant.I_DISPLAY_LENGTH);
        final boolean ascending = ParamUtil.getString(httpRequest, ReportConstant.S_SORT_DIR_0)
                .equalsIgnoreCase("asc");
        final String orderByColumn = getOrderByColumn(ParamUtil.getInteger(httpRequest, ReportConstant.I_SORT_COL_0));
        String fromDate = ParamUtil.getString(resourceRequest, ReportConstant.FROM_DATE);
        String toDate = ParamUtil.getString(resourceRequest, ReportConstant.TO_DATE);
        final long orderId = ParamUtil.getLong(resourceRequest, ReportConstant.ORDER_ID);
        final int status = ParamUtil.getInteger(resourceRequest, ReportConstant.STATUS);
        String productName = ParamUtil.getString(resourceRequest, ReportConstant.PRODUCT_NAME);
        
        JSONObject result = ReportUtil.getOrderDetailData(
                iDisplayStart,
                iDisplayLength,
                orderByColumn,
                ascending,
                fromDate,
                toDate,
                orderId,
                status,
                productName,
                defaultLocale);
//        _log.info(result);
        JSONObject response = GenerateReportUtil.createDataTableResponse(result.getLong(ReportConstant.TOTAL_COUNT),
                ParamUtil.getInteger(httpRequest, ReportConstant.S_ECHO),
                result.getJSONArray(ReportConstant.DATA));
        PrintWriter writer = resourceResponse.getWriter();
        writer.println(response);
        writer.close();
        _log.info(response);
    }

    

    private static String getOrderByColumn(final int iSortColumnIndex) {
        String orderBy = "orderCreatedDate";
        switch (iSortColumnIndex) {
            case 1:
                orderBy = "orderId";
                break;
            case 2:
                orderBy = "custEmail";
                break;
            case 3:
                orderBy = "custName";
                break;
            case 4:
                orderBy = "status";
                break;
            case 5:
                orderBy = "productName";
                break;
            case 6:
                orderBy = "sku";
                break;
            case 7:
                orderBy = "quantity";
                break;
            case 8:
                orderBy = "unitPrice";
                break;
            case 9:
                orderBy = "discount";
                break;
            case 10:
                orderBy = "netPrice";
                break;
            case 11:
                orderBy = "gst";
                break;
            case 12:
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
