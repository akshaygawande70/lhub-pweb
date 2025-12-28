package web.ntuc.eshop.reports.resource;

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
import org.osgi.service.component.annotations.Component;
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

@Component(
        immediate = true,
        property = {
                "mvc.command.name=" + MVCCommandNames.SPECIAL_PRODUCT_RESOURCE,
                "javax.portlet.name=" + ReportPortletKeys.SPECIAL_PRODUCT_REPORT_PORTLET
        },
        service = MVCResourceCommand.class
)
public class SpecialProductReportResource extends BaseMVCResourceCommand {

    private static final Log _log = LogFactoryUtil.getLog(SpecialProductReportResource.class);

    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
    	String defaultLocale = LocaleUtil.getDefault().toString();
    	_log.info(defaultLocale);
    	HttpServletRequest httpRequest = PortalUtil
                .getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
        final int iDisplayStart = ParamUtil.getInteger(httpRequest, ReportConstant.I_DISPLAY_START);
        final int iDisplayLength = ParamUtil.getInteger(httpRequest, ReportConstant.I_DISPLAY_LENGTH);
        final int iSortColumnIndex = ParamUtil.getInteger(httpRequest, ReportConstant.I_SORT_COL_0);
        final boolean ascending = ParamUtil.getString(httpRequest, ReportConstant.S_SORT_DIR_0)
                .equalsIgnoreCase("asc");
        final String orderByColumn = iSortColumnIndex == 0 ? "productName" : "citrepStatus";

        JSONObject result = ReportUtil.getSpecialProductReportData(
                iDisplayStart,
                iDisplayLength,
                orderByColumn,
                ascending,
                defaultLocale);
        
        JSONObject response = GenerateReportUtil.createDataTableResponse(result.getLong(ReportConstant.TOTAL_COUNT),
                ParamUtil.getInteger(httpRequest, ReportConstant.S_ECHO),
                result.getJSONArray(ReportConstant.DATA));
        PrintWriter writer = resourceResponse.getWriter();
        writer.println(response);
        writer.close();
    }

    
}
