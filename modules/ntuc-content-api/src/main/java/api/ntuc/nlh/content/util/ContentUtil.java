package api.ntuc.nlh.content.util;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.persistence.DDMStructurePersistence;
import com.liferay.dynamic.data.mapping.service.persistence.DDMStructureUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;

import api.ntuc.common.util.DocumentMediaUtil;
import api.ntuc.nlh.content.constant.ContentConstants;



public class ContentUtil {
	
	private static Log log = LogFactoryUtil.getLog(ContentUtil.class);
	
	public static DDMStructure getStructure(long groupId, String structureName) throws Exception {
		return getStructure(groupId, structureName, false);
	}
	
	public static DDMStructure getStructure(long groupOrCompanyId, String structureName, boolean isCompany) throws Exception {
		DDMStructure structure = null;
		
		List<DDMStructure> structures = getStructureList(groupOrCompanyId, structureName, isCompany);
		if(structures != null) {
			if(Validator.isNotNull(structures) && structures.isEmpty() ) {
				structure = structures.get(0);
			}
			else {
				throw new Exception(structureName + " structure is not found.");
			}		
		}
		return structure;
	}
	
	public static List<DDMStructure> getStructureList(long groupId, String structureName) {
		return getStructureList(groupId, structureName, false);
	}
	
