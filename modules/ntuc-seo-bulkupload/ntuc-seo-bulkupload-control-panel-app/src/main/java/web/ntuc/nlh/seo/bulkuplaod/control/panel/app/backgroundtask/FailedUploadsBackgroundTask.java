package web.ntuc.nlh.seo.bulkuplaod.control.panel.app.backgroundtask;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.backgroundtask.BaseBackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload;
import svc.ntuc.nlh.seo.bulkupload.service.NtucBulkUploadLocalService;
import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.NtucSeoBulkuploadControlPanelAppPortletKeys;
import web.ntuc.nlh.seo.bulkupload.control.panel.app.portlet.util.BulkUploadUtil;

/**
 * @author Dhivakar Sengottaiyan
 * The type Failed uploads background task.
 */
@Component(
		immediate = true,
		property = {
			"background.task.executor.class.name=web.ntuc.nlh.seo.bulkuplaod.control.panel.app.backgroundtask.FailedUploadsBackgroundTask"
		},
		service = BackgroundTaskExecutor.class
	)

public class FailedUploadsBackgroundTask extends BaseBackgroundTaskExecutor{

	String fromAddress, toAddress, body = StringPool.BLANK;
	
	private final Log _log = LogFactoryUtil.getLog(FailedUploadsBackgroundTask.class.getName());

	/**
	 * Execute background task result.
	 *
	 * @param backgroundTask the background task
	 * @return the background task result
	 * @throws Exception the exception
	 */
	@Override
	public BackgroundTaskResult execute(BackgroundTask backgroundTask) throws Exception {
		Map<String, String> countMap = new HashMap<String, String>();
		try {
			_log.info("Failed Uploads Starts : : "+System.currentTimeMillis());
			Map<String, Serializable> taskContextMap = backgroundTask.getTaskContextMap();
			long ntucBulkUploadId = MapUtil.getLong(taskContextMap, "ntucBulkUploadId");
			ThemeDisplay themeDisplay = BulkUploadUtil.getThemeDisplay(taskContextMap);
			BulkUploadUtil.updateMailerMessageMap(countMap, taskContextMap);
			countMap.putAll(createSEOExcel(ntucBulkUploadId, themeDisplay.getScopeGroupId(), themeDisplay));
			_log.info("Failed Uploads completed : : "+System.currentTimeMillis());
			BulkUploadUtil.sendEmail(countMap, 200, "failedupload");
		} catch (Exception e) {
			countMap.put("message", BulkUploadUtil.getPrintStackTrace(e));
			BulkUploadUtil.sendEmail(countMap, 400, "failedupload");
			_log.error("Error while getting BackgroundTask "+e.getMessage());
		}
		_log.info("Failed Uploads ends : : "+System.currentTimeMillis());
		return BackgroundTaskResult.SUCCESS;
	}

