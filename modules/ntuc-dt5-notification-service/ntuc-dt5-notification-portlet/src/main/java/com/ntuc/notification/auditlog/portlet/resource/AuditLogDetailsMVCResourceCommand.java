package com.ntuc.notification.auditlog.portlet.resource;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.ntuc.notification.audit.api.util.BlobUtil;
import com.ntuc.notification.model.AuditLog;
import com.ntuc.notification.portlet.constants.NtucDt5NotificationPortletKeys;
import com.ntuc.notification.portlet.portlet.AuditLogBlobExtractor;
import com.ntuc.notification.portlet.portlet.AuditLogDetailsSource;
import com.ntuc.notification.portlet.portlet.AuditLogDetailsViewModel;
import com.ntuc.notification.portlet.portlet.AuditLogDetailsViewModelBuilder;
import com.ntuc.notification.service.AuditLogLocalService;

import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + NtucDt5NotificationPortletKeys.AUDIT_LOG_BROWSER,
        "mvc.command.name=auditLogDetails"
    },
    service = MVCResourceCommand.class
)
public class AuditLogDetailsMVCResourceCommand implements MVCResourceCommand {

    @Reference
    private AuditLogLocalService _auditLogLocalService;

    @Override
    public boolean serveResource(ResourceRequest req, ResourceResponse res) {
        res.setContentType(ContentTypes.APPLICATION_JSON);

        JSONObject json = JSONFactoryUtil.createJSONObject();

        // Namespace tolerant read + backwards compatible alias:
        // - UI currently sends "auditId"
        // - ServiceBuilder PK is "auditLogId"
        String ns = res.getNamespace();
        long id = readLong(req, ns, "auditLogId");
        if (id <= 0) {
            id = readLong(req, ns, "auditId"); // alias support
        }

        try (PrintWriter out = res.getWriter()) {

            AuditLog al = (id > 0) ? _auditLogLocalService.fetchAuditLog(id) : null;

            if (al == null) {
                json.put("ok", false);
                json.put("error", "Not found");
                out.write(json.toString());
                return false;
            }

            AuditLogBlobExtractor blobExtractor = blob -> BlobUtil.toString(blob);

            AuditLogDetailsSource src = new AuditLogDetailsSource() {
                @Override public long getAuditLogId() { return al.getAuditLogId(); }
                @Override public long getNtucDTId() { return al.getNtucDTId(); }
                @Override public String getCourseCode() { return al.getCourseCode(); }
                @Override public String getSeverity() { return al.getSeverity(); }
                @Override public String getStatus() { return al.getStatus(); }
                @Override public String getStep() { return al.getStep(); }
                @Override public String getCategory() { return al.getCategory(); }
                @Override public String getMessage() { return al.getMessage(); }
                @Override public String getErrorCode() { return al.getErrorCode(); }
                @Override public String getErrorMessage() { return al.getErrorMessage(); }
                @Override public String getExceptionClass() { return al.getExceptionClass(); }
                @Override public String getStackTraceHash() { return al.getStackTraceHash(); }
                @Override public java.sql.Blob getDetailsJson() { return al.getDetailsJson(); }
                @Override public java.sql.Blob getStackTraceTruncated() { return al.getStackTraceTruncated(); }
            };

            AuditLogDetailsViewModel vm = AuditLogDetailsViewModelBuilder.build(src, blobExtractor);

            json.put("ok", true);
            json.put("auditLogId", vm.getAuditLogId());
            
            json.put("Ref ID", vm.getNtucDTId());
            json.put("courseCode", vm.getCourseCode());
            json.put("severity", vm.getSeverity());
            json.put("status", vm.getStatus());
            json.put("step", vm.getStep());
            json.put("category", vm.getCategory());
            json.put("message", vm.getMessage());

            json.put("errorCode", vm.getErrorCode());
            json.put("errorMessage", vm.getErrorMessage());
            json.put("exceptionClass", vm.getExceptionClass());
            json.put("stackTraceHash", vm.getStackTraceHash());

            json.put("detailsJson", vm.getDetailsJson());
            json.put("stackTraceTruncated", vm.getStackTraceTruncated());

            out.write(json.toString());
            return false;

        } catch (Exception e) {
            try (PrintWriter out = res.getWriter()) {
                writeError(json, out, "Request failed");
                return false;
            } catch (Exception ignore) {
                return false;
            }
        }
    }

    private static long readLong(ResourceRequest req, String ns, String key) {
        // non-namespaced or namespaced: ns + key
        return ParamUtil.getLong(req, ns + key, ParamUtil.getLong(req, key, 0L));
    }

    private static void writeError(JSONObject json, PrintWriter out, String msg) {
        json.put("ok", false);
        json.put("error", (msg == null) ? "Unknown error" : msg);
        out.write(json.toString());
    }
}
