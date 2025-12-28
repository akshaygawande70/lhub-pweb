package com.ntuc.notification.audit.web.filter;

import com.ntuc.notification.audit.api.constants.AuditConstants;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.slf4j.MDC;

/**
 * Correlation + ntucDTId propagation for NotificationDT.REST:
 * - Reads X-Correlation-Id (or generates one)
 * - Reads X-NtucDTId (or falls back to query param "ntucDTId")
 * - Stores into MDC and request properties
 * - Echoes correlation id back in response header
 *
 * Note: For full safety, also add an ExceptionMapper to clear MDC on unhandled exceptions.
 */
@Priority(Priorities.AUTHENTICATION)
@Component(
    service = {ContainerRequestFilter.class, ContainerResponseFilter.class},
    property = {
        JaxrsWhiteboardConstants.JAX_RS_APPLICATION_SELECT + "=("
            + JaxrsWhiteboardConstants.JAX_RS_NAME + "=NotificationDT.REST)",
        JaxrsWhiteboardConstants.JAX_RS_EXTENSION + "=true",
        JaxrsWhiteboardConstants.JAX_RS_NAME + "=CorrelationFilter"
    }
)
public class CorrelationFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String QP_NTUC_DT_ID = "ntucDTId";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String corrId = requestContext.getHeaderString(AuditConstants.HEADER_CORR_ID);

        if (corrId == null || corrId.trim().isEmpty()) {
            corrId = UUID.randomUUID().toString();
        }

        MDC.put(AuditConstants.MDC_CORR_ID, corrId);
        requestContext.setProperty(AuditConstants.REQPROP_CORR_ID, corrId);

        Long ntucDTId = parseLongSafe(requestContext.getHeaderString(AuditConstants.HEADER_NTUC_DT_ID));

        if (ntucDTId == null || ntucDTId <= 0) {
            MultivaluedMap<String, String> qp = requestContext.getUriInfo().getQueryParameters(false);
            ntucDTId = parseLongSafe(qp.getFirst(QP_NTUC_DT_ID));
        }

        if (ntucDTId != null && ntucDTId > 0) {
            MDC.put(AuditConstants.MDC_NTUC_DT_ID, String.valueOf(ntucDTId));
            requestContext.setProperty(AuditConstants.REQPROP_NTUC_DT_ID, ntucDTId);
        } else {
            MDC.remove(AuditConstants.MDC_NTUC_DT_ID);
        }
    }

    @Override
    public void filter(
        ContainerRequestContext requestContext, ContainerResponseContext responseContext)
        throws IOException {

        String corrId = (String) requestContext.getProperty(AuditConstants.REQPROP_CORR_ID);

        if (corrId == null) {
            corrId = MDC.get(AuditConstants.MDC_CORR_ID);
        }

        if (corrId != null) {
            responseContext.getHeaders().putSingle(AuditConstants.HEADER_CORR_ID, corrId);
        }

        MDC.remove(AuditConstants.MDC_CORR_ID);
        MDC.remove(AuditConstants.MDC_NTUC_DT_ID);
    }

    private static Long parseLongSafe(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(s.trim());
        } catch (Exception ignore) {
            return null;
        }
    }
}
