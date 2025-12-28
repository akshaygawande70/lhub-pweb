package com.ntuc.notification.rest;

import com.ntuc.notification.model.CourseEventList;
import com.ntuc.notification.rest.internal.dto.CourseRestDtos;
import com.ntuc.notification.rest.internal.processor.CourseRestProcessor;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

/**
 * REST entry point for CLS → DT5 course notifications.
 *
 * Responsibilities:
 * - Routing only
 * - Security enforcement
 * - HTTP response mapping
 *
 * Business logic, validation, auditing, and alerting
 * MUST be handled by CourseRestProcessor / service layer.
 */
@Path("/notify")
//@RequiresScope({"Course.REST.everything", "Course.REST.everything.write"})
@Component(
    property = {
        JaxrsWhiteboardConstants.JAX_RS_RESOURCE + "=true",
        JaxrsWhiteboardConstants.JAX_RS_APPLICATION_SELECT + "=("
            + JaxrsWhiteboardConstants.JAX_RS_NAME + "=Course.REST)"
    },
    service = Object.class
)
public class CourseResource {

    @Reference
    private CourseRestProcessor courseRestProcessor;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postCourse(CourseEventList wrapper) {
        return courseRestProcessor.postCourse(wrapper);
    }

    @POST
    @Path("/oneTimeLoad")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postOneTimeLoad(CourseRestDtos.OneTimeLoadRequest req) {
        return courseRestProcessor.postOneTimeLoad(req);
    }
}
