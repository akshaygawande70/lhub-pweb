package com.ntuc.notification.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.constants.ParameterGroupKeys;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.model.CourseResponse;
import com.ntuc.notification.model.ScheduleResponse;
import com.ntuc.notification.service.ClsConnectionHelper;
import com.ntuc.notification.service.internal.cls.ClsConnectionClient;
import com.ntuc.notification.service.internal.http.HttpExecutor;
import com.ntuc.notification.service.internal.http.UrlConnectionHttpExecutor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * OSGi wiring layer only.
 *
 * Core CLS integration logic is extracted into {@link ClsConnectionClient} for plain JUnit testing.
 *
 * Rule:
 * - No direct emails from here.
 * - Client writes audit events via AuditEventWriter.
 */
@Component(service = ClsConnectionHelper.class)
public class ClsConnectionHelperImpl implements ClsConnectionHelper {

    private static final Log _log = LogFactoryUtil.getLog(ClsConnectionHelperImpl.class);

    @Reference
    private ParameterGroupKeys _parameterGroupKeys;

    @Reference
    private AuditEventWriter _auditEventWriter;

    private volatile ClsConnectionClient _client;

    @Activate
    protected void activate() {
        Map<ParameterKeyEnum, Object> paramValues = new HashMap<ParameterKeyEnum, Object>();

        try {
            Map<ParameterKeyEnum, Object> loaded = _parameterGroupKeys.getAllParameterValues();
            if (loaded != null) {
                paramValues.putAll(loaded);
            }
        }
        catch (Throwable t) {
            _log.warn("Failed to load ParameterGroupKeys values during activation", t);
        }

        HttpExecutor httpExecutor = new UrlConnectionHttpExecutor();

        _client = new ClsConnectionClient(
            paramValues,
            httpExecutor,
            new ObjectMapper(),
            _auditEventWriter
        );

        _log.info("ClsConnectionHelperImpl activated (ClsConnectionClient initialized).");
    }

    @Override
    public CourseResponse getCourseDetails(
        com.ntuc.notification.audit.api.CourseEventContext eventCtx,
        String[] returnedProperties) throws IOException {

        return _client.getCourseDetails(eventCtx, returnedProperties);
    }

    @Override
    public ScheduleResponse getLatestCourseSchedules(String courseCode) throws IOException {
        return _client.getLatestCourseSchedules(courseCode);
    }

    @Override
    public String getCoursesDummyRawJson(String courseCode) throws IOException {
        return _client.getCoursesDummyRawJson(courseCode);
    }

    @Override
    public String getSubscriptionsDummyRawJson(String courseCode) throws IOException {
        return _client.getSubscriptionsDummyRawJson(courseCode);
    }
}
