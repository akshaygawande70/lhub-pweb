package com.ntuc.notification.auditlog.portlet.render;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.ntuc.notification.auditlog.api.AuditLogQueryRequest;
import com.ntuc.notification.auditlog.api.AuditLogQueryResult;
import com.ntuc.notification.auditlog.api.AuditLogQueryService;
import com.ntuc.notification.portlet.constants.NtucDt5NotificationPortletKeys;

import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + NtucDt5NotificationPortletKeys.AUDIT_LOG_BROWSER,
        "mvc.command.name=/"
    },
    service = MVCRenderCommand.class
)
public class AuditLogViewMVCRenderCommand implements MVCRenderCommand {

    @Reference
    private AuditLogQueryService auditLogQueryService;

    @Override
    public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {

        final Map<String, String[]> params = renderRequest.getParameterMap();
        
        // Get the namespace from the response
        String ns = renderResponse.getNamespace();

        // Use namespace-tolerant parameter reading
        String category = AuditLogPortletParamUtil.getString(params, ns, "category");
        String status   = AuditLogPortletParamUtil.getString(params, ns, "status");
        String severity = AuditLogPortletParamUtil.getString(params, ns, "severity");
        String step     = AuditLogPortletParamUtil.getString(params, ns, "step");
        String from     = AuditLogPortletParamUtil.getString(params, ns, "from");
        String to       = AuditLogPortletParamUtil.getString(params, ns, "to");
        String q        = AuditLogPortletParamUtil.getString(params, ns, "q");

        int delta = AuditLogPortletParamUtil.getInt(params, ns, "delta", 20);
        if (delta < 1) delta = 20;
        if (delta > 100) delta = 100;

        int cur = AuditLogPortletParamUtil.getInt(params, ns, "cur", 1);
        if (cur < 1) cur = 1;

        int start = (cur - 1) * delta;
        int end = start + delta;

        AuditLogQueryRequest req = AuditLogQueryRequest.builder()
            .category(category)
            .status(status)
            .severity(severity)
            .step(step)
            .fromYmd(from)
            .toYmd(to)
            .query(q)
            .start(start)
            .end(end)
            .build();

        AuditLogQueryResult result = auditLogQueryService.search(req);

        renderRequest.setAttribute("results", result.getRows());
        renderRequest.setAttribute("total", result.getTotal());
        renderRequest.setAttribute("delta", delta);

        AuditLogFilterOptionsProvider p = new AuditLogFilterOptionsProvider();

        renderRequest.setAttribute("categories", p.categories());
        renderRequest.setAttribute("statuses", p.statuses());
        renderRequest.setAttribute("severities", p.severities());
        renderRequest.setAttribute("steps", p.steps()); // <-- this is the key
        
		/*
		 * renderRequest.setAttribute("categories", result.getCategories());
		 * renderRequest.setAttribute("statuses", result.getStatuses());
		 * renderRequest.setAttribute("severities", result.getSeverities());
		 * renderRequest.setAttribute("steps", result.getSteps());
		 */

        return "/audit_log/view.jsp";
    }
}