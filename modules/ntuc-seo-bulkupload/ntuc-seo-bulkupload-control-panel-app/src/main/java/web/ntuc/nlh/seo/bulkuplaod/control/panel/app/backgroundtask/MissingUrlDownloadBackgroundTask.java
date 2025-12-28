package web.ntuc.nlh.seo.bulkuplaod.control.panel.app.backgroundtask;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.persistence.DDMStructurePersistence;
import com.liferay.dynamic.data.mapping.service.persistence.DDMStructureUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.backgroundtask.BaseBackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.NtucSeoBulkuploadControlPanelAppPortletKeys;
import web.ntuc.nlh.seo.bulkupload.control.panel.app.portlet.util.BulkUploadUtil;

/**
 * @author Dhivakar Sengottaiyan
 * The type Missing url download background task.
 */
@Component(
		immediate = true,
		property = {
			"background.task.executor.class.name=web.ntuc.nlh.seo.bulkuplaod.control.panel.app.backgroundtask.MissingUrlDownloadBackgroundTask"
		},
		service = BackgroundTaskExecutor.class
	)

public class MissingUrlDownloadBackgroundTask extends BaseBackgroundTaskExecutor{

	private static final Log _log = LogFactoryUtil.getLog(MissingUrlDownloadBackgroundTask.class.getName());

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
			_log.info("Missing Url Download starts : : "+System.currentTimeMillis());
			Map<String, Serializable> taskContextMap = backgroundTask.getTaskContextMap();
			ThemeDisplay themeDisplay = BulkUploadUtil.getThemeDisplay(taskContextMap);
			BulkUploadUtil.updateMailerMessageMap(countMap, taskContextMap);
			createMissSEOExcelTemplate(themeDisplay, countMap);
		} catch (Exception e) {
			countMap.put("message", BulkUploadUtil.getPrintStackTrace(e));
			BulkUploadUtil.sendEmail(countMap, 400, "missingurl");
		}
		_log.info("Missing Url Download ends : : "+System.currentTimeMillis());
		return BackgroundTaskResult.SUCCESS;
	}

	/**
	 * Create miss seo excel template.
	 *
	 * @param serviceContext the service context
	 * @param themeDisplay   the theme display
	 * @throws SAXException                 the sax exception
	 * @throws IOException                  the io exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws PortalException              the portal exception
	 */
	public void createMissSEOExcelTemplate(ThemeDisplay themeDisplay, Map<String, String> countMap)
			throws SAXException, IOException, ParserConfigurationException, PortalException {
		Workbook workbook = new SXSSFWorkbook();
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");  
		Sheet sheet = workbook.createSheet("SEO Template");
		sheet.setDefaultColumnWidth(30);
		CellStyle headerCellStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerCellStyle.setFont(headerFont);
		Row headRow = sheet.createRow(0);
		int headColumnNumber = 0;
		try {
			for (String i : NtucSeoBulkuploadControlPanelAppPortletKeys.MISSHEADERARR) {
				createCell(headRow, headColumnNumber++, i, headerCellStyle);
			}
		} catch (Exception e) {
			_log.error("excepion while create json -> " + e);
			workbook.close();
			throw e;
		}
		int var = 1;
		CellStyle ottDefaultCellStyle = workbook.createCellStyle();
		ottDefaultCellStyle.setWrapText(true);
		_log.info("Start Page: "+System.currentTimeMillis());
		List<Layout> layoutLst = LayoutLocalServiceUtil.getLayouts(themeDisplay.getScopeGroupId(), false);
		_log.info("layoutLst : "+layoutLst.size());
		for (Layout curLay : layoutLst) {
			int columnNumber = 0;
			Row row = sheet.createRow(var);
			if (!curLay.getType().equalsIgnoreCase("node")) {
				String missTitle = "";
				String missDesc = "";
				String missKey = "";
				if (curLay.getTitle().isEmpty()
						|| curLay.getTitle().equalsIgnoreCase(NtucSeoBulkuploadControlPanelAppPortletKeys.DefaultXML)) {
					missTitle = "";
				} else {
					missTitle = getNodeValue(curLay.getTitle(), NtucSeoBulkuploadControlPanelAppPortletKeys.Title);
				}
				if (curLay.getDescription().isEmpty() || curLay.getDescription()
						.equalsIgnoreCase(NtucSeoBulkuploadControlPanelAppPortletKeys.DefaultXML)) {
					missDesc = "";
				} else {
					missDesc = getNodeValue(curLay.getDescription(),
							NtucSeoBulkuploadControlPanelAppPortletKeys.Description);
				}
				if (curLay.getKeywords().isEmpty() || curLay.getKeywords()
						.equalsIgnoreCase(NtucSeoBulkuploadControlPanelAppPortletKeys.DefaultXML)) {
					missKey = "";
				} else {
					missKey = getNodeValue(curLay.getKeywords(), NtucSeoBulkuploadControlPanelAppPortletKeys.Keywords);
				}
				
				createCell(row, columnNumber++, themeDisplay.getPortalURL()+curLay.getFriendlyURL(), ottDefaultCellStyle);
				createCell(row, columnNumber++, missTitle, ottDefaultCellStyle);
				createCell(row, columnNumber++, missDesc, ottDefaultCellStyle);
				createCell(row, columnNumber++, missKey, ottDefaultCellStyle);
				createCell(row, columnNumber++, dateFormat.format(curLay.getModifiedDate()), ottDefaultCellStyle);
				var++;
			}
		}
		_log.info("End Page: "+System.currentTimeMillis());
		countMap.put("layout", String.valueOf(var-1));
		_log.info("Start Journal: "+System.currentTimeMillis());
		String ddmStructureKey = getStructureKey();
		List<JournalArticle> jurArtLst = JournalArticleLocalServiceUtil.getStructureArticles(themeDisplay.getScopeGroupId(), ddmStructureKey);
		_log.info("jurArtLst size : : "+jurArtLst.size());
		int count = 0;
		Map<String, JournalArticle> jAMap = new HashMap<String, JournalArticle>();
		for (JournalArticle curArticle : jurArtLst) {
			try {
				JournalArticle jurArtLatest = JournalArticleLocalServiceUtil.getLatestArticle(themeDisplay.getScopeGroupId(),
						curArticle.getArticleId(), WorkflowConstants.STATUS_APPROVED);
				if(!jAMap.containsKey(jurArtLatest.getArticleId())) {
					jAMap.put(jurArtLatest.getArticleId(), jurArtLatest);
				}
				count++;
			}
			catch(Exception ex) {
				_log.info("Error while getting latest article : "+ex);
			}
		}
		List<JournalArticle> journalList = new ArrayList<JournalArticle>(jAMap.values());
		_log.info("journalList : : : "+journalList.size());
		countMap.put("journal", String.valueOf(journalList.size()));
		for (JournalArticle curdata : journalList) {
			try {
				int columnNumber = 0;
				Row row = sheet.createRow(var);
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(curdata.getContent()));
				Document doc = db.parse(is);
				NodeList nodes = doc.getElementsByTagName(NtucSeoBulkuploadControlPanelAppPortletKeys.DynamicElement);
				String missTitle = "";
				String missDesc = "";
				String missKey = "";
				for (int i = 0; i < nodes.getLength(); i++) {
					try {
						Element element = (Element) nodes.item(i);
						NodeList name = element
								.getElementsByTagName(NtucSeoBulkuploadControlPanelAppPortletKeys.DynamicContent);
						Element line = (Element) name.item(getNodeIndex(name));
						String nodeValue = nodes.item(i).getAttributes().getNamedItem("name").getNodeValue();
						if (nodeValue.equals(NtucSeoBulkuploadControlPanelAppPortletKeys.SEOMetaTitle)
								&& getCharacterDataFromElement(line).isEmpty()) {
							missTitle = "";
						} else if (nodeValue.equals(NtucSeoBulkuploadControlPanelAppPortletKeys.SEOMetaTitle)
								&& !getCharacterDataFromElement(line).isEmpty()) {
							missTitle = line.getTextContent();
						}
						if (nodeValue.equals(NtucSeoBulkuploadControlPanelAppPortletKeys.SEOMetaDesc)
								&& getCharacterDataFromElement(line).isEmpty()) {
							missDesc = "";
						} else if (nodeValue.equals(NtucSeoBulkuploadControlPanelAppPortletKeys.SEOMetaDesc)
								&& !getCharacterDataFromElement(line).isEmpty()) {
							missDesc = line.getTextContent();
						}
						if (nodeValue.equals(NtucSeoBulkuploadControlPanelAppPortletKeys.SEOMetaKeyword)
								&& getCharacterDataFromElement(line).isEmpty()) {
							missKey = "";
						} else if (nodeValue.equals(NtucSeoBulkuploadControlPanelAppPortletKeys.SEOMetaKeyword)
								&& !getCharacterDataFromElement(line).isEmpty()) {
							missKey = line.getTextContent();
						}
					}
					catch(Exception ex) {
						_log.error("Error while getting node details : "+ex);
					}
				}
				createCell(row, columnNumber++, themeDisplay.getPortalURL() + "/-/" + curdata.getUrlTitle(),
						ottDefaultCellStyle);
				createCell(row, columnNumber++, missTitle, ottDefaultCellStyle);
				createCell(row, columnNumber++, missDesc, ottDefaultCellStyle);
				createCell(row, columnNumber++, missKey, ottDefaultCellStyle);
				createCell(row, columnNumber++, dateFormat.format(curdata.getModifiedDate()), ottDefaultCellStyle);
				var++;
			}
			catch(Exception ex) {
				_log.info("Error while generating excel : "+ex);
			}
		}
		_log.info("End Journal : "+System.currentTimeMillis());
		
		if (Validator.isNotNull(workbook)) {
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        try {
				workbook.write(bos);
				byte[] bytes = bos.toByteArray();
				ServiceContext serviceContext = BulkUploadUtil.getServiceContext(themeDisplay);
				DLFolder folder = BulkUploadUtil.createFolder(themeDisplay, serviceContext, "SEO Missing Url Excels");
				String fileName = "Missing_SEO_Details_" + new Timestamp(new Date().getTime()).getTime() + ".xlsx";
				DLAppServiceUtil.addFileEntry(themeDisplay.getScopeGroupId(), folder.getFolderId(), fileName, "application/vnd.ms-excel", fileName, "", "", bytes, serviceContext);
				countMap.put("fileName", fileName);
			} catch (Exception e) {
				throw e;
	        } finally {
	            bos.close();
	            workbook.close();
	        }
	    }
		_log.info("Excel upload at : "+System.currentTimeMillis());
		BulkUploadUtil.sendEmail(countMap, 200, "missingurl");
		_log.info("Mail Sent at : "+System.currentTimeMillis());
	}
	
	private int getNodeIndex(NodeList name) {
		 for (int j = 0; j < name.getLength(); j++) {
	         Element line = (Element) name.item(j);
	         _log.info("line : : " + line);
	         if (line.getAttribute("language-id").equals("en_GB")) {
	             return j;
	         }
	     }
		 return -1;
	}
	
	private String getStructureKey() {
		try {
			DDMStructurePersistence _persistence1 = DDMStructureUtil.getPersistence();
			Class<?> clazz1 = _persistence1.getClass();
			ClassLoader dynamicQueryClassLoader1 = clazz1.getClassLoader();
			DynamicQuery dynamicQueryStructure = DynamicQueryFactoryUtil.forClass(
					com.liferay.dynamic.data.mapping.model.DDMStructure.class,dynamicQueryClassLoader1);
			dynamicQueryStructure.add(PropertyFactoryUtil.forName("name").like(
	                "%>Courses Structure</Name>%"));
					
	        List<com.liferay.dynamic.data.mapping.model.DDMStructure> structures = ddmStructureLocalService.dynamicQuery(dynamicQueryStructure, 0, 1);
			return structures.get(0).getStructureKey();
		}
		catch(Exception ex) {
			_log.info("Error while getting structure key : "+ex);
		}
		return StringPool.BLANK;
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
			nodeValue = getCharacterDataFromNodeList(name);
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
		return "";
	}
	
	/**
	 * Gets character data from node list.
	 *
	 * @param name the name
	 * @return the character data from node list
	 */
	public static String getCharacterDataFromNodeList(NodeList name) {
		 for (int j = 0; j < name.getLength(); j++) {
	         Element line = (Element) name.item(j);
	         _log.info("line : : " + line);
	         if (line.getAttribute("language-id").equals("en_GB")) {
	        	 Node child = line.getFirstChild();
	     		if (child instanceof org.w3c.dom.CharacterData) {
	     			org.w3c.dom.CharacterData cd = (org.w3c.dom.CharacterData) child;
	     			return cd.getData();
	     		}
	         }
	     }
		return "";
	}
	
	private static void createCell(Row row, int cellIndex, String cellValue, CellStyle cellStyle) {
		Cell cell = row.createCell(cellIndex);
		cell.setCellValue(cellValue);
		cell.setCellStyle(cellStyle);
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
	
	@Reference
	private DDMStructureLocalService ddmStructureLocalService;
	
	@Reference
	private DLFolderLocalService dlFolderLocalService;
	
	@Reference
	private CounterLocalService counterLocalService;

	@Reference
	private ResourcePermissionLocalService resourcePermissionLocalService;

}