	public static List<DDMStructure> getStructureList(long groupOrCompanyId, String structureName, boolean isCompany) {
		try {
			DDMStructurePersistence dsp = DDMStructureUtil.getPersistence();
			Class<?> dsClazz = dsp.getClass();
			ClassLoader dsClassLoader = dsClazz.getClassLoader();

			DynamicQuery dqDDMStructure = DynamicQueryFactoryUtil.forClass(DDMStructure.class, dsClassLoader);

			if (groupOrCompanyId > 0) {
				if (!isCompany) {
					dqDDMStructure.add(RestrictionsFactoryUtil.eq("groupId", groupOrCompanyId));
				} else {
					dqDDMStructure.add(RestrictionsFactoryUtil.eq("companyId", groupOrCompanyId));
				}
			}

			dqDDMStructure.add(RestrictionsFactoryUtil.like("name", "%>" + structureName + "</Name>%"));
			if (!isCompany) {
				dqDDMStructure.addOrder(OrderFactoryUtil.asc("groupId"));
			} else {
				dqDDMStructure.addOrder(OrderFactoryUtil.asc("companyId"));
			}
			dqDDMStructure.addOrder(OrderFactoryUtil.desc("version"));

			List<DDMStructure> newsStructures = DDMStructureLocalServiceUtil.dynamicQuery(dqDDMStructure);

//			log.info(">> group "+groupId+" size "+productStructures.size());
			if (newsStructures != null && newsStructures.size() > 0) {
//				log.info(">>> "+productStructures.size()+" "+productStructures.get(0).getName());
//				log.info(productStructures.get(0).getDefinition());
				return newsStructures;
			}
		} catch (Exception e) {
			log.error("Error while getNewsStructure: " + e.getMessage());
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getOptionsFromStructure(DDMStructure structure, String fieldName) {
		List<String> options = new ArrayList<String>();
		try {
			if (structure != null) {
				JSONObject jsonDDMStructure = JSONFactoryUtil.createJSONObject(structure.getDefinition());
				JSONArray jArrayFields = jsonDDMStructure.getJSONArray("fields");
				jArrayFields.forEach(obj -> {
					JSONObject jsonField = (JSONObject) obj;
//					log.info(">>- "+jsonField);

					if (jsonField.getString("name").equals(fieldName)) {
						JSONArray jaNestFields = jsonField.getJSONArray("nestedFields");
//						log.info(">>> - "+jaNestFields);

						if (jaNestFields != null && jaNestFields.length() > 0) {
							JSONObject joNestFields = (JSONObject) jaNestFields.get(0);
//							log.info(">>> -- "+joNestFields);
							JSONArray jaOptions = joNestFields.getJSONArray("options");

							if (jaOptions != null && jaOptions.length() > 0) {
								jaOptions.forEach(opt -> {
									JSONObject jsonOption = (JSONObject) opt;
//									log.info(">>>> --- "+jsonOption);

									options.add(jsonOption.getString("value"));
								});
							}
						}

						return;
					}
				});
			}
		} catch (Exception e) {
			log.error("Error while getOptionsFromStructure " + fieldName + ": " + e.getMessage());
		}

		return options;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getSortedOptionsFromStructure(DDMStructure structure, String fieldName) {
		Map<String, String> options = new LinkedHashMap<String, String>();
		try {
			if (structure != null) {
				JSONObject jsonDDMStructure = JSONFactoryUtil.createJSONObject(structure.getDefinition());
				JSONArray jArrayFields = jsonDDMStructure.getJSONArray("fields");
				jArrayFields.forEach(obj -> {
					JSONObject jsonField = (JSONObject) obj;
//					log.info(">>- "+jsonField);

					if (jsonField.getString("name").equals(fieldName)) {
						JSONArray jaNestFields = jsonField.getJSONArray("nestedFields");
//						log.info(">>> - "+jaNestFields);

						if (jaNestFields != null && jaNestFields.length() > 0) {
							JSONObject joNestFields = (JSONObject) jaNestFields.get(0);
//							log.info(">>> -- "+joNestFields);
							JSONArray jaOptions = joNestFields.getJSONArray("options");

							if (jaOptions != null && jaOptions.length() > 0) {
								for (int i = 0; i < jaOptions.length(); i++) {
									JSONObject jsonOption = (JSONObject) jaOptions.get(i);
//									log.info(">>>> --- "+jsonOption);

									String option = jsonOption.getString("value");// log.info(">>>> --- option
																					// "+option);
									options.put(option, option);
								}
								;
							}
						}
					}
				});
			}
		} catch (Exception e) {
			log.error("Error while getOptionsFromStructure " + fieldName + ": " + e.getMessage());
		}

		return options;
	}

	// get article content element
	public static String getWebContentVal(Document document, int nodeDepth, String columnName, ThemeDisplay th) {
		String returnValue = "";
		try {
			//Document document = SAXReaderUtil.read(xmlContent);
			String nodeDepthStr = "";
			for (int i = 0; i < nodeDepth; i++) {
				nodeDepthStr = nodeDepthStr.concat("/dynamic-element");
			}

			// get by language if not exist, then get without language id
			Node node = document.selectSingleNode("/root".concat(nodeDepthStr).concat("[@name='").concat(columnName)
					.concat("']/dynamic-content[@language-id='").concat(th.getLocale().toString()).concat("']"));
			if (node == null) {
				node = document.selectSingleNode("/root".concat(nodeDepthStr).concat("[@name='").concat(columnName)
						.concat("']/dynamic-content"));
			}
			String value = node.getText();
			if (value != null) {
				returnValue = value;
			}
		} catch (Exception e) {
//			log.error("Error while getting "+columnName+" content: " + e.getMessage());
		}

		return returnValue;
	}
	
	public static String getWebContentVal(String xmlContent, int nodeDepth, String columnName, String locale) {
		String returnValue = "";
		try {
			Document document = SAXReaderUtil.read(xmlContent);
			String nodeDepthStr = "";
			for (int i = 0; i < nodeDepth; i++) {
				nodeDepthStr = nodeDepthStr.concat("/dynamic-element");
			}

			// get by language if not exist, then get without language id
			Node node = document.selectSingleNode("/root".concat(nodeDepthStr).concat("[@name='").concat(columnName)
					.concat("']/dynamic-content[@language-id='").concat(locale).concat("']"));
			if (node == null) {
				node = document.selectSingleNode("/root".concat(nodeDepthStr).concat("[@name='").concat(columnName)
						.concat("']/dynamic-content"));
			}
			String value = node.getText();
			if (value != null) {
				returnValue = value;
			}
		} catch (Exception e) {
//			log.error("Error while getting "+columnName+" content: " + e.getMessage());
		}

		return returnValue;
	}
	
	public static String getAndReplaceWebContentVal(String xmlContent, int nodeDepth, Map<String, String> fieldMap, String languageId) {
		String returnValue = "";
		try {
			Document document = SAXReaderUtil.read(xmlContent);
			String nodeDepthStr = "";
			for (int i = 0; i < nodeDepth; i++) {
				nodeDepthStr = nodeDepthStr.concat("/dynamic-element");
			}
			
			// get by language if not exist, then get without language id
			for(Map.Entry<String, String> entry : fieldMap.entrySet()) {
				Node node = document.selectSingleNode("/root".concat(nodeDepthStr).concat("[@name='").concat(entry.getKey())
						.concat("']/dynamic-content[@language-id='").concat(languageId).concat("']"));
				if (node == null) {
					node = document.selectSingleNode("/root".concat(nodeDepthStr).concat("[@name='").concat(entry.getKey())
							.concat("']/dynamic-content"));
				}
				node.setText(entry.getValue());
			}
			
			String value = document.asXML();
			if (value != null) {
				returnValue = value;
			}
		} catch (Exception e) {
			log.error("Error while getting content: " + e.getMessage());
		}

		return returnValue;
	}
	
	public static String getAndReplaceWebContentVal(String xmlContent, int nodeDepth, Map<String, String> fieldMap, String[] languageIds) {
		String returnValue = "";
		try {
			Document document = SAXReaderUtil.read(xmlContent);
			String nodeDepthStr = "";
			for (int i = 0; i < nodeDepth; i++) {
				nodeDepthStr = nodeDepthStr.concat("/dynamic-element");
			}
			
			// get by language if not exist, then get without language id
			for(Map.Entry<String, String> entry : fieldMap.entrySet()) {
				for(String languageId : languageIds) {
					Node node = document.selectSingleNode("/root".concat(nodeDepthStr).concat("[@name='").concat(entry.getKey())
							.concat("']/dynamic-content[@language-id='").concat(languageId).concat("']"));
					if (node == null) {
						node = document.selectSingleNode("/root".concat(nodeDepthStr).concat("[@name='").concat(entry.getKey())
								.concat("']/dynamic-content"));
					}
					node.setText(entry.getValue());
				}
			}
			
			String value = document.asXML();
			if (value != null) {
				returnValue = value;
			}
		} catch (Exception e) {
			log.error("Error while getting content: " + e.getMessage());
		}

		return returnValue;
	}

	// get multiple content element
	public static List<String> getMultipleWebContentVal(String xmlContent, int nodeDepth, String columnName,
			ThemeDisplay th) {
		List<String> returnValues = new ArrayList<String>();
		try {
			Document document = SAXReaderUtil.read(xmlContent);

			String nodeDepthStr = "";
			for (int i = 0; i < nodeDepth; i++) {
				nodeDepthStr = nodeDepthStr.concat("/dynamic-element");
			}

			// get by language if not exist, then get without language id
			List<Node> nodes = document.selectNodes("/root".concat(nodeDepthStr).concat("[@name='").concat(columnName)
					.concat("']/dynamic-content[@language-id='").concat(th.getLocale().toString()).concat("']"));
			if (nodes == null || nodes.size() == 0) {
				nodes = document.selectNodes("/root".concat(nodeDepthStr).concat("[@name='").concat(columnName)
						.concat("']/dynamic-content"));
			}

			if (nodes != null && nodes.size() > 0) {
				for (Node node : nodes) {
					String value = node.getText();
					if (value != null) {
						returnValues.add(value);
					}
				}
			}
		} catch (Exception e) {
			log.error("Error while getting multiple content: " + e.getMessage());
		}

		return returnValues;
	}

	// get multiple content element
	public static List<String> getMultipleOptionVal(String xmlContent, int nodeDepth, String columnName,
			ThemeDisplay th) {
		List<String> returnValues = new ArrayList<String>();
		try {
			Document document = SAXReaderUtil.read(xmlContent);

			String nodeDepthStr = "";
			for (int i = 0; i < nodeDepth; i++) {
				nodeDepthStr = nodeDepthStr.concat("/dynamic-element");
			}

			// get by language if not exist, then get without language id
			List<Node> nodes = document.selectNodes("/root".concat(nodeDepthStr).concat("[@name='").concat(columnName)
					.concat("']/dynamic-content[@language-id='").concat(th.getLocale().toString()).concat("']/option"));
			if (nodes == null || nodes.isEmpty()) {
				nodes = document.selectNodes("/root".concat(nodeDepthStr).concat("[@name='").concat(columnName)
						.concat("']/dynamic-content/option"));
			}

			if (nodes != null && nodes.size() > 0) {
				for (Node node : nodes) {
					String value = node.getText();
					if (value != null) {
						returnValues.add(value);
					}
				}
			}
		} catch (Exception e) {
			log.error("Error while getting multiple option: " + e.getMessage());
		}

		return returnValues;
	}

	// get multiple node
	public static List<Node> getNodes(String xmlContent, int nodeDepth, String columnName, ThemeDisplay th) {
		List<Node> returnValues = null;
		try {
			Document document = SAXReaderUtil.read(xmlContent);

			String nodeRoot = "";
			if (xmlContent.contains("root")) {
				nodeRoot = "/root";
			}

			String nodeDepthStr = "";
			for (int i = 0; i < nodeDepth; i++) {
				nodeDepthStr = nodeDepthStr.concat("/dynamic-element");
			}

			returnValues = document
					.selectNodes(nodeRoot.concat(nodeDepthStr).concat("[@name='").concat(columnName).concat("']"));
		} catch (Exception e) {
			log.error("Error while getting multiple node: " + e.getMessage());
		}

		return returnValues;
	}

	public static String getImageUrl(Document document, int nodeDepth, String columnName, ThemeDisplay th) {
		String imageUrl = "";
		try {
			String jsonObj = getWebContentVal(document, nodeDepth, columnName, th);
			if (Validator.isNotNull(jsonObj)) {
				JSONObject jsonLogo = JSONFactoryUtil.createJSONObject(jsonObj);
				Long fileEntryId = jsonLogo.getLong("fileEntryId");
				DLFileEntry fe = DLFileEntryLocalServiceUtil.getDLFileEntry(fileEntryId);
				if (fe != null) {
					imageUrl = DocumentMediaUtil.generateURL(fe.getFileEntryId(), false);
				}
			}
		} catch (Exception e) {
//			log.error("Error while getting image url: " + e.getMessage());
		}

		return imageUrl;
	}

	// get multiple image element
	public static List<String> getMultipleImageVal(String xmlContent, int nodeDepth, String columnName,
			ThemeDisplay th) {
		List<String> returnValues = new ArrayList<String>();
		try {
			Document document = SAXReaderUtil.read(xmlContent);

			String nodeDepthStr = "";
			for (int i = 0; i < nodeDepth; i++) {
				nodeDepthStr = nodeDepthStr.concat("/dynamic-element");
			}

			// get by language if not exist, then get without language id
			List<Node> nodes = document.selectNodes("/root".concat(nodeDepthStr).concat("[@name='").concat(columnName)
					.concat("']/dynamic-content[@language-id='").concat(th.getLocale().toString()).concat("']"));
			if (nodes == null || nodes.size() == 0) {
				nodes = document.selectNodes("/root".concat(nodeDepthStr).concat("[@name='").concat(columnName)
						.concat("']/dynamic-content"));
			}

			if (nodes != null && nodes.size() > 0) {
				for (Node node : nodes) {
					String value = node.getText();
					if (Validator.isNotNull(value)) {
						JSONObject jsonLogo = JSONFactoryUtil.createJSONObject(value);
						Long fileEntryId = jsonLogo.getLong("fileEntryId");
						DLFileEntry fe = DLFileEntryLocalServiceUtil.getDLFileEntry(fileEntryId);
						if (fe != null) {
							String imageUrl = DocumentMediaUtil.generateURL(fe.getFileEntryId(), false);
							returnValues.add(imageUrl);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Error while getting multiple option: " + e.getMessage());
		}

		return returnValues;
	}

	public static String getFileUrl(Document document, int nodeDepth, String columnName, ThemeDisplay th) {
		String fileUrl = "";
		try {
			String jsonObj = getWebContentVal(document, nodeDepth, columnName, th);
			if (Validator.isNotNull(jsonObj)) {
				JSONObject jsonFile = JSONFactoryUtil.createJSONObject(jsonObj);
				Long fileEntryId = jsonFile.getLong("classPK");
				DLFileEntry fe = DLFileEntryLocalServiceUtil.getDLFileEntry(fileEntryId);
				if (fe != null) {
					fileUrl = DocumentMediaUtil.generateURL(fe.getFileEntryId(), true);
				}
			}
		} catch (Exception e) {
			log.error("Error while getting file url: " + e.getMessage());
		}

		return fileUrl;
	}
	
	public static String getLinkUrl(Document document, int nodeDepth, String columnName, ThemeDisplay th) {
		String linkUrl = "";
		try {
			String linkObj = getWebContentVal(document, nodeDepth, columnName, th);
			if (Validator.isNotNull(linkObj) && linkObj.indexOf("@")>-1) {
				String[] linkArr = linkObj.split("@");
				long groupId = Long.valueOf(linkArr[2]);
				boolean isPrivate = (!linkArr[1].equalsIgnoreCase("public")) ? true : false;
				long layoutId = Long.valueOf(linkArr[0]);

				Layout layout = LayoutLocalServiceUtil.getLayout(groupId, isPrivate, layoutId);
				if (layout != null) {
					if (isPrivate) {
						linkUrl = linkUrl.concat(th.getPathFriendlyURLPrivateGroup());
					} else {
						linkUrl = linkUrl.concat(th.getPathFriendlyURLPublic());
					}

					linkUrl = linkUrl.concat(th.getScopeGroup().getFriendlyURL())
							.concat(layout.getFriendlyURL(th.getLocale()));
				}
			}
			else {
				throw new Exception("Field "+columnName+" don't have value.");
			}
		} catch (Exception e) {
			log.error("Error while getting link url : " + e.getMessage());
		}
		
		// to fix issue on google analytic because with or without /web/amfs detected as different page
		if(linkUrl.indexOf("/web/amfs") > -1) {
			linkUrl = linkUrl.replace("/web/amfs", "");
		}

		return linkUrl;
	}

	// get multiple node by node
	public static List<Node> getNodesByNode(Node nodeSource, int nodeDepth, String columnName, ThemeDisplay th) {
		List<Node> returnValues = null;
		try {
			String nodeElementStr = "";
			for (int i = 0; i < nodeDepth; i++) {
				nodeElementStr = nodeElementStr.concat("dynamic-element");
				if (i < (nodeDepth - 1)) {
					nodeElementStr = nodeElementStr.concat("/");
				}
			}

			if (Validator.isNotNull(columnName)) {
				nodeElementStr = nodeElementStr.concat("[@name='").concat(columnName).concat("']");
			}

			returnValues = nodeSource.selectNodes(nodeElementStr);
		} catch (Exception e) {
			log.error("Error while getting nodes by node: " + e.getMessage());
		}

		return returnValues;
	}

	// get node title
	public static String getNodeVal(Node nodeSource, int nodeDepth, String columnName, ThemeDisplay th) {
		String returnValue = "";
		try {
			String nodeElementStr = "";
			for (int i = 0; i < nodeDepth; i++) {
				nodeElementStr = nodeElementStr.concat("dynamic-element");
				if (i < (nodeDepth - 1)) {
					nodeElementStr = nodeElementStr.concat("/");
				}
			}

			if (Validator.isNotNull(columnName)) {
				nodeElementStr = nodeElementStr.concat("[@name='").concat(columnName).concat("']");
			}

			if (Validator.isNotNull(nodeElementStr)) {
				nodeElementStr = nodeElementStr.concat("/");
			}

			// get by language if not exist, then get without language id
			Node node = nodeSource.selectSingleNode(nodeElementStr.concat("dynamic-content[@language-id='")
					.concat(th.getLocale().toString()).concat("']"));
			if (node == null) {
				node = nodeSource.selectSingleNode(nodeElementStr.concat("dynamic-content"));
			}

			String value = node.getText();
			if (value != null) {
				returnValue = value;
			}
		} catch (Exception e) {
			log.error("Error while getting node content: " + e.getMessage());
		}

		return returnValue;
	}

	public static String getImageUrlByNode(Node nodeSource, int nodeDepth, String columnName, ThemeDisplay th) {
		String imageUrl = "";
		try {
			String jsonObj = getNodeVal(nodeSource, nodeDepth, columnName, th);
			if (Validator.isNotNull(jsonObj)) {
				JSONObject jsonLogo = JSONFactoryUtil.createJSONObject(jsonObj);
				Long fileEntryId = jsonLogo.getLong("fileEntryId");
				DLFileEntry fe = DLFileEntryLocalServiceUtil.getDLFileEntry(fileEntryId);
				if (fe != null) {
					imageUrl = DocumentMediaUtil.generateURL(fe.getFileEntryId(), false);
				}
			}
		} catch (Exception e) {
			log.error("Error while getting image url by node: " + e.getMessage());
		}

		return imageUrl;
	}

	public static String getLinkUrlByNode(Node nodeSource, int nodeDepth, String columnName, ThemeDisplay th) {
		String linkUrl = "";
		try {
			String linkObj = getNodeVal(nodeSource, nodeDepth, columnName, th);
			if (Validator.isNotNull(linkObj)) {
				String[] linkArr = linkObj.split("@");
				long groupId = Long.valueOf(linkArr[2]);
				boolean isPrivate = (!linkArr[1].equalsIgnoreCase("public")) ? true : false;
				long layoutId = Long.valueOf(linkArr[0]);

				Layout layout = LayoutLocalServiceUtil.getLayout(groupId, isPrivate, layoutId);
				if (layout != null) {
					if (isPrivate) {
						linkUrl = linkUrl.concat(th.getPathFriendlyURLPrivateGroup());
					} else {
						linkUrl = linkUrl.concat(th.getPathFriendlyURLPublic());
					}

					linkUrl = linkUrl.concat(th.getScopeGroup().getFriendlyURL())
							.concat(layout.getFriendlyURL(th.getLocale()));
				}
			}
		} catch (Exception e) {
			log.error("Error while getting link url by node: " + e.getMessage());
		}

		return linkUrl;
	}

	// generate detail url
	public static String getDetailUrl(PortletRequest request, ThemeDisplay themeDisplay, String detailPageKey,
			String pageUrl, String portletDetailName, long primaryKey, boolean enableBackUrl) {
		StringBuffer buffer = new StringBuffer();
		try {
			String detailNamespace = ("_").concat(portletDetailName).concat("_");

			if (Validator.isNull(pageUrl)) {
				if (themeDisplay.getURLCurrent().contains(themeDisplay.getPathFriendlyURLPrivateGroup())) {
					buffer.append(themeDisplay.getPathFriendlyURLPrivateGroup());
				} else {
					buffer.append(themeDisplay.getPathFriendlyURLPublic());
				}

				buffer.append(themeDisplay.getScopeGroup().getFriendlyURL());
			} else {
				buffer.append(pageUrl);
			}

			if (Validator.isNotNull(detailPageKey)) {
//				buffer.append(GetterUtil.getString(PortletProps.get(detailPageKey)));
			}

			buffer.append("?");
			buffer.append("p_p_id=");
			buffer.append(portletDetailName);
			buffer.append("&p_p_lifecycle=0&");
			buffer.append(detailNamespace);
			buffer.append(ContentConstants.JOURNAL_PRIMARYKEY);
			buffer.append("=");
			buffer.append(String.valueOf(primaryKey));

			if (enableBackUrl) {
				buffer.append("&");
				buffer.append(detailNamespace);
				buffer.append(ContentConstants.BACK_URL_VAR);
				buffer.append("=");
				buffer.append(themeDisplay.getURLCurrent());
			}
		} catch (Exception e) {
			log.error("Error while getDetailUrl: " + e.getMessage());
		}

		return buffer.toString();
	}

}
