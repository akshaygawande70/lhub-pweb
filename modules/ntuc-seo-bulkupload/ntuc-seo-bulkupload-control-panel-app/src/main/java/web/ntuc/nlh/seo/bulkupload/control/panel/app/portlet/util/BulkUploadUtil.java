package web.ntuc.nlh.seo.bulkupload.control.panel.app.portlet.util;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLFolderLocalServiceUtil;
import com.liferay.friendly.url.model.FriendlyURLEntryLocalization;
import com.liferay.friendly.url.service.FriendlyURLEntryLocalServiceUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassName;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ClassNameLocalServiceUtil;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.mail.internet.InternetAddress;
import javax.portlet.PortletPreferences;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import svc.ntuc.nlh.seo.bulkupload.service.NtucBulkUploadLocalServiceUtil;
import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.NtucSeoBulkuploadControlPanelAppPortletKeys;

/**
 * The type Bulk upload util.
 *
 * @author Sagar The type Bulk upload util.
 */
public class BulkUploadUtil {

    /**
     * Is blank row boolean.
     *
     * @param row the row
     * @return the boolean
     */
    public static boolean isBlankRow(Row row) {
        try {
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if ((0 == cell.getColumnIndex()) && Validator.isBlank(cell.getStringCellValue().trim())) {
                    return true;
                }
            }
        } catch (Exception ex) {
            if (_log.isErrorEnabled()) {
                _log.error("Error while checking row cells : " + ex);
            }
        }
        return false;
    }

    private static Workbook getWorkbook(InputStream inputStream, String fileExtension) throws IOException {
        Workbook workbook = null;
        if (fileExtension.endsWith(NtucSeoBulkuploadControlPanelAppPortletKeys.XLSX)) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (fileExtension.endsWith(NtucSeoBulkuploadControlPanelAppPortletKeys.XLS)) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    }

    /**
     * Add values to audit table map.
     *
     * @param themeDisplay the theme display
     * @param jsonArray    the json array
     * @param fileName     the file name
     * @return the map
     * @throws Exception the exception
     */
    public static Map<String, String> addValuesToAuditTable(ThemeDisplay themeDisplay, JSONArray jsonArray, String fileName) throws Exception {
        try {
            _log.info("Adding audit starts: " + System.currentTimeMillis());
            String status = null;
            String reason;
            JSONArray finalJsonArray = JSONFactoryUtil.createJSONArray();
            _log.debug("jsonArrayLength : : " + jsonArray.length());
            int successcount = 0;
            int failurecount = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                reason = "";
                JSONObject finalJsonObj = JSONFactoryUtil.createJSONObject();
                JSONObject curjsonOBJ = jsonArray.getJSONObject(i);
                String url = curjsonOBJ.getString(NtucSeoBulkuploadControlPanelAppPortletKeys.URL);
                String tmpURL = url.replace(themeDisplay.getPortalURL(), "");
                String title = curjsonOBJ.getString(NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_TITLE);
                String dec = curjsonOBJ.getString(NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_DESC);
                String keywords = curjsonOBJ.getString(NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_KEYWORDS);
                Layout layout = null;
                if (tmpURL.trim().isEmpty()) {
                    continue;
                }

                try {
                    layout = LayoutLocalServiceUtil.getLayoutByFriendlyURL(themeDisplay.getScopeGroupId(), false,
                            tmpURL);
                    if (title.trim().length() > 0) {
                        layout.setTitle(title);
                    }
                    if (dec.trim().length() > 0) {
                        layout.setDescription(dec);
                    }

                    if (keywords.trim().length() > 0) {
                        layout.setKeywords(keywords);
                    }

                    if (reason.isEmpty()) {
                        layout.setModifiedDate(new Date());
                        layout = LayoutLocalServiceUtil.updateLayout(layout);
                        status = "Success";
                        successcount++;
                    } else {
                        status = "Fail";
                        failurecount++;
                    }
                } catch (PortalException e1) {
                    try {
                        ClassName clsName = ClassNameLocalServiceUtil
                                .getClassName(NtucSeoBulkuploadControlPanelAppPortletKeys.CLASS_ARTICAL_NAME);
                        _log.debug("tmpURL : : " + tmpURL);
                        FriendlyURLEntryLocalization frURLData = FriendlyURLEntryLocalServiceUtil
                                .fetchFriendlyURLEntryLocalization(themeDisplay.getScopeGroupId(), clsName.getClassNameId(),
                                        tmpURL.substring(3));
                        if (frURLData != null) {
                            _log.debug("frURLData : : " + frURLData.getUrlTitle());
                            JournalArticle jurArt = JournalArticleLocalServiceUtil.getLatestArticle(frURLData.getClassPK(), WorkflowConstants.STATUS_APPROVED);
                            _log.debug("jurArt : : " + jurArt.getArticleId());
                            if (jurArt != null) {
                                String xmlRecords = jurArt.getContent();
                                String[] arrXML = xmlValueUpdate(xmlRecords, title, dec, keywords);
                                String content = arrXML[0];
                                reason = arrXML[1];
                                String isUpdate = arrXML[2];
                                if (isUpdate.equalsIgnoreCase("true")) {
                                    jurArt = JournalArticleLocalServiceUtil.updateContent(jurArt.getGroupId(),
                                            jurArt.getArticleId(), jurArt.getVersion(), content);
                                    status = "Success";
                                    successcount++;
                                } else {
                                    status = "Fail";
                                    failurecount++;
                                }
                            }
                        } else {
                            status = "Fail";
                            reason = "Url Not Found";
                            failurecount++;
                            _log.error("error while getting layout ->   " + e1);
                        }
                    } catch (Exception e) {
                        _log.error("Exception while getting journal details   " + e);
                    }
                }
                finalJsonObj.put(NtucSeoBulkuploadControlPanelAppPortletKeys.URL, url);
                finalJsonObj.put(NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_TITLE, title);
                finalJsonObj.put(NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_DESC, dec);
                finalJsonObj.put(NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_KEYWORDS, keywords);
                finalJsonObj.put(NtucSeoBulkuploadControlPanelAppPortletKeys.STATUS, status);
                finalJsonObj.put(NtucSeoBulkuploadControlPanelAppPortletKeys.REASON, reason);
                finalJsonArray.put(finalJsonObj);
            }
            Map<String, String> uploadCountMap = new HashMap<String, String>();
            uploadCountMap.put("successcount", String.valueOf(successcount));
            uploadCountMap.put("failurecount", String.valueOf(failurecount));
            _log.info("Counts : " + (successcount + failurecount));
            User user = UserLocalServiceUtil.getUser(themeDisplay.getUserId());
            NtucBulkUploadLocalServiceUtil.addNtucBulkUpload(
                    themeDisplay.getScopeGroupId(), themeDisplay.getCompanyId(), user.getUserId(), user.getFullName(),
                    fileName, finalJsonArray.toString());
            _log.info("Adding audit ends: " + System.currentTimeMillis());
            return uploadCountMap;
        } catch (Exception e) {
            _log.error("Error while adding values to custom tables : " + e);
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Xml value update string [ ].
     *
     * @param xmlRecords the xml records
     * @param title      the title
     * @param dec        the dec
     * @param keywords   the keywords
     * @return the string [ ]
     * @throws SAXException                 the sax exception
     * @throws IOException                  the io exception
     * @throws ParserConfigurationException the parser configuration exception
     * @throws TransformerException         the transformer exception
     */
    public static String[] xmlValueUpdate(String xmlRecords, String title, String dec, String keywords)
            throws SAXException, IOException, ParserConfigurationException, TransformerException {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlRecords));
        Document doc = db.parse(is);
        String[] arrXML = new String[3];
        String isUpdate = "true";
        String reason = "";
        NodeList nodes = doc.getElementsByTagName(NtucSeoBulkuploadControlPanelAppPortletKeys.DynamicElement);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            NodeList name = element.getElementsByTagName(NtucSeoBulkuploadControlPanelAppPortletKeys.DynamicContent);
            Element line = null;
            for (int j = 0; j < name.getLength(); j++) {
                line = (Element) name.item(j);
                if (line.getAttribute("language-id").equals("en_GB")) {
                    break;
                }
                line = null;
            }
            _log.debug("line : : " + line);
            if (line == null) {
                line = doc.createElement(NtucSeoBulkuploadControlPanelAppPortletKeys.DynamicContent);
                line.setAttribute("language-id", "en_GB");
                element.appendChild(line);
            }

            if (title.trim().length() > 0) {
                if (nodes.item(i).getAttributes().getNamedItem("name").getNodeValue()
                        .equals(NtucSeoBulkuploadControlPanelAppPortletKeys.SEOMetaTitle)) {
                    _log.debug("SEO TITLE : : " + title);
                    getCharacterDataFromElement(doc, line, title);
                }
            }
            if (dec.trim().length() > 0) {
                if (nodes.item(i).getAttributes().getNamedItem("name").getNodeValue()
                        .equals(NtucSeoBulkuploadControlPanelAppPortletKeys.SEOMetaDesc)) {
                    getCharacterDataFromElement(doc, line, dec);
                    _log.debug("SEO DESC : : " + title);
                }
            }
            if (keywords.trim().length() > 0) {
                if (nodes.item(i).getAttributes().getNamedItem("name").getNodeValue()
                        .equals(NtucSeoBulkuploadControlPanelAppPortletKeys.SEOMetaKeyword)) {
                    getCharacterDataFromElement(doc, line, keywords);
                    _log.debug("SEO KEYWORD : : " + title);
                }
            }
        }
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource domSource = new DOMSource(doc);
        transformer.transform(domSource, result);
        xmlRecords = writer.toString();
        arrXML[0] = xmlRecords;
        arrXML[1] = reason;
        arrXML[2] = isUpdate;
        return arrXML;
    }

    /**
     * Gets character data from element.
     *
     * @param doc         the doc
     * @param e           the e
     * @param textcontent the textcontent
     * @return the character data from element
     */
    public static String getCharacterDataFromElement(Document doc, Element e, String textcontent) {
        Node child = e.getFirstChild();
        if (Validator.isNull(child)) {
            Text node = doc.createCDATASection(textcontent);
            e.appendChild(node);
            _log.debug("node value : : " + node.getData());
            return node.getData();
        }
        if (child instanceof org.w3c.dom.CharacterData) {
            org.w3c.dom.CharacterData cd = (org.w3c.dom.CharacterData) child;
            cd.setNodeValue(textcontent);
            _log.debug("node value : : " + cd.getData());
            return cd.getData();
        }
        return StringPool.BLANK;
    }


    /**
     * Read excel file json array.
     *
     * @param file the file
     * @return the json array
     * @throws Exception the exception
     */
    public static JSONArray readExcelFile(InputStream inputStream, String fileExtension)
            throws Exception {
        _log.info("Read Excel starts: " + System.currentTimeMillis());
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
        JSONObject jsonObject = null;
        try {
            Workbook wb = getWorkbook(inputStream, fileExtension);

            if (Validator.isNotNull(wb)) {
                Sheet firstSheet = wb.getSheetAt(0); // Getting the sheet at 0th position
                Iterator<Row> iterator = firstSheet.iterator();
                int lineNo = 2;
                while (iterator.hasNext()) { // iterating over each row
                    Row row = iterator.next();
                    jsonObject = JSONFactoryUtil.createJSONObject();
                    // skipping 1st row
                    if (row.getRowNum() == 0) {
                        if (Validator.isNotNull(row)) {
                            if (iterator.hasNext()) {
                                continue;
                            } else {
                                throw new Exception("Empty excel uploaded");
                            }
                        } else {
                            throw new Exception("Wrong excel uploaded");
                        }
                    }

                    if (isBlankRow(row)) {
                        continue;
                    }
                    for (int i = 0; i < row.getLastCellNum(); i++) {

                        try {
                            Cell cell = row.getCell(i);
                            //cell.setCellType(CellType.STRING);

                            if (0 == cell.getColumnIndex()) {
                                jsonObject.put(NtucSeoBulkuploadControlPanelAppPortletKeys.URL,
                                        HtmlUtil.escape(cell.getStringCellValue().trim()));
                            }
                            if (1 == cell.getColumnIndex()) {
                                jsonObject.put(NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_TITLE,
                                        HtmlUtil.escape(cell.getStringCellValue().trim()));
                            }
                            if (2 == cell.getColumnIndex()) {
                                jsonObject.put(NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_DESC,
                                        HtmlUtil.escape(cell.getStringCellValue().trim()));
                            }
                            if (3 == cell.getColumnIndex()) {
                                jsonObject.put(NtucSeoBulkuploadControlPanelAppPortletKeys.SEO_META_KEYWORDS,
                                        HtmlUtil.escape(cell.getStringCellValue().trim()));
                            }
                        } catch (Exception e) {
                            _log.error("Exception while iterating the cell data : " + e);
                        }
                    }
                    jsonArray.put(jsonObject);
                    lineNo++;
                }
            }
        } catch (Exception e) {
            _log.error("Error while creating json array");
            throw new Exception(e.getMessage());
        }
        _log.info("Read Excel Ends : " + System.currentTimeMillis());
        return jsonArray;
    }

    /**
     * Get preference map map.
     *
     * @param portletPreferences the portlet preferences
     * @return the map
     */
    public static Map<String, String> getPreferenceMap(PortletPreferences portletPreferences) {
        Map<String, String> preferenceMap = new HashMap<String, String>();
        preferenceMap.put("fromAddress", portletPreferences.getValue("fromAddress", StringPool.BLANK));
        preferenceMap.put("toAddress", portletPreferences.getValue("toAddress", StringPool.BLANK));
        preferenceMap.put("descriptionEditor", portletPreferences.getValue("descriptionEditor", StringPool.BLANK));
        return preferenceMap;
    }

    /**
     * Gets print stack trace.
     *
     * @param e the e
     * @return the print stack trace
     */
    public static String getPrintStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Send email.
     *
     * @param messageMap the message map
     * @param errorCode  the error code
     * @param mailerType the mailer type
     */
    public static void sendEmail(Map<String, String> messageMap, int errorCode, String mailerType) {

        SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd G  HH:mm:ss z");
        Date date = new Date();
        sd.setTimeZone(TimeZone.getTimeZone("IST"));

        try {
            InternetAddress fromAddressmail = new InternetAddress(MapUtil.getString(messageMap, "fromAddress"));
            InternetAddress[] toAddressmail = getToAddress(MapUtil.getString(messageMap, "toAddressSuccess"));
            String body = MapUtil.getString(messageMap, "successBody");
            if(errorCode == 400){
                toAddressmail = getToAddress(MapUtil.getString(messageMap, "toAddressFailure"));
                body = MapUtil.getString(messageMap, "failureBody");
            }
            MailMessage mailMessage = new MailMessage();
            mailMessage.setFrom(fromAddressmail);
            mailMessage.setTo( toAddressmail);
            mailMessage.setSubject(getMailSubmit(mailerType));
            body = getBody(body, messageMap, errorCode, mailerType);
            mailMessage.setBody(body);
            mailMessage.setHTMLFormat(Boolean.TRUE);

            _log.info("Attempting to send mail to " + toAddressmail[0].getAddress() + " from address " + fromAddressmail.getAddress() + " body is " + body + " at " + sd.format(date));
            MailServiceUtil.sendEmail(mailMessage);

        } catch (Exception e) {
            _log.error("Error in sending mail..." + e);
        }

    }

    private static InternetAddress[] getToAddress(String toAddress){
        List<InternetAddress> internetAddresses = null;
        try{
            String[] toAddresses = toAddress.split(";");
            for(String toAddr : toAddresses){
                if(Validator.isEmailAddress(toAddr)){
                    if (internetAddresses == null){
                        internetAddresses = new ArrayList<InternetAddress>();
                    }
                    internetAddresses.add(new InternetAddress(toAddr));
                }
            }
        }
        catch(Exception ex){
            _log.error("Error while spliting toaddress : "+ex);
        }
        InternetAddress[] internetAddressArr = new InternetAddress[internetAddresses.size()];
        return internetAddresses.toArray(internetAddressArr);
    }

    /**
     * Gets body.
     *
     * @param body       the body
     * @param messageMap the message map
     * @param errorCode  the error code
     * @param mailerType the mailer type
     * @return the body
     */
    public static String getBody(String body, Map<String, String> messageMap, int errorCode, String mailerType) {
        if (errorCode == 400) {
            return body = getMailBody(mailerType + "failure", messageMap, body);
        }
        return getMailBody(mailerType + "success", messageMap, body);
    }

    private static String getMailBody(String mailerType, Map<String, String> messageMap, String body) {
        if (mailerType.equals("missingurlsuccess")) {
            String successMessage = "<table style='border: 1px solid black;'><tr><th>Content</th><th>Count</th></tr><tr><td>Page</td><td>" + messageMap.get("layout") + "</td></tr><tr><td>Webcontent</td><td>" + messageMap.get("journal") + "</td></tr></table>";
            return body.replace("[$STATUS$]", "<p>Missing url excel file download : <span style='color:green'>Success</span></p><p>File Name : " + messageMap.get("fileName") + "</p>")
                    .replace("[$MESSAGE$]", "<p>" + successMessage + "<p><br/>");
        } else if (mailerType.equals("missingurlfailure")) {
            return body.replace("[$STATUS$]", "<p>Missing url excel file download : <span style='color:red'>Failure</span></p>")
                    .replace("[$MESSAGE$]", "<p>" + MapUtil.getString(messageMap, "message") + "<p><br/>");
        } else if (mailerType.equals("bulkuploadsuccess")) {
            String successMessage = "<table style='border: 1px solid black;'><tr><th>Content</th><th>Count</th></tr><tr><td>Success Count</td><td>" + messageMap.get("successcount") + "</td></tr><tr><td>Fail Count</td><td>" + messageMap.get("failurecount") + "</td></tr></table>";
            return body.replace("[$STATUS$]", "<p>Bulk SEO excel upload : <span style='color:green'>Success</span></p><p>File Name : " + messageMap.get("fileName") + "</p>")
                    .replace("[$MESSAGE$]", "<p>" + successMessage + "<p><br/>");
        } else if (mailerType.equals("bulkuploadfailure")) {
            return body.replace("[$STATUS$]", "<p>Bulk SEO excel upload : <span style='color:red'>Failure</span></p><p>File Name : " + MapUtil.getString(messageMap, "fileName") + "</p>")
                    .replace("[$MESSAGE$]", "<p>" + MapUtil.getString(messageMap, "message") + "<p><br/>");
        } else if (mailerType.equals("faileduploadsuccess")) {
            String successMessage = "<table style='border: 1px solid black;'><tr><th>Content</th><th>Count</th></tr><tr><td>Success Count</td><td>" + messageMap.get("successcount") + "</td></tr><tr><td>Fail Count</td><td>" + messageMap.get("failurecount") + "</td></tr></table>";
            return body.replace("[$STATUS$]", "<p>Failed uploads excel file download : <span style='color:green'>Success</span></p><p>File Name : " + messageMap.get("fileName") + "</p>")
                    .replace("[$MESSAGE$]", "<p>" + successMessage + "<p><br/>");
        } else {
            return body.replace("[$STATUS$]", "<p>Failed uploads excel file download : <span style='color:red'>Failure</span></p><br/>")
                    .replace("[$MESSAGE$]", "<p>" + MapUtil.getString(messageMap, "message") + "<p><br/>");
        }
    }

    private static String getMailSubmit(String mailerType) {
        if (mailerType.equals("missingurl")) {
            return "Missing url excel file download status";
        } else if (mailerType.equals("bulkupload")) {
            return "Bulk SEO excel upload status";
        } else {
            return "Failed upload excel file download status";
        }
    }

    /**
     * Gets theme display.
     *
     * @param taskContextMap the task context map
     * @return the theme display
     * @throws PortalException the portal exception
     */
    public static ThemeDisplay getThemeDisplay(Map<String, Serializable> taskContextMap)
            throws PortalException {
        ThemeDisplay themeDisplay = new ThemeDisplay();
        themeDisplay.setCompany(CompanyLocalServiceUtil.getCompany(MapUtil.getLong(taskContextMap, "companyId")));
        themeDisplay.setUser(UserLocalServiceUtil.getUser(MapUtil.getLong(taskContextMap, "userId")));
        themeDisplay.setScopeGroupId(MapUtil.getLong(taskContextMap, "groupId"));
        themeDisplay.setPortalURL(MapUtil.getString(taskContextMap, "portalURL"));
        return themeDisplay;
    }

    /**
     * Update mailer message map.
     *
     * @param messageMap     the message map
     * @param taskContextMap the task context map
     */
    public static void updateMailerMessageMap(Map<String, String> messageMap, Map<String, Serializable> taskContextMap){
        messageMap.put("fromAddress", MapUtil.getString(taskContextMap, "fromAddress"));
        messageMap.put("toAddressSuccess", MapUtil.getString(taskContextMap, "toAddressSuccess"));
        messageMap.put("toAddressFailure", MapUtil.getString(taskContextMap, "toAddressFailure"));
        messageMap.put("successBody", MapUtil.getString(taskContextMap, "successBody"));
        messageMap.put("failureBody", MapUtil.getString(taskContextMap, "failureBody"));
        messageMap.put("fileName", MapUtil.getString(taskContextMap, "fileName"));
    }

    /**
     * Update context map.
     *
     * @param taskContextMap     the task context map
     * @param themeDisplay       the theme display
     * @param portletPreferences the portlet preferences
     */
    public static void updateContextMap(Map<String, Serializable> taskContextMap, ThemeDisplay themeDisplay, PortletPreferences portletPreferences){
        String fromAddress = portletPreferences.getValue("fromAddress", StringPool.BLANK);
        String toAddressSuccess = portletPreferences.getValue("toAddressSuccess", StringPool.BLANK);
        String toAddressFailure = portletPreferences.getValue("toAddressFailure", StringPool.BLANK);
        String successBody = portletPreferences.getValue("successBody", StringPool.BLANK);
        String failureBody = portletPreferences.getValue("failureBody", StringPool.BLANK);
        taskContextMap.put("groupId", themeDisplay.getScopeGroupId());
        taskContextMap.put("portalURL", themeDisplay.getPortalURL());
        taskContextMap.put("companyId", themeDisplay.getCompanyId());
        taskContextMap.put("userId", themeDisplay.getUserId());
        taskContextMap.put("fromAddress", fromAddress);
        taskContextMap.put("toAddressSuccess", toAddressSuccess);
        taskContextMap.put("toAddressFailure", toAddressFailure);
        taskContextMap.put("successBody", successBody);
        taskContextMap.put("failureBody", failureBody);
    }

    /**
     * Is admin role boolean.
     *
     * @return the boolean
     */
    public static boolean isAdminRole(){
        PermissionChecker permissionChecker = PermissionThreadLocal.getPermissionChecker();
        return permissionChecker.isOmniadmin();
    }

    /**
     * Gets service context.
     *
     * @param themeDisplay the theme display
     * @return the service context
     */
    public static ServiceContext getServiceContext(ThemeDisplay themeDisplay) {
    	ServiceContext serviceContext = new ServiceContext();
		serviceContext.setCreateDate(new Date());
		serviceContext.setModifiedDate(new Date());
		serviceContext.setScopeGroupId(themeDisplay.getScopeGroupId());
		serviceContext.setCompanyId(themeDisplay.getCompanyId());
		serviceContext.setUserId(themeDisplay.getUser().getUserId());
		return serviceContext;
    }

    /**
     * Create folder dl folder.
     *
     * @param themeDisplay   the theme display
     * @param serviceContext the service context
     * @param folderName     the folder name
     * @return the dl folder
     */
    public static DLFolder createFolder(ThemeDisplay themeDisplay, ServiceContext serviceContext, String folderName) {
        try {
            return DLFolderLocalServiceUtil.getFolder((themeDisplay.getScopeGroupId()), 0, folderName);
        }
        catch(PortalException exception) {
            try {
                return DLFolderLocalServiceUtil.addFolder(themeDisplay.getUserId(), themeDisplay.getScopeGroupId(), themeDisplay.getScopeGroupId(),
                        false, 0l, folderName, folderName, false, serviceContext);
            } catch (PortalException e) {
                _log.error("Exception while getting creating folder : " + e);
            }
        }
        return null;
    }

    private static final Log _log = LogFactoryUtil.getLog(BulkUploadUtil.class.getName());
}
