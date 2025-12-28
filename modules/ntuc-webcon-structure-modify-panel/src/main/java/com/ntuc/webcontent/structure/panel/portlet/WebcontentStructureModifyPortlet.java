package com.ntuc.webcontent.structure.panel.portlet;


import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManagerUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.ntuc.webcontent.structure.panel.background.task.WebBackgroundTask;
import com.ntuc.webcontent.structure.panel.constants.WebcontentStructureModifyPortletKeys;
import com.ntuc.webcontent.structure.panel.util.WebcontentModifyUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.ProcessAction;

import org.osgi.service.component.annotations.Component;

/**
 * @author Dhivakar Sengottaiyan
 * The type Webcontent structure modify portlet.
 */
@Component(
        property = {
                "com.liferay.portlet.add-default-resource=true",
                "com.liferay.portlet.display-category=category.hidden",
                "com.liferay.portlet.header-portlet-css=/css/main.css",
                "com.liferay.portlet.layout-cacheable=true",
                "com.liferay.portlet.private-request-attributes=false",
                "com.liferay.portlet.private-session-attributes=false",
                "com.liferay.portlet.render-weight=50",
                "com.liferay.portlet.use-default-template=true",
                "javax.portlet.display-name=Web Content Details Modification",
                "javax.portlet.expiration-cache=0",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/view.jsp",
                "javax.portlet.name=" + WebcontentStructureModifyPortletKeys.WEBCONTENTSTRUCTUREMODIFY,
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.security-role-ref=power-user,user",

        },
        service = Portlet.class
)
public class WebcontentStructureModifyPortlet extends MVCPortlet {

    private final static String SAXREADERUTIL_XPATH_TYPE_BY_NAME = "//dynamic-element[@name='";
    private final static String XML_ELEMENT_TYPE_DYNAMIC_CONTENT = "dynamic-content";
    private static final Log log = LogFactoryUtil.getLog(WebcontentStructureModifyPortlet.class.getName());

    /**
     * Modify ddm structure.
     *
     * @param actionRequest  the action request
     * @param actionResponse the action response
     * @throws PortalException  the portal exception
     * @throws PortletException the portlet exception
     */
    @ProcessAction(name = "modifyDDMStructure")
    public void modifyDDMStructure(ActionRequest actionRequest, ActionResponse actionResponse) throws PortalException, PortletException {
        log.info("action time : " + System.currentTimeMillis());
        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
        ServiceContext serviceContext = ServiceContextFactory
                .getInstance(WebBackgroundTask.class.getName(), actionRequest);
        serviceContext.setScopeGroupId(themeDisplay.getScopeGroupId());
        String fieldNameCurrent = ParamUtil.getString(actionRequest, "fieldName");
        String content = ParamUtil.getString(actionRequest, "content");
        String webContentID = ParamUtil.getString(actionRequest, "webContentStructureID");
        PortletPreferences portletPreferences = actionRequest.getPreferences();
        String subject = portletPreferences.getValue("subject", StringPool.BLANK);
        String fromAddress = portletPreferences.getValue("fromAddress", StringPool.BLANK);
        String toAddressSuccess = portletPreferences.getValue("toAddressSuccess", StringPool.BLANK);
        String toAddressFailure = portletPreferences.getValue("toAddressFailure", StringPool.BLANK);
        String successBody = portletPreferences.getValue("successBody", StringPool.BLANK);
        String failureBody = portletPreferences.getValue("failureBody", StringPool.BLANK);
        long userId = PortalUtil.getUserId(actionRequest);
        long companyId = PortalUtil.getCompanyId(actionRequest);
        long groupId = PortalUtil.getScopeGroupId(actionRequest);
        Map<String, Serializable> taskContextMap = new HashMap<>();
        taskContextMap.put("fieldNameCurrents", fieldNameCurrent);
        taskContextMap.put("contents", content);
        taskContextMap.put("webContentIDs", webContentID);
        taskContextMap.put("companyId", companyId);
        taskContextMap.put("groupId", groupId);
        taskContextMap.put("userId", userId);
        taskContextMap.put("userName", themeDisplay.getUser().getFullName());
        taskContextMap.put("subject", subject);
        taskContextMap.put("fromAddress", fromAddress);
        taskContextMap.put("toAddressSuccess", toAddressSuccess);
        taskContextMap.put("toAddressFailure", toAddressFailure);
        taskContextMap.put("successBody", successBody);
        taskContextMap.put("failureBody", failureBody);
        log.info("taskContextMap : " + taskContextMap);
        try {
            com.liferay.portal.kernel.backgroundtask.BackgroundTask bgTask = BackgroundTaskManagerUtil.addBackgroundTask(userId, groupId, "backgroud task contest", WebBackgroundTask.class.getName(), taskContextMap, serviceContext);
        } catch (Exception e) {
            WebcontentModifyUtil.sendEmail(subject, fromAddress, toAddressSuccess, toAddressFailure,
                    successBody, failureBody, WebcontentModifyUtil.getPrintStackTrace(e), 400);
            log.error("Error while creating background task : " + e);
        }
        SessionMessages.add(actionRequest, "success");
        log.info("action time : " + System.currentTimeMillis());
        actionResponse.getRenderParameters().setValue("jspPage", "/success.jsp");
    }

    /**
     * Upload excel.
     *
     * @param actionRequest  the action request
     * @param actionResponse the action response
     */
    @ProcessAction(name = "emailConfiguration")
    public void uploadExcel(ActionRequest actionRequest, ActionResponse actionResponse) {
        try {
            String subject = ParamUtil.getString(actionRequest, "subject");
            String fromAddress = ParamUtil.getString(actionRequest, "fromAddress");
            String toAddressSuccess = ParamUtil.getString(actionRequest, "toAddressSuccess");
            String toAddressFailure = ParamUtil.getString(actionRequest, "toAddressFailure");
            String successBody = ParamUtil.getString(actionRequest, "successBody");
            String failureBody = ParamUtil.getString(actionRequest, "failureBody");
            log.info("subject : " + subject + " fromAddress : " + fromAddress + " toAddress : " + toAddressSuccess + " successBody : " + successBody);
            actionRequest.getPreferences().setValue("subject", subject);
            actionRequest.getPreferences().setValue("fromAddress", fromAddress);
            actionRequest.getPreferences().setValue("toAddressSuccess", toAddressSuccess);
            actionRequest.getPreferences().setValue("toAddressFailure", toAddressFailure);
            actionRequest.getPreferences().setValue("successBody", successBody);
            actionRequest.getPreferences().setValue("failureBody", failureBody);
            actionRequest.getPreferences().store();
            return;
        } catch (Exception e) {
            log.error("Error in upload excel : " + e.getMessage());
        }
    }
}