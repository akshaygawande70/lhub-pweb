package web.ntuc.nlh.seo.bulkupload.control.panel.app.portlet;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManagerUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.ProcessAction;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import svc.ntuc.nlh.seo.bulkupload.service.NtucBulkUploadLocalService;
import web.ntuc.nlh.seo.bulkuplaod.control.panel.app.backgroundtask.BulkUploadBackgroundTask;
import web.ntuc.nlh.seo.bulkuplaod.control.panel.app.backgroundtask.FailedUploadsBackgroundTask;
import web.ntuc.nlh.seo.bulkuplaod.control.panel.app.backgroundtask.MissingUrlDownloadBackgroundTask;
import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.NtucSeoBulkuploadControlPanelAppPortletKeys;
import web.ntuc.nlh.seo.bulkupload.control.panel.app.portlet.util.BulkUploadUtil;

/**
 * @author sagar
 * The type Ntuc seo bulkupload control panel app portlet.
 */
@Component(immediate = true, property = {"com.liferay.portlet.add-default-resource=true",
        "com.liferay.portlet.display-category=category.hidden", "com.liferay.portlet.header-portlet-css=/css/main.css",
        "com.liferay.portlet.layout-cacheable=true", "com.liferay.portlet.private-request-attributes=false",
        "com.liferay.portlet.private-session-attributes=false", "com.liferay.portlet.render-weight=50",
        "com.liferay.portlet.use-default-template=true", "javax.portlet.display-name=Seo Bulk Upload Panel App",
        "javax.portlet.expiration-cache=0", "javax.portlet.init-param.template-path=/",
        "javax.portlet.init-param.view-template=/view.jsp",
        "javax.portlet.name=" + NtucSeoBulkuploadControlPanelAppPortletKeys.NTUCSEOBULKUPLOADCONTROLPANELAPP,
        "javax.portlet.resource-bundle=content.Language", "javax.portlet.security-role-ref=power-user,user",
        "com.liferay.portlet.action-url-redirect=true"

}, service = Portlet.class)

public class NtucSeoBulkuploadControlPanelAppPortlet extends MVCPortlet {

    @Override
    public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
            throws IOException, PortletException {
        _log.info("Compelte task " + System.currentTimeMillis());
        ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
        PortletPreferences portletPreferences = resourceRequest.getPreferences();
        String resourceId = resourceRequest.getResourceID();
        String myFile = null;
        Workbook workbook = null;
        if ("exportData".equalsIgnoreCase(resourceId)) {
            Map<String, String> countMap = new HashMap<String, String>();
            try {
                long ntucBulkUploadId = ParamUtil.getLong(resourceRequest, "bulkUploadId");
                ServiceContext serviceContext = ServiceContextFactory.getInstance(FailedUploadsBackgroundTask.class.getName(), resourceRequest);
                serviceContext.setScopeGroupId(themeDisplay.getScopeGroupId());
                Map<String, Serializable> taskContextMap = new HashMap<>();
                taskContextMap.put("ntucBulkUploadId", ntucBulkUploadId);
                BulkUploadUtil.updateContextMap(taskContextMap, themeDisplay, portletPreferences);
                com.liferay.portal.kernel.backgroundtask.BackgroundTask bgTask = BackgroundTaskManagerUtil.addBackgroundTask(themeDisplay.getUserId(), themeDisplay.getScopeGroupId(), "backgroud task contest", FailedUploadsBackgroundTask.class.getName(), taskContextMap, serviceContext);
            } catch (Exception e) {
                countMap.put("message", BulkUploadUtil.getPrintStackTrace(e));
                BulkUploadUtil.sendEmail(countMap, 400, "failedupload");
            }
        }
        if ("DownloadURL".equalsIgnoreCase(resourceId)) {
            workbook = new SXSSFWorkbook();
            createSEOExcelTemplate(workbook);
            myFile = NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_BulkUpload_Template;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            byte[] bytes = bos.toByteArray();
            PortletResponseUtil.sendFile(resourceRequest, resourceResponse, myFile, bytes, ContentTypes.APPLICATION_VND_MS_EXCEL);
        }
        if ("MissingURL".equalsIgnoreCase(resourceId)) {
            try {
                ServiceContext serviceContext = ServiceContextFactory.getInstance(MissingUrlDownloadBackgroundTask.class.getName(), resourceRequest);
                serviceContext.setScopeGroupId(themeDisplay.getScopeGroupId());
                Map<String, Serializable> taskContextMap = new HashMap<>();
                BulkUploadUtil.updateContextMap(taskContextMap, themeDisplay, portletPreferences);

                com.liferay.portal.kernel.backgroundtask.BackgroundTask bgTask = BackgroundTaskManagerUtil.addBackgroundTask(themeDisplay.getUserId(), themeDisplay.getScopeGroupId(), "backgroud task contest", MissingUrlDownloadBackgroundTask.class.getName(), taskContextMap, serviceContext);
            } catch (Exception e) {
                _log.error("Error while generating excel : " + e.getMessage());
            }
        }
        if ("validateFileName".equals(resourceId)) {
            JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
        	try(PrintWriter out = resourceResponse.getWriter()) {
        		String fileName = ParamUtil.getString(resourceRequest, "fileName");
        		ServiceContext serviceContext = ServiceContextFactory.getInstance(BulkUploadBackgroundTask.class.getName(), resourceRequest);
        		DLFolder folder = BulkUploadUtil.createFolder(themeDisplay, serviceContext, "SEO Bulk Upload Excels");
        		DLFileEntry dlFileEntry = getFileEntryByName(themeDisplay, folder.getFolderId(), fileName);
        		if(Validator.isNotNull(dlFileEntry)){
        			_log.info("dlFileEntry : "+dlFileEntry.getFileEntryId());
            		jsonObject.put("isFileNameExist", true);
            	}
        		out.print(jsonObject);
        	}
        	catch(Exception ex) {
        		 _log.error("Error while validating file name : " + ex.getMessage());
        	}
        	
        }
        _log.info("Compelte task " + System.currentTimeMillis());
        super.serveResource(resourceRequest, resourceResponse);
    }

