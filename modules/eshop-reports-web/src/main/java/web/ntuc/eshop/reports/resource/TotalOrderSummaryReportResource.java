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
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.CurrencyUtil;
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
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

@Component(
        immediate = true,
        property = {
                "mvc.command.name=" + MVCCommandNames.TOTAL_ORDER_SUMMARY_RESOURCE,
                "javax.portlet.name=" + ReportPortletKeys.TOTAL_ORDER_SUMMARY_REPORT_PORTLET
        },
        service = MVCResourceCommand.class
)
public class TotalOrderSummaryReportResource extends BaseMVCResourceCommand {

    private static final Log _log = LogFactoryUtil.getLog(TotalOrderSummaryReportResource.class);

    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
        HttpServletRequest httpRequest = PortalUtil
                .getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
        final int iDisplayStart = ParamUtil.getInteger(httpRequest, ReportConstant.I_DISPLAY_START);
        final int iDisplayLength = ParamUtil.getInteger(httpRequest, ReportConstant.I_DISPLAY_LENGTH);
        final int iSortColumnIndex = ParamUtil.getInteger(httpRequest, ReportConstant.I_SORT_COL_0);
        final boolean ascending = ParamUtil.getString(httpRequest, ReportConstant.S_SORT_DIR_0)
                .equalsIgnoreCase("asc");
        final String orderByColumn = getOrderByColumn(iSortColumnIndex);
        String fromDate = ParamUtil.getString(resourceRequest, ReportConstant.FROM_DATE);
        String toDate = ParamUtil.getString(resourceRequest, ReportConstant.TO_DATE);
        
        JSONObject result = ReportUtil.getTotalOrderSummaryData(
                iDisplayStart,
                iDisplayLength,
                orderByColumn,
                ascending,
                fromDate,
                toDate);
        JSONObject response = GenerateReportUtil.createDataTableResponse(result.getLong(ReportConstant.TOTAL_COUNT),
                ParamUtil.getInteger(httpRequest, ReportConstant.S_ECHO),
                result.getJSONArray(ReportConstant.DATA))
                    .put("totalOrder", result.getString("totalOrder"));
        PrintWriter writer = resourceResponse.getWriter();
        writer.println(response);
        writer.close();
    }

    

    private static String getOrderByColumn(final int iSortColumnIndex) {
        String orderBy = "orderDate";
        if (iSortColumnIndex == 3) {
            orderBy = "grossPrice";
        }
        return orderBy;
    }
}