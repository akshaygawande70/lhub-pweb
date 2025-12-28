package com.ntuc.notification.cron.logic;

import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.constants.NotificationType;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.service.ClsCourseFieldsProcessor;
import com.ntuc.notification.service.NtucSBLocalService;

import java.time.Clock;
import java.util.List;

public class ClsBatchCronJobRunner extends AbstractClsCronJobRunner {

    public ClsBatchCronJobRunner(
            NtucSBLocalService ntucSBLocalService,
            ClsCourseFieldsProcessor clsCourseFieldsProcessor,
            AuditEventWriter auditEventWriter,
            CorrelationIdSupplier correlationIdSupplier,
            JobRunIdSupplier jobRunIdSupplier,
            Clock clock) {

        super(
                ntucSBLocalService,
                clsCourseFieldsProcessor,
                auditEventWriter,
                correlationIdSupplier,
                jobRunIdSupplier,
                clock
        );
    }

    @Override
    protected List<NtucSB> fetchRecordsToProcess(NtucSBLocalService ntucSBLocalService) {
        return ntucSBLocalService.getUnprocessedCronRecords(NotificationType.CHANGED);
    }

    @Override
    protected String getSelectorDescription() {
        return "getUnprocessedCronRecords(type=CHANGED)";
    }

    @Override
    protected void markRecordSuccess(NtucSBLocalService ntucSBLocalService, NtucSB record) {
        record.setIsCronProcessed(true);
        ntucSBLocalService.updateNtucSB(record);
    }
}
