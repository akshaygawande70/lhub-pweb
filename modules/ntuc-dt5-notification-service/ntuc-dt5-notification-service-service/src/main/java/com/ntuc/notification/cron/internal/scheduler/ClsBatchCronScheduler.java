package com.ntuc.notification.cron.internal.scheduler;

import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.SchedulerEntryImpl;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactory;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.constants.ParameterGroupKeys;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.cron.logic.AbstractClsCronJobRunner;
import com.ntuc.notification.cron.logic.ClsBatchCronJobRunner;
import com.ntuc.notification.service.ClsCourseFieldsProcessor;
import com.ntuc.notification.service.NtucSBLocalService;

import java.time.Clock;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = MessageListener.class)
public class ClsBatchCronScheduler implements MessageListener {

    private static final String JOB_NAME = "cls-batch-cron-job";
    private static final String GROUP_NAME = "cls-batch-cron-group";
    private static final TimeZone TZ_SG = TimeZone.getTimeZone("Asia/Singapore");

    @Reference
    private SchedulerEngineHelper schedulerEngineHelper;

    @Reference
    private TriggerFactory triggerFactory;

    @Reference
    private NtucSBLocalService ntucSBLocalService;

    @Reference
    private ClsCourseFieldsProcessor clsCourseFieldsProcessor;

    @Reference
    private ParameterGroupKeys parameterGroupKeys;

    @Reference
    private AuditEventWriter auditEventWriter;

    private volatile String cronExpression;
    private volatile AbstractClsCronJobRunner runner;

    @Activate
    protected void activate() throws Exception {
        Map<ParameterKeyEnum, Object> paramValues = parameterGroupKeys.getAllParameterValues();
        cronExpression = (String) paramValues.get(ParameterKeyEnum.CLS_FIELD_BATCH_CRON_EXPRESSION);

        Trigger trigger = triggerFactory.createTrigger(
                JOB_NAME,
                GROUP_NAME,
                new Date(),
                null,
                cronExpression,
                TZ_SG
        );

        SchedulerEntryImpl schedulerEntry = new SchedulerEntryImpl(JOB_NAME, trigger);
        schedulerEngineHelper.register(this, schedulerEntry, DestinationNames.SCHEDULER_DISPATCH);

        runner = new ClsBatchCronJobRunner(
                ntucSBLocalService,
                clsCourseFieldsProcessor,
                auditEventWriter,
                () -> UUID.randomUUID().toString(),
                () -> UUID.randomUUID().toString(),
                Clock.systemUTC()
        );

        runner.auditActivated(JOB_NAME, GROUP_NAME, cronExpression);
    }

    @Deactivate
    protected void deactivate() {
        try {
            schedulerEngineHelper.unschedule(JOB_NAME, GROUP_NAME, StorageType.MEMORY_CLUSTERED);
        }
        catch (Exception ignored) {
            // Do not rely on server logs for ops.
        }
    }

    @Override
    public void receive(Message message) {
        AbstractClsCronJobRunner local = runner;
        if (local == null) {
            return;
        }
        local.run(JOB_NAME, GROUP_NAME);
    }
}
