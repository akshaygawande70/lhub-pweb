package datafetch.application;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.w3c.dom.Node;

/**
 * @author skitukale
 */
@Component(property = { JaxrsWhiteboardConstants.JAX_RS_APPLICATION_BASE + "=/data",
		JaxrsWhiteboardConstants.JAX_RS_NAME + "=Greetings.Rest",
		"liferay.auth.verifier=false" }, service = Application.class)
public class DataFetchApplication extends Application {

	public Set<Object> getSingletons() {
		return Collections.<Object>singleton(this);
	}

	private static Log log = LogFactoryUtil.getLog(DataFetchApplication.class);
	private static final Pattern IMG_TAG_PATTERN = Pattern.compile("<img\\s+[^>]*src=[\"']([^\"']+)[\"'][^>]*>");

	@GET
	@Path("/fetch")
	@Produces("text/plain")
	public String hello() {

		log.info("Data Fetch start");
//		boolean extractionEnabled = GetterUtil.getBoolean(PropsUtil.get("enable_data_extraction"), false);
		boolean extractionEnabled = true;
		System.out.println("Value: " + extractionEnabled);
		if (extractionEnabled) {
			log.info("Data extraction is ENABLED ");
			try {
				int start = 0;
				int pageSize = 5000;
				int total = JournalArticleLocalServiceUtil.getJournalArticlesCount();
				log.info("Total articles: " + total);

				Map<String, JournalArticle> latestArticles = new HashMap<>();

				/**
				 * Process articles in pages
				 */
				while (start < total) {
					List<JournalArticle> articles = JournalArticleLocalServiceUtil.getArticles(39367, start,
							start + pageSize);
					log.info("Processing articles " + start + " to " + (start + articles.size()));

					for (JournalArticle article : articles) {
						/**
						 * Only process articles with templateKey "98301" and groupId 39367
						 */
						if (!"98301".equals(article.getDDMTemplateKey()) || article.getGroupId() != 39367L) {
							continue;
						}

						try {
							String rawXML = article.getContent();
							String xmlContent = parseXml(rawXML);
							Document document = parseDocument(xmlContent);
							String courseCode = fetchElementValue(document, "courseCode");

							if (Validator.isNull(courseCode)) {
								continue;
							}
							JournalArticle existing = latestArticles.get(courseCode);
							if (existing == null || article.getVersion() > existing.getVersion()) {
								latestArticles.put(courseCode, article);
							}
						} catch (Exception e) {
							log.error("XML parsing error for articleId " + article.getArticleId(), e);
						}
					}
					start += pageSize;
				}

				Workbook workbook = new XSSFWorkbook();
				Sheet sheet1 = workbook.createSheet("All_Course_Elements");

				Row header1 = sheet1.createRow(0);
				header1.createCell(0).setCellValue("Course_Code");
				header1.createCell(1).setCellValue("Article_ID");
				header1.createCell(2).setCellValue("Version");
				header1.createCell(3).setCellValue("Element_Name");
				header1.createCell(4).setCellValue("ImagePublicUrl");
				header1.createCell(5).setCellValue("S3_Path");
				header1.createCell(6).setCellValue("Final_S3_Path");

				AtomicInteger rowNum1 = new AtomicInteger(1);

				Sheet sheet2 = workbook.createSheet("Active_Courses");
				Row h2 = sheet2.createRow(0);
				h2.createCell(0).setCellValue("Course_Code");
				h2.createCell(1).setCellValue("Article_ID");
				h2.createCell(2).setCellValue("Version");
				h2.createCell(3).setCellValue("Element_Name");
				h2.createCell(4).setCellValue("ImagePublicUrl");
				h2.createCell(5).setCellValue("S3_Path");
				h2.createCell(6).setCellValue("Final_S3_Path");
				AtomicInteger rowNum2 = new AtomicInteger(1);
				/**
				 * For each latest article, process the XML and search for images.
				 */
				for (JournalArticle article : latestArticles.values()) {
					try {
						String rawXML = article.getContent();
						String xmlContent = parseXml(rawXML);
						Document document = parseDocument(xmlContent);
						Element root = document.getDocumentElement();

						String courseCode = fetchElementValue(document, "courseCode");
						double version = article.getVersion();
						String articleId = article.getArticleId();
						String defaultLocale = document.getDocumentElement().getAttribute("default-locale");
						System.out.println("Default Locale: " + defaultLocale);
						/**
						 * Fetch all dynamic-element nodes.
						 */
						NodeList nodeList = document.getElementsByTagName("dynamic-element");
						for (int i = 0; i < nodeList.getLength(); i++) {
							Element dynamicEl = (Element) nodeList.item(i);

							String elemName = dynamicEl.getAttribute("name");
							if (elemName.equalsIgnoreCase("image")) {
								continue;
							}
							// track duplicate URLs per element
							Set<String> seenSrc = new HashSet<>();
							/**
							 * Get the dynamic-content child(ren)
							 */
//							NodeList contentNodes = dynamicEl.getElementsByTagName("dynamic-content");
							String s3Finalpath = "";
							long groupIdFound = 0;
							try {
								// Walk only the direct children of this <dynamic-element>
								NodeList kids = dynamicEl.getChildNodes();
								for (int j = 0; j < kids.getLength(); j++) {
									Node n = kids.item(j);
									if (n.getNodeType() != Node.ELEMENT_NODE) {
										continue;
									}
									Element child = (Element) n;
									if (!"dynamic-content".equals(child.getTagName())) {
										continue;
									}

									/**
									 * Check default locale
									 */
//									if (!"dynamic-content".equals(child.getTagName())) continue;
									String lang = child.getAttribute("language-id");
									if (!defaultLocale.equals(lang))
										continue;

									String contentValue = child.getTextContent().trim();
									if (contentValue.isEmpty()) {
										continue;
									}

									String imageName1 = fetchElementValue(document, "image");
									String imageName = "";
									String destinations3Path = "";
									Pattern p = Pattern.compile("name\"\\s*:\\s*\"([^\"]+)\"");
									Matcher m = p.matcher(imageName1);
									if (m.find()) {
										imageName = m.group(1);
									}
									/**
									 * Case 1: Check for inline <img> tag in HTML
									 */
									Matcher matcher = IMG_TAG_PATTERN.matcher(contentValue);
									while (matcher.find()) {
										String src = matcher.group(0);
										/**
										 * skip duplicates**
										 */
										if (!seenSrc.add(src)) {
											continue;
										}
										/**
										 * Extract original image name
										 */
										Pattern pattern2 = Pattern.compile(
												"/([^/]+\\.(png|jpg|jpeg|gif|svg|webp|bmp))/[\\w\\-]+",
												Pattern.CASE_INSENSITIVE);
										Matcher matcher2 = pattern2.matcher(src);

										String imageName2 = "";
										if (matcher2.find()) {
											imageName2 = matcher2.group(1); // This captures the image name with
																			// extension
										}
										String s3Path = src;
										/**
										 * Get S3 path Now
										 */
										String[] segments = s3Path.split("/");
										String uuidSegment = segments[5];
										String imageFolderId = segments[3];
										int queryIndex = uuidSegment.indexOf("?");
										if (queryIndex != -1) {
											uuidSegment = uuidSegment.substring(0, queryIndex);
										}
										groupIdFound = article.getGroupId();
										System.out.println("SRC Found: \n" + src);
										System.out.println("UUID \n" + uuidSegment);
										DLFileEntry fileEntry = DLFileEntryLocalServiceUtil
												.getFileEntryByUuidAndGroupId(uuidSegment, groupIdFound);
										System.out.println("issue with File Entry " + fileEntry);
										if (fileEntry == null || fileEntry.equals("")) {
											continue;
										}
										String companyId = String.valueOf(fileEntry.getCompanyId());
										long folderId = fileEntry.getFolderId();
										long fileEntryId = fileEntry.getFileEntryId();
										String fileName = fileEntry.getName();
										String fileversion = fileEntry.getVersion();
										/**
										 * File Version Table
										 */
										System.out.println("File Version Table");
										System.out.println("Folder ID: " + folderId);
										System.out.println("Group ID: " + groupIdFound);
										if (folderId == 0) {
											s3Finalpath = companyId + "/" + folderId + "/adaptive/Thumbnail-300x300/"
													+ groupIdFound + "/" + groupIdFound + "/" + fileEntryId + "/"
													+ (fileEntryId + 2) + "/" + fileversion;
											destinations3Path = companyId + "/" + folderId
													+ "/adaptive/Thumbnail-300x300/" + groupIdFound + "/" + groupIdFound
													+ "/" + fileEntryId + "/" + (fileEntryId + 2) + "/" + imageName2;
										} else {
											s3Finalpath = companyId + "/" + imageFolderId + "/" + fileName + "/"
													+ fileversion;
											destinations3Path = companyId + "/" + imageFolderId + "/" + fileName + "/"
													+ imageName2;
										}
										System.out.println("Article Status" + article.getStatus());
										writeExcelRow(sheet1, rowNum1, courseCode, articleId, version, elemName, src,
												s3Finalpath, destinations3Path);
										System.out.println("Article Status" + article.getStatus());
										if (article.getStatus() == WorkflowConstants.STATUS_APPROVED) {
											writeExcelRow(sheet2, rowNum2, courseCode, articleId, version, elemName,
													src, s3Finalpath, destinations3Path);
										}
									}
									/**
									 * Case 2: If content starts with '{', treat it as JSON-based image data.
									 */
									if (contentValue.trim().startsWith("{")) {
										try {
											JSONObject json = JSONFactoryUtil.createJSONObject(contentValue);
											String imageNameFromJson;

											try {
												imageNameFromJson = json.getString("name");

												if (imageNameFromJson == null) {
													imageNameFromJson = "";
												}
											} catch (Exception e) {
												imageNameFromJson = "";
											}

											if (json.has("uuid") && json.has("groupId")) {
												String uuid = json.getString("uuid");
												long groupId = json.getLong("groupId");
												FileEntry fileEntry = DLAppLocalServiceUtil
														.getFileEntryByUuidAndGroupId(uuid, groupId);
												String src = DLUtil.getPreviewURL(fileEntry, fileEntry.getFileVersion(),
														null, "");
												s3Finalpath = "s3 path 2 ";
												writeExcelRow(sheet1, rowNum1, courseCode, articleId, version, elemName,
														src, s3Finalpath, destinations3Path);
												if (article.getStatus() == WorkflowConstants.STATUS_APPROVED) {
													writeExcelRow(sheet2, rowNum2, courseCode, articleId, version,
															elemName, src, s3Finalpath, destinations3Path);
												}
											}
										} catch (Exception e) {
											log.warn("Invalid JSON or FileEntry not found in element " + elemName, e);
										}
									}
								}
							} catch (Exception e) {
								System.out.println("SearchGlobalPortlet.render()");
								continue;
							}
						}
					} catch (Exception e) {
						log.error("Error processing articleId " + article.getArticleId(), e);
					}
				}

				String liferayHome = System.getProperty("liferay.home", "/opt/liferay7.3/");
				File outputFile = new File(liferayHome, "course_image_details.xlsx");

				try (FileOutputStream fos = new FileOutputStream(outputFile)) {
					workbook.write(fos);
				}
				workbook.close();
				log.info("Excel written to: " + outputFile.getAbsolutePath());

			} catch (Exception e) {
				log.error("Error generating Excel: " + e.getMessage(), e);
			}
		} else {
			log.info("Data extraction is DISABLED via portalext.properties");
		}
		log.info("Data Fetch end");
		return "Data Processing Completed";
	}

