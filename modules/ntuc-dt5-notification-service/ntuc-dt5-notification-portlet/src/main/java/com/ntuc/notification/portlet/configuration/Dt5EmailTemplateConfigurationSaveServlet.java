package com.ntuc.notification.portlet.configuration;

import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.PortalUtil;

import com.ntuc.notification.emailtemplate.api.Dt5EmailTemplateAdminService;
import com.ntuc.notification.emailtemplate.api.Dt5EmailTemplateUpdateRequest;
import com.ntuc.notification.portlet.util.RequestParamSuffixUtil;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Saves DT5 email template configuration from System Settings UI via HTTP endpoint.
 *
 * Security rule:
 * - Only Company Administrators (role "Administrator") may save templates.
 * - DO NOT gate on isOmniadmin(); Control Panel access is broader than omniadmin.
 *
 * Final URL:
 * - http://localhost:8080/o/dt5-email-templates/save
 */
@Component(
    service = Servlet.class,
    property = {
        "osgi.http.whiteboard.context.select=(osgi.http.whiteboard.context.name=default)",
        "osgi.http.whiteboard.servlet.name=dt5-email-templates-save",
        "osgi.http.whiteboard.servlet.pattern=/dt5-email-templates/save"
    }
)
public class Dt5EmailTemplateConfigurationSaveServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String ROLE_ADMINISTRATOR = "Administrator";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        try {
            if (!isCompanyAdministrator(request)) {
                throw new PrincipalException("Not authorized");
            }
        }
        catch (Exception e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Not authorized");
            return;
        }

        String pAuth = RequestParamSuffixUtil.getStringBySuffix(request, "p_auth", "");
        HttpServletRequest csrfWrapped = new PAuthRequestWrapper(request, pAuth);

        try {
            AuthTokenUtil.checkCSRFToken(
                csrfWrapped, Dt5EmailTemplateConfigurationSaveServlet.class.getName());
        }
        catch (Exception e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF token validation failed");
            return;
        }

        Dt5EmailTemplateUpdateRequest updateRequest =
            Dt5EmailTemplateUpdateRequest.builder()
                .redirect(RequestParamSuffixUtil.getStringBySuffix(request, "redirect", ""))
                .dt5FailureSubject(RequestParamSuffixUtil.getStringBySuffix(request, "dt5FailureSubject", ""))
                .dt5FailureBody(RequestParamSuffixUtil.getStringBySuffix(request, "dt5FailureBody", ""))
                .clsFailureSubject(RequestParamSuffixUtil.getStringBySuffix(request, "clsFailureSubject", ""))
                .clsFailureBody(RequestParamSuffixUtil.getStringBySuffix(request, "clsFailureBody", ""))
                .jaFailureSubject(RequestParamSuffixUtil.getStringBySuffix(request, "jaFailureSubject", ""))
                .jaFailureBody(RequestParamSuffixUtil.getStringBySuffix(request, "jaFailureBody", ""))
                .build();

        _dt5EmailTemplateAdminService.saveSystemTemplates(updateRequest);

        String redirect = updateRequest.getRedirect();
        if (redirect != null && redirect.length() > 0) {
            response.sendRedirect(redirect);
            return;
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST required");
    }

    private boolean isCompanyAdministrator(HttpServletRequest request) throws Exception {
        long companyId = PortalUtil.getCompanyId(request);
        long userId = PortalUtil.getUserId(request);

        if (userId <= 0) {
            return false;
        }

        long adminRoleId = _roleLocalService.getRole(companyId, ROLE_ADMINISTRATOR).getRoleId();

        // "true" => inherited roles count
        return _userLocalService.hasRoleUser(adminRoleId, userId);
    }

    private static class PAuthRequestWrapper extends HttpServletRequestWrapper {

        private final String _pAuth;

        private PAuthRequestWrapper(HttpServletRequest request, String pAuth) {
            super(request);
            _pAuth = pAuth;
        }

        @Override
        public String getParameter(String name) {
            if ("p_auth".equals(name) && _pAuth != null && _pAuth.length() > 0) {
                return _pAuth;
            }
            return super.getParameter(name);
        }
    }

    @Reference
    private Dt5EmailTemplateAdminService _dt5EmailTemplateAdminService;

    @Reference
    private RoleLocalService _roleLocalService;

    @Reference
    private UserLocalService _userLocalService;
}
