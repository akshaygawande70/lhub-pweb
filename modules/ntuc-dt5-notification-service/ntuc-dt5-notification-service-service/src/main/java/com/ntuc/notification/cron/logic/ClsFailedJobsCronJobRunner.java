package com.ntuc.notification.cron.logic;

import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.service.ClsCourseFieldsProcessor;
import com.ntuc.notification.service.NtucSBLocalService;

import java.time.Clock;
import java.util.List;

public class ClsFailedJobsCronJobRunner extends AbstractClsCronJobRunner {

    public ClsFailedJobsCronJobRunner(
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
        return ntucSBLocalService.fetchByIsRowLockFailed(true);
    }

    @Override
    protected String getSelectorDescription() {
        return "fetchByIsRowLockFailed(true)";
    }

    @Override
    protected void markRecordSuccess(NtucSBLocalService ntucSBLocalService, NtucSB record) {
        record.setIsRowLockFailed(false);
        ntucSBLocalService.updateNtucSB(record);
    }
}
