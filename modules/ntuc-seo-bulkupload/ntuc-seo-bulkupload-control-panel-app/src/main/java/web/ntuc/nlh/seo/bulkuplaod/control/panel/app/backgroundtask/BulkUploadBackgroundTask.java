package web.ntuc.nlh.seo.bulkuplaod.control.panel.app.backgroundtask;

import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.backgroundtask.BaseBackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.theme.ThemeDisplay;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

import web.ntuc.nlh.seo.bulkupload.control.panel.app.portlet.util.BulkUploadUtil;

/**
 * @author Dhivakar Sengottaiyan
 * The type Bulk upload background task.
 */
@Component(
        immediate = true,
        property = {
                "background.task.executor.class.name=web.ntuc.nlh.seo.bulkuplaod.control.panel.app.backgroundtask.BulkUploadBackgroundTask"
        },
        service = BackgroundTaskExecutor.class
)

public class BulkUploadBackgroundTask extends BaseBackgroundTaskExecutor {

    private final Log _log = LogFactoryUtil.getLog(BulkUploadBackgroundTask.class.getName());

    /**
     * Execute background task result.
     *
     * @param backgroundTask the background task
     * @return the background task result
     * @throws Exception the exception
     */
    @Override
    public BackgroundTaskResult execute(BackgroundTask backgroundTask) throws Exception {
        Map<String, String> statusMap = new HashMap<String, String>();
        try {
            _log.info("BulkUpload backgroundtask Starts : : " + System.currentTimeMillis());
            Map<String, Serializable> taskContextMap = backgroundTask.getTaskContextMap();
            _log.info("taskContextMap : "+taskContextMap);
            ThemeDisplay themeDisplay = BulkUploadUtil.getThemeDisplay(taskContextMap);
            BulkUploadUtil.updateMailerMessageMap(statusMap, taskContextMap);
            long fileEntryId = Long.valueOf(String.valueOf(taskContextMap.get("fileEntryId")));
            _log.info("fileEntryId : "+fileEntryId);
            FileEntry fileEntry = DLAppServiceUtil.getFileEntry(fileEntryId);
            InputStream inputStream = DLFileEntryLocalServiceUtil.getFileAsStream(fileEntry.getFileEntryId(), fileEntry.getVersion());
            _log.info("prepare json audit  starts: : " + System.currentTimeMillis());
            JSONArray uploadJsonArray = BulkUploadUtil.readExcelFile(inputStream, fileEntry.getExtension());
            if (uploadJsonArray.length() > 0) {
                _log.info("prepare json audit ends: : " + System.currentTimeMillis());
                statusMap.putAll(BulkUploadUtil.addValuesToAuditTable(themeDisplay, uploadJsonArray, statusMap.get("fileName")));
                _log.info("adding audit data : : " + System.currentTimeMillis());
                _log.info("Status Map : : " + statusMap);
                BulkUploadUtil.sendEmail(statusMap, 200, "bulkupload");
            }
        } catch (Exception e) {
            statusMap.put("message", BulkUploadUtil.getPrintStackTrace(e));
            BulkUploadUtil.sendEmail(statusMap, 400, "bulkupload");
            _log.error("Error while getting BackgroundTask " + e.getMessage());
        }
        _log.info("BulkUpload background task ends : : " + System.currentTimeMillis());
        return BackgroundTaskResult.SUCCESS;
    }

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

}
