package web.ntuc.eshop.invoice.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

import web.ntuc.eshop.invoice.constants.InvoiceConstants;

public class GenerateInvoiceUtil {
	  public static JSONObject createDataTableResponse(long totalCount, int sEcho, JSONArray data) {
	        return JSONFactoryUtil.createJSONObject()
                .put(InvoiceConstants.I_TOTAL_RECORDS, totalCount)
                .put(InvoiceConstants.I_TOTAL_DISPLAY_RECORDS, totalCount)
                .put(InvoiceConstants.S_ECHO, sEcho)
                .put(InvoiceConstants.AA_DATA, data);
	    }
}
