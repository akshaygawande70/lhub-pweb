package com.ntuc.notification.service;

import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.model.CourseResponse;
import com.ntuc.notification.model.ScheduleResponse;
import java.io.IOException;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ClsConnectionHelper {

    /**
     * Retrieves course details from CLS using course code.
     * Allows to specify the fields to be returned.
     *
     * @param eventCtx event/audit context (courseCode is expected inside)
     * @param returnedProperties requested properties (can be overridden based on event type)
     * @return CourseResponse or null when call fails
     * @throws IOException If there is an error making the request
     */
    CourseResponse getCourseDetails(CourseEventContext eventCtx, String[] returnedProperties) throws IOException;

    /**
     * Retrieves course schedule details from CLS using course code.
     *
     * @param courseCode CLS course code
     * @return ScheduleResponse or null when call fails
     * @throws IOException If there is an error making the request
     */
    ScheduleResponse getLatestCourseSchedules(String courseCode) throws IOException;

    /**
     * Dummy API - raw JSON pass-through.
     *
     * IMPORTANT:
     * - Do NOT parse into DTO and re-serialize.
     * - Return exactly what CLS returns (body).
     */
    String getCoursesDummyRawJson(String courseCode) throws IOException;

    /**
     * Dummy API - raw JSON pass-through.
     *
     * IMPORTANT:
     * - Do NOT parse into DTO and re-serialize.
     * - Return exactly what CLS returns (body).
     */
    String getSubscriptionsDummyRawJson(String courseCode) throws IOException;

}
