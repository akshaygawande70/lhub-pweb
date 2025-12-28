package web.ntuc.eshop.reports.resource;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.DateUtil;
import web.ntuc.eshop.reports.constants.MVCCommandNames;
import web.ntuc.eshop.reports.constants.ReportConstant;
import web.ntuc.eshop.reports.constants.ReportPortletKeys;
import web.ntuc.eshop.reports.constants.ReportSql;
import web.ntuc.eshop.reports.util.GenerateReportUtil;
import web.ntuc.eshop.reports.util.ReportUtil;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

@Component(
        immediate = true,
        property = {
                "mvc.command.name=" + MVCCommandNames.PRODUCT_BEST_SELLER_RESOURCE,
                "javax.portlet.name=" + ReportPortletKeys.PRODUCT_BEST_SELLER_REPORT_PORTLET
        },
        service = MVCResourceCommand.class
)
public class ProductBestSellerReportResource extends BaseMVCResourceCommand {

    private static final Log _log = LogFactoryUtil.getLog(ProductBestSellerReportResource.class);
//    private SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy");
    
    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
            throws Exception {
    	String defaultLocale = LocaleUtil.getDefault().toString();
        HttpServletRequest httpRequest = PortalUtil
                .getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
        final int iDisplayStart = ParamUtil.getInteger(httpRequest, ReportConstant.I_DISPLAY_START);
        final int iDisplayLength = ParamUtil.getInteger(httpRequest, ReportConstant.I_DISPLAY_LENGTH);
        final int iSortColumnIndex = ParamUtil.getInteger(httpRequest, ReportConstant.I_SORT_COL_0);
        final boolean ascending = ParamUtil.getString(httpRequest, ReportConstant.S_SORT_DIR_0)
                .equalsIgnoreCase("asc");
        final String orderByColumn = getOrderByColumn(iSortColumnIndex);
        String searchKeyword = ParamUtil.getString(httpRequest, ReportConstant.S_SEARCH);
        String fromDate = ParamUtil.getString(resourceRequest, ReportConstant.FROM_DATE);
        String toDate = ParamUtil.getString(resourceRequest, ReportConstant.TO_DATE);

        _log.info("getting product best seller with keyword:"
                + searchKeyword + " from:" + fromDate + " to:" + toDate);

        JSONObject result = ReportUtil.getProductBestSellerData(
                iDisplayStart,
                iDisplayLength,
                orderByColumn,
                ascending,
                searchKeyword,
                fromDate,
                toDate,
                defaultLocale);
//        _log.info(result);
        JSONObject response = GenerateReportUtil.createDataTableResponse(result.getLong(ReportConstant.TOTAL_COUNT),
                ParamUtil.getInteger(httpRequest, ReportConstant.S_ECHO),
                result.getJSONArray(ReportConstant.DATA));
        PrintWriter writer = resourceResponse.getWriter();
        writer.println(response);
        writer.close();
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
