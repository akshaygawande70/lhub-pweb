package com.ntuc.notification.portlet.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.ntuc.notification.portlet.constants.NtucDt5NotificationPortletKeys;

import javax.portlet.Portlet;
import org.osgi.service.component.annotations.Component;

@Component(
    immediate = true,
    property = {
        "com.liferay.portlet.display-category=category.ntuc",
        "com.liferay.portlet.instanceable=false",
        "javax.portlet.name=" + NtucDt5NotificationPortletKeys.AUDIT_LOG_BROWSER,
        "javax.portlet.display-name=Audit Log Browser",
        "javax.portlet.init-param.template-path=/",
        // JSP file we'll render from our MVCRenderCommand
        "javax.portlet.init-param.view-template=/audit_log/view.jsp",
        "javax.portlet.security-role-ref=power-user,user"
    },
    service = Portlet.class
)
public class AuditLogPortlet extends MVCPortlet {}
