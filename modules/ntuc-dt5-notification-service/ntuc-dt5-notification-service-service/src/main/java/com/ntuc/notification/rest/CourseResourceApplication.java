package com.ntuc.notification.rest;

import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

/**
 * JAX-RS application entry point for NTUC Course-related REST endpoints.
 *
 * <p><b>Purpose (Business)</b>:
 * Exposes a stable REST base path for course-related integrations under the
 * NTUC DT5 notification ecosystem. This class acts as the anchor that allows
 * Liferay's JAX-RS Whiteboard to register and publish all REST resources
 * associated with course workflows.</p>
 *
 * <p><b>Purpose (Technical)</b>:
 * - Registers a JAX-RS {@link Application} with the OSGi JAX-RS Whiteboard.
 * - Defines the root application base path for all REST resources in this module.
 * - Enables Liferay authentication verification for incoming REST calls.</p>
 *
 * <p><b>Design Notes</b>:
 * - This class intentionally contains no logic or overridden methods.
 * - Resource classes are discovered and bound by the JAX-RS Whiteboard runtime.
 * - All request handling, validation, auditing, and processing are delegated
 *   to downstream REST resource and service classes.</p>
 *
 * <p><b>Operational Impact</b>:
 * - Changing the application base impacts all externally consumed REST URLs.
 * - Authentication enforcement is centralized via framework configuration,
 *   not per-resource logic.</p>
 *
 * @author @akshaygawande
 */
@Component(
    property = {
        // Root base path for all course-related REST endpoints
        JaxrsWhiteboardConstants.JAX_RS_APPLICATION_BASE + "=/tms/course",

        // Logical name used by the JAX-RS Whiteboard for registration and tracking
        JaxrsWhiteboardConstants.JAX_RS_NAME + "=Course.REST",

        // Enforces Liferay authentication verification for REST access
        "liferay.auth.verifier=true"
    },
    service = Application.class
)
public class CourseResourceApplication extends Application {
    // No overrides required; container-managed JAX-RS bootstrap
}
