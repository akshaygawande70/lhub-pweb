package web.ntuc.eshop.reports.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import web.ntuc.eshop.reports.constants.ReportConstant;

public class GenerateReportUtil {
    public static JSONObject createDataTableResponse(long totalCount, int sEcho, JSONArray data) {
        return JSONFactoryUtil.createJSONObject()
                .put(ReportConstant.I_TOTAL_RECORDS, totalCount)
                .put(ReportConstant.I_TOTAL_DISPLAY_RECORDS, totalCount)
                .put(ReportConstant.S_ECHO, sEcho)
                .put(ReportConstant.AA_DATA, data);
    }
}
