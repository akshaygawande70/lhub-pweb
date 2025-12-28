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
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
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
import web.ntuc.eshop.reports.portlet.OrderDetailReportPortlet;
import web.ntuc.eshop.reports.util.GenerateReportUtil;
import web.ntuc.eshop.reports.util.ReportUtil;

@Component(
        immediate = true,
        property = {
                "mvc.command.name=" + MVCCommandNames.PROMOTION_AND_DISCOUNT_DETAILS_RESOURCE,
                "javax.portlet.name=" + ReportPortletKeys.PROMOTION_AND_DISCOUNT_DETAILS_REPORT_PORTLET
        },
        service = MVCResourceCommand.class
)

public class PromotionDiscountDetailsReportResource extends BaseMVCResourceCommand {
	private static Log log = LogFactoryUtil.getLog(PromotionDiscountDetailsReportResource.class);

	@Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
		String defaultLocale = LocaleUtil.getDefault().toString();
        HttpServletRequest httpRequest = PortalUtil
                .getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
        final int iDisplayStart = ParamUtil.getInteger(httpRequest, ReportConstant.I_DISPLAY_START);
        final int iDisplayLength = ParamUtil.getInteger(httpRequest, ReportConstant.I_DISPLAY_LENGTH);
        final int iSortColumnIndex = ParamUtil.getInteger(httpRequest, ReportConstant.I_SORT_COL_0);
        final boolean ascending = ParamUtil.getString(httpRequest, ReportConstant.S_SORT_DIR_0)
                .equalsIgnoreCase("asc");
        final String orderByColumn = getOrderByColumn(iSortColumnIndex);
        final String status = ParamUtil.getString(resourceRequest, ReportConstant.STATUS);
        String discountType = ParamUtil.getString(resourceRequest, ReportConstant.DISCOUNT_TYPE);
        String discountCode = ParamUtil.getString(resourceRequest, ReportConstant.DISCOUNT_CODE);
        String fromDate = ParamUtil.getString(resourceRequest, ReportConstant.FROM_DATE);
        String toDate = ParamUtil.getString(resourceRequest, ReportConstant.TO_DATE);
        
        JSONObject result = ReportUtil.getPromotionDiscountData(
                iDisplayStart,
                iDisplayLength,
                orderByColumn,
                ascending,
                status,
                defaultLocale,
                fromDate,
                toDate,
                discountType,
                discountCode);
        log.info(result);
        JSONObject response = GenerateReportUtil.createDataTableResponse(result.getLong(ReportConstant.TOTAL_COUNT),
                ParamUtil.getInteger(httpRequest, ReportConstant.S_ECHO),
                result.getJSONArray(ReportConstant.DATA));
        PrintWriter writer = resourceResponse.getWriter();
        writer.println(response);
        writer.close();
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