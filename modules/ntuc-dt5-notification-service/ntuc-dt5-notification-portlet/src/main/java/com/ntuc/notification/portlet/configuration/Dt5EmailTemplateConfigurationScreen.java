package com.ntuc.notification.portlet.configuration;

import com.liferay.configuration.admin.display.ConfigurationScreen;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.ntuc.notification.configuration.Dt5EmailTemplateConfiguration;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * System Settings screen for editing DT5 alert email templates (UI only).
 *
 * Rules:
 * - UI wiring only: MUST NOT send email.
 * - MUST NOT contain alert policy logic.
 * - getKey() MUST match the @Meta.OCD id (PID).
 */
@Component(service = ConfigurationScreen.class)
public class Dt5EmailTemplateConfigurationScreen implements ConfigurationScreen {

    private static final String PID =
        "com.ntuc.notification.configuration.Dt5EmailTemplateConfiguration";

    // Must match bnd.bnd Bundle-SymbolicName of this WAB.
    private static final String WAB_SYMBOLIC_NAME = "ntuc.dt5.notification.portlet";

    @Override
    public String getCategoryKey() {
        return Dt5EmailConfigurationCategory.CATEGORY_KEY;
    }

    @Override
    public String getKey() {
        return PID;
    }

    @Override
    public String getName(Locale locale) {
        return "DT5 Email Templates";
    }

    @Override
    public String getScope() {
        return "system";
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Dt5EmailTemplateConfiguration cfg =
                _configurationProvider.getSystemConfiguration(Dt5EmailTemplateConfiguration.class);

            request.setAttribute(Dt5EmailTemplateConfiguration.class.getName(), cfg);

            RequestDispatcher rd =
                _servletContext.getRequestDispatcher("/email_templates/configuration.jsp");

            rd.include(request, response);
        }
        catch (ServletException e) {
            throw new IOException("Unable to render /email_templates/configuration.jsp", e);
        }
        catch (Exception e) {
            throw new IOException("Unable to load system configuration for Dt5EmailTemplateConfiguration", e);
        }
    }

    @Reference
    private ConfigurationProvider _configurationProvider;

    @Reference(target = "(osgi.web.symbolicname=" + WAB_SYMBOLIC_NAME + ")")
    private ServletContext _servletContext;
}
