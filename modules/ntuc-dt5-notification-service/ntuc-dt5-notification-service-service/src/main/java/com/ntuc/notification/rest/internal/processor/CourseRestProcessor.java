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
 * OSGi wiring wrapper only.
 *
 * Business logic must stay in {@link CourseRestProcessorLogic} for plain JUnit
 * testing.
 */
@Component(service = CourseRestProcessor.class)
public class CourseRestProcessor {

    @Reference
    private NtucSBLocalService ntucSBLocalService;

    @Reference
    private CounterLocalService counterLocalService;

    @Reference
    private ClsCourseFieldsProcessor clsCourseFieldsProcessor;

    @Reference
    private PortalExecutorManager portalExecutorManager;

    @Reference
    private AuditEventWriter auditEventWriter;

    @Reference
    private GroupLocalService groupLocalService;

    @Reference
    private UserLocalService userLocalService;

    @Reference
    private OneTimeS3LoadService oneTimeS3LoadService;

    private volatile CourseRestProcessorLogic logic;

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

    public Response postCourse(CourseEventList wrapper) {
        return logic.postCourse(wrapper);
    }

    public Response postOneTimeLoad(CourseRestDtos.OneTimeLoadRequest req) {
        return logic.postOneTimeLoad(req);
    }
}
