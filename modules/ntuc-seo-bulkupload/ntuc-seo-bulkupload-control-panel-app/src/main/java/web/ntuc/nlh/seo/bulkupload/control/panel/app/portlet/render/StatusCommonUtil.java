package web.ntuc.nlh.seo.bulkupload.control.panel.app.portlet.render;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.NtucSeoBulkuploadControlPanelAppPortletKeys;

public class StatusCommonUtil {
	public static String getStatus(String rowData) throws PortalException {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray(rowData);
		String status = "Success";

		for (int j = 0; j < jsonArray.length(); j++) {
			JSONObject curjsonOBJ = jsonArray.getJSONObject(j);
			if ("Fail".equalsIgnoreCase(curjsonOBJ.getString(NtucSeoBulkuploadControlPanelAppPortletKeys.STATUS))) {
				status = curjsonOBJ.getString(NtucSeoBulkuploadControlPanelAppPortletKeys.STATUS);
			}
		}
		return status;
	}
}
