package com.ntuc.notification.rest;

import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

@Component(
		property = { 
			JaxrsWhiteboardConstants.JAX_RS_APPLICATION_BASE + "=/tms/course",
			JaxrsWhiteboardConstants.JAX_RS_NAME + "=Course.REST" , 
			"liferay.auth.verifier=false"
		}, 
		service = Application.class
)
public class CourseResourceApplication extends Application {
}