	/**
	 * Create seo excel map.
	 *
	 * @param ntucBulkUploadId the ntuc bulk upload id
	 * @param groupId          the group id
	 * @param themeDisplay     the theme display
	 * @param serviceContext   the service context
	 * @return the map
	 * @throws Exception the exception
	 */
	@SuppressWarnings("resource")
	public Map<String, String> createSEOExcel(long ntucBulkUploadId, long groupId, ThemeDisplay themeDisplay) throws Exception {
		_log.info("create uploads excel starts : : "+System.currentTimeMillis());
		Workbook workbook = new SXSSFWorkbook();
		Map<String, String> countMap = new HashMap<String, String>();
		int successcount = 0;
		int failurecount = 0;
		Sheet sheet = workbook.createSheet("Uploaded SEO");
		sheet.setDefaultColumnWidth(30);
		CellStyle ottDefaultCellStyle = workbook.createCellStyle();
		ottDefaultCellStyle.setWrapText(true);

		CellStyle headerCellStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerCellStyle.setFont(headerFont);

		Row headRow = sheet.createRow(0);
		int headColumnNumber = 0;
		try {
			createCell(headRow, headColumnNumber++, NtucSeoBulkuploadControlPanelAppPortletKeys.URL, headerCellStyle);
			createCell(headRow, headColumnNumber++, NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_TITLE,
					headerCellStyle);
			createCell(headRow, headColumnNumber++, NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_DESC,
					headerCellStyle);
			createCell(headRow, headColumnNumber++, NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_KEYWORDS,
					headerCellStyle);
			createCell(headRow, headColumnNumber++, NtucSeoBulkuploadControlPanelAppPortletKeys.STATUS,
					headerCellStyle);
			createCell(headRow, headColumnNumber++, NtucSeoBulkuploadControlPanelAppPortletKeys.REASON,
					headerCellStyle);
		} catch (Exception e) {
			_log.error("excepion while create json -> " + e);
			throw new Exception(e.getMessage());
		}

		int var = 1;
		try {
			NtucBulkUpload ntucBulkUpload = _ntucBulkUploadLocalService.getNtucBulkUpload(ntucBulkUploadId);
			JSONArray jsonArray = JSONFactoryUtil.createJSONArray(ntucBulkUpload.getRowData());
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					JSONObject rowJsonObj = JSONFactoryUtil.createJSONObject(jsonArray.getString(i));
					int columnNumber = 0;
					Row row = sheet.createRow(var);
					createCell(row, columnNumber++, rowJsonObj.getString(NtucSeoBulkuploadControlPanelAppPortletKeys.URL),
							ottDefaultCellStyle);
					createCell(row, columnNumber++,
							rowJsonObj.getString(NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_TITLE),
							ottDefaultCellStyle);
					createCell(row, columnNumber++,
							rowJsonObj.getString(NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_DESC),
							ottDefaultCellStyle);
					createCell(row, columnNumber++,
							rowJsonObj.getString(NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_KEYWORDS),
							ottDefaultCellStyle);
					createCell(row, columnNumber++,
							rowJsonObj.getString(NtucSeoBulkuploadControlPanelAppPortletKeys.STATUS), ottDefaultCellStyle);
					createCell(row, columnNumber++,
							rowJsonObj.getString(NtucSeoBulkuploadControlPanelAppPortletKeys.REASON), ottDefaultCellStyle);
					if (rowJsonObj.getString(NtucSeoBulkuploadControlPanelAppPortletKeys.STATUS).equals("Success")) {
						successcount++;
					}
					else {
						failurecount++;
					}
					var++;
				}
				catch (Exception e) {
					_log.error("Exception while iterating the data for excel : "+e);
				}
			}
			countMap.put("successcount", String.valueOf(successcount));
			countMap.put("failurecount", String.valueOf(failurecount));
			_log.info("Counts : "+(successcount+failurecount));
			_log.info("uploading excel starts : : "+System.currentTimeMillis());
			if (Validator.isNotNull(workbook)) {
		        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        try {
					workbook.write(bos);
					byte[] bytes = bos.toByteArray();
					ServiceContext serviceContext = BulkUploadUtil.getServiceContext(themeDisplay);
					DLFolder folder = BulkUploadUtil.createFolder(themeDisplay, serviceContext, "Failed SEO Upload Excels");
					String fileName = "Failed_SEO_Uploads_" + new Timestamp(new Date().getTime()).getTime() + ".xlsx";
					DLAppServiceUtil.addFileEntry (groupId, folder.getFolderId(), fileName, "application/vnd.ms-excel", fileName, "", "", bytes, serviceContext);
					countMap.put("fileName", fileName);
				} catch (Exception e) {
					throw e;
		        } finally {
		            bos.close();
		            workbook.close();
		        }
		    }
			_log.info("uploading excel starts : : "+System.currentTimeMillis());
		} catch (Exception e) {
			_log.error("excepion while create json -> " + e);
			throw new Exception(e);
		}
		finally {
			try {
				workbook.close();
			} catch (IOException e) {
				_log.error("excepion while closing the workbook " + e);
			}
		}
		return countMap;
	}
	
	private static void createCell(Row row, int cellIndex, String cellValue, CellStyle cellStyle) {
		Cell cell = row.createCell(cellIndex);
		cell.setCellValue(cellValue);
		cell.setCellStyle(cellStyle);
	}
	
	@Reference
	private NtucBulkUploadLocalService _ntucBulkUploadLocalService;

	/**
	 * Gets background task display.
	 *
	 * @param backgroundTask the background task
	 * @return the background task display
	 */
	@Override
	public BackgroundTaskDisplay getBackgroundTaskDisplay(BackgroundTask backgroundTask) {
		return null;
	}

	/**
	 * Clone background task executor.
	 *
	 * @return the background task executor
	 */
	@SuppressWarnings("all")
	@Override
	public BackgroundTaskExecutor clone() {
		return this;
	}
	
	@Reference
	private DLFolderLocalService dlFolderLocalService;
	
	@Reference
	private CounterLocalService counterLocalService;

	@Reference
	private ResourcePermissionLocalService resourcePermissionLocalService;
	
}
