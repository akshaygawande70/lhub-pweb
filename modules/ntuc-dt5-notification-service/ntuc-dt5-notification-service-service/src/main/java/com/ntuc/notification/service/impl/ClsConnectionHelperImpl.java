package com.ntuc.notification.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.CourseEventContext;
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
 * OSGi wiring implementation of {@link ClsConnectionHelper}.
 *
 * <p><b>Business Purpose:</b><br>
 * Acts as the runtime integration bridge between the NTUC notification workflow
 * and the external CLS system, ensuring CLS access is correctly configured
 * using environment parameters.</p>
 *
 * <p><b>Technical Purpose:</b><br>
 * Performs OSGi lifecycle wiring and delegates all CLS operations to
 * {@link ClsConnectionClient}, which contains the testable business logic.</p>
 *
 * <p>Design Rules:</p>
 * <ul>
 *   <li>No CLS business logic is implemented here.</li>
 *   <li>No email alerts are triggered from this layer.</li>
 *   <li>All auditing is delegated to {@link AuditEventWriter} via the client.</li>
 * </ul>
 *
 * @author @akshaygawande
 */
@Component(service = ClsConnectionHelper.class)
public class ClsConnectionHelperImpl implements ClsConnectionHelper {

    private static final Log _log = LogFactoryUtil.getLog(ClsConnectionHelperImpl.class);

    /**
     * Provides CLS configuration values resolved from Parameter Groups.
     * These values are captured once during activation and treated as immutable
     * runtime configuration.
     */
    @Reference
    private ParameterGroupKeys _parameterGroupKeys;

    /**
     * Central audit writer used by the CLS client for all audit persistence.
     */
    @Reference
    private AuditEventWriter _auditEventWriter;

    /**
     * Thread-safe CLS client instance initialized during OSGi activation.
     */
    private volatile ClsConnectionClient _client;

    /**
     * OSGi activation hook.
     *
     * <p><b>Business Purpose:</b><br>
     * Initializes CLS connectivity using the latest configured parameters
     * so that downstream workflows can safely interact with CLS.</p>
     *
     * <p><b>Technical Purpose:</b><br>
     * Loads parameter values, initializes HTTP executor, JSON mapper,
     * and constructs a {@link ClsConnectionClient} instance.</p>
     *
     * <p><b>Inputs / Invariants:</b></p>
     * <ul>
     *   <li>ParameterGroupKeys may be unavailable during startup.</li>
     *   <li>Activation must not fail the component due to config load issues.</li>
     * </ul>
     *
     * <p><b>Side Effects:</b></p>
     * <ul>
     *   <li>Initializes HTTP executor.</li>
     *   <li>Constructs CLS client instance.</li>
     * </ul>
     *
     * <p><b>Audit Behavior:</b><br>
     * No audit events are written at activation time. Audit responsibility
     * begins only when CLS operations are invoked.</p>
     */
    @Activate
    protected void activate() {
        Map<ParameterKeyEnum, Object> paramValues = new HashMap<>();

        try {
            Map<ParameterKeyEnum, Object> loaded =
                _parameterGroupKeys.getAllParameterValues();

            if (loaded != null) {
                paramValues.putAll(loaded);
            }
        }
        catch (Throwable t) {
            // Activation must remain resilient to configuration load issues
            _log.warn(
                "Failed to load ParameterGroupKeys values during activation", t);
        }

        HttpExecutor httpExecutor = new UrlConnectionHttpExecutor();

        _client = new ClsConnectionClient(
            paramValues,
            httpExecutor,
            new ObjectMapper(),
            _auditEventWriter
        );

        _log.info(
            "ClsConnectionHelperImpl activated (ClsConnectionClient initialized).");
    }

    /**
     * Fetches course details from CLS.
     *
     * <p><b>Business Purpose:</b><br>
     * Retrieves authoritative course metadata required for notification
     * and publishing workflows.</p>
     *
     * <p><b>Technical Purpose:</b><br>
     * Delegates CLS course fetch to {@link ClsConnectionClient}.</p>
     *
     * @param eventCtx workflow context containing course and audit metadata
     * @param returnedProperties optional CLS fields to limit payload size
     *
     * @return populated {@link CourseResponse}
     *
     * @throws IOException if CLS communication fails
     *
     * <p><b>Side Effects:</b></p>
     * <ul>
     *   <li>Network I/O to CLS.</li>
     *   <li>Audit events written via client.</li>
     * </ul>
     */
    @Override
    public CourseResponse getCourseDetails(
        CourseEventContext eventCtx,
        String[] returnedProperties)
        throws IOException {

        return _client.getCourseDetails(eventCtx, returnedProperties);
    }

    /**
     * Retrieves the most recent course schedules from CLS.
     *
     * <p><b>Business Purpose:</b><br>
     * Ensures notifications and course pages reflect the latest
     * published schedules.</p>
     *
     * <p><b>Technical Purpose:</b><br>
     * Delegates schedule retrieval to {@link ClsConnectionClient}.</p>
     *
     * @param courseCode unique CLS course identifier
     *
     * @return {@link ScheduleResponse} containing schedule information
     *
     * @throws IOException if CLS communication fails
     */
    @Override
    public ScheduleResponse getLatestCourseSchedules(String courseCode)
        throws IOException {

        return _client.getLatestCourseSchedules(courseCode);
    }

    /**
     * Retrieves raw dummy course JSON for diagnostics and development testing.
     *
     * <p><b>Business Purpose:</b><br>
     * Supports troubleshooting and validation without invoking live CLS APIs.</p>
     *
     * <p><b>Technical Purpose:</b><br>
     * Delegates dummy payload retrieval to the CLS client.</p>
     *
     * @param courseCode course identifier
     *
     * @return raw JSON string
     *
     * @throws IOException if retrieval fails
     */
    @Override
    public String getCoursesDummyRawJson(String courseCode)
        throws IOException {

        return _client.getCoursesDummyRawJson(courseCode);
    }

    /**
     * Retrieves raw dummy subscription JSON for diagnostics and testing.
     *
     * <p><b>Business Purpose:</b><br>
     * Allows validation of downstream subscription processing
     * without live CLS dependency.</p>
     *
     * <p><b>Technical Purpose:</b><br>
     * Delegates dummy subscription retrieval to the CLS client.</p>
     *
     * @param courseCode course identifier
     *
     * @return raw JSON string
     *
     * @throws IOException if retrieval fails
     */
    @Override
    public String getSubscriptionsDummyRawJson(String courseCode)
        throws IOException {

        return _client.getSubscriptionsDummyRawJson(courseCode);
    }
}