	/**
	 * Parses and cleans the XML content.
	 */
	private String parseXml(String xmlContent) {
		if (xmlContent == null) {
			return null;
		}
		if (xmlContent.startsWith("\uFEFF")) {
			xmlContent = xmlContent.substring(1);
		}
		xmlContent = xmlContent.replace("\\n", "\n").trim();
		int xmlStart = xmlContent.indexOf("<?xml");
		if (xmlStart > 0) {
			xmlContent = xmlContent.substring(xmlStart);
		}
		return xmlContent.trim();
	}

	/**
	 * Parses the XML content using a DocumentBuilder (native JDK parser).
	 */
	private Document parseDocument(String xmlContent) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(xmlContent)));
	}

	/**
	 * Fetches the value of the dynamic-element with the given name.
	 */
	private String fetchElementValue(Document doc, String elementName) {
		NodeList nodeList = doc.getElementsByTagName("dynamic-element");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element el = (Element) nodeList.item(i);
			if (el.hasAttribute("name") && elementName.equals(el.getAttribute("name"))) {
				NodeList contentList = el.getElementsByTagName("dynamic-content");
				if (contentList.getLength() > 0) {
					return contentList.item(0).getTextContent().trim();
				}
			}
		}
		return null;
	}

	/**
	 * A convenience method that calls fetchElementValue using the Document's root
	 * element.
	 */
	private String fetchElementValue(Document doc, String elementName, boolean useRoot) {
		return fetchElementValue(doc, elementName);
	}

	/**
	 * Writes a row into the Excel sheet.
	 */
	private void writeExcelRow(Sheet sheet, AtomicInteger rowNum, String courseCode, String articleId, double version,
			String elementName, String src, String s3_path, String destinations3Path) {
		Row row = sheet.createRow(rowNum.getAndIncrement());
		row.createCell(0).setCellValue(courseCode);
		row.createCell(1).setCellValue(articleId);
		row.createCell(2).setCellValue(version);
		row.createCell(3).setCellValue(elementName);
		row.createCell(4).setCellValue(src);
		row.createCell(5).setCellValue(s3_path);
		row.createCell(6).setCellValue(destinations3Path);
		log.info("Wrote row: " + courseCode + ", " + articleId + ", " + version + ", " + elementName + ", " + src
				+ s3_path + ", " + destinations3Path);
	}

}
