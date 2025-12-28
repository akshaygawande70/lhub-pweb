package com.ntuc.notification.auditlog.portlet.resource;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.WebKeys;
import com.ntuc.notification.audit.api.dto.AuditLogSearchRequest;
import com.ntuc.notification.audit.api.dto.AuditLogSearchResult;
import com.ntuc.notification.audit.api.spi.AuditLogSearchGateway;

import com.ntuc.notification.auditlog.portlet.render.AuditLogDataTablesJsonWriter;
import com.ntuc.notification.auditlog.portlet.render.DataTablesAuditLogParamParser;
import com.ntuc.notification.portlet.constants.NtucDt5NotificationPortletKeys;

import java.io.PrintWriter;
import java.util.Map;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * DataTables server-side endpoint for AuditLog browsing.
 *
 * Contract:
 * - Always returns DataTables JSON (never blank, never HTML).
 * - JSON keys: draw, recordsTotal, recordsFiltered, data (+ optional error).
 *
 * Notes:
 * - This is a UI boundary (PORTLET). Business logic belongs in AuditLogSearchGateway/service.
 * - Writer must be obtained once; do not close/re-open (keeps contract reliable).
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + NtucDt5NotificationPortletKeys.AUDIT_LOG_BROWSER,
        "mvc.command.name=/auditlog/datatables"
    },
    service = MVCResourceCommand.class
)
public class AuditLogDataTablesMVCResourceCommand implements MVCResourceCommand {

    @Reference
    private AuditLogSearchGateway auditLogSearchGateway;

    @Override
    public boolean serveResource(
        ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

        resourceResponse.setContentType(ContentTypes.APPLICATION_JSON);
        resourceResponse.setCharacterEncoding("UTF-8");

        final String ns = resourceResponse.getNamespace();

        int draw = resolveDraw(resourceRequest, ns);

        PrintWriter pw = null;

        try {
            pw = resourceResponse.getWriter();

            ThemeDisplay td =
                (ThemeDisplay)resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

            long companyId = (td == null) ? 0L : td.getCompanyId();
            long groupId = (td == null) ? 0L : td.getScopeGroupId();

            Map<String, String[]> params = resourceRequest.getParameterMap();

            AuditLogSearchRequest req =
                DataTablesAuditLogParamParser.parse(params, companyId, groupId);

            // Keep draw consistent (DataTables expects the same draw echoed back).
            if (req != null && req.getDraw() > 0) {
                draw = req.getDraw();
            }

            AuditLogSearchResult result = auditLogSearchGateway.search(req);

            pw.write(AuditLogDataTablesJsonWriter.toJson(result));
            pw.flush();

        }
        catch (Exception e) {
            // Never blank. Never HTML.
            if (pw != null) {
                pw.write(
                    AuditLogDataTablesJsonWriter.toErrorJson(
                        draw,
                        "Audit log search failed: " + e.getClass().getSimpleName()
                    )
                );
                pw.flush();
            }
        }

        // Return false to avoid lifecycle oddities (consistent with your working commands).
        return false;
    }

    private static int resolveDraw(ResourceRequest req, String ns) {
        int draw = safeInt(req.getParameter("draw"));
        if (draw > 0) {
            return draw;
        }
        return safeInt(req.getParameter(ns + "draw"));
    }

    private static int safeInt(String s) {
        try {
            return (s == null) ? 0 : Integer.parseInt(s);
        }
        catch (Exception e) {
            return 0;
        }
    }
}
