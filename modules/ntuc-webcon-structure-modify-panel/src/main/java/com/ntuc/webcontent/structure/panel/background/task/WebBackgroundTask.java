package com.ntuc.webcontent.structure.panel.background.task;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.backgroundtask.BaseBackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReader;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.XPath;
import com.liferay.portal.xml.SAXReaderImpl;
import com.ntuc.webcontent.structure.panel.util.WebcontentModifyUtil;

import java.io.Serializable;
import java.util.*;

import org.osgi.service.component.annotations.Component;


/**
 * @author Dhivakar Sengottaiyan
 * The type Web background task.
 */
@Component(
        immediate = true,
        property = {
                "background.task.executor.class.name=com.ntuc.webcontent.structure.panel.background.task.WebBackgroundTask"
        },
        service = BackgroundTaskExecutor.class
)

public class WebBackgroundTask extends BaseBackgroundTaskExecutor {

    private final static String SAXREADERUTIL_XPATH_TYPE_BY_NAME = "//dynamic-element[@name='";
    private static final String XML_ELEMENT_TYPE_DYNAMIC_CONTENT = "dynamic-content"; // Define the XML element type
    private static final SAXReader SAX_READER = new SAXReaderImpl(); // Reuse SAXReader instance
    private static final Log log = LogFactoryUtil.getLog(WebBackgroundTask.class.getName());

    String subject, fromAddress, toAddressSuccess, toAddressFailure, successBody, failureBody = StringPool.BLANK;
    /**
     * The Count map.
     */
    Map<String, String> countMap = null;

    @Override
    public BackgroundTaskResult execute(BackgroundTask backgroundTask) throws Exception {
        countMap = new HashMap<String, String>();
        try {
            log.info("Starting background task at : " + System.currentTimeMillis());
            Map<String, Serializable> taskContextMap = backgroundTask.getTaskContextMap();
            String fieldNameCurrents = MapUtil.getString(taskContextMap, "fieldNameCurrents");
            String contents = MapUtil.getString(taskContextMap, "contents");
            String webContentIDs = MapUtil.getString(taskContextMap, "webContentIDs");
            countMap.put("fieldNameCurrents", fieldNameCurrents);
            countMap.put("contents", contents);
            countMap.put("webContentIDs", webContentIDs);
            long groupId = MapUtil.getLong(taskContextMap, "groupId");
            long userId = MapUtil.getLong(taskContextMap, "userId");
            subject = MapUtil.getString(taskContextMap, "subject");
            String userName = MapUtil.getString(taskContextMap, "userName");
            fromAddress = MapUtil.getString(taskContextMap, "fromAddress");
            toAddressSuccess = MapUtil.getString(taskContextMap, "toAddressSuccess");
            toAddressFailure = MapUtil.getString(taskContextMap, "toAddressFailure");
            successBody = MapUtil.getString(taskContextMap, "successBody");
            failureBody = MapUtil.getString(taskContextMap, "failureBody");
            try {
                log.info("Webconent getting start time : " + System.currentTimeMillis());
                DDMStructure ddmStructure = DDMStructureLocalServiceUtil.fetchDDMStructure(Long.parseLong(webContentIDs));
                List<JournalArticle> jList = JournalArticleLocalServiceUtil.getArticlesByStructureId(groupId, ddmStructure.getStructureKey(), WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
                Map<String, JournalArticle> jAMap = new HashMap<String, JournalArticle>();
                for (JournalArticle curArticle : jList) {
                    try {
                        JournalArticle jurArtLatest = JournalArticleLocalServiceUtil.getLatestArticle(groupId,
                                curArticle.getArticleId(), WorkflowConstants.STATUS_APPROVED);
                        if (!jAMap.containsKey(jurArtLatest.getArticleId())) {
                            jAMap.put(jurArtLatest.getArticleId(), jurArtLatest);
                        }
                    } catch (Exception ex) {
                        log.info("Error while getting latest article : " + ex);
                    }
                }
                List<JournalArticle> journalList = new ArrayList<JournalArticle>(jAMap.values());
                log.info("Webconent getting end time : " + System.currentTimeMillis());
                countMap.put("journal", String.valueOf(journalList.size()));
                log.info("Webconent list : " + journalList.size());
                for (JournalArticle journalArticle : journalList) {
                    try {
                        String xmlContent = journalArticle.getContentByLocale(Locale.UK.toString());
                        Document contentDoc = SAXReaderUtil.read(xmlContent);
                        String formattedString = "";
                        String fieldName = SAXREADERUTIL_XPATH_TYPE_BY_NAME.concat(fieldNameCurrents).concat("']");
                        contentDoc = contentDoc.clone();
                        XPath xPath = SAXReaderUtil.createXPath(fieldName);

                        List<Node> textNodes = xPath.selectNodes(contentDoc);

                        for (Node textNode : textNodes) {
                            Element textEl = (Element) textNode;
                            List<Element> dynamicContentEls = textEl.elements(XML_ELEMENT_TYPE_DYNAMIC_CONTENT);
                            for (Element dynamicContentEl : dynamicContentEls) {
                                dynamicContentEl.clearContent();
                                dynamicContentEl.addCDATA(contents);
                            }
                        }

                        formattedString = contentDoc.formattedString();
                        journalArticle.setContent(formattedString);
                        journalArticle.setUserId(userId);
                        journalArticle.setUserName(userName);
                        JournalArticleLocalServiceUtil.updateJournalArticle(journalArticle);
                    } catch (Exception e) {
                        log.error(e);
                    }
                }
                log.info("Webconent end time : " + System.currentTimeMillis());
                WebcontentModifyUtil.sendEmail(subject, fromAddress, toAddressSuccess, toAddressFailure,
                        successBody, failureBody, countMap, 200);
            } catch (Exception e) {
                countMap.put("message", WebcontentModifyUtil.getPrintStackTrace(e));
                log.error("Exception while getting ddmStructure " + e.getMessage());
                WebcontentModifyUtil.sendEmail(subject, fromAddress, toAddressSuccess, toAddressFailure,
                        successBody, failureBody, countMap, 400);
            }
        } catch (Exception e) {
            countMap.put("message", WebcontentModifyUtil.getPrintStackTrace(e));
            log.error("error in backgroud process " + e);
            WebcontentModifyUtil.sendEmail(subject, fromAddress, toAddressSuccess, toAddressFailure,
                    successBody, failureBody, countMap, 400);
        }
        log.info("end time : " + System.currentTimeMillis());
        return BackgroundTaskResult.SUCCESS;
    }

    @Override
    public BackgroundTaskDisplay getBackgroundTaskDisplay(BackgroundTask backgroundTask) {
        return null;
    }

    @SuppressWarnings("all")
    @Override
    public BackgroundTaskExecutor clone() {
        return this;
    }

}
