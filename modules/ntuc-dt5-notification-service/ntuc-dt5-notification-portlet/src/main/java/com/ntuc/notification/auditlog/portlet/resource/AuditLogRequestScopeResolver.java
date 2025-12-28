package com.ntuc.notification.auditlog.portlet.resource;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletRequest;

/**
 * Resolves companyId / groupId for baseline scoping.
 */
public final class AuditLogRequestScopeResolver {

    private AuditLogRequestScopeResolver() {
        // util
    }

    public static long companyId(PortletRequest req) {
        ThemeDisplay td = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
        return (td == null) ? 0L : td.getCompanyId();
    }

    public static long groupId(PortletRequest req) {
        ThemeDisplay td = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
        return (td == null) ? 0L : td.getScopeGroupId();
    }
}
