package com.ntuc.notification.portlet.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.ntuc.notification.configuration.Dt5EmailTemplateConfiguration;
import com.ntuc.notification.portlet.constants.NtucDt5NotificationPortletKeys;

import java.io.IOException;
import java.util.Dictionary;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.ProcessAction;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author skitukale
 */
@Component(
		immediate = true,
        property = {
                "com.liferay.portlet.display-category=category.ntuc",
                "com.liferay.portlet.header-portlet-css=/css/main.css",
                "com.liferay.portlet.instanceable=true",
                "javax.portlet.display-name=NtucDt5Notification",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/view.jsp",
                "javax.portlet.name=" + NtucDt5NotificationPortletKeys.NTUCDT5NOTIFICATION,
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.security-role-ref=power-user,user"
        },
        service = Portlet.class
)

public class NtucDt5NotificationPortlet extends MVCPortlet {
	private static final Log _log = LogFactoryUtil.getLog(NtucDt5NotificationPortlet.class);

    @Reference
    private ConfigurationProvider _configurationProvider;

    @Override
    public void render(RenderRequest renderRequest, RenderResponse renderResponse)
                    throws IOException, PortletException {
            _log.info("NTUC - DT5NotificationPortlet portlet render - start");
            _log.info("NTUC - DT5NotificationPortlet portlet render - end");
            super.render(renderRequest, renderResponse);
    }

    @ProcessAction(name = "saveEmailTemplates")
    public void saveEmailTemplates(ActionRequest actionRequest, ActionResponse actionResponse)
            throws PortletException {

            ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
            String successSubject = ParamUtil.getString(actionRequest, "successSubject");
            String successBody = ParamUtil.getString(actionRequest, "successBody");
            String failureSubject = ParamUtil.getString(actionRequest, "failureSubject");
            String failureBody = ParamUtil.getString(actionRequest, "failureBody");
            String redirect = ParamUtil.getString(actionRequest, "redirect");

            Dictionary<String, Object> properties = new HashMapDictionary<>();
            properties.put("successSubject", successSubject);
            properties.put("successBody", successBody);
            properties.put("failureSubject", failureSubject);
            properties.put("failureBody", failureBody);

            try {
                    _configurationProvider.saveCompanyConfiguration(
                            Dt5EmailTemplateConfiguration.class, themeDisplay.getCompanyId(), properties);
                    SessionMessages.add(actionRequest, "templateConfigurationSaved");
            } catch (ConfigurationException configurationException) {
                    _log.error("Unable to save DT5 email template configuration", configurationException);
                    SessionErrors.add(actionRequest, "templateConfigurationError");
            }

            if (Validator.isNotNull(redirect)) {
                    try {
                            actionResponse.sendRedirect(redirect);
                    } catch (IOException ioException) {
                            throw new PortletException(ioException);
                    }
            }
    }
}