    /**
     * Create seo excel template.
     *
     * @param workbook the workbook
     */
    public void createSEOExcelTemplate(Workbook workbook) {

        Sheet sheet = workbook.createSheet("SEO Template");
        sheet.setDefaultColumnWidth(30);
        CellStyle headerCellStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerCellStyle.setFont(headerFont);
        Row headRow = sheet.createRow(0);
        int headColumnNumber = 0;
        try {
            for (String i : NtucSeoBulkuploadControlPanelAppPortletKeys.HEADERARR) {
                createCell(headRow, headColumnNumber++, i, headerCellStyle);
            }
        } catch (Exception e) {
            _log.error("excepion while create json -> " + e);
        }
    }

    /**
     * Upload excel.
     *
     * @param actionRequest  the action request
     * @param actionResponse the action response
     */
    @ProcessAction(name = "uploadExcel")
    public void uploadExcel(ActionRequest actionRequest, ActionResponse actionResponse) {
        SessionMessages.add(actionRequest,
                PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
        try {
            UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(actionRequest);
            if (uploadRequest.getSize(NtucSeoBulkuploadControlPanelAppPortletKeys.FILENAME) == 0) {
                SessionErrors.add(actionRequest, NtucSeoBulkuploadControlPanelAppPortletKeys.EXCEL_IMPORT_FAILED);
            }
            File file = uploadRequest.getFile(NtucSeoBulkuploadControlPanelAppPortletKeys.FILENAME);
            if(!validateExcelfile(file)){
                SessionErrors.add(actionRequest, "exception_session_msg", "Cannot upload file. Invalid data in file/cannot read file data. Please download the template file, duly fill data and re-upload.");
                return;
            }
            String fileName = uploadRequest.getFileName(NtucSeoBulkuploadControlPanelAppPortletKeys.FILENAME);
            ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
            PortletPreferences portletPreferences = actionRequest.getPreferences();

            ServiceContext serviceContext = ServiceContextFactory.getInstance(BulkUploadBackgroundTask.class.getName(), actionRequest);
            serviceContext.setScopeGroupId(themeDisplay.getScopeGroupId());
            Map<String, Serializable> taskContextMap = new HashMap<>();
            BulkUploadUtil.updateContextMap(taskContextMap, themeDisplay, portletPreferences);
            taskContextMap.put("serviceContext", serviceContext);
            taskContextMap.put("fileName", fileName);
            DLFolder folder = BulkUploadUtil.createFolder(themeDisplay, serviceContext, "SEO Bulk Upload Excels");
            fileName = getFileName(themeDisplay, folder.getFolderId(), fileName);
            FileEntry fileEntry = DLAppServiceUtil.addFileEntry(themeDisplay.getScopeGroupId(), folder.getFolderId(), fileName,
                    "application/vnd.ms-excel", fileName, "", "", file, serviceContext);
            if(Validator.isNotNull(fileEntry)) {
                taskContextMap.put("fileEntryId", fileEntry.getFileEntryId());
                _log.info("filename : "+fileEntry.getFileName());
                _log.info("taskContextMap : "+taskContextMap);
                BackgroundTaskManagerUtil.addBackgroundTask(themeDisplay.getUserId(), themeDisplay.getScopeGroupId(), "backgroud task contest", BulkUploadBackgroundTask.class.getName(), taskContextMap, serviceContext);
            }
        } catch (Exception e) {
            _log.error("Error in upload excel : " + e);
            SessionErrors.add(actionRequest, "exception_session_msg", "Bulk upload failed, Please download the template file, duly fill data and re-upload.");
        }
    }

    /**
     * Validate excel file.
     *
     * @param file the file
     * @return the boolean
     */
    private boolean validateExcelfile(File file){
        try {
            String fileName = file.toString();
            int index = fileName.lastIndexOf('.');
            if (index > 0) {
                String extension = fileName.substring(index + 1);
                if(!extension.equalsIgnoreCase("xlsx") && !extension.equalsIgnoreCase("xls")){
                    return false;
                }
            }
        }
        catch(Exception ex){
            _log.error("Error while validating the file : "+ex);
        }
        return true;
    }

    /**
     * Get file name.
     *
     * @param fileName the file name
     * @return the file name
     */
    private String getFileName(ThemeDisplay themeDisplay, long folderId, String fileName){
    	DLFileEntry dlFileEntry = getFileEntryByName(themeDisplay, folderId, fileName);
        if(Validator.isNull(dlFileEntry)) {
        	return fileName;
        }
        int index = fileName.lastIndexOf('.');
        return fileName.substring(0, index ) + StringPool.UNDERLINE+ System.currentTimeMillis() + fileName.substring(index);
    }
    
    private DLFileEntry getFileEntryByName(ThemeDisplay themeDisplay, long folderId, String fileName) {
    	try {
    		_log.info("fileName : "+fileName);
			return DLFileEntryLocalServiceUtil.getFileEntry(themeDisplay.getScopeGroupId(), folderId, fileName);
    	}
    	catch(Exception ex) {
    		_log.error("Error while getting file entry by name : "+ex);
    	}
    	return null;
    }

    /**
     * Email configuration.
     *
     * @param actionRequest  the action request
     * @param actionResponse the action response
     */
    @ProcessAction(name = "emailConfiguration")
    public void emailConfiguration(ActionRequest actionRequest, ActionResponse actionResponse) {
        try {
            String fromAddress = ParamUtil.getString(actionRequest, "fromAddress");
            String toAddressSuccess = ParamUtil.getString(actionRequest, "toAddressSuccess");
            String toAddressFailure = ParamUtil.getString(actionRequest, "toAddressFailure");
            String successBody = ParamUtil.getString(actionRequest, "successBody");
            String failureBody = ParamUtil.getString(actionRequest, "failureBody");
            actionRequest.getPreferences().setValue("fromAddress", fromAddress);
            actionRequest.getPreferences().setValue("toAddressSuccess", toAddressSuccess);
            actionRequest.getPreferences().setValue("toAddressFailure", toAddressFailure);
            actionRequest.getPreferences().setValue("successBody", successBody);
            actionRequest.getPreferences().setValue("failureBody", failureBody);
            actionRequest.getPreferences().store();
        } catch (Exception e) {
            _log.error("Error in upload excel : " + e);
        }
    }

    /**
     * Gets node value.
     *
     * @param content the content
     * @param node    the node
     * @return the node value
     */
    public static String getNodeValue(String content, String node) {
        DocumentBuilder db;
        String nodeValue = "";
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(content));
            Document doc = db.parse(is);
            NodeList name = doc.getElementsByTagName(node);
            Element line = (Element) name.item(0);
            nodeValue = getCharacterDataFromElement(line);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            nodeValue = content;
        }

        return nodeValue;
    }

    /**
     * Gets character data from element.
     *
     * @param e the e
     * @return the character data from element
     */
    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof org.w3c.dom.CharacterData) {
            org.w3c.dom.CharacterData cd = (org.w3c.dom.CharacterData) child;
            return cd.getData();
        }
        return StringPool.BLANK;
    }

    private static void createCell(Row row, int cellIndex, String cellValue, CellStyle cellStyle) {
        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(cellValue);
        cell.setCellStyle(cellStyle);
    }

    @Reference
    private DLFolderLocalService dlFolderLocalService;

    @Reference
    private JournalArticleLocalService journalArticleLocalService;

    @Reference
    private DDMStructureLocalService ddmStructureLocalService;

    @Reference
    private NtucBulkUploadLocalService _ntucBulkUploadLocalService;
    private static final Log _log = LogFactoryUtil.getLog(NtucSeoBulkuploadControlPanelAppPortlet.class.getName());
}
