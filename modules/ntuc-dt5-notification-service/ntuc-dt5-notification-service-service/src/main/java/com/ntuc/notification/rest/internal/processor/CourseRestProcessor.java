package com.ntuc.notification.rest.internal.processor;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.petra.executor.PortalExecutorManager;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.model.CourseEventList;
import com.ntuc.notification.onetime.OneTimeS3LoadService;
import com.ntuc.notification.onetime.api.OneTimeLoadFacade;
import com.ntuc.notification.onetime.internal.OneTimeLoadFacadeImpl;
import com.ntuc.notification.rest.internal.dto.CourseRestDtos;
import com.ntuc.notification.rest.internal.processor.context.LiferayRequestContextProvider;
import com.ntuc.notification.rest.internal.processor.context.NotificationExecutorProvider;
import com.ntuc.notification.service.ClsCourseFieldsProcessor;
import com.ntuc.notification.service.NtucSBLocalService;

import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * OSGi-facing processor facade for the course REST entry points.
 *
 * <p><b>Purpose (Business)</b>:
 * Provides a stable processing boundary for inbound course notifications and one-time
 * load requests, while keeping the actual workflow logic isolated in a unit-testable
 * class. This separation reduces operational risk by ensuring the HTTP entry path
 * stays thin and the workflow remains verifiable via automated tests.</p>
 *
 * <p><b>Purpose (Technical)</b>:
 * - Acts as an OSGi wiring wrapper that assembles required Liferay and NTUC services.
 * - Constructs the request context provider and async executor provider.
 * - Wraps one-time load implementation behind a small facade.
 * - Delegates all real processing to {@link CourseRestProcessorLogic}.</p>
 *
 * <p><b>Audit Notes</b>:
 * This class does not write audit events itself. Audit is performed by the logic
 * and downstream service layers using {@link AuditEventWriter}, where persisted
 * audit records remain the single source of truth for workflow reconstruction.</p>
 *
 * <p><b>Lifecycle</b>:
 * The {@link #activate()} method builds the runtime graph once the OSGi references
 * are satisfied. The created {@link CourseRestProcessorLogic} instance is stored
 * in a {@code volatile} field to ensure safe publication across threads.</p>
 *
 * @author @akshaygawande
 */
@Component(service = CourseRestProcessor.class)
public class CourseRestProcessor {

    /**
     * ServiceBuilder local service used by the processing logic for NtucSB access.
     */
    @Reference
    private NtucSBLocalService ntucSBLocalService;

    /**
     * Used by the processing logic for generating identifiers where required.
     */
    @Reference
    private CounterLocalService counterLocalService;

    /**
     * Course field mapping/processing service used by downstream workflows.
     */
    @Reference
    private ClsCourseFieldsProcessor clsCourseFieldsProcessor;

    /**
     * Provides access to Liferay-managed executors used for async processing paths.
     */
    @Reference
    private PortalExecutorManager portalExecutorManager;

    /**
     * Audit writer used by the logic/service layers to persist audit events.
     *
     * <p>Persisted audit data is the authoritative timeline; server logs are not relied on
     * for reconstructing workflow outcomes.</p>
     */
    @Reference
    private AuditEventWriter auditEventWriter;

    /**
     * Provides group resolution required for request context building.
     */
    @Reference
    private GroupLocalService groupLocalService;

    /**
     * Provides user resolution required for request context building.
     */
    @Reference
    private UserLocalService userLocalService;

    /**
     * One-time S3 loader integration used by the one-time load API.
     */
    @Reference
    private OneTimeS3LoadService oneTimeS3LoadService;

    /**
     * Fully-initialized, unit-testable processor logic instance.
     *
     * <p>{@code volatile} ensures safe publication after activation so request threads
     * observe a fully constructed instance.</p>
     */
    private volatile CourseRestProcessorLogic logic;

    /**
     * Constructs runtime collaborators and assembles the unit-testable logic object.
     *
     * <p>This method is invoked by OSGi once component references are available.</p>
     */
    @Activate
    protected void activate() {
        LiferayRequestContextProvider requestContextProvider =
                new LiferayRequestContextProvider(groupLocalService, userLocalService);

        NotificationExecutorProvider executorProvider =
                new NotificationExecutorProvider(portalExecutorManager);

        OneTimeLoadFacade oneTimeLoadFacade =
                new OneTimeLoadFacadeImpl(oneTimeS3LoadService);

        logic = new CourseRestProcessorLogic(
                ntucSBLocalService,
                counterLocalService,
                clsCourseFieldsProcessor,
                auditEventWriter,
                oneTimeLoadFacade,
                requestContextProvider,
                executorProvider
        );
    }

    /**
     * Delegates standard CLS course notification processing.
     *
     * @param wrapper incoming request payload wrapper
     * @return HTTP response produced by the logic layer
     */
    public Response postCourse(CourseEventList wrapper) {
        return logic.postCourse(wrapper);
    }

    /**
     * Delegates one-time load processing.
     *
     * @param req request describing the one-time load parameters
     * @return HTTP response produced by the logic layer
     */
    public Response postOneTimeLoad(CourseRestDtos.OneTimeLoadRequest req) {
        return logic.postOneTimeLoad(req);
    }
}
